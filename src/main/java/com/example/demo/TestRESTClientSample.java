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
import java.util.*;

import static java.util.Collections.singletonMap;


public class TestRESTClientSample {

    public static void main(String[] args) throws IOException {
        Logger logger = LoggerFactory.getLogger(TestRESTClientSample.class);

        RestClientBuilder builder = RestClient.builder(new HttpHost("localhost", 9200));
        RestHighLevelClient client = new RestHighLevelClient(builder);

        IndexRequest request = new IndexRequest("custom-index"); //Add a document to the custom-index we created.
        request.id("1"); //Assign an ID to the document.

        HashMap<String, Object> stringMapping = new HashMap<String, Object>();

        //jo.put("replies",jo1.toString());
        stringMapping.put("text","hello");
        stringMapping.put("date","22-2-2022");


        HashMap<String,Object> oo =new HashMap<>();
        oo.put("jay","shah");
        oo.put("dfg","fgh");

        List<Object> o = new ArrayList<>();
        o.add(oo);
        stringMapping.put("replies",o );
        request.source(stringMapping);
        UpdateRequest request1 = new UpdateRequest(
                "custom-index",
                "1");

        Script inline = new Script(ScriptType.INLINE, "painless",
                "ctx._source.replies.add(params)", oo);
        request1.script(inline);
        try {
            UpdateResponse indexResponse = client.update(request1, RequestOptions.DEFAULT);
            logger.info("{}",indexResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }//



        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}