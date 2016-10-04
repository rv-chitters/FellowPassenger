package com.example.raghu.fellowpassenger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by raghu on 22/09/16.
 */
public class DbHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "data.db";
    private static final int VERSION = 1;

    private static final String TABLE_NAME = "locations";
    private static final String ID = "_id";
    private static final String NAME = "name";
    private static final String LATITUDE = "lat";
    private static final String LONGITUDE = "lng";
    private static final String STATUS = "status";
    private static final String DISTANCE = "distance";

    private SQLiteDatabase database;

    public DbHandler(Context context) {
        super(context, DB_NAME, null, VERSION);
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
                DISTANCE + " REAL " +
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

    public void insert(int id, String name, Double lat, Double lng, int status, Float distance){
        this.openDB();
        ContentValues contentValues = new ContentValues();
        if(id != -1)
            contentValues.put(ID,id);
        contentValues.put(NAME,name);
        contentValues.put(LATITUDE,lat);
        contentValues.put(LONGITUDE,lng);
        contentValues.put(STATUS,status);
        contentValues.put(DISTANCE,distance);
        database.insert(TABLE_NAME,null,contentValues);
        DataHandler.loadLocationData();
        this.closeDB();
    }

    public void updateStatus(int id,boolean status){
        int statusFlag = 0;
        if(status == true){
            statusFlag = 1;
        }
        this.openDB();
        ContentValues contentValues = new ContentValues();
        contentValues.put(STATUS,statusFlag);
        String where = ID + " = " + id;
        database.update(TABLE_NAME,contentValues,where,null);
        DataHandler.loadLocationData();
        this.closeDB();
    }

    public void updateDistance(int id,float distance){
        this.openDB();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DISTANCE,distance);
        String where = ID + " = " + id;
        database.update(TABLE_NAME,contentValues,where,null);
        DataHandler.loadLocationData();
        this.closeDB();
    }

    public void delete(int id){
        this.openDB();
        String where = ID + " = " + id;
        database.delete(TABLE_NAME,where,null);
        DataHandler.loadLocationData();
        this.closeDB();
    }

    public List<LocationData> getAllRecords(){
        this.openDB();
        List ls = new ArrayList();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor =  database.rawQuery(query,null);
        if (cursor.moveToFirst()){
            do{
                int id = cursor.getInt(cursor.getColumnIndex(ID));
                String name = cursor.getString(cursor.getColumnIndex(NAME));
                Double lat = cursor.getDouble(cursor.getColumnIndex(LATITUDE));
                Double lng = cursor.getDouble(cursor.getColumnIndex(LONGITUDE));
                int status = cursor.getInt(cursor.getColumnIndex(STATUS));
                Float  distance = cursor.getFloat(cursor.getColumnIndex(DISTANCE));
                LocationData ld = new LocationData(id,name,lat,lng,status,distance);
                ls.add(ld);
            }while(cursor.moveToNext());
        }
        cursor.close();
        this.closeDB();
        return ls;
    }
}