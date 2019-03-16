package com.example.wangshuaijie.notepad_v1;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DB.UserDateBaseHelper;

/**
 * Created by Administrator on 2018/5/28/028.
 */

public class SelectPictureActivity extends AppCompatActivity {
    private String username;
    GridView gridView;
    private int[] pictures = new int[]{R.drawable.p1, R.drawable.p2,R.drawable.p3,R.drawable.p4,R.drawable.p5,R.drawable.p6,
            R.drawable.p7,R.drawable.p8,R.drawable.p9,R.drawable.p10};
    private UserDateBaseHelper DbHelper;
    private SQLiteDatabase DB;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        DbHelper = new UserDateBaseHelper(this);
        DB = DbHelper.getReadableDatabase();
        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        for(int i=0;i<pictures.length;i++) {
            Map<String, Object> listItem = new HashMap<String, Object>();
            listItem.put("image", pictures[i]);
            listItems.add(listItem);
        }

            SimpleAdapter simpleAdapter=new SimpleAdapter(this,
                    listItems,
                    R.layout.cell_picture,
                    new String[]{"image"},
                    new int[]{R.id.imageView2});
            gridView=(GridView)findViewById(R.id.gridView);
            gridView.setAdapter(simpleAdapter);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    username=getIntent().getStringExtra("username");
                    ContentValues values=new ContentValues();
                    values.put("picture",img(pictures[position]));
                    DB.update("user",values,"username=?",new String[]{username});

                    SelectPictureActivity.this.finish();
                }
            });


}



    public byte[] img(int id)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(id)).getBitmap();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
}
