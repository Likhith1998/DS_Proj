package com.distsys.projects;

import com.distsys.projects.dao.*;
import com.distsys.projects.utils.Configuration;
import com.distsys.projects.utils.SendReceiveUtil;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class HotelServer {

    private String url;
    private String username;
    private String password;
    private int port = 9999;
    private static Vector<HotelServerThread> clientList = new Vector<>();
    private static int no_of_clients = 0;
    private static final String db_name = "dsproj";

    public void runServer(String type, Configuration config) throws IOException {

        System.out.println("Hotel "+type+" Server Running ...");
        this.url = config.getConnection().getUrl();
        Map.Entry<String,String> entry = config.getUsers().entrySet().iterator().next();
        this.username = entry.getKey();
        this.password = entry.getValue();
        if(testConnection() == true)
            System.out.println("Database Connection Successful ...");
        else {
            System.out.println("Database Connection Failed ...");
            System.out.println("Hotel "+type+" Server Shutting down");
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
            HotelServerThread mtch = new HotelServerThread(s,"client " + no_of_clients, ois, oos, clientList);
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
            ResultSet rs=stmt.executeQuery("select * from hotelinfo");
            while(rs.next()){}
            con.close();
            return true;
        }catch(Exception e){
            System.out.println(e);
            return false;
        }
    }
}

class HotelServerThread implements Runnable {

    Socket s;
    boolean isloggedin;
    private String name;
    final ObjectInputStream ois;
    final ObjectOutputStream oos;
    private static Vector<HotelServerThread> clientList;
    private Connection connection;


    public Connection createDBConnecttion(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/dsproj","katta","katta");
            return con;
        }catch(Exception e){
            System.out.println(e);
            return null;
        }
    }

    // constructor
    public HotelServerThread(Socket s, String name, ObjectInputStream ois, ObjectOutputStream oos, Vector<HotelServerThread> clientList) {
        this.ois = ois;
        this.oos = oos;
        this.name = name;
        this.s = s;
        this.isloggedin=true;
        this.clientList = clientList;
        this.connection = createDBConnecttion();
        if(connection == null){
            System.out.println("No connection established with the DB");
            return;
        }
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
                    if(queryDetails.isHotelOnly() == true) {
                        HotelQueryDetails hotelQueryDetails = queryDetails.getHotelDetails();
                        List<HotelResultInfo> hotelResultInfos = new ArrayList<>();
                        // TODO : Write database part
                        Date presDate;
                        presDate = hotelQueryDetails.getFromDate();
                        while (presDate.toString().compareTo(hotelQueryDetails.getToDate().toString()) <= 0) {
                            Statement statement = connection.createStatement();
                            ResultSet rs = statement.executeQuery("SELECT * \n" +
                                    "FROM hotelinfo as A\n" +
                                    "WHERE A.place = " + hotelQueryDetails.getCity() + " AND " +
                                    "A.total_rooms > " + hotelQueryDetails.getNo_of_rooms() + " AND "+
                                    "A.hoteldate = "+presDate);

                            while (rs.next()) {
                                Integer hotelid = rs.getInt("hotelid");
                                String hotelname = rs.getString("hotelname");
                                String place = rs.getString("place");
                                Integer price = rs.getInt("price");
                                HotelResultInfo hotelResultInfo = new HotelResultInfo(hotelid,hotelname,place,price);
                                hotelResultInfos.add(hotelResultInfo);
                            }
                            presDate = new Date( presDate.getTime() + 24*60*60*1000);
                        }
                        SearchResult searchResult = new SearchResult(null,hotelResultInfos);
                        oos.writeObject(searchResult);
                        ois.close();
                        oos.close();
                        s.close();
                    }
                    if(queryDetails.isFlightAndHotel() == true){
                        HotelQueryDetails hotelQueryDetails = queryDetails.getHotelDetails();
                        List<HotelResultInfo> hotelResultInfos = new ArrayList<>();
                        // TODO : Write database part
                        Date presDate;
                        presDate = hotelQueryDetails.getFromDate();
                        while (presDate.toString().compareTo(hotelQueryDetails.getToDate().toString()) <= 0) {
                            Statement statement = connection.createStatement();
                            ResultSet rs = statement.executeQuery("SELECT * \n" +
                                    "FROM hotelinfo as A\n" +
                                    "WHERE A.place = " + hotelQueryDetails.getCity() + " AND " +
                                    "A.total_rooms > " + hotelQueryDetails.getNo_of_rooms() + " AND "+
                                    "A.hoteldate = "+presDate);

                            while (rs.next()) {
                                Integer hotelid = rs.getInt("hotelid");
                                String hotelname = rs.getString("hotelname");
                                String place = rs.getString("place");
                                Integer price = rs.getInt("price");
                                HotelResultInfo hotelResultInfo = new HotelResultInfo(hotelid,hotelname,place,price);
                                hotelResultInfos.add(hotelResultInfo);
                            }
                            presDate = new Date( presDate.getTime() + 24*60*60*1000);
                        }
                        SearchResult searchResult = new SearchResult(null,hotelResultInfos);
                        oos.writeObject(searchResult);
                        ois.close();
                        oos.close();
                        s.close();
                    }
                }
            }
            //TODO : Booking

            if(queryDetails.isToBook() == true) {
                QueryDetails received;
                boolean flag=true;
                Integer c=1;
                while (true)
                {

                    try
                    {
                        //System.out.println("Before Receive");
                        received = (QueryDetails) this.ois.readObject();


                        System.out.println("Agency QueryDetails : " +c );
                        c++;
                        System.out.println(received.getMessage());

                        Thread.sleep(1);


                        if(received.getSameAirline()==false){
                            if(func1(received)==false)
                                break; // different airlines
                        }else{
                            if(func2(received)==false)
                                break; // same airlines
                        }
                        //oos.writeObject(received);
                    }catch (IOException | ClassNotFoundException |  InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                try {
                    this.ois.close();
                    this.oos.close();

                }catch(IOException  e){

                    e.printStackTrace();
                }
            }


        }catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public boolean func1(QueryDetails received)throws IOException, InterruptedException{

        System.out.println("Different Airlines");

        QueryDetails send = new QueryDetails();
        send.setServerType("airline");

        boolean closeItself=false;
        if(received.getMessage().equals("abort")){

            // UNLOCK THE ROW IN THE DB
            // releaseLock();
            // //////////

            // SEND A ABORT ACK TO AGENCY
            send.setMessage("abort_ack");
            send.setConnClose(true);
            oos.writeObject(send);
            closeItself=true;


            //connection will be closed query unsuccesfull

        }
        else if(received.getMessage().equals("prepare")){

            System.out.println(received.printAirlineQuery());


            boolean available=true;
            Integer flightId = received.getFlightId();
            Integer numSeats = received.getSeats();
            // SEARCH AND FIND IN DB
            ///////////////////
                    /*
                    if(search(flightId,numSeats);
                        available=true;
                    */
            ///////////////////



            // LOCK THE DB ROW
                    /*
                    setLock(flightId,numSeats);
                    */
            ///////////////////


            ///////////////////

            if(available){
                // send available message
                send.setMessage("ready");
                oos.writeObject(send);

            }else{
                send.setMessage("not_ready");
                oos.writeObject(send);
            }
        }
        else if (received.getMessage().equals("commit_request")){
            // MAKE CHANGES TO DB
                    /*
                        addFlightBooking();
                        releaseLock();
                    */
            Thread.sleep(1);
            // SEND COMMMITED TO AGENCY
            send.setConnClose(true);
            System.out.println("Commit Done");
            send.setMessage("commit_done");
            oos.writeObject(send);

            closeItself=true;
        }
        else if (received.getMessage().equals("abort_to")){
            System.out.println("TO by Other Server");
            // ROLLBACK
            closeItself=true;
        }




        if(closeItself||received.isConnClose()){
            this.isloggedin=false;
            this.s.close();
            return false;
        }

        return true;
    }

    public boolean func2(QueryDetails received)throws IOException{

        System.out.println("Same Airlines");

        QueryDetails send = new QueryDetails();
        send.setServerType("airline");

        boolean closeItself=false;
        if(received.getMessage().equals("abort")){

            // UNLOCK THE ROW IN THE DB
            // releaseLock();
            // //////////

            // SEND A ABORT ACK TO AGENCY
            send.setMessage("abort_ack");
            send.setConnClose(true);
            oos.writeObject(send);
            closeItself=true;


            //connection will be closed query unsuccesfull

        }
        else if(received.getMessage().equals("prepare")){

            System.out.println(received.printAirlineQuery());


            boolean available=true;
            Integer flightId = received.getFlightId();
            Integer numSeats = received.getSeats();
            // SEARCH AND FIND IN DB
            ///////////////////
                    /*
                    if(search(flightId,numSeats);
                        available=true;
                    */
            ///////////////////



            // LOCK THE DB ROW
                    /*
                    setLock(flightId,numSeats);
                    */
            ///////////////////


            ///////////////////

            if(available){
                // send available message
                send.setMessage("ready");
                oos.writeObject(send);

            }else{
                send.setMessage("not_ready");
                oos.writeObject(send);
            }
        }
        else if (received.getMessage().equals("commit_request")){
            // MAKE CHANGES TO DB
                    /*
                        addFlightBooking();
                        releaseLock();
                    */

            // SEND COMMMITED TO AGENCY
            send.setConnClose(true);
            System.out.println("Commit Done");
            send.setMessage("commit_done");
            oos.writeObject(send);

            closeItself=true;
        }



        if(closeItself||received.isConnClose()){
            this.isloggedin=false;
            this.s.close();
            return false;
        }

        return true;
    }
}