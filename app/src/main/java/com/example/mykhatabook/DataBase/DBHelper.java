package com.example.mykhatabook.DataBase;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.example.mykhatabook.Adapter.ModelData;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    Context activity;

    public DBHelper(@Nullable Context context) {
        super(context, "customer.db", null, 1);
        activity = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE customer(id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,mobile TEXT,whatsapp TEXT,address TEXT,rate INTEGER,date TEXT,payment TEXT,payment_method TEXT,duo_date TEXT)";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void InsertData(String name, String mobile, String whatsapp, String address, String rate, String date, String payment, String payment_method, String duo_date) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("name", name);
        contentValues.put("mobile", mobile);
        contentValues.put("whatsapp", whatsapp);
        contentValues.put("address", address);
        contentValues.put("rate", rate);
        contentValues.put("date", date);
        contentValues.put("payment", payment);
        contentValues.put("payment_method", payment_method);
        contentValues.put("duo_date", duo_date);
        long res=db.insert("customer",null,contentValues);
    }

    public List<ModelData> ReadData() {
        List<ModelData> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM customer";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") String mobile = cursor.getString(cursor.getColumnIndex("mobile"));
                @SuppressLint("Range") String whatsapp = cursor.getString(cursor.getColumnIndex("whatsapp"));
                @SuppressLint("Range") String address = cursor.getString(cursor.getColumnIndex("address"));
                @SuppressLint("Range") String rate = cursor.getString(cursor.getColumnIndex("rate"));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("date"));
                @SuppressLint("Range") String payment = cursor.getString(cursor.getColumnIndex("payment"));
                @SuppressLint("Range") String payment_method = cursor.getString(cursor.getColumnIndex("payment_method"));
                @SuppressLint("Range") String duo_date = cursor.getString(cursor.getColumnIndex("duo_date"));

                ModelData modelData = new ModelData();
                modelData.setId(id);
                modelData.setName(name);
                modelData.setMobile(mobile);
                modelData.setWhatsapp(whatsapp);
                modelData.setAddress(address);
                modelData.setRate(rate);
                modelData.setDate(date);
                modelData.setPayment(payment);
                modelData.setPayment_method(payment_method);
                modelData.setDuo_date(duo_date);
                list.add(modelData);
                Log.e("TAG", "ReadData: " + id + " " + name + " " + mobile + " " +whatsapp+" "+address+" "+rate+" "+date+" "+payment+" "+payment_method+" "+duo_date);
            } while (cursor.moveToNext());
        }

        return list;
    }
    public void DeleteData(String id) {
        SQLiteDatabase db=getWritableDatabase();
        db.delete("customer","id=?",new String[]{id});
    }
    public void UpdateData(String id,String name,String mobile,String whatsapp,String address,String rate,String date,String payment,String payment_method,String duo_date){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("name",name);
        contentValues.put("mobile",mobile);
        contentValues.put("whatsapp",whatsapp);
        contentValues.put("address",address);
        contentValues.put("rate",rate);
        contentValues.put("date",date);
        contentValues.put("payment",payment);
        contentValues.put("payment_method",payment_method);
        contentValues.put("duo_date",duo_date);
        db.update("customer",contentValues,"id=?",new String[]{id});
    }
}
