package com.game.milionerki;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Class <code>CreatePlayersAvatar</code>
 * Klasa odpowiedzialna za generowanie widoku wyboru avatara przy tworzeniu profilu
 * @author Kamil Gammert
 * @version 1.0, Marzec,Kwiecien 2015
 */
public class CreatePlayersAvatar extends Activity {

    private int selectedAvatar = 0;
    private int maxAvatar = 0;
    TypedArray array_avatar;

    ImageView _img;
    ImageView _prvAvek;
    ImageView _nxtAvek;

    /**
     * Funkcja tworząca widok, generowana domyślnie
     *
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_players_avatar);

        TextView _nextBtn = (TextView) findViewById(R.id.nextButton);
        TextView _backBtn = (TextView) findViewById(R.id.backButton);

        _prvAvek = (ImageView) findViewById(R.id.imageView);
        _nxtAvek = (ImageView) findViewById(R.id.imageView3);
        _img = (ImageView) findViewById(R.id.imageView4);

        array_avatar = getResources().obtainTypedArray(R.array.array_avek);
        maxAvatar = array_avatar.length();

        if (selectedAvatar == 0) {
            _prvAvek.setVisibility(View.INVISIBLE);
        } else {
            _prvAvek.setVisibility(View.VISIBLE);
        }

        if (selectedAvatar == maxAvatar) {
            _nxtAvek.setVisibility(View.INVISIBLE);
        } else {
            _nxtAvek.setVisibility(View.VISIBLE);
        }

        /**
         *Nasłuch, który umożliwia cofanie avatara
         */
        _prvAvek.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (selectedAvatar > 0) {
                    selectedAvatar -= 1;
                }

                if (selectedAvatar == 0) {
                    _prvAvek.setVisibility(View.INVISIBLE);
                } else {
                    _prvAvek.setVisibility(View.VISIBLE);
                }

                _img.setImageResource(array_avatar.getResourceId(selectedAvatar, -1));
            }
        });

        /**
         * Nasłuch przewijający avatar do przodu
         */
        _nxtAvek.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (selectedAvatar < maxAvatar) {
                    selectedAvatar += 1;
                }

                if (selectedAvatar == maxAvatar) {
                    _nxtAvek.setVisibility(View.INVISIBLE);
                } else {
                    _nxtAvek.setVisibility(View.VISIBLE);
                }
                _img.setImageResource(array_avatar.getResourceId(selectedAvatar, -1));
            }
        });

        /**
         * Nasłuch na hiperlnik "Dalej"
         */
        _nextBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreatePlayers.class);
                intent.putExtra("avek", selectedAvatar);
                startActivity(intent);
                finish();
            }
        });

        /**
         * Nasłuch powrotu do menu głównego
         */
        _backBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                startActivity(intent);
                finish();
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
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
