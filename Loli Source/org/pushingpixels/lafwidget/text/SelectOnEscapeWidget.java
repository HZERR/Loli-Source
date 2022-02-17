/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.text;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;
import org.pushingpixels.lafwidget.LafWidgetAdapter;
import org.pushingpixels.lafwidget.LafWidgetUtilities;

public class SelectOnEscapeWidget
extends LafWidgetAdapter<JTextComponent> {
    protected PropertyChangeListener propertyChangeListener;

    @Override
    public boolean requiresCustomLafSupport() {
        return false;
    }

    private void installTracking() {
        InputMap currMap = SwingUtilities.getUIInputMap(this.jcomp, 0);
        InputMap newMap = new InputMap();
        if (currMap != null) {
            KeyStroke[] kss = currMap.allKeys();
            for (int i2 = 0; i2 < kss.length; ++i2) {
                KeyStroke stroke = kss[i2];
                Object val2 = currMap.get(stroke);
                newMap.put(stroke, val2);
            }
        }
        newMap.put(KeyStroke.getKeyStroke(27, 0), "flipTextSelection");
        ((JTextComponent)this.jcomp).getActionMap().put("flipTextSelection", new AbstractAction(){

            @Override
            public void actionPerformed(ActionEvent e2) {
                SwingUtilities.invokeLater(new Runnable(){

                    @Override
                    public void run() {
                        int selectionLength = ((JTextComponent)SelectOnEscapeWidget.this.jcomp).getSelectionEnd() - ((JTextComponent)SelectOnEscapeWidget.this.jcomp).getSelectionStart();
                        if (selectionLength == 0) {
                            ((JTextComponent)SelectOnEscapeWidget.this.jcomp).selectAll();
                        } else {
                            int lastPos = ((JTextComponent)SelectOnEscapeWidget.this.jcomp).getSelectionEnd();
                            ((JTextComponent)SelectOnEscapeWidget.this.jcomp).setSelectionStart(0);
                            ((JTextComponent)SelectOnEscapeWidget.this.jcomp).setSelectionEnd(0);
                            ((JTextComponent)SelectOnEscapeWidget.this.jcomp).setCaretPosition(lastPos);
                        }
                    }
                });
            }
        });
        SwingUtilities.replaceUIInputMap(this.jcomp, 0, newMap);
    }

    private void uninstallTracking() {
        InputMap currMap = SwingUtilities.getUIInputMap(this.jcomp, 0);
        if (currMap != null) {
            InputMap newMap = new InputMap();
            KeyStroke[] kss = currMap.allKeys();
            for (int i2 = 0; i2 < kss.length; ++i2) {
                KeyStroke stroke = kss[i2];
                Object val2 = currMap.get(stroke);
                if (stroke.equals(KeyStroke.getKeyStroke(27, 0)) && "flipTextSelection".equals(val2)) continue;
                newMap.put(stroke, val2);
            }
            SwingUtilities.replaceUIInputMap(this.jcomp, 0, newMap);
        }
        ((JTextComponent)this.jcomp).getActionMap().remove("flipTextSelection");
    }

    @Override
    public void installListeners() {
        this.propertyChangeListener = new PropertyChangeListener(){

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("lafwidgets.textFlipSelectOnEscape".equals(evt.getPropertyName())) {
                    boolean hasTextFlipSelection = LafWidgetUtilities.hasTextFlipSelectOnEscapeProperty((JTextComponent)SelectOnEscapeWidget.this.jcomp);
                    if (hasTextFlipSelection) {
                        SelectOnEscapeWidget.this.installTracking();
                    } else {
                        SelectOnEscapeWidget.this.uninstallTracking();
                    }
                }
            }
        };
        ((JTextComponent)this.jcomp).addPropertyChangeListener(this.propertyChangeListener);
    }

    @Override
    public void uninstallListeners() {
        ((JTextComponent)this.jcomp).removePropertyChangeListener(this.propertyChangeListener);
        this.propertyChangeListener = null;
    }
}

