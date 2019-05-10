package com.example.secretstoriesuiv01;

import android.support.v7.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;

public class Conversations implements Serializable {
    private int chatID;
    private ArrayList<String> chatMembers;
    private ArrayList<String> conversation;

    public Conversations(int id, ArrayList<String> members, ArrayList<String> convo){
        chatID = id;
        chatMembers = members;
        conversation = convo;
    }
    public Conversations(ArrayList<String> members){
        chatMembers = members;
    }

    public int getChatID(){
        return chatID;
    }

    public ArrayList<String> getChatMembers() {
        return chatMembers;
    }

    public ArrayList<String> getConversation() {
        return conversation;
    }

    public void setChatID(int id){
        chatID = id;
    }
    public void setChatMembers(ArrayList<String> chatMembers){
        this.chatMembers = chatMembers;
    }
    public void setConversation(ArrayList<String> convo){
        this.conversation = convo;
    }

}
