package io.egreen.cyloon.crawler.app.db.repositary;

import io.egreen.cyloon.crawler.app.model.SiteDate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.stream.Stream;

/**
 * Created by dewmal on 11/9/15.
 */
public interface SiteDataRepositary extends MongoRepository<SiteDate, String> {

    @Query("{ 'grabCount'=-1}")
    Stream<SiteDate> getFirstSiteDataForCrawlling();

}
