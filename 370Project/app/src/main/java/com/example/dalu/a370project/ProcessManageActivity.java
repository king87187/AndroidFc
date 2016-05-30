package com.example.dalu.a370project;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dalu.a370project.dao.ProcessInfo;
import com.example.dalu.a370project.engine.ProcessInfoProvider;

import java.util.List;

/**
 * Created by DALU on 2016/5/24.
 */
public class ProcessManageActivity extends Activity {

    private List<ProcessInfo> processInfos;
    private List<ProcessInfo> userProcessInfos;
    private List<ProcessInfo> systemProcessInfos;
    private ListView listView;
    private TextView tv_processNum;
    private  TextView tv_ram;
    private int mProcessCount;
    private long mAvailSpace;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);
        initUI();
        initTitleData();
    }

    private void initUI() {
        listView = (ListView) findViewById(R.id.lv_process);
        tv_processNum = (TextView) findViewById(R.id.tv_processNum);
        tv_ram = (TextView) findViewById(R.id.tv_ram);
    }
    private void initData(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                processInfos = ProcessInfoProvider.getProcessInfo(getApplicationContext());
                for(ProcessInfo info :processInfos){
                    if(info.isSystem){
                        systemProcessInfos.add(info);
                    }else{
                        userProcessInfos.add(info);
                    }
                }
            }
        }.start();

    }
    private void initTitleData() {
        mProcessCount = ProcessInfoProvider.getProcessCount(this);
        tv_processNum.setText("进程总数:"+mProcessCount);

        //获取可用内存大小,并且格式化
        mAvailSpace = ProcessInfoProvider.getAvailSpace(this);
        String strAvailSpace = Formatter.formatFileSize(this, mAvailSpace);

        //总运行内存大小,并且格式化
        long totalSpace = ProcessInfoProvider.getTotalSpace(this);
        String mStrTotalSpace = Formatter.formatFileSize(this, totalSpace);

        tv_ram.setText("剩余/总共:"+strAvailSpace+"/"+mStrTotalSpace);
    }


    static class ViewHolder {
        ImageView iv_icon;
        TextView tv_title;
        TextView tv_memory_info;
        TextView tv_name;
        CheckBox checkBox;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
           /* ProcessManagerAdapter adapter = new ProcessManagerAdapter();
            listView.setAdapter(adapter);*/
        }
    };
    class  ProcessManagerAdapter extends BaseAdapter{
        @Override
        public int getViewTypeCount() {
            return super.getViewTypeCount()+1;
        }

        //指定索引指向的条目类型,条目类型状态码指定(0(复用系统),1)
        @Override
        public int getItemViewType(int position) {
            if(position == 0 || position == userProcessInfos.size()+1){
                //返回0,代表纯文本条目的状态码
                return 0;
            }else{
                //返回1,代表图片+文本条目状态码
                return 1;
            }
        }
        @Override
        public int getCount() {
            return userProcessInfos.size()+systemProcessInfos.size()+2;
        }

        @Override
        public Object getItem(int position) {
            if(position == 0 || position == userProcessInfos.size()+1){
                return null;
            }else{
                if(position<userProcessInfos.size()+1){
                    return userProcessInfos.get(position-1);
                }else{
                    //返回系统进程对应条目的对象
                    return systemProcessInfos.get(position - systemProcessInfos.size()-2);
                }
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            int type = getItemViewType(position);

            if(type == 0){
                //展示灰色纯文本条目
                ViewHolder holder = null;
                if(convertView == null){
                    convertView = View.inflate(getApplicationContext(), R.layout.item_process_manager, null);
                    holder = new ViewHolder();
                    holder.tv_title = (TextView)convertView.findViewById(R.id.tv_title);
                    convertView.setTag(holder);
                }else{
                    holder = (ViewHolder) convertView.getTag();
                }
                if(position == 0){
                    holder.tv_title.setText("用户进程("+userProcessInfos.size()+")");
                }else{
                    holder.tv_title.setText("系统进程("+systemProcessInfos.size()+")");
                }
                return convertView;
            }else{
                //展示图片+文字条目
                ViewHolder holder = null;
                if(convertView == null){
                    convertView = View.inflate(getApplicationContext(), R.layout.item_process_manager, null);
                    holder = new ViewHolder();
                    holder.iv_icon = (ImageView)convertView.findViewById(R.id.iv_icon);
                    holder.tv_name = (TextView)convertView.findViewById(R.id.tv_name);
                    holder.tv_memory_info = (TextView) convertView.findViewById(R.id.tv_memory_info);
                    holder.checkBox = (CheckBox) convertView.findViewById(R.id.cb_box);
                    convertView.setTag(holder);
                }else{
                    holder = (ViewHolder) convertView.getTag();
                }
                holder.iv_icon.setBackgroundDrawable(getItem(position).icon);
                holder.tv_name.setText(getItem(position).name);
                String strSize = Formatter.formatFileSize(getApplicationContext(), getItem(position).memSize);
                holder.tv_memory_info.setText(strSize);

                //本进程不能被选中,所以先将checkbox隐藏掉
                if(getItem(position).packageName.equals(getPackageName())){
                    holder.checkBox.setVisibility(View.GONE);
                }else{
                    holder.checkBox.setVisibility(View.VISIBLE);
                }

                holder.checkBox.setChecked(getItem(position).isCheck);

                return convertView;
        }
    }
}
