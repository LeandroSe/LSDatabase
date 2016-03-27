package br.com.tsujiguchi.lsdatabase.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by leandrose on 27/03/16.
 */
public class SqlParser {

    protected static final int STATE_NONE = 0;
    protected static final int STATE_COMMENT = 1;
    protected static final int STATE_COMMENT_BLOCK = 2;
    protected static final int STATE_STRING = 3;

    public static final List<String> parse(InputStream is) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(is);
        StringBuffer sb = new StringBuffer();

        List<String> commands = new ArrayList<>();

        try {
            Tokenizer tokenizer = new Tokenizer(is);
            int state = STATE_NONE;

            while (tokenizer.hasNext()) {
                final char c = (char) tokenizer.next();

                if (state == STATE_COMMENT_BLOCK) {
                    if (tokenizer.skip("*/")) {
                        state = STATE_NONE;
                    }
                    continue;

                } else if (state == STATE_COMMENT) {
                    if (isNewLine(c)) {
                        state = STATE_NONE;
                    }
                    continue;

                } else if (state == STATE_NONE && tokenizer.skip("/*")) {
                    state = STATE_COMMENT_BLOCK;
                    continue;

                } else if (state == STATE_NONE && tokenizer.skip("--")) {
                    state = STATE_COMMENT;
                    continue;

                } else if (state == STATE_NONE && c == ';') {
                    final String command = sb.toString().trim();
                    commands.add(command);
                    sb.setLength(0);
                    continue;

                } else if (state == STATE_NONE && c == '\'') {
                    state = STATE_STRING;

                } else if (state == STATE_STRING && c == '\'') {
                    state = STATE_NONE;

                }

                if (state == STATE_NONE || state == STATE_STRING) {
                    if (state == STATE_NONE && isWhitespace(c)) {
                        if (sb.length() > 0 && sb.charAt(sb.length() - 1) != ' ') {
                            sb.append(' ');
                        }
                    } else {
                        sb.append(c);
                    }
                }
            }
        } finally {

        }

        if (sb.length() > 0) {
            commands.add(sb.toString().trim());
        }

        return commands;
    }

    private static boolean isNewLine(final char c) {
        return c == '\r' || c == '\n';
    }

    private static boolean isWhitespace(final char c) {
        return c == '\r' || c == '\n' || c == '\t' || c == ' ';
    }

    private static class Tokenizer {

        private final InputStream mStream;
        private boolean mIsNext;
        private int mCurrent;

        public Tokenizer(InputStream stream) {
            mStream = stream;
        }

        public boolean hasNext() throws IOException {
            if (!mIsNext) {
                mIsNext = true;
                mCurrent = mStream.read();
            }

            return mCurrent != -1;
        }

        public int next() throws IOException {

            if (!this.mIsNext) {
                mCurrent = mStream.read();
            }
            mIsNext = false;
            return mCurrent;
        }

        public boolean skip(final String s) throws IOException {
            if (s == null || s.length() == 0) {
                return false;
            }

            if (s.charAt(0) != mCurrent) {
                return false;
            }

            final int len = s.length();
            mStream.mark(len - 1);

            for (int n = 1; n < len; n++) {
                final int value = mStream.read();

                if (value != s.charAt(n)) {
                    mStream.reset();
                    return false;
                }
            }
            return true;
        }
    }
}
