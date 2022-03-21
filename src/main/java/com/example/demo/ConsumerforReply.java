package com.example.demo;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;


public class ConsumerforReply implements Runnable {
    @Override
    public void run() {

        Logger logger = LoggerFactory.getLogger(ConsumerforReply.class);

        String topic = "twitter_tweets";
        KafkaConsumer<String, String> consumer = Producer_Consumer.getConsumer("C2");
        consumer.subscribe(Collections.singleton(topic));
        while (true) {
            ConsumerRecords<String, String> tweets = consumer.poll(Duration.ofMillis(100));

            for (ConsumerRecord<String, String> tweet : tweets) {
                // * extrct the feilds you want
                Producer_Consumer.countObject.decrease();
                String recived_tweet = tweet.value();
                String text = InterestingTweets.getFeild(recived_tweet, "text");
                String id = InterestingTweets.getFeild(recived_tweet, "id");
                String in_reply_to_status_id = InterestingTweets.getFeild(recived_tweet, "in_reply_to_status_id");

                if ((in_reply_to_status_id.equals("null")) || in_reply_to_status_id.equals("")) continue;
                boolean isParent = RedisHelper.isPresent(in_reply_to_status_id);

                if (!isParent) {
                    continue;
                }

                ElasticSearchHelper.addReply(recived_tweet);

            }
        }
    }
}
