/*
 * Decompiled with CFR 0.150.
 */
package netscape.javascript;

public class JSException
extends RuntimeException {
    public static final int EXCEPTION_TYPE_EMPTY = -1;
    public static final int EXCEPTION_TYPE_VOID = 0;
    public static final int EXCEPTION_TYPE_OBJECT = 1;
    public static final int EXCEPTION_TYPE_FUNCTION = 2;
    public static final int EXCEPTION_TYPE_STRING = 3;
    public static final int EXCEPTION_TYPE_NUMBER = 4;
    public static final int EXCEPTION_TYPE_BOOLEAN = 5;
    public static final int EXCEPTION_TYPE_ERROR = 6;
    protected String message = null;
    protected String filename = null;
    protected int lineno = -1;
    protected String source = null;
    protected int tokenIndex = -1;
    private int wrappedExceptionType = -1;
    private Object wrappedException = null;

    public JSException() {
        this((String)null);
    }

    public JSException(String string) {
        this(string, null, -1, null, -1);
    }

    public JSException(String string, String string2, int n2, String string3, int n3) {
        super(string);
        this.message = string;
        this.filename = string2;
        this.lineno = n2;
        this.source = string3;
        this.tokenIndex = n3;
        this.wrappedExceptionType = -1;
    }

    public JSException(int n2, Object object) {
        this();
        this.wrappedExceptionType = n2;
        this.wrappedException = object;
    }

    public int getWrappedExceptionType() {
        return this.wrappedExceptionType;
    }

    public Object getWrappedException() {
        return this.wrappedException;
    }
}

