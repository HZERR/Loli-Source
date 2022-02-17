/*
 * Decompiled with CFR 0.150.
 */
package javafx.stage;

import java.io.File;
import javafx.stage.DirectoryChooser;
import javafx.util.Builder;

@Deprecated
public final class DirectoryChooserBuilder
implements Builder<DirectoryChooser> {
    private int __set;
    private File initialDirectory;
    private String title;

    protected DirectoryChooserBuilder() {
    }

    public static DirectoryChooserBuilder create() {
        return new DirectoryChooserBuilder();
    }

    public void applyTo(DirectoryChooser directoryChooser) {
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            directoryChooser.setInitialDirectory(this.initialDirectory);
        }
        if ((n2 & 2) != 0) {
            directoryChooser.setTitle(this.title);
        }
    }

    public DirectoryChooserBuilder initialDirectory(File file) {
        this.initialDirectory = file;
        this.__set |= 1;
        return this;
    }

    public DirectoryChooserBuilder title(String string) {
        this.title = string;
        this.__set |= 2;
        return this;
    }

    @Override
    public DirectoryChooser build() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        this.applyTo(directoryChooser);
        return directoryChooser;
    }
}

