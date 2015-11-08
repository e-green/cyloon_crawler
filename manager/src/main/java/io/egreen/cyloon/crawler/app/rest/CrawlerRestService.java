package io.egreen.cyloon.crawler.app.rest;

import io.egreen.cyloon.crawler.app.model.SiteDate;
import io.egreen.cyloon.crawler.app.model.SiteLinks;
import io.egreen.cyloon.crawler.app.service.CrawlerService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by dewmal on 10/13/15.
 */

@Path("crawler")
public class CrawlerRestService {


    @Autowired
    private CrawlerService crawlerService;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String index() {
//        System.out.println(siteDate)
// ;
//        System.out.println(dbApiService.getDB().getName());

        return "HELLO";//dbApiService.getName("Hello");
    }

    @POST
    @Path("seeds/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String saveSeedUrl(SiteLinks siteLink) {
        return crawlerService.saveSeedUrl(siteLink);
    }


    @POST
    @Path("seeds/delete")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String deleteSeedUrl(SiteLinks url) {
        return crawlerService.deleteSeedUrl(url);
    }


    @POST
    @Path("seeds/save")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String saveSeedUrl(List<SiteLinks> urls) {
        return crawlerService.saveSeedUrl(urls);
    }


    /**
     * GET NEXT AVB Site urls for grabber
     *
     * @param crawlerId
     * @return
     */
    @Path("nextbulk")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<SiteLinks> getCrawlerBulk(@QueryParam("crawlerId") String crawlerId) {
        return crawlerService.getCrawlerBulk(crawlerId);
    }


    @POST
    @Path("save")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String updateUrlInfo(SiteDate siteDate) {
        return crawlerService.updateUrlInfo(siteDate);
    }
}
