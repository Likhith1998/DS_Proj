package com.distsys.projects.dao;

import java.io.Serializable;

public class HotelResultInfo implements Serializable {

    private Integer hotelId;
    private String hotelName;
    private String place;
    private Integer price;

    public HotelResultInfo(Integer hotelId, String hotelName, String place, Integer price) {
        this.hotelId = hotelId;
        this.hotelName = hotelName;
        this.place = place;
        this.price = price;
    }

    public Integer getHotelId() {
        return hotelId;
    }

    public void setHotelId(Integer hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
