package com.project.chattask.callBackInterfaces;

import com.project.chattask.models.Contact;

import java.util.ArrayList;

/**
 * Created by mah_y on 8/22/2017.
 */

public interface onContactsReadyListner {
    void contactsFetched(ArrayList<Contact> contacts);
}
