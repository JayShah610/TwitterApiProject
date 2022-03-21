package com.example.demo;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;


public class ConsumerforTweet implements Runnable {

    @Override
    public void run() {
        Logger logger = LoggerFactory.getLogger(ConsumerforTweet.class);

        String topic = "twitter_tweets";
        KafkaConsumer<String,String> consumer = Producer_Consumer.getConsumer("C1");
        consumer.subscribe(Collections.singleton(topic));

        while (true){
            ConsumerRecords<String,String> tweets = consumer.poll(Duration.ofMillis(100));
            for(ConsumerRecord<String ,String > tweet : tweets){
                Producer_Consumer.countObject.increase();
                // * extrct the feilds you want
                String recived_tweet = tweet.value();

                String text = InterestingTweets.getFeild(recived_tweet,"text");
                String id = InterestingTweets.getFeild(recived_tweet,"id") ;
                String in_reply_to_status_id = InterestingTweets.getFeild(recived_tweet,"in_reply_to_status_id");
                String isRetweet = InterestingTweets.getFeild(recived_tweet,"retweeted_status");
                //System.out.println(isRetweet);

                // TODO rempve retweets
                if(!isRetweet.equals("null") || text.equals("null") || !in_reply_to_status_id.equals("null")) continue;
                String text_low= text.toLowerCase();
                boolean flag = InterestingTweets.isIntresting(text_low,id);
                if(flag){
                    ElasticSearchHelper.addTweet(recived_tweet);
                }
                //System.out.println(id + " "+in_reply_to_status_id+" "+ text);
//                if(flag)
//                    logger.info(id + " "+in_reply_to_status_id+" "+ text);
                //logger.info("Partation: "+record.partition()+ ", Offset"  + record.offset());
            }

        }

    }
}
