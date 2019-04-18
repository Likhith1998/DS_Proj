package com.distsys.projects.utils;

import com.distsys.projects.dao.Details;
import com.distsys.projects.dao.SearchResult;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SendReceiveUtil {

    public void sendAndRecieve(Socket socket, Details details, SearchResult returnMessage){
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
        try {
            returnMessage = (SearchResult) is.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("return Message is=" + returnMessage);
    }
}
