package com.viewcore.test;

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
import com.bfmj.viewcore.view.GLImageView;
import com.bfmj.viewcore.view.GLListView;
import com.bfmj.viewcore.view.GLRectView;
import com.bfmj.viewcore.view.GLRootView;
import com.bfmj.viewcore.view.GLView;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class ListViewActivity extends BaseViewActivity {

	private GLListView listView;
	private String Url = "http://img.static.mojing.cn/resource/list/3.js";
//	private AQuery aq;
	private int index = 0;
	private GLRootView rootView;
	private ListViewAdapter adapter;
	private List<Integer> listData = new ArrayList<Integer>();

	// 图片封装为一个数组
	private int[] icon = {
			R.drawable.address_book,
			R.drawable.calendar,
			R.drawable.camera,
			R.drawable.clock,
			R.drawable.games_control,
			R.drawable.messenger,
			R.drawable.ringtone,
			R.drawable.settings,
			R.drawable.speech_balloon,
			R.drawable.weather,
			R.drawable.world,
			R.drawable.youtube
	};
	private String[] iconName = {
			"通讯录", "日历", "照相机", "时钟", "游戏", "短信",
			"铃声", "设置", "语音", "天气", "浏览器", "视频"
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
//		aq = new AQuery(this);
		rootView = getRootView();
		rootView.onResume();		
		listView = new GLListView(ListViewActivity.this, GLListView.VERTICAL);
		listView.setBackground(new GLColor(1.0f, 0.0f, 0.0f));

//		listView.setMargin(50, 50, 40,40);
		listView.setX( 580);
		listView.setY(500);
		listView.setLayoutParams(800, 1000);
		listView.setItemSpacing(20);

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


		final GLAdapterView.OnItemSelectedListener onItemSelectedListener = new GLAdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(GLAdapterView<?> glparent,
									   GLView glview, int position, long id) {
				Log.e("ListViewActivity", "OnItemSelectedListener.onItemSelected");
				//glview.setAlpha( 0.3f );
			}

			@Override
			public void onNothingSelected(GLAdapterView<?> glparent,
										  GLView glview, int position, long id) {
				Log.e("ListViewActivity", "OnItemSelectedListener.onNothingSelected");
				//glview.setAlpha( 1.0f );
			}

			@Override
			public void onNoItemData() {
				Log.e("ListViewActivity", "OnItemSelectedListener.onNoItemData");
			}
		};

		for(index =0;  index < 3; index++){
			listData.add(icon[index]);
		}

		listView.setOnItemSelectedListener( onItemSelectedListener );
		//listView.setOpenHeadControl( false );
		adapter = new ListViewAdapter(listData, ListViewActivity.this);
		listView.setAdapter(adapter);
		listView.rotate(-30, 0, 1, 0);
//		listView.setFocusListener( new GLViewFocusListener(){
//			@Override
//			public void onFocusChange( GLRectView view, boolean bfocused ){
//				Log.e("ListViewActivity", "listView is on focused");
//			}
//		});

		rootView.addView(listView);

		GLImageView lineH = new GLImageView(this);
		lineH.setX(0);
		lineH.setY(500);
		lineH.setLayoutParams( 960, 2 );
		lineH.setBackground( new GLColor( 1, 1, 1));
		rootView.addView(lineH);

		GLImageView line = new GLImageView(this);
		line.setX(580);
		line.setY( 0);
		line.setLayoutParams(2, 960);
		line.setBackground(new GLColor(0, 1, 0));
		rootView.addView(line);

		GLCursorView imageView = new GLCursorView(this);
		imageView.setWidth(10);
		imageView.setHeight(10);
		imageView.setBackground(new GLColor(1.0f, 1.0f, 1.0f));
		imageView.setDepth(3);
		imageView.setX( 1195);
		imageView.setY( 1195);
		imageView.setLayoutParams( 10, 10);
		
		rootView.addView(imageView);
//		rootView.addView(listView);		
		//aq.ajax(Url, JSONArray.class, this, "jsonCallBack");
		
	}
	
	public void jsonCallBack(String url, JSONArray jsonArray, AjaxStatus status) throws JSONException{
		
		if(jsonArray != null){//如果不为空
			Log.d("iiii","add-listview-----" + jsonArray.length());
			
			for(int i=0;i<jsonArray.length();i++){
				String imageUrl = jsonArray.getJSONObject(i).getString("logourl");
				listData.add(i);
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
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		Log.e("onKeyUp", "onKeyUpn code =" + keyCode);
		listView.onKeyUp( keyCode );
		return false;
	}

	public boolean onKeyDown( int keyCode, KeyEvent event ){
		Log.e("onKeyDown", "onKeyDown code = " + keyCode );
		listView.onKeyDown( keyCode );
		return false;
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
