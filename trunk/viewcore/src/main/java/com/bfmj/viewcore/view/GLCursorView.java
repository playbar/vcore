package com.bfmj.viewcore.view;

import android.content.Context;
import android.opengl.Matrix;

import com.baofeng.mojing.MojingSDK;

/**
 * 用于头控状态下的光标
 * ClassName: GLCursorView <br/>
 * @author lixianke    
 * @date: 2015-6-19 下午5:14:35 <br/>  
 * description:
 */
public class GLCursorView extends GLImageView {

	public GLCursorView(Context context) {
		super(context);
		setFixed(true);
	}

	@Override
	public void draw(boolean isLeft) {
		if (!isFixed()){
			float[] o = new float[3];
			MojingSDK.getLastHeadEulerAngles(o);
			float[] mtx = new float[16];
			Matrix.setIdentityM(mtx, 0);
			Matrix.rotateM(mtx, 0, (float)Math.toDegrees(o[0]), 0, 1, 0);
			Matrix.rotateM(mtx, 0, (float)Math.toDegrees(o[1]), 1, 0, 0);
			Matrix.rotateM(mtx, 0, (float)Math.toDegrees(o[2]), 0, 0, 1);
			getMatrixState().setVMatrix(mtx);
		}
		super.draw(isLeft);
	}
}
