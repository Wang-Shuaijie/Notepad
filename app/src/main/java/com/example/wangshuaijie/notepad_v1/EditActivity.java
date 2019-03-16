package com.example.wangshuaijie.notepad_v1;


import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.util.Date;

import DB.NoteDateBaseHelper;
import Util.ImageUtils;

/**
 * Created by WangShuaiJie on 2018/4/14.
 */

public class EditActivity extends Activity implements OnClickListener {
    private TextView tv_date;
    private EditText et_content;
    private Button btn_image;
    private Button btn_video;
    private Button btn_ok;
    private Button btn_cancel;
    private ImageView img;
    private ImageView img2;
    private NoteDateBaseHelper DBHelper;
    public int enter_state = 0;//用来区分是新建一个note还是更改原来的note
    public String last_content;//用来获取edittext内容
    public String user;//登录用户
    private String path;
    private String videoPath;
     private String dateString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        InitView();
    }


    private void InitView() {
        tv_date = (TextView) findViewById(R.id.tv_date);
        btn_image=(Button)findViewById(R.id.btn_image);
        btn_video=(Button)findViewById(R.id.btn_video);
        et_content = (EditText) findViewById(R.id.et_content);
        img=(ImageView)findViewById(R.id.id_img);
        img2=(ImageView)findViewById(R.id.id_video);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        DBHelper = new NoteDateBaseHelper(this);

        //获取此时时刻时间
        Date date=new Date();
        SimpleDateFormat sdf = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            dateString = sdf.format(date);

        }
        tv_date.setText(dateString);


        //接收内容和user
        Bundle myBundle = this.getIntent().getExtras();
        last_content = myBundle.getString("info");
        path=myBundle.getString("path");
        videoPath=myBundle.getString("videoPath");
        enter_state = myBundle.getInt("enter_state");
        user=myBundle.getString("user");
        et_content.setText(last_content);

        if(path!=null){
            Bitmap bm = BitmapFactory.decodeFile(path);
            bm=ImageUtils.zoomImage(bm);
            img.setImageBitmap(bm);
        }

        btn_video.setOnClickListener(this);
        btn_image.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_ok.setOnClickListener(this);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_image:
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(EditActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0x10);
                }else{
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, 0x11);
                }
                break;
            case R.id.btn_ok:
                SQLiteDatabase db = DBHelper.getWritableDatabase();
                // 获取edittext内容
                String content = et_content.getText().toString();

                // 添加一个新的日志
                if (enter_state == 0) {
                    if (!content.equals("")) {
                        //获取此时时刻时间
                        Date date = new Date();
                        SimpleDateFormat sdf = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            dateString = sdf.format(date);
                        }


                        //向数据库添加信息
                        ContentValues values = new ContentValues();
                        values.put("user",user);
                        values.put("content", content);
                        values.put("path",path);
                        values.put("videoPath",videoPath);
                        values.put("date", dateString);
                        db.insert("note",null,values);
                        finish();
                    } else {
                        Toast.makeText(EditActivity.this, "请输入你的内容！", Toast.LENGTH_SHORT).show();
                    }
                }
                // 查看并修改一个已有的日志
                else {
                    ContentValues values = new ContentValues();
                    values.put("content", content);
                    db.update("note", values, "content = ?", new String[]{last_content});
                    finish();
                }
                break;
            case R.id.btn_video:
                Intent intent=new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
                break;
            case R.id.btn_cancel:
                finish();
                break;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode==0x10){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 0x11);
            }else{
                Toast.makeText(EditActivity.this, "没有存储权限无法打开图库", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == 0x11) {
            //取得数据
            Uri uri = data.getData();

            path=ImageUtils.getRealPathFromUri(this, uri);

            ContentResolver cr = EditActivity.this.getContentResolver();
            Bitmap bitmap = null;
            Bundle extras = null;

                //将对象存入Bitmap中
                try {
                    bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            bitmap=ImageUtils.zoomImage(bitmap);
            img.setImageBitmap(bitmap);
        }
        if(resultCode == RESULT_OK && requestCode == 0){
            Uri uri = data.getData();
            ContentResolver cr = this.getContentResolver();
            Cursor cursor = cr.query(uri, null, null, null, null);
            if(cursor.moveToNext()){
                int videoId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                // 视频名称：MediaStore.Audio.Media.TITLE
                String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                // 视频路径：MediaStore.Audio.Media.DATA
                videoPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                // 视频时长：MediaStore.Audio.Media.DURATION
                int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                // 视频大小：MediaStore.Audio.Media.SIZE
                long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));

                // 视频缩略图路径：MediaStore.Images.Media.DATA
                String imagePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                // 缩略图ID:MediaStore.Audio.Media._ID
                int imageId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
                Bitmap bitmap1 = MediaStore.Video.Thumbnails.getThumbnail(cr, imageId, MediaStore.Video.Thumbnails.MINI_KIND, null);
                img2.setImageBitmap(bitmap1);
            }
            cursor.close();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
