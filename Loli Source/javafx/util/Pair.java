/*
 * Decompiled with CFR 0.150.
 */
package javafx.util;

import java.io.Serializable;
import javafx.beans.NamedArg;

public class Pair<K, V>
implements Serializable {
    private K key;
    private V value;

    public K getKey() {
        return this.key;
    }

    public V getValue() {
        return this.value;
    }

    public Pair(@NamedArg(value="key") K k2, @NamedArg(value="value") V v2) {
        this.key = k2;
        this.value = v2;
    }

    public String toString() {
        return this.key + "=" + this.value;
    }

    public int hashCode() {
        return this.key.hashCode() * 13 + (this.value == null ? 0 : this.value.hashCode());
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof Pair) {
            Pair pair = (Pair)object;
            if (this.key != null ? !this.key.equals(pair.key) : pair.key != null) {
                return false;
            }
            return !(this.value != null ? !this.value.equals(pair.value) : pair.value != null);
        }
        return false;
    }
}

