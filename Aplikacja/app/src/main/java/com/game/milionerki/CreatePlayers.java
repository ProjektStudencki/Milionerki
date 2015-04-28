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

/**
 * Class <code>CreatePlayers</code>
 * Klasa odpowiedzialna za generowanie widoku tworzenia profilu
 * @author Kamil Gammert
 * @version 1.0, Marzec,Kwiecien 2015
 */
public class CreatePlayers extends Activity {

    private int selectedAvatar = 0;
    private com.game.sql.sqlAdapter sqlAdapter;

    /**
     * Funkcja tworząca widok, generowana domyślnie
     *
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_players_profil);

        TextView _createBtn = (TextView) findViewById(R.id.createBtn);
        TextView _backBtn = (TextView) findViewById(R.id.backButton);

        Intent intents = getIntent();
        if (intents != null) {
            selectedAvatar = intents.getIntExtra("avek", 0);
        }

        /**
         * Nasłuch pozwajający wrócić do menu głównego
         */
        _backBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                startActivity(intent);
                finish();
            }
        });


        /**
         * Nasluch, który reaguje na kliknięcie hiperlinka
         */
        _createBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //zakładanie
                EditText _name = (EditText) findViewById(R.id.nameText);
                EditText _surname = (EditText) findViewById(R.id.surnameText);
                EditText _nick = (EditText) findViewById(R.id.nickText);

                String name = _name.getText().toString().trim();
                String surname = _surname.getText().toString().trim();
                String nick = _nick.getText().toString().trim();

                String _nameTmp = "";
                String alert = "";
                Boolean error = false;


                if (nick.length() == 0) {
                    error = true;
                    alert += getApplicationContext().getString(R.string.error_no_nick) + " " + getApplicationContext().getString(R.string.error_long) + "\n";
                }

                if (name.length() > 25) {
                    error = true;
                    alert += getApplicationContext().getString(R.string.create_profil_name) + " " + getApplicationContext().getString(R.string.error_long) + "\n";
                }
                if (surname.length() > 25) {
                    error = true;
                    alert += getApplicationContext().getString(R.string.create_profil_srunname) + " " + getApplicationContext().getString(R.string.error_long) + "\n";
                }
                if (nick.length() > 25) {
                    error = true;
                    alert += getApplicationContext().getString(R.string.create_profil_nick) + " " + getApplicationContext().getString(R.string.error_long);
                }

                if (!error) {
                    sqlAdapter = new sqlAdapter(getApplicationContext());
                    sqlAdapter.open();

                    String[] kolumny = {"Pseudonim", "Imie_gracza", "Nazwisko_gracza"};
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
                } else {
                    Toast.makeText(getApplicationContext(), alert, Toast.LENGTH_LONG).show();
                }
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
