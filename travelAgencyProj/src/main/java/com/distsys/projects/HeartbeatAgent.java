package com.distsys.projects;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class HeartbeatAgent implements Runnable{

    private String primaryIP;
    private int primaryPort;

    public HeartbeatAgent(String primaryIP, int primaryPort) {
        this.primaryIP = primaryIP;
        this.primaryPort = primaryPort;
    }


    @Override
    public void run() {
        System.out.println("Running HeartbeatAgent Deamon ...");
        Socket socket = null;
        try {
            socket = new Socket(primaryIP, primaryPort);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot find host with the IP "+primaryIP+" and port "+primaryPort);
            return;
        }
        System.out.println("Client connected");
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
        HeartbeatMessage message = new HeartbeatMessage("I am alive",false);
        try {
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Heartbeat message sent to server ...");

        ObjectInputStream is = null;
        try {
            is = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Message returnMessage = null;
        try {
            returnMessage = (Message) is.readObject();
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
}

class HeartbeatMessage {

    private String message;
    private boolean isACK;

    public HeartbeatMessage(String message, boolean isACK) {
        this.message = message;
        this.isACK = isACK;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isACK() {
        return isACK;
    }

    public void setACK(boolean ACK) {
        isACK = ACK;
    }
}
