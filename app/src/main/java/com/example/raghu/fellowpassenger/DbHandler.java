package com.example.raghu.fellowpassenger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by raghu on 22/09/16.
 */
public class DbHandler extends SQLiteOpenHelper {

    private static final String DBNAME = "data.db";
    private static final int VERSION = 1;

    private static final String TABLE_NAME = "locations";
    private static final String ID = "_id";
    private static final String NAME = "name";
    private static final String LATITUDE = "lat";
    private static final String LONGITUDE = "lng";
    private static final String STATUS = "status";

    private SQLiteDatabase database;

    public DbHandler(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String queryTable = "CREATE TABLE " + TABLE_NAME +
                " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NAME + " TEXT NOT NULL, " +
                LATITUDE + " REAL NOT NULL, " +
                LONGITUDE + " REAL NOT NULL, " +
                STATUS + " INTEGER NOT NULL, " +
                ")";
        sqLiteDatabase.execSQL(queryTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void openDB(){
        database = getWritableDatabase();
    }

    public void closeDB(){
        if(database != null && database.isOpen()){
            database.close();
        }
    }

    public long insert(int id,String name,Double lat,Double lng,int status){
        ContentValues contentValues = new ContentValues();
        if(id != -1)
            contentValues.put(ID,id);
        contentValues.put(NAME,name);
        contentValues.put(LATITUDE,lat);
        contentValues.put(LONGITUDE,lng);
        contentValues.put(STATUS,status);
        return  database.insert(TABLE_NAME,null,contentValues);
    }

    public long update(int id,String name,Double lat,Double lng,int status){
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME,name);
        contentValues.put(LATITUDE,lat);
        contentValues.put(LONGITUDE,lng);
        contentValues.put(STATUS,status);
        String where = ID + " = " + id;
        return  database.update(TABLE_NAME,contentValues,where,null);
    }

    public long delete(int id){
        String where = ID + " = " + id;
        return database.delete(TABLE_NAME,where,null);
    }

    public Cursor getAllRecords(){
        String query = "SELECT * FROM " + TABLE_NAME;
        return database.rawQuery(query,null);
    }
}