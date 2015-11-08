package io.egreen.cyloon.crawler.app.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by dewmal on 11/7/15.
 */
@Document(collection = "system_seed")
public class SeedUrl {


    private String name;

    @Indexed(unique = true)
    private String url;
    private String type;
    private boolean status;

    private String savePattern;
    private String shouldVisitPattern;


    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String getSavePattern() {
        return savePattern;
    }

    public void setSavePattern(String savePattern) {
        this.savePattern = savePattern;
    }

    public String getShouldVisitPattern() {
        return shouldVisitPattern;
    }

    public void setShouldVisitPattern(String shouldVisitPattern) {
        this.shouldVisitPattern = shouldVisitPattern;
    }

    @Override
    public String toString() {
        return "SeedUrl{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", type='" + type + '\'' +
                ", status=" + status +
                '}';
    }
}
