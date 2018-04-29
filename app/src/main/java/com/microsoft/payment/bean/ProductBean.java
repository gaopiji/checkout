package com.microsoft.payment.bean;

import java.io.Serializable;

/**
 * Created by v-pigao on 2/28/2018.
 */

public class ProductBean implements Serializable{
    String GoodsId;
    String GoodsName;
    int LikedTimes;
    double GoodsPrice;


    public String getGoodsId() {
        return GoodsId;
    }

    public void setGoodsId(String goodsId) {
        this.GoodsId = goodsId;
    }

    public String getName() {
        return GoodsName;
    }

    public void setName(String name) {
        this.GoodsName = name;
    }

    public int getTimes() {
        return LikedTimes;
    }

    public void setTimes(int times) {
        this.LikedTimes = times;
    }

    public double getPrice() {
        return GoodsPrice;
    }

    public void setPrice(double price) {
        this.GoodsPrice = price;
    }

    @Override
    public String toString() {
        return "ProductBean{" +
                "goodsId='" + GoodsId + '\'' +
                ", name='" + GoodsName + '\'' +
                ", times='" + LikedTimes + '\'' +
                ", price=" + GoodsPrice +
                '}';
    }
}
