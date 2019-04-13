package com.distsys.projects;

import com.distsys.projects.utils.Configuration;

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
    private int port = 1234; // TODO : Take the port number from the config file
    private static Vector<AgencyServerImpl> clientList = new Vector<>();
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
            AgencyServerImpl mtch = new AgencyServerImpl(s,"client " + no_of_clients, ois, oos);
            Thread t = new Thread(mtch);
            System.out.println("Adding this client to active client list");
            clientList.add(mtch);
            t.start();
            no_of_clients++;
        }
    }

    private boolean testConnection(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con= DriverManager.getConnection(url+db_name,username,password);
            Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery("select * from hotel_info");
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
