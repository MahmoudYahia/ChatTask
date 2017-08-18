package com.project.chattask.Adpters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.project.chattask.Activities.MainActivity;
import com.project.chattask.Models.Contact;
import com.project.chattask.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mah_y on 8/14/2017.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {
    private Context mContext;
    private List<Contact> ContactsList;

    public ContactsAdapter(Context context, List<Contact> contacts) {
        this.ContactsList =contacts;
        this.mContext=context;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.contact_item,parent,false);
        ContactViewHolder viewHolder= new ContactViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ContactViewHolder holder, final int position) {
        holder.contactName.setText(ContactsList.get(position).getUname());

        Glide.with(mContext)
                .load(ContactsList.get(position).getImgurl())
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                      //  holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .fitCenter()
                .crossFade()
                .into(holder.contactImg);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.putExtra("contact_id",ContactsList.get(position));
                mContext.startActivity(intent);
            }
        });
        Log.i("testcount", ContactsList.size()+"");
    }

    @Override
    public int getItemCount() {
        return ContactsList.size();
    }


class ContactViewHolder extends RecyclerView.ViewHolder{

    TextView contactName;
    CircleImageView contactImg;
    ProgressBar progressBar;
         public ContactViewHolder(View itemView) {
             super(itemView);
             contactImg= (CircleImageView) itemView.findViewById(R.id.contact_item_img);
             contactName= (TextView) itemView.findViewById(R.id.contact_item_name);
             progressBar= (ProgressBar) itemView.findViewById(R.id.prgrss_bar);
         }

     }
}
