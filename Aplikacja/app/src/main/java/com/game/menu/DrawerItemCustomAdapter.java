package com.game.menu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.game.milionerki.R;

import menu.ObjectDrawerItem;

@SuppressLint("ViewHolder") public class DrawerItemCustomAdapter extends ArrayAdapter<ObjectDrawerItem> {

	Context mContext;
	int layoutResourceId;
	ObjectDrawerItem data[] = null;
	
	public DrawerItemCustomAdapter(Context mContext, int layoutResourceId, ObjectDrawerItem[] data) {

		super(mContext, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.mContext = mContext;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ObjectDrawerItem folder = data[position];
		View listItem = convertView;
		
		LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
		listItem = inflater.inflate(folder.idLayout, parent, false);

        if (folder.action == "rankingView") {
            TextView _rankingText = (TextView) listItem.findViewById(R.id.rankingText);
            ImageView _avatar = (ImageView) listItem.findViewById(R.id.avatar);

            _rankingText.setText(folder.nick + " " + folder.name);
            if(folder.avatar.equals("1"))
                _avatar.setImageResource(R.drawable.awatar_k1);
            else
                _avatar.setImageResource(R.drawable.awatar_k2);
        }
				
		return listItem;
	}
}