/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.eclipse.swt.dnd.ByteArrayTransfer
 *  org.eclipse.swt.dnd.DND
 *  org.eclipse.swt.dnd.TransferData
 */
package javafx.embed.swt;

import java.nio.ByteBuffer;
import javafx.beans.NamedArg;
import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TransferData;

@Deprecated
public class CustomTransfer
extends ByteArrayTransfer {
    private String name;
    private String mime;

    public CustomTransfer(@NamedArg(value="name") String string, @NamedArg(value="mime") String string2) {
        this.name = string;
        this.mime = string2;
    }

    public String getName() {
        return this.name;
    }

    public String getMime() {
        return this.mime;
    }

    public void javaToNative(Object object, TransferData transferData) {
        if (!this.checkCustom(object) || !this.isSupportedType(transferData)) {
            DND.error((int)2003);
        }
        byte[] arrby = null;
        if (object instanceof ByteBuffer) {
            arrby = ((ByteBuffer)object).array();
        } else if (object instanceof byte[]) {
            arrby = (byte[])object;
        }
        if (arrby == null) {
            DND.error((int)2003);
        }
        super.javaToNative((Object)arrby, transferData);
    }

    public Object nativeToJava(TransferData transferData) {
        if (this.isSupportedType(transferData)) {
            return super.nativeToJava(transferData);
        }
        return null;
    }

    protected String[] getTypeNames() {
        return new String[]{this.name};
    }

    protected int[] getTypeIds() {
        return new int[]{CustomTransfer.registerType((String)this.name)};
    }

    boolean checkByteArray(Object object) {
        return object != null && object instanceof byte[] && ((byte[])object).length > 0;
    }

    boolean checkByteBuffer(Object object) {
        return object != null && object instanceof ByteBuffer && ((ByteBuffer)object).limit() > 0;
    }

    boolean checkCustom(Object object) {
        return this.checkByteArray(object) || this.checkByteBuffer(object);
    }

    protected boolean validate(Object object) {
        return this.checkCustom(object);
    }
}

