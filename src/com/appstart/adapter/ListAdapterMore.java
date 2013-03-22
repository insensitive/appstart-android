package com.appstart.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appstart.R;
import com.appstart.utility.Util;

public class ListAdapterMore extends BaseAdapter {

	private Activity activity;
	// private ArrayList&lt;HashMap&lt;String, String&gt;&gt; data;
	private static ArrayList data;
	private static ArrayList icon_name;
	private static LayoutInflater inflater = null;

	public ListAdapterMore(Activity a, ArrayList b, ArrayList c) {
		activity = a;
		data = b;
		icon_name = c;

		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	public int getCount() {
		return data.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		
		convertView=null;
		if (convertView == null)
			vi = inflater.inflate(R.layout.lay_row_more, null);

		TextView title = (TextView) vi.findViewById(R.id.txt_tab_more); // title
		String song = data.get(position).toString();
		// Setting all values in listview
		title.setText(song);

		

		if(!data.get(position).toString().equalsIgnoreCase("Settings"))
		{
			((ImageView) vi.findViewById(R.id.iv_icon_more)).setImageBitmap(Util
					.ImgBitFromFile(icon_name.get(position).toString()));
		}else
		{
			
		}
		// title.setTextColor(Util
		// .Colorcode());
		// title.setTypeface(Util.font(activity,
		// Constant.FONT_TYPE));
		// title.setTextSize(
		// TypedValue.COMPLEX_UNIT_DIP,
		// Float.parseFloat(Constant.FONT_SIZE));

		return vi;

	}

}