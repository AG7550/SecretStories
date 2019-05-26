package com.example.secretstoriesuiv01;

import java.io.Serializable;
import java.util.ArrayList;

public class NewChatInfo implements Serializable {
    private int id;
    private ArrayList<String> members;

    public NewChatInfo(int id, ArrayList<String> members){
        this.id = id;
        this.members = members;
    }
   
    public int getId() {
        return id;
    }
    

    public ArrayList<String> getMembers() {
        return members;
    }

    

    
    
   
    
    
}