package com.xx.jit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by 炊烟 on 2018/11/8.
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHodler> {
    private  int[] images = new int[]{R.mipmap.m1,R.mipmap.m2,R.mipmap.m4,R.mipmap.m3, R.mipmap.m5,R.mipmap.m6,R.mipmap.m7,R.mipmap.m8,R.mipmap.m9,R.mipmap.m10};
    private final Context contetx;
    private  ArrayList<JSONObject> data;
    public ListAdapter(Context context, ArrayList<JSONObject> data) {
        this.contetx = context;
        this.data=data;
    }
    @Override
    public MyViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        //定义视图
        View itemView = View.inflate(contetx,R.layout.list_recyclerview,null);
        return new MyViewHodler(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHodler holder, int position) {
        //根据位置得到数据
        JSONObject jsonObject = data.get(position);
        try {
            if(jsonObject.getString("name").length()<9)
            holder.tv_title.setText(jsonObject.getString("name"));
            else
                holder.tv_title.setText(jsonObject.getString("name").substring(0,9));
            if(jsonObject.getString("intro").length()<16)
                holder.tv_article.setText(jsonObject.getString("intro"));
            else
            holder.tv_article.setText(jsonObject.getString("intro").substring(0,16)+"...");
            if(jsonObject.getString("image").length()!=0)
                holder.iv_img.setImageBitmap(stringtoBitmap(jsonObject.getString("image")));
            else
                holder.iv_img.setImageResource(images[(int) (Math.random()*10)]);
            //holder.iv_img.setImageBitmap(stringtoBitmap(jsonObject.getString("image")));
            //将position保存在itemView的Tag中，以便点击时进行获取
            holder.itemView.setTag(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
    //得到总条数
    @Override
    public int getItemCount() {
        return data.size();
    }
    class MyViewHodler extends RecyclerView.ViewHolder {
        private ImageView iv_img;
        private TextView tv_title;
        private TextView tv_article;
    public MyViewHodler(View itemView)
    {
        super(itemView);
        iv_img = itemView.findViewById(R.id.img);
        tv_title = itemView.findViewById(R.id.title);
        tv_article = itemView.findViewById(R.id.article);
       itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (onItemClickListener != null) {
                   //注意这里使用getTag方法获取position
                   try {
                       onItemClickListener.onItemClick(v,(int)v.getTag(),data.get((int)v.getTag()));
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
               }
           }
       });
    }
  }
  //定义接口,某条的监听
    public interface OnItemClickListener{
      //点击某条视图的回调函数
      void onItemClick(View view, int position, JSONObject json) throws JSONException;
    }
    private OnItemClickListener onItemClickListener=null;
    //设置recycle某条的监听
    public void setOnItemClickListener(OnItemClickListener onItemClickListener)
    {
        this.onItemClickListener = onItemClickListener;
    }
}
