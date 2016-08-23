package com.bfmj.viewcore.util;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * Created by mac on 16/8/19.
 */
public class GLGenTexTask {

    private int mClassID=0;
    public int mTextureId;
    public int mHashCode;
    private GenTexIdInterface mGenTexInface = null;
    public GLGenTexTask(int hashCode){
        mHashCode =hashCode;
        NativeInit();
    }

//    public void init(){
//        NativeInit();
//    }
//
//    public void uninit(){
////        NativeUninit();
//    }

    private native void NativeInit();
    private native void NativeUninit();
    private native void NativeQueueEvent();
    private native void NativeGenTexId(Bitmap bmp, int widht, int height);

//    public void GenTexId(Bitmap bmp, int width, int height ){
//        NativeGenTexId(bmp, width, height);
//    }

    public void setGenTexIdInterface( GenTexIdInterface genInterface ){
        mGenTexInface = genInterface;
        NativeQueueEvent();
    }

    public void ExportTextureId(int textureId ){
//        Log.e("GLGenTexTask", "ExportTextureId");
        if( null != mGenTexInface ){
            mGenTexInface.ExportTextureId(textureId, mHashCode);
        }
    }

    public interface GenTexIdInterface{
        void ExportTextureId(int textureId, int hashCode);
    }

}
