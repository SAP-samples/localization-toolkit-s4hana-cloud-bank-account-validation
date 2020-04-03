package com.sap.poland.whitelist.service;

import java.time.LocalDateTime;

public class KeyDate {
    LocalDateTime _dateTime; 

    public KeyDate(LocalDateTime dateTime) {
        _dateTime = dateTime;
    }
    
    static KeyDate now() {
        LocalDateTime date = LocalDateTime.now();
        return new KeyDate(date);
    }
    
    String yyyyMMdd(){
        int month = _dateTime.getMonthValue();
        int day = _dateTime.getDayOfMonth();
        String year = String.valueOf(_dateTime.getYear());
        
        return year + twoDigits(month) + twoDigits(day);
    }
    
    private static String twoDigits(int num){
        return num < 10 ? "0" + String.valueOf(num) : String.valueOf(num);
    }
}
