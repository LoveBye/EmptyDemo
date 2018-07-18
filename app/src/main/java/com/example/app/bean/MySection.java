package com.example.app.bean;

import com.chad.library.adapter.base.entity.SectionEntity;

import org.jetbrains.annotations.NotNull;

public class MySection extends SectionEntity {
    private int id;
    private int icon;
    private String title;
    private String content;

    public MySection(int id, @NotNull int icon, @NotNull String title, @NotNull String content) {
        super(false, title);
        this.id = id;
        this.icon = icon;
        this.title = title;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setI(int id) {
        this.id = id;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public MySection(String title) {
        super(title);
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
}