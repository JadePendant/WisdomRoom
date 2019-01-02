package com.xx.jit;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Detail extends AdvertisementTools {
    private AppData appData;
    private ACache detail_aCache;
    private ACache list_aCache;
    private RecyclerView recyclerView;
    private TextView content;
    private TextView title;
    private ImageView image;
    private ArrayList<JSONObject> data;
    private DetailAdapter adapter;
    private  int[] images = new int[]{R.mipmap.m1,R.mipmap.m2,R.mipmap.m4,R.mipmap.m3, R.mipmap.m5,R.mipmap.m6,R.mipmap.m7,R.mipmap.m8,R.mipmap.m9,R.mipmap.m10};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        appData = (AppData) getApplication();
        detail_aCache = ACache.get(this);
        list_aCache = ACache.get(this);
        data = new ArrayList<>();
        Intent intent = getIntent();
        initView();
        String type = intent.getStringExtra("type");
        String id = intent.getIntExtra("detailId",1)+"";
        getList(appData.getUrl(),appData.getAcademy(),type);
        getDetail(appData.getUrl(),appData.getAcademy(),id);
        //设置LayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(Detail.this,LinearLayoutManager.VERTICAL,false));
        //recyclerView.addItemDecoration(new DividerItemDecoration(Detail.this,DividerItemDecoration.VERTICAL));
        Button back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Detail.this.finish();
            }
        });
    }
    @Override
    protected void onStop() {
            super.onStop();
            InitQueue.getHttpQueue().cancelAll("get_detail_list");
        InitQueue.getHttpQueue().cancelAll("get_detail");
    }
    //将字符串转换成Bitmap 类型注意：需要去掉字符串的data:image/png;base64,
    public Bitmap stringtoBitmap(String string){
        Bitmap bitmap=null;
        try {
            byte[] bitmapArray = Base64.decode(string.split(",")[1], Base64.DEFAULT);
            bitmap= BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    /*初始化视图*/
    private void initView() {

        recyclerView = findViewById(R.id.recyclerview);
        image =  findViewById(R.id.image);
        title =  findViewById(R.id.title);
        content =  findViewById(R.id.contentPanel);
    }
    /*初始化数据*/
    public void getList(String url, final String academy, final String type)
    {
        Map<String,String> map = new HashMap<>();
        map.put("academy",academy);
        map.put("type",type);
        //1.创建请求队列
        InitQueue.queue = Volley.newRequestQueue(Detail.this);
        //2.创建post请求
        CustomRequest jsonObjectRequest = new CustomRequest( Request.Method.POST,url,map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    String list_aCache_key = "detail_list"+type;
                    list_aCache.remove(list_aCache_key);
                    list_aCache.put(list_aCache_key, jsonObject);
                    JSONArray array = jsonObject.getJSONArray("list");
                    for(int i=0;i<array.length();i++)
                    {
                        JSONObject jsonObject1 = array.getJSONObject(i);
                        data.add(jsonObject1);
                    }
                    //设置适配器
                    adapter = new DetailAdapter(Detail.this,data);
                    recyclerView.setAdapter(adapter);
                    //设置点击某条的监听
                    adapter.setOnItemClickListener(new DetailAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position,JSONObject json) {
                            try {
                                getDetail(appData.getUrl(),academy,data.get(position).getString("id"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                } catch (JSONException e) {

                        String list_aCache_key = "detail_list"+type;
                        JSONObject jsonObject2 =list_aCache.getAsJSONObject(list_aCache_key);
                        if(jsonObject2!=null)
                        {
                            JSONArray array = null;
                            try {
                                array = jsonObject2.getJSONArray("list");
                                for(int i=0;i<array.length();i++)
                                {
                                    JSONObject jsonObject1 = array.getJSONObject(i);
                                    data.add(jsonObject1);
                                }
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }

                            //设置适配器
                            adapter = new DetailAdapter(Detail.this,data);
                            recyclerView.setAdapter(adapter);
                            //设置点击某条的监听
                            adapter.setOnItemClickListener(new DetailAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position,JSONObject json) {
                                    try {
                                        getDetail(appData.getUrl(),academy,data.get(position).getString("id"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }

                    e.printStackTrace();

                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(),"服务器异常",Toast.LENGTH_SHORT).show();

                try {
                        String list_aCache_key = "detail_list"+type;
                        JSONObject jsonObject =list_aCache.getAsJSONObject(list_aCache_key);
                        if(jsonObject!=null)
                        {
                            JSONArray array = jsonObject.getJSONArray("list");
                            for(int i=0;i<array.length();i++)
                            {
                                JSONObject jsonObject1 = array.getJSONObject(i);
                                data.add(jsonObject1);
                            }
                            //设置适配器
                            adapter = new DetailAdapter(Detail.this,data);
                            recyclerView.setAdapter(adapter);
                            //设置点击某条的监听
                            adapter.setOnItemClickListener(new DetailAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position,JSONObject json) {
                                    try {
                                        getDetail(appData.getUrl(),academy,data.get(position).getString("id"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        //3.添加请求队列
        jsonObjectRequest.setTag("get_detail_list");
        InitQueue.getHttpQueue().add(jsonObjectRequest);

    }
    //获得详细信息
    public void getDetail(String url, String academy, final String id)
    {
        Map<String,String> map = new HashMap<>();
        map.put("id",id);
        map.put("academy",academy);
        //1.创建请求队列
        InitQueue.queue = Volley.newRequestQueue(Detail.this);
        //2.创建post请求
        CustomRequest jsonObjectRequest = new CustomRequest( Request.Method.POST,url,map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    String detail_aCache_key = "detail_list"+id;//设置缓存key
                    detail_aCache.remove(detail_aCache_key);//清空缓存
                    detail_aCache.put(detail_aCache_key, jsonObject);//添加缓存
                    JSONArray array = jsonObject.getJSONArray("list");
                    String text =array.getJSONObject(0).getString("txt_html");
                    content.setText(Html.fromHtml(text));
                    title.setText(array.getJSONObject(0).getString("name"));
                    if(array.getJSONObject(0).getString("image").length()==0)
                        image.setImageResource(images[(int) (Math.random()*10)]);
                    else
                        image.setImageBitmap(stringtoBitmap(array.getJSONObject(0).getString("image")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(),"服务器异常",Toast.LENGTH_SHORT).show();
                try {
                    String detail_aCache_key = "detail_list"+id;//设置缓存key
                    JSONObject jsonObject = detail_aCache.getAsJSONObject(detail_aCache_key);
                    if(jsonObject!=null)
                    {
                        JSONArray array = jsonObject.getJSONArray("list");
                        String text =array.getJSONObject(0).getString("txt_html");
                        content.setText(Html.fromHtml(text));
                        title.setText(array.getJSONObject(0).getString("name"));
                        if(array.getJSONObject(0).getString("image").length()==0)
                            image.setImageResource(images[(int) (Math.random()*10)]);
                        else
                            image.setImageBitmap(stringtoBitmap(array.getJSONObject(0).getString("image")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        //3.添加请求队列
        jsonObjectRequest.setTag("get_detail");
        InitQueue.getHttpQueue().add(jsonObjectRequest);
    }
}
