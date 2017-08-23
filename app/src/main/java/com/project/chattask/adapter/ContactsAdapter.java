package com.project.chattask.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseUser;
import com.project.chattask.model.Contact;
import com.project.chattask.callback.contacsAdapter.OnContactSelectedListner;
import com.project.chattask.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mah_y on 8/14/2017.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {
    private Context mContext;
    private List<Contact> ContactsList;
    private OnContactSelectedListner contactSelected;
    FirebaseUser mFirebaseUser;
    public ContactsAdapter(Context context, List<Contact> contacts, FirebaseUser firebaseUser, OnContactSelectedListner onContactSelectedListner) {
        this.ContactsList =contacts;
        this.mContext=context;
        this.contactSelected= onContactSelectedListner;
        this.mFirebaseUser=firebaseUser;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.contact_item,parent,false);
        ContactViewHolder viewHolder= new ContactViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ContactViewHolder holder, final int position) {
        if (mFirebaseUser.getUid().equals(ContactsList.get(position).getUid())){
            holder.contactName.setText(" Just  Me ");

        }
        else {
            holder.contactName.setText(ContactsList.get(position).getUname());
        }

        Glide.with(mContext)
                .load(ContactsList.get(position).getImgurl())
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                      //  holder.progressBar.setVisibility(View.GONE);
                        Glide.with(mContext).load(R.drawable.person_flat).into(holder.contactImg);
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        Glide.with(mContext)
                                .load(ContactsList.get(position).getImgurl()).into(holder.contactImg);
                        return true;
                    }
                })
                .fitCenter()
                .crossFade()
                .into(holder.contactImg);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactSelected.contactSelected(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ContactsList.size();
    }


class ContactViewHolder extends RecyclerView.ViewHolder{

    TextView contactName;
    CircleImageView contactImg;
         public ContactViewHolder(View itemView) {
             super(itemView);
             contactImg= (CircleImageView) itemView.findViewById(R.id.contact_item_img);
             contactName= (TextView) itemView.findViewById(R.id.contact_item_name);
         }

     }
}
