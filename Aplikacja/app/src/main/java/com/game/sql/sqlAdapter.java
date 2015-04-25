package com.game.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Class <code>sqlAdapter</code>
 * Klasa odpowiedzialna za połączenie z bazą danych
 * @author Kamil Gammert
 * @version 1.0, Marzec,Kwiecien 2015
 */
public class sqlAdapter {
    private static final String DEBUG_TAG = "Baza danych";
 
    private static final int DB_VERSION = 2;

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
                "Nr_Pytania           INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "Pytanie              text not null, " +
                "Poprawna_odp         text not null, " +
                "Zla_odp1             text not null, " +
                "Zla_odp2             text not null, " +
                "Zla_odp3             text not null, " +
                "Poziom_trudnosci     Integer(2) not null " +
            ");";
    private static final String DROP_QUESTION_TABLE =
            "DROP TABLE IF EXISTS " + DB_QUESTION_TABLE;

    private static final String DB_CREATE_TEXTS_LED_TABLE =
            "CREATE TABLE " + DB_TEXTS_LED_TABLE + "( " +
                "ID_tekstu            INTEGER(3) PRIMARY KEY, " +
                "Tekst_prowadzacego   text not null, " +
                "Prawdopodobiensto_wypadniecia Integer(3) not null " +
            ");";
    private static final String DROP_TEXTS_LED_TABLE =
            "DROP TABLE IF EXISTS " + DB_TEXTS_LED_TABLE;

    private static final String DB_CREATE_TEXTS_FRIEND_TABLE =
            "CREATE TABLE " + DB_TEXTS_FRIEND_TABLE + "( " +
                "ID_tesktu            INTEGER(3) PRIMARY KEY, " +
                "Tekst_przyjaciela    text not null, " +
                "Prawdopodobienstwo_wypadniecia Integer(3) not null "+
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
            db.execSQL("INSERT INTO " + DB_QUESTION_TABLE + " ﻿VALUES(1, 'Jak nazywa się klub piłkarski, który zdobył Puchar Ligi Mistrzów w sezonie 2013/2014?', 'Real Madryt', 'FC Barcelona', 'Atletico Madryt', 'Bayern Monachium', 3)," +
                            "(2, 'Jaki jest wynik sumowania liczb 2 i 8?', 10, 6, 82, 28, 1), " +
                            "(3, 'Jaki jest wynik sumowania liczb 5 i 5?', 10, 55, 0, 5, 1), " +
                            "(4, 'Co powstanie z wody, gdy ją zamrozimy?', 'Lód', 'Ogień', 'Chmura gazu', 'Mleko', 1), " +
                            "(5, 'Jaka legenda jest związana z Warszawą?', 'O Syrence', 'O smoku wawelskim', 'O Szymonie, co zbierał grzyby', 'O wsi za miastem', 2), " +
                            "(6, 'Jaki jest kod pocztowy Wałcza?', '78-600', '78-650', '68-500', '75-800', 7), " +
                            "(7, 'Co nie jest nazwą stylu pływackiego?', 'Rekin', 'Żabka', 'Kraul', 'Delfin', 2), " +
                            "(8, 'Do ilu punktów liczy się set w tenisie stołowym?', '21', '22', '20', '11', 3), " +
                            "(9, 'Jaki jest numer straży pożarnej?', '998', '999', '666', '997', 2), " +
                            "(10, 'Ile jest znaków zodiaku?', '12', '10', '11', '14', 3), " +
                            "(11, 'Jakie są najwyższe góry na świecie?', 'Himalaje', 'Pireneje', 'Alpy', 'Sudety Wielkie', 2), " +
                            "(12, 'Jaka część mowy odpowiada na pytania: kto, co?', 'rzeczownik', 'czasownik', 'dopełniacz', 'przymiotnik', 2), " +
                            "(13, 'Kim jest Tomasz Kuszczak?', 'Bramkarzem', 'Prezenterem MTV', 'Aktorem', 'Koszykarzem', 4), " +
                            "(14, 'Lekarz leczący zęby nazywa się...', 'Stomatologiem', 'Laryngologiem', 'Kowalem', 'Weterynarzem', 1), " +
                            "(15, 'W którym roku rozpoczęła się II Wojna Światowa?', '1939', '2014', '1911', '1945', 1), " +
                            "(16, 'Ile trwała wojna stuletnia?', '116', '100', '144', '99', 5), " +
                            "(17, 'Jak się nazywa polski skoczek narciarski, który zdobył dwa złote medale olimpijskie w czasie jednej Olimpiady?', 'Kamil Stoch', 'Robert Mateja', 'Stefan Hula', 'Adam Małysz', 3), " +
                            "(18, 'Skąd pochodzi Conan Barbarzyńca?', 'z Cimmerii', 'z Rivii', 'z Oz', 'z Mordoru', 10), " +
                            "(19, 'Komiksowym \"dzieckiem\" rysownika Boba Kane\'a jest:', 'Batman', 'Superman', 'Spider-Man', 'Captain America', 10), " +
                            "(20, 'Likier maraskino produkuje się z maraski, czyli odmiany:', 'wiśni', 'jabłoni', 'figi', 'gruszy', 10), " +
                            "(21, 'Na których igrzyskach wybudowano po raz pierwszy wioskę olimpijską?', 'Los Angeles 1932', 'Sztokholm 1912', 'Londyn 1948', 'Helsinki 1952', 9), " +
                            "(22, 'Kogo, oprócz Judasza, umieścił Dante na samym dnie piekła?', 'Brutusa i Kasjusza', 'Kaligulę i Nerona', 'Kaina', 'Piłata', 9), " +
                            "(23, 'Kto w czasie sprawowania władzy był konduktatorem, czyli wodzem narodu?', 'Nicolae Ceausescu', 'Fidel Castro', 'Michaił Gorbaczow', 'Todor Żiwkow', 9), " +
                            "(24, 'Ile gwiazd znajduje się na fladze państwowej Australii?', 'sześć', 'cztery', 'osiem', 'pięć', 8), " +
                            "(25, 'Jak nazywał się władca krainy Mordor z \"Władcy pierścieni\" Tolkiena?', 'Sauron', 'Gorgoth', 'Saruman', 'Gandalf', 8), " +
                            "(26, 'Ile centymetrów wynosi całkowita długość kortu do badmintona w grze pojedynczej?', '1340', '1304', '1430', '1520', 8), " +
                            "(27, 'Sejm może obalić weto prezydenta wobec ustawy w obecności co najmniej połowy z 460 posłów jaką większością głosów?', 'trzech piątych', 'dwóch trzecich', 'trzech czwartych', 'czterech piątych', 7), " +
                            "(28, 'Która z ras kotów domowych wyróżnia się tym, że lubi pływać?', 'turecki van', 'perski', 'sfiński', 'egipski', 7), " +
                            "(29, 'Iloma kondygnacjami arkad jest otoczone rzymskie Koloseum?', 'czterema', 'trzema', 'dwoma', 'pięcioma', 7), " +
                            "(30, 'Kiedy powstał Narodowy Bank Polski?', 'w 1945', 'w 1890', 'w 1939', 'w 1960', 6), " +
                            "(31, 'Jakie nazwisko nosił mąż Elizy Orzeszkowej?', 'Orzeszko', 'Orzechow', 'Orzech', 'Orzesznik', 6), " +
                            "(32, 'Gdzie odbyły się pierwsze zimowe igrzyska olimpijskie?', 'Chamonix', 'Garmisch-Partenkirchen', 'Courchevel', 'Innsbruck', 6), " +
                            "(33, 'Jakiego koloru jest czerwony maluch? ', 'Czerwony', 'Żółty', 'Zielony', 'Biały', 1), " +
                            "(34, 'Która z tych postaci nie wystąpiła w trylogi J.R.R Tolkiena?', 'Thorin Dębowa Tarcza', 'Farmer Magot', 'Galadriela', 'Wszystkie wystąpiły', 9), " +
                            "(35, 'Jakiego koloru rybka obiecała rybakowi spełnić jego trzy życzenia ?', 'Złotego', 'Zielonego', 'Czarnego', 'Białego', 2), " +
                            "(36, 'Marmolady nie zrobimy z:', 'pomidorów', 'truskawek', 'agrestu', 'truskawek', 2), " +
                            "(36, 'Faraonowie rządzili:', 'Egiptem', 'Polską', 'Persją', 'Spartą', 2), " +
                            "(37, 'Preambuła to inaczej:', 'wstęp', 'rozwinięcie', 'errata', 'zakończenie', 3), " +
                            "(38, 'Kto jest mistrzem tego samego oręża, w jakim specjalizowała się mitologiczna Artemida?', 'Legolas', 'Don Kichot', 'Szymon Koczownik', 'Longinus Podbipięta', 4), " +
                            "(39, 'Z gry na jakim instrumencie słynie Czesław Mozil?', 'na akordeonie', 'na flecie', 'na kornecie', 'na gitarze', 4), " +
                            " 40, 'Turkiye Radyo Televizyon Kurumu (TRT) to:', 'Publiczne radio i telewizja w Turcji', 'Składniki idealnego kebaba', 'Sieć tureckich autostrad', 'Program telewizyjny przedstawiający życie kurumu', 5), " +
                            "(41, 'Świeta księga islamu to:', 'Koran', 'Kordian', 'Kornik', 'Kolon', 3), " +
                            " (42, 'Jak nazywa się jednostka monetarna Turcji:', 'Lira', 'Pira', 'Dira', 'Mira', 7), " +
                            "(43, 'Religia dominująca w Turcji jest:', 'Islam', 'Buddyzm', 'Bahaizm', 'Jazydyzm', 4), " +
                            "(44, 'Anatolian to:', 'Popularny w Turcji wielki i silny pies pasterski', 'Imię pierwszego prezydenta Turcji', 'Przejedzony kebabożerca', 'Budowla w Ankarze', 8), " +
                            "(45, 'Co w doslownym tlumaczeniu oznacza tureckie wyrazenie \"doner kebap\":', 'Obracające się pieczone mięso', 'Uciekający baran', 'Pieczona owca', 'Dobry kebab', 2), " +
                            "(46, 'Tradycyjnie na wigilijnym stole króluje:', 'karp', 'troć', 'płoć', 'okoń', 1), " +
                            " (47, 'Ciut to nie?', 'w bród', 'ociupina', 'zdziebko', 'krztyna', 4), " +
                            "(48, 'Droga Krzyżowa dzieli się na:', 'stacje', 'misje', 'sanktuaria', 'haramy', 2), " +
                            "(49, 'Kto wygrał w karty dwa patyki ten jest bogatszy o:', 'dwa tysiące złotych', 'dwa złote', 'dwieście złotych', 'dwadzieścia tysięcy złotych', 3), " +
                            "(50, 'Rodzinna wyspa Boba Marleya to:', 'Jamajka', 'Cejlon', 'Sumatra', 'Kuba', 6), " +
                            "(51, 'Które ciało stałe ma najmniejszą gęstość:', 'drewno', 'beton', 'piasek', 'szkło', 7), " +
                            "(52, 'Vincent Vega z filmu \"Pulp Fiction\" wyśmiewa Holendrów, którzy zamiast keczupu używają do frytek:', 'majonezu', 'musztardy', 'guacamole', 'sosu sojowego', 5), " +
                            "(53, 'Czego nie ma tetraedr:', 'kwadratu w podstawie', 'Sześciu krawędzi', 'trójkątnych ścian', 'czterech wierzchołków', 8), " +
                            "(54, 'Zdaniem Dana Browna, autora \"Kodu Leonarda daVinci\" Maria Magdalena była żoną:', 'Jezusa Chrystusa', 'Jana Chrzciciela', 'Jana Ewangelisty', 'Pawła z Tarsu', 3), " +
                            "(55, 'Ekliptyka to droga pozornego rocznego ruchu:', 'Słońca', 'Ziemi', 'Księżyca', 'Marsa', 7), " +
                            "(56, 'Tenisista prosi arbitra głównego o tzw. challenge, gdy:', ' Kwestionuje decyzję sędziów', 'skarży się na bóle', 'przeciwnik opóźnia się', ' potrzebuje przerwy ', 2), " +
                            "(57, 'Z gry, na jakim instrumencie słynie Czesław Mozil:', 'na akordeonie', 'na kornecie', 'na djembe', 'na ksylofonie', 4), " +
                            "(58, 'W którym roku została ochrzczona Polska:', '966', '996', '699', '969', 2), " +
                            "(59, 'Od czego pochodzi popularny skrót GG:', 'Gadu Gadu', 'Grupa Gandalfa ', 'Głupi Gigant ', 'żadna z powyższych ', 1), " +
                            "(60, 'Co się kręci wokół Ziemi:', 'Księżyc', 'Słońce', 'Mars', 'Wenus', 2)"
            );
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
     * Funkcja odpowiedzialna za wykonywania sql typu INSERT
     * @param newTodoValues wartości sql'a
     * @param table nazwa tabeli
     * @return true/false
     */
    public long insertTodo(ContentValues newTodoValues, String table) {
        return db.insert(table, null, newTodoValues);
    }
    
}
