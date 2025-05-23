package com.example.uddd_b3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

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
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    List<TodoItem> items = new ArrayList<TodoItem>();
    TodoAdapter todoAdapter;
    TextView optionTV;
    ActivityResultLauncher<Intent> launcher;
    TodoRepo repo;
    boolean selectMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        optionTV = findViewById(R.id.textView);
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        selectMode = false;
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        repo = new TodoRepo(this);
        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {

                            items.clear();
                            items.addAll(repo.loadAll());
                            todoAdapter.notifyDataSetChanged();

                        }
                    }
                });
        items = repo.loadAll();
        ListView taskListview = (ListView) findViewById(R.id.taskListview);
        todoAdapter = new TodoAdapter(this, R.layout.list_view_item, items);
        taskListview.setAdapter(todoAdapter);

        taskListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TodoItem item = (TodoItem) parent.getItemAtPosition(position);

                Intent intent = new Intent(MainActivity.this, detail_task.class);
                intent.putExtra("state", "edit");
                intent.putExtra("id", item.getId());
                intent.putExtra("position",position);
                launcher.launch(intent);
            }
        }

        );
    }
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.main_add){
            Intent intent = new Intent(MainActivity.this, detail_task.class);
            intent.putExtra("state", "add");
            launcher.launch(intent);
        }
        else if (id == R.id.main_delete){
            Iterator<TodoItem> iterator = items.iterator();
            while (iterator.hasNext()) {
                TodoItem _item = iterator.next();
                if (_item.isSelected()) {
                    repo.delete(_item.getId());
                }
            }
            items.clear();
            items.addAll(repo.loadAll());
            todoAdapter.notifyDataSetChanged();
        }
        else if (id == R.id.main_select){
            selectMode = !selectMode;
            todoAdapter.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
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
            CheckBox select = v.findViewById(R.id.select_checkbox);

            checkBox.setOnCheckedChangeListener(null); // tránh gán trùng
            checkBox.setChecked(todo.isDone());
            select.setOnCheckedChangeListener(null); // tránh gán trùng
            select.setChecked(false);
            todo.setSelected(false);
            if (selectMode){
                select.setVisibility(View.VISIBLE);
            }
            else {
                select.setVisibility(View.GONE);
            }
            titleText.setText(todo.getTitle());
            dateText.setText(todo.getDate());

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                todo.setDone(isChecked);
                repo.update(todo);
                items.clear();
                items.addAll(repo.loadAll());
                todoAdapter.notifyDataSetChanged();
            });
            select.setOnCheckedChangeListener((buttonView, isChecked) -> {
                todo.setSelected(isChecked);
            });

            return v;
        }

    }


}