package com.yalemang.flowlayout.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yalemang.flowlayout.R;
import com.yalemang.flowlayout.library.FlowAdapter;
import com.yalemang.flowlayout.library.FlowViewHolder;

public class MyFlowAdapter extends FlowAdapter<FlowViewHolder> {

    @Override
    protected int size() {
        return 200;
    }

    @Override
    protected int itemType(int position) {
        return position%2;
    }

    @Override
    protected FlowViewHolder createViewHolder(int itemType, int position, ViewGroup parent) {
        if(itemType == 0) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flow_type1, parent, false);
            return new FlowViewHolderType1(itemView);
        }else {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flow_type2, parent, false);
            return new FlowViewHolderType2(itemView);
        }
    }

    @Override
    protected void bindViewHolder(FlowViewHolder viewHolder, int position) {
          if(viewHolder instanceof FlowViewHolderType1){
              FlowViewHolderType1 flowViewHolderType1 = (FlowViewHolderType1) viewHolder;
              flowViewHolderType1.tvFlow.setText("标签:"+position);
          }
    }

    static class FlowViewHolderType1 extends FlowViewHolder {

        TextView tvFlow;

        public FlowViewHolderType1(View itemView) {
            super(itemView);
            tvFlow = findViewById(R.id.tv_flow);
        }
    }

    static class FlowViewHolderType2 extends FlowViewHolder {

        public FlowViewHolderType2(View itemView) {
            super(itemView);

        }
    }
}
