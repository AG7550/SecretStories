package com.example.secretstoriesuiv01;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ChatAdapter extends ArrayAdapter<String> {
    String[][] messages;
    String username;
    Context context;
    private ArrayList<String> chat;

    ChatAdapter(Context context, String username, ArrayList chat) {      // chat är chatthistoriken
        super(context, R.layout.custom_row, chat);
        this.username = username;
        this.messages = new String[chat.size()][chat.size()];
        this.context = context;
        this.chat = chat;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater nameInflator = LayoutInflater.from(this.context);
        View customView = nameInflator.inflate(R.layout.my_message, parent, false);
        String[] temp = chat.get(position).split(":", 2);
        if (temp[0].equals(username)) {
            if(temp.length > 1){
                customView = nameInflator.inflate(R.layout.my_message, parent, false);
                TextView tvwBubble = customView.findViewById(R.id.message_body_right);
                tvwBubble.setBackground(getContext().getDrawable(R.drawable.my_message));
                tvwBubble.setText(temp[1]);}


        } else {
            if(temp.length > 1){
                customView = nameInflator.inflate(R.layout.their_message, parent, false);
                TextView tvwBubble = customView.findViewById(R.id.message_body_left);
                tvwBubble.setBackground(getContext().getDrawable(R.drawable.their_message));
                tvwBubble.setText(temp[1]);
            }

        }


        return customView;
    }
}