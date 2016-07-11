package com.bfmj.viewcore;

import android.annotation.SuppressLint;
import android.os.Bundle;
import com.bfmj.viewcore.view.GLRootView;
import com.bfmj.viewcore.view.GLSenceView;

public class LxkTestActivity extends BaseViewActivity {
	private GLRootView rootView;

	@SuppressLint("SdCardPath")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.setDistortionEnable(false);
		super.onCreate(savedInstanceState);
		
		rootView = getRootView();
		rootView.setDoubleScreen(false);
		rootView.onResume();

		GLSenceView senceView = new GLSenceView(this);
		//senceView.setImage(R.drawable.bg);
		senceView.setObjFile("qiu.obj");
		senceView.scale(0.5f);
		senceView.rotate(180, 0, 0, 1);
		senceView.rotate(180, 0, 1, 0);
		rootView.addView(senceView);
	}
}
