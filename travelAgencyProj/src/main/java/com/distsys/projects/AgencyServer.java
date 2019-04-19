package com.distsys.projects;

import com.distsys.projects.dao.QueryDetails;
import com.distsys.projects.dao.SearchResult;
import com.distsys.projects.utils.Configuration;
import com.distsys.projects.utils.SendReceiveUtil;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;
import java.util.Vector;

public class AgencyServer {

    private String url;
    private String username;
    private String password;
    private int port = 9997; // TODO : Take the port number from the config file
    private static Vector<AgencyServerThread> clientList = new Vector<>();
    private static int no_of_clients = 0;
    private static final String db_name = "agency_db";

    public void runServer(String type, Configuration config) throws IOException{

        // TODO : Implement Heartbeat Messages

        // Checking for the database connection
        System.out.println("Agency "+type+" Server Running ...");
        this.url = config.getConnection().getUrl();
        Map.Entry<String,String> entry = config.getUsers().entrySet().iterator().next();
        this.username = entry.getKey();
        this.password = entry.getValue();
        if(testConnection() == true)
            System.out.println("Database Connection Successful ...");
        else {
            System.out.println("Database Connection Failed ...");
            System.out.println("Agency "+type+" Server Shutting down");
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
            AgencyServerThread mtch = new AgencyServerThread(s,"client " + no_of_clients, ois, oos, clientList);
            Thread t = new Thread(mtch);
            System.out.println("Adding this client to active client list");
            clientList.add(mtch);
            t.start();
            no_of_clients++;
        }
    }

    public void runserver() throws IOException, InterruptedException {
        System.out.println("Agency Server Running on port 9997");
        ServerSocket ss = new ServerSocket(port);
        Socket s;

//        while (true) {
            s = ss.accept();
            System.out.println("New client request received : " + s);

            ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(s.getInputStream());

            System.out.println("Creating a new handler for this client...");
            AgencyServerThread mtch = new AgencyServerThread(s, "client " + no_of_clients, ois, oos, clientList);
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
            ResultSet rs=stmt.executeQuery("select * from agency_info");
            // TODO : Instead of running a Query, Find a trick to just check connection to DB
            while(rs.next()){}
            con.close();
            return true;
        }catch(Exception e){
            System.out.println(e);
            return false;
        }
    }
}

class AgencyServerThread implements Runnable {

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

