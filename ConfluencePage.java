package com.bolt.slack.entities;

public class ConfluencePage {
    private String pageId;
    private String title;
    private String rawHtml;
    private String serviceName;
    private String endpointURL;
    private String jenkinsJob;
    private String githubURL;
    private String confluenceLinks;
    private String ktRecordings;
    private String swaggerLink; //TODO getter and setter

    //TODO create constructor for pageID, title, and rawHTML
    public ConfluencePage() {}

    public ConfluencePage(String title,String serviceName, String endpointURL, String jenkinsJob, String githubURL, String confluenceLinks, String ktRecordings) {
        this.title = title;
        this.serviceName = serviceName;
        this.endpointURL = endpointURL;
        this.jenkinsJob = jenkinsJob;
        this.githubURL = githubURL;
        this.confluenceLinks = confluenceLinks;
        this.ktRecordings = ktRecordings;
    }

    public String getPageId() {
        return pageId;
    }

    public String getTitle() {
        return title;
    }

    public String getRawHtml() {
        return rawHtml;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getEndpointURL() {
        return endpointURL;
    }

    public String getJenkinsJob() {
        return jenkinsJob;
    }

    public String getGithubURL() {
        return githubURL;
    }

    public String getConfluenceLinks() {
        return confluenceLinks;
    }

    public String getKtRecordings() {
        return ktRecordings;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRawHtml(String rawHtml) {
        this.rawHtml = rawHtml;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setEndpointURL(String endpointURL) {
        this.endpointURL = endpointURL;
    }

    public void setJenkinsJob(String jenkinsJob) {
        this.jenkinsJob = jenkinsJob;
    }

    public void setGithubURL(String githubURL) {
        this.githubURL = githubURL;
    }

    public void setConfluenceLinks(String confluenceLinks) {
        this.confluenceLinks = confluenceLinks;
    }

    public void setKtRecordings(String ktRecordings) {
        this.ktRecordings = ktRecordings;
    }

    //TODO

    @Override
    public String toString() {
        return "{" +
                "\"pageId=\":\"" + pageId + '"' +
                ",\"title=\":\"" + title + '"' +
                ",\"rawHtml\":\"" + rawHtml + '"' +
                ",\"serviceName\":\"" + serviceName + '"' +
                ",\"endpointURL\":\"" + endpointURL + '"' +
                ",\"jenkinsJob\":\"" + jenkinsJob + '"' +
                ",\"githubURL\":\"" + githubURL + '"' +
                ",\"confluenceLinks\":\"" + confluenceLinks + '"' +
                ",\"ktRecordings\":\"" + ktRecordings + '"' +
                '}';
    }
}
