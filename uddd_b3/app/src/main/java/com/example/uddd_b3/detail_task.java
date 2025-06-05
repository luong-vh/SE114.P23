package com.example.uddd_b3;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class detail_task extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    String state;
    TodoItem item;
    int position;
    TodoRepo repo;
    RecyclerView memberView;
    MembersAdapter adapter;
    List<String> membernames;
    List<String> memberPhones;
    List<ContactItem> contactItems = new ArrayList<ContactItem>();
    ContactProvider contactProvider;
    PopupWindow popupWindow;
    private static final int READ_CONTACTS_REQUEST_CODE = 1;
    private static final int CONTACT_LOADER = 1;
    private  static  final  boolean isASC = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    READ_CONTACTS_REQUEST_CODE);
        }
        LoaderManager.getInstance(this).restartLoader(this.CONTACT_LOADER,null,this);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_task);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        memberView = findViewById(R.id.memberListView);
        membernames = new ArrayList<>();
        memberPhones = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        memberView.setLayoutManager(layoutManager);
        adapter = new MembersAdapter(this, membernames, memberPhones);
        memberView.setAdapter(adapter);

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
            item = new TodoItem("","","","",false);
            membernames.clear();
            memberPhones.clear();
            adapter.notifyDataSetChanged();
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
        FloatingActionButton addMemberBtn = findViewById(R.id.addMemberBtn);
        addMemberBtn.setOnClickListener(v -> {
            // Gọi hàm hiện popup, truyền nút làm anchorView
            showListPopupWithButtons(v);
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
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        if (id == CONTACT_LOADER) {
            String[] SELECTED_FIELDS = new String[]
                    {
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                            ContactsContract.CommonDataKinds.Phone.NUMBER,
                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    };
            return new CursorLoader(this, ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    SELECTED_FIELDS,
                    null,
                    null,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " " + (isASC ? "ASC" : "DESC"));
        }

        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == CONTACT_LOADER) {
            List<ContactItem> contacts = new ArrayList<>();
            if (data != null) {
                while (!data.isClosed() && data.moveToNext()) {
                    String phone = data.getString(1);
                    String name = data.getString(2);
                    contacts.add(new ContactItem(name, phone));
                }
                contactProvider = new ContactProvider(this,contacts);
                if (state.equals("edit") ){
                    if (item.getMembers().isEmpty()){
                        membernames.clear();
                        memberPhones.clear();
                    }
                    else{
                        contactProvider.getData(item.getMembers(),membernames,memberPhones);
                    }
                    adapter.notifyDataSetChanged();
                }
                data.close();
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        loader = null;
    }
    private void showListPopupWithButtons(View anchorView) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.chose_contact_popup, null);

        ListView listView = popupView.findViewById(R.id.popup_listview);
        Button btnSave = popupView.findViewById(R.id.btn_save);
        Button btnClose = popupView.findViewById(R.id.btn_close);

        ContactAdapter contactAdapter = new ContactAdapter(this,R.layout.list_view_contact_item,contactProvider.getNoMembers(item.getMembers()));
        listView.setAdapter(contactAdapter);

        // Xử lý nút Close
        btnClose.setOnClickListener(v -> popupWindow.dismiss());

        // Xử lý nút Save
        btnSave.setOnClickListener(v -> {

            Type type = new TypeToken<List<String>>(){}.getType();
            Gson gson = new Gson();
            List<String> phoneList = gson.fromJson(item.getMembers(), type);
            if (phoneList == null) {
                phoneList = new ArrayList<>();
            }
            List<Boolean> selected = contactAdapter.getSelected();
            List<ContactItem> contactList = contactAdapter.getContactList();
            for(int i =0; i< selected.size();i++){
                if (selected.get(i)) phoneList.add(contactList.get(i).getPhone());
            }
            item.setMembers(gson.toJson(phoneList));
            boolean update = repo.update(item);
            membernames.clear();
            memberPhones.clear();
            contactProvider.getData(item.getMembers(),membernames,memberPhones);
            adapter.notifyDataSetChanged();
            popupWindow.dismiss();
        });

        popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                true);

        popupWindow.showAsDropDown(anchorView);
    }
}
