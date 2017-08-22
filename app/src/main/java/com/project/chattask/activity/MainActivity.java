package com.project.chattask.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.project.chattask.model.Contact;
import com.project.chattask.R;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener {


    private FirebaseUser mFirebaseUser;
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
                if (mFirebaseUser.getUid().equals(SelectedConttact.getUid())) {
                    personName.setText(" Just  Me ");
                } else {
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
            } else {
                personName.setText("SomeBody");
            }
        }

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this,R.string.errorFitchingData,Toast.LENGTH_LONG).show();
    }

}
