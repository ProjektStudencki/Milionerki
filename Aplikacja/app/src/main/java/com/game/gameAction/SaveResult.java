package com.game.gameAction;

import com.game.sql.sqlAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

/**
 * Class <code>SaveResult</code>
 * Klasa odpowiedzialna za zapisywanie wyników rankingowych
 * @author Kamil Gammert
 * @version 1.0, Marzec,Kwiecien 2015
 */

public class SaveResult {

	private sqlAdapter sqlAdapter;

    /**
     * Funcja do wyświetlania okna dialogowego zapisu
     * Na dzień dzisiejszy nie została zaimplementowana
     *
     * @param data
     * @return 
     */
	public Dialog saveDialog(Map<String, String> data) {
		return null;
	}

    /**
     * Funkcja realizuje zapis uzyskanego wyniku do bazy SQLite
	 *
     * @param context Kontekst
     * @param nick nick Nazwa gracza, który uzyskał wynik
     * @param result Uzyskany wynik w grze
     */
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
