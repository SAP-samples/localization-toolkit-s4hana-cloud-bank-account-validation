package com.sap.poland.whitelist.service;

import java.io.InputStream;
import java.util.List;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLUtilsTest {
    private static final String TEST_FILE_1 = "XMLUTilsTest1.xml";
    private static final String TEST_FILE_2 = "XMLUTilsTest2.xml";
    
    public XMLUtilsTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
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
    
    @Test(expected = NullPointerException.class)
    public void testGetChildElementsNull1() throws Exception {
        InputStream stream = getClass().getResourceAsStream(TEST_FILE_1);
        Element root = XMLUtils.getRootElement(stream);
        XMLUtils.getChildElements(root, null);
        fail();
    }
    
    @Test(expected = NullPointerException.class)
    public void testGetChildElementsNull2() throws Exception {
        XMLUtils.getChildElements(null, "element");
        fail();
    }
            
    @Test
    public void testGetChildElements() throws Exception {
        InputStream stream = getClass().getResourceAsStream(TEST_FILE_1);
        Element root = XMLUtils.getRootElement(stream);
        List<Element> result = XMLUtils.getChildElements(root, "subElem");
        assertEquals(3, result.size());
    }
    
    @Test
    public void testNewDocument() {
        XMLUtils.newDocument();
    }
    
    @Test(expected = NullPointerException.class)
    public void testLoadDocumentFromString() {
        XMLUtils.createDocumentFromString(null);
        fail();
    }
    
    @Test
    public void testLoadDocumentFromString2() {
        String xml = "<xml></xml>";
        Document document = XMLUtils.createDocumentFromString(xml);
        assertNotNull(document);
        assertEquals("xml", document.getDocumentElement().getNodeName());
    }
    
    @Test(expected = NullPointerException.class)
    public void testGetFirstChildNull1() {
        XMLUtils.getFirstChild(null, "");
        fail();
    }
    
    @Test(expected = NullPointerException.class)
    public void testGetFirstChildNull2() {
        XMLUtils.getFirstChild(EasyMock.createNiceMock(Element.class), null);
        fail();
    }
    
    
    @Test
    public void testGetFirstChild() throws Exception {
        InputStream stream = getClass().getResourceAsStream(TEST_FILE_2);
        Element root = XMLUtils.getRootElement(stream);
        Element child2 = XMLUtils.getFirstChild(root, "Child2");
        Element child6 = XMLUtils.getFirstChild(root, "Child6");
        assertNotNull(child2);
        assertNull(child6);
    }
    
   
    @Test(expected = NullPointerException.class)
    public void testgetValueGetFirstChildNull1() {
        XMLUtils.getValueOfFirstChild(null, "");
        fail();
    }
    
    @Test(expected = NullPointerException.class)
    public void testgetValueGetFirstChildNull2() {
        XMLUtils.getValueOfFirstChild(EasyMock.createNiceMock(Element.class), null);
        fail();
    }
    
    @Test
    public void testgetValueGetFirstChild() throws Exception {
        InputStream stream = getClass().getResourceAsStream(TEST_FILE_2);
        Element root = XMLUtils.getRootElement(stream);
        String child2 = XMLUtils.getValueOfFirstChild(root, "Child2");
        String child6 = XMLUtils.getValueOfFirstChild(root, "Child6");
        assertEquals("valChild2", child2);
        assertNull(child6);
    }
    
    @Test
    public void testCreateAppendChildElementNull() {
        try {
            XMLUtils.createAppendChildElement(null, "", "");
            fail();
        } catch (NullPointerException nullPointerException) {
        }
        try {
            XMLUtils.createAppendChildElement(EasyMock.createNiceMock(Element.class), null, "");
            fail();
        } catch (NullPointerException nullPointerException) {
        }
    }
    
    @Test
    public void createAppendChildElement() throws Exception {
        InputStream stream = getClass().getResourceAsStream(TEST_FILE_2);
        Element root = XMLUtils.getRootElement(stream);
        final String value = "myValue";
        final String childName = "myValue";
        Element child = XMLUtils.createAppendChildElement(root, childName, value);
        String act = XMLUtils.getValueOfFirstChild(root, childName);
        assertEquals(value, act);
    }
}
