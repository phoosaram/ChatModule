package test.demo.com.chatmodule.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import test.demo.com.chatmodule.dao.ChatDetailData;
import test.demo.com.chatmodule.dao.UsersData;

/**
 * Created by Admin on 3/6/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ";

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createUsersTable(sqLiteDatabase);
        createChatHistoryTable(sqLiteDatabase);
    }

    private void createChatHistoryTable(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE
                + DBConstants.CHAT_HISTORY_TABLE_NAME + "(" + UsersData.FLD_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
                + ChatDetailData.FLD_USER_FROM + " TEXT NOT NULL, "
                + ChatDetailData.FLD_USER_TO + " TEXT NOT NULL,"
                + ChatDetailData.FLD_MSG_ID + " TEXT UNIQUE ON CONFLICT REPLACE,"
                + ChatDetailData.FLD_TYPE + " TEXT,"
                + ChatDetailData.FLD_MESSAGE + " TEXT,"
                + ChatDetailData.FLD_EXTENSION + " TEXT,"
                + ChatDetailData.FLD_FIRST_NAME + " TEXT,"
                + ChatDetailData.FLD_RECEIVED + " TEXT,"
                + ChatDetailData.FLD_IS_LIKE + " TEXT,"
                + ChatDetailData.FLD_ATTACHMENT + " TEXT,"
                + ChatDetailData.FLD_DATE + " TEXT,"
                + ChatDetailData.FLD_TIME + " TEXT,"
                + ChatDetailData.FLD_ATTACHMENT_FILE_NAME + " TEXT,"
                + ChatDetailData.FLD_ATTACHMENT_DOWNLOAD_PATH + " TEXT);");
        sqLiteDatabase.execSQL("CREATE INDEX IF NOT EXISTS user_thread_index on " + DBConstants.CHAT_HISTORY_TABLE_NAME + "(" + ChatDetailData.FLD_USER_FROM + "," + ChatDetailData.FLD_USER_TO + ");");
    }

    /**
     * method to create usersTable in the database
     *
     * @param sqLiteDatabase
     */
    private void createUsersTable(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE
                + DBConstants.USERS_TABLE_NAME + "(" + UsersData.FLD_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
                + UsersData.FLD_USER_ID + " TEXT NOT NULL UNIQUE ON CONFLICT REPLACE,"
                + UsersData.FLD_PROFILE_PIC_URL + " TEXT,"
                + UsersData.FLD_DISPLAY_NAME + " TEXT,"
                + UsersData.FLD_USER_FROM + " TEXT,"
                + UsersData.FLD_MSG_ID + " TEXT,"
                + UsersData.FLD_TYPE + " TEXT,"
                + UsersData.FLD_MESSAGE + " TEXT,"
                + UsersData.FLD_DATE + " TEXT,"
                + UsersData.FLD_TIME + " TEXT);");
        sqLiteDatabase.execSQL("CREATE INDEX IF NOT EXISTS user_id_index on " + DBConstants.USERS_TABLE_NAME + "(" + UsersData.FLD_USER_ID + ");");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // your code here to migrate the data
    }
}
