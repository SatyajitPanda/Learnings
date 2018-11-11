package com.istore.contacts.screen.contactlist;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.istore.contacts.R;
import com.istore.contacts.model.Contact;
import com.istore.contacts.screen.contactdetails.ContactDetailActivity;
import com.istore.contacts.service.GetContactService;
import com.istore.contacts.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class ContactsActivity extends AppCompatActivity implements ContactListContractor.View, ContactListAdapter.OnItemClickListener {

    private static final int PERM_READ_CONTACT = 1;

    private ContactListPresenter mPresenter;
    private RecyclerView vContactList;
    private ContactListAdapter mAdapter;
    private List<Contact> contactList = new ArrayList<>();
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        mPresenter = new ContactListPresenter(this, this);
        initView();
        initAdapter();
        readContact();
    }

    private void initView() {
        vContactList = findViewById(R.id.rv_contact_list);
    }

    private void initAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        vContactList.setLayoutManager(layoutManager);
        mAdapter = new ContactListAdapter(contactList, this);
        vContactList.setAdapter(mAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(PERM_READ_CONTACT)
    private void readContact() {
        String[] perms = {Manifest.permission.READ_CONTACTS};
        if (EasyPermissions.hasPermissions(this, perms)) {
            mPresenter.loadContactList();
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.perm_read_contact),
                    PERM_READ_CONTACT, perms);
        }
    }

    @Override
    public void updateContactList(List<Contact> contactList) {
        this.contactList = contactList;
        mAdapter.swap(this.contactList);
    }

    @Override
    public void showProgress() {
        if (dialog == null) {
            dialog = new ProgressDialog(this);
            dialog.setMessage("Loading contact list.");
            dialog.setCancelable(false);
        }
        dialog.show();
    }

    @Override
    public void hideProgress() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onItemClick(Contact contact) {
        Intent intent = new Intent(this, ContactDetailActivity.class);
        intent.putExtra(Constants.EXTRA_CONTACTS, contact);
        startActivity(intent);
    }
}
