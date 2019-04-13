package com.distsys.projects;

import com.distsys.projects.utils.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

public class HotelServer {

    private String url;
    private String username;
    private String password;
    private static final String db_name = "hotel_db";

    public void runServer(String type, Configuration config){
        this.url = config.getConnection().getUrl();
        Map.Entry<String,String> entry = config.getUsers().entrySet().iterator().next();
        this.username = entry.getKey();
        this.password = entry.getValue();
        if(testConnection() == true)
            System.out.println("Database Connection Successful ...");
        System.out.println("Hotel "+type+" Server Running ...");

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
