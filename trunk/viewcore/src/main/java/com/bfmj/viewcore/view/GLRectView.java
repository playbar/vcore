package com.bfmj.viewcore.view;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.baofeng.mojing.MojingSDK;
import com.baofeng.mojing.input.base.MojingKeyCode;
import com.bfmj.viewcore.animation.GLAnimation;
import com.bfmj.viewcore.animation.GLRotateAnimation;
import com.bfmj.viewcore.animation.GLScaleAnimation;
import com.bfmj.viewcore.animation.GLTransformation;
import com.bfmj.viewcore.animation.GLTranslateAnimation;
import com.bfmj.viewcore.interfaces.GLOnKeyListener;
import com.bfmj.viewcore.interfaces.GLViewFocusListener;
import com.bfmj.viewcore.render.GLColor;
import com.bfmj.viewcore.render.GLConstant;
import com.bfmj.viewcore.render.GLRenderParams;
import com.bfmj.viewcore.render.GLScreenParams;
import com.bfmj.viewcore.util.GLFocusUtils;
import com.bfmj.viewcore.util.GLGenTexTask;
import com.bfmj.viewcore.util.GLMatrixState;
import com.bfmj.viewcore.util.GLTextureUtils;
import com.bfmj.viewcore.animation.GLAlphaAnimation;
import com.bfmj.viewcore.util.GLThreadPool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.opengl.Matrix;
import android.view.animation.AnimationUtils;

/**
 * 
 * ClassName: GLRectView <br/>
 * @author lixianke    
 * @date: 2015-3-9 下午1:52:42 <br/>  
 * description:
 */
public class GLRectView extends GLView {
	private boolean mbNeedUpdate = false;
	public static final int MATCH_PARENT = -1;
	public static final int WRAP_CONTENT = -2;
	
	private static float depthScale = 1.0f;
	
	private Context mContext;
	private List<GLRenderParams> mRenders = new ArrayList<GLRenderParams>();

	private GLGroupView mParent;
	private String mId = "";

	private boolean isFocusable = false;
	private boolean isFocused = false;
	private boolean isSelected = false;
	private boolean isEnable = true;
	private GLOnKeyListener mKeyListener;
	
	private boolean isSurfaceCreated = false;
	
	private float left = 0;
	private float top = 0;
    private float x = 0;
    private float y = 0;
    private float width = 0;
    private float height = 0;
    private float depth = GLScreenParams.getDefualtDepth();
    private float centerX = 0;
    private float centerY = 0;
    private float paddingLeft = 0;
    private float paddingRight = 0;
    private float paddingTop = 0;
    private float paddingBottom = 0;
    private float marginLeft = 0;
    private float marginRight = 0;
    private float marginTop = 0;
    private float marginBottom = 0;
    private float scaleX = 1.0f;
    private float scaleY = 1.0f;
    private float[] position = new float[3];
    private float angelX = 0;
    private float angelY = 0;
    private float angelZ = 0;
    private int zIndex = 0;
    private int zPosition = 0;
    private float animEndX=0;
    private float animEndY=0;
    private float animEndZ=0;
    private float originalX = 0; //原始坐标X
    private float originalY = 0; //原始坐标Y
    private boolean isSetOriginal = false;
    private float mLookAngle = 0;
    private float mLookTranslateZ = 0;
    
    private GLConstant.GLAlign mAlign;
    private float mRotateAngle = 0;
    
    private int mBackgroundResId;
    private Bitmap mBackgroundBitmap;
    private GLColor mBackgroundColor;
    private GLRenderParams mBackgroundRender;
    private boolean mUseMipMap = false;
    
    private int mHeadStayTime = 1500;
    private int mHeadClickTime = 2500;
    private OnHeadClickListener mHeadClickListener;
    private Timer mHeadClickTimer;
    private boolean isHeadClicking = false;
    private boolean isHeadClickStart = false;

    //动画实例
    private  ArrayList<GLAnimation> mAnimations = new ArrayList<GLAnimation>();
    private GLViewFocusListener mFocusListener;

	public GLRectView(Context context) {
		super(context);
		mContext = context;
	}
	
	@Override
	protected Context getContext() {
		return mContext;
	}
	
	/**
	 * 获取View是否能够响应焦点
	 * @author lixianke  @Date 2015-6-23 下午1:31:23
	 * @param 
	 * @return 是否能够响应焦点
	 */
	public boolean isFocusable(){
		return isFocusable;
	}
	
	/**
	 * 设置View是否能够响应焦点
	 * @author lixianke  @Date 2015-6-23 下午1:32:53
	 * @param focusable 是否能够响应焦点 
	 * @return
	 */
	public void setFocusable(boolean focusable) {
		if (focusable && getParent() != null){
			getParent().setFocusable(true);
		}
		
		isFocusable = focusable;
	}
	
	/**
	 * 获取View当前是否拥有焦点
	 * @author lixianke  @Date 2015-6-23 下午1:33:35
	 * @param
	 * @return 是否拥有焦点
	 */
	public boolean isFocused(){
		return isFocused;
	}
	
	/**
	 * 设置View当前是否拥有焦点
	 * @author lixianke  @Date 2015-6-23 下午1:34:14
	 * @param focused 当前是否拥有焦点
	 * @return
	 */
	public void setFocused(boolean focused){
		isFocused = focused;
	}
	
	/**
	 * 设置按键事件的监听器
	 * @author lixianke  @Date 2015-6-23 下午1:35:06
	 * @param  listener 监听器（GLOnKeyListener对象）
	 * @return
	 */
	public void setOnKeyListener(GLOnKeyListener listener){
		
		mKeyListener = listener;
		
		if (listener != null){			
			setFocusable(true);
		} else if (mFocusListener == null) {
			setFocusable(false);
		}
	}
	
	/**
	 * 设置ID
	 * @author lixianke  @Date 2015-6-23 下午1:35:56
	 * @param id ID 
	 * @return
	 */
	public void setId(String id){
		mId = id;
	}
	
	/**
	 * 获取ID
	 * @author lixianke  @Date 2015-6-23 下午1:36:30
	 * @param 
	 * @return ID串
	 */
	public String getId(){
		return mId;
	}
	
	/**
	 * 获取surfaceCreated回调是否已完成
	 * @author lixianke  @Date 2015-6-23 下午1:37:00
	 * @param
	 * @return surfaceCreated回调是否已完成
	 */
	public boolean isSurfaceCreated() {
		return isSurfaceCreated;
	}
	
	/**
	 * 设置父View
	 * @author lixianke  @Date 2015-6-23 下午1:37:50
	 * @param parent 父View（GLGroupView） 
	 * @return
	 */
	public void setParent(GLGroupView parent) {
		mParent = parent;
	}
	
	/**
	 * 获取父View
	 * @author lixianke  @Date 2015-6-23 下午1:38:27
	 * @param
	 * @return 父View（GLGroupView或null）
	 */
	public GLGroupView getParent(){
		return mParent;
	}
	
	/**
	 * 根据ID查找子View
	 * @author lixianke  @Date 2015-6-23 下午1:39:35
	 * @param id 需查找的View的ID 
	 * @return 查找结果（GLRectView或null）
	 */
	public GLRectView findViewById(String id){
		if (id.isEmpty() || !(this instanceof GLGroupView)){
			return null;
		}
		
		ArrayList<GLRectView> childs = ((GLGroupView)this).getView();
		for (GLRectView view : childs) {
			if (view.getId().equals(id)){
				return view;
			} else if (view instanceof GLGroupView){
				GLRectView result = view.findViewById(id);
				if (result != null){
					return result;
				}
			}
		}
		
		return null;
	}
	
	/**
	 * 判断是否为当前View的子View
	 * @author lixianke  @Date 2015-6-23 下午1:41:00
	 * @param v 要进行判断的View 
	 * @return 判断结果
	 */
	public boolean isGrandChild(GLRectView v){
		if (v == null || !(this instanceof GLGroupView)){
			return false;
		}
		
		ArrayList<GLRectView> childs = ((GLGroupView)this).getView();
		for (GLRectView view : childs) {
			if (view == v){
				return true;
			} else if (view instanceof GLGroupView){
				boolean result = view.isGrandChild(v);
				if (result){
					return result;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * 递归判断是否为父级View
	 * @author lixianke  @Date 2015-6-23 下午1:42:20
	 * @param v 需要判断的View
	 * @return 当前View是否为父级View或祖级View
	 */
	public boolean isGrandParent(GLRectView v){
		if (v == null || !(getParent() instanceof GLGroupView)){
			return false;
		}
		
		GLRectView view = getParent();
		
		while (view != null) {
			if (view == v){
				return true;
			}
			view = view.getParent();
		}
		
		return false;
	}
	
	/**
	 * 获取X坐标
	 * @author lixianke  @Date 2015-6-23 下午1:45:25
	 * @param
	 * @return X坐标
	 */
	public float getX(){
		return x;
	}
	
	/**
	 * 设置X坐标
	 * @author lixianke  @Date 2015-6-23 下午1:46:00
	 * @param x X坐标  
	 * @return
	 */
	public void setX(float x){
		this.x = x;
		mbNeedUpdate = true;
	}
	
	/**
	 * 获取Y坐标
	 * @author lixianke  @Date 2015-6-23 下午1:46:34
	 * @param  
	 * @return Y坐标
	 */
	public float getY(){
		return y;
	}
	
	/**
	 * 设置Y坐标
	 * @author lixianke  @Date 2015-6-23 下午1:47:02
	 * @param y  Y坐标  
	 * @return
	 */
	public void setY(float y){
		this.y = y;
		mbNeedUpdate = true;
	}
	
	/**
	 * 获取宽度
	 * @author lixianke  @Date 2015-6-23 下午1:47:58
	 * @param 
	 * @return 宽度
	 */
	public float getWidth(){
		updateRect();
		return width;
	}
	
	/**
	 * 获取内容宽度（不包含padding的宽度）
	 * @author lixianke  @Date 2015-6-23 下午1:48:23
	 * @param
	 * @return 内容宽度
	 */
	float getInnerWidth(){
		return width - paddingLeft - paddingRight;
	}
	
	/**
	 * 设置宽度
	 * @author lixianke  @Date 2015-6-23 下午1:49:10
	 * @param width 宽度 
	 * @return
	 */
	public void setWidth(float width){
		this.width = width;
		mbNeedUpdate = true;
	}
	
	/**
	 * 获取高度
	 * @author lixianke  @Date 2015-6-23 下午1:50:11
	 * @param
	 * @return 高度
	 */
	public float getHeight(){
		updateRect();
		return height;
	}
	
	/**
	 * 获取内容高度（不包含padding的高度）
	 * @author lixianke  @Date 2015-6-23 下午1:52:32
	 * @param
	 * @return 内容高度
	 */
	float getInnerHeight(){
		return height - paddingTop - paddingBottom;
	}
	
	/**
	 * 设置高度
	 * @author lixianke  @Date 2015-6-23 下午1:53:17
	 * @param height 高度 
	 * @return
	 */
	public void setHeight(float height){
		this.height = height;
		mbNeedUpdate = true;
	}
	
	/**
	 * 设置到父View的左边距
	 * @author lixianke  @Date 2015-6-23 下午1:53:51
	 * @param left 左边距 
	 * @return
	 */
	protected void setLeft(float left) {
		this.left = left;
		mbNeedUpdate = true;
		//updateRect();
	}
	
	/**
	 * 获取左边距
	 * @author lixianke  @Date 2015-6-23 下午1:54:50
	 * @param 
	 * @return 左边距
	 */
	public float getLeft(){
		return this.left;
	}
	
	/**
	 * 设置到父View的上边距
	 * @author lixianke  @Date 2015-6-23 下午1:55:39
	 * @param top 上边距
	 * @return
	 */
	protected void setTop(float top){
		this.top = top;
		mbNeedUpdate = true;
	}
	
	/**
	 * 获取上边距
	 * @author lixianke  @Date 2015-6-23 下午1:56:21
	 * @param  
	 * @return 上边距
	 */
	public float getTop(){
		return this.top;
	}
	
	/**
	 * 设置位置及大小
	 * @author lixianke  @Date 2015-6-23 下午1:56:44
	 * @param width 宽度
	 * @param height 高度
	 * @return
	 */
	public void setLayoutParams(float width, float height){
		this.width = width;
		this.height = height;
		mbNeedUpdate = true;
	}
	
	/**
	 * 设置内边距
	 * @author lixianke  @Date 2015-6-23 下午2:00:10
	 * @param left 内左边距
	 * @param top 内上边距
	 * @param right 内右边距
	 * @param bottom 内下边距 
	 * @return
	 */
	public void setPadding(float left, float top, float right, float bottom){
		paddingLeft = left;
		paddingTop = top;
		paddingRight = right;
		paddingBottom = bottom;
		mbNeedUpdate = true;
	}
	
	/**
	 * 获取内左边距
	 * @author lixianke  @Date 2015-6-23 下午2:01:51
	 * @param
	 * @return 内左边距
	 */
	public float getPaddingLeft(){
		return paddingLeft;
	}
	
	/**
	 * 获取内上边距
	 * @author lixianke  @Date 2015-6-23 下午2:01:51
	 * @param
	 * @return 内上边距
	 */
	public float getPaddingTop(){
		return paddingTop;
	}
	
	/**
	 * 获取内右边距
	 * @author lixianke  @Date 2015-6-23 下午2:01:51
	 * @param
	 * @return 内右边距
	 */
	public float getPaddingRight(){
		return paddingRight;
	}
	
	/**
	 * 获取内下边距
	 * @author lixianke  @Date 2015-6-23 下午2:01:51
	 * @param
	 * @return 内下边距
	 */
	public float getPaddingBottom(){
		return paddingBottom;
	}
	
	/**
	 * 设置外边距
	 * @author lixianke  @Date 2015-6-23 下午2:00:10
	 * @param left 外左边距
	 * @param top 外上边距
	 * @param right 外右边距
	 * @param bottom 外下边距 
	 * @return
	 */
	public void setMargin(float left, float top, float right, float bottom){
		marginLeft = left;
		marginTop = top;
		marginRight = right;
		marginBottom = bottom;
		mbNeedUpdate = true;
	}
	
	/**
	 * 获取外左边距
	 * @author lixianke  @Date 2015-6-23 下午2:01:51
	 * @param
	 * @return 外左边距
	 */
	public float getMarginLeft(){
		return marginLeft;
	}
	
	/**
	 * 获取外上边距
	 * @author lixianke  @Date 2015-6-23 下午2:01:51
	 * @param
	 * @return 外上边距
	 */
	public float getMarginTop(){
		return marginTop;
	}
	
	/**
	 * 获取外右边距
	 * @author lixianke  @Date 2015-6-23 下午2:01:51
	 * @param
	 * @return 外右边距
	 */
	public float getMarginRight(){
		return marginRight;
	}
	
	/**
	 * 获取外下边距
	 * @author lixianke  @Date 2015-6-23 下午2:01:51
	 * @param
	 * @return 外下边距
	 */
	public float getMarginBottom(){
		return marginBottom;
	}
	
	/**
	 * 设置对齐方式
	 * @author lixianke  @Date 2015-6-23 下午2:05:36
	 * @param align 对齐方式（GLConstant.GLAlign枚举值）
	 * @return
	 */
	public void setAlign(GLConstant.GLAlign align){
		mAlign = align;
	}
	
	/**
	 * 获取对齐方式
	 * @author lixianke  @Date 2015-6-23 下午2:06:48
	 * @param 
	 * @return 对齐方式（GLConstant.GLAlign枚举值）
	 */
	public GLConstant.GLAlign getAlign(){
		return mAlign;
	}
	
	private void updateRect(){
		if( mbNeedUpdate ){
			updateSize();
			updateCenterPosition();
			mbNeedUpdate = false;
		}
	}
	
	private void updateCenterPosition(){
		float screenWidth = GLScreenParams.getScreenWidth();
		float screenHeight = GLScreenParams.getScreenHeight();
		float xDpi = GLScreenParams.getXDpi();
		float yDpi = GLScreenParams.getYDpi();
		
		float nx = (left + x) / xDpi;
    	float ny = (top + y) / yDpi;
    	float nw = width / xDpi;
    	float nh = height / yDpi;
    	
    	centerX = screenWidth * (nx + nw / 2) - screenWidth / 2;
    	centerY = -(screenHeight * (ny + nh / 2) - screenHeight / 2);
    }
	
	private void updateSize(){
		if (width == MATCH_PARENT || width == WRAP_CONTENT){
			if (mParent != null){
				width = mParent.getInnerWidth();
			} else {
				width = GLScreenParams.getXDpi() - left - x;
			}
		}
		
		if (height == MATCH_PARENT){
			if (mParent != null){
				height = mParent.getInnerHeight();
			} else {
				height = GLScreenParams.getYDpi() - top - y;
			}
		}
		
		if (height == WRAP_CONTENT){
			return;
		}
		
		if (!isSurfaceCreated){
			return;
		}
		
		for (GLRenderParams render : mRenders){
			if (render != mBackgroundRender){
				updateRenderSize(render, getInnerWidth(), getInnerHeight());
			} else {
				updateRenderSize(render, width, height);
			}
		}
		return;
	}
	
	protected void updateRenderSize(GLRenderParams render, float width, float height){
		float xDpi = GLScreenParams.getXDpi();
		float yDpi = GLScreenParams.getYDpi();
		float screenWidth = GLScreenParams.getScreenWidth();
		float screenHeight = GLScreenParams.getScreenHeight();
		
		if (render == null) {
			return;
		}
		render.setScaleX(screenWidth * width / xDpi);
		render.setScaleY(screenHeight * height / yDpi);
	}
	
	/**
	 * 进行缩放，X轴和Y轴以相同比例缩放
	 * @author lixianke  @Date 2015-6-23 下午2:11:09
	 * @param scale 缩放比例 
	 * @return
	 */
	public void scale(float scale){
		scale(scale, scale);
    }
	
	/**
	 * 进行缩放
	 * @author lixianke  @Date 2015-6-23 下午2:12:05
	 * @param sx X轴的缩放比例
	 * @param sy Y轴的缩放比例
	 * @return
	 */
	public void scale(float sx, float sy){   	
    	float[] mtx = getMatrixState().getScaleMatrix();
    	System.arraycopy(GLMatrixState.getInitMatrix(), 0, mtx, 0, 16);
    	Matrix.scaleM(mtx, 0, sx, sy, 0);
    	scaleX = sx;
    	scaleY = sy;
	}
	
	/**
	 * 获取X轴的缩放比例
	 * @author lixianke  @Date 2015-6-23 下午2:13:02
	 * @param
	 * @return X轴的缩放比例
	 */
	public float getScaleX(){
		return scaleX;
	}
	
	/**
	 * 获取Y轴的缩放比例
	 * @author lixianke  @Date 2015-6-23 下午2:13:32
	 * @param 
	 * @return Y轴的缩放比例
	 */
	public float getScaleY(){
		return scaleY;
	}
	
	/**
	 * 进行平移
	 * @author lixianke  @Date 2015-6-23 下午2:15:59
	 * @param tx X轴的平移距离
	 * @param ty Y轴的平移距离
	 * @param tz Z轴的平移距离（空间单位）
	 * @return
	 */
	public void translate(float tx, float ty, float tz) {
//		float screenWidth = GLScreenParams.getScreenWidth();
//		float screenHeight = GLScreenParams.getScreenHeight();
//		float xDpi = GLScreenParams.getXDpi();
//		float yDpi = GLScreenParams.getYDpi();
//		
//		Matrix.translateM(getMatrixState().getTranslationMatrix(), 0, screenWidth * tx / xDpi, 
//				-screenHeight * ty / yDpi, tz);
		
		setX(getX() + tx);
		setY(getY() + ty);
		setDepth(getDepth() + tz);
	}

	/**
	 * 设置空间位置
	 * @param position 空间位置，放大10倍
	 */
	public void setPosition(float[] position){
		this.position = position;
		Matrix.translateM(getMatrixState().getCurrentMatrix(), 0, position[0], position[1], getDepth() + position[2]);
	}

	public float[] getPositon(){
		return this.position;
	}
	
	/**
	 * 进行旋转， 每次只能按一个轴旋转
	 * @author lixianke  @Date 2015-6-23 下午2:17:34
	 * @param angle 旋转角度
	 * @param rx 绕X轴旋转
	 * @param ry 绕Y轴旋转
	 * @param rz 绕Z轴旋转
	 * @return
	 */
	public void rotate(float angle, float rx, float ry, float rz){
		//Matrix.rotateM(getMatrixState().getRotateMatrix(), 0, angle, rx, ry, rz);
		if (rx > 0){
			angelX += angle;
		} else if (ry > 0){
			angelY += angle;
		} else {
			angelZ += angle;
		}
	}

	public float getAngelX(){
		return angelX;
	}

	public float getAngelY(){
		return angelY;
	}

	public float getAngelZ(){
		return angelZ;
	}
	
	/**
	 * 获取深度，即View到视点的距离
	 * @author lixianke  @Date 2015-6-23 下午2:20:09
	 * @param  
	 * @return 深度
	 */
	public float getDepth() {
		return depth;
	}
	
	/**
	 * 设置深度，即View到视点的距离
	 * @author lixianke  @Date 2015-6-23 下午2:21:05
	 * @param depth 深度
	 * @return
	 */
	public void setDepth(float depth) {
		this.depth = depth;
	}
	
	public void setZIndex(int index){
		zIndex = index;
	}
	
	public int getZIndex(){
		return zIndex;
	}
	
	@Override
	public void setAlpha(float alpha){
		super.setAlpha(alpha);
		
		if (mBackgroundColor != null){
			mBackgroundColor.setA(alpha);
		}
		int i = 0;
		int ilen = mRenders.size();
		for ( i = 0; i < ilen; i++) {
			mRenders.get(i).setAlpha(alpha);
		}
	}
	
	@Override
	public void setMask(float mask) {
		super.setMask(mask);

		int i = 0;
		int ilen = mRenders.size();
		for (i = 0; i < ilen; i++) {
			mRenders.get(i).setMask(mask);
		}
	}

	protected void addRender(GLRenderParams render) {
		mRenders.add( render );

		if (render instanceof GLRenderParams){
			render.setAlpha(getAlpha());
			render.setMask(getMask());
		}
	}
	
	protected void removeRender(GLRenderParams render) {
		mRenders.remove( render );

		if (render.getType() == GLRenderParams.RENDER_TYPE_IMAGE){
			int textureId = render.getTextureId();
			releaseTexture(textureId);
		}
	}

	public List<GLRenderParams> getRenders(){
		return mRenders;
	}
	
	/**
	 * 设置背景
	 * @author lixianke  @Date 2015-6-23 下午2:22:01
	 * @param resId 资源ID
	 * @return
	 */
	public void setBackground(int resId){
		if (mBackgroundResId == resId){
			return;
		}
		
		mBackgroundResId = resId;
		mBackgroundBitmap = null;
		mBackgroundColor = null;

		removeRender();
		initBackground();
	}
	
	/**
	 * 设置背景
	 * @author lixianke  @Date 2015-6-23 下午2:25:12
	 * @param bitmap 
	 * @return
	 */
	public void setBackground(Bitmap bitmap){
		if (bitmap == null){
			removeBackground();
			return;
		}
		
		if (mBackgroundBitmap == bitmap){
			return;
		}
		
		mBackgroundBitmap = bitmap;
		mBackgroundResId = 0;
		mBackgroundColor = null;

		removeRender();
		initBackground();
	}
	
	/**
	 * 设置背景颜色
	 * @author lixianke  @Date 2015-6-23 下午2:25:45
	 * @param color 背景颜色
	 * @return
	 */
	public void setBackground(GLColor color){
		if (color == null){
			removeBackground();
			return;
		}
		
		if (mBackgroundColor == color){
			return;
		}
		
		mBackgroundColor = color;
		mBackgroundColor.setA(color.getA() * getAlpha());
				
		mBackgroundResId = 0;
		mBackgroundBitmap = null;

		removeRender();
		initBackground();
	}
	
	private void removeBackground(){
		removeRender();
		mBackgroundBitmap = null;
		mBackgroundResId = 0;
		mBackgroundColor = null;
	}

	private boolean isNeedUpdateUI(){
		if (mBackgroundBitmap != null ||
				mBackgroundResId != 0 || mBackgroundColor != null){
			return true;
		}
		return false;
	}
	
	private Bitmap mBitmap = null;
	private void initBackground(){
		if (!isSurfaceCreated || !isVisible() || !isNeedUpdateUI()){
			return;
		}

		removeRender();

		GLThreadPool.getThreadPool().execute(new Runnable() {
			public void run() {

				boolean isRecycle = true;
				Bitmap bitmap = null;
				if (mBackgroundResId != 0) {
					InputStream is = getContext().getResources().openRawResource(mBackgroundResId);

					try {
						bitmap = BitmapFactory.decodeStream(is);
					} finally {
						try {
							is.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				} else if (mBackgroundBitmap != null) {
					bitmap = mBackgroundBitmap;
					isRecycle = false;
				}

				if (bitmap != null) {
					GLTextureUtils.mUseMipMap = getMipMap();
					mBitmap = GLTextureUtils.handleBitmap(bitmap, isRecycle);
				} else if (mBackgroundColor != null){
					mBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
					Canvas canvas = new Canvas(mBitmap);
					canvas.drawARGB((int)(mBackgroundColor.getA() * 255), (int)(mBackgroundColor.getR() * 255),
							(int)(mBackgroundColor.getG() * 255), (int)(mBackgroundColor.getB() * 255));
					GLTextureUtils.mUseMipMap = getMipMap();
				}

				GLGenTexTask.QueueEvent( new GLGenTexTask() {
					public void ExportTextureId() {

						int textureId = -1;
						textureId = GLTextureUtils.initImageTexture(getContext(), mBitmap, false);
						if( mBitmap != null) {
							mBitmap.recycle();
							mBitmap = null;
						}
						if (textureId > -1) {
							mBackgroundRender = new GLRenderParams(GLRenderParams.RENDER_TYPE_IMAGE);
							mBackgroundRender.setTextureId(textureId);
							updateRenderSize(mBackgroundRender, width, height);
						}

						if (mBackgroundRender != null) {
							mBackgroundRender.setMask(getMask());
							mRenders.add(0, mBackgroundRender);
						}
					}
				});

			}
		});

//		GLGenTexTask.QueueEvent( new GLGenTexTask(){
//			public void ExportTextureId(){
//				removeRender();
//
//				boolean isRecycle = true;
//				Bitmap bitmap = null;
//				if (mBackgroundResId != 0) {
//					InputStream is = getContext().getResources().openRawResource(mBackgroundResId);
//
//					try {
//						bitmap = BitmapFactory.decodeStream(is);
//					} finally {
//						try {
//							is.close();
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
//					}
//				} else if (mBackgroundBitmap != null) {
//					bitmap = mBackgroundBitmap;
//					isRecycle = false;
//				}
//				int textureId = -1;
//				if (bitmap != null) {
//					GLTextureUtils.mUseMipMap = getMipMap();
//					textureId = GLTextureUtils.initImageTexture(getContext(), GLTextureUtils.handleBitmap(bitmap, isRecycle), true);
//				} else if (mBackgroundColor != null){
//					bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
//					Canvas canvas = new Canvas(bitmap);
//					canvas.drawARGB((int)(mBackgroundColor.getA() * 255), (int)(mBackgroundColor.getR() * 255),
//							(int)(mBackgroundColor.getG() * 255), (int)(mBackgroundColor.getB() * 255));
//					GLTextureUtils.mUseMipMap = getMipMap();
//					textureId = GLTextureUtils.initImageTexture(getContext(), bitmap, true);
//				}
//
//				if (textureId > -1) {
//					mBackgroundRender = new GLRenderParams(GLRenderParams.RENDER_TYPE_IMAGE);
//					mBackgroundRender.setTextureId(textureId);
//					updateRenderSize(mBackgroundRender, width, height);
//				}
//
//				if (mBackgroundRender != null){
//					mBackgroundRender.setMask(getMask());
//					mRenders.add(0, mBackgroundRender );
//				}
//			}
//		});
	}

	@Override
	public void initDraw() {
		isSurfaceCreated = true;
		initBackground();
	}

	@Override
	public void setVisible(boolean isVisible) {
		super.setVisible(isVisible);

		if (mBackgroundRender == null) {
			initBackground();
		}
	}

	private void removeRender(){
		if (mBackgroundRender != null){
			if (mBackgroundRender.getTextureId() > -1) {
				releaseTexture(mBackgroundRender.getTextureId());
			}
			removeRender(mBackgroundRender);
			mBackgroundRender = null;
		}
	}

	@Override
	public void onBeforeDraw(boolean isLeft) {
		updateRect();
		if (!isVisible() || !isSurfaceCreated){
			return;
		}

		GLMatrixState state = getMatrixState();

		if (isFixed()){
			float[] mtx = new float[16];
			Matrix.setIdentityM(mtx, 0);

			float[] out = new float[3];
			MojingSDK.getLastHeadEulerAngles(out);
			double zAngle = Math.toDegrees(out[2]);
			if (Math.abs(zAngle) > 90.0){
				Matrix.rotateM(mtx, 0, 180, 0, 0, 1);
			}
			state.setVMatrix(mtx);
		}

		float[] curMatrix = state.getCurrentMatrix();
		state.pushMatrix();

		getEyeMatrix(state.getVMatrix(), isLeft);
		Matrix.translateM(state.getVMatrix(), 0, 0, 0, mLookTranslateZ);
		Matrix.rotateM(state.getVMatrix(), 0, mLookAngle, 0, 1, 0);

		Matrix.translateM(curMatrix, 0, centerX, centerY, 0);

		//动画处理
		doAnimation();
	}

	@Override
	public void onAfterDraw(boolean isLeft) {
		if (!isVisible() || !isSurfaceCreated){
			return;
		}
		getMatrixState().popMatrix();
	}

	@Override
	public void draw() {}
	
	/**
	 * @author zhangxin  @Date 2015-3-12 上午10:06:40
	 * description: 每帧渲染时调用动画类的getTransformation取出变换矩阵
	 * 				设置到view的mMatrixState属性的变换矩阵属性中
	 * @param 
	 * @return
	 */
	private void doAnimation() {
		//平移动画
		for (int i=0; i<mAnimations.size(); i++) {
			GLAnimation animation = mAnimations.get(i);
			if (animation == null) continue;
			
			boolean isStart = animation.isStart();
			boolean isEnd = animation.isEnd();			
			GLTransformation t = animation.getGlTransformation();
			if (t == null) {
				animation.setStart(false);
				mAnimations.remove(animation);
				continue;
			}
			
	        if (isStart) 
	        {
	        	if (!isEnd) {
	        		boolean retIsEnd = animation.getTransformation(AnimationUtils.currentAnimationTimeMillis(), t);
	        		animation.setEnd(retIsEnd);	  
	        		
	        		GLRectView animView = animation.getAnimView();  
	        		
	        		if (animation instanceof GLTranslateAnimation)
	        		{	
	        			//只有子view xy位移
	        			if (animation.isOnlyChids() && animView instanceof GLGroupView) 
	        			{
	        				GLGroupView grpView = (GLGroupView)animation.getAnimView(); 
	        				grpView.setChildXY(t.getX(), t.getY());
	        			} 
	        			else //全部view xy位移 
	        			{
	        				animView.setX(getX() + t.getX());
	        				animView.setY(getY() + t.getY());
	        			}
	        			
	        			float currdep = getDepth() + t.getZ();
	        			if (currdep > animView.getParent().getDepth()) 
	        			{
	        				currdep = animView.getParent().getDepth();
	        			}
	        			animView.setDepth(currdep);
	        			
	        		} 
	        		else if (animation instanceof GLScaleAnimation)
	        		{
	        			
	        			animView.scale(t.getX(), t.getY());
	        		}
	        		else if (animation instanceof GLRotateAnimation)
	        		{
	        			//Log.d("test", "getDegree:"+t.getDegree() +";getX:" + t.getX());
	        			animView.rotate(t.getDegree(), t.getX(), t.getY(), t.getZ());
	        		}
	        		else if (animation instanceof GLAlphaAnimation)
	        		{
	        			animView.setAlpha(t.getAlpha());
	        		}
	        		
	            } else {
	            	mAnimations.remove(animation);
	            }        	
	        }
		}
	}
	
	/**
	 * 开始动画
	 * @author lixianke  @Date 2015-6-23 下午2:26:28
	 * @param animation 动画对象
	 * @return
	 */
	public void startAnimation(GLAnimation animation) {
		if (animation != null) {
			GLTransformation transformation = new GLTransformation();
			animation.setGlTransformation(transformation);
			animation.setStart(true);
			mAnimations.add(animation);
		}
	}
	
	/**
	 * 设置焦点改变事件
	 * @author lixianke  @Date 2015-3-16 上午11:00:28
	 * @param listener GLViewFocusListener对象
	 * @return
	 */
	public void setFocusListener(GLViewFocusListener listener){
		mFocusListener = listener;

		if (listener != null){			
			setFocusable(true);
		} else if (mKeyListener == null) {
			setFocusable(false);
		}
	}
	
	public boolean hasListeter(){
		return mFocusListener != null || mKeyListener != null;
	}
	
	/**
	 * 强制设置焦点
	 * @author lixianke  @Date 2015-3-27 上午10:16:09
	 * @param  
	 * @return
	 */
	public void requestFocus(){
		if (GLFocusUtils.isOpenHeadControl){
			return;
		}
		
		doRequestFocus();
	}
	
	public void doRequestFocus(){
		GLGroupView parent = getParent();
		
		if (parent != null){
			if (!parent.isFocused()){
				parent.doRequestFocus();
			}
			if (parent.getFocusedChild() != this){
				parent.onFocusChild();
			}
			
			parent.setFocusedChild(this);
		}
		
		isFocusable = true;
		onFocusChange(GLFocusUtils.TO_UNKNOWN, true);
	}

	@Override
	public void onSurfaceCreated() {
		//isSurfaceCreated = true;
	}

	@Override
	public void onSurfaceChanged(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	public void onFocusChange(int direction, boolean isFocused) {
		if (isFocused() == isFocused){
			return;
		}
		
		this.isFocused = isFocused;
		onHeadFocusChange(isFocused);
		
		if (mFocusListener != null){
			mFocusListener.onFocusChange(this, isFocused);
		}
	}
	
	public void onHeadFocusChange(boolean isFocused) {
		
		if (isFocused) {
			if (mHeadClickListener != null && !isHeadClicking) {
				startHeadClick();
			}
		}
	}

	public boolean onKeyDown(int keycode) {
		if (isHeadClickStart && mHeadClickListener != null) {
			mHeadClickListener.onHeadClickCancel(this);
			stopHeadClick();
		}
		if (mKeyListener != null){
			return mKeyListener.onKeyDown(this, keycode);
		}
		return false;
	}

	public boolean onKeyUp(int keycode) {
		if (mKeyListener != null){
			return mKeyListener.onKeyUp(this, keycode);
		}
		return false;
	}

	public boolean onKeyLongPress(int keycode) {
		if (mKeyListener != null){
			return mKeyListener.onKeyLongPress(this, keycode);
		}
		return false;
	}
	
	public void setOriginal() {
		isSetOriginal = true;
		originalX = getX();
		originalY = getY();
	}
	
	public boolean isSetOriginal() {
		return isSetOriginal;
	}
	
	public float getOriginalX() {
		return originalX;
	}
	
	public float getOriginalY() {
		return originalY;
	}
	
	/**
	 * 获取是否为被选中状态
	 * @author lixianke  @Date 2015-6-23 下午2:28:15
	 * @param 
	 * @return 否为被选中状态
	 */
	public boolean isSelected() {
		return isSelected;
	}
	
	/**
	 * 设置选中状态
	 * @author lixianke  @Date 2015-6-23 下午2:28:53
	 * @param isSelected 选中状态
	 * @return
	 */
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	/**
	 * 获取是否可用
	 * @author lixianke  @Date 2015-6-23 下午2:29:30
	 * @param 
	 * @return 是否可用
	 */
	public boolean isEnable() {
		return isEnable;
	}

	/**
	 * 设置是否可用
	 * @author lixianke  @Date 2015-6-23 下午2:30:08
	 * @param isEnable 是否可用
	 * @return
	 */
	public void setEnable(boolean isEnable) {
		this.isEnable = isEnable;
	}
	
	/**
	 * 获取中心点的X坐标
	 * @author lixianke  @Date 2015-6-23 下午2:31:23
	 * @param  
	 * @return X坐标
	 */
	public float getCenterX() {
		updateRect();
		return centerX;
	}
	
	/**
	 * 获取中心点的Y坐标
	 * @author lixianke  @Date 2015-6-23 下午2:32:04
	 * @param 
	 * @return Y坐标
	 */
	public float getCenterY() {
		updateRect();
		return centerY;
	}

	public float getAnimEndX() {
		return animEndX;
	}

	public void setAnimEndX(float animEndX) {
		this.animEndX = animEndX;
	}

	public float getAnimEndY() {
		return animEndY;
	}

	public void setAnimEndY(float animEndY) {
		this.animEndY = animEndY;
	}

	public float getAnimEndZ() {
		return animEndZ;
	}

	public void setAnimEndZ(float animEndZ) {
		this.animEndZ = animEndZ;
	}

	public static float getDepthScale() {
		return depthScale;
	}

	public static void setDepthScale(float depthScale) {
		GLRectView.depthScale = depthScale;
	}

	@Override
	public void release() {
		isSurfaceCreated = false;
		if( mRenders != null ){
			int ilen = mRenders.size();
			for( int i = 0; i < ilen; ++i ){
				if (mRenders.get(i) != null){
					final int textureId = mRenders.get(i).getTextureId();
					mRenders.get(i).setTextureId(-1);
					if (textureId > -1 && getRootView() != null) {
						getRootView().queueEvent(new Runnable() {

							@Override
							public void run() {
								GLTextureUtils.releaseTexture(textureId);
							}
						});
					}
				}
			}
			mRenders.clear();
			mBackgroundRender = null;
		}
	}
	
	/**
	 * 获取View与默认位置的夹角
	 * @author lixianke  @Date 2015-6-23 下午2:32:51 
	 * @param
	 * @return
	 */
	public float getLookAngle() {
		return mLookAngle;
	}
	
	/**
	 * 设置View与默认位置的夹角
	 * @author lixianke  @Date 2015-6-23 下午2:32:51
	 * @param lookAngle 夹角
	 * @return
	 */
	public void setLookAngle(float lookAngle) {
		this.mLookAngle = lookAngle;
	}

	public float getLookTranslateZ() {
		return mLookTranslateZ;
	}

	public void setLookTranslateZ(float mLookTranslateZ) {
		this.mLookTranslateZ = mLookTranslateZ;
	}
	
	/**
	 * 是否使用mipmap模式
	 * @author linzanxian  @Date 2015-7-16 下午5:04:15
	 * @param mipMap 是否使用mipmap  
	 * @return void
	 */
	public void setMipMap(boolean mipMap) {
		mUseMipMap = mipMap;
	}
	
	/**
	 * 获取是否使用mipmap
	 * @author linzanxian  @Date 2015-7-16 下午5:06:59
	 * @param {引入参数名} {引入参数说明}  
	 * @return {返回值说明}
	 */
	public boolean getMipMap() {
		return mUseMipMap;
	}
	
	/**
	 * 开启头控停留定时器
	 * @author lixianke  @Date 2015-9-1 上午11:08:52
	 * @param  
	 * @return
	 */
	private void startHeadClick(){
		stopHeadClick();
		isHeadClicking = true;
		
		final int timeSpan = 20;
		
		mHeadClickTimer = new Timer();
		mHeadClickTimer.schedule(new TimerTask() {
			private int timeTotal = 0;
			
			
			@Override
			public void run() {
				timeTotal += timeSpan;
				
				if (!isSurfaceCreated || !isFocused || mHeadClickListener == null || !isHeadClicking 
						|| !GLFocusUtils.isOpenHeadControl || getRootView() == null) {
					if (isHeadClickStart) {
						mHeadClickListener.onHeadClickCancel(GLRectView.this);
					}
					stopHeadClick();
					return;
				}
				
				if (timeTotal > mHeadStayTime + mHeadClickTime) {
					timeTotal = 0;
					stopHeadClick();
					getRootView().queueEvent(new Runnable() {
						
						@Override
						public void run() {
							if (mHeadClickListener != null) {
								mHeadClickListener.onHeadClickEnd(GLRectView.this);
							}
							if (mKeyListener != null) {
								mKeyListener.onKeyDown(GLRectView.this, MojingKeyCode.KEYCODE_ENTER);
								mKeyListener.onKeyUp(GLRectView.this, MojingKeyCode.KEYCODE_ENTER);
							}
						}
					});
				} if (timeTotal > mHeadStayTime && !isHeadClickStart) {
					isHeadClickStart = true;
					getRootView().queueEvent(new Runnable() {
						
						@Override
						public void run() {
							if (mHeadClickListener != null) {
								mHeadClickListener.onHeadClickStart(GLRectView.this);
							}
						}
					});
				}
			}
		}, timeSpan, timeSpan);
	}
	
	/**
	 * 关闭头控停留定时器
	 * @author lixianke  @Date 2015-9-1 上午11:09:47
	 * @param 
	 * @return
	 */
	private void stopHeadClick(){
		isHeadClicking = false;
		isHeadClickStart = false;
		if (mHeadClickTimer != null) {
			mHeadClickTimer.cancel();
			mHeadClickTimer = null;
		}
	}
	
	/**
	 * 获取头控停留触发点击的时间间隔
	 * @author lixianke  @Date 2015-9-1 上午11:10:33
	 * @param 
	 * @return 时间
	 */
	public int getHeadStayTime() {
		return mHeadStayTime;
	}
	
	/**
	 * 设置头控停留触发点击的时间间隔
	 * @author lixianke  @Date 2015-9-1 上午11:10:33
	 * @param headStayTime 时间间隔（毫秒）
	 * @return
	 */
	public void setHeadStayTime(int headStayTime) {
		this.mHeadStayTime = headStayTime;
	}
	
	/**
	 * 获取头控点击的时间
	 * @author lixianke  @Date 2015-9-1 上午11:10:33
	 * @param
	 * @return 点击时间（毫秒）
	 */
	public int getHeadClickTime() {
		return mHeadClickTime;
	}
	
	/**
	 * 设置头控点击的时间
	 * @author lixianke  @Date 2015-9-1 上午11:10:33
	 * @param headClickTime 点击时间（毫秒）
	 * @return
	 */
	public void setHeadClickTime(int headClickTime) {
		this.mHeadClickTime = headClickTime;
	}
	
	/**
	 * 设置头控点击事件的监听器
	 * @author lixianke  @Date 2015-9-1 上午11:13:34
	 * @param listener 监听器
	 * @return
	 */
	public void setOnHeadClickListener(OnHeadClickListener listener){
		this.mHeadClickListener = listener;
	}
	
	/**
	 * 设置头控点击事件的监听器
	 * @author lixianke  @Date 2015-9-1 上午11:13:34
	 * @param headStayTime 时间间隔（毫秒）
	 * @param headClickTime 点击时间（毫秒）
	 * @param listener 监听器
	 * @return
	 */
	public void setOnHeadClickListener(int headStayTime, int headClickTime, OnHeadClickListener listener){
		this.mHeadStayTime = headStayTime;
		this.mHeadClickTime = headClickTime;
		this.mHeadClickListener = listener;
	}
	
	public void setZPosition(int zPosition) {
		this.zPosition = zPosition;
	}

	public int getZPosition(){
		return zPosition;
	}

	public interface OnHeadClickListener {
		boolean onHeadClickStart(GLRectView view);
		boolean onHeadClickEnd(GLRectView view);
		boolean onHeadClickCancel(GLRectView view);
	}
}
