package com.game.milionerki;

import com.game.menu.DrawerItemCustomAdapter;
import com.game.sql.sqlAdapter;
import com.game.gameAction.SaveResult;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import menu.ObjectDrawerItem;

/**
 * Class <code>RankingView</code>
 * Klasa odpowiedzialna za tworzenie widoku rankingu
 * @author Kamil Gammert
 * @version 1.0, Marzec,Kwiecien 2015
 */
@SuppressWarnings("unused") public class RankingView extends Activity {

	private Map<String, String> ranking = new HashMap<String, String>();	
	private sqlAdapter sqlAdapter;

    /**
     * Funkcja tworząca widok, generowanie domyślnie
     *
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ranking_view);

        final TextView backButton = (TextView) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ListView bodyRanking = (ListView) findViewById(R.id.listView);

        SaveResult save = new SaveResult();
        //save.saveReuslt(getApplicationContext(), "Kamil", 100);

        sqlAdapter = new sqlAdapter(getApplicationContext());
        sqlAdapter.open();

        String[] kolumny = { "Pseudonim", "Uzyskany_wynik", "Avatar", "Czas_gry" };
        Cursor data = sqlAdapter.getColumn(kolumny, sqlAdapter.DB_RESULTS_TABLE, null, "Uzyskany_wynik DESC, Czas_gry DESC");

        int count = data.getCount();
        ObjectDrawerItem[] rankingObject = new ObjectDrawerItem[count];

        int i = 0;
        if (count > 0) {
            while (data.moveToNext()) {
                String name = data.getString(0);
                String result = data.getString(1);
                String avatar = data.getString(2);
                float time = data.getFloat(3);

                rankingObject[i] = new ObjectDrawerItem("rankingView", R.layout.ranking_row, name, result, avatar, (i + 1));
                i++;
            }

            DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.ranking_row, rankingObject);
            bodyRanking.setAdapter(adapter);
        }
        sqlAdapter.close();
    }

	private Map<String, String> downloadData()  {

		return ranking;		
	}

    /**
     * Funkcja odpowiedzialna za tworzenie w menu przycisku fizycznego
     *
     * @param menu
     * @return
     */
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.start, menu);
        return true;
    }

    /**
     * Funkcja odpowiedzialna za wykonywanie akcji z menu
     *
     * @param item wybrana opcja menu
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
