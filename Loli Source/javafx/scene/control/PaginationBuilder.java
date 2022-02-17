/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import javafx.scene.Node;
import javafx.scene.control.ControlBuilder;
import javafx.scene.control.Pagination;
import javafx.util.Builder;
import javafx.util.Callback;

@Deprecated
public class PaginationBuilder<B extends PaginationBuilder<B>>
extends ControlBuilder<B>
implements Builder<Pagination> {
    private int __set;
    private int currentPageIndex;
    private int maxPageIndicatorCount;
    private int pageCount;
    private Callback<Integer, Node> pageFactory;

    protected PaginationBuilder() {
    }

    public static PaginationBuilder<?> create() {
        return new PaginationBuilder();
    }

    public void applyTo(Pagination pagination) {
        super.applyTo(pagination);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            pagination.setCurrentPageIndex(this.currentPageIndex);
        }
        if ((n2 & 2) != 0) {
            pagination.setMaxPageIndicatorCount(this.maxPageIndicatorCount);
        }
        if ((n2 & 4) != 0) {
            pagination.setPageCount(this.pageCount);
        }
        if ((n2 & 8) != 0) {
            pagination.setPageFactory(this.pageFactory);
        }
    }

    public B currentPageIndex(int n2) {
        this.currentPageIndex = n2;
        this.__set |= 1;
        return (B)this;
    }

    public B maxPageIndicatorCount(int n2) {
        this.maxPageIndicatorCount = n2;
        this.__set |= 2;
        return (B)this;
    }

    public B pageCount(int n2) {
        this.pageCount = n2;
        this.__set |= 4;
        return (B)this;
    }

    public B pageFactory(Callback<Integer, Node> callback) {
        this.pageFactory = callback;
        this.__set |= 8;
        return (B)this;
    }

    @Override
    public Pagination build() {
        Pagination pagination = new Pagination();
        this.applyTo(pagination);
        return pagination;
    }
}

