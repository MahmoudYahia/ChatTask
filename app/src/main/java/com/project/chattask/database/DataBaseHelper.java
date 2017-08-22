package com.project.chattask.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.project.chattask.models.Contact;

import java.util.ArrayList;

/**
 * Created by mah_y on 8/21/2017.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    Context mContext;

    private static final int DATABASE_VERSION = 2;
    private static final String DataBase_name = "ContactsDataBase";
    private static final String Table_name = "Contacts";


    private String contactId = "_id";
    private String contact_img_url = "Img_Url";
    private String contact_name = "Displayed_Name";
    private String contactemail = "Contact_Email";
    private String tokenid = "Token_id";

    private String CREATE_TABLE = "CREATE TABLE " + Table_name + "("
            + contactId + " PRIMARY KEY,"
            + contact_img_url + ","
            + contact_name + ","
            + contactemail + ","
            + tokenid + ");";

    private String Drop_table = "DROP TABLE IF EXISTS " + Table_name;

    public DataBaseHelper(Context context) {
        super(context, DataBase_name, null, DATABASE_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Drop_table);
        onCreate(db);
    }

    public long addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues Data = new ContentValues();

        Data.put(contactId, contact.getUid());
        Data.put(contact_img_url, contact.getImgurl());
        Data.put(contact_name, contact.getUname());
        Data.put(contactemail, contact.getUemail());
        Data.put(tokenid, contact.getTokenid());

        Long id = db.insert(Table_name, null, Data); // execute sql

        db.close();
        return id;
    }

    public boolean IfExist(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {contactId};
        String[] args = new String[]{id};
        Cursor cursor = db.query(Table_name, columns, contactId + "=?", args, null, null, null, null);

        if (cursor.getCount() == 0)
            return false;
        else
            return true;
    }

    public ArrayList<Contact> getContacts() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {contactId, contact_img_url, contact_name, contactemail, tokenid};
        Cursor cursor = db.query(Table_name, columns, null, null, null, null, null);

        ArrayList<Contact> contacts = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {

                Contact contact = new Contact();

                contact.setUid(cursor.getString(0));
                contact.setImgurl(cursor.getString(1));
                contact.setUname(cursor.getString(2));
                contact.setUemail(cursor.getString(3));
                contact.setTokenid(cursor.getString(4));

                contacts.add(contact);
            }
            while (cursor.moveToNext());
        }

        return contacts;

    }
}
