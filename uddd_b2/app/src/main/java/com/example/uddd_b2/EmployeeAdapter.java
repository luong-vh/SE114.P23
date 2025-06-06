package com.example.uddd_b2;

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

public class EmployeeAdapter extends ArrayAdapter<Employee> {
    int resource;
    private List<Employee> employeeList;
    private List<Boolean> selected;

    public EmployeeAdapter(@NonNull Context context, int resource, @NonNull List<Employee> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.employeeList = objects;
        selected = new ArrayList<>(Collections.nCopies(employeeList.size(), false));

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Employee employee = getItem(position);

        View v = convertView;
        if (v == null) {
            LayoutInflater vi = LayoutInflater.from(getContext());
            v = vi.inflate(resource, parent, false);
        }

        TextView name = v.findViewById(R.id.employee_name);
        TextView phone = v.findViewById(R.id.employee_age);
        CheckBox select = v.findViewById(R.id.select_checkbox);

        select.setOnCheckedChangeListener(null);
        select.setChecked(selected.get(position));

        name.setText(employee.getName());
        phone.setText("Tuổi: " + employee.getAge());


        select.setOnCheckedChangeListener((buttonView, isChecked) -> {
            selected.set(position,isChecked);
        });

        return v;
    }

    public List<Boolean> getSelected() {
        return selected;
    }
    public List<Employee> getContactList(){
        return employeeList;
    }
}
