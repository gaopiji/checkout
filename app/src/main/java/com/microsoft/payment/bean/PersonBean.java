package com.microsoft.payment.bean;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * Created by v-pigao on 3/2/2018.
 */

public class PersonBean implements Serializable {
    public static final String CUSTOMER = "Customer";
    public static final String MANAGER = "Manager";
    public static final String VIP = "VIP";

    private String id;
    private String name;
    private String photo_url;
    private String person_type  ;
    private List<ProductBean> goodslist;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public String getPerson_type() {
        return person_type;
    }

    public void setPerson_type(String person_type) {
        this.person_type = person_type;
    }

    public List<ProductBean> getGoodslist() {
        return goodslist;
    }

    public void setGoodslist(List<ProductBean> goodslist) {
        this.goodslist = goodslist;
    }

    @Override
    public String toString() {
        return "PersonBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", photo_url='" + photo_url + '\'' +
                ", person_type='" + person_type + '\'' +
                ", goodslist=" + goodslist +
                '}';
    }
}
