package com.example.wangshuaijie.notepad_v1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import DB.NoteDateBaseHelper;
import Util.MyAdapter;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener{
    private ListView listview;
    private SimpleAdapter simple_adapter;
    private List<Map<String, Object>> dataList;
    private Button addNote;
    private TextView tv_content;
    private NoteDateBaseHelper DbHelper;
    private SQLiteDatabase DB;

    private String user;//获得登录用户
    private String pass;
    private String path;//图片路径
    private String videoPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获得登录用户
        Bundle bundle = this.getIntent().getExtras();
        user=bundle.getString("user");
        pass=bundle.getString("pass");
        //初始化
        InitView();
    }

    //在activity显示的时候更新listview
    @Override
    protected void onStart() {
        super.onStart();
        RefreshNotesList();
    }

    private void InitView() {
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        DrawerLayout drawer=(DrawerLayout)findViewById(R.id.drawer_layout) ;
        tv_content = (TextView) findViewById(R.id.tv_content);
        listview = (ListView) findViewById(R.id.listview);
        dataList = new ArrayList<Map<String, Object>>();
        addNote = (Button) findViewById(R.id.btn_editnote);
        DbHelper = new NoteDateBaseHelper(this);
        DB = DbHelper.getReadableDatabase();

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);//设置导航图标
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this, drawer, toolbar, 0, 0);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView=(NavigationView)findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_exit:
                        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this)
                                .setTitle("退出")
                                .setMessage("确定退出吗？");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                System.exit(0);

                            }
                        });
                        builder.setNegativeButton("取消",null);
                        builder.create().show();
                        break;
                    case R.id.nav_about:
                        Intent intent0=new Intent(MainActivity.this,AboutActivity.class);
                        startActivity(intent0);

                        break;
                    case R.id.nav_message:
                        Intent intent = new Intent(MainActivity.this,InformationActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("user",user);
                        intent.putExtras(bundle);
                        startActivity(intent);

                        break;
                    case R.id.nav_setting:
                        Toast.makeText(getApplicationContext(),"测试版，无此功能噢~~等待添加",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_more:
                        Intent intent1=new Intent();
                        String data="http://note.youdao.com/semdl/todolist.html?semType=1&vendor=unsilent46";
                        Uri uri=Uri.parse(data);
                        intent1.setAction(Intent.ACTION_VIEW);
                        intent1.setData(uri);
                        startActivity(intent1);

                        break;
                }
                return true;
            }
        });

        listview.setOnItemClickListener(this);
        listview.setOnItemLongClickListener(this);
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,EditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("user",user);
                bundle.putString("info", "");
                bundle.putInt("enter_state", 0);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    //刷新listview
    public void RefreshNotesList() {
        //如果dataList已经有的内容，全部删掉
        //并且更新simp_adapter
        int size = dataList.size();
        if (size > 0) {
            dataList.removeAll(dataList);
            simple_adapter.notifyDataSetChanged();
        }

        //从数据库读取信息
        Cursor cursor = DB.query("note", null, "user=?", new String[]{user}, null, null, null);
        while (cursor.moveToNext()) {
            path=cursor.getString(cursor.getColumnIndex("path"));
            videoPath=cursor.getString(cursor.getColumnIndex("videoPath"));
        }

        MyAdapter adapter=new MyAdapter(this,cursor);
        listview.setAdapter(adapter);
    }



    // 点击listview中某一项的点击监听事件
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        //获取listview中此个item中的内容
        TextView text=(TextView) arg1.findViewById(R.id.tv_content);
        String content=text.getText().toString();
        Intent myIntent = new Intent(MainActivity.this, EditActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("user",user);
        bundle.putString("info", content);
        bundle.putString("path", path);
        bundle.putString("videoPath",videoPath);
        bundle.putInt("enter_state", 1);
        myIntent.putExtras(bundle);
        startActivity(myIntent);

    }

    // 点击listview中某一项长时间的点击事件
    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, final View arg1, int arg2,
                                   long arg3) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("删除该日志");
        builder.setMessage("确认删除吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                 //获取listview中此个item中的内容
                //删除该行后刷新listview的内容
                TextView text=(TextView) arg1.findViewById(R.id.tv_content);
                String content=text.getText().toString();
                DB.delete("note", "content = ?", new String[]{content});
                RefreshNotesList();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create();
        builder.show();
        return true;
    }


}
