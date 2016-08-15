package com.jingb.application.ninegag.imageload.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.v4.content.CursorLoader;

import com.jingb.application.ninegag.imageload.model.Category;
import com.jingb.application.ninegag.imageload.model.Column;
import com.jingb.application.ninegag.imageload.model.GagDatagram;
import com.jingb.application.ninegag.imageload.model.SQLiteTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jingb on 16/7/12.
 */
public class GagDatagramHelper extends BaseDataHelper {

    private Category mCategory;

    public GagDatagramHelper(Context context, Category category) {
        super(context);
        mCategory = category;
    }

    @Override
    protected Uri getContentUri() {
        return GagDataProvider.GAGDATAS_CONTENT_URI;
    }

    public CursorLoader getCursorLoader() {
        /**
         * projection代表查询返回哪些字段，传null则全部都查
         */
        String[] projection = null;
        /**
         * A filter declaring which rows to return, formatted as an
         * SQL WHERE clause (excluding the WHERE itself). Passing null will
         * return all rows for the given URI.
         */
        String selection = GagDatagramDBinfo.CATEGORY + "=?";
        /**
         * 替换selection中的'?'
         */
        String[] selectionArgs = new String[] {
                String.valueOf(mCategory.ordinal())
        };
        /**
         * 传null是无序
         */
        String sortOrder = GagDatagramDBinfo._ID + " ASC";
        return new CursorLoader(getContext(),
                                getContentUri(),
                                projection,
                                selection,
                                selectionArgs,
                                sortOrder);
    }

    private ContentValues generateContentValues(GagDatagram gagDatagram) {
        ContentValues values = new ContentValues();
        values.put(GagDatagramDBinfo.ID, gagDatagram.id);
        values.put(GagDatagramDBinfo.CATEGORY, mCategory.ordinal());
        values.put(GagDatagramDBinfo.JSON, gagDatagram.toJson());
        return values;
    }

    public void bulkInsert(List<GagDatagram> gagDatagrams) {
        ArrayList<ContentValues> contentValues = new ArrayList<>();
        for (GagDatagram gagDatagram : gagDatagrams) {
            ContentValues values = generateContentValues(gagDatagram);
            contentValues.add(values);
        }
        ContentValues[] valueArray = new ContentValues[contentValues.size()];
        super.bulkInsert(contentValues.toArray(valueArray));
    }

    public int deleteAll() {
        DBHelper mDBHelper = GagDataProvider.getDBHelper();
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        int row = db.delete(GagDatagramDBinfo.TABLE_NAME,
                GagDatagramDBinfo.CATEGORY + "=?",
                new String[] {
                        String.valueOf(mCategory.ordinal())
                }
        );
        //Logger.i("delete " + row + " rows");
        return row;
    }

    /**
     * 将GagDatagram抽象成在数据库中的一条数据
     * 只划分3个字段，id标识唯一，category标识分类，其余数据统一放json里即可，不再细分字段
     */
    public static final class GagDatagramDBinfo implements BaseColumns {
        private GagDatagramDBinfo() { }

        public static final String TABLE_NAME = "GagDatagram";

        public static final String ID = "id";

        public static final String CATEGORY = "category";

        public static final String JSON = "json";

        public static final SQLiteTable TABLE = new SQLiteTable(TABLE_NAME)
                .addColumn(ID, Column.DataType.INTEGER)
                .addColumn(CATEGORY, Column.DataType.INTEGER)
                .addColumn(JSON, Column.DataType.TEXT);
    }
}
