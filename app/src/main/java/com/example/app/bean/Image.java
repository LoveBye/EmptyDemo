package com.example.app.bean;

/**
 * 图片实体类
 */
public class Image {

    private String path;
    private long time;
    private String name;

    public Image(String path, long time, String name) {
        this.path = path;
        this.time = time;
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}