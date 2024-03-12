package com.simplecrudsqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "names_db";
    private static final String TABLE_NAMES = "names";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAMES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_NAME + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAMES);
        onCreate(db);
    }

    // Method to add a new name
    public void addName(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        db.insert(TABLE_NAMES, null, values);
        db.close();
    }

    // Method to delete a name
    public void deleteName(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAMES, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Method to delete all names
    public void deleteAllNames() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAMES, null, null);
        db.close();
    }

    // Method to get all names
    public List<String> getAllNames() {
        List<String> namesList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAMES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(1);
                namesList.add(name);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return namesList;
    }
}

