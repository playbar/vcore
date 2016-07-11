package com.bfmj.viewcore.render;

public class GLScreenParams {
	public static float UNIT = 184.6f;
	
	private static float NEAR = 0.916f;
	private static float FAR = 200.0f;
	private static float screenWidth = 4f / NEAR;
	private static float screenHeight = 4f / NEAR;
	private static float xDpi = 960;
	private static float yDpi = 960;
	private static float defualtDepth = 4;
	private static float eyeDistance = 0.05f;
	
	public static float getNear(){
		return NEAR;
	}
	
	public static void setNear(float near){
		NEAR = near;
	}
	
	public static float getFar(){
		return FAR;
	}
	
	public static float getScreenWidth(){
		return screenWidth;
	}
	
	public static float getScreenHeight(){
		return screenHeight;
	}
	
	public static void setDpi(float x, float y){
		xDpi = x;
		yDpi = y;
	}
	
	public static void setXDpi(float x){
		xDpi = x;
	}
	
	public static void setYDpi(float y){
		yDpi = y;
	}
	
	public static float getXDpi(){
		return xDpi;
	}
	
	public static float getYDpi(){
		return yDpi;
	}

	public static float getDefualtDepth() {
		return defualtDepth;
	}

	public static void setDefualtDepth(float defualtDepth) {
		GLScreenParams.defualtDepth = defualtDepth;
	}

	public static float getEyeDistance() {
		return eyeDistance;
	}

	public static void setEyeDistance(float eyeDistance) {
		GLScreenParams.eyeDistance = eyeDistance;
	}
}
