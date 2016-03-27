package br.com.tsujiguchi.lsdatabase.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.com.tsujiguchi.lsdatabase.LSDatabase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LSDatabase.getReadableDatabase().query(
                "accounts",
                new String[]{"_id", "title"},
                "",
                null, null, null, null
        );
    }
}
