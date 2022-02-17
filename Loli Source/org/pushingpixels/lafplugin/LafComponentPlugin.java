/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafplugin;

import org.pushingpixels.lafplugin.LafPlugin;

public interface LafComponentPlugin
extends LafPlugin {
    public static final String COMPONENT_TAG_PLUGIN_CLASS = "component-plugin-class";

    public void initialize();

    public void uninitialize();

    public Object[] getDefaults(Object var1);
}

