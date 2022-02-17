/*
 * Decompiled with CFR 0.150.
 */
package javafx.stage;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import javafx.stage.FileChooser;
import javafx.util.Builder;

@Deprecated
public final class FileChooserBuilder
implements Builder<FileChooser> {
    private int __set;
    private Collection<? extends FileChooser.ExtensionFilter> extensionFilters;
    private File initialDirectory;
    private String title;

    protected FileChooserBuilder() {
    }

    public static FileChooserBuilder create() {
        return new FileChooserBuilder();
    }

    public void applyTo(FileChooser fileChooser) {
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            fileChooser.getExtensionFilters().addAll(this.extensionFilters);
        }
        if ((n2 & 2) != 0) {
            fileChooser.setInitialDirectory(this.initialDirectory);
        }
        if ((n2 & 4) != 0) {
            fileChooser.setTitle(this.title);
        }
    }

    public FileChooserBuilder extensionFilters(Collection<? extends FileChooser.ExtensionFilter> collection) {
        this.extensionFilters = collection;
        this.__set |= 1;
        return this;
    }

    public FileChooserBuilder extensionFilters(FileChooser.ExtensionFilter ... arrextensionFilter) {
        return this.extensionFilters(Arrays.asList(arrextensionFilter));
    }

    public FileChooserBuilder initialDirectory(File file) {
        this.initialDirectory = file;
        this.__set |= 2;
        return this;
    }

    public FileChooserBuilder title(String string) {
        this.title = string;
        this.__set |= 4;
        return this;
    }

    @Override
    public FileChooser build() {
        FileChooser fileChooser = new FileChooser();
        this.applyTo(fileChooser);
        return fileChooser;
    }
}

