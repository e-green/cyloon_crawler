package io.egreen.cyloon.crawler.app.crawler.workers;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import io.egreen.cyloon.crawler.app.db.repositary.SiteDataRepository;
import io.egreen.cyloon.crawler.app.model.SeedUrl;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import javax.inject.Singleton;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by dewmal on 11/8/15.
 */
@Repository
@Singleton
public class Crawler {

    @Autowired
    private String crawlerStorage;

    @Autowired
    private CrawlConfig crawlConfig;


    @Autowired
    private CrawlerDBHelper crawlerDBHelper;
    @Autowired
    private HttpSolrClient httpSolrClient;

    private final static Map<String, CrawlController> CRAWL_CONTROLLER_MAP = new ConcurrentHashMap<>();


    public void load() throws Exception {
//        httpSolrClient.deleteByQuery("*:*");

        int numberOfCrawlers = 4;
//        System.out.println(crawlConfig);
        /*
         * Instantiate the controller for this crawl.
         */
        PageFetcher pageFetcher = new PageFetcher(crawlConfig);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);


        List<SeedUrl> seedUrls = crawlerDBHelper.getSeedUrls();
        Map<String, List<SeedUrl>> seedUrlMap = new HashMap<>();
        for (SeedUrl seedUrl : seedUrls) {
            List<SeedUrl> urls = seedUrlMap.get(seedUrl.getName());
            if (urls == null) {
                urls = new ArrayList<>();
            }

            urls.add(seedUrl);
            seedUrlMap.put(seedUrl.getName(), urls);

        }
//        System.out.println(seedUrlMap);

        for (String key : seedUrlMap.keySet()) {

            CrawlController controller = CRAWL_CONTROLLER_MAP.get(key);

            if (controller == null || controller != null && !controller.isFinished()) {
                controller = new CrawlController(crawlConfig, pageFetcher, robotstxtServer);
            }

        /*
         * For each crawl, you need to add some seed urls. These are the first
         * URLs that are fetched and then the crawler starts following links
         * which are found in these pages
         */
            List<SeedUrl> urls = seedUrlMap.get(key);

            for (SeedUrl url : urls) {
                controller.addSeed(url.getUrl());
            }

            Map<Object, Object> customData = new HashMap<>();
            customData.put(CrawlerDBHelper.class, crawlerDBHelper);
            customData.put("NAME", key);

            controller.setCustomData(customData);

        /*
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */


            CRAWL_CONTROLLER_MAP.put(key, controller);
        }

        for (String crawlerControllerKey : CRAWL_CONTROLLER_MAP.keySet()) {
            CrawlController crawlController = CRAWL_CONTROLLER_MAP.get(crawlerControllerKey);

            crawlController.startNonBlocking(CrawlerWorker.class, numberOfCrawlers);

//            System.out.println(crawlController.isFinished());
        }


    }


    /**
     * Check crawler Status
     *
     * @return
     */
    public boolean isRunning() {

        for (String keyNameCrawler : CRAWL_CONTROLLER_MAP.keySet()) {
            if (!CRAWL_CONTROLLER_MAP.get(keyNameCrawler).isFinished()) {
                return true;
            }
        }

        return false;
    }


    /**
     * Seed List has new data please consider that.
     */
    public void notifyNewSeeds() throws Exception {
        load();
    }


    /**
     * Helper function to convert a string into an array of strings by
     * separating them using whitespace.
     *
     * @param str string to be tokenized
     * @return an array of strings that contain a each word each
     */
    public static String[] tokenize(String str) {
        StringTokenizer tok = new StringTokenizer(str);
        String tokens[] = new String[tok.countTokens()];
        int i = 0;
        while (tok.hasMoreTokens()) {
            tokens[i] = tok.nextToken();
            i++;
        }

        return tokens;

    }
}
