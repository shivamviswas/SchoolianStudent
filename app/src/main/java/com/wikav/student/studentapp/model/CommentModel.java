package com.wikav.student.studentapp.model;

import java.util.Date;
import com.google.firebase.firestore.ServerTimestamp;

/**
 * Created by wikav-pc on 11/28/2018.
 */

public class CommentModel {

    private String user_id;
    private String comment;
    private Date timeStamp;

    public CommentModel() {
    }

    public CommentModel(String user_id, String comment, Date timeStamp) {
        this.user_id = user_id;
        this.comment = comment;
        this.timeStamp = timeStamp;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
}
