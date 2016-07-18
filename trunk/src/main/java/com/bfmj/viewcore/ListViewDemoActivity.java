package com.bfmj.viewcore;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.bfmj.viewcore.interfaces.GLOnKeyListener;
import com.bfmj.viewcore.render.GLColor;
import com.bfmj.viewcore.view.GLCursorView;
import com.bfmj.viewcore.view.GLListView;
import com.bfmj.viewcore.view.GLRootView;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

public class ListViewDemoActivity extends BaseViewActivity {

	private GLListView listView;
	private String Url = "http://img.static.mojing.cn/resource/list/3.js";
//	private AQuery aq;
	private GLRootView rootView;
	private List<String> listData = new ArrayList<String>();

    // 图片封装为一个数组
    private int[] icon = { R.drawable.address_book, R.drawable.calendar,
            R.drawable.camera, R.drawable.clock, R.drawable.games_control,
            R.drawable.messenger, R.drawable.ringtone, R.drawable.settings,
            R.drawable.speech_balloon, R.drawable.weather, R.drawable.world,
            R.drawable.youtube };
    private String[] iconName = { "通讯录", "日历", "照相机", "时钟", "游戏", "短信", "铃声",
            "设置", "语音", "天气", "浏览器", "视频" };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
//		aq = new AQuery(this);
		rootView = getRootView();
		rootView.onResume();		
		//slistView = new GLListView(this);
//		listView.setBackground(new GLColor(1.0f, 0.0f, 0.0f));
		listView.setWidth(500);
		listView.setHeight(200);
		listView.setItemSpacing(20);

		for(int i=0;  i < iconName.length; i++){
			listData.add(iconName[i]);
		}

		AdapterDemo adapter = new AdapterDemo(listData, ListViewDemoActivity.this);
		listView.setAdapter(adapter);
		rootView.addView(listView);

//		listView.setOnKeyListener(new GLOnKeyListener() {
//			
//			@Override
//			public boolean onKeyUp(int keycode) {
//				// TODO Auto-generated method stub
//				return false;
//			}
//			
//			@Override
//			public boolean onKeyLongPress(int keycode) {
//				// TODO Auto-generated method stub
//				return false;
//			}
//			
//			
//			@Override
//			public boolean onKeyDown(int keycode) {
//
//				if(keycode == MojingKeyCode.KEYCODE_DPAD_RIGHT){
//					listView.moveLeft();
//				}
//				if(keycode == MojingKeyCode.KEYCODE_DPAD_LEFT){
//					listView.moveRight();
//				}
//				
//				return true;
//			}
//		});
		
		
		
		GLCursorView imageView = new GLCursorView(this);
		imageView.setWidth(10);
		imageView.setHeight(10);
		imageView.setBackground(new GLColor(1.0f, 1.0f, 1.0f));
		imageView.setDepth(3);
//		imageView.setImage(R.drawable.ic_launcher);
//		textView.setText("北京欢迎你");
//		textView.setAlpha(0.3f);
		imageView.setLayoutParams(460, 460, 40, 40);
		
		rootView.addView(imageView);
//		rootView.addView(listView);		
		//aq.ajax(Url, JSONArray.class, this, "jsonCallBack");
		
	}
	
	public void jsonCallBack(String url, JSONArray jsonArray, AjaxStatus status) throws JSONException{
		
		if(jsonArray != null){//如果不为空
			Log.d("iiii","add-listview-----" + jsonArray.length());
			
			for(int i=0;i<jsonArray.length();i++){
				String imageUrl = jsonArray.getJSONObject(i).getString("logourl");
				listData.add(imageUrl);
			}
			
			AdapterDemo adapter = new AdapterDemo(listData, ListViewDemoActivity.this);
			listView.setAdapter(adapter);
			
			
			getRootView().queueEvent(new Runnable() {
				@Override
				public void run() {
					rootView.addView(listView);
				}
			});
			
			
		}	        
	}
	
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		// TODO Auto-generated method stub
//		if(event.getAction() == MotionEvent.ACTION_DOWN){
////			listview.changeItem(false);
////			listview.move();
//			listView.moveLeft();
//			
//		}
//		if(event.getAction() == MotionEvent.ACTION_MOVE){
////			listview.changeItem(false);
////			listview.move();
//			listView.moveRight();
//		}
//		return super.onTouchEvent(event);
//	}
	
}
