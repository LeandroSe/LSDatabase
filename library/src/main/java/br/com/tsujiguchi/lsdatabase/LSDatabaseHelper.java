package br.com.tsujiguchi.lsdatabase;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.DefaultDatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import br.com.tsujiguchi.lsdatabase.util.SqlParser;

/**
 * Created by leandrose on 24/03/16.
 */
public class LSDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "LSDatabaseHelper";
    public static final String LSDATABASE_MIGRATIONS = "lsdatabase_migrations";

    protected Context mContext;
    protected LSDatabaseConfiguration mConfiguration;

    public LSDatabaseHelper(LSDatabaseConfiguration configuration) {
        super(
                configuration.getContext(),
                configuration.getDatabaseName(),
                null,
                configuration.getDatabaseVersion()
        );

        mContext = configuration.getContext();
        mConfiguration = configuration;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (LSDatabase.isDebug())
            Log.d(TAG, "onCreate() : version=" + mConfiguration.getDatabaseVersion());
        executeMigrations(db, -1, mConfiguration.getDatabaseVersion());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (LSDatabase.isDebug())
            Log.d(TAG, "onUpgrade() : oldVersion=" + oldVersion + " newVersion=" + newVersion);
        executeMigrations(db, oldVersion, newVersion);
    }

    private void executeMigrations(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            final List<String> migrations = Arrays.asList(mContext.getAssets().list(LSDATABASE_MIGRATIONS));
            final List<Integer> versions = new ArrayList<>();
            for (String m : migrations) {
                versions.add(Integer.valueOf(m.substring(0, m.indexOf(".sql"))));
            }
            Collections.sort(versions);

            for (int version : versions) {
                if (version > oldVersion && version <= newVersion) {
                    executeScript(db, version);
                }
            }

        } catch (IOException ioe) {
            throw new RuntimeException("error in migrations for db", ioe);
        }
    }

    private void executeScript(SQLiteDatabase db, int version) {
        if (mConfiguration.isDebug())
            Log.d(TAG, "executeScript() : version=" + version);
        try {
            InputStream is = mContext.getAssets().open(LSDATABASE_MIGRATIONS + "/" + version + ".sql");
            List<String> commands = SqlParser.parse(is);

            for (String sql : commands) {
                db.execSQL(sql);
            }

            if (mConfiguration.isDebug())
                Log.d(TAG, "SUCCESS executeScript() : version=" + version);
        } catch (IOException ioe) {
            throw new RuntimeException("error in running migration scripts", ioe);
        }
    }
}
