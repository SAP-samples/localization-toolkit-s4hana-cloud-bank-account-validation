package com.sap.poland.whitelist.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class Whitelist {
    static final String GEN_DATE_NAME = "dataGenerowaniaDanych" ;
    static final String NUMBER_OF_TRANFORMATIONS = "liczbaTransformacji";
    
    private static String _checkSum;
    private static String _checkSumMethod;
            
    @JsonProperty("naglowek")    
    public Map<String, String> header;
    
    @JsonProperty("skrotyPodatnikowCzynnych") 
    public String [] activeTaxpayers;
    
    @JsonProperty("skrotyPodatnikowZwolnionych")    
    public String [] excemptedTaxpayers;
    
    @JsonProperty("maski")    
    public String [] masks;
    
    
    int getNumberOfTranformations() {
        String numOfTrans = header.get(NUMBER_OF_TRANFORMATIONS);
        if(numOfTrans != null) {
            return Integer.valueOf(numOfTrans);
        }
        return 0;
    }
    
    String getDate() {
        if(header == null) {
            return null;
        }
        return header.getOrDefault(GEN_DATE_NAME, null);
    }
    
    @JsonIgnore
    String getCheckSum() {
        return _checkSum;
    }
    
    @JsonIgnore
    String getCheckSumMethod() {
        return _checkSumMethod;
    }
    
    @JsonIgnore
    void setCheckSum(String checkSum, String checkSumMethod) {
        _checkSum = checkSum;
        _checkSumMethod = checkSumMethod;
    }
}
