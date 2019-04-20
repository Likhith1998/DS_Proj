package com.distsys.projects;

import java.io.Serializable;

public class Message implements Serializable {

    private boolean connClose;
    private String message;
    private Integer numOfSeats;
    private Integer flightId;
    private String serverType;
    private Integer bookingId;
    private Integer bookingId2;
    private boolean sameAirline;
    //private Integer


    public Message(){
        connClose=false;
        sameAirline=false;
    }


    public boolean isConnClose() {
        return connClose;
    }

    public void setConnClose(boolean disconnect) {
        this.connClose = disconnect;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }






    public void setServerType(String type){
        serverType=type;
    }
    public String getServerType(){
        return serverType;
    }

    public void setAirlineQuery(Integer fId, Integer n){
        numOfSeats=n;
        flightId=fId;
    }

    public String printAirlineQuery(){
        return "Flight ID : " +flightId + " ||  Num of Seats : " + numOfSeats;
    }

    public void setBookingId(Integer bID){
        bookingId=bID;
    }

    public Integer getBookingId(){
        return bookingId;
    }
    public void setBookingId2(Integer bID){
        bookingId2=bID;
    }

    public Integer getBookingId2(){
        return bookingId2;
    }
    public Integer getSeats(){
        return numOfSeats;
    }
    public Integer getFlightId(){
        return flightId;
    }


    public void setSameAirline(boolean sa){
        sameAirline=sa;
    }
    public boolean getSameAirline(){
        return sameAirline;
    }

}

