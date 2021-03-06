package com.example.secretstoriesuiv01;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import static com.example.secretstoriesuiv01.R.drawable.baseline_lock_open_black_18dp;

/**
 * @author Ali Menhem
 * This class handles the display of a users existing chats
 */
public class CustomAdapter extends ArrayAdapter<String> {
    private BtnClickListener mClickListener = null;
    public static Button ativeChat;
    public static Button activeLockBtn;

    CustomAdapter(Context context, String[] names, BtnClickListener listener){
        super(context, R.layout.custom_row, names);
        this.mClickListener = listener;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater nameInflator = LayoutInflater.from(getContext());
        String name = getItem(position);
        View customView = nameInflator.inflate(R.layout.fragment_chat, parent, false);
        if(name != null){
            customView = nameInflator.inflate(R.layout.custom_row, parent, false);

            final Button btnName = (Button) customView.findViewById(R.id.btnContact);
            final Button btnLock = customView.findViewById(R.id.btnLock);
            btnName.setTag(position);
            btnName.setText(name);
            btnName.setEnabled(false);
            btnLock.setTag(position);
            btnLock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (btnName.isEnabled()) {
                        btnName.setEnabled(false);
                        activeLockBtn = btnLock;
                        ativeChat = btnName;
                        Drawable img = getContext().getDrawable(R.drawable.baseline_lock_black_18dp);
                        img.setBounds(0,0,100,90);
                        CustomAdapter.activeLockBtn.setCompoundDrawables(null, null, null, img );
                        Toast.makeText(getContext(), "Chat has been locked.", Toast.LENGTH_SHORT).show();
                    } else {
                        ativeChat = btnName;
                        activeLockBtn = btnLock;
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());

                        //AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();

                        // Setting Dialog Title
                        alertDialog.setTitle("PASSWORD");

                        // Setting Dialog Message
                        alertDialog.setMessage("Enter Password");

                        final EditText input = new EditText(getContext());
                        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);
                        input.setLayoutParams(lp);
                        alertDialog.setView(input);
                        //alertDialog.setView(input);
                        // Setting Positive "Yes" Button
                        alertDialog.setPositiveButton("Unlock chat",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Write your code here to execute after dialog
                                        Lock lock = new Lock("");
                                        if (LoginActivity.client != null) {
                                            LoginActivity.client.verifyChatPassword(lock);
                                        } else {
                                            CreateAccountActivity.client.verifyChatPassword(lock);
                                        }
                                        dialog.cancel();
                                    }
                                });
                        alertDialog.show();

                    }
                }
            });


            btnName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mClickListener != null){
                        mClickListener.onBtnClick((Integer) v.getTag(), (String) btnName.getText());
                    }
                }
            });
        }

        return customView;
    }
    public static void setAtiveChat(boolean value){
        ativeChat.setEnabled(value);
    }
}
