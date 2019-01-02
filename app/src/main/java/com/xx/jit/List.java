package com.xx.jit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class List extends AdvertisementTools {
    private AppData appData;//全局数据
    private ACache aCache;//缓存
    private RecyclerView recyclerView;
    private ArrayList<JSONObject> data;
    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        appData = (AppData) getApplication();
        aCache = ACache.get(this);
        Intent intent = getIntent();
        data = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerview);
        //设置LayoutManager
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        String type = intent.getStringExtra("type");
        int typeLength = intent.getStringExtra("type").length();
        getList(appData.getUrl(), type, appData.getAcademy(), typeLength);
        Button back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List.this.finish();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        InitQueue.getHttpQueue().cancelAll("get_list");
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

    //将Bitmap转换成字符串
    public String bitmaptoString(Bitmap bitmap) {
        String string = null;
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream);
        byte[] bytes = bStream.toByteArray();
        string = Base64.encodeToString(bytes, Base64.DEFAULT);
        return string;
    }


    //转码并保存图片
    public static void decoderBase64File(String base64Code, String savePath) throws Exception {
        //byte[] buffer = new BASE64Decoder().decodeBuffer(base64Code);
        byte[] buffer = Base64.decode(base64Code, Base64.DEFAULT);
        FileOutputStream out = new FileOutputStream(savePath);
        out.write(buffer);
        out.close();
    }

    /*初始化数据*/
    public void getList(String url, final String type, String academy, int typeLength) {

        Map<String, String> map = new HashMap<>();
        if (typeLength != 0) {
            map.put("type", type);
            map.put("academy", academy);
        } else
            map.put("academy", academy);
        //1.创建请求队列
        InitQueue.queue = Volley.newRequestQueue(List.this);
        //2.创建post请求
        CustomRequest jsonObjectRequest = new CustomRequest(Request.Method.POST, url, map, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    JSONArray array = jsonObject.getJSONArray("list");
                        String aCache_key = "list"+type;//缓存key
                        aCache.remove(aCache_key);
                        aCache.put(aCache_key, jsonObject);

                        for (int i = array.length()-1; i >=0 ; i--) {
                            JSONObject jsonObject1 = array.getJSONObject(i);
                            data.add(jsonObject1);
                        }
                        //设置适配器
                        adapter = new ListAdapter(List.this, data);
                        recyclerView.setAdapter(adapter);
                        //设置点击某条的监听
                        adapter.setOnItemClickListener(new ListAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position, JSONObject json) throws JSONException {
                                Intent intent = new Intent(List.this, Detail.class);
                                intent.putExtra("type", json.getString("type"));
                                intent.putExtra("detailId", json.getInt("id"));
                                startActivity(intent);
                            }
                        });

                } catch (JSONException e) {
                    String aCache_key = "list"+type;//缓存key
                    JSONObject jsonObject2 = aCache.getAsJSONObject(aCache_key);
                    if(jsonObject2!=null)
                    {
                        // array = null;
                        try {
                            JSONArray array = jsonObject2.getJSONArray("list");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject1 = array.getJSONObject(i);
                                data.add(jsonObject1);
                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        //设置适配器
                        adapter = new ListAdapter(List.this, data);
                        recyclerView.setAdapter(adapter);
                        //设置点击某条的监听
                        adapter.setOnItemClickListener(new ListAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position, JSONObject json) throws JSONException {
                                Intent intent = new Intent(List.this, Detail.class);
                                intent.putExtra("type", json.getString("type"));
                                intent.putExtra("detailId", json.getInt("id"));
                                startActivity(intent);
                            }
                        });
                    }
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), "服务器异常", Toast.LENGTH_SHORT).show();
                try {
                    String aCache_key = "list"+type;//缓存key
                    JSONObject jsonObject = aCache.getAsJSONObject(aCache_key);
                    if(jsonObject!=null)
                    {
                        JSONArray array = jsonObject.getJSONArray("list");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject1 = array.getJSONObject(i);
                            data.add(jsonObject1);
                        }
                        //设置适配器
                        adapter = new ListAdapter(List.this, data);
                        recyclerView.setAdapter(adapter);
                        //设置点击某条的监听
                        adapter.setOnItemClickListener(new ListAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position, JSONObject json) throws JSONException {
                                Intent intent = new Intent(List.this, Detail.class);
                                intent.putExtra("type", json.getString("type"));
                                intent.putExtra("detailId", json.getInt("id"));
                                startActivity(intent);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        //3.添加请求队列
        jsonObjectRequest.setTag("get_list");
        InitQueue.getHttpQueue().add(jsonObjectRequest);
    }
}
