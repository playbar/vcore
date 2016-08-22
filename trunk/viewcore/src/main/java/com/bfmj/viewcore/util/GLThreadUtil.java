package com.bfmj.viewcore.util;

import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by mac on 16/8/19.
 */
public class GLThreadUtil {

    public native void onDrawFrame(GL10 gl);

    public native void onSurfaceChanged(GL10 gl, int width, int height);

    public native void onSurfaceCreated(GL10 gl, EGLConfig config);

}
