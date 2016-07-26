package com.baofeng.mojing;

public class MojingSDK3288
{
//    static
//    {
//        System.loadLibrary("com.baofeng.mojing");
//    }
    public static native void RenderInit(int width, int height, int modelType, float[] modelView, float[] perspective);
    public static native void RenderFrame(float[] sensorView);
}
