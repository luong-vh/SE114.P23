package com.example.uddd_b2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Account Acc = (Account) getIntent().getSerializableExtra("account");

        TextView nameTv = findViewById(R.id.name_text);
        TextView dobTv = findViewById(R.id.DOB_text);
        TextView descTv = findViewById(R.id.description_text);
        TextView deptTv = findViewById(R.id.department_text);
        TextView idTv = findViewById(R.id.ID);
        ImageView avt = findViewById(R.id.avt);
        Button logOut = findViewById(R.id.btn_logout);

        assert Acc != null;
        nameTv.setText(Acc.getName());
        dobTv.setText(Acc.getDOB());
        descTv.setText(Acc.getDescription());
        deptTv.setText(Acc.getDepartment());
        idTv.setText(Acc.getMSNV());
        avt.setImageResource(Acc.getAvt());

        logOut.setOnClickListener(v -> {
            Logout();
        });
        List<Student> students = new ArrayList<Student>();
        students.add(new Student("Nguyen Van A", "10/2/1999"));
        students.add(new Student("Nguyen Tan B",  "25/10/2000"));
        students.add(new Student("Tran Xuan B", "13/1/2001"));

        ListView list = (ListView) findViewById(R.id.studentListview);
        StudentListAdapter adapter = new StudentListAdapter(getApplicationContext(), R.layout.list_item_student, students);
        list.setAdapter(adapter);
    }

    public void Logout(){
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
    class StudentListAdapter extends ArrayAdapter<Student>{
        int resource;
        private List<Student> students;
        public StudentListAdapter(Context context, int resource, List<Student> students) {
            super(context, resource, students);
            this.students = students;
            this.resource = resource;
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View v = convertView;
            if (v == null){
                LayoutInflater vi;
                vi = LayoutInflater.from(this.getContext());
                v = vi.inflate(this.resource,null);
            }
            Student s = getItem(position);

            if (s!=null){
                TextView nameTextView = (TextView) v.findViewById(R.id.name);
                TextView dobTextView = (TextView) v.findViewById(R.id.dob);

                if(nameTextView!=null)
                    nameTextView.setText(s.getName());
                if(dobTextView!=null)
                    dobTextView.setText(String.valueOf(s.getDoB()));
            }
            return v;
        }
    }


    class Student {
        private String name;
        private String dob;

        public Student(String name, String dob) {
            this.name = name;
            this.dob = dob;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDoB() {
            return dob;
        }

        public void setDoB(String dob) {
            this.dob = dob;
        }

    }
}

