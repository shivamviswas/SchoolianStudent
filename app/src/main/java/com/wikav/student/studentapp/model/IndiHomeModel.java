package com.wikav.student.studentapp.model;


import java.util.Date;

import com.wikav.student.studentapp.individual.BlogPostID;

/**
 * Created by wikav-pc on 11/27/2018.
 */

public class IndiHomeModel extends BlogPostID {
    private String userId;
    private String name;
    private String post;
    private String image;
    private String thumb;
    private Date timeStamp;
    private String comments;
    private String profileImage;
    private String likes;

    public IndiHomeModel() {
    }

    public IndiHomeModel(String userId, String name, String post, String image, String thumb, Date timeStamp) {
        this.userId = userId;
        this.name = name;
        this.post = post;
        this.image = image;
        this.thumb = thumb;
        this.timeStamp = timeStamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
}
