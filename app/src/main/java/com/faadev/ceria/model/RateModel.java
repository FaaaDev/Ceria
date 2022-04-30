package com.faadev.ceria.model;

import com.google.gson.annotations.SerializedName;

public class RateModel {
    @SerializedName("sell_price")
    private int sellPrice;
    @SerializedName("buy_price")
    private int buyPrice;

    public int getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(int sellPrice) {
        this.sellPrice = sellPrice;
    }

    public int getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(int buyPrice) {
        this.buyPrice = buyPrice;
    }
}
