/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.utils;

import java.awt.Component;
import java.awt.Insets;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;
import java.util.Set;
import org.pushingpixels.substance.api.SubstanceConstants;

public class SubstanceOutlineUtilities {
    public static GeneralPath getBaseOutline(Component comp, float radius, Set<SubstanceConstants.Side> straightSides) {
        int width = comp.getWidth();
        int height = comp.getHeight();
        return SubstanceOutlineUtilities.getBaseOutline(width, height, radius, straightSides);
    }

    public static GeneralPath getBaseOutline(int width, int height, float radius, Set<SubstanceConstants.Side> straightSides) {
        return SubstanceOutlineUtilities.getBaseOutline(width, height, radius, straightSides, null);
    }

    public static GeneralPath getBaseOutline(int width, int height, float radius, Set<SubstanceConstants.Side> straightSides, int insets) {
        return SubstanceOutlineUtilities.getBaseOutline(width, height, radius, straightSides, new Insets(insets, insets, insets, insets));
    }

    public static GeneralPath getBaseOutline(int width, int height, float radius, Set<SubstanceConstants.Side> straightSides, Insets insets) {
        int ys;
        boolean isTopLeftCorner = straightSides != null && (straightSides.contains((Object)SubstanceConstants.Side.LEFT) || straightSides.contains((Object)SubstanceConstants.Side.TOP));
        boolean isTopRightCorner = straightSides != null && (straightSides.contains((Object)SubstanceConstants.Side.RIGHT) || straightSides.contains((Object)SubstanceConstants.Side.TOP));
        boolean isBottomRightCorner = straightSides != null && (straightSides.contains((Object)SubstanceConstants.Side.RIGHT) || straightSides.contains((Object)SubstanceConstants.Side.BOTTOM));
        boolean isBottomLeftCorner = straightSides != null && (straightSides.contains((Object)SubstanceConstants.Side.LEFT) || straightSides.contains((Object)SubstanceConstants.Side.BOTTOM));
        int xs = insets == null ? 0 : insets.left;
        int n2 = ys = insets == null ? 0 : insets.top;
        if (insets != null) {
            width -= insets.right + insets.left;
        }
        if (insets != null) {
            height -= insets.top + insets.bottom;
        }
        GeneralPath result = new GeneralPath();
        if (isTopLeftCorner) {
            result.moveTo(xs, ys);
        } else {
            result.moveTo((float)xs + radius, ys);
        }
        if (isTopRightCorner) {
            result.lineTo(xs + width - 1, ys);
        } else {
            if (isTopLeftCorner || (float)(xs + width) - radius - 1.0f >= radius) {
                result.lineTo((float)(xs + width) - radius - 1.0f, ys);
            }
            result.append(new Arc2D.Double((float)(xs + width - 1) - 2.0f * radius, ys, 2.0f * radius, 2.0f * radius, 90.0, -90.0, 0), true);
        }
        if (isBottomRightCorner) {
            result.lineTo(xs + width - 1, ys + height - 1);
        } else {
            if (isTopRightCorner || (float)(ys + height) - radius - 1.0f >= radius) {
                result.lineTo(xs + width - 1, (float)(ys + height) - radius - 1.0f);
            }
            result.append(new Arc2D.Double((float)(xs + width) - 2.0f * radius - 1.0f, (float)(ys + height - 1) - 2.0f * radius, 2.0f * radius, 2.0f * radius, 0.0, -90.0, 0), true);
        }
        if (isBottomLeftCorner) {
            result.lineTo(xs, ys + height - 1);
        } else {
            if (isBottomRightCorner || (float)(xs + width) - radius - 1.0f >= radius) {
                result.lineTo((float)xs + radius, ys + height - 1);
            }
            result.append(new Arc2D.Double(xs, (float)(ys + height) - 2.0f * radius - 1.0f, 2.0f * radius, 2.0f * radius, 270.0, -90.0, 0), true);
        }
        if (isTopLeftCorner) {
            result.lineTo(xs, ys);
        } else {
            if (isBottomLeftCorner || (float)(ys + height) - radius - 1.0f >= radius) {
                result.lineTo(xs, (float)ys + radius);
            }
            result.append(new Arc2D.Double(xs, ys, 2.0f * radius, 2.0f * radius, 180.0, -90.0, 0), true);
        }
        return result;
    }

    public static GeneralPath getTriangleButtonOutline(int width, int height, float radius, int insets) {
        return SubstanceOutlineUtilities.getTriangleButtonOutline(width, height, radius, new Insets(insets, insets, insets, insets));
    }

    public static GeneralPath getTriangleButtonOutline(int width, int height, float radius, Insets insets) {
        int xs = insets.left;
        int ys = insets.top;
        int xe = width - insets.right;
        --xe;
        int ye = height - insets.bottom;
        GeneralPath result = new GeneralPath();
        float radius3 = (float)((double)radius / (1.5 * Math.pow(height -= insets.top + insets.bottom, 0.5)));
        if (Math.max(width -= insets.right + insets.left, height) < 15) {
            radius3 /= 2.0f;
        }
        result.moveTo(radius + (float)xs, ys);
        if ((float)xe - radius >= radius) {
            result.lineTo((float)xe - radius, ys);
        }
        result.quadTo((float)xe - radius3, (float)xs + radius3, xe, (float)xs + radius);
        float h2 = ((float)ye - 1.0f) / 2.0f;
        if (h2 >= radius) {
            result.lineTo(xe, h2);
        }
        result.lineTo((float)(xe + insets.right) / 2.0f, ye - 1);
        result.lineTo(xs, h2);
        if (h2 >= radius) {
            result.lineTo(xs, h2);
        }
        if ((float)height - radius - 1.0f >= radius) {
            result.lineTo(xs, radius + (float)ys);
        }
        result.quadTo((float)xs + radius3, (float)ys + radius3, (float)xs + radius, ys);
        return result;
    }
}

