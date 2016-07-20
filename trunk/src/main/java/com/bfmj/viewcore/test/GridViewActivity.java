package com.bfmj.viewcore.test;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.androidquery.AQuery;
import com.bfmj.viewcore.R;
import com.bfmj.viewcore.render.GLColor;
import com.bfmj.viewcore.render.GLConstant;
import com.bfmj.viewcore.view.BaseViewActivity;
import com.bfmj.viewcore.view.GLAdapterView;
import com.bfmj.viewcore.view.GLCursorView;
import com.bfmj.viewcore.view.GLGridView;
import com.bfmj.viewcore.view.GLImageView;
import com.bfmj.viewcore.view.GLRootView;
import com.bfmj.viewcore.view.GLTextView;
import com.bfmj.viewcore.view.GLView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GridViewActivity extends BaseViewActivity {

	private GLRootView rootView;
	private GLGridView gridView;
	private AQuery aq;
	private GridViewAdapter adapter;
	private int index;
	private List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();

	// 图片封装为一个数组
	private int[] icon = { R.drawable.address_book, R.drawable.calendar,
			R.drawable.camera, R.drawable.clock, R.drawable.games_control,
			R.drawable.messenger, R.drawable.ringtone, R.drawable.settings,
			R.drawable.speech_balloon, R.drawable.weather, R.drawable.world,
			R.drawable.youtube };
	private String[] iconName = { "通讯录", "日历", "照相机", "时钟", "游戏", "短信", "铃声",
			"设置", "语音", "天气", "浏览器", "视频" };

	public List<Map<String, Object>> getData(){
		//cion和iconName的长度是相同的，这里任选其一都可以
		for(index=0; index<3; ++index){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("image", icon[index]);
			map.put("text", iconName[index]);
			listData.add(map);
		}

		return listData;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		rootView = getRootView();
		rootView.onResume();
		//rootView.setDoubleScreen(false);
		GLAdapterView.OnItemSelectedListener listener = new GLAdapterView.OnItemSelectedListener(){
			public void onItemSelected(GLAdapterView<?> glparent, GLView glview, int position, long id){
				Log.e("test", "onItemSelected");
				glview.setAlpha(0.3f);
			}

			public void onNothingSelected(GLAdapterView<?> glparent, GLView glview, int position, long id){
				Log.e("test", "onNothingSelected");
				glview.setAlpha( 1.0f);
			}
			public void onNoItemData(){
				Log.e("test", "onNoItemData");
			}
		};

		GLAdapterView.OnItemClickListener clickListener = new GLAdapterView.OnItemClickListener(){
			public void onItemClick(GLAdapterView<?> glparent, GLView glview, int position, long id){
				Log.e("test", "onItemClick");

			}
		};

		gridView = new GLGridView( this, 3, 3 );
		gridView.setLayoutParams(500, 500, 40, 40);
		gridView.setBackground( new GLColor(1.0f, 1.0f, 1.0f ));
		gridView.setHorizontalSpacing( 20.0f);
		gridView.setVerticalSpacing( 20.0f);
		gridView.setMargin(10, 10, 10, 10 );
		gridView.setPadding( 10, 10, 10, 10);
		gridView.setOrientation(GLConstant.GLOrientation.HORIZONTAL );

		getData();
		adapter = new GridViewAdapter(listData, this);
		gridView.setOnItemSelectedListener( listener );
		gridView.setOnItemClickListener( clickListener );
		gridView.setAdapter( adapter );
		gridView.setWidth(1000);
		gridView.setHeight(800);




		//gridView.rotate(90.0f, 1.0f, 0.0f, 0.0f );
		rootView.addView(gridView);

		GLImageView lineH = new GLImageView(this);
		lineH.setLayoutParams(0, 500, 960, 2 );
		lineH.setBackground( new GLColor( 1, 1, 1));
		rootView.addView(lineH);

		GLImageView line = new GLImageView(this);
		line.setLayoutParams(500, 0, 2, 960);
		line.setBackground(new GLColor(0, 1, 0));
		rootView.addView(line);

		//rootView.setRotationX( 90 );

		//gridView.setItemSpacing(20);
		
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


		GLTextView textView = new GLTextView(this);
		textView.setLayoutParams( 1000, 1200, 1000, 200 );
		textView.setTextColor(new GLColor(0.0f, 1.0f, 1.0f));
		textView.setText("111的境况");
		textView.setTextSize(100);
		rootView.addView(textView);

//		GLCursorView imageView = new GLCursorView(this);
//		imageView.setWidth(10);
//		imageView.setHeight(10);
//		imageView.setBackground(new GLColor(1.0f, 1.0f, 1.0f));
//		imageView.setDepth(3);
//		imageView.setLayoutParams(1000, 1000, 100, 100);
//		rootView.addView(imageView);

		GLCursorView cursorView = new GLCursorView(this);
		cursorView.setLayoutParams(1190, 1190, 20, 20);
		cursorView.setBackground(new GLColor(1.0f, 0, 0));
		cursorView.setDepth(3);
		rootView.addView(cursorView);




//		rootView.addView(listView);		

	}
	


	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if( index >= icon.length )
			return super.onTouchEvent( event );
		if(event.getAction() == MotionEvent.ACTION_DOWN){
//			listview.changeItem(false);
//			listview.move();
//			listView.moveLeft();
			getRootView().queueEvent(new Runnable() {
				@Override
				public void run() {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("image", icon[index]);
					map.put("text", iconName[index]);
					listData.add(map);
					adapter.notifyDataSetChanged();
					// todo
					//gridView.showItem(0);
					index++;
					Log.e("OnTouchEvent", "add item");
				}
			});

		}
		if(event.getAction() == MotionEvent.ACTION_MOVE){
//			listview.changeItem(false);
//			listview.move();
//			listView.moveRight();
		}
		return super.onTouchEvent(event);
	}
	
}
