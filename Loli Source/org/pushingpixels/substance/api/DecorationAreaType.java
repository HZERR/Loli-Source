/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api;

public final class DecorationAreaType {
    String displayName;
    public static final DecorationAreaType PRIMARY_TITLE_PANE = new DecorationAreaType("Primary title pane");
    public static final DecorationAreaType PRIMARY_TITLE_PANE_INACTIVE = new DecorationAreaType("Primary title pane Inactive");
    public static final DecorationAreaType SECONDARY_TITLE_PANE = new DecorationAreaType("Secondary title pane");
    public static final DecorationAreaType SECONDARY_TITLE_PANE_INACTIVE = new DecorationAreaType("Secondary title pane Inactive");
    public static final DecorationAreaType TOOLBAR = new DecorationAreaType("Toolbar");
    public static final DecorationAreaType HEADER = new DecorationAreaType("Header");
    public static final DecorationAreaType FOOTER = new DecorationAreaType("Footer");
    public static final DecorationAreaType GENERAL = new DecorationAreaType("General");
    public static final DecorationAreaType NONE = new DecorationAreaType("None");

    public DecorationAreaType(String displayName) {
        this.displayName = displayName;
    }

    public String toString() {
        return this.displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }
}

