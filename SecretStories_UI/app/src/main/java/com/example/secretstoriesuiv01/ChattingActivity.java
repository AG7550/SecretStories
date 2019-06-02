package com.example.secretstoriesuiv01;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import static android.app.PendingIntent.getActivity;

/**
 * @author Klara Rosengren, Ali Menhem, Jerry Rosengren
 * Activity for chatting between users
 */
public class ChattingActivity extends AppCompatActivity{
    public static boolean active = false;
    private static String username = "";
    private int chatID;
    private static ArrayList<String> chatMembers = new ArrayList<>();
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
                if(!tbxMessage.getText().toString().isEmpty()) {
                    String textMessage = tbxMessage.getText().toString();
                    Message message = new Message(textMessage, username, chatMembers, CreateChatActivity.convoID);
                    if (LoginActivity.client != null) {
                        LoginActivity.client.sendMessage(message);
                        tbxMessage.getText().clear();
                    } else {
                        CreateAccountActivity.client.sendMessage(message);
                        tbxMessage.getText().clear();
                    }
                }

            }
        });
        lvwUsers.setAdapter(chatAdapter);
        Button btnBack = (Button) findViewById(R.id.inChatBackButton);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public static ArrayList<String> getChatMembers(){
        ArrayList<String> tempMembers = new ArrayList<>(chatMembers);
        tempMembers.add(username);
        return tempMembers;
    }
    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }
    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }
}
