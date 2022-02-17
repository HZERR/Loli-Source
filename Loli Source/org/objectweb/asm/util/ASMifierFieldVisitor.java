/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm.util;

import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.util.ASMifierAbstractVisitor;

public class ASMifierFieldVisitor
extends ASMifierAbstractVisitor
implements FieldVisitor {
    public ASMifierFieldVisitor() {
        super("fv");
    }
}

