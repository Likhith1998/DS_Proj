package com.distsys.projects;

import java.io.Serializable;

public class Message implements Serializable {

    private boolean connClose;
    private String message;

    public boolean isConnClose() {
        return connClose;
    }

    public void setConnClose(boolean disconnect) {
        this.connClose = disconnect;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
