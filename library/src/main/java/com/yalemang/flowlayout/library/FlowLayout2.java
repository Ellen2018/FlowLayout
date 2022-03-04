package com.yalemang.flowlayout.library;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 1.支持XML配置
 * 2.支持适配器配置
 * 3.支持滑动
 * 4.可复用划出控件
 */
public class FlowLayout2 extends ViewGroup {

    private int line = 0;
    private HashMap<Integer, Item> itemHashMap;
    private HashMap<Integer, List<Item>> lineHashMap;
    private int startX = 0;
    private int startY = 0;
    private int moveY = 0;
    private int maxMoveHeight = 0;

    private FlowAdapter adapter;

    public FlowLayout2(Context context) {
        super(context);
        initView(null);
    }

    public FlowLayout2(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    public FlowLayout2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    public FlowLayout2(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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

    public FlowAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(FlowAdapter adapter) {
        this.adapter = adapter;
        //移除所有子控件
        removeAllViews();
        requestLayout();
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

        if (parentHeightMode == MeasureSpec.EXACTLY) {
            if (xmlHeight == LayoutParams.MATCH_PARENT) {
                measureHeight = parentHeightSize;
            } else if (xmlHeight == LayoutParams.WRAP_CONTENT) {
                //这里需要测量子类高度，再决定自身高度
                //但是子控件并没有进行正确的排列。所这里也处理为LayoutParams.MATCH_PARENT
                measureHeight = parentHeightSize;
            } else {
                if (xmlHeight <= parentHeightSize) {
                    //没有超过父亲高度
                    measureHeight = xmlHeight;
                } else {
                    //超过了父亲宽度
                    measureHeight = parentHeightSize;
                }
            }
        } else if (parentHeightMode == MeasureSpec.AT_MOST) {
            if (xmlHeight == LayoutParams.MATCH_PARENT) {
                measureHeight = parentHeightSize;
            } else if (xmlHeight == LayoutParams.WRAP_CONTENT) {
                //这里需要测量子类高度，再决定自身高度
                //但是子控件并没有进行正确的排列。所这里也处理为LayoutParams.MATCH_PARENT
                measureHeight = parentHeightSize;
            } else {
                if (xmlHeight <= parentHeightSize) {
                    //没有超过父亲高度
                    measureHeight = xmlHeight;
                } else {
                    //超过了父亲宽度
                    measureHeight = parentHeightSize;
                }
            }
        } else {
            //MeasureSpec.UNSPECIFIED
            if (xmlHeight == LayoutParams.MATCH_PARENT) {
                measureHeight = parentHeightSize;
            } else if (xmlHeight == LayoutParams.WRAP_CONTENT) {
                measureHeight = parentHeightSize;
            } else {
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
        //这里除去padding和margin
        int measureWidth = MeasureSpec.getSize(flowWidthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(flowHeightMeasureSpec);
        itemHashMap = new HashMap<>();
        lineHashMap = new HashMap<>();
        int useX = 0;
        line = 0;
        int column = 0;
        int userHeight = 0;
        int currentMaxHeight = 0;
        int startIndex = -1;
        int endIndex = -1;
        //加载第一屏数据
        for (int i = 0; i < adapter.size(); i++) {
            int itemType = adapter.itemType(i);
            FlowViewHolder flowViewHolder = adapter.createViewHolder(itemType, i, this);
            View childView = flowViewHolder.getItemView();
            //添加子View
            addView(childView, i);
            //测量子View
            measureChild(childView, flowWidthMeasureSpec, flowHeightMeasureSpec);
            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();
            if (startIndex < 0) {
                //统计每行的开始位置
                startIndex = i;
            }
            Item item = new Item();
            item.setFlowViewHolder(flowViewHolder);
            item.setColumn(column);
            item.setX(useX);
            if (measureWidth - useX < 0) {
                if (endIndex < 0) {
                    endIndex = i;
                }
                //换行
                line++;
                column = 0;
                userHeight = userHeight + currentMaxHeight;
                List<Item> itemList = new ArrayList<>();
                lineHashMap.put(line-1,itemList);
                //设置每排item的y坐标
                for (int index = startIndex; index < endIndex; index++) {
                    itemHashMap.get(index).setY(userHeight);
                    itemHashMap.get(index).setLine(line - 1);
                    itemList.add(itemHashMap.get(index));
                }
                startIndex = -1;
                endIndex = -1;
                if (userHeight > measureHeight) {
                    break;
                } else {
                    useX = 0;
                }
            } else {
                //继续排列
                if (currentMaxHeight < childHeight) {
                    currentMaxHeight = childHeight;
                }
                useX = useX + childWidth;
                column++;
            }
            adapter.bindViewHolder(flowViewHolder, i, itemType);
            item.setPosition(i);
            itemHashMap.put(i, item);
        }

        //设置最终宽高
        setMeasuredDimension(measureWidth, measureHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int lin = 0; lin < line; lin++) {
            List<Item> itemList = lineHashMap.get(lin);
            Log.d("Ellen2018","第"+lin+"行");
            for(Item item:itemList){
                Log.d("Ellen2018",item.toString());
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
        boolean isIntercept = true;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return isIntercept;
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
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

}