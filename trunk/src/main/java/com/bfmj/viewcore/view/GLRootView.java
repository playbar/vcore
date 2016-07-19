package com.bfmj.viewcore.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import android.util.Log;

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

    private float mResetXAngle = 0;
    private float mLastXangle = 0;
    private float mXangle = 0;
    private boolean isResetGroy = false;
    private boolean mIsDouble = true;

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
        setEGLContextClientVersion(2);
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
        setRenderMode(RENDERMODE_CONTINUOUSLY);

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

    private void startTracker() {
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

    private void stopTracker() {
        if (!isGroyTracking) {
            return;
        }

        MojingSDK.StopTracker();
//		Log.d("video", "StopTracker");
        isGroyTracking = false;
    }

    private void setFov() {
        float near = 2.4f;
        if (mDistortionEnable && isSurfaceCreated) {
            float fov = MojingSDK.GetMojingWorldFOV();
            if (fov >= 80) {
                near = (float) (1 / Math.tan(Math.toRadians(fov / 2)));
            } else if (fov < 60) {
                mDistortionEnable = false;
            }
        }
        GLScreenParams.setNear(near);
    }

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
        setFov();

        startTracker();
        resetScreenDirection();

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
    private long lastFrame = System.nanoTime();
    public float FPS = 0;

    public void logFrame() {
        long time = (System.nanoTime() - lastFrame);
        FPS = 1 / (time / 1000000000.0f);
        Log.e("当前帧率", "FPS =" + FPS);
        mRenderCallback.updateFPS(FPS);
        lastFrame = System.nanoTime();
    }
    //FPS测试 end//////

    @Override
    public void onDrawFrame(GL10 gl) {
        // logFrame();
        if (mChild == null || mChild.size() == 0) {
            return;
        }

        for (int i = 0; i < mChild.size(); i++) {
            GLView view = mChild.get(i);
            if (view != null) {
                view.onBeforeDraw();
            }
        }

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        ArrayList<GLView> allViews = getAllViews();

        if (mGroyEnable) {
            MojingSDK.getLastHeadView(headView);
        }

        float[] groyMatrix = getGroyMatrix();

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

                for (int j = 0; j < allViews.size(); j++) {
                    GLView view = allViews.get(j);
                    if (view != null) {
                        view.getMatrixState().setVMatrix(groyMatrix);
                        Matrix.frustumM(view.getMatrixState().getProjMatrix(), 0, -nearRight, nearRight, -nearRight, nearRight, GLScreenParams.getNear(), GLScreenParams.getFar());
                        //					Matrix.orthoM(view.getMatrixState().getProjMatrix(), 0, -40, 40, -40, 40, GLScreenParams.getNear(), GLScreenParams.getFar());
                        //			Matrix.setLookAtM(view.getMatrixState().getVMatrix(), 0, 0, 0, 0, headView[2], -headView[6], headView[10], 0, 1, 0);

                        view.draw(i == 0 ? true : false);
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

                    view.draw(true);
                }
            }
        }

        mGlFocusUtils.handleFocused(groyMatrix, allViews);

        for (int i = 0; i < mChild.size(); i++) {
            GLView view = mChild.get(i);
            if (view != null) {
                view.onAfterDraw();
            }
        }
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
            setFov();
        }
    }

    public void initHeadView() {
        if (mGroyEnable) {
//			if (isLockViewAngle){
//				if (mMoJingGroy != null){
//					mMoJingGroy.onReset();
//				} else {
//					mMoJingGroy = new MoJingGroy(mContext);
//				}
//			} else {
            //TODO reset
            if (isSurfaceCreated) {
                MojingSDK.ResetTracker();
//				Log.d("video", "ResetSensorOrientation");

                resetScreenDirection();
            }
//			}
        } else {
            Matrix.setLookAtM(headView, 0, 0, 0, 0, 0, 0, -4, 0, 1, 0);
        }
    }

    private void resetScreenDirection() {
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
//				Log.d("video", "zAngle = " + zAngle );
                if (mIsDouble) {
                    if (Math.abs(zAngle) > 90.0) {
                        mResetXAngle = (float) Math.toDegrees(out[0]);
//						Log.d("video", "mResetXAngle = " + mResetXAngle );
                        isReverseScreen = true;
                    } else {
                        isReverseScreen = false;
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