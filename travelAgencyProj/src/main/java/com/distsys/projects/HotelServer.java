package com.distsys.projects;

import com.distsys.projects.dao.QueryDetails;
import com.distsys.projects.dao.SearchResult;
import com.distsys.projects.utils.Configuration;
import com.distsys.projects.utils.SendReceiveUtil;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;
import java.util.Vector;

public class HotelServer {

    private String url;
    private String username;
    private String password;
    private int port = 9999;
    private static Vector<HotelServerThread> clientList = new Vector<>();
    private static int no_of_clients = 0;
    private static final String db_name = "hotel_db";

    public void runServer(String type, Configuration config) throws IOException {

        System.out.println("Hotel "+type+" Server Running ...");
        this.url = config.getConnection().getUrl();
        Map.Entry<String,String> entry = config.getUsers().entrySet().iterator().next();
        this.username = entry.getKey();
        this.password = entry.getValue();
        if(testConnection() == true)
            System.out.println("Database Connection Successful ...");
        else {
            System.out.println("Database Connection Failed ...");
            System.out.println("Hotel "+type+" Server Shutting down");
            return;
        }

        // Creating server socket to listen to clients
        ServerSocket ss = new ServerSocket(port);
        Socket s;

        while (true)
        {
            s = ss.accept();
            System.out.println("New client request received : " + s);

            ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(s.getInputStream());

            System.out.println("Creating a new handler for this client...");
            HotelServerThread mtch = new HotelServerThread(s,"client " + no_of_clients, ois, oos, clientList);
            Thread t = new Thread(mtch);
            System.out.println("Adding this client to active client list");
            clientList.add(mtch);
            t.start();
            no_of_clients++;
        }
    }

    public void runserver() throws IOException, InterruptedException {
        System.out.println("Hotel Server Running on port 9999");
        ServerSocket ss = new ServerSocket(port);
        Socket s;

//        while (true) {
        s = ss.accept();
        System.out.println("New client request received : " + s);

        ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(s.getInputStream());

        System.out.println("Creating a new handler for this client...");
        HotelServerThread mtch = new HotelServerThread(s, "client " + no_of_clients, ois, oos, clientList);
        Thread t = new Thread(mtch);
        System.out.println("Adding this client to active client list");
        clientList.add(mtch);
        t.start();
        no_of_clients++;
        t.join();
//        }
    }

    private boolean testConnection(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con= DriverManager.getConnection(url+db_name,username,password);
            Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery("select * from hotel_info");
            while(rs.next()){}
            con.close();
            return true;
        }catch(Exception e){
            System.out.println(e);
            return false;
        }
    }
}

class HotelServerThread implements Runnable {

    Socket s;
    boolean isloggedin;
    private String name;
    final ObjectInputStream ois;
    final ObjectOutputStream oos;
    private static Vector<HotelServerThread> clientList;

    // constructor
    public HotelServerThread(Socket s, String name, ObjectInputStream ois, ObjectOutputStream oos, Vector<HotelServerThread> clientList) {
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
                            socket = new Socket("127.0.0.1",9999);
                        } catch (IOException e) {
                            e.printStackTrace();
                            System.out.println("Cannot find host with the IP 127.0.0.1 and port 9999");
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