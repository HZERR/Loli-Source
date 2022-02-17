/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.CharacterDataImpl;
import com.sun.webkit.dom.StyleSheetImpl;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.stylesheets.StyleSheet;

public class ProcessingInstructionImpl
extends CharacterDataImpl
implements ProcessingInstruction {
    ProcessingInstructionImpl(long l2) {
        super(l2);
    }

    static Node getImpl(long l2) {
        return ProcessingInstructionImpl.create(l2);
    }

    @Override
    public String getTarget() {
        return ProcessingInstructionImpl.getTargetImpl(this.getPeer());
    }

    static native String getTargetImpl(long var0);

    public StyleSheet getSheet() {
        return StyleSheetImpl.getImpl(ProcessingInstructionImpl.getSheetImpl(this.getPeer()));
    }

    static native long getSheetImpl(long var0);
}

