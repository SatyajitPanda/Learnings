package com.istore.contacts.service;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;

import com.istore.contacts.model.Contact;
import com.istore.contacts.screen.contactlist.ContactListPresenter;
import com.istore.contacts.screen.contactlist.ContactsActivity;
import com.istore.contacts.utils.Helper;

import java.util.ArrayList;
import java.util.List;

public class GetContactService extends IntentService {
    public static final String TAG = GetContactService.class.getSimpleName();

    public static final int CONTACT_SYNC_STARTED = 101;
    public static final int CONTACT_SYNC_DONE = 102;

    public static final String EXTRA_CONTACT_LIST = "ContactList";
    private static final String EXTRA_RESULT_RECEIVER = "receiver";

    private List<Contact> allContact = new ArrayList<Contact>();
    @SuppressLint("ObsoleteSdkInt")
    private final String DISPLAY_NAME = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY : ContactsContract.Contacts.DISPLAY_NAME;

    private final String FILTER = DISPLAY_NAME + " NOT LIKE '%@%'";

    private final String ORDER = String.format("%1$s COLLATE NOCASE", DISPLAY_NAME);
    private Context context;

    private final String[] PROJECTION = {
            ContactsContract.Contacts._ID,
            DISPLAY_NAME,
            ContactsContract.Contacts.HAS_PHONE_NUMBER
    };

    public GetContactService() {
        super("GetContactService");
    }

    public static void fetchContact(Context context, ContactListPresenter.ContactSyncResultReceiver receiver) {
        Intent intent = new Intent(context, GetContactService.class);
        intent.putExtra(EXTRA_RESULT_RECEIVER, receiver);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final ResultReceiver receiver = intent.getParcelableExtra(EXTRA_RESULT_RECEIVER);
        receiver.send(CONTACT_SYNC_STARTED, null);
        List<Contact> contactList = getContactList();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(EXTRA_CONTACT_LIST, (ArrayList) contactList);
        receiver.send(CONTACT_SYNC_DONE, bundle);
        Log.d("Contact", ""+contactList.size());
    }

    private List<Contact> getContactList() {
        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, PROJECTION, FILTER, null, ORDER);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                if (!TextUtils.isEmpty(name)) {
                    ArrayList<String> phones = addPhone(cursor, cr, id);
                    if(phones != null && phones.size() > 0) {
                        Contact contact = new Contact(name, phones);
                        if (Helper.verifyContact(contact)) {
                            allContact.add(contact);
                        }
                    }
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        return allContact;
    }




    private ArrayList<String> addPhone(Cursor cursor, ContentResolver cr, String id) {
        ArrayList<String> phones = new ArrayList<>();
        Integer hasPhone = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
        if (hasPhone > 0) {
            Cursor cp = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
            if (cp != null && cp.moveToFirst()) {
                do {
                    String phone = Helper.getFormattedNumber(cp.getString(cp.
                            getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                    if (Helper.verifyPhone(allContact, phones, phone))
                        phones.add(phone);
                } while (cp.moveToNext());
                cp.close();
            }
        }
        return phones;
    }

}
