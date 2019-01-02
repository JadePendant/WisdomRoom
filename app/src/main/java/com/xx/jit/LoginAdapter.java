package com.xx.jit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

    public class LoginAdapter extends RecyclerView.Adapter<LoginAdapter.MyViewHodler> {
        private final Context context;
        private ArrayList<JSONObject> data;
        public LoginAdapter(Context context, ArrayList<JSONObject> data) {
            this.context = context;
            this.data=data;
        }
        @Override
        public LoginAdapter.MyViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
            //定义视图
            View itemView = View.inflate(context,R.layout.login_recyclerview,null);
            return new LoginAdapter.MyViewHodler(itemView);
        }

        @Override
        public void onBindViewHolder(LoginAdapter.MyViewHodler holder, int position) {
            //根据位置得到数据
            JSONObject jsonObject = data.get(position);
            try {
                holder.academy.setText(jsonObject.getString("academy"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            holder.itemView.setTag(position);
        }
        //得到总条数
        @Override
        public int getItemCount() {
            return data.size();
        }
        class MyViewHodler extends RecyclerView.ViewHolder {
            private TextView academy;
            public MyViewHodler(View itemView)
            {
                super(itemView);
                academy = itemView.findViewById(R.id.academy);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            //注意这里使用getTag方法获取position
                            try {
                                onItemClickListener.onItemClick(v, (int) v.getTag());
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
        private LoginAdapter.OnItemClickListener onItemClickListener=null;
        //设置recycle某条的监听
        public void setOnItemClickListener(LoginAdapter.OnItemClickListener onItemClickListener)
        {
            this.onItemClickListener = onItemClickListener;
        }
    }


