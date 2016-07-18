package com.bfmj.viewcore;

import android.os.Bundle;
import android.util.Log;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.bfmj.viewcore.render.GLColor;
import com.bfmj.viewcore.view.GLCursorView;
import com.bfmj.viewcore.view.GLGridView;
import com.bfmj.viewcore.view.GLListView;
import com.bfmj.viewcore.view.GLRootView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GridViewActivity extends BaseViewActivity {

	private GLRootView rootView;
	private GLGridView gridView;
	private AQuery aq;
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
		for(int i=0;i<icon.length;i++){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("image", icon[i]);
			map.put("text", iconName[i]);
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

		gridView = new GLGridView( this, 2, 3 );
		gridView.setLayoutParams(200, 280, 40, 40);
		gridView.setBackground( new GLColor(1.0f, 0, 0 ));

		getData();

		GridViewAdapter adapter = new GridViewAdapter(listData, this);

		gridView.setAdapter( adapter );
		gridView.setWidth(500);
		gridView.setHeight(400);
		//gridView.rotate(90.0f, 1.0f, 0.0f, 0.0f );
		rootView.addView(gridView);
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
		
		
		
//		GLCursorView imageView = new GLCursorView(this);
//		imageView.setWidth(10);
//		imageView.setHeight(10);
//		imageView.setBackground(new GLColor(1.0f, 1.0f, 1.0f));
//		imageView.setDepth(3);
////		imageView.setImage(R.drawable.ic_launcher);
////		textView.setText("北京欢迎你");
////		textView.setAlpha(0.3f);
//		imageView.setLayoutParams(460, 460, 40, 40);
//
//		rootView.addView(imageView);
//		rootView.addView(listView);		

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
