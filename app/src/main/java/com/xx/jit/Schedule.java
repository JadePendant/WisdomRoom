package com.xx.jit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class Schedule extends AdvertisementTools{
    private RecyclerView recyclerView;
    private ScheduleAdapter adapter;
    private ArrayList<String> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule);
        data = new ArrayList<String>();
        data.add("信息安全基础教学实验室(科技楼2 208-2)教室课程表");
        data.add("Linux技术实验室(科技楼2 211)教室课程表");
        data.add("网络攻防实验室(科技楼2 215)教室课程表");
        data.add("软件测试实验室(科技楼2 309)教室课程表");
        data.add("项目开发实验室(科技楼2 316)教室课程表");
        data.add("项目管理实验室(科技楼2 317)教室课程表");
        data.add("NIIT项目实验室(一分室)(科技楼2 408-1)教室课程表");
        data.add("NIIT项目实验室(二分室)(科技楼2 408-2)教室课程表");
        data.add("NIIT项目实验室(三分室)(科技楼2 409-1)教室课程表");
        data.add("程序设计与软件技术实验室(一分室)(科技楼2 409-2)教室课程表");
        data.add("Java技术实验室(科技楼2 410)教室课程表");
        data.add("软件工程实验室(科技楼2 415)教室课程表");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        //设置LayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        //设置适配器
        adapter = new ScheduleAdapter(Schedule.this,data);
        recyclerView.setAdapter(adapter);
        //设置点击某条的监听
        adapter.setOnItemClickListener(new ScheduleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                    Intent intent = new Intent(Schedule.this,ScheduleDetail.class);
                    intent.putExtra("index",position);
                    startActivity(intent);
            }
        });
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Schedule.this.finish();
            }
        });

    }
}
