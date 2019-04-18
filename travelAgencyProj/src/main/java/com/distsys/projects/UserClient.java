package com.distsys.projects;

import com.distsys.projects.dao.Details;
import com.distsys.projects.dao.SearchResult;
import com.distsys.projects.utils.SendReceiveUtil;

import java.io.IOException;
import java.net.Socket;

public class UserClient {

    private String agencyIP;
    private Integer agencyPort;

    public UserClient(){
        this.agencyIP = "127.0.0.1";
        this.agencyPort = 9999;
    }
    public void run(){
        Details details = new Details();
        // TODO : take details from here.
        
        search(details);
        book(details);
    }

    public void search(Details details){
        Socket socket = null;
        try {
            socket = new Socket(agencyIP,agencyPort);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot find host with the IP "+agencyIP+" and port "+agencyPort);
            return;
        }
        System.out.println("Server connected");

        SearchResult searchResult = null;
        new SendReceiveUtil().sendAndRecieve(socket, details, searchResult);

//        ObjectOutputStream oos = null;
//        ObjectInputStream ois = null;
//        try {
//            oos = new ObjectOutputStream(socket.getOutputStream());
//            ois = new ObjectInputStream(socket.getInputStream());
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.out.println("Cannot create ObjectInputStream or ObjectOutputStream instance");
//            return;
//        }
//        System.out.println("ObjectInputStream and ObjectOutputStream created successfully");
//        Details details = new Details();
//        try {
//            oos.writeObject(details);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println("Details sent to server ...");
//
//        ObjectInputStream is = null;
//        try {
//            is = new ObjectInputStream(socket.getInputStream());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Details returnMessage = null;
//        try {
//            returnMessage = (Details) is.readObject();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        System.out.println("return Message is=" + returnMessage);
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void book(Details details){

    }
}

