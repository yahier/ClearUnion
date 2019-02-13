package yahier.com.clear;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 6);
        recyclerView.setLayoutManager(layoutManager);
        MainRecycleAdapter mAdapter = new MainRecycleAdapter(this);
        mAdapter.setOnRemoveListener(positions -> {
            recyclerView.postDelayed(() -> {
                testRemoveEffect(positions);
            }, 300);
        });
        mAdapter.setData();

        recyclerView.setAdapter(mAdapter);
    }

    private void testRemoveEffect(int[] positions) {
        Log.e("准备删除", Arrays.toString(positions));
        for (int i = 0; i < positions.length; i++) {
            View view = recyclerView.getChildAt(positions[i]);
            AnimatorSet set = new AnimatorSet();
            set.playTogether(
                    ObjectAnimator.ofFloat(view, "scaleX", 1, 0.6f),
                    ObjectAnimator.ofFloat(view, "scaleY", 1, 0.6f)
            );
            set.setDuration(2000).start();
        }
    }

}

