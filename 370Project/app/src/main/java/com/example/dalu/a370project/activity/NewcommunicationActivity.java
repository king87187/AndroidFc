package com.example.dalu.a370project.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dalu.a370project.Adapter.ListViewAdapter;
import com.example.dalu.a370project.R;
import com.example.dalu.a370project.WangApplication;
import com.example.dalu.a370project.customView.AutoListView;
import com.example.dalu.a370project.customView.XListView;
import com.example.dalu.a370project.dao.BlackNumberInfo;

import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DALU on 2016/5/17.
 */
public class NewcommunicationActivity extends Activity  implements AutoListView.OnRefreshListener,
        AutoListView.OnLoadListener {
    private ListView lv_balck;
    DbManager db;
    //BlackListAdapter adapter;
    List<BlackNumberInfo> persons;

    /**
     * 开始的位置
     */
    private int mStartIndex = 0;
    /**
     * 每页展示20条数据
     */
    private int maxCount = 20;

    /**
     * 一共有多少页面
     */
    private int totalPage;
    private int totalNumber;
    private XListView mListView;
    private ViewHolder holder;

    private AutoListView lstv;
    private ListViewAdapter adapter;
    private List<String> list = new ArrayList<String>();
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            List<String> result = (List<String>) msg.obj;
            switch (msg.what) {
                case AutoListView.REFRESH:
                    lstv.onRefreshComplete();
                    list.clear();
                    list.addAll(result);
                    break;
                case AutoListView.LOAD:
                    lstv.onLoadComplete();
                    list.addAll(result);
                    break;
            }
            lstv.setResultSize(result.size());
            adapter.notifyDataSetChanged();
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newcommunication);

        lstv = (AutoListView) findViewById(R.id.lv_black);
        db = x.getDb(((WangApplication)getApplicationContext()).getDaoConfig());
        persons = new ArrayList<BlackNumberInfo>();
        try {
            persons = db.selector(BlackNumberInfo.class).findAll();

        } catch (DbException e) {
            e.printStackTrace();
        }
        adapter = new ListViewAdapter(this, persons) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    holder = new ViewHolder();
                    convertView = LayoutInflater.from(NewcommunicationActivity.this).inflate(
                            R.layout.listview_item, null);
                    holder.text = (TextView) convertView.findViewById(R.id.text);
                    holder.content = (TextView) convertView.findViewById(R.id.content);
                    holder.imageView = (ImageView) convertView.findViewById(R.id.img);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                holder.text.setText(persons.get(position).getNumber());
                String mode = persons.get(position).getMode();
                if (mode.equals("1")) {
                    holder.content.setText("来电拦截+短信");
                } else if (mode.equals("2")) {
                    holder.content.setText("电话拦截");
                } else if (mode.equals("3")) {
                    holder.content.setText("短信拦截");
                }
                final BlackNumberInfo info = persons.get(position);

                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String number = info.getNumber();
                        int flag =0;
                        BlackNumberInfo person = null;
                        try {
                            //  person = db.selector(BlackNumberInfo.class).where("number", "=", number).findFirst();

                            flag= db.delete(BlackNumberInfo.class, WhereBuilder.b("number", "=", number));
                            Log.e("w",flag+"");
                        } catch (DbException e) {
                            e.printStackTrace();
                        }


                        if (flag!=0) {
                            Toast.makeText(NewcommunicationActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                            persons.remove(info);
                            //刷新界面
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(NewcommunicationActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
                return convertView;
            }
        };
        lstv.setAdapter(adapter);
        lstv.setOnRefreshListener(this);
        lstv.setOnLoadListener(this);
        initData();
    }


    private static class ViewHolder {
        TextView text;
        ImageView imageView;
        TextView content;
    }
    private void initData() {
        loadData(AutoListView.REFRESH);
    }

    private void loadData(final int what) {
        // 这里模拟从服务器获取数据
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(700);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg = handler.obtainMessage();
                msg.what = what;
                msg.obj = getData();
                handler.sendMessage(msg);
            }
        }).start();
    }

    @Override
    public void onRefresh() {
        loadData(AutoListView.REFRESH);
    }

    @Override
    public void onLoad() {
        loadData(AutoListView.LOAD);
    }

    // 测试数据
    public List<BlackNumberInfo> getData() {
        List<String> result = new ArrayList<String>();
        db = x.getDb(((WangApplication)getApplicationContext()).getDaoConfig());
        persons = new ArrayList<BlackNumberInfo>();
        try {
            persons = db.selector(BlackNumberInfo.class).findAll();

        } catch (DbException e) {
            e.printStackTrace();
        }
        return persons;
    }

    private  String translate(String mode){
        String str = "";
    if (mode.equals("1")) {
        str ="来电拦截+短信";
    } else if (mode.equals("2")) {
        str ="电话拦截";
    } else if (mode.equals("3")) {
        str ="短信拦截";
    }
        return str;
}



    public void addBlackNumber(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View dialog_view = View.inflate(this, R.layout.dialog_add_black_number, null);
        final EditText et_number = (EditText) dialog_view.findViewById(R.id.et_number);

        Button btn_ok = (Button) dialog_view.findViewById(R.id.btn_ok);

        Button btn_cancel = (Button) dialog_view.findViewById(R.id.btn_cancel);

        final CheckBox cb_phone = (CheckBox) dialog_view.findViewById(R.id.cb_phone);

        final CheckBox cb_sms = (CheckBox) dialog_view.findViewById(R.id.cb_sms);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_number = et_number.getText().toString().trim();
                if(TextUtils.isEmpty(str_number)){
                    Toast.makeText(NewcommunicationActivity.this,"请输入黑名单号码",Toast.LENGTH_SHORT).show();
                    return;
                }

                String mode = "";

                if(cb_phone.isChecked()&& cb_sms.isChecked()){
                    mode = "1";
                }else if(cb_phone.isChecked()){
                    mode = "2";
                }else if(cb_sms.isChecked()){
                    mode = "3";
                }else{
                    Toast.makeText(NewcommunicationActivity.this,"请勾选拦截模式",Toast.LENGTH_SHORT).show();
                    return;
                }
                BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
                blackNumberInfo.setNumber(str_number);
                blackNumberInfo.setMode(mode);
               // persons.add(0,blackNumberInfo);
                //把电话号码和拦截模式添加到数据库。
                try {
                    db.save(blackNumberInfo);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                /*if(adapter == null){
                    adapter = new BlackListAdapter(persons, communicationActivity.this);
                    lv_balck.setAdapter(adapter);
                }else{
                    adapter.notifyDataSetChanged();
                }*/

                dialog.dismiss();
            }
        });
        dialog.setView(dialog_view);
        dialog.show();
    }


}
