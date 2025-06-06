package com.example.uddd_b2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public class MainActivity extends AppCompatActivity {
    Retrofit retrofit;
    ApiService service;
    List<Employee> employeeList;
    EmployeeAdapter adapter;
    ListView listView;
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        getAll();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        listView = (ListView) findViewById(R.id.employee_list_view);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Employee item = (Employee) parent.getItemAtPosition(position);

                Intent intent = new Intent(MainActivity.this, Profile.class);
                intent.putExtra("state", "edit");
                intent.putExtra("id", item.getId());
                intent.putExtra("position", position);
                launcher.launch(intent);
            }
        }

        );
        retrofit = new Retrofit.Builder()
                .baseUrl("http://blackntt.net:88")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(ApiService.class);

        employeeList = new ArrayList<>();
        getAll();

    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.main_add) {
            Intent intent = new Intent(MainActivity.this, Profile.class);
            intent.putExtra("state", "add");
            intent.putExtra("id", -1);
            intent.putExtra("position", -1);
            launcher.launch(intent);
        } else if (id == R.id.main_delete) {
            List<Boolean> selected = adapter.getSelected();
            final int[] deletedCount = {0};
            final int[] totalDelete = {0};
            for (Boolean b : selected) {
                if (b) totalDelete[0]++;
            }
            for (int b = 0; b < selected.size(); b++) {
                if (selected.get(b)) {
                    service.deleteEmployeeById(Integer.parseInt(employeeList.get(b).getId())).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            deletedCount[0]++;
                            if (deletedCount[0] == totalDelete[0]) {
                                // Đã xóa hết các nhân viên, gọi lại getAll()
                                getAll();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            deletedCount[0]++;
                            if (deletedCount[0] == totalDelete[0]) {
                                // Đã xóa hết các nhân viên (có thể thất bại một số), gọi lại getAll()
                                getAll();
                            }
                        }
                    });
                }
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void getAll() {
        Call<List<Employee>> getAllAPI = service.getAll();

        getAllAPI.enqueue(new Callback<List<Employee>>() {
            @Override
            public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Employee> employees = response.body();
                    employeeList.clear();
                    employeeList.addAll(employees);
                    adapter = new EmployeeAdapter(MainActivity.this, R.layout.employee_item_listview, employeeList);
                    listView.setAdapter(adapter);


                } else {
                    Log.e("API", "Response error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Employee>> call, Throwable t) {
                Log.e("API", "Failure: " + t.getMessage());
            }
        });
    }





}


