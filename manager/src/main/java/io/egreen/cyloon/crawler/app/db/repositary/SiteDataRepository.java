package io.egreen.cyloon.crawler.app.db.repositary;

import io.egreen.cyloon.crawler.app.model.SiteDate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

/**
 * Created by dewmal on 11/9/15.
 */
@Repository
public interface SiteDataRepository extends MongoRepository<SiteDate, String> {


}
