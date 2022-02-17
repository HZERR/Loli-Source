/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.text;

import javafx.scene.text.Font;
import javafx.util.Builder;

@Deprecated
public final class FontBuilder
implements Builder<Font> {
    private String name;
    private double size;

    protected FontBuilder() {
    }

    public static FontBuilder create() {
        return new FontBuilder();
    }

    public FontBuilder name(String string) {
        this.name = string;
        return this;
    }

    public FontBuilder size(double d2) {
        this.size = d2;
        return this;
    }

    @Override
    public Font build() {
        Font font = new Font(this.name, this.size);
        return font;
    }
}

