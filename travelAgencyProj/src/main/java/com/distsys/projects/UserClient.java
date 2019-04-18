package com.distsys.projects;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Date;

public class UserClient {

    private String agencyIP;
    private Integer agencyPort;

    public UserClient(){
        this.agencyIP = "127.0.0.1";
        this.agencyPort = 9999;
    }
    public void run(){

    }

    public void search(){
        Socket socket = null;
        try {
            socket = new Socket(agencyIP,agencyPort);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot find host with the IP "+agencyIP+" and port "+agencyPort);
            return;
        }
        System.out.println("Server connected");
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot create ObjectInputStream or ObjectOutputStream instance");
            return;
        }
        System.out.println("ObjectInputStream and ObjectOutputStream created successfully");
        Details details = new Details();
        try {
            oos.writeObject(details);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Details sent to server ...");

        ObjectInputStream is = null;
        try {
            is = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Details returnMessage = null;
        try {
            returnMessage = (Details) is.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("return Message is=" + returnMessage);
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void book(){

    }
}

class Details {

    private boolean flightAndHotel;

    private boolean flightOnly;
    private FlightDetails flightDetails;

    private boolean hotelOnly;
    private HotelDetails hotelDetails;


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

class FlightDetails {

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

class HotelDetails {

    private String city;
    private Date fromDate;
    private Date toDate;
    private Integer no_of_rooms;

    public HotelDetails(String city, Date fromDate, Date toDate, Integer no_of_rooms) {
        this.city = city;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.no_of_rooms = no_of_rooms;
    }

    public String getCity() {
        return city;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public Integer getNo_of_rooms() {
        return no_of_rooms;
    }
}

