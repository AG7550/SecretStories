package com.example.secretstoriesuiv01;

import java.io.Serializable;

public class ClientHandler implements Serializable {
    private String username;
    private String password;
    public ClientHandler(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
