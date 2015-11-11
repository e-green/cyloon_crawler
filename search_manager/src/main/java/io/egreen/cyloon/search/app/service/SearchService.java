package io.egreen.cyloon.search.app.service;

import io.egreen.cyloon.search.app.model.SiteDate;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by dewmal on 11/7/15.
 */
@Service
public class SearchService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private HttpSolrClient httpSolrClient;


    /**
     * @param querString
     * @param filterQuery
     * @param feilds
     * @param start
     * @param limit
     * @return
     * @throws IOException
     * @throws SolrServerException String filterQuery[], String feilds[],
     */
    public SolrDocumentList find(String querString, int start, int limit) throws IOException, SolrServerException {

        SolrQuery query = new SolrQuery();
        query.setQuery(querString);

        query.setStart(start);
        query.setRows(limit);


        QueryResponse response = httpSolrClient.query(query);
        SolrDocumentList results = response.getResults();

        return results;

    }
}
