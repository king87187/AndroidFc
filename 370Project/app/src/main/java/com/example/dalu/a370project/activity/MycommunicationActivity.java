package com.example.dalu.a370project.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dalu.a370project.Adapter.MyBaseAdapter;
import com.example.dalu.a370project.R;
import com.example.dalu.a370project.WangApplication;
import com.example.dalu.a370project.customView.XListView;
import com.example.dalu.a370project.dao.BlackNumberInfo;

import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by DALU on 2016/5/17.
 */
public class MycommunicationActivity extends Activity {
    private ListView lv_balck;
    DbManager db;
    BlackListAdapter adapter;
    List<BlackNumberInfo> persons;
    private int mStartIndex = 0;
    /**
     * 每页展示20条数据
     */
    private int maxCount = 10;

    /**
     * 一共有多少页面
     */
    private int totalPage;
    private int totalNumber;

    private XListView mListView;
    private SimpleAdapter mAdapter1;
    private Handler mHandler;
    private ArrayList<HashMap<String, Object>> dlist;
private ImageView imageView;



    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //ll_pb.setVisibility(View.INVISIBLE);
            if(adapter == null){
                adapter = new BlackListAdapter(persons, MycommunicationActivity.this);
                lv_balck.setAdapter(adapter);
            }else{
                adapter.notifyDataSetChanged();
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication);
    lv_balck = (ListView) findViewById(R.id.lv_black);
           getData();
        lv_balck.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        //获取到最后一条显示的数据
                        int lastVisiblePosition = lv_balck.getLastVisiblePosition();
                        System.out.println("lastVisiblePosition==========" + lastVisiblePosition+""+persons.size());
                        if(lastVisiblePosition ==9){
                            // 加载更多的数据。 更改加载数据的开始位置
                            mStartIndex += maxCount;
                            if (mStartIndex >= totalNumber) {
                                Toast.makeText(getApplicationContext(),
                                        "没有更多的数据了。", Toast.LENGTH_SHORT).show();
                                return;
                            }else {
                                System.out.println("lastVisiblePosition==========getshuju" );
                                getData();
                            }

                       }


                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });


    }
    private void getData() {

        db = x.getDb(((WangApplication)getApplicationContext()).getDaoConfig());
       // persons = new ArrayList<BlackNumberInfo>();
        List<BlackNumberInfo> lstnum = null;
        try {
            lstnum = db.selector(BlackNumberInfo.class).findAll();
            if(null!=lstnum) {
                totalNumber = lstnum.size();
            }else {
                totalNumber = 0;
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
             //分批加载数据
                    if (persons == null) {
                        try {
                            persons = db.selector(BlackNumberInfo.class).limit(maxCount)
                                    .offset(mStartIndex).findAll();
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        //把后面的数据。追加到blackNumberInfos集合里面。防止黑名单被覆盖
                        try {
                            Log.e("person",mStartIndex+"");
                            persons.addAll( db.selector(BlackNumberInfo.class).limit(maxCount)
                                    .offset(mStartIndex).findAll());
                           // Log.e("person",persons.size()+"");
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }

                    handler.sendEmptyMessage(0);


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



   class BlackListAdapter extends MyBaseAdapter<BlackNumberInfo> {

        private BlackListAdapter(List lists, Context mContext) {

                super(lists, mContext);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView==null){
                convertView = View.inflate(MycommunicationActivity.this, R.layout.item_blacklist, null);
                 holder = new ViewHolder();
                holder.tv_number = (TextView) convertView.findViewById(R.id.tv_pNum);
                holder.tv_mode = (TextView) convertView.findViewById(R.id.tv_content);
                holder.iv_delete = (ImageView) convertView.findViewById(R.id.img);
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
                        Toast.makeText(MycommunicationActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                        lists.remove(info);
                        //刷新界面
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(MycommunicationActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(MycommunicationActivity.this,"请输入黑名单号码",Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(MycommunicationActivity.this,"请勾选拦截模式",Toast.LENGTH_SHORT).show();
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
