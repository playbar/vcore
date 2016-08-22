//
// Created by mac on 16/8/19.
//

#include "GenTexTask.h"
#include "android/bitmap.h"

#define GENTEXTASKCLASS "com/bfmj/viewcore/util/GLGenTexTask"

jclass GenTexTask::mThizClass = NULL;
jfieldID GenTexTask::mClassID = NULL;
jmethodID GenTexTask::mExportTextureId = NULL;

static JavaVM *gs_jvm=NULL;
static jobject gs_object=NULL;

static GenTexTask* getGenTexTask( JNIEnv* env, jobject thiz)
{
    GenTexTask *p = (GenTexTask*)env->GetIntField(thiz, GenTexTask::mClassID );
    return p;
}

JNIEXPORT void JNICALL Java_com_bfmj_viewcore_util_GLGenTexTask_NativeInit(JNIEnv* env, jobject thiz)
{
    env->GetJavaVM(&gs_jvm); //保存到全局变量中JVM
    gs_object=env->NewGlobalRef(thiz); // DeleteGlobalRef

    if( GenTexTask::mThizClass == NULL ){
        GenTexTask::mThizClass = env->FindClass(GENTEXTASKCLASS);
    }
    GenTexTask::mClassID = env->GetFieldID( GenTexTask::mThizClass, "mClassID", "I");
    GenTexTask::mExportTextureId = env->GetMethodID( GenTexTask::mThizClass, "ExportTextureId", "(I)V");

    GenTexTask *pTask = new GenTexTask(env, thiz);
    GenTexTask *pTmp = (GenTexTask*)env->GetIntField(thiz, GenTexTask::mClassID );
    env->SetIntField( thiz, GenTexTask::mClassID, (int)pTask);
    if( pTmp != NULL ){
        delete pTmp;
    }
    return;
}


JNIEXPORT void JNICALL Java_com_bfmj_viewcore_util_GLGenTexTask_NativeUninit(JNIEnv* env, jobject thiz)
{
    GenTexTask *pTmp = getGenTexTask( env, thiz);
    delete pTmp;
}

JNIEXPORT void JNICALL Java_com_bfmj_viewcore_util_GLGenTexTask_NativeGenTexId(JNIEnv* env,
            jobject thiz, jobject bmp, jint width, jint height)
{
    GenTexTask *pTmp = getGenTexTask( env, thiz );
    pTmp->GenTexID( bmp, width, height);
    return;
}


GenTexTask::GenTexTask(JNIEnv* env, jobject thiz)
{
    mEnv = env;
    mThiz = thiz;
    mpData = NULL;
}

GenTexTask::GenTexTask()
{
    mEnv = NULL;
    mThiz = NULL;
    mpData = NULL;
}

GenTexTask::~GenTexTask()
{
    if( mpData != NULL ){
        delete mpData;
    }
}

void GenTexTask::GenTexID( jobject bmp, int width, int height )
{
    mBitmap = bmp;
    mWidth = width;
    mHeight = height;
    mpData = new unsigned char[mWidth * mHeight * 4];
    AndroidBitmapInfo infocolor;
    void *pixels;
//    JNIEnv *env = AttachCurrentThreadJNI();
    AndroidBitmap_getInfo(mEnv, mBitmap, &infocolor);
    AndroidBitmap_lockPixels(mEnv, mBitmap, &pixels);
    memcpy( mpData, pixels, mWidth * mHeight );
    AndroidBitmap_unlockPixels(mEnv, mBitmap);


    gThreadPool.AddTask( this );
}

int GenTexTask::Run()
{
    if( !eglMakeCurrent( gDisplay, NULL, NULL, gShareContext )){
        printf("error");
    }

    GLuint textureId = CreateTexture2D();

    if( mThiz != NULL ){
        JNIEnv *env=0;
        gs_jvm->AttachCurrentThread(&env, NULL);
        jclass cls = env->GetObjectClass(gs_object);
        jmethodID fieldPtr = env->GetMethodID(cls,"ExportTextureId", "(I)V");
        env->CallVoidMethod( gs_object, fieldPtr, textureId );
        gs_jvm->DetachCurrentThread();
    }

    LOGI("mytask textureid = %d", textureId );
    return 0;
}


GLuint GenTexTask::CreateSimpleTexture2D( )
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


GLuint GenTexTask::CreateTexture2D( )
{
    GLuint textureId =0;
    // Use tightly packed data
    glPixelStorei ( GL_UNPACK_ALIGNMENT, 1 );

    // Generate a texture object
    glGenTextures ( 1, &textureId );

    // Bind the texture object
    glBindTexture ( GL_TEXTURE_2D, textureId );

//    AndroidBitmapInfo infocolor;
//    void *pixels;
//    JNIEnv *env = AttachCurrentThreadJNI();
//    AndroidBitmap_getInfo(env, mBitmap, &infocolor);
//    AndroidBitmap_lockPixels(env, mBitmap, &pixels);
    // Load the texture
    glTexImage2D ( GL_TEXTURE_2D, 0, GL_RGBA, mWidth, mHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, mpData );

//    AndroidBitmap_unlockPixels(env, mBitmap);

//    DetachCurrentThreadJNI();

    // Set the filtering mode
    glTexParameteri ( GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST );
    glTexParameteri ( GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST );

    return textureId;

}

