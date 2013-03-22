package com.appstart.adapter;

import java.util.ArrayList;

import com.appstart.R;
import com.appstart.R.id;
import com.appstart.R.layout;
import com.appstart.utility.Constant;
import com.appstart.utility.Util;

import android.app.Activity;
import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class ListAdapterSocialMedia extends BaseAdapter {

	private Activity activity;
	// private ArrayList&lt;HashMap&lt;String, String&gt;&gt; data;
	private static ArrayList data,contactid,icon;
	private static LayoutInflater inflater = null;

	public ListAdapterSocialMedia(Activity a, ArrayList b,ArrayList c,ArrayList icon_path) {
		activity = a;
		data = b;
		contactid=c;
		icon=icon_path;
		
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
		if (convertView == null)
			vi = inflater.inflate(R.layout.row_socialmedia, null);
						
		
		
		ImageView img = (ImageView) vi.findViewById(R.id.iv_icon_social);
		img.setImageBitmap(Util.ImgBitFromFile(icon.get(position).toString()));
		TextView title = (TextView) vi.findViewById(R.id.txt_ttlsm_row); // title
		String song = data.get(position).toString();
		// Setting all values in listview
		title.setText(song);

		
//		title.setTextColor(Util
//				.Colorcode());
//		title.setTypeface(Util.font(activity,
//				Constant.FONT_TYPE));
//		title.setTextSize(
//				TypedValue.COMPLEX_UNIT_DIP,
//				Float.parseFloat(Constant.FONT_SIZE));
		
		return vi;

	}

}