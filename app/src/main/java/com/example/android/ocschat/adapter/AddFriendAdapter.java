package com.example.android.ocschat.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.ocschat.R;
import com.example.android.ocschat.model.User;

import java.util.List;

public class AddFriendAdapter extends ArrayAdapter<User> {

    private Context context;
    private List<User> friendsList;

    public AddFriendAdapter(@NonNull Context context, List<User> friendsList) {
        super(context, 0, friendsList);
        this.context = context;
        this.friendsList = friendsList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null){
            listItem = LayoutInflater.from(context).inflate(R.layout.friend_item, parent, false);
        }

        User currentUser = friendsList.get(position);

        TextView usernameTextView = listItem.findViewById(R.id.friend_item_username);

        usernameTextView.setText(currentUser.getName());

        return listItem;
    }
}
