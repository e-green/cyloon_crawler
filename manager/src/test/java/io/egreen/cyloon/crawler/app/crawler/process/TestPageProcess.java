package io.egreen.cyloon.crawler.app.crawler.process;

import io.egreen.cyloon.crawler.app.crawler.process.impl.RiayaSewanaProcessor;
import io.egreen.cyloon.crawler.app.model.SiteDate;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by dewmal on 11/9/15.
 */
public class TestPageProcess {

    public static void main(String[] args) throws IOException {




        Document document = Jsoup.connect("http://riyasewana.com/buy/engines-engine-parts/vgr-caravan-sale-kandy-415554").get();

        ProcessPages processPages = new RiayaSewanaProcessor();
        SiteDate siteDate = processPages.getCrawlerModel(document);

        System.out.println(siteDate);

    }
}
