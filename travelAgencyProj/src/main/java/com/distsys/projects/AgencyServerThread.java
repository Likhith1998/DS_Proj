package com.distsys.projects;

import com.distsys.projects.dao.QueryDetails;
import com.distsys.projects.dao.SearchResult;
import com.distsys.projects.utils.SendReceiveUtil;

import java.io.*;
import java.net.Socket;
import java.util.Vector;

public class AgencyServerThread implements Runnable {

    Socket s;
    boolean isloggedin;
    private String name;
    final ObjectInputStream ois;
    final ObjectOutputStream oos;
    private static Vector<AgencyServerThread> clientList;

    // constructor
    public AgencyServerThread(Socket s, String name, ObjectInputStream ois, ObjectOutputStream oos, Vector<AgencyServerThread> clientList) {
        this.ois = ois;
        this.oos = oos;
        this.name = name;
        this.s = s;
        this.isloggedin=true;
        this.clientList = clientList;
    }

    @Override
    public void run() {

        QueryDetails queryDetails;
        try {
            System.out.println("Got here ... Yay");
            queryDetails = (QueryDetails) ois.readObject();
            System.out.println("Got query details "+queryDetails);

            if(queryDetails.isToBook() == false){
                if(queryDetails.isFlightAndHotel() == false){
                    if(queryDetails.isFlightOnly() == true){
                        Socket socket;
                        try {
                            socket = new Socket("127.0.0.1",9998);
                        } catch (IOException e) {
                            e.printStackTrace();
                            System.out.println("Cannot find host with the IP 127.0.0.1 and port 9998");
                            return;
                        }
                        System.out.println("Server connected");
                        SearchResult searchResult = new SendReceiveUtil<SearchResult>().sendAndRecieve(socket,queryDetails);
                        oos.writeObject(searchResult);
                        ois.close();
                        oos.close();
                        socket.close();
                    }
                    if(queryDetails.isHotelOnly() == true){
                        Socket socket;
                        try {
                            socket = new Socket("127.0.0.1",9999);
                        } catch (IOException e) {
                            e.printStackTrace();
                            System.out.println("Cannot find host with the IP 127.0.0.1 and port 9999");
                            return;
                        }
                        System.out.println("Server connected");
                        SearchResult searchResult = new SendReceiveUtil<SearchResult>().sendAndRecieve(socket,queryDetails);
                        oos.writeObject(searchResult);
                        ois.close();
                        oos.close();
                        socket.close();
                    }
                    if(queryDetails.isFlightAndHotel() == true){
                        Socket socket;
                        try {
                            socket = new Socket("127.0.0.1",9998);
                        } catch (IOException e) {
                            e.printStackTrace();
                            System.out.println("Cannot find host with the IP 127.0.0.1 and port 9998");
                            return;
                        }
                        System.out.println("Server connected");
                        SearchResult searchResult = new SendReceiveUtil<SearchResult>().sendAndRecieve(socket,queryDetails);
                        socket.close();

                        try {
                            socket = new Socket("127.0.0.1",9999);
                        } catch (IOException e) {
                            e.printStackTrace();
                            System.out.println("Cannot find host with the IP 127.0.0.1 and port 9999");
                            return;
                        }
                        System.out.println("Server connected");
                        SearchResult searchResult1 = new SendReceiveUtil<SearchResult>().sendAndRecieve(socket,queryDetails);
                        searchResult.setHotelResultInfoList(searchResult1.getHotelResultInfoList());
                        oos.writeObject(searchResult1);
                        ois.close();
                        oos.close();
                        socket.close();
                    }
                }
            }
            //TODO : Booking

        }catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
