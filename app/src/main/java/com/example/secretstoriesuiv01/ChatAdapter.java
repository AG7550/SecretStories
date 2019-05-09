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

import org.w3c.dom.Text;

public class ChatAdapter extends ArrayAdapter<String> {
    String[][] messages;
    String username;

    ChatAdapter(Context context, String username, String[] chat) {      // chat är chatthistoriken
        super(context, R.layout.custom_row, chat);
        this.username = username;
        formatChat(chat);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater nameInflator = LayoutInflater.from(getContext());
        View customView = nameInflator.inflate(R.layout.my_message, parent, false);
        String name = getItem(position);
        TextView tvwBubble = (TextView) customView.findViewById(R.id.message_body);

        if(messages[position][0].equals(username)){
            tvwBubble.setBackground(getContext().getDrawable(R.drawable.my_message));
            tvwBubble.setText(messages[position][1]);
        }
        else
        {
            tvwBubble.setBackground(getContext().getDrawable(R.drawable.their_message));
            tvwBubble.setText(messages[position][1]);
        }
        return customView;
    }
    public void formatChat(String[] chat){
        int row = 0;
        for(String message: chat){
            String[] tempMessage = message.split(":", 2);
            messages[row][0] = tempMessage[0];
            messages[row][1] = tempMessage[1];
            row++;
        }
    }
}