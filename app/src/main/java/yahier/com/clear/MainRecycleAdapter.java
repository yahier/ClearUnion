package yahier.com.clear;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yahier on 16/12/30.
 */

public class MainRecycleAdapter extends RecyclerView.Adapter<MainRecycleAdapter.ViewHolder> {

    private Context context;

    private List<Animal> list;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView img;

        public ViewHolder(View v) {
            super(v);
            img = v.findViewById(R.id.my_textview);
        }
    }


    public MainRecycleAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
    }

    public void setData() {
        list = AnimalManager.getNewLine(10);
        notifyDataSetChanged();
        checkToRemoveWithNode();
        click1Position = click2Position = 0;
        clicked1 = clicked2 = false;
    }

    public void setData(List<Animal> list) {
        notifyDataSetChanged();
        checkToRemoveWithNode();
        click1Position = click2Position = 0;
        clicked1 = clicked2 = false;
    }

    /**
     * 校验是否删除 用链表的方式
     */
    private void checkToRemoveWithNode() {
        //倒序 从下面删起
        //List<Integer> removes = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Animal temp = list.get(i);

            int leftSize = getSizeOfLinkedSameType(temp, 1);
            int rightSize = getSizeOfLinkedSameType(temp, 3);
            int topSize = getSizeOfLinkedSameType(temp, 2);
            int bottomSize = getSizeOfLinkedSameType(temp, 4);


            int horizonLinkedSize = leftSize + 1 + rightSize;
            int verticalSize = topSize + 1 + bottomSize;
            //横向消除
            if (horizonLinkedSize >= 3 || verticalSize >= 3) {
                //爆炸
                //removes.add(i);
                temp.setToRemove(true);
            } else {
                int movePosition = getSizeOfRemovedUp(temp);
                temp.setMovePosition(movePosition);
            }
        }

        onRemoveListener.onReadyToMove(list);

    }


    /**
     * 获取下面被移除的数目
     */
    private int getSizeOfRemovedUp(Animal animal) {
        int size = 0;
        if (animal == null) {
            return 0;
        }


        Animal temp = animal.getNodeBottom();
        while (temp != null) {
            //获取此元素向下的连接数目
            int bottomVerticalSize = getSizeOfLinkedSameType(temp, 4);

            if (bottomVerticalSize + 1 >= 3) {
                size = size + bottomVerticalSize + 1;
                for (int i = 0; i < bottomVerticalSize; i++) {
                    temp = temp.getNodeBottom();
                }
            } else
                temp = temp.getNodeBottom();
        }

        //还要去掉交叉路口的点
        Animal temp2 = animal.getNodeBottom();
        while (temp2 != null) {
            //横向消除的计算
            int upVerticalSize = getSizeOfLinkedSameType(temp2, 2);
            int bottomVerticalSize = getSizeOfLinkedSameType(temp2, 4);
            if (upVerticalSize + bottomVerticalSize + 1 >= 3) {
                //去掉交叉
            } else {
                Animal left = temp2.getNodeLeft();
                Animal right = temp2.getNodeRight();
                int leftSize = 0, rightSize = 0;
                if (left != null)
                    leftSize = getSizeOfLinkedSameType(temp2, 1);
                if (right != null)
                    rightSize = getSizeOfLinkedSameType(temp2, 3);

                if ((leftSize + 1 + rightSize) >= 3) {
                    size = size + 1;
                }
            }
            temp2 = temp2.getNodeBottom();
        }


        return size;

    }


    /**
     * 返回当前结点 四个方向 连接相同类别结点的数目
     *
     * @param node
     * @param orentation 左上右下 1234
     * @return
     */
    private int getSizeOfLinkedSameType(Animal node, int orentation) {
        int size = 0;
        switch (orentation) {
            case 1:
                Animal left = node.getNodeLeft();
                while (left != null && left.getType() == node.getType()) {
                    left = left.getNodeLeft();
                    size++;
                }
                break;
            case 2:
                Animal top = node.getNodeTop();
                while (top != null && top.getType() == node.getType()) {
                    top = top.getNodeTop();
                    size++;
                }
                break;
            case 3:
                Animal right = node.getNodeRight();
                while (right != null && right.getType() == node.getType()) {
                    right = right.getNodeRight();
                    size++;
                }
                break;
            case 4:
                Animal bottom = node.getNodeBottom();
                while (bottom != null && bottom.getType() == node.getType()) {
                    bottom = bottom.getNodeBottom();
                    size++;
                }
                break;
        }


        return size;
    }


    /**
     * 校验是否交换
     * todo 1.只有靠近的才可以切换   2.需要增加动画
     */
    private void checkSwap() {
        Animal animal1 = list.get(click1Position);
        Animal animal2 = list.get(click2Position);
        list.set(click1Position, animal2);
        list.set(click2Position, animal1);
        notifyDataSetChanged();
    }


    @Override
    public MainRecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    private int click1Position, click2Position;
    private boolean clicked1 = false, clicked2 = false;

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Animal animal = list.get(position);
        holder.img.setImageResource(animal.getResId());


        holder.img.setOnClickListener(v -> {
            //todo 点击了第一个图时，应该给此图做一些调整，让用户知道 此图已经被选择了。
            if (!clicked1) {
                clicked1 = true;
                clicked2 = false;
                click1Position = position;
            } else if (!clicked2) {
                clicked2 = true;
                click2Position = position;
            }

            if (clicked1 && clicked2) {
                clicked1 = false;
                clicked2 = false;
                checkSwap();

            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    OnRemoveListener onRemoveListener;

    public void setOnRemoveListener(OnRemoveListener onRemoveListener) {
        this.onRemoveListener = onRemoveListener;
    }

    public interface OnRemoveListener {
        void onReadyToMove(List<Animal> animals);
    }

}


