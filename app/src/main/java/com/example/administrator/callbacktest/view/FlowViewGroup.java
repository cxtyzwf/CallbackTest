package com.example.administrator.callbacktest.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FlowViewGroup extends ViewGroup {


    public FlowViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs)
    {
        return new MarginLayoutParams(getContext(), attrs);
    }

    //根据childView的高宽，计算布局的宽高，并进行设置
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取布局的父容器为它设置的测量模式和大小
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        //最后计算的布局宽高
        int width = 0, height = 0;
        //每一行的宽高
        int lineWidth = 0, lineHeight = 0;
        //循环childView
        for (int i = 0, count = getChildCount(); i < count; i++) {
            View view = getChildAt(i);
            //计算当前childView的高和宽
            measureChild(view, widthMeasureSpec, heightMeasureSpec);
            //得到childView的LayoutParams
            MarginLayoutParams params = (MarginLayoutParams) view.getLayoutParams();
            //得到childView所占的宽度和高度
            int childWidth = view.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            if (childWidth > widthSize) {
                params.width = widthSize - params.leftMargin - params.rightMargin;
                view.setLayoutParams(params);
                measureChild(view, widthMeasureSpec, heightMeasureSpec);
            }
            params = (MarginLayoutParams) view.getLayoutParams();
            childWidth = view.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            int childHeight = view.getMeasuredHeight() + params.topMargin + params.bottomMargin;

            //如果加入当前childView的宽度会超出父容器计算的宽度话，则需要开启新的一行，累加height
            //否则累加当前行的宽高
            if (lineWidth + childWidth > widthSize) {
                //取宽度最大值
                width = Math.max(lineWidth, childWidth);
                //累加height
                height += lineHeight;
                //记录下一行的宽高
                lineWidth = childWidth;
                lineHeight = childHeight;
            } else {
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            }

            //如果是最后一个，则将当前行的宽度与width做比较，累加height
            if (i == count - 1) {
                width = Math.max(lineWidth, width);
                height += lineHeight;
            }
        }

        //设置布局的宽高
        setMeasuredDimension((widthMode == MeasureSpec.EXACTLY ? widthSize : width),
                (heightMode == MeasureSpec.EXACTLY ? heightSize : height));

    }


    /**
     * 存储所有的View，按行记录
     */
    private List<List<View>> mAllViews = new ArrayList<List<View>>();
    /**
     * 记录每一行的最大高度
     */
    private List<Integer> mLineHeight = new ArrayList<Integer>();

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int widht = getWidth();
        int height = getHeight();


        //记录的高度
        int recordHeight = 0;
        //每行的宽高
        int lineWidth = 0, lineHeight = 0;
        //每个childView的坐标
        int left = 0, top = 0, right = 0, bottom = 0;
        //循环childView
        for (int i = 0, count = getChildCount(); i < count; i++) {
            View view = getChildAt(i);
            //得到childView的LayoutParams
            MarginLayoutParams params = (MarginLayoutParams) view.getLayoutParams();
            //得到childView宽高
            int childWidth = view.getMeasuredWidth();
            int childHeight = view.getMeasuredHeight();
            if (childWidth + params.leftMargin + params.rightMargin + lineWidth > widht) {
                recordHeight = recordHeight + lineHeight;
                left = params.leftMargin;
                right = left + childWidth;
                top = recordHeight + params.topMargin;
                bottom = top + childHeight;
                lineWidth = childWidth + params.leftMargin + params.rightMargin;
                lineHeight = childHeight + params.topMargin + params.bottomMargin;
            } else {
                left = lineWidth + params.leftMargin;
                right = left + childWidth;
                top = recordHeight + params.topMargin;
                bottom = top + childHeight;
                lineWidth = lineWidth + childWidth + params.leftMargin + params.rightMargin;
                lineHeight = Math.max(lineHeight, childHeight + params.topMargin + params.bottomMargin);
            }


            view.layout(left, top, right, bottom);
        }

    }
}
