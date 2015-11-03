package com.example.yiyao2;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.yiyao2.adapter.MyTabAdapter;
import com.example.yiyao2.utils.ConnectionChangeReceiver;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends ActionBarActivity {

	// 声明相关变量
	private ViewPager mViewPager;
	private Toolbar toolbar;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private ListView lvLeftMenu;
	private SimpleAdapter mSimpleAdapter;
	private TabPageIndicator mTabPageIndicator;
	private MyTabAdapter mAdapter;
	private ConnectionChangeReceiver myRecevier;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.MyTheme);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// 友盟检测更新 默认为WIFi下检测
		UmengUpdateAgent.setUpdateOnlyWifi(true);
		UmengUpdateAgent.setDeltaUpdate(true);
		UmengUpdateAgent.update(this);
		initViews(); // 获取控件

		initToolBar();// 初始化ToolBar

		// 获取左侧滑动菜单ListView数据
		getLeftListData();

		SharedPreferences preferences = getSharedPreferences("user_settings", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		int check_values = preferences.getInt("check_values", 0);
		System.out.println(check_values);
		if (check_values == 0) {
			// 注册广播
			registerReceiver();
		} else if (check_values == 1) {
			// 取消注册广播
			if (myRecevier != null) {
				this.unregisterReceiver(myRecevier);
			}
		}

	}

	// @Override
	// protected void onDestroy() {
	// /**
	// * 注销广播
	// */
	// this.unregisterReceiver(myRecevier);
	// super.onDestroy();
	// }
	/**
	 * 初始化toolbar
	 */
	private void initToolBar() {
		toolbar.setTitle("啦啦健康");// 设置Toolbar标题
		toolbar.setTitleTextColor(Color.parseColor("#FFFFFF")); // 设置标题颜色
		setSupportActionBar(toolbar);
		toolbar.setOnMenuItemClickListener(onMenuItemClick);// 按钮监听
		getSupportActionBar().setHomeButtonEnabled(true); // 设置返回键可用
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// 创建返回键，并实现打开关或闭监听
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close) {
			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
			}
		};

		mDrawerToggle.syncState();
		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	/**
	 * 在toolbar上创建按钮
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// overflow 三个点
		// getMenuInflater().inflate(R.menu.main, menu);
		// MenuItem search = menu.add(0, 0, 0, null);
		// search.setIcon(R.drawable.search);
		// search.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * 按钮监听事件
	 */
	private Toolbar.OnMenuItemClickListener onMenuItemClick = new OnMenuItemClickListener() {

		@Override
		public boolean onMenuItemClick(MenuItem arg0) {

			return false;
		}
	};

	/**
	 * 获取左侧滑动菜单ListView数据
	 */
	private List<Map<String, Object>> getLeftListData() {
		List<Map<String, Object>> leftList = new ArrayList<Map<String, Object>>();
		Map<String, Object> leftMap = new HashMap<String, Object>();
		leftMap.put("img", null);
		leftMap.put("title", "");
		leftList.add(leftMap);
		// leftMap = new HashMap<String, Object>();
		// leftMap.put("img", R.drawable.location_arrows);
		// leftMap.put("title", "附近医院");
		// leftList.add(leftMap);
		leftMap = new HashMap<String, Object>();
		leftMap.put("img", R.drawable.collect);
		leftMap.put("title", "收藏夹");
		leftList.add(leftMap);
		// leftMap = new HashMap<String, Object>();
		// leftMap.put("img", R.drawable.feedback);
		// leftMap.put("title", "反馈");
		// leftList.add(leftMap);
		leftMap = new HashMap<String, Object>();
		leftMap.put("img", R.drawable.setting);
		leftMap.put("title", "设置");
		leftList.add(leftMap);
		leftMap = new HashMap<String, Object>();
		leftMap.put("img", R.drawable.update);
		leftMap.put("title", "检查新版本");
		leftList.add(leftMap);
		return leftList;
	}

	/**
	 * 获取控件
	 */
	private void initViews() {

		mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
		mTabPageIndicator = (TabPageIndicator) findViewById(R.id.id_indicator);
		mAdapter = new MyTabAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mAdapter);
		mTabPageIndicator.setViewPager(mViewPager, 0);

		toolbar = (Toolbar) findViewById(R.id.tl_custom);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_left);
		lvLeftMenu = (ListView) findViewById(R.id.lv_left_menu);
		// SimpleAdapter设置菜单列表
		mSimpleAdapter = new SimpleAdapter(this, getLeftListData(), R.layout.item_left, new String[] { "img", "title" },
				new int[] { R.id.iv_left, R.id.tv_left });
		lvLeftMenu.setAdapter(mSimpleAdapter);
		lvLeftMenu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
				// case 1:
				// Intent intenthospital=new
				// Intent(MainActivity.this,HospitalActivity.class);
				// startActivity(intenthospital);
				// break;
				case 1:
					Intent intentcollect = new Intent(MainActivity.this, CollectActivity.class);
					startActivity(intentcollect);
					overridePendingTransition(R.anim.activity_set, R.anim.alpha_out);
					break;
				case 2:
					Intent intentsettings = new Intent(MainActivity.this, SettingActivity.class);
					startActivity(intentsettings);
					overridePendingTransition(R.anim.activity_set, R.anim.alpha_out);
					break;
				case 3:
					// 检查版本更新
					UmengUpdateAgent.forceUpdate(MainActivity.this);
					UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {

						@Override
						public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
							switch (updateStatus) {
							case UpdateStatus.Yes: // has update
								UmengUpdateAgent.showUpdateDialog(MainActivity.this, updateInfo);
								break;
							case UpdateStatus.No: // has no update
								Toast.makeText(MainActivity.this, "已经是最新版本啦!", Toast.LENGTH_SHORT).show();
								break;
//							case UpdateStatus.NoneWifi: // none wifi
//								Toast.makeText(MainActivity.this, "没有wifi连接， 只在wifi下更新", Toast.LENGTH_SHORT).show();
//								break;
							case UpdateStatus.Timeout: // time out
								Toast.makeText(MainActivity.this, "网络超时", Toast.LENGTH_SHORT).show();
								break;

							}
						}
					});
					break;

				default:
					break;
				}

			}
		});
	}

	/**
	 * 注册广播
	 */
	private void registerReceiver() {
		IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		myRecevier = new ConnectionChangeReceiver();
		registerReceiver(myRecevier, filter);

	}

}
