package com.example.dalu.a370project.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dalu.a370project.R;

import java.util.List;

/**
 * @author SunnyCoffee
 * @date 2014-2-2
 * @version 1.0
 * @desc 适配器
 * 
 */
public abstract  class ListViewAdapter<T> extends BaseAdapter {

	private ViewHolder holder;
	public List<T> list;
	//public List<T> lists;
	private Context context;

	public ListViewAdapter(Context context, List<T> list) {
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}



	private static class ViewHolder {
		TextView text;
		ImageView imageView;
		TextView content;
	}

}
