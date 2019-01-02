package com.xx.jit;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
public class Animation extends AppCompatActivity {
    private  AppData appData;
    private ACache aCache;
    private int length;
    private ArrayList<Bitmap> adimgs = new ArrayList<>();
    ImageView image1;
    int count = 0;
    Random ran = new Random();
    android.view.animation.Animation[] actor = new android.view.animation.Animation[3];
    Timer time;
    TimerTask task;
    Handler handler;
    Thread thread1;
    Runnable runnabel;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //adimgs=null;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.animation);
        appData = new AppData();
        image1 = (ImageView)findViewById(R.id.image1);
        //text1.setOnClickListener(new roll());
        actor[0] = AnimationUtils.loadAnimation(this, R.anim.alpha);
        actor[1] = AnimationUtils.loadAnimation(this, R.anim.scale);
        actor[2] = AnimationUtils.loadAnimation(this, R.anim.rote);
        //image1.startAnimation(actor[0]);
        aCache =  ACache.get(this);
        try {
            String aCache_key = "adimages";//缓存key
            JSONObject jsonObject = aCache.getAsJSONObject(aCache_key);
            if(jsonObject!=null)
            {
                JSONArray array = jsonObject.getJSONArray("list");
                length = array.length();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObject1 = array.getJSONObject(i);
                    adimgs.add(stringtoBitmap(jsonObject1.getString("url")));
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }






















        int i = 0;
        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Context context = Animation.this;
                //Intent next = new Intent(context, Main2Activity.class);

                Animation.this.finish();
                //startActivity(next);

            }
        });

        handler = new Handler(){
          public void handleMessage(Message msg){
              super.handleMessage(msg);




              int action = ran.nextInt(3);
              for(int i=0;i<adimgs.size();i++)

              {
                  if (msg.what == i) {
                      image1.startAnimation(actor[action]);
                      image1.setImageBitmap(adimgs.get(i));
                      // image1.setImageDrawable(getResources().getDrawable());
                      image1.setScaleType(ImageView.ScaleType.FIT_XY);
                  }
              }
           }
        };



        runnabel = new Runnable() {

            public void run() {
                    Message message = new Message();
                    count++;
                    message.what = count%length;
                    handler.sendMessage(message);
                    handler.postDelayed(this, 8000);

            }
        };


        handler.postDelayed(runnabel, 8000);
//        task.cancel();
//        time.cancel();
//        thread1.destroy();


    }
    //将字符串转换成Bitmap 类型注意：需要去掉字符串的data:image/png;base64,
    public Bitmap stringtoBitmap(String string) {
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray = Base64.decode(string.split(",")[1], Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }



}
