package com.example.dalu.a370project.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dalu.a370project.R;
import com.example.dalu.a370project.dao.ProcessInfo;
import com.example.dalu.a370project.engine.ProcessInfoProvider;
import com.example.dalu.a370project.utils.ConstantValue;
import com.example.dalu.a370project.utils.SpUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DALU on 2016/5/24.
 */
public class ProcessManageActivity extends Activity implements View.OnClickListener {

    private List<ProcessInfo> processInfos;
    private ArrayList<ProcessInfo> userProcessInfos;
    private ArrayList<ProcessInfo> systemProcessInfos;
    private ListView listView;
    private TextView tv_processNum;
    private TextView tv_ram;
    private TextView tv_des ;
    private int mProcessCount;
    String mStrTotalSpace;
    private long mAvailSpace;
    private long mTotalspace;
    String mStrTotalSpace;
    ProcessManagerAdapter adapter;
    private Button bt_select_all,bt_select_reverse,bt_clear,bt_setting;
    private ProcessInfo mProcessInfo;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            adapter = new ProcessManagerAdapter();
            listView.setAdapter(adapter);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);
        initData();
        initUI();
        initTitleData();

    }

    private void initUI() {
        listView = (ListView) findViewById(R.id.lv_process);
        tv_processNum = (TextView) findViewById(R.id.tv_processNum);
        tv_ram = (TextView) findViewById(R.id.tv_ram);
        tv_des = (TextView) findViewById(R.id.tv_des);

        bt_select_all = (Button) findViewById(R.id.btn_all);
        bt_select_reverse = (Button) findViewById(R.id.btn_reall);
        bt_clear = (Button)  findViewById(R.id.btn_clean);
        bt_setting = (Button) findViewById(R.id.btn_setting);

        bt_select_all.setOnClickListener(this);
        bt_select_reverse.setOnClickListener(this);
        bt_clear.setOnClickListener(this);
        bt_setting.setOnClickListener(this);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                //滚动过程中调用方法
                //AbsListView中view就是listView对象
                //firstVisibleItem第一个可见条目索引值
                //visibleItemCount当前一个屏幕的可见条目数
                //总共条目总数
                if(userProcessInfos!=null && systemProcessInfos!=null){
                    if(firstVisibleItem>=userProcessInfos.size()+1){
                        //滚动到了系统条目
                        tv_des.setText("系统进程("+systemProcessInfos.size()+")");
                    }else{
                        //滚动到了用户应用条目
                        tv_des.setText("用户进程("+userProcessInfos.size()+")");
                    }
                }

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //view选中条目指向的view对象
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                if(position == 0 || position == userProcessInfos.size()+1){
                    return;
                }else{
                    if(position<userProcessInfos.size()+1){
                        mProcessInfo = userProcessInfos.get(position-1);
                    }else{
                        //返回系统应用对应条目的对象
                        mProcessInfo = systemProcessInfos.get(position - userProcessInfos.size()-2);
                    }
                    if(mProcessInfo!=null){
                        if(!mProcessInfo.packageName.equals(getPackageName())){
                            //选中条目指向的对象和本应用的包名不一致,才需要去状态取反和设置单选框状态
                            //状态取反
                            mProcessInfo.isCheck = !mProcessInfo.isCheck;
                            //checkbox显示状态切换
                            //通过选中条目的view对象,findViewById找到此条目指向的cb_box,然后切换其状态
                            CheckBox cb_box = (CheckBox) view.findViewById(R.id.cb_box);
                            cb_box.setChecked(mProcessInfo.isCheck);
                        }
                    }
                }
            }
        });

    }

    private void initData() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                processInfos = ProcessInfoProvider.getProcessInfo(getApplicationContext());
                userProcessInfos = new ArrayList<ProcessInfo>();
                systemProcessInfos = new ArrayList<ProcessInfo>();
                for (ProcessInfo info : processInfos) {
                    if (info.isSystem) {
                        systemProcessInfos.add(info);
                    } else {
                        userProcessInfos.add(info);
                    }
                }
                mHandler.sendEmptyMessage(0);
            };
        }.start();


    }

    private void initTitleData() {
        mProcessCount = ProcessInfoProvider.getProcessCount(this);
        tv_processNum.setText("进程总数:" + mProcessCount);

        //获取可用内存大小,并且格式化
        mAvailSpace = ProcessInfoProvider.getAvailSpace(this);
        String strAvailSpace = Formatter.formatFileSize(this, mAvailSpace);

        //总运行内存大小,并且格式化
        long totalSpace = ProcessInfoProvider.getTotalSpace(this);

        mStrTotalSpace = Formatter.formatFileSize(this, totalSpace);

        tv_ram.setText("剩余/总共:" + strAvailSpace + "/" + mStrTotalSpace);
    }


    static class ViewHolder {
        ImageView iv_icon;
        TextView tv_title;
        TextView tv_memory_info;
        TextView tv_name;
        CheckBox checkBox;
    }



    class ProcessManagerAdapter extends BaseAdapter {
        @Override
        public int getViewTypeCount() {
            return super.getViewTypeCount() + 1;
        }

        //指定索引指向的条目类型,条目类型状态码指定(0(复用系统),1)
        @Override
        public int getItemViewType(int position) {
            if (position == 0 || position == userProcessInfos.size() + 1) {
                //返回0,代表纯文本条目的状态码
                return 0;
            } else {
                //返回1,代表图片+文本条目状态码
                return 1;
            }
        }

        @Override
        public int getCount() {
            boolean aBoolean = SpUtil.getBoolean(ProcessManageActivity.this, ConstantValue.SHOW_SYSTEM, false);
            if (aBoolean){
                return userProcessInfos.size() + systemProcessInfos.size() + 2;
            }
            else {
                return userProcessInfos.size() + 1;
            }
        }

        @Override
        public ProcessInfo getItem(int position) {
            if (position == 0 || position == userProcessInfos.size() + 1) {
                return null;
            } else {
                if (position < userProcessInfos.size() + 1) {
                    return userProcessInfos.get(position - 1);
                } else {
                    //返回系统进程对应条目的对象
                    return systemProcessInfos.get(position - userProcessInfos.size() - 2);
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

            if (type == 0) {
                //展示灰色纯文本条目
               /* ViewHolder holder = null;
                if (convertView == null) {
                    //convertView = new TextView(ProcessManageActivity.this);
                    convertView = View.inflate(getApplicationContext(), R.layout.listview_app_item_title, null);
                    holder = new ViewHolder();
                    holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                if (position == 0) {
                    holder.tv_title.setText("用户进程(" + userProcessInfos.size() + ")");
                } else {
                    holder.tv_title.setText();
                }*/
                TextView textView = new TextView(ProcessManageActivity.this);
                textView.setTextColor(Color.WHITE);

                textView.setBackgroundColor(Color.GRAY);
                if(position == 0) {


                    textView.setText("用户程序(" + userProcessInfos.size() + ")");
                    convertView = textView;

                }else{
                    textView.setText("系统进程(" + systemProcessInfos.size() + ")");
                    convertView = textView;
                }
                return convertView;
            } else {
                //展示图片+文字条目
                ViewHolder holder = null;
                if (convertView == null) {
                    convertView = View.inflate(getApplicationContext(), R.layout.item_process_manager, null);
                    convertView.setBackgroundColor(Color.BLACK);
                    holder = new ViewHolder();
                    holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                    holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                    holder.tv_name.setTextColor(Color.BLUE);
                    holder.tv_memory_info = (TextView) convertView.findViewById(R.id.tv_memory_info);
                    holder.tv_memory_info.setTextColor(Color.RED);
                    holder.checkBox = (CheckBox) convertView.findViewById(R.id.cb_box);

                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                holder.iv_icon.setBackgroundDrawable(getItem(position).icon);
                holder.tv_name.setText(getItem(position).name);
                String strSize = Formatter.formatFileSize(getApplicationContext(), getItem(position).memSize);
                holder.tv_memory_info.setText(strSize);

                //本进程不能被选中,所以先将checkbox隐藏掉
                if (getItem(position).packageName.equals(getPackageName())) {
                    holder.checkBox.setVisibility(View.GONE);
                } else {
                    holder.checkBox.setVisibility(View.VISIBLE);
                }

                holder.checkBox.setChecked(getItem(position).isCheck);

                return convertView;
            }
        }
    }//adpater over




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_all:
                selectAll();
                break;
            case R.id.btn_reall:
                selectReverse();
                break;
            case R.id.btn_clean:
                clearAll();
                break;
            case R.id.btn_setting:
                processSetting();
                break;
        }
    }

    private void processSetting() {
        Intent intent = new Intent(this,ProcessSetting.class);
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (0==requestCode){
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 清理选中进程
     */
    private void clearAll() {
        //1,获取选中进程
        //2,创建一个记录需要杀死的进程的集合
        List<ProcessInfo> killProcessList = new ArrayList<ProcessInfo>();
        for(ProcessInfo processInfo:userProcessInfos){
            if(processInfo.getPackageName().equals(getPackageName())){
                continue;
            }
            if(processInfo.isCheck){
                //不能在集合循环过程中去移除集合中的对象
//				mCustomerList.remove(processInfo);
                //3,记录需要杀死的用户进程
                killProcessList.add(processInfo);
            }
        }

        for(ProcessInfo processInfo:systemProcessInfos){
            if(processInfo.isCheck){
                //4,记录需要杀死的系统进程
                killProcessList.add(processInfo);
            }
        }
        //5,循环遍历killProcessList,然后去移除mCustomerList和mSystemList中的对象
        long totalReleaseSpace = 0;
        for (ProcessInfo processInfo : killProcessList) {
            //6,判断当前进程在那个集合中,从所在集合中移除
            if(userProcessInfos.contains(processInfo)){
                userProcessInfos.remove(processInfo);
            }

            if(systemProcessInfos.contains(processInfo)){
                systemProcessInfos.remove(processInfo);
            }
            //7,杀死记录在killProcessList中的进程
            ProcessInfoProvider.killProcess(this,processInfo);

            //记录释放空间的总大小
            totalReleaseSpace += processInfo.memSize;
        }
        //8,在集合改变后,需要通知数据适配器刷新
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }
        //9,进程总数的更新
        mProcessCount -= killProcessList.size();
        //10,更新可用剩余空间(释放空间+原有剩余空间 == 当前剩余空间)
        mAvailSpace += totalReleaseSpace;
        //11,根据进程总数和剩余空间大小
        tv_processNum.setText("进程总数:"+mProcessCount);
        tv_ram.setText("剩余/总共"+Formatter.formatFileSize(this, mAvailSpace)+"/"+mStrTotalSpace);
        //12,通过吐司告知用户,释放了多少空间,杀死了几个进程,
        String totalRelease =   Formatter.formatFileSize(this, totalReleaseSpace);
//		ToastUtil.show(getApplicationContext(), );
        Toast.makeText(this,"杀死了"+killProcessList.size()+"个进程,释放了"+ totalRelease +"空间",
                Toast.LENGTH_SHORT).show();
//		jni  java--c   c---java
        //占位符指定数据%d代表整数占位符,%s代表字符串占位符
       /* Toast.show(getApplicationContext(),
                String.format("杀死了%d进程,释放了%s空间", killProcessList.size(),totalRelease));*/
    }

    private void selectReverse() {
        //1,将所有的集合中的对象上isCheck字段取反
        for(ProcessInfo processInfo:userProcessInfos){
            if(processInfo.getPackageName().equals(getPackageName())){
                continue;
            }
            processInfo.isCheck = !processInfo.isCheck;
        }
        for(ProcessInfo processInfo:systemProcessInfos){
            processInfo.isCheck = !processInfo.isCheck;
        }
        //2,通知数据适配器刷新
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }
    }

    private void selectAll() {
        //1,将所有的集合中的对象上isCheck字段设置为true,代表全选,排除当前应用
        for(ProcessInfo processInfo:userProcessInfos){
            if(processInfo.getPackageName().equals(getPackageName())){
                continue;
            }
            processInfo.isCheck = true;
        }
        for(ProcessInfo processInfo:systemProcessInfos){
            processInfo.isCheck = true;
        }
        //2,通知数据适配器刷新
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }
    }
}
