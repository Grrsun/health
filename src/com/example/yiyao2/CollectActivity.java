package com.example.yiyao2;

import java.util.ArrayList;
import java.util.List;

import com.example.yiyao2.adapter.CollectAdapter;
import com.example.yiyao2.bean.CollectBean;
import com.example.yiyao2.utils.DB;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * 收藏页面
 * @author GRR
 *
 */
public class CollectActivity extends ActionBarActivity{

	private ListView lv_collect;
	private Toolbar toolbar;
	private DB db;
	private SQLiteDatabase sqLiteDatabase;
	List<CollectBean> list=new ArrayList<CollectBean>();
	String detailid,fragment,collecttitle,collecttime,collectclass,collectimg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collect);
		db=new DB(getBaseContext());
		sqLiteDatabase=db.getReadableDatabase();
		
		//初始化View
		initView();
		
		System.out.println(sqLiteDatabase.toString());
	}

	/**
	 * 初始化View
	 */
	private void initView() {
		lv_collect=(ListView)findViewById(R.id.lv_collect);
		toolbar=(Toolbar)findViewById(R.id.tl_custom);
		getDataFromDB();
		CollectAdapter adapter=new CollectAdapter(getBaseContext(),list);
		lv_collect.setAdapter(adapter);
		lv_collect.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(CollectActivity.this, WebActivity.class);
				intent.putExtra("id", detailid);
				intent.putExtra("frangmentNum", fragment);
				intent.putExtra("titleDis", collecttitle);
				intent.putExtra("imgShare", collectimg);
				startActivity(intent);
				overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
				
			}
		});
	}
	
	private void getDataFromDB(){
		Cursor cursor=sqLiteDatabase.query("collect", null, null, null, null, null, null);
		System.out.println(cursor.toString());
		if (cursor.getCount()!=0) {
			while (cursor.moveToNext()) {
				fragment=cursor.getString(cursor.getColumnIndex("fragment"));
				collecttitle=cursor.getString(cursor.getColumnIndex("collecttitle"));
				collecttime=cursor.getString(cursor.getColumnIndex("collecttime"));
				detailid=cursor.getString(cursor.getColumnIndex("detailid"));
				collectimg=cursor.getString(cursor.getColumnIndex("collectimg"));
				CollectBean collectBean=new CollectBean();
				collectBean.setDetailid(detailid);
				collectBean.setFragment(fragment);
				collectBean.setCollecttitle(collecttitle);
				collectBean.setCollecttime(collecttime);
				list.add(collectBean);
			}
		}
	}
}
