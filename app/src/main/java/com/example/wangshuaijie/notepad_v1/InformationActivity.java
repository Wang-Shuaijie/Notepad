package com.example.wangshuaijie.notepad_v1;

import android.app.assist.AssistContent;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import DB.UserDateBaseHelper;

/**
 * Created by WangShuaiJie on 2018/5/1.
 */

public class InformationActivity extends AppCompatActivity {
    private String user;
    private String pass;
    private String phonenumber;
    private Button save;
    private ImageView h_head;
    private UserDateBaseHelper DbHelper;
    private SQLiteDatabase DB;
    private Bitmap bmpout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        Bundle bundle = this.getIntent().getExtras();
        user=bundle.getString("user");
        DbHelper = new UserDateBaseHelper(this);
        DB = DbHelper.getReadableDatabase();
         Cursor cursor= DB.query("user", new String[]{"password", "phonenumber"},"username=?",new String[]{user},null,null,null);
         while (cursor.moveToNext()){
             pass=cursor.getString(cursor.getColumnIndex("password"));
             phonenumber=cursor.getString(cursor.getColumnIndex("phonenumber"));
         }

        final EditText et1=(EditText)findViewById(R.id.user_EditText);
        final EditText et2=(EditText)findViewById(R.id.pass_EditText);
        final EditText et3=(EditText)findViewById(R.id.phone_EditText);
        et1.setText(user);
        et2.setText(pass);
        et3.setText(phonenumber);

        et2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et2.setEnabled(true);
                et2.setFocusable(true);
                et2.setText("");
                et2.setHint("请输入新密码");

            }
        });

        et3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et3.setEnabled(true);
                et3.setFocusable(true);
                et3.setText("");
                et3.setHint("请输入新手机号");

            }
        });

        save=(Button)findViewById(R.id.baocun);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbHelper = new UserDateBaseHelper(InformationActivity.this);
                DB = DbHelper.getWritableDatabase();

                ContentValues values=new ContentValues();
                values.put("password",et2.getText().toString());
                values.put("phonenumber",et3.getText().toString());
                DB.update("user",values,"username=?",new String[]{InformationActivity.this.user});
                Toast.makeText(InformationActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
            }
        });
        h_head=(ImageView) findViewById(R.id.h_head);

        Cursor cursor1 = DB.query("user", new String[]{"picture"},
                "username=?", new String[]{this.user}, null, null, null);
        while (cursor.moveToNext()) {
            byte[] in = cursor1.getBlob(cursor.getColumnIndex("picture"));
            bmpout = BitmapFactory.decodeByteArray(in, 0, in.length);
        }

        h_head.setImageBitmap(bmpout);
         h_head.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Intent intent=new Intent(InformationActivity.this,SelectPictureActivity.class);
             intent.putExtra("username", InformationActivity.this.user);
             startActivity(intent);
         }
     });

    }

    @Override
    protected void onStart() {
        super.onStart();
        h_head=(ImageView) findViewById(R.id.h_head);
        DbHelper = new UserDateBaseHelper(this);
        DB = DbHelper.getReadableDatabase();
        Cursor cursor = DB.query("user", new String[]{"picture"},
                "username=?", new String[]{user}, null, null, null);
        while (cursor.moveToNext()) {
            byte[] in = cursor.getBlob(cursor.getColumnIndex("picture"));
            bmpout = BitmapFactory.decodeByteArray(in, 0, in.length);
        }

        h_head.setImageBitmap(bmpout);
    }
}
