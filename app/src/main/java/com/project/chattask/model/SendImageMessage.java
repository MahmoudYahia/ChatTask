package com.project.chattask.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.chattask.datamodel.Contact;
import com.project.chattask.datamodel.Message;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by mah_y on 8/22/2017.
 */

public class SendImageMessage extends SendMessage {

    private StorageReference storageRef;
     Uri uri;
    private Bitmap mBitmapImage;

    public SendImageMessage(Context context, FirebaseUser authUser, Contact SelectedUser, String message, Bitmap bitmap) {
        super(context, authUser, SelectedUser, message);
        this.mBitmapImage=bitmap;
    }


    public void uploadPhotoToStorage() {

        //selected_img_toUpload.setImageBitmap(null); // remove img
        //sendMessage.setVisibility(View.INVISIBLE);  // deactivate send btn

        File file=new File(String.valueOf(uri));
        String name=file.getName();

        UploadTask uploadTask = storageRef.child("messagesPhotos")
                .child(name)
                .putFile(uri);

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
                Toast.makeText(mContext, "Added", Toast.LENGTH_LONG).show();
                @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();

               //Message(String receiverid, String senderid, String text, String name, String imageurl, String uploadedimg)
               Message message= new Message(selectedContat.getUid(),mFirebaseUser.getUid(),mTextMessage,mFirebaseUser.getPhotoUrl()+"",downloadUrl.toString());

            }
        });
    }

    public byte[] getImageBytes(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return imageBytes;
    }
}
