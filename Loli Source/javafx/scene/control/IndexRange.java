/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import javafx.beans.NamedArg;

public final class IndexRange {
    private int start;
    private int end;
    public static final String VALUE_DELIMITER = ",";

    public IndexRange(@NamedArg(value="start") int n2, @NamedArg(value="end") int n3) {
        if (n3 < n2) {
            throw new IllegalArgumentException();
        }
        this.start = n2;
        this.end = n3;
    }

    public IndexRange(@NamedArg(value="range") IndexRange indexRange) {
        this.start = indexRange.start;
        this.end = indexRange.end;
    }

    public int getStart() {
        return this.start;
    }

    public int getEnd() {
        return this.end;
    }

    public int getLength() {
        return this.end - this.start;
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof IndexRange) {
            IndexRange indexRange = (IndexRange)object;
            return this.start == indexRange.start && this.end == indexRange.end;
        }
        return false;
    }

    public int hashCode() {
        return 31 * this.start + this.end;
    }

    public String toString() {
        return this.start + VALUE_DELIMITER + " " + this.end;
    }

    public static IndexRange normalize(int n2, int n3) {
        return new IndexRange(Math.min(n2, n3), Math.max(n2, n3));
    }

    public static IndexRange valueOf(String string) {
        if (string == null) {
            throw new IllegalArgumentException();
        }
        String[] arrstring = string.split(VALUE_DELIMITER);
        if (arrstring.length != 2) {
            throw new IllegalArgumentException();
        }
        int n2 = Integer.parseInt(arrstring[0].trim());
        int n3 = Integer.parseInt(arrstring[1].trim());
        return IndexRange.normalize(n2, n3);
    }
}

