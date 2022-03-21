package com.example.demo;

import org.apache.http.HttpHost;
import org.apache.lucene.search.TotalHits;
import org.json.JSONException;
import org.json.JSONObject;
import org.opensearch.action.search.SearchRequest;
import org.opensearch.action.search.SearchResponse;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestClientBuilder;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.index.query.QueryBuilder;
import org.opensearch.index.query.QueryBuilders;
import org.opensearch.search.SearchHit;
import org.opensearch.search.SearchHits;
import org.opensearch.search.builder.SearchSourceBuilder;
import org.opensearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.*;

@RestController
public class GetTweet {
    private static Logger logger = LoggerFactory.getLogger(GetTweet.class);
    private static RestClientBuilder  builder = RestClient.builder(new HttpHost("localhost", 9200));

    @GetMapping("/api1")
    public static List<HashMap > getTweets(@RequestParam(name="word",required = false,defaultValue = "-1$") String word,
                    @RequestParam(name="size",required = false,defaultValue = "100") String siz){
        word=word.toLowerCase(Locale.ROOT);
        Integer si = Integer.parseInt(siz);
        RestHighLevelClient client = new RestHighLevelClient(builder);
        //* get the tweets
        SearchRequest searchRequest = new SearchRequest("tweets");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchSourceBuilder.sort("created_at", SortOrder.DESC).size(si);
        if(!word.equals("-1$")){
            searchSourceBuilder.query(QueryBuilders.matchPhrasePrefixQuery("tweet", word)).size(si);
            //searchSourceBuilder.query(W)
        }

        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        SearchHits hits = searchResponse.getHits();
        SearchHit[] hit = hits.getHits();
        TotalHits totalHits = hits.getTotalHits();
        List<HashMap> results = new ArrayList<>();
        for (int i=0;i<hit.length;i++) {
            Map<String, Object> sourceAsMap = hit[i].getSourceAsMap();
            String user = (String) sourceAsMap.get("user");
            String tweet = (String) sourceAsMap.get("tweet");
            String date = (String) sourceAsMap.get("created_at");
            HashMap<String,String> ans = new HashMap<>();
            ans.put("user",user);
            ans.put("tweet",tweet);
            ans.put("date",date);
            results.add(ans);
        }
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;

    }
    @GetMapping("/api2")
    public static List<HashMap> getReply(@RequestParam(name="id", required = true) String tweetId){
        RestHighLevelClient client = new RestHighLevelClient(builder);
        SearchRequest searchRequest = new SearchRequest("tweets");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.termQuery("_id", tweetId));
            //searchSourceBuilder.query(W)
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<HashMap> results = new ArrayList<>();
        SearchHits hits = searchResponse.getHits();
        SearchHit[] hit = hits.getHits();
        TotalHits totalHitxs = hits.getTotalHits();

        for (int i=0;i<hit.length;i++) {
            Map<String, Object> sourceAsMap = hit[i].getSourceAsMap();
            String user = (String) sourceAsMap.get("user");
            String tweet = (String) sourceAsMap.get("tweet");
            String date = (String) sourceAsMap.get("created_at");
            ArrayList<String> reply = (ArrayList<String>) sourceAsMap.get("replies");
            HashMap<String,Object> ans = new HashMap<>();
            ans.put("user",user);
            ans.put("tweet",tweet);
            ans.put("date",date);
            ans.put("replies",reply);
            results.add(ans);
        }
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }
}
