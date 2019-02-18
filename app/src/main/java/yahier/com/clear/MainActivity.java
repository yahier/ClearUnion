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
        mAdapter.setOnRemoveListener(animalList -> {
            //Log.e("准备移除", "移除元素的size:" + removedPositions.size());
            recyclerView.postDelayed(() -> {
                clear(animalList);
            }, 1000);

            recyclerView.postDelayed(() -> {
                move(animalList);
            }, 3000);


        });

        mAdapter.setData();


        findViewById(R.id.btnRestart).setOnClickListener(view -> {
            mAdapter.setData();
        });


    }


    private void clear(List<Animal> animalList) {
        for (Animal animal : animalList) {
            if (animal.isToRemove()) {
                View view = recyclerView.getChildAt(animal.getPosition());
                //Log.d("移除", "position:" + removedPositions.get(i));
                AnimatorSet set = new AnimatorSet();
                set.playTogether(
                        ObjectAnimator.ofFloat(view, "scaleX", 1, 0f),
                        ObjectAnimator.ofFloat(view, "scaleY", 1, 0f)
                );
                set.setDuration(2000).start();
            }

        }
    }


    private void move(List<Animal> animalList) {
        List<Animal> moveList = Stream.of(animalList).filter(item -> item.movePosition > 0).toList();
        //Log.e("准备移动", "size:" + moveList.size());
        for (Animal animal : moveList) {
            int position = animal.getPosition();
            View view = recyclerView.getChildAt(position);
            int distance = animal.movePosition * ElementHeight;
            ObjectAnimator.ofFloat(view, "translationY", 0, distance).setDuration(1000).start();
        }


    }


    private void transforData(List<Animal> animalList) {
        //转移数据
        long clearSize = Stream.of(animalList).filter(item -> item.isToRemove()).count();
        Log.e("移除的个数", "size:" + clearSize);

        //先置空 再移动
        for (Animal animal : animalList) {
            if (animal.isToRemove()) {
                animalList.set(animal.getPosition(), null);
            }
        }

        for (Animal animal : animalList) {
            if (animal != null) {
                int movePosition = animal.getMovePosition();
                if (movePosition > 0) {
                    animalList.set(animal.getPosition(), null);
                    animalList.set(animal.getPosition() + 6 * movePosition, animal);
                }
            }
        }


        List<Animal> list = Stream.of(animalList).filter(item -> item != null).toList();
        Log.e("剩余元素", "size:" + list.size());
    }
}

