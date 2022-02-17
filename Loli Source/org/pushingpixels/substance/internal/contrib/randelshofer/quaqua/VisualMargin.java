/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.contrib.randelshofer.quaqua;

import java.awt.Component;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.UIResource;

public class VisualMargin
extends AbstractBorder
implements UIResource {
    private Insets layoutMargin;
    private String uiManagerPropertyName = "Component.visualMargin";
    private String propertyName = "Quaqua.Component.visualMargin";
    private boolean isTopFixed;
    private boolean isLeftFixed;
    private boolean isBottomFixed;
    private boolean isRightFixed;

    public VisualMargin() {
        this.layoutMargin = new Insets(0, 0, 0, 0);
    }

    public void setPropertyName(String propertyName) {
    }

    public void setFixed(boolean top, boolean left, boolean bottom, boolean right) {
        this.isTopFixed = top;
        this.isLeftFixed = left;
        this.isBottomFixed = bottom;
        this.isRightFixed = right;
    }

    public VisualMargin(int top, int left, int bottom, int right) {
        this.layoutMargin = new Insets(top, left, bottom, right);
    }

    public VisualMargin(int top, int left, int bottom, int right, boolean ftop, boolean fleft, boolean fbottom, boolean fright) {
        this.layoutMargin = new Insets(top, left, bottom, right);
        this.isTopFixed = ftop;
        this.isLeftFixed = fleft;
        this.isBottomFixed = fbottom;
        this.isRightFixed = fright;
    }

    public VisualMargin(boolean ftop, boolean fleft, boolean fbottom, boolean fright) {
        this.layoutMargin = new Insets(0, 0, 0, 0);
        this.isTopFixed = ftop;
        this.isLeftFixed = fleft;
        this.isBottomFixed = fbottom;
        this.isRightFixed = fright;
    }

    public VisualMargin(Insets layoutMargin) {
        this.layoutMargin = layoutMargin;
    }

    public Insets getVisualMargin(Component c2) {
        Insets insets = new Insets(this.layoutMargin.top, this.layoutMargin.left, this.layoutMargin.bottom, this.layoutMargin.right);
        if (c2 instanceof JComponent) {
            Insets componentMargin = (Insets)((JComponent)c2).getClientProperty(this.propertyName);
            if (componentMargin == null && this.propertyName != null) {
                componentMargin = UIManager.getInsets(this.uiManagerPropertyName);
            }
            if (componentMargin != null) {
                if (!this.isTopFixed) {
                    insets.top = componentMargin.top;
                }
                if (!this.isLeftFixed) {
                    insets.left = componentMargin.left;
                }
                if (!this.isBottomFixed) {
                    insets.bottom = componentMargin.bottom;
                }
                if (!this.isRightFixed) {
                    insets.right = componentMargin.right;
                }
            }
        }
        return insets;
    }

    @Override
    public Insets getBorderInsets(Component c2) {
        return this.getBorderInsets(c2, new Insets(0, 0, 0, 0));
    }

    @Override
    public Insets getBorderInsets(Component c2, Insets insets) {
        return this.getVisualMargin(c2, insets);
    }

    protected Insets getVisualMargin(Component c2, Insets insets) {
        insets.top = -this.layoutMargin.top;
        insets.left = -this.layoutMargin.left;
        insets.bottom = -this.layoutMargin.bottom;
        insets.right = -this.layoutMargin.right;
        if (c2 instanceof JComponent) {
            Insets componentMargin = (Insets)((JComponent)c2).getClientProperty(this.propertyName);
            if (componentMargin == null && this.propertyName != null) {
                componentMargin = UIManager.getInsets(this.uiManagerPropertyName);
            }
            if (componentMargin != null) {
                if (!this.isTopFixed) {
                    insets.top += componentMargin.top;
                }
                if (!this.isLeftFixed) {
                    insets.left += componentMargin.left;
                }
                if (!this.isBottomFixed) {
                    insets.bottom += componentMargin.bottom;
                }
                if (!this.isRightFixed) {
                    insets.right += componentMargin.right;
                }
            }
        }
        return insets;
    }
}

