package com.bfmj.viewcore.view;

import android.content.Context;

import com.baofeng.mojing.input.base.MojingInputConfig;
import com.baofeng.mojing.input.base.MojingKeyCode;
import com.bfmj.viewcore.adapter.GLListAdapter;
import com.bfmj.viewcore.interfaces.GLViewFocusListener;
import com.bfmj.viewcore.render.GLColor;
import com.bfmj.viewcore.render.GLConstant;

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

	public void setBottomSpaceing( float spaceing){
		mBtnSpace = spaceing;
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
		showPage();
	}

	@Override
	public void requestLayout(){
		super.requestLayout();
		showPage();
	}

	public void nextPage(){
		if( mCurIndex < mCount ){
			++mCurIndex;
			setStartIndex((mCurIndex - 1) * getNumOneScreen());
			requestLayout();
			showPage();
		}
	}

	public void previousPage(){
		if( mCurIndex > 0 ){
			--mCurIndex;
			setStartIndex((mCurIndex - 1) * getNumOneScreen());
			requestLayout();
			showPage();
		}
	}

	public boolean isLastPage(){
		return (mCurIndex == mCount);
	}

	@Override
	public boolean onKeyDown(int keycode){
		return super.onKeyDown(keycode);
	}

	@Override
	public boolean onKeyUp(int keycode){
		if( nextBtnView.isFocused() || prvBtnView.isFocused() || mbIndexFocused ) {
			if (keycode == MojingKeyCode.KEYCODE_DPAD_CENTER) {
				getRootView().queueEvent(new Runnable() {
					@Override
					public void run() {
						if (nextBtnView.isFocused()) {
							++mCurIndex;
							setStartIndex((mCurIndex - 1) * getNumOneScreen());
							requestLayout();
							showPage();
						} else if (prvBtnView.isFocused()) {
							--mCurIndex;
							setStartIndex((mCurIndex - 1) * getNumOneScreen());
							requestLayout();
							showPage();
						} else if (mbIndexFocused) {
							setStartIndex((mCurFocuseIndex - 1) * getNumOneScreen());
							mCurIndex = mCurFocuseIndex;
							requestLayout();
							showPage();
						}
					}
				});

			}
			return true;
		}else {
			return super.onKeyUp(keycode);
		}
	}

	@Override
	public boolean onKeyLongPress(int keycode){
		return super.onKeyLongPress(keycode);
	}


	private void prvBtn(){

		prvBtnView.setX(mStart - mStep);
		prvBtnView.setY(getY() + getHeight() + mBtnSpace);
		prvBtnView.setLayoutParams(100, 100);
		prvBtnView.setTextColor(new GLColor(1.0f, 1.0f, 1.0f));
		prvBtnView.setBackground(new GLColor(0.43f, 0.4f, 0.34f));

		prvBtnView.setAlignment(GLTextView.ALIGN_CENTER);

		prvBtnView.setText("<");
		prvBtnView.setTextSize(80);
		prvBtnView.setFocusListener(new GLViewFocusListener() {
			@Override
			public void onFocusChange(GLRectView view, boolean focused) {
				if (focused) {
					view.setAlpha(0.3f);
//					--mCurIndex;
//					setStartIndex((mCurIndex - 1) * getNumOneScreen());
//					requestLayout();
//					showPage();
				} else {
					view.setAlpha(1.0f);
				}
			}
		});
		addView(prvBtnView);
		return;
	}

	private void nextBtn(){

		nextBtnView.setX(mStart + mShowMaxCount * mStep);
		nextBtnView.setY(getY() + getHeight() + mBtnSpace);
		nextBtnView.setLayoutParams(100, 100);
		nextBtnView.setTextColor(new GLColor(1.0f, 1.0f, 1.0f));
		nextBtnView.setBackground(new GLColor(0.43f, 0.4f, 0.34f));
		nextBtnView.setAlignment(GLTextView.ALIGN_CENTER);

		nextBtnView.setText(">");
		nextBtnView.setTextSize(80);
		nextBtnView.setFocusListener(new GLViewFocusListener() {
			@Override
			public void onFocusChange(GLRectView view, boolean focused) {
				if (focused) {
					view.setAlpha(0.3f);
//					++mCurIndex;
//					setStartIndex((mCurIndex -1) * getNumOneScreen());
//					requestLayout();
//					showPage();
				} else {
					view.setAlpha(1.0f);
				}
			}
		});
		addView(nextBtnView);
	}

	private GLTextView prvBtnView = new GLTextView(this.getContext());
	private GLTextView nextBtnView = new GLTextView(this.getContext());
	private float mStart = 0.0f;
	private float mStep = 120.0f;
	private int mCurIndex = 1; //当前分页的位置,从1开始计数
	private int mCurFocuseIndex = 0;
	private int mCount = 0;  // 分页的个数, 从1开始计数
	private int mShowMaxCount = 0;
	private boolean mbIndexFocused = false;

	private final static int MAXSHOW = 5;
	private float mBtnSpace = 20; // 底部按钮也GridView之间的距离

	//创建分页
	public void showPage(){

		mCount = getTotalNum() / getNumOneScreen();
		if( getTotalNum() % getNumOneScreen() != 0 )
			++mCount;

//		GLRectView rectView = new GLRectView( this.getContext() );
//		rectView.setLayoutParams( getX(), getY() + getHeight() - 140 , getWidth(), 140 );
//		rectView.setBackground(new GLColor( 1.0f, 1.0f, 0.0f));
//		addView( rectView );

		float mid = getX() + getWidth() / 2;

		mShowMaxCount = mCount > MAXSHOW ? MAXSHOW : mCount;
		if( mCount > 1 ){
			mStart = mid - (mStep * mShowMaxCount) / 2;
			if (mCurIndex < mCount) {
				nextBtn();
				if (mCurIndex > 1) {
					prvBtn();
				}
			} else if (mCurIndex == mCount) {
				prvBtn();
			}

		}
		else{
			mStart = mid;
		}

		int istart = 1;
		int iend = mCount;
		if( mCount > MAXSHOW ) {
			if( mCurIndex <= MAXSHOW / 2 ){
				istart = 1;
			} else{
				istart = (mCurIndex + MAXSHOW/2) < mCount ? mCurIndex - MAXSHOW / 2 : (mCount + 1 - MAXSHOW);
			}
			iend = istart + MAXSHOW - 1;

		}

		for( int i = istart; i <= iend; ++i ) {

			GLTextView textView = new GLTextView(this.getContext());
			textView.setX(mStart + (i - istart) * mStep);
			textView.setY(getY() + getHeight() + mBtnSpace);
			textView.setLayoutParams(100, 100);
			textView.setTextColor(new GLColor(1.0f, 1.0f, 1.0f));
			if( mCurIndex == i ){
				textView.setBackground( new GLColor(0.21f, 0.45f, 0.68f ));
			}else {
				textView.setBackground(new GLColor(0.43f, 0.4f, 0.34f));
			}
			//textView.setAlignment( GLTextView.ALIGN_CENTER );
			textView.setPadding(50, -20, 0, 0 );

			textView.setText("" + i);
			textView.setTextSize(80);
			final int index = i;
			textView.setFocusListener(new GLViewFocusListener() {
				@Override
				public void onFocusChange(GLRectView view, boolean focused) {
					if (focused){
						mbIndexFocused = true;
						view.setAlpha(0.3f);
						mCurFocuseIndex = index;

					}
					else{
						mbIndexFocused = false;
						view.setAlpha(1.0f);
					}
				}
			});
			addView(textView);
		}
		return;
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

	public void setOrientation(GLConstant.GLOrientation orientation) {
		super.setOrientation( orientation );
	}

	/**
	 * 获取布局方向
	 * @author linzanxian  @Date 2015年3月10日 下午5:36:47
	 * @return String
	 */
	public GLConstant.GLOrientation getOrientation() {
		return super.getOrientation();
	}

}
