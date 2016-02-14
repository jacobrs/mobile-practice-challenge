package com.csgames.diabetus;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Thinesh on 2016-02-14.
 */
public class DatabaseHelper extends SQLiteOpenHelper {


    //Database file name
    public static String DB_NAME;
    public SQLiteDatabase database;
    public final Context context;
    public static  String DB_PATH;

    public DatabaseHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, databaseName, factory, version);
        this.context = context;

        String packageName = context.getPackageName();
//        DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        DB_PATH = String.format("//data//data//%s//databases//", packageName);
        DB_NAME = databaseName;
        openDataBase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //Method for copying the database
    private void copyDataBase() throws IOException {
        //Open a stream for reading from our ready-made database
        //The stream source is located in the assets
        InputStream externalDbStream = context.getAssets().open(DB_NAME);

        //Path to the created empty database on your Android device
        String outFileName = DB_PATH + DB_NAME;

        //Now create a stream for writing the database byte by byte
        OutputStream localDbStream = new FileOutputStream(outFileName);

        //Copying the database
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = externalDbStream.read(buffer)) > 0) {
            localDbStream.write(buffer, 0, bytesRead);
        }
        //Don’t forget to close the streams
        localDbStream.close();
        externalDbStream.close();
    }

    public SQLiteDatabase openDataBase() throws SQLException {
        String path = DB_PATH + DB_NAME;
        if (database == null) {
            createDataBase();
            database = SQLiteDatabase.openDatabase(path, null,
                    SQLiteDatabase.OPEN_READWRITE);
        }
        return database;
    }

    public void createDataBase() {
        if(!checkDB()) {
            this.getReadableDatabase();
            this.close();
            try {
                copyDataBase();
                Log.d(this.getClass().toString(), "Copying Database");
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(this.getClass().toString(), "Copying error");
                throw new Error("Error copying database!");
            }
        }
    }

    private boolean checkDB()
    {
        File dbFile = new File(DB_PATH+DB_NAME);
        return dbFile.exists();


    }


}
