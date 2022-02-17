/*
 * Decompiled with CFR 0.150.
 */
package javafx.print;

import javafx.beans.NamedArg;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;

public final class PageRange {
    private ReadOnlyIntegerWrapper startPage;
    private ReadOnlyIntegerWrapper endPage;

    public PageRange(@NamedArg(value="startPage") int n2, @NamedArg(value="endPage") int n3) {
        if (n2 <= 0 || n2 > n3) {
            throw new IllegalArgumentException("Invalid range : " + n2 + " -> " + n3);
        }
        this.startPageImplProperty().set(n2);
        this.endPageImplProperty().set(n3);
    }

    private ReadOnlyIntegerWrapper startPageImplProperty() {
        if (this.startPage == null) {
            this.startPage = new ReadOnlyIntegerWrapper(this, "startPage", 1){

                @Override
                public void set(int n2) {
                    if (n2 <= 0 || PageRange.this.endPage != null && n2 < PageRange.this.endPage.get()) {
                        return;
                    }
                    super.set(n2);
                }
            };
        }
        return this.startPage;
    }

    public ReadOnlyIntegerProperty startPageProperty() {
        return this.startPageImplProperty().getReadOnlyProperty();
    }

    public int getStartPage() {
        return this.startPageProperty().get();
    }

    private ReadOnlyIntegerWrapper endPageImplProperty() {
        if (this.endPage == null) {
            this.endPage = new ReadOnlyIntegerWrapper(this, "endPage", 9999){

                @Override
                public void set(int n2) {
                    if (n2 <= 0 || PageRange.this.startPage != null && n2 < PageRange.this.startPage.get()) {
                        return;
                    }
                    super.set(n2);
                }
            };
        }
        return this.endPage;
    }

    public ReadOnlyIntegerProperty endPageProperty() {
        return this.endPageImplProperty().getReadOnlyProperty();
    }

    public int getEndPage() {
        return this.endPageProperty().get();
    }
}

