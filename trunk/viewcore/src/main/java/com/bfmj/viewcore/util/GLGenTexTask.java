package com.bfmj.viewcore.util;

import android.graphics.Bitmap;

/**
 * Created by mac on 16/8/19.
 */
public class GLGenTexTask {

    public int mTextureId;
    public int mHashCode;
    private GenTexIdInterface mGenTexInface = null;
    public GLGenTexTask(){

    }

    public void GenTexId(Bitmap bmp, int width, int height ){

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
