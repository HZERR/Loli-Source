/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm.util;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import org.objectweb.asm.Attribute;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public abstract class AbstractVisitor {
    public static final String[] OPCODES;
    public static final String[] TYPES;
    public static final String[] METHOD_HANDLE_TAG;
    public final List<Object> text = new ArrayList<Object>();
    protected final StringBuffer buf = new StringBuffer();

    protected AbstractVisitor() {
    }

    public List<Object> getText() {
        return this.text;
    }

    public void print(PrintWriter pw) {
        AbstractVisitor.printList(pw, this.text);
    }

    public static void appendString(StringBuffer buf, String s2) {
        buf.append('\"');
        for (int i2 = 0; i2 < s2.length(); ++i2) {
            char c2 = s2.charAt(i2);
            if (c2 == '\n') {
                buf.append("\\n");
                continue;
            }
            if (c2 == '\r') {
                buf.append("\\r");
                continue;
            }
            if (c2 == '\\') {
                buf.append("\\\\");
                continue;
            }
            if (c2 == '\"') {
                buf.append("\\\"");
                continue;
            }
            if (c2 < ' ' || c2 > '\u007f') {
                buf.append("\\u");
                if (c2 < '\u0010') {
                    buf.append("000");
                } else if (c2 < '\u0100') {
                    buf.append("00");
                } else if (c2 < '\u1000') {
                    buf.append('0');
                }
                buf.append(Integer.toString(c2, 16));
                continue;
            }
            buf.append(c2);
        }
        buf.append('\"');
    }

    static void printList(PrintWriter pw, List<?> l2) {
        for (int i2 = 0; i2 < l2.size(); ++i2) {
            Object o2 = l2.get(i2);
            if (o2 instanceof List) {
                AbstractVisitor.printList(pw, (List)o2);
                continue;
            }
            pw.print(o2.toString());
        }
    }

    public static Attribute[] getDefaultAttributes() {
        return new Attribute[0];
    }

    static {
        int l2;
        String s2 = "NOP,ACONST_NULL,ICONST_M1,ICONST_0,ICONST_1,ICONST_2,ICONST_3,ICONST_4,ICONST_5,LCONST_0,LCONST_1,FCONST_0,FCONST_1,FCONST_2,DCONST_0,DCONST_1,BIPUSH,SIPUSH,LDC,,,ILOAD,LLOAD,FLOAD,DLOAD,ALOAD,,,,,,,,,,,,,,,,,,,,,IALOAD,LALOAD,FALOAD,DALOAD,AALOAD,BALOAD,CALOAD,SALOAD,ISTORE,LSTORE,FSTORE,DSTORE,ASTORE,,,,,,,,,,,,,,,,,,,,,IASTORE,LASTORE,FASTORE,DASTORE,AASTORE,BASTORE,CASTORE,SASTORE,POP,POP2,DUP,DUP_X1,DUP_X2,DUP2,DUP2_X1,DUP2_X2,SWAP,IADD,LADD,FADD,DADD,ISUB,LSUB,FSUB,DSUB,IMUL,LMUL,FMUL,DMUL,IDIV,LDIV,FDIV,DDIV,IREM,LREM,FREM,DREM,INEG,LNEG,FNEG,DNEG,ISHL,LSHL,ISHR,LSHR,IUSHR,LUSHR,IAND,LAND,IOR,LOR,IXOR,LXOR,IINC,I2L,I2F,I2D,L2I,L2F,L2D,F2I,F2L,F2D,D2I,D2L,D2F,I2B,I2C,I2S,LCMP,FCMPL,FCMPG,DCMPL,DCMPG,IFEQ,IFNE,IFLT,IFGE,IFGT,IFLE,IF_ICMPEQ,IF_ICMPNE,IF_ICMPLT,IF_ICMPGE,IF_ICMPGT,IF_ICMPLE,IF_ACMPEQ,IF_ACMPNE,GOTO,JSR,RET,TABLESWITCH,LOOKUPSWITCH,IRETURN,LRETURN,FRETURN,DRETURN,ARETURN,RETURN,GETSTATIC,PUTSTATIC,GETFIELD,PUTFIELD,INVOKEVIRTUAL,INVOKESPECIAL,INVOKESTATIC,INVOKEINTERFACE,INVOKEDYNAMIC,NEW,NEWARRAY,ANEWARRAY,ARRAYLENGTH,ATHROW,CHECKCAST,INSTANCEOF,MONITORENTER,MONITOREXIT,,MULTIANEWARRAY,IFNULL,IFNONNULL,";
        OPCODES = new String[200];
        int i2 = 0;
        int j2 = 0;
        while ((l2 = s2.indexOf(44, j2)) > 0) {
            AbstractVisitor.OPCODES[i2++] = j2 + 1 == l2 ? null : s2.substring(j2, l2);
            j2 = l2 + 1;
        }
        s2 = "T_BOOLEAN,T_CHAR,T_FLOAT,T_DOUBLE,T_BYTE,T_SHORT,T_INT,T_LONG,";
        TYPES = new String[12];
        j2 = 0;
        i2 = 4;
        while ((l2 = s2.indexOf(44, j2)) > 0) {
            AbstractVisitor.TYPES[i2++] = s2.substring(j2, l2);
            j2 = l2 + 1;
        }
        s2 = "MH_GETFIELD,MH_GETSTATIC,MH_PUTFIELD,MH_PUTSTATIC,MH_INVOKEVIRTUAL,MH_INVOKESTATIC,MH_INVOKESPECIAL,MH_NEWINVOKESPECIAL,MH_INVOKEINTERFACE,";
        METHOD_HANDLE_TAG = new String[10];
        j2 = 0;
        i2 = 1;
        while ((l2 = s2.indexOf(44, j2)) > 0) {
            AbstractVisitor.METHOD_HANDLE_TAG[i2++] = s2.substring(j2, l2);
            j2 = l2 + 1;
        }
    }
}

