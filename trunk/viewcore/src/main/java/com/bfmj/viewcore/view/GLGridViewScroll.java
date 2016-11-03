package com.bfmj.viewcore.view;

import android.content.Context;

import com.baofeng.mojing.input.base.MojingKeyCode;
import com.bfmj.distortion.Logger;
import com.bfmj.viewcore.adapter.GLListAdapter;
import com.bfmj.viewcore.interfaces.GLViewFocusListener;
import com.bfmj.viewcore.render.GLColor;
import com.bfmj.viewcore.render.GLConstant;

public class GLGridViewScroll extends GLGridView {

	/**
	 * GLGridView构造函数
	 * @param context
	 */
	public GLGridViewScroll(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/**
	 * GLGridView构造函数
	 * @param context
	 * @param rows 行数
	 * @param columns 列数
	 */
	public GLGridViewScroll(Context context, int rows, int columns){
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

	public void setTotalCount(int count){
		mTotalCount = count;
	}

	@Override
	public void setAdapter(GLListAdapter adapter) {
		if( adapter != null && mTotalCount < adapter.getCount() ){
			mTotalCount = adapter.getCount();
		}
		super.setAdapter( adapter );
	}

	private boolean mbadd = false;
	@Override
	public void requestLayout(){
		if(  mTotalCount < getTotalNum() ){
			mTotalCount = getTotalNum();
		}

		mCount = getTotalNum() / getNumOneScreen();
		if( getTotalNum() % getNumOneScreen() != 0 )
			++mCount;

		if( mCurIndex > mCount && mCount > 0 ){
			mCurIndex = mCount;
			setStartIndex((mCurIndex - 1) * getNumOneScreen());
		}

		super.requestLayout();
		if( !mbadd ) {
			mbadd = true;
			showPage();
		}

	}

	public void nextPage(){

		if( mCurIndex < mCount ){
			processView.setProcessAnimation( mCount );
			++mCurIndex;
			setStartIndex((mCurIndex - 1) * getNumOneScreen());
			pageChange();
		}
	}

	public void previousPage(){
		if( mCurIndex > 1 ){
			processView.setProcessAnimation( -mCount );
			--mCurIndex;
			setStartIndex((mCurIndex - 1) * getNumOneScreen());
			pageChange();

		}
	}

	public void pageChange(){
		if(  mTotalCount < getTotalNum() ){
			mTotalCount = getTotalNum();
		}

		mCount = getTotalNum() / getNumOneScreen();
		if( getTotalNum() % getNumOneScreen() != 0 )
			++mCount;

		if( mCurIndex > mCount && mCount > 0 ){
			mCurIndex = mCount;
			setStartIndex((mCurIndex - 1) * getNumOneScreen());
		}

		super.pageChange();
	}

	public void resetPage(){
		mTotalCount = 0;
		mStartIndex = 0;
		mCurIndex = 1;
		mCount = 0;
	}

	public boolean isLastPage(){
		return (mCurIndex == mCount);
	}

	public boolean isFirstPage(){
		return ( mCurIndex == 1 );
	}

	@Override
	public boolean onKeyDown(int keycode){
		return super.onKeyDown(keycode);
	}

	public interface PageChangeListener{
		void onPageChange();
	}

	PageChangeListener mPrvPageChange = null;
	PageChangeListener mNextPageChange = null;

	public void setPrvPageChange(PageChangeListener pagechange){
		mPrvPageChange = pagechange;
	}

	public void setNextPageChange(PageChangeListener pagechanger){
		mNextPageChange = pagechanger;
	}

	@Override
	public boolean onKeyUp(int keycode){
		if( nextBtnImgView == null || prvBtnImgView == null ){
			return false;
		}
		if( nextBtnImgView.isFocused() || prvBtnImgView.isFocused() ) {
			if (keycode == MojingKeyCode.KEYCODE_ENTER) {
//			if (keycode == 96) {
				getRootView().queueEvent(new Runnable() {
					@Override
					public void run() {
						if (nextBtnImgView.isFocused()) {
							if( mNextPageChange != null ){
								mNextPageChange.onPageChange();
							}
//							++mCurIndex;
//							setStartIndex((mCurIndex - 1) * getNumOneScreen());
//							requestLayout();
						} else if (prvBtnImgView.isFocused()) {
							if( mPrvPageChange != null ){
								mPrvPageChange.onPageChange();
							}
//							--mCurIndex;
//							setStartIndex((mCurIndex - 1) * getNumOneScreen());
//							requestLayout();
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


	private void showPrvBtn(){
		prvBtnImgView.setX(getX() - 80 );
		prvBtnImgView.setY(getY() + getHeight() + mBtnSpace - 10);
		prvBtnImgView.setLayoutParams(60, 60);
		prvBtnImgView.setImage(mFlipLeftID );
		prvBtnImgView.setBackground( mDefaultColor);

		prvBtnImgView.setFocusListener(new GLViewFocusListener() {
			@Override
			public void onFocusChange(GLRectView view, boolean focused) {
				if (focused) {
					prvBtnImgView.setBackground( mOnFouseColor );
				} else {
					prvBtnImgView.setBackground(mDefaultColor );
				}
			}
		});
		addView(prvBtnImgView);
		return;
	}

	private void showNextBtn(){

		nextBtnImgView.setX(getX() + getWidth() + 20);
		nextBtnImgView.setY(getY() + getHeight() + mBtnSpace -10);
		nextBtnImgView.setLayoutParams(60, 60);
		nextBtnImgView.setImage(mFlipRightID);
		nextBtnImgView.setBackground( mDefaultColor );

		nextBtnImgView.setFocusListener(new GLViewFocusListener() {
			@Override
			public void onFocusChange(GLRectView view, boolean focused) {
				if (focused) {
					nextBtnImgView.setBackground( mOnFouseColor );
				} else {
					nextBtnImgView.setBackground( mDefaultColor );
				}
			}
		});
		addView(nextBtnImgView);
	}

	private GLImageView prvBtnImgView = null;//new GLImageView( this.getContext() );
	private GLImageView nextBtnImgView = null;// new GLImageView(this.getContext());
	private GLSeekBarView processView = null;
	private float mOffsetX = 0.0f;
	private int mCurIndex = 1; //当前分页的位置,从1开始计数
	private int mCount = 0;  // 分页的个数, 从1开始计数
	private int mTotalCount = 0; // 最大的内容个数

	private float mBtnSpace = -20; // 底部按钮也GridView之间的距离

	private boolean mbSeekBarVisible = true;
	private GLColor mDefaultColor = new GLColor(0.43f, 0.4f, 0.34f); // 默认页码选中状态
	private GLColor mSelectedColor = new GLColor(0.21f, 0.45f, 0.68f );
	private GLColor mOnFouseColor = new GLColor( 0.33f, 0.3f, 0.23f );
	private int mFlipLeftID = 0;  //R.drawable.flip_leftarrow;
	private int mFlipRightID = 0; //R.drawable.flip_rightarrow;

	//是否显示页码
	public void setSeekBarVisible(boolean b ){
		mbSeekBarVisible = b;
	}

	//设置页码默认颜色
	public void setNumDefaultColor( GLColor color ){
		mDefaultColor = color;
	}

	//设置页码选中状态颜色
	public void setNumSelectedColor( GLColor  color ){
		mSelectedColor = color;
	}

	//设置焦点在页码上颜色位置
	public void setNumOnFouseColor( GLColor color ){
		mOnFouseColor = color;
	}

	//设置向左箭头资源
	public void setFlipLeftIcon(int id ){
		mFlipLeftID = id;
	}

	//设置向右选择箭头资源
	public void setFlipRightIcon(int id ){
		mFlipRightID = id;
	}

	//获取当前分页位置,从1开始
	public int getCurIndexPage() {
		return mCurIndex;
	}

	public void setOffsetX(float offsetx ){
		this.mOffsetX = offsetx;
	}


	private int mBackgroundResId = 0;
	private int mResImg = 0;
	public void setProcessBackground(int resId){
		mBackgroundResId = resId;
	}

	public void setBarImage(int resId)
	{
		mResImg = resId;
	}
	//创建分页

	public void showPage(){

		if ( !mbSeekBarVisible){
			return;
		}
		nextBtnImgView = new GLImageView(this.getContext());
		prvBtnImgView = new GLImageView( this.getContext() );

		mCount = getTotalNum() / getNumOneScreen();
		if( getTotalNum() % getNumOneScreen() != 0 )
			++mCount;

		int totalPageCount = mTotalCount / getNumOneScreen();
		if( mTotalCount % getNumOneScreen() != 0 )
			++totalPageCount;


		int iw = (int)(getWidth() / mCount);

		processView = new GLSeekBarView(this.getContext());
		processView.setBackground(mBackgroundResId);
//		processView.setProcessColor(R.drawable.playbar_progressbar);
		processView.setBarWidth(iw);
		processView.setBarImage(mResImg);
		processView.setLayoutParams(getWidth(),20);
		processView.setX(getX());
		processView.setY(getY() + getHeight() + mBtnSpace);
		addView(processView);
//		if( mCount > 0) {
//			int process = ((mCurIndex - 1) * 100 / mCount);
//			processView.setProcess(process);
//		}

		showNextBtn();
		showPrvBtn();

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
