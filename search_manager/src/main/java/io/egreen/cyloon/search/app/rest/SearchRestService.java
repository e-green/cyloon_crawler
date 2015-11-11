package io.egreen.cyloon.search.app.rest;

import io.egreen.cyloon.search.app.model.SiteDate;
import io.egreen.cyloon.search.app.service.SearchService;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

/**
 * Created by dewmal on 10/13/15.
 */

@Path("search")
public class SearchRestService {

    @Autowired
    private SearchService searchService;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String index() {
//        System.out.println(siteDate)
// ;
//        System.out.println(dbApiService.getDB().getName());

        return "HELLO";//dbApiService.getName("Hello");
    }


    @Path("/search")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public SolrDocumentList getREsult(@QueryParam("query") String query,
//                                    @QueryParam("filterQuery") String filterQuery,
//                                    @QueryParam("fields") @DefaultValue("title,content,location,keywords") String fields,
                                      @QueryParam("start") @DefaultValue(value = "0") int start,
                                      @QueryParam("limit") @DefaultValue(value = "10") int limit
    ) {

        System.out.println(searchService);

        try {

//            if (filterQuery == null) {
//                fields = "";
//            }

            return searchService.find(query, start, limit);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
