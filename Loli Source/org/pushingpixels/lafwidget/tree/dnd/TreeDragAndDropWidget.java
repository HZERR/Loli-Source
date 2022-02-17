/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.tree.dnd;

import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetContext;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.EventListener;
import javax.swing.JTree;
import javax.swing.event.EventListenerList;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.pushingpixels.lafwidget.LafWidgetAdapter;
import org.pushingpixels.lafwidget.LafWidgetUtilities;
import org.pushingpixels.lafwidget.tree.dnd.AutoScrollingTreeDropTarget;
import org.pushingpixels.lafwidget.tree.dnd.DnDCellRendererProxy;
import org.pushingpixels.lafwidget.tree.dnd.DnDVetoException;
import org.pushingpixels.lafwidget.tree.dnd.StringTreeDnDEvent;
import org.pushingpixels.lafwidget.tree.dnd.StringTreeDnDListener;
import org.pushingpixels.lafwidget.tree.dnd.TransferableTreeNode;
import org.pushingpixels.lafwidget.tree.dnd.TreeTreeDnDEvent;
import org.pushingpixels.lafwidget.tree.dnd.TreeTreeDnDListener;

public class TreeDragAndDropWidget
extends LafWidgetAdapter<JTree> {
    private static Class[] EMPTY_CLASS_ARRAY = new Class[0];
    private static Method getTransferableMethod = null;
    private static Object[] EMPTY_OBJECT_ARRAY = new Object[0];
    protected DnDCellRendererProxy rendererProxy;
    protected DragSource dragSource;
    protected DropTarget dropTarget;
    protected MutableTreeNode dropNode;
    protected EventListenerList listeners;
    protected PropertyChangeListener propertyChangeListener;
    protected PropertyChangeListener cellRendererChangeListener;
    protected TreeDropTargetListener dropListener;
    protected TreeDragGestureListener gestureListener;
    protected DragGestureRecognizer dragGestureRecognizer;

    @Override
    public boolean requiresCustomLafSupport() {
        return false;
    }

    @Override
    public void installListeners() {
        this.listeners = new EventListenerList();
        this.propertyChangeListener = new PropertyChangeListener(){

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("lafwidgets.treeAutoDnDSupport".equals(evt.getPropertyName())) {
                    Object oldValue = evt.getOldValue();
                    Object newValue = evt.getNewValue();
                    boolean hadDnd = false;
                    if (oldValue instanceof Boolean) {
                        hadDnd = (Boolean)oldValue;
                    }
                    boolean hasDnd = false;
                    if (newValue instanceof Boolean) {
                        hasDnd = (Boolean)newValue;
                    }
                    if (!hadDnd && hasDnd && ((JTree)TreeDragAndDropWidget.this.jcomp).isEnabled()) {
                        TreeDragAndDropWidget.this.installDnDSupport();
                    }
                    if (hadDnd && !hasDnd) {
                        TreeDragAndDropWidget.this.uninstallDnDSupport();
                    }
                }
                if ("enabled".equals(evt.getPropertyName())) {
                    boolean wasEnabled = (Boolean)evt.getOldValue();
                    boolean isEnabled = (Boolean)evt.getNewValue();
                    if (!wasEnabled && isEnabled && LafWidgetUtilities.hasAutomaticDnDSupport((JTree)TreeDragAndDropWidget.this.jcomp)) {
                        TreeDragAndDropWidget.this.installDnDSupport();
                    }
                    if (wasEnabled && !isEnabled) {
                        TreeDragAndDropWidget.this.uninstallDnDSupport();
                    }
                }
            }
        };
        ((JTree)this.jcomp).addPropertyChangeListener(this.propertyChangeListener);
        if (((JTree)this.jcomp).isEnabled() && LafWidgetUtilities.hasAutomaticDnDSupport((JTree)this.jcomp)) {
            this.installDnDSupport();
        }
    }

    private void installDnDSupport() {
        if (this.cellRendererChangeListener != null) {
            return;
        }
        this.cellRendererChangeListener = new PropertyChangeListener(){

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                String name = evt.getPropertyName();
                if (name.equals("cellRenderer")) {
                    TreeCellRenderer renderer = ((JTree)TreeDragAndDropWidget.this.jcomp).getCellRenderer();
                    if (!(renderer instanceof DnDCellRendererProxy)) {
                        TreeDragAndDropWidget.this.rendererProxy = new DnDCellRendererProxy(renderer);
                        ((JTree)TreeDragAndDropWidget.this.jcomp).setCellRenderer(TreeDragAndDropWidget.this.rendererProxy);
                        ((JTree)TreeDragAndDropWidget.this.jcomp).repaint();
                    } else {
                        TreeDragAndDropWidget.this.rendererProxy = (DnDCellRendererProxy)renderer;
                    }
                }
            }
        };
        ((JTree)this.jcomp).addPropertyChangeListener(this.cellRendererChangeListener);
        if (((JTree)this.jcomp).getCellRenderer() != null) {
            ((JTree)this.jcomp).setCellRenderer(new DnDCellRendererProxy(((JTree)this.jcomp).getCellRenderer()));
        }
        this.dragSource = new DragSource();
        this.gestureListener = new TreeDragGestureListener();
        this.dragGestureRecognizer = this.dragSource.createDefaultDragGestureRecognizer(this.jcomp, 3, this.gestureListener);
        this.dropListener = new TreeDropTargetListener();
        this.dropTarget = new AutoScrollingTreeDropTarget((JTree)this.jcomp, (DropTargetListener)this.dropListener);
    }

    @Override
    public void uninstallListeners() {
        ((JTree)this.jcomp).removePropertyChangeListener(this.propertyChangeListener);
        this.propertyChangeListener = null;
        this.uninstallDnDSupport();
    }

    private void uninstallDnDSupport() {
        TreeCellRenderer tcl;
        if (this.cellRendererChangeListener != null) {
            ((JTree)this.jcomp).removePropertyChangeListener(this.cellRendererChangeListener);
            this.cellRendererChangeListener = null;
        }
        if ((tcl = ((JTree)this.jcomp).getCellRenderer()) instanceof DnDCellRendererProxy) {
            TreeCellRenderer origRenderer = ((DnDCellRendererProxy)tcl).getOriginalTreeCellRenderer();
            ((JTree)this.jcomp).setCellRenderer(origRenderer);
        }
        if (this.dropListener != null) {
            this.dropTarget.removeDropTargetListener(this.dropListener);
            this.dropListener = null;
            ((JTree)this.jcomp).setDropTarget(null);
        }
        if (this.dragGestureRecognizer != null) {
            this.dragGestureRecognizer.removeDragGestureListener(this.gestureListener);
            this.gestureListener = null;
            this.dragGestureRecognizer = null;
        }
    }

    private boolean mayDropHere(String aSourceString, JTree aTargetTree, TreePath aPath) {
        return this.mayDropHere(aSourceString, aTargetTree, (TreeNode)aPath.getLastPathComponent());
    }

    private boolean mayDropHere(String aSourceString, JTree aTargetTree, TreeNode aNode) {
        EventListener[] listeners = this.listeners.getListeners(StringTreeDnDListener.class);
        if (listeners != null && listeners.length > 0) {
            try {
                StringTreeDnDEvent event = new StringTreeDnDEvent(aSourceString, aTargetTree, aNode);
                for (int i2 = 0; i2 < listeners.length; ++i2) {
                    ((StringTreeDnDListener)listeners[i2]).mayDrop(event);
                }
            }
            catch (DnDVetoException exception) {
                return false;
            }
        }
        return true;
    }

    private boolean mayDropHere(JTree aSourceTree, MutableTreeNode aSourceNode, TreePath aPath) {
        if (aPath == null) {
            return false;
        }
        return this.mayDropHere(aSourceTree, aSourceNode, (TreeNode)aPath.getLastPathComponent());
    }

    private boolean mayDropHere(JTree aSourceTree, MutableTreeNode aSourceNode, TreeNode aNode) {
        EventListener[] listeners;
        boolean mayDropHere;
        boolean bl = mayDropHere = !(aNode == aSourceNode || !(aNode instanceof MutableTreeNode) || aNode.getParent() != null && !(aNode.getParent() instanceof MutableTreeNode) || !(((JTree)this.jcomp).getModel() instanceof DefaultTreeModel) || this.jcomp == aSourceTree && TreeDragAndDropWidget.isAncestorOf(aSourceNode, aNode));
        if (mayDropHere && (listeners = this.listeners.getListeners(TreeTreeDnDListener.class)) != null && listeners.length > 0) {
            try {
                TreeTreeDnDEvent event = new TreeTreeDnDEvent(aSourceTree, aSourceNode, (JTree)this.jcomp, aNode);
                for (int i2 = 0; i2 < listeners.length; ++i2) {
                    ((TreeTreeDnDListener)listeners[i2]).mayDrop(event);
                }
            }
            catch (DnDVetoException exception) {
                mayDropHere = false;
            }
        }
        return mayDropHere;
    }

    private static boolean isAncestorOf(TreeNode aPossibleParent, TreeNode aNode) {
        if (aPossibleParent == null || aNode.getParent() == null) {
            return false;
        }
        if (aNode.getParent() == aPossibleParent) {
            return true;
        }
        return TreeDragAndDropWidget.isAncestorOf(aPossibleParent, aNode.getParent());
    }

    private void resetDragAndDrop() {
        this.dropNode = null;
        this.rendererProxy.setDraggedNode(null);
        this.rendererProxy.setDropAllowed(false);
        this.rendererProxy.setDropNode(null);
        ((JTree)this.jcomp).repaint();
    }

    class TreeDropTargetListener
    implements DropTargetListener {
        private TreeNode lastDragOverNode = null;

        TreeDropTargetListener() {
        }

        @Override
        public void drop(DropTargetDropEvent dtde) {
            TreePath dropPath = ((JTree)TreeDragAndDropWidget.this.jcomp).getClosestPathForLocation(dtde.getLocation().x, dtde.getLocation().y);
            if (!((JTree)TreeDragAndDropWidget.this.jcomp).isEnabled() || dropPath == null) {
                dtde.rejectDrop();
                dtde.dropComplete(false);
                TreeDragAndDropWidget.this.resetDragAndDrop();
                return;
            }
            TreeDragAndDropWidget.this.dropNode = (MutableTreeNode)dropPath.getLastPathComponent();
            try {
                dtde.acceptDrop(3);
                TransferableTreeNode ttn = (TransferableTreeNode)dtde.getTransferable().getTransferData(TransferableTreeNode.getJavaJVMLocalObjectFlavor());
                JTree sourceTree = ttn.getSourceTree();
                MutableTreeNode sourceNode = ttn.getSourceNode();
                if (TreeDragAndDropWidget.this.mayDropHere(sourceTree, sourceNode, TreeDragAndDropWidget.this.dropNode)) {
                    dtde.dropComplete(this.dropNodes(ttn.getSourceTree(), ttn.getSourceNode(), (JTree)TreeDragAndDropWidget.this.jcomp, TreeDragAndDropWidget.this.dropNode, (dtde.getDropAction() & 2) != 0));
                    if (ttn.isNodeWasExpanded()) {
                        DefaultTreeModel targetModel = (DefaultTreeModel)((JTree)TreeDragAndDropWidget.this.jcomp).getModel();
                        ((JTree)TreeDragAndDropWidget.this.jcomp).expandPath(new TreePath(targetModel.getPathToRoot(ttn.getSourceNode())));
                    }
                } else {
                    try {
                        dtde.rejectDrop();
                    }
                    catch (Exception e2) {
                        // empty catch block
                    }
                    dtde.dropComplete(false);
                    TreeDragAndDropWidget.this.resetDragAndDrop();
                    return;
                }
                TreeDragAndDropWidget.this.resetDragAndDrop();
            }
            catch (UnsupportedFlavorException ufe) {
                try {
                    String droppedString = (String)dtde.getTransferable().getTransferData(DataFlavor.stringFlavor);
                    if (!TreeDragAndDropWidget.this.mayDropHere(droppedString, (JTree)TreeDragAndDropWidget.this.jcomp, dropPath)) {
                        dtde.rejectDrop();
                        dtde.dropComplete(false);
                        TreeDragAndDropWidget.this.resetDragAndDrop();
                        return;
                    }
                    dtde.dropComplete(this.dropString(droppedString));
                    TreeDragAndDropWidget.this.resetDragAndDrop();
                }
                catch (Exception exception) {
                    dtde.rejectDrop();
                    dtde.dropComplete(false);
                    TreeDragAndDropWidget.this.resetDragAndDrop();
                    return;
                }
            }
            catch (Exception e3) {
                e3.printStackTrace();
                dtde.rejectDrop();
                dtde.dropComplete(true);
                TreeDragAndDropWidget.this.resetDragAndDrop();
                return;
            }
        }

        private boolean dropString(String droppedString) {
            DefaultTreeModel model = (DefaultTreeModel)((JTree)TreeDragAndDropWidget.this.jcomp).getModel();
            boolean doItOurselves = true;
            EventListener[] listeners = TreeDragAndDropWidget.this.listeners.getListeners(StringTreeDnDListener.class);
            if (listeners != null && listeners.length > 0) {
                try {
                    StringTreeDnDEvent event = new StringTreeDnDEvent(droppedString, (JTree)TreeDragAndDropWidget.this.jcomp, TreeDragAndDropWidget.this.dropNode);
                    for (int i2 = 0; i2 < listeners.length; ++i2) {
                        ((StringTreeDnDListener)listeners[i2]).drop(event);
                    }
                }
                catch (DnDVetoException exception) {
                    doItOurselves = true;
                }
            }
            if (doItOurselves) {
                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(droppedString);
                MutableTreeNode parent = (MutableTreeNode)TreeDragAndDropWidget.this.dropNode.getParent();
                if (TreeDragAndDropWidget.this.dropNode.isLeaf()) {
                    int index = parent.getIndex(TreeDragAndDropWidget.this.dropNode);
                    model.insertNodeInto(newNode, parent, index + 1);
                } else {
                    model.insertNodeInto(newNode, TreeDragAndDropWidget.this.dropNode, 0);
                }
            }
            return true;
        }

        private boolean dropNodes(JTree aSourceTree, MutableTreeNode aSourceNode, JTree aTargetTree, MutableTreeNode aDropNode, boolean move) {
            DefaultTreeModel sourceModel = (DefaultTreeModel)aSourceTree.getModel();
            DefaultTreeModel targetModel = (DefaultTreeModel)aTargetTree.getModel();
            boolean doItOurselves = true;
            EventListener[] listeners = TreeDragAndDropWidget.this.listeners.getListeners(TreeTreeDnDListener.class);
            if (listeners != null && listeners.length > 0) {
                try {
                    TreeTreeDnDEvent event = new TreeTreeDnDEvent(aSourceTree, aSourceNode, aTargetTree, aDropNode);
                    for (int i2 = 0; i2 < listeners.length; ++i2) {
                        ((TreeTreeDnDListener)listeners[i2]).drop(event);
                    }
                }
                catch (DnDVetoException exception) {
                    doItOurselves = false;
                }
            }
            if (doItOurselves) {
                MutableTreeNode sourceNodeCopy = aSourceNode;
                if (move) {
                    sourceModel.removeNodeFromParent(aSourceNode);
                } else {
                    sourceNodeCopy = this.recursivelyCopyNodes(targetModel, aSourceNode);
                }
                MutableTreeNode parent = (MutableTreeNode)aDropNode.getParent();
                if (aDropNode.isLeaf() && parent != null) {
                    int index = parent.getIndex(aDropNode);
                    targetModel.insertNodeInto(sourceNodeCopy, parent, index + 1);
                } else {
                    targetModel.insertNodeInto(sourceNodeCopy, aDropNode, 0);
                }
            }
            return true;
        }

        private DefaultMutableTreeNode recursivelyCopyNodes(DefaultTreeModel aModel, TreeNode aNode) {
            DefaultMutableTreeNode copy = new DefaultMutableTreeNode(aNode.toString());
            copy.setAllowsChildren(aNode.getAllowsChildren());
            if (aNode.getChildCount() != 0) {
                Enumeration children = aNode.children();
                while (children.hasMoreElements()) {
                    TreeNode child = (TreeNode)children.nextElement();
                    DefaultMutableTreeNode childCopy = this.recursivelyCopyNodes(aModel, child);
                    aModel.insertNodeInto(childCopy, copy, copy.getChildCount());
                    childCopy.setParent(copy);
                }
            }
            return copy;
        }

        @Override
        public void dragExit(DropTargetEvent dte) {
            TreeDragAndDropWidget.this.dropNode = null;
            TreeDragAndDropWidget.this.rendererProxy.setDropNode(null);
            ((JTree)TreeDragAndDropWidget.this.jcomp).repaint();
        }

        @Override
        public void dropActionChanged(DropTargetDragEvent dtde) {
        }

        private Transferable getTransferable(DropTargetDragEvent dtde) {
            try {
                DropTargetContext context = dtde.getDropTargetContext();
                if (getTransferableMethod == null) {
                    getTransferableMethod = context.getClass().getDeclaredMethod("getTransferable", EMPTY_CLASS_ARRAY);
                    getTransferableMethod.setAccessible(true);
                }
                return (Transferable)getTransferableMethod.invoke(context, EMPTY_OBJECT_ARRAY);
            }
            catch (Exception e2) {
                e2.printStackTrace(System.err);
                return null;
            }
        }

        @Override
        public void dragOver(DropTargetDragEvent dtde) {
            if (!((JTree)TreeDragAndDropWidget.this.jcomp).isEnabled()) {
                dtde.rejectDrag();
                return;
            }
            TreePath dropPath = ((JTree)TreeDragAndDropWidget.this.jcomp).getClosestPathForLocation(dtde.getLocation().x, dtde.getLocation().y);
            TreeNode currentDropNode = (TreeNode)dropPath.getLastPathComponent();
            if (dropPath == null || currentDropNode == null || currentDropNode.equals(this.lastDragOverNode)) {
                return;
            }
            this.lastDragOverNode = currentDropNode;
            Transferable transferable = this.getTransferable(dtde);
            boolean mayDropHere = false;
            try {
                TransferableTreeNode transferredNode = (TransferableTreeNode)transferable.getTransferData(TransferableTreeNode.getJavaJVMLocalObjectFlavor());
                JTree sourceTree = transferredNode.getSourceTree();
                MutableTreeNode sourceNode = transferredNode.getSourceNode();
                if (TreeDragAndDropWidget.this.mayDropHere(sourceTree, sourceNode, dropPath)) {
                    TreeDragAndDropWidget.this.dropNode = (MutableTreeNode)dropPath.getLastPathComponent();
                    if (!((JTree)TreeDragAndDropWidget.this.jcomp).isExpanded(dropPath)) {
                        ((JTree)TreeDragAndDropWidget.this.jcomp).expandPath(dropPath);
                    }
                    mayDropHere = true;
                } else {
                    TreeDragAndDropWidget.this.dropNode = null;
                }
            }
            catch (UnsupportedFlavorException ufe) {
                try {
                    String sourceText = (String)transferable.getTransferData(DataFlavor.stringFlavor);
                    if (TreeDragAndDropWidget.this.mayDropHere(sourceText, (JTree)TreeDragAndDropWidget.this.jcomp, dropPath)) {
                        TreeDragAndDropWidget.this.dropNode = (MutableTreeNode)dropPath.getLastPathComponent();
                        if (!((JTree)TreeDragAndDropWidget.this.jcomp).isExpanded(dropPath)) {
                            ((JTree)TreeDragAndDropWidget.this.jcomp).expandPath(dropPath);
                        }
                        mayDropHere = true;
                    }
                    TreeDragAndDropWidget.this.dropNode = null;
                }
                catch (Exception e2) {
                    TreeDragAndDropWidget.this.dropNode = null;
                }
            }
            catch (Exception e3) {
                TreeDragAndDropWidget.this.dropNode = null;
            }
            TreeDragAndDropWidget.this.rendererProxy.setDropAllowed(mayDropHere);
            TreeDragAndDropWidget.this.rendererProxy.setDropNode((TreeNode)dropPath.getLastPathComponent());
            ((JTree)TreeDragAndDropWidget.this.jcomp).repaint();
            if (!mayDropHere) {
                dtde.rejectDrag();
            } else {
                dtde.acceptDrag(dtde.getDropAction());
            }
            ((JTree)TreeDragAndDropWidget.this.jcomp).repaint();
        }

        @Override
        public void dragEnter(DropTargetDragEvent dtde) {
            this.dragOver(dtde);
        }
    }

    class TreeDragSourceListener
    implements DragSourceListener {
        TreeDragSourceListener() {
        }

        @Override
        public void dragExit(DragSourceEvent dse) {
        }

        @Override
        public void dropActionChanged(DragSourceDragEvent dsde) {
        }

        @Override
        public void dragOver(DragSourceDragEvent dsde) {
        }

        @Override
        public void dragEnter(DragSourceDragEvent dsde) {
        }

        @Override
        public void dragDropEnd(DragSourceDropEvent dsde) {
            TreeDragAndDropWidget.this.resetDragAndDrop();
        }
    }

    class TreeDragGestureListener
    implements DragGestureListener {
        TreeDragGestureListener() {
        }

        @Override
        public void dragGestureRecognized(DragGestureEvent dge) {
            if (!((JTree)TreeDragAndDropWidget.this.jcomp).isEnabled()) {
                return;
            }
            TreePath draggedPath = ((JTree)TreeDragAndDropWidget.this.jcomp).getClosestPathForLocation(dge.getDragOrigin().x, dge.getDragOrigin().y);
            if (draggedPath == null) {
                return;
            }
            TreeNode node = (TreeNode)draggedPath.getLastPathComponent();
            if (!(node instanceof MutableTreeNode) || node.getParent() == null || !(node.getParent() instanceof MutableTreeNode)) {
                return;
            }
            TransferableTreeNode transferableNode = new TransferableTreeNode((JTree)TreeDragAndDropWidget.this.jcomp, (MutableTreeNode)node, ((JTree)TreeDragAndDropWidget.this.jcomp).isExpanded(draggedPath));
            TreeDragAndDropWidget.this.rendererProxy.setDraggedNode(node);
            BufferedImage image = null;
            Point imageOffset = null;
            TreeCellRenderer renderer = TreeDragAndDropWidget.this.rendererProxy.getOriginalTreeCellRenderer();
            Rectangle dragBounds = ((JTree)TreeDragAndDropWidget.this.jcomp).getPathBounds(draggedPath);
            imageOffset = new Point(dge.getDragOrigin().x - dragBounds.x, dge.getDragOrigin().y - dragBounds.y);
            Component component = renderer.getTreeCellRendererComponent((JTree)TreeDragAndDropWidget.this.jcomp, node, false, ((JTree)TreeDragAndDropWidget.this.jcomp).isExpanded(draggedPath), node.isLeaf(), 0, false);
            component.setSize(dragBounds.width, dragBounds.height);
            image = new BufferedImage(dragBounds.width, dragBounds.height, 2);
            Graphics2D g2d = image.createGraphics();
            g2d.setComposite(AlphaComposite.getInstance(2, 0.75f));
            component.paint(g2d);
            g2d.dispose();
            if (DragSource.isDragImageSupported()) {
                TreeDragAndDropWidget.this.dragSource.startDrag(dge, null, image, imageOffset, transferableNode, new TreeDragSourceListener());
            } else {
                TreeDragAndDropWidget.this.dragSource.startDrag(dge, null, transferableNode, new TreeDragSourceListener());
            }
        }
    }
}

