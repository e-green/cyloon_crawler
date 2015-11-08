package io.egreen.cyloon.crawler.app.crawler;

import io.egreen.cyloon.crawler.app.crawler.workers.Crawler;
import io.egreen.cyloon.crawler.app.model.SeedUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by dewmal on 11/8/15.
 */
@Service
@Scope(value = "singleton")
public class CrawlerServiceApi {

    @Autowired
    private MongoTemplate mongoTemplate;


    @Autowired
    private Crawler crawler;


    /**
     * Add Seeds
     *
     * @param seeds
     * @return
     */
    public boolean addSeeds(String... seeds) {

        for (String seed : seeds) {
            SeedUrl seedUrl = new SeedUrl();
            seedUrl.setName(seed);
            seedUrl.setUrl(seed);
            seedUrl.setStatus(false);
            seedUrl.setType("CALSSIFIED");
            seedUrl.setSavePattern("fill");
            seedUrl.setShouldVisitPattern("fill");
            mongoTemplate.save(seedUrl);
        }

        try {
            crawler.notifyNewSeeds();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return false;
    }


    /**
     * Check Crawler Status
     *
     * @return
     */
    public boolean isCrawlerRunning() {
        return crawler.isRunning();
    }

    public void start() throws Exception {

        crawler.load();

    }
}
