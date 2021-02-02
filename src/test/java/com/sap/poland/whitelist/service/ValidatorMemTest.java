package com.sap.poland.whitelist.service;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValidatorMemTest {
    
    public ValidatorMemTest() {
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

    
    @Test
    public void testGetHash() {
        String taxNumber = "9510061264";
        String bankAccount = "88103015080000000500754001";
        String date = "20200310";
        int hashCycles = 5000;
        String expResult = "56EAA5BFB85222796C90C5BEEDFA4FAA42CD3FCB58B95461AFFAE9CD8E424B470F5A40992512E6ED7CF711FC23C382079024E74ACEBB875F4C1D0E691F2DE95D".toLowerCase();
        String result = ValidatorMem.getHash(taxNumber, bankAccount, date, hashCycles);
        assertEquals(expResult, result);
    }
    
    @Test
    public void getGetHash2(){
        String taxNumber = "6770066541";
        String bankAccount = "70175013125650000010122510";
        String mask = "XX17501312YYYXXXXXXXXXXXXX";
        String date = "20191213";
        int hashCycles = 5000;
        String expResult = "252A2D3166C59ADAD1C461343644B9658C13359EA16A435BCCFB7C43E486BA237FC4BD81AF357602A7CE5860A0457252D92D29D93B690B48DEA459BCB3B3E3AB".toLowerCase();
        String masked = ValidatorMem.mask(mask, bankAccount);
        String result = ValidatorMem.getHash(taxNumber, masked, date, hashCycles);
        assertEquals(expResult, result); 
    }
}
