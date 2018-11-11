package com.istore.contacts.screen.contactlist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.istore.contacts.R;
import com.istore.contacts.model.Contact;
import java.util.List;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ContactListHolder> {

    private List<Contact> contactList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(Contact contact);
    }

    public class ContactListHolder extends RecyclerView.ViewHolder {
        public TextView contactName;

        public ContactListHolder(View view) {
            super(view);
            contactName = view.findViewById(R.id.txt_contact_name);
        }
    }


    public ContactListAdapter(List<Contact> contactList, OnItemClickListener listener) {
        this.contactList = contactList;
        this.mListener = listener;
    }

    @Override
    public ContactListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_card, parent, false);

        return new ContactListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ContactListHolder holder, int position) {
        final Contact contact = contactList.get(position);
        holder.contactName.setText(contact.getName());
        holder.contactName.setOnClickListener(new View.OnClickListener() {
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

    public void swap(List<Contact> contactList) {
        this.contactList = contactList;
        notifyDataSetChanged();
    }
}
