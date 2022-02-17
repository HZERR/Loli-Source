/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm.commons;

import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.signature.SignatureVisitor;

public class RemappingSignatureAdapter
implements SignatureVisitor {
    private final SignatureVisitor v;
    private final Remapper remapper;
    private String className;

    public RemappingSignatureAdapter(SignatureVisitor v2, Remapper remapper) {
        this.v = v2;
        this.remapper = remapper;
    }

    public void visitClassType(String name) {
        this.className = name;
        this.v.visitClassType(this.remapper.mapType(name));
    }

    public void visitInnerClassType(String name) {
        this.className = this.className + '$' + name;
        String remappedName = this.remapper.mapType(this.className);
        this.v.visitInnerClassType(remappedName.substring(remappedName.lastIndexOf(36) + 1));
    }

    public void visitFormalTypeParameter(String name) {
        this.v.visitFormalTypeParameter(name);
    }

    public void visitTypeVariable(String name) {
        this.v.visitTypeVariable(name);
    }

    public SignatureVisitor visitArrayType() {
        this.v.visitArrayType();
        return this;
    }

    public void visitBaseType(char descriptor) {
        this.v.visitBaseType(descriptor);
    }

    public SignatureVisitor visitClassBound() {
        this.v.visitClassBound();
        return this;
    }

    public SignatureVisitor visitExceptionType() {
        this.v.visitExceptionType();
        return this;
    }

    public SignatureVisitor visitInterface() {
        this.v.visitInterface();
        return this;
    }

    public SignatureVisitor visitInterfaceBound() {
        this.v.visitInterfaceBound();
        return this;
    }

    public SignatureVisitor visitParameterType() {
        this.v.visitParameterType();
        return this;
    }

    public SignatureVisitor visitReturnType() {
        this.v.visitReturnType();
        return this;
    }

    public SignatureVisitor visitSuperclass() {
        this.v.visitSuperclass();
        return this;
    }

    public void visitTypeArgument() {
        this.v.visitTypeArgument();
    }

    public SignatureVisitor visitTypeArgument(char wildcard) {
        this.v.visitTypeArgument(wildcard);
        return this;
    }

    public void visitEnd() {
        this.v.visitEnd();
    }
}

