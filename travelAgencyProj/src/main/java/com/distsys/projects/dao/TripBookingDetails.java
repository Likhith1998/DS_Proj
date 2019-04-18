package com.distsys.projects.dao;

public class TripBookingDetails {
    private Integer userId;
    private Integer goingFlightId;
    private Integer returnFlightId;
    private Integer no_of_passengers;
    private Integer no_of_days;
    private Integer[] hotelIds = new Integer[no_of_days];

    public TripBookingDetails(Integer userId, Integer goingFlightId, Integer returnFlightId, Integer no_of_passengers,Integer no_of_days, Integer[] hotelIds)
    {
        this.userId = userId;
        this.goingFlightId = goingFlightId;
        this.returnFlightId = returnFlightId;
        this.no_of_passengers = no_of_passengers;
        this.no_of_days = no_of_days;
        for(int i = 0;i< no_of_days;i++)
            this.hotelIds[i] = hotelIds[i];
    }
}
