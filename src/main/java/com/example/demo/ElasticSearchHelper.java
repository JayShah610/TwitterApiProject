package com.example.demo;

import org.apache.http.HttpHost;
import org.opensearch.action.index.IndexRequest;
import org.opensearch.action.index.IndexResponse;
import org.opensearch.action.update.UpdateRequest;
import org.opensearch.action.update.UpdateResponse;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestClientBuilder;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.script.Script;
import org.opensearch.script.ScriptType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;

public class ElasticSearchHelper {
    private static final String HOST = ConfigProperties.getProperty("elasticsearch.host");
    private static final Integer PORT = Integer.parseInt(ConfigProperties.getProperty("elasticsearch.port"));
    private static final RestClientBuilder builder = RestClient.builder(new HttpHost(HOST, PORT));
    private static final RestHighLevelClient client = new RestHighLevelClient(builder);
    private static Logger logger = LoggerFactory.getLogger(ElasticSearchHelper.class);
    private ElasticSearchHelper(){

    }
    public static void addTweet(String json){

        HashMap<String, Object > stringMapping = new HashMap<>();

        String id = InterestingTweets.getFeild(json,"id");

        String userDetail = InterestingTweets.getFeild(json,"user");
        String name = InterestingTweets.getFeild(userDetail,"name");
        stringMapping.put("user",name.substring(1,name.length()-1));


        String text = InterestingTweets.getFeild(json,"text");
        stringMapping.put("tweet",text.substring(1,text.length()-1));

        String dateAsStr = InterestingTweets.getFeild(json,"created_at");
        String date = DateHelper.getDate(dateAsStr);
        stringMapping.put("created_at",date);


        stringMapping.put("replies", Collections.EMPTY_LIST);

        IndexRequest request = new IndexRequest("tweets");
        request.id(id);
        request.source(stringMapping); //Place your content into the index's source.
        //System.out.println(date);
        try {
            IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);
            //logger.info("{}",indexResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void addReply(String json){
        HashMap<String,Object > stringMapping = new HashMap<>();


        String pid = InterestingTweets.getFeild(json,"in_reply_to_status_id");

        String id = InterestingTweets.getFeild(json,"id");
        stringMapping.put("id",id);

        String userDetail = InterestingTweets.getFeild(json,"user");
        String name = InterestingTweets.getFeild(userDetail,"name");
        stringMapping.put("user",name.substring(1,name.length()-1));


        String text = InterestingTweets.getFeild(json,"text");
        stringMapping.put("tweet",text.substring(1,text.length()-1));

        String dateAsStr = InterestingTweets.getFeild(json,"created_at");
        String date = DateHelper.getDate(dateAsStr);
        stringMapping.put("created_at",date);

        UpdateRequest request1 = new UpdateRequest("tweets", pid);

        Script inline = new Script(ScriptType.INLINE, "painless",
                "ctx._source.replies.add(params)", stringMapping);
        request1.script(inline);
        try {
            UpdateResponse indexResponse = client.update(request1, RequestOptions.DEFAULT);
            logger.info("{}",indexResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }//
    }

    public static void close(){
        try {
            client.close();
            logger.info("ES closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
