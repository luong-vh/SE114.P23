package com.example.uddd_b3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class TodoRepo extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Todo.db";
    public static final String TABLE_NAME = "todos";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DESC = "description";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_IS_DONE = "is_done";
    public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME
            + " (" + COLUMN_ID + " TEXT PRIMARY KEY,"
            + COLUMN_TITLE + " TEXT, "
            + COLUMN_DESC + " TEXT, "
            + COLUMN_DATE + " TEXT, "
            + COLUMN_IS_DONE + " INTEGER " + ")";
    public static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    public TodoRepo(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL(SQL_DELETE_TABLE);
        onCreate(db);
    }
    public void addNew(TodoItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, item.getId());
        values.put(COLUMN_TITLE, item.getTitle());
        values.put(COLUMN_DESC, item.getDescription());
        values.put(COLUMN_DATE, item.getDate());
        values.put(COLUMN_IS_DONE, item.isDone() ? 1 : 0);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }
    public boolean update(TodoItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, item.getTitle());
        values.put(COLUMN_DESC, item.getDescription());
        values.put(COLUMN_DATE, item.getDate());
        values.put(COLUMN_IS_DONE, item.isDone() ? 1 : 0);
        int rowAffected = db.update(TABLE_NAME, values,
                COLUMN_ID + "= ?", new String[]{item.getId()});
        db.close();
        return rowAffected > 0;
    }
    public boolean delete(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowAffected = db.delete(TABLE_NAME, COLUMN_ID + "= ?", new String[]{id});
                db.close();
        return rowAffected > 0;
    }
    public ArrayList<TodoItem> loadAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] projection = {
                COLUMN_ID,
                COLUMN_TITLE,
                COLUMN_DESC,
                COLUMN_DATE,
                COLUMN_IS_DONE
        };
        Cursor cursor = db.query(TABLE_NAME, projection, null,
                null, null, null, null);
        ArrayList<TodoItem> items = new ArrayList<TodoItem>();
        while (cursor.moveToNext()) {
            String id = cursor.getString(0);
            String title = cursor.getString(1);
            String desc = cursor.getString(2);
            String date = cursor.getString(3);
            Boolean isDone = cursor.getInt(4)==1;
            items.add(new TodoItem(id,title, desc,date,isDone));
        }
        db.close();
        return items;
    }
    public TodoItem getById(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] projection = {
                COLUMN_ID,
                COLUMN_TITLE,
                COLUMN_DESC,
                COLUMN_DATE,
                COLUMN_IS_DONE
        };
        Cursor cursor = db.query(TABLE_NAME, projection,
                COLUMN_ID + "= ?", new String[]{id}, null, null, null);
        ArrayList<TodoItem> items = new ArrayList<TodoItem>();
        if(cursor.moveToFirst()){
            String title = cursor.getString(1);
            String desc = cursor.getString(2);
            String date = cursor.getString(3);
            Boolean isDone = cursor.getInt(4)==1;
            db.close();
            return new TodoItem(id,title, desc,date,isDone);
        }
        return null;
    }
}