package io.egreen.cyloon.crawler.app.service;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import javax.inject.Singleton;

/**
 * Created by dewmal on 10/15/15.
 */


public interface DBApiService {

    public MongoDatabase getDB();
}
