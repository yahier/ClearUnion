package yahier.com.clear;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.annimon.stream.Stream;

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
        recyclerView.setAdapter(mAdapter);

        //新的监听
        mAdapter.setOnRemoveListener((removedPositions, animalList) -> {
            Log.e("准备移除", "移除元素的size:" + removedPositions.size());
            recyclerView.postDelayed(() -> {
                clear(removedPositions);
            }, 1000);

            recyclerView.postDelayed(() -> {
                move(animalList);
            }, 1500);


        });

        mAdapter.setData();


        findViewById(R.id.btnRestart).setOnClickListener(view -> {
            mAdapter.setData();
        });


    }


    private void clear(List<Integer> removedPositions) {
        /*消失*/
        for (int i = 0; i < removedPositions.size(); i++) {
            View view = recyclerView.getChildAt(removedPositions.get(i));
            Log.d("移除", "position:" + removedPositions.get(i));
            AnimatorSet set = new AnimatorSet();
            set.playTogether(
                    ObjectAnimator.ofFloat(view, "scaleX", 1, 0f),
                    ObjectAnimator.ofFloat(view, "scaleY", 1, 0f)
            );
            set.setDuration(500).start();
        }
    }


    private void move(List<Animal> animalList) {
        animalList = Stream.of(animalList).filter(item -> item.movePostion > 0).toList();
        Log.e("准备移动", "size:" + animalList.size());
        for (Animal animal : animalList) {
            int position = animal.getPosition();
            View view = recyclerView.getChildAt(position);
            int distance = animal.movePostion * ElementHeight;
            ObjectAnimator.ofFloat(view, "translationY", 0, distance).setDuration(500).start();
        }


    }

}

