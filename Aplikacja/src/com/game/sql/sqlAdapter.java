package com.game.sql;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
 
public class sqlAdapter {
    private static final String DEBUG_TAG = "Baza danych";
 
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "interKamilSQL.db";
    public static final String DB_TODO_TABLE = "zadania";
 
    public static final String KEY_ID = "id";
    public static final String ID_OPTIONS = "INTEGER PRIMARY KEY AUTOINCREMENT";
    
 
    private static final String DB_CREATE_TODO_TABLE =
            "CREATE TABLE " + DB_TODO_TABLE + "( " +
            KEY_ID + " " + ID_OPTIONS + 
            ");";
    
    private static final String DROP_TODO_TABLE =
            "DROP TABLE IF EXISTS " + DB_TODO_TABLE;
 
    private SQLiteDatabase db;
    private Context context;
    private DatabaseHelper dbHelper;
 
    private static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context, String name,
                CursorFactory factory, int version) {
            super(context, name, factory, version);
        }
 
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE_TODO_TABLE);
 
            Log.d(DEBUG_TAG, "Database creating...");
            Log.d(DEBUG_TAG, "Table " + DB_TODO_TABLE + " ver." + DB_VERSION + " created");
        }
 
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_TODO_TABLE);
 
            Log.d(DEBUG_TAG, "Database updating...");
            Log.d(DEBUG_TAG, "Table " + DB_TODO_TABLE + " updated from ver." + oldVersion + " to ver." + newVersion);
            Log.d(DEBUG_TAG, "All data is lost.");
 
            onCreate(db);
        }
    }
 
    public sqlAdapter(Context context) {
        this.context = context;
    }
 
    public sqlAdapter open(){
        dbHelper = new DatabaseHelper(context, DB_NAME, null, DB_VERSION);
        try {
            db = dbHelper.getWritableDatabase();
        } catch (SQLException e) {
            db = dbHelper.getReadableDatabase();
        }
        return this;
    }
 
    public void close() {
        dbHelper.close();
    }
 
    public void sql(String sql) {
    	db.execSQL(sql);
    }
     
    public boolean deleteTodo(int id){
        String where = KEY_ID + "=" + id;
        return db.delete(DB_TODO_TABLE, where, null) > 0;
    }
 
    public Cursor getColumn(String[] kolumny) {
    	return db.query(DB_TODO_TABLE, kolumny, null, null, null, null, null);    	
    }
    
    public Cursor getColumn(String[] kolumny, String where) {
    	return db.query(DB_TODO_TABLE, kolumny, where, null, null, null, null);    	
    }
    
}
