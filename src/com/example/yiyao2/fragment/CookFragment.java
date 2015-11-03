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
import com.example.yiyao2.adapter.CookAdapter;
import com.example.yiyao2.adapter.LoreAdapter;
import com.example.yiyao2.bean.CookBean;
import com.example.yiyao2.bean.LoreBean;
import com.example.yiyao2.utils.DB;
import com.example.yiyao2.utils.UrlUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class CookFragment extends Fragment{

	private PullToRefreshListView lv_health;
	private int pageNum=1;
	private String url=UrlUtils.COOKURL+pageNum;
	private List<CookBean> totallist=new ArrayList<CookBean>();
	private CookAdapter cookAdapter;
	private DB db;
	private SQLiteDatabase sqLiteDatabase;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.fragment_health, container, false);
		lv_health=(PullToRefreshListView)view.findViewById(R.id.lv_health);
		lv_health.setMode(Mode.BOTH);
		lv_health.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {

				if (lv_health.isHeaderShown()) {
					lv_health.getLoadingLayoutProxy(true, false).setPullLabel("下拉可以刷新哦~");
					lv_health.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新数据,请稍等哒~");
					lv_health.getLoadingLayoutProxy(true, false).setReleaseLabel("松开可以更新数据哦~");
					getNewData();
				}else if (lv_health.isFooterShown()) {
					lv_health.getLoadingLayoutProxy(true, false).setPullLabel("上拉可以刷新哦~");
					lv_health.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新数据,请稍等哒~");
					lv_health.getLoadingLayoutProxy(true, false).setReleaseLabel("松开可以更新数据哦~");
					getNextNetData();
				}
			}
		});
		db=new DB(getContext());
		sqLiteDatabase = db.getWritableDatabase();
		totallist.clear();
		getDataFromDB();
		lv_health.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent=new Intent(getActivity(),WebActivity.class);
				intent.putExtra("id", totallist.get(position-1).getId());
				intent.putExtra("frangmentNum", 3);
				intent.putExtra("titleDis", totallist.get(position-1).getName());
				intent.putExtra("imgShare", totallist.get(position-1).getImg());
				startActivity(intent);
			}
		});
		return view;
	}
	
	/**
	 * 获取最新数据
	 */
	protected void getNewData() {
		totallist.clear();
		pageNum=1;
		url = UrlUtils.COOKURL + pageNum;
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
	
	protected void getNextNetData() {
		pageNum++;
		url=UrlUtils.COOKURL+pageNum;
		getData();
	}

	/**
	 * 从数据库获取数据
	 */
	protected void getDataFromDB() {
		Cursor cursor = sqLiteDatabase.query("cook", null, null, null, null, null, null);
		if (cursor.getCount() != 0) {
			while (cursor.moveToNext()) {
				CookBean cook = new CookBean();
				cook.setName(cursor.getString(cursor.getColumnIndex("title")));
				cook.setImg(cursor.getString(cursor.getColumnIndex("img")));
				cook.setTag(cursor.getString(cursor.getColumnIndex("tag")));
				cook.setCount(cursor.getString(cursor.getColumnIndex("count")));
				cook.setId(cursor.getString(cursor.getColumnIndex("detailid")));
				cook.setFood(cursor.getString(cursor.getColumnIndex("food")));
				totallist.add(cook);
				lv_health.setAdapter(new CookAdapter(getContext(), totallist));
			}

			cursor.close();
		} else {
			getNetData();
			
		}

	}

	protected void getData() {
		
		RequestQueue mQueue=Volley.newRequestQueue(getContext());
		StringRequest mRequest=new StringRequest(url, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				JSONObject jsonObject=JSONObject.parseObject(arg0);
				JSONArray jsonArray=jsonObject.getJSONArray("yi18");
				System.out.println("==1=="+jsonArray.toString());
				final List<CookBean> list=JSONArray.parseArray(jsonArray.toString(), CookBean.class);
				totallist.addAll(list);
				if (pageNum==1) {
					cookAdapter=new CookAdapter(getActivity(), totallist);
					lv_health.setAdapter(cookAdapter);
				}else {
					cookAdapter=new CookAdapter(getActivity(), totallist);
					cookAdapter.notifyDataSetChanged();
				}
				//清空数据库表
				sqLiteDatabase.execSQL("delete from cook");
				//将新数据存入
				for (int i = 0; i < totallist.size(); i++) {
					ContentValues values = new ContentValues();
					values.put("title", totallist.get(i).getName());
					values.put("img", totallist.get(i).getImg());
					values.put("tag", totallist.get(i).getTag());
					values.put("count", totallist.get(i).getCount());
					values.put("detailid", totallist.get(i).getId());
					values.put("food", totallist.get(i).getFood());
					sqLiteDatabase.insert("cook", null, values);
				}
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
