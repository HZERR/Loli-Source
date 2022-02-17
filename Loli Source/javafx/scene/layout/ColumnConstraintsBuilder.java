/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.layout;

import javafx.geometry.HPos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.util.Builder;

@Deprecated
public class ColumnConstraintsBuilder<B extends ColumnConstraintsBuilder<B>>
implements Builder<ColumnConstraints> {
    private int __set;
    private boolean fillWidth;
    private HPos halignment;
    private Priority hgrow;
    private double maxWidth;
    private double minWidth;
    private double percentWidth;
    private double prefWidth;

    protected ColumnConstraintsBuilder() {
    }

    public static ColumnConstraintsBuilder<?> create() {
        return new ColumnConstraintsBuilder();
    }

    public void applyTo(ColumnConstraints columnConstraints) {
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            columnConstraints.setFillWidth(this.fillWidth);
        }
        if ((n2 & 2) != 0) {
            columnConstraints.setHalignment(this.halignment);
        }
        if ((n2 & 4) != 0) {
            columnConstraints.setHgrow(this.hgrow);
        }
        if ((n2 & 8) != 0) {
            columnConstraints.setMaxWidth(this.maxWidth);
        }
        if ((n2 & 0x10) != 0) {
            columnConstraints.setMinWidth(this.minWidth);
        }
        if ((n2 & 0x20) != 0) {
            columnConstraints.setPercentWidth(this.percentWidth);
        }
        if ((n2 & 0x40) != 0) {
            columnConstraints.setPrefWidth(this.prefWidth);
        }
    }

    public B fillWidth(boolean bl) {
        this.fillWidth = bl;
        this.__set |= 1;
        return (B)this;
    }

    public B halignment(HPos hPos) {
        this.halignment = hPos;
        this.__set |= 2;
        return (B)this;
    }

    public B hgrow(Priority priority) {
        this.hgrow = priority;
        this.__set |= 4;
        return (B)this;
    }

    public B maxWidth(double d2) {
        this.maxWidth = d2;
        this.__set |= 8;
        return (B)this;
    }

    public B minWidth(double d2) {
        this.minWidth = d2;
        this.__set |= 0x10;
        return (B)this;
    }

    public B percentWidth(double d2) {
        this.percentWidth = d2;
        this.__set |= 0x20;
        return (B)this;
    }

    public B prefWidth(double d2) {
        this.prefWidth = d2;
        this.__set |= 0x40;
        return (B)this;
    }

    @Override
    public ColumnConstraints build() {
        ColumnConstraints columnConstraints = new ColumnConstraints();
        this.applyTo(columnConstraints);
        return columnConstraints;
    }
}

