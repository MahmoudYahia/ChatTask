package com.project.chattask.Adpters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.project.chattask.Models.Message;
import com.project.chattask.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mah_y on 8/13/2017.
 */

public class MessgesAdapter extends RecyclerView.Adapter<MessgesAdapter.MyViewHolder>{
    List<Message>messages;
    Context mContext;
    public MessgesAdapter(Context mContext,List<Message> messages ) {
        this.messages = messages;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View  view;

        if (viewType==1){
            view=LayoutInflater.from(mContext).inflate(R.layout.message_item_me,parent,false);
        }
        else {
            view=LayoutInflater.from(mContext).inflate(R.layout.message_item,parent,false);
        }


        MyViewHolder holder= new MyViewHolder(view);
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).getSenderid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            return 1; // me
        }
        else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.name.setText(messages.get(position).getName());
        holder.MessageContent.setText(messages.get(position).getText());
        Glide.with(mContext).load(messages.get(position).getImageurl()).placeholder(R.drawable.person_flat).into(holder.imgView);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        CircleImageView imgView;
        TextView name;
        TextView MessageContent;
        public MyViewHolder(View itemView) {
            super(itemView);
            imgView= (CircleImageView) itemView.findViewById(R.id.messengerImageView);
            name= (TextView) itemView.findViewById(R.id.messengerTextView);
            MessageContent= (TextView) itemView.findViewById(R.id.messageTextView);

        }
    }
}
