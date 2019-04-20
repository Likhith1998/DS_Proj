package com.distsys.projects;

import com.distsys.projects.dao.QueryDetails;

import java.io.*;
import java.net.Socket;

public class AtomicCommit {
    // two phase commit

    // check for availability to airlines server and hotel booking server
    public String primaryIP1=null;
    public Integer primaryPort1=null;
    public String primaryIP2=null;
    public Integer primaryPort2=null;
    public String primaryIP3=null;
    public Integer primaryPort3=null;

    public QueryDetails queryDetails;

    public void setServer(Integer ch,String IP, Integer port){
        if(ch==1){
            primaryIP1=IP;
            primaryPort1=port;
        }else if(ch==2){
            primaryIP2=IP;
            primaryPort2=port;
        }else{
            primaryIP3=IP;
            primaryPort3=port;
        }
    }

    public void commit(Integer ch,QueryDetails queryDetails) throws IOException{

        System.out.println("Running Two Phase Commit");
        this.queryDetails = queryDetails;
        if (ch==1)      func1(queryDetails);
        else            func2(queryDetails);



    }


    public void func1(QueryDetails queryDetails) throws IOException{
        Socket socket1 = null;
        Socket socket2 = null;
        Socket socket3 = null;

        try {
            socket1 = new Socket(primaryIP1, primaryPort1);
            socket2 = new Socket(primaryIP2, primaryPort2);
            socket3 = new Socket(primaryIP3, primaryPort3);

        } catch (
                IOException e) {
            e.printStackTrace();
            System.out.println("Cannot find host with the IP " + primaryIP1 + "/"+primaryIP2 +"/"+primaryIP3+ " and port " + primaryPort1);
        }

        // check for availability
        System.out.println("All Server connected");
        ObjectOutputStream oos1 = null;
        ObjectInputStream ois1 = null;
        ObjectOutputStream oos2 = null;
        ObjectInputStream ois2 = null;
        ObjectOutputStream oos3 = null;
        ObjectInputStream ois3 = null;

        try {
            oos1 = new ObjectOutputStream(socket1.getOutputStream());
            ois1 = new ObjectInputStream(socket1.getInputStream());

            oos2 = new ObjectOutputStream(socket2.getOutputStream());
            ois2  = new ObjectInputStream(socket2.getInputStream());

            oos3 = new ObjectOutputStream(socket3.getOutputStream());
            ois3 = new ObjectInputStream(socket3.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot create ObjectInputStream or ObjectOutputStream instance");
        }
        System.out.println("ObjectInputStream and ObjectOutputStream created successfully");



        QueryDetails msg1 = new QueryDetails();
        msg1.setAirlineQuery(1004,2);

        QueryDetails msg2 =  new  QueryDetails();
        //msg2.setHotelQuery(1003,2);

        QueryDetails msg3 = new QueryDetails();
        msg3.setAirlineQuery(1003,2);

        // ask for availability
        // send number flight info
        //msg.setConnClose(true);

        // SEND PREPARE MSG TO ALL 3
        msg1.setMessage("prepare");
        msg2.setMessage("prepare");
        msg3.setMessage("prepare");


        oos1.writeObject(msg1);
        oos2.writeObject(msg2);
        oos3.writeObject(msg3);
        System.out.println("Sent availability request msg  to Servers!");

        QueryDetails send1 = new QueryDetails();
        QueryDetails send2 = new QueryDetails();
        QueryDetails send3 = new QueryDetails();
        QueryDetails received1 =null;
        QueryDetails received2 =null;
        QueryDetails received3= null;

        try{

            // RECEIVE REPLY
            ReceiveSet rs1 = new ReceiveSet(ois1,ois2,ois3);
            received1=rs1.getR1();
            received2=rs1.getR2();
            received3=rs1.getR3();
            boolean t1,t2,t3;
            t1 = rs1.getT1();
            t2 = rs1.getT2();
            t3 = rs1.getT3();



            if(t1||t2||t3){
                // TIMEOUT IN PHASE 1
                if(t1==false){
                    send1.setMessage("abort_to");
                    oos1.writeObject(send1);
                    if(t2==false){
                        send2.setMessage("abort_to");
                        oos2.writeObject(send2);
                        if(t3==false){
                            send3.setMessage("abort_to");
                            oos3.writeObject(send3);
                        }
                    }else{
                        if(t3==false){
                            send3.setMessage("abort_to");
                            oos3.writeObject(send3);
                        }
                    }

                }else{
                    if(t2==false){
                        send2.setMessage("abort_to");
                        oos2.writeObject(send2);
                        if(t3==false){
                            send3.setMessage("abort_to");
                            oos3.writeObject(send3);
                        }
                    }else{
                        if(t3==false){
                            send3.setMessage("abort_to");
                            oos3.writeObject(send3);
                        }
                    }

                }

            }

            else if (   received1.getMessage().equals("ready")
                    && received2.getMessage().equals("ready")
                    && received3.getMessage().equals("ready")

            ){

                System.out.println("RECEIVED READY FROM ALL : Finished Phase 1");
                // IF SUCCESS
                send1.setMessage("commit_request");
                send2.setMessage("commit_request");
                send3.setMessage("commit_request");
                oos1.writeObject(send1);
                oos2.writeObject(send2);
                oos3.writeObject(send3);

                // RECEIVE COMMITTED DONE MSG
                ReceiveSet rs2 = new ReceiveSet(ois1,ois2,ois3);
                received1=rs2.getR1();
                received2=rs2.getR2();
                received3=rs2.getR3();
                t1 = rs2.getT1();
                t2 = rs2.getT2();
                t3 = rs2.getT3();

                if(t1||t2||t3){
                    System.out.println("TIMEOUT in PHASE 2");
                }
                else{
                    if(    received1.getMessage().equals("commit_done")
                            && received2.getMessage().equals("commit_done")
                            && received3.getMessage().equals("commit_done")

                    ){
                        // COMITTED DONE
                        // INFORM THE CLIENT
                        System.out.println("Booked !!");
                        System.out.println("Booking Id1 : " + received1.getBookingId());
                        System.out.println("Booking Id2 : " + received2.getBookingId());
                        System.out.println("Booking Id3 : " + received3.getBookingId());

                    }
                    else
                    {
                        System.out.println("Booking Unsuccessfull Phase 2!!");
                    }
                    if(   received1.getMessage().equals("abort")
                            || received2.getMessage().equals("abort")
                            || received3.getMessage().equals("abort")

                    ){

                        System.out.println("RECEIVED ABORT FROM ANY");

                        // IF ABORT
                        send1.setMessage("abort");
                        send2.setMessage("abort");
                        send3.setMessage("abort");
                        oos1.writeObject(send1);
                        oos2.writeObject(send2);
                        oos3.writeObject(send3);



                        // RECEIVE ABORT ACK
                        received1 = (QueryDetails)ois1.readObject();
                        received2 = (QueryDetails)ois2.readObject();
                        received3 = (QueryDetails)ois3.readObject();

                        if(    received1.getMessage().equals("abort_ack")
                                && received2.getMessage().equals("abort_ack")
                                && received3.getMessage().equals("abort_ack")
                        ){
                            System.out.println("Booking Unsuccessfull !!");
                        }

                    }
                    else {
                    }
                }
            }
            //if(received.isConnClose()){
            socket1.close();
            ois1.close();
            oos1.close();
            //if(received.isConnClose()){
            socket2.close();
            ois2.close();
            oos2.close();
            //if(received.isConnClose()){
            socket3.close();
            ois3.close();
            oos3.close();
            //}



        }
        catch(ClassNotFoundException |  IOException | InterruptedException  ce){
            ce.printStackTrace();
        }

    }


    public void func2(QueryDetails queryDetails) throws IOException {
        Socket socket1 = null;
        Socket socket2 = null;

        try {
            socket1 = new Socket(primaryIP1, primaryPort1);
            socket2 = new Socket(primaryIP2, primaryPort2);

        } catch (
                IOException e) {
            e.printStackTrace();
            System.out.println("Cannot find host with the IP " + primaryIP1 + "/"+primaryIP2 );
        }

        // check for availability
        System.out.println("All Server connected");
        ObjectOutputStream oos1 = null;
        ObjectInputStream ois1 = null;
        ObjectOutputStream oos2 = null;
        ObjectInputStream ois2 = null;


        try {
            oos1 = new ObjectOutputStream(socket1.getOutputStream());
            ois1 = new ObjectInputStream(socket1.getInputStream());

            oos2 = new ObjectOutputStream(socket2.getOutputStream());
            ois2  = new ObjectInputStream(socket2.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot create ObjectInputStream or ObjectOutputStream instance");        }
        System.out.println("ObjectInputStream and ObjectOutputStream created successfully");


//
        QueryDetails msg1 = null;
        try {
            msg1 = (QueryDetails)queryDetails.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        QueryDetails msg2 = null;
        try {
            msg2 = (QueryDetails)queryDetails.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
//        QueryDetails msg1 = new QueryDetails();
//        msg1.setAirlineQuery(1004,2);
//
//        QueryDetails msg2 =  new  QueryDetails();
        //msg2.setHotelQuery(1003,2);

        // ask for availability
        // send number flight info
        //msg.setConnClose(true);

        // SEND PREPARE MSG TO ALL 3
        msg1.setMessage("prepare");
        msg2.setMessage("prepare");

        oos1.writeObject(msg1);
        oos2.writeObject(msg2);
        System.out.println("Sent availability request msg  to Servers!");

        QueryDetails send1 = new QueryDetails();
        QueryDetails send2 = new QueryDetails();

        QueryDetails received1 =null;
        QueryDetails received2 =null;


        send1.setSameAirline(true);
        send2.setSameAirline(true);


        try{


            // RECEIVE REPLY
            received1 = (QueryDetails) ois1.readObject();
            received2 = (QueryDetails) ois2.readObject();

            if (   received1.getMessage().equals("ready")
                    && received2.getMessage().equals("ready")

            ){

                System.out.println("RECEIVED READY FROM ALL : Finished Phase 1");
                // IF SUCCESS
                send1.setMessage("commit_request");
                send2.setMessage("commit_request");
                oos1.writeObject(send1);
                oos2.writeObject(send2);

                // RECEIVE COMMITTED DONE MSG
                received1 = (QueryDetails)ois1.readObject();
                received2 = (QueryDetails)ois2.readObject();


                if(    received1.getMessage().equals("commit_done")
                        && received2.getMessage().equals("commit_done")

                ){
                    // COMITTED DONE
                    // INFORM THE CLIENT
                    System.out.println("Booked !!");
                    System.out.println("Booking Id1 : " + received1.getBookingId());
                    System.out.println("Booking Id2 : " + received2.getBookingId());
                    System.out.println("Booking Id3 : " + received1.getBookingId2());

                }
                else
                {
                    System.out.println("Booking Unsuccessfull Phase 2!!");
                }
            }
            else if(   received1.getMessage().equals("abort")
                    || received2.getMessage().equals("abort")

            ){

                System.out.println("RECEIVED ABORT FROM ANY");

                // IF ABORT
                send1.setMessage("abort");
                send2.setMessage("abort");
                oos1.writeObject(send1);
                oos2.writeObject(send2);



                // RECEIVE ABORT ACK
                received1 = (QueryDetails)ois1.readObject();
                received2 = (QueryDetails)ois2.readObject();

                if(    received1.getMessage().equals("abort_ack")
                        && received2.getMessage().equals("abort_ack")
                ){
                    System.out.println("Booking Unsuccessfull !!");
                }

            }
            else {
                // IF TIMEOUT

                // connect to backup
            }
            //if(received.isConnClose()){
            socket1.close();
            ois1.close();
            oos1.close();
            //if(received.isConnClose()){
            socket2.close();
            ois2.close();
            oos2.close();

        }
        catch(ClassNotFoundException |  IOException ce){
            ce.printStackTrace();
        }

    }






}

class ReceiveSet{
    private QueryDetails received1;
    private QueryDetails received2;
    private QueryDetails received3;
    private boolean t1,t2,t3;

    public ReceiveSet(ObjectInputStream ois1, ObjectInputStream ois2, ObjectInputStream ois3)throws InterruptedException,IOException{

        t1=false;t2=false;t3=false;

        GetMsg to = new GetMsg(ois1);
        to.start();

        GetMsg to2 = new GetMsg(ois2);
        to2.start();

        GetMsg to3 = new GetMsg(ois3);
        to3.start();

        Integer c=5;
        while(c>=0 && to.getTo()){
            System.out.println(c);
            Thread.sleep(1000);
            c--;
        }
        if(to.getTo()==false){
            received1 = to.getMsg();
            System.out.println(received1.getMessage());
        }
        else{
            t1=true;
            System.out.println("Timeout R1");
        }

        c=5;
        while((t1==false) && c>=0 && to2.getTo()){
            System.out.println(c);
            Thread.sleep(1000);
            c--;
        }
        if(to2.getTo()==false){
            received2 = to2.getMsg();
            System.out.println(received2.getMessage());
        }
        else{
            t2=true;
            System.out.println("Timeout R2");
        }


        c=5;
        while((t2==false) && c>=0 && to3.getTo()){
            System.out.println(c);
            Thread.sleep(1000);
            c--;
        }
        if(to3.getTo()==false){
            received3 = to.getMsg();
            System.out.println(received3.getMessage());
        }
        else{
            t3=true;
            System.out.println("Timeout R3");
        }
    }

    public QueryDetails getR1(){
        return received1;
    }

    public QueryDetails getR2(){
        return received2;
    }
    public QueryDetails getR3(){
        return received3;
    }
    public boolean getT1(){
        return t1;
    }
    public boolean getT2(){
        return t2;
    }
    public boolean getT3(){
        return t3;
    }
}




class GetMsg extends Thread{
    private QueryDetails msg ;
    ObjectInputStream ois;
    private boolean timout;
    public GetMsg(ObjectInputStream ois1){
        ois=ois1;
        timout=true;
    }

    @Override
    public void run(){

        try{
            msg = (QueryDetails) ois.readObject();
            System.out.println("Read Msg");
        }
        catch(IOException |  ClassNotFoundException e){
            e.printStackTrace();
        }
        timout=false;
    }
    public boolean getTo(){
        return timout;
    }
    public QueryDetails getMsg(){
        return msg;
    }


}
