package com.bfmj.viewcore;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.androidquery.AQuery;
import com.baofeng.mojing.MojingSurfaceView;
import com.bfmj.mojing.MatrixState;
import com.bfmj.mojing.MojingActivity;
import com.bfmj.mojing.VrPhotoRender;
import com.bfmj.viewcore.interfaces.GLOnKeyListener;
import com.bfmj.viewcore.interfaces.GLViewFocusListener;
import com.bfmj.viewcore.render.GLColor;
import com.bfmj.viewcore.render.GLConstant;
import com.bfmj.viewcore.view.BaseViewActivity;
import com.bfmj.viewcore.view.GLAdapterView;
import com.bfmj.viewcore.view.GLCursorView;
import com.bfmj.viewcore.view.GLGridView;
import com.bfmj.viewcore.view.GLGridViewPage;
import com.bfmj.viewcore.view.GLImageView;
import com.bfmj.viewcore.view.GLRectView;
import com.bfmj.viewcore.view.GLRootView;
import com.bfmj.viewcore.view.GLTextView;
import com.bfmj.viewcore.view.GLView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.reflect.TypeVariable;

public class GridViewActivity extends BaseViewActivity {

	private GLRootView rootView;
	private GLGridView gridView;
	private AQuery aq;
	private GridViewAdapter adapter;
	private int index;
	private List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();

	static String MJ4 = "2WF5F5-FPWGZZ-H7AE2C-H3F8SW-EE8KCF-YTHBCN";
	static String Test1 = "9ZQRW8-CH9T98-WZYYF8-ZZY3DW-9YDWQ8-WGFUWU";
	static String Test2 = "8KYB29-QTA8EE-95YB9B-28FUAA-AHA22W-2FCQSH";
	static String Test3 = "ZR95FU-HTA84C-YN2NDW-9H8R2X-D8WGH8-SZWNFM";
	static String Test4 = "FBEYH7-YG8LCH-4823EZ-QBCFD8-CBZ598-8KWVCC";
	static String Test5 = "HH23QR-EH83CC-SWS3EH-Q7CVWQ-YVSHX8-WBSSSH";
	static String Test6 = "EXFPZN-ST8BS2-Q34HH5-AXC3E8-298RFF-8G44Q3";
	static String Test7 = "ZBED2F-FFS849-F7CYEF-QNE2XX-S9QY9V-SWCW86";
	static String Test8 = "ZHSXEF-EX48QR-2WQ8YF-4DY7Q3-4Y8BAS-87CZST";
	static String Test9 = "AHAS9Z-DB4CE9-QNCFEZ-48Z5ZH-HRSTWQ-2YFRX8";
	static String Test10 = "DTYHAD-WQACWV-QYEZEW-S34CAW-DT9H9Y-SHYVA8";
	static String Test11 = "WUAC4Z-SGCYDZ-DFZKAF-CNQRED-8NQ8FR-9R8KQF";
	static String[] KeyList = {
			Test1 			,
			Test2 			,
			Test3 			,
			Test4 			,
			Test5 			,
			Test6 			,
			Test7 			,
			Test8 			,
			Test9 			,
			Test10			,
			Test11
	};


	static String VrBox = "WYAQWH-CF95YZ-WWHL4Y-ZBE9YT-QZWGWT-9VDGWH";
	static String QIKU360="WUZFSH-XX8393-8RDY9B-XCS9WW-CBH8ZK-Q599XC";//奇酷360
	static String MOKE="X9DDQ5-XG9RDC-ADX3XF-DFHLHT-YTXWSQ-ZUHVWY";// 莫克
	static String QHMJ="9FWZDH-4SSXEH-8UZTFR-QYEH4Z-2XSFWQ-H8F59G";// 千幻魔镜

	static boolean   bTimeWarp = true;
	static boolean   bMultiThread = true;
	static String DefaultGlass = MJ4;
	private VrPhotoRender renderer;

	private Handler handler = new Handler( );

	private Runnable runnable = new Runnable( ) {
		public void run ( ) {
//	    	Log.e("TEST", "GetLightSensation:" + com.baofeng.com.bfmj.mojing.MojingSDK.GetLightSensation() + " GetProximitySensation:" + com.baofeng.com.bfmj.mojing.MojingSDK.GetProximitySensation());
			handler.postDelayed(this,1000);
		}
	};

	GLMsgHandler mGLMsgHandler;

	private static class GLMsgHandler extends Handler {
		private final WeakReference<GridViewActivity> mActivity;

		public GLMsgHandler(GridViewActivity activity) {
			mActivity = new WeakReference<GridViewActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			GridViewActivity activity = mActivity.get();
			if (activity != null) {
				if (msg.what == MojingSurfaceView.ON_CHANGE_MOJING_WORLD)
				{
					activity.onChangeMojingWorld();
				}
			}
		}
	}

	public void onChangeMojingWorld()
	{
		float fov = com.baofeng.mojing.MojingSDK.GetMojingWorldFOV() / 2.f;
		float ratio = (float)Math.tan(Math.toRadians(fov));
		MatrixState.setProjectFrustum(-ratio, ratio, -ratio, ratio, 1.f, 800);
	}


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

	public List<Map<String, Object>> getData(){
		//cion和iconName的长度是相同的，这里任选其一都可以
		for(index=0; index < 9; ++index){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("image", icon[index]);
			map.put("text", iconName[index]);
			listData.add(map);
		}

		return listData;
	}

	private void createGridView(){

		//rootView.onResume();
		//rootView.setDoubleScreen(false);
//		GLAdapterView.OnItemSelectedListener listener = new GLAdapterView.OnItemSelectedListener(){
//			public void onItemSelected(GLAdapterView<?> glparent, GLView glview, int position, long id){
//				Log.e("com/bfmj/viewcore/test", "onItemSelected:" + glview.toString());
//				glview.setAlpha(0.3f);
//			}
//
//			public void onNothingSelected(GLAdapterView<?> glparent, GLView glview, int position, long id){
//				Log.e("com/bfmj/viewcore/test", "onNothingSelected:" + glview.toString() );
//				glview.setAlpha( 1.0f);
//			}
//			public void onNoItemData(){
//				Log.e("com/bfmj/viewcore/test", "onNoItemData");
//			}
//		};
//
//		GLAdapterView.OnItemClickListener clickListener = new GLAdapterView.OnItemClickListener(){
//			public void onItemClick(GLAdapterView<?> glparent, GLView glview, int position, long id){
//				Log.e("com/bfmj/viewcore/test", "onItemClick");
//
//			}
//		};
//
//		gridView = new GLGridViewPage( this, 2, 3 );
//		gridView.setLayoutParams(500, 500, 40, 40);
//		gridView.setBackground( new GLColor(1.0f, 1.0f, 1.0f ));
//		gridView.setHorizontalSpacing( 20.0f);
//		gridView.setVerticalSpacing( 20.0f);
//		gridView.setMargin(10, 10, 10, 10 );
//		gridView.setPadding( 10, 10, 10, 10);
//		gridView.setOrientation(GLConstant.GLOrientation.HORIZONTAL );
//
//		getData();
//		adapter = new GridViewAdapter(listData, this);
//		gridView.setOnItemSelectedListener( listener );
//		gridView.setOnItemClickListener( clickListener );
//		gridView.setWidth(1000);
//		gridView.setHeight(800);
//		gridView.setAdapter( adapter );
//
//
//		//gridView.rotate(90.0f, 1.0f, 0.0f, 0.0f );
//		rootView.addView(gridView);
//
//		GLImageView lineH = new GLImageView(this);
//		lineH.setLayoutParams(0, 500, 960, 2 );
//		lineH.setBackground( new GLColor( 1, 1, 1));
//		rootView.addView(lineH);
//
//		GLImageView line = new GLImageView(this);
//		line.setLayoutParams(500, 0, 2, 960);
//		line.setBackground(new GLColor(0, 1, 0));
//		rootView.addView(line);
//
//		//rootView.setRotationX( 90 );
//
//		//gridView.setItemSpacing(20);
//
//		gridView.setOnKeyListener(new GLOnKeyListener() {
//
//			@Override
//			public boolean onKeyDown(GLRectView view, int keycode) {
////				if(keycode == MojingKeyCode.KEYCODE_DPAD_RIGHT){
////					listView.moveLeft();
////				}
////				if(keycode == MojingKeyCode.KEYCODE_DPAD_LEFT){
////					listView.moveRight();
////				}
//				Log.e("onCreate", "onKeyDown");
//				return false;
//			}
//
//			@Override
//			public boolean onKeyUp(GLRectView view, int keycode) {
//				Log.e("onCreate", "onKeyUp");
//				return false;
//			}
//
//			@Override
//			public boolean onKeyLongPress(GLRectView view, int keycode) {
//				return false;
//			}
//
//		});
//
//
//		GLTextView textView = new GLTextView(this);
//		textView.setLayoutParams( 1000, 2000, 1000, 200 );
//		textView.setTextColor(new GLColor(0.0f, 1.0f, 1.0f));
//		textView.setText("111的境况");
//		textView.setTextSize(100);
//		textView.setOnKeyListener(new GLOnKeyListener() {
//			@Override
//			public boolean onKeyDown(GLRectView view, int keycode) {
//				view.setAlpha( 0.3f );
//				return false;
//			}
//
//			@Override
//			public boolean onKeyUp(GLRectView view, int keycode) {
//				view.setAlpha( 1.0f );
//				return false;
//			}
//
//			@Override
//			public boolean onKeyLongPress(GLRectView view, int keycode) {
//				return false;
//			}
//		});
//		textView.setFocusListener(new GLViewFocusListener() {
//			@Override
//			public void onFocusChange(GLRectView view, boolean focused) {
//				if( focused )
//					view.setAlpha( 0.3f );
//				else
//					view.setAlpha( 1.0f );
//			}
//		});
//
//		//gridView.addView( textView );
//		rootView.addView(textView);
//
////		GLCursorView imageView = new GLCursorView(this);
////		imageView.setWidth(10);
////		imageView.setHeight(10);
////		imageView.setBackground(new GLColor(1.0f, 1.0f, 1.0f));
////		imageView.setDepth(3);
////		imageView.setLayoutParams(1000, 1000, 100, 100);
////		rootView.addView(imageView);


		GLCursorView cursorView = new GLCursorView(this);
		cursorView.setLayoutParams(1190, 1190, 20, 20);
		cursorView.setBackground(new GLColor(1.0f, 0, 0));
		cursorView.setDepth(3);
		rootView.addView(cursorView);
		//rootView.setBackgroundColor( 0xFFFFFF );

	}

	private void createSkyBox(){
		mGLMsgHandler = new GLMsgHandler(this);
		rootView.setMessageHandler(mGLMsgHandler);

		rootView.setTimeWarp(bTimeWarp);
		rootView.setMultiThread(bMultiThread);
		rootView.setGlassesKey(DefaultGlass);

		//renderer=new VrPhotoRender(this);
		//rootView.setRenderer(renderer);
		//rootView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

		String sUserSetting = com.baofeng.mojing.MojingSDK.GetUserSettings();
		sUserSetting = "{\"ClassName\":\"UserSettingProfile\",\"EnableScreenSize\":0,\"ScreenSize\":9}";
		com.baofeng.mojing.MojingSDK.SetUserSettings(sUserSetting);

		String  strManufacturerList =
				com.baofeng.mojing.MojingSDK.GetManufacturerList("ZH");
		String  strProductList =
				com.baofeng.mojing.MojingSDK.GetProductList("E92YDY-AHDQWV-ACF39Y-QT4EDC-CZ4CFU-4E8B85", "ZH");

		String  strGlassList =
				com.baofeng.mojing.MojingSDK.GetGlassList("SG2W2B-HG95SX-29CR8M-EFZ8DQ-9HEZW3-2BYZZU", "ZH");

		String  strGlassInfo =
				com.baofeng.mojing.MojingSDK.GetGlassInfo("SFCNDH-4WCYA4-4W8NX8-ZN9NF8-C386HT-A49ZWB", "ZH");

		String  strGenerationGlassKey =
				com.baofeng.mojing.MojingSDK.GenerationGlassKey("E92YDY-AHDQWV-ACF39Y-QT4EDC-CZ4CFU-4E8B85",  "CY42HN-FKCRQ8-SGS34S-8K23ZR-SZSBQ8-QFXF95");

		com.baofeng.mojing.MojingSDK.LogTrace("strManufacturerList >>>>" + strManufacturerList);
		com.baofeng.mojing.MojingSDK.LogTrace("strProductList >>>>" + strProductList);
		com.baofeng.mojing.MojingSDK.LogTrace("strGlassList >>>>" + strGlassList);
		com.baofeng.mojing.MojingSDK.LogTrace("strGlassInfo >>>>" + strGlassInfo);

		strManufacturerList =
				com.baofeng.mojing.MojingSDK.GetManufacturerList("ZH");
		strProductList =
				com.baofeng.mojing.MojingSDK.GetProductList("E92YDY-AHDQWV-ACF39Y-QT4EDC-CZ4CFU-4E8B85", "ZH");

		strGlassList =
				com.baofeng.mojing.MojingSDK.GetGlassList("SG2W2B-HG95SX-29CR8M-EFZ8DQ-9HEZW3-2BYZZU", "ZH");

		strGlassInfo =
				com.baofeng.mojing.MojingSDK.GetGlassInfo("SFCNDH-4WCYA4-4W8NX8-ZN9NF8-C386HT-A49ZWB", "ZH");

		strGenerationGlassKey =
				com.baofeng.mojing.MojingSDK.GenerationGlassKey("E92YDY-AHDQWV-ACF39Y-QT4EDC-CZ4CFU-4E8B85",  "CY42HN-FKCRQ8-SGS34S-8K23ZR-SZSBQ8-QFXF95");

		com.baofeng.mojing.MojingSDK.LogTrace("strManufacturerList 2 >>>>" + strManufacturerList);
		com.baofeng.mojing.MojingSDK.LogTrace("strProductList 2 >>>>" + strProductList);
		com.baofeng.mojing.MojingSDK.LogTrace("strGlassList 2 >>>>" + strGlassList);
		com.baofeng.mojing.MojingSDK.LogTrace("strGlassInfo 2 >>>>" + strGlassInfo);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		// 获取渲染模式
//		String glassKey = "FPQ8D2-2NHGWY-93S32F-DXD8YG-9QDCSG-444YZT"; //96
//		setMojingType(glassKey);

		super.onCreate(savedInstanceState);
		
		rootView = getRootView();

		createGridView();
		createSkyBox();


//		rootView.addView(listView);		

	}


	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		Log.e("onKeyUp", "onKeyUpn code =" + keyCode);
		gridView.onKeyUp( keyCode );
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
