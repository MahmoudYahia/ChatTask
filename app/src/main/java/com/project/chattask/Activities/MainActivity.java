package com.project.chattask.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.project.chattask.Models.Contact;
import com.project.chattask.Models.Message;
import com.project.chattask.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener {

    // FireBase fields
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    int request_code_contacts = 100;


    private FirebaseRecyclerAdapter<Message, MessageViewHolder>
            mFirebaseAdapter;
    // user Fields

    private Contact SelectedConttact;

    CircleImageView personImg;
    TextView personName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        personImg = (CircleImageView) toolbar.findViewById(R.id.chat_bar_img);
        personName = (TextView) toolbar.findViewById(R.id.chat_bar_name);



        if (getIntent().getExtras() != null) {
            SelectedConttact = (Contact) getIntent().getExtras().getSerializable("contact_id");

            // Toast.makeText(this,,Toast.LENGTH_LONG).show();
            if (SelectedConttact.getUname() != null) {
                if (mFirebaseUser.getUid().equals(SelectedConttact.getUid())){
                    personName.setText(" Just  Me ");
                }
                else {
                    personName.setText(SelectedConttact.getUname());
                }

                Glide.with(this).load(SelectedConttact.getImgurl()).listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        Glide.with(MainActivity.this).load(SelectedConttact.getImgurl()).into(personImg);

                        return false;
                    }
                }).placeholder(R.drawable.person_flat).into(personImg);
            }
            else {
                personName.setText("SomeBody");
            }
        }
        //                        Glide.with(MainActivity.this).load(SelectedConttact.getImgurl()).into(personImg)

/*
        mFirebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in
            startActivity(new Intent(this, SignIn.class));
            finish();
            return;
        }
*/

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public MessageViewHolder(View itemView) {
            super(itemView);
        }
    }
}
