package io.egreen.cyloon.crawler.app.crawler.dep;

import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import io.egreen.cyloon.crawler.app.crawler.process.impl.IkmanLkProcessor;
import io.egreen.cyloon.crawler.app.model.SiteDate;
import io.egreen.cyloon.crawler.app.model.SiteLinks;
import io.egreen.cyloon.crawler.app.service.CrawlerService;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by dewmal on 10/29/15.
 */
//@Component
public class CyloonCrawler extends WebCrawler {


    private CrawlerService crawlerService;

//    private static final String BASE_URL = "http://apps.egreenhive.com:6868/web/api/";
//    private static final String BASE_URL = "http://localhost:8080/web/api/";

//    private JerseyClient client = JerseyClientBuilder.createClient();

    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg"
            + "|png|mp3|mp3|zip|gz))$");

//    private final static Pattern FILTERS_NOT_SAVE = Pattern.compile(".*(\\.(css|js|gif|jpg"
//            + "|png|mp3|mp3|zip|gz))$");


    public CyloonCrawler() {

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
        return checkExcludes(href);
    }

    private boolean checkMatch(String href) {
        return !FILTERS.matcher(href).matches()
                && href.startsWith("http://ikman.lk/") || href.startsWith("https://ikman.lk/");
    }


    /**
     * This function is called when a page is fetched and ready
     * to be processed by your program.
     */
    @Override
    public void visit(Page page) {

        CrawlController myController1 = getMyController();
        crawlerService = (CrawlerService) myController1.getCustomData();


        String url = page.getWebURL().getURL();
        System.out.println("URL: " + url);

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String text = htmlParseData.getText();
            String html = htmlParseData.getHtml();

            try {
                if (checkExcludes(url)) {
                    System.out.println("Accepted ");

                    SiteDate siteDate = new IkmanLkProcessor().getCrawlerModel(Jsoup.parse(html));
                    siteDate.setLink(url);
                    System.out.println(siteDate);

                    System.out.println("Strating container update.....");

                    crawlerService.updateUrlInfo(siteDate);
//                    Response post = client.target(new URI(BASE_URL + "crawler/save")
//                    ).request().post(Entity.entity(siteDate, MediaType.APPLICATION_JSON_TYPE));
                    System.out.println("Ending container update.....");
                } else {
                    System.out.println("Rejected ");

                }

            } catch (HttpStatusException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }


            Set<WebURL> links = htmlParseData.getOutgoingUrls();

            List<SiteLinks> backLinks = new ArrayList<>();

//            Elements anchor = doc.select("a");
            for (WebURL element : links) {
                if (checkMatch(element.getURL())) {
                    SiteLinks siteLinks = new SiteLinks();
                    siteLinks.setLink(element.getURL());
                    siteLinks.setLastVisit(null);
                    siteLinks.setVisitCount(0);
                    siteLinks.setStatus("born");
                    siteLinks.setNote("Just Found");
                    backLinks.add(siteLinks);
                }
            }


            Response response = null;
            try {


                crawlerService.saveSeedUrl(backLinks);

            } catch (Exception e) {
                e.printStackTrace();
            }

//            System.out.println("Text length: " + text.length());
//            System.out.println("Html length: " + html.length());
//            System.out.println("Number of outgoing links: " + links.size());
        }
//        myController.ad
    }


    /**
     * These url should not save
     *
     * @param url
     * @return
     */
    private boolean checkExcludes(String url) {

        String rejects[] = new String[]{

                "http://ikman.lk/en/ad/",
                "http://ikman.lk/si/ad/",
                "http://ikman.lk/ta/ad/"

        };


        String rejectsEnd[] = new String[]{

                "/report"

        };

        for (String sCurrentLine :
                rejects) {
//            System.out.println("SAVING DATA : " + url + " , " + sCurrentLine + " , " + url.toLowerCase().startsWith(sCurrentLine));
            if (url.toLowerCase().startsWith(sCurrentLine)) {
                for (String rejectEnd : rejectsEnd) {
                    if (url.toLowerCase().startsWith(sCurrentLine)) {
                        if (url.toLowerCase().endsWith(sCurrentLine)) {
                            return false;
                        }
                    }

                }
                return true;
            }
        }

//                System.out.println(sCurrentLine);


        return false;
    }
}
