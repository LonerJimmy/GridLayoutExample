package loner.library.gridlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
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
    private boolean isScroll = false;
    private int count;
    private int widthMode;
    private int width;
    private int heightMode;
    private int height;

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

        isScroll = type.getBoolean(R.styleable.GridLayoutAttrs_scroll, isScroll);

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

        init(widthMeasureSpec, heightMeasureSpec);

        int childWidth = Math.max(0, (width - paddingLeft - paddingRight - mHorizontalSpacing * (mNumColumns - 1)) / mNumColumns);
        int childHeight = Math.max(0, (height - paddingTop - paddingBottom - mVerticalSpacing * (mNumRows - 1)) / mNumRows);
        int parentWidthSpec = MeasureSpec.makeMeasureSpec(childWidth, widthMode);
        int parentHeightSpec = MeasureSpec.makeMeasureSpec(childHeight, heightMode);
        int maxChildWidth = 0;
        int maxChildHeight = 0;
        int maxWidth = 0;
        int maxHeight = 0;

        for (int i = 0; i < count; ++i) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final LayoutParams lp = child.getLayoutParams();
                final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthSpec, 0, lp.width);
                final int childHeightMeasureSpec = getChildMeasureSpec(parentHeightSpec, 0, lp.height);
                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
                maxChildWidth = Math.max(maxChildWidth, child.getMeasuredWidth());
                maxChildHeight = Math.max(maxChildHeight, child.getMeasuredHeight());
            }
        }

        if (widthMode != MeasureSpec.EXACTLY || heightMode != MeasureSpec.EXACTLY) {
            //maxChildWidth = Math.min()
            parentWidthSpec = MeasureSpec.makeMeasureSpec(maxChildWidth, widthMode);
            parentHeightSpec = MeasureSpec.makeMeasureSpec(maxChildHeight, heightMode);
            for (int i = 0; i < count; ++i) {
                final View child = getChildAt(i);
                if (child.getVisibility() != GONE) {
                    final LayoutParams lp = child.getLayoutParams();
                    final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthSpec, 0, lp.width);
                    final int childHeightMeasureSpec = getChildMeasureSpec(parentHeightSpec, 0, lp.height);
                    child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
                    maxChildWidth = Math.max(maxChildWidth, child.getMeasuredWidth());
                    maxChildHeight = Math.max(maxChildHeight, child.getMeasuredHeight());
                }
            }
        }

        maxWidth = Math.max(
                maxChildWidth * mNumColumns + mHorizontalSpacing * (mNumColumns - 1) + paddingLeft + paddingRight,
                getSuggestedMinimumWidth());
        maxHeight = Math.max(
                maxChildHeight * mNumRows + mVerticalSpacing * (mNumRows - 1) + paddingTop + paddingBottom,
                getSuggestedMinimumHeight());
        int widthResult = widthMode == MeasureSpec.EXACTLY ? width : maxWidth;
        int heightResult = heightMode == MeasureSpec.EXACTLY ? height : maxHeight;

        setMeasuredDimension(widthResult, heightResult);
    }

    private void init(int widthMeasureSpec, int heightMeasureSpec) {
        count = getChildCount();
        widthMode = MeasureSpec.getMode(widthMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        heightMode = MeasureSpec.getMode(heightMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);

        //获取viewgroup的padding
        paddingLeft = getPaddingLeft();
        paddingRight = getPaddingRight();
        paddingTop = getPaddingTop();
        paddingBottom = getPaddingBottom();

        if (mNumRows == 0) {
            mNumRows = getCeil(getChildCount(), mNumColumns);
        }
        if (mNumColumns == 0) {
            mNumColumns = getCeil(getChildCount(), mNumRows);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int count = getChildCount();
        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();
        final int paddingTop = getPaddingTop();
        final int paddingBottom = getPaddingBottom();
        final int numRows = mNumRows;
        final int numColumns = mNumColumns;
        final int horizontalSpacing = mHorizontalSpacing;
        final int verticalSpacing = mVerticalSpacing;
        final int childWidth = (getMeasuredWidth() - paddingLeft - paddingRight - horizontalSpacing * (numColumns - 1)) / numColumns;
        final int childHeight = (getMeasuredHeight() - paddingTop - paddingBottom - verticalSpacing * (numRows - 1)) / numRows;
        for (int i = 0; i < count; ++i) {
            final View child = getChildAt(i);
            final int row = i / numColumns;
            final int column = i % numColumns;
            if (child.getVisibility() != View.GONE) {
                final int childLeft = paddingLeft + column * (childWidth + horizontalSpacing);
                final int childTop = paddingTop + row * (childHeight + verticalSpacing);
                final int tmpWidth = Math.max(childWidth, child.getMeasuredWidth());
                final int tmpHeight = Math.max(childHeight, child.getMeasuredHeight());
                child.layout(childLeft, childTop, childLeft + tmpWidth, childTop + tmpHeight);
            }
        }
    }
}
