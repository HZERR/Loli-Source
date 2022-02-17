/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.tk.quantum;

import com.sun.javafx.tk.quantum.GestureRecognizer;
import java.util.Collection;
import java.util.Vector;

class GestureRecognizers
implements GestureRecognizer {
    private Collection<GestureRecognizer> recognizers = new Vector<GestureRecognizer>();
    private GestureRecognizer[] workList;

    GestureRecognizers() {
    }

    void add(GestureRecognizer gestureRecognizer) {
        if (!this.contains(gestureRecognizer)) {
            this.recognizers.add(gestureRecognizer);
            this.workList = null;
        }
    }

    void remove(GestureRecognizer gestureRecognizer) {
        if (this.contains(gestureRecognizer)) {
            this.recognizers.remove(gestureRecognizer);
            this.workList = null;
        }
    }

    boolean contains(GestureRecognizer gestureRecognizer) {
        return this.recognizers.contains(gestureRecognizer);
    }

    private GestureRecognizer[] synchWorkList() {
        if (this.workList == null) {
            this.workList = this.recognizers.toArray(new GestureRecognizer[0]);
        }
        return this.workList;
    }

    @Override
    public void notifyBeginTouchEvent(long l2, int n2, boolean bl, int n3) {
        GestureRecognizer[] arrgestureRecognizer = this.synchWorkList();
        for (int i2 = 0; i2 != arrgestureRecognizer.length; ++i2) {
            arrgestureRecognizer[i2].notifyBeginTouchEvent(l2, n2, bl, n3);
        }
    }

    @Override
    public void notifyNextTouchEvent(long l2, int n2, long l3, int n3, int n4, int n5, int n6) {
        GestureRecognizer[] arrgestureRecognizer = this.synchWorkList();
        for (int i2 = 0; i2 != arrgestureRecognizer.length; ++i2) {
            arrgestureRecognizer[i2].notifyNextTouchEvent(l2, n2, l3, n3, n4, n5, n6);
        }
    }

    @Override
    public void notifyEndTouchEvent(long l2) {
        GestureRecognizer[] arrgestureRecognizer = this.synchWorkList();
        for (int i2 = 0; i2 != arrgestureRecognizer.length; ++i2) {
            arrgestureRecognizer[i2].notifyEndTouchEvent(l2);
        }
    }
}

