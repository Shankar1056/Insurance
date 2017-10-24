package com.bigappcompany.insurance.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.bigappcompany.insurance.R;
import com.bigappcompany.insurance.model.MenuListPojo;
import java.util.ArrayList;



public class MenuListAdapter extends BaseAdapter {
	private ArrayList<MenuListPojo> _mydata = new ArrayList<MenuListPojo>();
	private LayoutInflater _Inflater = null;
	private Context _context;
	
	public MenuListAdapter(Context c, ArrayList<MenuListPojo> _data) {
		this._mydata = _data;
		this._context = c;
		this._Inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return _mydata.size();
	}
	
	@Override
	public Object getItem(int position) {
		return position;
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final MyHolder holder;
		
		if (convertView == null) {
			holder = new MyHolder();
			convertView = this._Inflater.inflate(R.layout.menu_item_format, null);
			holder._name = (TextView) convertView.findViewById(R.id.tvName);

			convertView.setTag(holder);
		} else {
			holder = (MyHolder) convertView.getTag();
		}
		
		holder._name.setText(this._mydata.get(position).getName());
		return convertView;
	}
	
	class MyHolder {
		TextView _name;
	}
}
