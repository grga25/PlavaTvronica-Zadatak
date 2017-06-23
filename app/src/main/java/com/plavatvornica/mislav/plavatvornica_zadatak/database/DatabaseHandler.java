package com.plavatvornica.mislav.plavatvornica_zadatak.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.plavatvornica.mislav.plavatvornica_zadatak.models.Article;

import java.util.ArrayList;


public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "articlesManager";

    private static final String TABLE_ARTICLES = "articles";

    private static final String KEY_ID = "id";
    private static final String KEY_AUTHOR = "author";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_URL = "url";
    private static final String KEY_URL_TO_IMAGE = "urlToImage";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_TIMESTAMP = "timestamp";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ARTICLES_TABLE = "CREATE TABLE " + TABLE_ARTICLES
                + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_AUTHOR + " TEXT,"
                + KEY_TITLE + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_URL + " TEXT,"
                + KEY_URL_TO_IMAGE + " TEXT,"
                + KEY_IMAGE + " BLOB,"
                + KEY_TIMESTAMP + " INTEGER"
                + ")";
        db.execSQL(CREATE_ARTICLES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTICLES);
        onCreate(db);
    }

    public void addArticle(Article article, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_AUTHOR, article.getAuthor());
        values.put(KEY_TITLE, article.getTitle());
        values.put(KEY_DESCRIPTION, article.getDescription());
        values.put(KEY_URL, article.getUrl());
        values.put(KEY_URL_TO_IMAGE, article.getUrlToImage());
        values.put(KEY_IMAGE, image);
        values.put(KEY_TIMESTAMP, System.currentTimeMillis());

        db.insert(TABLE_ARTICLES, null, values);
        db.close();
    }

    public Article getArticle(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ARTICLES, new String[]{KEY_ID,
                        KEY_AUTHOR, KEY_TITLE, KEY_DESCRIPTION,
                        KEY_URL, KEY_URL_TO_IMAGE, KEY_IMAGE, KEY_TIMESTAMP}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Article article = new Article(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getBlob(6),
                cursor.getInt(7));

        cursor.close();
        db.close();

        return article;
    }

    public ArrayList<Article> getAllArticles() {
        ArrayList<Article> articles = new ArrayList<Article>();

        String selectQuery = "SELECT  * FROM " + TABLE_ARTICLES;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Article article = new Article();
                article.setId(cursor.getInt(0));
                article.setAuthor(cursor.getString(1));
                article.setTitle(cursor.getString(2));
                article.setDescription(cursor.getString(3));
                article.setUrl(cursor.getString(4));
                article.setUrlToImage(cursor.getString(5));
                article.setImageByte(cursor.getBlob(6));
                article.setTimeStamp(cursor.getInt(7));
                articles.add(article);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return articles;
    }

    public long getTimeStamp(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ARTICLES, new String[]{KEY_ID,
                        KEY_AUTHOR, KEY_TITLE, KEY_DESCRIPTION,
                        KEY_URL, KEY_URL_TO_IMAGE, KEY_IMAGE, KEY_TIMESTAMP}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        long timestamp = cursor.getLong(7);
        cursor.close();


        return timestamp;
    }

    public int getArticlesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_ARTICLES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count;

    }

    public void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ARTICLES, null, null);
        db.close();
    }
}
