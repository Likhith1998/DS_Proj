package com.distsys.projects;

import com.distsys.projects.dao.QueryDetails;
import com.distsys.projects.dao.SearchResult;
import com.distsys.projects.utils.SendReceiveUtil;

import java.io.IOException;
import java.net.Socket;
import java.sql.Date;
import java.text.ParseException;
import java.util.Scanner;

public class UserClient {

    private String agencyIP;
    private Integer agencyPort;

    public UserClient(){
        //TODO : take agency server IP from config file
        this.agencyIP = "10.145.181.84";
        this.agencyPort = 9997;
    }

    // Testing purposes
    public QueryDetails getDummyDetails() throws ParseException {
        QueryDetails queryDetails = new QueryDetails();
        queryDetails.setToBook(true);
        queryDetails.setFlightAndHotel(true);
        queryDetails.setFlightOnly(false);
        String dep="2019-04-10";
        String ret="2019-04-13";
        Date depDate = Date.valueOf(dep);
        Date retDate = Date.valueOf(ret);
        queryDetails.setFlightDetails("Port1","Port2",depDate,retDate,2);
        String from="2019-04-11";
        Date fromDate = Date.valueOf(from);
        queryDetails.setHotelDetails("Port2",fromDate,fromDate,1);
        return queryDetails;
    }
    public void run(){
        userInput();
    }

    public void testrun() throws ParseException {
        QueryDetails queryDetails = getDummyDetails();
        book(queryDetails);
    }

    public void userInput(){
        QueryDetails details = new QueryDetails();
        int input;
        // TODO : take details from here.
        System.out.println("Enter your choice : ");
        System.out.println("1 : search ");
        System.out.println("2 : book ");
        System.out.println("3 : exit ");
        Scanner in = new Scanner(System.in);
        input = in.nextInt();
        switch(input) {
            case 1:
                System.out.println("1: Flight Search ");
                System.out.println("2 : Hotel Search ");
                System.out.println("3 : Trip Search ");
                int input1 = in.nextInt();
                if (input1 == 1) {
                    details.setToBook(false);
                    details.setFlightAndHotel(false);
                    details.setFlightOnly(true);
                    details.setHotelOnly(false);
                    System.out.println("Enter the flight details :::  ");
                    System.out.println("1: Origin");
                    in.nextLine();
                    String origin = in.nextLine();
                    System.out.println("2: Destination");
                    String dest = in.nextLine();
                    System.out.println("3: Date of departure ");
                    String departure = in.nextLine();
                    Date depart = Date.valueOf(departure);
                    System.out.println("4: Date of return ");
                    String returnstr = in.nextLine();
                    Date return1 = Date.valueOf(returnstr);
                    System.out.println("5: Number of seats ");
                    int no_of_seats = in.nextInt();
                    details.setFlightDetails(origin, dest, depart, return1, no_of_seats);
                } else if (input1 == 2) {
                    details.setToBook(false);
                    details.setFlightAndHotel(false);
                    details.setFlightOnly(false);
                    details.setHotelOnly(true);
                    System.out.println("Enter the hotel details :::  ");
                    System.out.println("1: City ");
                    in.nextLine();
                    String city = in.nextLine();
                    System.out.println("2: From Date ");
                    String fromDatestr = in.nextLine();
                    Date fromDate = Date.valueOf(fromDatestr);
                    System.out.println("3: To Date ");
                    String returnstr = in.nextLine();
                    Date toDate = Date.valueOf(returnstr);
                    System.out.println("4: Number of rooms ");
                    int no_of_rooms = in.nextInt();
                    details.setHotelDetails(city, fromDate, toDate, no_of_rooms);
                } else {
                    details.setFlightAndHotel(true);
                    details.setToBook(false);
                    details.setFlightOnly(true);
                    details.setHotelOnly(false);
                    System.out.println("Enter the trip details :::  ");
                    System.out.println("1: Origin ");
                    in.nextLine();
                    String origin = in.nextLine();
                    System.out.println("2: Destination");
                    String dest = in.nextLine();
                    System.out.println("3: Date of departure ");
                    String departure = in.nextLine();
                    Date depart = Date.valueOf(departure);
                    System.out.println("4: Date of return ");
                    String returnstr = in.nextLine();
                    Date return1 = Date.valueOf(returnstr);
                    System.out.println("5: Number of persons ");
                    int no_of_persons = in.nextInt();
                    details.setFlightDetails(origin, dest, depart, return1, no_of_persons);
                    details.setHotelDetails(dest, depart, return1, no_of_persons);
                }
                search(details);
                break;

            case 2: {
                System.out.println("1: Flight Booking ");
                System.out.println("2 : Hotel Booking ");
                System.out.println("3 : Trip Booking ");
                int input2 = in.nextInt();
                System.out.println("Enter the user_id ::: ");
                Integer user_id = in.nextInt();
                if (input2 == 1) {
                    details.setToBook(true);
                    details.setFlightAndHotel(false);
                    details.setFlightOnly(true);
                    details.setHotelOnly(false);
                    System.out.println("Enter the flight booking details :::  ");
                    System.out.println("1: FlightId ");
                    Integer flightId = in.nextInt();
                    System.out.println("2: Number of seats ");
                    int no_of_seats = in.nextInt();
                    details.setFlightBookingDetails(user_id, flightId , no_of_seats);
                } else if (input2 == 2) {
                    details.setToBook(true);
                    details.setFlightAndHotel(false);
                    details.setFlightOnly(false);
                    details.setHotelOnly(true);
                    System.out.println("Enter the hotel booking details :::  ");
                    System.out.println("1: Hotel Id ");
                    Integer hotelId = in.nextInt();
                    System.out.println("2: Number of rooms ");
                    int no_of_rooms = in.nextInt();
                    details.setHotelBookingDetails(user_id,hotelId, no_of_rooms);
                } else {
                    details.setFlightAndHotel(false);
                    details.setToBook(true);
                    details.setFlightOnly(true);
                    details.setHotelOnly(false);
                    System.out.println("Enter the trip details :::  ");
                    System.out.println("1: Going Flight ID ");
                    Integer goingFlightId = in.nextInt();
                    System.out.println("2: Return Flight ID");
                    Integer returnFlightId = in.nextInt();
                    System.out.println("3: Number of persons ");
                    int no_of_persons = in.nextInt();
                    System.out.println("4: Number of days ");
                    int no_of_days = in.nextInt();
                    System.out.println("5: Hotel IDs ");
                    Integer[] hotelIds = new Integer[no_of_days];
                    for(int i=0;i<no_of_days;i++)
                    {
                        int temp = in.nextInt();
                        hotelIds[i] = temp;
                    }
                    details.setTripBookingDetails(user_id,goingFlightId,returnFlightId,no_of_persons,no_of_days,hotelIds);
                }
                book(details);
                break;

            }

            case 3:
                System.out.println("Exiting....");
                break;
        }
    }

    public void search(QueryDetails queryDetails){
        Socket socket;
        try {
            socket = new Socket(agencyIP,agencyPort);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot find host with the IP "+agencyIP+" and port "+agencyPort);
            return;
        }
        System.out.println("Server connected");
        SearchResult searchResult = new SendReceiveUtil<SearchResult>().sendAndRecieve(socket, queryDetails);
        if(searchResult == null){
            System.out.println("Cannot find any results");
            return;
        }
        searchResult.printSearchResult();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void book(QueryDetails queryDetails){
        Socket socket;
        try {
            socket = new Socket(agencyIP,agencyPort);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot find host with the IP "+agencyIP+" and port "+agencyPort);
            return;
        }
        System.out.println("Server connected");
        SearchResult searchResult = new SendReceiveUtil<SearchResult>().sendAndRecieve(socket, queryDetails);
        if(searchResult == null){
            System.out.println("Cannot find any results");
            return;
        }
        searchResult.printSearchResult();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

