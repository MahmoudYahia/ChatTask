package com.project.chattask.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.chattask.adapter.ContactsAdapter;
import com.project.chattask.callback.signOut.OnCompleteSignOut;
import com.project.chattask.callback.signOut.UserSignOut;
import com.project.chattask.datamodel.Contact;
import com.project.chattask.callback.contacsAdapter.OnContactSelectedListner;
import com.project.chattask.R;
import com.project.chattask.callback.fetchContacts.OnContactsReadyListner;
import com.project.chattask.model.FetchContacts;
import com.project.chattask.model.SignOut;

import java.util.ArrayList;
import java.util.List;

public class ContactsListActivity extends AppCompatActivity implements
        OnContactSelectedListner
        , OnContactsReadyListner
         {

    DatabaseReference databaseRef;
    RecyclerView ContactsRecycler;
    RecyclerView.LayoutManager layoutManager;
    ContactsAdapter contactsAdapter;

    List<Contact> ContactsList;
    FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_list);

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        }

        ContactsRecycler = (RecyclerView) findViewById(R.id.contacts_recycler);
        layoutManager = new LinearLayoutManager(this);
        ContactsRecycler.setLayoutManager(layoutManager);
        ContactsRecycler.setHasFixedSize(true);

        ContactsList = new ArrayList<>();

        contactsAdapter = new ContactsAdapter(ContactsListActivity.this, ContactsList, mFirebaseUser, ContactsListActivity.this);
        ContactsRecycler.setAdapter(contactsAdapter);

        databaseRef = FirebaseDatabase.getInstance().getReference().child("Contacts");

        if (isOnline(this)) {
            FetchContacts fetchContacts = new FetchContacts(this);
            fetchContacts.readContacts();
        } else {
            Toast.makeText(getBaseContext(), R.string.noConnection, Toast.LENGTH_LONG).show();
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

        UserSignOut userSignOut = new SignOut(this, new OnCompleteSignOut() {
            @Override
            public void signOutCompleted() {
                startActivity(new Intent(ContactsListActivity.this, SignInActivity.class));
                finish();
            }
        });

        userSignOut.signOut();

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
        this.ContactsList.addAll(contacts);
        Log.i("qqqq",ContactsList.size()+"");
        contactsAdapter.notifyDataSetChanged();
    }

    @Override
    public void contactsFetchFailed() {
       Toast.makeText(this,R.string.errorFitchingData,Toast.LENGTH_LONG).show();
    }

}


