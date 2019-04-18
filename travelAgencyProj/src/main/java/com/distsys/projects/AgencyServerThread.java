package com.distsys.projects;

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

        Message received;
        while (true)
        {
            try
            {
                received = (Message) ois.readObject();
                System.out.println(received.getMessage());

                if(received.isConnClose() == true){
                    for(AgencyServerThread ast : clientList){
                        if(ast.name == this.name){
                            clientList.remove(ast);
                        }
                    }
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
