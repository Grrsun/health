package com.example.yiyao2.adapter;

import java.util.ArrayList;
import java.util.List;

import com.example.yiyao2.R;
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

public class LoreAdapter extends BaseAdapter{

	private Context context;
	private List<LoreBean> list=new ArrayList<LoreBean>();
	
	public LoreAdapter(Context context,List<LoreBean> list) {
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
			convertView=LayoutInflater.from(context).inflate(R.layout.item_lore, null);
			holder=new ViewHolder();
			holder.iv_lore=(ImageView)convertView.findViewById(R.id.iv_lore);
			holder.tv_loretitle=(TextView)convertView.findViewById(R.id.tv_loretitle);
			holder.tv_loreclassname=(TextView)convertView.findViewById(R.id.tv_loreclassname);
			holder.tv_lorecount=(TextView)convertView.findViewById(R.id.tv_lorecount);
			holder.tv_loretime=(TextView)convertView.findViewById(R.id.tv_loretime);
			
			convertView.setTag(holder);
		}else {
			holder=(ViewHolder)convertView.getTag();
		}
		
		LoreBean data=list.get(position);
		System.out.println("==3=="+data.getTitle());
		
		System.out.println("==3=="+data.getImg());
		holder.tv_loretitle.setText(data.getTitle());
		holder.tv_loreclassname.setText(data.getClassName());
		holder.tv_lorecount.setText(data.getCount());
		holder.tv_loretime.setText(data.getTime());
		
		final String imgUrl=UrlUtils.IMGURL+data.getImg();
		//下载图片  使用universal image loader要提前进行配置 
		ImageLoader.getInstance().displayImage(imgUrl, holder.iv_lore, new ImageLoadingListener() {
			
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
				holder.iv_lore.setImageBitmap(arg2);
			}
			
			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				
			}
		});
		return convertView;
	}

	class ViewHolder{
		ImageView iv_lore;
		TextView tv_loretitle,tv_loreclassname,tv_lorecount,tv_loretime;
	}
}
