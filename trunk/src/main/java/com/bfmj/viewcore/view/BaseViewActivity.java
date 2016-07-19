package com.bfmj.viewcore.view;

import java.lang.reflect.Method;

import com.baofeng.mojing.MojingSDK;
import com.baofeng.mojing.input.base.MojingKeyCode;
//import com.bfmj.viewcore.util.StickUtil;
import com.bfmj.viewcore.view.GLPageManager;
import com.bfmj.viewcore.view.GLRootView;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class BaseViewActivity extends Activity implements SensorEventListener {
	private RelativeLayout rootLayout;
	private GLRootView rootView;
	private GLPageManager mPageManager;
	private SensorManager mSensorManager;
	private float mLight = 0;
	private boolean isGroyEnable = true;
	private boolean isDistortionEnable = true;
//	private boolean isSavingMode = true;
	private boolean isLockViewAngle = false;
	private float mLockedAngle = 0f;
	private String mMojingType = "F79F2H-Q8ZNXN-2HQN2F-2ZA9YV-QG4H9H-QGAYAE";
	private boolean isTouchControl = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);	
//		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		super.onCreate(savedInstanceState);
		
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		
		MojingSDK.Init(this);
		
		rootView = new GLRootView(this);
		rootView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		rootView.setGlassesKey(mMojingType);
		
		if (isVirtualKey()) {
			rootView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION); 
		}
		
		mPageManager = new GLPageManager();
		mPageManager.setRootView(rootView);
		
		rootLayout = new RelativeLayout(this);
		rootLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		rootLayout.addView(rootView);
		setContentView(rootLayout);
		
//		StickUtil.getInstance(this);
	}
	
	/**
	 * 
	 * @author linzanxian  @Date 2015年3月16日 下午2:41:34
	 * description:
	 * @return GLRootView
	 */
	public GLRootView getRootView() {
		return rootView;
	}
	
	public RelativeLayout getRootLayout(){
		return rootLayout;
	}
	
	public GLPageManager getPageManager() {
		return mPageManager;
	}
	
	/**
	 * 隐藏光标，具体由子类实
	 * @author lixianke  @Date 2015-5-14 下午2:14:23
	 */
	public void hideCursorView(){}
	
	/**
	 * 显示光标，具体由子类实
	 * @author lixianke  @Date 2015-5-14 下午2:14:23
	 */
	public void showCursorView(){}
	
	/**
	 * 显示阴影，具体由子类实
	 * @author lixianke  @Date 2015-5-14 下午2:14:23
	 */
	public void showShadow(){}
	
	/**
	 * 隐藏阴影，具体由子类实
	 * @author lixianke  @Date 2015-5-14 下午2:14:23
	 */
	public void hideShadow(){}
	
	/**
	 * 缩放阴影，具体由子类实
	 * @author lixianke  @Date 2015-5-14 下午2:14:23
	 */
	public void scaleShaow(float sx, float sy, float sz){}
	
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		
//		switch (keyCode) {
//			case KeyEvent.KEYCODE_MENU:
//				onMojingKeyDown("", MojingKeyCode.KEYCODE_MENU);
//				event.startTracking();
//				return true;
//			case KeyEvent.KEYCODE_BACK:
//				onMojingKeyDown("", MojingKeyCode.KEYCODE_BACK);
//				event.startTracking();
//				return true;
//			default:
//				break;
//		}
//
//		return super.onKeyDown(keyCode, event);
//	}
//	
//	@Override
//	public boolean onKeyUp(int keyCode, KeyEvent event) {
//		
//		switch (keyCode) {
//			case KeyEvent.KEYCODE_MENU:
//				onMojingKeyUp("", MojingKeyCode.KEYCODE_MENU);
//				return true;
//			case KeyEvent.KEYCODE_BACK:
//				onMojingKeyUp("", MojingKeyCode.KEYCODE_BACK);
//				return true;
//			default:
//				break;
//		}
//		
//		return super.onKeyUp(keyCode, event);
//	}
//	
//	@Override
//	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
//		
//		switch (keyCode) {
//			case KeyEvent.KEYCODE_MENU:
//				onMojingKeyLongPress("", MojingKeyCode.KEYCODE_MENU);
//				return true;
//			case KeyEvent.KEYCODE_BACK:
//				onMojingKeyLongPress("", MojingKeyCode.KEYCODE_BACK);
//				return true;
//			default:
//				break;
//		}
//		
//		return super.onKeyLongPress(keyCode, event);
//	}
//	
//	@Override public boolean dispatchKeyEvent(KeyEvent event) {
//		if (StickUtil.dispatchKeyEvent(event))
//			return true;
//		else
//			return super.dispatchKeyEvent(event);
//	}
//
//	@Override 
//	public boolean dispatchGenericMotionEvent(MotionEvent event) {
//		if (StickUtil.dispatchGenericMotionEvent(event))
//			return true;
//		else
//			return super.dispatchGenericMotionEvent(event);
//	}
//	
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		if (!isTouchControl) {
//			return false;
//		}
//		
//		switch (event.getAction()) {
//			case MotionEvent.ACTION_DOWN:
//				onMojingKeyDown("", MojingKeyCode.KEYCODE_ENTER);
//				break;
//			case MotionEvent.ACTION_UP:
//				onMojingKeyUp("", MojingKeyCode.KEYCODE_ENTER);
//				break;
//	
//			default:
//				break;
//		}
//		
//		return false;
//	}
	
	@Override
	protected void onResume() {
		mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), 
            SensorManager.SENSOR_DELAY_GAME);
		
//		StickUtil.setCallback(this);
		
		if (rootView != null){
			rootView.onResume();
		}
		
//		if (mPageManager != null){
//			mPageManager.onResume();
//		}
		super.onResume();
	}
	

	@Override
	protected void onPause() {
		mSensorManager.unregisterListener(this);
		
//		StickUtil.setCallback(null);
		
		if (rootView != null){
			rootView.onPause();
		}
		
		if (mPageManager != null){
			mPageManager.onPause();
		}
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		if (rootView != null){
			rootView.onDestroy();
		}
		super.onDestroy();
	}
	
	/**
	 * 判断是否有虚拟键
	 * @author linzanxian  @Date 2015年3月19日 上午10:37:23
	 * description:判断是否有虚拟键
	 * @return void
	 */
	private boolean isVirtualKey() {
		if (getScreen().equals(getDefaultScreen())) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * 获取默认屏幕格式
	 * @author linzanxian  @Date 2015年3月19日 上午10:34:39
	 * description:获取默认屏幕格式
	 * @return String
	 */
	private String getDefaultScreen() {
		DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm); 
        
        return dm.widthPixels+"*"+dm.heightPixels;
	}
	
	/**
	 * 获取可用屏幕格式
	 * @author linzanxian  @Date 2015年3月19日 上午10:35:59
	 * description:获取可用屏幕格式 
	 * @return String
	 */
	private String getScreen() {   
		String dpi=null;
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            dpi=dm.widthPixels+"*"+dm.heightPixels;
        }catch(Exception e){
            e.printStackTrace();
        }  
        
        return dpi;
    }

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		float[] values = event.values; 
        int sensorType = Sensor.TYPE_LIGHT; 
        if (sensorType == Sensor.TYPE_LIGHT) {         	
        	if (mLight >= 2.0 &&  values[0] < 2.0){
        		if (rootView != null){
        			rootView.initHeadView();
        		}
        	}
        	
        	mLight = values[0];
        }
	}

//	public boolean isSavingMode() {
//		return isSavingMode;
//	}

//	public void setSavingMode(boolean isSavingMode) {
//		this.isSavingMode = isSavingMode;
//		
//		if (rootView != null){
//			rootView.setSavingMode(isSavingMode);
//		}
//	}

	public boolean isGroyEnable() {
		return isGroyEnable;
	}

	public void setGroyEnable(boolean isGroyEnable) {
		this.isGroyEnable = isGroyEnable;
		
		if (rootView != null){
			rootView.setGroyEnable(isGroyEnable);
		}
	}

	public boolean isDistortionEnable() {
		return isDistortionEnable;
	}

	public void setDistortionEnable(boolean isDistortionEnable) {
		this.isDistortionEnable = isDistortionEnable;
		
		if (rootView != null){
			rootView.setDistortionEnable(isDistortionEnable);
		}
	}

	public boolean isLockViewAngle() {
		return isLockViewAngle;
	}

	public void setLockViewAngle(boolean isLockViewAngle) {
		this.isLockViewAngle = isLockViewAngle;
		
		if (rootView != null){
			rootView.setLockViewAngle(isLockViewAngle);
		}
	}

	public float getLockedAngle() {
		return mLockedAngle;
	}

	public void setLockedAngle(float mLockedAngle) {
		this.mLockedAngle = mLockedAngle;
		
		if (rootView != null){
			rootView.setLockedAngle(mLockedAngle);
		}
	}

	public String getMojingType() {
		return mMojingType;
	}

	public void setMojingType(String mojingType) {
		this.mMojingType = mojingType;
	}

	public boolean isTouchControl() {
		return isTouchControl;
	}

	public void setTouchControl(boolean isTouchControl) {
		this.isTouchControl = isTouchControl;
	}
	
	public boolean onZKeyDown(final int keyCode) {		
		rootView.queueEvent(new Runnable() {
			
			@Override
			public void run() {
				boolean flag = rootView.onKeyDown(keyCode);
				if (keyCode == MojingKeyCode.KEYCODE_BACK && !flag){
					mPageManager.pop();
				}
			}
		});
		return false;
	}

	public boolean onZKeyUp(final int keyCode) {
		rootView.queueEvent(new Runnable() {
			
			@Override
			public void run() {
				rootView.onKeyUp(keyCode);
			}
		});
		return false;
	}

	public boolean onZKeyLongPress(final int keyCode) {
		rootView.queueEvent(new Runnable() {
			
			@Override
			public void run() {
				rootView.onKeyLongPress(keyCode);
			}
		});
		return false;
	}

//	@Override
//	public void onBluetoothAdapterStateChanged(int arg0) {
//	}
//
//	@Override
//	public void onMojingDeviceAttached(String arg0) {
//	}
//
//	@Override
//	public void onMojingDeviceDetached(String arg0) {
//	}
//
//	@Override
//	public boolean onMojingKeyDown(String deviceName, final int keyCode) {
//		rootView.queueEvent(new Runnable() {
//			
//			@Override
//			public void run() {
//				boolean flag = rootView.onKeyDown(keyCode);
//				if (keyCode == MojingKeyCode.KEYCODE_BACK && !flag){
//					mPageManager.pop();
//				}
//			}
//		});
//		return false;
//	}
//
//	@Override
//	public boolean onMojingKeyLongPress(String deviceName, final int keyCode) {	
//		rootView.queueEvent(new Runnable() {
//			
//			@Override
//			public void run() {
//				rootView.onKeyLongPress(keyCode);
//			}
//		});
//		return false;
//	}
//
//	@Override
//	public boolean onMojingKeyUp(String deviceName, final int keyCode) {
//		rootView.queueEvent(new Runnable() {
//			
//			@Override
//			public void run() {
//				rootView.onKeyUp(keyCode);
//			}
//		});
//		return false;
//	}
//
//	@Override
//	public boolean onMojingMove(String arg0, int arg1, int arg2, int arg3,
//			int arg4) {
//		return false;
//	}

}
