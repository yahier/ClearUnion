package yahier.com.clear;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    public final static int spanCount = 6;
    public final static int ElementHeight = 150;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        recyclerView.setLayoutManager(layoutManager);
        MainRecycleAdapter mAdapter = new MainRecycleAdapter(this);
        mAdapter.setOnRemoveListener((positions, orientation) -> {
            recyclerView.postDelayed(() -> {
                testRemoveEffect(positions, orientation);
            }, 300);
        });
        mAdapter.setData();

        recyclerView.setAdapter(mAdapter);

        findViewById(R.id.btnRestart).setOnClickListener(view -> {
            mAdapter.setData();
        });
    }

    /**
     * 删除相同的结点  这个方法会一次性被反复调用
     * orientation 横向为1 纵向为2
     */
    private void testRemoveEffect(int[] positions, int orientation) {
        Log.e("准备删除", Arrays.toString(positions));
        for (int i = 0; i < positions.length; i++) {
            View view = recyclerView.getChildAt(positions[i]);
            AnimatorSet set = new AnimatorSet();
            set.playTogether(
                    ObjectAnimator.ofFloat(view, "scaleX", 1, 0f),
                    ObjectAnimator.ofFloat(view, "scaleY", 1, 0f)
            );
            set.setDuration(8000).start();
        }

        recyclerView.postDelayed(() -> {
            fillEmpty(positions, orientation);
        }, 2000);
    }

    /**
     * 降落，填补空白
     */
    private void fillEmpty(int[] positions, int orientation) {
        int[] movePositions = null;
        if (orientation == 1) {
            for (int i = 0; i < positions.length; i++) {
                positions[i] = positions[i] - 6;
            }


            List<Integer> listPosition = new ArrayList<>();
            for (int temp : positions) {
                int position = temp;
                while (position >= 0) {
                    listPosition.add(position);
                    position = position - spanCount;
                }
            }

            movePositions = new int[listPosition.size()];
            for (int i = 0; i < movePositions.length; i++) {
                movePositions[i] = listPosition.get(i);
            }


        } else {
            List<Integer> listPosition = new ArrayList<>();
            for (int i = 0; i < positions.length; i++) {
                int temp = positions[i] - 6;
                Log.d("准备移动", "temp:" + temp);
                while (temp >= 0) {
                    if (!listPosition.contains(temp))
                        listPosition.add(temp);
                    temp = temp - spanCount;
                }
            }

            Log.e("准备移动", "size:" + listPosition.size());

            movePositions = new int[listPosition.size()];
            for (int i = 0; i < movePositions.length; i++) {
                movePositions[i] = listPosition.get(i);
            }
        }

        Log.e("准备移动", Arrays.toString(movePositions));
        for (int i = 0; i < movePositions.length; i++) {
            View view = recyclerView.getChildAt(movePositions[i]);
            AnimatorSet set = new AnimatorSet();
            int distance = 150;
            if (orientation == 1) {
                int sameElement = sameElement(movePositions, movePositions[i]);
                distance = ElementHeight * sameElement;
            } else {
                //fixme 此处有破绽，假如需要删除的数组是[3, 9, 15, 6, 0, 12, 18]，难道每个元素的移动都是相同的吗
                //fixme 实际上 只有同一个垂直线上的元素，移动的距离才相同
                distance = ElementHeight * positions.length;
            }

            set.playTogether(
                    ObjectAnimator.ofFloat(view, "translationY", 0, distance)
            );
            set.setDuration(5000).start();
        }


    }

    /**
     * 生成新的元素
     */
    private void generateNew() {

    }


    /**
     * 返回 数组 中有该元素的个数。在横向删除中时 垂直的下面有多个元素被删除时 使用
     */
    private int sameElement(int[] array, int element) {
        int sameCount = 0;
        for (int temp : array) {
            if (temp == element) {
                sameCount++;
            }
        }
        return sameCount;
    }

}

