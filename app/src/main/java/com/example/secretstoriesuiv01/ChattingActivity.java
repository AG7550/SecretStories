package com.example.secretstoriesuiv01;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import static android.app.PendingIntent.getActivity;

public class ChattingActivity extends AppCompatActivity{
    private String username = "";
    private int chatID;
    private ArrayList<String> chatMembers = new ArrayList<>();
   // private String[] chat =  {"Ali:Hejsan:Sandra:Haaj"};              // test
    public static ArrayList<String> list = new ArrayList<>();
    public static ArrayAdapter<String> chatAdapter;
    private Intent intent;

    public ChattingActivity(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        intent = getIntent();
        if(savedInstanceState == null) {
            Bundle bundle = getIntent().getExtras();
            if(bundle == null){
                list = null;
            }
            else{
                username = bundle.getString("username");
                list = bundle.getStringArrayList("conversationList");
                chatMembers = bundle.getStringArrayList("chatMembers");
                chatMembers.remove(username);
                chatID = bundle.getInt("chatID");
            }
        }
        chatAdapter = new ChatAdapter(this, username, list);
        ListView lvwUsers = findViewById(R.id.messages_view);
        TextView tbarName = findViewById(R.id.tbarName);
        ArrayList<String> tempNames = chatMembers;
        tempNames.remove(username);
        tbarName.setText(tempNames.toString().substring(1, tempNames.toString().length()-1));
        final EditText tbxMessage = findViewById(R.id.tbxMessage);
        ImageView btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> members = new ArrayList<String>();

                String textMessage = tbxMessage.getText().toString();
                Message message = new Message(textMessage, username, chatMembers);
                if(LoginActivity.client != null){
                    LoginActivity.client.sendMessage(message);
                    tbxMessage.getText().clear();
                    //Resten måste hända efter klienten har fått objekt och gör "putExtra"
                    //Bundle bundle = getIntent().getExtras();
                    //String txtMessage = bundle.getString("newMessage");
                    //list.add(username + ":" + txtMessage);
                    //chatAdapter.notifyDataSetChanged();
                }
                else{
                    CreateAccountActivity.client.sendMessage(message);
                    tbxMessage.getText().clear();
                    //Bundle bundle = getIntent().getExtras();
                    //String txtMessage = bundle.getString("newMessage");
                    //list.add(username + "" + txtMessage);
                    //chatAdapter.notifyDataSetChanged();
                }

            }
        });
        lvwUsers.setAdapter(chatAdapter);

    }


//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        // Inflate the layout for this fragment
//        View v = inflater.inflate(R.layout.activity_chat, container, false);
//        chatAdapter = new ChatAdapter(this, username, chat);
//        ListView lvwUsers = (ListView) v.findViewById(R.id.messages_view);
//        lvwUsers.setAdapter(chatAdapter);
//
//        return v;
//
//    }


}
