package com.example.secretstoriesuiv01;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class CustomAdapter extends ArrayAdapter<String> {
    private BtnClickListener mClickListener = null;

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
            btnName.setTag(position);
            btnName.setText(name);
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
}
