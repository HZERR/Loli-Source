/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.skin;

import org.pushingpixels.substance.internal.utils.TraitInfoImpl;

public class SkinInfo
extends TraitInfoImpl {
    public SkinInfo(String skinDisplayName, String skinClassName) {
        super(skinDisplayName, skinClassName);
    }

    public boolean equals(Object obj) {
        if (obj instanceof SkinInfo) {
            return this.getDisplayName().equals(((SkinInfo)obj).getDisplayName());
        }
        return false;
    }

    public int hashCode() {
        return this.getDisplayName().hashCode();
    }
}

