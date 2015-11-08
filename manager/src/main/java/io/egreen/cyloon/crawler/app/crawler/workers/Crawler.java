package io.egreen.cyloon.crawler.app.crawler.workers;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import io.egreen.cyloon.crawler.app.model.SeedUrl;
import org.springframework.beans.factory.annotation.Autowired;
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


    private final static Map<String, CrawlController> CRAWL_CONTROLLER_MAP = new ConcurrentHashMap<>();


    public void load() throws Exception {
        int numberOfCrawlers = 4;

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


        for (String key : seedUrlMap.keySet()) {

            CrawlController controller = CRAWL_CONTROLLER_MAP.get(key);

            if (controller == null) {
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

            controller.setCustomData(crawlerDBHelper);

        /*
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */
            controller.startNonBlocking(CrawlerWorker.class, numberOfCrawlers);

            CRAWL_CONTROLLER_MAP.put(key, controller);
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
