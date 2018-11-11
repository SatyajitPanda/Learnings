package com.istore.contacts.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

public class Contact implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    public String name;
    public ArrayList<String> phoneNumbers;

    // Constructor
    public Contact(String name, ArrayList<String> phoneNumberList) {
        this.name = name;
        this.phoneNumbers = phoneNumberList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(ArrayList<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    // Parcelling part
    public Contact(Parcel in){
        this.name = in.readString();
        this.phoneNumbers = in.readArrayList(null);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeList(phoneNumbers);
    }

    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                ", Numbers='" + phoneNumbers + '\'' +
                '}';
    }
}
