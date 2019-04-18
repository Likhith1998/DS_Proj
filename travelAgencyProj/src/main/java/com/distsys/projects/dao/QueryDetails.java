package com.distsys.projects.dao;

import java.io.Serializable;
import java.sql.Date;

public class QueryDetails implements Serializable {

    private boolean toBook;

    private boolean flightAndHotel;

    private boolean flightOnly;
    private FlightQueryDetails flightDetails;
    private FlightBookingDetails flightBookingDetails;

    private boolean hotelOnly;
    private HotelQueryDetails hotelDetails;
    private HotelBookingDetails hotelBookingDetails;

    private TripBookingDetails tripBookingDetails;

    public void setToBook(boolean toBook) {
        this.toBook = toBook;
    }

    public boolean isToBook() {
        return toBook;
    }

    public boolean isFlightAndHotel() {
        return flightAndHotel;
    }

    public void setFlightAndHotel(boolean flightAndHotel) {
        this.flightAndHotel = flightAndHotel;
    }

    public boolean isFlightOnly() {
        return flightOnly;
    }

    public void setFlightOnly(boolean flightOnly) {
        this.flightOnly = flightOnly;
    }

    public boolean isHotelOnly() {
        return hotelOnly;
    }

    public void setHotelOnly(boolean hotelOnly) {
        this.hotelOnly = hotelOnly;
    }

    public void setFlightDetails(String origin,
                                 String destination,
                                 Date departing,
                                 Date returning,
                                 Integer no_of_seats) {
        FlightQueryDetails flightDetails = new FlightQueryDetails(origin, destination, departing, returning, no_of_seats);
        this.flightDetails = flightDetails;
    }

    public FlightQueryDetails getFlightDetails(){
        return flightDetails;
    }

    public void setHotelDetails(String city,
                                Date fromDate,
                                Date toDate,
                                Integer no_of_rooms){
        HotelQueryDetails hotelDetails = new HotelQueryDetails(city, fromDate, toDate, no_of_rooms);
        this.hotelDetails = hotelDetails;
    }

    public HotelQueryDetails getHotelDetails(){
        return hotelDetails;
    }

    public void setFlightBookingDetails(Integer user_id,Integer flight_id,Integer no_of_seats)
    {
        FlightBookingDetails flightBookingDetails = new FlightBookingDetails(user_id,flight_id,no_of_seats);
        this.flightBookingDetails = flightBookingDetails;
    }

    public void setHotelBookingDetails(Integer user_id,Integer hotelId,Integer no_of_rooms)
    {
        HotelBookingDetails hotelBookingDetails = new HotelBookingDetails(user_id,hotelId,no_of_rooms);
        this.hotelBookingDetails = hotelBookingDetails;
    }

    public void setTripBookingDetails(Integer userId, Integer goingFlightId, Integer returnFlightId, Integer no_of_passengers,Integer no_of_days, Integer[] hotelIds)
    {
        TripBookingDetails tripBookingDetails = new TripBookingDetails(userId,goingFlightId, returnFlightId, no_of_passengers, no_of_days, hotelIds);
    }
}
