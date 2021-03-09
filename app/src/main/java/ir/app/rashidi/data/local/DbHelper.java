package ir.app.rashidi.data.local;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import ir.app.rashidi.entity.Book;
import ir.app.rashidi.entity.User;

public class DbHelper extends SQLiteOpenHelper {
    private String TAG = "Database";
    public static final String DATABASE_NAME = "book" ;
    public static final int VERSION = 1;

    private SQLiteDatabase db;

    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null,VERSION );
        Log.e(TAG,"constructor");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.e(TAG,"onCreate");
        createBookTable(sqLiteDatabase);
        createUserTable(sqLiteDatabase);
        createStarBookTable(sqLiteDatabase);
    }

    //create tables
    private void createBookTable(SQLiteDatabase sqLiteDatabase){
        String query = "create table tbl_book (" +
                "id integer(10) primary key," +
                "name varchar(255)," +
                "image text(255)," +
                "fileUrl text(255)" +
                ")";
        sqLiteDatabase.execSQL(query);
    }

    private void createUserTable(SQLiteDatabase sqLiteDatabase){
        String query="create table tbl_user (" +
                "id  integer(10) PRIMARY KEY," +
                "name varchar(50), " +
                "family varchar(50), " +
                "email varchar(50), " +
                "password text(250))";
        sqLiteDatabase.execSQL(query);
    }

    private void createStarBookTable(SQLiteDatabase sqLiteDatabase){
        String query = "create table tbl_star_book (" +
                "id integer(10) PRIMARY KEY," +
                "user integer(10)," +
                "book integer(10)" +
                ")";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.e(TAG,"onUpgrade");
    }

    //insert Queries

    public boolean insertUser(String name,String family,String email,String password){
        db = getWritableDatabase();
        if (!checkExitUser(email)){
            ContentValues contentValues = new ContentValues();
            contentValues.put("name",name);
            contentValues.put("family",family);
            contentValues.put("email",email);
            contentValues.put("password",password);
            db.insert("tbl_user",null,contentValues);

            return true;
        }else
            return false;
    }

    public User getUser(String email,String password){
        db = getReadableDatabase();
        User user = null;
        String query = "select * from tbl_user where email = '"+email+"' and password = '"+password+"'";
        Cursor cursor = db.rawQuery(query,null);
        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndex("id")));
            user.setName(cursor.getString(cursor.getColumnIndex("name")));
            user.setFamily(cursor.getString(cursor.getColumnIndex("family")));
            user.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
        }

        cursor.close();
        return user;
    }

    public void insertBook(Book book){
        db = getWritableDatabase();
        if (!checkExitBook(book.getId())){
            ContentValues contentValues = new ContentValues();
            contentValues.put("id",book.getId());
            contentValues.put("name",book.getName());
            contentValues.put("image",book.getImage());
            contentValues.put("fileUrl",book.getFileUrl());

            db.insert("tbl_book",null,contentValues);
        }
    }

    public List<Book> getAllBook(){
        db = getReadableDatabase();

        List<Book> books = new ArrayList<>();

        String query = "select * from tbl_book";
        Cursor cursor = db.rawQuery(query,null);
        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            do {
                Book book = new Book();
                book.setId(cursor.getInt(cursor.getColumnIndex("id")));
                book.setName(cursor.getString(cursor.getColumnIndex("name")));
                book.setImage(cursor.getString(cursor.getColumnIndex("image")));
                book.setFileUrl(cursor.getString(cursor.getColumnIndex("fileUrl")));

                books.add(book);
            }while (cursor.moveToNext());
        }

        cursor.close();
        return books;
    }

    public Book getBook(int id){
        db = getReadableDatabase();
        Book book = null;
        String query = "select * from tbl_book where id = '"+id+"'";
        Cursor cursor = db.rawQuery(query,null);
        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            book = new Book();
            book.setId(cursor.getInt(cursor.getColumnIndex("id")));
            book.setName(cursor.getString(cursor.getColumnIndex("name")));
            book.setImage(cursor.getString(cursor.getColumnIndex("image")));
            book.setFileUrl(cursor.getString(cursor.getColumnIndex("fileUrl")));
        }

        return book;
    }


    //exit methods
    private boolean checkExitUser(String email){
        db = getReadableDatabase();
        String query = "select * from tbl_user where email = '"+email+"'";
        Cursor cursor = db.rawQuery(query,null);
        return cursor.getCount() > 0;
    }

    private boolean checkExitBook(int id){
        db = getReadableDatabase();
        String query = "select * from tbl_book where id = '"+id+"'";
        Cursor cursor = db.rawQuery(query,null);

        return cursor.getCount() > 0;
    }
}
