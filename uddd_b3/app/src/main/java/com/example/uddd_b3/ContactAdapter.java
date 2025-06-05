package com.example.uddd_b3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ContactAdapter extends ArrayAdapter<ContactItem> {
    int resource;
    private List<ContactItem> contactList;
    private List<Boolean> selected;

    public ContactAdapter(@NonNull Context context, int resource, @NonNull List<ContactItem> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.contactList = objects;
        selected = new ArrayList<>(Collections.nCopies(contactList.size(), false));

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ContactItem contactItem = getItem(position);

        View v = convertView;
        if (v == null) {
            LayoutInflater vi = LayoutInflater.from(getContext());
            v = vi.inflate(resource, parent, false);
        }

        TextView name = v.findViewById(R.id.contact_name);
        TextView phone = v.findViewById(R.id.contact_phone);
        CheckBox select = v.findViewById(R.id.select_contact_checkbox);

        select.setOnCheckedChangeListener(null);
        select.setChecked(false);

        name.setText(contactItem.getName());
        phone.setText(contactItem.getPhone());


        select.setOnCheckedChangeListener((buttonView, isChecked) -> {
            selected.set(position,isChecked);
        });

        return v;
    }

    public List<Boolean> getSelected() {
        return selected;
    }
    public List<ContactItem> getContactList(){
        return contactList;
    }
}
