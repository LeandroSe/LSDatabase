package br.com.tsujiguchi.lsdatabase.query;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Teste da classe {@link SQLBuilder}.
 *
 * @author LeandroSe
 */
public class SQLBuilderUnitTest {

    @Test
    public void testSimple() {
        SQLBuilder SQLBuilder = new SQLBuilder();
        SQLBuilder.from("accounts")
                .columns("_id", "title");

        assertEquals("SELECT `_id`, `title` FROM `accounts`;", SQLBuilder.toSql());
    }

    @Test
    public void testSimpleAlias() {
        SQLBuilder SQLBuilder = new SQLBuilder();
        SQLBuilder.from("accounts", "a");

        assertEquals("SELECT * FROM `accounts` AS a;", SQLBuilder.toSql());
    }

    @Test
    public void testSimpleAllColumns() {
        SQLBuilder SQLBuilder = new SQLBuilder();
        SQLBuilder.from("accounts");

        assertEquals("SELECT * FROM `accounts`;", SQLBuilder.toSql());
    }

    @Test
    public void testSimpleAllDistinct() {
        SQLBuilder SQLBuilder = new SQLBuilder();
        SQLBuilder.from("accounts")
                .distinct();

        assertEquals("SELECT DISTINCT * FROM `accounts`;", SQLBuilder.toSql());
    }

    @Test
    public void testSimpleHaving() {
        SQLBuilder SQLBuilder = new SQLBuilder();
        SQLBuilder.from("accounts")
                .having("_id > 0");

        assertEquals("SELECT * FROM `accounts` HAVING _id > 0;", SQLBuilder.toSql());
    }

    @Test
    public void testSimpleLimitTake() {
        SQLBuilder SQLBuilder = new SQLBuilder();
        SQLBuilder.from("accounts")
                .take(10);

        assertEquals("SELECT * FROM `accounts` LIMIT 10;", SQLBuilder.toSql());
    }

    @Test
    public void testSimpleLimitSkipTake() {
        SQLBuilder SQLBuilder = new SQLBuilder();
        SQLBuilder.from("accounts")
                .take(10)
                .skip(50);

        assertEquals("SELECT * FROM `accounts` LIMIT 50, 10;", SQLBuilder.toSql());
    }

    @Test
    public void testSimpleJoinInner() {
        SQLBuilder SQLBuilder = new SQLBuilder();
        SQLBuilder.from("accounts", "a")
                .innerJoin("balance", "b", "a._id = b.account_id");

        assertEquals("SELECT * FROM `accounts` AS a INNER JOIN `balance` b ON a._id = b.account_id;", SQLBuilder.toSql());
    }

    @Test
    public void testSimpleJoinLeft() {
        SQLBuilder SQLBuilder = new SQLBuilder();
        SQLBuilder.from("accounts", "a")
                .leftJoin("balance", "b", "a._id = b.account_id");

        assertEquals("SELECT * FROM `accounts` AS a LEFT JOIN `balance` b ON a._id = b.account_id;", SQLBuilder.toSql());
    }

    @Test
    public void testSimpleJoinRight() {
        SQLBuilder SQLBuilder = new SQLBuilder();
        SQLBuilder.from("accounts", "a")
                .rightJoin("balance", "b", "a._id = b.account_id");

        assertEquals("SELECT * FROM `accounts` AS a RIGHT JOIN `balance` b ON a._id = b.account_id;", SQLBuilder.toSql());
    }

    @Test
    public void testSimpleJoinFull() {
        SQLBuilder SQLBuilder = new SQLBuilder();
        SQLBuilder.from("accounts", "a")
                .fullJoin("balance", "b", "a._id = b.account_id");

        assertEquals("SELECT * FROM `accounts` AS a FULL JOIN `balance` b ON a._id = b.account_id;", SQLBuilder.toSql());
    }

    @Test
    public void testSimpleGroup() {
        SQLBuilder SQLBuilder = new SQLBuilder();
        SQLBuilder.from("accounts")
                .group("_id");

        assertEquals("SELECT * FROM `accounts` GROUP BY `_id`;", SQLBuilder.toSql());
    }

    @Test
    public void testSimpleGroups() {
        SQLBuilder SQLBuilder = new SQLBuilder();
        SQLBuilder.from("accounts")
                .group("_id")
                .group("title");

        assertEquals("SELECT * FROM `accounts` GROUP BY `_id`, `title`;", SQLBuilder.toSql());
    }

    @Test
    public void testSimpleOrderAsc() {
        SQLBuilder SQLBuilder = new SQLBuilder();
        SQLBuilder.from("accounts")
                .orderAsc("_id");

        assertEquals("SELECT * FROM `accounts` ORDER BY `_id` ASC;", SQLBuilder.toSql());
    }

    @Test
    public void testSimpleOrderDesc() {
        SQLBuilder SQLBuilder = new SQLBuilder();
        SQLBuilder.from("accounts")
                .orderDesc("_id");

        assertEquals("SELECT * FROM `accounts` ORDER BY `_id` DESC;", SQLBuilder.toSql());
    }

    @Test
    public void testSimpleOrders() {
        SQLBuilder SQLBuilder = new SQLBuilder();
        SQLBuilder.from("accounts")
                .orderAsc("_id")
                .orderDesc("title");

        assertEquals("SELECT * FROM `accounts` ORDER BY `_id` ASC, `title` DESC;", SQLBuilder.toSql());
    }

    @Test
    public void testSimpleWhere() {
        SQLBuilder SQLBuilder = new SQLBuilder();
        SQLBuilder.from("accounts")
                .where("_id = ?", 1);

        assertEquals("SELECT * FROM `accounts` WHERE _id = ?;", SQLBuilder.toSql());
        assertEquals(1, SQLBuilder.getArgs().length);
    }

    @Test
    public void testSimpleWheresAnd() {
        SQLBuilder SQLBuilder = new SQLBuilder();
        SQLBuilder.from("accounts")
                .where("_id = ?", 1)
                .where("title = ?", 1);

        assertEquals("SELECT * FROM `accounts` WHERE _id = ? AND title = ?;", SQLBuilder.toSql());
        assertEquals(2, SQLBuilder.getArgs().length);
    }

    @Test
    public void testSimpleWheresOr() {
        SQLBuilder SQLBuilder = new SQLBuilder();
        SQLBuilder.from("accounts")
                .where("_id = ?", 1)
                .orWhere("title = ?", 1);

        assertEquals("SELECT * FROM `accounts` WHERE _id = ? OR title = ?;", SQLBuilder.toSql());
        assertEquals(2, SQLBuilder.getArgs().length);
    }
}
