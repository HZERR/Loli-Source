/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodHandle;
import org.objectweb.asm.MethodType;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.tree.analysis.BasicValue;
import org.objectweb.asm.tree.analysis.BasicVerifier;
import org.objectweb.asm.util.CheckAnnotationAdapter;
import org.objectweb.asm.util.CheckClassAdapter;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class CheckMethodAdapter
extends MethodAdapter {
    public int version;
    private boolean startCode;
    private boolean endCode;
    private boolean endMethod;
    private final Map<Label, Integer> labels;
    private static final int[] TYPE;
    private static Field labelStatusField;

    public CheckMethodAdapter(MethodVisitor mv) {
        this(mv, new HashMap<Label, Integer>());
    }

    public CheckMethodAdapter(MethodVisitor mv, Map<Label, Integer> labels) {
        super(mv);
        this.labels = labels;
    }

    public CheckMethodAdapter(int access, String name, String desc, final MethodVisitor mv, Map<Label, Integer> labels) {
        this(new MethodNode(access, name, desc, null, null){

            public void visitEnd() {
                Analyzer<BasicValue> a2 = new Analyzer<BasicValue>(new BasicVerifier());
                try {
                    a2.analyze("dummy", this);
                }
                catch (Exception e2) {
                    if (e2 instanceof IndexOutOfBoundsException && this.maxLocals == 0 && this.maxStack == 0) {
                        throw new RuntimeException("Data flow checking option requires valid, non zero maxLocals and maxStack values.");
                    }
                    e2.printStackTrace();
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw, true);
                    CheckClassAdapter.printAnalyzerResult(this, a2, pw);
                    pw.close();
                    throw new RuntimeException(e2.getMessage() + ' ' + sw.toString());
                }
                this.accept(mv);
            }
        }, labels);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        this.checkEndMethod();
        CheckMethodAdapter.checkDesc(desc, false);
        return new CheckAnnotationAdapter(this.mv.visitAnnotation(desc, visible));
    }

    @Override
    public AnnotationVisitor visitAnnotationDefault() {
        this.checkEndMethod();
        return new CheckAnnotationAdapter(this.mv.visitAnnotationDefault(), false);
    }

    @Override
    public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible) {
        this.checkEndMethod();
        CheckMethodAdapter.checkDesc(desc, false);
        return new CheckAnnotationAdapter(this.mv.visitParameterAnnotation(parameter, desc, visible));
    }

    @Override
    public void visitAttribute(Attribute attr) {
        this.checkEndMethod();
        if (attr == null) {
            throw new IllegalArgumentException("Invalid attribute (must not be null)");
        }
        this.mv.visitAttribute(attr);
    }

    @Override
    public void visitCode() {
        this.startCode = true;
        this.mv.visitCode();
    }

    @Override
    public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
        int i2;
        int mStack;
        int mLocal;
        switch (type) {
            case -1: 
            case 0: {
                mLocal = Integer.MAX_VALUE;
                mStack = Integer.MAX_VALUE;
                break;
            }
            case 3: {
                mLocal = 0;
                mStack = 0;
                break;
            }
            case 4: {
                mLocal = 0;
                mStack = 1;
                break;
            }
            case 1: 
            case 2: {
                mLocal = 3;
                mStack = 0;
                break;
            }
            default: {
                throw new IllegalArgumentException("Invalid frame type " + type);
            }
        }
        if (nLocal > mLocal) {
            throw new IllegalArgumentException("Invalid nLocal=" + nLocal + " for frame type " + type);
        }
        if (nStack > mStack) {
            throw new IllegalArgumentException("Invalid nStack=" + nStack + " for frame type " + type);
        }
        if (type != 2) {
            if (nLocal > 0 && (local == null || local.length < nLocal)) {
                throw new IllegalArgumentException("Array local[] is shorter than nLocal");
            }
            for (i2 = 0; i2 < nLocal; ++i2) {
                CheckMethodAdapter.checkFrameValue(local[i2]);
            }
        }
        if (nStack > 0 && (stack == null || stack.length < nStack)) {
            throw new IllegalArgumentException("Array stack[] is shorter than nStack");
        }
        for (i2 = 0; i2 < nStack; ++i2) {
            CheckMethodAdapter.checkFrameValue(stack[i2]);
        }
        this.mv.visitFrame(type, nLocal, local, nStack, stack);
    }

    @Override
    public void visitInsn(int opcode) {
        this.checkStartCode();
        this.checkEndCode();
        CheckMethodAdapter.checkOpcode(opcode, 0);
        this.mv.visitInsn(opcode);
    }

    @Override
    public void visitIntInsn(int opcode, int operand) {
        this.checkStartCode();
        this.checkEndCode();
        CheckMethodAdapter.checkOpcode(opcode, 1);
        switch (opcode) {
            case 16: {
                CheckMethodAdapter.checkSignedByte(operand, "Invalid operand");
                break;
            }
            case 17: {
                CheckMethodAdapter.checkSignedShort(operand, "Invalid operand");
                break;
            }
            default: {
                if (operand >= 4 && operand <= 11) break;
                throw new IllegalArgumentException("Invalid operand (must be an array type code T_...): " + operand);
            }
        }
        this.mv.visitIntInsn(opcode, operand);
    }

    @Override
    public void visitVarInsn(int opcode, int var2) {
        this.checkStartCode();
        this.checkEndCode();
        CheckMethodAdapter.checkOpcode(opcode, 2);
        CheckMethodAdapter.checkUnsignedShort(var2, "Invalid variable index");
        this.mv.visitVarInsn(opcode, var2);
    }

    @Override
    public void visitTypeInsn(int opcode, String type) {
        this.checkStartCode();
        this.checkEndCode();
        CheckMethodAdapter.checkOpcode(opcode, 3);
        CheckMethodAdapter.checkInternalName(type, "type");
        if (opcode == 187 && type.charAt(0) == '[') {
            throw new IllegalArgumentException("NEW cannot be used to create arrays: " + type);
        }
        this.mv.visitTypeInsn(opcode, type);
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
        this.checkStartCode();
        this.checkEndCode();
        CheckMethodAdapter.checkOpcode(opcode, 4);
        CheckMethodAdapter.checkInternalName(owner, "owner");
        CheckMethodAdapter.checkUnqualifiedName(this.version, name, "name");
        CheckMethodAdapter.checkDesc(desc, false);
        this.mv.visitFieldInsn(opcode, owner, name, desc);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
        this.checkStartCode();
        this.checkEndCode();
        CheckMethodAdapter.checkOpcode(opcode, 5);
        CheckMethodAdapter.checkMethodIdentifier(this.version, name, "name");
        CheckMethodAdapter.checkInternalName(owner, "owner");
        CheckMethodAdapter.checkMethodDesc(desc);
        this.mv.visitMethodInsn(opcode, owner, name, desc);
    }

    @Override
    public void visitInvokeDynamicInsn(String name, String desc, MethodHandle bsm, Object ... bsmArgs) {
        this.checkStartCode();
        this.checkEndCode();
        CheckMethodAdapter.checkMethodIdentifier(this.version, name, "name");
        CheckMethodAdapter.checkMethodDesc(desc);
        if (bsm.getTag() != 6 && bsm.getTag() != 8) {
            throw new IllegalArgumentException("invalid constant method handle tag " + bsm.getTag());
        }
        for (int i2 = 0; i2 < bsmArgs.length; ++i2) {
            this.checkLDCConstant(bsmArgs[i2]);
        }
        this.mv.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs);
    }

    @Override
    public void visitJumpInsn(int opcode, Label label) {
        this.checkStartCode();
        this.checkEndCode();
        CheckMethodAdapter.checkOpcode(opcode, 6);
        this.checkLabel(label, false, "label");
        CheckMethodAdapter.checkNonDebugLabel(label);
        this.mv.visitJumpInsn(opcode, label);
    }

    @Override
    public void visitLabel(Label label) {
        this.checkStartCode();
        this.checkEndCode();
        this.checkLabel(label, false, "label");
        if (this.labels.get(label) != null) {
            throw new IllegalArgumentException("Already visited label");
        }
        this.labels.put(label, new Integer(this.labels.size()));
        this.mv.visitLabel(label);
    }

    @Override
    public void visitLdcInsn(Object cst) {
        this.checkStartCode();
        this.checkEndCode();
        this.checkLDCConstant(cst);
        this.mv.visitLdcInsn(cst);
    }

    @Override
    public void visitIincInsn(int var2, int increment) {
        this.checkStartCode();
        this.checkEndCode();
        CheckMethodAdapter.checkUnsignedShort(var2, "Invalid variable index");
        CheckMethodAdapter.checkSignedShort(increment, "Invalid increment");
        this.mv.visitIincInsn(var2, increment);
    }

    @Override
    public void visitTableSwitchInsn(int min, int max, Label dflt, Label ... labels) {
        this.checkStartCode();
        this.checkEndCode();
        if (max < min) {
            throw new IllegalArgumentException("Max = " + max + " must be greater than or equal to min = " + min);
        }
        this.checkLabel(dflt, false, "default label");
        CheckMethodAdapter.checkNonDebugLabel(dflt);
        if (labels == null || labels.length != max - min + 1) {
            throw new IllegalArgumentException("There must be max - min + 1 labels");
        }
        for (int i2 = 0; i2 < labels.length; ++i2) {
            this.checkLabel(labels[i2], false, "label at index " + i2);
            CheckMethodAdapter.checkNonDebugLabel(labels[i2]);
        }
        this.mv.visitTableSwitchInsn(min, max, dflt, labels);
    }

    @Override
    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        this.checkEndCode();
        this.checkStartCode();
        this.checkLabel(dflt, false, "default label");
        CheckMethodAdapter.checkNonDebugLabel(dflt);
        if (keys == null || labels == null || keys.length != labels.length) {
            throw new IllegalArgumentException("There must be the same number of keys and labels");
        }
        for (int i2 = 0; i2 < labels.length; ++i2) {
            this.checkLabel(labels[i2], false, "label at index " + i2);
            CheckMethodAdapter.checkNonDebugLabel(labels[i2]);
        }
        this.mv.visitLookupSwitchInsn(dflt, keys, labels);
    }

    @Override
    public void visitMultiANewArrayInsn(String desc, int dims) {
        this.checkStartCode();
        this.checkEndCode();
        CheckMethodAdapter.checkDesc(desc, false);
        if (desc.charAt(0) != '[') {
            throw new IllegalArgumentException("Invalid descriptor (must be an array type descriptor): " + desc);
        }
        if (dims < 1) {
            throw new IllegalArgumentException("Invalid dimensions (must be greater than 0): " + dims);
        }
        if (dims > desc.lastIndexOf(91) + 1) {
            throw new IllegalArgumentException("Invalid dimensions (must not be greater than dims(desc)): " + dims);
        }
        this.mv.visitMultiANewArrayInsn(desc, dims);
    }

    @Override
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        this.checkStartCode();
        this.checkEndCode();
        this.checkLabel(start, false, "start label");
        this.checkLabel(end, false, "end label");
        this.checkLabel(handler, false, "handler label");
        CheckMethodAdapter.checkNonDebugLabel(start);
        CheckMethodAdapter.checkNonDebugLabel(end);
        CheckMethodAdapter.checkNonDebugLabel(handler);
        if (this.labels.get(start) != null || this.labels.get(end) != null || this.labels.get(handler) != null) {
            throw new IllegalStateException("Try catch blocks must be visited before their labels");
        }
        if (type != null) {
            CheckMethodAdapter.checkInternalName(type, "type");
        }
        this.mv.visitTryCatchBlock(start, end, handler, type);
    }

    @Override
    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
        this.checkStartCode();
        this.checkEndCode();
        CheckMethodAdapter.checkUnqualifiedName(this.version, name, "name");
        CheckMethodAdapter.checkDesc(desc, false);
        this.checkLabel(start, true, "start label");
        this.checkLabel(end, true, "end label");
        CheckMethodAdapter.checkUnsignedShort(index, "Invalid variable index");
        int s2 = this.labels.get(start);
        int e2 = this.labels.get(end);
        if (e2 < s2) {
            throw new IllegalArgumentException("Invalid start and end labels (end must be greater than start)");
        }
        this.mv.visitLocalVariable(name, desc, signature, start, end, index);
    }

    @Override
    public void visitLineNumber(int line, Label start) {
        this.checkStartCode();
        this.checkEndCode();
        CheckMethodAdapter.checkUnsignedShort(line, "Invalid line number");
        this.checkLabel(start, true, "start label");
        this.mv.visitLineNumber(line, start);
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        this.checkStartCode();
        this.checkEndCode();
        this.endCode = true;
        CheckMethodAdapter.checkUnsignedShort(maxStack, "Invalid max stack");
        CheckMethodAdapter.checkUnsignedShort(maxLocals, "Invalid max locals");
        this.mv.visitMaxs(maxStack, maxLocals);
    }

    @Override
    public void visitEnd() {
        this.checkEndMethod();
        this.endMethod = true;
        this.mv.visitEnd();
    }

    void checkStartCode() {
        if (!this.startCode) {
            throw new IllegalStateException("Cannot visit instructions before visitCode has been called.");
        }
    }

    void checkEndCode() {
        if (this.endCode) {
            throw new IllegalStateException("Cannot visit instructions after visitMaxs has been called.");
        }
    }

    void checkEndMethod() {
        if (this.endMethod) {
            throw new IllegalStateException("Cannot visit elements after visitEnd has been called.");
        }
    }

    static void checkFrameValue(Object value) {
        if (value == Opcodes.TOP || value == Opcodes.INTEGER || value == Opcodes.FLOAT || value == Opcodes.LONG || value == Opcodes.DOUBLE || value == Opcodes.NULL || value == Opcodes.UNINITIALIZED_THIS) {
            return;
        }
        if (value instanceof String) {
            CheckMethodAdapter.checkInternalName((String)value, "Invalid stack frame value");
            return;
        }
        if (!(value instanceof Label)) {
            throw new IllegalArgumentException("Invalid stack frame value: " + value);
        }
    }

    static void checkOpcode(int opcode, int type) {
        if (opcode < 0 || opcode > 199 || TYPE[opcode] != type) {
            throw new IllegalArgumentException("Invalid opcode: " + opcode);
        }
    }

    static void checkSignedByte(int value, String msg) {
        if (value < -128 || value > 127) {
            throw new IllegalArgumentException(msg + " (must be a signed byte): " + value);
        }
    }

    static void checkSignedShort(int value, String msg) {
        if (value < -32768 || value > 32767) {
            throw new IllegalArgumentException(msg + " (must be a signed short): " + value);
        }
    }

    static void checkUnsignedShort(int value, String msg) {
        if (value < 0 || value > 65535) {
            throw new IllegalArgumentException(msg + " (must be an unsigned short): " + value);
        }
    }

    static void checkConstant(Object cst) {
        if (!(cst instanceof Integer || cst instanceof Float || cst instanceof Long || cst instanceof Double || cst instanceof String)) {
            throw new IllegalArgumentException("Invalid constant: " + cst);
        }
    }

    void checkLDCConstant(Object cst) {
        if (cst instanceof Type) {
            if ((this.version & 0xFFFF) < 49) {
                throw new IllegalArgumentException("ldc of a constant class requires at least version 1.5");
            }
        } else if (cst instanceof MethodType) {
            if ((this.version & 0xFFFF) < 51) {
                throw new IllegalArgumentException("ldc of a constant method type requires at least version 1.7");
            }
        } else if (cst instanceof MethodHandle) {
            if ((this.version & 0xFFFF) < 51) {
                throw new IllegalArgumentException("ldc of a constant method handle requires at least version 1.7");
            }
            int tag = ((MethodHandle)cst).getTag();
            if (tag < 1 || tag > 9) {
                throw new IllegalArgumentException("invalid constant method handle tag " + tag);
            }
        } else {
            CheckMethodAdapter.checkConstant(cst);
        }
    }

    static void checkUnqualifiedName(int version, String name, String msg) {
        if ((version & 0xFFFF) < 49) {
            CheckMethodAdapter.checkIdentifier(name, msg);
        } else {
            for (int i2 = 0; i2 < name.length(); ++i2) {
                if (".;[/".indexOf(name.charAt(i2)) == -1) continue;
                throw new IllegalArgumentException("Invalid " + msg + " (must be a valid unqualified name): " + name);
            }
        }
    }

    static void checkIdentifier(String name, String msg) {
        CheckMethodAdapter.checkIdentifier(name, 0, -1, msg);
    }

    static void checkIdentifier(String name, int start, int end, String msg) {
        if (name == null || (end == -1 ? name.length() <= start : end <= start)) {
            throw new IllegalArgumentException("Invalid " + msg + " (must not be null or empty)");
        }
        if (!Character.isJavaIdentifierStart(name.charAt(start))) {
            throw new IllegalArgumentException("Invalid " + msg + " (must be a valid Java identifier): " + name);
        }
        int max = end == -1 ? name.length() : end;
        for (int i2 = start + 1; i2 < max; ++i2) {
            if (Character.isJavaIdentifierPart(name.charAt(i2))) continue;
            throw new IllegalArgumentException("Invalid " + msg + " (must be a valid Java identifier): " + name);
        }
    }

    static void checkMethodIdentifier(int version, String name, String msg) {
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("Invalid " + msg + " (must not be null or empty)");
        }
        if ("<init>".equals(name) || "<clinit>".equals(name)) {
            return;
        }
        if ((version & 0xFFFF) >= 49) {
            for (int i2 = 0; i2 < name.length(); ++i2) {
                if (".;[/<>".indexOf(name.charAt(i2)) == -1) continue;
                throw new IllegalArgumentException("Invalid " + msg + " (must be a valid unqualified name): " + name);
            }
            return;
        }
        if (!Character.isJavaIdentifierStart(name.charAt(0))) {
            throw new IllegalArgumentException("Invalid " + msg + " (must be a '<init>', '<clinit>' or a valid Java identifier): " + name);
        }
        for (int i3 = 1; i3 < name.length(); ++i3) {
            if (Character.isJavaIdentifierPart(name.charAt(i3))) continue;
            throw new IllegalArgumentException("Invalid " + msg + " (must be '<init>' or '<clinit>' or a valid Java identifier): " + name);
        }
    }

    static void checkInternalName(String name, String msg) {
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("Invalid " + msg + " (must not be null or empty)");
        }
        if (name.charAt(0) == '[') {
            CheckMethodAdapter.checkDesc(name, false);
        } else {
            CheckMethodAdapter.checkInternalName(name, 0, -1, msg);
        }
    }

    static void checkInternalName(String name, int start, int end, String msg) {
        int max = end == -1 ? name.length() : end;
        try {
            int slash;
            int begin = start;
            do {
                if ((slash = name.indexOf(47, begin + 1)) == -1 || slash > max) {
                    slash = max;
                }
                CheckMethodAdapter.checkIdentifier(name, begin, slash, null);
                begin = slash + 1;
            } while (slash != max);
        }
        catch (IllegalArgumentException _) {
            throw new IllegalArgumentException("Invalid " + msg + " (must be a fully qualified class name in internal form): " + name);
        }
    }

    static void checkDesc(String desc, boolean canBeVoid) {
        int end = CheckMethodAdapter.checkDesc(desc, 0, canBeVoid);
        if (end != desc.length()) {
            throw new IllegalArgumentException("Invalid descriptor: " + desc);
        }
    }

    static int checkDesc(String desc, int start, boolean canBeVoid) {
        if (desc == null || start >= desc.length()) {
            throw new IllegalArgumentException("Invalid type descriptor (must not be null or empty)");
        }
        switch (desc.charAt(start)) {
            case 'V': {
                if (canBeVoid) {
                    return start + 1;
                }
                throw new IllegalArgumentException("Invalid descriptor: " + desc);
            }
            case 'B': 
            case 'C': 
            case 'D': 
            case 'F': 
            case 'I': 
            case 'J': 
            case 'S': 
            case 'Z': {
                return start + 1;
            }
            case '[': {
                int index;
                for (index = start + 1; index < desc.length() && desc.charAt(index) == '['; ++index) {
                }
                if (index < desc.length()) {
                    return CheckMethodAdapter.checkDesc(desc, index, false);
                }
                throw new IllegalArgumentException("Invalid descriptor: " + desc);
            }
            case 'L': {
                int index = desc.indexOf(59, start);
                if (index == -1 || index - start < 2) {
                    throw new IllegalArgumentException("Invalid descriptor: " + desc);
                }
                try {
                    CheckMethodAdapter.checkInternalName(desc, start + 1, index, null);
                }
                catch (IllegalArgumentException _) {
                    throw new IllegalArgumentException("Invalid descriptor: " + desc);
                }
                return index + 1;
            }
        }
        throw new IllegalArgumentException("Invalid descriptor: " + desc);
    }

    static void checkMethodDesc(String desc) {
        if (desc == null || desc.length() == 0) {
            throw new IllegalArgumentException("Invalid method descriptor (must not be null or empty)");
        }
        if (desc.charAt(0) != '(' || desc.length() < 3) {
            throw new IllegalArgumentException("Invalid descriptor: " + desc);
        }
        int start = 1;
        if (desc.charAt(start) != ')') {
            do {
                if (desc.charAt(start) != 'V') continue;
                throw new IllegalArgumentException("Invalid descriptor: " + desc);
            } while ((start = CheckMethodAdapter.checkDesc(desc, start, false)) < desc.length() && desc.charAt(start) != ')');
        }
        if ((start = CheckMethodAdapter.checkDesc(desc, start + 1, true)) != desc.length()) {
            throw new IllegalArgumentException("Invalid descriptor: " + desc);
        }
    }

    static void checkClassSignature(String signature) {
        int pos = 0;
        if (CheckMethodAdapter.getChar(signature, 0) == '<') {
            pos = CheckMethodAdapter.checkFormalTypeParameters(signature, pos);
        }
        pos = CheckMethodAdapter.checkClassTypeSignature(signature, pos);
        while (CheckMethodAdapter.getChar(signature, pos) == 'L') {
            pos = CheckMethodAdapter.checkClassTypeSignature(signature, pos);
        }
        if (pos != signature.length()) {
            throw new IllegalArgumentException(signature + ": error at index " + pos);
        }
    }

    static void checkMethodSignature(String signature) {
        int pos = 0;
        if (CheckMethodAdapter.getChar(signature, 0) == '<') {
            pos = CheckMethodAdapter.checkFormalTypeParameters(signature, pos);
        }
        pos = CheckMethodAdapter.checkChar('(', signature, pos);
        while ("ZCBSIFJDL[T".indexOf(CheckMethodAdapter.getChar(signature, pos)) != -1) {
            pos = CheckMethodAdapter.checkTypeSignature(signature, pos);
        }
        pos = CheckMethodAdapter.getChar(signature, pos = CheckMethodAdapter.checkChar(')', signature, pos)) == 'V' ? ++pos : CheckMethodAdapter.checkTypeSignature(signature, pos);
        while (CheckMethodAdapter.getChar(signature, pos) == '^') {
            if (CheckMethodAdapter.getChar(signature, ++pos) == 'L') {
                pos = CheckMethodAdapter.checkClassTypeSignature(signature, pos);
                continue;
            }
            pos = CheckMethodAdapter.checkTypeVariableSignature(signature, pos);
        }
        if (pos != signature.length()) {
            throw new IllegalArgumentException(signature + ": error at index " + pos);
        }
    }

    static void checkFieldSignature(String signature) {
        int pos = CheckMethodAdapter.checkFieldTypeSignature(signature, 0);
        if (pos != signature.length()) {
            throw new IllegalArgumentException(signature + ": error at index " + pos);
        }
    }

    private static int checkFormalTypeParameters(String signature, int pos) {
        pos = CheckMethodAdapter.checkChar('<', signature, pos);
        pos = CheckMethodAdapter.checkFormalTypeParameter(signature, pos);
        while (CheckMethodAdapter.getChar(signature, pos) != '>') {
            pos = CheckMethodAdapter.checkFormalTypeParameter(signature, pos);
        }
        return pos + 1;
    }

    private static int checkFormalTypeParameter(String signature, int pos) {
        pos = CheckMethodAdapter.checkIdentifier(signature, pos);
        if ("L[T".indexOf(CheckMethodAdapter.getChar(signature, pos = CheckMethodAdapter.checkChar(':', signature, pos))) != -1) {
            pos = CheckMethodAdapter.checkFieldTypeSignature(signature, pos);
        }
        while (CheckMethodAdapter.getChar(signature, pos) == ':') {
            pos = CheckMethodAdapter.checkFieldTypeSignature(signature, pos + 1);
        }
        return pos;
    }

    private static int checkFieldTypeSignature(String signature, int pos) {
        switch (CheckMethodAdapter.getChar(signature, pos)) {
            case 'L': {
                return CheckMethodAdapter.checkClassTypeSignature(signature, pos);
            }
            case '[': {
                return CheckMethodAdapter.checkTypeSignature(signature, pos + 1);
            }
        }
        return CheckMethodAdapter.checkTypeVariableSignature(signature, pos);
    }

    private static int checkClassTypeSignature(String signature, int pos) {
        pos = CheckMethodAdapter.checkChar('L', signature, pos);
        pos = CheckMethodAdapter.checkIdentifier(signature, pos);
        while (CheckMethodAdapter.getChar(signature, pos) == '/') {
            pos = CheckMethodAdapter.checkIdentifier(signature, pos + 1);
        }
        if (CheckMethodAdapter.getChar(signature, pos) == '<') {
            pos = CheckMethodAdapter.checkTypeArguments(signature, pos);
        }
        while (CheckMethodAdapter.getChar(signature, pos) == '.') {
            if (CheckMethodAdapter.getChar(signature, pos = CheckMethodAdapter.checkIdentifier(signature, pos + 1)) != '<') continue;
            pos = CheckMethodAdapter.checkTypeArguments(signature, pos);
        }
        return CheckMethodAdapter.checkChar(';', signature, pos);
    }

    private static int checkTypeArguments(String signature, int pos) {
        pos = CheckMethodAdapter.checkChar('<', signature, pos);
        pos = CheckMethodAdapter.checkTypeArgument(signature, pos);
        while (CheckMethodAdapter.getChar(signature, pos) != '>') {
            pos = CheckMethodAdapter.checkTypeArgument(signature, pos);
        }
        return pos + 1;
    }

    private static int checkTypeArgument(String signature, int pos) {
        char c2 = CheckMethodAdapter.getChar(signature, pos);
        if (c2 == '*') {
            return pos + 1;
        }
        if (c2 == '+' || c2 == '-') {
            ++pos;
        }
        return CheckMethodAdapter.checkFieldTypeSignature(signature, pos);
    }

    private static int checkTypeVariableSignature(String signature, int pos) {
        pos = CheckMethodAdapter.checkChar('T', signature, pos);
        pos = CheckMethodAdapter.checkIdentifier(signature, pos);
        return CheckMethodAdapter.checkChar(';', signature, pos);
    }

    private static int checkTypeSignature(String signature, int pos) {
        switch (CheckMethodAdapter.getChar(signature, pos)) {
            case 'B': 
            case 'C': 
            case 'D': 
            case 'F': 
            case 'I': 
            case 'J': 
            case 'S': 
            case 'Z': {
                return pos + 1;
            }
        }
        return CheckMethodAdapter.checkFieldTypeSignature(signature, pos);
    }

    private static int checkIdentifier(String signature, int pos) {
        if (!Character.isJavaIdentifierStart(CheckMethodAdapter.getChar(signature, pos))) {
            throw new IllegalArgumentException(signature + ": identifier expected at index " + pos);
        }
        ++pos;
        while (Character.isJavaIdentifierPart(CheckMethodAdapter.getChar(signature, pos))) {
            ++pos;
        }
        return pos;
    }

    private static int checkChar(char c2, String signature, int pos) {
        if (CheckMethodAdapter.getChar(signature, pos) == c2) {
            return pos + 1;
        }
        throw new IllegalArgumentException(signature + ": '" + c2 + "' expected at index " + pos);
    }

    private static char getChar(String signature, int pos) {
        return pos < signature.length() ? signature.charAt(pos) : (char)'\u0000';
    }

    void checkLabel(Label label, boolean checkVisited, String msg) {
        if (label == null) {
            throw new IllegalArgumentException("Invalid " + msg + " (must not be null)");
        }
        if (checkVisited && this.labels.get(label) == null) {
            throw new IllegalArgumentException("Invalid " + msg + " (must be visited first)");
        }
    }

    private static void checkNonDebugLabel(Label label) {
        Field f2 = CheckMethodAdapter.getLabelStatusField();
        int status = 0;
        try {
            status = f2 == null ? 0 : (Integer)f2.get(label);
        }
        catch (IllegalAccessException e2) {
            throw new Error("Internal error");
        }
        if ((status & 1) != 0) {
            throw new IllegalArgumentException("Labels used for debug info cannot be reused for control flow");
        }
    }

    private static Field getLabelStatusField() {
        if (labelStatusField == null && (labelStatusField = CheckMethodAdapter.getLabelField("a")) == null) {
            labelStatusField = CheckMethodAdapter.getLabelField("status");
        }
        return labelStatusField;
    }

    private static Field getLabelField(String name) {
        try {
            Field f2 = Label.class.getDeclaredField(name);
            f2.setAccessible(true);
            return f2;
        }
        catch (NoSuchFieldException e2) {
            return null;
        }
    }

    static {
        String s2 = "BBBBBBBBBBBBBBBBCCIAADDDDDAAAAAAAAAAAAAAAAAAAABBBBBBBBDDDDDAAAAAAAAAAAAAAAAAAAABBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBJBBBBBBBBBBBBBBBBBBBBHHHHHHHHHHHHHHHHDKLBBBBBBFFFFGGGGAECEBBEEBBAMHHAA";
        TYPE = new int[s2.length()];
        for (int i2 = 0; i2 < TYPE.length; ++i2) {
            CheckMethodAdapter.TYPE[i2] = s2.charAt(i2) - 65 - 1;
        }
    }
}

