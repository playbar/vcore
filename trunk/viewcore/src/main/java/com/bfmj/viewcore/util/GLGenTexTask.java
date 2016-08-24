package com.bfmj.viewcore.util;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * Created by mac on 16/8/19.
 */
public abstract class GLGenTexTask {

    private int mClassID=0;
    public int mTextureId;
    public int mHashCode;
    private GenTexIdInterface mGenTexInface = null;
    public GLGenTexTask(int hashCode){
        mHashCode =hashCode;
        NativeInit();
    }

    public static void QueueEvent( GLGenTexTask task){
        task.NativeQueueEvent();
    }

    private native void NativeInit();
    private native void NativeUninit();
    private native void NativeQueueEvent();
    private native void NativeGenTexId(Bitmap bmp, int widht, int height);


    public void setGenTexIdInterface( GenTexIdInterface genInterface ){
        mGenTexInface = genInterface;
        NativeQueueEvent();
    }

    abstract public void ExportTextureId(int textureId );
//    {
////        Log.e("GLGenTexTask", "ExportTextureId");
//        if( null != mGenTexInface ){
//            mGenTexInface.ExportTextureId(textureId, mHashCode);
//        }
//    }

    public interface GenTexIdInterface{
        void ExportTextureId(int textureId, int hashCode);
    }

}
