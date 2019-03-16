package DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by WangShuaiJie on 2018/4/21.
 */

public class UserDateBaseHelper extends SQLiteOpenHelper {
    public static final String sql = "create table user ("
            + "username varchar(255) not null, "
            + "password varchar(255),"
            + "phonenumber varchar(255),"
            +"picture BLOB)";

    public UserDateBaseHelper(Context context) {
        super(context, "user", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
