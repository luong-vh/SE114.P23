package com.example.uddd_b2;

import static android.content.Intent.getIntent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Profile extends AppCompatActivity {
    ApiService service;
    Retrofit retrofit;
    Employee employee;
    String state;

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

        retrofit = new Retrofit.Builder()
                .baseUrl("http://blackntt.net:88")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(ApiService.class);


        EditText nameTv = findViewById(R.id.name_text);
        EditText ageTv = findViewById(R.id.age_text);
        EditText salaryTv = findViewById(R.id.salary_text);
        EditText idTv = findViewById(R.id.ID);
        EditText url = findViewById(R.id.url);
        ImageView avt = findViewById(R.id.avt);
        Button close = findViewById(R.id.btn_close);
        Button save = findViewById(R.id.btn_save);

        Intent intent = getIntent();
        state = intent.getStringExtra("state");
        if (state.equals("edit")){
            int id = Integer.parseInt(intent.getStringExtra("id"));
            service.getEmployeeById(id).enqueue(new Callback<Employee>() {
                @Override
                public void onResponse(Call<Employee> call, Response<Employee> response) {
                    employee = response.body();
                    nameTv.setText(employee.getName());
                    ageTv.setText(employee.getAge()+"");
                    salaryTv.setText(employee.getSalary()+"");
                    idTv.setText(employee.getId());
                    url.setText(employee.getProfileImage());
                    Glide.with(Profile.this)
                            .load(employee.getProfileImage())
                            .into(avt);
                }

                @Override
                public void onFailure(Call<Employee> call, Throwable t) {

                }
            });
        }
        else if (state.equals("add")) {
            employee = new Employee();
            service.createEmployee(employee).enqueue(new Callback<Employee>() {
                @Override
                public void onResponse(Call<Employee> call, Response<Employee> response) {
                    if (response.isSuccessful()) {
                        employee = response.body();
                        nameTv.setText(employee.getName());
                        ageTv.setText(employee.getAge()+"");
                        salaryTv.setText(employee.getSalary()+"");
                        idTv.setText(employee.getId());
                        url.setText(employee.getProfileImage());
                        Glide.with(Profile.this)
                                .load(employee.getProfileImage())
                                .into(avt);
                    } else {
                        Log.e("Create", "Tạo thất bại, code: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Employee> call, Throwable t) {
                    Log.e("Create", "Lỗi khi gọi API: " + t.getMessage());
                }
            });
        }

        nameTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                employee.setName(s.toString());
            }
        });
        ageTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                employee.setAge(s.toString());
            }
        });
        idTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                employee.setId(s.toString());
            }
        });
        salaryTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                employee.setSalary(s.toString());
            }
        });
        url.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                employee.setProfileImage(s.toString());
                Glide.with(Profile.this)
                        .load(employee.getProfileImage())
                        .into(avt);
            }
        });
        close.setOnClickListener(v -> {
            Close();
        });
        save.setOnClickListener(v -> {
            service.updateEmployee(Integer.parseInt(employee.getId()),employee).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(response.isSuccessful()) {
                        Close();
                        Log.d("Update", "Update thành công");
                    } else {
                        Log.e("Update", "Update thất bại, code: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                }
            });





        });

    }

    public void Close(){
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }


}

