package com.bfmj.viewcore.view;

import android.content.Context;

import com.baofeng.mojing.input.base.MojingKeyCode;
import com.bfmj.viewcore.adapter.GLListAdapter;
import com.bfmj.viewcore.interfaces.GLOnKeyListener;
import com.bfmj.viewcore.interfaces.GLViewFocusListener;
import com.bfmj.viewcore.render.GLConstant.GLOrientation;

import java.util.ArrayList;

public class GLGridViewPage extends GLGridView {

	/**
	 * GLGridView构造函数
	 * @param context
	 */
	public GLGridViewPage(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/**
	 * GLGridView构造函数
	 * @param context
	 * @param rows 行数
	 * @param columns 列数
	 */
	public GLGridViewPage(Context context, int rows, int columns){
		super(context, rows, columns );
	}

	/**
	 * 设置列数
	 * @param columns 列数
	 */
	public void setNumColumns(int columns){
		super.setNumColumns( columns );
	}

	/**
	 * 设置行数
	 * @param rows 行数
	 */
	public void setNumRows(int rows){
		super.setNumRows( rows );
	}

	/**
	 * 设置垂直间距
	 * @param verticalSpacing 垂直间距
	 */
	public void setVerticalSpacing(float verticalSpacing){
		super.setVerticalSpacing( verticalSpacing );
	}

	/**
	 * 设置水平间矩
	 * @param horizontalSpacing 水平间矩
	 */
	public void setHorizontalSpacing(float horizontalSpacing){
		super.setHorizontalSpacing( horizontalSpacing );
	}

	/**
	 * 设置一屏显示几条数据
	 * @param oneScreen
	 */
	public void setNumOneScreen(int oneScreen){
		super.setNumOneScreen( oneScreen );
	}

	/**
	 * 设置当前第一个显示的索引
	 * @param index 开始的索引，从0开始算
	 */
	public void setStartIndex(int index){
		super.setStartIndex( index );
	}

	/**
	 * 获取当前的索引
	 * @return
	 */
	public int getStartIndex(){
		return super.getStartIndex();
	}

	@Override
	public void addView(GLRectView view) {
		// TODO Auto-generated method stub
		super.addView(view);
	}
	
	@Override
	public void addView(int index, GLRectView view) {
		// TODO Auto-generated method stub
		super.addView(index, view);
		
	}

	@Override
	public GLListAdapter getGLAdapter() {
		return super.getGLAdapter();
	}

	@Override
	public void setAdapter(GLListAdapter adapter) {
		super.setAdapter( adapter );
	}

	@Override
	public GLView getSelectedGLView() {
		return super.getSelectedGLView();
	}
	
	/**
	 * 返回索引为index的项
	 * @param index
	 * @return
	 */
	public GLRectView getChatAtChild(int index){

		return null;
	}
	
	/**
	 * 得到总个数
	 * @return
	 */
	public int getTotalNum(){
		return super.getTotalNum();
	}
	
	/**
	 * 删除一个View
	 * @param index 要删除的索引
	 */
	public void removeView(int index){
		super.removeView( index );
	}
	
	/**
	 * 添加一个控件到最后
	 * @param view
	 */
	public void addChildView(GLRectView view){

		super.addChildView( view );
	}
	
	/**
	 * 添加一个控件到指定的索引处
	 * @param view
	 * @param index
	 */
	public void addChildView(GLRectView view, int index){

		super.addChildView( view, index );

	}

	@Override
	public void draw(boolean isLeft) {
		super.draw(isLeft);
	}

	public int getPrevIndex() {
		return super.getPrevIndex();
	}
	
	public void setChildFocus() {
		super.setChildFocus();
	}
	
	/**
	 * 获取列数
	 * @author linzanxian  @Date 2015-7-9 下午3:09:13
	 * @return int
	 */
	public int getNumColumns() {
		return super.getNumColumns();
	}

	public void setOrientation(GLOrientation orientation) {
		super.setOrientation( orientation );
	}

	/**
	 * 获取布局方向
	 * @author linzanxian  @Date 2015年3月10日 下午5:36:47
	 * @return String
	 */
	public GLOrientation getOrientation() {
		return super.getOrientation();
	}

}
