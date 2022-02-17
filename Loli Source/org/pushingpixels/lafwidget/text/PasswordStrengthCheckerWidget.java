/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.text;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JPasswordField;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.BorderUIResource;
import org.pushingpixels.lafwidget.LafWidgetAdapter;
import org.pushingpixels.lafwidget.LafWidgetRepository;
import org.pushingpixels.lafwidget.LafWidgetSupport;
import org.pushingpixels.lafwidget.LafWidgetUtilities2;
import org.pushingpixels.lafwidget.text.PasswordStrengthChecker;
import org.pushingpixels.lafwidget.utils.LafConstants;

public class PasswordStrengthCheckerWidget
extends LafWidgetAdapter<JPasswordField> {
    protected PropertyChangeListener strengthCheckerListener;

    @Override
    public void installListeners() {
        this.strengthCheckerListener = new PropertyChangeListener(){

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("lafwidgets.passwordStrengthChecker".equals(evt.getPropertyName())) {
                    Object newValue = evt.getNewValue();
                    Object oldValue = evt.getOldValue();
                    if (newValue != null && newValue instanceof PasswordStrengthChecker && !(oldValue instanceof PasswordStrengthChecker)) {
                        ((JPasswordField)PasswordStrengthCheckerWidget.this.jcomp).setBorder(new BorderUIResource.CompoundBorderUIResource(((JPasswordField)PasswordStrengthCheckerWidget.this.jcomp).getBorder(), new StrengthCheckedBorder()));
                    } else {
                        Border coreBorder = UIManager.getBorder("PasswordField.border");
                        ((JPasswordField)PasswordStrengthCheckerWidget.this.jcomp).setBorder(coreBorder);
                        ((JPasswordField)PasswordStrengthCheckerWidget.this.jcomp).setToolTipText(null);
                    }
                }
            }
        };
        ((JPasswordField)this.jcomp).addPropertyChangeListener(this.strengthCheckerListener);
    }

    @Override
    public void uninstallListeners() {
        ((JPasswordField)this.jcomp).removePropertyChangeListener(this.strengthCheckerListener);
        this.strengthCheckerListener = null;
    }

    @Override
    public void installDefaults() {
        super.installDefaults();
        Object checker = ((JPasswordField)this.jcomp).getClientProperty("lafwidgets.passwordStrengthChecker");
        if (checker != null && checker instanceof PasswordStrengthChecker) {
            ((JPasswordField)this.jcomp).setBorder(new BorderUIResource.CompoundBorderUIResource(((JPasswordField)this.jcomp).getBorder(), new StrengthCheckedBorder()));
        }
    }

    @Override
    public boolean requiresCustomLafSupport() {
        return false;
    }

    private static class StrengthCheckedBorder
    implements Border {
        public static final int GUTTER_WIDTH = 5;

        private StrengthCheckedBorder() {
        }

        @Override
        public boolean isBorderOpaque() {
            return true;
        }

        @Override
        public Insets getBorderInsets(Component c2) {
            JPasswordField jpf = (JPasswordField)c2;
            if (LafWidgetUtilities2.getPasswordStrengthChecker(jpf) == null) {
                return new Insets(0, 0, 0, 0);
            }
            if (c2.getComponentOrientation().isLeftToRight()) {
                return new Insets(0, 0, 0, 5);
            }
            return new Insets(0, 5, 0, 0);
        }

        @Override
        public void paintBorder(Component c2, Graphics g2, int x2, int y2, int width, int height) {
            JPasswordField jpf = (JPasswordField)c2;
            PasswordStrengthChecker passwordStrengthChecker = LafWidgetUtilities2.getPasswordStrengthChecker(jpf);
            if (passwordStrengthChecker == null) {
                return;
            }
            LafConstants.PasswordStrength strength = passwordStrengthChecker.getStrength(jpf.getPassword());
            LafWidgetSupport lafSupport = LafWidgetRepository.getRepository().getLafSupport();
            if (c2.getComponentOrientation().isLeftToRight()) {
                lafSupport.paintPasswordStrengthMarker(g2, x2 + width - 5, y2, 5, height, strength);
            } else {
                lafSupport.paintPasswordStrengthMarker(g2, x2, y2, 5, height, strength);
            }
            String tooltip = passwordStrengthChecker.getDescription(strength);
            jpf.setToolTipText(tooltip);
        }
    }
}

