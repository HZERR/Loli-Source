/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafplugin;

import java.util.Set;
import javax.swing.UIDefaults;
import org.pushingpixels.lafplugin.LafComponentPlugin;
import org.pushingpixels.lafplugin.PluginManager;

public class ComponentPluginManager
extends PluginManager {
    public ComponentPluginManager(String xmlName) {
        super(xmlName, "laf-plugin", "component-plugin-class");
    }

    public void initializeAll() {
        Set availablePlugins = this.getAvailablePlugins();
        for (Object pluginObject : availablePlugins) {
            if (!(pluginObject instanceof LafComponentPlugin)) continue;
            ((LafComponentPlugin)pluginObject).initialize();
        }
    }

    public void uninitializeAll() {
        Set availablePlugins = this.getAvailablePlugins();
        for (Object pluginObject : availablePlugins) {
            if (!(pluginObject instanceof LafComponentPlugin)) continue;
            ((LafComponentPlugin)pluginObject).uninitialize();
        }
    }

    public void processAllDefaultsEntries(UIDefaults table, Object themeInfo) {
        Set availablePlugins = this.getAvailablePlugins();
        for (Object pluginObject : availablePlugins) {
            Object[] defaults;
            if (!(pluginObject instanceof LafComponentPlugin) || (defaults = ((LafComponentPlugin)pluginObject).getDefaults(themeInfo)) == null) continue;
            table.putDefaults(defaults);
        }
    }
}

