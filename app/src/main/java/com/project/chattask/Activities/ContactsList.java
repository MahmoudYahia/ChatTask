package com.project.chattask.Activities;
import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AlertDialogLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.chattask.Adpters.ContactsAdapter;
import com.project.chattask.Models.Contact;
import com.project.chattask.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactsList extends AppCompatActivity {

    DatabaseReference databaseRef;
    RecyclerView ContactsRecycler;
    RecyclerView.LayoutManager layoutManager;
    ContactsAdapter ContactsAdapter;
    List<Contact> ContactsList;
    private FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_list);

        mFirebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in
            startActivity(new Intent(this, SignIn.class));
            finish();
            return;
        }
        ContactsRecycler = (RecyclerView) findViewById(R.id.contacts_recycler);
        layoutManager=new LinearLayoutManager(this);
        ContactsRecycler.setLayoutManager(layoutManager);
        ContactsRecycler.setHasFixedSize(true);

        ContactsList = new ArrayList<>();


        databaseRef = FirebaseDatabase.getInstance().getReference().child("Contacts");
        if (isOnline(getBaseContext())){
            databaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildren()!=null){
                        Map<String, Contact> td = new HashMap<>();
                        for (DataSnapshot contacts: dataSnapshot.getChildren()) {
                            Contact contact = contacts.getValue(Contact.class);
                            td.put(contacts.getKey(), contact);
                        }

                        ContactsList = new ArrayList<>(td.values());
                   /* for (DataSnapshot child: dataSnapshot.getChildren()) {
                        ContactsList.add(child.getValue(Contact.class));
                    }*/
                        ContactsAdapter =new ContactsAdapter(ContactsList.this,ContactsList);
                        ContactsRecycler.setAdapter(ContactsAdapter);

                        ContactsAdapter.notifyDataSetChanged();
                        Log.i("contacts",ContactsList.size()+"");
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else {
            Toast.makeText(getBaseContext(),"No Internet Connection",Toast.LENGTH_LONG).show();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_signout) {
            if (isOnline(getBaseContext())){
                ShowDialog();
            }
            return true;
        }

        return true;
    }
    public void ShowDialog(){
        AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);

        builder.setMessage("SignOut")
                    .setCancelable(false)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ContactsList.this, SignIn.class));
                finish();
                                }
                            });
        builder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
