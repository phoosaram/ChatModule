package test.demo.com.chatmodule.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import test.demo.com.chatmodule.dao.ChatDetailData;
import test.demo.com.chatmodule.dao.ChatMessageData;
import test.demo.com.chatmodule.dao.LastMessageData;
import test.demo.com.chatmodule.dao.UserInfoData;
import test.demo.com.chatmodule.dao.UsersData;

/**
 * Created by Admin on 3/6/2017.
 */

public class DatabaseManager {
    private static DatabaseManager databaseManager;
    private static SQLiteDatabase writableDatabase;
    private static SQLiteDatabase readableDatabase;

    private DatabaseManager() {

    }

    /**
     * creates an singleton instance
     *
     * @param context
     * @return
     * @throws Exception
     */
    public static DatabaseManager getInstance(Context context) throws Exception {
        if (databaseManager == null) {
            databaseManager = new DatabaseManager();
            DatabaseHelper databaseHelper = new DatabaseHelper(context, DBConstants.DB_NAME, null, DBConstants.DB_VERSION);
            writableDatabase = databaseHelper.getWritableDatabase();
            readableDatabase = databaseHelper.getReadableDatabase();
        }
        return databaseManager;
    }

    /**
     * method to insert multiple rows into the specified table
     *
     * @param tableName     table name i.e. app_users
     * @param contentValues
     * @return
     */
    private int insertRows(String tableName, ContentValues[] contentValues) {
        int insertedRows = 0;
        if (contentValues == null) {
            return insertedRows;
        }
        try {
            writableDatabase.beginTransaction();
            for (ContentValues cvs : contentValues) {
                writableDatabase.insertWithOnConflict(tableName, null, cvs, SQLiteDatabase.CONFLICT_REPLACE);
                insertedRows++;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (writableDatabase != null) {
                writableDatabase.setTransactionSuccessful();
                writableDatabase.endTransaction();
            }
        }
        return insertedRows;
    }

    /**
     * method to insert values in the specified table
     *
     * @param tableName
     * @param contentValues
     * @return returns id of last row inserted
     */
    private int insertRow(String tableName, ContentValues contentValues) throws Exception {
        int insertedRows = 0;
        if (contentValues == null) {
            return insertedRows;
        }
        try {
            writableDatabase.beginTransaction();
            insertedRows = (int) writableDatabase.insertWithOnConflict(tableName, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (writableDatabase != null) {
                writableDatabase.setTransactionSuccessful();
                writableDatabase.endTransaction();
            }
        }
        return insertedRows;
    }

    private int updateRow(String tableName, ContentValues values, String whereClause, String[] whereArgs) {
        int result = -1;
        try {
            writableDatabase.beginTransaction();
            result = writableDatabase.update(tableName, values, whereClause, whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writableDatabase != null) {
                writableDatabase.setTransactionSuccessful();
                writableDatabase.endTransaction();
            }
        }
        return result;
    }

    public boolean clearTable(String tableName) {
        try {
            writableDatabase.execSQL("delete from " + tableName);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writableDatabase != null) {
                writableDatabase.setTransactionSuccessful();
                writableDatabase.endTransaction();
            }
        }
        return false;
    }

    public boolean insertDataToChatHistoryTable(ArrayList<ChatMessageData> messageList) {
        try {
            if (messageList != null && !messageList.isEmpty()) {
                ContentValues[] cvs = new ContentValues[messageList.size()];
                for (int i = 0; i < messageList.size(); i++) {
                    cvs[i] = getContentValuesForChatMessage(messageList.get(i));
                }
                insertRows(DBConstants.CHAT_HISTORY_TABLE_NAME, cvs);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private ContentValues getContentValuesForChatMessage(ChatMessageData chatMessageData) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ChatDetailData.FLD_USER_FROM, chatMessageData.user_from);
        contentValues.put(ChatDetailData.FLD_USER_TO, chatMessageData.user_to);
        contentValues.put(ChatDetailData.FLD_MSG_ID, chatMessageData.msg_id);
        contentValues.put(ChatDetailData.FLD_TYPE, chatMessageData.type);
        contentValues.put(ChatDetailData.FLD_MESSAGE, chatMessageData.message);
        contentValues.put(ChatDetailData.FLD_IS_LIKE, chatMessageData.is_like);
        contentValues.put(ChatDetailData.FLD_DATE, chatMessageData.date);
        contentValues.put(ChatDetailData.FLD_TIME, chatMessageData.time);
        contentValues.put(ChatDetailData.FLD_RECEIVED, chatMessageData.received);
        contentValues.put(ChatDetailData.FLD_FIRST_NAME, chatMessageData.first_name);
        return contentValues;
    }

    public boolean insertDataToUsersTable(ArrayList<UserInfoData> list) {
        try {
            if (list != null) {
                ContentValues[] contentValues = new ContentValues[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    contentValues[i] = getContentValueForUserInfoData(list.get(i));
                }
                insertRows(DBConstants.USERS_TABLE_NAME, contentValues);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private ContentValues getContentValueForUserInfoData(UserInfoData userInfoData) {
        if (userInfoData != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(UsersData.FLD_DISPLAY_NAME, userInfoData.username);
            contentValues.put(UsersData.FLD_PROFILE_PIC_URL, userInfoData.picture_url);
            contentValues.put(UsersData.FLD_USER_ID, userInfoData.user_to);
            if (userInfoData.last_message != null) {
                contentValues.put(UsersData.FLD_USER_FROM, userInfoData.last_message.user_from);
                contentValues.put(UsersData.FLD_MSG_ID, userInfoData.last_message.msg_id);
                contentValues.put(UsersData.FLD_TYPE, userInfoData.last_message.type);
                contentValues.put(UsersData.FLD_MESSAGE, userInfoData.last_message.message);
                contentValues.put(UsersData.FLD_DATE, userInfoData.last_message.date);
                contentValues.put(UsersData.FLD_TIME, userInfoData.last_message.time);
            }
            return contentValues;
        }
        return null;
    }

    public ArrayList<UserInfoData> fetchUsersList() {
        ArrayList<UserInfoData> arrayList = new ArrayList<UserInfoData>();
        try {
            Cursor cursor = readableDatabase.query(DBConstants.USERS_TABLE_NAME, null, null, null, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    UserInfoData userInfoData = new UserInfoData();
                    userInfoData.username = cursor.getString(cursor.getColumnIndex(UsersData.FLD_DISPLAY_NAME));
                    userInfoData.picture_url = cursor.getString(cursor.getColumnIndex(UsersData.FLD_PROFILE_PIC_URL));
                    userInfoData.user_to = cursor.getString(cursor.getColumnIndex(UsersData.FLD_USER_ID));
                    userInfoData.last_message = new LastMessageData();
                    userInfoData.last_message.user_from = cursor.getString(cursor.getColumnIndex(UsersData.FLD_USER_FROM));
                    userInfoData.last_message.message = cursor.getString(cursor.getColumnIndex(UsersData.FLD_MESSAGE));
                    userInfoData.last_message.msg_id = cursor.getString(cursor.getColumnIndex(UsersData.FLD_MSG_ID));
                    userInfoData.last_message.type = cursor.getString(cursor.getColumnIndex(UsersData.FLD_TYPE));
                    userInfoData.last_message.date = cursor.getString(cursor.getColumnIndex(UsersData.FLD_DATE));
                    userInfoData.last_message.time = cursor.getString(cursor.getColumnIndex(UsersData.FLD_TIME));
                    arrayList.add(userInfoData);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }
}
