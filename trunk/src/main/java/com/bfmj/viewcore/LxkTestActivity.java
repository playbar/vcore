package com.bfmj.viewcore;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.bfmj.viewcore.adapter.GLListAdapter;
import com.bfmj.viewcore.interfaces.GLViewFocusListener;
import com.bfmj.viewcore.interfaces.IGLPlayerListener;
import com.bfmj.viewcore.player.GLSystemPlayer;
import com.bfmj.viewcore.render.GLColor;
import com.bfmj.viewcore.view.GLCursorView;
import com.bfmj.viewcore.view.GLGroupView;
import com.bfmj.viewcore.view.GLImageView;
import com.bfmj.viewcore.view.GLPlayerView;
import com.bfmj.viewcore.view.GLRectView;
import com.bfmj.viewcore.view.GLRootView;
import com.bfmj.viewcore.view.GLSenceView;
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
		GLImageView[] imageViews = new GLImageView[6];

		imageViews[0] = new GLImageView(this);
		imageViews[0].setLayoutParams(getX(460), getY(440), getWidth(40), getHeight(40));

		imageViews[1] = new GLImageView(this);
		imageViews[1].setLayoutParams(getX(460), getY(360), getWidth(40), getHeight(40));

		imageViews[2] = new GLImageView(this);
		imageViews[2].setLayoutParams(getX(460), getY(280), getWidth(40), getHeight(40));

		imageViews[3] = new GLImageView(this);
		imageViews[3].setLayoutParams(getX(460), getY(200), getWidth(40), getHeight(40));

		imageViews[4] = new GLImageView(this);
		imageViews[4].setLayoutParams(getX(460), getY(120), getWidth(40), getHeight(40));

		imageViews[5] = new GLImageView(this);
		imageViews[5].setLayoutParams(getX(460), getY(40), getWidth(40), getHeight(40));

		GLImageView line = new GLImageView(this);
		line.setLayoutParams(479, 0, 2, 960);
		line.setBackground(new GLColor(0, 1, 0));
		rootView.addView(line);



		for (int i = 0; i < imageViews.length; i++) {
			imageViews[i].setBackground(new GLColor(1, 1, 1));
			imageViews[i].setDepth(40f);
			imageViews[i].setFocusListener(listener);
			rootView.addView(imageViews[i]);
		}

		GLCursorView cursorView = new GLCursorView(this);
		cursorView.setLayoutParams(475, 475, 10, 10);
		cursorView.setBackground(new GLColor(1.0f, 0, 0));
		cursorView.setDepth(3);
		rootView.addView(cursorView);

		GLTextView textView = new GLTextView(this);
		textView.setText("北京欢迎你");
		textView.setTextColor( new GLColor( 1.0f, 1.0f, 1.0f ));
		textView.setLayoutParams(475, 480, 10, 10 );
		textView.setAlpha(0.3f);
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
		return (int)((x - 480) * scaleX + 480);
	}

	private int getY(int y){
		return (int)((y - 480) * scaleY + 480);
	}

	private int getWidth(int w){
		return (int)(w * scaleX);
	}

	private int getHeight(int h){
		return (int)(h * scaleY);
	}

}
