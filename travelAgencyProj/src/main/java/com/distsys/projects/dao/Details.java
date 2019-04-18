package com.distsys.projects.dao;

import java.sql.Date;

public class Details {

    private boolean toBook;

    private boolean flightAndHotel;

    private boolean flightOnly;
    private FlightDetails flightDetails;

    private boolean hotelOnly;
    private HotelDetails hotelDetails;

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
        FlightDetails flightDetails = new FlightDetails(origin, destination, departing, returning, no_of_seats);
        this.flightDetails = flightDetails;
    }

    public FlightDetails getFlightDetails(){
        return flightDetails;
    }

    public void setHotelDetails(String city,
                                Date fromDate,
                                Date toDate,
                                Integer no_of_rooms){
        HotelDetails hotelDetails = new HotelDetails(city, fromDate, toDate, no_of_rooms);
        this.hotelDetails = hotelDetails;
    }

    public HotelDetails getHotelDetails(){
        return hotelDetails;
    }
}
