/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ColorSchemeAssociationKind {
    private static Set<ColorSchemeAssociationKind> values = new HashSet<ColorSchemeAssociationKind>();
    private String name;
    private ColorSchemeAssociationKind fallback;
    public static final ColorSchemeAssociationKind FILL = new ColorSchemeAssociationKind("fill", null);
    public static final ColorSchemeAssociationKind SEPARATOR = new ColorSchemeAssociationKind("separator", FILL);
    public static final ColorSchemeAssociationKind TAB = new ColorSchemeAssociationKind("tab", FILL);
    public static final ColorSchemeAssociationKind BORDER = new ColorSchemeAssociationKind("border", FILL);
    public static final ColorSchemeAssociationKind MARK = new ColorSchemeAssociationKind("mark", BORDER);
    public static final ColorSchemeAssociationKind TAB_BORDER = new ColorSchemeAssociationKind("tabBorder", BORDER);
    public static final ColorSchemeAssociationKind HIGHLIGHT = new ColorSchemeAssociationKind("highlight", FILL);
    public static final ColorSchemeAssociationKind TEXT_HIGHLIGHT = new ColorSchemeAssociationKind("textHighlight", HIGHLIGHT);
    public static final ColorSchemeAssociationKind HIGHLIGHT_BORDER = new ColorSchemeAssociationKind("highlightBorder", BORDER);
    public static final ColorSchemeAssociationKind HIGHLIGHT_MARK = new ColorSchemeAssociationKind("highlightMark", MARK);

    public ColorSchemeAssociationKind(String name, ColorSchemeAssociationKind fallback) {
        this.name = name;
        this.fallback = fallback;
        values.add(this);
    }

    public String toString() {
        return this.name;
    }

    public static Set<ColorSchemeAssociationKind> values() {
        return Collections.unmodifiableSet(values);
    }

    public ColorSchemeAssociationKind getFallback() {
        return this.fallback;
    }
}

