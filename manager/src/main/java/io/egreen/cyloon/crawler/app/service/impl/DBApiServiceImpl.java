package io.egreen.cyloon.crawler.app.service.impl;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import io.egreen.cyloon.crawler.app.service.DBApiService;
import org.bson.RawBsonDocument;

/**
 * Created by dewmal on 10/17/15.
 */


public class DBApiServiceImpl implements DBApiService {
    @Override
    public MongoDatabase getDB() {


        // To directly connect to a single MongoDB server (note that this will not auto-discover the primary even
// if it's a member of a replica set:
//        MongoClient mongoClient = new MongoClient();
// or
//        MongoClient mongoClient = new MongoClient( "localhost" );
// or
        MongoClient mongoClient = new MongoClient("208.43.16.210", 27017);

        MongoDatabase db = mongoClient.getDatabase("crawler_date_classified");

        return db;
    }
}
