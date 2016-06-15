package com.example.dalu.a370project.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.dalu.a370project.R;
import com.example.dalu.a370project.engine.CommonnumDao;

import java.util.List;

/**
 * Created by DALU on 2016/6/14.
 */
public class CommonNumberQuery extends Activity {
    ExpandableListView elv_number;
    List<CommonnumDao.Group> list;
    MyCommonNumberAdapter madp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_number);
        initUI();
        initData();
    }

    private void initData() {
        CommonnumDao commonnumDao = new CommonnumDao();
        list = commonnumDao.getGroup();
        madp = new MyCommonNumberAdapter();
        elv_number.setAdapter(madp);
        elv_number.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                startCall(madp.getChild(groupPosition,childPosition).number);
                return false;
            }

        });

    }

    protected void startCall(String number) {
        //开启系统的打电话界面
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + number));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(intent);
    }

    private void initUI() {
        elv_number = (ExpandableListView) findViewById(R.id.elv_number);

    }
    class MyCommonNumberAdapter extends BaseExpandableListAdapter{

        @Override
        public int getGroupCount() {
            return list.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return list.get(groupPosition).clist.size();
        }

        @Override
        public CommonnumDao.Group getGroup(int groupPosition) {
            return list.get(groupPosition);
        }

        @Override
        public CommonnumDao.Child getChild(int groupPosition, int childPosition) {
            return list.get(groupPosition).clist.get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            TextView tv = new TextView(getApplicationContext());
            tv.setText("      "+getGroup(groupPosition).name);
            tv.setTextColor(Color.RED);
            tv.setTextSize(20);
            return tv;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
           View view = View.inflate(getApplicationContext(),R.layout.elv_child_item,null);
            TextView tvName = (TextView) view.findViewById(R.id.tv_name);
            TextView tvNum = (TextView) view.findViewById(R.id.tv_number);
            tvName.setText(getChild(groupPosition,childPosition).name);
            tvName.setTextColor(Color.BLACK);
            tvNum.setTextColor(Color.BLACK);
            tvNum.setText(getChild(groupPosition,childPosition).number);

            return view;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
