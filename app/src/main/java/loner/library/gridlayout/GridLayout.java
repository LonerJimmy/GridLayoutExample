package loner.library.gridlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import example.loner.gridlayoutexample.R;

/**
 * Created by loner on 16/9/9.
 */
public class GridLayout extends ViewGroup {

    private int mHorizontalSpacing = 0;
    private int mVerticalSpacing = 0;
    private int mNumColumns = 0;
    private int mNumRows = 0;
    private int width;
    private int height;

    private boolean isColumn = false;
    private boolean isRow = false;
    private boolean isBoth = false;

    //获取viewgroup的padding
    private int paddingLeft;
    private int paddingRight;
    private int paddingTop;
    private int paddingBottom;

    public GridLayout(Context context) {
        this(context, null, 0);
    }

    public GridLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        TypedArray type = context.obtainStyledAttributes(attributeSet, R.styleable.GridLayoutAttrs);
        mHorizontalSpacing = (int) type.getDimension(R.styleable.GridLayoutAttrs_horizontalSpacing, mHorizontalSpacing);
        mVerticalSpacing = (int) type.getDimension(R.styleable.GridLayoutAttrs_verticalSpacing, mVerticalSpacing);
        mNumColumns = type.getInteger(R.styleable.GridLayoutAttrs_numColumns, mNumColumns);
        mNumRows = type.getInteger(R.styleable.GridLayoutAttrs_numRows, mNumRows);

        if (mNumColumns != 0 && mNumRows != 0) {
            isBoth = true;
        }

        if (mNumRows == 0) {
            isColumn = true;
        }
        if (mNumColumns == 0) {
            isRow = true;
        }

        type.recycle();
    }

    private int getCeil(int count, int num) {
        return count % num == 0 ? count / num : (count / num + 1);
    }

    public void addView(View child) {
        if (isFull()) {
            throw new UnsupportedOperationException("GridLayout is full");
        }
        super.addView(child);
    }

    public void addView(View child, int index, LayoutParams params) {
        if (isFull()) {
            throw new UnsupportedOperationException("GridLayout is full.");
        }
        super.addView(child, index, params);
    }

    public void addView(View child, LayoutParams params) {
        if (isFull()) {
            throw new UnsupportedOperationException("GridLayout is full.");
        }
        super.addView(child, params);
    }

    private boolean isFull() {
        if (mNumColumns == 0 || mNumRows == 0) {
            return false;
        } else {
            return getChildCount() > mNumColumns * mNumRows;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        initMeasure(widthMeasureSpec, heightMeasureSpec);

        int maxChildWidth = 0;
        int maxChildHeight = 0;
        int maxWidth = 0;
        int maxHeight = 0;

        if (isColumn || isBoth) {
            for (int i = 0; i < mNumRows; i++) {
                for (int j = 0; j < mNumColumns; j++) {
                    final View child = getChildAt(i * mNumColumns + j);
                    if (child != null) {
                        if (child.getVisibility() != GONE) {
                            final LayoutParams lp = child.getLayoutParams();
                            final int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, 0, lp.width);
                            final int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, 0, lp.height);
                            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
                            maxChildHeight = Math.max(maxChildHeight, child.getMeasuredHeight());
                        }
                    }
                }
                maxHeight += maxChildHeight;
                maxChildHeight = 0;
            }

        }

        if (isRow) {
            for (int i = 0; i < mNumRows; i++) {
                for (int j = 0; j < mNumColumns; j++) {
                    final View child = getChildAt(i * mNumColumns + j);
                    if (child != null) {
                        if (child.getVisibility() != GONE) {
                            final LayoutParams lp = child.getLayoutParams();
                            final int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, 0, lp.width);
                            final int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, 0, lp.height);
                            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
                            maxChildWidth += child.getMeasuredWidth();
                        }
                    }
                }
                maxWidth = Math.max(maxWidth, maxChildWidth);
                maxChildWidth=0;
            }

        }

        maxWidth = maxWidth + mHorizontalSpacing * (mNumColumns - 1) + paddingLeft + paddingRight;
        maxHeight = maxHeight + mVerticalSpacing * (mNumRows - 1) + paddingTop + paddingBottom;

        int widthResult = 0;
        int heightResult = 0;
        if (isColumn || isBoth) {
            widthResult = width;
            heightResult = maxHeight;
        }
        if (isRow) {
            widthResult = maxWidth;
            heightResult = height;
        }

        setMeasuredDimension(widthResult, heightResult);
    }

    private void initMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);

        //获取viewgroup的padding
        paddingLeft = getPaddingLeft();
        paddingRight = getPaddingRight();
        paddingTop = getPaddingTop();
        paddingBottom = getPaddingBottom();


        if (isColumn) {
            mNumRows = getCeil(getChildCount(), mNumColumns);
        }
        if (isRow) {
            mNumColumns = getCeil(getChildCount(), mNumRows);
        }

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int numRows = mNumRows;
        final int numColumns = mNumColumns;
        final int horizontalSpacing = mHorizontalSpacing;
        final int verticalSpacing = mVerticalSpacing;
//        final int childWidth = (getMeasuredWidth() - paddingLeft - paddingRight - horizontalSpacing * (numColumns - 1)) / numColumns;
//        final int childHeight = (getMeasuredHeight() - paddingTop - paddingBottom - verticalSpacing * (numRows - 1)) / numRows;
        int maxChildHeight = 0;
        int childWidth = 0;
        int childHeight = paddingTop;

        if (isColumn || isBoth) {
            childWidth = (getMeasuredWidth() - paddingLeft - paddingRight - horizontalSpacing * (numColumns - 1)) / numColumns;
            for (int i = 0; i < mNumRows; i++) {
                for (int j = 0; j < mNumColumns; j++) {
                    final View child = getChildAt(i * mNumColumns + j);
                    if (child != null) {
                        if (child.getVisibility() != View.GONE) {
                            final int childLeft = paddingLeft + j * (childWidth + horizontalSpacing);
                            final int tmpWidth = Math.max(childWidth, child.getMeasuredWidth());
                            child.layout(childLeft, childHeight, childLeft + tmpWidth, childHeight + child.getMeasuredHeight());
                            maxChildHeight = Math.max(maxChildHeight, child.getMeasuredHeight());
                        }
                    }
                }
                childHeight += (maxChildHeight + mVerticalSpacing);
                maxChildHeight = 0;
            }
        }

        if (isRow) {
            childWidth = paddingLeft;
            childHeight = (getMeasuredHeight() - paddingTop - paddingBottom - verticalSpacing * (numRows - 1)) / numRows;
            for (int i = 0; i < mNumRows; i++) {
                for (int j = 0; j < mNumColumns; j++) {
                    final View child = getChildAt(i * mNumColumns + j);
                    if (child != null) {
                        if (child.getVisibility() != View.GONE) {
                            if (i == 0) {
                                int p=child.getMeasuredWidth();
                                child.layout(childWidth, childHeight * i, childWidth + child.getMeasuredWidth(), childHeight * i + childHeight);
                            } else {
                                child.layout(childWidth, (childHeight + verticalSpacing) * i, childWidth + child.getMeasuredWidth(), (childHeight + verticalSpacing) * i + childHeight);
                            }
                            childWidth += (child.getMeasuredWidth() + horizontalSpacing);
                        }
                    }
                }
                childWidth = paddingLeft;
            }
        }

    }
}
