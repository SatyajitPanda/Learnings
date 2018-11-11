package com.istore.contacts.screen.contactlist;

import android.app.Activity;

import com.istore.contacts.model.Contact;

import java.util.List;

public interface ContactListContractor {

    interface Presenter {
        void loadContactList();
    }

    interface View {
        void updateContactList(List<Contact> contactList);
        void showProgress();
        void hideProgress();
    }
}
