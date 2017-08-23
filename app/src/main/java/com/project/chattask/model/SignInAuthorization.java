package com.project.chattask.model;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.chattask.callback.signIn.onCheckAuthorizationListener;

/**
 * Created by mah_y on 8/22/2017.
 */

public class SignInAuthorization {
    Context mContext;
    FirebaseAuth mFirebaseAuth;
    DatabaseReference mDatabaseRef;
    onCheckAuthorizationListener mOnCheckAuthorizationListner;

    public SignInAuthorization(Context context, onCheckAuthorizationListener onCheckAuthorizationListener) {
        this.mContext = context;
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mOnCheckAuthorizationListner = onCheckAuthorizationListener;
    }

    public void addAuthUserToContacts(FirebaseUser account) {


        String FormattedName;
        String ImgUrl;

        if (account.getDisplayName()==null){
            String email = account.getEmail();
            FormattedName = email.substring(0, email.indexOf('@'));
        }
        else {

            FormattedName= account.getDisplayName();
        }

        if (account.getPhotoUrl()==null){
            ImgUrl= null;
        }else {
            ImgUrl=account.getPhotoUrl().toString();
        }


        Contact contact = new Contact(
                account.getUid()
                , FormattedName
                , account.getEmail()
                , ImgUrl);


        mDatabaseRef.child("Contacts")
                .child(account.getUid())
                .setValue(contact);


    }


}
