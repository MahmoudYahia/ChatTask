package com.project.chattask.callback;

import com.project.chattask.model.Message;

import java.util.ArrayList;

/**
 * Created by mah_y on 8/22/2017.
 */

public interface OnfetchingMessagesListner {
    public void onMessagesFetched(ArrayList<Message>messagesList);
    public void onfetchMessagesFailed();
}
