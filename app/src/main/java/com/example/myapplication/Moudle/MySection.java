package com.example.myapplication.Moudle;

import com.chad.library.adapter.base.entity.SectionEntity;

import org.jetbrains.annotations.NotNull;

public class MySection extends SectionEntity {
    private boolean isHeader;
    private String title;
    private String content;

//    public MySection(boolean isHeader, String header) {
//        super(isHeader, header);
//    }

    public MySection(boolean b, @NotNull String title, @NotNull String content) {
        super(b, title);
        this.isHeader = isHeader;
        this.title = title;
        this.content = content;
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