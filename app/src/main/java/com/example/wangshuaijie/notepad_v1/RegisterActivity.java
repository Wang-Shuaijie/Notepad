package com.example.wangshuaijie.notepad_v1;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import DB.UserDateBaseHelper;



/**
 * Created by WangShuaiJie on 2018/4/21.
 */

public class RegisterActivity extends Activity {
    private EditText name;
    private EditText password;
    private EditText re_password;
    private Button register;
    private CardView cvAdd;
    private FloatingActionButton fab;

    private UserDateBaseHelper DBHelper;
    private SQLiteDatabase db;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name=(EditText) findViewById(R.id.id_username);
        password=(EditText)findViewById(R.id.id_password);
        re_password=(EditText)findViewById(R.id.id_repeatpassword);
        register=(Button)findViewById(R.id.button_next);

        DBHelper=new UserDateBaseHelper(this);
        db = DBHelper.getWritableDatabase();
        register.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String username = name.getText().toString();
               String pwd = password.getText().toString();
               String re_pwd = re_password.getText().toString();
               if (username == null || username.equals("") || pwd.equals("") || pwd == null || re_pwd.equals("") || re_pwd == null) {
                   Toast.makeText(RegisterActivity.this, "注册信息不能为空", Toast.LENGTH_SHORT).show();
               } else {
                   if (!pwd.equals(re_pwd)) {
                       Toast.makeText(RegisterActivity.this, "前后密码不同，请重新输入", Toast.LENGTH_SHORT).show();
                   } else if (isCheck()) {
                       Toast.makeText(RegisterActivity.this, "该用户已存在！", Toast.LENGTH_SHORT).show();
                   } else {

                       //向数据库添加信息
                       ContentValues values = new ContentValues();
                       values.put("username", username);
                       values.put("password", pwd);
                       values.put("phonenumber","000");
                       values.put("picture", img(R.drawable.p1));
                       db.insert("user", null, values);
                       finish();
                       Intent intent = new Intent();
                       intent.setClass(RegisterActivity.this, LoginActivity.class);
                       RegisterActivity.this.startActivity(intent);
                   }
               }
           }
           private boolean isCheck() {
               String username = name.getText().toString();
               String sql = "select * from user where username=?";
               Cursor cursor = db.rawQuery(sql, new String[]{username});
               if (cursor.getCount() > 0) {
                   cursor.close();
                   return true;
               } else {
                   return false;
               }
           }
       });


        cvAdd=(CardView)findViewById(R.id.cardView_add);
        fab=(FloatingActionButton)findViewById(R.id.floatingActionButton2);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ShowEnterAnimation();
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateRevealClose();
            }

        });
    }

    private void ShowEnterAnimation() {
        Transition transition= TransitionInflater.from(RegisterActivity.this).inflateTransition(R.transition.fabtransition);
        getWindow().setSharedElementEnterTransition(transition);

        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                cvAdd.setVisibility(View.GONE);
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                animateRevealShow();
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });
    }

    private void animateRevealShow() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd, cvAdd.getWidth()/2,0, fab.getWidth() / 2, cvAdd.getHeight());
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                cvAdd.setVisibility(View.VISIBLE);
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    private void animateRevealClose() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd,cvAdd.getWidth()/2,0, cvAdd.getHeight(), fab.getWidth() / 2);
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                cvAdd.setVisibility(View.INVISIBLE);
                super.onAnimationEnd(animation);
                fab.setImageResource(R.drawable.plus);
                RegisterActivity.super.onBackPressed();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    @Override
    public void onBackPressed() {
        animateRevealClose();
    }

    public byte[] img(int id)
{
              ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(id)).getBitmap();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            return baos.toByteArray();
        }


}

