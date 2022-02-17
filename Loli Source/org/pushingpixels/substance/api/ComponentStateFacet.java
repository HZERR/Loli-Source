/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api;

public final class ComponentStateFacet {
    int value;
    String name;
    public static final ComponentStateFacet ENABLE = new ComponentStateFacet("enable", 0);
    public static final ComponentStateFacet ROLLOVER = new ComponentStateFacet("rollover", 10);
    public static final ComponentStateFacet SELECTION = new ComponentStateFacet("selection", 10);
    public static final ComponentStateFacet PRESS = new ComponentStateFacet("press", 50);
    public static final ComponentStateFacet ARM = new ComponentStateFacet("arm", 10);
    public static final ComponentStateFacet DEFAULT = new ComponentStateFacet("default", 500);
    public static final ComponentStateFacet DETERMINATE = new ComponentStateFacet("determinate", 10);
    public static final ComponentStateFacet EDITABLE = new ComponentStateFacet("editable", 50);

    public ComponentStateFacet(String name, int value) {
        this.name = name;
        if (value < 0) {
            throw new IllegalArgumentException("Facet value must be non-negative");
        }
        this.value = value;
    }

    public String toString() {
        return this.name + ":" + this.value;
    }
}

