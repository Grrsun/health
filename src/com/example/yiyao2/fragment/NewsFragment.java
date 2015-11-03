package com.example.yiyao2.fragment;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.yiyao2.R;
import com.example.yiyao2.WebActivity;
import com.example.yiyao2.adapter.NewsAdapter;
import com.example.yiyao2.bean.NewsBean;
import com.example.yiyao2.utils.ConnectionChangeReceiver;
import com.example.yiyao2.utils.DB;
import com.example.yiyao2.utils.UrlUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.DialogInterface.OnClickListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class NewsFragment extends Fragment {

	private PullToRefreshListView lv_health;
	private int pageNum = 1;
	private String url = UrlUtils.NEWSURL + pageNum;
	private NewsAdapter newsAdapter;
	private List<NewsBean> totallist = new ArrayList<NewsBean>();
	private DB db;
	private SQLiteDatabase sqLiteDatabase;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_health, container, false);
		// 获取控件
		lv_health = (PullToRefreshListView) view.findViewById(R.id.lv_health);
		// 设置支持上拉下拉模式
		lv_health.setMode(Mode.BOTH);
		// 刷新监听
		lv_health.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (refreshView.isHeaderShown()) {
					// 下拉
					lv_health.getLoadingLayoutProxy(true, false).setPullLabel("下拉可以刷新哦~");
					lv_health.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新数据,请稍等哒~");
					lv_health.getLoadingLayoutProxy(true, false).setReleaseLabel("松开可以更新数据哦~");
					// 获取最新数据
					getNewData();
				} else {
					lv_health.getLoadingLayoutProxy(true, false).setPullLabel("上拉可以刷新哦~");
					lv_health.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新数据,请稍等哒~");
					lv_health.getLoadingLayoutProxy(true, false).setReleaseLabel("松开可以更新数据哦~");
					// 加载下一页数据
					getNextNetData();
				}

			}

			
		});

		db = new DB(getContext());
		sqLiteDatabase = db.getWritableDatabase();
		totallist.clear();
		getDataFromDB();

		lv_health.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				RelativeLayout rl_news = (RelativeLayout) view.findViewById(R.id.rl_news);
				Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.trans_toright);
				rl_news.startAnimation(animation);
				Intent intent = new Intent(getActivity(), WebActivity.class);
				intent.putExtra("id", totallist.get(position-1).getId());
				intent.putExtra("frangmentNum", 0);
				intent.putExtra("titleDis", totallist.get(position-1).getTitle());
				intent.putExtra("imgShare", totallist.get(position-1).getImg());
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);

			}
		});
		// getData();
		return view;
	}

	/**
	 * 获取最新数据
	 */
	private void getNewData() {
		//下拉刷新时清除列表 防止数据重复  并且pagNum=1以便获取最新数据
		totallist.clear();
		pageNum=1;
		url = UrlUtils.NEWSURL + pageNum;
		getNetData();
	}
	
	/**
	 * 加载下一页数据
	 */
	protected void getNextNetData() {
		// 页面加1
		pageNum++;
		url = UrlUtils.NEWSURL + pageNum;
		getNetData();
	}

	/**
	 * 获取网络数据
	 */
	private void getNetData() {
		/**
		 * 开子线程获取数据
		 */
		new Thread() {
			public void run() {
				// 从网络获取数据
				getData();
			};
		}.start();
	}

	/**
	 * 从数据库获取数据
	 */
	protected void getDataFromDB() {
		Cursor cursor = sqLiteDatabase.query("news", null, null, null, null, null, null);
		if (cursor.getCount() != 0) {
			while (cursor.moveToNext()) {
				NewsBean news = new NewsBean();
				news.setTitle(cursor.getString(cursor.getColumnIndex("title")));
				news.setImg(cursor.getString(cursor.getColumnIndex("img")));
				news.setTag(cursor.getString(cursor.getColumnIndex("tag")));
				news.setCount(cursor.getString(cursor.getColumnIndex("count")));
				news.setId(cursor.getString(cursor.getColumnIndex("detailid")));
				news.setTime(cursor.getString(cursor.getColumnIndex("time")));
				totallist.add(news);
				lv_health.setAdapter(new NewsAdapter(getContext(), totallist));
			}

			cursor.close();
		} else {
			getNetData();
			
		}

	}

	/**
	 * 从网络获取数据
	 */
	protected void getData() {
		RequestQueue mQueue = Volley.newRequestQueue(getContext());
		StringRequest mRequest = new StringRequest(url, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				// fast-json解析数据
				JSONObject jsonObject = JSONObject.parseObject(arg0);
				JSONArray jsonArray = jsonObject.getJSONArray("yi18");
				final List<NewsBean> list = JSONArray.parseArray(jsonArray.toString(), NewsBean.class);
				// 将刷新出的listview存入totallist	
				totallist.addAll(list);
				System.out.println("totalist" + totallist.toString());
				// 如果为第一页直接设置adapter
				if (pageNum == 1) {
					newsAdapter = new NewsAdapter(getActivity(), totallist);
					lv_health.setAdapter(newsAdapter);
				} else {
					// 不是第一页 通知数据变化即可
					newsAdapter = new NewsAdapter(getActivity(), totallist);
					newsAdapter.notifyDataSetChanged();
				}
				//清空数据库表
				sqLiteDatabase.execSQL("delete from news");
				//将新数据存入
				for (int i = 0; i < totallist.size(); i++) {
					ContentValues values = new ContentValues();
					values.put("title", totallist.get(i).getTitle());
					values.put("img", totallist.get(i).getImg());
					values.put("tag", totallist.get(i).getTag());
					values.put("count", totallist.get(i).getCount());
					values.put("detailid", totallist.get(i).getId());
					values.put("time", totallist.get(i).getTime());
					sqLiteDatabase.insert("news", null, values);
				}
				// 数据刷新完成
				lv_health.onRefreshComplete();

			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {

			}
		});
		mQueue.add(mRequest);

	}
}
