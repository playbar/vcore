package com.bfmj.viewcore;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.androidquery.callback.AjaxStatus;
import com.bfmj.viewcore.interfaces.GLOnKeyListener;
import com.bfmj.viewcore.render.GLColor;
import com.bfmj.viewcore.view.BaseViewActivity;
import com.bfmj.viewcore.view.GLAdapterView;
import com.bfmj.viewcore.view.GLCursorView;
import com.bfmj.viewcore.view.GLListView;
import com.bfmj.viewcore.view.GLRectView;
import com.bfmj.viewcore.view.GLRootView;
import com.bfmj.viewcore.view.GLView;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

public class ListViewActivity extends BaseViewActivity {

	private GLListView listView;
	private String Url = "http://img.static.mojing.cn/resource/list/3.js";
//	private AQuery aq;
	private int index = 0;
	private GLRootView rootView;
	private ListViewAdapter adapter;
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
		listView = new GLListView(ListViewActivity.this, GLListView.VERTICAL);
		listView.setBackground(new GLColor(1.0f, 0.0f, 0.0f));

		listView.setLayoutParams(280, 400, 1000, 800);
		listView.setItemSpacing(20);


		final GLAdapterView.OnItemSelectedListener onItemSelectedListener = new GLAdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(GLAdapterView<?> glparent,
									   GLView glview, int position, long id) {
				Log.e("LxkTestActivity", "OnItemSelectedListener.onItemSelected");
			}

			@Override
			public void onNothingSelected(GLAdapterView<?> glparent,
										  GLView glview, int position, long id) {
				Log.e("LxkTestActivity", "OnItemSelectedListener.onNothingSelected");
			}

			@Override
			public void onNoItemData() {
				Log.e("LxkTestActivity", "OnItemSelectedListener.onNoItemData");
			}
		};

		for(index =0;  index < 4; index++){
			listData.add(iconName[index]);
		}

		listView.setOnItemSelectedListener( onItemSelectedListener );
		adapter = new ListViewAdapter(listData, ListViewActivity.this);
		listView.setAdapter(adapter);
		rootView.addView(listView);

		listView.setOnKeyListener(new GLOnKeyListener() {

			@Override
			public boolean onKeyUp(GLRectView view, int keycode) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onKeyLongPress(GLRectView view, int keycode) {
				// TODO Auto-generated method stub
				return false;
			}


			@Override
			public boolean onKeyDown(GLRectView view, int keycode) {



				return true;
			}
		});
		
		
		
		GLCursorView imageView = new GLCursorView(this);
		imageView.setWidth(10);
		imageView.setHeight(10);
		imageView.setBackground(new GLColor(1.0f, 1.0f, 1.0f));
		imageView.setDepth(3);
		imageView.setLayoutParams(1000, 1000, 10, 10);
		
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
			
			ListViewAdapter adapter = new ListViewAdapter(listData, ListViewActivity.this);
			listView.setAdapter(adapter);
			
			
			getRootView().queueEvent(new Runnable() {
				@Override
				public void run() {
					rootView.addView(listView);
				}
			});
			
			
		}	        
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if(event.getAction() == MotionEvent.ACTION_DOWN){

			//listView.changeItem(false);
//			listview.move();

//			getRootView().queueEvent(new Runnable() {
//				@Override
//				public void run() {
//					listData.add(iconName[++index]);
//					adapter.notifyDataSetChanged();
//				}
//			});

			//listView.moveLeft();

		}
		if(event.getAction() == MotionEvent.ACTION_MOVE){
//			listView.changeItem(false);
//			listview.move();
			listView.moveRight();
		}
		return super.onTouchEvent(event);
	}
	
}
