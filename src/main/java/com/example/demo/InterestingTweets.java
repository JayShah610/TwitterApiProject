package com.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.regex.Pattern;

public class InterestingTweets {

    private InterestingTweets(){

    }
    public static String getFeild(String recived_tweet , String feild)  {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(recived_tweet);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if(jsonNode == null ) {
            return "null";
        }
        JsonNode f2FieldNode = jsonNode.get(feild);
        if(f2FieldNode==null) {
            return "null";
        }
        return f2FieldNode.toString();
    }


    public static Boolean isIntresting(String text,String id_str){
        Boolean is = false;
        List<String> regexlist = Mongo.getList();

        for (String s : regexlist) {
            if (Pattern.matches(s, text)) {
                RedisHelper.addToRedis(id_str);
                return true;
            }
        }
        return is;
    }

}
