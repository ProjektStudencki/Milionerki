package com.game.milionerki;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.game.sql.sqlAdapter;

public class CreatePlayers extends Activity {

    private int selectedAvatar = 0;
    private com.game.sql.sqlAdapter sqlAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_players_profil);

        TextView _createBtn = (TextView) findViewById(R.id.createBtn);
        TextView _backBtn = (TextView) findViewById(R.id.backButton);

        Intent intents = getIntent();
        if (intents != null) {
            selectedAvatar = intents.getIntExtra("avek", 0);
        }

        _backBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                startActivity(intent);
                finish();
            }
        });


        _createBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //zakładanie
                EditText _name = (EditText) findViewById(R.id.nameText);
                EditText _surname = (EditText) findViewById(R.id.surnameText);
                EditText _nick = (EditText) findViewById(R.id.nickText);

                String name = _name.getText().toString();
                String surname = _surname.getText().toString();
                String nick = _nick.getText().toString();

                String _nameTmp = "";
                Boolean error = false;

                sqlAdapter = new sqlAdapter(getApplicationContext());
                sqlAdapter.open();

                String[] kolumny = { "Pseudonim", "Imie_gracza", "Nazwisko_gracza" };
                Cursor data = sqlAdapter.getColumn(kolumny, sqlAdapter.DB_USERS_TABLE);
                while (data.moveToNext()) {
                    _nameTmp = data.getString(0);

                    if (_nameTmp.equalsIgnoreCase(name)) {
                        error = true;
                    }
                }

                if (error) {
                    Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.error_create), Toast.LENGTH_SHORT).show();
                } else {
                    ContentValues newTodoValuess = new ContentValues();
                    newTodoValuess.put("Pseudonim", nick);
                    newTodoValuess.put("Imie_gracza", name);
                    newTodoValuess.put("Nazwisko_gracza", surname);
                    newTodoValuess.put("Avatar", selectedAvatar);

                    sqlAdapter.insertTodo(newTodoValuess, sqlAdapter.DB_USERS_TABLE);

                    Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.success_create_profil), Toast.LENGTH_LONG).show();
                }

                sqlAdapter.close();

                //powrót do głównego menu
                Intent intent = new Intent(getApplicationContext(), StartActivity.class);
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