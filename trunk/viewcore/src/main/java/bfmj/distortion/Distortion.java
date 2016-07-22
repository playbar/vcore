package bfmj.distortion;


import java.nio.IntBuffer;

import com.baofeng.mojing.EyeTextureParameter;
import com.baofeng.mojing.MojingSDK;

import android.opengl.GLES20;

public class Distortion {	
	private int[] mTextureIds = {0, 0};
	private int framebufferId;
	private int mTextureWidth = 0;
	private int mTextureHeight = 0;
	private static Distortion mInstance;
	
	public static Distortion getInstance() {    
		if (mInstance == null) {
			mInstance = new Distortion(); 
		}    
		
		return mInstance;    
	}
	
	public Distortion() {
		initData();
	}
	
	public void setScreen(int width, int height) {
		this.framebufferId = generateFrameBufferObject();
	}
	
	private void initData() {
	    this.framebufferId = -1;
	}
	
	public void beforeDraw(int eye){
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, this.framebufferId);
		EyeTextureParameter textureParameter = MojingSDK.GetEyeTextureParameter(eye + 1);
		this.mTextureIds[eye] = textureParameter.m_EyeTexID;
		GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, this.mTextureIds[eye], 0);
		GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
    	GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    	mTextureWidth = textureParameter.m_Width;
    	mTextureHeight = textureParameter.m_Height;
    	GLES20.glViewport(0, 0, mTextureWidth, mTextureHeight);
	}
	
	public void afterDraw() {
		GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, 0, 0);
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
		
		if (GLES20.glIsTexture(this.mTextureIds[0]) && GLES20.glIsTexture(this.mTextureIds[1])){
			MojingSDK.DrawTexture(this.mTextureIds[0], this.mTextureIds[1]);
		} else {
			this.framebufferId = generateFrameBufferObject();
		}
	}
	
	private static int generateFrameBufferObject() {
		IntBuffer framebuffer = IntBuffer.allocate(1);
		GLES20.glGenFramebuffers(1, framebuffer); 
		
		return framebuffer.get(0);
	}
	
	public int getTextureWidth(){
		return mTextureWidth;
	}
	
	public int getTextureHeight(){
		return mTextureHeight;
	}
}
