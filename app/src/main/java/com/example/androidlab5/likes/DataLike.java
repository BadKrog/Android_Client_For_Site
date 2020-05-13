package com.example.androidlab5.likes;

import java.util.LinkedList;

public class DataLike {
    private static DataLike instance;
    private LinkedList<String> urls;
    private DataLike() {
        urls = new LinkedList<>();
    }
    public static DataLike createInstance() {
        if(instance == null) {
            instance = new DataLike();
        }
        return instance;
    }
    public void addUrl(String url) {
        if(urls.size() == 10) {
            urls.pop();
        }
        urls.add(url);
    }
    public void deleteUrl(String url) {
        urls.remove(url);
    }
    public LinkedList<String> getUrls() {
        return urls;
    }
}
