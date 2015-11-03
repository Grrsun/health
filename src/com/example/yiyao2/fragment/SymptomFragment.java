package com.example.yiyao2.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.utils.URIUtils;

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
import com.example.yiyao2.adapter.SymptomAdapter;
import com.example.yiyao2.bean.CookBean;
import com.example.yiyao2.bean.LoreBean;
import com.example.yiyao2.bean.SymptomBean;
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

public class SymptomFragment extends Fragment{

	private PullToRefreshListView lv_health;
	private int pageNum=1;
	private String url=UrlUtils.SYMPTOMURL+pageNum;
	private List<SymptomBean> totallist=new ArrayList<SymptomBean>();
	private SymptomAdapter symptomAdapter;
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
				// TODO Auto-generated method stub
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
				// TODO Auto-generated method stub
				Intent intent=new Intent(getActivity(),WebActivity.class);
				intent.putExtra("id", totallist.get(position-1).getId());
				intent.putExtra("frangmentNum", 4);
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
		url = UrlUtils.SYMPTOMURL + pageNum;
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
		// TODO Auto-generated method stub
		pageNum++;
		url=UrlUtils.SYMPTOMURL+pageNum;
		getData();
		
	}

	/**
	 * 从数据库获取数据
	 */
	protected void getDataFromDB() {
		Cursor cursor = sqLiteDatabase.query("symptom", null, null, null, null, null, null);
		if (cursor.getCount() != 0) {
			while (cursor.moveToNext()) {
				SymptomBean symptom = new SymptomBean();
				symptom.setName(cursor.getString(cursor.getColumnIndex("title")));
				symptom.setImg(cursor.getString(cursor.getColumnIndex("img")));
				symptom.setPlace(cursor.getString(cursor.getColumnIndex("place")));
				symptom.setCount(cursor.getString(cursor.getColumnIndex("count")));
				symptom.setId(cursor.getString(cursor.getColumnIndex("detailid")));
				totallist.add(symptom);
				lv_health.setAdapter(new SymptomAdapter(getContext(), totallist));
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
				final List<SymptomBean> list=JSONArray.parseArray(jsonArray.toString(), SymptomBean.class);
				totallist.addAll(list);
				if (pageNum==1) {
					symptomAdapter=new SymptomAdapter(getActivity(), totallist);
					lv_health.setAdapter(symptomAdapter);
				}else {
					symptomAdapter=new SymptomAdapter(getActivity(), totallist);
					symptomAdapter.notifyDataSetChanged();
				}
				//清空数据库表
				sqLiteDatabase.execSQL("delete from symptom");
				//将新数据存入
				for (int i = 0; i < totallist.size(); i++) {
					ContentValues values = new ContentValues();
					values.put("title", totallist.get(i).getName());
					values.put("img", totallist.get(i).getImg());
					values.put("count", totallist.get(i).getCount());
					values.put("detailid", totallist.get(i).getId());
					values.put("place", totallist.get(i).getPlace());
					sqLiteDatabase.insert("symptom", null, values);
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
