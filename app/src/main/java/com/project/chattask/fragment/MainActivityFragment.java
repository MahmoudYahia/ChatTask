package com.project.chattask.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.example.imagespickmodule.AndroidSingleImagePicker;
import com.example.imagespickmodule.BaseAndroidImagePicker;
import com.example.imagespickmodule.IImagePickerHandler;
import com.example.imagespickmodule.ISingleImagePickerCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.chattask.adapter.MessgesAdapter;
import com.project.chattask.callback.OnfetchingMessagesListner;
import com.project.chattask.model.Contact;
import com.project.chattask.model.FetchMessages;
import com.project.chattask.model.Message;
import com.project.chattask.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements View.OnClickListener,
        TextWatcher,
        ISingleImagePickerCallback,
        IImagePickerHandler,OnfetchingMessagesListner {

    RecyclerView messageRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    MessgesAdapter messgesAdapter;
    //MessgesAdapter messgesAdapter;


    EditText inputMessage;
    ImageButton sendMessage, SelectImgBtn;

    ImageView selected_img_toUpload;

    private DatabaseReference mDatabaseRef;
    private FirebaseUser mFirebaseUser;
    private Contact SelectedConttact;
    private StorageReference storageRef;


    List<Message> messageList;
   // List<String> MessagesIds;
    // user Fields

    private Message message;

    Bitmap selectedBitmapImgToUpload;
    FirebaseStorage storage;
    boolean IfimgSelected = false;

    String sendedImgName;
    String uploadedImgUrl;

    BaseAndroidImagePicker imagePicker;


    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        inputMessage = (EditText) view.findViewById(R.id.messageEditText);
        sendMessage = (ImageButton) view.findViewById(R.id.sendButton);
        selected_img_toUpload = (ImageView) view.findViewById(R.id.selected_img_toUpload);
        SelectImgBtn = (ImageButton) view.findViewById(R.id.select_img);
        SelectImgBtn.setOnClickListener(this);
        inputMessage.addTextChangedListener(this);
        sendMessage.setOnClickListener(this);
        imagePicker = new AndroidSingleImagePicker(this, this);


        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        if (getActivity().getIntent().getExtras() != null) {
            SelectedConttact = (Contact) getActivity().getIntent().getExtras().getSerializable("contact_id");
            Toast.makeText(getActivity(), SelectedConttact.getUname(), Toast.LENGTH_LONG).show();
        }

        messageRecyclerView = (RecyclerView) view.findViewById(R.id.messageRecyclerView);

        layoutManager = new LinearLayoutManager(getContext());
        messageRecyclerView.setLayoutManager(layoutManager);
        messageRecyclerView.setHasFixedSize(true);

        messageList = new ArrayList<>();
        messgesAdapter = new MessgesAdapter(getActivity(), messageList);

        messageRecyclerView.setAdapter(messgesAdapter);

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        if (isOnline(getActivity())) {
            readMessages();

        } else {
            Toast.makeText(getActivity(),R.string.noConnection,Toast.LENGTH_LONG).show();
        }
        return view;

    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.sendButton:
                // sendMessage();
                checkToUpload();
                break;

            case R.id.select_img:
                //  showFileChooser();
                imagePicker.openPicker();
                break;
        }
    }

    public void sendMessage(Message UploadMSg) {


        //message=createMessageInstance();

        if (isOnline(getActivity())){
            mDatabaseRef.child("messeges")
                    .child(mFirebaseUser.getUid())
                    .child(SelectedConttact.getUid())
                    .push()
                    .setValue(UploadMSg);

            // back up messages at
            if (!SelectedConttact.getUid().equals(mFirebaseUser.getUid())) {

                mDatabaseRef.child("messeges")
                        .child(SelectedConttact.getUid())
                        .child(mFirebaseUser.getUid())
                        .push()
                        .setValue(UploadMSg);
            }

            inputMessage.setText("");
        }
        else {
            Toast.makeText(getActivity(),R.string.noConnection,Toast.LENGTH_LONG).show();
        }

    }

    public void checkToUpload() {
        if (isOnline(getActivity())){

            if (IfimgSelected) {
                uploadPhotoToStorage();
            } else {
                sendMessage(createMessageInstance());
            }
        }
        else {
            Toast.makeText(getActivity(),R.string.noConnection,Toast.LENGTH_LONG).show();
        }

    }

    private Message createMessageInstance() {


        String typedMessage = inputMessage.getText().toString().trim();

        String photo_url;

        String uploadedMsgImg;

        if (mFirebaseUser.getPhotoUrl() == null) {
            photo_url = "def";
        } else {
            photo_url = mFirebaseUser.getPhotoUrl().toString();
        }

        if (IfimgSelected) {
            uploadedMsgImg = uploadedImgUrl;
        } else {
            uploadedMsgImg = "null";
        }
        //Message(String receiverid, String senderid, String text, String name, String imageurl, String uploadedimg)

        message = new Message(
                SelectedConttact.getUid()
                , mFirebaseUser.getUid()
                , typedMessage
                , mFirebaseUser.getDisplayName()
                , photo_url
                , uploadedMsgImg);


        return message;
    }

    public void readMessages() {
        FetchMessages fetchMessages= new FetchMessages(mFirebaseUser,SelectedConttact,this);
        fetchMessages.getMessages();

    }

    public void uploadPhotoToStorage() {
        //  storageRef.child("MessagesPhotos");

        selected_img_toUpload.setImageBitmap(null); // remove img
        sendMessage.setVisibility(View.INVISIBLE);  // deactivate send btn

        UploadTask uploadTask = storageRef.child("messagesPhotos")
                .child(sendedImgName)
                .putBytes(getImageBytes(selectedBitmapImgToUpload));

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.i("errrr", exception.toString());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Toast.makeText(getActivity(), "Added", Toast.LENGTH_LONG).show();

                @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                if (downloadUrl != null) {
                    uploadedImgUrl = downloadUrl.toString();
                } else {
                    uploadedImgUrl = null;
                }

                sendMessage(createMessageInstance());
                IfimgSelected = false; // change flag after sending

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagePicker.onActivityResult(requestCode, resultCode, data);
    }
    public byte[] getImageBytes(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return imageBytes;
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
        if (!TextUtils.isEmpty(inputMessage.getText().toString())) {
            sendMessage.setVisibility(View.VISIBLE);
        } else {
            sendMessage.setVisibility(View.INVISIBLE);

        }
    }


    @Override
    public void onUriReceived(Uri resultUri) {
        try {
            selectedBitmapImgToUpload = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), resultUri);
            File file = new File(resultUri.getPath());
            if (file.getName() != null) {
                sendedImgName = file.getName();
            } else {
                sendedImgName = "null";
            }

            selected_img_toUpload.setImageBitmap(selectedBitmapImgToUpload);
            sendMessage.setVisibility(View.VISIBLE);
            IfimgSelected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNoThingSelected() {

    }

    @Override
    public void startPickerHandler(Intent data, int requestCode) {
        startActivityForResult(Intent.createChooser(data,
                "Select Picture"), requestCode);
    }


    @Override
    public void onMessagesFetched(ArrayList<Message> messagesList) {
        this.messageList.addAll(messagesList);
        messgesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onfetchMessagesFailed() {
        Toast.makeText(getActivity(),getString(R.string.errorFitchingData),Toast.LENGTH_LONG).show();
    }
}

