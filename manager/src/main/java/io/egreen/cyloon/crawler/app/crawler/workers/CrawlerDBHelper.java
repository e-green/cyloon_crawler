package io.egreen.cyloon.crawler.app.crawler.workers;

import io.egreen.cyloon.crawler.app.model.SeedUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by dewmal on 11/8/15.
 */
@Repository
public class CrawlerDBHelper {

    @Autowired
    private MongoTemplate mongoTemplate;


    /**
     * Get All Seeds
     *
     * @return
     */
    public List<SeedUrl> getSeedUrls() {
        List<SeedUrl> seeds = mongoTemplate.findAll(SeedUrl.class);
        return seeds;
    }

    /**
     * IS URL SHOULD VISIT
     *
     * @param href
     * @return
     */
    public boolean shouldVisit(String href) {
        return false;
    }


    /***
     * @param url
     * @param title
     */
    public void savePage(String url, String title) {


        // Before Save please check save policy

//Higly Working method should use in thread


    }


}
