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
        //Log.e("准备删除", Arrays.toString(positions));
        for (int i = 0; i < positions.length; i++) {
            View view = recyclerView.getChildAt(positions[i]);
            AnimatorSet set = new AnimatorSet();
            set.playTogether(
                    ObjectAnimator.ofFloat(view, "scaleX", 1, 0.5f),
                    ObjectAnimator.ofFloat(view, "scaleY", 1, 0.5f)
            );
            set.setDuration(2000).start();
        }

        recyclerView.postDelayed(() -> {
            fillEmpty(positions, orientation);
        }, 2000);
    }

    /**
     * 降落，填补空白
     */
    private void fillEmpty(int[] positions, int orientation) {
        //先尝试降落一个格子
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
            for (int i = 0; i < positions.length; i++) {
                positions[i] = positions[i] - 6 * (i + 1);
            }
        }
        // movePositions = IntStream.of(positions).filter(item -> item >= 0).toArray();

        Log.e("准备移动", Arrays.toString(movePositions));
        for (int i = 0; i < movePositions.length; i++) {
            View view = recyclerView.getChildAt(movePositions[i]);
            AnimatorSet set = new AnimatorSet();
            int distance = 150;
            if (orientation == 1) {
                distance = 150;   //todo  即使是横向删除后，下坠的距离也不是 全都150
            } else {
                distance = 150 * positions.length;
            }

            set.playTogether(
                    ObjectAnimator.ofFloat(view, "translationY", 0, distance)
            );
            set.setDuration(1000).start();
        }


    }

    /**
     * 生成新的元素
     */
    private void generateNew() {

    }

}

