package com.example.sultan.newsapp;

public class NewsCard {

    private String imgUrl, articleTitle, articleDescription,
            websiteURL, source;

    public NewsCard(String imgURL, String title, String desc, String web, String articleSource) {
        imgUrl = imgURL;
        articleTitle = title;
        articleDescription = desc;
        websiteURL = web;
        source = articleSource;
    }

    public void setUrl(String url) {
        imgUrl = url;
    }

    public String getUrl() {
        return imgUrl;
    }

    public void setTitle(String title) {
        articleTitle = title;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setDescription(String d) {
        articleDescription = d;
    }

    public String getDescription() {
        return articleDescription;
    }

    public void setWebsite(String url) {
        websiteURL = url;
    }

    public String getWebsite() {
        return websiteURL;
    }

    public void setSource(String s) {
        source = s;
    }

    public String getSource() {
        return source;
    }
}
