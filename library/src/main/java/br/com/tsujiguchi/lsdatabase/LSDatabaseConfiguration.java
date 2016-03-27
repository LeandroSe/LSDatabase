package br.com.tsujiguchi.lsdatabase;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * Created by leandrose on 24/03/16.
 */
public class LSDatabaseConfiguration {

    private static final String TAG="LSDatabaseConfiguration";
    public static final String LSDATABASE_FILENAME = "LSDatabase_Filename";
    public static final String LSDATABASE_VERSION = "LSDatabase_Version";

    protected Context mContext;
    protected String mDatabaseName;
    protected int mDatabaseVersion;
    protected boolean mIsDebug;

    public LSDatabaseConfiguration(Context context, boolean debug) throws PackageManager.NameNotFoundException {
        mIsDebug=debug;
        if(mIsDebug)
            Log.d(TAG, "LSDatabase Initializing");

        ApplicationInfo appInfo = context.getPackageManager()
                .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        mDatabaseName = (String) appInfo.metaData.get(LSDATABASE_FILENAME);
        mDatabaseVersion = (Integer) appInfo.metaData.get(LSDATABASE_VERSION);

        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    public String getDatabaseName() {
        return mDatabaseName;
    }

    public int getDatabaseVersion() {
        return mDatabaseVersion;
    }

    public boolean isDebug() {
        return mIsDebug;
    }
}
