package com.bfmj.viewcore;

import android.os.Bundle;

import com.bfmj.viewcore.view.BaseViewActivity;
import com.bfmj.viewcore.view.GLSenceView;

/**
 * Created by lixianke on 2016/7/28.
 */
public class TestVertexLoadActivity extends BaseViewActivity {

    public static final int SCENE_TYPE_DEFAULT = 0x0;
    public static final int SCENE_TYPE_CINEMA = 0x1;
    private int mSceneType = -1;
    private GLSenceView mSkyboxView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showSkyBox(SCENE_TYPE_DEFAULT);

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
                        hideSkyBox();
                        GLSenceView senceView2 = new GLSenceView(TestVertexLoadActivity.this);
                        senceView2.setObjFile("sphere.obj");
                        senceView2.setImage(R.drawable.sence);
                        getRootView().addView(senceView2);
                    }
                });
            }
        }).start();
    }

    /**
     * 显示天空盒场景
     * @param type 场景类型
     */
    public void showSkyBox(int type){
        if (mSceneType == type && mSceneType != -1){
            return;
        }
        mSceneType = type;

        if (mSkyboxView == null){
            mSkyboxView = new GLSenceView(this);
            mSkyboxView.setObjFile("skybox.obj");
            getRootView().addView(mSkyboxView);
        }

        if(type == SCENE_TYPE_CINEMA){
            mSkyboxView.setImage(R.drawable.skybox_launcher);
        } else {
            mSkyboxView.setImage(R.drawable.skybox_launcher);
        }

        mSkyboxView.setVisible(true);
    }

    /**
     * 隐藏天空盒场景
     */
    public void hideSkyBox(){
        if (mSkyboxView != null){
            mSkyboxView.setVisible(false);
        }
    }

}
