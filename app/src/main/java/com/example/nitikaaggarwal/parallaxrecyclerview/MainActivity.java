package com.example.nitikaaggarwal.parallaxrecyclerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.nitikaaggarwal.parallaxrecyclerview.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView multipleRowRecyclerView;
    private MultipleRowAdapter multipleRowAdapter;

    private List<MultipleRowModel> multipleRowModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        multipleRowRecyclerView = (RecyclerView)findViewById(R.id.multipleRowRecyclerView);
        multipleRowRecyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        multipleRowRecyclerView.setLayoutManager(linearLayoutManager);
        multipleRowRecyclerView.setItemAnimator(new DefaultItemAnimator());
        fillAdapter();
        multipleRowAdapter = new MultipleRowAdapter(MainActivity.this, multipleRowModelList);
        multipleRowRecyclerView.setAdapter(multipleRowAdapter);
    }

    private void fillAdapter() {

        int type;

        String content;

        for (int i = 0; i < 100; i++) {

            if (i %15==0) {
                type = AppConstants.FIRST_ROW;
                content = "";
            } else {
                type = AppConstants.OTHER_ROW;
                content = "Type 2";
            }

            multipleRowModelList.add(new MultipleRowModel(type , content));
        }
    }
}
