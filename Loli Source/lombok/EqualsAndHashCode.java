/*
 * Decompiled with CFR 0.150.
 */
package lombok;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value={ElementType.TYPE})
@Retention(value=RetentionPolicy.SOURCE)
public @interface EqualsAndHashCode {
    public String[] exclude() default {};

    public String[] of() default {};

    public boolean callSuper() default false;

    public boolean doNotUseGetters() default false;

    public AnyAnnotation[] onParam() default {};

    public boolean onlyExplicitlyIncluded() default false;

    @Deprecated
    @Retention(value=RetentionPolicy.SOURCE)
    @Target(value={})
    public static @interface AnyAnnotation {
    }

    @Target(value={ElementType.FIELD})
    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface Exclude {
    }

    @Target(value={ElementType.FIELD, ElementType.METHOD})
    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface Include {
        public String replaces() default "";
    }
}

