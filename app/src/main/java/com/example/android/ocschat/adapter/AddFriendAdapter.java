package com.example.android.ocschat.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.example.android.ocschat.R;
import com.example.android.ocschat.model.User;

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

        usernameTextView.setText(currentUser.getName());

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
