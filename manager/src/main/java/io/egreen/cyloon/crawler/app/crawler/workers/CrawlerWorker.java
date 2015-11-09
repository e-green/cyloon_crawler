package io.egreen.cyloon.crawler.app.crawler.workers;

import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import io.egreen.cyloon.crawler.app.model.SeedPolicy;
import io.egreen.cyloon.crawler.app.model.SeedUrl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by dewmal on 11/8/15.
 */
public class CrawlerWorker extends WebCrawler {


    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg"
            + "|png|mp3|mp3|zip|gz))$");


    private CrawlerDBHelper crawlerDBHelper;

    private String name;

    private List<SeedUrl> seedUrls;
    private final Map<String, SeedPolicy> seedPolicies = new HashMap<>();


    @Override
    public void onStart() {
        super.onStart();
        System.out.println("CrawlerWorker" + crawlerDBHelper);
        try {
            if (crawlerDBHelper == null) {
                CrawlController myController1 = getMyController();
                Map customData = (Map) myController1.getCustomData();
                crawlerDBHelper = (CrawlerDBHelper) customData.get(CrawlerDBHelper.class);
                seedUrls = crawlerDBHelper.getSeedUrls();
                name = (String) customData.get("NAME");

                List<SeedPolicy> seedPoliciesList = crawlerDBHelper.getSeedPolicies();
                for (SeedPolicy seedPolicy : seedPoliciesList) {
                    seedPolicies.put(seedPolicy.getName(), seedPolicy);
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("CrawlerWorker" + crawlerDBHelper);
    }

    @Override
    public boolean isNotWaitingForNewURLs() {
        System.out.println("Wating for urls ....");
        return super.isNotWaitingForNewURLs();
    }

    @Override
    public void onBeforeExit() {
        super.onBeforeExit();
        System.out.println("Exitting....");
    }

    @Override
    protected WebURL handleUrlBeforeProcess(WebURL curURL) {
        return super.handleUrlBeforeProcess(curURL);
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
        boolean match = checkMatch(href);
        System.out.println(name + " = " + href + " - " + match);

        return match;
    }

    private boolean checkMatch(String href) {

        if (!FILTERS.matcher(href).matches()) {
            for (SeedUrl seedUrl : seedUrls) {
                System.out.println(seedUrl.getShouldVisitPattern());
                boolean shouldVisit = Pattern.matches(seedUrl.getShouldVisitPattern(), href);
                boolean shouldReject = Pattern.matches(seedPolicies.get(seedUrl.getName()).getRejectPolicy(), href);
                if (shouldVisit && !shouldReject) {
                    return true;
                }
            }
        }


        return false;
    }


    /**
     * This function is called when a page is fetched and ready
     * to be processed by your program.
     */
    @Override
    public void visit(Page page) {


        String url = page.getWebURL().getURL();
//        System.out.println("URL: " + url);

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            if (canSave(url)) {
                crawlerDBHelper.savePage(url, htmlParseData.getTitle());
            }
        }
    }


    private boolean canSave(String url) {
        for (String policyKey : seedPolicies.keySet()) {
            SeedPolicy seedPolicy = seedPolicies.get(policyKey);
            return Pattern.matches(seedPolicy.getAcceptPolicy(), url);
        }
        return false;
    }

}
