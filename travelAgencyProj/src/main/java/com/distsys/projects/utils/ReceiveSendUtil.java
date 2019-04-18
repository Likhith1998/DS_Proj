package com.distsys.projects.utils;

import com.distsys.projects.dao.QueryDetails;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ReceiveSendUtil<T> {

    private T returnMessage;

    public T receiveAndSend(Socket socket, QueryDetails queryDetails){

        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;

        try {
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            returnMessage = (T) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("return Message is =" + returnMessage);

        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot create ObjectInputStream or ObjectOutputStream instance");
            return null;
        }
        System.out.println("ObjectInputStream and ObjectOutputStream created successfully");

        try {
            oos.writeObject(queryDetails);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("QueryDetails sent to server ...");

        return returnMessage;
    }
}
