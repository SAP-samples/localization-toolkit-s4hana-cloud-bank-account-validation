package com.sap.poland.whitelist.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import org.apache.commons.codec.binary.Hex;

class ValidatorMem implements Validator {
    
    private static final char MASK_PLACEHOLDER_TO_REPLACE = 'Y';
    private final Whitelist _whitelist;
    

    ValidatorMem(Whitelist whitelist) {
        _whitelist = whitelist;
    }
    
    @Override
    public int validate(String taxNumber, String bankAccount) {
        String date = _whitelist.header.get(Whitelist.GEN_DATE_NAME);
        Meter hashMeter = new Meter();
        Meter dbMeter = new Meter();
        String hash;
        
        int hashCycles = _whitelist.getNumberOfTranformations();
        
        dbMeter.start();
        hash = getHash(taxNumber, bankAccount, date, hashCycles);
        int result = isWhitelisted(hash);
        if(result != RESULT_NOT_FOUND) {
            dbMeter.stop();
            java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, System.currentTimeMillis() + ", found unmasked, db: " + dbMeter.report());
            return result;
        } else {
            String msg = 
                    "TaxNo: " + taxNumber + 
                    ", BA: " + bankAccount + 
                    ", date: " + date +
                    ", cycles: " + hashCycles +
                    ", Unmasked hash: " + hash;
            java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, msg);
        }
        dbMeter.stop();
        
        
        
        java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, System.currentTimeMillis() + ", Masking ");
        for(String mask : _whitelist.masks) {
            hashMeter.start();
            String maskedAccount = mask(mask, bankAccount);
            hashMeter.stop();
            if(maskedAccount != null) {
                dbMeter.start();
                hash = getHash(taxNumber, maskedAccount, date, hashCycles);
                result = isWhitelisted(hash);
                if(result != RESULT_NOT_FOUND ) {
                    dbMeter.stop();
                    java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, System.currentTimeMillis() + ", found masked, db: " + dbMeter.report());
                    return RESULT_VIRTUAL; 
                } else {
                    String msg = 
                    " Not found, TaxNo: " + taxNumber + 
                    ", BA: " + bankAccount + 
                    ", date: " + date +
                    ", mask: " + mask +
                    ", masked: " + maskedAccount +
                    ", Masked hash: " + hash;
                    java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, msg);
                }
                dbMeter.stop();
            }
        }
        
        java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, System.currentTimeMillis() + ", Not found, db: " + dbMeter.report());
        return RESULT_NOT_FOUND; //Not Valid;
    }
    
    static class Meter {
        private long _start;
        private long _time;
        private long _runs;
        void start() {
            _start = System.currentTimeMillis();
        }
        void stop() {
            _time += (System.currentTimeMillis() - _start);
        }
        String avg() {
            if(_runs != 0) {
                float avg = ((float) _time) / ((float)_runs);
                return String.valueOf(avg) + " ms";
            }
            return "n/a";
        }
        
        String total() {
            return String.valueOf(_time) +" ms";
        }
        String report() {
            return "Total: " + total() + ", avg: " + avg();
        }
    }
    
    static String mask(String mask, String bankAccount) {
        String maskRegEx = mask.replaceAll("X", ".").replaceAll("Y", ".");
        
        if(!bankAccount.matches(maskRegEx)) {
            return null;
        }
        
        char [] chars = mask.toCharArray();
        for(int i = 0; i < chars.length; i++){
            if(chars[i] == MASK_PLACEHOLDER_TO_REPLACE) {
                chars[i] = bankAccount.charAt(i);
            }
        }
        return new String(chars);
    }
    
    static String sha512(String message, int cycles) throws IllegalStateException {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte [] bytes = message.getBytes();
            String result = "";
            for(int i = 0; i < cycles ; i++) {
                 bytes = md.digest(bytes);
                 char [] chars = Hex.encodeHex(bytes);
                 result = new String(chars);
                 bytes = result.getBytes();
            }
            //char [] chars = Hex.encodeHex(bytes);
            //result = new String(chars);
            return result.toLowerCase();

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    } 
    
    static String getHash(String taxNumber, String bankAccount, String date, int hashCycles) {
        String notMasked = date  + taxNumber + bankAccount;
        String hashed = sha512(notMasked, hashCycles);
        return hashed;
    }
    private int isWhitelisted(String hashed) {
        try {
            int index = Arrays.binarySearch(_whitelist.activeTaxpayers, hashed);
            if(index >= 0) {
                return RESULT_ACTIVE;
            }
            String msg = "Index active: " + index;
            java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, msg);
            
            index = Arrays.binarySearch(_whitelist.excemptedTaxpayers, hashed);
            msg = "Index Exempted: " + index;
            java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, msg);
            
            if( index >= 0 ) {
                return RESULT_EXEMPTED;
            } else {
               return RESULT_NOT_FOUND; 
            }
        } catch (RuntimeException ex) {
            return RESULT_NOT_FOUND;
        }
    }
    
    
}
