/*
 * Decompiled with CFR 0.150.
 */
package com.sun.jna;

import java.lang.reflect.InvocationTargetException;

abstract class Klass {
    private Klass() {
    }

    public static <T> T newInstance(Class<T> klass) {
        try {
            return klass.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        }
        catch (IllegalAccessException e2) {
            String msg = "Can't create an instance of " + klass + ", requires a public no-arg constructor: " + e2;
            throw new IllegalArgumentException(msg, e2);
        }
        catch (IllegalArgumentException e3) {
            String msg = "Can't create an instance of " + klass + ", requires a public no-arg constructor: " + e3;
            throw new IllegalArgumentException(msg, e3);
        }
        catch (InstantiationException e4) {
            String msg = "Can't create an instance of " + klass + ", requires a public no-arg constructor: " + e4;
            throw new IllegalArgumentException(msg, e4);
        }
        catch (NoSuchMethodException e5) {
            String msg = "Can't create an instance of " + klass + ", requires a public no-arg constructor: " + e5;
            throw new IllegalArgumentException(msg, e5);
        }
        catch (SecurityException e6) {
            String msg = "Can't create an instance of " + klass + ", requires a public no-arg constructor: " + e6;
            throw new IllegalArgumentException(msg, e6);
        }
        catch (InvocationTargetException e7) {
            if (e7.getCause() instanceof RuntimeException) {
                throw (RuntimeException)e7.getCause();
            }
            String msg = "Can't create an instance of " + klass + ", requires a public no-arg constructor: " + e7;
            throw new IllegalArgumentException(msg, e7);
        }
    }
}

