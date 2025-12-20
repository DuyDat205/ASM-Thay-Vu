package com.example.campusexpensemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "CampusExpense.db";
    private static final int DATABASE_VERSION = 5; // Phiên bản mới nhất

    private static final String TABLE_USERS = "users";
    private static final String COL_USERNAME = "username";
    private static final String COL_PASSWORD = "password";
    private static final String COL_EMAIL = "email";
    private static final String TABLE_EXPENSES = "expenses";
    private static final String COL_ID = "id";
    private static final String COL_AMOUNT = "amount";
    private static final String COL_DESC = "description";
    private static final String COL_CATEGORY = "category";
    private static final String COL_DATE = "date";

    private static final String TABLE_BUDGETS = "budgets";
    private static final String COL_BUDGET_CAT = "category";
    private static final String COL_BUDGET_LIMIT = "limit_amount";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                COL_USERNAME + " TEXT PRIMARY KEY, " +
                COL_PASSWORD + " TEXT, " +
                COL_EMAIL + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_EXPENSES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "amount REAL, " +
                "description TEXT, " +
                "category TEXT, " +
                "date TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_BUDGETS + " (category TEXT PRIMARY KEY, limit_amount REAL)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUDGETS);
        onCreate(db);
    }

    public boolean registerUser(String user, String pass, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_USERNAME, user);
        cv.put(COL_PASSWORD, pass);
        cv.put(COL_EMAIL, email);
        long result = db.insert(TABLE_USERS, null, cv);
        return result != -1;
    }


    public boolean checkUser(String user, String pass) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE username=? AND password=?", new String[]{user, pass});
        return cursor.getCount() > 0;
    }

    public void addExpense(double amount, String desc, String category, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_AMOUNT, amount);
        cv.put(COL_DESC, desc);
        cv.put(COL_CATEGORY, category);
        cv.put(COL_DATE, date);
        db.insert(TABLE_EXPENSES, null, cv);
    }

    public void updateExpense(String id, double amount, String desc, String category, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_AMOUNT, amount);
        cv.put(COL_DESC, desc);
        cv.put(COL_CATEGORY, category);
        cv.put(COL_DATE, date);
        db.update(TABLE_EXPENSES, cv, "id=?", new String[]{id});
    }

    public void deleteExpense(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EXPENSES, "id=?", new String[]{id});
    }

    public Cursor getAllExpenses() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_EXPENSES + " ORDER BY id DESC", null);
    }

    public void setBudget(String category, double limit) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_BUDGET_CAT, category);
        cv.put(COL_BUDGET_LIMIT, limit);
        db.replace(TABLE_BUDGETS, null, cv);
    }

    public double getBudgetLimit(String category) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT limit_amount FROM " + TABLE_BUDGETS + " WHERE category=?", new String[]{category});
        if (cursor.moveToFirst()) return cursor.getDouble(0);
        return 0;
    }

    public double getCategorySpent(String category) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(amount) FROM " + TABLE_EXPENSES + " WHERE category=?", new String[]{category});
        if (cursor.moveToFirst()) return cursor.getDouble(0);
        return 0;
    }

    public double getTotalSpent() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(amount) FROM " + TABLE_EXPENSES, null);
        if (cursor.moveToFirst()) return cursor.getDouble(0);
        return 0;
    }
}