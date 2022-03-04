package com.yalemang.flowlayout.library;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

/**
 * 1.支持XML配置
 * 2.支持适配器配置
 * 3.支持滑动
 */
public class FlowLayout extends ViewGroup {

    private int line = 0;
    private HashMap<Integer, Integer> lineColumn;
    private HashMap<Integer, Integer> lineMaxHeight;
    private int startX = 0;
    private int startY = 0;
    private int moveY = 0;
    private int maxMoveHeight = 0;

    private FlowAdapter adapter;

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

    public void setAdapter(FlowAdapter adapter) {
        this.adapter = adapter;
        //移除所有子控件
        removeAllViews();
        for (int i = 0; i < this.adapter.size(); i++) {
            int itemType = this.adapter.itemType(i);
            FlowViewHolder flowViewHolder = this.adapter.createViewHolder(itemType,i,this);
            this.adapter.bindViewHolder(flowViewHolder, i, itemType);
            addView(flowViewHolder.getItemView());
        }
        requestLayout();
    }

    public FlowAdapter getAdapter() {
        return adapter;
    }

    /**
     * 根据父亲的Measure测量自身的MeasureSpec:宽
     *
     * @param parentWidthMeasureSpec
     * @return
     */
    private int getWidthMeasureSpecFromParent(int parentWidthMeasureSpec) {
        int parentWidthSize = MeasureSpec.getSize(parentWidthMeasureSpec);
        int parentWidthMode = MeasureSpec.getMode(parentWidthMeasureSpec);

        int xmlWidth = getLayoutParams().width;
        int measureWidth = 0;

        if (parentWidthMode == MeasureSpec.EXACTLY) {
            if (xmlWidth == LayoutParams.MATCH_PARENT) {
                measureWidth = parentWidthSize;
            } else if (xmlWidth == LayoutParams.WRAP_CONTENT) {
                //这里的WRAP_CONTENT要先计算子类的宽度才能计算出流式布局的宽度
                //但是流式布局的子控件的排列需要流式布局的宽度而定，因此这里处理为LayoutParams.MATCH_PARENT
                measureWidth = parentWidthSize;
            } else {
                //具体的值
                if (xmlWidth <= parentWidthSize) {
                    //没有超过父亲宽度
                    measureWidth = xmlWidth;
                } else {
                    //超过了父亲的宽度
                    measureWidth = parentWidthSize;
                }
            }
        } else if (parentWidthMode == MeasureSpec.AT_MOST) {
            if (xmlWidth == LayoutParams.MATCH_PARENT) {
                measureWidth = parentWidthSize;
            } else if (xmlWidth == LayoutParams.WRAP_CONTENT) {
                //这里的WRAP_CONTENT要先计算子类的宽度才能计算出流式布局的宽度
                //但是流式布局的子控件的排列需要流式布局的宽度而定，因此这里处理为LayoutParams.MATCH_PARENT
                measureWidth = parentWidthSize;
            } else {
                //具体的值
                if (xmlWidth <= parentWidthSize) {
                    //没有超过父亲宽度
                    measureWidth = xmlWidth;
                } else {
                    //超过了父亲的宽度
                    measureWidth = parentWidthSize;
                }
            }
        } else {
            //MeasureSpec.UNSPECIFIED
            if (xmlWidth == LayoutParams.MATCH_PARENT) {
                measureWidth = parentWidthSize;
            } else if (xmlWidth == LayoutParams.WRAP_CONTENT) {
                measureWidth = parentWidthSize;
            } else {
                measureWidth = xmlWidth;
            }
        }
        int measureSpec = MeasureSpec.makeMeasureSpec(measureWidth, MeasureSpec.EXACTLY);
        return measureSpec;
    }

    /**
     * 根据父亲的Measure测量自身的MeasureSpec: 高
     *
     * @param parentHeightMeasureSpec
     * @return
     */
    private int getHeightMeasureSpecFromParent(int parentHeightMeasureSpec) {
        int parentHeightSize = MeasureSpec.getSize(parentHeightMeasureSpec);
        int parentHeightMode = MeasureSpec.getMode(parentHeightMeasureSpec);

        int xmlHeight = getLayoutParams().height;
        int measureHeight = 0;

        if(parentHeightMode == MeasureSpec.EXACTLY){
             if(xmlHeight == LayoutParams.MATCH_PARENT){
                 measureHeight = parentHeightSize;
             }else if(xmlHeight == LayoutParams.WRAP_CONTENT){
                 //这里需要测量子类高度，再决定自身高度
                 //但是子控件并没有进行正确的排列。所这里也处理为LayoutParams.MATCH_PARENT
                 measureHeight = parentHeightSize;
             }else {
                if(xmlHeight <= parentHeightSize){
                    //没有超过父亲高度
                    measureHeight = xmlHeight;
                }else {
                    //超过了父亲宽度
                    measureHeight = parentHeightSize;
                }
             }
        }else if(parentHeightMode == MeasureSpec.AT_MOST){
            if(xmlHeight == LayoutParams.MATCH_PARENT){
                measureHeight = parentHeightSize;
            }else if(xmlHeight == LayoutParams.WRAP_CONTENT){
                //这里需要测量子类高度，再决定自身高度
                //但是子控件并没有进行正确的排列。所这里也处理为LayoutParams.MATCH_PARENT
                measureHeight = parentHeightSize;
            }else {
                if(xmlHeight <= parentHeightSize){
                    //没有超过父亲高度
                    measureHeight = xmlHeight;
                }else {
                    //超过了父亲宽度
                    measureHeight = parentHeightSize;
                }
            }
        }else {
            //MeasureSpec.UNSPECIFIED
            if(xmlHeight == LayoutParams.MATCH_PARENT){
                 measureHeight = parentHeightSize;
            }else if(xmlHeight == LayoutParams.WRAP_CONTENT){
                 measureHeight = parentHeightSize;
            }else {
                 measureHeight = xmlHeight;
            }
        }
        int measureSpec = MeasureSpec.makeMeasureSpec(measureHeight, MeasureSpec.EXACTLY);
        return measureSpec;
    }

    /**
     * 测量过程需要重新规划
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //先根据父亲提供的MeasureSpec测量自身的宽高MeasureSpec
        int flowWidthMeasureSpec = getWidthMeasureSpecFromParent(widthMeasureSpec);
        int flowHeightMeasureSpec = getHeightMeasureSpecFromParent(heightMeasureSpec);
        //然后测量子类
        measureChildren(flowWidthMeasureSpec, flowHeightMeasureSpec);
        //结合xml设置的宽高和子控件需要的宽高，以及父亲给的限制计算出最终宽高
        //这里除去padding和margin
        int measureWidth = MeasureSpec.getSize(flowWidthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(flowHeightMeasureSpec);

        int currentMaxHeight = 0;
        int column = 0;
        line = 0;
        int useWidth = measureWidth;
        lineColumn = new HashMap<>();
        maxMoveHeight = 0;
        lineMaxHeight = new HashMap<>();
        //还没有做完的 未适配margin
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
                maxMoveHeight = maxMoveHeight + currentMaxHeight;
                Log.d("Ellen2018", "第" + line + "行最大高度:" + currentMaxHeight);
                line++;
                column = 0;
                useWidth = measureWidth;

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
        maxMoveHeight = maxMoveHeight + currentMaxHeight;
        maxMoveHeight = maxMoveHeight - measureHeight;
        if (maxMoveHeight <= 0) {
            maxMoveHeight = 0;
        }
        Log.d("Ellen2018", "最大可移动高度:" + maxMoveHeight);
        //设置最终宽高
        setMeasuredDimension(measureWidth, measureHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int y = t;
        for (int i = 0; i <= line; i++) {
            Log.d("Ellen2018","line = " +i);
            int currentLineColumn = lineColumn.get(i);
            Log.d("Ellen2018","当前列数:"+currentLineColumn);
            int x = l;
            if(i > 0){
                int currentLineMaxHeight = lineMaxHeight.get(i - 1);
                y = y + currentLineMaxHeight;
            }
            Log.d("Ellen2018", "当前Y起始位置:" + y);
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
                Log.d("Ellen2018", "当前X坐标:" + x);
                Log.d("Ellen2018", "当前y坐标:" + y);
                int right = x + childWith;
                int bottom = y + childHeight;
                child.layout(x, y, right, bottom);
                x = x + childWith;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    /**
     * 负责事件分发
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 负责拦截事件
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) ev.getX();
                startY = (int) ev.getY();
                Log.d("Ellen2018", "开始的坐标Y:" + startY);
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) (ev.getY() - startY);
                scrollTo(0, moveY);
                Log.d("Ellen2018", "移动距离:" + moveY);
                startY = moveY;
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    /**
     * 负责处理事件
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                int currentMoveY = (int) (startY - event.getY()) + moveY;
                if (currentMoveY >= 0) {
                    if (currentMoveY > maxMoveHeight) {
                        currentMoveY = maxMoveHeight;
                    }
                } else {
                    currentMoveY = 0;
                }
                scrollTo(0, currentMoveY);
                Log.d("Ellen2018", "移动距离:" + currentMoveY);
                break;
            case MotionEvent.ACTION_UP:
                moveY = moveY + (int) (startY - event.getY());
                if (moveY >= 0) {
                    if (moveY > maxMoveHeight) {
                        moveY = maxMoveHeight;
                    }
                } else {
                    moveY = 0;
                }
                break;
        }
        return true;
    }
}
