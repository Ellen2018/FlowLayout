package com.yalemang.flowlayout.library;

import android.view.View;

public class FlowViewHolder {

    private View itemView;

    public FlowViewHolder(View itemView){
       this.itemView = itemView;
    }

    public <T extends View> T findViewById(int id){
        if(itemView == null){
            return null;
        }else {
            return itemView.findViewById(id);
        }
    }

    public View getItemView() {
        return itemView;
    }

    public void setItemView(View itemView) {
        this.itemView = itemView;
    }
}
