
# Backend project
This is a Backend project which provides you 2 APIs. One for the intersting tweets and other for the full conversation. 
The tweet is intersting if it matches with one of the regex that is provided by the user.

## Description 
In this project, I have made the tweeter developer account to receive the tweets. I used Hosebird Client, A Java HTTP client for consuming Twitter's standard Streaming API. The Kafka producer will produce these tweets and Kafka consumers will consume these tweets. Regex List is stored in MongoDB. One consumer will check the tweet is interesting or not by checking the tweet with the regex list which is stored in MongoDB. If the tweet is interesting then that tweet will be stored in the elastic-search and the id of this tweet will be saved in the Redis. Now the second consumer will first check whether the tweet is a reply or not. If it is a reply then the consumer will check whether the parent of this reply is an interesting tweet or not by checking its parent id in Redis. If its parent is interesting then this reply wil be stored in the reply array of that parent tweet in elastic-search. Now the APIs can be created with the help of the spring-boot framework. And according to the API request, the tweets will be provided. In the first API you can give the name you want to see in the interesting tweets and you also can mention the number of tweets you want to see. If you don’t mention the size or name then it will give you the 10 latest interesting tweets. The second API will give you the full conversation of the given tweet id.


## Dependencies
It will work on any OS. To run this program I installed intellij idea. You need to install java with version>=8. You need to install Redis, MongoDB, Kafka, and elastic-search. You can change the ports and other configurations from the application.properties file.

## To run the app
Write your Twitter API tokens in src/main/resources/application.properties file.
### Steps

    1. Start Mongodb
        brew services start mongodb-community@5.0
             
    2. Start kafka zookeeper
        zookeeper-server-start config/zookeeper.properties

    3. Start kafka-server
        kafka-server-start config/server.properties

    4. Start Redis-server
        src/redis-server

    5. Start Elasticsearch
        bin/elasticsearch
    
    6. Start kibana (if you want to see the data)
        bin/kibana 

    7. If you are starting application first time, you have to make kafka-topic
        kafka-topics --bootstrap-server 127.0.0.1:9092 --topic twitter_tweets --create --partitions 3 --replication-factor 1

    8. Now you can run the Springboot application from intellij idea.

## Help
Make sure to start the servers or services mentioned above before starting the application. Otherwise, your application will not start.

## Authors
    Name  : Jay Shah
    Email : jaynshah610@gmail.com
