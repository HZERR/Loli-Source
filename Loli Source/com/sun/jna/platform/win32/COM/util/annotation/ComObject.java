/*
 * Decompiled with CFR 0.150.
 */
package com.sun.jna.platform.win32.COM.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.TYPE})
@Inherited
public @interface ComObject {
    public String clsId() default "";

    public String progId() default "";
}

