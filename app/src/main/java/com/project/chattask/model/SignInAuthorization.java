package com.project.chattask.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.chattask.R;
import com.project.chattask.callBackInterface.onCheckAuthorizationListener;

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
