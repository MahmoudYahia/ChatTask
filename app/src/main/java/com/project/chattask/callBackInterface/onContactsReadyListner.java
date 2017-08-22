package com.project.chattask.callBackInterface;

import com.project.chattask.model.Contact;

import java.util.ArrayList;

/**
 * Created by mah_y on 8/22/2017.
 */

public interface onContactsReadyListner {
    void contactsFetched(ArrayList<Contact> contacts);
}
