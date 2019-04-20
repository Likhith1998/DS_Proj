package com.distsys.projects;

import com.distsys.projects.utils.Configuration;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    Configuration configuration = getConfig();

    @Test
    public void test1() throws ParseException {
        UserClient userClient = new UserClient();
        userClient.testrun();
    }

    @Test
    public void test2() throws IOException {
        AgencyServer server = new AgencyServer();
        server.runServer("primary", configuration);
    }

    @Test
    public void test3() throws IOException {
        AirlineServer server = new AirlineServer();
        server.runServer("primary", configuration);
    }

    @Test
    public void test4() throws IOException {
        HotelServer server = new HotelServer();
        server.runServer("primary", configuration);
    }

    public Configuration getConfig(){
        Yaml yaml = new Yaml();
        Configuration config;
        try( InputStream in = Files.newInputStream( Paths.get( "config.yml" ) ) ) {
            config = yaml.loadAs( in, Configuration.class );
            System.out.println( config.toString() );
            return config;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
