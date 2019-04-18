package com.distsys.projects.dao;

import java.sql.Date;

public class FlightDetails {

    private String origin;
    private String destination;
    private Date departing;
    private Date returning;
    private Integer no_of_seats;

    public FlightDetails(String origin, String destination, Date departing, Date returning, Integer no_of_seats) {
        this.origin = origin;
        this.destination = destination;
        this.departing = departing;
        this.returning = returning;
        this.no_of_seats = no_of_seats;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public Date getDeparting() {
        return departing;
    }

    public Date getReturning() {
        return returning;
    }

    public Integer getNo_of_seats() {
        return no_of_seats;
    }
}
