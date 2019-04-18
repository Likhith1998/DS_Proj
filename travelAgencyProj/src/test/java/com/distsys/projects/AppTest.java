package com.distsys.projects;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.IOException;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void dummyTest1() throws IOException, InterruptedException {
        AgencyServer server = new AgencyServer();
        server.runserver();
    }

    @Test
    public void dummyTest2() throws IOException {
        UserClient userClient = new UserClient();
        userClient.run();
    }
}
