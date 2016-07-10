package com.example.dalu.a370project.Adapter;



import android.content.Context;
import android.widget.BaseAdapter;

import java.util.List;


/**
 * Created by DALU on 2016/5/17.
 */

    public abstract class MyBaseAdapter<T> extends BaseAdapter {


        public List<T> lists;

        public Context mContext;

        protected MyBaseAdapter(List<T> lists, Context mContext) {
            this.lists = lists;
            this.mContext = mContext;
        }
        protected MyBaseAdapter() {
        }
        @Override
        public int getCount() {
            if (lists!=null) {
                return lists.size();
            }
            else {
                return 0;
            }
        }

        @Override
        public Object getItem(int position) {
            return lists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
}
