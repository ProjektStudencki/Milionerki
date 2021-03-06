package com.game.milionerki;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Class <code>StartActivity</code>
 * Klasa odpowiedzialna za tworzenie widoku głównego gry
 * @author Kamil Gammert
 * @version 1.0, Marzec,Kwiecien 2015
 */
@SuppressWarnings("unused") public class StartActivity extends Activity {

	private TextView _startGame;
	private TextView _rankingView;
	private TextView _aboutView;

    /**
     * Funkcja tworząca widok, generowana domyślnie
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        
        _startGame = (TextView) findViewById(R.id.startGame);
        _rankingView = (TextView) findViewById(R.id.rankingView);
        _aboutView = (TextView) findViewById(R.id.aboutView);

        _startGame.setPaintFlags(_startGame.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        _rankingView.setPaintFlags(_rankingView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        _aboutView.setPaintFlags(_aboutView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        _startGame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChooseProfil.class);
                startActivity(intent);
            }
        });

        _rankingView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RankingView.class);
                startActivity(intent);
            }
        });

        _aboutView.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), AboutView.class);
				startActivity(intent);
			}
		});
        
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
