package com.yalemang.flowlayout.library;

class Item {
    private FlowViewHolder flowViewHolder;
    private int itemType;
    private int line;
    private int column;
    private int x = 0;
    private int y = 0;
    private int position;
    private int width;
    private int height;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

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

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
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
                "flowViewHolder=" + flowViewHolder +
                ", itemType=" + itemType +
                ", line=" + line +
                ", column=" + column +
                ", x=" + x +
                ", y=" + y +
                ", position=" + position +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
