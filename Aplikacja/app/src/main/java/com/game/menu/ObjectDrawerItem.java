package menu;

import java.util.ArrayList;

public class ObjectDrawerItem {

	public String action;
	public int icon;
	public String name;
	
	public String avatar;
	public String nick;
	public String id;
	
	ArrayList<String> subitems;
	
	public int idLayout;

	public ObjectDrawerItem(String action, int idLayout, String val, String val1, String val2) {
		this.idLayout = idLayout;
		this.action = action;
        this.nick = val;
		this.name = val1;
        this.avatar = val2;
	}
}

