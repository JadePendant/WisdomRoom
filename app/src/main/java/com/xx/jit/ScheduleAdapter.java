package com.xx.jit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by 炊烟 on 2018/11/8.
 */
public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.MyViewHodler> {
    private final Context contetx;
    private  ArrayList<String> data;
    public ScheduleAdapter(Context context, ArrayList<String> data) {
        this.contetx = context;
        this.data=data;
    }
    @Override
    public MyViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        //定义视图
        View itemView = View.inflate(contetx,R.layout.schedule_recyclerview,null);
        return new MyViewHodler(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHodler holder, int position) {
        //根据位置得到数据
        //JSONObject jsonObject = data.get(position);
        holder.room.setText(data.get(position));
        holder.itemView.setTag(position);
    }
    //得到总条数
    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHodler extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView room;
        public MyViewHodler(View itemView)
        {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            room = itemView.findViewById(R.id.room);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        //注意这里使用getTag方法获取position
                        try {
                            onItemClickListener.onItemClick(v,(int)v.getTag());
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
        void onItemClick(View view, int position) throws JSONException;
    }
    private OnItemClickListener onItemClickListener=null;
    //设置recycle某条的监听
    public void setOnItemClickListener(OnItemClickListener onItemClickListener)
    {
        this.onItemClickListener = onItemClickListener;
    }
}
