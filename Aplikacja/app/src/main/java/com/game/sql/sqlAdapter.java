package com.game.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.Integer;

/**
 * Class <code>sqlAdapter</code>
 * Klasa odpowiedzialna za połączenie z bazą danych
 * @author Kamil Gammert
 * @version 1.0, Marzec,Kwiecien 2015
 */
public class sqlAdapter {
    private static final String DEBUG_TAG = "Baza danych";

    private static final int DB_VERSION = 8;

    private static final String DB_NAME = "milionerkiGame.db";
    public static final String DB_USERS_TABLE = "Gracze";
    public static final String DB_QUESTION_TABLE = "PytaniaiOdpowiedzi";
    public static final String DB_TEXTS_LED_TABLE = "TekstyProwadzacego";
    public static final String DB_TEXTS_FRIEND_TABLE = "TekstyPrzyjacielaDoKtoregoDzwonimy";
    public static final String DB_RESULTS_TABLE = "WynikiGraczy";

    private static final String DB_CREATE_USERS_TABLE =
            "CREATE TABLE " + DB_USERS_TABLE + "( " +
                    "Pseudonim            text not null, " +
                    "Imie_gracza          text, " +
                    "Nazwisko_gracza      text, " +
                    "Avatar               int not null," +
                    "primary key (Pseudonim)" +
                    ");";
    private static final String DROP_USERS_TABLE =
            "DROP TABLE IF EXISTS " + DB_USERS_TABLE;

    private static final String DB_CREATE_QUESTION_TABLE =
            "CREATE TABLE " + DB_QUESTION_TABLE + "( " +
                    "Nr_Pytania           INTEGER PRIMARY KEY, "+
                    "Pytanie              text not null, " +
                    "Poprawna_odp         text not null, " +
                    "Zla_odp1             text not null, " +
                    "Zla_odp2             text not null, " +
                    "Zla_odp3             text not null, " +
                    "Poziom_trudnosci     INTEGER not null " +
                    ");";
    private static final String DROP_QUESTION_TABLE =
            "DROP TABLE IF EXISTS " + DB_QUESTION_TABLE;

    private static final String DB_CREATE_TEXTS_LED_TABLE =
            "CREATE TABLE " + DB_TEXTS_LED_TABLE + "( " +
                    "ID_tekstu            INTEGER PRIMARY KEY, " +
                    "Tekst_prowadzacego   text not null, " +
                    "Prawdopodobienstwo_wypadniecia INTEGER not null " +
                    ");";
    private static final String DROP_TEXTS_LED_TABLE =
            "DROP TABLE IF EXISTS " + DB_TEXTS_LED_TABLE;

    private static final String DB_CREATE_TEXTS_FRIEND_TABLE =
            "CREATE TABLE " + DB_TEXTS_FRIEND_TABLE + "( " +
                    "ID_tekstu            INTEGER PRIMARY KEY, " +
                    "Tekst_przyjaciela    text not null, " +
                    "Prawdopodobienstwo_wypadniecia INTEGER not null "+
                    ");";
    private static final String DROP_TEXTS_FRIEND_TABLE =
            "DROP TABLE IF EXISTS " + DB_TEXTS_FRIEND_TABLE;

    private static final String DB_CREATE_RESULTS_TABLE =
            "CREATE TABLE " + DB_RESULTS_TABLE + "( " +
                    "Nr_wyniku            int not null, " +
                    "Pseudonim            text not null, " +
                    "Czas_gry             float not null, " +
                    "Data_wpisu           date not null, " +
                    "Uzyskany_wynik       int not null, " +
                    "Avatar               int not null," +
                    "primary key (Nr_wyniku), " +
                    "FOREIGN KEY(Pseudonim) REFERENCES " + DB_USERS_TABLE + "(Pseudonim)" +
                    ");";
    private static final String DROP_RESULTS_TABLE =
            "DROP TABLE IF EXISTS " + DB_RESULTS_TABLE;

    private SQLiteDatabase db;
    private Context context;
    private DatabaseHelper dbHelper;

    /**
     * Klasa odpowiedzialna za tworzenie i aktualizację bazy danych
     * @author Kamil Gammert
     * @version v0.8 2015r.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, factory, version);
        }


        private void questionAdd(SQLiteDatabase db) {
            String[][] question_values = new String[61][];
            question_values[0] = new String[]{"Jak nazywa się klub piłkarski, który zdobył Puchar Ligi Mistrzów w sezonie 2013/2014?", "Real Madryt", "FC Barcelona", "Atletico Madryt", "Bayern Monachium", "3"};
            question_values[1] = new String[]{"Jaki jest wynik sumowania liczb 2 i 8?", "10", "6", "82", "28", "1"};
            question_values[2] = new String[]{"Jaki jest wynik sumowania liczb 5 i 5?", "10", "55", "0", "5", "1"};
            question_values[3] = new String[]{"Co powstanie z wody, gdy ją zamrozimy?", "Lód", "Ogień", "Chmura gazu", "Mleko", "1"};
            question_values[4] = new String[]{"Jaka legenda jest związana z Warszawą?", "O Syrence", "O smoku wawelskim", "O Szymonie, co zbierał grzyby", "O wsi za miastem", "2"};
            question_values[5] = new String[]{"Jaki jest kod pocztowy Wałcza?", "78-600", "78-650", "68-500", "75-800", "7"};
            question_values[6] = new String[]{"Co nie jest nazwą stylu pływackiego?", "Rekin", "Żabka", "Kraul", "Delfin", "2"};
            question_values[7] = new String[]{"Do ilu punktów liczy się set w tenisie stołowym?", "21", "22", "20", "11", "3"};
            question_values[8] = new String[]{"Jaki jest numer straży pożarnej?", "998", "999", "666", "997", "2"};
            question_values[9] = new String[]{"Ile jest znaków zodiaku?", "12", "10", "11", "14", "3"};
            question_values[10] = new String[]{"Jakie są najwyższe góry na świecie?", "Himalaje", "Pireneje", "Alpy", "Sudety Wielkie", "2"};
            question_values[11] = new String[]{"Jaka część mowy odpowiada na pytania: kto, co?", "rzeczownik", "czasownik", "dopełniacz", "przymiotnik", "2"};
            question_values[12] = new String[]{"Kim jest Tomasz Kuszczak?", "Bramkarzem", "Prezenterem MTV", "Aktorem", "Koszykarzem", "4"};
            question_values[13] = new String[]{"Lekarz leczący zęby nazywa się...", "Stomatologiem", "Laryngologiem", "Kowalem", "Weterynarzem", "1"};
            question_values[14] = new String[]{"W którym roku rozpoczęła się II Wojna Światowa?", "1939", "2014", "1911", "1945", "1"};
            question_values[15] = new String[]{"Ile trwała wojna stuletnia?", "116", "100", "144", "99", "5"};
            question_values[16] = new String[]{"Jak się nazywa polski skoczek narciarski, który zdobył dwa złote medale olimpijskie w czasie jednej Olimpiady?", "Kamil Stoch", "Robert Mateja", "Stefan Hula", "Adam Małysz", "3"};
            question_values[17] = new String[]{"Skąd pochodzi Conan Barbarzyńca?", "z Cimmerii", "z Rivii", "z Oz", "z Mordoru", "10"};
            question_values[18] = new String[]{"Komiksowym \"dzieckiem\" rysownika Boba Kane'a jest:", "Batman", "Superman", "Spider-Man", "Captain America", "10"};
            question_values[19] = new String[]{"Likier maraskino produkuje się z maraski, czyli odmiany:", "wiśni", "jabłoni", "figi", "gruszy", "10"};
            question_values[20] = new String[]{"Na których igrzyskach wybudowano po raz pierwszy wioskę olimpijską?", "Los Angeles 1932", "Sztokholm 1912", "Londyn 1948", "Helsinki 1952", "9"};
            question_values[21] = new String[]{"Kogo, oprócz Judasza, umieścił Dante na samym dnie piekła?", "Brutusa i Kasjusza", "Kaligulę i Nerona", "Kaina", "Piłata", "9"};
            question_values[22] = new String[]{"Kto w czasie sprawowania władzy był konduktatorem, czyli wodzem narodu?", "Nicolae Ceausescu", "Fidel Castro", "Michaił Gorbaczow", "Todor Żiwkow", "9"};
            question_values[23] = new String[]{"Ile gwiazd znajduje się na fladze państwowej Australii?", "sześć", "cztery", "osiem", "pięć", "8"};
            question_values[24] = new String[]{"Jak nazywał się władca krainy Mordor z \"Władcy pierścieni\" Tolkiena?", "Sauron", "Gorgoth", "Saruman", "Gandalf", "8"};
            question_values[25] = new String[]{"Ile centymetrów wynosi całkowita długość kortu do badmintona w grze pojedynczej?", "1340", "1304", "1430", "1520", "8"};
            question_values[26] = new String[]{"Sejm może obalić weto prezydenta wobec ustawy w obecności co najmniej połowy z 460 posłów jaką większością głosów?", "trzech piątych", "dwóch trzecich", "trzech czwartych", "czterech piątych", "7"};
            question_values[27] = new String[]{"Która z ras kotów domowych wyróżnia się tym, że lubi pływać?", "turecki van", "perski", "sfiński", "egipski", "7"};
            question_values[28] = new String[]{"Iloma kondygnacjami arkad jest otoczone rzymskie Koloseum?", "czterema", "trzema", "dwoma", "pięcioma", "7"};
            question_values[29] = new String[]{"Kiedy powstał Narodowy Bank Polski?", "w 1945", "w 1890", "w 1939", "w 1960", "6"};
            question_values[30] = new String[]{"Jakie nazwisko nosił mąż Elizy Orzeszkowej?", "Orzeszko", "Orzechow", "Orzech", "Orzesznik", "6"};
            question_values[31] = new String[]{"Gdzie odbyły się pierwsze zimowe igrzyska olimpijskie?", "Chamonix", "Garmisch-Partenkirchen", "Courchevel", "Innsbruck", "6"};
            question_values[32] = new String[]{"Jakiego koloru jest czerwony maluch? ", "Czerwony", "Żółty", "Zielony", "Biały", "1"};
            question_values[33] = new String[]{"Która z tych postaci nie wystąpiła w trylogi J.R.R Tolkiena?", "Thorin Dębowa Tarcza", "Farmer Magot", "Galadriela", "Wszystkie wystąpiły", "9"};
            question_values[34] = new String[]{"Jakiego koloru rybka obiecała rybakowi spełnić jego trzy życzenia ?", "Złotego", "Zielonego", "Czarnego", "Białego", "2"};
            question_values[35] = new String[]{"Marmolady nie zrobimy z:", "pomidorów", "truskawek", "agrestu", "truskawek", "2"};
            question_values[36] = new String[]{"Faraonowie rządzili:", "Egiptem", "Polską", "Persją", "Spartą", "2"};
            question_values[37] = new String[]{"Preambuła to inaczej:", "wstęp", "rozwinięcie", "errata", "zakończenie", "3"};
            question_values[38] = new String[]{"Kto jest mistrzem tego samego oręża, w jakim specjalizowała się mitologiczna Artemida?", "Legolas", "Don Kichot", "Szymon Koczownik", "Longinus Podbipięta", "4"};
            question_values[39] = new String[]{"Z gry na jakim instrumencie słynie Czesław Mozil?", "na akordeonie", "na flecie", "na kornecie", "na gitarze", "4"};
            question_values[40] = new String[]{"Turkiye Radyo Televizyon Kurumu (TRT) to:", "Publiczne radio i telewizja w Turcji", "Składniki idealnego kebaba", "Sieć tureckich autostrad", "Program telewizyjny przedstawiający życie kurumu", "5"};
            question_values[41] = new String[]{"Świeta księga islamu to:", "Koran", "Kordian", "Kornik", "Kolon", "3"};
            question_values[42] = new String[]{"Jak nazywa się jednostka monetarna Turcji:", "Lira", "Pira", "Dira", "Mira", "7"};
            question_values[43] = new String[]{"Religia dominująca w Turcji jest:", "Islam", "Buddyzm", "Bahaizm", "Jazydyzm", "4"};
            question_values[44] = new String[]{"Anatolian to:", "Popularny w Turcji wielki i silny pies pasterski", "Imię pierwszego prezydenta Turcji", "Przejedzony kebabożerca", "Budowla w Ankarze", "8"};
            question_values[45] = new String[]{"Co w doslownym tlumaczeniu oznacza tureckie wyrazenie \"doner kebap\":", "Obracające się pieczone mięso", "Uciekający baran", "Pieczona owca", "Dobry kebab", "2"};
            question_values[46] = new String[]{"Tradycyjnie na wigilijnym stole króluje:", "karp", "troć", "płoć", "okoń", "1"};
            question_values[47] = new String[]{"Ciut to nie?", "w bród", "ociupina", "zdziebko", "krztyna", "4"};
            question_values[48] = new String[]{"Droga Krzyżowa dzieli się na:", "stacje", "misje", "sanktuaria", "haramy", "2"};
            question_values[49] = new String[]{"Kto wygrał w karty dwa patyki ten jest bogatszy o:", "dwa tysiące złotych", "dwa złote", "dwieście złotych", "dwadzieścia tysięcy złotych", "3"};
            question_values[50] = new String[]{"Rodzinna wyspa Boba Marleya to:", "Jamajka", "Cejlon", "Sumatra", "Kuba", "6"};
            question_values[51] = new String[]{"Które ciało stałe ma najmniejszą gęstość:", "drewno", "beton", "piasek", "szkło", "7"};
            question_values[52] = new String[]{"Vincent Vega z filmu \"Pulp Fiction\" wyśmiewa Holendrów, którzy zamiast keczupu używają do frytek:", "majonezu", "musztardy", "guacamole", "sosu sojowego", "5"};
            question_values[53] = new String[]{"Czego nie ma tetraedr:", "kwadratu w podstawie", "Sześciu krawędzi", "trójkątnych ścian", "czterech wierzchołków", "8"};
            question_values[54] = new String[]{"Zdaniem Dana Browna, autora \"Kodu Leonarda daVinci\" Maria Magdalena była żoną:", "Jezusa Chrystusa", "Jana Chrzciciela", "Jana Ewangelisty", "Pawła z Tarsu", "3"};
            question_values[55] = new String[]{"Ekliptyka to droga pozornego rocznego ruchu:", "Słońca", "Ziemi", "Księżyca", "Marsa", "7"};
            question_values[56] = new String[]{"Tenisista prosi arbitra głównego o tzw. challenge, gdy:", " Kwestionuje decyzję sędziów", "skarży się na bóle", "przeciwnik opóźnia się", " potrzebuje przerwy ", "2"};
            question_values[57] = new String[]{"Z gry, na jakim instrumencie słynie Czesław Mozil:", "na akordeonie", "na kornecie", "na djembe", "na ksylofonie", "4"};
            question_values[58] = new String[]{"W którym roku została ochrzczona Polska:", "966", "996", "699", "969", "2"};
            question_values[59] = new String[]{"Od czego pochodzi popularny skrót GG:", "Gadu Gadu", "Grupa Gandalfa ", "Głupi Gigant ", "żadna z powyższych ", "1"};
            question_values[60] = new String[]{"Co się kręci wokół Ziemi:", "Księżyc", "Słońce", "Mars", "Wenus", "2"};

            try {
                for (int i = 0; i < question_values.length; i++) {
                    String[] val = question_values[i];

                    ContentValues values = new ContentValues();
                    values.put("Nr_Pytania", i + 1);
                    values.put("Pytanie", val[0]);
                    values.put("Poprawna_odp", val[1]);
                    values.put("Zla_odp1", val[2]);
                    values.put("Zla_odp2", val[3]);
                    values.put("Zla_odp3", val[4]);
                    values.put("Poziom_trudnosci", Integer.parseInt(val[5]));
                    db.insert(DB_QUESTION_TABLE, null, values);
                }
            } catch (Exception error) {
                Log.e("Error", error.getMessage());
            }

            String[][] text_leading_value = new String[5][];
            text_leading_value[0] = new String[]{"Czy jesteś pewien tej odpowiedzi?", "3"};
            text_leading_value[1] = new String[]{"Jesteś pewien?", "1"};
            text_leading_value[2] = new String[]{"Masz pewność, że ta odpowiedź jest prawidłowa?", "2"};
            text_leading_value[3] = new String[]{"Myślisz, że ta odpowiedź jest poprawna?", "1"};
            text_leading_value[4] = new String[]{"Grasz o wielkie pieniądze, zastananów się jeszcze.", "3"};

            try {
                for (int i = 0; i < text_leading_value.length; i++) {
                    String[] val = text_leading_value[i];

                    ContentValues values = new ContentValues();
                    values.put("ID_tekstu", i + 1);
                    values.put("Tekst_prowadzacego", val[0]);
                    values.put("Prawdopodobienstwo_wypadniecia", val[1]);
                    db.insert(DB_TEXTS_LED_TABLE, null, values);
                }
            } catch (Exception error) {
                Log.e("Error", error.getMessage());
            }

            String[][] text_friends_value = new String[5][];
            text_friends_value[0] = new String[]{"Chyba znam odpowiedź", "3"};
            text_friends_value[1] = new String[]{"Możliwe, że jest to:", "2"};
            text_friends_value[2] = new String[]{"Chyba:", "1"};
            text_friends_value[3] = new String[]{"Nie mam pewności. Chyba:", "2"};
            text_friends_value[4] = new String[]{"W moim przekonaniu jest to:", "2"};

            try {
                for (int i = 0; i < text_friends_value.length; i++) {
                    String[] val = text_friends_value[i];

                    ContentValues values = new ContentValues();
                    values.put("ID_tekstu", i + 1);
                    values.put("Tekst_przyjaciela", val[0]);
                    values.put("Prawdopodobienstwo_wypadniecia", val[1]);
                    db.insert(DB_TEXTS_FRIEND_TABLE, null, values);
                }
            } catch (Exception error) {
                Log.e("Error", error.getMessage());
            }

        }

        /**
         * Funkcja odpowiedzialna za tworzenie bazy danych
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE_USERS_TABLE);
            db.execSQL(DB_CREATE_QUESTION_TABLE);
            db.execSQL(DB_CREATE_TEXTS_LED_TABLE);
            db.execSQL(DB_CREATE_TEXTS_FRIEND_TABLE);
            db.execSQL(DB_CREATE_RESULTS_TABLE);

            questionAdd(db);

            Log.d(DEBUG_TAG, "Database creating...");
            Log.d(DEBUG_TAG, "Table ver." + DB_VERSION + " created");
        }

        /**
         * Funkcja odpowiedzialna za aktualizację bazy danych
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_USERS_TABLE);
            db.execSQL(DROP_QUESTION_TABLE);
            db.execSQL(DROP_TEXTS_LED_TABLE);
            db.execSQL(DROP_TEXTS_FRIEND_TABLE);
            db.execSQL(DROP_RESULTS_TABLE);

            Log.d(DEBUG_TAG, "Database updating...");
            Log.d(DEBUG_TAG, "Table updated from ver." + oldVersion + " to ver." + newVersion);
            Log.d(DEBUG_TAG, "All data is lost.");

            onCreate(db);
        }
    }

    /**
     * Konstruktor klasy
     * @param context kontekts
     */
    public sqlAdapter(Context context) {
        this.context = context;
    }

    /**
     * Funkcja odpowiedzialna za otworzenie połączenia z bazą daych
     * @return
     */
    public sqlAdapter open(){
        dbHelper = new DatabaseHelper(context, DB_NAME, null, DB_VERSION);
        try {
            db = dbHelper.getWritableDatabase();
        } catch (SQLException e) {
            Log.i("info", e.getMessage());
            db = dbHelper.getReadableDatabase();
        }
        return this;
    }

    /**
     * Funkcja odpowiedzialna za zamknięcie połączenia z bazą danych
     */
    public void close() {
        dbHelper.close();
    }

    /**
     * Funkcja odpowiedzialna za wykonanie zapytania sql
     * @param sql zapytanie sql
     */
    public void sql(String sql) {
        db.execSQL(sql);
    }

    /**
     * Funkcja odpowiedzialna za usuwanie danego wiersza w danej tabeli db
     * @param id klucz podstawowy wiersza usuwanego
     * @param table nazwa tabeli
     * @param key_id nazwa kolumny klucza podstawowego
     * @return true/false
     */
    public boolean deleteTodo(int id, String table, String key_id){
        String where = key_id + "=" + id;
        return db.delete(table, where, null) > 0;
    }

    /**
     * Funkcja odpowiedzialna za pobranie wartości z danych kolumn tabeli db
     * @param kolumny lista kolumn
     * @param table nazwa tabeli
     * @return wynik zapytania
     */
    public Cursor getColumn(String[] kolumny, String table) {
        return db.query(table, kolumny, null, null, null, null, null);
    }

    /**
     * Funkcja odpowiedzialna za pobranie wartości z danych kolumn tabeli db
     * @param kolumny lista kolumn
     * @param table nazwa tabeli
     * @param where warunek szukania
     * @return wynik zapytania
     */
    public Cursor getColumn(String[] kolumny, String table, String where) {
        return db.query(table, kolumny, where, null, null, null, null);
    }

    /**
     * Funkcja odpowiedzialna za pobranie wartości z danych kolumn tabeli db
     * @param kolumny lista kolumn
     * @param table nazwa tabeli
     * @param where warunek szukania
     * @param orderBy sortowanie
     * @return wynik zapytania
     */
    public Cursor getColumn(String[] kolumny, String table, String where, String orderBy) {
        return db.query(table, kolumny, where, null, null, null, orderBy);
    }

    /**
     * Funkcja odpowiedzialna za wykonywania sql typu INSERT
     * @param newTodoValues wartości sql'a
     * @param table nazwa tabeli
     * @return true/false
     */
    public long insertTodo(ContentValues newTodoValues, String table) {
        return db.insert(table, null, newTodoValues);
    }

}
