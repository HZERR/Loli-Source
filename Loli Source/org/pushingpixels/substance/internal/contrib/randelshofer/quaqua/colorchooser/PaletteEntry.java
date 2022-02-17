/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser;

import java.awt.Color;

public class PaletteEntry {
    private String name;
    private Color color;

    public PaletteEntry(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return this.name;
    }

    public Color getColor() {
        return this.color;
    }
}

