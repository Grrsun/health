package com.example.yiyao2.adapter;

import java.util.ArrayList;
import java.util.List;

import com.example.yiyao2.R;
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

public class NewsAdapter extends BaseAdapter{

	private Context context;
	private List<NewsBean> list=new ArrayList<NewsBean>();
	
	public NewsAdapter(Context context,List<NewsBean> list) {
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
			convertView=LayoutInflater.from(context).inflate(R.layout.item_news, null);
			holder=new ViewHolder();
			holder.iv_news=(ImageView)convertView.findViewById(R.id.iv_news);
			holder.tv_newstitle=(TextView)convertView.findViewById(R.id.tv_newstitle);
			holder.tv_newstag=(TextView)convertView.findViewById(R.id.tv_newstag);
			holder.tv_newscount=(TextView)convertView.findViewById(R.id.tv_newscount);
			holder.tv_newstime=(TextView)convertView.findViewById(R.id.tv_newstime);
			
			convertView.setTag(holder);
		}else {
			holder=(ViewHolder)convertView.getTag();
		}
		
		NewsBean data=list.get(position);
		holder.tv_newstitle.setText(data.getTitle());
		holder.tv_newstag.setText(data.getTag());
		holder.tv_newscount.setText(data.getCount());
		holder.tv_newstime.setText(data.getTime());
		
		final String imgUrl=UrlUtils.IMGURL+data.getImg();
		//下载图片  使用universal image loader要提前进行配置 
		ImageLoader.getInstance().displayImage(imgUrl, holder.iv_news, new ImageLoadingListener() {
			
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
				holder.iv_news.setImageBitmap(arg2);
			}
			
			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				
			}
		});
		return convertView;
	}

	class ViewHolder{
		ImageView iv_news;
		TextView tv_newstitle,tv_newscount,tv_newstag,tv_newstime;
	}
}
