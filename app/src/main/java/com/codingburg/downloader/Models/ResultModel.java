package com.codingburg.downloader.Models;

public class VideoResultModel {
    private String  AudioUrl;
    private String  FileSize;
    private String  Quality;
    private String  ServiceName;
    private String  Title;
    private String  VideoUrl;
    private String  thumbnil;

    public String getAudioUrl() {
        return AudioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        AudioUrl = audioUrl;
    }

    public String getFileSize() {
        return FileSize;
    }

    public void setFileSize(String fileSize) {
        FileSize = fileSize;
    }

    public String getQuality() {
        return Quality;
    }

    public void setQuality(String quality) {
        Quality = quality;
    }

    public String getServiceName() {
        return ServiceName;
    }

    public void setServiceName(String serviceName) {
        ServiceName = serviceName;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getVideoUrl() {
        return VideoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        VideoUrl = videoUrl;
    }

    public String getThumbnil() {
        return thumbnil;
    }

    public void setThumbnil(String thumbnil) {
        this.thumbnil = thumbnil;
    }
}
