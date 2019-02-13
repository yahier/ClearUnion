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
    }

    public void setData() {
        list = AnimalManager.getNewLine(8);
        notifyDataSetChanged();
        checkIfSameToClearNode();
    }

    /**
     * 校验是否可消除 方法1  在列表中过滤 生成每个类别的新列表
     */
    private void checkIfSameToClear() {
        //横向遍历列表
        List<Animal> list1 = new ArrayList<>();
        List<Animal> list2 = new ArrayList<>();
        List<Animal> list3 = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            Animal temp = list.get(i);
            temp.setPosition(i);

            switch (temp.getType()) {
                case 1:
                    list1.add(temp);
                    break;
                case 2:
                    list2.add(temp);
                    break;
                case 3:
                    list3.add(temp);
                    break;
            }
        }


    }


    /**
     * 校验是否可消除 方法2 链表方式
     */
    private void checkIfSameToClearNode() {
        for (int i = 0; i < list.size(); i++) {
            Animal temp = list.get(i);
            Animal newTemp = temp.clone();

            int leftSize = getSizeOfLinkedSameType(newTemp, 1);
            int rightSize = getSizeOfLinkedSameType(newTemp, 3);
            int topSize = getSizeOfLinkedSameType(newTemp, 2);
            int bottomSize = getSizeOfLinkedSameType(newTemp, 4);

            //横向消除
            int horizonLinkedSize = leftSize + 1 + rightSize;
            if (horizonLinkedSize >= 3) {
                int[] positions = new int[horizonLinkedSize];
                for (int a = 0; a < leftSize; a++) {
                    positions[a] = newTemp.getPosition() - leftSize + a;
                }

                positions[leftSize] = newTemp.getPosition();

                for (int a = 0; a < rightSize; a++) {
                    positions[leftSize + 1 + a] = newTemp.getPosition() + a;
                }

                onRemoveListener.onReadyToMove(positions);
                //i = i + 1 + rightSize;
            }

            //纵向消除
            if ((topSize + 1 + bottomSize) >= 3) {

            }
        }
    }


    /**
     * 爆炸 消除
     */
    private void clearExplode(int position) {


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

    public void setOnRemoveListener(OnRemoveListener listener) {
        onRemoveListener = listener;
    }


    public interface OnRemoveListener {
        void onReadyToMove(int[] positions);
    }

}


