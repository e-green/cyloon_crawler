package io.egreen.cyloon.crawler.app.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by dewmal on 11/8/15.
 */
@Document(collection = "system_seed_policy")
public class SeedPolicy {

    @Indexed(unique = true)
    private String name;
    private String acceptPolicy;
    private String rejectPolicy;
    private String paserPolicy;


    public String getPaserPolicy() {
        return paserPolicy;
    }

    public void setPaserPolicy(String paserPolicy) {
        this.paserPolicy = paserPolicy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAcceptPolicy() {
        return acceptPolicy;
    }

    public void setAcceptPolicy(String acceptPolicy) {
        this.acceptPolicy = acceptPolicy;
    }

    public String getRejectPolicy() {
        return rejectPolicy;
    }

    public void setRejectPolicy(String rejectPolicy) {
        this.rejectPolicy = rejectPolicy;
    }

    @Override
    public String toString() {
        return "SeedPolicy{" +
                "name='" + name + '\'' +
                ", acceptPolicy='" + acceptPolicy + '\'' +
                ", rejectPolicy='" + rejectPolicy + '\'' +
                '}';
    }
}
