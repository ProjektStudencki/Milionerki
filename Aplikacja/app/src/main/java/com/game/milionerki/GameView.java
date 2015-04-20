package com.game.milionerki;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.game.menu.DrawerItemCustomAdapter;

import java.util.HashMap;
import java.util.Map;

import menu.ObjectDrawerItem;

/**
 * Class <code>GameView</code>
 * Klasa odpowiedzialna za widok gry
 * @author Kamil Gammert
 * @version 0.5, Kwiecień 2015
 */
public class GameView extends Activity implements ActionBar.OnNavigationListener {

    private String[] mNavigationDrawerItemTitles;
    private CharSequence mTitle;
    private DrawerLayout mDrawerLayout;
    public ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private com.game.sql.sqlAdapter sqlAdapter;
    private Fragment fragment = null;
    private Dialog alert;

    private Map<String, String> question = new HashMap<String, String>();
    private int questionCount = 0;
    private int cash = 0;

    private int profil = 0;

    /**
     * Stworzenie widoku gry
     *
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_view);

        /**
         * Odebranie danych z wyboru profilu
         */
        Intent intents = getIntent();
        if (intents != null) {
            profil = intents.getIntExtra("profil", 0);
            String nick = intents.getStringExtra("nick");

            TextView _testNick = (TextView) findViewById(R.id.testNick);
            _testNick.setText(nick);
        }

        /**
         * Ustawianie tytułu oraz paneli
         */
        mTitle = "Gra";
        mNavigationDrawerItemTitles = getResources().getStringArray(R.array.navigation_items_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        /**
         * Lista itemów menu bocznego
         */
        ObjectDrawerItem[] menuObject = new ObjectDrawerItem[4];
        menuObject[0] = new ObjectDrawerItem("menuItem", R.layout.menu_row, "Zrezygnuj");
        menuObject[1] = new ObjectDrawerItem("menuItem", R.layout.menu_row, "Nowa gra");
        menuObject[2] = new ObjectDrawerItem("menuItem", R.layout.menu_row, "Najlepsze wyniki");
        menuObject[3] = new ObjectDrawerItem("menuItem", R.layout.menu_row, "O grze");
        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.menu_row, menuObject);

        /**
         * Dodanie adaptera i nasluchów
         */
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* object menu */
                R.drawable.ic_drawer,  /* ikonka menu */
                R.string.drawer_open,  /* opis menu otwartego */
                R.string.drawer_close  /* opis menu zamkniętego */
        ) {
            /**
             * Kiedy menu jest zamknięte
             * @param view widok menu
             */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(mTitle);
            }

            /**
             * Gdy menu jsest otworzone
             * @param drawerView widok menu
             */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle("Menu");
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        if (savedInstanceState == null) {
            selectItem(0);
        }


    }

    /**
     * Funkcja odpowiedzialna za tworzenie w menu przycisku fizycznego
     *
     * @param menu
     * @return
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    /**
     * Funkcja odpowiedzialna za wykonywanie akcji z menu
     *
     * @param item wybrana opcja menu
     * @return
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        } else {
            switch (item.getItemId()) {
                case R.id.action_settings:
                    finish();
                    break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Funkcja, która blokuje przycisk cofania
     *
     * @param keyCode kod wciśniętego przycisku
     * @param event wydarzenie
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            //Toast.makeText(getApplicationContext(), "back press", Toast.LENGTH_LONG).show();
        }

        return false;
    }

    /**
     * Funkcja odpowiadzialna za zmianę obrazka
     * @param savedInstanceState
     */
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }


    /**
     * Klasa odpowiedzialna za nasłuch menu
     */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        /**
         * Funkcja odpowiedzialna za nasłuch wybrania elementu w menu
         *
         * @param parent lista wszystkich widoków w listView
         * @param view widok klikniętego itemu
         * @param position pozycja klikniętego profilu
         * @param id
         */
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /**
     * Funkcja obsługująca nasłuch wybranego elementu z menu
     *
     * @param position numer wybranego elementu
     */
    private void selectItem(int position) {
        if (position != 0) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setView(getCustomDialogLayout(position));
            alert = dialogBuilder.create();
            alert.show();
        }
    }

    private View getCustomDialogLayout(int val) {
        final int position = val;

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog, (ViewGroup) this.findViewById(R.id.layout_root));

        TextView _txt = (TextView) layout.findViewById(R.id.textView2);
        switch (position){
            case 1:
                _txt.setText(getResources().getText(R.string.alert_yes_or_no_new));
                break;
            case 2:
                _txt.setText(getResources().getText(R.string.alert_yes_or_no_ranking));
                break;
            case 3:
                _txt.setText(getResources().getText(R.string.alert_yes_or_no_about));
                break;
        }

        final TextView _yesButton = (TextView) layout.findViewById(R.id.yesButton);
        final TextView _noButton = (TextView) layout.findViewById(R.id.noButton);

        View.OnClickListener yesAction = new View.OnClickListener() {
            public void onClick(View v) {
                alert.dismiss();
                if (position == 1) {
                    /** nowa gra */
                    Intent intent = new Intent(getApplicationContext(), ChooseProfil.class);
                    startActivity(intent);
                    finish();
                } else if (position == 2) {
                    /** ranking */
                    Intent intent = new Intent(getApplicationContext(), RankingView.class);
                    startActivity(intent);
                    finish();
                } else if (position == 3) {
                    /** o grze */
                    Intent intent = new Intent(getApplicationContext(), AboutView.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        _yesButton.setOnClickListener(yesAction);

        View.OnClickListener noAction = new View.OnClickListener() {
            public void onClick(View v) {
                alert.dismiss();
            }
        };
        _noButton.setOnClickListener(noAction);

        return layout;
    }

    /**
     * Funkcja odpowiedzialna za ustawianie tytułu
     * @param title tytuł
     */
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * Funkcja odpowiedzialna za nasłuch wybrania elementu z menu
     *
     * @param arg0 numer pozycji
     * @param arg1 id pozycji
     * @return
     */
    public boolean onNavigationItemSelected(int arg0, long arg1) {
        fragment = null;

        return false;
    }
}