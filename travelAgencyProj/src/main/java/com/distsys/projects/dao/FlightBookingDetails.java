package com.distsys.projects.dao;

public class FlightBookingDetails {
    private Integer user_id;
    private Integer flightId;
    private Integer no_of_seats;

    public FlightBookingDetails(Integer user_id,Integer flightId,Integer no_of_seats)
    {
        this.user_id = user_id;
        this.flightId = flightId;
        this.no_of_seats = no_of_seats;
    }

    public Integer getUser_id() {   return user_id; }
    public Integer getFlightId() {  return flightId; }
    public Integer getNoOfSeats() { return no_of_seats; }
}
