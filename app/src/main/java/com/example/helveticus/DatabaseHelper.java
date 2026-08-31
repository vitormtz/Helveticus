package com.example.helveticus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "helveticus.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_PHOTOS = "photos";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_IMAGE = "image";
    private static final String CREATE_TABLE_PHOTOS =
            "CREATE TABLE " + TABLE_PHOTOS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_IMAGE + " TEXT NOT NULL " +
                    ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PHOTOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHOTOS);
        onCreate(db);
    }

    public long saveImage(String base64Image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IMAGE, base64Image);

        long id = db.insert(TABLE_PHOTOS, null, values);
        db.close();
        return id;
    }

    public List<String> getAllImages() {
        List<String> imagesList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_PHOTOS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            int imageColumnIndex = cursor.getColumnIndex(COLUMN_IMAGE);
            if (imageColumnIndex != -1) {
                do {
                    String image = cursor.getString(imageColumnIndex);
                    imagesList.add(image);
                } while (cursor.moveToNext());
            }
        }

        cursor.close();
        db.close();
        return imagesList;
    }

    public List<PhotoItem> getAllPhotoItems() {
        List<PhotoItem> photoItems = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_PHOTOS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int idColumnIndex = cursor.getColumnIndex(COLUMN_ID);
                int imageColumnIndex = cursor.getColumnIndex(COLUMN_IMAGE);

                if (idColumnIndex != -1 && imageColumnIndex != -1) {
                    long id = cursor.getLong(idColumnIndex);
                    String image = cursor.getString(imageColumnIndex);

                    photoItems.add(new PhotoItem(id, image));
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return photoItems;
    }
}