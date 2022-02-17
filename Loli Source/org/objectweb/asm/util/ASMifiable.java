/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm.util;

import java.util.Map;
import org.objectweb.asm.Label;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public interface ASMifiable {
    public void asmify(StringBuffer var1, String var2, Map<Label, String> var3);
}

