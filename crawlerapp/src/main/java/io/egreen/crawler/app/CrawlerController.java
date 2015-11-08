package io.egreen.crawler.app;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

/**
 * Created by dewmal on 10/29/15.
 */
public class CrawlerController {

    private static CrawlController controller;

    public void Start() throws Exception {

        if (controller == null || controller.isShuttingDown()) {
//        Logger.getRootLogger().setLevel(Level.INFO);


//        String crawlStorageFolder = "/data/cyloon";
            String crawlStorageFolder = "~/data/cyloon";
            int numberOfCrawlers = 2;

            CrawlConfig config = new CrawlConfig();

//        config.setMaxDepthOfCrawling(3);
            config.setUserAgentString("cyloon (http://cyloon.com)");
            config.setCrawlStorageFolder(crawlStorageFolder);
            config.setPolitenessDelay(500);
            config.setResumableCrawling(true);
        /*
         * Instantiate the controller for this crawl.
         */
            PageFetcher pageFetcher = new PageFetcher(config);


            RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
            RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);


            controller = new CrawlController(config, pageFetcher, robotstxtServer);

        /*
         * For each crawl, you need to add some seed urls. These are the first
         * URLs that are fetched and then the crawler starts following links
         * which are found in these pages
         */
            controller.addSeed("http://ikman.lk/en/ads/ads-in-sri-lanka");
//        controller.addSeed("http://ikman.lk/en/ad/carp-fish-for-sale-ampara-1");


//        controller.addSeed("http://ikman.lk/si/ads/ads-in-sri-lanka");

        /*
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */
            controller.start(CyloonCrawler.class, numberOfCrawlers);

        }
    }
}
