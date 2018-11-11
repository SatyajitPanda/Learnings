package com.istore.contacts.screen.contactdetails;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.istore.contacts.R;
import com.istore.contacts.model.Contact;
import com.istore.contacts.utils.Constants;
import com.istore.contacts.utils.Helper;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class ContactDetailActivity extends AppCompatActivity implements ContactDetailsAdapter.OnItemClickListener {

    private RecyclerView vContactList;
    private AppCompatTextView txtContactName;
    private Contact contact;
    private ContactDetailsAdapter mAdapter;
    private String selectedContact;

    private static final int PERM_CALL_PHONE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);
        contact = getIntent().getParcelableExtra(Constants.EXTRA_CONTACTS);
        initView();
        initAdapter();
        txtContactName.setText(contact.getName());

    }

    private void initView() {
        txtContactName = findViewById(R.id.txt_contact_name);
        vContactList = findViewById(R.id.rv_contact_list);
    }

    private void initAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        vContactList.setLayoutManager(layoutManager);
        mAdapter = new ContactDetailsAdapter(contact.getPhoneNumbers(), this);
        vContactList.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(String contact) {
        this.selectedContact = contact;
        readContact();
    }

    @AfterPermissionGranted(PERM_CALL_PHONE)
    private void readContact() {
        String[] perms = {Manifest.permission.CALL_PHONE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            Helper.phoneCallTask(this, selectedContact);
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.perm_call_phone),
                    PERM_CALL_PHONE, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
