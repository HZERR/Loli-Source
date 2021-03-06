/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.text;

import java.text.BreakIterator;
import java.text.CharacterIterator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

final class TextBreakIterator {
    static final int CHARACTER_ITERATOR = 0;
    static final int WORD_ITERATOR = 1;
    static final int LINE_ITERATOR = 2;
    static final int SENTENCE_ITERATOR = 3;
    static final int TEXT_BREAK_FIRST = 0;
    static final int TEXT_BREAK_LAST = 1;
    static final int TEXT_BREAK_NEXT = 2;
    static final int TEXT_BREAK_PREVIOUS = 3;
    static final int TEXT_BREAK_CURRENT = 4;
    static final int TEXT_BREAK_PRECEDING = 5;
    static final int TEXT_BREAK_FOLLOWING = 6;
    static final int IS_TEXT_BREAK = 7;
    static final int IS_WORD_TEXT_BREAK = 8;
    private static final Map<CacheKey, BreakIterator> iteratorCache = new HashMap<CacheKey, BreakIterator>();

    TextBreakIterator() {
    }

    static BreakIterator getIterator(int n2, String string, String string2, boolean bl) {
        BreakIterator breakIterator;
        Locale locale;
        String string3;
        String[] arrstring = string.split("-");
        switch (arrstring.length) {
            case 1: {
                string3 = null;
                break;
            }
            case 2: {
                string3 = arrstring[1];
                break;
            }
            default: {
                string3 = arrstring[2];
            }
        }
        String string4 = arrstring[0].toLowerCase();
        Locale locale2 = locale = string3 == null ? new Locale(string4) : new Locale(string4, string3.toUpperCase());
        if (bl) {
            breakIterator = TextBreakIterator.createIterator(n2, locale);
        } else {
            CacheKey cacheKey = new CacheKey(n2, locale);
            breakIterator = iteratorCache.get(cacheKey);
            if (breakIterator == null) {
                breakIterator = TextBreakIterator.createIterator(n2, locale);
                iteratorCache.put(cacheKey, breakIterator);
            }
        }
        breakIterator.setText(string2);
        return breakIterator;
    }

    private static BreakIterator createIterator(int n2, Locale locale) {
        switch (n2) {
            case 0: {
                return BreakIterator.getCharacterInstance(locale);
            }
            case 1: {
                return BreakIterator.getWordInstance(locale);
            }
            case 2: {
                return BreakIterator.getLineInstance(locale);
            }
            case 3: {
                return BreakIterator.getSentenceInstance(locale);
            }
        }
        throw new IllegalArgumentException("invalid type: " + n2);
    }

    static int invokeMethod(BreakIterator breakIterator, int n2, int n3) {
        CharacterIterator characterIterator = breakIterator.getText();
        int n4 = characterIterator.getEndIndex() - characterIterator.getBeginIndex();
        if (n2 == 5 && n3 > n4) {
            return n4;
        }
        if (n3 < 0 || n3 > n4) {
            n3 = n3 < 0 ? 0 : n4;
        }
        switch (n2) {
            case 0: {
                return breakIterator.first();
            }
            case 1: {
                return breakIterator.last();
            }
            case 2: {
                return breakIterator.next();
            }
            case 3: {
                return breakIterator.previous();
            }
            case 4: {
                return breakIterator.current();
            }
            case 5: {
                return breakIterator.preceding(n3);
            }
            case 6: {
                return breakIterator.following(n3);
            }
            case 7: {
                return breakIterator.isBoundary(n3) ? 1 : 0;
            }
            case 8: {
                return 1;
            }
        }
        throw new IllegalArgumentException("invalid method: " + n2);
    }

    private static final class CacheKey {
        private final int type;
        private final Locale locale;
        private final int hashCode;

        CacheKey(int n2, Locale locale) {
            this.type = n2;
            this.locale = locale;
            this.hashCode = locale.hashCode() + n2;
        }

        public boolean equals(Object object) {
            if (!(object instanceof CacheKey)) {
                return false;
            }
            CacheKey cacheKey = (CacheKey)object;
            return cacheKey.type == this.type && cacheKey.locale.equals(this.locale);
        }

        public int hashCode() {
            return this.hashCode;
        }
    }
}

