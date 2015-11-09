package io.egreen.cyloon.crawler.app.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dewmal on 10/13/15.
 */
@Document(collection = "crawler_site_row_data")
public class SiteDate {

    @Id
    private String id;
    @Indexed
    private String title;
    @Indexed
    private String content;
    @Indexed
    private String keywords;
    private List<String> images;
    private double price;
    private String curruncy;
    @Indexed
    private String location;
    @Indexed
    private String ownerName;
    private List<String> tpNumbers;
    @Indexed
    private String email;
    private Date postDateTime;
    @Field("postdate")
    @Indexed()
    private String postDateTimeS;
    @Field("link")
    @Indexed(unique = true)
    private String link;


    private boolean grabbed;
    private int grabCount;
    private boolean indexed;
    private Date lastIndexedTime;

    public Date getLastIndexedTime() {
        return lastIndexedTime;
    }

    public void setLastIndexedTime(Date lastIndexedTime) {
        this.lastIndexedTime = lastIndexedTime;
    }

    public boolean isIndexed() {
        return indexed;
    }

    public void setIndexed(boolean indexed) {
        this.indexed = indexed;
    }

    public boolean isGrabbed() {
        return grabbed;
    }

    public void setGrabbed(boolean grabbed) {
        this.grabbed = grabbed;
    }

    public int getGrabCount() {
        return grabCount;
    }

    public void setGrabCount(int grabCount) {
        this.grabCount = grabCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCurruncy() {
        return curruncy;
    }

    public void setCurruncy(String curruncy) {
        this.curruncy = curruncy;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public List<String> getTpNumbers() {
        return tpNumbers;
    }

    public void setTpNumbers(List<String> tpNumbers) {
        this.tpNumbers = tpNumbers;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getPostDateTime() {
        return postDateTime;
    }

    public void setPostDateTime(Date postDateTime) {
        this.postDateTime = postDateTime;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPostDateTimeS() {
        return postDateTimeS;
    }

    public void setPostDateTimeS(String postDateTimeS) {
        this.postDateTimeS = postDateTimeS;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "SiteDate{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", keywords='" + keywords + '\'' +
                ", images=" + images +
                ", price=" + price +
                ", curruncy='" + curruncy + '\'' +
                ", location='" + location + '\'' +
                ", ownerName='" + ownerName + '\'' +
                ", tpNumbers=" + tpNumbers +
                ", email='" + email + '\'' +
                ", postDateTime=" + postDateTime +
                ", link='" + link + '\'' +
                '}';
    }
}
