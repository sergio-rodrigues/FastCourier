package pt.srodrigues.srod.fastcourier.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    public class Columns {
        public static final String TASK = "task";
        public static final String _ID = "_id";
    }

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "pt.srodrigues.fastcourier.db.tasks";
    public static final String TABLE = "tasks";

    public static final String DELETE_FROM = "DELETE FROM %s WHERE %s = '%s'";
    public static final String CREATE_TABLE = "CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT)";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS %s";


    public void addTask(final String task) {
        final SQLiteDatabase db = getWritableDatabase();
        final ContentValues values = new ContentValues();
        values.clear();
        values.put(Columns.TASK, task);
        db.insertWithOnConflict(TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public void delete(final String task) {
        final String sql = String.format(DELETE_FROM, TABLE, Columns.TASK, task);
        final SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sql);
    }

    public DBHelper(final Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqlDB) {
        final String sqlQuery = String.format(CREATE_TABLE, TABLE, Columns._ID, Columns.TASK);
        Log.d("DBHelper", "Query to form table: " + sqlQuery);
        sqlDB.execSQL(sqlQuery);
        fill(sqlDB);
    }

    private void fill(final SQLiteDatabase sqlDB) {
        final ContentValues values = new ContentValues();
        values.clear();
        values.put(Columns.TASK, "batatas");
        sqlDB.insertWithOnConflict(TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        values.put(Columns.TASK, "couves");
        sqlDB.insertWithOnConflict(TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        values.put(Columns.TASK, "cebolas");
        sqlDB.insertWithOnConflict(TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        values.put(Columns.TASK, "cenouras");
        sqlDB.insertWithOnConflict(TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqlDB, int i, int i2) {
        String sql = String.format(DROP_TABLE, TABLE);
        sqlDB.execSQL(sql);
        onCreate(sqlDB);
    }
}