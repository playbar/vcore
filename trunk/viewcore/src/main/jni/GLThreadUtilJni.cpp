//
// Created by mac on 16/8/19.
//

#include <string.h>
#include <inttypes.h>
#include <EGL/egl.h>
#include <GLES3/gl3.h>

#include "ThreadPool.h"
#include "GenTexTask.h"


EGLContext gShareContext;
EGLDisplay gDisplay;
CThreadPool gThreadPool(1);


void createSharedContext(){
    EGLint contextAttribs[] = { EGL_CONTEXT_CLIENT_VERSION, 3, EGL_NONE };
    EGLint numConfigs = 0;
    EGLConfig config;
    int flags = 0;
    EGLint attribList[] =
            {
                    EGL_RED_SIZE,       5,
                    EGL_GREEN_SIZE,     6,
                    EGL_BLUE_SIZE,      5,
                    EGL_ALPHA_SIZE,     8,
                    EGL_DEPTH_SIZE,     8,
                    EGL_STENCIL_SIZE,   8,
                    EGL_SAMPLE_BUFFERS, 1,
                    EGL_RENDERABLE_TYPE, EGL_OPENGL_ES2_BIT,
                    EGL_NONE
            };

    // Choose config
    EGLDisplay display = eglGetCurrentDisplay( );
    EGLContext context = eglGetCurrentContext();
    if ( !eglChooseConfig ( display, attribList, &config, 1, &numConfigs ) )
    {
        return;
    }

    gShareContext = eglCreateContext( display, config, context, contextAttribs );
    return;

}

GenTexTask taskObj;

JNIEXPORT void JNICALL
Java_com_bfmj_viewcore_util_GLThreadUtil_onSurfaceCreated(JNIEnv* env, jobject obj)
{
    gDisplay = eglGetCurrentDisplay();
    createSharedContext();
    gThreadPool.Create();
    gThreadPool.AddTask( &taskObj );
    return;
}

JNIEXPORT void JNICALL
Java_com_bfmj_viewcore_util_GLThreadUtil_onSurfaceChanged(JNIEnv* env, jobject obj, jint width, jint height)
{

}

JNIEXPORT void JNICALL
Java_com_bfmj_viewcore_util_GLThreadUtil_onDrawFrame(JNIEnv* env, jobject obj)
{

}

