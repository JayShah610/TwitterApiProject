package com.example.demo;

public class Main  {
    private Main()
    {

    }    public static void go() {
        new Thread(new Mongo()).start();
        new Thread(new TwitterProducer()).start();
        new Thread(new ConsumerforTweet()).start();
        new Thread(new ConsumerforReply()).start();

    }

}
