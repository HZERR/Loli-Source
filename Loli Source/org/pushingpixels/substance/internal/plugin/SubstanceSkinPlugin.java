/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.plugin;

import java.util.Set;
import org.pushingpixels.lafplugin.LafPlugin;
import org.pushingpixels.substance.api.skin.SkinInfo;

public interface SubstanceSkinPlugin
extends LafPlugin {
    public static final String TAG_SKIN_PLUGIN_CLASS = "skin-plugin-class";

    public Set<SkinInfo> getSkins();

    public String getDefaultSkinClassName();
}

