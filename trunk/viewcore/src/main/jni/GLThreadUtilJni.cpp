//
// Created by mac on 16/8/19.
//

#include <string.h>
#include <inttypes.h>
#include <EGL/egl.h>
#include <GLES3/gl3.h>

#include "viewcore.h"
#include "ThreadPool.h"

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT void JNICALL Java_com_bfmj_viewcore_util_GLThreadUtil_onSurfaceCreated(JNIEnv* env, jobject obj);
JNIEXPORT void JNICALL Java_com_bfmj_viewcore_util_GLThreadUtil_onSurfaceChanged(JNIEnv* env, jobject obj, jint width, jint height);
JNIEXPORT void JNICALL Java_com_bfmj_viewcore_util_GLThreadUtil_onDrawFrame(JNIEnv* env, jobject obj);

#ifdef __cplusplus
}
#endif


EGLContext gShareContext;
EGLDisplay gDisplay;
CThreadPool gThreadPool(1);

GLuint CreateSimpleTexture2D( )
{
    // Texture object handle
    GLuint textureId =0;

    // 2x2 Image, 3 bytes per pixel (R, G, B)
    GLubyte pixels[4 * 3] =
            {
                    255,   0,   0, // Red
                    0, 255,   0, // Green
                    0,   0, 255, // Blue
                    255, 255,   0  // Yellow
            };

    // Use tightly packed data
    glPixelStorei ( GL_UNPACK_ALIGNMENT, 1 );

    // Generate a texture object
    glGenTextures ( 1, &textureId );


    // Bind the texture object
    glBindTexture ( GL_TEXTURE_2D, textureId );

    // Load the texture
    glTexImage2D ( GL_TEXTURE_2D, 0, GL_RGB, 2, 2, 0, GL_RGB, GL_UNSIGNED_BYTE, pixels );

    // Set the filtering mode
    glTexParameteri ( GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST );
    glTexParameteri ( GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST );

    return textureId;

}

class CMyTask: public CTask
{
public:
    CMyTask(){}

    int Run()
    {
        if( !eglMakeCurrent( gDisplay, NULL, NULL, gShareContext )){
            printf("error");
        }

        GLuint textureId = CreateSimpleTexture2D ();

        LOGI("mytask textureid = %d", textureId );
//        sleep(1);
        return 0;
    }
};

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

CMyTask taskObj;

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

