/*
 * Decompiled with CFR 0.150.
 */
package javafx.beans;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.PARAMETER})
public @interface NamedArg {
    public String value();

    public String defaultValue() default "";
}

