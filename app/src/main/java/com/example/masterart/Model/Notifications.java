package com.example.masterart.Model;

public class Notifications {

    private String userid;
    private String text;
    private String Video_id;
    private boolean ispost;

    public Notifications(String userid, String text, String Video_id, boolean ispost) {
        this.userid = userid;
        this.text = text;
        this.Video_id = Video_id;
        this.ispost = ispost;
    }

    public Notifications() {
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getVideo_id() {
        return Video_id;
    }

    public void setPostid(String postid) {
        this.Video_id = Video_id;
    }

    public boolean isIspost() {
        return ispost;
    }

    public void setIspost(boolean ispost) {
        this.ispost = ispost;
    }
}
