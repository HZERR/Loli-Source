/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import javafx.scene.control.IndexRange;
import javafx.util.Builder;

@Deprecated
public final class IndexRangeBuilder
implements Builder<IndexRange> {
    private int end;
    private int start;

    protected IndexRangeBuilder() {
    }

    public static IndexRangeBuilder create() {
        return new IndexRangeBuilder();
    }

    public IndexRangeBuilder end(int n2) {
        this.end = n2;
        return this;
    }

    public IndexRangeBuilder start(int n2) {
        this.start = n2;
        return this;
    }

    @Override
    public IndexRange build() {
        IndexRange indexRange = new IndexRange(this.start, this.end);
        return indexRange;
    }
}

