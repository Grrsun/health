package com.example.yiyao2.adapter;

import java.util.ArrayList;
import java.util.List;

import com.example.yiyao2.R;
import com.example.yiyao2.bean.HospitalBean;
import com.example.yiyao2.utils.UrlUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HospitalAdapter extends BaseAdapter{

	private List<HospitalBean> list=new ArrayList<HospitalBean>();
	private Context context;
	
	public HospitalAdapter(Context context,List<HospitalBean> list) {
		this.context=context;
		this.list=list;
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		final ViewHolder holder;
		if (arg1==null) {
			arg1=LayoutInflater.from(context).inflate(R.layout.item_hospital, null);
			holder=new ViewHolder();
			holder.iv_hospital=(ImageView) arg1.findViewById(R.id.iv_hospital);
			holder.tv_hospitalname=(TextView)arg1.findViewById(R.id.tv_hospitalname);
			holder.tv_hospitallevel=(TextView)arg1.findViewWithTag(R.id.tv_hospitallevel);
			holder.tv_hospitaldistance=(TextView)arg1.findViewById(R.id.tv_hospitaldistance);
			holder.tv_hospitaladdress=(TextView)arg1.findViewById(R.id.tv_hospitaladdress);
			arg1.setTag(holder);
		}else {
			holder=(ViewHolder) arg1.getTag();
		}
		
		HospitalBean data=list.get(arg0);
		holder.tv_hospitalname.setText(data.getName());
		holder.tv_hospitallevel.setText(data.getLevel());
		holder.tv_hospitaldistance.setText("暂无数据");
		holder.tv_hospitaladdress.setText(data.getAddress());
		System.out.println(data.getName());
		System.out.println(data.getLogo());
		
		String imgurl=UrlUtils.IMGURL+data.getLogo();
		ImageLoader.getInstance().displayImage(imgurl, holder.iv_hospital, new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				
			}
			
			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
				
			}
			
			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
				holder.iv_hospital.setImageBitmap(arg2);
			}
			
			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				
			}
		});
		
		return arg1;
	}
	
	class ViewHolder{
		ImageView iv_hospital;
		TextView tv_hospitalname,tv_hospitallevel,tv_hospitaldistance,tv_hospitaladdress;
	}
	

}
