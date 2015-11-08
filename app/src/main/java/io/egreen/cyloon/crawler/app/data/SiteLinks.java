package io.egreen.cyloon.crawler.app.data;

import java.util.Date;

/**
 * Created by dewmal on 10/22/15.
 */

public class SiteLinks {

    private String link;
    private String note;
    private long visitCount;
    private Date lastVisit;
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
}
