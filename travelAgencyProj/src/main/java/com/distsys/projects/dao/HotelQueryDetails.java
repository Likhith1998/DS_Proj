package com.distsys.projects.dao;

import java.io.Serializable;
import java.sql.Date;

public class HotelQueryDetails implements Serializable {

    private String city;
    private Date fromDate;
    private Date toDate;
    private Integer no_of_rooms;

    public HotelQueryDetails(String city, Date fromDate, Date toDate, Integer no_of_rooms) {
        this.city = city;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.no_of_rooms = no_of_rooms;
    }

    public String getCity() {
        return city;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public Integer getNo_of_rooms() {
        return no_of_rooms;
    }

    @Override
    public String toString() {
        return "HotelQueryDetails{" +
                "city='" + city + '\'' +
                ", fromDate=" + fromDate +
                ", toDate=" + toDate +
                ", no_of_rooms=" + no_of_rooms +
                '}';
    }

}
