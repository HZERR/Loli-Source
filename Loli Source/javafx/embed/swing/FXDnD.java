/*
 * Decompiled with CFR 0.150.
 */
package javafx.embed.swing;

import com.sun.javafx.tk.Toolkit;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.SecondaryLoop;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetContext;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.InvalidDnDOperationException;
import java.awt.dnd.MouseDragGestureRecognizer;
import java.awt.dnd.peer.DragSourceContextPeer;
import java.awt.dnd.peer.DropTargetContextPeer;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Platform;
import javafx.embed.swing.CachingTransferable;
import javafx.embed.swing.SwingDnD;
import javafx.embed.swing.SwingEvents;
import javafx.embed.swing.SwingFXUtils;
import javafx.embed.swing.SwingNode;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import sun.awt.AWTAccessor;
import sun.awt.dnd.SunDragSourceContextPeer;
import sun.swing.JLightweightFrame;

final class FXDnD {
    private final SwingNode node;
    private boolean isDragSourceListenerInstalled = false;
    private javafx.scene.input.MouseEvent pressEvent = null;
    private long pressTime = 0L;
    private volatile SecondaryLoop loop;
    private final Map<Component, FXDragGestureRecognizer> recognizers = new HashMap<Component, FXDragGestureRecognizer>();
    private final EventHandler<javafx.scene.input.MouseEvent> onMousePressHandler = mouseEvent -> {
        this.pressEvent = mouseEvent;
        this.pressTime = System.currentTimeMillis();
    };
    private volatile FXDragSourceContextPeer activeDSContextPeer;
    private final EventHandler<javafx.scene.input.MouseEvent> onDragStartHandler = mouseEvent -> {
        this.activeDSContextPeer = null;
        javafx.scene.input.MouseEvent mouseEvent2 = this.getInitialGestureEvent();
        SwingFXUtils.runOnEDTAndWait(this, () -> this.fireEvent((int)mouseEvent2.getX(), (int)mouseEvent2.getY(), this.pressTime, SwingEvents.fxMouseModsToMouseMods(mouseEvent2)));
        if (this.activeDSContextPeer == null) {
            return;
        }
        mouseEvent.consume();
        Dragboard dragboard = this.getNode().startDragAndDrop(SwingDnD.dropActionsToTransferModes(this.activeDSContextPeer.sourceActions).toArray((T[])new TransferMode[1]));
        HashMap<DataFormat, Object> hashMap = new HashMap<DataFormat, Object>();
        for (String string : this.activeDSContextPeer.transferable.getMimeTypes()) {
            DataFormat dataFormat = DataFormat.lookupMimeType(string);
            if (dataFormat == null) continue;
            hashMap.put(dataFormat, this.activeDSContextPeer.transferable.getData(string));
        }
        boolean bl = dragboard.setContent(hashMap);
        if (!bl) {
            this.loop.exit();
        }
    };
    private final EventHandler<DragEvent> onDragDoneHandler = dragEvent -> {
        dragEvent.consume();
        this.loop.exit();
        if (this.activeDSContextPeer != null) {
            TransferMode transferMode = dragEvent.getTransferMode();
            this.activeDSContextPeer.dragDone(transferMode == null ? 0 : SwingDnD.transferModeToDropAction(transferMode), (int)dragEvent.getX(), (int)dragEvent.getY());
        }
    };
    private boolean isDropTargetListenerInstalled = false;
    private volatile FXDropTargetContextPeer activeDTContextPeer = null;
    private final Map<Component, DropTarget> dropTargets = new HashMap<Component, DropTarget>();
    private final EventHandler<DragEvent> onDragEnteredHandler = dragEvent -> {
        int n2;
        if (this.activeDTContextPeer == null) {
            this.activeDTContextPeer = new FXDropTargetContextPeer();
        }
        if ((n2 = this.activeDTContextPeer.postDropTargetEvent(dragEvent)) != 0) {
            dragEvent.consume();
        }
    };
    private final EventHandler<DragEvent> onDragExitedHandler = dragEvent -> {
        if (this.activeDTContextPeer == null) {
            this.activeDTContextPeer = new FXDropTargetContextPeer();
        }
        this.activeDTContextPeer.postDropTargetEvent(dragEvent);
        this.activeDTContextPeer = null;
    };
    private final EventHandler<DragEvent> onDragOverHandler = dragEvent -> {
        int n2;
        if (this.activeDTContextPeer == null) {
            this.activeDTContextPeer = new FXDropTargetContextPeer();
        }
        if ((n2 = this.activeDTContextPeer.postDropTargetEvent(dragEvent)) != 0) {
            dragEvent.acceptTransferModes(SwingDnD.dropActionsToTransferModes(n2).toArray((T[])new TransferMode[1]));
            dragEvent.consume();
        }
    };
    private final EventHandler<DragEvent> onDragDroppedHandler = dragEvent -> {
        int n2;
        if (this.activeDTContextPeer == null) {
            this.activeDTContextPeer = new FXDropTargetContextPeer();
        }
        if ((n2 = this.activeDTContextPeer.postDropTargetEvent(dragEvent)) != 0) {
            dragEvent.setDropCompleted(this.activeDTContextPeer.success);
            dragEvent.consume();
        }
        this.activeDTContextPeer = null;
    };

    private SwingNode getNode() {
        return this.node;
    }

    FXDnD(SwingNode swingNode) {
        this.node = swingNode;
    }

    public <T> ComponentMapper<T> mapComponent(Map<Component, T> map, int n2, int n3) {
        return new ComponentMapper(map, n2, n3);
    }

    private void fireEvent(int n2, int n3, long l2, int n4) {
        ComponentMapper<FXDragGestureRecognizer> componentMapper = this.mapComponent(this.recognizers, n2, n3);
        FXDragGestureRecognizer fXDragGestureRecognizer = (FXDragGestureRecognizer)((ComponentMapper)componentMapper).object;
        if (fXDragGestureRecognizer != null) {
            fXDragGestureRecognizer.fireEvent(((ComponentMapper)componentMapper).x, ((ComponentMapper)componentMapper).y, l2, n4);
        } else {
            SwingFXUtils.leaveFXNestedLoop(this);
        }
    }

    private javafx.scene.input.MouseEvent getInitialGestureEvent() {
        return this.pressEvent;
    }

    public <T extends DragGestureRecognizer> T createDragGestureRecognizer(Class<T> class_, DragSource dragSource, Component component, int n2, DragGestureListener dragGestureListener) {
        return (T)new FXDragGestureRecognizer(dragSource, component, n2, dragGestureListener);
    }

    public DragSourceContextPeer createDragSourceContextPeer(DragGestureEvent dragGestureEvent) throws InvalidDnDOperationException {
        return new FXDragSourceContextPeer(dragGestureEvent);
    }

    public void addDropTarget(DropTarget dropTarget) {
        this.dropTargets.put(dropTarget.getComponent(), dropTarget);
        Platform.runLater(() -> {
            if (!this.isDropTargetListenerInstalled) {
                this.node.addEventHandler(DragEvent.DRAG_ENTERED, this.onDragEnteredHandler);
                this.node.addEventHandler(DragEvent.DRAG_EXITED, this.onDragExitedHandler);
                this.node.addEventHandler(DragEvent.DRAG_OVER, this.onDragOverHandler);
                this.node.addEventHandler(DragEvent.DRAG_DROPPED, this.onDragDroppedHandler);
                this.isDropTargetListenerInstalled = true;
            }
        });
    }

    public void removeDropTarget(DropTarget dropTarget) {
        this.dropTargets.remove(dropTarget.getComponent());
        Platform.runLater(() -> {
            if (this.isDropTargetListenerInstalled && this.dropTargets.isEmpty()) {
                this.node.removeEventHandler(DragEvent.DRAG_ENTERED, this.onDragEnteredHandler);
                this.node.removeEventHandler(DragEvent.DRAG_EXITED, this.onDragExitedHandler);
                this.node.removeEventHandler(DragEvent.DRAG_OVER, this.onDragOverHandler);
                this.node.removeEventHandler(DragEvent.DRAG_DROPPED, this.onDragDroppedHandler);
                this.isDropTargetListenerInstalled = true;
            }
        });
    }

    private final class FXDropTargetContextPeer
    implements DropTargetContextPeer {
        private int targetActions = 0;
        private int currentAction = 0;
        private DropTarget dt = null;
        private DropTargetContext ctx = null;
        private final CachingTransferable transferable = new CachingTransferable();
        private boolean success = false;
        private int dropAction = 0;

        private FXDropTargetContextPeer() {
        }

        @Override
        public synchronized void setTargetActions(int n2) {
            this.targetActions = n2;
        }

        @Override
        public synchronized int getTargetActions() {
            return this.targetActions;
        }

        @Override
        public synchronized DropTarget getDropTarget() {
            return this.dt;
        }

        @Override
        public synchronized boolean isTransferableJVMLocal() {
            return false;
        }

        @Override
        public synchronized DataFlavor[] getTransferDataFlavors() {
            return this.transferable.getTransferDataFlavors();
        }

        @Override
        public synchronized Transferable getTransferable() {
            return this.transferable;
        }

        @Override
        public synchronized void acceptDrag(int n2) {
            this.currentAction = n2;
        }

        @Override
        public synchronized void rejectDrag() {
            this.currentAction = 0;
        }

        @Override
        public synchronized void acceptDrop(int n2) {
            this.dropAction = n2;
        }

        @Override
        public synchronized void rejectDrop() {
            this.dropAction = 0;
        }

        @Override
        public synchronized void dropComplete(boolean bl) {
            this.success = bl;
        }

        private int postDropTargetEvent(DragEvent dragEvent) {
            ComponentMapper componentMapper = FXDnD.this.mapComponent(FXDnD.this.dropTargets, (int)dragEvent.getX(), (int)dragEvent.getY());
            EventType<DragEvent> eventType = dragEvent.getEventType();
            Dragboard dragboard = dragEvent.getDragboard();
            this.transferable.updateData(dragboard, DragEvent.DRAG_DROPPED.equals(eventType));
            int n2 = SwingDnD.transferModesToDropActions(dragboard.getTransferModes());
            int n3 = dragEvent.getTransferMode() == null ? 0 : SwingDnD.transferModeToDropAction(dragEvent.getTransferMode());
            DropTarget dropTarget = componentMapper.object != null ? (DropTarget)componentMapper.object : this.dt;
            SwingFXUtils.runOnEDTAndWait(FXDnD.this, () -> {
                if (dropTarget != this.dt) {
                    if (this.ctx != null) {
                        this.ctx.removeNotify();
                    }
                    this.ctx = null;
                    this.dropAction = 0;
                    this.currentAction = 0;
                }
                if (dropTarget != null) {
                    if (this.ctx == null) {
                        this.ctx = dropTarget.getDropTargetContext();
                        this.ctx.addNotify(this);
                    }
                    DropTarget dropTarget2 = dropTarget;
                    if (DragEvent.DRAG_DROPPED.equals(eventType)) {
                        DropTargetDropEvent dropTargetDropEvent = new DropTargetDropEvent(this.ctx, new Point(componentMapper.x, componentMapper.y), n3, n2);
                        dropTarget2.drop(dropTargetDropEvent);
                    } else {
                        DropTargetDragEvent dropTargetDragEvent = new DropTargetDragEvent(this.ctx, new Point(componentMapper.x, componentMapper.y), n3, n2);
                        if (DragEvent.DRAG_OVER.equals(eventType)) {
                            dropTarget2.dragOver(dropTargetDragEvent);
                        } else if (DragEvent.DRAG_ENTERED.equals(eventType)) {
                            dropTarget2.dragEnter(dropTargetDragEvent);
                        } else if (DragEvent.DRAG_EXITED.equals(eventType)) {
                            dropTarget2.dragExit(dropTargetDragEvent);
                        }
                    }
                }
                this.dt = (DropTarget)componentMapper.object;
                if (this.dt == null) {
                    if (this.ctx != null) {
                        this.ctx.removeNotify();
                    }
                    this.ctx = null;
                    this.dropAction = 0;
                    this.currentAction = 0;
                }
                if (DragEvent.DRAG_DROPPED.equals(eventType) || DragEvent.DRAG_EXITED.equals(eventType)) {
                    if (this.ctx != null) {
                        this.ctx.removeNotify();
                    }
                    this.ctx = null;
                }
                SwingFXUtils.leaveFXNestedLoop(FXDnD.this);
            });
            if (DragEvent.DRAG_DROPPED.equals(eventType)) {
                return this.dropAction;
            }
            return this.currentAction;
        }
    }

    private final class FXDragSourceContextPeer
    extends SunDragSourceContextPeer {
        private volatile int sourceActions;
        private final CachingTransferable transferable;

        @Override
        public void startSecondaryEventLoop() {
            Toolkit.getToolkit().enterNestedEventLoop(this);
        }

        @Override
        public void quitSecondaryEventLoop() {
            assert (!Platform.isFxApplicationThread());
            Platform.runLater(() -> Toolkit.getToolkit().exitNestedEventLoop(this, null));
        }

        @Override
        protected void setNativeCursor(long l2, Cursor cursor, int n2) {
        }

        private void dragDone(int n2, int n3, int n4) {
            this.dragDropFinished(n2 != 0, n2, n3, n4);
        }

        FXDragSourceContextPeer(DragGestureEvent dragGestureEvent) {
            super(dragGestureEvent);
            this.sourceActions = 0;
            this.transferable = new CachingTransferable();
        }

        @Override
        protected void startDrag(Transferable transferable, long[] arrl, Map map) {
            FXDnD.this.activeDSContextPeer = this;
            this.transferable.updateData(transferable, true);
            this.sourceActions = this.getDragSourceContext().getSourceActions();
            FXDnD.this.loop = java.awt.Toolkit.getDefaultToolkit().getSystemEventQueue().createSecondaryLoop();
            SwingFXUtils.leaveFXNestedLoop(FXDnD.this);
            if (!FXDnD.this.loop.enter()) {
                // empty if block
            }
        }
    }

    private class FXDragGestureRecognizer
    extends MouseDragGestureRecognizer {
        FXDragGestureRecognizer(DragSource dragSource, Component component, int n2, DragGestureListener dragGestureListener) {
            super(dragSource, component, n2, dragGestureListener);
            if (component != null) {
                FXDnD.this.recognizers.put(component, this);
            }
        }

        @Override
        public void setComponent(Component component) {
            Component component2 = this.getComponent();
            if (component2 != null) {
                FXDnD.this.recognizers.remove(component2);
            }
            super.setComponent(component);
            if (component != null) {
                FXDnD.this.recognizers.put(component, this);
            }
        }

        @Override
        protected void registerListeners() {
            SwingFXUtils.runOnFxThread(() -> {
                if (!FXDnD.this.isDragSourceListenerInstalled) {
                    FXDnD.this.node.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_PRESSED, FXDnD.this.onMousePressHandler);
                    FXDnD.this.node.addEventHandler(javafx.scene.input.MouseEvent.DRAG_DETECTED, FXDnD.this.onDragStartHandler);
                    FXDnD.this.node.addEventHandler(DragEvent.DRAG_DONE, FXDnD.this.onDragDoneHandler);
                    FXDnD.this.isDragSourceListenerInstalled = true;
                }
            });
        }

        @Override
        protected void unregisterListeners() {
            SwingFXUtils.runOnFxThread(() -> {
                if (FXDnD.this.isDragSourceListenerInstalled) {
                    FXDnD.this.node.removeEventHandler(javafx.scene.input.MouseEvent.MOUSE_PRESSED, FXDnD.this.onMousePressHandler);
                    FXDnD.this.node.removeEventHandler(javafx.scene.input.MouseEvent.DRAG_DETECTED, FXDnD.this.onDragStartHandler);
                    FXDnD.this.node.removeEventHandler(DragEvent.DRAG_DONE, FXDnD.this.onDragDoneHandler);
                    FXDnD.this.isDragSourceListenerInstalled = false;
                }
            });
        }

        private void fireEvent(int n2, int n3, long l2, int n4) {
            this.appendEvent(new MouseEvent(this.getComponent(), 501, l2, n4, n2, n3, 0, false));
            int n5 = SunDragSourceContextPeer.convertModifiersToDropAction(n4, this.getSourceActions());
            this.fireDragGestureRecognized(n5, new Point(n2, n3));
        }
    }

    private class ComponentMapper<T> {
        private int x;
        private int y;
        private T object = null;

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        private ComponentMapper(Map<Component, T> map, int n2, int n3) {
            this.x = n2;
            this.y = n3;
            JLightweightFrame jLightweightFrame = FXDnD.this.node.getLightweightFrame();
            Component component = AWTAccessor.getContainerAccessor().findComponentAt(jLightweightFrame, this.x, this.y, false);
            if (component == null) {
                return;
            }
            Object object = component.getTreeLock();
            synchronized (object) {
                do {
                    this.object = map.get(component);
                } while (this.object == null && (component = component.getParent()) != null);
                if (this.object != null) {
                    while (component != jLightweightFrame && component != null) {
                        this.x -= component.getX();
                        this.y -= component.getY();
                        component = component.getParent();
                    }
                }
            }
        }
    }
}

