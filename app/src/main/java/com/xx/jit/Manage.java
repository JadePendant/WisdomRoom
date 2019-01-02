package com.xx.jit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class Manage extends AdvertisementTools {

    private RecyclerView recyclerView;
    private ManageAdapter adapter;
    private ArrayList<String> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage);
        data = new ArrayList<String>();
        data.add("NIIT项目实验室（一分室）408-1摄像头一");
        data.add("NIIT项目实验室（一分室）408-1摄像头二");
        data.add("NIIT项目实验室（二分室）408-2摄像头一");
        data.add("NIIT项目实验室（二分室）408-2摄像头二");
        data.add("NIIT项目实验室（三分室）409-1摄像头一");
        data.add("NIIT项目实验室（三分室）409-1摄像头二");
        data.add("程序设计与软件技术实验室（一分室）409-2摄像头一");
        data.add("程序设计与软件技术实验室（一分室）409-2摄像头二");
        data.add("程序设计与软件技术实验室（二分室）414摄像头一");
        data.add("程序设计与软件技术实验室（二分室）414摄像头二");
        data.add("软件工程实验室415摄像头一");
        data.add("软件工程实验室415摄像头二");
        data.add("Java技术实验室410摄像头一");
        data.add("Java技术实验室410摄像头二");
        data.add("软件测试实验室308,309摄像头一");
        data.add("软件测试实验室308,309摄像头二");
        data.add("软件测试实验室308,309摄像头三");
        data.add("移动互联实验室310,311摄像头一");
        data.add("移动互联实验室310,311摄像头二");
        data.add("移动互联实验室310,311摄像头三");
        data.add("软件创新实验室(一)312摄像头一");
        data.add("软件创新实验室(一)312摄像头二");
        data.add("项目开发实验室316摄像头一");
        data.add("项目开发实验室316摄像头二");
        data.add("项目管理实验室317摄像头一");
        data.add("项目管理实验室317摄像头二");
        data.add("大数据与云安全实验室208-1摄像头一");
        data.add("大数据与云安全实验室208-1摄像头二");
        data.add("安全软件技术实验室208-2摄像头一");
        data.add("安全软件技术实验室208-2摄像头二");
        data.add("系统安全与逆向工程实验室209摄像头一");
        data.add("系统安全与逆向工程实验室209摄像头二");
        data.add("信息安全基础教学实验室210摄像头一");
        data.add("信息安全基础教学实验室210摄像头二");
        data.add("Linux技术实验室211摄像头一");
        data.add("Linux技术实验室211摄像头二");
        data.add("网络攻防实验室215摄像头一");
        data.add("网络攻防实验室215摄像头二");
        data.add("无线安全实验室216摄像头一");
        data.add("无线安全实验室216摄像头二");
        data.add("软件创新实验室（二）412");
        data.add("软件创新实验室（三）413");
        data.add("实验中心库房（辅助用房）213");
        data.add("数据中心机房（辅助用房）314摄像头一");
        data.add("数据中心机房（辅助用房）314摄像头二");
        data.add("实验中心控制室（辅助用房）315");
        data.add("2楼过道摄像头一");
        data.add("2楼过道摄像头二");
        data.add("2楼过道摄像头三");
        data.add("2楼过道摄像头四");
        data.add("3楼过道摄像头一");
        data.add("3楼过道摄像头二");
        data.add("3楼过道摄像头三");
        data.add("3楼过道摄像头四");
        data.add("4楼过道摄像头一");
        data.add("4楼过道摄像头二");
        data.add("4楼过道摄像头三");
        data.add("4楼过道摄像头四");
        data.add("3楼会议室");
        data.add("4楼会议室");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        //设置LayoutManager
        //recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        //设置适配器
        adapter = new ManageAdapter(Manage.this,data);
        recyclerView.setAdapter(adapter);
        //设置点击某条的监听
        adapter.setOnItemClickListener(new ManageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent intent = new Intent(Manage.this,CameraBaseActivity.class);
                intent.putExtra("index",position);
                startActivity(intent);
            }
        });
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Manage.this.finish();
            }
        });

    }
}
