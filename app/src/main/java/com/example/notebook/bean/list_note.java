package com.example.notebook.bean;

public class list_note {
    private String notename;
    private int lid;

    public list_note(String notename,int lid) {
        this.notename = notename;
        this.lid = lid;
    }

    public String getNotename() {
        return notename;
    }

    public void setNotename(String notename) {
        this.notename = notename;
    }

    public int getLid() {
        return lid;
    }

    public void setLid(int lid) {
        this.lid = lid;
    }
}
