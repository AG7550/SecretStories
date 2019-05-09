package com.example.secretstoriesuiv01;

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
import android.widget.ListView;
import android.widget.TextView;

import static android.app.PendingIntent.getActivity;

public class ChattingActivity extends AppCompatActivity {
    private String username = "Ali";
    private String[] chat =  {"Ali:Hejsan:Sandra:Haaj"};              // test
    private ArrayAdapter<String> chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        chatAdapter = new ChatAdapter(this, username, chat);    //ändrat

        ListView lvwUsers = findViewById(R.id.messages_view);
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
