package com.bfmj.viewcore;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.bfmj.viewcore.interfaces.GLViewFocusListener;
import com.bfmj.viewcore.player.GLSystemPlayer;
import com.bfmj.viewcore.render.GLColor;
import com.bfmj.viewcore.view.BaseViewActivity;
import com.bfmj.viewcore.view.GLCursorView;
import com.bfmj.viewcore.view.GLImageView;
import com.bfmj.viewcore.view.GLRectView;
import com.bfmj.viewcore.view.GLRootView;
import com.bfmj.viewcore.view.GLTextView;

public class LxkTestActivity extends BaseViewActivity {
	private GLRootView rootView;

	GLSystemPlayer player;


	@SuppressLint("SdCardPath")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.setDistortionEnable(false);
		super.onCreate(savedInstanceState);

		rootView = getRootView();
		//rootView.setDoubleScreen(false);
		rootView.onResume();

		//////////////
		GLViewFocusListener listener = new GLViewFocusListener() {

			@Override
			public void onFocusChange(GLRectView view, boolean focused) {
				if (focused){
					view.setBackground(new GLColor(0, 0, 1));
				} else {
					view.setBackground(new GLColor(1, 1, 1));
				}
			}
		};

		GLImageView[] imageViews = new GLImageView[10];

		imageViews[0] = new GLImageView(this);
		imageViews[0].setX(getX(1150));
		imageViews[0].setY(getY(1150));
		imageViews[0].setLayoutParams( getWidth(100), getHeight(100));

		imageViews[1] = new GLImageView(this);
		imageViews[1].setX(getX(850));
		imageViews[1].setY(getY(1150));
		imageViews[1].setLayoutParams(getWidth(100), getHeight(100));

		imageViews[2] = new GLImageView(this);
		imageViews[2].setX(getX(550));
		imageViews[2].setY(getY(1150));
		imageViews[2].setLayoutParams(getWidth(100), getHeight(100));

		imageViews[3] = new GLImageView(this);
		imageViews[3].setX(getX(250));
		imageViews[3].setY(getY(1150));
		imageViews[3].setLayoutParams( getWidth(100), getHeight(100));

		imageViews[4] = new GLImageView(this);
		imageViews[4].setX(getX(1150));
		imageViews[4].setY(getY(850));
		imageViews[4].setLayoutParams(getWidth(100), getHeight(100));

		imageViews[5] = new GLImageView(this);
		imageViews[5].setX(getX(1150));
		imageViews[5].setY(getY(550));
		imageViews[5].setLayoutParams(getWidth(100), getHeight(100));

		imageViews[6] = new GLImageView(this);
		imageViews[6].setX(getX(1150));
		imageViews[6].setY(getY(250));
		imageViews[6].setLayoutParams( getWidth(100), getHeight(100));

		imageViews[7] = new GLImageView(this);
		imageViews[7].setX(getX(850));
		imageViews[7].setY(getY(850));
		imageViews[7].setLayoutParams(getWidth(100), getHeight(100));

		imageViews[8] = new GLImageView(this);
		imageViews[8].setX( getX(550));
		imageViews[8].setY( getY(550));
		imageViews[8].setLayoutParams(getWidth(100), getHeight(100));

		imageViews[9] = new GLImageView(this);
		imageViews[9].setX( getX( 250));
		imageViews[9].setY( getY(250));
		imageViews[9].setLayoutParams(getWidth(100), getHeight(100));

		GLImageView line = new GLImageView(this);
		line.setX( 1198);
		line.setY( 0);
		line.setLayoutParams(4, 2400);
		line.setBackground(new GLColor(0, 1, 0));
		rootView.addView(line);



		for (int i = 0; i < imageViews.length; i++) {
			imageViews[i].setBackground(new GLColor(1, 1, 1));
//			imageViews[i].setDepth(f);
			imageViews[i].setFocusListener(listener);
			rootView.addView(imageViews[i]);
		}

//		imageViews[0].setBackground(new GLColor(1, 0, 0));

		GLCursorView cursorView = new GLCursorView(this);
		cursorView.setX( 1190);
		cursorView.setY( 1190);
		cursorView.setLayoutParams(20, 20);
		cursorView.setBackground(new GLColor(1.0f, 0, 0));
		cursorView.setDepth(3);
		rootView.addView(cursorView);

		GLTextView textView = new GLTextView(this);
		textView.setX( 1000 );
		textView.setY( 1200);
		textView.setLayoutParams( 1000, 200 );
		textView.setTextColor(new GLColor(0.0f, 1.0f, 1.0f));
		textView.setText("北京欢迎你");
		textView.setTextSize(100);
		rootView.addView(textView);


// 		GLImageView imageView = new GLImageView(this);
//		imageView.setImage(R.drawable.ic_launcher);
//		imageView.setLayoutParams(460, 460, 40, 40);
//
//		GLSenceView senceView = new GLSenceView(this);
//		senceView.setImage(R.drawable.k4_left3);
//		senceView.setObjFile("qiu.obj");
//		senceView.scale(4);
//		senceView.rotate(180, 0, 0, 1);
//		senceView.rotate(180, 0, 1, 0);
//		rootView.addView(senceView);
//
//		GLSenceView sence1 = new GLSenceView(this);
//		//sence1.setImage(R.drawable.gl_sence_half);
//		sence1.setObjFile("left.obj");
////		sence1.scale(0.4f);
//		sence1.rotate(-90, 0, 0, 1);
////		sence1.translate(6, 0, 0);
//		sence1.setVisible(true);
//		rootView.addView(sence1);
//		// 地面
//		GLSenceView sence2 = new GLSenceView(this);
//		//sence2.setImage(R.drawable.gl_purplehome_ground);
//		sence2.setObjFile("gl_purplehome_ground.obj");
////		sence2.rotate(180, 0, 1, 0);
//		sence2.setVisible(true);
//		rootView.addView(sence2);

		//////////

//		player = new GLSystemPlayer(this);
//		player.setVideoPath("/mnt/sdcard/111/1.mp4");
//		player.setLayoutParams(0, 0, 960, 960);
////		player.set3D(true);
//		//player.rotate(90);
//		player.setListener(new IGLPlayerListener() {
//
//			@Override
//			public void onVideoSizeChanged(GLPlayerView player, int width, int height) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void onTimedText(GLPlayerView player, String text) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void onSeekComplete(GLPlayerView player) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void onPrepared(GLPlayerView player) {
//				player.start();
//			}
//
//			@Override
//			public boolean onInfo(GLPlayerView player, int what, Object extra) {
//				// TODO Auto-generated method stub
//				return false;
//			}
//
//			@Override
//			public boolean onError(GLPlayerView player, int what, int extra) {
//				// TODO Auto-generated method stub
//				return false;
//			}
//
//			@Override
//			public void onCompletion(GLPlayerView player) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void onBufferingUpdate(GLPlayerView player, int percent) {
//				// TODO Auto-generated method stub
//
//			}
//		});
//
//		rootView.addView(player);

//		GLSenceView senceView = new GLSenceView(this);
//		senceView.setImage(R.drawable.sence);
//		senceView.setObjFile("qiu.obj");
//		senceView.scale(4.0f);
//		senceView.rotate(180, 0, 0, 1);
//		senceView.rotate(180, 0, 1, 0);
//		rootView.addView(senceView);
	}

	private static float scaleX = 6.8f * 1.75f;
	private static float scaleY = 6.8f * 1.75f;

	private int getX(int x){
		return x;
	}

	private int getY(int y){
		return y;
	}

	private int getWidth(int w){
		return w;
	}

	private int getHeight(int h){
		return h;
	}

}
