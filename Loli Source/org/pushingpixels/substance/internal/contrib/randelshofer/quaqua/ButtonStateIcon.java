/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.contrib.randelshofer.quaqua;

import java.awt.Component;
import java.awt.Image;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.MultiIcon;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.QuaquaUtilities;

public class ButtonStateIcon
extends MultiIcon {
    private static final int E = 0;
    private static final int EP = 1;
    private static final int ES = 2;
    private static final int EPS = 3;
    private static final int D = 4;
    private static final int DS = 5;
    private static final int I = 6;
    private static final int IS = 7;
    private static final int DI = 8;
    private static final int DIS = 9;

    public ButtonStateIcon(Icon e2, Icon ep, Icon es, Icon eps, Icon d2, Icon ds) {
        super(new Icon[]{e2, ep, es, eps, d2, ds});
    }

    public ButtonStateIcon(Image[] images) {
        super(images);
    }

    public ButtonStateIcon(Icon[] icons) {
        super(icons);
    }

    public ButtonStateIcon(Image tiledImage, int tileCount, boolean isTiledHorizontally) {
        super(tiledImage, tileCount, isTiledHorizontally);
    }

    @Override
    protected Icon getIcon(Component c2) {
        Icon icon;
        boolean isActive = QuaquaUtilities.isOnActiveWindow(c2);
        if (c2 instanceof AbstractButton) {
            ButtonModel model = ((AbstractButton)c2).getModel();
            icon = isActive ? (model.isEnabled() ? (model.isArmed() ? (model.isSelected() ? this.icons[3] : this.icons[1]) : (model.isSelected() ? this.icons[2] : this.icons[0])) : (model.isSelected() ? this.icons[5] : this.icons[4])) : (model.isEnabled() ? (model.isSelected() ? this.icons[7] : this.icons[6]) : (model.isSelected() ? this.icons[9] : this.icons[8]));
        } else {
            icon = isActive ? (c2.isEnabled() ? this.icons[0] : this.icons[4]) : (c2.isEnabled() ? this.icons[6] : this.icons[8]);
        }
        return icon;
    }

    @Override
    protected void generateMissingIcons() {
        if (this.icons.length != 10) {
            Icon[] helper = this.icons;
            this.icons = new Icon[10];
            System.arraycopy(helper, 0, this.icons, 0, Math.min(helper.length, this.icons.length));
        }
        if (this.icons[1] == null) {
            this.icons[1] = this.icons[0];
        }
        if (this.icons[2] == null) {
            this.icons[2] = this.icons[1];
        }
        if (this.icons[3] == null) {
            this.icons[3] = this.icons[1];
        }
        if (this.icons[4] == null) {
            this.icons[4] = this.icons[0];
        }
        if (this.icons[5] == null) {
            this.icons[5] = this.icons[2];
        }
        if (this.icons[6] == null) {
            this.icons[6] = this.icons[0];
        }
        if (this.icons[7] == null) {
            this.icons[7] = this.icons[2];
        }
        if (this.icons[8] == null) {
            this.icons[8] = this.icons[4];
        }
        if (this.icons[9] == null) {
            this.icons[9] = this.icons[5];
        }
    }
}

