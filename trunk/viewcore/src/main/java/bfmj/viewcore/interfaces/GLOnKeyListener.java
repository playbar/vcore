package bfmj.viewcore.interfaces;

import bfmj.viewcore.view.GLRectView;

public interface GLOnKeyListener {
	boolean onKeyDown(GLRectView view, int keycode);
	boolean onKeyUp(GLRectView view, int keycode);
	boolean onKeyLongPress(GLRectView view, int keycode);
}
