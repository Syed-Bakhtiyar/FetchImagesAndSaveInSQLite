package bilal.com.apiparseandsaveinsqlite.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by BILAL on 8/15/2017.
 */

public class SQLiteDB extends SQLiteOpenHelper {

    public static final String DB_NAME = "Mydb.db";

    public static final String TABLE_NAME = "IMAGES";

    public static final String _ID = "_ID";

    public static final String LINK = "LINK";

    public static final String CREATEIMAGE_TABLE = "CREATE TABLE "+TABLE_NAME+" ( "+_ID+" INTEGER PRIMARY KEY AUTO INCREMENT , "+LINK+" TEXT)";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME;

    //--------------------------------------------------------------------------------------------------------

    Context context;

    SQLiteDatabase sqLiteDatabase;

    ContentValues contentValues;

    public SQLiteDB(Context context) {
        super(context, DB_NAME, null, 1);

        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        try {

            sqLiteDatabase.execSQL(CREATEIMAGE_TABLE);
        }catch (Exception e){

            Log.d("create_table_exception", "onCreate: "+e);

        }

    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL(DROP_TABLE);

    }


    public boolean save(String link){

        try {

            sqLiteDatabase = getWritableDatabase();

            contentValues = new ContentValues();

            contentValues.put(LINK, link);

            long check = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);


            if (check == -1) {


                return false;

            } else {

                Log.d("row_id", "save: " + check);

                return true;

            }
        }catch (Exception e){


            Log.d("insertion", "save: "+e);

            return false;

        }

    }



}
