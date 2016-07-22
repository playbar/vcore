package bfmj.viewcore.adapter;

import bfmj.viewcore.view.GLGroupView;
import bfmj.viewcore.view.GLRectView;


public interface GLSpinnerAdapter extends GLAdapter {
	public GLRectView getDropDownView(int position, GLRectView convertView, GLGroupView parent);
}
