package com.game.gameAction;

import com.game.sql.sqlAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

@SuppressWarnings("unused") public class SaveResult {

	private sqlAdapter sqlAdapter;
	
	public Dialog saveDialog(Map<String, String> data) {
		return null;
		
	}
	
	public void saveReuslt(Context context, String nick, int result) {
		sqlAdapter = new sqlAdapter(context);
        sqlAdapter.open();

        float time = 25;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());

        String[] kolumny = { "Pseudonim", "Uzyskany_wynik" };
        Cursor data = sqlAdapter.getColumn(kolumny, sqlAdapter.DB_RESULTS_TABLE);
        int count = data.getCount();

        ContentValues newTodoValuess = new ContentValues();
        newTodoValuess.put("Pseudonim", nick);
       // sqlAdapter.insertTodo(newTodoValuess, sqlAdapter.DB_USERS_TABLE);

        ContentValues newTodoValues = new ContentValues();
        newTodoValues.put("Nr_wyniku", count + 1);
        newTodoValues.put("Pseudonim", nick);
        newTodoValues.put("Czas_gry", time);
        newTodoValues.put("Data_wpisu", date);
        newTodoValues.put("Uzyskany_wynik", result);

        sqlAdapter.insertTodo(newTodoValues, sqlAdapter.DB_RESULTS_TABLE);
        sqlAdapter.close();
	}
	
}
