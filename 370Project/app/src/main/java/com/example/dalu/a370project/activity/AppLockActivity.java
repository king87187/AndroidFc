package com.example.dalu.a370project.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.LockSupport;


import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dalu.a370project.R;
import com.example.dalu.a370project.WangApplication;
import com.example.dalu.a370project.dao.AppInfo;
import com.example.dalu.a370project.dao.AppLockInfo;
import com.example.dalu.a370project.dao.BlackNumberInfo;
import com.example.dalu.a370project.engine.AppInfos;

import org.xutils.DbManager;
import org.xutils.db.sqlite.SqlInfo;
import org.xutils.ex.DbException;
import org.xutils.x;

public class AppLockActivity extends Activity {
	DbManager db;
	private Button bt_unlock,bt_lock;
	private LinearLayout ll_unlock,ll_lock;
	private TextView tv_unlock,tv_lock;
	private ListView lv_unlock,lv_lock;
	private List<AppInfo> mAppInfoList;
	private List<AppInfo> mLockList;
	private List<AppInfo> mUnLockList;
	private MylkAdpter mLockAdapter;
	private MylkAdpter mUnLockAdapter;

	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			//6.接收到消息,填充已加锁和未加锁的数据适配器
			mLockAdapter = new MylkAdpter(true);
			lv_lock.setAdapter(mLockAdapter);

			mUnLockAdapter = new MylkAdpter(false);
			lv_unlock.setAdapter(mUnLockAdapter);
		};
	};
	private TranslateAnimation mTranslateAnimation;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_lock);
		initUI();
		initData();
		initAnimation();
	}

	class MylkAdpter extends BaseAdapter{
		private final boolean islock;

		public MylkAdpter(boolean islock){
			this.islock = islock;
		}

		@Override
		public int getCount() {
			if(islock){
				tv_lock.setText("已加锁应用:"+mLockList.size());
				return mLockList.size();
			}else{
				tv_unlock.setText("未加锁应用:"+mUnLockList.size());
				return mUnLockList.size();
			}
		}

		@Override
		public AppInfo getItem(int position) {
			if(islock){
				return mLockList.get(position);
			}else {
				return  mUnLockList.get(position);
			}
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if(null==convertView){
				convertView = View.inflate(getApplicationContext(), R.layout.listview_islock_item, null);
				holder = new ViewHolder();
				holder.iv_icon= (ImageView) convertView.findViewById(R.id.iv_icon);
				holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
				holder.iv_lock = (ImageView) convertView.findViewById(R.id.iv_lock);
				convertView.setTag(holder);
			}else {
				holder = (ViewHolder) convertView.getTag();
			}
			final AppInfo appInfo = getItem(position);
			final View animationView = convertView;

			holder.iv_icon.setImageDrawable(appInfo.getIcon());
			holder.tv_name.setText(appInfo.getApkPackageName());
			if(islock){
				holder.iv_lock.setBackgroundResource(R.drawable.unlock);
			}else {
				holder.iv_lock.setBackgroundResource(R.drawable.lock);
			}
			holder.iv_lock.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					animationView.startAnimation(mTranslateAnimation);
					mTranslateAnimation.setAnimationListener(new Animation.AnimationListener() {
						@Override
						public void onAnimationStart(Animation animation) {

						}

						@Override
						public void onAnimationEnd(Animation animation) {
							if(islock){
								mLockList.remove(appInfo);
								mUnLockList.add(appInfo);
								db = x.getDb(((WangApplication)getApplicationContext()).getDaoConfig());
								try {
									AppLockInfo lk = db.selector(AppLockInfo.class).where("packname", "=", appInfo.getApkPackageName()).findFirst();

									db.delete(lk);
									context.getContentResolver().notifyChange(Uri.parse("content://applock/change"), null);
								} catch (DbException e) {
									e.printStackTrace();
								}
								mLockAdapter.notifyDataSetChanged();
							}else{
								mLockList.add(appInfo);
								mUnLockList.remove(appInfo);
								db = x.getDb(((WangApplication)getApplicationContext()).getDaoConfig());
								try {
									AppLockInfo lk = new AppLockInfo();
									lk.setPackname(appInfo.getApkPackageName());
									db.save(lk);
									context.getContentResolver().notifyChange(Uri.parse("content://applock/change"), null);
								} catch (DbException e) {
									e.printStackTrace();
								}
								mUnLockAdapter.notifyDataSetChanged();
							}
						}

						@Override
						public void onAnimationRepeat(Animation animation) {

						}
					});

				}
			});
			return convertView;
		}
		class ViewHolder{
			ImageView iv_icon;
			TextView tv_name;
			ImageView iv_lock;
		}
	}
	/**
	 *
	 * 区分已加锁和未加锁应用的集合
	 */
	Cursor cursor;
	private void initAnimation() {
		mTranslateAnimation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, 1,
				Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, 0);
		mTranslateAnimation.setDuration(500);
	}
	private void initData() {
		new Thread(){
			public void run() {
				//1.获取所有手机中的应用
				mAppInfoList = AppInfos.getAppInfos(getApplicationContext());
				Log.e("list",mAppInfoList.toString());
				//2.区分已加锁应用和未加锁应用
				mLockList = new ArrayList<AppInfo>();
				mUnLockList = new ArrayList<AppInfo>();

				//3.获取数据库中已加锁应用包名的的结合


				List<String> lockPackageList = new ArrayList<String>();
				lockPackageList = findAll();
					for (AppInfo appInfo : mAppInfoList) {
						//4,如果循环到的应用的包名,在数据库中,则说明是已加锁应用
						if(lockPackageList.contains(appInfo.getApkPackageName())){
							mLockList.add(appInfo);
						}else{
							mUnLockList.add(appInfo);
						}
					}
					Log.e("wann",mUnLockList.toString());
				/*else {
					for (AppInfo appInfo : mAppInfoList) {
						mUnLockList.add(appInfo);
					}
				}*/
				//5.告知主线程,可以使用维护的数据
				mHandler.sendEmptyMessage(0);
			}
		}.start();
	}

	//查询所有
	public List<String> findAll(){
		List<String> lockPackageList=new ArrayList<String>();;
		try {
			db = x.getDb(((WangApplication)getApplicationContext()).getDaoConfig());
			cursor = db.execQuery("select packname from applock");

			while(cursor.moveToNext()){
				lockPackageList.add(cursor.getString(0));
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}


		return lockPackageList;
	}

	private void initUI() {
		bt_unlock = (Button) findViewById(R.id.bt_unlock);
		bt_lock = (Button) findViewById(R.id.bt_lock);

		ll_unlock = (LinearLayout) findViewById(R.id.ll_unlock);
		ll_lock = (LinearLayout) findViewById(R.id.ll_lock);

		tv_unlock = (TextView) findViewById(R.id.tv_unlock);
		tv_lock = (TextView) findViewById(R.id.tv_lock);

		lv_unlock = (ListView) findViewById(R.id.lv_unlock);
		lv_lock = (ListView) findViewById(R.id.lv_lock);
		bt_unlock.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//1.已加锁列表隐藏,未加锁列表显示
				ll_lock.setVisibility(View.GONE);
				ll_unlock.setVisibility(View.VISIBLE);
				//2.未加锁变成深色图片,已加锁变成浅色图片
				bt_unlock.setBackgroundResource(R.drawable.tab_left_pressed);
				bt_lock.setBackgroundResource(R.drawable.tab_right_default);
			}
		});

		bt_lock.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//1.已加锁列表显示,未加锁列表隐藏
				ll_lock.setVisibility(View.VISIBLE);
				ll_unlock.setVisibility(View.GONE);
				//2.未加锁变成浅色图片,已加锁变成深色图片
				bt_unlock.setBackgroundResource(R.drawable.tab_left_default);
				bt_lock.setBackgroundResource(R.drawable.tab_right_pressed);
			}
		});
	}
}
