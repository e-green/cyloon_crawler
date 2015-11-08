package io.egreen.cyloon.crawler.app.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * Created by dewmal on 10/22/15.
 */

@Document(collection = "crawler_site_links")
public class SiteLinks {

    @Field("link")
    @Indexed(unique = true)
    private String link;
    @Indexed
    private String note;

    @Indexed
    private long visitCount;
    @Indexed
    private Date lastVisit;

    @Indexed
    private String status;

    public SiteLinks() {

    }

    public SiteLinks(String link, String status) {
        this.link = link;
        this.status = status;
    }

    public long getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(long visitCount) {
        this.visitCount = visitCount;
    }

    public Date getLastVisit() {
        return lastVisit;
    }

    public void setLastVisit(Date lastVisit) {
        this.lastVisit = lastVisit;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    @Override
    public String toString() {
        return "SiteLinks{" +
                "link='" + link + '\'' +
                ", note='" + note + '\'' +
                ", visitCount=" + visitCount +
                ", lastVisit=" + lastVisit +
                ", status='" + status + '\'' +
                '}';
    }
}
