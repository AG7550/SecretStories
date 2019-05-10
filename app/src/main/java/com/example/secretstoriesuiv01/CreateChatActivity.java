package com.example.secretstoriesuiv01;

import android.content.ClipData;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

public class CreateChatActivity extends AppCompatActivity {
    private static ListView lstSearch;
    private EditText editSearch;
    public static ArrayAdapter<String> adapter;
    public static ArrayList<String> data = new ArrayList<String>();
    public static ArrayList<String> checkedUsers = new ArrayList<String>();



    public void setData(String[] data){
        this.data = new ArrayList<String>();
        for(String user : data)
        {
            this.data.add(user);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chat);

        lstSearch = (ListView) findViewById(R.id.lstSearch);
        editSearch = (EditText) findViewById(R.id.searchField);
        adapter = new SearchAdapter(this,"", data);
        lstSearch.setAdapter(adapter);
        lstSearch.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);




        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CreateChatActivity.this.adapter.getFilter().filter(s);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        final Button createBtn = findViewById(R.id.createChatbtn);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                  int id = ChatFragment.nameAdapter.getCount() + 1;
                  boolean self = false;
                  boolean chatExists = false;

                  if(LoginActivity.client != null){
                      for(String user : checkedUsers){
                          if(user.equals(LoginActivity.client.getUsername())){
                              self = true;
                          }
                      }
                      if(self){
                          Toast.makeText(getApplicationContext(), "Cannot create a chat with yourself.", Toast.LENGTH_LONG).show();
                          self = false;
                      }
                      else if(duplicateChat()){
                          Toast.makeText(getApplicationContext(), "Chat with selected users already exists.", Toast.LENGTH_LONG).show();
                      }
                      else {
                          checkedUsers.add(LoginActivity.client.getUsername());         // här blir rätt
                          NewChatInfo chatInfo = new NewChatInfo(id, checkedUsers);
                          LoginActivity.client.createChat(chatInfo);
                          Log.d("H", chatInfo.getMembers().toString());
                      }


                  }
                else{
                      for(String user : checkedUsers){
                          if(user.equals(CreateAccountActivity.client.getUsername())){
                              self = true;
                          }
                      }
                      if(self){
                          Toast.makeText(getApplicationContext(), "Cannot create a chat with yourself.", Toast.LENGTH_LONG).show();
                          self = false;
                      }
                      else if(duplicateChat()){
                          Toast.makeText(getApplicationContext(), "Chat with selected users already exists.", Toast.LENGTH_LONG).show();
                      }
                      else {
                          checkedUsers.add(CreateAccountActivity.client.getUsername());
                          NewChatInfo chatInfo = new NewChatInfo(id, checkedUsers);
                          CreateAccountActivity.client.createChat(chatInfo);
                          Log.d("H", chatInfo.getMembers().toString());
                      }


                  }
                // skriva till servern och lägga till chat
            }

        });

    }
    public static void setUser(String username){
        checkedUsers.add(username);
    }
    public static void removeUser(String username){
        checkedUsers.remove(username);
    }
    public ArrayList<String> getExistingChats(){
        ArrayList<String> names = new ArrayList<String>();
        String[] temp = ChatFragment.namesList;
        for(String name : temp){
            names.add(name);
        }
        return names;
    }
    public boolean duplicateChat(){
        boolean exists = false;
        ArrayList<String> names1 = getExistingChats();
        Collections.sort(names1);
        ArrayList<String> names2 = checkedUsers;
        Collections.sort(names2);
        for(String users : names1){
            if(users != null) {
                if (users.equals(names2.toString().substring(1, names2.toString().length() - 1))) {
                    exists = true;
                }
            }
        }

        return exists;
    }

}
