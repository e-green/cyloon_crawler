package io.egreen.cyloon.crawler.app.index;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import io.egreen.cyloon.crawler.app.crawler.process.ProcessPages;
import io.egreen.cyloon.crawler.app.crawler.process.impl.IkmanLkProcessor;
import io.egreen.cyloon.crawler.app.crawler.process.impl.RiayaSewanaProcessor;
import io.egreen.cyloon.crawler.app.model.SiteDate;
import io.egreen.cyloon.crawler.app.service.CrawlerService;
import org.apache.http.HttpStatus;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by dewmal on 11/9/15.
 */
@Component
public class IndexJob {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private CrawlerService crawlerService;


//    @Scheduled(cron = "*/10 * * * * *")
    public void grabData() {
//        System.out.println("Working ");
        List<SiteDate> siteDates = new ArrayList<>();

        DBObject queryObject = new BasicDBObject();
        queryObject.put("grabCount", -1);
        queryObject.put("grabbed", true);
        Query query = new BasicQuery(queryObject); //Query.query(Criteria.where("grabCount").is(-1));
        query.limit(50);

        siteDates.addAll(mongoTemplate.find(query, SiteDate.class));

        if (siteDates.size() < 50) {
            queryObject = new BasicDBObject();
            queryObject.put("grabbed", false);
            query = new BasicQuery(queryObject); //Query.query(Criteria.where("grabCount").is(-1));
            query.limit(50);
            siteDates.addAll(mongoTemplate.find(query, SiteDate.class));
        }


//        System.out.println("  " + Arrays.toString(siteDates.toArray()));
        for (SiteDate siteDate : siteDates) {
            Query querySite = Query.query(Criteria.where("link").is(siteDate.getLink()));
//            mongoTemplate.updateFirst(querySite, Update.update("grabbed", true), SiteDate.class);
        }
        new Indexing(siteDates).run();

//        taskExecutor.execute();
    }

    /**
     * Indexing And Grabbing data
     */
    private class Indexing implements Runnable {

        private final List<SiteDate> siteDates;

        private ProcessPages processPages;

        private final Map<String, ProcessPages> htmlPagesMap = new ConcurrentHashMap<>();

        private Indexing(List<SiteDate> siteDates) {
            this.siteDates = siteDates;
        }

        private ProcessPages getSeedPolicy(String name) {
            ProcessPages processPages[] = new ProcessPages[]{new IkmanLkProcessor(), new RiayaSewanaProcessor()};

            for (ProcessPages processPage : processPages) {
                if (processPage.accepts(name)) {
                    return processPage;
                }
            }
            return null;
        }

        @Override
        public void run() {

            if (siteDates != null && !siteDates.isEmpty()) {
                System.out.println(siteDates.size());

                for (SiteDate siteDate : siteDates) {
                    System.out.println(siteDate);
                    ProcessPages processPages = getSeedPolicy(siteDate.getLink());
                    System.out.println(processPages);
                    if (processPages != null) {
                        try {
                            System.out.println("Wirjubg ");
                            Document document = Jsoup.connect(siteDate.getLink()).timeout(1000).get();
                            System.out.println(document);
                            SiteDate model = processPages.getCrawlerModel(document);
                            model.setGrabbed(false);
                            model.setGrabCount((model.getGrabCount() + 1));
                            model.setLastIndexedTime(new Date());


                            System.out.println(" Model Doc" + model);

//                        crawlerService.updateUrlInfo(model);

                        } catch (HttpStatusException e) {

                            if (e.getStatusCode() == 403 || e.getStatusCode() == 404) {
                                try {
                                    crawlerService.removeUrl(siteDate);
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                } catch (SolrServerException e1) {
                                    e1.printStackTrace();
                                }
                            }

                            System.out.println(e.toString());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
