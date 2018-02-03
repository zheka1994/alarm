package com.example.eugen.alarmv1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class AlarmDbHelper extends SQLiteOpenHelper {
    private static final String NAME = "AlarmDb";
    private static final int VERSION = 1;
    public AlarmDbHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(MyDbContract.AlarmsTable.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MyDbContract.AlarmsTable.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
