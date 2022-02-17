/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser;

import java.awt.Color;
import javax.swing.AbstractListModel;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser.PaletteEntry;

public class PaletteListModel
extends AbstractListModel {
    private String name;
    private String info;
    private PaletteEntry[] entries;
    private int closestIndex;

    public PaletteListModel(String name, String info, PaletteEntry[] entries) {
        this.name = name;
        this.info = info;
        this.entries = entries;
    }

    public void setName(String newValue) {
        this.name = newValue;
    }

    public String getName() {
        return this.name;
    }

    public void setInfo(String newValue) {
        this.info = newValue;
    }

    public String getInfo() {
        return this.info;
    }

    @Override
    public Object getElementAt(int index) {
        return this.entries[index];
    }

    @Override
    public int getSize() {
        return this.entries.length;
    }

    public String toString() {
        return this.getName();
    }

    public int computeClosestIndex(Color referenceColor) {
        int refRGB = referenceColor.getRGB();
        int closest = -1;
        int closestDistance = 3072;
        int n2 = this.getSize();
        for (int i2 = 0; i2 < n2; ++i2) {
            int bDiff;
            int gDiff;
            PaletteEntry entry = (PaletteEntry)this.getElementAt(i2);
            int entryRGB = entry.getColor().getRGB();
            int rDiff = (entryRGB & 0xFF0000) - (refRGB & 0xFF0000) >> 16;
            int distance = rDiff * rDiff + (gDiff = (entryRGB & 0xFF00) - (refRGB & 0xFF00) >> 8) * gDiff + (bDiff = (entryRGB & 0xFF) - (refRGB & 0xFF)) * bDiff;
            if (distance >= closestDistance) continue;
            closest = i2;
            closestDistance = distance;
        }
        return closest;
    }

    public void setClosestIndex(int newValue) {
        this.closestIndex = newValue;
    }

    public int getClosestIndex() {
        return this.closestIndex;
    }
}

