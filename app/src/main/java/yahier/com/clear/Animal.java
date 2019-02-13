package yahier.com.clear;

public class Animal implements Cloneable {
    private int type;
    private int resId;
    private int position;
    //左右上下 四个节点
    private Animal nodeLeft, nodeRight, nodeTop, nodeBottom;

    public Animal(int type, int resId) {
        this.type = type;
        this.resId = resId;
    }

    public int getType() {
        return type;
    }


    public void setType(int type) {
        this.type = type;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }


    public Animal getNodeLeft() {
        return nodeLeft;
    }

    public void setNodeLeft(Animal nodeLeft) {
        this.nodeLeft = nodeLeft;
        if (nodeLeft != null)
            nodeLeft.nodeRight = this;
    }

    public Animal getNodeRight() {
        return nodeRight;
    }

    public void setNodeRight(Animal nodeRight) {
        this.nodeRight = nodeRight;
        if (nodeRight != null)
            nodeRight.nodeLeft = this;
    }

    public Animal getNodeTop() {
        return nodeTop;
    }

    public void setNodeTop(Animal nodeTop) {
        this.nodeTop = nodeTop;
        if (nodeTop != null)
            nodeTop.nodeBottom = this;
    }

    public Animal getNodeBottom() {
        return nodeBottom;
    }

    public void setNodeBottom(Animal nodeBottom) {
        this.nodeBottom = nodeBottom;
    }

    @Override
    public Animal clone() {
        Animal animal = new Animal(type, resId);
        animal.setNodeTop(nodeTop);
        animal.setNodeRight(nodeRight);
        animal.setNodeLeft(nodeLeft);
        animal.setNodeBottom(nodeBottom);
        animal.setPosition(position);
        return animal;
    }

    @Override
    public String toString() {
        Animal nodeLeft = getNodeLeft();
        Animal nodeTop = getNodeTop();
        Animal nodeRight = getNodeRight();
        Animal nodeBottom = getNodeBottom();
        StringBuffer string = new StringBuffer();
        string.append(" 我的节点是").append(getType());
        string.append(" 左边节点是").append(nodeLeft == null ? "null" : nodeLeft.getType());
        string.append(" 上边节点是").append(nodeTop == null ? "null" : nodeTop.getType());
        string.append(" 右边节点是").append(nodeRight == null ? "null" : nodeRight.getType());
        string.append(" 下边节点是").append(nodeBottom == null ? "null" : nodeBottom.getType());
        return string.toString();
    }
}
