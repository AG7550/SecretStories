package com.example.secretstoriesuiv01;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
    private String[] chat =  {"Ali:Hejsan"};              // test
    private ArrayAdapter<String> chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_chat, container, false);
        chatAdapter = new ChatAdapter(this, username, chat);
        ListView lvwUsers = (ListView) v.findViewById(R.id.messages_view);
        lvwUsers.setAdapter(chatAdapter);

        return v;

    }

    // När man får ett meddelande så ska det skapas en ny bubbla.
    // Message ska vara i formatet --> username, textmeddelandet
    public void addBubbel(String[] message){
        TextView tvwBubble = (TextView) findViewById(R.id.message_body);
        if(this.username.equals(message[0])){
            tvwBubble.setBackground(getDrawable(R.drawable.my_message));
            tvwBubble.setText(message[1]);
        }
        else{
            tvwBubble.setBackground(getDrawable(R.drawable.their_message));
            tvwBubble.setText(message[1]);
        }
        // lägag till den till adaptorn
    }
}
