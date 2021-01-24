package com.codingburg.downloader.Models;

import java.util.List;

public class SuppotedSites {

    private String Name;
    private String ServiceUrl;
    private List<String> TestUrls;
    private String MobileSupport;

    public String getMobileSupport() {
        return MobileSupport;
    }

    public void setMobileSupport(String mobileSupport) {
        MobileSupport = mobileSupport;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getServiceUrl() {
        return ServiceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        ServiceUrl = serviceUrl;
    }

    public List<String> getTestUrls() {
        return TestUrls;
    }

    public void setTestUrls(List<String> testUrls) {
        TestUrls = testUrls;
    }



}
