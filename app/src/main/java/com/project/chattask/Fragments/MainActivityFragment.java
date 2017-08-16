package com.project.chattask.Fragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.chattask.Activities.ContactsList;
import com.project.chattask.Activities.MainActivity;
import com.project.chattask.Activities.SignIn;
import com.project.chattask.Adpters.MessgesAdapter;
import com.project.chattask.Models.Contact;
import com.project.chattask.Models.Message;
import com.project.chattask.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements View.OnClickListener, TextWatcher {
    RecyclerView messageRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    EditText inputMessage;
    ImageButton sendMessage;

    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private Contact SelectedConttact;

    List <Message>messages;
    private FirebaseRecyclerAdapter<Message, MainActivity.MessageViewHolder> mFirebaseAdapter;
    // user Fields
    private String mUserName;
    private String Img_URl;
    private Message message;
    DatabaseReference databaseRef;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        inputMessage = (EditText) view.findViewById(R.id.messageEditText);
        sendMessage = (ImageButton) view.findViewById(R.id.sendButton);
        sendMessage.setEnabled(false);
        inputMessage.addTextChangedListener(this);
        sendMessage.setOnClickListener(this);


        if (getActivity().getIntent().getExtras() != null) {
            SelectedConttact = (Contact) getActivity().getIntent().getExtras().getSerializable("contact_id");
            Toast.makeText(getActivity(), SelectedConttact.getUname(), Toast.LENGTH_LONG).show();
        }

        messageRecyclerView = (RecyclerView) view.findViewById(R.id.messageRecyclerView);

        layoutManager = new LinearLayoutManager(getContext());
        messageRecyclerView.setLayoutManager(layoutManager);
        messageRecyclerView.setHasFixedSize(true);

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        if (mFirebaseUser != null) {
            // authenticated
            // fetching profile data
            mUserName = mFirebaseUser.getDisplayName();
            String id = mFirebaseUser.getUid();
            Log.i("UserId", id);

            if (mFirebaseUser.getPhotoUrl() != null) {
                Img_URl = mFirebaseUser.getPhotoUrl().toString();
            }

        }
        ReadMessages();


        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.sendButton:
                SendMessage();
                break;

        }
    }

    public void SendMessage() {

        // public Message(String senderid, String text, String name, String imageurl) {
        String photo_url;
        if (mFirebaseUser.getPhotoUrl()==null){
            photo_url =" def";
        }
        else{
            photo_url=mFirebaseUser.getPhotoUrl().toString();
        }


        message = new Message(mFirebaseUser.getUid()
                , inputMessage.getText().toString()
                , mFirebaseUser.getDisplayName()
                , photo_url);

        mDatabaseRef.child("messeges")
                .child(mFirebaseUser.getUid())
                .child(mFirebaseUser.getUid() + SelectedConttact.getUid())
                .push()
                .setValue(message);
        inputMessage.setText("");

        mDatabaseRef.child("messeges")
                .child(SelectedConttact.getUid())
                .child(SelectedConttact.getUid() + mFirebaseUser.getUid())
                .push()
                .setValue(message);
        inputMessage.setText("");
    }


    public void ReadMessages() {
        DatabaseReference mDatabaseRef;
        mDatabaseRef = FirebaseDatabase.getInstance().getReference()
                .child("messeges")
                .child(mFirebaseUser.getUid())
                .child(mFirebaseUser.getUid()+SelectedConttact.getUid());

        Log.i("wwwww",mFirebaseUser.getUid()+SelectedConttact.getUid());

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Map<String, Message> td = new HashMap<>();

                messages = new ArrayList<>(td.values());

                for (DataSnapshot Messages: dataSnapshot.getChildren()) {
                    Message message = Messages.getValue(Message.class);
                    messages.add(message);
                }

                MessgesAdapter messgesAdapter= new MessgesAdapter(getActivity(),messages);
                messageRecyclerView.setAdapter(messgesAdapter);
                messgesAdapter.notifyDataSetChanged();
                layoutManager.scrollToPosition(messages.size()-1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }




    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        checkText();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        checkText();
    }

    @Override
    public void afterTextChanged(Editable s) {
        checkText();
    }

    public void checkText() {
        if (TextUtils.isEmpty(inputMessage.getText().toString())) {
            sendMessage.setEnabled(false);
        } else {
            sendMessage.setEnabled(true);

        }
    }
}

