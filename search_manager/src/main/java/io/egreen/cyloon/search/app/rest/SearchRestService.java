package io.egreen.cyloon.search.app.rest;

import io.egreen.cyloon.search.app.model.SiteDate;
import io.egreen.cyloon.search.app.service.SearchService;
import org.apache.lucene.queryparser.classic.ParseException;
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


    @Path("/index")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String indexingData() {
        System.out.println("ASDASD");
        try {
            searchService.index();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "HELLO";//dbApiService.getName("Hello");
    }

    @Path("/search")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<SiteDate> getREsult(@QueryParam("query") String query) {
        try {
            return searchService.find(query);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
