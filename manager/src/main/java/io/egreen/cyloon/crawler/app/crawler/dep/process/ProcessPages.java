package io.egreen.cyloon.crawler.app.crawler.dep.process;

import io.egreen.cyloon.crawler.app.model.SiteDate;
import org.jsoup.HttpStatusException;
import org.jsoup.nodes.Document;

/**
 * Created by dewmal on 11/7/15.
 */
public interface ProcessPages {


    String[] accepts();

    SiteDate getCrawlerModel(Document document) throws HttpStatusException;





}
