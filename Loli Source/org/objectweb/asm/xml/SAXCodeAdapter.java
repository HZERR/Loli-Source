/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm.xml;

import java.util.HashMap;
import java.util.Map;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodHandle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.util.AbstractVisitor;
import org.objectweb.asm.xml.SAXAdapter;
import org.objectweb.asm.xml.SAXAnnotationAdapter;
import org.objectweb.asm.xml.SAXClassAdapter;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.AttributesImpl;

public final class SAXCodeAdapter
extends SAXAdapter
implements MethodVisitor {
    static final String[] TYPES = new String[]{"top", "int", "float", "double", "long", "null", "uninitializedThis"};
    private final Map<Label, String> labelNames = new HashMap<Label, String>();

    public SAXCodeAdapter(ContentHandler h2, int access) {
        super(h2);
        if ((access & 0x700) == 0) {
            this.addStart("code", new AttributesImpl());
        }
    }

    public final void visitCode() {
    }

    public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
        AttributesImpl attrs = new AttributesImpl();
        switch (type) {
            case -1: 
            case 0: {
                if (type == -1) {
                    attrs.addAttribute("", "type", "type", "", "NEW");
                } else {
                    attrs.addAttribute("", "type", "type", "", "FULL");
                }
                this.addStart("frame", attrs);
                this.appendFrameTypes(true, nLocal, local);
                this.appendFrameTypes(false, nStack, stack);
                break;
            }
            case 1: {
                attrs.addAttribute("", "type", "type", "", "APPEND");
                this.addStart("frame", attrs);
                this.appendFrameTypes(true, nLocal, local);
                break;
            }
            case 2: {
                attrs.addAttribute("", "type", "type", "", "CHOP");
                attrs.addAttribute("", "count", "count", "", Integer.toString(nLocal));
                this.addStart("frame", attrs);
                break;
            }
            case 3: {
                attrs.addAttribute("", "type", "type", "", "SAME");
                this.addStart("frame", attrs);
                break;
            }
            case 4: {
                attrs.addAttribute("", "type", "type", "", "SAME1");
                this.addStart("frame", attrs);
                this.appendFrameTypes(false, 1, stack);
            }
        }
        this.addEnd("frame");
    }

    private void appendFrameTypes(boolean local, int n2, Object[] types) {
        for (int i2 = 0; i2 < n2; ++i2) {
            Object type = types[i2];
            AttributesImpl attrs = new AttributesImpl();
            if (type instanceof String) {
                attrs.addAttribute("", "type", "type", "", (String)type);
            } else if (type instanceof Integer) {
                attrs.addAttribute("", "type", "type", "", TYPES[(Integer)type]);
            } else {
                attrs.addAttribute("", "type", "type", "", "uninitialized");
                attrs.addAttribute("", "label", "label", "", this.getLabel((Label)type));
            }
            this.addElement(local ? "local" : "stack", attrs);
        }
    }

    public final void visitInsn(int opcode) {
        this.addElement(AbstractVisitor.OPCODES[opcode], new AttributesImpl());
    }

    public final void visitIntInsn(int opcode, int operand) {
        AttributesImpl attrs = new AttributesImpl();
        attrs.addAttribute("", "value", "value", "", Integer.toString(operand));
        this.addElement(AbstractVisitor.OPCODES[opcode], attrs);
    }

    public final void visitVarInsn(int opcode, int var2) {
        AttributesImpl attrs = new AttributesImpl();
        attrs.addAttribute("", "var", "var", "", Integer.toString(var2));
        this.addElement(AbstractVisitor.OPCODES[opcode], attrs);
    }

    public final void visitTypeInsn(int opcode, String type) {
        AttributesImpl attrs = new AttributesImpl();
        attrs.addAttribute("", "desc", "desc", "", type);
        this.addElement(AbstractVisitor.OPCODES[opcode], attrs);
    }

    public final void visitFieldInsn(int opcode, String owner, String name, String desc) {
        AttributesImpl attrs = new AttributesImpl();
        attrs.addAttribute("", "owner", "owner", "", owner);
        attrs.addAttribute("", "name", "name", "", name);
        attrs.addAttribute("", "desc", "desc", "", desc);
        this.addElement(AbstractVisitor.OPCODES[opcode], attrs);
    }

    public final void visitMethodInsn(int opcode, String owner, String name, String desc) {
        AttributesImpl attrs = new AttributesImpl();
        attrs.addAttribute("", "owner", "owner", "", owner);
        attrs.addAttribute("", "name", "name", "", name);
        attrs.addAttribute("", "desc", "desc", "", desc);
        this.addElement(AbstractVisitor.OPCODES[opcode], attrs);
    }

    public void visitInvokeDynamicInsn(String name, String desc, MethodHandle bsm, Object ... bsmArgs) {
        AttributesImpl attrs = new AttributesImpl();
        attrs.addAttribute("", "name", "name", "", name);
        attrs.addAttribute("", "desc", "desc", "", desc);
        attrs.addAttribute("", "bsm", "bsm", "", SAXClassAdapter.encode(bsm.toString()));
        this.addStart("INVOKEDYNAMIC", attrs);
        for (int i2 = 0; i2 < bsmArgs.length; ++i2) {
            this.addElement("bsmArg", SAXCodeAdapter.getConstantAttribute(bsmArgs[i2]));
        }
        this.addEnd("INVOKEDYNAMIC");
    }

    public final void visitJumpInsn(int opcode, Label label) {
        AttributesImpl attrs = new AttributesImpl();
        attrs.addAttribute("", "label", "label", "", this.getLabel(label));
        this.addElement(AbstractVisitor.OPCODES[opcode], attrs);
    }

    public final void visitLabel(Label label) {
        AttributesImpl attrs = new AttributesImpl();
        attrs.addAttribute("", "name", "name", "", this.getLabel(label));
        this.addElement("Label", attrs);
    }

    public final void visitLdcInsn(Object cst) {
        this.addElement(AbstractVisitor.OPCODES[18], SAXCodeAdapter.getConstantAttribute(cst));
    }

    private static AttributesImpl getConstantAttribute(Object cst) {
        AttributesImpl attrs = new AttributesImpl();
        attrs.addAttribute("", "cst", "cst", "", SAXClassAdapter.encode(cst.toString()));
        attrs.addAttribute("", "desc", "desc", "", Type.getDescriptor(cst.getClass()));
        return attrs;
    }

    public final void visitIincInsn(int var2, int increment) {
        AttributesImpl attrs = new AttributesImpl();
        attrs.addAttribute("", "var", "var", "", Integer.toString(var2));
        attrs.addAttribute("", "inc", "inc", "", Integer.toString(increment));
        this.addElement(AbstractVisitor.OPCODES[132], attrs);
    }

    public final void visitTableSwitchInsn(int min, int max, Label dflt, Label ... labels) {
        AttributesImpl attrs = new AttributesImpl();
        attrs.addAttribute("", "min", "min", "", Integer.toString(min));
        attrs.addAttribute("", "max", "max", "", Integer.toString(max));
        attrs.addAttribute("", "dflt", "dflt", "", this.getLabel(dflt));
        String o2 = AbstractVisitor.OPCODES[170];
        this.addStart(o2, attrs);
        for (int i2 = 0; i2 < labels.length; ++i2) {
            AttributesImpl att2 = new AttributesImpl();
            att2.addAttribute("", "name", "name", "", this.getLabel(labels[i2]));
            this.addElement("label", att2);
        }
        this.addEnd(o2);
    }

    public final void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        AttributesImpl att = new AttributesImpl();
        att.addAttribute("", "dflt", "dflt", "", this.getLabel(dflt));
        String o2 = AbstractVisitor.OPCODES[171];
        this.addStart(o2, att);
        for (int i2 = 0; i2 < labels.length; ++i2) {
            AttributesImpl att2 = new AttributesImpl();
            att2.addAttribute("", "name", "name", "", this.getLabel(labels[i2]));
            att2.addAttribute("", "key", "key", "", Integer.toString(keys[i2]));
            this.addElement("label", att2);
        }
        this.addEnd(o2);
    }

    public final void visitMultiANewArrayInsn(String desc, int dims) {
        AttributesImpl attrs = new AttributesImpl();
        attrs.addAttribute("", "desc", "desc", "", desc);
        attrs.addAttribute("", "dims", "dims", "", Integer.toString(dims));
        this.addElement(AbstractVisitor.OPCODES[197], attrs);
    }

    public final void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        AttributesImpl attrs = new AttributesImpl();
        attrs.addAttribute("", "start", "start", "", this.getLabel(start));
        attrs.addAttribute("", "end", "end", "", this.getLabel(end));
        attrs.addAttribute("", "handler", "handler", "", this.getLabel(handler));
        if (type != null) {
            attrs.addAttribute("", "type", "type", "", type);
        }
        this.addElement("TryCatch", attrs);
    }

    public final void visitMaxs(int maxStack, int maxLocals) {
        AttributesImpl attrs = new AttributesImpl();
        attrs.addAttribute("", "maxStack", "maxStack", "", Integer.toString(maxStack));
        attrs.addAttribute("", "maxLocals", "maxLocals", "", Integer.toString(maxLocals));
        this.addElement("Max", attrs);
        this.addEnd("code");
    }

    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
        AttributesImpl attrs = new AttributesImpl();
        attrs.addAttribute("", "name", "name", "", name);
        attrs.addAttribute("", "desc", "desc", "", desc);
        if (signature != null) {
            attrs.addAttribute("", "signature", "signature", "", SAXClassAdapter.encode(signature));
        }
        attrs.addAttribute("", "start", "start", "", this.getLabel(start));
        attrs.addAttribute("", "end", "end", "", this.getLabel(end));
        attrs.addAttribute("", "var", "var", "", Integer.toString(index));
        this.addElement("LocalVar", attrs);
    }

    public final void visitLineNumber(int line, Label start) {
        AttributesImpl attrs = new AttributesImpl();
        attrs.addAttribute("", "line", "line", "", Integer.toString(line));
        attrs.addAttribute("", "start", "start", "", this.getLabel(start));
        this.addElement("LineNumber", attrs);
    }

    public AnnotationVisitor visitAnnotationDefault() {
        return new SAXAnnotationAdapter(this.getContentHandler(), "annotationDefault", 0, null, null);
    }

    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        return new SAXAnnotationAdapter(this.getContentHandler(), "annotation", visible ? 1 : -1, null, desc);
    }

    public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible) {
        return new SAXAnnotationAdapter(this.getContentHandler(), "parameterAnnotation", visible ? 1 : -1, parameter, desc);
    }

    public void visitEnd() {
        this.addEnd("method");
    }

    private final String getLabel(Label label) {
        String name = this.labelNames.get(label);
        if (name == null) {
            name = Integer.toString(this.labelNames.size());
            this.labelNames.put(label, name);
        }
        return name;
    }
}

