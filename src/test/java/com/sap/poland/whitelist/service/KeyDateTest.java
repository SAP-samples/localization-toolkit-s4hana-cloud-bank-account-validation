package com.sap.poland.whitelist.service;

import java.time.LocalDateTime;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


public class KeyDateTest {
    
    public KeyDateTest() {
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


    /**
     * Test of yyyyMMdd method, of class KeyDate.
     */
    @Test
    public void testYyyyMMdd() {
        KeyDate instance = new KeyDate(LocalDateTime.of(2020, 1, 2, 1, 1));
        String act = instance.yyyyMMdd();
        assertEquals("20200102", act);
    }
    
}
