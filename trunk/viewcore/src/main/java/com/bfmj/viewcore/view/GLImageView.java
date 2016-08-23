package com.bfmj.viewcore.view;

import java.io.IOException;
import java.io.InputStream;
import com.bfmj.viewcore.render.GLRenderParams;
import com.bfmj.viewcore.util.GLGenTexTask;
import com.bfmj.viewcore.util.GLTextureUtils;
import com.bfmj.viewcore.util.GenTextureTask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

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
	private int textureId = -1;
    
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

		if (isSurfaceCreated()){
			getRootView().mCreateTextureQueue.offer(this);
		}
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

		if (isSurfaceCreated()){
			getRootView().mCreateTextureQueue.offer(this);
		}
	}

	private  Bitmap mTmpbitmap = null;

	@Override
	public void createTexture(){
		Log.e("testtime", "createTexture 10 thread = >  " + Thread.currentThread() + "; time => " + System.nanoTime());
		if (!isSurfaceCreated()){
			return;
		}
		

//			textureId = -1;
//    		if (mIsCutting) {
//    			textureId = GLTextureUtils.initImageTexture(getContext(), GLTextureUtils.handleBitmap(bitmap, isRecycle), true);
//    		} else {
//    			textureId = GLTextureUtils.initImageTexture(getContext(), bitmap, true);
//    		}
//
//			if (textureId > -1){
//				mRenderParams = new GLRenderParams(GLRenderParams.RENDER_TYPE_IMAGE);
//				mRenderParams.setTextureId(textureId);
//				updateRenderSize(mRenderParams, getInnerWidth(), getInnerHeight());
//			}
//
//			if (mRenderParams != null){
//				addRender(mRenderParams);
//			}

			Log.e("testtime", "createTexture 0 thread = >  " + Thread.currentThread() + "; time => " + System.nanoTime());
			final GLGenTexTask mTask = new GLGenTexTask(GLImageView.this.hashCode());
			mTask.setGenTexIdInterface( new GLGenTexTask.GenTexIdInterface(){
				public void ExportTextureId(int textureId, int mHashCode){
					float width = getInnerWidth();
					float height = getInnerHeight();

//		if (width <= 0){
//			return;
//		}

					if (mRenderParams != null){
						removeRender(mRenderParams);
						mRenderParams = null;
					}

					boolean isRecycle = true;
//		Bitmap bitmap = null;
					if (mResId != 0){
						InputStream is = getContext().getResources().openRawResource(mResId);

						try {
							mTmpbitmap = BitmapFactory.decodeStream(is);
						} finally {
							try {
								is.close();
							} catch(IOException e) {
								e.printStackTrace();
							}
						}
					} else if (mBitmap != null){
						mTmpbitmap = mBitmap;
						isRecycle = false;
					}

					if (mTmpbitmap != null){
						if ( getWidth() == 0 || getHeight() == 0 ){
							setWidth( mTmpbitmap.getWidth());
							setHeight( mTmpbitmap.getHeight() );
						}
						if (getHeight() == WRAP_CONTENT){
							height = mTmpbitmap.getHeight() * width / mTmpbitmap.getWidth();
							setHeight(height + getPaddingTop() + getPaddingBottom());
						}

						GLTextureUtils.mUseMipMap = getMipMap();

//					Log.e("GLImageView", "ExportTextureId");
					if (mHashCode == GLImageView.this.hashCode()){
						Log.e("testtime", "createTexture 4 thread = >  " + Thread.currentThread() + "; time => " + System.nanoTime());
						textureId = GLTextureUtils.initImageTexture(getContext(), mTmpbitmap, false);
						Log.e("testtime", "createTexture 5 thread = >  " + Thread.currentThread() + "; time => " + System.nanoTime());
//						textureId = textureId;
					}
					if (textureId > -1){
						mRenderParams = new GLRenderParams(GLRenderParams.RENDER_TYPE_IMAGE);
						mRenderParams.setTextureId(textureId);
						updateRenderSize(mRenderParams, getInnerWidth(), getInnerHeight());
					}

					if (mRenderParams != null){
						addRender(mRenderParams);
					}
					mTask.uninit();
					mTmpbitmap = null;
//					mBitmap.recycle();

					}
				}
			});
			Log.e("testtime", "createTexture 1 thread = >  " + Thread.currentThread() + "; time => " + System.nanoTime());
			mTask.init();
			Log.e("testtime", "createTexture 2 thread = >  " + Thread.currentThread() + "; time => " + System.nanoTime());
			try {
				mTask.GenTexId(mTmpbitmap, 0, 0);
				Log.e("testtime", "createTexture 3 thread = >  " + Thread.currentThread() + "; time => " + System.nanoTime());
			} catch (Exception e) {
				// TODO: handle exception
			}

			Log.e("testtime", "createTexture 10 thread = >  " + Thread.currentThread() + "; time => " + System.nanoTime());


	}

	@Override
	public void initDraw() {
		super.initDraw();

		if (isVisible()){
			createTexture();
		}
	}

	@Override
	public void setVisible(boolean isVisible) {
		if (isVisible){
			if (mRenderParams == null){
				createTexture();
			}
		}

		super.setVisible(isVisible);
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
