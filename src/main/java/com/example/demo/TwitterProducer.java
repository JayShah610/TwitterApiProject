package com.example.demo;
import com.google.common.collect.Lists;
import com.twitter.hbc.core.Client;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class TwitterProducer implements Runnable {

    Logger logger = LoggerFactory.getLogger(TwitterProducer.class.getName());
    List<String> terms = Lists.newArrayList("war", "modi", "putin", "india", "virat","rohit","bcci","corona","covid");


    public void run(){

        //logger.info("Setup");

        // * Set up your blocking queues: Be sure to size these properly based on expected TPS of your stream */
        BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>(1000);

        // * create a twitter FilterStreamClient
        //Client client = TwiiterCilent.getFilterStreamClient(msgQueue,terms);

        // * create a twitter FilterStreamClient
        Client client = TwiiterCilent.getSampleStreamClient(msgQueue);

        // * Attempts to establish a connection.
        client.connect();

        // *create a kafka producer
        KafkaProducer<String, String> producer = Producer_Consumer.getProducer();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("stopping application...");
            Mongo.close();
            RedisHelper.close();
            ElasticSearchHelper.close();
            client.stop();
            logger.info("Tweeter client stopped");
            producer.close();
            logger.info("Producer closed");
            logger.info("done!");
        }));

        // * loop to send tweets to kafka
        while (!client.isDone()) {
            String msg = null;
            try {
                msg = msgQueue.poll(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
                client.stop();
            }
            if (msg != null){
                //logger.info(msg);
                producer.send(new ProducerRecord<>("twitter_tweets", null, msg), new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                        if (e != null) {
                            logger.error("Something bad happened", e);
                        }
                    }
                });
            }
        }
    }





}