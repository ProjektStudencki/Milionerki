package com.game.milionerki;

import com.game.menu.DrawerItemCustomAdapter;
import com.game.sql.sqlAdapter;
import com.game.gameAction.SaveResult;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import menu.ObjectDrawerItem;

@SuppressWarnings("unused") public class RankingView extends Activity {

	private Map<String, String> ranking = new HashMap<String, String>();	
	private sqlAdapter sqlAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ranking_view);

        ListView bodyRanking = (ListView) findViewById(R.id.listView);

        SaveResult save = new SaveResult();
        //save.saveReuslt(getApplicationContext(), "Kamil", 100);

        sqlAdapter = new sqlAdapter(getApplicationContext());
        sqlAdapter.open();

        String[] kolumny = { "Pseudonim", "Uzyskany_wynik" };
        Cursor data = sqlAdapter.getColumn(kolumny, sqlAdapter.DB_RESULTS_TABLE);

        int count = data.getCount();
        ObjectDrawerItem[] rankingObject = new ObjectDrawerItem[count];

        int i = 0;
        if (count > 0) {
            while (data.moveToNext()) {
                String name = data.getString(0);
                String result = data.getString(1);
                String avatar = "";

                rankingObject[i] = new ObjectDrawerItem("rankingView", R.layout.ranking_row, name, result, avatar);
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
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}