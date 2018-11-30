package com.abacus.android.model;

import java.util.UUID;

public class Bookmark {

    private String id;
    private String value;
    private String time;
    private boolean latex;

    public Bookmark() {};

    public Bookmark(String value, String time, boolean latex) {
        this.id = UUID.randomUUID().toString();
        this.value = value;
        this.time = time;
        this.latex = latex;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isLatex() {
        return latex;
    }

}
