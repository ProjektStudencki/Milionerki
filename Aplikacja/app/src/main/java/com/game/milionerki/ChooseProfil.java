package com.game.milionerki;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.game.menu.DrawerItemCustomAdapter;
import com.game.sql.sqlAdapter;

import menu.ObjectDrawerItem;

/**
 * Class <code>ChooseProfil</code>
 * Klasa odpowiedzialna za wybór profilu gracza
 * @author Kamil Gammert
 * @version 1.0, Kwiecień 2015
 */
public class ChooseProfil extends Activity {

    private com.game.sql.sqlAdapter sqlAdapter;
    private ListView _body;

    /**
     * Funkcja tworząca widok, generowana domyślnie
     *
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_profil);

        TextView _newProfil = (TextView) findViewById(R.id.newProfil);
        TextView _backBtn = (TextView) findViewById(R.id.backButton);

        /**
         * Nasłuch, który umożliwia przejście do widoku tworzenia nowego profilu
         */
        _newProfil.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreatePlayersAvatar.class);
                startActivity(intent);
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

        _body = (ListView) findViewById(R.id.listView);
        _body.setDivider(new ColorDrawable(android.R.color.transparent));
        _body.setDividerHeight(0);

        sqlAdapter = new sqlAdapter(getApplicationContext());
        sqlAdapter.open();

        String[] kolumny = { "Pseudonim", "Avatar" };
        Cursor data = sqlAdapter.getColumn(kolumny, sqlAdapter.DB_USERS_TABLE);

        int count = data.getCount();
        ObjectDrawerItem[] profilObject = new ObjectDrawerItem[count];

        int i = 0;
        if (count > 0) {
            while (data.moveToNext()) {
                String name = data.getString(0);
                String avatar = data.getString(1);

                profilObject[i] = new ObjectDrawerItem("profilView", R.layout.profil_row, name, avatar);
                i++;
            }

            DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.profil_row, profilObject);
            _body.setAdapter(adapter);
            _body.setOnItemClickListener(new ProfilChoose());
            sqlAdapter.close();
        } else {
            sqlAdapter.close();
            Intent intent = new Intent(getApplicationContext(), CreatePlayersAvatar.class);
            startActivity(intent);
            finish();
        }

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

    /**
     * Class <code>ProfilChoose</code>
     * Klasa nasłuchująca wybór profilu
     * @author Kamil Gammert
     * @version 1.0, Kwiecień 2015
     */
    private class ProfilChoose implements ListView.OnItemClickListener {
        /**
         * Funkcja wywoływana po wybraniu danego profilu
         *
         * @param parent lista wszystkich widoków w listView
         * @param view widok klikniętego itemu
         * @param position pozycja klikniętego profilu
         * @param id
         */
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TextView _txt = (TextView) view.findViewById(R.id.textView1);

            Intent intent = new Intent(getApplicationContext(), GameView.class);
            intent.putExtra("profil", position);
            intent.putExtra("nick", _txt.getText().toString());
            startActivity(intent);
            finish();
        }
    }

}
