package com.project.chattask.model;
import android.content.Context;
import android.content.Intent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.chattask.callback.signIn.ApiClientBuilderListener;
import com.project.chattask.callback.signIn.CheckAuthorizationListener;
import com.project.chattask.callback.signIn.GoogleLoginAuthenticator;
import com.project.chattask.callback.signIn.EmailAndPassAuthenticator;
import com.project.chattask.datamodel.Contact;

/**
 * Created by mah_y on 8/22/2017.
 */

public class UserAuthentecation implements EmailAndPassAuthenticator,GoogleLoginAuthenticator {

    FirebaseAuth mFirebaseAuth;
    DatabaseReference mDatabaseRef;

    public UserAuthentecation() {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void authenticateEmailAndPass(String email, String pass, final CheckAuthorizationListener authorizationListener) {
        EmailAndPassAuthenticator emailAndPassAuthenticator=new EmailAndPassLogin();
        emailAndPassAuthenticator.authenticateEmailAndPass(email, pass, new CheckAuthorizationListener() {
            @Override
            public void onAutherizationSuccess() {
                authorizationListener.onAutherizationSuccess();
                addAuthUserToContacts(mFirebaseAuth.getCurrentUser());
            }

            @Override
            public void onAutherizationFailed() {
                authorizationListener.onAutherizationFailed();
            }
        });

    }

    @Override
    public void authGoogleAccount(Intent data, final CheckAuthorizationListener authorizationListener) {
        GoogleLoginAuthenticator googleLoginAuthenticator= new GoogleSignIn();
        googleLoginAuthenticator.authGoogleAccount(data, new CheckAuthorizationListener() {
            @Override
            public void onAutherizationSuccess() {
                addAuthUserToContacts(mFirebaseAuth.getCurrentUser());
                authorizationListener.onAutherizationSuccess();
            }
            @Override
            public void onAutherizationFailed() {
                authorizationListener.onAutherizationFailed();

            }
        });

    }

    @Override
    public void BuildApiClient(Context context, ApiClientBuilderListener apiClientBuilderListener) {
        GoogleLoginAuthenticator authenticator= new GoogleSignIn();
        authenticator.BuildApiClient(context,apiClientBuilderListener);
    }


    public void addAuthUserToContacts(FirebaseUser account) {

        String FormattedName;
        String ImgUrl;

        if (account.getDisplayName() == null) {
            String email = account.getEmail();
            FormattedName = email.substring(0, email.indexOf('@'));
        } else {

            FormattedName = account.getDisplayName();
        }

        if (account.getPhotoUrl() == null) {
            ImgUrl = null;
        } else {
            ImgUrl = account.getPhotoUrl().toString();
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
