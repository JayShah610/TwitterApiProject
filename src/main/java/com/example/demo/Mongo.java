package com.example.demo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Mongo implements Runnable{
    private static final MongoClient client = MongoClients.create(ConfigProperties.getProperty("mongo.url"));
    private static final MongoDatabase database = client.getDatabase("Backend_Project");
    private static final MongoCollection<Document> regex_list = database.getCollection("regex_list");
    private static List<String> rlist = new ArrayList<>();
    private static Logger logger = LoggerFactory.getLogger(Mongo.class);
    public static List<String> getList(){
        synchronized (rlist) {
            return new ArrayList<>(rlist);
        }
    }

    public static void close(){
        client.close();
        logger.info("Mongo closed");
    }

    @Override
    public void run() {
        while(true) {
            List<Document> regex_collection = new ArrayList<>();
            regex_list.find().into(regex_collection);
            List<String> temp = new ArrayList<>();
            for (Document it : regex_collection) {
                String reg = InterestingTweets.getFeild(it.toJson(), "regex_string");
                temp.add(reg);
            }
            synchronized (rlist) {
                rlist = temp;
            }
//            System.out.println("---------------");
//            for(String s : rlist){
//                System.out.println(s);
//            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
