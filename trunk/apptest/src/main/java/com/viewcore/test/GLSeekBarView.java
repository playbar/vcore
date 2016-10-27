package com.viewcore.test;

import android.content.Context;

import com.baofeng.mojing.input.base.MojingKeyCode;
import com.bfmj.viewcore.interfaces.GLOnKeyListener;
import com.bfmj.viewcore.interfaces.GLViewFocusListener;
import com.bfmj.viewcore.render.GLColor;
import com.bfmj.viewcore.util.GLFocusUtils;
import com.bfmj.viewcore.view.GLImageView;
import com.bfmj.viewcore.view.GLProcessView;
import com.bfmj.viewcore.view.GLRectView;
import com.bfmj.viewcore.view.GLTextView;

import java.util.Timer;
import java.util.TimerTask;


public class GLSeekBarView extends GLProcessView {
    private int mResImg;
    private GLImageView mBarView;
//    private IPlayerControlCallBack mCallBack;
    private int bar_width = 100;
    private int bar_height=20;

    public void setBarImage(int barImage){
        this.mResImg = barImage;
        initView();
    }
    public GLSeekBarView(Context context){
        super(context);
    }

    private void initView(){
        mBarView = new GLImageView(getContext());
        mBarView.setImage(mResImg);
        mBarView.setLayoutParams(bar_width,bar_height);
        this.addView(mBarView);
    }

    @Override
    public void addView(GLRectView view) {
        view.setX(this.getX() + this.getPaddingLeft() + view.getMarginLeft());
        view.setY(this.getY() + this.getPaddingTop() + view.getMarginTop());
        super.addView(view);
    }

    @Override
    public void setProcess(int process) {
        float width = (this.getWidth() - bar_width - this.getPaddingLeft() - this.getPaddingRight()) / 100.0F * process;
//        this.mBarView.setLayoutParams(bar_width ,bar_height);
        this.mBarView.setX(this.getX() + this.getPaddingLeft()+width);
        this.mBarView.setY(this.getY() + this.getPaddingTop());
    }

    private GLOnKeyListener mKeyListener = new GLOnKeyListener() {
        @Override
        public boolean onKeyDown(GLRectView glRectView, int keyCode) {
            if(keyCode== MojingKeyCode.KEYCODE_ENTER){
                float[] pos = GLFocusUtils.getPosition(GLSeekBarView.this.getMatrixState().getVMatrix(), GLSeekBarView.this.getDepth());
                int x=(int)pos[0];//;,y=xy[1];
                float viewX = GLSeekBarView.this.getX();
                float viewWidth = GLSeekBarView.this.getWidth();
                if(x>=viewX&&x< viewX+viewWidth){
                    float process =(x-viewX)/viewWidth;
//                    int current = (int) (mDuration*process);
                }
            }
            return false;
        }
        @Override
        public boolean onKeyUp(GLRectView glRectView, int i) {
            return false;
        }

        @Override
        public boolean onKeyLongPress(GLRectView glRectView, int i) {
            return false;
        }
    };

}
