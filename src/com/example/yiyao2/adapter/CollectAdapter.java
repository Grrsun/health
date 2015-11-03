package com.example.yiyao2.adapter;

import java.util.List;

import com.example.yiyao2.R;
import com.example.yiyao2.bean.CollectBean;

import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CollectAdapter extends BaseAdapter {

	private List<CollectBean> list;
	private Context context;

	public CollectAdapter(Context context, List<CollectBean> list) {

		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView==null) {
			convertView=LayoutInflater.from(context).inflate(R.layout.item_collect, null);
			holder=new ViewHolder();
			holder.tv_collectclass=(TextView) convertView.findViewById(R.id.tv_collectclass);
			holder.tv_collecttime=(TextView) convertView.findViewById(R.id.tv_collecttime);
			holder.tv_collecttitle=(TextView) convertView.findViewById(R.id.tv_collecttitle);
			convertView.setTag(holder);
		}else {
			holder=(ViewHolder) convertView.getTag();
		}
		
		CollectBean collectBean=list.get(position);
		String fragment=collectBean.getFragment();
		if (fragment.equals("0")) {
			holder.tv_collectclass.setText("[健康资讯]");
		}
		if (fragment.equals("1")) {
			holder.tv_collectclass.setText("[健康知识]");
		}
		if (fragment.equals("2")) {
			holder.tv_collectclass.setText("[药品直达]");
		}
		if (fragment.equals("3")) {
			holder.tv_collectclass.setText("[健康食谱]");
		}
		if (fragment.equals("4")) {
			holder.tv_collectclass.setText("[症状查找]");
		}
		if (fragment.equals("5")) {
			holder.tv_collectclass.setText("[项目检查]");
		}
		holder.tv_collecttime.setText(collectBean.getCollecttime());
		holder.tv_collecttitle.setText(collectBean.getCollecttitle());
		return convertView;
	}
	
	class ViewHolder{
		TextView tv_collectclass,tv_collecttime,tv_collecttitle;
	}

}
