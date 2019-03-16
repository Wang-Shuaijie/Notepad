package com.example.wangshuaijie.notepad_v1;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import DB.UserDateBaseHelper;

public class LoginActivity extends Activity {
    private EditText name;
    private EditText password;
    private Button login;
    private FloatingActionButton fab;
    private UserDateBaseHelper DBHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        name=(EditText)findViewById(R.id.id_username);
        password=(EditText)findViewById(R.id.id_password);
        login=(Button)findViewById(R.id.button_go);

        DBHelper=new UserDateBaseHelper(this);
        db = DBHelper.getReadableDatabase();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = name.getText().toString();
                String pwd = password.getText().toString();
                if (username == null || username.equals("") || pwd == null || pwd.equals("")) {
                    Toast.makeText(LoginActivity.this, "用户名或密码不能为空！", Toast.LENGTH_SHORT).show();
                }else{
                    String sql = "select * from user where username=? and password=?";
                    String[] data=new String[]{username,pwd};
                    Cursor cursor = db.rawQuery(sql, data);
                    if (cursor.getCount() > 0){
                        cursor.close();
                        Intent intent = new Intent();
                        intent.setClass(LoginActivity.this, MainActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("user", username);
                        intent.putExtras(bundle);
                        LoginActivity.this.startActivity(intent);
                    }else{
                        Toast.makeText(LoginActivity.this, "没有该用户！", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });


        fab=(FloatingActionButton)findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWindow().setExitTransition(null);
                getWindow().setEnterTransition(null);

                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){
                    ActivityOptions options= ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this,fab,fab.getTransitionName());
                    startActivity(new Intent(LoginActivity.this,RegisterActivity.class),options.toBundle());
                }else{
                    startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                }
            }
        });
    }
}