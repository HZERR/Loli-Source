/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.chart;

import javafx.collections.ObservableList;
import javafx.scene.chart.AxisBuilder;
import javafx.scene.chart.CategoryAxis;

@Deprecated
public final class CategoryAxisBuilder
extends AxisBuilder<String, CategoryAxisBuilder> {
    private int __set;
    private ObservableList<String> categories;
    private double endMargin;
    private boolean gapStartAndEnd;
    private double startMargin;

    protected CategoryAxisBuilder() {
    }

    public static CategoryAxisBuilder create() {
        return new CategoryAxisBuilder();
    }

    public void applyTo(CategoryAxis categoryAxis) {
        super.applyTo(categoryAxis);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            categoryAxis.setCategories(this.categories);
        }
        if ((n2 & 2) != 0) {
            categoryAxis.setEndMargin(this.endMargin);
        }
        if ((n2 & 4) != 0) {
            categoryAxis.setGapStartAndEnd(this.gapStartAndEnd);
        }
        if ((n2 & 8) != 0) {
            categoryAxis.setStartMargin(this.startMargin);
        }
    }

    public CategoryAxisBuilder categories(ObservableList<String> observableList) {
        this.categories = observableList;
        this.__set |= 1;
        return this;
    }

    public CategoryAxisBuilder endMargin(double d2) {
        this.endMargin = d2;
        this.__set |= 2;
        return this;
    }

    public CategoryAxisBuilder gapStartAndEnd(boolean bl) {
        this.gapStartAndEnd = bl;
        this.__set |= 4;
        return this;
    }

    public CategoryAxisBuilder startMargin(double d2) {
        this.startMargin = d2;
        this.__set |= 8;
        return this;
    }

    @Override
    public CategoryAxis build() {
        CategoryAxis categoryAxis = new CategoryAxis();
        this.applyTo(categoryAxis);
        return categoryAxis;
    }
}

