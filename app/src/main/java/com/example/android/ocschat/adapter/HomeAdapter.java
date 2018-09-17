package com.example.android.ocschat.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
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

import java.io.IOException;
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
        holder.image.setImageResource(R.drawable.person_placeholder);
        if(currentFriend.getHasImage()){
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(currentFriend.getImageFilePath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(bitmap != null)
                holder.image.setImageBitmap(bitmap);
        }
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    /**
     * clear friends list and notify adapter
     */
    public void clear(){
        final int size = getItemCount();

        if(size > 0){
            friendsList.clear();
            notifyItemRangeRemoved(0, size);
        }
    }
}
