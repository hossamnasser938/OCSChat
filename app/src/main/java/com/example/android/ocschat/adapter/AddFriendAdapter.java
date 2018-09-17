package com.example.android.ocschat.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.ocschat.R;
import com.example.android.ocschat.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom Adapter for AutoComplete property when adding friends
 */
public class AddFriendAdapter extends ArrayAdapter<User> {

    private Context context;
    private final List<User> usersList;

    public AddFriendAdapter(Context context, final List<User> usersList) {
        super(context, 0);
        this.context = context;
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;

        if(listItem == null){
            listItem = LayoutInflater.from(context).inflate(R.layout.friend_item, parent, false);
        }

        User currentUser = getItem(position);

        TextView usernameTextView = listItem.findViewById(R.id.friend_item_username);
        ImageView userImageView = listItem.findViewById(R.id.friend_item_image);

        usernameTextView.setText(currentUser.getName());

        userImageView.setImageResource(R.drawable.person_placeholder);

        if(currentUser.getHasImage()){
            //TODO: need a way to check if the user a friend or not
            /*
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(currentUser.getImageFilePath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(bitmap != null)
                userImageView.setImageBitmap(bitmap);
            */
        }

        return listItem;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new AddFriendFilter();
    }

    private class AddFriendFilter extends Filter{
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((User) resultValue).getName();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(constraint != null){
                ArrayList<User> suggestions = new ArrayList<>();
                for(User user : usersList){
                    if(user.getName().toLowerCase().contains(constraint.toString().toLowerCase()))
                        suggestions.add(user);
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            }
            return new FilterResults();
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<User> filteredUsers = (ArrayList<User>) results.values;
            if(results.count > 0){
                clear();
                addAll(filteredUsers);
                notifyDataSetChanged();
            }else{
                notifyDataSetInvalidated();
            }
        }
    }
}
