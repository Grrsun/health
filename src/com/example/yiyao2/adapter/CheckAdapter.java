package com.example.yiyao2.adapter;

import java.util.ArrayList;
import java.util.List;

import com.example.yiyao2.R;
import com.example.yiyao2.bean.CheckBean;
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

public class CheckAdapter extends BaseAdapter{

	private Context context;
	private List<CheckBean> list=new ArrayList<CheckBean>();
	
	public CheckAdapter(Context context,List<CheckBean> list) {
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
			convertView=LayoutInflater.from(context).inflate(R.layout.item_check, null);
			holder=new ViewHolder();
			holder.iv_check=(ImageView)convertView.findViewById(R.id.iv_check);
			holder.tv_checkname=(TextView)convertView.findViewById(R.id.tv_checkname);
			holder.tv_checkmenu=(TextView)convertView.findViewById(R.id.tv_checkmenu);
			holder.tv_checkcount=(TextView)convertView.findViewById(R.id.tv_checkcount);
			convertView.setTag(holder);
		}else {
			holder=(ViewHolder)convertView.getTag();
		}
		
		CheckBean data=list.get(position);
		
		holder.tv_checkname.setText(data.getName());
		holder.tv_checkmenu.setText(data.getMenu());
		holder.tv_checkcount.setText(data.getCount());
		final String imgUrl=UrlUtils.IMGURL+data.getImg();
		//下载图片  使用universal image loader要提前进行配置 
		ImageLoader.getInstance().displayImage(imgUrl, holder.iv_check, new ImageLoadingListener() {
			
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
				holder.iv_check.setImageBitmap(arg2);
			}
			
			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				
			}
		});
		return convertView;
	}

	class ViewHolder{
		ImageView iv_check;;
		TextView tv_checkname,tv_checkmenu,tv_checkcount;
	}
}
