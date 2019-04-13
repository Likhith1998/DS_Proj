package com.distsys.projects.utils;

import java.util.List;
import java.util.Map;

import static java.lang.String.format;

public class Configuration {
    private MySqlConn connection;
    private List< String > protocols;
    private Map< String, String > users;

    public MySqlConn getConnection() {
        return connection;
    }

    public void setConnection(MySqlConn connection) {
        this.connection = connection;
    }

    public List< String > getProtocols() {
        return protocols;
    }

    public void setProtocols(List< String > protocols) {
        this.protocols = protocols;
    }

    public Map< String, String > getUsers() {
        return users;
    }

    public void setUsers(Map< String, String > users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append( format( "Connecting to database: %s\n", connection ) )
                .append( format( "Supported protocols: %s\n", protocols ) )
                .append( format( "Users: %s\n", users ) )
                .toString();
    }
}
