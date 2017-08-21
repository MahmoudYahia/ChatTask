package com.project.chattask.Models;

/**
 * Created by mah_y on 8/13/2017.
 */

public class Message {
    private String messageid;
    private String receiverid;
    private String senderid;
    private String text;
    private String name;
    private String imageurl;
    private String uploadedimg;

    public Message() {

    }

    public Message(String receiverid, String senderid, String text, String name, String imageurl, String uploadedimg) {
        this.receiverid = receiverid;
        this.senderid = senderid;
        this.text = text;
        this.name = name;
        this.imageurl = imageurl;
        this.uploadedimg = uploadedimg;
    }

    public String getReceiverid() {
        return receiverid;
    }

    public void setReceiverid(String receiverid) {
        this.receiverid = receiverid;
    }

    public String getSenderid() {
        return senderid;
    }


    public String getImageurl() {
        return imageurl;
    }


    public String getUploadedimg() {
        return uploadedimg;
    }

    public void setUploadedimg(String uploadedimg) {
        this.uploadedimg = uploadedimg;
    }

    public String getId() {
        return messageid;
    }

    public void setId(String id) {
        this.messageid = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
