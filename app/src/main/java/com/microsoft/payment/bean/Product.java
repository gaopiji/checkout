package com.microsoft.payment.bean;

/**
 * Created by v-pigao on 3/7/2018.
 */

public class Product {
    public String goodsId;
    public String name;
    public double price;
    public int productDra;

    public Product(){}
    public Product(String goodsId ,String name,int price,int productDra){
        this.goodsId = goodsId;
        this.name = name;
        this.price = price;
        this.productDra = productDra;
    }

    @Override
    public String toString() {
        return "Product{" +
                "goodsId='" + goodsId + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", productDra=" + productDra +
                '}';
    }
}
