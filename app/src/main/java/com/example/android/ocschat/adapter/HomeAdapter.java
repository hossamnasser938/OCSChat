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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.ocschat.R;
import com.example.android.ocschat.model.HasImage;
import com.example.android.ocschat.model.User;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private Context context;
    private List<User> friendsList;
    private int lastPosition = -1;

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
        switch(currentFriend.getHasImage()){
            case IMAGE_LOCALLY: {

                break;
            }
            case IMAGE_ON_FIREBASE: {

                break;
            }
            default: {
                holder.image.setImageResource(R.drawable.person_placeholder);
            }
        }
        setAnimation(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    private void setAnimation(View viewToAnimate, int position){
        if(position != lastPosition){
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
            viewToAnimate.setAnimation(animation);
            lastPosition= position;
        }
    }
}
