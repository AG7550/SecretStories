package com.example.secretstoriesuiv01;

import android.content.ClipData;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class CreateChatActivity extends AppCompatActivity {
    private static ListView lstSearch;
    private EditText editSearch;
    public static ArrayAdapter<String> adapter;
    public static String[] data = {"Inga användare"}; //för att den inte ska vara null



    public void setData(String[] data){

        this.data = data;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chat);

        lstSearch = (ListView) findViewById(R.id.lstSearch);
        editSearch = (EditText) findViewById(R.id.searchField);
        adapter = new ArrayAdapter<String>(this, R.layout.checkbox_item,R.id.textView, data);
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
                ArrayList<String> memberlist = new ArrayList<String>();
//
//               for(int pos = 0; pos < lstSearch.getAdapter().getCount(); pos++){
//                   CheckBox box = (CheckBox) lstSearch.getAdapter().getItem(pos);
//                   if(box.isChecked()){
//                       memberlist.add(box.getText().toString());
//                   }
//               }





                SparseBooleanArray checked = lstSearch.getCheckedItemPositions();
                for(int i = 0; i < lstSearch.getAdapter().getCount(); i++){
                    if(checked.get(i)){

                        memberlist.add(adapter.getItem(i));
                    }
                }









                int id = ChatFragment.nameAdapter.getCount();       // -1
                NewChatInfo chatInfo = new NewChatInfo(id,memberlist);
                if(LoginActivity.client != null){
                    LoginActivity.client.createChat(chatInfo);
                }
                else{
                    CreateAccountActivity.client.createChat(chatInfo);
                }
                // skriva till servern och lägga till chat
            }
        });
    }


}
