package com.project.chattask.model;

import android.content.Context;

import com.google.firebase.auth.FirebaseUser;
import com.project.chattask.datamodel.Contact;

/**
 * Created by mah_y on 8/22/2017.
 */

public class SendMessage {

    Context mContext;
    FirebaseUser mFirebaseUser;
    Contact selectedContat;
    String mTextMessage;

    public SendMessage(Context context, FirebaseUser authUser,Contact SelectedUser,String message) {
        this.mContext=context;
        mFirebaseUser=authUser;
        selectedContat=SelectedUser;
        this.mTextMessage=message;
    }




}
