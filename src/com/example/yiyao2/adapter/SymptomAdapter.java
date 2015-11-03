package com.example.yiyao2.adapter;

import java.util.ArrayList;
import java.util.List;

import com.example.yiyao2.R;
import com.example.yiyao2.bean.SymptomBean;
import com.example.yiyao2.bean.LoreBean;
import com.example.yiyao2.bean.NewsBean;
import com.example.yiyao2.utils.UrlUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SymptomAdapter extends BaseAdapter{

	private Context context;
	private List<SymptomBean> list=new ArrayList<SymptomBean>();
	
	public SymptomAdapter(Context context,List<SymptomBean> list) {
		this.context=context;
		this.list=list;
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
		final ViewHolder holder;
		if (convertView==null) {
			convertView=LayoutInflater.from(context).inflate(R.layout.item_symptom, null);
			holder=new ViewHolder();
			holder.iv_symptom=(ImageView)convertView.findViewById(R.id.iv_symptom);
			holder.tv_symptomname=(TextView)convertView.findViewById(R.id.tv_symptomname);
			holder.tv_symptomplace=(TextView)convertView.findViewById(R.id.tv_symptomplace);
			holder.tv_symptomcount=(TextView)convertView.findViewById(R.id.tv_symptomcount);
			
			convertView.setTag(holder);
		}else {
			holder=(ViewHolder)convertView.getTag();
		}
		
		SymptomBean data=list.get(position);
		
		holder.tv_symptomname.setText(data.getName());
		holder.tv_symptomplace.setText(data.getPlace());
		holder.tv_symptomcount.setText(data.getCount());
		final String imgUrl=UrlUtils.IMGURL+data.getImg();
		//下载图片  使用universal image loader要提前进行配置 
		ImageLoader.getInstance().displayImage(imgUrl, holder.iv_symptom, new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				
			}
			
			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
				
			}
			
			/**
			 * 下载完成 
			 */
			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
				holder.iv_symptom.setImageBitmap(arg2);
			}
			
			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				
			}
		});
		return convertView;
	}

	class ViewHolder{
		ImageView iv_symptom;;
		TextView tv_symptomname,tv_symptomplace,tv_symptomcount;
	}
}
