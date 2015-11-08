package io.egreen.cyloon.crawler.app.process;


import io.egreen.cyloon.crawler.app.data.SiteDate;
import org.jsoup.HttpStatusException;
import org.jsoup.nodes.Document;

/**
 * Created by dewmal on 10/13/15.
 */
public interface PagePaserProcess {


    SiteDate getCrawlerModel(Document doc) throws HttpStatusException;

}
