package br.com.tsujiguchi.lsdatabase.query;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Classe para criação de SQL do tipo Select.
 *
 * @author LeandroSe
 */
public class SQLBuilder {

    public static final String WHERE_AND = "AND";
    public static final String WHERE_OR = "OR";

    protected boolean mDistinct = false;
    protected ArrayList<String> mColumns = new ArrayList<>();
    protected String mTablename;
    protected String mTablenameAlias;
    protected ArrayList<String> mJoins = new ArrayList<>();
    protected ArrayList<String> mWhere = new ArrayList<>();
    protected ArrayList<String> mWhereCondition = new ArrayList<>();
    protected ArrayList<Object> mWhereArgs = new ArrayList<>();
    protected int mTake;
    protected int mSkip;
    protected ArrayList<String> mGroups = new ArrayList<>();
    protected ArrayList<String> mOrders = new ArrayList<>();
    protected String mHaving;

    public SQLBuilder from(String tablename) {
        mTablename = tablename;

        return this;
    }

    public SQLBuilder from(String tablename, String alias) {
        mTablename = tablename;
        mTablenameAlias = alias;

        return this;
    }

    public SQLBuilder distinct() {
        mDistinct = true;

        return this;
    }

    public SQLBuilder columns(String... columns) {
        mColumns.addAll(Arrays.asList(columns));

        return this;
    }

    public SQLBuilder innerJoin(String tablename, String alias, String condition) {
        mJoins.add(String.format(
                "INNER JOIN `%s` %s ON %s",
                tablename,
                alias,
                condition
        ));

        return this;
    }

    public SQLBuilder leftJoin(String tablename, String alias, String condition) {
        mJoins.add(String.format(
                "LEFT JOIN `%s` %s ON %s",
                tablename,
                alias,
                condition
        ));

        return this;
    }

    public SQLBuilder rightJoin(String tablename, String alias, String condition) {
        mJoins.add(String.format(
                "RIGHT JOIN `%s` %s ON %s",
                tablename,
                alias,
                condition
        ));

        return this;
    }

    public SQLBuilder fullJoin(String tablename, String alias, String condition) {
        mJoins.add(String.format(
                "FULL JOIN `%s` %s ON %s",
                tablename,
                alias,
                condition
        ));

        return this;
    }

    public SQLBuilder where(String condition, Object... args) {
        mWhere.add(condition);
        mWhereCondition.add(WHERE_AND);
        mWhereArgs.addAll(Arrays.asList(args));

        return this;
    }

    public SQLBuilder orWhere(String condition, Object... args) {
        mWhere.add(condition);
        mWhereCondition.add(WHERE_OR);
        mWhereArgs.addAll(Arrays.asList(args));

        return this;
    }

    public SQLBuilder group(String group) {
        mGroups.add(group);

        return this;
    }

    public SQLBuilder orderAsc(String order) {
        mOrders.add("`" + order + "` ASC");

        return this;
    }

    public SQLBuilder orderDesc(String order) {
        mOrders.add("`" + order + "` DESC");

        return this;
    }

    public SQLBuilder take(int take) {
        mTake = take;

        return this;
    }

    public SQLBuilder skip(int skip) {
        mSkip = skip;

        return this;
    }

    public SQLBuilder having(String condition) {
        mHaving = condition;

        return this;
    }

    public String[] getArgs() {
        String[] args = new String[mWhereArgs.size()];
        int position = 0;
        for (Object arg : mWhereArgs) {
            args[position++] = arg.toString();
        }

        return args;
    }

    public String toSql() {
        String sql = "SELECT ";

        // DISTINCT
        if (mDistinct) {
            sql += "DISTINCT ";
        }

        // COLUMNS
        if (mColumns.size() == 0) {
            sql += "*";
        } else {
            for (String col : mColumns) {
                sql += "`" + col + "`, ";
            }
            sql = sql.substring(0, sql.length() - 2);
        }

        // FROM
        if (null == mTablename) {
            throw new RuntimeException("Tablename not defined");
        }
        if (mJoins.size() > 0) {
            sql += " FROM `" + mTablename + "` AS " + (null == mTablenameAlias ? mTablename : mTablenameAlias);
        } else {
            sql += " FROM `" + mTablename + "`";
            if (null != mTablenameAlias) {
                sql += " AS " + mTablenameAlias;
            }
        }

        // JOIN
        if (mJoins.size() > 0) {
            for (String join : mJoins) {
                sql += " " + join;
            }
        }

        // WHERE
        if (mWhere.size() > 0) {
            sql += " WHERE ";
            boolean isFirst = true;
            for (int i = 0; i < mWhere.size(); i++) {
                if (!isFirst) {
                    sql += " " + mWhereCondition.get(i) + " ";
                }
                sql += mWhere.get(i);

                isFirst = false;
            }
        }

        // GROUP
        if (mGroups.size() > 0) {
            sql += " GROUP BY ";
            for (String group : mGroups) {
                sql += "`" + group + "`, ";
            }
            sql = sql.substring(0, sql.length() - 2);
        }

        // HAVING
        if (null != mHaving) {
            sql += " HAVING " + mHaving;
        }

        // ORDER
        if (mOrders.size() > 0) {
            sql += " ORDER BY ";
            for (String order : mOrders) {
                sql += order + ", ";
            }
            sql = sql.substring(0, sql.length() - 2);
        }

        // LIMIT
        if (mTake > 0) {
            if (mSkip > 0) {
                sql += " LIMIT " + mSkip + ", " + mTake;
            } else {
                sql += " LIMIT " + mTake;
            }
        }

        return sql + ";";
    }
}
