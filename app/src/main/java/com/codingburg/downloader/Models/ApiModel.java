package com.codingburg.downloader.Models;

import java.util.List;

public class ApiResultModel {
    private String  ErrorLog;
    private String  HTMLData;
    private String  InternalDownload;
    private String  NeedClientExecution;
    private String  Status;
    private List<VideoResultModel> VideoResult;

    public String getErrorLog() {
        return ErrorLog;
    }

    public void setErrorLog(String errorLog) {
        ErrorLog = errorLog;
    }

    public String getHTMLData() {
        return HTMLData;
    }

    public void setHTMLData(String HTMLData) {
        this.HTMLData = HTMLData;
    }

    public String getInternalDownload() {
        return InternalDownload;
    }

    public void setInternalDownload(String internalDownload) {
        InternalDownload = internalDownload;
    }

    public String getNeedClientExecution() {
        return NeedClientExecution;
    }

    public void setNeedClientExecution(String needClientExecution) {
        NeedClientExecution = needClientExecution;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public List<VideoResultModel> getVideoResult() {
        return VideoResult;
    }

    public void setVideoResult(List<VideoResultModel> videoResult) {
        VideoResult = videoResult;
    }
}
