package com.game.milionerki;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.game.gameAction.QuestionsData;
import com.game.gameAction.SaveResult;
import com.game.menu.DrawerItemCustomAdapter;

import java.util.Calendar;
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
    private Calendar now;

    private String nick = "";


    //ustawienia pytań
    private Button btn_a;
    private Button btn_b;
    private Button btn_c;
    private Button btn_d;

    private int tab_quest[] = new int[10];
    private QuestionsData questionsData;

    /**
     * Stworzenie widoku gry
     *
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_view);

        for (int i = 0; i < 10; i++)
            tab_quest[i] = -1;

        btn_a = (Button) findViewById(R.id.btn_a);
        btn_b = (Button) findViewById(R.id.btn_b);
        btn_c = (Button) findViewById(R.id.btn_c);
        btn_d = (Button) findViewById(R.id.btn_d);
        onOrOffButton(true);

        /**
         * Odebranie danych z wyboru profilu
         */
        Intent intents = getIntent();
        if (intents != null) {
            profil = intents.getIntExtra("profil", 0);
            nick = intents.getStringExtra("nick");

            questionCount = 1;
        }

        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);

        /*
        final float width = metrics.widthPixels;
        LinearLayout.LayoutParams layoutParamsWidth50 = new LinearLayout.LayoutParams((int) (width/2) , ViewGroup.LayoutParams.MATCH_PARENT);

        btn_a.setLayoutParams(layoutParamsWidth50);
        btn_b.setLayoutParams(layoutParamsWidth50);
        btn_c.setLayoutParams(layoutParamsWidth50);
        btn_d.setLayoutParams(layoutParamsWidth50);*/

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

        /** Ustawienie aktualnego czasu */
        now = Calendar.getInstance();

        /** Losowanie pytań */
        questionsData = new QuestionsData(getApplicationContext());
        setQuestion();
    }

    /**
     * Funkcja odpowiedzialna za ustawianie pytań
     */
    private void setQuestion() {
        Log.i("Info", "setQuestion");
        if (questionCount == 10) {
            endGame("win");
        } else {
            /** Ustawianie **/
            TextView _currCash = (TextView) findViewById(R.id.prog_txt);
            TextView _currQuestionCount = (TextView) findViewById(R.id.question_count);
            TypedArray cash_array = getResources().obtainTypedArray(R.array.cash_array);

            _currCash.setText(getResources().getString(R.string.cash) + " " + cash_array.getString(questionCount - 1));
            _currQuestionCount.setText(getResources().getString(R.string.question_count) + " " + questionCount);

            /* pobranie pytań */
            question = questionsData.downloadQuestions(questionCount, tab_quest);
            String val = question.get("error");
            if (val != null) {
                finish();
            } else {
                onOrOffButton(true);
                String poprawna_odp = question.get("poprawna");
                tab_quest[questionCount - 1] = Integer.parseInt(question.get("id"));

                TextView question_txt = (TextView) findViewById(R.id.question_txt);

                question_txt.setText(question.get("pytanie"));
                btn_a.setText(question.get("odp_0"));
                btn_b.setText(question.get("odp_1"));
                btn_c.setText(question.get("odp_2"));
                btn_d.setText(question.get("odp_3"));
            }

            /** nasłuch na odpowiedzi */
            btn_a.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    onOrOffButton(false);
                    btn_a.setBackgroundColor(getResources().getColor(R.color.btn_active));
                    if (!questionsData.checkAnswer(question, 0)) {
                        endGame("loss");
                    } else {
                        questionCount++;
                        setQuestion();
                    }
                }
            });
            btn_b.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    onOrOffButton(false);
                    btn_b.setBackgroundColor(getResources().getColor(R.color.btn_active));
                    if (!questionsData.checkAnswer(question, 1)) {
                        endGame("loss");
                    } else {
                        questionCount++;
                        setQuestion();
                    }
                }
            });
            btn_c.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    onOrOffButton(false);
                    btn_c.setBackgroundColor(getResources().getColor(R.color.btn_active));
                    if (!questionsData.checkAnswer(question, 2)) {
                        endGame("loss");
                    } else {
                        questionCount++;
                        setQuestion();
                    }
                }
            });
            btn_d.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    onOrOffButton(false);
                    btn_d.setBackgroundColor(getResources().getColor(R.color.btn_active));
                    if (!questionsData.checkAnswer(question, 3)) {
                        endGame("loss");
                    } else {
                        questionCount++;
                        setQuestion();
                    }
                }
            });
        }
    }

    /**
     * Funkcja do wyłączania buttonów
     * @param on włączone/wyłączone
     */
    private void onOrOffButton(boolean on) {
        btn_a.setClickable(on);
        btn_a.setBackgroundColor(getResources().getColor(R.color.btn));
        btn_b.setClickable(on);
        btn_b.setBackgroundColor(getResources().getColor(R.color.btn));
        btn_c.setClickable(on);
        btn_c.setBackgroundColor(getResources().getColor(R.color.btn));
        btn_d.setClickable(on);
        btn_d.setBackgroundColor(getResources().getColor(R.color.btn));
    }

    /**
     * Funkcja odpowiedzialna za kończenie gry
     * @param typ rodzaj kończenia gry
     */
    private void endGame(String typ) {
        if(!typ.equalsIgnoreCase("loss")) {
            TypedArray cash_array = getResources().obtainTypedArray(R.array.cash_array);
            String val = cash_array.getString(questionCount - 1);
            int cash = Integer.parseInt(val);

            Calendar end = Calendar.getInstance();
            float timeGame = (float)(end.getTimeInMillis() - now.getTimeInMillis());

            SaveResult save = new SaveResult();
            save.saveReuslt(getApplicationContext(), nick, cash, timeGame, profil);
        }

        if (typ.equalsIgnoreCase("win")) {
            Toast.makeText(getApplicationContext(), "Gratulacje! Wygrałeś główną nagrodę.", Toast.LENGTH_LONG).show();;
        } else if(typ.equalsIgnoreCase("loss")) {
            Toast.makeText(getApplicationContext(), "Przegrałeś! Źle odpowiedziałeś na to pytanie.", Toast.LENGTH_LONG).show();;
        } else if(typ.equalsIgnoreCase("end")) {
            Toast.makeText(getApplicationContext(), "Wynik zapisany.", Toast.LENGTH_LONG).show();;
        }

        Intent intent = new Intent(getApplicationContext(), RankingView.class);
        startActivity(intent);
        finish();
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
        } else {
            endGame("end");
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