package com.xx.jit;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Main extends AdvertisementTools implements View.OnClickListener {
    private AppData appData;
    private ACache aCache;
    private Button btn_home;
    private Button btn_room;
    private Button btn_inform;
    private Button btn_ruler;
    private Button btn_schedule;
    private Button btn_teacher;
    private Button btn_innovate;
    private Button btn_cooperation;
    private Button btn_manage;
    private Button btn_consult;
    private TextView setting;
    private TextView time;
    private Intent intent;
    private ViewFlipper mViewFlipper;
    private LinearLayout mScrollTitleView;
    private DownLoadCompleteReceiver receiver;
    private Dialog dialog;
    //1.定义一个Handler
    final Handler handler=new Handler();
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            //要做的事情
            dialog.dismiss();
            checkUpdate(Main.this, true, 3, "本次修复:1.想让我更新,不存在的2.不知道修复了什么");
            initUpdata();
            handler.postDelayed(this, 3600000);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        bindLinearLayout();
        appData = (AppData) getApplication();
        aCache = ACache.get(this);
        /*定时更新*/
        //初始化更新弹出框
        dialog = new Dialog(this,R.style.MyDialog);
        //启动计时器
        handler.postDelayed(runnable, 3600000);//每六秒执行一次runnable
        getNotice(appData.getNoticeUrl(),appData.getAcademy());
        //菜单点击事件
        setting = findViewById(R.id.setting);
        btn_home = findViewById(R.id.button1);
        btn_room = findViewById(R.id.button2);
        btn_inform =  findViewById(R.id.button3);
        btn_ruler = findViewById(R.id.button4);
        btn_schedule = findViewById(R.id.button5);
        btn_teacher = findViewById(R.id.button6);
        btn_innovate =  findViewById(R.id.button7);
        btn_cooperation =  findViewById(R.id.button8);
        btn_manage = findViewById(R.id.button9);
        btn_consult = findViewById(R.id.button10);
        btn_home.setOnClickListener(this);
        btn_room.setOnClickListener(this);
        btn_inform.setOnClickListener(this);
        btn_ruler.setOnClickListener(this);
        btn_schedule.setOnClickListener(this);
        btn_teacher.setOnClickListener(this);
        btn_innovate.setOnClickListener(this);
        btn_cooperation.setOnClickListener(this);
        btn_manage.setOnClickListener(this);
        btn_consult.setOnClickListener(this);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSettingDialog();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        InitQueue.getHttpQueue().cancelAll("get_notice");
        //停止计时器
       // handler.removeCallbacks(runnable);
    }

    /*初始化布局*/
    private void bindLinearLayout() {
        mScrollTitleView = (LinearLayout) getLayoutInflater().inflate(R.layout.scrollnoticebar, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout infoList = findViewById(R.id.noticeList);
        infoList.addView(mScrollTitleView, params);
        mViewFlipper = mScrollTitleView.findViewById(R.id.id_scrollNoticeTitle);
        mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_bottom));
        mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_out_top));
        mViewFlipper.startFlipping();
    }

    /**
     * 网络请求内容后进行适配
     */
    /*初始化数据*/
    public void getNotice(String url,String academy) {

        Map<String,String> map = new HashMap<>();
            map.put("academy",academy);
        //1.创建请求队列
        InitQueue.queue = Volley.newRequestQueue(Main.this);
        //2.创建post请求
        CustomRequest jsonObjectRequest = new CustomRequest( Request.Method.POST,url,map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                try {
                    String aCache_key = "notice_list";//缓存key
                    aCache.remove(aCache_key);//清理缓存
                    aCache.put(aCache_key, jsonObject);//添加缓存
                    JSONArray array = jsonObject.getJSONArray("list");
                    bindNotices(array);
                } catch (JSONException e) {
                    String aCache_key = "notice_list";//缓存key
                    JSONObject jsonObject2 =  aCache.getAsJSONObject(aCache_key);
                    if(jsonObject2!=null)
                    {
                        JSONArray array = null;
                        try {
                            array = jsonObject2.getJSONArray("list");
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        bindNotices(array);
                    }
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                try {
                    String aCache_key = "notice_list";//缓存key
                   JSONObject jsonObject =  aCache.getAsJSONObject(aCache_key);
                   if(jsonObject!=null)
                   {
                       JSONArray array = jsonObject.getJSONArray("list");
                       bindNotices(array);
                   }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        //3.添加请求队列
        jsonObjectRequest.setTag("get_notice");
        InitQueue.getHttpQueue().add(jsonObjectRequest);
    }

    protected void bindNotices(JSONArray array) {
        mViewFlipper.removeAllViews();
        int i = 0;
        while (i < array.length()) {
            final TextView textview = new TextView(this);
            try {
                textview.setText(array.getJSONObject(i).getString("text"));
                textview.setTag(array.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            textview.setTextColor(this.getResources().getColor(R.color.colorNotice));
            textview.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);//设置25DIP
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            textview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JSONObject json = (JSONObject) textview.getTag();
                    Intent intent = new Intent(Main.this, Detail.class);
                    intent.putExtra("type", "2");
                    try {
                        intent.putExtra("detailId", json.getInt("id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                }
            });
            mViewFlipper.addView(textview, layoutParams);
            i++;

        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                intent = new Intent(Main.this, List.class);
                intent.putExtra("type", "");
                startActivity(intent);
                break;
            case R.id.button2:
                intent = new Intent(Main.this, List.class);
                intent.putExtra("type", "1");
                startActivity(intent);
                break;
            case R.id.button3:
                intent = new Intent(Main.this, List.class);
                intent.putExtra("type", "2");
                startActivity(intent);
                break;
            case R.id.button4:
                intent = new Intent(Main.this, List.class);
                intent.putExtra("type", "3");
                startActivity(intent);
                break;
            case R.id.button5:
                intent = new Intent(Main.this, Schedule.class);
                //intent.putExtra("type","4");
                startActivity(intent);
                break;
            case R.id.button6:
                intent = new Intent(Main.this, List.class);
                intent.putExtra("type", "4");
                startActivity(intent);
                break;
            case R.id.button7:
                intent = new Intent(Main.this, List.class);
                intent.putExtra("type", "5");
                startActivity(intent);
                break;
            case R.id.button8:
                intent = new Intent(Main.this, List.class);
                intent.putExtra("type", "6");
                startActivity(intent);
                break;
            case R.id.button9:
                intent = new Intent(Main.this, Manage.class);
                //intent.putExtra("type","7");
                startActivity(intent);
                break;
            case R.id.button10:
                intent = new Intent(Main.this, List.class);
                intent.putExtra("type", "8");
                startActivity(intent);
                break;
        }
    }

    /**
     * 检测版本更新
     *
     * @param context
     * @param isForceCheck 是否强制检测更新
     *                     true强制 - 无论什么网络环境都会提示更新
     *                     false非强制 - WiFi情况下才提示更新
     *                     newVersionCode- 后台请求的版本号
     *                     updataInfo- 更新的内容
     */
    public void checkUpdate(final Context context, final boolean isForceCheck, int newVersionCode, String updataInfo) {
        if (!NetUtils.isConnected(context)) {
            // 无网络时
            if (isForceCheck) {
                // 手动强制检测更新时，提示文字
                Toast.makeText(Main.this, "请检查网络连接", Toast.LENGTH_LONG).show();
            } else {
                // 非强制不做操作
            }
            return;
        }
        // 开始检测更新
        if (newVersionCode > Tools.getAppVersionCode(context)) {
            showUpdateConfirmDialog(context, updataInfo);
        }
    }

    /**
     * 显示更新对话框,包含版本相关信息
     */
    private void showUpdateConfirmDialog(final Context context, final String updateInfo) {
        final View view = View.inflate(this, R.layout.update_dialog, null);
        View dialog_sure = view.findViewById(R.id.dialog_sure);
        // final Dialog dialog = new Dialog(context,R.style.MyDialog);
        dialog.setContentView(view);
        WindowManager windowManager = ((Activity) context).getWindowManager();
        final WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        Display display = windowManager.getDefaultDisplay();
        //params.width = display.getWidth()*1/3;
        params.height = display.getHeight()*3/4;
        dialog.getWindow().setAttributes(params);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        dialog_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetUtils.isWifi(context)) {
                    downLoadApp(context);
                    Toast.makeText(Main.this, "已进入后台更新", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                } else {
                    new AlertDialog.Builder(context).setTitle("友情提醒").setIcon(R.mipmap.waring).setMessage("您当前使用的移动网络，是否继续更新。").setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            downLoadApp(context);
                            Toast.makeText(Main.this, "已进入后台更新", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                    }).setNegativeButton("否", null).show();
                }

            }
        });
    }

    /**
     * 下载文件 * @param context
     */
    public static void downLoadApp(Context context) {
        //downurl:下载app的后台地址
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse("http://223.2.197.91/download/apk/wisdomroom.apk"));
        // 下载时的网络状态，默认是wifi和移动网络都可以下载，如果选择一个，只能在选中的状态下进行下载
        //request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setTitle("app更新");
        request.setDescription("app正在下载");
        request.setAllowedOverRoaming(false);
        //设置文件存放目录
        // 判断文件是否存在，保证其唯一性
        File file = context.getExternalFilesDir("Download/wisdomroom.apk");
        assert file != null;
        if (file.exists()) {
            file.delete();
        }
        request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, "downApp");
        DownloadManager downManager = (DownloadManager) context.getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
        long id = downManager.enqueue(request);
        // 存储下载Key
        SharedPreferences sharedPreferences = context.getSharedPreferences("downloadApp", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("downloadId", id);
        editor.apply();
    }

    /**
     * 初始化app更新广播
     */
    private void initUpdata() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        filter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED);
        filter.addCategory("android.intent.category.DEFAULT");
        receiver = new DownLoadCompleteReceiver();
        registerReceiver(receiver, filter);
    }

    public class DownLoadCompleteReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                installApk(context, id);
            } else if (intent.getAction().equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)) {
            }
        }

        /**
         * 下载完后安装apk * * @param
         */
        // 安装Apk
        private void installApk(Context context, long downloadApkId) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("downloadApp", Activity.MODE_PRIVATE);
            long id = sharedPreferences.getLong("downloadId", 0);
            // 获取存储ID
            if (downloadApkId == id) {
                DownloadManager dManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                Uri downloadFileUri = dManager.getUriForDownloadedFile(downloadApkId);
                if (downloadFileUri != null) {
                    Intent install = new Intent(Intent.ACTION_VIEW);
                    File apkFile = context.getExternalFilesDir("Download/downApp");
                    //对Android 版本判断
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        install.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        // context.getPackageName() + ".fileprovider" 是配置中的authorities
                        Uri contentUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", apkFile);
                        install.setDataAndType(contentUri, "application/vnd.android.package-archive");
                    } else {
                        install.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                    }
                    install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(install);
                } else {
                    Toast.makeText(Main.this, "下载失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void showSettingDialog()

    {
        View view = View.inflate(this, R.layout.setting_dialog, null);
        new AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }
}
