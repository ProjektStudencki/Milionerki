package com.example.milionerki;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StartActivity extends Activity {

	private Button _startGame;
	private TextView _rankingView;
	private TextView _aboutMeView;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        
        _startGame = (Button) findViewById(R.id.startGame);
        _rankingView = (TextView) findViewById(R.id.rankingView);
        _aboutMeView = (TextView) findViewById(R.id.aboutMeView);
        
        _aboutMeView.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), AboutMeView.class);
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
