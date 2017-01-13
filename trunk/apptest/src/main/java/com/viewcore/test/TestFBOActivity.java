package com.viewcore.test;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;

import com.bfmj.viewcore.view.BaseViewActivity;
import com.bfmj.viewcore.view.GLCursorView;
import com.bfmj.viewcore.view.GLImageView;
import com.bfmj.viewcore.view.GLPanoView;

import java.io.IOException;
import java.util.Random;

/**
 * Created by lixianke on 2016/7/28.
 */
public class TestFBOActivity extends BaseViewActivity {

    public static final int SCENE_TYPE_DEFAULT = 0x0;
    public static final int SCENE_TYPE_CINEMA = 0x1;
    private int mSceneType = -1;
    MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        showSkyBox(SCENE_TYPE_DEFAULT);

        GLImageView imageViews = new GLImageView(this);
        imageViews.setX(700);
        imageViews.setY(700);
        imageViews.setLayoutParams(1000, 1000);
        imageViews.setImage(R.drawable.bird);
        imageViews.setAlpha(0.5f);
//		imageViews[i].setDepth(4 - (i - 100)*0.005f);
        getRootView().addView(imageViews);


//        GLCursorView cursorView = new GLCursorView(this);
//        cursorView.setX( 1150);
//        cursorView.setY( 1150);
//        cursorView.setLayoutParams(100, 100);
//        cursorView.setBackground(R.drawable.a1);
//        cursorView.setDepth(3.9f);
////		cursorView.setFixed(false);
//        getRootView().addView(cursorView);

        showSkyBox(SCENE_TYPE_CINEMA);

    }

    public void showVideo(){
        final GLPanoView panoView = GLPanoView.getSharedPanoView(this);
        panoView.setRenderType(GLPanoView.RENDER_TYPE_VIDEO);

        player = new MediaPlayer();


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                getRootView().queueEvent(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            player.setDataSource("/mnt/sdcard/test.mp4");
                            player.setSurface(new Surface(panoView.getSurfaceTexture()));

                            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mp) {
                                    mp.start();
                                }
                            });
                            player.prepareAsync();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

//                getRootView().queueEvent(new Runnable() {
//                    @Override
//                    public void run() {
//                        GLPanoView  senceView2 = GLPanoView.getSharedPanoView(TestVertexLoadActivity.this);
//                        senceView2.reset();
//                        senceView2.setSceneType(GLPanoView.SCENE_TYPE_HALF_SPHERE);
//                        senceView2.setImage(R.drawable.sence);
//                    }
//                });
            }
        }).start();
        return;
    }

    /**
     * 显示天空盒场景
     * @param type 场景类型
     */
    public void showSkyBox(int type){
        mSceneType = type;

        GLPanoView  mSkyboxView = GLPanoView.getSharedPanoView(this);
//        mSkyboxView.setRenderType(GLPanoView.RENDER_TYPE_VIDEO);
//        mSkyboxView.setPlayType(GLPanoView.PLAY_TYPE_3D_LR);

        if(type == SCENE_TYPE_CINEMA){
            mSkyboxView.setSceneType(GLPanoView.SCENE_TYPE_SPHERE);
            mSkyboxView.setImage(R.drawable.sphere);
        } else {
            mSkyboxView.setImage(R.drawable.skybox_launcher);
            mSkyboxView.setLeftImage(R.drawable.strip);
        }

        mSkyboxView.setVisible(true);
    }

    /**
     * 隐藏天空盒场景
     */
    public void hideSkyBox(){
        GLPanoView.getSharedPanoView(this).setVisible(false);
    }

    @Override
    protected void onDestroy() {
        GLPanoView.finish();
        super.onDestroy();
    }
}
