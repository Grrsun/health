package com.example.yiyao2.adapter;

import java.util.ArrayList;
import java.util.List;

import com.example.yiyao2.R;
import com.example.yiyao2.bean.CookBean;
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

public class CookAdapter extends BaseAdapter{

	private Context context;
	private List<CookBean> list=new ArrayList<CookBean>();
	
	public CookAdapter(Context context,List<CookBean> list) {
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
			convertView=LayoutInflater.from(context).inflate(R.layout.item_cook, null);
			holder=new ViewHolder();
			holder.iv_cook=(ImageView)convertView.findViewById(R.id.iv_cook);
			holder.tv_cookname=(TextView)convertView.findViewById(R.id.tv_cookname);
			holder.tv_cooktag=(TextView)convertView.findViewById(R.id.tv_cooktag);
			holder.tv_cookfood=(TextView)convertView.findViewById(R.id.tv_cookfood);
			holder.tv_cookcount=(TextView)convertView.findViewById(R.id.tv_cookcount);
			convertView.setTag(holder);
		}else {
			holder=(ViewHolder)convertView.getTag();
		}
		
		CookBean data=list.get(position);
		
		holder.tv_cookname.setText(data.getName());
		holder.tv_cooktag.setText(data.getTag());
		holder.tv_cookfood.setText(data.getFood());
		holder.tv_cookcount.setText(data.getCount());
		final String imgUrl=UrlUtils.IMGURL+data.getImg();
		//下载图片  使用universal image loader要提前进行配置 
		ImageLoader.getInstance().displayImage(imgUrl, holder.iv_cook, new ImageLoadingListener() {
			
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
				holder.iv_cook.setImageBitmap(arg2);
			}
			
			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				
			}
		});
		return convertView;
	}

	class ViewHolder{
		ImageView iv_cook;;
		TextView tv_cookname,tv_cooktag,tv_cookfood,tv_cookcount;
	}
}
