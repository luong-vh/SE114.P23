package com.example.uddd_b3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<TodoItem> items = new ArrayList<TodoItem>();
    TodoAdapter todoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ActivityResultLauncher<Intent> launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent intent = result.getData();
                            int position = intent.getIntExtra("position",-1);
                            TodoItem item = (TodoItem) intent.getSerializableExtra("item");
                            if (position == -1 ){
                                if (item.getTitle()!="" && item.getDate()!="") items.add(item);
                            }
                            else {
                                items.set(position,item);
                            }
                            todoAdapter.notifyDataSetChanged();

                        }
                    }
                });

        items.add(new TodoItem("task1", "description", "25/10/2025", false));
        items.add(new TodoItem("task2", null, "25/10/2025", false));


        ListView taskListview = (ListView) findViewById(R.id.taskListview);
        todoAdapter = new TodoAdapter(this, R.layout.list_view_item, items);
        taskListview.setAdapter(todoAdapter);

        taskListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TodoItem item = (TodoItem) parent.getItemAtPosition(position);

                Intent intent = new Intent(MainActivity.this, detail_task.class);
                intent.putExtra("state", "edit");
                intent.putExtra("item", item);
                intent.putExtra("position",position);
                launcher.launch(intent);
            }
        }
        );

        FloatingActionButton fab = findViewById(R.id.addBtn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Xử lý khi FAB được bấm
                Intent intent = new Intent(MainActivity.this, detail_task.class);
                intent.putExtra("state", "add");
                launcher.launch(intent);
            }
        });

    }

    public class TodoAdapter extends ArrayAdapter<TodoItem> {
        int resource;
        private List<TodoItem> todoList;

        public TodoAdapter(@NonNull Context context, int resource, @NonNull List<TodoItem> objects) {
            super(context, resource, objects);
            this.resource = resource;
            this.todoList = objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            TodoItem todo = getItem(position);

            View v = convertView;
            if (v == null) {
                LayoutInflater vi = LayoutInflater.from(getContext());
                v = vi.inflate(resource, parent, false);
            }

            TextView titleText = v.findViewById(R.id.task_title);
            TextView dateText = v.findViewById(R.id.task_date);
            CheckBox checkBox = v.findViewById(R.id.task_done_checkbox);

            checkBox.setOnCheckedChangeListener(null); // tránh gán trùng
            checkBox.setChecked(todo.isDone());

            titleText.setText(todo.getTitle());
            dateText.setText(todo.getDate());

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                todo.setDone(isChecked);
            });

            return v;
        }

    }


}