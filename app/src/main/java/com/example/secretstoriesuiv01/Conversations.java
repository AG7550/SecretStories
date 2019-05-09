package com.example.secretstoriesuiv01;

import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

public class Conversations extends AppCompatActivity {
    private int chatID;
    private ArrayList<String> chatMembers;
    private String conversation;

    public Conversations(int id, ArrayList<String> members, String convo){
        chatID = id;
        chatMembers = members;
        conversation = convo;
    }

    public int getChatID(){
        return chatID;
    }

    public ArrayList<String> getChatMembers() {
        return chatMembers;
    }

    public String getConversation() {
        return conversation;
    }

}
