package com.faadev.ceria.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MyPost implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("user_id")
    private int userId;
    @SerializedName("category_id")
    private CategoryModel category;
    @SerializedName("tittle")
    private String tittle;
    @SerializedName("article")
    private String article;
    @SerializedName("ilustration")
    private String illustration;
    @SerializedName("approve")
    private int approve;
    @SerializedName("monetized")
    private boolean monetized;
    @SerializedName("total_reward")
    private int totalReward;
    @SerializedName("likes")
    private int likes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public CategoryModel getCategory() {
        return category;
    }

    public void setCategory(CategoryModel category) {
        this.category = category;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getIllustration() {
        return illustration;
    }

    public void setIllustration(String illustration) {
        this.illustration = illustration;
    }

    public int getApprove() {
        return approve;
    }

    public void setApprove(int approve) {
        this.approve = approve;
    }

    public boolean isMonetized() {
        return monetized;
    }

    public void setMonetized(boolean monetized) {
        this.monetized = monetized;
    }

    public int getTotalReward() {
        return totalReward;
    }

    public void setTotalReward(int totalReward) {
        this.totalReward = totalReward;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
}
