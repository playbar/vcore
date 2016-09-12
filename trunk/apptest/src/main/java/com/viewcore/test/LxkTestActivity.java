package com.viewcore.test;


import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.opengl.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;

import com.baofeng.mojing.MojingSDK;
import com.bfmj.viewcore.interfaces.GLViewFocusListener;
import com.bfmj.viewcore.interfaces.IGLPlayerListener;
import com.bfmj.viewcore.player.GLSystemPlayer;
import com.bfmj.viewcore.render.GLColor;
import com.bfmj.viewcore.view.BaseViewActivity;
import com.bfmj.viewcore.view.GLCursorView;
import com.bfmj.viewcore.view.GLImageView;
import com.bfmj.viewcore.view.GLPanoView;
import com.bfmj.viewcore.view.GLPlayerView;
import com.bfmj.viewcore.view.GLRectView;
import com.bfmj.viewcore.view.GLRootView;
import com.bfmj.viewcore.view.GLTextView;
import com.mojing.sdk.pay.widget.mudoles.Glass;
import com.mojing.sdk.pay.widget.mudoles.Manufacturer;
import com.mojing.sdk.pay.widget.mudoles.ManufacturerList;
import com.mojing.sdk.pay.widget.mudoles.Product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LxkTestActivity extends BaseViewActivity {
	private GLRootView rootView;
	GLSystemPlayer player;

//	GLSystemPlayer player;


	@SuppressLint("SdCardPath")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
//		super.setDistortionEnable(false);
		if (!MojingSDK.GetInitSDK()) {
			MojingSDK.Init(this.getApplicationContext());
		}
		ManufacturerList m_ManufacturerList = ManufacturerList.getInstance("zh");

		List<Manufacturer> manufacturers = m_ManufacturerList.mManufaturerList;
		Log.e("glasses", "manufacturers => " + manufacturers.size() + " == ");
		List<Product> products = manufacturers.get(0).mProductList;
		Log.e("glasses", "products => " + products.size() + " == ");
		List<Glass> glasses = products.get(0).mGlassList;
		Log.e("glasses", "products => " + glasses.size() + " == ");
		String key = glasses.get(0).mKey;
		Log.e("glasses", "key => " + key + " == ");
		setMojingType(key);
		super.onCreate(savedInstanceState);

		rootView = getRootView();
		//rootView.setDoubleScreen(false);
		rootView.onResume();

		GLPanoView panoView = GLPanoView.getSharedPanoView(this);
		panoView.setImage(R.drawable.skybox_launcher);

//		GLImageView imageView1 = new GLImageView(this);
//		imageView1.setX(200);
//		imageView1.setY(200);
//		imageView1.setDepth(5f);
//		imageView1.setLayoutParams(2000, 2000);
//		imageView1.setImage(R.drawable.a2);
//
//		rootView.addView(imageView1);

		initLog();

		int len = 4;
		for (int i = 0; i < 2; i++){
//			GLImageView imageView1 = new GLImageView(this);
//			imageView1.setX(1000);
//			imageView1.setY(i * (300 + 50) + 400);
//			imageView1.setLayoutParams(400, 300);
//			imageView1.setImage(R.drawable.a2);
//
//			rootView.addView(imageView1);

			GLTextView textView = new GLTextView(this);
			textView.setX(700);
			textView.setY(i * (300 + 50) + 800);
			textView.setLayoutParams(400, 300);
			textView.setPadding(50, 100, 30, 30);
			textView.setText("钓鱼岛是中国的！！");
			textView.setTextColor(new GLColor(0xffffff));
			textView.setTextSize(32 + 4 * i);
//			textView.setAlpha(0.5f);

			rootView.addView(textView);
		}

		for (int i = 0; i < 2; i++){
//			GLImageView imageView1 = new GLImageView(this);
//			imageView1.setX(1000);
//			imageView1.setY(i * (300 + 50) + 400);
//			imageView1.setLayoutParams(400, 300);
//			imageView1.setImage(R.drawable.a2);
//
//			rootView.addView(imageView1);

			GLTextView textView = new GLTextView(this);
			textView.setX(1300);
			textView.setY(i * (300 + 50) + 800);
			textView.setLayoutParams(400, 300);
			textView.setPadding(50, 100, 30, 30);
			textView.setText("钓鱼岛是中国的！！");
			textView.setTextColor(new GLColor(0xffffff));
			textView.setTextSize(40 + 4 * i);
//			textView.setAlpha(0.5f);

			rootView.addView(textView);
		}



//		final GLPanoView panoView = GLPanoView.getSharedPanoView(this);
//		panoView.setRenderType(GLPanoView.RENDER_TYPE_VIDEO);
//
////		player = new MediaPlayer();
//
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					Thread.sleep(2000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//
//				rootView.queueEvent(new Runnable() {
//					@Override
//					public void run() {
////
////						try {
////							player.setDataSource("/mnt/sdcard/beardyman_cyberlink_720.mp4");
////							player.setSurface(new Surface(panoView.getSurfaceTexture()));
////
////							player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
////								@Override
////								public void onPrepared(MediaPlayer mp) {
////									mp.start();
////								}
////							});
////							player.prepareAsync();
////						} catch (IOException e) {
////							e.printStackTrace();
////						}
//
////						Log.d("play", "GLSystemPlayer");
//						player = new GLSystemPlayer(LxkTestActivity.this);
//						player.setVideoPath("/mnt/sdcard/疯狂动物城.mp4");
//						player.setLayoutParams(2400, 2400);
//						player.setDepth(4);
////		player.set3D(true);
//						//player.rotate(90);
//						player.setListener(new IGLPlayerListener() {
//
//							@Override
//							public void onVideoSizeChanged(GLPlayerView player, int width, int height) {
//								// TODO Auto-generated method stub
//
//							}
//
//							@Override
//							public void onTimedText(GLPlayerView player, String text) {
//								// TODO Auto-generated method stub
//
//							}
//
//							@Override
//							public void onSeekComplete(GLPlayerView player) {
//								// TODO Auto-generated method stub
//
//							}
//
//							@Override
//							public void onPrepared(GLPlayerView player) {
//								Log.d("play", "onPrepared");
//								player.start();
//							}
//
//							@Override
//							public boolean onInfo(GLPlayerView player, int what, Object extra) {
//								// TODO Auto-generated method stub
//								return false;
//							}
//
//							@Override
//							public boolean onError(GLPlayerView player, int what, int extra) {
//								// TODO Auto-generated method stub
//								return false;
//							}
//
//							@Override
//							public void onCompletion(GLPlayerView player) {
//								// TODO Auto-generated method stub
//
//							}
//
//							@Override
//							public void onBufferingUpdate(GLPlayerView player, int percent) {
//								// TODO Auto-generated method stub
//
//							}
//						});
//
//						rootView.addView(player);
//					}
//				});
//			}
//		}).start();


	}

	private void initLog(){
//		final GLTextView fps = new GLTextView(this);
//		fps.setX(900);
//		fps.setY(1800);
//		fps.setLayoutParams(600, 100);
//		fps.setFixed(true);
//		fps.setBackground(new GLColor(0x000000, 0.5f));
//		fps.setTextColor(new GLColor(0xffffff));
//		fps.setTextSize(80);
//
//		getRootView().addView(fps);

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
							getRootView().queueEvent(new Runnable() {
								@Override
								public void run() {
									String msg = "FPS : " + f;
									if (max > 0){
										msg +=  " [" + min + "~" + max + "]";
									}
//									fps.setText(msg);
									Log.e("fps=>", msg);
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

}
