/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InnerClassNode;
import org.objectweb.asm.tree.MemberNode;
import org.objectweb.asm.tree.MethodNode;

public class ClassNode
extends MemberNode
implements ClassVisitor {
    public int version;
    public int access;
    public String name;
    public String signature;
    public String superName;
    public List<String> interfaces = new ArrayList<String>();
    public String sourceFile;
    public String sourceDebug;
    public String outerClass;
    public String outerMethod;
    public String outerMethodDesc;
    public List<InnerClassNode> innerClasses = new ArrayList<InnerClassNode>();
    public List<FieldNode> fields = new ArrayList<FieldNode>();
    public List<MethodNode> methods = new ArrayList<MethodNode>();

    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.version = version;
        this.access = access;
        this.name = name;
        this.signature = signature;
        this.superName = superName;
        if (interfaces != null) {
            this.interfaces.addAll(Arrays.asList(interfaces));
        }
    }

    public void visitSource(String file, String debug) {
        this.sourceFile = file;
        this.sourceDebug = debug;
    }

    public void visitOuterClass(String owner, String name, String desc) {
        this.outerClass = owner;
        this.outerMethod = name;
        this.outerMethodDesc = desc;
    }

    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        InnerClassNode icn = new InnerClassNode(name, outerName, innerName, access);
        this.innerClasses.add(icn);
    }

    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        FieldNode fn = new FieldNode(access, name, desc, signature, value);
        this.fields.add(fn);
        return fn;
    }

    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodNode mn = new MethodNode(access, name, desc, signature, exceptions);
        this.methods.add(mn);
        return mn;
    }

    public void accept(ClassVisitor cv) {
        AnnotationNode an;
        int i2;
        String[] interfaces = new String[this.interfaces.size()];
        this.interfaces.toArray(interfaces);
        cv.visit(this.version, this.access, this.name, this.signature, this.superName, interfaces);
        if (this.sourceFile != null || this.sourceDebug != null) {
            cv.visitSource(this.sourceFile, this.sourceDebug);
        }
        if (this.outerClass != null) {
            cv.visitOuterClass(this.outerClass, this.outerMethod, this.outerMethodDesc);
        }
        int n2 = this.visibleAnnotations == null ? 0 : this.visibleAnnotations.size();
        for (i2 = 0; i2 < n2; ++i2) {
            an = (AnnotationNode)this.visibleAnnotations.get(i2);
            an.accept(cv.visitAnnotation(an.desc, true));
        }
        n2 = this.invisibleAnnotations == null ? 0 : this.invisibleAnnotations.size();
        for (i2 = 0; i2 < n2; ++i2) {
            an = (AnnotationNode)this.invisibleAnnotations.get(i2);
            an.accept(cv.visitAnnotation(an.desc, false));
        }
        n2 = this.attrs == null ? 0 : this.attrs.size();
        for (i2 = 0; i2 < n2; ++i2) {
            cv.visitAttribute((Attribute)this.attrs.get(i2));
        }
        for (i2 = 0; i2 < this.innerClasses.size(); ++i2) {
            this.innerClasses.get(i2).accept(cv);
        }
        for (i2 = 0; i2 < this.fields.size(); ++i2) {
            this.fields.get(i2).accept(cv);
        }
        for (i2 = 0; i2 < this.methods.size(); ++i2) {
            this.methods.get(i2).accept(cv);
        }
        cv.visitEnd();
    }
}

