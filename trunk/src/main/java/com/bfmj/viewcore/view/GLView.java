package com.bfmj.viewcore.view;

import com.bfmj.viewcore.interfaces.GLRenderListener;
import com.bfmj.viewcore.render.GLScreenParams;
import com.bfmj.viewcore.util.GLMatrixState;
import com.bfmj.viewcore.util.GLTextureUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.Matrix;
import android.util.FloatMath;

/**
 * 
 * ClassName: GLView <br/>
 * @author lixianke    
 * @date: 2015-3-9 下午1:53:05 <br/>  
 * description:
 */
public abstract class GLView implements GLRenderListener {
	private Context mContext;
	private GLMatrixState mMatrixState;
	private float mAlpha = 1.0f;
	private float mMask = 1.0f;
	private boolean isVisible = true;
	private float mEyeDeviation = GLScreenParams.getEyeDistance();
	
	public GLView(Context context){
		mContext = context;
		mMatrixState = new GLMatrixState();
	}

	public void requestLayout(){};
	/**
	 * 获取上下文
	 * @author lixianke  @Date 2015-6-19 下午5:13:00
	 * @param  
	 * @return 上下文引用
	 */
	protected Context getContext() {
		return mContext;
	}
	
	/**
	 * 初始化绘制，一般由系统调用
	 * 如果自行调用，需放到GL绘制线程
	 * @author lixianke  @Date 2015-6-19 下午5:08:11
	 * @param 
	 * @return
	 */
	public abstract void initDraw();
	
	/**
	 * 绘制，由系统自动调用
	 * @author lixianke  @Date 2015-6-19 下午5:10:26
	 * @param isLeft 是否为左屏 
	 * @return
	 */
	public abstract void draw(boolean isLeft);
	
	/**
	 * 释放资源，由系统自动调用
	 * @author lixianke  @Date 2015-6-19 下午5:11:50
	 * @param 
	 * @return
	 */
	public abstract void release();
	
	/**
	 * 获取是否可见
	 * @author lixianke  @Date 2015-6-19 下午5:07:34
	 * @param 
	 * @return 是否可见
	 */
	public boolean isVisible() {
		return isVisible;
	}
	
	/**
	 * 设置是否可见
	 * @author lixianke  @Date 2015-6-19 下午5:06:46
	 * @param isVisible 是否可见
	 * @return
	 */
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
	
	public GLMatrixState getMatrixState(){
		return mMatrixState;
	}
	
	/**
	 * View切换到前台
	 * @author lixianke  @Date 2015-6-19 下午5:03:23
	 * @param  
	 * @return
	 */
	public void onResume(){
		
	}
	
	/**
	 * View切换到后台
	 * @author lixianke  @Date 2015-6-19 下午5:05:58
	 * @param 
	 * @return
	 */
	public void onPause(){
		
	}
	
	/**
	 * 设置透明度
	 * @author lixianke  @Date 2015-3-12 下午3:50:08
	 * @param alpha 透明度值，0~1之间 
	 * @return
	 */
	public void setAlpha(float alpha){
    	mAlpha = alpha;
    }
	
	/**
	 * 获取透明度
	 * @author lixianke  @Date 2015-3-12 下午4:15:05
	 * @param
	 * @return 透明度值，0~1之间
	 */
	public float getAlpha(){
    	return mAlpha;
    }
	
	/**
     * 设置View亮度
     * @author lixianke  @Date 2015-7-9 下午2:49:02
     * @param mask 亮度0-1
     * @return
     */
    public void setMask(float mask){
    	mMask = mask;
    }
    
    /**
     * 获取View亮度
     * @author lixianke  @Date 2015-7-9 下午2:56:19
     * @param
     * @return 亮度值，0~1之间
     */
    public float getMask(){
    	return mMask;
    }
	
	@SuppressLint("FloatMath")
	protected void getEyeMatrix(float[] headView, boolean isLeft) {
		double yaw;
		if (Math.sqrt(1.0F - headView[6] * headView[6]) >= 0.01F) {
			yaw = Math.toDegrees((double) Math.atan2(-headView[2],headView[10]));
		} else {
			yaw = 0.0F;
		}
		
		Matrix.rotateM(headView, 0, -(float)yaw, 0, 1, 0);
		
		if (isLeft){
        	Matrix.translateM(headView, 0, mEyeDeviation, 0, 0);
        } else {
        	Matrix.translateM(headView, 0, -mEyeDeviation, 0, 0);
        }
		
		Matrix.rotateM(headView, 0, (float)yaw, 0, 1, 0);		
	}

	public float getEyeDeviation() {
		return mEyeDeviation;
	}

	public void setEyeDeviation(float mEyeDeviation) {
		this.mEyeDeviation = mEyeDeviation;
	}
	
	protected void releaseTexture(final int textureId) {
		GLTextureUtils.releaseTexture(textureId);
	}
}
