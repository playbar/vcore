package com.viewcore.test;

import android.content.Context;

import com.bfmj.viewcore.animation.GLAlphaAnimation;
import com.bfmj.viewcore.animation.GLAnimation;
import com.bfmj.viewcore.animation.GLTranslateAnimation;
import com.bfmj.viewcore.view.GLImageView;
import com.bfmj.viewcore.view.GLRectView;
import com.bfmj.viewcore.view.GLRelativeView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yushaochen on 2016/11/8.
 */

public class DownloadIconView extends GLRelativeView {

    private Timer timer;

    private float y;
    public DownloadIconView(Context context) {
        super(context);
        this.setLayoutParams(56f,56f*3);
        setVisible(true);
        createOneView(context);
        createTwoView(context);
    }
    private GLImageView twoView;
    private void createTwoView(Context context) {
        twoView = new GLImageView(context);
        twoView.setLayoutParams(56f,56f);
        setBackground(R.drawable.state_icon_download);
        //this.addView(twoView);
    }
    private GLImageView oneView;
    private void createOneView(Context context) {
        oneView = new GLImageView(context);
        oneView.setLayoutParams(56f,56f);
        setBackground(R.drawable.state_icon_download);
        this.addView(oneView);
    }

    public void show(){
        y = getY();

        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                startAnimationOne(oneView);
                //startAnimationTwo(twoView);
            }
        }, 0, 3000);
    }

    public void dismiss(){
        setVisible(false);
        timer.cancel();
    }

    private void startAnimationOne(GLRectView view){
        AlphaAnimation_show(view);
        TranslateAnimation_down1(view);
    }

    private void startAnimationTwo(GLRectView view){
        AlphaAnimation_hide(view);
        TranslateAnimation_down2(view);
    }

    private void AlphaAnimation_hide(GLRectView view) {
        // 淡出
        GLAnimation animation_hide = new GLAlphaAnimation(1, 0);
        animation_hide.setAnimView(view);
        animation_hide.setDuration(3000);
        view.startAnimation(animation_hide);
    }

    private void AlphaAnimation_show(final GLRectView view) {
        // 淡入
        GLAnimation animation_show = new GLAlphaAnimation(0, 1);
        animation_show.setAnimView(view);
        animation_show.setDuration(3000);
        view.startAnimation(animation_show);
    }

    private void TranslateAnimation_down1(final GLRectView view) {
        view.setY(getY());
        GLAnimation animation_down1 = new GLTranslateAnimation(0f, view.getHeight(), 0f);
        animation_down1.setAnimView(view);
        animation_down1.setDuration(3000);
        animation_down1.setOnGLAnimationListener(new GLAnimation.OnGLAnimationListener() {
            @Override
            public void onAnimationStart(GLAnimation glAnimation) {

            }

            @Override
            public void onAnimationEnd(GLAnimation glAnimation) {

            }
        });
        view.startAnimation(animation_down1);
    }

    private void TranslateAnimation_down2(GLRectView view) {
        //setY(y+12);
        GLAnimation animation_down1 = new GLTranslateAnimation(0f, view.getHeight(), 0f);
        animation_down1.setAnimView(view);
        animation_down1.setDuration(3000);
        animation_down1.setOnGLAnimationListener(new GLAnimation.OnGLAnimationListener() {
            @Override
            public void onAnimationStart(GLAnimation glAnimation) {

            }

            @Override
            public void onAnimationEnd(GLAnimation glAnimation) {

            }
        });
        view.startAnimation(animation_down1);
    }
}
