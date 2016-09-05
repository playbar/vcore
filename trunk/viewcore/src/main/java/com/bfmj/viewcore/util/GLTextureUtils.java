package com.bfmj.viewcore.util;

import java.io.IOException;
import java.io.InputStream;
import javax.microedition.khronos.opengles.GL10;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.opengl.GLES11Ext;
import android.opengl.GLES30;
import android.opengl.GLUtils;
import android.util.Log;

import com.bfmj.viewcore.render.GLColor;
import com.bfmj.viewcore.view.BaseViewActivity;

/**
 * 
 * ClassName: GLTextureUtils <br/>
 * @author lixianke    
 * @date: 2015-3-9 下午1:51:58 <br/>  
 * description:
 */
public class GLTextureUtils {
	public static boolean mUseMipMap = false;

	public static int initImageTexture(Context context, int drawableId) {
		//通过输入流加载图片===============begin===================
        InputStream is = context.getResources().openRawResource(drawableId);
        Bitmap bitmap;
        try 
        {
        	bitmap = BitmapFactory.decodeStream(is);
        } 
        finally 
        {
            try 
            {
                is.close();
            } 
            catch(IOException e) 
            {
                e.printStackTrace();
            }
        }
        //通过输入流加载图片===============end=====================  
        
        return initImageTexture(context, bitmap, true);
	}
	
	public static int initImageTexture(Context context, Bitmap bm, boolean isRecycle) {
//		Log.e("GLTextureUtils", "initImageTexture begin");
		if (bm == null) {
			return -1;
		}
		
		//生成纹理ID
		int[] textures = new int[1];

		GLES30.glGenTextures(1, textures, 0);
		
		if (mUseMipMap) {
			GLES30.glEnable(GLES30.GL_TEXTURE_2D);
			GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textures[0]);
			GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER,GLES30.GL_LINEAR_MIPMAP_LINEAR);
			GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D,GLES30.GL_TEXTURE_MAG_FILTER,GLES30.GL_LINEAR_MIPMAP_LINEAR);
			GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S,GLES30.GL_CLAMP_TO_EDGE);
			GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T,GLES30.GL_CLAMP_TO_EDGE);
	        //实际加载纹理       
			GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bm, 0);
			GLES30.glGenerateMipmap(GLES30.GL_TEXTURE_2D);
		} else {
			GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textures[0]);
			GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER,GLES30.GL_LINEAR);
			GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D,GLES30.GL_TEXTURE_MAG_FILTER,GLES30.GL_LINEAR);
			GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S,GLES30.GL_CLAMP_TO_EDGE);
			GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T,GLES30.GL_CLAMP_TO_EDGE);
	        //实际加载纹理       
	        GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bm, 0);
		}
        
        if (isRecycle){
	        bm.recycle(); 		  //纹理加载成功后释放图片
		}
//		BaseViewActivity.log(System.currentTimeMillis() + "-create : " + textures[0] + " " + Thread.currentThread());
//		Log.e("GLTextureUtils", "initImageTexture end");
        return textures[0];
	}
	
	public static void releaseTexture(int textureId){
		if (textureId < 0) {
			return;
		}
		BaseViewActivity.log(System.currentTimeMillis() + "-release : " + textureId + " " + Thread.currentThread());
		GLES30.glDeleteTextures(1, new int[]{textureId}, 0);
	}
	
	/**
	 * 生成视频纹理ID
	 * @author lixianke  @Date 2015-3-17 上午9:59:19
	 * @param
	 * @return
	 */
	@SuppressLint("InlinedApi")
	public static int createVideoTextureID(){
		int[] texture = new int[1];

		GLES30.glGenTextures(1, texture, 0);
		GLES30.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0]);
		GLES30.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
				GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_LINEAR);
		GLES30.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
				GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		GLES30.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
				GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
		GLES30.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
				GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

		return texture[0];
	}
	
	/**
	 * 图片边框处理为透明
	 * @author lixianke  @Date 2015-3-27 下午2:38:43
	 * @param bm
	 * @param width
	 * @param height
	 */
	public static Bitmap handleBitmap(Bitmap bm, float width, float height){
		int border = 1;
		int bWidth = bm.getWidth();
		int bHeight = bm.getHeight();
		int w = (int)width / 2;
		int h = (int)height / 2;
		if (w < 16){
			w = 16;
		}
		if (h < 16){
			h = 16;
		}

    	Bitmap bitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.TRANSPARENT);
        
        try {
			Paint p = new Paint();
			Rect oRect = new Rect(0, 0, bWidth, bHeight);
			Rect dRect = new Rect(border, border, w - border, h - border);
        	canvas.drawBitmap(bm, oRect, dRect, p);
		} catch (Exception e) {
			return null;
		}

        return bitmap;
	}

	public static Bitmap handleColor(GLColor color, float width, float height){
		int border = 0;
		int w = (int)width / 8;
		int h = (int)height / 8;
		if (w < 16){
			w = 16;
		}
		if (h < 16){
			h = 16;
		}

		Bitmap bmp = Bitmap.createBitmap(w , h, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bmp);
		canvas.drawColor(Color.TRANSPARENT);

		Paint p = new Paint();
		p.setStyle(Paint.Style.FILL);
		p.setARGB((int)(color.getA() * 255), (int)(color.getR() * 255),
				(int)(color.getG() * 255), (int)(color.getB() * 255));
		canvas.drawRect(border, border, w - border, h - border, p);

		return bmp;
	}
}
