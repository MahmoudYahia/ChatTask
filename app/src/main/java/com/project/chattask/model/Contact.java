package com.project.chattask.model;

import java.io.Serializable;

/**
 * Created by mah_y on 8/14/2017.
 */

public class Contact implements Serializable{
    public String uid;
    public String tokenid;
    public String uname;
    public String uemail;
    public String imgurl;

    public Contact() {
    }

    public Contact(String uid, String tokenid, String uname, String uemail, String imgurl) {
        this.uid = uid;
        this.tokenid = tokenid;
        this.uname = uname;
        this.uemail = uemail;
        this.imgurl = imgurl;
    }

    public Contact(String uid, String displayName, String email, String img) {
        this.uid = uid;
        this.uname = displayName;
        this.uemail = email;
        this.imgurl = img;
    }

    public String getUid() {
        return uid;
    }

   public   String getTokenid() {
        return tokenid;
    }

    public String getUname() {
        return uname;
    }

    public String getUemail() {
        return uemail;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setTokenid(String tokenid) {
        this.tokenid = tokenid;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public void setUemail(String uemail) {
        this.uemail = uemail;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }
}
