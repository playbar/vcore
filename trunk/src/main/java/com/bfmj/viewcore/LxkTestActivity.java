package com.bfmj.viewcore;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.bfmj.viewcore.interfaces.IGLPlayerListener;
import com.bfmj.viewcore.player.GLSystemPlayer;
import com.bfmj.viewcore.view.GLPlayerView;
import com.bfmj.viewcore.view.GLRootView;
import com.bfmj.viewcore.view.GLSenceView;

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

		GLSenceView senceView = new GLSenceView(this);
		senceView.setImage(R.drawable.sence);
		senceView.setObjFile("qiu.obj");
		senceView.scale(4.0f);
		senceView.rotate(180, 0, 0, 1);
		senceView.rotate(180, 0, 1, 0);
		rootView.addView(senceView);
	}
}
