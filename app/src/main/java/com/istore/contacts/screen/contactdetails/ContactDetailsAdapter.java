package com.istore.contacts.screen.contactdetails;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.istore.contacts.R;
import com.istore.contacts.model.Contact;

import java.util.List;

public class ContactDetailsAdapter extends RecyclerView.Adapter<ContactDetailsAdapter.ContactListHolder> {

    private List<String> contactList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(String phoneNumber);
    }

    public class ContactListHolder extends RecyclerView.ViewHolder {
        public TextView txtContact;
        public ImageView ivCallNumber;

        public ContactListHolder(View view) {
            super(view);
            txtContact = view.findViewById(R.id.txt_contact);
            ivCallNumber = view.findViewById(R.id.iv_call_number);
        }
    }


    public ContactDetailsAdapter(List<String> contactList, OnItemClickListener listener) {
        this.contactList = contactList;
        this.mListener = listener;
    }

    @Override
    public ContactListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_contact_detail, parent, false);

        return new ContactListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ContactListHolder holder, int position) {
        final String contact = contactList.get(position);
        holder.txtContact.setText(contact);
        holder.ivCallNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onItemClick(contact);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

}
