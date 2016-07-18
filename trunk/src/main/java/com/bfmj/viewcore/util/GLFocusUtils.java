package com.bfmj.viewcore.util;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.opengl.Matrix;
import android.util.FloatMath;

import com.bfmj.viewcore.render.GLScreenParams;
import com.bfmj.viewcore.view.GLGroupView;
import com.bfmj.viewcore.view.GLRectView;
import com.bfmj.viewcore.view.GLView;

/**
 * 
 * ClassName: GLFocusUtils <br/>
 * @author lixianke    
 * @date: 2015-3-9 下午1:52:08 <br/>  
 * description:
 */
public class GLFocusUtils {
	public static final int TO_LEFT = 0;
	public static final int TO_RIGHT = 1;
	public static final int TO_UP = 2;
	public static final int TO_DOWN = 3;
	public static final int TO_UNKNOWN = 4;
	public static float[] headView;
	
	public static boolean isOpenHeadControl = true;
	
	private static OnCursorDepthChangeListener mCursorDepthChangeListener;
	
	private GLRectView mFocusedView;
	private int mComputeTimes = 0;
	
	/**
	 * 开启头控
	 * @author lixianke  @Date 2015-3-20 上午11:43:48
	 * @param 
	 * @return
	 */
	public static void openHeadControl(){
		isOpenHeadControl = true;		
	}
	
	/**
	 * 关闭头控
	 * @author lixianke  @Date 2015-3-20 上午11:43:48
	 * @param 
	 * @return
	 */
	public static void closeHeadControl(){
		isOpenHeadControl = false;
	}
	
	/**
	 * 获取拥有焦点的view索引
	 * @author lixianke  @Date 2015-3-20 上午11:45:07
	 * @param 
	 * @return 索引
	 */
	public GLRectView getFocusedView(){
		return mFocusedView;
	}
	
	public void setFousedView(GLRectView view){
		mFocusedView = view;
	}
	
	private int scale(float p, float s){
		float width = GLScreenParams.getXDpi() / 2;
		return (int)((p - width) * s + width);
	}
	
	public static void getEulerAngles(float[] eulerAngles, int offset) {
		getEulerAngles(headView, eulerAngles, offset);
	}
	
	@SuppressLint("FloatMath")
	public static void getEulerAngles(float[] headView, float[] eulerAngles, int offset) {
		if (offset + 3 > eulerAngles.length) {
			throw new IllegalArgumentException(
					"Not enough space to write the result");
		}
		float pitch = (float) Math.asin(headView[6]);
		float roll;
		float yaw;
		if (Math.sqrt(1.0F - headView[6] * headView[6]) >= 0.01F) {
			yaw = (float) Math.atan2(-headView[2], headView[10]);
			roll = (float) Math.atan2(-headView[4], headView[5]);
		} else {
			yaw = 0.0F;
			roll = (float) Math.atan2(headView[1], headView[0]);
		}
		
//		Log.d("video", "yaw = " + Math.toDegrees(-yaw) + "; pitch = " + Math.toDegrees(-pitch) + "; roll = " + Math.toDegrees(-roll));
		
		eulerAngles[(offset + 0)] = (-yaw);
		eulerAngles[(offset + 1)] = (-pitch);
		eulerAngles[(offset + 2)] = (-roll);
	}
	
	private float[] getCursorPosition(float offsetXAngle){
		float[] position = new float[2];
		float[] matrix = new float[16];
		
		System.arraycopy(headView, 0, matrix, 0, 16);
		
		if (offsetXAngle != 0){
			Matrix.rotateM(matrix, 0, offsetXAngle,0, 1, 0);
		}
		
		float[] out = new float[3];
		getEulerAngles(matrix, out, 0);
		
		float screenWidth = GLScreenParams.getScreenWidth();
		float screenHeight = GLScreenParams.getScreenHeight();
		float xDpi = GLScreenParams.getXDpi();
		float yDpi = GLScreenParams.getYDpi();
		float depth = GLScreenParams.getDefualtDepth() * GLRectView.getDepthScale();
		
		position[0] = xDpi/2 -(float)(depth * Math.tan(out[0])) / screenWidth * xDpi;
		position[1] = yDpi/2 - (float)(depth * Math.tan(out[1])) / screenHeight * yDpi;
		
		return position;
	}
	
	/**
	 * 处理焦点
	 * @author lixianke  @Date 2015-3-16 上午10:52:11
	 * @param headView 陀螺仪矩阵
	 * @param views View列表
	 * @return 
	 */
	public void handleFocused(float[] headView, ArrayList<GLView> views){
		GLFocusUtils.headView = headView;
		
		if (!isOpenHeadControl){
			return;
		}
		
		if (mComputeTimes < 20){
			mComputeTimes++;
			return;
		} else {
			mComputeTimes = 0;
		}
		
		boolean hasFocused = false;
		float x = -1;
		float y = -1;
		float defualtDepth = GLScreenParams.getDefualtDepth();
		GLRectView parentView = null;
		boolean isAdjustCursor = false;
		
		for (int i = views.size() - 1; i >= 0; i--) {
			if (! (views.get(i) instanceof GLRectView)){
				continue;
			}
			
			GLRectView v = (GLRectView)views.get(i);
			if (!v.isVisible()){
				continue;
			}
			
			if (v.getParent() == null && v instanceof GLGroupView 
					&& v.hasListeter() && v.isFocusable() && v.isEnable()){
				parentView = v;
			}
			
			// 计算相对于View选转后光标的位置
			float[] position = getCursorPosition(v.getLookAngle());
			x = position[0];
			y = position[1];
			
			float s = defualtDepth / v.getDepth();
			float vx1 = v.getLeft() + v.getX();
			float vy1 = v.getTop() + v.getY();

			float vx2 = vx1 + v.getWidth();
			float vy2 = vy1 + v.getHeight();

//			int valx1 = (int)((vx1 - 480) * s + 480);
//			int valx2 = (int)((vx2 - 480) * s + 480);
//
//			int valy1 = (int)((vy1 - 480) * s + 480);
//			int valy2 = (int)((vy2 - 480) * s + 480);

			if (x >= scale(vx1, s) && x < scale(vx2, s)
					&& y >= scale(vy1, s) && y < scale(vy2, s))
			//if( x >= vx1 && x < valx2 && y >= valy1 && y < valy2 )
			{
//				
				if (!isAdjustCursor) {
					isAdjustCursor = true;
					if (mCursorDepthChangeListener != null) {
						mCursorDepthChangeListener.onCursorDepthChange(v.getDepth());
					}
				}
//				
//				Log.d("video", "view x = " + x + "; y = " + y + "; s = " + s);
//				Log.d("video", "view v1 = " + vx1 + "; y = " + vy1);
//				Log.d("video", "view v2 = " + vx2 + "; y = " + vy2);
				
				if (!v.hasListeter() || !v.isFocusable() || !v.isEnable()){
					continue;
				}

				if (v != mFocusedView){
					if (mFocusedView != null && !v.isGrandParent(mFocusedView)){
						mFocusedView.onFocusChange(TO_UNKNOWN, false);
					}
					
					v.doRequestFocus();
				}
				
				hasFocused = true;
				mFocusedView = v;
				v.onHeadFocusChange(true);
				return;
			}
		}
		
		if (!hasFocused){
			if (parentView != null){
				if (parentView != mFocusedView){
					if (mFocusedView != null){
						mFocusedView.onFocusChange(TO_UNKNOWN, false);
					}
					parentView.onFocusChange(TO_UNKNOWN, true);
					mFocusedView = parentView;
					parentView.onHeadFocusChange(true);
				}
			} else if (mFocusedView != null){
				mFocusedView.onFocusChange(TO_UNKNOWN, false);
				mFocusedView = null;
			}
		}
		
		if (mCursorDepthChangeListener != null && !isAdjustCursor) {
			mCursorDepthChangeListener.onCursorDepthChange(GLScreenParams.getDefualtDepth());
		}
	}
	
	/**
	 * 处理焦点
	 * @author lixianke  @Date 2015-3-16 上午10:52:11
	 * @param views View列表
	 * @return 
	 */
	public void handleFocused(ArrayList<GLView> views){
		if (!isOpenHeadControl){
			return;
		}
		
		handleFocused(GLFocusUtils.headView, views);
	}
	
	/**
	 * 处理焦点
	 * @author lixianke  @Date 2015-3-16 上午10:52:11
	 * @param views View列表
	 * @return 
	 */
	public boolean handleFocused(int direction, GLRectView view, ArrayList<GLRectView> views){
		if (isOpenHeadControl){
			return false;
		}
		
		mFocusedView = view;
		
		GLRectView focusedView = null;
		float x = -10000;
		float y = -10000;
		float vx11 = 0;
		float vx12 = 0;
		float vy11 = 0;
		float vy12 = 0;
		float vx21 = 0;
		float vx22 = 0;
		float vy21 = 0;
		float vy22 = 0;
		
		for (int i = 0; i < views.size(); i++) {
			GLRectView v = views.get(i);
			
			if (!v.isFocusable() || !v.isVisible() || !v.isEnable()){
				continue;
			}
			
			vx11 = v.getLeft() + v.getX();
			vx12 = v.getLeft() + v.getX() + v.getWidth();
			vy11 = v.getTop() + v.getY();
			vy12 = v.getTop() + v.getY() + v.getHeight();
			
			switch (direction) {
				case TO_LEFT:
					if (view != null){
						vx21 = view.getLeft() + view.getX();
						vy21 = view.getTop() + view.getY();
						vy22 = view.getTop() + view.getY() + view.getHeight();
						if (vx12 <= vx21 && (vx12 > x || (vx12 == x && Math.abs(vy21 + vy22 - vy11 - vy12) < Math.abs(vy21 + vy22 - y)))){
							x = vx12;
							y = vy11 + vy12;
							focusedView = v;
						}
					} else {
						if (vx12 > x){
							x = vx12;
							focusedView = v;
						}
					}
					break;
				case TO_RIGHT:
					if (view != null){
						vx22 = view.getLeft() + view.getX() + view.getWidth();
						vy21 = view.getTop() + view.getY();
						vy22 = view.getTop() + view.getY() + view.getHeight();
						if (vx11 >= vx22 && ((vx11 < x || x == -10000) || (vx11 == x && Math.abs(vy21 + vy22 - vy11 - vy12) < Math.abs(vy21 + vy22 - y)))){
							x = vx11;
							y = vy11 + vy12;
							focusedView = v;
						}
					} else {
						if (vx11 < x || x == -10000){
							x = vx11;
							focusedView = v;
						}
					}
					break;
				case TO_UP:
					if (view != null){
						vx21 = view.getLeft() + view.getX();
						vx22 = view.getLeft() + view.getX() + view.getWidth();
						vy21 = view.getTop() + view.getY();
						if (vy12 <= vy21 && (vy12 > y || (vy12 == y && Math.abs(vx21 + vx22 - vx11 - vx12) < Math.abs(vx21 + vx22 - x)))){
							x = vx11 + vx12;
							y = vy12;
							focusedView = v;
						}
					} else {
						if (vy12 > y){
							y = vy12;
							focusedView = v;
						}
					}
					break;
				case TO_DOWN:
					if (view != null){
						vx21 = view.getLeft() + view.getX();
						vx22 = view.getLeft() + view.getX() + view.getWidth();
						vy22 = view.getTop() + view.getY() + view.getHeight();
						if (vy11 >= vy22 && ((vy11 < y || y == -10000) || (vy11 == y && Math.abs(vx21 + vx22 - vx11 - vx12) < Math.abs(vx21 + vx22 - x)))){
							x = vx11 + vx12;
							y = vy11;
							focusedView = v;
						}
					} else {
						if (vy11 > y || y == -10000){
							y = vy11;
							focusedView = v;
						}
					}
					break;
				default:
					break;
			}
		}
		
		if (focusedView != null && mFocusedView != focusedView){
			if (mFocusedView != null){
				mFocusedView.onFocusChange(direction, false);
			}
			focusedView.onFocusChange(direction, true);
			mFocusedView = focusedView;
			
			return true;
		}
		
		return false;
	}
	
	public static void setOnCursorDepthChangeListener(OnCursorDepthChangeListener listener){
		mCursorDepthChangeListener = listener;
	}
	
	public interface OnCursorDepthChangeListener {
		void onCursorDepthChange(float depth);
	}
}
