package com.istore.contacts.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Patterns;

import com.istore.contacts.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class Helper {

    public static void phoneCallTask(Context context, String phoneNumber){
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        context.startActivity(intent);
        /*if (PermissionUtil.checkPermission(context, PermissionUtil.Permissions.CALL_CONTACTS)) {
        }*/
    }

    public static boolean verifyContact(Contact contact) {
        if ((contact.phoneNumbers != null && !contact.phoneNumbers.isEmpty())
                && !TextUtils.isEmpty(contact.name)) {
            return true;
        }
        return false;
    }

    public static String getFormattedNumber(String number) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            number = PhoneNumberUtils.formatNumber(number, "IN").replaceAll("\\s+", "");

        } else {
            number = PhoneNumberUtils.formatNumber(number).replaceAll("\\s+", "");

        }
        return number;
    }

    public static boolean verifyPhone(List<Contact> allContact, ArrayList<String> phones, String phone) {
        return Patterns.PHONE.matcher(phone).matches() && isUniquePhoneNumber(phones, phone) &&
                isUniquePhoneNumberInContact(allContact, phone);
    }

    public static boolean isUniquePhoneNumberInContact(List<Contact> allContact, String phone) {
        if(allContact != null && allContact.size() > 0) {
            for (int x = 0; x < allContact.size(); x++) {
                if (allContact.get(x).phoneNumbers != null && allContact.get(x).phoneNumbers.size() > 0) {
                    for (int i = 0; i < allContact.get(x).phoneNumbers.size(); i++) {
                        if (matchPhoneNumber(allContact.get(x).phoneNumbers.get(i), phone, null)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public static boolean isUniquePhoneNumber(ArrayList<String> phoneNumbers, String phone) {
        if (phoneNumbers != null && phoneNumbers.size() > 0) {
            for (int i = 0; i < phoneNumbers.size(); i++) {
                if (matchPhoneNumber(phoneNumbers.get(i), phone, null)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean matchPhoneNumber(String firebaseUserPhone, String contactNumber, String countryCode) {
        if (countryCode == null) countryCode = "";
        if (firebaseUserPhone.equals(contactNumber)) return true;
        firebaseUserPhone = firebaseUserPhone.replace(countryCode, "");
        if (firebaseUserPhone.equals(contactNumber)) return true;
        if (firebaseUserPhone.length() <= contactNumber.length()) {
            contactNumber = contactNumber.substring(contactNumber.length() - firebaseUserPhone.length());
            return contactNumber.equals(firebaseUserPhone);
        }
        return false;
    }


}
