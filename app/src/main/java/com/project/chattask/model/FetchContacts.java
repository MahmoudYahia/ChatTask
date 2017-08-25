package com.project.chattask.model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.chattask.callback.fetchContacts.OnContactsReadyListner;
import com.project.chattask.datamodel.Contact;

import java.util.ArrayList;

/**
 * Created by mah_y on 8/22/2017.
 */

public class FetchContacts {
    DatabaseReference databaseRef;
    ArrayList<Contact>contactsList;
    OnContactsReadyListner mContactsReadyListner;

   public FetchContacts(OnContactsReadyListner contactsReadyListner) {
       mContactsReadyListner=contactsReadyListner;
    }

    public void readContacts() {

        contactsList=new ArrayList<>();
        databaseRef = FirebaseDatabase.getInstance().getReference().child("Contacts");
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildren() != null) {

                    for (DataSnapshot contacts : dataSnapshot.getChildren()) {
                        Contact contact = contacts.getValue(Contact.class);
                        contactsList.add(contact);
                    }

                    mContactsReadyListner.contactsFetched(contactsList);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                mContactsReadyListner.contactsFetchFailed();
            }
        });


    }

    public void fetchFavouriteContacts() {

    }
}
