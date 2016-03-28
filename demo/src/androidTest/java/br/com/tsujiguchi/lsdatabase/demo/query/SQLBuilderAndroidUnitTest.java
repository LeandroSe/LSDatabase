package br.com.tsujiguchi.lsdatabase.demo.query;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;

import br.com.tsujiguchi.lsdatabase.LSDatabase;
import br.com.tsujiguchi.lsdatabase.demo.MainActivity;
import br.com.tsujiguchi.lsdatabase.query.SQLBuilder;

/**
 * Teste da execução dos SQL criados na class {@link SQLBuilder}.
 *
 * @author LeandroSe
 */
public class SQLBuilderAndroidUnitTest extends ActivityInstrumentationTestCase2<MainActivity> {

    protected SQLiteDatabase mDb;

    public SQLBuilderAndroidUnitTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        LSDatabase.initialize(getActivity(), true);
        mDb = LSDatabase.getReadableDatabase();
    }

    @SmallTest
    public void testSimple() {
        SQLBuilder SQLBuilder = new SQLBuilder();
        SQLBuilder.from("accounts")
                .columns("_id", "title");

        Cursor c = mDb.rawQuery(SQLBuilder.toSql(), SQLBuilder.getArgs());
        assertTrue(c.getCount() > 0);
    }

    @SmallTest
    public void testSimpleAlias() {
        SQLBuilder SQLBuilder = new SQLBuilder();
        SQLBuilder.from("accounts", "a");

        Cursor c = mDb.rawQuery(SQLBuilder.toSql(), SQLBuilder.getArgs());
        assertTrue(c.getCount() > 0);
    }

    @SmallTest
    public void testSimpleAllColumns() {
        SQLBuilder SQLBuilder = new SQLBuilder();
        SQLBuilder.from("accounts");

        Cursor c = mDb.rawQuery(SQLBuilder.toSql(), SQLBuilder.getArgs());
        assertTrue(c.getCount() > 0);
    }

    @SmallTest
    public void testSimpleAllDistinct() {
        SQLBuilder SQLBuilder = new SQLBuilder();
        SQLBuilder.from("accounts")
                .distinct();

        Cursor c = mDb.rawQuery(SQLBuilder.toSql(), SQLBuilder.getArgs());
        assertTrue(c.getCount() > 0);
    }

    @SmallTest
    public void testSimpleHaving() {
        SQLBuilder SQLBuilder = new SQLBuilder();
        SQLBuilder.from("accounts")
                .group("_id")
                .having("_id > 0");

        Cursor c = mDb.rawQuery(SQLBuilder.toSql(), SQLBuilder.getArgs());
        assertTrue(c.getCount() > 0);
    }

    @SmallTest
    public void testSimpleLimitTake() {
        SQLBuilder SQLBuilder = new SQLBuilder();
        SQLBuilder.from("accounts")
                .take(10);

        Cursor c = mDb.rawQuery(SQLBuilder.toSql(), SQLBuilder.getArgs());
        assertTrue(c.getCount() > 0);
    }

    @SmallTest
    public void testSimpleLimitSkipTake() {
        SQLBuilder SQLBuilder = new SQLBuilder();
        SQLBuilder.from("accounts")
                .take(10)
                .skip(0);

        Cursor c = mDb.rawQuery(SQLBuilder.toSql(), SQLBuilder.getArgs());
        assertTrue(c.getCount() > 0);
    }

    @SmallTest
    public void testSimpleJoinInner() {
        SQLBuilder SQLBuilder = new SQLBuilder();
        SQLBuilder.from("accounts", "a")
                .innerJoin("starting_balance", "b", "a._id = b.account_id");

        Cursor c = mDb.rawQuery(SQLBuilder.toSql(), SQLBuilder.getArgs());
        assertTrue(c.getCount() > 0);
    }

    @SmallTest
    public void testSimpleJoinLeft() {
        SQLBuilder SQLBuilder = new SQLBuilder();
        SQLBuilder.from("accounts", "a")
                .leftJoin("starting_balance", "b", "a._id = b.account_id");

        Cursor c = mDb.rawQuery(SQLBuilder.toSql(), SQLBuilder.getArgs());
        assertTrue(c.getCount() > 0);
    }

    @SmallTest
    public void testSimpleGroup() {
        SQLBuilder SQLBuilder = new SQLBuilder();
        SQLBuilder.from("accounts")
                .group("_id");

        Cursor c = mDb.rawQuery(SQLBuilder.toSql(), SQLBuilder.getArgs());
        assertTrue(c.getCount() > 0);
    }

    @SmallTest
    public void testSimpleGroups() {
        SQLBuilder SQLBuilder = new SQLBuilder();
        SQLBuilder.from("accounts")
                .group("_id")
                .group("title");

        Cursor c = mDb.rawQuery(SQLBuilder.toSql(), SQLBuilder.getArgs());
        assertTrue(c.getCount() > 0);
    }

    @SmallTest
    public void testSimpleOrderAsc() {
        SQLBuilder SQLBuilder = new SQLBuilder();
        SQLBuilder.from("accounts")
                .orderAsc("_id");

        Cursor c = mDb.rawQuery(SQLBuilder.toSql(), SQLBuilder.getArgs());
        assertTrue(c.getCount() > 0);
    }

    @SmallTest
    public void testSimpleOrderDesc() {
        SQLBuilder SQLBuilder = new SQLBuilder();
        SQLBuilder.from("accounts")
                .orderDesc("_id");

        Cursor c = mDb.rawQuery(SQLBuilder.toSql(), SQLBuilder.getArgs());
        assertTrue(c.getCount() > 0);
    }

    @SmallTest
    public void testSimpleOrders() {
        SQLBuilder SQLBuilder = new SQLBuilder();
        SQLBuilder.from("accounts")
                .orderAsc("_id")
                .orderDesc("title");

        Cursor c = mDb.rawQuery(SQLBuilder.toSql(), SQLBuilder.getArgs());
        assertTrue(c.getCount() > 0);
    }

    @SmallTest
    public void testSimpleWhere() {
        SQLBuilder SQLBuilder = new SQLBuilder();
        SQLBuilder.from("accounts")
                .where("_id = ?", 1);

        Cursor c = mDb.rawQuery(SQLBuilder.toSql(), SQLBuilder.getArgs());
        assertTrue(c.getCount() > 0);
    }

    @SmallTest
    public void testSimpleWheresAnd() {
        SQLBuilder SQLBuilder = new SQLBuilder();
        SQLBuilder.from("accounts")
                .where("_id = ?", 1)
                .where("title = ?", "CONTA 1");

        Cursor c = mDb.rawQuery(SQLBuilder.toSql(), SQLBuilder.getArgs());
        assertTrue(c.getCount() > 0);
    }

    @SmallTest
    public void testSimpleWheresOr() {
        SQLBuilder SQLBuilder = new SQLBuilder();
        SQLBuilder.from("accounts")
                .where("_id = ?", 1)
                .orWhere("title = ?", 1);

        Cursor c = mDb.rawQuery(SQLBuilder.toSql(), SQLBuilder.getArgs());
        assertTrue(c.getCount() > 0);
    }
}
