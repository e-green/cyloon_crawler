package io.egreen.cyloon.crawler.app.crawler.workers;

import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

import java.util.regex.Pattern;

/**
 * Created by dewmal on 11/8/15.
 */
public class CrawlerWorker extends WebCrawler {


    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg"
            + "|png|mp3|mp3|zip|gz))$");


    private CrawlerDBHelper crawlerDBHelper;


    public CrawlerWorker() {
        if (crawlerDBHelper == null) {
            CrawlController myController1 = getMyController();
            crawlerDBHelper = (CrawlerDBHelper) myController1.getCustomData();
        }
    }


    /**
     * This method receives two parameters. The first parameter is the page
     * in which we have discovered this new url and the second parameter is
     * the new url. You should implement this function to specify whether
     * the given url should be crawled or not (based on your crawling logic).
     * In this example, we are instructing the crawler to ignore urls that
     * have css, js, git, ... extensions and to only accept urls that start
     * with "http://www.ics.uci.edu/". In this case, we didn't need the
     * referringPage parameter to make the decision.
     * <p>
     * <p>
     * http://ikman.lk/en/ads/electronics-in-sri-lanka-428
     */
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        return checkMatch(href);
    }

    private boolean checkMatch(String href) {
        return !FILTERS.matcher(href).matches()
                && crawlerDBHelper.shouldVisit(href);
    }


    /**
     * This function is called when a page is fetched and ready
     * to be processed by your program.
     */
    @Override
    public void visit(Page page) {


        String url = page.getWebURL().getURL();
        System.out.println("URL: " + url);

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String text = htmlParseData.getText();
            String html = htmlParseData.getHtml();

            crawlerDBHelper.savePage(url, htmlParseData.getTitle());


        }
    }


}
