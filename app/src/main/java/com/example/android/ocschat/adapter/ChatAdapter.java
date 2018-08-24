package com.example.android.ocschat.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.ocschat.R;
import com.example.android.ocschat.model.Message;
import com.example.android.ocschat.util.Constants;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private Context context;
    private List<Message> messagesList;


    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView text, date, user;

        public ViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.message_text);
            date = itemView.findViewById(R.id.message_date);
            user = itemView.findViewById(R.id.message_user);
        }
    }

    public ChatAdapter(Context context, List<Message> messagesList) {
        this.context = context;
        this.messagesList = messagesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.message_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message currentMessage = messagesList.get(position);
        holder.text.setText(currentMessage.getText());
        holder.user.setText(currentMessage.getFromUser());
        holder.date.setText(DateFormat.format(Constants.DATE_FORMAT, currentMessage.getDate()));
    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }
}
