/*
 * Decompiled with CFR 0.150.
 */
package lombok;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class Lombok {
    public static RuntimeException sneakyThrow(Throwable t2) {
        if (t2 == null) {
            throw new NullPointerException("t");
        }
        return (RuntimeException)Lombok.sneakyThrow0(t2);
    }

    private static <T extends Throwable> T sneakyThrow0(Throwable t2) throws T {
        throw t2;
    }

    public static <T> T preventNullAnalysis(T value) {
        return value;
    }

    public static <T> T checkNotNull(T value, String message) {
        if (value == null) {
            throw new NullPointerException(message);
        }
        return value;
    }
}

