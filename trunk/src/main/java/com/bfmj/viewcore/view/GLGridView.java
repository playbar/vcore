package com.bfmj.viewcore.view;

import java.util.ArrayList;

import com.baofeng.mojing.input.base.MojingKeyCode;
import com.bfmj.viewcore.BaseViewActivity;
import com.bfmj.viewcore.adapter.GLListAdapter;
import com.bfmj.viewcore.interfaces.GLOnKeyListener;
import com.bfmj.viewcore.interfaces.GLViewFocusListener;
import android.content.Context;

public class GLGridView extends GLAdapterView<GLListAdapter> {
	
	private int mNumColumns=0; //列数
	private int mNumRows=0;//行数
	private ArrayList<GLView> mList;//数据集
	private int mStartIndex=0;//当前开始的索引
	private int mSelectedIndex=0;//当前选中的索引
	private float mVerticalSpacing=0.0f;//垂直间距离
	private float mHorizontalSpacing=0.0f;//水平间距离
	private GLListAdapter mGLListAdapter;
	private int mNumOneScreen=-1;
	private int mTotalCount = 0;
	private GLRectView convertView;
	private OnItemSelectedListener mOnItemSelectedListener;
	private OnItemClickListener mOnItemClickListener;
	private int mPrevIndex = 0;
	private GLRectView mFirstView;
	
	
	/**
	 * 设置列数
	 * @param columns 列数
	 */
	public void setNumColumns(int columns){
		this.mNumColumns = columns;
	}
	
	/**
	 * 设置行数
	 * @param rows 行数
	 */
	public void setNumRows(int rows){
		this.mNumRows = rows;
	}
	
	/**
	 * 设置垂直间距
	 * @param verticalSpacing 垂直间距
	 */
	public void setVerticalSpacing(float verticalSpacing){
		this.mVerticalSpacing = verticalSpacing;
	}
	
	/**
	 * 设置水平间矩
	 * @param horizontalSpacing 水平间矩
	 */
	public void setHorizontalSpacing(float horizontalSpacing){
		this.mHorizontalSpacing = horizontalSpacing;
	}	
	
	/**
	 * 设置一屏显示几条数据
	 * @param oneScreen
	 */
	public void setNumOneScreen(int oneScreen){
		this.mNumOneScreen = oneScreen;
	}

	/**
	 * GLGridView构造函数
	 * @param context
	 */
	public GLGridView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 设置当前第一个显示的索引
	 * @param index 开始的索引，从0开始算
	 */
	public void setStartIndex(int index){
		this.mStartIndex = index;
	}
	
	/**
	 * 获取当前的索引
	 * @return
	 */
	public int getStartIndex(){
		return this.mStartIndex;
	}	
	
	/**
	 * GLGridView构造函数
	 * @param context
	 * @param rows 行数
	 * @param columns 列数
	 */
	public GLGridView(Context context, int rows, int columns){
		super(context);
		this.mNumColumns = columns;
		this.mNumRows = rows;
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
		// TODO Auto-generated method stub
		if(mGLListAdapter != null) {
			return mGLListAdapter;
		}
		return null;
	}

	@Override
	public void setAdapter(GLListAdapter adapter) {
		// TODO Auto-generated method stub
		//清除以前的数据
		mGLListAdapter = adapter;
		this.mTotalCount = adapter.getCount();
		mOnItemSelectedListener = getOnItemSelectedListener();
		mOnItemClickListener = getOnItemClickListener();
		showItem(this.mStartIndex);
	}
	
	/**
	 * 显示list列表
	 * @param cIndex 从哪个索引开始显示
	 */
	public void showItem(int cIndex){
		if(this.mGLListAdapter==null)
			return;
		int tempIndex=cIndex;
		for(int rows=0; rows<this.mNumRows; rows++)
		{
			for(int col=0;col<this.mNumColumns;col++)
			{
				//如果大于设置的一屏显示数则不再添加
				if((tempIndex > this.mNumOneScreen && this.mNumOneScreen != -1)
						|| tempIndex > this.mTotalCount-1){
					break;
				}
				final GLRectView view = this.mGLListAdapter.getGLView(tempIndex, convertView, null);
				if (col == 0 && rows == 0) {
					mFirstView = view;
				}
				view.setX(GLGridView.this.getX() + view.getWidth() * col + this.mHorizontalSpacing * col);
				view.setY(GLGridView.this.getY() + view.getHeight() * rows + this.mVerticalSpacing * rows);
				view.setId("gridview_" + tempIndex);
				(((BaseViewActivity) getContext()).getRootView()).queueEvent(new Runnable() {
					@Override
					public void run() {
						GLGridView.this.addView(view);
					}
				});
				
				final int position = mNumColumns*rows+col;
				view.setFocusListener(new GLViewFocusListener() {
					
					@Override
					public void onFocusChange(GLRectView view, boolean focused) {
						if (mOnItemSelectedListener == null) {
							return;
						}
						if (focused) {
							mOnItemSelectedListener.onItemSelected(null, view, position, position);
						} else {
							mOnItemSelectedListener.onNothingSelected(null, view, position, position);
						}
					}
				});
				view.setOnKeyListener(new GLOnKeyListener() {
					
					@Override
					public boolean onKeyUp(GLRectView view, int keycode) {
						return false;
					}
					
					@Override
					public boolean onKeyLongPress(GLRectView view, int keycode) {
						return false;
					}
					
					@Override
					public boolean onKeyDown(GLRectView view, int keycode) {
						if (mOnItemClickListener == null) {
							return false;
						}
						
						if (keycode == MojingKeyCode.KEYCODE_ENTER) {
							mOnItemClickListener.onItemClick(null, view, position, position);
						}
						mPrevIndex = position;
						return false;
					}
				});
				//this.addView(view);
				tempIndex++;
			}
		}
	}

	@Override
	public GLView getSelectedGLView() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 返回索引为index的项
	 * @param index
	 * @return
	 */
	public GLRectView getChatAtChild(int index){
		if(this.mGLListAdapter!=null){
//			return this.mGLListAdapter.getItem(index);
		}
		return null;
	}
	
	/**
	 * 得到总个数
	 * @return
	 */
	public int getTotalNum(){
		if(this.mGLListAdapter!=null){
			return this.mGLListAdapter.getCount();
		}
		
		return 0;
	}
	
	/**
	 * 删除一个View
	 * @param index 要删除的索引
	 */
	public void removeView(int index){
		if(this.mGLListAdapter == null) return;
		//如果索引大于长度则不处理
		if(index >= this.mTotalCount) return;
		
//		super.removeView(this.mGLListAdapter.getItem(index));
		//总数量减去1
		this.mTotalCount--;
		//从数据集里移除
		this.mGLListAdapter.removeIndex(index);
		//计算行数
		this.mNumRows = (int)Math.ceil((float)this.mTotalCount/(float)this.mNumColumns);
		
		//行数
		int _rows = (int)Math.ceil((float)(index+1)/(float)this.mNumColumns)-1;
		//列数
		int _col = (index+1)%this.mNumColumns-1;
		int tempIndex = index;
		for(int rows=_rows; rows<this.mNumRows; rows++){
			if(rows > _rows) _col = 0;
			for(int col=_col;col<this.mNumColumns;col++){
				//如果大于设置的一屏显示数则不再添加
				if((tempIndex > this.mNumOneScreen && this.mNumOneScreen != -1) || tempIndex > this.mTotalCount-1){
					break;
				}
				GLRectView view = this.getChatAtChild(tempIndex);
				view.setX(view.getWidth() * col + this.mHorizontalSpacing * col);
				view.setY(view.getHeight() * rows + this.mVerticalSpacing * rows);
				tempIndex++;
			}
		}
	}
	
	/**
	 * 添加一个控件到最后
	 * @param view
	 */
	public void addChildView(GLRectView view){
		if(this.mGLListAdapter == null) return;
		
		this.mTotalCount++;
		//添加到数据集里
		this.mGLListAdapter.addIndex(this.mGLListAdapter.getCount()-1, view);
		//计算行数
		this.mNumRows = (int)Math.ceil((double)this.mTotalCount/(double)this.mNumColumns) - 1;
		
		//列数
		int _col = (this.mTotalCount-1) %this.mNumColumns;		
				
		view.setX(view.getWidth() * _col + this.mHorizontalSpacing * _col);
		view.setY(view.getHeight() * this.mNumRows + this.mVerticalSpacing * this.mNumRows);
		view.setId("gridview_" + this.mTotalCount);
		this.addView(view);
	}
	
	/**
	 * 添加一个控件到指定的索引处
	 * @param view
	 * @param index
	 */
	public void addChildView(GLRectView view, int index){
		if(this.mGLListAdapter == null) return;
		
		this.mTotalCount++;
		//添加到数据集里
		this.mGLListAdapter.addIndex(index, view);
		
		//总行数
		this.mNumRows = (int)Math.ceil((double)this.mTotalCount/(double)this.mNumColumns);
		//计算行数
		
		int _rows = (int)Math.ceil((double)index/(double)this.mNumColumns) - 1;
		
		//要插入的列数
		int _col = index % this.mNumColumns;
		
		int tempIndex = index;
		
		for(int rows=_rows; rows<this.mNumRows; rows++){
			if(rows > _rows) _col = 0;
			for(int col=_col;col<this.mNumColumns;col++){
				//如果大于设置的一屏显示数则不再添加
				if((tempIndex > this.mNumOneScreen && this.mNumOneScreen != -1) || tempIndex > this.mTotalCount-1){
					break;
				}
				
				GLRectView _view = this.getChatAtChild(tempIndex);
				_view.setX(view.getWidth() * col + this.mHorizontalSpacing * col);
				_view.setY(view.getHeight() * rows + this.mVerticalSpacing * rows);
				tempIndex++;
			}
		}
		
		this.addView(view, index);
	}

	public int getPrevIndex() {
		return mPrevIndex;
	}
	
	public void setChildFocus() {
		if (mFirstView != null) {
			mFirstView.requestFocus();
		}
	}
	
	/**
	 * 获取列数
	 * @author linzanxian  @Date 2015-7-9 下午3:09:13
	 * @return int
	 */
	public int getNumColumns() {
		return mNumColumns;
	}
}
