package com.example.demo;

import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.endpoint.StatusesSampleEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public class TwiiterCilent {
    private static final String CONSUMER_KEY = ConfigProperties.getProperty("consumerKey");
    private static final String CONSUMER_SECRET = ConfigProperties.getProperty("consumerSecret");
    private static final String TOKEN =ConfigProperties.getProperty("Accesstoken");
    private static final String TOKEN_SECRET = ConfigProperties.getProperty("AccessTokenSecret");
    private TwiiterCilent(){

    }
    public static Client getFilterStreamClient(BlockingQueue<String> msgQueue, List<String> terms) {

        /* * Declare the host you want to connect to, the endpoint, and authentication (basic auth or oauth) */
        Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
        StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();

        hosebirdEndpoint.trackTerms(terms);

        // These secrets should be read from a config file
        Authentication hosebirdAuth = new OAuth1(CONSUMER_KEY, CONSUMER_SECRET, TOKEN, TOKEN_SECRET);

        ClientBuilder builder = new ClientBuilder()
                .name("Hosebird-Client-01")
                .hosts(hosebirdHosts)
                .authentication(hosebirdAuth)
                .endpoint(hosebirdEndpoint)
                .processor(new StringDelimitedProcessor(msgQueue));

        return builder.build();
    }

    public static Client getSampleStreamClient(BlockingQueue<String> msgQueue) {

        /* * Declare the host you want to connect to, the endpoint, and authentication (basic auth or oauth) */

        StatusesSampleEndpoint hosebirdEndpoint = new StatusesSampleEndpoint();
        hosebirdEndpoint.stallWarnings(false);

        // These secrets should be read from a config file
        Authentication hosebirdAuth = new OAuth1(CONSUMER_KEY, CONSUMER_SECRET, TOKEN, TOKEN_SECRET);

        ClientBuilder builder = new ClientBuilder()
                .name("Hosebird-Client-01")
                .hosts(Constants.STREAM_HOST)
                .authentication(hosebirdAuth)
                .endpoint(hosebirdEndpoint)
                .processor(new StringDelimitedProcessor(msgQueue));

        return builder.build();
    }
}
