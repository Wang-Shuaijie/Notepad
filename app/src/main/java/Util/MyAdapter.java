package Util;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.wangshuaijie.notepad_v1.LoginActivity;
import com.example.wangshuaijie.notepad_v1.MainActivity;
import com.example.wangshuaijie.notepad_v1.R;
import com.example.wangshuaijie.notepad_v1.VideoActivity;


public class MyAdapter extends BaseAdapter {

    private Context context;
    private Cursor cursor;
    private LinearLayout layout;
    private MediaController mediaController;

    public MyAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public Object getItem(int i) {
        return cursor.getPosition();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        layout = (LinearLayout) inflater.inflate(R.layout.cell,null);
        TextView tv_content = (TextView) layout.findViewById(R.id.tv_content);
        TextView tv_date = (TextView) layout.findViewById(R.id.tv_date);
        ImageView img = (ImageView) layout.findViewById(R.id.img);
        Button startVideo=(Button)layout.findViewById(R.id.startVideo);

        cursor.moveToPosition(i);
        String content=cursor.getString(cursor.getColumnIndex("content"));
        String date=cursor.getString(cursor.getColumnIndex("date"));
        String path=cursor.getString(cursor.getColumnIndex("path"));
        final String videoPath=cursor.getString(cursor.getColumnIndex("videoPath"));
        tv_content.setText(content);
        tv_date.setText(date);
        if(path!=null) {
            Bitmap bm = BitmapFactory.decodeFile(path);
            img.setImageBitmap(bm);
        }else {
            img.setVisibility(View.GONE);
        }
        startVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(videoPath==null){
                    Toast.makeText(context,"还没有添加视频噢~~",Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent=new Intent(context,VideoActivity.class);
                    intent.putExtra("videoPath",videoPath);
                    context.startActivity(intent);
                }
            }
        });

        return layout;
    }
}
