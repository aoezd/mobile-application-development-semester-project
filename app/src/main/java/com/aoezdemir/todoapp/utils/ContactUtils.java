package com.aoezdemir.todoapp.utils;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

public class ContactUtils {

    public static String getContactIdAndName(ContentResolver resolver, Uri uri) {
        String idName = ";";
        Cursor cursorContact = resolver.query(uri, null, null, null, null);
        if (cursorContact != null && cursorContact.moveToFirst()) {
            idName = cursorContact.getString(cursorContact.getColumnIndex(ContactsContract.Contacts._ID)) + ";" + cursorContact.getString(cursorContact.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            cursorContact.close();
        }
        return idName;
    }

    public static String getContactPhoneById(ContentResolver resolver, String id) {
        String phone = null;
        Cursor cursorPhone = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[] { id }, null);
        if (cursorPhone != null && cursorPhone.moveToFirst()) {
            phone = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            cursorPhone.close();
        }
        return phone;
    }

    public static String getContactEmailById(ContentResolver resolver, String id) {
        String email = null;
        Cursor cursorEmail = resolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[] { id }, null);
        if (cursorEmail != null && cursorEmail.moveToFirst()) {
            email = cursorEmail.getString(cursorEmail.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
            cursorEmail.close();
        }
        return email;
    }
}