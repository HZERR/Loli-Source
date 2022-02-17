/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.jmx;

import com.sun.javafx.tk.TKScene;
import javafx.geometry.Rectangle2D;

public class HighlightRegion
extends Rectangle2D {
    private TKScene tkScene;
    private int hash = 0;

    public HighlightRegion(TKScene tKScene, double d2, double d3, double d4, double d5) {
        super(d2, d3, d4, d5);
        this.tkScene = tKScene;
    }

    public TKScene getTKScene() {
        return this.tkScene;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof HighlightRegion) {
            HighlightRegion highlightRegion = (HighlightRegion)object;
            return this.tkScene.equals(highlightRegion.tkScene) && super.equals(highlightRegion);
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (this.hash == 0) {
            long l2 = 7L;
            l2 = 31L * l2 + (long)super.hashCode();
            l2 = 31L * l2 + (long)this.tkScene.hashCode();
            this.hash = (int)(l2 ^ l2 >> 32);
        }
        return this.hash;
    }

    @Override
    public String toString() {
        return "HighlighRegion [tkScene = " + this.tkScene + ", rectangle = " + super.toString() + "]";
    }
}

