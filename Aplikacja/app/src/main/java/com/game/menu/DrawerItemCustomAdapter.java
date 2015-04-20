package com.game.menu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.game.milionerki.R;

import org.w3c.dom.Text;

import menu.ObjectDrawerItem;

/**
 * Class <code>DrawerItemCustomAdapter</code>	
 * Klasa odpowiedzialna za tworzenie widoków
 * @author Kamil Gammert
 * @version 1.0, Marzec,Kwiecien 2015
 */
@SuppressLint("ViewHolder") public class DrawerItemCustomAdapter extends ArrayAdapter<ObjectDrawerItem> {

	Context mContext;
	int layoutResourceId;
	ObjectDrawerItem data[] = null;

    /**
     * Konstruktor klasy
     *
     * @param mContext kontekst
     * @param layoutResourceId Numer identyfikacyjny layoutu
     * @param data Dane, które zostają przekazane do klasy
     */
	public DrawerItemCustomAdapter(Context mContext, int layoutResourceId, ObjectDrawerItem[] data) {

		super(mContext, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.mContext = mContext;
		this.data = data;
	}

    /**
     * Funkcja tworząca widok
     *
     * @param position aktualna pozycja w tablicy danych
     * @param convertView widok, w którym wprowadzane są dane
     * @param parent grupa widoku
     * @return zostaje zwrócony widok
     */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ObjectDrawerItem folder = data[position];
		View listItem = convertView;
		
		LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
		listItem = inflater.inflate(folder.idLayout, parent, false);

        if (folder.action == "rankingView") {
            RelativeLayout _lay = (RelativeLayout) listItem.findViewById(R.id.relativeLayout);
            //ImageView _img = (ImageView) listItem.findViewById(R.id.imageView2);
            TextView _lp = (TextView) listItem.findViewById(R.id.numRanking);
            TextView _rankingText = (TextView) listItem.findViewById(R.id.rankingText);
            ImageView _avatar = (ImageView) listItem.findViewById(R.id.avatar);

            if (folder.lp == 1) {
                _lay.setBackgroundResource(R.drawable.zlo2);
                //_img.setImageResource(R.drawable.obr_1);
                _lp.setText(Html.fromHtml("<b>I</b>"));
                _rankingText.setTextColor(Color.BLACK);
            } else if (folder.lp == 2) {
                _lay.setBackgroundResource(R.drawable.sre2);
                //_img.setImageResource(R.drawable.obr_2);
                _lp.setText(Html.fromHtml("<b>II</b>"));
            } else if (folder.lp == 3) {
                _lay.setBackgroundResource(R.drawable.bra2);
                //_img.setImageResource(R.drawable.obr_3);
                _lp.setText(Html.fromHtml("<b>III</b>"));
            }

            _rankingText.setText(folder.nick + " " + folder.name);
            if(folder.avatar.equals("1"))
                _avatar.setImageResource(R.drawable.awatar_k1);
            else
                _avatar.setImageResource(R.drawable.awatar_k2);
        } else if (folder.action == "profilView") {
            TextView _nick = (TextView) listItem.findViewById(R.id.textView1);
            ImageView _img = (ImageView) listItem.findViewById(R.id.imageView1);
            int _avek = Integer.parseInt(folder.avatar);

            TypedArray array_avatar = listItem.getResources().obtainTypedArray(R.array.array_avek);
            _img.setImageResource(array_avatar.getResourceId(_avek, -1));
            _nick.setText(folder.nick);
        } else if (folder.action == "menuItem") {
            TextView textViewName = (TextView) listItem.findViewById(R.id.textViewName);
            textViewName.setText(folder.name);
        }
				
		return listItem;
	}
}