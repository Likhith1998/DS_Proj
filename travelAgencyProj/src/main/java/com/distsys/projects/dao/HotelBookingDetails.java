package com.distsys.projects.dao;

import java.io.Serializable;

public class HotelBookingDetails implements Serializable {
    private Integer user_id;
    private Integer hotelId;
    private Integer no_of_rooms;

    public HotelBookingDetails(Integer user_id,Integer hotelId,Integer no_of_rooms)
    {
        this.user_id = user_id;
        this.hotelId = hotelId;
        this.no_of_rooms = no_of_rooms;
    }

    public Integer getUser_id() {   return user_id; }
    public Integer getHotelId() {  return hotelId; }
    public Integer getNoOfRooms() { return no_of_rooms; }
}
