package com.faadev.ceria.model;

import com.google.gson.annotations.SerializedName;

public class BankModel {
    @SerializedName("id")
    private int id;
    @SerializedName("bank_name")
    private String bankName;
    @SerializedName("bank_number")
    private String noRek;
    @SerializedName("bank_alias")
    private String alias;
    @SerializedName("bank_logo")
    private String image;
    private boolean selected = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getNoRek() {
        return noRek;
    }

    public void setNoRek(String noRek) {
        this.noRek = noRek;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
