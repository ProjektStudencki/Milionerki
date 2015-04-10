package com.game.sql;

import android.content.ContentValues;
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

    private static final String DB_NAME = "milionerkiGame.db";
    public static final String DB_USERS_TABLE = "Gracze";
    public static final String DB_QUESTION_TABLE = "PytaniaiOdpowiedzi";
    public static final String DB_TEXTS_LED_TABLE = "TekstyProwadzacego";
    public static final String DB_TEXTS_FRIEND_TABLE = "TekstyPrzyjacielaDoKtoregoDzwonimy";
    public static final String DB_RESULTS_TABLE = "WynikiGraczy";
 
    private static final String DB_CREATE_USERS_TABLE =
            "CREATE TABLE " + DB_USERS_TABLE + "( " +
                "Pseudonim            text not null, " +
                "Imie_gracza          text, " +
                "Nazwisko_gracza      text, " +
                "Avatar               int not null," +
                "primary key (Pseudonim)" +
            ");";
    private static final String DROP_USERS_TABLE =
            "DROP TABLE IF EXISTS " + DB_USERS_TABLE;

    private static final String DB_CREATE_QUESTION_TABLE =
            "CREATE TABLE " + DB_QUESTION_TABLE + "( " +
                "Nr_Pytania           INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "Pytanie              text not null, " +
                "Poprawna_odp         text not null, " +
                "Zla_odp1             text not null, " +
                "Zla_odp2             text not null, " +
                "Zla_odp3             text not null, " +
                "Poziom_trudnosci     Integer(2) not null " +
            ");";
    private static final String DROP_QUESTION_TABLE =
            "DROP TABLE IF EXISTS " + DB_QUESTION_TABLE;

    private static final String DB_CREATE_TEXTS_LED_TABLE =
            "CREATE TABLE " + DB_TEXTS_LED_TABLE + "( " +
                "ID_tekstu            INTEGER(3) PRIMARY KEY, " +
                "Tekst_prowadzacego   text not null, " +
                "Prawdopodobiensto_wypadniecia Integer(3) not null " +
            ");";
    private static final String DROP_TEXTS_LED_TABLE =
            "DROP TABLE IF EXISTS " + DB_TEXTS_LED_TABLE;

    private static final String DB_CREATE_TEXTS_FRIEND_TABLE =
            "CREATE TABLE " + DB_TEXTS_FRIEND_TABLE + "( " +
                "ID_tesktu            INTEGER(3) PRIMARY KEY, " +
                "Tekst_przyjaciela    text not null, " +
                "Prawdopodobienstwo_wypadniecia Integer(3) not null "+
            ");";
    private static final String DROP_TEXTS_FRIEND_TABLE =
            "DROP TABLE IF EXISTS " + DB_TEXTS_FRIEND_TABLE;

    private static final String DB_CREATE_RESULTS_TABLE =
            "CREATE TABLE " + DB_RESULTS_TABLE + "( " +
                "Nr_wyniku            int not null, " +
                "Pseudonim            text not null, " +
                "Czas_gry             float not null, " +
                "Data_wpisu           date not null, " +
                "Uzyskany_wynik       int not null, " +
                "primary key (Nr_wyniku), " +
                "FOREIGN KEY(Pseudonim) REFERENCES " + DB_USERS_TABLE + "(Pseudonim)" +
            ");";
    private static final String DROP_RESULTS_TABLE =
            "DROP TABLE IF EXISTS " + DB_RESULTS_TABLE;

    private SQLiteDatabase db;
    private Context context;
    private DatabaseHelper dbHelper;
 
    private static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, factory, version);
        }
 
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE_USERS_TABLE);
            db.execSQL(DB_CREATE_QUESTION_TABLE);
            db.execSQL(DB_CREATE_TEXTS_LED_TABLE);
            db.execSQL(DB_CREATE_TEXTS_FRIEND_TABLE);
            db.execSQL(DB_CREATE_RESULTS_TABLE);

            Log.d(DEBUG_TAG, "Database creating...");
            Log.d(DEBUG_TAG, "Table ver." + DB_VERSION + " created");
        }
 
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_USERS_TABLE);
            db.execSQL(DROP_QUESTION_TABLE);
            db.execSQL(DROP_TEXTS_LED_TABLE);
            db.execSQL(DROP_TEXTS_FRIEND_TABLE);
            db.execSQL(DROP_RESULTS_TABLE);
 
            Log.d(DEBUG_TAG, "Database updating...");
            Log.d(DEBUG_TAG, "Table updated from ver." + oldVersion + " to ver." + newVersion);
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
            Log.i("info", e.getMessage());
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
     
    public boolean deleteTodo(int id, String table, String key_id){
        String where = key_id + "=" + id;
        return db.delete(table, where, null) > 0;
    }
 
    public Cursor getColumn(String[] kolumny, String table) {
    	return db.query(table, kolumny, null, null, null, null, null);
    }
    
    public Cursor getColumn(String[] kolumny, String table, String where) {
    	return db.query(table, kolumny, where, null, null, null, null);
    }

    public long insertTodo(ContentValues newTodoValues, String table) {
        return db.insert(table, null, newTodoValues);
    }
    
}
