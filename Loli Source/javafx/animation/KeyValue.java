/*
 * Decompiled with CFR 0.150.
 */
package javafx.animation;

import javafx.animation.Interpolator;
import javafx.beans.NamedArg;
import javafx.beans.value.WritableBooleanValue;
import javafx.beans.value.WritableDoubleValue;
import javafx.beans.value.WritableFloatValue;
import javafx.beans.value.WritableIntegerValue;
import javafx.beans.value.WritableLongValue;
import javafx.beans.value.WritableNumberValue;
import javafx.beans.value.WritableValue;

public final class KeyValue {
    private static final Interpolator DEFAULT_INTERPOLATOR = Interpolator.LINEAR;
    private final Type type;
    private final WritableValue<?> target;
    private final Object endValue;
    private final Interpolator interpolator;

    @Deprecated
    public Type getType() {
        return this.type;
    }

    public WritableValue<?> getTarget() {
        return this.target;
    }

    public Object getEndValue() {
        return this.endValue;
    }

    public Interpolator getInterpolator() {
        return this.interpolator;
    }

    public <T> KeyValue(@NamedArg(value="target") WritableValue<T> writableValue, @NamedArg(value="endValue") T t2, @NamedArg(value="interpolator") Interpolator interpolator) {
        if (writableValue == null) {
            throw new NullPointerException("Target needs to be specified");
        }
        if (interpolator == null) {
            throw new NullPointerException("Interpolator needs to be specified");
        }
        this.target = writableValue;
        this.endValue = t2;
        this.interpolator = interpolator;
        this.type = writableValue instanceof WritableNumberValue ? (writableValue instanceof WritableDoubleValue ? Type.DOUBLE : (writableValue instanceof WritableIntegerValue ? Type.INTEGER : (writableValue instanceof WritableFloatValue ? Type.FLOAT : (writableValue instanceof WritableLongValue ? Type.LONG : Type.OBJECT)))) : (writableValue instanceof WritableBooleanValue ? Type.BOOLEAN : Type.OBJECT);
    }

    public <T> KeyValue(@NamedArg(value="target") WritableValue<T> writableValue, @NamedArg(value="endValue") T t2) {
        this(writableValue, t2, DEFAULT_INTERPOLATOR);
    }

    public String toString() {
        return "KeyValue [target=" + this.target + ", endValue=" + this.endValue + ", interpolator=" + this.interpolator + "]";
    }

    public int hashCode() {
        assert (this.target != null && this.interpolator != null);
        int n2 = 1;
        n2 = 31 * n2 + this.target.hashCode();
        n2 = 31 * n2 + (this.endValue == null ? 0 : this.endValue.hashCode());
        n2 = 31 * n2 + this.interpolator.hashCode();
        return n2;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof KeyValue) {
            KeyValue keyValue = (KeyValue)object;
            assert (this.target != null && this.interpolator != null && keyValue.target != null && keyValue.interpolator != null);
            return this.target.equals(keyValue.target) && (this.endValue == null ? keyValue.endValue == null : this.endValue.equals(keyValue.endValue)) && this.interpolator.equals(keyValue.interpolator);
        }
        return false;
    }

    @Deprecated
    public static enum Type {
        BOOLEAN,
        DOUBLE,
        FLOAT,
        INTEGER,
        LONG,
        OBJECT;

    }
}

