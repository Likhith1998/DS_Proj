package com.distsys.projects.dao;

import java.util.ArrayList;
import java.util.List;

public class SearchResult {

    private List<FlightInfo> flightInfoList =  new ArrayList<>();
    private List<HotelInfo> hotelInfoList = new ArrayList<>();

    public SearchResult(List<FlightInfo> flightInfoList, List<HotelInfo> hotelInfoList) {
        this.flightInfoList = flightInfoList;
        this.hotelInfoList = hotelInfoList;
    }

    public List<FlightInfo> getFlightInfoList() {
        return flightInfoList;
    }

    public void setFlightInfoList(List<FlightInfo> flightInfoList) {
        this.flightInfoList = flightInfoList;
    }

    public List<HotelInfo> getHotelInfoList() {
        return hotelInfoList;
    }

    public void setHotelInfoList(List<HotelInfo> hotelInfoList) {
        this.hotelInfoList = hotelInfoList;
    }
}
