package com.example.uddd_b2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity {

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                    }
                }
            });
    private Account[] accounts = {
            new Account("ID1", "user1", 123456, "01/01/2000", "Description 1", "Dep1", R.drawable.nv1),
            new Account("ID2", "user2", 234567, "02/02/2001", "Description 2", "Dep2", R.drawable.nv2),
            new Account("ID3", "user3", 345678, "03/03/2002", "Description 3", "Dep3", R.drawable.nv3),
    };
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
        Button loginButton = findViewById(R.id.btn_login);

        loginButton.setOnClickListener(v -> {
            loginClick();
        });
    }

    public boolean checkClick(Account[] accounts, Account test) {
        for (Account account : accounts) {
            if (account.getMSNV().equals(test.getMSNV()) && account.getPassword() == test.getPassword()) {
                return true;
            }
        }
        return false;
    }

    public void loginClick(){
        EditText nameInput = findViewById(R.id.username_input);
        EditText passwordInput = findViewById(R.id.password_input);
        String username = nameInput.getText().toString();
        String password = passwordInput.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }
        else {
            int pass = Integer.parseInt(password);
            Account tempAccount = new Account(username, pass);
            Account matchedAccount = null;

            for (Account acc : accounts) {
                if (acc.getMSNV().equals(tempAccount.getMSNV()) && acc.getPassword() == tempAccount.getPassword()) {
                    matchedAccount = acc;
                    break;
                }
            }

            if (matchedAccount != null) {
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, Profile.class);
                intent.putExtra("account", matchedAccount); // ✅ truyền đúng object đủ thông tin
//                startActivity(intent);
                launcher.launch(intent);
            } else {
                Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

}

class Account  implements Serializable {
    private String MSNV;
    private String name;
    private int password;
    private String DOB;
    private String desc;
    private String department;
    private int avt;

    public Account(String MSNV, int password) {
        this.MSNV = MSNV;
        this.password = password;
    }
    public Account() {}
    public Account(String MSNV, String name, int password, String DOB, String desc, String department, int avt) {
        this.MSNV = MSNV;
        this.name = name;
        this.password = password;
        this.DOB = DOB;
        this.desc = desc;
        this.department = department;
        this.avt = avt;
    }

    public Account(Account a){
        this.MSNV = a.MSNV;
        this.password = a.password;
        this.name = a.name;
        this.DOB = a.DOB;
        this.desc = a.desc;
        this.department = a.department;
    }
    public String getMSNV() {
        return MSNV;
    }

    public String getName() {
        return name;
    }

    public int getPassword() {
        return password;
    }
    public String getDescription() {
        return desc;
    }
    public String getDOB() {
        return DOB;
    }
    public String getDepartment(){ return department;}
    public int getAvt(){return avt;}
}