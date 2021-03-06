package com.faadev.ceria.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("email")
    private String email;
    @SerializedName("name")
    private String name;
    @SerializedName("previlege")
    private String role;
    @SerializedName("image")
    private String image;
    @SerializedName("followed")
    private boolean followed;
    @SerializedName("monetized")
    private boolean monetized;
    @SerializedName("followers")
    private int followers;
    @SerializedName("posts")
    private List<Post> post;

    public User(int id, String email, String name, String role, String image, boolean monetized) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.role = role;
        this.image = image;
        this.monetized = monetized;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isMonetized() {
        return monetized;
    }

    public void setMonetized(boolean monetized) {
        this.monetized = monetized;
    }

    public List<Post> getPost() {
        return post;
    }

    public void setPost(List<Post> post) {
        this.post = post;
    }

    public boolean isFollowed() {
        return followed;
    }

    public void setFollowed(boolean followed) {
        this.followed = followed;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }
}
