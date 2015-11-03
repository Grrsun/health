package com.example.yiyao2.utils;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.widget.Toast;
/**
 * 网络连接状态改变
 * @author GRR
 *
 */
public class ConnectionChangeReceiver extends BroadcastReceiver{

	
	public static final String ACTION="com.example.yiyao2.utils.intent.action.ConnectionChangeReceiver";
	
	@Override
	public void onReceive(final Context context, Intent intent) {
		ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobile=connectivityManager.getNetworkInfo(connectivityManager.TYPE_MOBILE);
		NetworkInfo wifi=connectivityManager.getNetworkInfo(connectivityManager.TYPE_WIFI);
		System.out.println("wifi=="+wifi.toString());
		if (wifi.isConnected()) {
			Toast.makeText(context, "当前为WIFI网络!", 0).show();	
		}
		if (!wifi.isConnected()&&mobile.isConnected()) {
			Toast.makeText(context, "当前为手机网络!", 0).show();
			AlertDialog.Builder builder=new AlertDialog.Builder(context);
			builder.setTitle("流量提示:");
			builder.setMessage("当前使用的手机流量,伤不起啊! 是否切换为Wifi? 土豪请随意.");
			builder.setNegativeButton("立即切换", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Toast.makeText(context, "正在切换为Wifi网络...",Toast.LENGTH_LONG).show();
					new Thread(){
						public void run() {
							WifiManager wifiManager=(WifiManager)context.getSystemService(Context.WIFI_SERVICE);
							wifiManager.setWifiEnabled(true);
						};
					}.start();
					
				}

				private WifiManager getSystemService(String wifiService) {
					return null;
				}
			});
			builder.setPositiveButton("切个毛线", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					
				}
			});
			builder.create();
			builder.show();
		}
		if(!wifi.isConnected()&&!mobile.isConnected()) {
			Toast.makeText(context, "当前无网络!", 3000).show();
			AlertDialog.Builder builder=new AlertDialog.Builder(context);
			builder.setTitle("网络错误:");
			builder.setMessage("网络出问题了哦! 检查一下吧");
			builder.setPositiveButton("检查网络", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
				//进入网络设置
				context.startActivity(new Intent(Settings.ACTION_SETTINGS));
					
				}
			});
			builder.create();
			builder.show();
		}
	}

	

}
