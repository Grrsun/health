package com.example.yiyao2;

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
import com.example.yiyao2.adapter.HospitalAdapter;
import com.example.yiyao2.bean.HospitalBean;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.Toast;

public class HospitalActivity extends ActionBarActivity{

	//http://api.1ccf.com/hospital/location?x=113.6&&y=34.7
	private double x,y;
	private MyApplication myApplication;
	//private String hospitalurl="http://api.1ccf.com/hospital/location?x="+x+"&y="+y;
	private String hospitalurl="http://api.1ccf.com/hospital/list?page=1";
	private ListView lv_hospital;
	//private List<HospitalBean> list;
	private HospitalAdapter adapter;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//		x=myApplication.getLocationX();
//		y=myApplication.getLocationY();
//		x=113.6;
//		y=34.7;
		System.out.println(">>>>>"+x);
		System.out.println(">>>>>"+y);
		
		initView();
		
		getData();
	}


	private void getData() {
		new Thread(){
			public void run() {
				getNetData();
			};
		}.start();
		
	}


	protected void getNetData() {
		RequestQueue mQueue=Volley.newRequestQueue(this);
		StringRequest mRequest=new StringRequest(hospitalurl, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				JSONObject jsonObject = JSONObject.parseObject(arg0);
				JSONArray jsonArray = jsonObject.getJSONArray("yi18");
				List<HospitalBean> list=JSONArray.parseArray(jsonArray.toString(), HospitalBean.class);
				adapter=new HospitalAdapter(HospitalActivity.this, list);
				lv_hospital.setAdapter(adapter);
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				
			}
		});
		mQueue.add(mRequest);
	}


	private void initView() {
		
		lv_hospital=(ListView)findViewById(R.id.lv_hospital);
		
	}
	
}
