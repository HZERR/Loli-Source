/*
 * Decompiled with CFR 0.150.
 */
package javafx.util;

import javafx.util.Builder;

@FunctionalInterface
public interface BuilderFactory {
    public Builder<?> getBuilder(Class<?> var1);
}

