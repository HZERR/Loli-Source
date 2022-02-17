/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.text;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import org.pushingpixels.lafwidget.LafWidgetAdapter;
import org.pushingpixels.lafwidget.LafWidgetRepository;
import org.pushingpixels.lafwidget.LafWidgetSupport;
import org.pushingpixels.lafwidget.text.LockBorder;

public class LockBorderWidget
extends LafWidgetAdapter {
    protected PropertyChangeListener propertyChangeListener;
    protected boolean isUninstalling = false;
    public static String ORIGINAL_BORDER = "lafwidget.internal.originalBorder";

    @Override
    public void installListeners() {
        this.propertyChangeListener = new PropertyChangeListener(){

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("border".equals(evt.getPropertyName())) {
                    return;
                }
                if (ORIGINAL_BORDER.equals(evt.getPropertyName())) {
                    return;
                }
                SwingUtilities.invokeLater(new Runnable(){

                    @Override
                    public void run() {
                        if (LockBorderWidget.this.isUninstalling) {
                            return;
                        }
                        LafWidgetSupport lafSupport = LafWidgetRepository.getRepository().getLafSupport();
                        boolean hasLockIcon = lafSupport.hasLockIcon(LockBorderWidget.this.jcomp);
                        if (hasLockIcon) {
                            LockBorderWidget.this.installLockBorder();
                        } else {
                            LockBorderWidget.this.restoreOriginalBorder();
                        }
                    }
                });
            }
        };
        this.jcomp.addPropertyChangeListener(this.propertyChangeListener);
    }

    @Override
    public void uninstallListeners() {
        this.jcomp.removePropertyChangeListener(this.propertyChangeListener);
        this.propertyChangeListener = null;
    }

    @Override
    public void uninstallUI() {
        this.isUninstalling = true;
        Border original = (Border)this.jcomp.getClientProperty(ORIGINAL_BORDER);
        if (original != null) {
            this.jcomp.setBorder(original);
        }
    }

    @Override
    public void installDefaults() {
        super.installDefaults();
        LafWidgetSupport lafSupport = LafWidgetRepository.getRepository().getLafSupport();
        boolean hasLockIcon = lafSupport.hasLockIcon(this.jcomp);
        if (hasLockIcon) {
            Border currBorder = this.jcomp.getBorder();
            this.jcomp.putClientProperty(ORIGINAL_BORDER, currBorder);
            this.jcomp.setBorder(new LockBorder(currBorder));
        }
    }

    @Override
    public void uninstallDefaults() {
        this.isUninstalling = true;
        this.jcomp.putClientProperty(ORIGINAL_BORDER, null);
    }

    @Override
    public boolean requiresCustomLafSupport() {
        return false;
    }

    private void installLockBorder() {
        if (this.jcomp.getClientProperty(ORIGINAL_BORDER) instanceof Border) {
            return;
        }
        Border currBorder = this.jcomp.getBorder();
        if (currBorder != null) {
            this.jcomp.putClientProperty(ORIGINAL_BORDER, currBorder);
            this.jcomp.setBorder(new LockBorder(currBorder));
        }
    }

    private void restoreOriginalBorder() {
        if (this.jcomp.getClientProperty(ORIGINAL_BORDER) instanceof Border) {
            Border originalBorder = (Border)this.jcomp.getClientProperty(ORIGINAL_BORDER);
            this.jcomp.setBorder(originalBorder);
            this.jcomp.putClientProperty(ORIGINAL_BORDER, null);
        }
    }
}

