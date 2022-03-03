package com.yalemang.flowlayout.library;

import android.view.ViewGroup;

public abstract class FlowAdapter<VH extends FlowViewHolder> {

    /**
     * item个数
     * @return
     */
    protected abstract int size();

    /**
     * Item Type
     * @param position
     * @return
     */
    protected int itemType(int position){
        return 0;
    }

    /**
     * 绑定ViewHolder
     * @param itemType
     * @param position
     * @param parent
     * @return
     */
    protected abstract VH createViewHolder(int itemType, int position, ViewGroup parent);


    /**
     * 显示具体数据
     * @param viewHolder
     * @param position
     */
    protected abstract void bindViewHolder(VH viewHolder,int position);

}
