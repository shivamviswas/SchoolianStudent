package com.wikav.student.studentapp.individual;

/**
 * Created by wikav-pc on 11/28/2018.
 */

import android.support.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class BlogPostID {

    @Exclude
    public String BlogPostId;

    public <T extends BlogPostID> T withId(@NonNull final String id) {
        this.BlogPostId = id;
        return (T) this;
    }

}
