package io.egreen.cyloon.crawler.app.rest;

import io.egreen.cyloon.crawler.app.crawler.CrawlerServiceApi;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

/**
 * Created by dewmal on 11/5/15.
 */
@Path("/admin")
public class AdminService {

//    @Autowired
//    private CrawlerJob crawlerJob;


    @Autowired
    private CrawlerServiceApi crawlerServiceApi;


    @Path("/crawler/start")
    @GET
    public String start() {

        try {
            crawlerServiceApi.start();
        } catch (Exception e) {
            return e.getMessage();
        }
//        crawlerJob.doRun();
        return "DONE";
    }


    @Path("/crawler/seed/save")
    @GET
    public String saveSeed(@QueryParam("seed") String seed) {

        try {
            crawlerServiceApi.addSeeds(seed);
        } catch (Exception e) {
            return e.getMessage();
        }
//        crawlerJob.doRun();
        return "DONE";
    }
}
