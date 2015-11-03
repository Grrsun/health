package com.example.yiyao2.adapter;

import java.util.ArrayList;
import java.util.List;

import com.example.yiyao2.R;
import com.example.yiyao2.bean.DrugBean;
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

public class DrugAdapter extends BaseAdapter{

	private Context context;
	private List<DrugBean> list=new ArrayList<DrugBean>();
	
	public DrugAdapter(Context context,List<DrugBean> list) {
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
			convertView=LayoutInflater.from(context).inflate(R.layout.item_drug, null);
			holder=new ViewHolder();
			holder.iv_drug=(ImageView)convertView.findViewById(R.id.iv_drug);
			holder.tv_drugname=(TextView)convertView.findViewById(R.id.tv_drugname);
			holder.tv_drugptype=(TextView)convertView.findViewById(R.id.tv_drugptype);
			holder.tv_drugcount=(TextView)convertView.findViewById(R.id.tv_drugcount);
			holder.tv_drugfactory=(TextView)convertView.findViewById(R.id.tv_drugfactory);
			
			convertView.setTag(holder);
		}else {
			holder=(ViewHolder)convertView.getTag();
		}
		
		DrugBean data=list.get(position);
		
		holder.tv_drugname.setText(data.getName());
		holder.tv_drugcount.setText(data.getCount());
		holder.tv_drugptype.setText(data.getPType());
		holder.tv_drugfactory.setText(data.getFactory());
		
		final String imgUrl=UrlUtils.IMGURL+data.getImage();
		//下载图片  使用universal image loader要提前进行配置 
		ImageLoader.getInstance().displayImage(imgUrl, holder.iv_drug, new ImageLoadingListener() {
			
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
				holder.iv_drug.setImageBitmap(arg2);
			}
			
			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				
			}
		});
		return convertView;
	}

	class ViewHolder{
		ImageView iv_drug;
		TextView tv_drugname,tv_drugcount,tv_drugptype,tv_drugfactory;
	}
}
