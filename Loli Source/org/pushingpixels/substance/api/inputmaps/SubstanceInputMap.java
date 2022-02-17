/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.inputmaps;

import java.util.Map;
import java.util.TreeMap;
import javax.swing.UIDefaults;

public final class SubstanceInputMap {
    private Map<String, String> mapping = new TreeMap<String, String>();

    public void put(String keyStroke, String actionName) {
        this.mapping.put(keyStroke, actionName);
    }

    public void remove(String keyStroke) {
        this.mapping.remove(keyStroke);
    }

    public UIDefaults.LazyInputMap getUiMap() {
        Object[] flat = new Object[2 * this.mapping.size()];
        int index = 0;
        for (Map.Entry<String, String> entry : this.mapping.entrySet()) {
            flat[index++] = entry.getKey();
            flat[index++] = entry.getValue();
        }
        return new UIDefaults.LazyInputMap(flat);
    }
}

