package br.com.tsujiguchi.lsdatabase.demo;

import br.com.tsujiguchi.lsdatabase.LSDatabase;

/**
 * Created by leandrose on 27/03/16.
 */
public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        LSDatabase.initialize(getBaseContext(), true);
    }
}
