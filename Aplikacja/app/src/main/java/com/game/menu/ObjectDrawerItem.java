package menu;

import java.util.ArrayList;

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

	public ObjectDrawerItem(String action, int idLayout, String val, String val1, String val2, int lp) {
		this.idLayout = idLayout;
		this.action = action;
        this.nick = val;
		this.name = val1;
        this.avatar = val2;
        this.lp = lp;
	}
}

