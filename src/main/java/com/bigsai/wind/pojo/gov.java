package com.bigsai.wind.pojo;

import java.io.Serializable;
import java.util.Date;

public class gov implements Serializable {
    private int id;
    private String caseNumber;
    private String title;
    private  String content;
    private String url;
    private Date releaseTime;
    private Date operationTime;

    @Override
    public String toString() {
        return "gov{" +
                "id=" + id +
                ", caseNumber='" + caseNumber + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", releaseTime=" + releaseTime +
                ", url='" + url + '\'' +
                ", operationTime=" + operationTime +
                '}';
    }

    public gov(int id,String caseNumber, String title, String content,  String url,Date releaseTime, Date operationTime) {
        this.id=id;
        this.caseNumber = caseNumber;
        this.title = title;
        this.content = content;
        this.releaseTime = releaseTime;
        this.url = url;
        this.operationTime = operationTime;
    }
    public gov(String caseNumber, String title, String content,  String url,Date releaseTime, Date operationTime) {
        this.caseNumber = caseNumber;
        this.title = title;
        this.content = content;
        this.releaseTime = releaseTime;
        this.url = url;
        this.operationTime = operationTime;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
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

    public Date getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(Date operationTime) {
        this.operationTime = operationTime;
    }
}
