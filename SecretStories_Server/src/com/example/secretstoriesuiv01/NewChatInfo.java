package com.example.secretstoriesuiv01;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
 * @author Klara Rosengren
 * 
 * Holds information about a new chat that is created, same as Conversation class but without a conversation string
 *
 */
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