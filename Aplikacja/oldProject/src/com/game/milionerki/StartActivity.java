package com.game.milionerki;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

@SuppressWarnings("unused") public class StartActivity extends Activity {

	private TextView _startGame;
	private TextView _rankingView;
	private TextView _aboutView;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        
        _startGame = (TextView) findViewById(R.id.startGame);
        _rankingView = (TextView) findViewById(R.id.rankingView);
        _aboutView = (TextView) findViewById(R.id.aboutView);
        
        _aboutView.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), AboutView.class);
				startActivity(intent);
				finish();
			}
		});
        
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
