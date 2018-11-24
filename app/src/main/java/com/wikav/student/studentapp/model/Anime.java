package com.wikav.student.studentapp.model;

import java.io.Serializable;

/**
 * Created by Aws on 11/03/2018.
 */

public class Anime implements Serializable {

    private String name ;
    private String Description;
    private int rating;
    private String id;
    private String categorie;
    private String time;
    private String studio ;
    private String sid ;
    private String image_url;
    private String pageId;
    private String comment;

    private int imageStar;

    public Anime() {

    }

    public Anime(String name, String description, String rating, String nb_episode, String categorie, String studio, String image_url,String sid) {
        this.name = name;
        Description = description;
        this.rating = Integer.parseInt(rating);
        this.id = nb_episode;
        this.categorie = categorie;
        this.studio = studio;
        this.sid = sid;
        this.image_url = image_url;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {

        if(!(obj instanceof Anime))
            return false;
        return this.hashCode() == obj.hashCode();
    }

    public int getImageStar()
    {
        return imageStar;
    }
    public void setImageStar(int imageStar)
    {
        this.imageStar=imageStar;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return Description;
    }
    public String getTime() {
        return time;
    }

    public int getStar() {
        return rating;
    }

    public String getIdd() {
        return id;
    }

    public String getPosts() {
        return categorie;
    }

    public String getProfilePic() {
        return studio;
    }
    public String getSId() {
        return sid;
    }

    public String getImage_url() {
        return image_url;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setTime(String description) {
        time = description;
    }

    public void setStars(String rating) {
        this.rating = Integer.parseInt(rating);
    }

    public void setPostId(String nb_episode) {
        this.id = nb_episode;
    }

    public void setSId(String nb_episode) {
        this.sid = nb_episode;
    }

    public void setPosts(String categorie) {
        this.categorie = categorie;
    }

    public void setProfilePic(String studio) {
        this.studio = studio;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
