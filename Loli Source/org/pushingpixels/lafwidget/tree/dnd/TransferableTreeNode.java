/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.tree.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.JTree;
import javax.swing.tree.MutableTreeNode;

class TransferableTreeNode
implements Transferable {
    private static DataFlavor javaJVMLocalObjectFlavor;
    private static DataFlavor[] supportedDataFlavors;
    private JTree sourceTree;
    private MutableTreeNode sourceNode;
    private boolean nodeWasExpanded;

    public static DataFlavor getJavaJVMLocalObjectFlavor() {
        if (javaJVMLocalObjectFlavor == null) {
            try {
                javaJVMLocalObjectFlavor = new DataFlavor("application/x-java-jvm-local-objectref");
            }
            catch (ClassNotFoundException cnfe) {
                System.err.println("Cannot create JVM Local Object Flavor " + cnfe.getMessage());
            }
        }
        return javaJVMLocalObjectFlavor;
    }

    private static DataFlavor[] getSupportedDataFlavors() {
        if (supportedDataFlavors == null) {
            DataFlavor[] arrdataFlavor;
            DataFlavor localJVMFlavor = TransferableTreeNode.getJavaJVMLocalObjectFlavor();
            if (localJVMFlavor == null) {
                DataFlavor[] arrdataFlavor2 = new DataFlavor[1];
                arrdataFlavor = arrdataFlavor2;
                arrdataFlavor2[0] = DataFlavor.stringFlavor;
            } else {
                DataFlavor[] arrdataFlavor3 = new DataFlavor[2];
                arrdataFlavor3[0] = localJVMFlavor;
                arrdataFlavor = arrdataFlavor3;
                arrdataFlavor3[1] = DataFlavor.stringFlavor;
            }
            supportedDataFlavors = arrdataFlavor;
        }
        return supportedDataFlavors;
    }

    public TransferableTreeNode(JTree aTree, MutableTreeNode aNode, boolean wasExpanded) {
        this.setSourceTree(aTree);
        this.setSourceNode(aNode);
        this.setNodeWasExpanded(wasExpanded);
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        DataFlavor[] flavors = TransferableTreeNode.getSupportedDataFlavors();
        for (int i2 = 0; i2 < flavors.length; ++i2) {
            if (!flavor.equals(flavors[i2])) continue;
            return true;
        }
        return false;
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (flavor.equals(javaJVMLocalObjectFlavor)) {
            return this;
        }
        if (flavor.equals(DataFlavor.stringFlavor)) {
            return this.getSourceNode().toString();
        }
        throw new UnsupportedFlavorException(flavor);
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return TransferableTreeNode.getSupportedDataFlavors();
    }

    public JTree getSourceTree() {
        return this.sourceTree;
    }

    public void setSourceTree(JTree sourceTree) {
        this.sourceTree = sourceTree;
    }

    public MutableTreeNode getSourceNode() {
        return this.sourceNode;
    }

    public void setSourceNode(MutableTreeNode sourceNode) {
        this.sourceNode = sourceNode;
    }

    public boolean isNodeWasExpanded() {
        return this.nodeWasExpanded;
    }

    public void setNodeWasExpanded(boolean nodeWasExpanded) {
        this.nodeWasExpanded = nodeWasExpanded;
    }
}

