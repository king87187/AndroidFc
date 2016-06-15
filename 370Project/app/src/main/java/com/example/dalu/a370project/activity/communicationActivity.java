package com.example.dalu.a370project.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.dalu.a370project.R;
import com.example.dalu.a370project.WangApplication;
import com.example.dalu.a370project.customView.XListView;
import com.example.dalu.a370project.dao.BlackNumberInfo;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by DALU on 2016/5/17.
 */
public class communicationActivity extends Activity implements XListView.IXListViewListener {
    private ListView lv_balck;
    DbManager db;
    //BlackListAdapter adapter;
    List<BlackNumberInfo> persons;

    private XListView mListView;
    private SimpleAdapter mAdapter1;
    private Handler mHandler;
    private ArrayList<HashMap<String, Object>> dlist;
private ImageView imageView;
    @TargetApi(Build.VERSION_CODES.M)

    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication);
       /* lv_balck = (ListView) findViewById(R.id.lv_black);
        db = x.getDb(((WangApplication)getApplicationContext()).getDaoConfig());
        persons = new ArrayList<BlackNumberInfo>();
        try {
            persons = db.selector(BlackNumberInfo.class).findAll();

        } catch (DbException e) {
                e.printStackTrace();
            }
        adapter = new BlackListAdapter(persons,communicationActivity.this);
        lv_balck.setAdapter(adapter);*/
        /** 下拉刷新，上拉加载 */
        dlist = new ArrayList<HashMap<String, Object>>();
        mListView = (XListView) findViewById(R.id.lv_black);// 这个listview是在这个layout里面
        mListView.setPullLoadEnable(true);// 设置让它上拉，FALSE为不让上拉，便不加载更多数据
        mAdapter1 = new SimpleAdapter(communicationActivity.this, getData(),
                R.layout.scenic_item_list, new String[] { "name", "img",
                "content" }, new int[] { R.id.title, R.id.img,
                R.id.content });
        mListView.setAdapter(mAdapter1);
        mListView.setXListViewListener(this);
        mHandler = new Handler();
        imageView= (ImageView) mListView.findViewById(R.id.img);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             /*   String number = dlist.get()
                int flag =0;
                BlackNumberInfo person = null;
                try {
                    //  person = db.selector(BlackNumberInfo.class).where("number", "=", number).findFirst();

                    flag= db.delete(BlackNumberInfo.class, WhereBuilder.b("number", "=", number));
                    Log.e("w",flag+"");
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }*/}
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getApplicationContext(), "您点击了", Toast.LENGTH_LONG).show();
            }
        });
    }
    private ArrayList<HashMap<String, Object>> getData() {
        db = x.getDb(((WangApplication)getApplicationContext()).getDaoConfig());
        persons = new ArrayList<BlackNumberInfo>();
        try {
            persons = db.selector(BlackNumberInfo.class).findAll();

        } catch (DbException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < persons.size(); i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("name", persons.get(i).getNumber());
            map.put("content",translate(persons.get(i).getMode()));
            map.put("img", R.drawable.ic_delete_btn);
            dlist.add(map);
        }
        return dlist;
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
    /** 停止刷新， */
    private void onLoad() {
        mListView.stopRefresh();
        mListView.stopLoadMore();
        mListView.setRefreshTime("刚刚");
    }
    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getData();
                mListView.setAdapter(mAdapter1);
                onLoad();
            }
        }, 2000);
    }

    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                getData();
                mAdapter1.notifyDataSetChanged();
                onLoad();
            }
        }, 2000);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            this.finish();
        }
        return false;
    }


  /*  class BlackListAdapter extends MyBaseAdapter<BlackNumberInfo>{

        private BlackListAdapter(List lists, Context mContext) {
            super(lists, mContext);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView==null){
                convertView = View.inflate(communicationActivity.this, R.layout.item_blacklist, null);
                 holder = new ViewHolder();
                holder.tv_number = (TextView) convertView.findViewById(R.id.tv_number);
                holder.tv_mode = (TextView) convertView.findViewById(R.id.tv_describe);
                holder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_del);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv_number.setText(lists.get(position).getNumber());
            String mode = lists.get(position).getMode();
            if (mode.equals("1")) {
                holder.tv_mode.setText("来电拦截+短信");
            } else if (mode.equals("2")) {
                holder.tv_mode.setText("电话拦截");
            } else if (mode.equals("3")) {
                holder.tv_mode.setText("短信拦截");
            }
            final BlackNumberInfo info = lists.get(position);

            holder.iv_delete.setOnClickListener(new View.OnClickListener() {
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
                        Toast.makeText(communicationActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                        lists.remove(info);
                        //刷新界面
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(communicationActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                    }
                }

            });
                return convertView;
        }
    }
    class ViewHolder {
        TextView tv_number;
        TextView tv_mode;
        ImageView iv_delete;
    }*/


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
                    Toast.makeText(communicationActivity.this,"请输入黑名单号码",Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(communicationActivity.this,"请勾选拦截模式",Toast.LENGTH_SHORT).show();
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
