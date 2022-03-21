package com.example.demo;

import java.util.HashMap;
import java.util.Map;

public class DateHelper {
    private static Map<String, String> month = new HashMap<>();
    static {
        month.put("Jan", "01");
        month.put("Feb", "02");
        month.put("Mar", "03");
        month.put("Apr", "04");
        month.put("May", "05");
        month.put("Jun", "06");
        month.put("Jul", "07");
        month.put("Aug", "08");
        month.put("Sep", "09");
        month.put("Oct", "10");
        month.put("Nov", "11");
        month.put("Dec", "12");
    }
    private DateHelper(){

    }
    public static String getDate(String s){
        String[] splited = s.split(" ");
        return splited[2]+"-"+month.get(splited[1])+"-"+splited[5].substring(0,4)+" "+splited[3];
    }
}
