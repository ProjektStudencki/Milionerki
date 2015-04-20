package com.game.milionerki;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Class <code>AboutView</code>
 * Klasa odpowiedzialna za wyświetlanie widoku "O grze"
 * @author Kamil Gammert
 * @version 1.0, Marzec,Kwiecien 2015
 */
public class AboutView extends Activity {

    /**
     * Funkcja tworząca widok, domyślna
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_view);
    }

    /**
     * Funkcja odpowiedzialna za tworzenie przycisku fizycznego w menu
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
