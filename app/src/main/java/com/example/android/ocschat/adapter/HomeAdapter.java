package com.example.android.ocschat.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.ocschat.R;
import com.example.android.ocschat.model.User;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private Context context;
    private List<User> friendsList;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.friend_item_username);
            image = itemView.findViewById(R.id.friend_item_image);
        }
    }

    public HomeAdapter(Context context, List<User> friendsList) {
        this.context = context;
        this.friendsList = friendsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.friend_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User currentFriend = friendsList.get(position);
        holder.name.setText(currentFriend.getName());
        if(currentFriend.getHasImage()){
            //TODO: Postponed functionality
        }
        else{
            holder.image.setImageResource(R.drawable.person_placeholder);
        }
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }
}
