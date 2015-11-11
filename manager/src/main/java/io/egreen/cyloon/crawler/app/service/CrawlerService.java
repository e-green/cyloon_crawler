package io.egreen.cyloon.crawler.app.service;

import io.egreen.cyloon.crawler.app.db.repositary.SiteDataRepository;
import io.egreen.cyloon.crawler.app.model.SiteDate;
import io.egreen.cyloon.crawler.app.model.SiteLinks;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.client.solrj.request.schema.SchemaRequest;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrInputField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.stereotype.Service;

import javax.ws.rs.QueryParam;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by dewmal on 11/5/15.
 */
@Service
public class CrawlerService {


    @Qualifier("siteDataRepository")
    @Autowired
    private SiteDataRepository siteDataRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private HttpSolrClient httpSolrClient;

    public String saveSeedUrl(SiteLinks siteLink) {
        Query query = Query.query(Criteria.where("link").is(siteLink.getLink()));

        mongoTemplate.updateFirst(query, Update.update("status", "visited"), SiteLinks.class);
        mongoTemplate.updateFirst(query, Update.update("lastVisit", new Date()), SiteLinks.class);
        mongoTemplate.updateFirst(query, Update.update("visitCount", (siteLink.getVisitCount() + 1)), SiteLinks.class);


        return "DONE";
    }

    public String deleteSeedUrl(SiteLinks url) {
        Query query = Query.query(Criteria.where("link").is(url.getLink()));
        mongoTemplate.remove(query, SiteLinks.class);
        return "DONE";
    }


    public String saveSeedUrl(List<SiteLinks> urls) {
        if (urls != null)
            System.out.println(Arrays.toString(urls.toArray()));
        for (SiteLinks url : urls) {
            mongoTemplate.save(url);
        }
        return "DONE";
    }

    public List<SiteLinks> getCrawlerBulk(@QueryParam("crawlerId") String crawlerId) {
        System.out.println("working ");
        try {

//            .and("lastUpdateDate").lte(startCalender.getTime())
            Query query = Query.query(Criteria.where("status").ne("visited").and("sent"));
            query.limit(20);
            List<SiteLinks> crawlerData = mongoTemplate.find(query, SiteLinks.class);

//            System.out.println("DATA SET" + crawlerData);


            if (crawlerData == null || crawlerData.isEmpty()) {
                crawlerData = new ArrayList<>();
                crawlerData.add(new SiteLinks("http://ikman.lk/en/ads/ads-in-sri-lanka", "sent"));
                mongoTemplate.insertAll(new ArrayList<>(crawlerData));
            }

            for (SiteLinks siteLinks : crawlerData) {
                siteLinks.setStatus("sent");
                // Update status
                mongoTemplate.updateFirst(Query.query(
                        Criteria.where("url").is(siteLinks.getLink())),
                        Update.update("status", "sent"), SiteLinks.class);
            }

            return crawlerData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public String updateUrlInfo(SiteDate siteDate) {
//        mongoTemplate.save();


        try {
            System.out.println(siteDate.getLink());
            Query query = Query.query(Criteria.where("link").is(siteDate.getLink()));
            SiteDate siteDate1 = mongoTemplate.findOne(query, SiteDate.class);

            if (siteDate1 != null) {
                siteDate.setId(siteDate1.getId());
            }


            siteDate.setIndexed(true);
            siteDate.setLastIndexedTime(new Date());
            siteDataRepository.save(siteDate);
            System.out.println("Update Done...." + siteDate);
            new IndexUpdate(siteDate).run();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "DONe";
    }

    public void removeUrl(SiteDate siteDate) throws IOException, SolrServerException {
        UpdateResponse queryResponse = httpSolrClient.deleteByQuery("_id", siteDate != null ? siteDate.getId() : null);
        mongoTemplate.remove(siteDate);
    }


    private class IndexUpdate implements Runnable {

        private final SiteDate siteDate;

        private IndexUpdate(SiteDate siteDate) {
            this.siteDate = siteDate;
        }


        @Override
        public void run() {


            SolrQuery query = new SolrQuery();


            SolrInputDocument solrInputDocument = new SolrInputDocument();
            solrInputDocument.addField("title", siteDate.getTitle() + "");
            solrInputDocument.addField("content", siteDate.getContent());
            solrInputDocument.addField("keywords", siteDate.getKeywords());
            solrInputDocument.addField("location", siteDate.getLocation());
            solrInputDocument.addField("price", siteDate.getPrice());
            solrInputDocument.addField("updateTime", new Date());
            solrInputDocument.addField("sortID", new Date().getTime());
            solrInputDocument.addField("id_", siteDate.getId());

            try {


                UpdateRequest updateRequest = new UpdateRequest();
                updateRequest.add(solrInputDocument);

                updateRequest.process(httpSolrClient);


//                httpSolrClient.add(solrInputDocument);
                httpSolrClient.commit();

            } catch (SolrServerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
