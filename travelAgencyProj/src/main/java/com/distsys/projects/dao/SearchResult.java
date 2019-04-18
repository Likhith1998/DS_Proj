package com.distsys.projects.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SearchResult implements Serializable {

    private List<FlightResultInfo> flightResultInfoList =  new ArrayList<>();
    private List<HotelResultInfo> hotelResultInfoList = new ArrayList<>();

    public SearchResult(List<FlightResultInfo> flightResultInfoList, List<HotelResultInfo> hotelResultInfoList) {
        this.flightResultInfoList = flightResultInfoList;
        this.hotelResultInfoList = hotelResultInfoList;
    }

    public List<FlightResultInfo> getFlightResultInfoList() {
        return flightResultInfoList;
    }

    public void setFlightResultInfoList(List<FlightResultInfo> flightResultInfoList) {
        this.flightResultInfoList = flightResultInfoList;
    }

    public List<HotelResultInfo> getHotelResultInfoList() {
        return hotelResultInfoList;
    }

    public void setHotelResultInfoList(List<HotelResultInfo> hotelResultInfoList) {
        this.hotelResultInfoList = hotelResultInfoList;
    }
}
