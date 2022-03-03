package com.yalemang.flowlayout.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yalemang.flowlayout.R;
import com.yalemang.flowlayout.library.FlowAdapter;
import com.yalemang.flowlayout.library.FlowViewHolder;

public class MyFlowAdapter extends FlowAdapter<MyFlowAdapter.TestFlowViewHolder> {

    @Override
    protected int size() {
        return 50;
    }

    @Override
    protected TestFlowViewHolder createViewHolder(int itemType, int position, ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flow,parent,false);
        return new TestFlowViewHolder(itemView);
    }

    @Override
    protected void bindViewHolder(TestFlowViewHolder viewHolder, int position) {
        TestFlowViewHolder testFlowViewHolder = (TestFlowViewHolder) viewHolder;
        testFlowViewHolder.tvFlow.setText("流式标签:"+position);
    }

    static class TestFlowViewHolder extends FlowViewHolder {

        TextView tvFlow;

        public TestFlowViewHolder(View itemView) {
            super(itemView);
            tvFlow = findViewById(R.id.tv_flow);
        }
    }
}
