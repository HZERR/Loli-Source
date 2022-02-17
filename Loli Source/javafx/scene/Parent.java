/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene;

import com.sun.javafx.collections.TrackableObservableList;
import com.sun.javafx.collections.VetoableListDecorator;
import com.sun.javafx.css.Selector;
import com.sun.javafx.css.StyleManager;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.PickRay;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.scene.CssFlags;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.scene.LayoutFlags;
import com.sun.javafx.scene.input.PickResultChooser;
import com.sun.javafx.scene.traversal.ParentTraversalEngine;
import com.sun.javafx.sg.prism.NGGroup;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.tk.Toolkit;
import com.sun.javafx.util.TempState;
import com.sun.javafx.util.Utils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.WritableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.PropertyHelper;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.stage.Window;

public abstract class Parent
extends Node {
    static final int DIRTY_CHILDREN_THRESHOLD = 10;
    private static final boolean warnOnAutoMove = PropertyHelper.getBooleanProperty("javafx.sg.warn");
    private static final int REMOVED_CHILDREN_THRESHOLD = 20;
    private boolean removedChildrenOptimizationDisabled = false;
    private final Set<Node> childSet = new HashSet<Node>();
    private int startIdx = 0;
    private int pgChildrenSize = 0;
    private boolean childrenTriggerPermutation = false;
    private List<Node> removed;
    private boolean geomChanged;
    private boolean childSetModified;
    private final ObservableList<Node> children = new VetoableListDecorator<Node>((ObservableList)new TrackableObservableList<Node>(){

        @Override
        protected void onChanged(ListChangeListener.Change<Node> change) {
            boolean bl;
            block17: {
                Parent.this.unmodifiableManagedChildren = null;
                bl = false;
                if (Parent.this.childSetModified) {
                    Object object;
                    int n2;
                    int n3;
                    while (change.next()) {
                        Node node;
                        int n4;
                        n3 = change.getFrom();
                        n2 = change.getTo();
                        for (int i2 = n3; i2 < n2; ++i2) {
                            Node node2 = (Node)Parent.this.children.get(i2);
                            if (node2.getParent() == null || node2.getParent() == Parent.this) continue;
                            if (warnOnAutoMove) {
                                System.err.println("WARNING added to a new parent without first removing it from its current");
                                System.err.println("    parent. It will be automatically removed from its current parent.");
                                System.err.println("    node=" + node2 + " oldparent= " + node2.getParent() + " newparent=" + this);
                            }
                            node2.getParent().children.remove(node2);
                            if (!warnOnAutoMove) continue;
                            Thread.dumpStack();
                        }
                        object = change.getRemoved();
                        int n5 = object.size();
                        for (n4 = 0; n4 < n5; ++n4) {
                            node = object.get(n4);
                            if (!node.isManaged()) continue;
                            bl = true;
                        }
                        for (n4 = n3; n4 < n2; ++n4) {
                            node = (Node)Parent.this.children.get(n4);
                            if (node.isManaged() || node instanceof Parent && ((Parent)node).layoutFlag != LayoutFlags.CLEAN) {
                                bl = true;
                            }
                            node.setParent(Parent.this);
                            node.setScenes(Parent.this.getScene(), Parent.this.getSubScene(), true);
                            if (!node.isVisible()) continue;
                            Parent.this.geomChanged = true;
                            Parent.this.childIncluded(node);
                        }
                    }
                    if (Parent.this.dirtyChildren == null && Parent.this.children.size() > 10) {
                        Parent.this.dirtyChildren = new ArrayList(20);
                        if (Parent.this.dirtyChildrenCount > 0) {
                            n3 = Parent.this.children.size();
                            for (n2 = 0; n2 < n3; ++n2) {
                                object = (Node)Parent.this.children.get(n2);
                                if (!((Node)object).isVisible() || !((Node)object).boundsChanged) continue;
                                Parent.this.dirtyChildren.add(object);
                            }
                        }
                    }
                } else {
                    while (change.next()) {
                        int n6;
                        List<Node> list = change.getRemoved();
                        int n7 = list.size();
                        for (n6 = 0; n6 < n7; ++n6) {
                            if (!list.get(n6).isManaged()) continue;
                            bl = true;
                            break block17;
                        }
                        n7 = change.getTo();
                        for (n6 = change.getFrom(); n6 < n7; ++n6) {
                            if (!((Node)Parent.this.children.get(n6)).isManaged()) continue;
                            bl = true;
                            break block17;
                        }
                    }
                }
            }
            if (bl) {
                Parent.this.requestLayout();
            }
            if (Parent.this.geomChanged) {
                Parent.this.impl_geomChanged();
            }
            change.reset();
            change.next();
            if (Parent.this.startIdx > change.getFrom()) {
                Parent.this.startIdx = change.getFrom();
            }
            Parent.this.impl_markDirty(DirtyBits.PARENT_CHILDREN);
            Parent.this.impl_markDirty(DirtyBits.NODE_FORCE_SYNC);
        }
    }){

        @Override
        protected void onProposedChange(List<Node> list, int[] arrn) {
            Node node;
            int n2;
            Window window;
            Scene scene = Parent.this.getScene();
            if (scene != null && (window = scene.getWindow()) != null && window.impl_getPeer() != null) {
                Toolkit.getToolkit().checkFxUserThread();
            }
            Parent.this.geomChanged = false;
            long l2 = Parent.this.children.size() + list.size();
            int n3 = 0;
            for (n2 = 0; n2 < arrn.length; n2 += 2) {
                n3 += arrn[n2 + 1] - arrn[n2];
            }
            l2 -= (long)n3;
            if (Parent.this.childrenTriggerPermutation) {
                Parent.this.childSetModified = false;
                return;
            }
            Parent.this.childSetModified = true;
            if (l2 == (long)Parent.this.childSet.size()) {
                Parent.this.childSetModified = false;
                for (n2 = list.size() - 1; n2 >= 0; --n2) {
                    node = list.get(n2);
                    if (Parent.this.childSet.contains(node)) continue;
                    Parent.this.childSetModified = true;
                    break;
                }
            }
            for (n2 = 0; n2 < arrn.length; n2 += 2) {
                for (int i2 = arrn[n2]; i2 < arrn[n2 + 1]; ++i2) {
                    Parent.this.childSet.remove(Parent.this.children.get(i2));
                }
            }
            try {
                if (Parent.this.childSetModified) {
                    for (n2 = list.size() - 1; n2 >= 0; --n2) {
                        node = list.get(n2);
                        if (node == null) {
                            throw new NullPointerException(this.constructExceptionMessage("child node is null", null));
                        }
                        if (node.getClipParent() != null) {
                            throw new IllegalArgumentException(this.constructExceptionMessage("node already used as a clip", node));
                        }
                        if (!Parent.this.wouldCreateCycle(Parent.this, node)) continue;
                        throw new IllegalArgumentException(this.constructExceptionMessage("cycle detected", node));
                    }
                }
                Parent.this.childSet.addAll(list);
                if ((long)Parent.this.childSet.size() != l2) {
                    throw new IllegalArgumentException(this.constructExceptionMessage("duplicate children added", null));
                }
            }
            catch (RuntimeException runtimeException) {
                Parent.this.childSet.clear();
                Parent.this.childSet.addAll(Parent.this.children);
                throw runtimeException;
            }
            if (!Parent.this.childSetModified) {
                return;
            }
            if (Parent.this.removed == null) {
                Parent.this.removed = new ArrayList();
            }
            if (Parent.this.removed.size() + n3 > 20 || !Parent.this.impl_isTreeVisible()) {
                Parent.this.removedChildrenOptimizationDisabled = true;
            }
            for (n2 = 0; n2 < arrn.length; n2 += 2) {
                for (int i3 = arrn[n2]; i3 < arrn[n2 + 1]; ++i3) {
                    Node node2 = (Node)Parent.this.children.get(i3);
                    Scene scene2 = node2.getScene();
                    if (scene2 != null) {
                        scene2.generateMouseExited(node2);
                    }
                    if (Parent.this.dirtyChildren != null) {
                        Parent.this.dirtyChildren.remove(node2);
                    }
                    if (node2.isVisible()) {
                        Parent.this.geomChanged = true;
                        Parent.this.childExcluded(node2);
                    }
                    if (node2.getParent() == Parent.this) {
                        node2.setParent(null);
                        node2.setScenes(null, null, false);
                    }
                    if (scene == null || Parent.this.removedChildrenOptimizationDisabled) continue;
                    Parent.this.removed.add(node2);
                }
            }
        }

        private String constructExceptionMessage(String string, Node node) {
            StringBuilder stringBuilder = new StringBuilder("Children: ");
            stringBuilder.append(string);
            stringBuilder.append(": parent = ").append(Parent.this);
            if (node != null) {
                stringBuilder.append(", node = ").append(node);
            }
            return stringBuilder.toString();
        }
    };
    private final ObservableList<Node> unmodifiableChildren = FXCollections.unmodifiableObservableList(this.children);
    private List<Node> unmodifiableManagedChildren = null;
    private ObjectProperty<ParentTraversalEngine> impl_traversalEngine;
    private ReadOnlyBooleanWrapper needsLayout;
    LayoutFlags layoutFlag = LayoutFlags.CLEAN;
    boolean performingLayout = false;
    private boolean sizeCacheClear = true;
    private double prefWidthCache = -1.0;
    private double prefHeightCache = -1.0;
    private double minWidthCache = -1.0;
    private double minHeightCache = -1.0;
    private boolean sceneRoot = false;
    boolean layoutRoot = false;
    private final ObservableList<String> stylesheets = new TrackableObservableList<String>(){

        @Override
        protected void onChanged(ListChangeListener.Change<String> change) {
            Scene scene = Parent.this.getScene();
            if (scene != null) {
                StyleManager.getInstance().stylesheetsChanged(Parent.this, change);
                change.reset();
                while (change.next() && !change.wasRemoved()) {
                }
                Parent.this.impl_reapplyCSS();
            }
        }
    };
    private BaseBounds tmp = new RectBounds();
    private BaseBounds cachedBounds = new RectBounds();
    private boolean cachedBoundsInvalid;
    private int dirtyChildrenCount;
    private ArrayList<Node> dirtyChildren;
    private Node top;
    private Node left;
    private Node bottom;
    private Node right;
    private Node near;
    private Node far;
    private final int LEFT_INVALID = 1;
    private final int TOP_INVALID = 2;
    private final int NEAR_INVALID = 4;
    private final int RIGHT_INVALID = 8;
    private final int BOTTOM_INVALID = 16;
    private final int FAR_INVALID = 32;
    private Node currentlyProcessedChild;

    @Override
    @Deprecated
    public void impl_updatePeer() {
        List<NGNode> list;
        super.impl_updatePeer();
        NGGroup nGGroup = (NGGroup)this.impl_getPeer();
        if (Utils.assertionEnabled() && (list = nGGroup.getChildren()).size() != this.pgChildrenSize) {
            System.err.println("*** pgnodes.size() [" + list.size() + "] != pgChildrenSize [" + this.pgChildrenSize + "]");
        }
        if (this.impl_isDirty(DirtyBits.PARENT_CHILDREN)) {
            int n2;
            nGGroup.clearFrom(this.startIdx);
            for (n2 = this.startIdx; n2 < this.children.size(); ++n2) {
                nGGroup.add(n2, (NGNode)((Node)this.children.get(n2)).impl_getPeer());
            }
            if (this.removedChildrenOptimizationDisabled) {
                nGGroup.markDirty();
                this.removedChildrenOptimizationDisabled = false;
            } else if (this.removed != null && !this.removed.isEmpty()) {
                for (n2 = 0; n2 < this.removed.size(); ++n2) {
                    nGGroup.addToRemoved((NGNode)this.removed.get(n2).impl_getPeer());
                }
            }
            if (this.removed != null) {
                this.removed.clear();
            }
            this.startIdx = this.pgChildrenSize = this.children.size();
        }
        if (Utils.assertionEnabled()) {
            this.validatePG();
        }
    }

    void validatePG() {
        boolean bl = false;
        NGGroup nGGroup = (NGGroup)this.impl_getPeer();
        List<NGNode> list = nGGroup.getChildren();
        if (list.size() != this.children.size()) {
            System.err.println("*** pgnodes.size validatePG() [" + list.size() + "] != children.size() [" + this.children.size() + "]");
            bl = true;
        } else {
            for (int i2 = 0; i2 < this.children.size(); ++i2) {
                Node node = (Node)this.children.get(i2);
                if (node.getParent() != this) {
                    System.err.println("*** this=" + this + " validatePG children[" + i2 + "].parent= " + node.getParent());
                    bl = true;
                }
                if (node.impl_getPeer() == list.get(i2)) continue;
                System.err.println("*** pgnodes[" + i2 + "] validatePG != children[" + i2 + "]");
                bl = true;
            }
        }
        if (bl) {
            throw new AssertionError((Object)"validation of PGGroup children failed");
        }
    }

    void printSeq(String string, List<Node> list) {
        String string2 = string;
        for (Node node : list) {
            string2 = string2 + node + " ";
        }
        System.out.println(string2);
    }

    protected ObservableList<Node> getChildren() {
        return this.children;
    }

    public ObservableList<Node> getChildrenUnmodifiable() {
        return this.unmodifiableChildren;
    }

    protected <E extends Node> List<E> getManagedChildren() {
        if (this.unmodifiableManagedChildren == null) {
            this.unmodifiableManagedChildren = new ArrayList<Node>();
            int n2 = this.children.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                Node node = (Node)this.children.get(i2);
                if (!node.isManaged()) continue;
                this.unmodifiableManagedChildren.add(node);
            }
        }
        return this.unmodifiableManagedChildren;
    }

    final void managedChildChanged() {
        this.requestLayout();
        this.unmodifiableManagedChildren = null;
    }

    final void impl_toFront(Node node) {
        if (Utils.assertionEnabled() && !this.childSet.contains(node)) {
            throw new AssertionError((Object)"specified node is not in the list of children");
        }
        if (this.children.get(this.children.size() - 1) != node) {
            this.childrenTriggerPermutation = true;
            try {
                this.children.remove(node);
                this.children.add(node);
            }
            finally {
                this.childrenTriggerPermutation = false;
            }
        }
    }

    final void impl_toBack(Node node) {
        if (Utils.assertionEnabled() && !this.childSet.contains(node)) {
            throw new AssertionError((Object)"specified node is not in the list of children");
        }
        if (this.children.get(0) != node) {
            this.childrenTriggerPermutation = true;
            try {
                this.children.remove(node);
                this.children.add(0, node);
            }
            finally {
                this.childrenTriggerPermutation = false;
            }
        }
    }

    @Override
    void scenesChanged(Scene scene, SubScene subScene, Scene scene2, SubScene subScene2) {
        int n2;
        if (scene2 != null && scene == null) {
            StyleManager.getInstance().forget(this);
            if (this.removed != null) {
                this.removed.clear();
            }
        }
        for (n2 = 0; n2 < this.children.size(); ++n2) {
            ((Node)this.children.get(n2)).setScenes(scene, subScene, false);
        }
        n2 = this.layoutFlag != LayoutFlags.CLEAN ? 1 : 0;
        this.sceneRoot = subScene != null && subScene.getRoot() == this || scene != null && scene.getRoot() == this;
        boolean bl = this.layoutRoot = !this.isManaged() || this.sceneRoot;
        if (n2 != 0 && scene != null && this.layoutRoot && subScene != null) {
            subScene.setDirtyLayout(this);
        }
    }

    @Override
    void setDerivedDepthTest(boolean bl) {
        super.setDerivedDepthTest(bl);
        int n2 = this.children.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            Node node = (Node)this.children.get(i2);
            node.computeDerivedDepthTest();
        }
    }

    @Override
    @Deprecated
    protected void impl_pickNodeLocal(PickRay pickRay, PickResultChooser pickResultChooser) {
        double d2 = this.impl_intersectsBounds(pickRay);
        if (!Double.isNaN(d2)) {
            for (int i2 = this.children.size() - 1; i2 >= 0; --i2) {
                ((Node)this.children.get(i2)).impl_pickNode(pickRay, pickResultChooser);
                if (!pickResultChooser.isClosed()) continue;
                return;
            }
            if (this.isPickOnBounds()) {
                pickResultChooser.offer(this, d2, PickResultChooser.computePoint(pickRay, d2));
            }
        }
    }

    @Override
    boolean isConnected() {
        return super.isConnected() || this.sceneRoot;
    }

    @Override
    public Node lookup(String string) {
        Node node = super.lookup(string);
        if (node == null) {
            int n2 = this.children.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                Node node2 = (Node)this.children.get(i2);
                node = node2.lookup(string);
                if (node == null) continue;
                return node;
            }
        }
        return node;
    }

    @Override
    List<Node> lookupAll(Selector selector, List<Node> list) {
        list = super.lookupAll(selector, list);
        int n2 = this.children.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            Node node = (Node)this.children.get(i2);
            list = node.lookupAll(selector, list);
        }
        return list;
    }

    @Deprecated
    public final void setImpl_traversalEngine(ParentTraversalEngine parentTraversalEngine) {
        this.impl_traversalEngineProperty().set(parentTraversalEngine);
    }

    @Deprecated
    public final ParentTraversalEngine getImpl_traversalEngine() {
        return this.impl_traversalEngine == null ? null : (ParentTraversalEngine)this.impl_traversalEngine.get();
    }

    @Deprecated
    public final ObjectProperty<ParentTraversalEngine> impl_traversalEngineProperty() {
        if (this.impl_traversalEngine == null) {
            this.impl_traversalEngine = new SimpleObjectProperty<ParentTraversalEngine>(this, "impl_traversalEngine");
        }
        return this.impl_traversalEngine;
    }

    protected final void setNeedsLayout(boolean bl) {
        if (bl) {
            this.markDirtyLayout(true);
        } else if (this.layoutFlag == LayoutFlags.NEEDS_LAYOUT) {
            boolean bl2 = false;
            int n2 = this.children.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                Node node = (Node)this.children.get(i2);
                if (!(node instanceof Parent) || ((Parent)node).layoutFlag == LayoutFlags.CLEAN) continue;
                bl2 = true;
                break;
            }
            this.setLayoutFlag(bl2 ? LayoutFlags.DIRTY_BRANCH : LayoutFlags.CLEAN);
        }
    }

    public final boolean isNeedsLayout() {
        return this.layoutFlag == LayoutFlags.NEEDS_LAYOUT;
    }

    public final ReadOnlyBooleanProperty needsLayoutProperty() {
        if (this.needsLayout == null) {
            this.needsLayout = new ReadOnlyBooleanWrapper(this, "needsLayout", this.layoutFlag == LayoutFlags.NEEDS_LAYOUT);
        }
        return this.needsLayout;
    }

    void setLayoutFlag(LayoutFlags layoutFlags) {
        if (this.needsLayout != null) {
            this.needsLayout.set(layoutFlags == LayoutFlags.NEEDS_LAYOUT);
        }
        this.layoutFlag = layoutFlags;
    }

    private void markDirtyLayout(boolean bl) {
        this.setLayoutFlag(LayoutFlags.NEEDS_LAYOUT);
        if (bl || this.layoutRoot) {
            if (this.sceneRoot) {
                Toolkit.getToolkit().requestNextPulse();
                if (this.getSubScene() != null) {
                    this.getSubScene().setDirtyLayout(this);
                }
            } else {
                this.markDirtyLayoutBranch();
            }
        } else {
            this.requestParentLayout();
        }
    }

    public void requestLayout() {
        this.clearSizeCache();
        this.markDirtyLayout(false);
    }

    protected final void requestParentLayout() {
        Parent parent;
        if (!this.layoutRoot && (parent = this.getParent()) != null && !parent.performingLayout) {
            parent.requestLayout();
        }
    }

    void clearSizeCache() {
        if (this.sizeCacheClear) {
            return;
        }
        this.sizeCacheClear = true;
        this.prefWidthCache = -1.0;
        this.prefHeightCache = -1.0;
        this.minWidthCache = -1.0;
        this.minHeightCache = -1.0;
    }

    @Override
    public double prefWidth(double d2) {
        if (d2 == -1.0) {
            if (this.prefWidthCache == -1.0) {
                this.prefWidthCache = this.computePrefWidth(-1.0);
                if (Double.isNaN(this.prefWidthCache) || this.prefWidthCache < 0.0) {
                    this.prefWidthCache = 0.0;
                }
                this.sizeCacheClear = false;
            }
            return this.prefWidthCache;
        }
        double d3 = this.computePrefWidth(d2);
        return Double.isNaN(d3) || d3 < 0.0 ? 0.0 : d3;
    }

    @Override
    public double prefHeight(double d2) {
        if (d2 == -1.0) {
            if (this.prefHeightCache == -1.0) {
                this.prefHeightCache = this.computePrefHeight(-1.0);
                if (Double.isNaN(this.prefHeightCache) || this.prefHeightCache < 0.0) {
                    this.prefHeightCache = 0.0;
                }
                this.sizeCacheClear = false;
            }
            return this.prefHeightCache;
        }
        double d3 = this.computePrefHeight(d2);
        return Double.isNaN(d3) || d3 < 0.0 ? 0.0 : d3;
    }

    @Override
    public double minWidth(double d2) {
        if (d2 == -1.0) {
            if (this.minWidthCache == -1.0) {
                this.minWidthCache = this.computeMinWidth(-1.0);
                if (Double.isNaN(this.minWidthCache) || this.minWidthCache < 0.0) {
                    this.minWidthCache = 0.0;
                }
                this.sizeCacheClear = false;
            }
            return this.minWidthCache;
        }
        double d3 = this.computeMinWidth(d2);
        return Double.isNaN(d3) || d3 < 0.0 ? 0.0 : d3;
    }

    @Override
    public double minHeight(double d2) {
        if (d2 == -1.0) {
            if (this.minHeightCache == -1.0) {
                this.minHeightCache = this.computeMinHeight(-1.0);
                if (Double.isNaN(this.minHeightCache) || this.minHeightCache < 0.0) {
                    this.minHeightCache = 0.0;
                }
                this.sizeCacheClear = false;
            }
            return this.minHeightCache;
        }
        double d3 = this.computeMinHeight(d2);
        return Double.isNaN(d3) || d3 < 0.0 ? 0.0 : d3;
    }

    protected double computePrefWidth(double d2) {
        double d3 = 0.0;
        double d4 = 0.0;
        int n2 = this.children.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            Node node = (Node)this.children.get(i2);
            if (!node.isManaged()) continue;
            double d5 = node.getLayoutBounds().getMinX() + node.getLayoutX();
            d3 = Math.min(d3, d5);
            d4 = Math.max(d4, d5 + this.boundedSize(node.prefWidth(-1.0), node.minWidth(-1.0), node.maxWidth(-1.0)));
        }
        return d4 - d3;
    }

    protected double computePrefHeight(double d2) {
        double d3 = 0.0;
        double d4 = 0.0;
        int n2 = this.children.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            Node node = (Node)this.children.get(i2);
            if (!node.isManaged()) continue;
            double d5 = node.getLayoutBounds().getMinY() + node.getLayoutY();
            d3 = Math.min(d3, d5);
            d4 = Math.max(d4, d5 + this.boundedSize(node.prefHeight(-1.0), node.minHeight(-1.0), node.maxHeight(-1.0)));
        }
        return d4 - d3;
    }

    protected double computeMinWidth(double d2) {
        return this.prefWidth(d2);
    }

    protected double computeMinHeight(double d2) {
        return this.prefHeight(d2);
    }

    @Override
    public double getBaselineOffset() {
        int n2 = this.children.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            double d2;
            Node node = (Node)this.children.get(i2);
            if (!node.isManaged() || (d2 = node.getBaselineOffset()) == Double.NEGATIVE_INFINITY) continue;
            return node.getLayoutBounds().getMinY() + node.getLayoutY() + d2;
        }
        return super.getBaselineOffset();
    }

    public final void layout() {
        switch (this.layoutFlag) {
            case CLEAN: {
                break;
            }
            case NEEDS_LAYOUT: {
                if (this.performingLayout) break;
                this.performingLayout = true;
                this.layoutChildren();
            }
            case DIRTY_BRANCH: {
                int n2 = this.children.size();
                for (int i2 = 0; i2 < n2; ++i2) {
                    Node node = (Node)this.children.get(i2);
                    if (node instanceof Parent) {
                        ((Parent)node).layout();
                        continue;
                    }
                    if (!(node instanceof SubScene)) continue;
                    ((SubScene)node).layoutPass();
                }
                this.setLayoutFlag(LayoutFlags.CLEAN);
                this.performingLayout = false;
            }
        }
    }

    protected void layoutChildren() {
        int n2 = this.children.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            Node node = (Node)this.children.get(i2);
            if (!node.isResizable() || !node.isManaged()) continue;
            node.autosize();
        }
    }

    @Override
    final void notifyManagedChanged() {
        this.layoutRoot = !this.isManaged() || this.sceneRoot;
    }

    final boolean isSceneRoot() {
        return this.sceneRoot;
    }

    public final ObservableList<String> getStylesheets() {
        return this.stylesheets;
    }

    @Deprecated
    public List<String> impl_getAllParentStylesheets() {
        List<String> list = null;
        Parent parent = this.getParent();
        if (parent != null) {
            list = parent.impl_getAllParentStylesheets();
        }
        if (this.stylesheets != null && !this.stylesheets.isEmpty()) {
            if (list == null) {
                list = new ArrayList<String>(this.stylesheets.size());
            }
            int n2 = this.stylesheets.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                list.add((String)this.stylesheets.get(i2));
            }
        }
        return list;
    }

    @Override
    @Deprecated
    protected void impl_processCSS(WritableValue<Boolean> writableValue) {
        if (this.cssFlag == CssFlags.CLEAN) {
            return;
        }
        if (this.cssFlag == CssFlags.DIRTY_BRANCH) {
            super.processCSS();
            return;
        }
        super.impl_processCSS(writableValue);
        if (this.children.isEmpty()) {
            return;
        }
        Node[] arrnode = this.children.toArray(new Node[this.children.size()]);
        for (int i2 = 0; i2 < arrnode.length; ++i2) {
            Node node = arrnode[i2];
            Parent parent = node.getParent();
            if (parent == null || parent != this) continue;
            if (CssFlags.UPDATE.compareTo(node.cssFlag) > 0) {
                node.cssFlag = CssFlags.UPDATE;
            }
            node.impl_processCSS(writableValue);
        }
    }

    protected Parent() {
        this.layoutFlag = LayoutFlags.NEEDS_LAYOUT;
        this.setAccessibleRole(AccessibleRole.PARENT);
    }

    @Override
    @Deprecated
    protected NGNode impl_createPeer() {
        return new NGGroup();
    }

    @Override
    void nodeResolvedOrientationChanged() {
        int n2 = this.children.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            ((Node)this.children.get(i2)).parentResolvedOrientationInvalidated();
        }
    }

    @Override
    @Deprecated
    public BaseBounds impl_computeGeomBounds(BaseBounds baseBounds, BaseTransform baseTransform) {
        if (this.children.isEmpty()) {
            return baseBounds.makeEmpty();
        }
        if (baseTransform.isTranslateOrIdentity()) {
            if (this.cachedBoundsInvalid) {
                this.recomputeBounds();
                if (this.dirtyChildren != null) {
                    this.dirtyChildren.clear();
                }
                this.cachedBoundsInvalid = false;
                this.dirtyChildrenCount = 0;
            }
            baseBounds = !baseTransform.isIdentity() ? baseBounds.deriveWithNewBounds((float)((double)this.cachedBounds.getMinX() + baseTransform.getMxt()), (float)((double)this.cachedBounds.getMinY() + baseTransform.getMyt()), (float)((double)this.cachedBounds.getMinZ() + baseTransform.getMzt()), (float)((double)this.cachedBounds.getMaxX() + baseTransform.getMxt()), (float)((double)this.cachedBounds.getMaxY() + baseTransform.getMyt()), (float)((double)this.cachedBounds.getMaxZ() + baseTransform.getMzt())) : baseBounds.deriveWithNewBounds(this.cachedBounds);
            return baseBounds;
        }
        double d2 = Double.MAX_VALUE;
        double d3 = Double.MAX_VALUE;
        double d4 = Double.MAX_VALUE;
        double d5 = Double.MIN_VALUE;
        double d6 = Double.MIN_VALUE;
        double d7 = Double.MIN_VALUE;
        boolean bl = true;
        int n2 = this.children.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            Node node = (Node)this.children.get(i2);
            if (!node.isVisible() || (baseBounds = this.getChildTransformedBounds(node, baseTransform, baseBounds)).isEmpty()) continue;
            if (bl) {
                d2 = baseBounds.getMinX();
                d3 = baseBounds.getMinY();
                d4 = baseBounds.getMinZ();
                d5 = baseBounds.getMaxX();
                d6 = baseBounds.getMaxY();
                d7 = baseBounds.getMaxZ();
                bl = false;
                continue;
            }
            d2 = Math.min((double)baseBounds.getMinX(), d2);
            d3 = Math.min((double)baseBounds.getMinY(), d3);
            d4 = Math.min((double)baseBounds.getMinZ(), d4);
            d5 = Math.max((double)baseBounds.getMaxX(), d5);
            d6 = Math.max((double)baseBounds.getMaxY(), d6);
            d7 = Math.max((double)baseBounds.getMaxZ(), d7);
        }
        if (bl) {
            baseBounds.makeEmpty();
        } else {
            baseBounds = baseBounds.deriveWithNewBounds((float)d2, (float)d3, (float)d4, (float)d5, (float)d6, (float)d7);
        }
        return baseBounds;
    }

    private void setChildDirty(Node node, boolean bl) {
        if (node.boundsChanged == bl) {
            return;
        }
        node.boundsChanged = bl;
        if (bl) {
            if (this.dirtyChildren != null) {
                this.dirtyChildren.add(node);
            }
            ++this.dirtyChildrenCount;
        } else {
            if (this.dirtyChildren != null) {
                this.dirtyChildren.remove(node);
            }
            --this.dirtyChildrenCount;
        }
    }

    private void childIncluded(Node node) {
        this.cachedBoundsInvalid = true;
        this.setChildDirty(node, true);
    }

    private void childExcluded(Node node) {
        if (node == this.left) {
            this.left = null;
            this.cachedBoundsInvalid = true;
        }
        if (node == this.top) {
            this.top = null;
            this.cachedBoundsInvalid = true;
        }
        if (node == this.near) {
            this.near = null;
            this.cachedBoundsInvalid = true;
        }
        if (node == this.right) {
            this.right = null;
            this.cachedBoundsInvalid = true;
        }
        if (node == this.bottom) {
            this.bottom = null;
            this.cachedBoundsInvalid = true;
        }
        if (node == this.far) {
            this.far = null;
            this.cachedBoundsInvalid = true;
        }
        this.setChildDirty(node, false);
    }

    private void recomputeBounds() {
        if (this.children.isEmpty()) {
            this.cachedBounds.makeEmpty();
            return;
        }
        if (this.children.size() == 1) {
            Node node = (Node)this.children.get(0);
            node.boundsChanged = false;
            if (node.isVisible()) {
                this.cachedBounds = this.getChildTransformedBounds(node, BaseTransform.IDENTITY_TRANSFORM, this.cachedBounds);
                this.near = this.far = node;
                this.right = this.far;
                this.bottom = this.far;
                this.left = this.far;
                this.top = this.far;
            } else {
                this.cachedBounds.makeEmpty();
            }
            return;
        }
        if (this.dirtyChildrenCount == 0 || !this.updateCachedBounds(this.dirtyChildren != null ? this.dirtyChildren : this.children, this.dirtyChildrenCount)) {
            this.createCachedBounds(this.children);
        }
    }

    private boolean updateCachedBounds(List<Node> list, int n2) {
        if (this.cachedBounds.isEmpty()) {
            this.createCachedBounds(list);
            return true;
        }
        int n3 = 0;
        if (this.left == null || this.left.boundsChanged) {
            n3 |= 1;
        }
        if (this.top == null || this.top.boundsChanged) {
            n3 |= 2;
        }
        if (this.near == null || this.near.boundsChanged) {
            n3 |= 4;
        }
        if (this.right == null || this.right.boundsChanged) {
            n3 |= 8;
        }
        if (this.bottom == null || this.bottom.boundsChanged) {
            n3 |= 0x10;
        }
        if (this.far == null || this.far.boundsChanged) {
            n3 |= 0x20;
        }
        float f2 = this.cachedBounds.getMinX();
        float f3 = this.cachedBounds.getMinY();
        float f4 = this.cachedBounds.getMinZ();
        float f5 = this.cachedBounds.getMaxX();
        float f6 = this.cachedBounds.getMaxY();
        float f7 = this.cachedBounds.getMaxZ();
        int n4 = list.size() - 1;
        while (n2 > 0) {
            Node node = list.get(n4);
            if (node.boundsChanged) {
                node.boundsChanged = false;
                --n2;
                this.tmp = this.getChildTransformedBounds(node, BaseTransform.IDENTITY_TRANSFORM, this.tmp);
                if (!this.tmp.isEmpty()) {
                    float f8 = this.tmp.getMinX();
                    float f9 = this.tmp.getMinY();
                    float f10 = this.tmp.getMinZ();
                    float f11 = this.tmp.getMaxX();
                    float f12 = this.tmp.getMaxY();
                    float f13 = this.tmp.getMaxZ();
                    if (f8 <= f2) {
                        f2 = f8;
                        this.left = node;
                        n3 &= 0xFFFFFFFE;
                    }
                    if (f9 <= f3) {
                        f3 = f9;
                        this.top = node;
                        n3 &= 0xFFFFFFFD;
                    }
                    if (f10 <= f4) {
                        f4 = f10;
                        this.near = node;
                        n3 &= 0xFFFFFFFB;
                    }
                    if (f11 >= f5) {
                        f5 = f11;
                        this.right = node;
                        n3 &= 0xFFFFFFF7;
                    }
                    if (f12 >= f6) {
                        f6 = f12;
                        this.bottom = node;
                        n3 &= 0xFFFFFFEF;
                    }
                    if (f13 >= f7) {
                        f7 = f13;
                        this.far = node;
                        n3 &= 0xFFFFFFDF;
                    }
                }
            }
            --n4;
        }
        if (n3 != 0) {
            return false;
        }
        this.cachedBounds = this.cachedBounds.deriveWithNewBounds(f2, f3, f4, f5, f6, f7);
        return true;
    }

    private void createCachedBounds(List<Node> list) {
        Node node;
        int n2;
        int n3 = list.size();
        for (n2 = 0; n2 < n3; ++n2) {
            node = list.get(n2);
            node.boundsChanged = false;
            if (!node.isVisible()) continue;
            this.tmp = node.getTransformedBounds(this.tmp, BaseTransform.IDENTITY_TRANSFORM);
            if (this.tmp.isEmpty()) continue;
            this.bottom = this.far = node;
            this.right = this.far;
            this.near = this.far;
            this.top = this.far;
            this.left = this.far;
            break;
        }
        if (n2 == n3) {
            this.far = null;
            this.bottom = null;
            this.right = null;
            this.near = null;
            this.top = null;
            this.left = null;
            this.cachedBounds.makeEmpty();
            return;
        }
        float f2 = this.tmp.getMinX();
        float f3 = this.tmp.getMinY();
        float f4 = this.tmp.getMinZ();
        float f5 = this.tmp.getMaxX();
        float f6 = this.tmp.getMaxY();
        float f7 = this.tmp.getMaxZ();
        ++n2;
        while (n2 < n3) {
            node = list.get(n2);
            node.boundsChanged = false;
            if (node.isVisible()) {
                this.tmp = node.getTransformedBounds(this.tmp, BaseTransform.IDENTITY_TRANSFORM);
                if (!this.tmp.isEmpty()) {
                    float f8 = this.tmp.getMinX();
                    float f9 = this.tmp.getMinY();
                    float f10 = this.tmp.getMinZ();
                    float f11 = this.tmp.getMaxX();
                    float f12 = this.tmp.getMaxY();
                    float f13 = this.tmp.getMaxZ();
                    if (f8 < f2) {
                        f2 = f8;
                        this.left = node;
                    }
                    if (f9 < f3) {
                        f3 = f9;
                        this.top = node;
                    }
                    if (f10 < f4) {
                        f4 = f10;
                        this.near = node;
                    }
                    if (f11 > f5) {
                        f5 = f11;
                        this.right = node;
                    }
                    if (f12 > f6) {
                        f6 = f12;
                        this.bottom = node;
                    }
                    if (f13 > f7) {
                        f7 = f13;
                        this.far = node;
                    }
                }
            }
            ++n2;
        }
        this.cachedBounds = this.cachedBounds.deriveWithNewBounds(f2, f3, f4, f5, f6, f7);
    }

    @Override
    protected void updateBounds() {
        int n2 = this.children.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            ((Node)this.children.get(i2)).updateBounds();
        }
        super.updateBounds();
    }

    private BaseBounds getChildTransformedBounds(Node node, BaseTransform baseTransform, BaseBounds baseBounds) {
        this.currentlyProcessedChild = node;
        baseBounds = node.getTransformedBounds(baseBounds, baseTransform);
        this.currentlyProcessedChild = null;
        return baseBounds;
    }

    void childBoundsChanged(Node node) {
        if (node == this.currentlyProcessedChild) {
            return;
        }
        this.cachedBoundsInvalid = true;
        this.setChildDirty(node, true);
        this.impl_geomChanged();
    }

    void childVisibilityChanged(Node node) {
        if (node.isVisible()) {
            this.childIncluded(node);
        } else {
            this.childExcluded(node);
        }
        this.impl_geomChanged();
    }

    @Override
    @Deprecated
    protected boolean impl_computeContains(double d2, double d3) {
        Point2D point2D = TempState.getInstance().point;
        int n2 = this.children.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            Node node = (Node)this.children.get(i2);
            point2D.x = (float)d2;
            point2D.y = (float)d3;
            try {
                node.parentToLocal(point2D);
            }
            catch (NoninvertibleTransformException noninvertibleTransformException) {
                continue;
            }
            if (!node.contains(point2D.x, point2D.y)) continue;
            return true;
        }
        return false;
    }

    @Override
    @Deprecated
    public Object impl_processMXNode(MXNodeAlgorithm mXNodeAlgorithm, MXNodeAlgorithmContext mXNodeAlgorithmContext) {
        return mXNodeAlgorithm.processContainerNode(this, mXNodeAlgorithmContext);
    }

    @Override
    public Object queryAccessibleAttribute(AccessibleAttribute accessibleAttribute, Object ... arrobject) {
        switch (accessibleAttribute) {
            case CHILDREN: {
                return this.getChildrenUnmodifiable();
            }
        }
        return super.queryAccessibleAttribute(accessibleAttribute, arrobject);
    }

    @Override
    void releaseAccessible() {
        int n2 = this.children.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            Node node = (Node)this.children.get(i2);
            node.releaseAccessible();
        }
        super.releaseAccessible();
    }

    List<Node> test_getRemoved() {
        return this.removed;
    }
}

