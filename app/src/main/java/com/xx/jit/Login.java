package com.xx.jit;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
public class Login extends AdvertisementTools {
    private AppData appData;
    private ACache aCache;
    private ACache academy_aCache;
    private RecyclerView recyclerView;
    private LoginAdapter adapter;
    private ArrayList<JSONObject> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appData = (AppData) getApplication();
        aCache = ACache.get(this);
        academy_aCache = ACache.get(this);
        data = new ArrayList<>();
        String academy_url = appData.getAcademyUrl();
        String adimg_url = appData.getAdimgUrl();
        JSONObject jsonObject =appData.getAcademy_selected_acahe();
        if(jsonObject!=null)
        {
            try {
                appData.setAcademy(jsonObject.getString("academy"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(Login.this, Main.class);
            // intent.putExtra("academy",data.get(position).getString("academy"));
            startActivity(intent);

        }
        else{
           // Toast.makeText(Login.this,"没有缓存",Toast.LENGTH_LONG).show();
            setContentView(R.layout.login);
            getAcademy(academy_url);
            getAdimgs(adimg_url);
            recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
            //设置LayoutManager
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            //设置适配器
            adapter = new LoginAdapter(Login.this, data);
            recyclerView.setAdapter(adapter);
        }

    }
    @Override
    protected void onStop() {
        super.onStop();
        InitQueue.getHttpQueue().cancelAll("get_academy");
        InitQueue.getHttpQueue().cancelAll("get_advertisement");
    }

    /*请求图片数据*/
    public void getAdimgs(String url) {
        //1.创建请求队列
        InitQueue.queue = Volley.newRequestQueue(Login.this);
        //2.创建get请求
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,null,new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject) {
                    String aCache_key = "adimages";//缓存key
                    aCache.remove(aCache_key);
                    aCache.put(aCache_key, jsonObject);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(Login.this,"服务器异常",Toast.LENGTH_LONG).show();
            }
        });
        //3.添加请求队列
        jsonObjectRequest.setTag("get_advertisement");
        InitQueue.getHttpQueue().add(jsonObjectRequest);

    }

    /*初始化数据*/
    public void getAcademy(String url) {

        //1.创建请求队列
        InitQueue.queue = Volley.newRequestQueue(Login.this);
        //2.创建post请求
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,null,new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    String aCache_key = "adacemy";//缓存key
                    academy_aCache.remove(aCache_key);
                    academy_aCache.put(aCache_key, jsonObject);
                    JSONArray array = jsonObject.getJSONArray("list");
                    for (int i = array.length()-1; i >=0 ; i--) {
                        JSONObject jsonObject1 = array.getJSONObject(i);
                        data.add(jsonObject1);
                    }
                    //设置适配器
                    adapter = new LoginAdapter(Login.this, data);
                    recyclerView.setAdapter(adapter);
                    //设置点击某条的监听
                    adapter.setOnItemClickListener(new LoginAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) throws JSONException {
                            appData.setAcademy(data.get(position).getString("academy"));
                            appData.setAcademy_selected_acahe(data.get(position));
                            Intent intent = new Intent(Login.this, Main.class);
                           // intent.putExtra("academy",data.get(position).getString("academy"));
                            startActivity(intent);
                        }
                    });
                } catch (JSONException e) {
                    try {
                        String aCache_key = "adacemy";//缓存key
                        JSONObject jsonObject2=academy_aCache.getAsJSONObject(aCache_key);
                        JSONArray array = jsonObject2.getJSONArray("list");
                        for (int i = array.length()-1; i >=0 ; i--) {
                            JSONObject jsonObject1 = array.getJSONObject(i);
                            data.add(jsonObject1);
                        }
                        //设置适配器
                        adapter = new LoginAdapter(Login.this, data);
                        recyclerView.setAdapter(adapter);
                        //设置点击某条的监听
                        adapter.setOnItemClickListener(new LoginAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) throws JSONException {
                                appData.setAcademy(data.get(position).getString("academy"));
                                appData.setAcademy_selected_acahe(data.get(position));
                                Intent intent = new Intent(Login.this, Main.class);
                                // intent.putExtra("academy",data.get(position).getString("academy"));
                                startActivity(intent);
                            }
                        });
                    } catch (JSONException e1) {

                    }
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                try {
                    String aCache_key = "adacemy";//缓存key
                    final JSONObject jsonObject=academy_aCache.getAsJSONObject(aCache_key);
                    JSONArray array = jsonObject.getJSONArray("list");
                    for (int i = array.length()-1; i >=0 ; i--) {
                        JSONObject jsonObject1 = array.getJSONObject(i);
                        data.add(jsonObject1);
                    }
                    //设置适配器
                    adapter = new LoginAdapter(Login.this, data);
                    recyclerView.setAdapter(adapter);
                    //设置点击某条的监听
                    adapter.setOnItemClickListener(new LoginAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) throws JSONException {
                            //Toast.makeText(Login.this,"1",Toast.LENGTH_LONG).show();
                           // String aCache_key = "academy_saved";//缓存key
                            //is_saved_academy.remove(aCache_key);
                            //JSONObject jsonObject3 =data.get(position);
                            //is_saved_academy.put(aCache_key,jsonObject3);
                            appData.setAcademy_selected_acahe(data.get(position));
                            appData.setAcademy(data.get(position).getString("academy"));
                            Intent intent = new Intent(Login.this, Main.class);
                            // intent.putExtra("academy",data.get(position).getString("academy"));
                            startActivity(intent);
                        }
                    });
                } catch (JSONException e) {

                }
            }
        });
        //3.添加请求队列
        jsonObjectRequest.setTag("get_academy");
        InitQueue.getHttpQueue().add(jsonObjectRequest);
    }
}

