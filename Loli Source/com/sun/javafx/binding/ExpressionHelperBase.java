/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.binding;

import javafx.beans.WeakListener;

public class ExpressionHelperBase {
    protected static int trim(int n2, Object[] arrobject) {
        for (int i2 = 0; i2 < n2; ++i2) {
            Object object = arrobject[i2];
            if (!(object instanceof WeakListener) || !((WeakListener)object).wasGarbageCollected()) continue;
            int n3 = n2 - i2 - 1;
            if (n3 > 0) {
                System.arraycopy(arrobject, i2 + 1, arrobject, i2, n3);
            }
            arrobject[--n2] = null;
            --i2;
        }
        return n2;
    }
}

