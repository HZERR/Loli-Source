/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.scroll;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneLayout;
import javax.swing.Scrollable;
import javax.swing.UIManager;
import javax.swing.border.Border;

public class TweakedScrollPaneLayout
extends ScrollPaneLayout {
    private void superlayoutContainer(Container parent) {
        boolean hsbNeeded;
        boolean vsbNeeded;
        Scrollable sv;
        boolean isEmpty;
        Insets vpbInsets;
        Border viewportBorder;
        JScrollPane scrollPane = (JScrollPane)parent;
        this.vsbPolicy = scrollPane.getVerticalScrollBarPolicy();
        this.hsbPolicy = scrollPane.getHorizontalScrollBarPolicy();
        Rectangle availR = scrollPane.getBounds();
        Insets insets = parent.getInsets();
        availR.x = insets.left;
        availR.y = insets.top;
        availR.width -= insets.left + insets.right;
        availR.height -= insets.top + insets.bottom;
        boolean leftToRight = scrollPane.getComponentOrientation().isLeftToRight();
        Rectangle colHeadR = new Rectangle(0, availR.y, 0, 0);
        if (this.colHead != null && this.colHead.isVisible()) {
            int colHeadHeight;
            colHeadR.height = colHeadHeight = Math.min(availR.height, this.colHead.getPreferredSize().height);
            availR.y += colHeadHeight;
            availR.height -= colHeadHeight;
        }
        Rectangle rowHeadR = new Rectangle(0, 0, 0, 0);
        if (this.rowHead != null && this.rowHead.isVisible()) {
            int rowHeadWidth;
            rowHeadR.width = rowHeadWidth = Math.min(availR.width, this.rowHead.getPreferredSize().width);
            availR.width -= rowHeadWidth;
            if (leftToRight) {
                rowHeadR.x = availR.x;
                availR.x += rowHeadWidth;
            } else {
                rowHeadR.x = availR.x + availR.width;
            }
        }
        if ((viewportBorder = scrollPane.getViewportBorder()) != null) {
            vpbInsets = viewportBorder.getBorderInsets(parent);
            availR.x += vpbInsets.left;
            availR.y += vpbInsets.top;
            availR.width -= vpbInsets.left + vpbInsets.right;
            availR.height -= vpbInsets.top + vpbInsets.bottom;
        } else {
            vpbInsets = new Insets(0, 0, 0, 0);
        }
        Component view = this.viewport != null ? this.viewport.getView() : null;
        Dimension viewPrefSize = view != null ? view.getPreferredSize() : new Dimension(0, 0);
        Dimension extentSize = this.viewport != null ? this.viewport.toViewCoordinates(availR.getSize()) : new Dimension(0, 0);
        boolean viewTracksViewportWidth = false;
        boolean viewTracksViewportHeight = false;
        boolean bl = isEmpty = availR.width < 0 || availR.height < 0;
        if (!isEmpty && view instanceof Scrollable) {
            sv = (Scrollable)((Object)view);
            viewTracksViewportWidth = sv.getScrollableTracksViewportWidth();
            viewTracksViewportHeight = sv.getScrollableTracksViewportHeight();
        } else {
            sv = null;
        }
        Rectangle vsbR = new Rectangle(0, availR.y - vpbInsets.top, 0, 0);
        if (isEmpty) {
            vsbNeeded = false;
        } else if (this.vsbPolicy == 22) {
            vsbNeeded = true;
        } else if (this.vsbPolicy == 21) {
            vsbNeeded = false;
        } else {
            boolean bl2 = vsbNeeded = !viewTracksViewportHeight && viewPrefSize.height > extentSize.height;
        }
        if (this.vsb != null && vsbNeeded) {
            this.adjustForVSB(true, availR, vsbR, vpbInsets, leftToRight);
            extentSize = this.viewport.toViewCoordinates(availR.getSize());
        }
        Rectangle hsbR = new Rectangle(availR.x - vpbInsets.left, 0, 0, 0);
        if (isEmpty) {
            hsbNeeded = false;
        } else if (this.hsbPolicy == 32) {
            hsbNeeded = true;
        } else if (this.hsbPolicy == 31) {
            hsbNeeded = false;
        } else {
            boolean bl3 = hsbNeeded = !viewTracksViewportWidth && viewPrefSize.width > extentSize.width;
        }
        if (this.hsb != null && hsbNeeded) {
            this.adjustForHSB(true, availR, hsbR, vpbInsets);
            if (this.vsb != null && !vsbNeeded && this.vsbPolicy != 21) {
                extentSize = this.viewport.toViewCoordinates(availR.getSize());
                boolean bl4 = vsbNeeded = viewPrefSize.height > extentSize.height;
                if (vsbNeeded) {
                    this.adjustForVSB(true, availR, vsbR, vpbInsets, leftToRight);
                    if (!leftToRight) {
                        hsbR.x += vsbR.width;
                    }
                }
            }
        }
        if (this.viewport != null) {
            this.viewport.setBounds(availR);
            if (sv != null) {
                extentSize = this.viewport.toViewCoordinates(availR.getSize());
                boolean oldHSBNeeded = hsbNeeded;
                boolean oldVSBNeeded = vsbNeeded;
                viewTracksViewportWidth = sv.getScrollableTracksViewportWidth();
                viewTracksViewportHeight = sv.getScrollableTracksViewportHeight();
                if (this.vsb != null && this.vsbPolicy == 20) {
                    boolean newVSBNeeded;
                    boolean bl5 = newVSBNeeded = !viewTracksViewportHeight && viewPrefSize.height > extentSize.height;
                    if (newVSBNeeded != vsbNeeded) {
                        vsbNeeded = newVSBNeeded;
                        this.adjustForVSB(vsbNeeded, availR, vsbR, vpbInsets, leftToRight);
                        extentSize = this.viewport.toViewCoordinates(availR.getSize());
                    }
                }
                if (this.hsb != null && this.hsbPolicy == 30) {
                    boolean newHSBbNeeded;
                    boolean bl6 = newHSBbNeeded = !viewTracksViewportWidth && viewPrefSize.width > extentSize.width;
                    if (newHSBbNeeded != hsbNeeded) {
                        hsbNeeded = newHSBbNeeded;
                        this.adjustForHSB(hsbNeeded, availR, hsbR, vpbInsets);
                        if (this.vsb != null && !vsbNeeded && this.vsbPolicy != 21) {
                            extentSize = this.viewport.toViewCoordinates(availR.getSize());
                            boolean bl7 = vsbNeeded = viewPrefSize.height > extentSize.height;
                            if (vsbNeeded) {
                                this.adjustForVSB(true, availR, vsbR, vpbInsets, leftToRight);
                            }
                        }
                    }
                }
                if (oldHSBNeeded != hsbNeeded || oldVSBNeeded != vsbNeeded) {
                    this.viewport.setBounds(availR);
                }
            }
        }
        vsbR.height = availR.height + vpbInsets.top + vpbInsets.bottom;
        hsbR.width = availR.width + vpbInsets.left + vpbInsets.right;
        rowHeadR.height = availR.height + vpbInsets.top + vpbInsets.bottom;
        rowHeadR.y = availR.y - vpbInsets.top;
        colHeadR.width = availR.width + vpbInsets.left + vpbInsets.right;
        colHeadR.x = availR.x - vpbInsets.left;
        if (this.rowHead != null) {
            this.rowHead.setBounds(rowHeadR);
        }
        if (this.colHead != null) {
            this.colHead.setBounds(colHeadR);
        }
        if (this.vsb != null) {
            if (vsbNeeded) {
                this.vsb.setVisible(true);
                this.vsb.setBounds(vsbR);
            } else {
                this.vsb.setVisible(false);
            }
        }
        if (this.hsb != null) {
            if (hsbNeeded) {
                this.hsb.setVisible(true);
                this.hsb.setBounds(hsbR);
            } else {
                this.hsb.setVisible(false);
            }
        }
        if (this.lowerLeft != null) {
            this.lowerLeft.setBounds(leftToRight ? rowHeadR.x : vsbR.x, hsbR.y, leftToRight ? rowHeadR.width : vsbR.width, hsbR.height);
        }
        if (this.lowerRight != null) {
            this.lowerRight.setBounds(leftToRight ? vsbR.x : rowHeadR.x, hsbR.y, leftToRight ? vsbR.width : rowHeadR.width, hsbR.height);
        }
        if (this.upperLeft != null) {
            this.upperLeft.setBounds(leftToRight ? rowHeadR.x : vsbR.x, colHeadR.y, leftToRight ? rowHeadR.width : vsbR.width, colHeadR.height);
        }
        if (this.upperRight != null) {
            this.upperRight.setBounds(leftToRight ? vsbR.x : rowHeadR.x, colHeadR.y, leftToRight ? vsbR.width : rowHeadR.width, colHeadR.height);
        }
    }

    private void adjustForVSB(boolean wantsVSB, Rectangle available, Rectangle vsbR, Insets vpbInsets, boolean leftToRight) {
        int oldWidth = vsbR.width;
        if (wantsVSB) {
            int vsbWidth = Math.max(0, Math.min(this.vsb.getPreferredSize().width, available.width));
            available.width -= vsbWidth;
            vsbR.width = vsbWidth;
            if (leftToRight) {
                vsbR.x = available.x + available.width + vpbInsets.right;
            } else {
                vsbR.x = available.x - vpbInsets.left;
                available.x += vsbWidth;
            }
        } else {
            available.width += oldWidth;
        }
    }

    private void adjustForHSB(boolean wantsHSB, Rectangle available, Rectangle hsbR, Insets vpbInsets) {
        int oldHeight = hsbR.height;
        if (wantsHSB) {
            int hsbHeight = Math.max(0, Math.min(available.height, this.hsb.getPreferredSize().height));
            available.height -= hsbHeight;
            hsbR.y = available.y + available.height + vpbInsets.bottom;
            hsbR.height = hsbHeight;
        } else {
            available.height += oldHeight;
        }
    }

    @Override
    public void layoutContainer(Container parent) {
        boolean onlyHsb;
        boolean onlyVsb;
        this.superlayoutContainer(parent);
        boolean isLeftToRight = parent.getComponentOrientation().isLeftToRight();
        if (isLeftToRight && this.lowerRight == null) {
            return;
        }
        if (!isLeftToRight && this.lowerLeft == null) {
            return;
        }
        boolean bl = onlyVsb = this.vsb != null && this.vsb.isVisible() && (this.hsb == null || this.hsb != null && !this.hsb.isVisible());
        if (onlyVsb) {
            Rectangle vsbBounds = this.vsb.getBounds();
            int delta = this.hsb == null ? (Integer)UIManager.get("ScrollBar.width") : this.hsb.getPreferredSize().height;
            vsbBounds.height -= delta;
            this.vsb.setBounds(vsbBounds);
            if (isLeftToRight) {
                this.lowerRight.setBounds(vsbBounds.x, vsbBounds.y + vsbBounds.height, vsbBounds.width, delta);
                this.lowerRight.setVisible(true);
            } else {
                this.lowerLeft.setBounds(vsbBounds.x, vsbBounds.y + vsbBounds.height, vsbBounds.width, delta);
                this.lowerLeft.setVisible(true);
            }
        }
        boolean bl2 = onlyHsb = this.hsb != null && this.hsb.isVisible() && (this.vsb == null || this.vsb != null && !this.vsb.isVisible());
        if (onlyHsb) {
            Rectangle hsbBounds = this.hsb.getBounds();
            int delta = this.vsb == null ? (Integer)UIManager.get("ScrollBar.width") : this.vsb.getPreferredSize().width;
            hsbBounds.width -= delta;
            if (!isLeftToRight) {
                hsbBounds.x += delta;
            }
            this.hsb.setBounds(hsbBounds);
            if (isLeftToRight) {
                this.lowerRight.setBounds(hsbBounds.x + hsbBounds.width, hsbBounds.y, delta, hsbBounds.height);
                this.lowerRight.setVisible(true);
            } else {
                this.lowerLeft.setBounds(hsbBounds.x - delta, hsbBounds.y, delta, hsbBounds.height);
                this.lowerLeft.setVisible(true);
            }
        }
    }
}

