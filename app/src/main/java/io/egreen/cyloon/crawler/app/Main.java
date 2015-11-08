package io.egreen.cyloon.crawler.app;

import io.egreen.cyloon.crawler.app.data.SiteDate;
import io.egreen.cyloon.crawler.app.data.SiteLinks;
import io.egreen.cyloon.crawler.app.process.PagePaserProcess;
import io.egreen.cyloon.crawler.app.process.impl.IkmanLKCategoryPageProcessor;
import io.egreen.cyloon.crawler.app.process.impl.IkmanLkProcessor;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

/**
 * Created by dewmal on 10/12/15.
 */
public class Main {

//            private static final String BASE_URL = "http://apps.egreenhive.com:6868/web/api/";
    private static final String BASE_URL = "http://localhost:8080/web/api/";
    private static long errors = 0;
    private static long sucess = 0;

    private static long count = 0;

    private static boolean exit;
    private static long linkCount;

    public static void main(String[] args) throws IOException, URISyntaxException {
        final long startTime = System.nanoTime();
        checkAndCrawler(startTime);
    }

    private static void checkAndCrawler(final long startTime) {
        final JerseyClient client = JerseyClientBuilder.createClient();
        System.out.println("Strating container update.....");
        List<LinkedHashMap> list = client.target(BASE_URL + "crawler/nextbulk").queryParam("crawlerId", 4512).request().get(List.class);

        if (list != null)
            linkCount = list.size();

        System.out.println(Arrays.toString(list.toArray()));
        for (LinkedHashMap map : list) {
            final SiteLinks grabberUrl = new SiteLinks();
            grabberUrl.setLink(map.get("link") + "");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        load(grabberUrl.getLink());

                        grabberUrl.setStatus("visited");
                        grabberUrl.setLastVisit(new Date());
                        Response response = client.target(new URI(BASE_URL + "crawler/seeds/update")).request().post(Entity.entity(grabberUrl, MediaType.APPLICATION_JSON_TYPE));
                        System.out.println(response);

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    System.out.println(" End " + (sucess++) + " " + (System.nanoTime() - startTime) / 1000000);
                }
            }).start();

        }
        System.out.println(" Total End" + (System.nanoTime() - startTime) / 1000000);
//        while (true) {
//        }
    }

    public static void load(String url) throws IOException, URISyntaxException {
        URL urlLink = new URL(url);
        JerseyClient client = JerseyClientBuilder.createClient();
        try {
            Document doc = Jsoup.connect(url).userAgent("chrome").get();


            PagePaserProcess paserProcess = null;

            if (url.contains("ikman.lk/en/ads") || url.contains("ikman.lk/si/ads") || url.contains("ikman.lk/ta/ads")) {
                paserProcess = new IkmanLKCategoryPageProcessor();
            } else if (url.contains("ikman.lk")) {
                paserProcess = new IkmanLkProcessor();

            }

            SiteDate crawlerModel = paserProcess.getCrawlerModel(doc);

            if (crawlerModel != null) {
                crawlerModel.setLink(url);
                System.out.println("Strating container update.....");
                Response post = client.target(new URI(BASE_URL + "crawler/save")).request().post(Entity.entity(crawlerModel, MediaType.APPLICATION_JSON_TYPE));
                System.out.println("Ending container update.....");

            }
            List<SiteLinks> links = new ArrayList<>();

            Elements anchor = doc.select("a");
            for (Element element : anchor) {
                String href = element.attr("href");
                if (!(href.startsWith("http://") || href.startsWith("https://"))) {
                    href = urlLink.getProtocol() + "://" + urlLink.getHost() + "" + href;

                    SiteLinks siteLinks = new SiteLinks();
                    siteLinks.setLink(href);
                    siteLinks.setLastVisit(null);
                    siteLinks.setVisitCount(0);
                    siteLinks.setStatus("born");
                    siteLinks.setNote("Just Found");
                    links.add(siteLinks);
                }
                System.out.println(href);
            }


            Response response = client.target(new URI(BASE_URL + "crawler/seeds/save")).request().post(Entity.entity(links, MediaType.APPLICATION_JSON_TYPE));
            System.out.println(response);

        } catch (HttpStatusException e) {
            SiteDate crawlerModel = new SiteDate();
            crawlerModel.setLink(url);
            Response response = client.target(new URI(BASE_URL + "crawler/seeds/delete")).request().post(Entity.entity(crawlerModel, MediaType.APPLICATION_JSON_TYPE));
            System.out.println(response);
        }

        count++;
        if (count == linkCount) {
            final long startTime = System.nanoTime();
            checkAndCrawler(startTime);
        }
    }
}
