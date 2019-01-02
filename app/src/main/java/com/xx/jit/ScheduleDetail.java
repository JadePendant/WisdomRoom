package com.xx.jit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ScheduleDetail extends AdvertisementTools {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_detail);
        Intent intent = getIntent();
        int index =intent.getIntExtra("index",1);
        index =index+1;
        ImageView image = (ImageView) findViewById(R.id.image);
        if(index==1)
            image.setImageResource(R.mipmap.p1);
        else if(index==2)
            image.setImageResource(R.mipmap.p2);
        else if(index==3)
            image.setImageResource(R.mipmap.p3);
        else if(index==4)
            image.setImageResource(R.mipmap.p4);
        else if(index==5)
            image.setImageResource(R.mipmap.p5);
        else if(index==6)
            image.setImageResource(R.mipmap.p6);
        else if(index==7)
            image.setImageResource(R.mipmap.p7);
        else if(index==8)
            image.setImageResource(R.mipmap.p8);
        else if(index==9)
            image.setImageResource(R.mipmap.p9);
        else if(index==10)
            image.setImageResource(R.mipmap.p10);
        else if(index==11)
            image.setImageResource(R.mipmap.p11);
        else
            image.setImageResource(R.mipmap.p12);
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScheduleDetail.this.finish();
            }
        });
    }
}
