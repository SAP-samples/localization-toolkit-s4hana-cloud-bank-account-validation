package com.sap.poland.whitelist.service;

interface Validator {
    /**
     * Validate given paramters. 
     * @param taxNumber Polish Domestic Tax number (NIP)
     * @param bankAccount Bank account in the NRB (BAN) format
     * @return True if the tax number and bank account are whitelisted. False otherwise. 
     * @throws NullPointerException when any of the parameters is null. 
     */
    boolean validate(String taxNumber, String bankAccount);
}
