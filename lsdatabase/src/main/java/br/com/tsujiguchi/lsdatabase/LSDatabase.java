package br.com.tsujiguchi.lsdatabase;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;

/**
 * Classe de manipulação com o banco de dados.
 *
 * @author LeandroSe
 */
public class LSDatabase {

    private static LSDatabaseHelper mHelper;
    private static LSDatabaseConfiguration mConfiguration;

    public static void initialize(Context context, boolean debug) {
        try {
            mConfiguration = new LSDatabaseConfiguration(context, debug);
            mHelper = new LSDatabaseHelper(mConfiguration);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("error in configuration LSDatabase.", e);
        }
    }

    public static boolean isDebug() {
        return mConfiguration.isDebug();
    }

    public static SQLiteDatabase getWritableDatabase() {
        return mHelper.getWritableDatabase();
    }

    public static SQLiteDatabase getReadableDatabase() {
        return mHelper.getReadableDatabase();
    }

    public static void close() {
        mHelper.close();
    }

    public static SQLiteDatabase beginTransaction(){
        SQLiteDatabase db=mHelper.getWritableDatabase();
        db.beginTransaction();
        return db;
    }
}
