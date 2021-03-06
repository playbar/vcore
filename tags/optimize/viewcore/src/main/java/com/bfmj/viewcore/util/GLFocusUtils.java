package com.bfmj.viewcore.util;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.opengl.Matrix;
import android.util.Log;

import com.baofeng.mojing.MojingSDK;
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

	private float getX(float x){
		return (x - GLScreenParams.getXDpi() / 2) / GLScreenParams.getXDpi() * GLScreenParams.getScreenWidth();
	}

	private float getY(float y){
		return (GLScreenParams.getYDpi() / 2 - y) / GLScreenParams.getYDpi() * GLScreenParams.getScreenHeight();
	}

	private static int[] mCurosrPosition = new int[]{-1, -1};

	/**
	 * 获取当前的焦点位置
	 * @return 焦点位置
     */
	public static int[] getCursorPosition(){
		return mCurosrPosition;
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

		float defualtDepth = GLScreenParams.getDefualtDepth();
		boolean isAdjustCursor = false;
		boolean hasFocused = false;

		float x1 = getX(0);
		float y1 = getY(0);

		float x2 = getX(GLScreenParams.getXDpi());
		float y2 = getY(GLScreenParams.getYDpi());

		float[] outPos = new float[]{0, 0};

		boolean isFoused = MojingSDK.DirectionalRadiaInRect(headView, new float[]{x1, y1}, new float[]{x2, y2}, -defualtDepth, outPos);

		float rate = GLScreenParams.getXDpi() / GLScreenParams.getScreenWidth();

		mCurosrPosition[0] = (int)(outPos[0] * rate);
		mCurosrPosition[1] = (int)(outPos[1] * rate);

//		ArrayList<Float> leftTop = new ArrayList<>();
//		ArrayList<Float> bottomRight = new ArrayList<>();
		if (isFoused) {

			for (int i = views.size() - 1; i >= 0; i--) {
				if (!(views.get(i) instanceof GLRectView)) {
					continue;
				}

				GLRectView v = (GLRectView) views.get(i);
				if (!v.isVisible() || !v.hasListeter() || !v.isFocusable() || !v.isEnable()) {
					continue;
				}

				float vx1 = v.getLeft() + v.getX();
				float vy1 = v.getTop() + v.getY();

				float vx2 = v.getLeft() + v.getX() + v.getWidth();
				float vy2 = v.getTop() + v.getY() + v.getHeight();

//				float vx1 = getX(v.getLeft() + v.getX());
//				float vy1 = getY(v.getTop() + v.getY());
//
//				float vx2 = getX(v.getLeft() + v.getX() + v.getWidth());
//				float vy2 = getY(v.getTop() + v.getY() + v.getHeight());
//
//				leftTop.add(vx1);
//				leftTop.add(vy1);
//				leftTop.add(-v.getDepth());
//
//				bottomRight.add(vx2);
//				bottomRight.add(vy2);
//				bottomRight.add(-v.getDepth());

				if (vx1 <= mCurosrPosition[0] && vy1 <= mCurosrPosition[1] && vx2 >= mCurosrPosition[0] && vy2 >= mCurosrPosition[1]) {
					if (!isAdjustCursor) {
						isAdjustCursor = true;
						if (mCursorDepthChangeListener != null) {
							mCursorDepthChangeListener.onCursorDepthChange(v.getDepth());
						}
					}

					if (v != mFocusedView) {
						if (mFocusedView != null && !v.isGrandParent(mFocusedView)) {
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
		}

//		String msg1= "", msg2 = "";
//		float[] ltPos = new float[leftTop.size()];
//		float[] brPos = new float[bottomRight.size()];
//		for (int i = 0; i < leftTop.size() && i < bottomRight.size(); i++){
//			ltPos[i] = leftTop.get(i);
//			brPos[i] = bottomRight.get(i);
//			msg1 += ltPos[i];
//			msg2 += brPos[i];
//			if (i < leftTop.size() - 1){
//				msg1 += ", ";
//				msg2 += ", ";
//			}
//		}
//
//		int index = MojingSDK.SelectRectByDirectional(headView, ltPos, brPos);
//
//		String msg = "headview = {";
//		for (int m = 0; m < headView.length; m++){
//			msg += headView[m];
//			if (m < headView.length - 1){
//				msg += ", ";
//			}
//		}
//		msg += "},\n {" + msg1 + "},\n {" + msg2 + "},\n index = " + index;
////		msg += "}, Pos1 = {" + x1 + ", " + y1 + "}, Pos2 = {" + x2 + ", " + y2 + "}, z = " + -defualtDepth + ", outPos = {" + outPos[0] + ", " + outPos[1] + "}";
//		Log.e("DirectionalRadiaInRect", msg);




		if (!hasFocused && mFocusedView !=null){
			mFocusedView.onFocusChange(TO_UNKNOWN, false);
			mFocusedView = null;
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
