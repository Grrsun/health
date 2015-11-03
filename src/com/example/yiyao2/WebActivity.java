package com.example.yiyao2;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.yiyao2.utils.DB;
import com.example.yiyao2.utils.UrlUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class WebActivity extends ActionBarActivity {

	private TextView tv_webtitle, tv_webcontent;
	private ImageView iv_web;
	private WebView wv_web;
	private String shareurl,imgUrl,id,titleDis,imgShare,title,name,image,img;
	private DB db;
	private SQLiteDatabase sqLiteDatabase;
	private MenuItem share,collect;
	private int frangmentNum;
	private Toolbar toolbar;
	
	private String arrUrlDetail[] = { UrlUtils.NEWSDETAIL, UrlUtils.LOREDETAIL, UrlUtils.DRUGDETAIL,
			UrlUtils.COOKDETAIL, UrlUtils.SYMPTOMDETAIL, UrlUtils.CHECKDETAIL };
	private String arrUrlDis[] = { UrlUtils.NEWSDIS, UrlUtils.LOREDIS, UrlUtils.DRUGDIS, UrlUtils.COOKDIS,
			UrlUtils.SYMPTOMDIS, UrlUtils.CHECKDIS };
	
	UMSocialService mController;
	String appId = "wx967daebe835fbeac";
	String appSecret = "5fa9e68ca3970e87a1f83e563c8dcbce";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);
		db = new DB(getBaseContext());
		sqLiteDatabase = db.getWritableDatabase();
		//初始化View
		initView();
		//初始化ToolBar
		initToolbar();
		//初始化Intent
		initIntent();
		//子线程加载数据
		new Thread() {
			public void run() {
				getDetailData();
			};
		}.start();
		//初始化分享面板
		initShare();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/** 使用SSO授权必须添加如下代码 */
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

	/**
	 * 初始化按钮
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		collect = menu.add(0, 0, 0, null);
		List<String> detaillist=new ArrayList<String>();
		Cursor cursor = sqLiteDatabase.query("collect", null, null, null, null, null, null);
		if (cursor.getCount()!=0) {
			while (cursor.moveToNext()) {
			String detailid=cursor.getString(cursor.getColumnIndex("detailid"));
			detaillist.add(detailid);
			}
			if (detaillist.contains(id)) {
				collect.setIcon(R.drawable.collect);
			}else {
				collect.setIcon(R.drawable.uncollect);
			}
		}else {
			collect.setIcon(R.drawable.like);
		}
		collect.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		share = menu.add(0, 1, 0, null);
		share.setIcon(R.drawable.share);
		share.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * 初始化ToolBar
	 */
	private void initToolbar() {
		toolbar = (Toolbar) findViewById(R.id.tl_custom);
		toolbar.setTitle("详情");
		toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
		setSupportActionBar(toolbar);
		toolbar.setNavigationIcon(null);
		
		// 按钮监听
		toolbar.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem arg0) {
				switch (arg0.getItemId()) {
				// 收藏
				case 0:
					List<String> detailids = new ArrayList<String>();
					Cursor cursor = sqLiteDatabase.query("collect", null, null, null, null, null, null);
					String detailid = null;
					String collecttime=getCurrentTime();
					if (cursor.getCount() != 0) {
						while (cursor.moveToNext()) {
							detailid = cursor.getString(cursor.getColumnIndex("detailid"));
							detailids.add(detailid);
						}
						System.out.println("==id==" + id);
						System.out.println("==detailids==" + detailids.toString());
						if (detailids.contains(id)) {
							sqLiteDatabase.delete("collect", "detailid=?", new String[] { String.valueOf(id) });
							collect.setIcon(R.drawable.uncollect);
							Toast.makeText(getBaseContext(), "取消收藏成功!", 0).show();
						} else {
							ContentValues values = new ContentValues();
							values.put("detailid", id);
							values.put("fragment", frangmentNum);
							values.put("collecttitle", titleDis);
							values.put("collecttime", collecttime);
							values.put("collectimg", imgShare);
							sqLiteDatabase.insert("collect", null, values);
							collect.setIcon(R.drawable.collect);
							Toast.makeText(getBaseContext(), "收藏成功!", 0).show();
						}
						
					} else {
						ContentValues values = new ContentValues();
						values.put("detailid", id);
						values.put("fragment", frangmentNum);
						values.put("collecttitle", titleDis);
						values.put("collecttime", collecttime);
						values.put("collectimg", imgShare);
						sqLiteDatabase.insert("collect", null, values);
						collect.setIcon(R.drawable.collect);
						Toast.makeText(getBaseContext(), "收藏成功!", 0).show();
					}
					detailids.clear();
					System.out.println("=="+detailids.toString());
					System.out.println("=="+cursor.toString());
					break;
				case 1:
					// 分享
					mController.getConfig().removePlatform(SHARE_MEDIA.RENREN, SHARE_MEDIA.DOUBAN);
					mController.openShare(WebActivity.this, false);
					break;
				default:
					break;
				}
				return false;
			}
		});
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	/**
	 * 获取当前时间
	 */
	private String getCurrentTime(){
		SimpleDateFormat format=new SimpleDateFormat("MM/dd  HH:mm:ss");
		Date currentData=new Date();
		String currentTime=format.format(currentData);
		return currentTime;
	}
	
	/**
	 * 获取Intent传来的值
	 */
	private void initIntent() {
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		frangmentNum = intent.getIntExtra("frangmentNum", 0);
		titleDis = intent.getStringExtra("titleDis");
		imgShare = intent.getStringExtra("imgShare");
		shareurl = arrUrlDetail[frangmentNum] + id;
	}

	/**
	 * 获取页面数据
	 */
	protected void getDetailData() {
		RequestQueue mQueue = Volley.newRequestQueue(this);
		StringRequest mRequest = new StringRequest(shareurl, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				System.out.println(arg0.toString());
				JSONObject jsonObject = JSONObject.parseObject(arg0);
				JSONObject jsonObject2 = jsonObject.getJSONObject("yi18");
				title = jsonObject2.getString("title");
				name = jsonObject2.getString("name");
				image = jsonObject2.getString("image");
				img = jsonObject2.getString("img");
				String message = jsonObject2.getString("message");
				String summary = jsonObject2.getString("summary");
				if (title != null) {
					tv_webtitle.setText(title);
				} else {
					tv_webtitle.setText(name);
				}
				if (image != null) {
					imgUrl = UrlUtils.IMGURL + image;
				} else {
					imgUrl = UrlUtils.IMGURL + img;
				}
				if (message != null) {
					// 此写法解决乱码
					wv_web.loadData(message, "text/html; charset=UTF-8", null);
				} else {
					wv_web.loadData(summary, "text/html; charset=UTF-8", null);
				}

				getImageData();
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {

			}
		});
		mQueue.add(mRequest);

	}

	/**
	 * 下载图片
	 */
	protected void getImageData() {
		ImageLoader.getInstance().displayImage(imgUrl, iv_web, new ImageLoadingListener() {

			@Override
			public void onLoadingStarted(String arg0, View arg1) {

			}

			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {

			}

			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
				iv_web.setImageBitmap(arg2);
			}

			@Override
			public void onLoadingCancelled(String arg0, View arg1) {

			}
		});

	}

	/**
	 * 初始化View
	 */
	private void initView() {
		tv_webtitle = (TextView) findViewById(R.id.tv_webtitle);
		wv_web = (WebView) findViewById(R.id.wv_web);
		iv_web = (ImageView) findViewById(R.id.iv_web);
		Animation animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.alpha_in);
		wv_web.startAnimation(animation);
		iv_web.startAnimation(animation);
		
		

	}

	/**
	 * 初始化分享面板
	 */
	private void initShare() {
		// 首先在您的Activity中添加如下成员变量
		mController = UMServiceFactory.getUMSocialService("com.umeng.share");
		// 设置分享内容
		mController.setShareContent("友盟社会化组件（SDK）让移动应用快速整合社交分享功能，http://www.umeng.com/social");
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(WebActivity.this, appId, appSecret);
		wxHandler.addToSocialSDK();
		// 添加微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(WebActivity.this, appId, appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
		// 设置微信好友分享内容
		WeiXinShareContent weixinContent = new WeiXinShareContent();
		// 设置分享文字
		String shareurl = arrUrlDis[frangmentNum] + id;
		String shareImgurl = UrlUtils.IMGURL + imgShare;
		weixinContent.setShareContent(titleDis);
		// 设置title
		weixinContent.setTitle("医药吧精彩分享");
		// 设置分享内容跳转URL
		weixinContent.setTargetUrl(shareurl);
		// 设置分享图片
		// weixinContent.setShareImage(localImage);
		mController.setShareMedia(weixinContent);

		// 设置微信朋友圈分享内容
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent(titleDis);
		// 设置朋友圈title
		circleMedia.setTitle("医药吧精彩分享");
		// circleMedia.setShareImage(localImage);
		circleMedia.setTargetUrl(shareurl);
		mController.setShareMedia(circleMedia);
		// 注册微信回调
		SnsPostListener mSnsPostListener = new SnsPostListener() {

			@Override
			public void onStart() {

			}

			@Override
			public void onComplete(SHARE_MEDIA platform, int stCode, SocializeEntity entity) {
				if (stCode == 200) {
					Toast.makeText(WebActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(WebActivity.this, "分享失败 : error code : " + stCode, Toast.LENGTH_SHORT).show();
				}
			}
		};
		mController.registerListener(mSnsPostListener);

		// 参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(WebActivity.this, "100424468",
				"c7394704798a158208a74ab60104f0ba");
		qqSsoHandler.addToSocialSDK();
		QQShareContent qqShareContent = new QQShareContent();
		// 设置分享文字
		qqShareContent.setShareContent(titleDis);
		// 设置分享title
		qqShareContent.setTitle("医药吧精彩分享");
		// 设置分享图片
		qqShareContent.setShareImage(new UMImage(WebActivity.this, shareImgurl));
		// 设置点击分享内容的跳转链接
		qqShareContent.setTargetUrl(shareurl);
		mController.setShareMedia(qqShareContent);

		// 参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(WebActivity.this, "100424468",
				"c7394704798a158208a74ab60104f0ba");
		qZoneSsoHandler.addToSocialSDK();
		QZoneShareContent qzone = new QZoneShareContent();
		// 设置分享文字
		qzone.setShareContent(titleDis);
		// 设置点击消息的跳转URL
		qzone.setTargetUrl(shareurl);
		// 设置分享内容的标题
		qzone.setTitle("医药吧精彩分享");
		// 设置分享图片
		qzone.setShareImage(new UMImage(WebActivity.this, shareImgurl));
		mController.setShareMedia(qzone);
	}
}
