package io.egreen.cyloon.crawler.app.process.impl;

import io.egreen.cyloon.crawler.app.data.SiteDate;
import io.egreen.cyloon.crawler.app.process.PagePaserProcess;
import org.jsoup.HttpStatusException;
import org.jsoup.nodes.Document;

/**
 * Created by dewmal on 10/22/15.
 */
public class IkmanLKCategoryPageProcessor implements PagePaserProcess {
    @Override
    public SiteDate getCrawlerModel(Document doc) throws HttpStatusException {
        return null;
    }
}
