package io.egreen.cyloon.crawler.app.jobs;

import io.egreen.cyloon.crawler.app.crawler.dep.CrawlerController;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by dewmal on 11/5/15.
 */
//@CronTrigger(cron = "0/20 * * * * ?")
//@SimpleTrigger(repeatInterval = 1, repeatCount = 10, timeUnit = TimeUnit.SECONDS, isConcurrencyAllowed = true)


public class CrawlerJob {

    @Autowired
    private CrawlerController crawlerController;

    public void doRun() {
        try {
            crawlerController.Start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
