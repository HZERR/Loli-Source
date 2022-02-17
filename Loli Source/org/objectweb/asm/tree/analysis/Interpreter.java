/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm.tree.analysis;

import java.util.List;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.Value;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public interface Interpreter<V extends Value> {
    public V newValue(Type var1);

    public V newOperation(AbstractInsnNode var1) throws AnalyzerException;

    public V copyOperation(AbstractInsnNode var1, V var2) throws AnalyzerException;

    public V unaryOperation(AbstractInsnNode var1, V var2) throws AnalyzerException;

    public V binaryOperation(AbstractInsnNode var1, V var2, V var3) throws AnalyzerException;

    public V ternaryOperation(AbstractInsnNode var1, V var2, V var3, V var4) throws AnalyzerException;

    public V naryOperation(AbstractInsnNode var1, List<? extends V> var2) throws AnalyzerException;

    public void returnOperation(AbstractInsnNode var1, V var2, V var3) throws AnalyzerException;

    public V merge(V var1, V var2);
}

