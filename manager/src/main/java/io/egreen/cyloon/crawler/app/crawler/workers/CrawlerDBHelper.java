package io.egreen.cyloon.crawler.app.crawler.workers;

import io.egreen.cyloon.crawler.app.model.SeedPolicy;
import io.egreen.cyloon.crawler.app.model.SeedUrl;
import io.egreen.cyloon.crawler.app.model.SiteDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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
        return true;
    }


    /***
     * @param url
     * @param title
     */
    public void savePage(String url, String title) {
        SiteDate siteDate = new SiteDate();
        siteDate.setGrabbed(false);
        siteDate.setGrabCount(-1);
        siteDate.setLink(url);
        siteDate.setTitle(title);
        mongoTemplate.save(siteDate);
    }


    public List<SeedPolicy> getSeedPolicies() {
        return mongoTemplate.findAll(SeedPolicy.class);
    }
}
