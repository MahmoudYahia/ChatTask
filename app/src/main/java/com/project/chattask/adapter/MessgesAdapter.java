package com.project.chattask.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseAuth;
import com.project.chattask.model.Message;
import com.project.chattask.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mah_y on 8/13/2017.
 */

public class MessgesAdapter extends RecyclerView.Adapter<MessgesAdapter.MyViewHolder> {
    List<Message> messages;
    Context mContext;


    public MessgesAdapter(Context mContext, List<Message> messages) {
        this.messages = messages;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;

        if (viewType == 1) {
            view = LayoutInflater.from(mContext).inflate(R.layout.message_item_me, parent, false);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.message_item, parent, false);
        }


        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).getSenderid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            return 1; // me
        } else {
            return super.getItemViewType(position);
        }
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
//        holder.name.setText(messages.get(position).getName());
        holder.MessageContent.setText(messages.get(position).getText());

        int next = position + 1;
        if (next < messages.size()) {
            if (messages.get(position).getSenderid().equals(messages.get(next).getSenderid())) {
                holder.imgView.setVisibility(View.INVISIBLE);
            }
        }

        Glide.with(mContext).load(messages.get(position).getImageurl()).placeholder(R.drawable.person_flat).into(holder.imgView);
        Picasso.with(mContext).load(messages.get(position).getUploadedimg()).into(holder.Img_Msg);

        if (!messages.get(position).getUploadedimg().equals("null")) {

            Glide.with(mContext).load(messages.get(position).getUploadedimg())
                    .placeholder(R.drawable.person_flat)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            Glide.with(mContext).load(messages.get(position).getUploadedimg()).into(holder.Img_Msg);
                            return true;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            Glide.with(mContext).load(messages.get(position).getUploadedimg()).into(holder.Img_Msg);
                            return true;
                        }
                    }).centerCrop().into(holder.Img_Msg);
        }
        else {
            holder.Img_Msg.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imgView;
        TextView name;
        TextView MessageContent;
        ImageView Img_Msg;

        public MyViewHolder(View itemView) {
            super(itemView);
            imgView = (CircleImageView) itemView.findViewById(R.id.messengerImageView);
            // name= (TextView) itemView.findViewById(R.id.messengerTextView);
            MessageContent = (TextView) itemView.findViewById(R.id.messageTextView);
            Img_Msg = (ImageView) itemView.findViewById(R.id.img_message);

        }
    }
}
