package com.viewcore.test;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.bfmj.viewcore.interfaces.GLOnKeyListener;
import com.bfmj.viewcore.interfaces.GLViewFocusListener;
import com.bfmj.viewcore.render.GLColor;
import com.bfmj.viewcore.util.GLGenTexTask;
import com.bfmj.viewcore.util.GLGenTexTask.GenTexIdInterface;
import com.bfmj.viewcore.view.BaseViewActivity;
import com.bfmj.viewcore.view.GLCursorView;
import com.bfmj.viewcore.view.GLRectView;
import com.bfmj.viewcore.view.GLRootView;
import com.bfmj.viewcore.view.GLTextView;

import java.io.InputStream;

public class TextViewActivity extends BaseViewActivity {

	private GLRootView rootView;
	private GLGenTexTask mTask;
	private Bitmap mBitmap;

	AssetManager assetManager =null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setMojingType("42EGCV-WQXG87-WHQHS8-YYYTEF-4SWGST-CRY7SS");
		super.onCreate(savedInstanceState);

//		BitmapOp bmtOp = new BitmapOp();
//		String str = bmtOp.stringFromJNI();
//		Log.e("TextViewActivity", "onCreate");

		mTask = new GLGenTexTask(GLGenTexTask.class.hashCode());
		mTask.setGenTexIdInterface( new GenTexIdInterface(){
			public void ExportTextureId(int mTextureId, int mHashCode){
				Log.e("TextViewActivity", "mTask");
				mTask.uninit();
				mBitmap.recycle();
			}
		});
		mTask.init();
		assetManager= getAssets();
		try {
			InputStream in=assetManager.open("textext.png");
			mBitmap = BitmapFactory.decodeStream(in);
			mTask.GenTexId(mBitmap, mBitmap.getWidth(), mBitmap.getHeight());
		} catch (Exception e) {
			// TODO: handle exception
		}


		rootView = getRootView();

		rootView.onResume();
		//rootView.setDoubleScreen(false);
		rootView.setDistortionEnable( true );


		GLTextView textView = new GLTextView(this);
		textView.setX( 1000);
		textView.setY( 2000 );
		textView.setLayoutParams(600, 600 );
		textView.setBackground( new GLColor(1.0f, 1.0f, 1.0f));
		textView.setTextColor(new GLColor(1.0f, 0.0f, 0.0f));
		textView.setText("88");
		textView.setPadding(10, 5, 0, 0);
		textView.setTextSize(40);
		textView.setOnKeyListener(new GLOnKeyListener() {
			@Override
			public boolean onKeyDown(GLRectView view, int keycode) {
				view.setAlpha( 0.3f );
				return false;
			}

			@Override
			public boolean onKeyUp(GLRectView view, int keycode) {
				view.setAlpha( 1.0f );
				return false;
			}

			@Override
			public boolean onKeyLongPress(GLRectView view, int keycode) {
				return false;
			}
		});
		textView.setFocusListener(new GLViewFocusListener() {
			@Override
			public void onFocusChange(GLRectView view, boolean focused) {
				if( focused )
					view.setAlpha( 0.3f );
				else
					view.setAlpha( 1.0f );
			}
		});

		rootView.addView(textView);

		GLCursorView cursorView = new GLCursorView(this);
		cursorView.setX(1190);
		cursorView.setY(1190);
		cursorView.setLayoutParams( 20, 20);
		cursorView.setBackground(new GLColor(1.0f, 0, 0));
		cursorView.setDepth(3);
		rootView.addView(cursorView);

	}


	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		Log.e("onKeyUp", "onKeyUpn code =" + keyCode);
		switch (keyCode) {
			case KeyEvent.KEYCODE_MENU:
				return true;
			case KeyEvent.KEYCODE_BACK:
				return true;
			default:
				break;
		}
		return false;
		//return super.onKeyUp(keyCode, event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub

		if(event.getAction() == MotionEvent.ACTION_DOWN){
//			listview.changeItem(false);
//			listview.move();
//			listView.moveLeft();
//			getRootView().queueEvent(new Runnable() {
//				@Override
//				public void run() {
//					Map<String, Object> map = new HashMap<String, Object>();
//					map.put("image", icon[index]);
//					map.put("text", iconName[index]);
//					listData.add(map);
//					adapter.notifyDataSetChanged();
//					// todo
//					//gridView.showItem(0);
//					index++;
//					Log.e("OnTouchEvent", "add item");
//				}
//			});

		}
		if(event.getAction() == MotionEvent.ACTION_MOVE){
//			listview.changeItem(false);
//			listview.move();
//			listView.moveRight();
		}
		return super.onTouchEvent(event);
	}
	
}
