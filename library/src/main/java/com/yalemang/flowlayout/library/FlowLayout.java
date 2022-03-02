package com.yalemang.flowlayout.library;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;

public class FlowLayout extends ViewGroup {

    public FlowLayout(Context context) {
        super(context);
        initView(null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        if (attrs != null) {
            //解析自定义属性
            parseCustomProperties();
        }
    }

    private void parseCustomProperties() {
    }

    /**
     * 测量过程需要重新规划
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        //记录最终宽和高
        int measureWith = 0, measureHeight = 0;

        //获取父控件给流式布局的Size和Mode
        int parentWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        int parentWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int parentHeightMode = MeasureSpec.getMode(heightMeasureSpec);
        int parentHeightSize = MeasureSpec.getSize(heightMeasureSpec);

        //对应XML布局中设置的，或者代码动态设置的LayoutParams
        int xmlWidth = getLayoutParams().width;
        int xmlHeight = getLayoutParams().height;

        if (parentWidthMode == MeasureSpec.EXACTLY) {
            if (xmlWidth == LayoutParams.MATCH_PARENT) {
                measureWith = parentWidthSize;
            } else if (xmlWidth == LayoutParams.WRAP_CONTENT) {
                //这里的WARP指定为MATCH处理
                measureWith = parentWidthSize;
            } else {
                if (xmlWidth <= parentWidthSize) {
                    measureWith = xmlWidth;
                } else {
                    measureWith = parentWidthSize;
                }
            }
        } else if (parentWidthMode == MeasureSpec.AT_MOST) {
            if (xmlWidth == LayoutParams.MATCH_PARENT) {
                measureWith = parentWidthSize;
            } else if (xmlWidth == LayoutParams.WRAP_CONTENT) {
                //这里的WARP指定为MATCH处理
                measureWith = parentWidthSize;
            } else {
                if (xmlWidth <= parentWidthSize) {
                    measureWith = xmlWidth;
                } else {
                    measureWith = parentWidthSize;
                }
            }
        } else {
            measureWith = parentWidthSize;
        }

        if (parentHeightMode == MeasureSpec.EXACTLY) {
            if (xmlHeight == LayoutParams.MATCH_PARENT) {
                measureHeight = parentHeightSize;
            } else if (xmlHeight == LayoutParams.WRAP_CONTENT) {
                //这里的WARP指定为MATCH处理
                measureHeight = parentHeightSize;
            } else {
                if (xmlHeight <= parentHeightSize) {
                    measureHeight = xmlHeight;
                } else {
                    measureHeight = parentHeightSize;
                }
            }
        } else if (parentHeightMode == MeasureSpec.AT_MOST) {
            if (xmlHeight == LayoutParams.MATCH_PARENT) {
                measureHeight = parentHeightSize;
            } else if (xmlHeight == LayoutParams.WRAP_CONTENT) {
                //这里的WARP指定为MATCH处理
                measureHeight = parentHeightSize;
            } else {
                if (xmlHeight <= parentHeightSize) {
                    measureHeight = xmlHeight;
                } else {
                    measureHeight = parentHeightSize;
                }
            }
        } else {
            measureHeight = parentHeightSize;
        }
        //结合xml设置的宽高和子控件需要的宽高，以及父亲给的限制计算出最终宽高
        //这里除去padding和margin
        //设置最终宽高
        setMeasuredDimension(measureWith, measureHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int currentMaxHeight = 0;
        int line = 0;
        int column = 0;
        int useWidth = width;
        HashMap<Integer, Integer> lineColumn = new HashMap<>();
        HashMap<Integer, Integer> lineMaxHeight = new HashMap<>();
        //第一次统计子控件的排列
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            int childWith = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            useWidth = useWidth - childWith;
            Log.d("Ellen2018","剩余空间:"+useWidth);
            if (useWidth <= 0) {
                //统计改行的最大高度
                lineColumn.put(line, column);
                lineMaxHeight.put(line, currentMaxHeight);
                line++;
                column = 0;
                useWidth = width;

                //统计当前换行的
                useWidth = useWidth - childWith;
                currentMaxHeight = childHeight;
            } else {
                //没有换行,统计最大宽度 & 列信息
                if (currentMaxHeight < childHeight) {
                    currentMaxHeight = childHeight;
                }
            }
            column++;
        }
        lineColumn.put(line, column);
        lineMaxHeight.put(line, currentMaxHeight);
        int y = t;
        for (int i = 0; i <= line; i++) {
            Log.d("Ellen2018","line = " +i);
            int currentLineMaxHeight = lineMaxHeight.get(i);
            int currentLineColumn = lineColumn.get(i);
            Log.d("Ellen2018","当前列数:"+currentLineColumn);
            int x = l;
            if(i > 0){
                y = y + currentLineMaxHeight;
            }
            for (int j = 0; j < currentLineColumn; j++) {
                int index = 0;
                if (i > 0) {
                    //计算子控件索引
                    for (int z = 0; z < i; z++) {
                        index = index + lineColumn.get(z);
                    }
                    index = index + j;
                } else {
                    index = j;
                }
                View child = getChildAt(index);
                int childWith = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();
                TextView textView = (TextView) child;
                Log.d("Ellen2018", "当前控件:" + textView.getText());
                Log.d("Ellen2018", "当前X坐标:" + x);
                Log.d("Ellen2018", "当前y坐标:" + y);
                int left = x;
                int top = y;
                int right = x + childWith;
                int bottom = y + childHeight;
                child.layout(left, top, right, bottom);
                x = x + childWith;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
