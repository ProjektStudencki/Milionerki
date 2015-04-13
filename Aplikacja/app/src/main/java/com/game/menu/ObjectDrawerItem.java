package menu;

import java.util.ArrayList;

/**
 * Class <code>ObjectDrawerItem</code>
 * Klasa odpowiedzialna za stworzenie obiektu danych
 * @author Kamil Gammert
 * @version 1.0, Marzec,Kwiecien 2015
 */
public class ObjectDrawerItem {

	public String action;
	public int icon;
	public String name;
	
	public String avatar;
	public String nick;
	public String id;
	public int lp;

	ArrayList<String> subitems;
	
	public int idLayout;

    /**
     * Konstruktor tworzący objekt
     *
     * @param action nazwa akcji
     * @param idLayout id layoutu
     * @param val nick gracza
     * @param val1 nazwa gracza
     * @param val2 awatar gracza
     * @param lp pozycja gracza
     */
	public ObjectDrawerItem(String action, int idLayout, String val, String val1, String val2, int lp) {
		this.idLayout = idLayout;
		this.action = action;
        this.nick = val;
		this.name = val1;
        this.avatar = val2;
        this.lp = lp;
	}
}

