package com.bfmj.viewcore.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.baofeng.mojing.MojingSDK;
import com.baofeng.mojing.MojingSurfaceView;
import com.baofeng.mojing.input.base.MojingKeyCode;
import com.bfmj.distortion.Distortion;
import com.bfmj.viewcore.render.GLColorRect;
import com.bfmj.viewcore.render.GLImageRect;
import com.bfmj.viewcore.render.GLScreenParams;
import com.bfmj.viewcore.render.GLVideoRect;
import com.bfmj.viewcore.util.GLFocusUtils;

import android.content.Context;
import android.opengl.GLES10;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.AttributeSet;

public class GLRootView extends MojingSurfaceView implements GLSurfaceView.Renderer {
    private ArrayList<GLView> mChild = new ArrayList<GLView>();
    private Context mContext;
    private int mWidth = 0;
    private int mHeight = 0;
    private float ratio = 1;
    private boolean isSurfaceCreated = false;
    private boolean isVisible = true;

    //	private MoJingGroy mMoJingGroy;
    private GLFocusUtils mGlFocusUtils;
    //private MoJingDistortion mDistortion;
    private Distortion mDistortion;
    private boolean mDistortionEnable = true;
    private boolean mGroyEnable = true;
    private boolean isLockViewAngle = false;
    //	private boolean isSavingMode = false;
    private float[] headView = new float[16];
    private float mLockedAngle = 0.0f;

    //	private Timer mHeadViewChangeTimer;
//	private RenderModeChangeCallback mRenderModeChangeCallback;
    private RenderCallback mRenderCallback;
    private int groyRate = 100; //陀罗仪频率
    private boolean isGroyTracking = false;
    private boolean isReverseScreen = false;
    private boolean isReverseEnter = false;

    private float mResetXAngle = 0;
    private float mLastXangle = 0;
    private float mXangle = 0;
    private boolean isResetGroy = false;
    private boolean mIsDouble = true;

    public Queue<GLView> mCreateTextureQueue = new LinkedList<>();

    public GLRootView(Context context) {
        super(context);
        init(context);
    }

    public GLRootView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        setEGLContextClientVersion(3);
        //setMultiThread(true);
        //多重采样，抗锯齿
//		setEGLConfigChooser(new EGLConfigChooser() {  
//			@Override
//			public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) {
//				int[] attrList = new int[] { //  
//		            EGL10.EGL_SURFACE_TYPE, EGL10.EGL_WINDOW_BIT, //  
//		            EGL10.EGL_RED_SIZE, 8, //  
//		            EGL10.EGL_GREEN_SIZE, 8, //  
//		            EGL10.EGL_BLUE_SIZE, 8, //  
//		            EGL10.EGL_ALPHA_SIZE, 8, 
//		            EGL10.EGL_DEPTH_SIZE, 16, //  
//		            EGL10.EGL_SAMPLE_BUFFERS, 1,  
//		            EGL10.EGL_SAMPLES, 4,  
//		            EGL10.EGL_NONE //  
//			    };  
//				
//				int[] numConfig = new int[1];
//				egl.eglChooseConfig(display, attrList, null, 0, numConfig);
//		        int configSize = numConfig[0];
//		        EGLConfig[] mEGLConfigs = new EGLConfig[configSize];
//		        egl.eglChooseConfig(display, attrList, mEGLConfigs, configSize, numConfig);
//		        
//		        return mEGLConfigs[0];
//			}
//		});

        setRenderer(this);
        setRenderMode(MojingSurfaceView.RENDERMODE_CONTINUOUSLY);

        BaseViewActivity activity = (BaseViewActivity) mContext;
        mGroyEnable = activity.isGroyEnable();
        mDistortionEnable = activity.isDistortionEnable();
//		isSavingMode = activity.isSavingMode();
        isLockViewAngle = activity.isLockViewAngle();
        mLockedAngle = activity.getLockedAngle();

        initHeadView();
        mGlFocusUtils = new GLFocusUtils();
    }

    @Override
    public void onResume() {
        startTracker();

//		if (isSavingMode){
//			startHeadViewChangeTimer();
//		}

        for (GLView view : mChild) {
            view.onResume();
        }

        super.onResume();
    }

    @Override
    public void onPause() {
//		stopHeadViewChangeTimer();

        saveLastAngle();
        stopTracker();

        for (GLView view : mChild) {
            view.onPause();
        }

//		queueEvent(new Runnable() {
//
//			@Override
//			public void run() {
//				MojingSDK.LeaveMojingWorld();
////				Log.d("video", "LeaveMojingWorld");
//			}
//		});

        super.onPause();

        if (mChild != null && mChild.size() > 0) {
            for (int i = 0; i < mChild.size(); i++) {
                mChild.get(i).release();
            }
        }
    }

    public void onDestroy() {
        stopTracker();
        if (mChild != null && mChild.size() > 0) {
            for (int i = 0; i < mChild.size(); i++) {
                mChild.get(i).release();
            }
        }
    }

//	private float[] mHeadViewAngles = {0, 0, 0};
//	private int mHeadViewNoChangeTimes = 0;
//	
//	private void startHeadViewChangeTimer(){
//		if (mHeadViewChangeTimer != null){
//			mHeadViewChangeTimer.cancel();
//			mHeadViewChangeTimer = null;
//		}
//		
//		mHeadViewChangeTimer = new Timer();
//		mHeadViewChangeTimer.schedule(new TimerTask() {
//			private float[] angles = {0, 0, 0};
//			
//			@Override
//			public void run() {
//				if (mGroyEnable && isSurfaceCreated) {
//					if (mMoJingGroy != null){
//						mMoJingGroy.getGroy(headView);
//					} else {
//						MojingSDK.getLastHeadView(headView);
//					}
//					
//					float[] out = new float[3];
//					GLFocusUtils.getEulerAngles(headView, out, 0);
////					Log.d("video", "yaw = " + out[0] + "; pitch = " + out[1] + "; roll = " + out[2]);
//					
//					if (Math.abs(out[0] - mHeadViewAngles[0]) < 0.06 && 
//							Math.abs(out[1] - mHeadViewAngles[1]) < 0.06 && 
//								Math.abs(out[2] - mHeadViewAngles[2]) < 0.06){
//						mHeadViewNoChangeTimes++;
////						Log.d("video", "mHeadViewNoChangeTimes = " + mHeadViewNoChangeTimes);
//						
//						if (getRenderMode() ==  RENDERMODE_CONTINUOUSLY){
//							mHeadViewAngles = out;
//							angles = out;
//						} else if (Math.abs(out[0] - angles[0]) > 0.005 || 
//							Math.abs(out[1] - angles[1]) > 0.005 || 
//								Math.abs(out[2] - angles[2]) > 0.005) {
//							requestRender();
//							angles = out;
//						}
//						
//						if (mHeadViewNoChangeTimes > 300){
//							changeRenderMode(RENDERMODE_WHEN_DIRTY);
//						}
//					} else {
//						changeRenderMode(RENDERMODE_CONTINUOUSLY);
//						mHeadViewAngles = out;
//					}
//				}
//				//}
//			}
//		}, 1000, 50);
//	}

//	public void changeRenderMode(int mode){
//		mHeadViewNoChangeTimes = 0;
//		
//		if (getRenderMode() == mode){
//			return;
//		}
//		
//		if (mRenderModeChangeCallback != null){
//			mRenderModeChangeCallback.onRenderModeChange(mode == RENDERMODE_CONTINUOUSLY);
//		}
//		
//		setRenderMode(mode);
//	}

//	private void stopHeadViewChangeTimer(){
//		if (mHeadViewChangeTimer != null){
//			mHeadViewChangeTimer.cancel();
//			mHeadViewChangeTimer = null;
//		}
//	}

    /**
     * 启动采集陀螺仪数据
     */
    public void startTracker() {
        if (isGroyTracking) {
            return;
        }

        if (!MojingSDK.StartTracker(groyRate)) {
            setGroyEnable(false);
        } else {
            isGroyTracking = true;
        }

//		Log.d("video", "StartTracker");
    }

    /**
     * 关闭采集陀螺仪数据
     */
    public void stopTracker() {
        if (!isGroyTracking) {
            return;
        }

        MojingSDK.StopTracker();
        isGroyTracking = false;
    }

//    private void setFov() {
//        float near = 2.4f;
//        if (mDistortionEnable && isSurfaceCreated) {
//            float fov = MojingSDK.GetMojingWorldFOV();
//            if (fov >= 80) {
//                near = (float) (1 / Math.tan(Math.toRadians(fov / 2)));
//            } else if (fov < 60) {
//                mDistortionEnable = false;
//            }
//        }
//        GLScreenParams.setNear(near);
//    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        isSurfaceCreated = true;

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glDepthFunc(GLES20.GL_LEQUAL);
        GLES20.glEnable(GLES20.GL_DITHER);
        GLES20.glEnable(GLES10.GL_MULTISAMPLE);

//		MojingSDK.EnterMojingWorld(mMojingType);
//		MojingSDK.SetImageYOffset(0.06f);
//		Log.d("video", "EnterMojingWorld");
//        setFov();

        startTracker();
        resetScreenDirection(true);

//		changeRenderMode(RENDERMODE_CONTINUOUSLY);

        for (GLView view : mChild) {
            view.initDraw();
            view.onSurfaceCreated();
        }

        if (mRenderCallback != null) {
            mRenderCallback.onSurfaceCreated();
        }

        GLColorRect.initInstance();
        GLImageRect.initInstance();
        GLVideoRect.initInstance();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
//		DisplayMetrics displayMetrics = new DisplayMetrics();
//		Activity activity = (Activity)mContext;
//		activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//		GL2JNILib.init(displayMetrics, 0.0605f);

//		if (width > height) {
//			MojingSDK.OnSurfaceChanged(width, height);
//		} else {
//			MojingSDK.OnSurfaceChanged(height, width);
//		}

        if (mDistortionEnable) {
            mDistortion = Distortion.getInstance();
            mDistortion.setScreen(width, width / 2);
        }
        mWidth = width;
        mHeight = height;
        ratio = (float) width / 2.0f / (float) height;
        //Log.d("test", "ratio:"+ratio);
        for (GLView view : mChild) {
            view.onSurfaceChanged(width, height);
        }
    }

    //FPS测试 start//////
    private long lastFrame = System.currentTimeMillis();
    private int times = 0;

    public int getFPS() {
        long time = (System.currentTimeMillis() - lastFrame);
        int ts = times;
        lastFrame = System.currentTimeMillis();
        times = 0;
        return time > 0 ? (int)(ts * 1000 / time) : 60;
    }
    //FPS测试 end//////

    @Override
    public void onDrawFrame(GL10 gl) {
        times ++;
        if (mChild == null || mChild.size() == 0) {
            return;
        }

        GLView v = mCreateTextureQueue.poll();
        if (v != null){
            v.createTexture();
        }

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        final ArrayList<GLView> allViews = getAllViews();

        if (mGroyEnable) {
            MojingSDK.getLastHeadView(headView);
            String msg = "{";
            for (int h = 0; h < headView.length; h++){
                msg += headView[h];
                if (h < headView.length - 1){
                    msg += ",";
                }
            }
        }

        final float[] groyMatrix = getGroyMatrix();

        int height = mWidth / 2;
        float nearRight = GLScreenParams.getNear() * (float)Math.tan(GLScreenParams.getFOV() / 2);

        //双屏
        if (mIsDouble) {
            for (int i = 0; i < 2; i++) {
                if (mDistortion != null) {
                    mDistortion.beforeDraw(i);
                } else {
                    GLES20.glViewport(i * mWidth / 2, (mHeight - height) / 2, mWidth / 2, height);
                }

                // 为了绘制中间的视频,把GLRectView分成两部分
                ArrayList<GLRectView> imageRectView1 = new ArrayList<>();
                ArrayList<GLRectView> imageRectView2 = new ArrayList<>();
                GLPlayerView playerView = null;
                for (int j = 0; j < allViews.size(); j++) {
                    GLView view = allViews.get(j);
                    if (view != null  && view.isVisible()) {
                        view.getMatrixState().setVMatrix(groyMatrix);
                        Matrix.frustumM(view.getMatrixState().getProjMatrix(), 0, -nearRight, nearRight, -nearRight, nearRight, GLScreenParams.getNear(), GLScreenParams.getFar());
                        //					Matrix.orthoM(view.getMatrixState().getProjMatrix(), 0, -40, 40, -40, 40, GLScreenParams.getNear(), GLScreenParams.getFar());
                        //			Matrix.setLookAtM(view.getMatrixState().getVMatrix(), 0, 0, 0, 0, headView[2], -headView[6], headView[10], 0, 1, 0);

                        view.onBeforeDraw(i == 0 ? true : false);

                        if (!(view instanceof GLRectView)){
                            view.draw();
                        } else if (view instanceof GLPlayerView){
                            playerView = (GLPlayerView)view;
                        } else if (playerView == null){
                            imageRectView1.add((GLRectView) view);
                        } else {
                            imageRectView2.add((GLRectView) view);
                        }
                    }
                }

                if (imageRectView1.size() > 0){
                    GLImageRect.getInstance().drawViews(imageRectView1);
                }
                if (playerView != null){
                    GLVideoRect.getInstance().draw(playerView);
                }
                if (imageRectView2.size() > 0){
                    GLImageRect.getInstance().drawViews(imageRectView2);
                }

                for (int j = 0; j < allViews.size(); j++) {
                    GLView view = allViews.get(j);
                    if (view != null  && view.isVisible()) {
                        view.onAfterDraw(i == 0 ? true : false);
                    }
                }
            }

            if (mDistortion != null) {
                mDistortion.afterDraw();
            }
        } else { //单屏
            GLES20.glViewport(0, 0, mWidth, mHeight);
            float ratio = (float) mHeight / mWidth;
            for (int j = 0; j < allViews.size(); j++) {
                GLView view = allViews.get(j);
                if (view != null) {
                    view.getMatrixState().setVMatrix(groyMatrix);
                    Matrix.frustumM(view.getMatrixState().getProjMatrix(), 0, -nearRight, nearRight, -nearRight * ratio, nearRight * ratio, GLScreenParams.getNear(), GLScreenParams.getFar());
//					Matrix.orthoM(view.getMatrixState().getProjMatrix(), 0, -40, 40, -40, 40, GLScreenParams.getNear(), GLScreenParams.getFar());
                    //Matrix.setLookAtM(view.getMatrixState().getVMatrix(), 0, 0, 0, 0, 0, 0, -10, 0, 1, 0);

                    view.draw();
                }
            }
        }

        mGlFocusUtils.handleFocused(groyMatrix, allViews);
    }

    private void saveLastAngle() {
        float[] out = new float[3];
        GLFocusUtils.getEulerAngles(headView, out, 0);
        mLastXangle = -((float) Math.toDegrees(out[0]) - mResetXAngle) + mLastXangle;
        mResetXAngle = 0;
        isResetGroy = false;
    }

    private float[] getGroyMatrix() {
        float[] matrix = new float[16];
        if (mGroyEnable) {
            float[] out = new float[3];
            GLFocusUtils.getEulerAngles(headView, out, 0);
            if (mIsDouble) {
                if (isLockViewAngle) {
                    Matrix.setLookAtM(matrix, 0, 0, 0, 0, 0, 0, -4, 0, 1, 0);

                    float yAngle = (float) Math.toDegrees(out[1]) / 3;
                    if (yAngle < -mLockedAngle) {
                        yAngle = -mLockedAngle;
                    } else if (yAngle > mLockedAngle) {
                        yAngle = mLockedAngle;
                    }
                    Matrix.rotateM(matrix, 0, -yAngle, 1, 0, 0);

                    if (isReverseScreen) {
                        Matrix.rotateM(matrix, 0, -180, 0, 0, 1);
                    }

                    float xAngle = (float) Math.toDegrees(out[0]) / 3;
                    if (xAngle < -mLockedAngle) {
                        xAngle = -mLockedAngle;
                    } else if (xAngle > mLockedAngle) {
                        xAngle = mLockedAngle;
                    }
                    Matrix.rotateM(matrix, 0, -xAngle, 0, 1, 0);
                } else {
                    System.arraycopy(headView, 0, matrix, 0, 16);

                    if (isReverseScreen) {
                        Matrix.rotateM(matrix, 0, mResetXAngle, 0, 1, 0);
                    } else if (isReverseEnter) {
                        Matrix.rotateM(matrix, 0, mResetXAngle - mLastXangle, 0, 1, 0);
                    }
                }
            } else {
                Matrix.setLookAtM(matrix, 0, 0, 0, 0, 0, 0, -4, 0, 1, 0);
                if (isResetGroy) {
                    float yAngle = (float) Math.toDegrees(out[0]);

                    if (Math.abs(Math.toDegrees(out[1])) < 60.0) {
                        mXangle = mResetXAngle - yAngle;
                    } else {
                        mResetXAngle = yAngle + mXangle;
                    }
                    Matrix.rotateM(matrix, 0, mXangle, 0, 1, 0);

                } else {
                    Matrix.rotateM(matrix, 0, mLastXangle, 0, 1, 0);
                }
            }

        } else {
            System.arraycopy(headView, 0, matrix, 0, 16);
        }
        return matrix;
    }

    public void setDistortionEnable(boolean enable) {
        if (mDistortionEnable != enable) {
            if (enable && mWidth > 0 && mHeight > 0) {
                mDistortion = Distortion.getInstance();
                mDistortion.setScreen(mWidth, mHeight);
            } else {
                mDistortion = null;
            }
            mDistortionEnable = enable;
//            setFov();
        }
    }

    public void initHeadView() {
        if (mGroyEnable) {
            if (isSurfaceCreated) {
                MojingSDK.ResetTracker();
                resetScreenDirection(false);
            }
        } else {
            Matrix.setLookAtM(headView, 0, 0, 0, 0, 0, 0, -4, 0, 1, 0);
        }
    }

    private void resetScreenDirection(final boolean isFirst) {
        if (!mGroyEnable) {
            return;
        }

        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                float[] out = new float[3];
                float[] matrix = new float[16];
                MojingSDK.getLastHeadView(matrix);
                GLFocusUtils.getEulerAngles(matrix, out, 0);
                double zAngle = Math.toDegrees(out[2]);
                if (mIsDouble) {
                    if (Math.abs(zAngle) > 90.0) {
                        mResetXAngle = (float) Math.toDegrees(out[0]);
                        isReverseScreen = true;
                        mLastXangle = 0;
                    } else {
                        isReverseScreen = false;
                        mLastXangle = 180;
                    }
                    if (isFirst && isReverseScreen){
                        isReverseEnter = true;
                    }
                } else {
                    mResetXAngle = (float) Math.toDegrees(out[0]) + mLastXangle;
                    mLastXangle = 0;
                }
                isResetGroy = true;
            }
        }, 300);
    }

    public void addView(GLView view) {
        if (mChild.contains(view)){
            return;
        }

        if (isSurfaceCreated) {
            view.initDraw();
        }

        mChild.add(view);
    }

    public void addView(GLRectView view, boolean isFoucs) {
        addView(view);

        if (isFoucs) {
            if (mGlFocusUtils.getFocusedView() != null) {
                mGlFocusUtils.getFocusedView().onFocusChange(GLFocusUtils.TO_UNKNOWN, false);
            }
            mGlFocusUtils.setFousedView(view);
            view.onFocusChange(GLFocusUtils.TO_UNKNOWN, true);
        }
    }

    public void removeView(GLView view) {
        if (view instanceof GLRectView) {
            GLRectView v1 = (GLRectView) view;
            GLRectView v2 = mGlFocusUtils.getFocusedView();
            if (v1 == v2 || v1.isGrandChild(v2)) {
                mGlFocusUtils.setFousedView(null);
            }
        }
        view.release();
        mChild.remove(view);
    }

    /**
     * 遍历GLGroupView下所有的view
     *
     * @param
     * @return view列表
     * @author lixianke  @Date 2015-3-18 下午4:45:01
     */
    private ArrayList<GLRectView> getViews(GLGroupView groupView) {
        ArrayList<GLRectView> views = new ArrayList<GLRectView>();
        views.add(groupView);

        if (groupView.isVisible()) {
            GLRectView view;
            int size = groupView.getView().size();
            for (int i = 0; i < size; i++) {
                view = groupView.getView(i);
                if (view instanceof GLGroupView) {
                    views.addAll(getViews((GLGroupView) view));
                } else {
                    views.add(view);
                }
            }
        }

        return views;
    }

    /**
     * 遍历所有的view
     *
     * @param
     * @return view列表
     * @author lixianke  @Date 2015-3-18 下午4:45:01
     */
    private ArrayList<GLView> getAllViews() {
        ArrayList<GLView> views1 = new ArrayList<GLView>();
        ArrayList<GLRectView> views2 = new ArrayList<GLRectView>();

        if (!isVisible) {
            return views1;
        }

        try {
            for (GLView view : mChild) {
                if (!view.isVisible()) {
                    continue;
                } else if (view instanceof GLGroupView) {
                    views2.addAll(getViews((GLGroupView) view));
                } else if (view instanceof GLRectView) {
                    views2.add((GLRectView) view);
                } else {
                    views1.add(view);
                }
            }

            Collections.sort(views2, new Comparator<GLRectView>() {

                @Override
                public int compare(GLRectView lhs, GLRectView rhs) {
                    if (lhs == null || rhs == null) {
                        return 0;
                    }

                    if (lhs.getZIndex() < rhs.getZIndex()) {
                        return 1;
                    } else if (lhs.getZIndex() > rhs.getZIndex()) {
                        return -1;
                    } else if (lhs.getDepth() < rhs.getDepth()) {
                        return 1;
                    } else if (lhs.getDepth() > rhs.getDepth()) {
                        return -1;
                    }
                    return 0;
                }
            });

            int zPosition = 0;
            for (GLRectView view : views2) {
                if (view != null) {
                    view.setZPosition(zPosition++);
                }
            }
        } catch (Exception e) {
        }

        views1.addAll(views2);

        return views1;
    }

    private GLRectView getFocusedView() {
        for (int i = 0; i < mChild.size(); i++) {
            if (mChild.get(i) instanceof GLRectView) {
                GLRectView view = (GLRectView) mChild.get(i);
                if (view.isVisible() && view.isFocused()) {
                    return view;
                }
            }
        }
        return null;
    }

    private ArrayList<GLRectView> getRectViews() {
        ArrayList<GLRectView> views = new ArrayList<GLRectView>();
        for (GLView view : mChild) {

            if (view instanceof GLRectView) {
                views.add((GLRectView) view);
            }
        }
        return views;
    }

    public boolean onKeyDown(int keycode) {
//		if (getRenderMode() ==  RENDERMODE_WHEN_DIRTY){
//			mHeadViewNoChangeTimes = 0;
//			setRenderMode(RENDERMODE_CONTINUOUSLY);
//		}

        GLRectView view = getFocusedView();

        boolean flag = false;
        if (view != null) {
            flag = view.onKeyDown(keycode);
        }

        if (!flag) {
            ArrayList<GLRectView> views = getRectViews();
            switch (keycode) {
                case MojingKeyCode.KEYCODE_DPAD_LEFT:
                    mGlFocusUtils.handleFocused(GLFocusUtils.TO_LEFT, view, views);
                    break;
                case MojingKeyCode.KEYCODE_DPAD_RIGHT:
                    mGlFocusUtils.handleFocused(GLFocusUtils.TO_RIGHT, view, views);
                    break;
                case MojingKeyCode.KEYCODE_DPAD_UP:
                    mGlFocusUtils.handleFocused(GLFocusUtils.TO_UP, view, views);
                    break;
                case MojingKeyCode.KEYCODE_DPAD_DOWN:
                    mGlFocusUtils.handleFocused(GLFocusUtils.TO_DOWN, view, views);
                    break;
                default:
                    break;
            }
        }
        return flag;
    }

    public boolean onKeyUp(int keycode) {
        GLRectView view = getFocusedView();
        if (view != null) {
            return view.onKeyUp(keycode);
        }
        return false;
    }

    public boolean onKeyLongPress(int keycode) {
        GLRectView view = getFocusedView();
        if (view != null) {
            return view.onKeyLongPress(keycode);
        }
        return false;
    }

//	public void setRenderModeChangeCallback(RenderModeChangeCallback callback){
//		mRenderModeChangeCallback = callback;
//	}

//	public boolean isSavingMode() {
//		return isSavingMode;
//	}

//	public void setSavingMode(boolean isSavingMode) {
//		this.isSavingMode = isSavingMode;
//		
//		if (isSavingMode && isSurfaceCreated){
//			startHeadViewChangeTimer();
//		} else {
//			stopHeadViewChangeTimer();
//		}
//	}

    public boolean isGroyEnable() {
        return mGroyEnable;
    }

    public void setGroyEnable(boolean groyEnable) {
        if (mGroyEnable == groyEnable) {
            return;
        }

        mGroyEnable = groyEnable;
        initHeadView();
    }

    public interface RenderModeChangeCallback {
        void onRenderModeChange(boolean isContinuously);
    }

    public void setRenderCallback(RenderCallback callback) {
        mRenderCallback = callback;
    }

    public interface RenderCallback {
        void onSurfaceCreated();

        void updateFPS(float fps);
    }

    public boolean isLockViewAngle() {
        return isLockViewAngle;
    }

    public void setLockViewAngle(boolean isLockViewAngle) {
        if (this.isLockViewAngle != isLockViewAngle) {
            initHeadView();
        }

        this.isLockViewAngle = isLockViewAngle;
    }

    public float getLockedAngle() {
        return mLockedAngle;
    }

    public void setLockedAngle(float mLockedAngle) {
        this.mLockedAngle = mLockedAngle;
    }

    public boolean isReverseScreen() {
        return isReverseScreen;
    }

    /**
     * 设置是否双屏
     */
    public void setDoubleScreen(boolean isDouble) {
        mIsDouble = isDouble;
    }

    public boolean getIsDoubleScreen() {
        return mIsDouble;
    }
}