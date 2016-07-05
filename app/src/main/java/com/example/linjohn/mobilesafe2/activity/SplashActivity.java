package com.example.linjohn.mobilesafe2.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.linjohn.mobilesafe2.R;
import com.example.linjohn.mobilesafe2.utils.StreamUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SplashActivity extends AppCompatActivity {
    private static final int MSG_CONNECTION_SUCCESS = 0;
    private static final int MSG_CONNECTION_FAILURE = 1;
    private static final int MSG_URL_ERROR = 2;
    private static final int MSG_IO_ERROR = 3;
    private static final int MSG_JSON_ERROR = 4;
    private long startTimeMills;
    private TextView tv_splash_versionname;
    private TextView tv_splash_progress;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case MSG_CONNECTION_SUCCESS:
                    showVersionInfo(msg);
                    break;
                case MSG_CONNECTION_FAILURE:
                    enterHome();
                    break;
                case  MSG_URL_ERROR:
                    Toast.makeText(getApplicationContext(),"Server error",Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case MSG_IO_ERROR:
                    Toast.makeText(getApplicationContext(),"IO error",Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case MSG_JSON_ERROR:
                    Toast.makeText(getApplicationContext(),"Json error",Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
            }

        }
    };
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //
        tv_splash_versionname = (TextView) findViewById(R.id.tv_splash_versionname);

        //
        tv_splash_versionname.setText("版本号:" + getCurrentVersionName());

        //
        tv_splash_progress = (TextView) findViewById(R.id.tv_splash_progress);

        //
        sp = getSharedPreferences("config",MODE_PRIVATE);
        if (sp.getBoolean("update",true)) {

            update();

        } else {

            new Thread(){
                @Override
                public void run() {

                    SystemClock.sleep(2000);
                    enterHome();
                }
            }.start();
        }


        //
        startTimeMills = System.currentTimeMillis();

        //

    }

    private String getCurrentVersionName() {

        PackageManager pm = getPackageManager();
        try {
            String name = getPackageName();
            PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
            String versionname = info.versionName;
            return versionname;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return "";
    }

    private void update() {

        new Thread() {

            @Override
            public void run() {
                Message message = Message.obtain();
                try {
                    URL url = new URL("http://192.168.146.133/mobilsafe.html");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(10000);
                    connection.setRequestMethod("GET");

                    if (connection.getResponseCode() == 200) {

                        InputStream inputStream = connection.getInputStream();

                        String str = StreamUtil.parserStreamUtil(inputStream);

                        JSONObject jsonOBJ = new JSONObject(str);
                        //String currentVersionName = getCurrentVersionName();
                        message.obj = jsonOBJ;
                        message.what = MSG_CONNECTION_SUCCESS;
                        System.out.print("Connection Success!");

                    } else {

                        System.out.print("Connection Failure!");
                        message.what = MSG_CONNECTION_FAILURE;
                    }

                } catch (MalformedURLException e) {
                    message.what = MSG_URL_ERROR;
                    e.printStackTrace();
                } catch (IOException e) {
                    message.what = MSG_IO_ERROR;
                    e.printStackTrace();
                } catch (JSONException e) {
                    message.what = MSG_JSON_ERROR;
                    e.printStackTrace();

                } finally {
                    long endTimeMills = System.currentTimeMillis();
                    long duration = endTimeMills - startTimeMills;
                    if (duration < 2000) {
                        SystemClock.sleep(2000 - duration);
                    }
                    handler.sendMessage(message);

                }
            }
        }.start();
    }

    private void showVersionInfo(Message message) {

        try {
            final JSONObject obj = (JSONObject) message.obj;
            String versionname = obj.getString("versionName");
            final String apkurl = obj.getString("apkurl");
            String version = tv_splash_versionname.getText().toString().split(":")[1];
            if (versionname != tv_splash_versionname.getText().toString().split(":")[1]) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("版本提示");
                String msg = obj.getString("msg");
                dialog.setMessage("有新版本" + versionname + "\n" + msg);
                dialog.setPositiveButton("升级", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        download(apkurl);
                    }
                });

                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        enterHome();
                    }
                });

                dialog.show();

            } else  {

                enterHome();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private  void  download(String apkurl) {

        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        System.out.print(path);
       if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

           final String apkPath = path + "/mobilesafe2.apk";
           HttpUtils http = new HttpUtils();

           HttpHandler handler = http.download(apkurl,apkPath ,true ,true,new RequestCallBack<File>() {

               @Override
               public void onStart() {
                   System.out.print("download success");
               }

               @Override
               public void onSuccess(ResponseInfo<File> responseInfo) {
                   tv_splash_progress.setVisibility(View.INVISIBLE);
                   installAPK(apkPath);
               }

               @Override
               public void onFailure(HttpException error, String msg) {
                    System.out.print("Failure");
               }

               @Override
               public void onLoading(long total, long current, boolean isUploading) {
                   tv_splash_progress.setVisibility(View.VISIBLE);
                   tv_splash_progress.setText(current + "/" + total);
               }
           });

       }

    }

    private void installAPK(String apkPath) {

        Intent intent = new Intent();
        intent.setAction("android.intent.action.INSTALL_PACKAGE");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setDataAndType(Uri.fromFile(new File(apkPath)),"application/vnd.android.package-archive");
        startActivity(intent);
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        enterHome();
    }

    private void enterHome() {

        Intent intent = new Intent();
        intent.setClass(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
