/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.text;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;
import org.pushingpixels.lafwidget.LafWidgetAdapter;
import org.pushingpixels.lafwidget.LafWidgetUtilities;

public class SelectAllOnFocusGainWidget
extends LafWidgetAdapter<JTextComponent> {
    protected FocusListener focusListener;

    @Override
    public boolean requiresCustomLafSupport() {
        return false;
    }

    @Override
    public void installListeners() {
        this.focusListener = new FocusAdapter(){

            @Override
            public void focusGained(FocusEvent e2) {
                SwingUtilities.invokeLater(new Runnable(){

                    @Override
                    public void run() {
                        if (LafWidgetUtilities.hasTextFocusSelectAllProperty((JTextComponent)SelectAllOnFocusGainWidget.this.jcomp) && ((JTextComponent)SelectAllOnFocusGainWidget.this.jcomp).isEditable()) {
                            ((JTextComponent)SelectAllOnFocusGainWidget.this.jcomp).selectAll();
                        }
                    }
                });
            }
        };
        ((JTextComponent)this.jcomp).addFocusListener(this.focusListener);
    }

    @Override
    public void uninstallListeners() {
        ((JTextComponent)this.jcomp).removeFocusListener(this.focusListener);
        this.focusListener = null;
    }
}

