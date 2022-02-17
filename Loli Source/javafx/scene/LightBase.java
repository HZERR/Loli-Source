/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene;

import com.sun.javafx.collections.TrackableObservableList;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.BoxBounds;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.sg.prism.NGLightBase;
import com.sun.javafx.tk.Toolkit;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape3D;
import sun.util.logging.PlatformLogger;

public abstract class LightBase
extends Node {
    private Affine3D localToSceneTx = new Affine3D();
    private ObjectProperty<Color> color;
    private BooleanProperty lightOn;
    private ObservableList<Node> scope;

    protected LightBase() {
        this(Color.WHITE);
    }

    protected LightBase(Color color) {
        if (!Platform.isSupported(ConditionalFeature.SCENE3D)) {
            String string = LightBase.class.getName();
            PlatformLogger.getLogger(string).warning("System can't support ConditionalFeature.SCENE3D");
        }
        this.setColor(color);
        this.localToSceneTransformProperty().addListener(observable -> this.impl_markDirty(DirtyBits.NODE_LIGHT_TRANSFORM));
    }

    public final void setColor(Color color) {
        this.colorProperty().set(color);
    }

    public final Color getColor() {
        return this.color == null ? null : (Color)this.color.get();
    }

    public final ObjectProperty<Color> colorProperty() {
        if (this.color == null) {
            this.color = new SimpleObjectProperty<Color>((Object)this, "color"){

                @Override
                protected void invalidated() {
                    LightBase.this.impl_markDirty(DirtyBits.NODE_LIGHT);
                }
            };
        }
        return this.color;
    }

    public final void setLightOn(boolean bl) {
        this.lightOnProperty().set(bl);
    }

    public final boolean isLightOn() {
        return this.lightOn == null ? true : this.lightOn.get();
    }

    public final BooleanProperty lightOnProperty() {
        if (this.lightOn == null) {
            this.lightOn = new SimpleBooleanProperty(this, "lightOn", true){

                @Override
                protected void invalidated() {
                    LightBase.this.impl_markDirty(DirtyBits.NODE_LIGHT);
                }
            };
        }
        return this.lightOn;
    }

    public ObservableList<Node> getScope() {
        if (this.scope == null) {
            this.scope = new TrackableObservableList<Node>(){

                @Override
                protected void onChanged(ListChangeListener.Change<Node> change) {
                    LightBase.this.impl_markDirty(DirtyBits.NODE_LIGHT_SCOPE);
                    while (change.next()) {
                        for (Node node : change.getRemoved()) {
                            if (!(node instanceof Parent) && !(node instanceof Shape3D)) continue;
                            LightBase.this.markChildrenDirty(node);
                        }
                        for (Node node : change.getAddedSubList()) {
                            if (!(node instanceof Parent) && !(node instanceof Shape3D)) continue;
                            LightBase.this.markChildrenDirty(node);
                        }
                    }
                }
            };
        }
        return this.scope;
    }

    @Override
    void scenesChanged(Scene scene, SubScene subScene, Scene scene2, SubScene subScene2) {
        if (subScene2 != null) {
            subScene2.removeLight(this);
        } else if (scene2 != null) {
            scene2.removeLight(this);
        }
        if (subScene != null) {
            subScene.addLight(this);
        } else if (scene != null) {
            scene.addLight(this);
        }
    }

    private void markOwnerDirty() {
        SubScene subScene = this.getSubScene();
        if (subScene != null) {
            subScene.markContentDirty();
        } else {
            Scene scene = this.getScene();
            if (scene != null) {
                scene.setNeedsRepaint();
            }
        }
    }

    private void markChildrenDirty(Node node) {
        if (node instanceof Shape3D) {
            ((Shape3D)node).impl_markDirty(DirtyBits.NODE_DRAWMODE);
        } else if (node instanceof Parent) {
            for (Node node2 : ((Parent)node).getChildren()) {
                this.markChildrenDirty(node2);
            }
        }
    }

    @Override
    @Deprecated
    protected void impl_markDirty(DirtyBits dirtyBits) {
        super.impl_markDirty(dirtyBits);
        if (this.scope == null || this.getScope().isEmpty()) {
            this.markOwnerDirty();
        } else if (dirtyBits != DirtyBits.NODE_LIGHT_SCOPE) {
            ObservableList<Node> observableList = this.getScope();
            int n2 = observableList.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                this.markChildrenDirty((Node)observableList.get(i2));
            }
        }
    }

    @Override
    @Deprecated
    public void impl_updatePeer() {
        super.impl_updatePeer();
        NGLightBase nGLightBase = (NGLightBase)this.impl_getPeer();
        if (this.impl_isDirty(DirtyBits.NODE_LIGHT)) {
            nGLightBase.setColor(this.getColor() == null ? Toolkit.getPaintAccessor().getPlatformPaint(Color.WHITE) : Toolkit.getPaintAccessor().getPlatformPaint(this.getColor()));
            nGLightBase.setLightOn(this.isLightOn());
        }
        if (this.impl_isDirty(DirtyBits.NODE_LIGHT_SCOPE) && this.scope != null) {
            ObservableList<Node> observableList = this.getScope();
            if (observableList.isEmpty()) {
                nGLightBase.setScope(null);
            } else {
                Object[] arrobject = new Object[observableList.size()];
                for (int i2 = 0; i2 < observableList.size(); ++i2) {
                    Node node = (Node)observableList.get(i2);
                    arrobject[i2] = node.impl_getPeer();
                }
                nGLightBase.setScope(arrobject);
            }
        }
        if (this.impl_isDirty(DirtyBits.NODE_LIGHT_TRANSFORM)) {
            this.localToSceneTx.setToIdentity();
            this.getLocalToSceneTransform().impl_apply(this.localToSceneTx);
            nGLightBase.setWorldTransform(this.localToSceneTx);
        }
    }

    @Override
    @Deprecated
    public BaseBounds impl_computeGeomBounds(BaseBounds baseBounds, BaseTransform baseTransform) {
        return new BoxBounds();
    }

    @Override
    @Deprecated
    protected boolean impl_computeContains(double d2, double d3) {
        return false;
    }

    @Override
    @Deprecated
    public Object impl_processMXNode(MXNodeAlgorithm mXNodeAlgorithm, MXNodeAlgorithmContext mXNodeAlgorithmContext) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

