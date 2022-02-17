/*
 * Decompiled with CFR 0.150.
 */
package javafx.embed.swt;

import javafx.embed.swt.CustomTransfer;
import javafx.util.Builder;

@Deprecated
public class CustomTransferBuilder<B extends CustomTransferBuilder<B>>
implements Builder<CustomTransfer> {
    private String mime;
    private String name;

    protected CustomTransferBuilder() {
    }

    public static CustomTransferBuilder<?> create() {
        return new CustomTransferBuilder();
    }

    public B mime(String string) {
        this.mime = string;
        return (B)this;
    }

    public B name(String string) {
        this.name = string;
        return (B)this;
    }

    @Override
    public CustomTransfer build() {
        CustomTransfer customTransfer = new CustomTransfer(this.name, this.mime);
        return customTransfer;
    }
}

