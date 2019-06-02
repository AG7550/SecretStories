package com.example.secretstoriesuiv01;

import java.io.Serializable;
/**
 *
 * @author Sandra Smrekar
 *
 * This class is used to send information about chat passwords and contains a boolean value to check if
 * passwords match
 *
 */
public class Lock implements Serializable{
    private String password;
    private Boolean valid = false;

    public Lock(String password) {
        this.password = password;
    }

    public String getpassword() {
        return this.password;
    }

    public void setValid(Boolean bol) {
        this.valid = bol;
    }

    public Boolean getValid() {
        return this.valid;
    }
}
