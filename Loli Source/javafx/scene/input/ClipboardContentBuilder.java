/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.input;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import javafx.scene.input.ClipboardContent;
import javafx.util.Builder;

@Deprecated
public class ClipboardContentBuilder<B extends ClipboardContentBuilder<B>>
implements Builder<ClipboardContent> {
    private boolean __set;
    private Collection<? extends File> files;

    protected ClipboardContentBuilder() {
    }

    public static ClipboardContentBuilder<?> create() {
        return new ClipboardContentBuilder();
    }

    public void applyTo(ClipboardContent clipboardContent) {
        if (this.__set) {
            clipboardContent.getFiles().clear();
            clipboardContent.getFiles().addAll(this.files);
        }
    }

    public B files(Collection<? extends File> collection) {
        this.files = collection;
        this.__set = true;
        return (B)this;
    }

    public B files(File ... arrfile) {
        return this.files(Arrays.asList(arrfile));
    }

    @Override
    public ClipboardContent build() {
        ClipboardContent clipboardContent = new ClipboardContent();
        this.applyTo(clipboardContent);
        return clipboardContent;
    }
}

