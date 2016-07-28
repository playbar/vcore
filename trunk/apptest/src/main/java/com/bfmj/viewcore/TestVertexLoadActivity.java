package com.bfmj.viewcore;

import android.os.Bundle;

import com.bfmj.viewcore.util.GLExtraData;
import com.bfmj.viewcore.view.BaseViewActivity;
import com.bfmj.viewcore.view.GLSenceView;

/**
 * Created by lixianke on 2016/7/28.
 */
public class TestVertexLoadActivity extends BaseViewActivity {

    public static final int SCENE_TYPE_DEFAULT = 0x0;
    public static final int SCENE_TYPE_CINEMA = 0x1;
    private int mSceneType = -1;
    private GLSenceView[] mSceneViews = new GLSenceView[6];
    private int[]  mDefaultSceneResourceIds = {
            R.drawable.launch_cube_front, //顺序前、左、后、右、上、下
            R.drawable.launch_cube_left,
            R.drawable.launch_cube_back,
            R.drawable.launch_cube_right,
            R.drawable.launch_cube_top,
            R.drawable.launch_cube_bottom
    };

    private int[]  mCinemaSceneResourceIds = {
            R.drawable.cinema_cube_front, //顺序前、左、后、右、上、下
            R.drawable.cinema_cube_left,
            R.drawable.cinema_cube_back,
            R.drawable.cinema_cube_right,
            R.drawable.cinema_cube_top,
            R.drawable.cinema_cube_bottom
    };

    private String[] mSceneObjs = {
            "gl_cube_front.obj",
            "gl_cube_left.obj",
            "gl_cube_back.obj",
            "gl_cube_right.obj",
            "gl_cube_top.obj",
            "gl_cube_bottom.obj"
    };

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
                        senceView2.setObjFile("qiu.obj");
                        senceView2.setImage(R.drawable.four);
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

        for (int i = 0; i < 6; i++){
            if (mSceneViews[i] == null){
                mSceneViews[i] = new GLSenceView(this);
                getRootView().addView(mSceneViews[i]);
                if(i == 4){
                    mSceneViews[i].rotate(90, 0, 1, 0);
                } else if(i == 5){
                    mSceneViews[i].rotate(180, 0, 1, 0);
                }

                mSceneViews[i].setObjFile(mSceneObjs[i]);
            }

            if(type == SCENE_TYPE_CINEMA){
                mSceneViews[i].setImage(mCinemaSceneResourceIds[i]);
            } else {
                mSceneViews[i].setImage(mDefaultSceneResourceIds[i]);
            }

            mSceneViews[i].setVisible(true);
        }
    }

    /**
     * 隐藏天空盒场景
     */
    public void hideSkyBox(){
        for (int i = 0; i < 6; i++){
            if (mSceneViews[i] != null){
                mSceneViews[i].setVisible(false);
            }
        }
    }

}
