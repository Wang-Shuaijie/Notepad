package DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by WangShuaiJie on 2018/4/14.
 */

public class NoteDateBaseHelper extends SQLiteOpenHelper {
    public static final String sql = "create table note ("
            + "user varchar(255),"
            + "content text,"
            + "path text,"
            + "videoPath text,"
            + "date text)";

    public NoteDateBaseHelper(Context context) {
        super(context, "note", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
       sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
