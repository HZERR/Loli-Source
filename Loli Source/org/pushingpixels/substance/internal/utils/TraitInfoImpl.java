/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.utils;

import org.pushingpixels.substance.api.trait.SubstanceTraitInfo;

public class TraitInfoImpl
implements SubstanceTraitInfo {
    private String displayName;
    private String className;
    private boolean isDefault;

    public TraitInfoImpl(String displayName, String className) {
        this.displayName = displayName;
        this.className = className;
    }

    @Override
    public String getClassName() {
        return this.className;
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public boolean isDefault() {
        return this.isDefault;
    }

    @Override
    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }
}

