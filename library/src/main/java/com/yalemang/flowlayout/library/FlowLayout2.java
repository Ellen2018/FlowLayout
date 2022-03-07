package com.yalemang.flowlayout.library;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
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
        int measureSpec = MeasureSpec.makeMeasureSpec(measureWidth, MeasureSpec.UNSPECIFIED);
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
        int measureSpec = MeasureSpec.makeMeasureSpec(measureHeight, MeasureSpec.UNSPECIFIED);
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
        boolean isOverHeight = false;
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
            if (measureWidth - useX < 0) {
                if (endIndex < 0) {
                    endIndex = i;
                }
                List<Item> itemList = new ArrayList<>();
                for (int index = startIndex; index < endIndex; index++) {
                    itemHashMap.get(index).setY(userHeight);
                    itemHashMap.get(index).setLine(line);
                    itemList.add(itemHashMap.get(index));
                }
                userHeight = userHeight + currentMaxHeight;
                lineHashMap.put(line, itemList);
                //设置每排item的y坐标
                if (userHeight > measureHeight) {
                    isOverHeight = true;
                    break;
                } else {
                    line++;
                    useX = 0;
                    column = 0;
                    endIndex = -1;

                    //添加超过的第一个
                    currentMaxHeight = childHeight;
                    Item itm = new Item();
                    itm.setFlowViewHolder(flowViewHolder);
                    itm.setColumn(column);
                    itm.setX(useX);
                    itm.setLine(line);
                    startIndex = i;
                    useX = useX + childWidth;
                    column++;
                    itm.setPosition(i);
                    itemHashMap.put(i, itm);
                }
            } else {
                Item item = new Item();
                item.setFlowViewHolder(flowViewHolder);
                item.setColumn(column);
                item.setX(useX);
                //继续排列
                if (currentMaxHeight < childHeight) {
                    currentMaxHeight = childHeight;
                }
                useX = useX + childWidth;
                column++;
                item.setPosition(i);
                itemHashMap.put(i, item);
            }
            adapter.bindViewHolder(flowViewHolder, i, itemType);
        }
        if (isOverHeight) {
            //这里多加一行,提供复用机制的保障就是这里的多加载一行
            int loadStartIndex = endIndex;
            useX = 0;
            column = 0;
            line++;
            for (int i = loadStartIndex; i < adapter.size(); i++) {
                int itemType = adapter.itemType(i);
                FlowViewHolder flowViewHolder = adapter.createViewHolder(itemType, i, this);
                View childView = flowViewHolder.getItemView();
                //添加子View
                addView(childView, i);
                //测量子View
                measureChild(childView, flowWidthMeasureSpec, flowHeightMeasureSpec);
                int childWidth = childView.getMeasuredWidth();
                int childHeight = childView.getMeasuredHeight();
                Log.d("Ellen2018","测量的子类:"+i);
                Log.d("Ellen2018","测量的子类宽度:"+childWidth);
                if (measureWidth - useX < 0) {
                    List<Item> itemList = new ArrayList<>();
                    for (int index = loadStartIndex; index < i; index++) {
                        itemHashMap.get(index).setY(userHeight);
                        itemHashMap.get(index).setLine(line);
                        itemList.add(itemHashMap.get(index));
                    }
                    lineHashMap.put(line, itemList);
                    break;
                } else {
                    Item item = new Item();
                    item.setFlowViewHolder(flowViewHolder);
                    item.setColumn(column);
                    item.setX(useX);
                    //继续排列
                    if (currentMaxHeight < childHeight) {
                        currentMaxHeight = childHeight;
                    }
                    useX = useX + childWidth;
                    column++;
                    item.setPosition(i);
                    itemHashMap.put(i, item);
                }
                adapter.bindViewHolder(flowViewHolder, i, itemType);
            }
        } else {
            //说明高度超过了需要的高度
            endIndex = adapter.size();
            List<Item> itemList = new ArrayList<>();
            for (int index = startIndex; index < endIndex; index++) {
                itemHashMap.get(index).setY(userHeight);
                itemHashMap.get(index).setLine(line);
                itemList.add(itemHashMap.get(index));
            }
            lineHashMap.put(line, itemList);
        }


        //设置最终宽高
        setMeasuredDimension(measureWidth, measureHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int lin = 0; lin <= line; lin++) {
            List<Item> itemList = lineHashMap.get(lin);
            Log.d("Ellen2018", "第" + lin + "行");
            for (Item item : itemList) {
                Log.d("Ellen2018", item.toString());
                FlowViewHolder flowViewHolder = item.getFlowViewHolder();
                View childView = flowViewHolder.getItemView();
                int childWidth = childView.getMeasuredWidth();
                int childHeight = childView.getMeasuredHeight();
                childView.layout(item.getX(), item.getY(), item.getX() + childWidth, item.getY() + childHeight);
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
                startX = (int) ev.getX();
                startY = (int) ev.getY();
                isIntercept = false;
                break;
            case MotionEvent.ACTION_MOVE:
                //这里move事件拦截的前提是移动超过了系统规定最小距离
                //规定的移动最小距离
                int touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
                int moveY = (int) (ev.getY() - startY);
                if(moveY < 0){
                    moveY = -moveY;
                }
                if(moveY >= touchSlop){
                    isIntercept = true;
                }else {
                    isIntercept = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                this.moveY = currentMoveY;
                isIntercept = false;
                break;
        }
        return isIntercept;
    }

    private int currentMoveY = 0;

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
                currentMoveY = (int) (startY - event.getY()) + moveY;
                if (currentMoveY < 0) {
                    currentMoveY = 0;
                }
                //当滑动的距离已经达到最后一行开始时，就回收屏幕滑出去的
                //当新的一行出现时就从复用池里面找到可复用的进行复用
                scrollTo(0, currentMoveY);
                break;
            case MotionEvent.ACTION_UP:
                this.moveY = currentMoveY;
                break;
        }
        return true;
    }

}
