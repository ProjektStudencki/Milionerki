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

    private static final int DB_VERSION = 9;

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
            String[][] question_values = new String[143][];
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
            question_values[61] = new String[]{"Czym jest wynik dodawania:", "sumą", "różnicą", "iloczynem", "ilorazem", "1"};
            question_values[62] = new String[]{"Czym jest wynik odejmowania:", "różnicą", "sumą", "iloczynem", "ilorazem", "1"};
            question_values[63] = new String[]{"Czym jest wynik mnożenia:", "iloczynem", "sumą", "różnicą", "ilorazem", "1"};
            question_values[64] = new String[]{"Czym jest wynik dzielenia:", "ilorazem", "sumą", "różnicą", "iloczynem", "1"};
            question_values[65] = new String[]{"Jedenastką w piłce nożnej nazywamy:", "karny", "wyrzut z autu", "rożny", "rzut sędziowski", "1"};
            question_values[66] = new String[]{"Ile jest kół w znaku olimpijskim:", "pieć", "cztery", "sześć", "siedem", "2"};
            question_values[67] = new String[]{"Urządzenie, które można wielokrotnie ładować i przechowuje prąd to:", "akumulator", "bateria", "opornik", "kondensator", "2"};
            question_values[68] = new String[]{"Znane porzekadło głosiło, że: \"Polska dla Polaków, ziemia dla...:\"", "Ziemniaków", "Cebuli", "Francuzów", "Niemców", "3"};
            question_values[69] = new String[]{"Jaki żółty środek transportu przyniósł sławę Beatlesom ?", "łódz podwodna", "autobus", "parostatek", "tramwaj", "4"};
            question_values[70] = new String[]{"Który z podanych instrumentów nie należydo grupy aerofonów?", "żele", "tuba", "dudy", "obój", "7"};
            question_values[71] = new String[]{"Wysoka czapka futrzana noszona w Wielkiej Brytanii przez reprezentacyjne oddziały gwardii to:", "bermyca", "papacha", "tiara", "kołpak", "5"};
            question_values[72] = new String[]{"Czym Chińczycy tradycyjnie jadają potrawy zryżu?", "pałeczkami", "widelcami", "wykałaczkami", "łyżkami", "1"};
            question_values[73] = new String[]{"Ile miesięcy liczy kwartał?", "trzy", "cztery", "dwa", "jeden", "2"};
            question_values[74] = new String[]{"11 listopada to rocznica:", "odzyskania niepodległośc", "wybuchu I wojny światowej", "uchwalenia konstytucji", "powstania listopadowego", "4"};
            question_values[75] = new String[]{"Woda to tlenek", "wodoru", "węgla", "srebra", "żelaza", "4"};
            question_values[76] = new String[]{"Międzynarodowa organizacja policyjna ścigająca przestępstwa kryminalne to:", "Interpol", "Mosad", "Czeka", "Secret Service", "4"};
            question_values[77] = new String[]{"Które z określeń nie oznacza wysłannika?", "ordynat", "poseł", "kurier", "emisariusz", "3"};
            question_values[78] = new String[]{"Jakie włoskie miasto było tłem burzliwego itragicznego w skutkach romansu Romea i Julii?", "Werona", "Wenecja", "Florencja", "Palermo", "3"};
            question_values[79] = new String[]{"Kto wypowiedział słowa: Ja nie z soli aniz roli, ale z tego, co mnie boli;?", "Stefan Czarniecki", "Jan III Sobieski", "Bartosz Głowacki", "Józef Piłsudzki", "5"};
            question_values[80] = new String[]{"Wafel pieczony z delikatnego ciasta w specjalnych foremkach to:", "andrut", "bajgiel", "bakława", "beza", "3"};
            question_values[81] = new String[]{"Gdzie produkowany jest od 1835 roku włoski wermut Cinzano (rodzaj wina)?", "w Turynie", "w Wenecji", "w Mediolanie", "we Florencji", "4"};
            question_values[82] = new String[]{"Jak nazywa się amerykańska bazawojskowa na Kubie?", "Guantanamo", "Santa Clara", "Bayamo", "Matanzas", "5"};
            question_values[83] = new String[]{"Z jakimi plemionami Indian walczył generał Custer nad Little Big Horn?", "Sjuksami i Szejenami", "Czirokezami i Seminolami", "Szoszonami i Wronami", "Huronami i Mohikanami", "9"};
            question_values[84] = new String[]{"Kto wypowiedział słynne słowa: mały krok dla człowieka, ale wielki skok dla ludzkości?", "kosmonauta na księżycu", "laureat nagrody nobla", "genetyk, który sklonował owcę","konstruktor pierwszego komputera", "4"};
            question_values[85] = new String[]{"W którym roku została wynaleziona żarówka", "1879", "1979", "1679","1875", "4"};
            question_values[86] = new String[]{"Jakimi słowami kończy się 'Zemsta' Aleksandra Fredro?", "Tak jest - zgoda, a Bóg wtedy rękę poda.", "Nigdy! Nigdy! Nigdy!", "Ja tam byłem... Wodę, miód i wino piłem.", "I tak właśnie kończy się ta historia. Historia prawdziwa...", "8"};
            question_values[87] = new String[]{"Jak nazywa się światowa sieć komputerowa?", "Internet", "światłowód", "lokalna sieć", "kablówka", "2"};
            question_values[88] = new String[]{"System operacyjny Windows to zbiór programów, które:", "zarządzają komputerem i nadzorują jego pracę", "obsługują wyłącznie klawiaturę i monitor", "wykonują skomplikowane obliczenia matematyczne", "wyświetlają tylko okna dialogowe dla użytkownika", "1"};
            question_values[89] = new String[]{"Pliki o rozszerzeniu nazwy COM to pliki:", "wykonywalne", "multimedialne", "muzyczne", "graficzne", "2"};
            question_values[90] = new String[]{"Urządzenia peryferyjne to:", "urządzenia wejścia-wyjścia", "urządzenia wejścia", "urządzenia wyjścia", "urządzenia bez wejścia i bez wyjścia", "3"};
            question_values[91] = new String[]{"Klawisz Backspace pozwala", "skasować znak po lewej stronie kursora", "skasować znak po prawej stronie kursora", "skasować znak po lewej i prawej stronie kursora", "dodać znak twardej spacji", "2"};
            question_values[92] = new String[]{"Pamięć operacyjna (o swobodnym dostępie) jest określana symbolem:", "RAM", "ROM", "GB", "MB", "2"};
            question_values[93] = new String[]{"Ile bajtów zawarte jest w 4 kB?", "4096", "4000", "4192", "4012", "4"};
            question_values[94] = new String[]{"Wyrazem autonomii, jaką uzyskała Galicja na początku lat sześćdziesiątych XIX wieku, był:", "Sejm Krajowy", "Rada Krajowa", "namiestnik", "minister do spraw galicyjskich", "9"};
            question_values[95] = new String[]{"W monarchii absolutnej Ludwika XIV pojawia się nowy urzędnik prowincjonalny:", "intendent", "kontroler skarbu", "sekretarz stanu", "gubernator prowincji", "8"};
            question_values[96] = new String[]{"Żakerią nazywano:", "powstanie chłopów francuskich", "ruch studencki", "powstanie chłopów słowiańskich", "oddziały francuskie na służbie króla Anglii", "9"};
            question_values[97] = new String[]{"Który z przywilejów szlacheckich zabraniał chłopom opuszczać wieś bez zgody pana?", "piotrkowski", "brzeski", "czerwiński", "warecki", "9"};
            question_values[98] = new String[]{"Pierwsza pielgrzymka Jana Pawła II do Polski miała miejsce w roku:", "1983", "1984", "1985", "1982", "7"};
            question_values[99] = new String[]{"Jednym z głównych teoretyków konserwatyzmu był:", "Edmund Burke", "Micheal Bekanuin", "Louis-Augusto Benaventura", "Peter Sampepe", "8"};
            question_values[100] = new String[]{"Inicjatorem utworzenia organizacji Czerwonego Krzyża był:", "Henri Dunant", "Maria Skłodowska-Curie", "Alessandro de la Fleming", "Albert Schweitzer", "7"};
            question_values[101] = new String[]{"Pierwiastki chemiczne, które są niezbędne dla żywych organizmów i potrzebne w dużych ilościach, to:", "makroskładniki", "kwasy nukleinowe ", "mikroskładniki ", "megaskładniki", "6"};
            question_values[102] = new String[]{"Jeden z pierwiastków - fluor, pełni ważną funkcję dla organizmu. Jaką?", "niezbędny w tworzeniu szkliwa", "składnik hemoglobiny", "niezbędny w procesie oddychania", "wpływa na wzrost kości", "5"};
            question_values[103] = new String[]{"Organizmy, dla których miejscem bytowania i zdobywania pokarmu są inne organizmy żywe, to:", "pasożyty", "drapieżniki", "konsumenci", "pasożyty i drapieżniki", "5"};
            question_values[104] = new String[]{"Dorosły człowiek powinien mieć 32 zęby. Zęby do kruszenia i żucia pokarmu to: ", "przedtrzonowe", "siekacze", "kły", "trzonowe", "5"};
            question_values[105] = new String[]{"Język jest w stanie wykryć cztery podstawowe smaki. Który jest wykrywany na jego czubku:", "słodki", "kwaśny", "słony", "gorzki", "5"};
            question_values[106] = new String[]{"Zdolność populacji do wydania nowych osobników to:", "rozrodczość", "zagęszczanie", "kolonizacja", "emigracja", "3"};
            question_values[107] = new String[]{"Hałas zmierzony w decybelach w mieście i podczas przerwy w szkole wynosi:", "70 dB", "60 dB", "30 dB", "40 dB", "4"};
            question_values[108] = new String[]{"Jaką metodę zastosowałbyś bezpośrednio po oparzeniu palca?", "wkładamy go do zimnej wody", "wkładamy go do gorącej wody", "masujemy palec ", "nacieramy palec maścią", "2"};
            question_values[109] = new String[]{"Dializator to tzw. sztuczna nerka - urządzenie, które ratuje życie ludzi w czasie:", "niewydolności nerek", "kamicy nerkowej ", "zapalenia cewki moczowej", "zapalenia nerek", "3"};
            question_values[110] = new String[]{"Reakcją obronną skóry na nadmiar promieni słonecznych jest opalenizna, spowodowana wytwarzaniem w skórze:", "melaniny", "karotenu", "melatoniny ", "bilirubiny", "5"};
            question_values[111] = new String[]{"Które z zanieczyszczeń powietrza powodują powstawanie kwaśnych opadów?", "tlenek siarki", "pyły", "sadza", "tlenek wapnia", "2"};
            question_values[112] = new String[]{"Pierwszym ogniwem łańcucha pokarmowego są:", "producenci", "konsumenci I rzędu", "reducenci", "destruenci", "2"};
            question_values[113] = new String[]{"Gruczoły dokrewne produkują:", "hormony", "krwinki czerwone", "limfę", "enzymy trawienne", "4"};
            question_values[114] = new String[]{"Niedobór insuliny we krwi powoduje:", "cukrzycę", "tężyczkę", "chorobę wieńcową", "zawał serca", "2"};
            question_values[115] = new String[]{"Krew jasnoczerwona, pod ciśnieniem płynie w: ", "tętnicach", "żyłach", "naczyniach włosowatych", "kapilarach", "4"};
            question_values[116] = new String[]{"Pierwszy odcinek jelita cienkiego to:", "dwunastnica", "pęcherzyk żółciowy ", "jelito grube", "wyrostek robaczkowy ", "3"};
            question_values[117] = new String[]{"W procesach anabolicznych energia jest:", "pobierana", "wydalana", "wyzwalana", "syntezowana", "4"};
            question_values[118] = new String[]{"Odczyn gleby nie może być:", "solny", "obojętny", "kwaśny", "zasadowy", "2"};
            question_values[119] = new String[]{"Przykładem pasożyta jest:", "huba", "lew", "jeż", "owad i kwiat", "2"};
            question_values[120] = new String[]{"Składniki krwi odpowiadające za krzepnięcie to:", "trombocyty", "erytrocyty ", "leukocyty", "osocze", "6"};
            question_values[121] = new String[]{"Przedstawiciele, której gromady składają skrzek?", "płazy", "gady", "ryby", "ssaki", "3"};
            question_values[122] = new String[]{"Zespół nabytego upośledzenia odporności to:", "AIDS", "anemia", "NSM", "BCB", "2"};
            question_values[123] = new String[]{"Która z podanych odpowiedzi nie jest prawdziwa? Skórę tworzą:", "tkanka nabłonkowa", "skóra właściwa ", "naskórek", "tkanka podskórna", "2"};
            question_values[124] = new String[]{"Który z podanych opisów dotyczy glukozy?", "cukier prosty, powstaje w fotosyntezie", "dwucukier składnik mleka ssaków ", "błonnik, składnik ściany komórkowej", "wielocukier, magazynowany w wątrobie ", "3"};
            question_values[125] = new String[]{"Wśród wymienionych niżej drzew najbardziej wrażliwym na skutki kwaśnych opadów jest:", "jodła", "modrzew", "dąb", "buk", "6"};
            question_values[126] = new String[]{"Cząsteczka DNA zbudowana jest z:", "dwóch nici", "jednej nici", "trzech nici", "czterech nici", "5"};
            question_values[127] = new String[]{"Nie u wszystkich organizmów występuje:", "chlorofil", "DNA", "tłuszcze", "węglodowany", "4"};
            question_values[128] = new String[]{"Który z poniższych odpadów nie może być wyrzucony do pojemnika na kompost:", "kartony po mleku", "obierki z warzyw", "skorupki jaj ", "fusy od kawy i herbaty ", "3"};
            question_values[129] = new String[]{"Burak, rzepa i marchew mają korzenie:", "spichrzowe", "czepne", "namorzynowe", "posiadające chlorofil", "4"};
            question_values[130] = new String[]{"Larwa muchy i pszczoły to:", "czerw", "pędrak", "gąsiennica", "poczwarka", "6"};
            question_values[131] = new String[]{"Czym Chińczycy jadają potrawy z ryżu?", "pałeczkami", "widelcami", "łyżkami", "nożami", "1"};
            question_values[132] = new String[]{"Ile miesięcy liczy kwartał?", "3", "2", "1", "4", "2"};
            question_values[133] = new String[]{"11 listopada to w Polsce rocznica:", "odzyskania niepodległości", "uchwalenia nowego prawa wyborczego", "pochodu majowego", "powstania legendy o smoku", "2"};
            question_values[134] = new String[]{"Woda to tlenek", "wodoru", "metanu", "złota", "tlenu", "2"};
            question_values[135] = new String[]{"Międzynarodowa organizacja policyjna ścigajśca przestępstwa kryminalne to:", "Interpol", "Moas", "Mosad", "SecretService", "3"};
            question_values[136] = new String[]{"Jakie miasto było tłem burzliwego i tragicznego romansu Romea i Julii?", "Werona", "Wenecja", "Wenezuela", "Florencja", "4"};
            question_values[137] = new String[]{"Wafel pieczony z delikatnego ciasta w specjalnych foremkach to: ", "andrut", "wafel light", "beza", "bajgiel", "4"};
            question_values[138] = new String[]{"Wysoka czapka futrzana noszona w Wielkiej Brytanii przez reprezentacyjne oddziały gwardii to:", "bermyca", "tiara", "papaja", "kapeleano", "6"};
            question_values[139] = new String[]{"Gdzie produkowany jest od 1835 roku włoski rodzaj wina Cinzano?", "w Turynie", "w Paryżu", "we Florencji", "w Rzymie", "6"};
            question_values[140] = new String[]{"Który z podanych instrumentów nie należy do grupy aerofonów?", "żele", "obój", "tuba", "duda", "8"};
            question_values[141] = new String[]{"Jak nazywa się amerykańska baza wojskowa na Kubie?", "Guantanamo", "Alcatraz", "Bayamolo", "Santa Closed", "8"};
            question_values[142] = new String[]{"Z jakimi plemionami Indian walczył generał Custer nad Little Big Horn", "Sjuksami i Szejenami", "Mohikanami i Huronami", "Troglodytami i Harpiami", "Czirokezami i Seminolami", "9"};

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

            String[][] text_leading_value = new String[40][];
            text_leading_value[0] = new String[]{"Czy jesteś pewien tej odpowiedzi?", "3"};
            text_leading_value[1] = new String[]{"Jesteś pewny tego?", "2"};
            text_leading_value[2] = new String[]{"Na pewno chcesz zaznaczyć tę odpowiedź?", "1"};
            text_leading_value[3] = new String[]{"Zastanowiłeś się i chcesz wybrać tę odpowiedź?", "1"};
            text_leading_value[4] = new String[]{"Na pewno?", "3"};
            text_leading_value[5] = new String[]{"Wybierasz to?", "2"};
            text_leading_value[6] = new String[]{"Pomyśl chwilę zanim potwierdzisz wybór", "1"};
            text_leading_value[7] = new String[]{"Na pewno ta odpowiedź?", "3"};
            text_leading_value[8] = new String[]{"Potem już nie będzie odwrotu", "2"};
            text_leading_value[9] = new String[]{"Zaznaczamy?", "1"};
            text_leading_value[10] = new String[]{"Wybierasz tę odpowiedź?", "3"};
            text_leading_value[11] = new String[]{"Niezły z Ciebie ryzykant", "2"};
            text_leading_value[12] = new String[]{"Nie biorę odpowiedzialności za Twój wybór", "1"};
            text_leading_value[13] = new String[]{"Kto nie ryzykuje ten nie wygrywa", "3"};
            text_leading_value[14] = new String[]{"Zaznaczasz tę odpowiedź?", "2"};
            text_leading_value[15] = new String[]{"Na sto procent wybieramy tę odpowiedź?", "1"};
            text_leading_value[16] = new String[]{"Jesteś pewien?", "1"};
            text_leading_value[17] = new String[]{"Masz pewność, że ta odpowiedź jest prawidłowa?", "2"};
            text_leading_value[18] = new String[]{"Myślisz, że ta odpowiedź jest poprawna?", "1"};
            text_leading_value[19] = new String[]{"Grasz o wielkie pieniądze, zastananów się jeszcze.", "3"};
            text_leading_value[20] = new String[]{"Zastanów się jeszcze...", "1"};
            text_leading_value[21] = new String[]{"Pomyśl jeszcze nad tym chwilę. Grasz o wysoką wygraną.", "2"};
            text_leading_value[22] = new String[]{"Czy jest to Twoja ostateczna decyzja?", "3"};
            text_leading_value[23] = new String[]{"Pamiętaj, że grasz o wysoką stawkę.", "3"};
            text_leading_value[24] = new String[]{"Jedna błędna odpowiedź i odpadasz z rozgrywki. Pamiętaj o tym!", "3"};
            text_leading_value[25] = new String[]{"To tylko Twoje przypuszczenie, czy ostateczna odpowiedź?", "2"};
            text_leading_value[26] = new String[]{"Czy jest Twoja ostatecza odpowiedź?", "2"};
            text_leading_value[27] = new String[]{"Jesteś pewien, że chcesz zaznaczyć właśnie taką odpowiedź?", "3"};
            text_leading_value[28] = new String[]{"Strzelasz?", "1"};
            text_leading_value[29] = new String[]{"Pamiętaj, że ryzykujesz sporą wygraną...", "2"};
            text_leading_value[30] = new String[]{"Nie wydaje Ci się, że jest to zbyt pochopna decyzja?", "3"};
            text_leading_value[31] = new String[]{"Przemyśl to jeszcze raz. Mamy dużo czasu.", "3"};
            text_leading_value[32] = new String[]{"Jestem ciekawy, czy jesteś pewień swojej odpowiedzi.", "1"};
            text_leading_value[33] = new String[]{"Pamiętaj, że możesz stracić szansę na osiągnięciu najlepszego wyniku udzielając jednej błędnej odpowiedzi.", "2"};
            text_leading_value[34] = new String[]{"Zaskakujesz mnie. Jesteś tego pewien?", "3"};
            text_leading_value[35] = new String[]{"Przemyśl to jeszcze raz nim podejmiesz ostateczną decyzję.", "3"};
            text_leading_value[36] = new String[]{"Widzę, że jesteś pewien swojej odpowiedzi, ale co w przypadku, gdy nie okaże się ona poprawna?", "2"};
            text_leading_value[37] = new String[]{"Daj sobie jeszcze chwilę czasu na podjęcie decyzji.", "2"};
            text_leading_value[38] = new String[]{"Może warto jeszcze przemyśleć swoją odpowiedź?", "2"};
            text_leading_value[39] = new String[]{"Pamiętaj - jeden błąd i odpadasz z rozgrywki!", "3"};

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

            String[][] text_friends_value = new String[36][];
            text_friends_value[0] = new String[]{"Chyba znam odpowiedź", "3"};
            text_friends_value[1] = new String[]{"Możliwe, że jest to:", "2"};
            text_friends_value[2] = new String[]{"Chyba:", "1"};
            text_friends_value[3] = new String[]{"Prawdopodobnie jest to:", "3"};
            text_friends_value[4] = new String[]{"Chyba jest to:", "2"};
            text_friends_value[5] = new String[]{"Strzelam:", "1"};
            text_friends_value[6] = new String[]{"Na sto procent jest to:", "3"};
            text_friends_value[7] = new String[]{"Wiem! Jest to:", "2"};
            text_friends_value[8] = new String[]{"Wybierz:", "1"};
            text_friends_value[9] = new String[]{"Odpowiedź:", "3"};
            text_friends_value[10] = new String[]{"Znam odpowiedź:", "2"};
            text_friends_value[11] = new String[]{"Wybieram:", "1"};
            text_friends_value[12] = new String[]{"Ciężko Ci pomóc, ale chyba:", "3"};
            text_friends_value[13] = new String[]{"Jest to:", "2"};
            text_friends_value[14] = new String[]{"Zaznacz:", "1"};
            text_friends_value[15] = new String[]{"Bez zastanowienia:", "3"};
            text_friends_value[16] = new String[]{"Nie mam pewności. Chyba:", "2"};
            text_friends_value[17] = new String[]{"W moim przekonaniu jest to:", "2"};
            text_friends_value[18] = new String[]{"Nie ulega to żadnej wątpliwości, iż jest to:", "1"};
            text_friends_value[19] = new String[]{"Przypuszczam, że jest to:", "2"};
            text_friends_value[20] = new String[]{"Nie wiem. Czysty strzał:", "3"};
            text_friends_value[21] = new String[]{"To z całą pewnością będzie:", "3"};
            text_friends_value[22] = new String[]{"Wydaje mi się, że będzie to:", "2"};
            text_friends_value[23] = new String[]{"Daj mi chwilę... Myślę, że będzie to:", "2"};
            text_friends_value[24] = new String[]{"Myślę, że to:", "3"};
            text_friends_value[25] = new String[]{"Z pełnym przekonaniem: ", "3"};
            text_friends_value[26] = new String[]{"Chciałbym pomóc Ci, ale to będzie tylko strzał:", "1"};
            text_friends_value[27] = new String[]{"Czekaj, zapytam siostrę... Mówi, że:", "1"};
            text_friends_value[28] = new String[]{"Czekaj, zapytam brata... Mówi, że:", "2"};
            text_friends_value[29] = new String[]{"Czekaj, zapytam dziadka... Mówi, że:", "2"};
            text_friends_value[30] = new String[]{"Czekaj, zapytam babcię... Mówi, że:", "2"};
            text_friends_value[31] = new String[]{"Wybierz odpowiedź:", "1"};
            text_friends_value[32] = new String[]{"Nie jestem znawcą tego tematu, ale chyba:", "1"};
            text_friends_value[33] = new String[]{"Bez przekonania, ale...", "2"};
            text_friends_value[34] = new String[]{"Daj mi chwilę... Chyba:", "2"};
            text_friends_value[35] = new String[]{"Nie tylko Ty masz problem z odpowiedzią. Strzelam:", "1"};

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
