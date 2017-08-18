package com.project.chattask.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.chattask.Activities.MainActivity;
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

    List<Message> Messages;
    List<String> MessagesIds;
    private FirebaseRecyclerAdapter<Message, MainActivity.MessageViewHolder> mFirebaseAdapter;
    // user Fields

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
        if (mFirebaseUser.getPhotoUrl() == null) {
            photo_url = " def";
        } else {
            photo_url = mFirebaseUser.getPhotoUrl().toString();
        }

        String typeedMessage= inputMessage.getText().toString().trim();
        //public Message(String messageid, String receiverid, String senderid, String text, String name, String imageurl)

        message = new Message(SelectedConttact.getUid()
                , mFirebaseUser.getUid()
                , typeedMessage
                , mFirebaseUser.getDisplayName()
                , photo_url);
/*
        message = new Message(mFirebaseUser.getUid(),
                , inputMessage.getText().toString()
                , mFirebaseUser.getDisplayName()
                , photo_url);
*/
        mDatabaseRef.child("messeges")
                .child(mFirebaseUser.getUid())
                .child(SelectedConttact.getUid())
                .push()
                .setValue(message);

        // back up messages at
        if (!SelectedConttact.getUid().equals(mFirebaseUser.getUid())){

            mDatabaseRef.child("messeges")
                    .child(SelectedConttact.getUid())
                    .child(mFirebaseUser.getUid())
                    .push()
                    .setValue(message);
        }

        inputMessage.setText("");
    }


    public void ReadMessages() {

        DatabaseReference mDatabaseRef;
        mDatabaseRef = FirebaseDatabase.getInstance().getReference()
                .child("messeges")
                .child(mFirebaseUser.getUid())
                .child(SelectedConttact.getUid());

        Log.i("wwwww", mFirebaseUser.getUid() + SelectedConttact.getUid());

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Map<String, Message> td = new HashMap<>();

                Messages = new ArrayList<>(td.values());
                MessagesIds= new ArrayList<>(td.keySet());


                for (DataSnapshot Messages : dataSnapshot.getChildren()) {
                    Message message = Messages.getValue(Message.class);
                    MainActivityFragment.this.Messages.add(message);
//
                    String string = Messages.getKey();
                    MessagesIds.add(string);
                }

                MessgesAdapter messgesAdapter = new MessgesAdapter(getActivity(), Messages);
                messageRecyclerView.setAdapter(messgesAdapter);
                messgesAdapter.notifyDataSetChanged();
                layoutManager.scrollToPosition(Messages.size() - 1);
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

