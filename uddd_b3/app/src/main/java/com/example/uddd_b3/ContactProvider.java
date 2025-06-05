package com.example.uddd_b3;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ContactProvider {
    private Context context;
    private List<ContactItem> contacts;

    public ContactProvider(Context context, List<ContactItem> contacts){
        this.contacts = contacts;
        this.context = context;
    }

    public void getData(String phone,List<String> names, List<String> phones){

        Type type = new TypeToken<List<String>>(){}.getType();
        Gson gson = new Gson();
        List<String> phoneList = gson.fromJson(phone, type);
        for (ContactItem contact : contacts) {
            if (phoneList.contains(contact.getPhone())) {
                names.add(contact.getName());
                phones.add(contact.getPhone());
            }
        }
    }
    public List<ContactItem> getNoMembers(String phones){
        List<ContactItem> result = new ArrayList<>();
        Type type = new TypeToken<List<String>>(){}.getType();
        Gson gson = new Gson();
        List<String> phoneList = gson.fromJson(phones, type);
        if (phoneList == null) {
            phoneList = new ArrayList<>();
        }
        for (ContactItem contact : contacts) {
            if (!phoneList.contains(contact.getPhone())) result.add(contact);
        }
        return result;
    }

}
