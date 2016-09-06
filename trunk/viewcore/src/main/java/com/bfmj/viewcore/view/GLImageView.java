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
import android.graphics.Matrix;
import android.util.Log;

/**
 *
 * ClassName: GLImageView <br/>
 * @author lixianke
 * @date: 2015-3-9 下午1:51:44 <br/>
 * description:
 */
public class GLImageView extends GLRectView {

	private int mResId = 0;
	private int mLastResId = 0;
	private Bitmap mBitmap = null;
	private Bitmap mLastBitmap = null;
	private GLRenderParams mRenderParams;
	private boolean mIsCutting = true;
	private  Bitmap mTmpbitmap = null;

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
		mLastBitmap = null;

		updateTexture();
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
		mLastResId = 0;

		updateTexture();
	}


	@Override
	public void initDraw() {
		super.initDraw();
		createTexture();
	}

	@Override
	public void setVisible(boolean isVisible) {
		super.setVisible(isVisible);
		updateTexture();
	}

	private void updateTexture(){
		if (!isSurfaceCreated() || !isVisible()){
			return;
		}

		if (mBitmap != mLastBitmap ||
				mLastResId != mResId){
			getRootView().mCreateTextureQueue.offer(this);
		}
	}

	@Override
	public void createTexture(){
		if (!isSurfaceCreated() || !isVisible()){
			return;
		}

		mLastResId = mResId;
		mLastBitmap = mBitmap;

		GLGenTexTask.QueueEvent( new GLGenTexTask(){
			public void ExportTextureId( ){
//				Log.e("GLImageView", "ExportTextureId");
				float width = getInnerWidth();
				float height = getInnerHeight();

				boolean isRecycle = true;

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

				if (mTmpbitmap != null) {
					if (getWidth() == 0 || getHeight() == 0) {
						setWidth(mTmpbitmap.getWidth());
						setHeight(mTmpbitmap.getHeight());
					}
					if (getHeight() == WRAP_CONTENT) {
						height = mTmpbitmap.getHeight() * width / mTmpbitmap.getWidth();
						setHeight(height + getPaddingTop() + getPaddingBottom());
					}

					GLTextureUtils.mUseMipMap = getMipMap();

					int textureId = -1;
					if (mIsCutting && (width > 100 || height > 100) ) {
						mTmpbitmap = GLTextureUtils.handleBitmap(mTmpbitmap, width, height);
						isRecycle = true;
					}
					textureId = GLTextureUtils.initImageTexture(getContext(), mTmpbitmap, isRecycle);
					if (textureId > 0) {
						if (mRenderParams == null){
							mRenderParams = new GLRenderParams(GLRenderParams.RENDER_TYPE_IMAGE);
							addRender(mRenderParams);
						} else if (mRenderParams.getTextureId() > 0){
							releaseTexture(mRenderParams.getTextureId());
						}
						mRenderParams.setTextureId(textureId);
						updateRenderSize(mRenderParams, getInnerWidth(), getInnerHeight());
					}

					if( isRecycle ) {
						mTmpbitmap = null;
					}
				}
			}
		});

//		final GLGenTexTask mTask = new GLGenTexTask(GLImageView.this.hashCode());
//		mTask.setGenTexIdInterface( new GLGenTexTask.GenTexIdInterface(){
//			public void ExportTextureId(int textureId, int mHashCode){
//				float width = getInnerWidth();
//				float height = getInnerHeight();
//
//				if (mRenderParams != null){
//					removeRender(mRenderParams);
//					mRenderParams = null;
//				}
//
//				boolean isRecycle = true;
//
//				if (mResId != 0){
//					InputStream is = getContext().getResources().openRawResource(mResId);
//
//					try {
//						mTmpbitmap = BitmapFactory.decodeStream(is);
//					} finally {
//						try {
//							is.close();
//						} catch(IOException e) {
//							e.printStackTrace();
//						}
//					}
//				} else if (mBitmap != null){
//					mTmpbitmap = mBitmap;
//					isRecycle = false;
//				}
//
//				if (mTmpbitmap != null) {
//					if (getWidth() == 0 || getHeight() == 0) {
//						setWidth(mTmpbitmap.getWidth());
//						setHeight(mTmpbitmap.getHeight());
//					}
//					if (getHeight() == WRAP_CONTENT) {
//						height = mTmpbitmap.getHeight() * width / mTmpbitmap.getWidth();
//						setHeight(height + getPaddingTop() + getPaddingBottom());
//					}
//
//					GLTextureUtils.mUseMipMap = getMipMap();
//
////					Log.e("GLImageView", "ExportTextureId");
//					if (mHashCode == GLImageView.this.hashCode()) {
//						textureId = GLTextureUtils.initImageTexture(getContext(), mTmpbitmap, false);
////						textureId = textureId;
//					}
//					if (textureId > -1) {
//						mRenderParams = new GLRenderParams(GLRenderParams.RENDER_TYPE_IMAGE);
//						mRenderParams.setTextureId(textureId);
//						updateRenderSize(mRenderParams, getInnerWidth(), getInnerHeight());
//					}
//
//					if (mRenderParams != null) {
//						addRender(mRenderParams);
//					}
//					mTmpbitmap = null;
//				}
//			}
//		});
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

	@Override
	public void release() {
		super.release();
		mLastResId = 0;
		mLastBitmap = null;
		mRenderParams = null;
	}
}
