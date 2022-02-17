/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.tree.dnd;

import java.util.EventObject;
import javax.swing.JTree;
import javax.swing.tree.TreeNode;

public class StringTreeDnDEvent
extends EventObject {
    private JTree targetTree;
    private TreeNode targetNode;
    private String sourceString;

    public StringTreeDnDEvent(String aSourceString, JTree aTargetTree, TreeNode aTargetNode) {
        super(aSourceString);
        this.setSourceString(aSourceString);
        this.setTargetTree(aTargetTree);
        this.setTargetNode(aTargetNode);
    }

    public JTree getTargetTree() {
        return this.targetTree;
    }

    public void setTargetTree(JTree targetTree) {
        this.targetTree = targetTree;
    }

    public TreeNode getTargetNode() {
        return this.targetNode;
    }

    public void setTargetNode(TreeNode targetNode) {
        this.targetNode = targetNode;
    }

    public String getSourceString() {
        return this.sourceString;
    }

    public void setSourceString(String sourceString) {
        this.sourceString = sourceString;
    }
}

