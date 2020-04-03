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
    
}
