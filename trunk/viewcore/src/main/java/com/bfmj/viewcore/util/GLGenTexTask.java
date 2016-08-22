package com.bfmj.viewcore.util;

import android.graphics.Bitmap;

/**
 * Created by mac on 16/8/19.
 */
public class GLGenTexTask {

    private int mClassID=0;
    public int mTextureId;
    public int mHashCode;
    private GenTexIdInterface mGenTexInface = null;
    public GLGenTexTask(){
    }

    public void init(){
        NativeInit();
    }

    public void uninit(){
        NativeUninit();
    }

    private native void NativeInit();
    private native void NativeUninit();
    private native void NativeGenTexId(Bitmap bmp, int widht, int height);

    public void GenTexId(Bitmap bmp, int width, int height ){
        NativeGenTexId(bmp, width, height);
    }

    public void setGenTexIdInterface( GenTexIdInterface genInterface ){
        mGenTexInface = genInterface;
    }

    public void ExportTextureId(int mTextureId, int mHashCode){
        if( null != mGenTexInface ){
            mGenTexInface.ExportTextureId(mTextureId, mHashCode);
        }
    }

    public interface GenTexIdInterface{
        void ExportTextureId(int mTextureId, int mHashCode);
    }
}
