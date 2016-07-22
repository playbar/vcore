package bfmj.viewcore.view;

import android.content.Context;
import android.opengl.Matrix;

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
	}
	
	@Override
	public void draw(boolean isLeft) {
		float[] mtx = new float[16];
		Matrix.setRotateM(mtx, 0, 0, 1, 1, 1);
		getMatrixState().setVMatrix(mtx);
		super.draw(isLeft);
	}
}
