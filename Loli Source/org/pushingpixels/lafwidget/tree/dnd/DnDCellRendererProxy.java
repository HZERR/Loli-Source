/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.tree.dnd;

import java.awt.Component;
import java.awt.Image;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeNode;
import org.pushingpixels.lafwidget.tree.dnd.DnDBorderFactory;

class DnDCellRendererProxy
extends Component
implements TreeCellRenderer {
    private TreeCellRenderer originalTreeCellRenderer;
    private DnDBorderFactory borderFactory;
    private TreeNode draggedNode;
    private TreeNode dropNode;
    private int dropNodeRow;
    private Image shadowImage;
    private boolean fetchBorder;
    private Border originalBorder;
    private boolean dropAllowed;

    public DnDCellRendererProxy(TreeCellRenderer trueCellRenderer) {
        this.originalTreeCellRenderer = trueCellRenderer;
        this.borderFactory = new DnDBorderFactory();
        this.fetchBorder = true;
    }

    public TreeCellRenderer getOriginalTreeCellRenderer() {
        return this.originalTreeCellRenderer;
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        Component c2 = this.originalTreeCellRenderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        TreeNode nodeToRender = (TreeNode)value;
        if (c2 instanceof JComponent) {
            if (this.fetchBorder) {
                this.fetchBorder = false;
                this.originalBorder = ((JComponent)c2).getBorder();
            }
            JComponent jComponent = (JComponent)c2;
            if (nodeToRender.equals(this.dropNode)) {
                Border border = null;
                if (this.isDropAllowed()) {
                    border = this.borderFactory.getDropAllowedBorder();
                    this.dropNodeRow = row;
                } else {
                    border = this.borderFactory.getDropNotAllowedBorder();
                    this.dropNodeRow = -2;
                }
                jComponent.setBorder(border);
            } else if (this.isDropAllowed() && row == this.dropNodeRow + 1) {
                jComponent.setBorder(this.borderFactory.getOffsetBorder());
            } else {
                jComponent.setBorder(this.originalBorder);
                this.dropNodeRow = -2;
            }
        }
        return c2;
    }

    public TreeNode getDraggedNode() {
        return this.draggedNode;
    }

    public void setDraggedNode(TreeNode draggedNode) {
        this.draggedNode = draggedNode;
    }

    public TreeNode getDropNode() {
        return this.dropNode;
    }

    public void setDropNode(TreeNode dropNode) {
        this.dropNode = dropNode;
        if (dropNode == null) {
            this.dropNodeRow = -2;
        }
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[DnDCellRendererProxy for : ").append(this.originalTreeCellRenderer).append("]");
        return sb.toString();
    }

    public boolean isDropAllowed() {
        return this.dropAllowed;
    }

    public void setDropAllowed(boolean dropAllowed) {
        this.dropAllowed = dropAllowed;
        if (!dropAllowed) {
            this.dropNodeRow = -2;
        }
    }

    public void setShadowImage(Image anImage) {
        this.shadowImage = anImage;
    }
}

