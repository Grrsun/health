package com.example.yiyao2;

import java.io.File;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.example.yiyao2.R;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.umeng.update.UmengUpdateAgent;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;

public class MyApplication extends Application {

	// 经纬度
	private double locationX;
	private double locationY;

	@Override
	public void onCreate() {
		super.onCreate();

		

		// registerReceiver();

		/**
		 * universal image loader 配置文件
		 */
		@SuppressWarnings("deprecation")
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).memoryCacheExtraOptions(180, 320)
				// max width, max height，即保存的每个缓存文件的最大长宽
				// .discCacheExtraOptions(180, 320, null)
				// 设置缓存的详细信息，最好不要设置这个
				.threadPoolSize(3)
				// 线程池内加载的数量 推荐1-5
				.threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
				.memoryCache(new WeakMemoryCache())
				// implementation/你可以通过自己的内存缓存实现
				.memoryCacheSize(2 * 1024 * 1024).discCacheSize(50 * 1024 * 1024)
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				// 将保存的时候的URI名称用MD5 加密
				.tasksProcessingOrder(QueueProcessingType.LIFO).discCacheFileCount(100)
				// 缓存的文件数量
				.discCache(
						new UnlimitedDiscCache(new File(Environment.getExternalStorageDirectory() + "/myApp/imgCache")))
				// 自定义缓存路径
				.defaultDisplayImageOptions(getDisplayOptions())
				.imageDownloader(new BaseImageDownloader(this, 5 * 1000, 30 * 1000)).writeDebugLogs() // Remove
																										// for
																										// release
																										// app
				.build();// 开始构建
		ImageLoader.getInstance().init(config);

		/**
		 * 百度地图定位配置文件
		 */
		initBDMap();

	}

	public void setLocationX(double x) {
		this.locationX = x;
	}

	public void setLocationY(double y) {
		this.locationY = y;
	}

	public double getLocationX() {
		return locationX;
	}

	public double getLocationY() {
		return locationY;
	}

	/**
	 * 百度地图定位配置文件
	 */
	private void initBDMap() {
		try {
			// 初始化定位功能
			SDKInitializer.initialize(this);
			// 生成客户端
			LocationClient client = new LocationClient(this);
			System.out.println("-----==");
			// 注册定位监听器
			client.registerLocationListener(new BDLocationListener() {

				// 处理接收到的定位信息
				@Override
				public void onReceiveLocation(BDLocation arg0) {
					locationX = arg0.getLongitude();
					locationY = arg0.getLatitude();
					System.out.println("==x==" + locationX);
					System.out.println("==y==" + locationY);

				}
			});
			// 配置客户端
			LocationClientOption option = new LocationClientOption();
			// 设置定位模式(高精度)
			option.setLocationMode(LocationMode.Hight_Accuracy);
			// 使用百度坐标系统 百度坐标系统代码为"bd0911"
			option.setCoorType("bd0911");
			option.setIsNeedAddress(false);
			// 自动定位间隔时间
			option.setScanSpan(3000);
			client.setLocOption(option);
			// 启动定位
			client.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		setLocationX(locationX);
		setLocationX(locationY);

	}

	@SuppressWarnings("deprecation")
	private DisplayImageOptions getDisplayOptions() {
		DisplayImageOptions options;
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.applogo) // 设置图片在下载期间显示的图片
				.showImageForEmptyUri(R.drawable.applogo)// 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.applogo) // 设置图片加载/解码过程中错误时候显示的图片
				.cacheInMemory(true)// 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true)// 设置下载的图片是否缓存在SD卡中
				.considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)// 设置图片以如何的编码方式显示
				.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
				// .delayBeforeLoading(int delayInMillis)//int
				// delayInMillis为你设置的下载前的延迟时间
				// 设置图片加入缓存前，对bitmap进行设置
				// .preProcessor(BitmapProcessor preProcessor)
				.resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
				.displayer(new RoundedBitmapDisplayer(20))// 是否设置为圆角，弧度为多少
				.displayer(new FadeInBitmapDisplayer(100))// 是否图片加载好后渐入的动画时间
				.build();// 构建完成
		return options;
	}

}
