package com.sap.poland.whitelist.service;

interface Validator {
    /** Validation result - Not Whitelisted **/
    static final int RESULT_NOT_FOUND = 0;
    /** Validation result - Whitelisted in the Active taxpayers section **/
    static final int RESULT_ACTIVE = 1;
    /** Validation result - Whitelisted in the Exempted taxpayers section **/
    static final int RESULT_EXEMPTED = 2;
    /** Validation result - Whitelisted, the Bank account is Virtual **/
    static final int RESULT_VIRTUAL = 3;
    
    /**
     * Validate given parameters. 
     * @param taxNumber Polish Domestic Tax number (NIP)
     * @param bankAccount Bank account in the NRB (BAN) format
     * @return Validation result. 
     * @throws NullPointerException when any of the parameters is null. 
     */
    int validate(String taxNumber, String bankAccount);
}
