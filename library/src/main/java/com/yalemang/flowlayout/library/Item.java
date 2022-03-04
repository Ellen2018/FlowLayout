package com.yalemang.flowlayout.library;

class Item {
    private FlowViewHolder flowViewHolder;
    private int line;
    private int column;
    private int x = 0;
    private int y = 0;
    private int position;

    public FlowViewHolder getFlowViewHolder() {
        return flowViewHolder;
    }

    public void setFlowViewHolder(FlowViewHolder flowViewHolder) {
        this.flowViewHolder = flowViewHolder;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Item{" +
                "line=" + line +
                ", column=" + column +
                ", x=" + x +
                ", y=" + y +
                ", position=" + position +
                '}';
    }
}
