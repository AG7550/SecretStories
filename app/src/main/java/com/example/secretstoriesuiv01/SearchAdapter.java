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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class SearchAdapter extends ArrayAdapter<String> {
    String username;
    Context context;
    ArrayList<String> users;
    ArrayList<String> checkedUsers = new ArrayList<String>();


    SearchAdapter(Context context, String username, ArrayList users) {      // chat är chatthistoriken
        super(context, R.layout.activity_create_chat, users);
        this.username = username;
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater nameInflator = LayoutInflater.from(this.context);
        View customView = nameInflator.inflate(R.layout.checkbox_item, parent, false);
        CheckBox cbox = (CheckBox) customView.findViewById(R.id.checkBox);
        cbox.setText(users.get(position));
        cbox.setTag(Integer.valueOf(position));
        cbox.setOnCheckedChangeListener(mListener);
        return customView;
    }

    CompoundButton.OnCheckedChangeListener mListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(buttonView.isChecked()){
                setUser(buttonView.getText().toString());
            }
            else{
                removeUser(buttonView.getText().toString());
            }
        }
    };

    public void setUser(String username){
        CreateChatActivity.setUser(username);
    }
    public void removeUser(String username){
        CreateChatActivity.removeUser(username);
    }
}