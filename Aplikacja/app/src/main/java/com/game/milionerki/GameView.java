package com.game.milionerki;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.game.gameAction.QuestionsData;
import com.game.gameAction.SaveResult;
import com.game.menu.DrawerItemCustomAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
    private long now;

    private String nick = "";


    //ustawienia pytań
    private Button btn_a;
    private Button btn_b;
    private Button btn_c;
    private Button btn_d;

    private int tab_quest[] = new int[10];
    private QuestionsData questionsData;

    //koła
    private ImageView kolo_50;
    private ImageView kolo_przyjaciel;
    private ImageView kolo_publicznosc;

    private int[] usedHelp = new int[] { 0, 0, 0 };
    private String[] odp_50 = new String[2];

    private boolean textCurr = false;
    private int randText = 0;

    private Thread odpThread;
    private int count = 0;

    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        outState.putIntArray("tab_quest", tab_quest);
        outState.putIntArray("usedHelp", usedHelp);
        outState.putStringArray("odp_50", odp_50);

        outState.putString("pytanie", question.get("pytanie"));
        outState.putString("poprawna", question.get("poprawna"));
        outState.putString("odp_0", question.get("odp_0"));
        outState.putString("odp_1", question.get("odp_1"));
        outState.putString("odp_2", question.get("odp_2"));
        outState.putString("odp_3", question.get("odp_3"));

        outState.putInt("questionCount", questionCount);
        outState.putInt("profil", profil);
        outState.putLong("now", now);
        outState.putString("nick", nick);
    }

    /**
     * Stworzenie widoku gry
     *
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_view);

        if(savedInstanceState != null)
        {
            tab_quest = savedInstanceState.getIntArray("tab_quest");
            usedHelp = savedInstanceState.getIntArray("usedHelp");
            odp_50 = savedInstanceState.getStringArray("odp_50");

            String _tmp;
            _tmp = savedInstanceState.getString("pytanie");
            question.put("pytanie", _tmp);
            _tmp = savedInstanceState.getString("poprawna");
            question.put("poprawna", _tmp);
            _tmp = savedInstanceState.getString("odp_0");
            question.put("odp_0", _tmp);
            _tmp = savedInstanceState.getString("odp_1");
            question.put("odp_1", _tmp);
            _tmp = savedInstanceState.getString("odp_2");
            question.put("odp_2", _tmp);
            _tmp = savedInstanceState.getString("odp_3");
            question.put("odp_3", _tmp);

            questionCount = savedInstanceState.getInt("questionCount");
            profil = savedInstanceState.getInt("profil");
            now = savedInstanceState.getLong("now");
            nick = savedInstanceState.getString("nick");
        }

        for (int i = 0; i < 10; i++)
            tab_quest[i] = -1;

        kolo_50 = (ImageView) findViewById(R.id.kolo_50);
        kolo_przyjaciel = (ImageView) findViewById(R.id.kolo_telefon);
        kolo_publicznosc = (ImageView) findViewById(R.id.kolo_publicznosc);

        btn_a = (Button) findViewById(R.id.btn_a);
        btn_b = (Button) findViewById(R.id.btn_b);
        btn_c = (Button) findViewById(R.id.btn_c);
        btn_d = (Button) findViewById(R.id.btn_d);
        onOrOffButton(true);

        sqlAdapter = new com.game.sql.sqlAdapter(getApplicationContext());

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

        /**
         * nasłuchy na koła
         */
        kolo_50.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (usedHelp[0] == 0) {
                    help50();
                    usedHelp[0] = 1;
                    kolo_50.setImageResource(R.drawable.kolo_5050_skr);
                } else Toast.makeText(getApplicationContext(), getString(R.string.error_used_yet), Toast.LENGTH_LONG).show();
            }
        });
        kolo_przyjaciel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (usedHelp[1] == 0) {
                    helpFriend();
                    usedHelp[1] = 1;
                    kolo_przyjaciel.setImageResource(R.drawable.kolo_przyjaciel_skr);
                } else Toast.makeText(getApplicationContext(), getString(R.string.error_used_yet), Toast.LENGTH_LONG).show();
            }
        });
        kolo_publicznosc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (usedHelp[2] == 0) {
                    helpAudience();
                    usedHelp[2] = 1;
                    kolo_publicznosc.setImageResource(R.drawable.kolo_publicznosc_skr);
                } else
                    Toast.makeText(getApplicationContext(), getString(R.string.error_used_yet), Toast.LENGTH_LONG).show();
            }
        });

        /** Ustawienie aktualnego czasu */
        Calendar nowCalendar = Calendar.getInstance();
        now = nowCalendar.getTimeInMillis();

        /** Losowanie pytań */
        questionsData = new QuestionsData(getApplicationContext());
        setQuestion();
    }

    /**
     * Funkcja odpowiedzialna za wyświetlanie tekstu prowadzącego
     */
    private void textLeading(int val) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(randTextLeading());
        alert = dialogBuilder.create();
        alert.show();

        count = 0;
        Runnable runnable = new Runnable() {
            public void run() {
                while (count <= 1) {
                    if (count == 1) {
                        try {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    alert.dismiss();
                                }
                            });
                        } catch (NullPointerException error) {
                            count = 4;
                            Thread.currentThread().interrupt();
                        }
                    }

                    try {
                        Thread.sleep(2000);
                        count++;
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        };
        odpThread = new Thread(runnable);
        odpThread.start();

    }

    /**
     *
     * Funkcja odpowiedzialna za losowanie tekstu prowadzącego
     */
    private View randTextLeading() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_alert_text_leading, (ViewGroup) this.findViewById(R.id.layout_root));

        textCurr = true;

        int max = 100;
        int min = 0;

        Random r = new Random();
        int rand = r.nextInt(max - min + 1) + min;

        int level = 3;
        if (rand < 20) {
            level = 1;
        } else if (rand < 50) {
            level = 2;
        }

        sqlAdapter.open();

        String[] kolumny = { "Tekst_prowadzacego" };
        Cursor data = sqlAdapter.getColumn(kolumny, sqlAdapter.DB_TEXTS_LED_TABLE, "Prawdopodobienstwo_wypadniecia = " + level);
        int total = data.getCount() - 1;

        r = new Random();
        int chooseText = r.nextInt(total + 1);

        data.moveToPosition(chooseText);
        String text_friend = data.getString(0);

        sqlAdapter.close();

        TextView _text_lead = (TextView) layout.findViewById(R.id.text_lead);
        _text_lead.setText(text_friend);

        return layout;
    }

    /**
     * Funkcja odpowiedzialna za koło 50/50
     */
    private void help50() {
        String poprawna_odp = question.get("poprawna");
        int count = 0;
        int oldCount = -1;

        do {
            Random r = new Random();
            int _result = r.nextInt(4);
            String odp_rand = "odp_" + _result;

            if (!odp_rand.equalsIgnoreCase(poprawna_odp) && _result != oldCount) {
                Button disable = null;
                switch (_result) {
                    case 0:
                        disable = btn_a;
                        break;
                    case 1:
                        disable = btn_b;
                        break;
                    case 2:
                        disable = btn_c;
                        break;
                    default:
                        disable = btn_d;
                        break;
                }
                disable.setClickable(false);
                disable.setText("");

                odp_50[count] = odp_rand;

                count++;
                oldCount = _result;
            }
        } while(count < 2);

    }

    /**
     * Funkcja odpowiedzialna za koło do przyjaciela
     */
    private void helpFriend() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(R.string.dialog_alert_friend).setView(getCustomDialogLayoutFreind()).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        alert = dialogBuilder.create();
        alert.show();
    }

    /**
     * Funkcja odpowiedzialna za działanie koła do przyjaciela
     * @return wygląd
     */
    private View getCustomDialogLayoutFreind() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.help_dialog_friend, (ViewGroup) this.findViewById(R.id.layout_root));

        TypedArray name_array = getResources().obtainTypedArray(R.array.name_friend);
        Random r = new Random();
        int name_rand = r.nextInt(name_array.length());
        String name = name_array.getString(name_rand);

        /**
         * Losowanie tekstu
         */
        int max = 100;
        int min = 0;

        r = new Random();
        int rand = r.nextInt(max - min + 1) + min;

        int level = 3;
        if (rand < 20) {
            level = 1;
        } else if (rand < 50) {
            level = 2;
        }

        sqlAdapter.open();

        String[] kolumny = { "Tekst_przyjaciela" };
        Cursor data = sqlAdapter.getColumn(kolumny, sqlAdapter.DB_TEXTS_FRIEND_TABLE, "Prawdopodobienstwo_wypadniecia = " + level);
        int total = data.getCount() - 1;

        r = new Random();
        int chooseText = r.nextInt(total + 1);

        data.moveToPosition(chooseText);
        String text_friend = data.getString(0);

        sqlAdapter.close();

        String text_1 = getApplicationContext().getString(R.string.hello) + " " + name + ", " + getApplicationContext().getString(R.string.text_hello);
        TextView _text_1 = (TextView) layout.findViewById(R.id.text_1);
        _text_1.setText(text_1);

        TextView _text_3 = (TextView) layout.findViewById(R.id.text_3);
        _text_3.setText(text_friend);

        Button a = (Button) layout.findViewById(R.id.button);
        Button b = (Button) layout.findViewById(R.id.button2);
        Button c = (Button) layout.findViewById(R.id.button3);
        Button d = (Button) layout.findViewById(R.id.button4);

        a.setClickable(false);
        a.setBackgroundColor(getResources().getColor(R.color.btn));
        b.setClickable(false);
        b.setBackgroundColor(getResources().getColor(R.color.btn));
        c.setClickable(false);
        c.setBackgroundColor(getResources().getColor(R.color.btn));
        d.setClickable(false);
        d.setBackgroundColor(getResources().getColor(R.color.btn));

        String text_2 = question.get("pytanie") + "\n";
        int counts = 0;

        if (!odp_50[0].equalsIgnoreCase("odp_0") && !odp_50[1].equalsIgnoreCase("odp_0")) {
            text_2 += "A: " + question.get("odp_0") + " ";
            counts++;
        } else a.setVisibility(View.INVISIBLE);
        if (!odp_50[0].equalsIgnoreCase("odp_1") && !odp_50[1].equalsIgnoreCase("odp_1")) {
            text_2 += "B: " + question.get("odp_1") + " ";
            counts++;
            if (counts == 2) text_2 += "\n";
        } else b.setVisibility(View.INVISIBLE);
        if (!odp_50[0].equalsIgnoreCase("odp_2") && !odp_50[1].equalsIgnoreCase("odp_2")) {
            text_2 += "C: " + question.get("odp_2") + " ";
            counts++;
            if (counts == 2) text_2 += "\n";
        } else c.setVisibility(View.INVISIBLE);
        if (!odp_50[0].equalsIgnoreCase("odp_3") && !odp_50[1].equalsIgnoreCase("odp_3")) {
            text_2 += "D: " + question.get("odp_3") + " ";
            counts++;
            if (counts == 2) text_2 += "\n";
        } else d.setVisibility(View.INVISIBLE);

        TextView _text_2 = (TextView) layout.findViewById(R.id.text_2);
        _text_2.setText(text_2);

        max = 100;
        min = 0;

        r = new Random();
        rand = r.nextInt(max - min + 1) + min;

        if (rand <= 75) {
            if (question.get("poprawna").equalsIgnoreCase("odp_0"))
                a.setBackgroundColor(getResources().getColor(R.color.btn_active));
            else if (question.get("poprawna").equalsIgnoreCase("odp_1"))
                b.setBackgroundColor(getResources().getColor(R.color.btn_active));
            else if (question.get("poprawna").equalsIgnoreCase("odp_2"))
                c.setBackgroundColor(getResources().getColor(R.color.btn_active));
            else if (question.get("poprawna").equalsIgnoreCase("odp_3"))
                d.setBackgroundColor(getResources().getColor(R.color.btn_active));
        } else {
            boolean exit = false;
            do {
                r = new Random();
                int _rand = r.nextInt(4);
                String _rands = "odp_" + _rand;

                if (!_rands.equalsIgnoreCase(question.get("poprawna")) && !_rands.equalsIgnoreCase(odp_50[0]) && !_rands.equalsIgnoreCase(odp_50[1])) {
                    exit = true;

                    if (_rands.equalsIgnoreCase("odp_0"))
                        a.setBackgroundColor(getResources().getColor(R.color.btn_active));
                    else if (_rands.equalsIgnoreCase("odp_1"))
                        b.setBackgroundColor(getResources().getColor(R.color.btn_active));
                    else if (_rands.equalsIgnoreCase("odp_2"))
                        c.setBackgroundColor(getResources().getColor(R.color.btn_active));
                    else if (_rands.equalsIgnoreCase("odp_3"))
                        d.setBackgroundColor(getResources().getColor(R.color.btn_active));
                }

            } while(!exit);
        }

        return layout;
    }

    /**
     * Funkcja odpowiedzialna za koło do publiczności
     */
    private void helpAudience() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(R.string.dialog_alert_audience).setView(getCustomDialogLayoutAudience()).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        alert = dialogBuilder.create();
        alert.show();
    }

    /**
     * Funkcja odpowiedzialna za działanie koła do publiczności
     * @return widok
     */
    private View getCustomDialogLayoutAudience() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.help_dialog_audience, (ViewGroup) this.findViewById(R.id.layout_root));

        TextView question_txt = (TextView) layout.findViewById(R.id.textView14);
        question_txt.setText(question.get("pytanie"));

        String[] nameOdp = new String[] { "odp_0", "odp_1", "odp_2", "odp_3" };
        String poprawna_odp = question.get("poprawna");

        int max = 100;
        int min = 0;

        Random r = new Random();
        int rand = r.nextInt(max - min + 1) + min;

        ProgressBar odp_a = (ProgressBar) layout.findViewById(R.id.progressBar_1);
        ProgressBar odp_b = (ProgressBar) layout.findViewById(R.id.progressBar);
        ProgressBar odp_c = (ProgressBar) layout.findViewById(R.id.progressBar2);
        ProgressBar odp_d = (ProgressBar) layout.findViewById(R.id.progressBar3);

        TextView odp_a_prc = (TextView) layout.findViewById(R.id.prc_1);
        TextView odp_b_prc = (TextView) layout.findViewById(R.id.textView8);
        TextView odp_c_prc = (TextView) layout.findViewById(R.id.textView9);
        TextView odp_d_prc = (TextView) layout.findViewById(R.id.textView13);

        if (rand <= 100) {
            int count = 0;
            int total = 0;
            int total_odp = 4;
            int[] tab_temp = new int[4];
            if (!odp_50[0].equalsIgnoreCase("")) {
                total_odp = 2;
            }
            max = (int)((100 - 1) / total_odp);
            do {
                int _rand_val = r.nextInt(max - min + 1) + min;

                if (nameOdp[count].equalsIgnoreCase(poprawna_odp))
                    count++;

                boolean error = true;
                do {
                    if (count < 4) {
                        error = false;
                        if (nameOdp[count].equalsIgnoreCase("odp_0") && (odp_50[0].equalsIgnoreCase("odp_0") || odp_50[1].equalsIgnoreCase("odp_0"))) {
                            count++;
                            error = true;
                        } else if (nameOdp[count].equalsIgnoreCase("odp_1") && (odp_50[0].equalsIgnoreCase("odp_1") || odp_50[1].equalsIgnoreCase("odp_1"))) {
                            count++;
                            error = true;
                        } else if (nameOdp[count].equalsIgnoreCase("odp_2") && (odp_50[0].equalsIgnoreCase("odp_2") || odp_50[1].equalsIgnoreCase("odp_2"))) {
                            count++;
                            error = true;
                        } else if (nameOdp[count].equalsIgnoreCase("odp_3") && (odp_50[0].equalsIgnoreCase("odp_3") || odp_50[1].equalsIgnoreCase("odp_3"))) {
                            count++;
                            error = true;
                        }
                    }
                } while (error && count < 4);

                if (count < 4) {
                    if (nameOdp[count].equalsIgnoreCase("odp_0")) {
                        odp_a.setProgress(_rand_val);
                        odp_a_prc.setText(_rand_val + "%");
                        total += _rand_val;
                        tab_temp[0] = _rand_val;
                    } else if (nameOdp[count].equalsIgnoreCase("odp_1")) {
                        odp_b.setProgress(_rand_val);
                        odp_b_prc.setText(_rand_val + "%");
                        total += _rand_val;
                        tab_temp[1] = _rand_val;
                    } else if (nameOdp[count].equalsIgnoreCase("odp_2")) {
                        odp_c.setProgress(_rand_val);
                        odp_c_prc.setText(_rand_val + "%");
                        total += _rand_val;
                        tab_temp[2] = _rand_val;
                    } else if (nameOdp[count].equalsIgnoreCase("odp_3")) {
                        odp_d.setProgress(_rand_val);
                        odp_d_prc.setText(_rand_val + "%");
                        total += _rand_val;
                        tab_temp[3] = _rand_val;
                    }
                }
                count++;
            } while(count < 4);

            if (poprawna_odp.equalsIgnoreCase("odp_0")) {
                odp_a.setProgress(100 - total + tab_temp[0]);
                odp_a_prc.setText((100 - total + tab_temp[0])+"%");
            } else if (poprawna_odp.equalsIgnoreCase("odp_1")) {
                odp_b.setProgress(100 - total + tab_temp[1]);
                odp_b_prc.setText((100 - total + tab_temp[1])+"%");
            } else if (poprawna_odp.equalsIgnoreCase("odp_2")) {
                odp_c.setProgress(100 - total + tab_temp[2]);
                odp_c_prc.setText((100 - total + tab_temp[2])+"%");
            } else if (poprawna_odp.equalsIgnoreCase("odp_3")) {
                odp_d.setProgress(100 - total + tab_temp[3]);
                odp_d_prc.setText((100 - total + tab_temp[3])+"%");
            }
        } else {
            int count = 0;
            int total = 0;
            int total_odp = 4;
            if (!odp_50[0].equalsIgnoreCase("")) {
                total_odp = 2;
            }
            max = 100;
            int total_yet = 0;
            do {
                int _rand_val = 0;
                boolean error = true;
                do {
                    error = false;
                    if (nameOdp[count].equalsIgnoreCase("odp_0") && (odp_50[0].equalsIgnoreCase("odp_0") || odp_50[1].equalsIgnoreCase("odp_0"))) {
                        count++;
                        error = true;
                    } else if (nameOdp[count].equalsIgnoreCase("odp_1") && (odp_50[0].equalsIgnoreCase("odp_1") || odp_50[1].equalsIgnoreCase("odp_1"))) {
                        count++;
                        error = true;
                    } else if (nameOdp[count].equalsIgnoreCase("odp_2") && (odp_50[0].equalsIgnoreCase("odp_2") || odp_50[1].equalsIgnoreCase("odp_2"))) {
                        count++;
                        error = true;
                    } else if (nameOdp[count].equalsIgnoreCase("odp_3") && (odp_50[0].equalsIgnoreCase("odp_3") || odp_50[1].equalsIgnoreCase("odp_3"))) {
                        count++;
                        error = true;
                    }
                } while(error && count < 4);

                total_yet++;

                if (total_yet < total_odp - 1) {
                    _rand_val = r.nextInt(max - min + 1) + min;
                    max -= _rand_val;
                } else {
                    _rand_val = max;
                }

                if (count < 4) {
                    if (nameOdp[count].equalsIgnoreCase("odp_0")) {
                        odp_a.setProgress(_rand_val);
                        odp_a_prc.setText(_rand_val + "%");
                    } else if (nameOdp[count].equalsIgnoreCase("odp_1")) {
                        odp_b.setProgress(_rand_val);
                        odp_b_prc.setText(_rand_val + "%");
                    } else if (nameOdp[count].equalsIgnoreCase("odp_2")) {
                        odp_c.setProgress(_rand_val);
                        odp_c_prc.setText(_rand_val + "%");
                    } else if (nameOdp[count].equalsIgnoreCase("odp_3")) {
                        odp_d.setProgress(_rand_val);
                        odp_d_prc.setText(_rand_val + "%");
                    }
                    count++;
                }
            } while(count < 4);

        }

        return layout;
    }

    /**
     * Funkcja odpowiedzialna za ustawianie pytań
     */
    private void setQuestion() {
        if (questionCount > 10) {
            endGame("win");
        } else {
            count = 0;
            if (questionCount == 1)
                count = 3;

            Runnable runnable = new Runnable() {
                public void run() {
                    while (count <= 3) {
                        if (count == 3) {
                            try {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        textCurr = false;
                                        for (int i = 0; i < 2; i++) {
                                            odp_50[i] = "";
                                        }

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
                                    }
                                });
                            } catch (NullPointerException error) {
                                count = 4;
                                Thread.currentThread().interrupt();
                            }
                        }

                        try {
                            Thread.sleep(300);
                            count++;
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            };
            odpThread = new Thread(runnable);
            odpThread.start();

            /** nasłuch na odpowiedzi */
            btn_a.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    randomText();
                    if (randText <= 80) {
                        btn_a.setBackgroundColor(getResources().getColor(R.color.btn_choose));
                        textLeading(1);
                    } else {
                        onOrOffButton(false);
                        if (!questionsData.checkAnswer(question, 0)) {
                            btn_a.setBackgroundColor(getResources().getColor(R.color.btn_bad));
                            colorGoodAnswer();
                            badAnswer();
                        } else {
                            btn_a.setBackgroundColor(getResources().getColor(R.color.btn_active));
                            questionCount++;
                            setQuestion();
                        }
                    }
                }
            });
            btn_b.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    randomText();
                    if (randText <= 80) {
                        btn_b.setBackgroundColor(getResources().getColor(R.color.btn_choose));
                        textLeading(2);
                    } else {
                        onOrOffButton(false);
                        if (!questionsData.checkAnswer(question, 1)) {
                            btn_b.setBackgroundColor(getResources().getColor(R.color.btn_bad));
                            colorGoodAnswer();
                            badAnswer();
                        } else {
                            btn_b.setBackgroundColor(getResources().getColor(R.color.btn_active));
                            questionCount++;
                            setQuestion();
                        }
                    }
                }
            });
            btn_c.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    randomText();
                    if (randText <= 80) {
                        btn_c.setBackgroundColor(getResources().getColor(R.color.btn_choose));
                        textLeading(3);
                    } else {
                        onOrOffButton(false);
                        if (!questionsData.checkAnswer(question, 2)) {
                            btn_c.setBackgroundColor(getResources().getColor(R.color.btn_bad));
                            colorGoodAnswer();
                            badAnswer();
                        } else {
                            btn_c.setBackgroundColor(getResources().getColor(R.color.btn_active));
                            questionCount++;
                            setQuestion();
                        }
                    }
                }
            });
            btn_d.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    randomText();
                    if (randText <= 80) {
                        btn_d.setBackgroundColor(getResources().getColor(R.color.btn_choose));
                        textLeading(4);
                    } else {
                        onOrOffButton(false);
                        if (!questionsData.checkAnswer(question, 3)) {
                            btn_d.setBackgroundColor(getResources().getColor(R.color.btn_bad));
                            colorGoodAnswer();
                            badAnswer();
                        } else {
                            btn_d.setBackgroundColor(getResources().getColor(R.color.btn_active));
                            questionCount++;
                            setQuestion();
                        }
                    }
                }
            });
        }
    }

    /**
     * Funkcja odpowiedzialna za kolorowanie poprawnej odpowiedzi
     */
    private void colorGoodAnswer() {
        String poprawna = question.get("poprawna");

        if (poprawna.equalsIgnoreCase("odp_0"))
            btn_a.setBackgroundColor(getResources().getColor(R.color.btn_active));
        else if (poprawna.equalsIgnoreCase("odp_1"))
            btn_b.setBackgroundColor(getResources().getColor(R.color.btn_active));
        else if (poprawna.equalsIgnoreCase("odp_2"))
            btn_c.setBackgroundColor(getResources().getColor(R.color.btn_active));
        else if (poprawna.equalsIgnoreCase("odp_3"))
            btn_d.setBackgroundColor(getResources().getColor(R.color.btn_active));
    }

    /**
     * Funkcja odpowiedzialna za wątek wykonania zakończenia gry
     */
    private void badAnswer() {
        count = 0;
        Runnable runnable = new Runnable() {
            public void run() {
                while (count <= 3) {
                    if (count == 3) {
                        try {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    endGame("loss");
                                }
                            });
                        } catch (NullPointerException error) {
                            count = 4;
                            Thread.currentThread().interrupt();
                        }
                    }

                    try {
                        Thread.sleep(300);
                        count++;
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        };
        odpThread = new Thread(runnable);
        odpThread.start();

    }


    /**
     * Funkcja odpowiedzialna za losowanie
     */
    private void randomText() {
        Random r = new Random();
        randText = r.nextInt(100);

        if (textCurr) randText = 100;
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
            String val = "0";
            if (questionCount > 1) {
                val = cash_array.getString(questionCount - 2);
            }
            int cash = Integer.parseInt(val);

            Calendar end = Calendar.getInstance();
            float timeGame = (float)(end.getTimeInMillis() - now);

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