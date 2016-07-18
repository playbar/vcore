package com.bfmj.viewcore;

import java.util.ArrayList;
import java.util.List;

import com.androidquery.AQuery;
import com.androidquery.callback.BitmapAjaxCallback;
import com.bfmj.viewcore.adapter.GLBaseAdapter;
import com.bfmj.viewcore.view.GLGroupView;
import com.bfmj.viewcore.view.GLImageView;
import com.bfmj.viewcore.view.GLListView;
import com.bfmj.viewcore.view.GLRectView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.androidquery.callback.AjaxStatus;

public class ListViewAdapter extends GLBaseAdapter {

	private List<String> list;
	private Context context;
//	private AQuery mAq;
	private DisplayImageOptions options;        // DisplayImageOptions是用于设置图片显示的类
	private ImageLoader imageLoader = ImageLoader.getInstance();  
//	private ImageView iv;
	
	public ListViewAdapter(List<String> listData, Context c) {
		context = c;
		list = listData;
//		mAq = aq;
		
		imageLoader.init(ImageLoaderConfiguration.createDefault(c));
		
		// 使用DisplayImageOptions.Builder()创建DisplayImageOptions  
        options = new DisplayImageOptions.Builder()  
            .showStubImage(R.drawable.a1)          // 设置图片下载期间显示的图片  
            .showImageForEmptyUri(R.drawable.a1)  // 设置图片Uri为空或是错误的时候显示的图片  
            .showImageOnFail(R.drawable.a1)       // 设置图片加载或解码过程中发生错误显示的图片      
            .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中  
            .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中  
            .displayer(new RoundedBitmapDisplayer(20))  // 设置成圆角图片  
            .build();                                   // 创建配置过得DisplayImageOption对象  
	}

	@Override
	public void addIndex(int index, GLRectView view) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeIndex(int index) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public GLRectView getGLView(int position, GLRectView convertView,
			GLGroupView parent) {
		// TODO Auto-generated method stub
		
		TestRelativeLayout grv = (TestRelativeLayout)convertView;
		
		if(grv == null){
			grv = new TestRelativeLayout(context);			
		}

		final GLImageView image = (GLImageView)grv.getView("timage");
		image.setImage(R.drawable.a1);
		
		/** 
         * 显示图片 
         * 参数1：图片url 
         * 参数2：显示图片的控件 
         * 参数3：显示图片的设置 
         * 参数4：监听器 
         */  
//		imageLoader.displayImage(list.get(position), iv, options, new ImageLoadingListener() {
//			
//			@Override
//			public void onLoadingStarted(String arg0, View arg1) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onLoadingComplete(String arg0, View arg1, Bitmap bitmap) {
//				// TODO Auto-generated method stub	
//				
//			}
//			
//			@Override
//			public void onLoadingCancelled(String arg0, View arg1) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
		ImageView iv = new ImageView(context);
//		mAq.id(iv).image(list.get(position), true, true, 0, 0, new BitmapAjaxCallback(){
//
//	        @Override
//	        public void callback(String url, ImageView iv, Bitmap bm,  AjaxStatus status){
//
//	        	Log.d("iiii",url);
//
//				final Bitmap _bitmap = bm;
//				(((BaseViewActivity)context).getRootView()).queueEvent(new Runnable() {
//					@Override
//					public void run() {
//						image.setImage(_bitmap);
////						_bitmap.recycle();
//					}
//				});
//
//	 	       }
//
//		});
		
		
//		mAq.im
		
//		image.setImage(list.get(position));
		Log.d("bbbbb",list.get(position) + "");
		return grv;
		
	}
	
	

}
