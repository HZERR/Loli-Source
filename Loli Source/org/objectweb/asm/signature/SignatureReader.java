/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm.signature;

import org.objectweb.asm.signature.SignatureVisitor;

public class SignatureReader {
    private final String signature;

    public SignatureReader(String signature) {
        this.signature = signature;
    }

    public void accept(SignatureVisitor v2) {
        int pos;
        String signature = this.signature;
        int len = signature.length();
        if (signature.charAt(0) == '<') {
            char c2;
            pos = 2;
            do {
                int end = signature.indexOf(58, pos);
                v2.visitFormalTypeParameter(signature.substring(pos - 1, end));
                pos = end + 1;
                c2 = signature.charAt(pos);
                if (c2 == 'L' || c2 == '[' || c2 == 'T') {
                    pos = SignatureReader.parseType(signature, pos, v2.visitClassBound());
                }
                while ((c2 = signature.charAt(pos++)) == ':') {
                    pos = SignatureReader.parseType(signature, pos, v2.visitInterfaceBound());
                }
            } while (c2 != '>');
        } else {
            pos = 0;
        }
        if (signature.charAt(pos) == '(') {
            ++pos;
            while (signature.charAt(pos) != ')') {
                pos = SignatureReader.parseType(signature, pos, v2.visitParameterType());
            }
            pos = SignatureReader.parseType(signature, pos + 1, v2.visitReturnType());
            while (pos < len) {
                pos = SignatureReader.parseType(signature, pos + 1, v2.visitExceptionType());
            }
        } else {
            pos = SignatureReader.parseType(signature, pos, v2.visitSuperclass());
            while (pos < len) {
                pos = SignatureReader.parseType(signature, pos, v2.visitInterface());
            }
        }
    }

    public void acceptType(SignatureVisitor v2) {
        SignatureReader.parseType(this.signature, 0, v2);
    }

    private static int parseType(String signature, int pos, SignatureVisitor v2) {
        char c2 = signature.charAt(pos++);
        switch (c2) {
            case 'B': 
            case 'C': 
            case 'D': 
            case 'F': 
            case 'I': 
            case 'J': 
            case 'S': 
            case 'V': 
            case 'Z': {
                v2.visitBaseType(c2);
                return pos;
            }
            case '[': {
                return SignatureReader.parseType(signature, pos, v2.visitArrayType());
            }
            case 'T': {
                int end = signature.indexOf(59, pos);
                v2.visitTypeVariable(signature.substring(pos, end));
                return end + 1;
            }
        }
        int start = pos;
        boolean visited = false;
        boolean inner = false;
        while (true) {
            c2 = signature.charAt(pos++);
            block5 : switch (c2) {
                case '.': 
                case ';': {
                    String name;
                    if (!visited) {
                        name = signature.substring(start, pos - 1);
                        if (inner) {
                            v2.visitInnerClassType(name);
                        } else {
                            v2.visitClassType(name);
                        }
                    }
                    if (c2 == ';') {
                        v2.visitEnd();
                        return pos;
                    }
                    start = pos;
                    visited = false;
                    inner = true;
                    break;
                }
                case '<': {
                    String name = signature.substring(start, pos - 1);
                    if (inner) {
                        v2.visitInnerClassType(name);
                    } else {
                        v2.visitClassType(name);
                    }
                    visited = true;
                    block15: while (true) {
                        c2 = signature.charAt(pos);
                        switch (c2) {
                            case '>': {
                                break block5;
                            }
                            case '*': {
                                ++pos;
                                v2.visitTypeArgument();
                                continue block15;
                            }
                            case '+': 
                            case '-': {
                                pos = SignatureReader.parseType(signature, pos + 1, v2.visitTypeArgument(c2));
                                continue block15;
                            }
                        }
                        pos = SignatureReader.parseType(signature, pos, v2.visitTypeArgument('='));
                    }
                }
            }
        }
    }
}

