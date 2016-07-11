package com.bfmj.viewcore.view;

import java.io.IOException;
import java.io.InputStream;
import com.bfmj.viewcore.render.GLRenderParams;
import com.bfmj.viewcore.util.GLTextureUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 
 * ClassName: GLImageView <br/>
 * @author lixianke    
 * @date: 2015-3-9 下午1:51:44 <br/>  
 * description:
 */
public class GLImageView extends GLRectView {
	
	private int mResId;
    private Bitmap mBitmap;
    private GLRenderParams mRenderParams;
    private boolean mIsCutting = true;
    
	public GLImageView(Context context) {
		super(context);
	}
	
	/**
	 * 设置图片
	 * @author lixianke  @Date 2015-3-11 下午5:06:59
	 * @param resId 资源ID
	 * @return
	 */
	public void setImage(int resId){
		if (mResId == resId){
			return;
		}
		
		mResId = resId;
		mBitmap = null;
		
		initImage();
	}
	
	/**
	 * 设置图片
	 * @author lixianke  @Date 2015-3-11 下午5:07:28
	 * @param bitmap Bitmap对象
	 * @return
	 */
	public void setImage(Bitmap bitmap){
		if (mBitmap == bitmap){
			return;
		}
		
		mBitmap = bitmap;
		mResId = 0;
		
		initImage();
	}
	
	private void initImage(){
		if (!isSurfaceCreated()){
			return;
		}
		
		float width = getInnerWidth();
		float height = getInnerHeight();
		
		if (width <= 0){
			return;
		}
		
		if (mRenderParams != null){
			removeRender(mRenderParams);
			mRenderParams = null;
		}
		
		boolean isRecycle = true;
		int textureId = -1;
		Bitmap bitmap = null;
		if (mResId != 0){
			InputStream is = getContext().getResources().openRawResource(mResId);
	        
	        try {
	        	bitmap = BitmapFactory.decodeStream(is);
	        } finally {
	            try {
	                is.close();
	            } catch(IOException e) {
	                e.printStackTrace();
	            }
	        }
		} else if (mBitmap != null){
			bitmap = mBitmap;
			isRecycle = false;
		}

    	if (bitmap != null){
    		if (getHeight() == WRAP_CONTENT){
	    		height = bitmap.getHeight() * width / bitmap.getWidth();
	    		setHeight(height + getPaddingTop() + getPaddingBottom());
	    	}
    		
    		GLTextureUtils.mUseMipMap = getMipMap();
    		if (mIsCutting) {
    			textureId = GLTextureUtils.initImageTexture(getContext(), GLTextureUtils.handleBitmap(bitmap, isRecycle), true);
    		} else {
    			textureId = GLTextureUtils.initImageTexture(getContext(), bitmap, true);
    		}
			
		}
		
		if (textureId > -1){
			mRenderParams = new GLRenderParams(GLRenderParams.RENDER_TYPE_IMAGE);
			mRenderParams.setTextureId(textureId);
			updateRenderSize(mRenderParams, getInnerWidth(), getInnerHeight());
		}
		
		if (mRenderParams != null){
			addRender(mRenderParams);
		}
	}

	@Override
	public void initDraw() {
		super.initDraw();

		initImage();
	}
	
	/**
	 * 设置是否裁边
	 * @author linzanxian  @Date 2015-6-30 下午3:24:24
	 * @param cutting 是否裁边  
	 * @return void
	 */
	public void setCutting(boolean cutting) {
		mIsCutting = cutting;
	}
}
