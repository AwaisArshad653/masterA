package com.example.masterart.Model;

public class Post {
    private String Video_id;
    private String Video;
    private String Video_uploader;

    public Post(String video_id, String video, String video_uploader) {
        Video_id = video_id;
        Video = video;
        Video_uploader = video_uploader;
    }

    public Post() {
    }

    public String getVideo_id() {
        return Video_id;
    }

    public void setVideo_id(String video_id) {
        Video_id = video_id;
    }

    public String getVideo() {
        return Video;
    }

    public void setVideo(String video) {
        Video = video;
    }

    public String getVideo_uploader() {
        return Video_uploader;
    }

    public void setVideo_uploader(String video_uploader) {
        Video_uploader = video_uploader;
    }
}
