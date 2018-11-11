package com.istore.contacts.screen.contactlist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.View;
import android.widget.Toast;

import com.istore.contacts.model.Contact;
import com.istore.contacts.service.GetContactService;

import java.util.ArrayList;

public class ContactListPresenter implements ContactListContractor.Presenter {

    private Context mContext;
    private ContactListContractor.View mView;
    private ContactSyncResultReceiver mReceiver;

    public ContactListPresenter(Context context, ContactListContractor.View view) {
        this.mContext = context;
        this.mView = view;
        mReceiver = new ContactSyncResultReceiver(new Handler());
    }

    @Override
    public void loadContactList() {
        GetContactService.fetchContact(mContext, mReceiver);

    }

    public class ContactSyncResultReceiver extends ResultReceiver {

        public ContactSyncResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            switch (resultCode) {
                case GetContactService.CONTACT_SYNC_STARTED : {
                    mView.showProgress();
                    //Toast.makeText(mContext, "Started", Toast.LENGTH_SHORT).show();
                }
                break;
                case GetContactService.CONTACT_SYNC_DONE : {
                    mView.hideProgress();
                    ArrayList<Contact> contactList = resultData.getParcelableArrayList(GetContactService.EXTRA_CONTACT_LIST);
                    //Toast.makeText(mContext, "Done = " + contactList.size(), Toast.LENGTH_SHORT).show();
                    mView.updateContactList(contactList);

                }
                break;
            }
            super.onReceiveResult(resultCode, resultData);
        }

    }
}
