package com.project.chattask.callback.fetchContacts;

import com.project.chattask.model.Contact;

import java.util.ArrayList;

/**
 * Created by mah_y on 8/22/2017.
 */

public interface OnContactsReadyListner {
    void contactsFetched(ArrayList<Contact> contacts);
    void contactsFetchFailed();
}
