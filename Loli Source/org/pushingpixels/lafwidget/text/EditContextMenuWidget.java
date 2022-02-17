/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.text;

import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;
import org.pushingpixels.lafwidget.LafWidgetAdapter;
import org.pushingpixels.lafwidget.LafWidgetUtilities;

public class EditContextMenuWidget
extends LafWidgetAdapter<JTextComponent> {
    protected MouseListener menuMouseListener;

    @Override
    public boolean requiresCustomLafSupport() {
        return false;
    }

    @Override
    public void installListeners() {
        this.menuMouseListener = new MouseAdapter(){

            @Override
            public void mousePressed(MouseEvent e2) {
                this.handleMouseEvent(e2);
            }

            @Override
            public void mouseReleased(MouseEvent e2) {
                this.handleMouseEvent(e2);
            }

            private void handleMouseEvent(MouseEvent e2) {
                if (!LafWidgetUtilities.hasTextEditContextMenu((JTextComponent)EditContextMenuWidget.this.jcomp)) {
                    return;
                }
                if (!e2.isPopupTrigger()) {
                    return;
                }
                ((JTextComponent)EditContextMenuWidget.this.jcomp).requestFocus(true);
                JPopupMenu editMenu = new JPopupMenu();
                editMenu.add(new CutAction());
                editMenu.add(new CopyAction());
                editMenu.add(new PasteAction());
                editMenu.addSeparator();
                editMenu.add(new DeleteAction());
                editMenu.add(new SelectAllAction());
                Point pt = SwingUtilities.convertPoint(e2.getComponent(), e2.getPoint(), EditContextMenuWidget.this.jcomp);
                editMenu.show(EditContextMenuWidget.this.jcomp, pt.x, pt.y);
            }
        };
        ((JTextComponent)this.jcomp).addMouseListener(this.menuMouseListener);
    }

    @Override
    public void uninstallListeners() {
        ((JTextComponent)this.jcomp).removeMouseListener(this.menuMouseListener);
        this.menuMouseListener = null;
    }

    private class CopyAction
    extends AbstractAction {
        public CopyAction() {
            super(LafWidgetUtilities.getResourceBundle(EditContextMenuWidget.this.jcomp).getString("EditMenu.copy"), new ImageIcon(EditContextMenuWidget.class.getClassLoader().getResource("org/pushingpixels/lafwidget/text/edit-copy.png")));
        }

        @Override
        public void actionPerformed(ActionEvent e2) {
            ((JTextComponent)EditContextMenuWidget.this.jcomp).copy();
        }

        @Override
        public boolean isEnabled() {
            return ((JTextComponent)EditContextMenuWidget.this.jcomp).isEnabled() && ((JTextComponent)EditContextMenuWidget.this.jcomp).getSelectedText() != null;
        }
    }

    private class CutAction
    extends AbstractAction {
        public CutAction() {
            super(LafWidgetUtilities.getResourceBundle(EditContextMenuWidget.this.jcomp).getString("EditMenu.cut"), new ImageIcon(EditContextMenuWidget.class.getClassLoader().getResource("org/pushingpixels/lafwidget/text/edit-cut.png")));
        }

        @Override
        public void actionPerformed(ActionEvent e2) {
            ((JTextComponent)EditContextMenuWidget.this.jcomp).cut();
        }

        @Override
        public boolean isEnabled() {
            return ((JTextComponent)EditContextMenuWidget.this.jcomp).isEditable() && ((JTextComponent)EditContextMenuWidget.this.jcomp).isEnabled() && ((JTextComponent)EditContextMenuWidget.this.jcomp).getSelectedText() != null;
        }
    }

    private class DeleteAction
    extends AbstractAction {
        public DeleteAction() {
            super(LafWidgetUtilities.getResourceBundle(EditContextMenuWidget.this.jcomp).getString("EditMenu.delete"), new ImageIcon(EditContextMenuWidget.class.getClassLoader().getResource("org/pushingpixels/lafwidget/text/edit-delete.png")));
        }

        @Override
        public void actionPerformed(ActionEvent e2) {
            ((JTextComponent)EditContextMenuWidget.this.jcomp).replaceSelection(null);
        }

        @Override
        public boolean isEnabled() {
            return ((JTextComponent)EditContextMenuWidget.this.jcomp).isEditable() && ((JTextComponent)EditContextMenuWidget.this.jcomp).isEnabled() && ((JTextComponent)EditContextMenuWidget.this.jcomp).getSelectedText() != null;
        }
    }

    private class SelectAllAction
    extends AbstractAction {
        public SelectAllAction() {
            super(LafWidgetUtilities.getResourceBundle(EditContextMenuWidget.this.jcomp).getString("EditMenu.selectAll"), new ImageIcon(EditContextMenuWidget.class.getClassLoader().getResource("org/pushingpixels/lafwidget/text/edit-select-all.png")));
        }

        @Override
        public void actionPerformed(ActionEvent e2) {
            ((JTextComponent)EditContextMenuWidget.this.jcomp).selectAll();
        }

        @Override
        public boolean isEnabled() {
            return ((JTextComponent)EditContextMenuWidget.this.jcomp).isEnabled() && ((JTextComponent)EditContextMenuWidget.this.jcomp).getDocument().getLength() > 0;
        }
    }

    private class PasteAction
    extends AbstractAction {
        public PasteAction() {
            super(LafWidgetUtilities.getResourceBundle(EditContextMenuWidget.this.jcomp).getString("EditMenu.paste"), new ImageIcon(EditContextMenuWidget.class.getClassLoader().getResource("org/pushingpixels/lafwidget/text/edit-paste.png")));
        }

        @Override
        public void actionPerformed(ActionEvent e2) {
            ((JTextComponent)EditContextMenuWidget.this.jcomp).paste();
        }

        @Override
        public boolean isEnabled() {
            if (((JTextComponent)EditContextMenuWidget.this.jcomp).isEditable() && ((JTextComponent)EditContextMenuWidget.this.jcomp).isEnabled()) {
                Transferable contents = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(this);
                return contents.isDataFlavorSupported(DataFlavor.stringFlavor);
            }
            return false;
        }
    }
}

