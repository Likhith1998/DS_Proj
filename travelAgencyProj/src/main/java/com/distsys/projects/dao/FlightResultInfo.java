package com.distsys.projects.dao;

import java.io.Serializable;

public class FlightResultInfo implements Serializable {

    private Integer airlineId;
    private Integer flightId;
    private String flightName;
    private String source;
    private String destination;
    private Integer price;

    public FlightResultInfo(Integer airlineId, Integer flightId, String flightName, String source, String destination, Integer price) {
        this.airlineId = airlineId;
        this.flightId = flightId;
        this.flightName = flightName;
        this.source = source;
        this.destination = destination;
        this.price = price;
    }

    public Integer getAirlineId() {
        return airlineId;
    }

    public void setAirlineId(Integer airlineId) {
        this.airlineId = airlineId;
    }

    public Integer getFlightId() {
        return flightId;
    }

    public void setFlightId(Integer flightId) {
        this.flightId = flightId;
    }

    public String getFlightName() {
        return flightName;
    }

    public void setFlightName(String flightName) {
        this.flightName = flightName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void printFlightResults(){
        System.out.println("Airline Id : "+getAirlineId());
        System.out.println("Flight Id : "+getFlightId());
        System.out.println("Flight Name : "+getFlightName());
        System.out.println("Source : "+getSource());
        System.out.println("Destination : "+getDestination());
        System.out.println("Price : "+getPrice());
    }
}
