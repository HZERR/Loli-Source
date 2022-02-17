/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.tree.dnd;

import java.util.EventObject;
import javax.swing.JTree;
import javax.swing.tree.TreeNode;

public class TreeTreeDnDEvent
extends EventObject {
    private JTree sourceTree;
    private JTree targetTree;
    private TreeNode sourceNode;
    private TreeNode targetNode;

    public TreeTreeDnDEvent(JTree aSourceTree, TreeNode aSourceNode, JTree aTargetTree, TreeNode aTargetNode) {
        super(aSourceTree);
        this.setSourceTree(aSourceTree);
        this.setSourceNode(aSourceNode);
        this.setTargetTree(aTargetTree);
        this.setTargetNode(aTargetNode);
    }

    public JTree getSourceTree() {
        return this.sourceTree;
    }

    public void setSourceTree(JTree sourceTree) {
        this.sourceTree = sourceTree;
    }

    public JTree getTargetTree() {
        return this.targetTree;
    }

    public void setTargetTree(JTree targetTree) {
        this.targetTree = targetTree;
    }

    public TreeNode getSourceNode() {
        return this.sourceNode;
    }

    public void setSourceNode(TreeNode sourceNode) {
        this.sourceNode = sourceNode;
    }

    public TreeNode getTargetNode() {
        return this.targetNode;
    }

    public void setTargetNode(TreeNode targetNode) {
        this.targetNode = targetNode;
    }
}

