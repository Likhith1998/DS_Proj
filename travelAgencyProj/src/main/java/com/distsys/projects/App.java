package com.distsys.projects;


import com.distsys.projects.utils.Configuration;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class App {
    public static void main(String[] args) throws IOException {
        if(args.length == 0){
            System.out.println("Give command line arguments");
            return;
        }
        Yaml yaml = new Yaml();
        Configuration config;
        try( InputStream in = Files.newInputStream( Paths.get( args[ 2 ] ) ) ) {
            config = yaml.loadAs( in, Configuration.class );
            System.out.println( config.toString() );
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        if( args[0].equals("airline_server")){
            AirlineServer airline_server = new AirlineServer();
            airline_server.runServer(args[1], config);
        }
        if( args[0].equals("hotel_server")){
            HotelServer hotel_server = new HotelServer();
            hotel_server.runServer(args[1], config);
        }
        if( args[0].equals("agency_server")){
            AgencyServer agency_server = new AgencyServer();
            try {
                agency_server.runServer(args[1], config);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if( args[0].equals("client")){
            new UserClient().run();
        }
    }
}
