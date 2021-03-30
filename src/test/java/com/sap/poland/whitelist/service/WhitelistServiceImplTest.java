package com.sap.poland.whitelist.service;

import com.sap.poland.whitelist.controllers.NotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.ElementNameAndAttributeQualifier;
import org.custommonkey.xmlunit.ElementNameAndTextQualifier;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class WhitelistServiceImplTest {
    private static final String TEST_FILE_1 = "Request1.xml";
    private static final String RESULT_FILE_1 = "Response1.xml";
    
    
    public WhitelistServiceImplTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        XMLUnit.setIgnoreAttributeOrder(true);
        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setIgnoreComments(true);
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of downloadWhitelist method, of class WhitelistServiceImpl.
     */
    @Test
    public void testDownloadWhitelist1() throws Exception{
        // no action when the whitelist has been downloaded today
        WhitelistProcessor processor = EasyMock.createStrictMock(WhitelistProcessor.class);
        EasyMock.expect(processor.downloadAndProcess(EasyMock.anyString())).andReturn(createDummyWhitelist()).times(1);
        EasyMock.replay(processor);
        final WhitelistServiceImpl instance = new WhitelistServiceImpl(processor);
        instance.downloadWhitelist();
        instance.downloadWhitelist();
        instance.downloadWhitelist();
        EasyMock.verify(processor);
    }

   
    /**
     * Test of performValidation method, of class WhitelistServiceImpl.
     */
    @Test
    public void testPerformValidation() throws Exception {
        Validator validator = EasyMock.createNiceMock(Validator.class);
        EasyMock.expect(validator.validate(EasyMock.eq("1"), EasyMock.anyString())).andReturn(Validator.RESULT_NOT_FOUND);
        EasyMock.expect(validator.validate(EasyMock.eq("2"), EasyMock.anyString())).andReturn(Validator.RESULT_ACTIVE);
        EasyMock.replay(validator);
        
        InputStream stream = getClass().getResourceAsStream(TEST_FILE_1);
        Document doc = XMLUtils.documentFromStream(stream);
        String requests = XMLUtils.documentToString(doc);
       
        WhitelistProcessor processor = EasyMock.createNiceMock(WhitelistProcessor.class);
        Whitelist whiteList = EasyMock.createNiceMock(Whitelist.class);
        EasyMock.expect(whiteList.getDate()).andReturn("20201010").anyTimes();
        EasyMock.expect(whiteList.getCheckSum()).andReturn("ABCD").anyTimes();
        EasyMock.expect(whiteList.getCheckSumMethod()).andReturn("SHA512").anyTimes();
        whiteList.setCheckSum("abcdef", "SHA512");
        EasyMock.expect(processor.downloadAndProcess(EasyMock.anyString())).andReturn(whiteList).anyTimes();
        EasyMock.replay(whiteList, processor);
        
        WhitelistServiceImpl instance = new WhitelistServiceImpl(processor);
        instance.downloadWhitelist();
        
        String result = instance.performValidation(validator, requests);
        stream = getClass().getResourceAsStream(RESULT_FILE_1);
        Diff diff = XMLUnit.compareXML(XMLUtils.documentFromStream(stream), XMLUtils.createDocumentFromString(result));
        assertTrue(diff.similar());
    }
    
    /**
     * Test of appendResult method, of class WhitelistServiceImpl.
     */
    @Test
    public void testAppendResult() throws Exception {
       //void appendResult(Element responseRoot, String bankAccount, String taxNo, boolean isValid)
       Document d = XMLUtils.newDocument();
       Element root = d.createElement("ValResponses");
       d.appendChild(root);
       
       WhitelistProcessor processor = EasyMock.createNiceMock(WhitelistProcessor.class);
       Whitelist whiteList = EasyMock.createNiceMock(Whitelist.class);
       EasyMock.expect(whiteList.getDate()).andReturn("20201010").anyTimes();
       EasyMock.expect(whiteList.getCheckSum()).andReturn("ABCD").anyTimes();
       EasyMock.expect(whiteList.getCheckSumMethod()).andReturn("SHA512").anyTimes();
       whiteList.setCheckSum("abcdef", "SHA512");
       EasyMock.expect(processor.downloadAndProcess(EasyMock.anyString())).andReturn(whiteList).anyTimes();
       EasyMock.replay(whiteList, processor);
       
       WhitelistServiceImpl instance = new WhitelistServiceImpl(processor);
       instance.downloadWhitelist();
       
       instance.appendResult(root, "88103015080000000500754001", "2", Validator.RESULT_ACTIVE);
       instance.appendResult(root, "88103015080000000500754001", "1", Validator.RESULT_NOT_FOUND);
       
       InputStream stream = getClass().getResourceAsStream(RESULT_FILE_1);
       Document expDoc = XMLUtils.documentFromStream(stream);
       Diff diff = XMLUnit.compareXML(expDoc, d);
       diff.overrideElementQualifier(new ElementNameAndTextQualifier());
       XMLAssert.assertXMLEqual(diff.toString(), diff, true);
    }

    /**
     * Test of validate method, of class WhitelistServiceImpl.
     */
    @Test(expected = NotFoundException.class)
    public void testValidateNull1() {
        // the whitelist instance will be empty but not null. 
        WhitelistProcessor processor = EasyMock.createNiceMock(WhitelistProcessor.class);
        EasyMock.expect(processor.downloadAndProcess(EasyMock.anyString())).andReturn(new Whitelist());
        EasyMock.replay(processor);
        WhitelistService instance = new WhitelistServiceImpl(processor);
        instance.downloadWhitelist();
        instance.validate("");
        fail();
    }
    
    @Test(expected = NotFoundException.class)
    public void testValidateNull2() {
       new WhitelistServiceImpl(new WhitelistProcessor()).validate("");
       fail();
    }
    
    private static Whitelist createDummyWhitelist() {
        Whitelist list = new Whitelist();
        list.header = new HashMap<>();
        list.header.put(Whitelist.GEN_DATE_NAME, KeyDate.now().yyyyMMdd());
        list.activeTaxpayers = new String[0];
        list.excemptedTaxpayers = list.activeTaxpayers;
        list.masks = list.activeTaxpayers;
        return list;
    }
    
    @Test
    public void testValidate() throws IOException {
        WhitelistProcessor processor = EasyMock.createNiceMock(WhitelistProcessor.class);
        Whitelist list = createDummyWhitelist();
        
        EasyMock.expect(processor.downloadAndProcess(EasyMock.anyString())).andReturn(list);
        EasyMock.replay(processor);
        WhitelistService instance = new WhitelistServiceImpl(processor);
        instance.downloadWhitelist();
        
        InputStream stream = getClass().getResourceAsStream(TEST_FILE_1);
        Document doc = XMLUtils.documentFromStream(stream);
        String requests = XMLUtils.documentToString(doc);
        String result = instance.validate(requests);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

}
