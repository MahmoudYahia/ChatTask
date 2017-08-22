package com.project.chattask.activity;

import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.project.chattask.adapter.ContactsAdapter;
import com.project.chattask.model.Contact;
import com.project.chattask.callBackInterface.OnContactSelectedListner;
import com.project.chattask.R;
import com.project.chattask.callBackInterface.onContactsReadyListner;
import com.project.chattask.database.DataBaseHelper;

import java.util.ArrayList;
import java.util.List;

public class ContactsListActivity extends AppCompatActivity implements OnContactSelectedListner, onContactsReadyListner {

    DatabaseReference databaseRef;
    RecyclerView ContactsRecycler;
    RecyclerView.LayoutManager layoutManager;
    ContactsAdapter ContactsAdapter;
    List<Contact> ContactsList;
    FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_list);

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in
            startActivity(new Intent(this, SignIn.class));
            finish();
        }

        ContactsRecycler = (RecyclerView) findViewById(R.id.contacts_recycler);
        layoutManager = new LinearLayoutManager(this);
        ContactsRecycler.setLayoutManager(layoutManager);
        ContactsRecycler.setHasFixedSize(true);

        ContactsList = new ArrayList<>();


        ContactsAdapter = new ContactsAdapter(ContactsListActivity.this, ContactsList, mFirebaseUser, ContactsListActivity.this);
        ContactsRecycler.setAdapter(ContactsAdapter);

        databaseRef = FirebaseDatabase.getInstance().getReference().child("Contacts");

        if (isOnline(this)) {
            readContacts();
        } else {
            Toast.makeText(getBaseContext(), R.string.noConnection, Toast.LENGTH_LONG).show();
        }

    }

    public void readContacts() {

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildren() != null) {

                    for (DataSnapshot contacts : dataSnapshot.getChildren()) {

                        Contact contact = contacts.getValue(Contact.class);
                        ContactsList.add(contact);
                    }
                    ContactsAdapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
            if (isOnline(getBaseContext())) {
                ShowConfirmationDialog();
            }
            return true;
        }

        return true;
    }

    public void ShowConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("SignOut")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(ContactsListActivity.this, SignIn.class));
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

    @Override
    public void contactSelected(int position) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("contact_id", ContactsList.get(position));
        startActivity(intent);
    }


    @Override
    public void contactsFetched(ArrayList<Contact> contacts) {
        Toast.makeText(this, contacts.size() + "", Toast.LENGTH_LONG).show();
    }
}

class AddContactsToDataBase extends AsyncTask<Context, Void, ArrayList<Contact>> {


    private Context mContext;
    private DataBaseHelper dataBaseHelper;
    private onContactsReadyListner onContactsReadyListner;
    ArrayList<Contact> contactsList;

    public AddContactsToDataBase(Context context, onContactsReadyListner onContactsReadyListner) {
        this.onContactsReadyListner = onContactsReadyListner;
        this.mContext = context;
    }

    @Override
    protected ArrayList<Contact> doInBackground(Context... params) {
        dataBaseHelper = new DataBaseHelper(mContext);
        contactsList = new ArrayList<>();
        contactsList = dataBaseHelper.getContacts();
        return contactsList;
    }

    @Override
    protected void onPostExecute(ArrayList<Contact> contacts) {
        super.onPostExecute(contacts);
        onContactsReadyListner.contactsFetched(contacts);
    }
}
