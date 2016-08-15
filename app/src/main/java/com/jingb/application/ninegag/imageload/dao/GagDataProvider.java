package com.jingb.application.ninegag.imageload.dao;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.jingb.application.App;
import com.orhanobut.logger.Logger;

/**
 * Created by jingb on 16/7/12.
 */
public class GagDataProvider extends ContentProvider {

    private static UriMatcher matcher;
    private static DBHelper dbHelper;
    public static final int code = 1;
    /**
     * Implement this to handle requests for the MIME type of the data at the
     * given URI.  The returned MIME type should start with
     * <code>vnd.android.cursor.item</code> for a single record,
     * or <code>vnd.android.cursor.dir/</code> for multiple items.
     */
    public static final String GagData_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.jingb.9gag.data";

    public static final String AUTHORITY = "com.jingb.application.ninegag.imageload.dao.GagDataProvider";
    public static final String PATH = "/gagdatas";
    public static final Uri GAGDATAS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + PATH);

    static{
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(AUTHORITY, PATH, code);
    }

    private String matchTable(Uri uri) {
        String table = null;
        switch (matcher.match(uri)) {
            case code:
                table = GagDatagramHelper.GagDatagramDBinfo.TABLE_NAME;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        return table;
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    public static DBHelper getDBHelper() {
        if (dbHelper == null) {
            dbHelper = new DBHelper(App.getContext());
        }
        return dbHelper;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        String table = matchTable(uri);
        queryBuilder.setTables(table);

        SQLiteDatabase db = getDBHelper().getReadableDatabase();
        Cursor cursor = queryBuilder.query(
                db, // The database to
                projection, // The columns to return from the queryFromDB
                selection, // The columns for the where clause
                selectionArgs, // The values for the where clause
                null, // don't group the rows
                null, // don't filter by row groups
                sortOrder // The sort order
        );
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return GagData_CONTENT_TYPE;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        switch (matcher.match(uri)) {
            //把数据库打开放到里面是想证明uri匹配完成
            case code:
                SQLiteDatabase db = getDBHelper().getWritableDatabase();
                String table = matchTable(uri);
                //第二个参数置null则在values为空的时候不会有数据插入数据库
                long rowId = db.insert(table, null, values);
                if (rowId > 0) {
                    //在前面已有的Uri后面追加ID
                    Uri nameUri = ContentUris.withAppendedId(uri, rowId);
                    //通知数据已经发生改变
                    getContext().getContentResolver().notifyChange(nameUri, null);
                    return nameUri;
                }
                throw new SQLException("Failed to insert row into " + uri);
            default:
                break ;
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Logger.i("delete invoked!");
        SQLiteDatabase db = getDBHelper().getWritableDatabase();
        int count = 0;
        String table = matchTable(uri);
        db.beginTransaction();
        try {
            count = db.delete(table, selection, selectionArgs);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = getDBHelper().getWritableDatabase();
        int count;
        String table = matchTable(uri);
        db.beginTransaction();
        try {
            count = db.update(table, values, selection, selectionArgs);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
