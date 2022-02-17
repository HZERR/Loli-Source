/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget;

import java.awt.Component;
import java.awt.Container;
import javax.swing.JComponent;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import org.pushingpixels.lafwidget.preview.PreviewPainter;
import org.pushingpixels.lafwidget.tabbed.TabPreviewPainter;
import org.pushingpixels.lafwidget.text.PasswordStrengthChecker;

public class LafWidgetUtilities2 {
    private LafWidgetUtilities2() {
    }

    public static TabPreviewPainter getTabPreviewPainter(JTabbedPane tabbedPane) {
        if (tabbedPane == null) {
            return null;
        }
        Object tabProp = tabbedPane.getClientProperty("lafwidgets.tabbedpanePreviewPainter");
        if (tabProp instanceof TabPreviewPainter) {
            return (TabPreviewPainter)tabProp;
        }
        return null;
    }

    public static PreviewPainter getComponentPreviewPainter(Component comp) {
        Object parentProp;
        Object compProp;
        if (comp == null) {
            return null;
        }
        if (comp instanceof JComponent && (compProp = ((JComponent)comp).getClientProperty("lafwidgets.componentPreviewPainter")) instanceof PreviewPainter) {
            return (PreviewPainter)compProp;
        }
        Container parent = comp.getParent();
        if (parent instanceof JComponent && (parentProp = ((JComponent)parent).getClientProperty("lafwidgets.componentPreviewPainter")) instanceof PreviewPainter) {
            return (PreviewPainter)parentProp;
        }
        Object globProp = UIManager.get("lafwidgets.componentPreviewPainter");
        if (globProp instanceof PreviewPainter) {
            return (PreviewPainter)globProp;
        }
        return null;
    }

    public static PasswordStrengthChecker getPasswordStrengthChecker(JPasswordField jpf) {
        Object obj = jpf.getClientProperty("lafwidgets.passwordStrengthChecker");
        if (obj != null && obj instanceof PasswordStrengthChecker) {
            return (PasswordStrengthChecker)obj;
        }
        return null;
    }
}

