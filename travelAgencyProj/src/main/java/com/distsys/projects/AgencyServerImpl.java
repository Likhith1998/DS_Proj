package com.distsys.projects;

import java.io.*;
import java.net.Socket;

public class AgencyServerImpl implements Runnable {

    Socket s;
    boolean isloggedin;
    private String name;
    final ObjectInputStream ois;
    final ObjectOutputStream oos;

    // constructor
    public AgencyServerImpl(Socket s, String name, ObjectInputStream ois, ObjectOutputStream oos) {
        this.ois = ois;
        this.oos = oos;
        this.name = name;
        this.s = s;
        this.isloggedin=true;
    }

    @Override
    public void run() {

        Message received;
        while (true)
        {
            try
            {
                received = (Message) ois.readObject();
                System.out.println(received.getMessage());

                if(received.isConnClose() == true){
                    this.isloggedin=false;
                    this.s.close();
                    break;
                }
                oos.writeObject(received);
            }catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        try {
            this.ois.close();
            this.oos.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
