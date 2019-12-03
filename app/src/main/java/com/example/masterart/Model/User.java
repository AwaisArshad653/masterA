package com.example.masterart.Model;

public class User {

    private String ID;
    private String Username;
    private String Fullname;
    private String ImageURL;
    private String bio;

    public User(String ID, String Username, String Fullname, String ImageURL, String bio) {
        this.ID = ID;
        this.Username = Username;
        this.Fullname = Fullname;
        this.ImageURL = ImageURL;
        this.bio = bio;
    }

    public User() {
    }

    public String getId() {
        return ID;
    }

    public void setId(String ID) {
        this.ID = ID;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String Username) {
        this.Username = Username;
    }

    public String getFullname() {
        return Fullname;
    }

    public void setFullname(String Fullname) {
        this.Fullname = Fullname;
    }

    public String getImageurl() {
        return ImageURL;
    }

    public void setImageurl(String ImageURL) {
        this.ImageURL = ImageURL;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
