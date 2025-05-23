package com.example.uddd_b3;

import static androidx.core.widget.TextViewKt.addTextChangedListener;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Objects;

public class detail_task extends AppCompatActivity {
    String state;
    TodoItem item;
    int position;
    TodoRepo repo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_task);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        repo = new TodoRepo(this);
        Intent intent = getIntent();
        TextView tv = findViewById(R.id.textView);
        EditText title = findViewById(R.id.edit_Title);
        EditText description = findViewById(R.id.edit_Description);
        EditText date = findViewById(R.id.dateEditText);
        CheckBox checkBox = findViewById(R.id.task_done_checkbox);
        state = intent.getStringExtra("state");
        if (state.equals( "add")){
            tv.setText("Add Task");
            item = new TodoItem("","","",false);
        }
        else if (state.equals("edit") ){
            tv.setText("Edit Task");

            item = repo.getById(intent.getStringExtra("id"));
        }
        position = intent.getIntExtra("position",-1);
        title.setText(item.getTitle());
        description.setText(item.getDescription());
        date.setText(item.getDate());
        checkBox.setChecked(item.isDone());


        checkBox.setOnCheckedChangeListener((buttonView, isChecked) ->
        {
            item.setDone(isChecked);
                });
        Button backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (position == -1 ){
                    if (item.getTitle()!="" && item.getDate()!="") {
                        repo.addNew(item);
                    }
                }
                else {
                    boolean update = repo.update(item);
                }
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        EditText dateEditText = findViewById(R.id.dateEditText);

        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy ngày hiện tại
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                // Hiển thị DatePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        detail_task.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                // Gán ngày được chọn vào EditText
                                String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                                dateEditText.setText(selectedDate);
                                item.setDate(selectedDate);
                            }
                        },
                        year, month, day
                );
                datePickerDialog.show();
            }
        });
        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String newText = s.toString();
                item.setTitle(newText);
            }
        });
        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String newText = s.toString();
                item.setDescription(newText);
            }
        });
    }
}