package io.egreen.cyloon.crawler.app.crawler.process;

import io.egreen.cyloon.crawler.app.model.SiteDate;
import org.jsoup.HttpStatusException;
import org.jsoup.nodes.Document;

/**
 * Created by dewmal on 11/7/15.
 */
public interface ProcessPages {


    boolean accepts(String pattern);

    SiteDate getCrawlerModel(Document document) throws HttpStatusException;


}
