package io.egreen.cyloon.search.app.workers;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Created by dewmal on 11/7/15.
 */
public class IndexBuilder {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private Boolean indexBuilderStatus;

    @Autowired
    private HttpSolrClient httpSolrClient;



    public void IndexBuilder() {

    }

    public void build() throws IOException {
        TaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
        taskExecutor.execute(new ExecuteBuildIndex());
    }


    private class ExecuteBuildIndex implements Runnable {

        @Override
        public void run() {

//

            try {
//                httpSolrClient.deleteByQuery("*:*");

                if (!indexBuilderStatus) {
                    indexBuilderStatus = true;


                    BasicDBObject fields = new BasicDBObject();
                    fields.put("_id", 1);
                    fields.put("title", 1);
                    fields.put("content", 1);
                    fields.put("keywords", 1);
                    fields.put("location", 1);
                    fields.put("price", 1);
                    fields.put("postDateTime", 1);
                    DBCursor crawler_site_row_data = mongoTemplate.getCollection("crawler_site_row_data").find(new BasicDBObject(), fields);
                    int a = 0;
                    while (crawler_site_row_data.hasNext()) {
                        SolrInputDocument solrInputDocument = new SolrInputDocument();
                        DBObject dbObject = crawler_site_row_data.next();

                        Map map = dbObject.toMap();
                        for (Object key : map.keySet()) {
                            String value = map.get(key) + "";
                            System.out.println(key + " : " + value);
                            solrInputDocument.addField(key + "", value);
                        }


                        httpSolrClient.add(solrInputDocument);


                        if (a > 100) {
                            httpSolrClient.commit();
                        }

                        a++;

                    }
                    httpSolrClient.commit();
                    indexBuilderStatus = false;
                }
            } catch (SolrServerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }
}
