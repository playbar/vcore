package com.bfmj.viewcore.view;

import java.lang.reflect.Method;

import com.baofeng.mojing.MojingSDK;
import com.baofeng.mojing.input.base.MojingKeyCode;

import android.app.Activity;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class BaseViewActivity extends Activity implements SensorEventListener {
	private static BaseViewActivity instance;
	private RelativeLayout rootLayout;
	private GLRootView rootView;
	private GLPageManager mPageManager;
	private SensorManager mSensorManager;
	private float mLight = 0;
	private boolean isGroyEnable = true;
	private boolean isDistortionEnable = true;
	private boolean isLockViewAngle = false;
	private float mLockedAngle = 0f;
	private String mMojingType = "F79F2H-Q8ZNXN-2HQN2F-2ZA9YV-QG4H9H-QGAYAE";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

		instance = this;
		
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

		initLog();
	}

	public static void log(String msg){
		if (instance != null){
			instance.doLog(msg);
		}
	}

	private void doLog(String msg){
		Log.d("aaaaaaaaaaaa", msg);
	}

	private void initLog(){
		final TextView fps = new TextView(this);
//		LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//		lp.setMargins(0, 300, 0, 0);
//		fps.setLayoutParams(lp);
		fps.setPadding(0, 400, 0, 0);
		fps.setTextColor(Color.RED);
		fps.setTextSize(30);

		getRootLayout().addView(fps);

		new Thread(new Runnable() {
			long times = 0;
			int max = 0;
			int min = 60;

			@Override
			public void run() {
				getRootView().getFPS();
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				while (true){
					final int f = getRootView().getFPS();
					if (f > 0 && f < 70){
						times++;
						if (times > 2) {
							max = Math.max(f, max);
							min = Math.min(f, min);
							BaseViewActivity.this.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									String msg = "FPS : " + f;
									if (max > 0){
										msg +=  " [" + min + "~" + max + "]";
									}
									fps.setText(msg);
								}
							});
						}
					}
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		}).start();
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

	
	@Override
	protected void onResume() {
		mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), 
            SensorManager.SENSOR_DELAY_GAME);
		
		if (rootView != null){
			rootView.startTracker();
		}
		
		if (mPageManager != null){
			mPageManager.onResume();
		}
		super.onResume();
	}

	@Override
	protected void onPause() {
		mSensorManager.unregisterListener(this);
		
		if (rootView != null){
			rootView.stopTracker();
		}
		
		if (mPageManager != null){
			mPageManager.onPause();
		}
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		if (mPageManager != null){
			mPageManager.finish();
		}

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
}
