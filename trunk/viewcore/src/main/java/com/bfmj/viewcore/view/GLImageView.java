package com.bfmj.viewcore.view;

import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import com.bfmj.viewcore.render.GLRenderParams;
import com.bfmj.viewcore.util.GLTextureUtils;
import com.bfmj.viewcore.util.GenTextureTask;

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

	@Override
	public void createTexture(){
		Timer timer = new Timer();
		timer.schedule( task, 4000);
	}

	public  TimerTask task = new TimerTask() {
		public void run() {
			if (!isSurfaceCreated()){
				return;
			}

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
				if ( getWidth() == 0 || getHeight() == 0 ){
					setWidth( bitmap.getWidth());
					setHeight( bitmap.getHeight() );
				}
				if (getHeight() == WRAP_CONTENT){
					height = bitmap.getHeight() * width / bitmap.getWidth();
					setHeight(height + getPaddingTop() + getPaddingBottom());
				}

				GLTextureUtils.mUseMipMap = getMipMap();
//    		if (mIsCutting) {
//    			textureId = GLTextureUtils.initImageTexture(getContext(), GLTextureUtils.handleBitmap(bitmap, isRecycle), true);
//    		} else {
//    			textureId = GLTextureUtils.initImageTexture(getContext(), bitmap, true);
//    		}
				textureId = -1;
				GenTextureTask task = new GenTextureTask(getContext(), GLImageView.this.hashCode(), bitmap);
				task.SetInterfaceTex(new GenTextureTask.ExportTextureId(){

					@Override
					public void exportId(int texid, int hashcode) {
						if (hashcode == GLImageView.this.hashCode()){
							textureId = texid;
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
				});

				GLRootView.GetThreadPool().execute(task);
			}
			return;
		}
	};

	@Override
	public void initDraw() {
		super.initDraw();

		createTexture();
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
