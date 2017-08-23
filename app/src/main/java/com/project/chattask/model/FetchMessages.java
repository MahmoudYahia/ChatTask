package com.project.chattask.model;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.chattask.callback.OnfetchingMessagesListner;

import java.util.ArrayList;


/**
 * Created by mah_y on 8/22/2017.
 */

public class FetchMessages {
    DatabaseReference mDatabaseRef;
    ArrayList<Message> mMessagesList;
    OnfetchingMessagesListner mOnfetchingMessagesListner;

    public FetchMessages(FirebaseUser authUser, Contact SelectedUser, OnfetchingMessagesListner onfetchingMessagesListner) {

        mDatabaseRef = FirebaseDatabase.getInstance().getReference()
                .child("messeges")
                .child(authUser.getUid())
                .child(SelectedUser.getUid());
        mOnfetchingMessagesListner = onfetchingMessagesListner;
    }

    public void getMessages() {
        mMessagesList = new ArrayList<>();

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot Messages : dataSnapshot.getChildren()) {
                    Message message = Messages.getValue(Message.class);
                    mMessagesList.add(message);
                }
                mOnfetchingMessagesListner.onMessagesFetched(mMessagesList);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
