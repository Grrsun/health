package com.example.yiyao2;

import com.example.yiyao2.utils.ConnectionChangeReceiver;
import com.example.yiyao2.utils.DB;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.gesture.Prediction;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

public class SettingActivity extends ActionBarActivity implements OnClickListener{

	private Switch switch_checknet, switch_checkcache;
	//private TextView tv_clearcache,tv_cachesize, tv_update, tv_about;
	private TextView tv_about;
	private ConnectionChangeReceiver receiver;
	private DB db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		initView();
		
		SharedPreferences preferences=getSharedPreferences("user_settings", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor=preferences.edit();
		int check_values=preferences.getInt("check_values", 0);
		System.out.println(check_values);
		if (check_values==0) {
			//注册广播
			
			switch_checknet.setChecked(true);
		}else if (check_values==1) {
			switch_checknet.setChecked(false);
		}
	}

	private void initView() {

		/**
		 * SharedPreferences存储用户设置
		 */
		final SharedPreferences preferences=getSharedPreferences("user_settings", Context.MODE_PRIVATE);
		final Editor editor=preferences.edit();
		
		switch_checknet = (Switch) findViewById(R.id.switch_checknet);
		//switch_checkcache = (Switch) findViewById(R.id.switch_checkcache);
		switch_checknet.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			//网络状态广播
			IntentFilter filter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
			ConnectionChangeReceiver myRecevier = new ConnectionChangeReceiver();
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					//注册广播
					//if (myRecevier==null) {
						registerReceiver(myRecevier, filter);
					//}	
					//存储设置信息
					editor.putInt("check_values", 0);
					editor.commit();
				}else {
					//if (myRecevier!=null) {
						registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
					//}
					//存储设置信息
					editor.putInt("check_values", 1);
					editor.commit();
				}
				
			}
		});
//		switch_checkcache.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
		//tv_clearcache = (TextView) findViewById(R.id.tv_clearcache);
		//tv_update = (TextView) findViewById(R.id.tv_update);
		tv_about = (TextView) findViewById(R.id.tv_about);
		//tv_cachesize=(TextView)findViewById(R.id.tv_cachesize);
		
		//tv_clearcache.setOnClickListener(this);
		//tv_update.setOnClickListener(this);
		tv_about.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.tv_clearcache:
//
//			break;
//		case R.id.tv_update:
//			
//			break;
		case R.id.tv_about:
			AlertDialog.Builder builder=new AlertDialog.Builder(this);
			builder.setTitle("关于我们").setMessage("啦啦健康,您的个人健康助手! ").create().show();
			Animation animation=AnimationUtils.loadAnimation(getBaseContext(), R.anim.alpha_in);
			tv_about.startAnimation(animation);
			break;

		default:
			break;
		}

	}

	
}
