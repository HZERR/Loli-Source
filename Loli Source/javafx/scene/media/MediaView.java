/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.media;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.sg.prism.MediaFrameTracker;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.tk.Toolkit;
import com.sun.media.jfxmedia.events.VideoFrameRateListener;
import com.sun.media.jfxmediaimpl.HostUtils;
import com.sun.media.jfxmediaimpl.platform.ios.IOSMediaPlayer;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.ObservableMap;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaErrorEvent;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.NGMediaView;

public class MediaView
extends Node {
    private static final String VIDEO_FRAME_RATE_PROPERTY_NAME = "jfxmedia.decodedVideoFPS";
    private static final String DEFAULT_STYLE_CLASS = "media-view";
    private InvalidationListener errorListener = new MediaErrorInvalidationListener();
    private InvalidationListener mediaDimensionListener = observable -> {
        this.impl_markDirty(DirtyBits.NODE_VIEWPORT);
        this.impl_geomChanged();
    };
    private VideoFrameRateListener decodedFrameRateListener;
    private boolean registerVideoFrameRateListener = false;
    private boolean mediaPlayerReady;
    private ChangeListener<Parent> parentListener;
    private ChangeListener<Boolean> treeVisibleListener;
    private ChangeListener<Number> opacityListener;
    private ObjectProperty<MediaPlayer> mediaPlayer;
    private ObjectProperty<EventHandler<MediaErrorEvent>> onError;
    private BooleanProperty preserveRatio;
    private BooleanProperty smooth;
    private DoubleProperty x;
    private DoubleProperty y;
    private DoubleProperty fitWidth;
    private DoubleProperty fitHeight;
    private ObjectProperty<Rectangle2D> viewport;
    private int decodedFrameCount;
    private int renderedFrameCount;

    private VideoFrameRateListener createVideoFrameRateListener() {
        String string = null;
        try {
            string = System.getProperty(VIDEO_FRAME_RATE_PROPERTY_NAME);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        if (string == null || !Boolean.getBoolean(VIDEO_FRAME_RATE_PROPERTY_NAME)) {
            return null;
        }
        return d2 -> Platform.runLater(() -> {
            ObservableMap<Object, Object> observableMap = this.getProperties();
            observableMap.put(VIDEO_FRAME_RATE_PROPERTY_NAME, d2);
        });
    }

    private void createListeners() {
        this.parentListener = (observableValue, parent, parent2) -> this.updateOverlayVisibility();
        this.treeVisibleListener = (observableValue, bl, bl2) -> this.updateOverlayVisibility();
        this.opacityListener = (observableValue, number, number2) -> this.updateOverlayOpacity();
    }

    private IOSMediaPlayer getIOSPlayer() {
        return (IOSMediaPlayer)this.getMediaPlayer().retrieveJfxPlayer();
    }

    private boolean determineVisibility() {
        return this.getParent() != null && this.isVisible();
    }

    private synchronized void updateOverlayVisibility() {
        if (this.mediaPlayerReady) {
            this.getIOSPlayer().setOverlayVisible(this.determineVisibility());
        }
    }

    private synchronized void updateOverlayOpacity() {
        if (this.mediaPlayerReady) {
            this.getIOSPlayer().setOverlayOpacity(this.getOpacity());
        }
    }

    private synchronized void updateOverlayX() {
        if (this.mediaPlayerReady) {
            this.getIOSPlayer().setOverlayX(this.getX());
        }
    }

    private synchronized void updateOverlayY() {
        if (this.mediaPlayerReady) {
            this.getIOSPlayer().setOverlayY(this.getY());
        }
    }

    private synchronized void updateOverlayWidth() {
        if (this.mediaPlayerReady) {
            this.getIOSPlayer().setOverlayWidth(this.getFitWidth());
        }
    }

    private synchronized void updateOverlayHeight() {
        if (this.mediaPlayerReady) {
            this.getIOSPlayer().setOverlayHeight(this.getFitHeight());
        }
    }

    private synchronized void updateOverlayPreserveRatio() {
        if (this.mediaPlayerReady) {
            this.getIOSPlayer().setOverlayPreserveRatio(this.isPreserveRatio());
        }
    }

    private static Affine3D calculateNodeToSceneTransform(Node node) {
        Affine3D affine3D = new Affine3D();
        do {
            affine3D.preConcatenate(node.impl_getLeafTransform());
        } while ((node = node.getParent()) != null);
        return affine3D;
    }

    private void updateOverlayTransformDirectly() {
        Affine3D affine3D = MediaView.calculateNodeToSceneTransform(this);
        this.getIOSPlayer().setOverlayTransform(affine3D.getMxx(), affine3D.getMxy(), affine3D.getMxz(), affine3D.getMxt(), affine3D.getMyx(), affine3D.getMyy(), affine3D.getMyz(), affine3D.getMyt(), affine3D.getMzx(), affine3D.getMzy(), affine3D.getMzz(), affine3D.getMzt());
    }

    private synchronized void updateOverlayTransform() {
        if (this.mediaPlayerReady) {
            this.updateOverlayTransformDirectly();
        }
    }

    private void updateIOSOverlay() {
        this.getIOSPlayer().setOverlayX(this.getX());
        this.getIOSPlayer().setOverlayY(this.getY());
        this.getIOSPlayer().setOverlayPreserveRatio(this.isPreserveRatio());
        this.getIOSPlayer().setOverlayWidth(this.getFitWidth());
        this.getIOSPlayer().setOverlayHeight(this.getFitHeight());
        this.getIOSPlayer().setOverlayOpacity(this.getOpacity());
        this.getIOSPlayer().setOverlayVisible(this.determineVisibility());
        this.updateOverlayTransformDirectly();
    }

    @Override
    @Deprecated
    public void impl_transformsChanged() {
        super.impl_transformsChanged();
        if (HostUtils.isIOS()) {
            this.updateOverlayTransform();
        }
    }

    private MediaView getMediaView() {
        return this;
    }

    public MediaView() {
        this.getStyleClass().add(DEFAULT_STYLE_CLASS);
        this.setSmooth(Toolkit.getToolkit().getDefaultImageSmooth());
        this.decodedFrameRateListener = this.createVideoFrameRateListener();
        this.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        if (HostUtils.isIOS()) {
            this.createListeners();
            this.parentProperty().addListener(this.parentListener);
            this.impl_treeVisibleProperty().addListener(this.treeVisibleListener);
            this.opacityProperty().addListener(this.opacityListener);
        }
    }

    public MediaView(MediaPlayer mediaPlayer) {
        this();
        this.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        this.setMediaPlayer(mediaPlayer);
    }

    public final void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayerProperty().set(mediaPlayer);
    }

    public final MediaPlayer getMediaPlayer() {
        return this.mediaPlayer == null ? null : (MediaPlayer)this.mediaPlayer.get();
    }

    public final ObjectProperty<MediaPlayer> mediaPlayerProperty() {
        if (this.mediaPlayer == null) {
            this.mediaPlayer = new ObjectPropertyBase<MediaPlayer>(){
                MediaPlayer oldValue = null;

                @Override
                protected void invalidated() {
                    Object object;
                    if (this.oldValue != null) {
                        object = this.oldValue.getMedia();
                        if (object != null) {
                            ((Media)object).widthProperty().removeListener(MediaView.this.mediaDimensionListener);
                            ((Media)object).heightProperty().removeListener(MediaView.this.mediaDimensionListener);
                        }
                        if (MediaView.this.decodedFrameRateListener != null && MediaView.this.getMediaPlayer().retrieveJfxPlayer() != null) {
                            MediaView.this.getMediaPlayer().retrieveJfxPlayer().getVideoRenderControl().removeVideoFrameRateListener(MediaView.this.decodedFrameRateListener);
                        }
                        this.oldValue.errorProperty().removeListener(MediaView.this.errorListener);
                        this.oldValue.removeView(MediaView.this.getMediaView());
                    }
                    if ((object = (MediaPlayer)this.get()) != null) {
                        ((MediaPlayer)object).addView(MediaView.this.getMediaView());
                        ((MediaPlayer)object).errorProperty().addListener(MediaView.this.errorListener);
                        if (MediaView.this.decodedFrameRateListener != null && MediaView.this.getMediaPlayer().retrieveJfxPlayer() != null) {
                            MediaView.this.getMediaPlayer().retrieveJfxPlayer().getVideoRenderControl().addVideoFrameRateListener(MediaView.this.decodedFrameRateListener);
                        } else if (MediaView.this.decodedFrameRateListener != null) {
                            MediaView.this.registerVideoFrameRateListener = true;
                        }
                        Media media = ((MediaPlayer)object).getMedia();
                        if (media != null) {
                            media.widthProperty().addListener(MediaView.this.mediaDimensionListener);
                            media.heightProperty().addListener(MediaView.this.mediaDimensionListener);
                        }
                    }
                    MediaView.this.impl_markDirty(DirtyBits.MEDIAVIEW_MEDIA);
                    MediaView.this.impl_geomChanged();
                    this.oldValue = object;
                }

                @Override
                public Object getBean() {
                    return MediaView.this;
                }

                @Override
                public String getName() {
                    return "mediaPlayer";
                }
            };
        }
        return this.mediaPlayer;
    }

    public final void setOnError(EventHandler<MediaErrorEvent> eventHandler) {
        this.onErrorProperty().set(eventHandler);
    }

    public final EventHandler<MediaErrorEvent> getOnError() {
        return this.onError == null ? null : (EventHandler)this.onError.get();
    }

    public final ObjectProperty<EventHandler<MediaErrorEvent>> onErrorProperty() {
        if (this.onError == null) {
            this.onError = new ObjectPropertyBase<EventHandler<MediaErrorEvent>>(){

                @Override
                protected void invalidated() {
                    MediaView.this.setEventHandler(MediaErrorEvent.MEDIA_ERROR, (EventHandler)this.get());
                }

                @Override
                public Object getBean() {
                    return MediaView.this;
                }

                @Override
                public String getName() {
                    return "onError";
                }
            };
        }
        return this.onError;
    }

    public final void setPreserveRatio(boolean bl) {
        this.preserveRatioProperty().set(bl);
    }

    public final boolean isPreserveRatio() {
        return this.preserveRatio == null ? true : this.preserveRatio.get();
    }

    public final BooleanProperty preserveRatioProperty() {
        if (this.preserveRatio == null) {
            this.preserveRatio = new BooleanPropertyBase(true){

                @Override
                protected void invalidated() {
                    if (HostUtils.isIOS()) {
                        MediaView.this.updateOverlayPreserveRatio();
                    } else {
                        MediaView.this.impl_markDirty(DirtyBits.NODE_VIEWPORT);
                        MediaView.this.impl_geomChanged();
                    }
                }

                @Override
                public Object getBean() {
                    return MediaView.this;
                }

                @Override
                public String getName() {
                    return "preserveRatio";
                }
            };
        }
        return this.preserveRatio;
    }

    public final void setSmooth(boolean bl) {
        this.smoothProperty().set(bl);
    }

    public final boolean isSmooth() {
        return this.smooth == null ? false : this.smooth.get();
    }

    public final BooleanProperty smoothProperty() {
        if (this.smooth == null) {
            this.smooth = new BooleanPropertyBase(){

                @Override
                protected void invalidated() {
                    MediaView.this.impl_markDirty(DirtyBits.NODE_SMOOTH);
                }

                @Override
                public Object getBean() {
                    return MediaView.this;
                }

                @Override
                public String getName() {
                    return "smooth";
                }
            };
        }
        return this.smooth;
    }

    public final void setX(double d2) {
        this.xProperty().set(d2);
    }

    public final double getX() {
        return this.x == null ? 0.0 : this.x.get();
    }

    public final DoubleProperty xProperty() {
        if (this.x == null) {
            this.x = new DoublePropertyBase(){

                @Override
                protected void invalidated() {
                    if (HostUtils.isIOS()) {
                        MediaView.this.updateOverlayX();
                    } else {
                        MediaView.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                        MediaView.this.impl_geomChanged();
                    }
                }

                @Override
                public Object getBean() {
                    return MediaView.this;
                }

                @Override
                public String getName() {
                    return "x";
                }
            };
        }
        return this.x;
    }

    public final void setY(double d2) {
        this.yProperty().set(d2);
    }

    public final double getY() {
        return this.y == null ? 0.0 : this.y.get();
    }

    public final DoubleProperty yProperty() {
        if (this.y == null) {
            this.y = new DoublePropertyBase(){

                @Override
                protected void invalidated() {
                    if (HostUtils.isIOS()) {
                        MediaView.this.updateOverlayY();
                    } else {
                        MediaView.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                        MediaView.this.impl_geomChanged();
                    }
                }

                @Override
                public Object getBean() {
                    return MediaView.this;
                }

                @Override
                public String getName() {
                    return "y";
                }
            };
        }
        return this.y;
    }

    public final void setFitWidth(double d2) {
        this.fitWidthProperty().set(d2);
    }

    public final double getFitWidth() {
        return this.fitWidth == null ? 0.0 : this.fitWidth.get();
    }

    public final DoubleProperty fitWidthProperty() {
        if (this.fitWidth == null) {
            this.fitWidth = new DoublePropertyBase(){

                @Override
                protected void invalidated() {
                    if (HostUtils.isIOS()) {
                        MediaView.this.updateOverlayWidth();
                    } else {
                        MediaView.this.impl_markDirty(DirtyBits.NODE_VIEWPORT);
                        MediaView.this.impl_geomChanged();
                    }
                }

                @Override
                public Object getBean() {
                    return MediaView.this;
                }

                @Override
                public String getName() {
                    return "fitWidth";
                }
            };
        }
        return this.fitWidth;
    }

    public final void setFitHeight(double d2) {
        this.fitHeightProperty().set(d2);
    }

    public final double getFitHeight() {
        return this.fitHeight == null ? 0.0 : this.fitHeight.get();
    }

    public final DoubleProperty fitHeightProperty() {
        if (this.fitHeight == null) {
            this.fitHeight = new DoublePropertyBase(){

                @Override
                protected void invalidated() {
                    if (HostUtils.isIOS()) {
                        MediaView.this.updateOverlayHeight();
                    } else {
                        MediaView.this.impl_markDirty(DirtyBits.NODE_VIEWPORT);
                        MediaView.this.impl_geomChanged();
                    }
                }

                @Override
                public Object getBean() {
                    return MediaView.this;
                }

                @Override
                public String getName() {
                    return "fitHeight";
                }
            };
        }
        return this.fitHeight;
    }

    public final void setViewport(Rectangle2D rectangle2D) {
        this.viewportProperty().set(rectangle2D);
    }

    public final Rectangle2D getViewport() {
        return this.viewport == null ? null : (Rectangle2D)this.viewport.get();
    }

    public final ObjectProperty<Rectangle2D> viewportProperty() {
        if (this.viewport == null) {
            this.viewport = new ObjectPropertyBase<Rectangle2D>(){

                @Override
                protected void invalidated() {
                    MediaView.this.impl_markDirty(DirtyBits.NODE_VIEWPORT);
                    MediaView.this.impl_geomChanged();
                }

                @Override
                public Object getBean() {
                    return MediaView.this;
                }

                @Override
                public String getName() {
                    return "viewport";
                }
            };
        }
        return this.viewport;
    }

    void notifyMediaChange() {
        MediaPlayer mediaPlayer = this.getMediaPlayer();
        if (mediaPlayer != null) {
            NGMediaView nGMediaView = (NGMediaView)this.impl_getPeer();
            nGMediaView.setMediaProvider(mediaPlayer);
        }
        this.impl_markDirty(DirtyBits.MEDIAVIEW_MEDIA);
        this.impl_geomChanged();
    }

    void notifyMediaSizeChange() {
        this.impl_markDirty(DirtyBits.NODE_VIEWPORT);
        this.impl_geomChanged();
    }

    void notifyMediaFrameUpdated() {
        ++this.decodedFrameCount;
        this.impl_markDirty(DirtyBits.NODE_CONTENTS);
    }

    @Override
    @Deprecated
    protected NGNode impl_createPeer() {
        NGMediaView nGMediaView = new NGMediaView();
        nGMediaView.setFrameTracker(new MediaViewFrameTracker());
        return nGMediaView;
    }

    @Override
    @Deprecated
    public BaseBounds impl_computeGeomBounds(BaseBounds baseBounds, BaseTransform baseTransform) {
        double d2;
        Media media = this.getMediaPlayer() == null ? null : this.getMediaPlayer().getMedia();
        double d3 = media != null ? (double)media.getWidth() : 0.0;
        double d4 = media != null ? (double)media.getHeight() : 0.0;
        double d5 = this.getFitWidth();
        double d6 = this.getFitHeight();
        double d7 = this.getViewport() != null ? this.getViewport().getWidth() : 0.0;
        double d8 = d2 = this.getViewport() != null ? this.getViewport().getHeight() : 0.0;
        if (d7 > 0.0 && d2 > 0.0) {
            d3 = d7;
            d4 = d2;
        }
        if (this.getFitWidth() <= 0.0 && this.getFitHeight() <= 0.0) {
            d5 = d3;
            d6 = d4;
        } else if (this.isPreserveRatio()) {
            if (this.getFitWidth() <= 0.0) {
                d5 = d4 > 0.0 ? d3 * (this.getFitHeight() / d4) : 0.0;
                d6 = this.getFitHeight();
            } else if (this.getFitHeight() <= 0.0) {
                d5 = this.getFitWidth();
                d6 = d3 > 0.0 ? d4 * (this.getFitWidth() / d3) : 0.0;
            } else {
                if (d3 == 0.0) {
                    d3 = this.getFitWidth();
                }
                if (d4 == 0.0) {
                    d4 = this.getFitHeight();
                }
                double d9 = Math.min(this.getFitWidth() / d3, this.getFitHeight() / d4);
                d5 = d3 * d9;
                d6 = d4 * d9;
            }
        } else if (this.getFitHeight() <= 0.0) {
            d6 = d4;
        } else if (this.getFitWidth() <= 0.0) {
            d5 = d3;
        }
        if (d6 < 1.0) {
            d6 = 1.0;
        }
        if (d5 < 1.0) {
            d5 = 1.0;
        }
        d3 = d5;
        d4 = d6;
        if (d3 <= 0.0 || d4 <= 0.0) {
            return baseBounds.makeEmpty();
        }
        baseBounds = baseBounds.deriveWithNewBounds((float)this.getX(), (float)this.getY(), 0.0f, (float)(this.getX() + d3), (float)(this.getY() + d4), 0.0f);
        baseBounds = baseTransform.transform(baseBounds, baseBounds);
        return baseBounds;
    }

    @Override
    @Deprecated
    protected boolean impl_computeContains(double d2, double d3) {
        return true;
    }

    void updateViewport() {
        if (this.getMediaPlayer() == null) {
            return;
        }
        NGMediaView nGMediaView = (NGMediaView)this.impl_getPeer();
        if (this.getViewport() != null) {
            nGMediaView.setViewport((float)this.getFitWidth(), (float)this.getFitHeight(), (float)this.getViewport().getMinX(), (float)this.getViewport().getMinY(), (float)this.getViewport().getWidth(), (float)this.getViewport().getHeight(), this.isPreserveRatio());
        } else {
            nGMediaView.setViewport((float)this.getFitWidth(), (float)this.getFitHeight(), 0.0f, 0.0f, 0.0f, 0.0f, this.isPreserveRatio());
        }
    }

    @Override
    @Deprecated
    public void impl_updatePeer() {
        super.impl_updatePeer();
        NGMediaView nGMediaView = (NGMediaView)this.impl_getPeer();
        if (this.impl_isDirty(DirtyBits.NODE_GEOMETRY)) {
            nGMediaView.setX((float)this.getX());
            nGMediaView.setY((float)this.getY());
        }
        if (this.impl_isDirty(DirtyBits.NODE_SMOOTH)) {
            nGMediaView.setSmooth(this.isSmooth());
        }
        if (this.impl_isDirty(DirtyBits.NODE_VIEWPORT)) {
            this.updateViewport();
        }
        if (this.impl_isDirty(DirtyBits.NODE_CONTENTS)) {
            nGMediaView.renderNextFrame();
        }
        if (this.impl_isDirty(DirtyBits.MEDIAVIEW_MEDIA)) {
            MediaPlayer mediaPlayer = this.getMediaPlayer();
            if (mediaPlayer != null) {
                nGMediaView.setMediaProvider(mediaPlayer);
                this.updateViewport();
            } else {
                nGMediaView.setMediaProvider(null);
            }
        }
    }

    @Deprecated
    public void impl_perfReset() {
        this.decodedFrameCount = 0;
        this.renderedFrameCount = 0;
    }

    @Deprecated
    public int impl_perfGetDecodedFrameCount() {
        return this.decodedFrameCount;
    }

    @Deprecated
    public int impl_perfGetRenderedFrameCount() {
        return this.renderedFrameCount;
    }

    @Override
    @Deprecated
    public Object impl_processMXNode(MXNodeAlgorithm mXNodeAlgorithm, MXNodeAlgorithmContext mXNodeAlgorithmContext) {
        return mXNodeAlgorithm.processLeafNode(this, mXNodeAlgorithmContext);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    void _mediaPlayerOnReady() {
        if (this.decodedFrameRateListener != null && this.getMediaPlayer().retrieveJfxPlayer() != null && this.registerVideoFrameRateListener) {
            this.getMediaPlayer().retrieveJfxPlayer().getVideoRenderControl().addVideoFrameRateListener(this.decodedFrameRateListener);
            this.registerVideoFrameRateListener = false;
        }
        if (HostUtils.isIOS()) {
            MediaView mediaView = this;
            synchronized (mediaView) {
                this.updateIOSOverlay();
                this.mediaPlayerReady = true;
            }
        }
    }

    private class MediaViewFrameTracker
    implements MediaFrameTracker {
        private MediaViewFrameTracker() {
        }

        @Override
        public void incrementDecodedFrameCount(int n2) {
            MediaView.this.decodedFrameCount = MediaView.this.decodedFrameCount + n2;
        }

        @Override
        public void incrementRenderedFrameCount(int n2) {
            MediaView.this.renderedFrameCount = MediaView.this.renderedFrameCount + n2;
        }
    }

    private class MediaErrorInvalidationListener
    implements InvalidationListener {
        private MediaErrorInvalidationListener() {
        }

        @Override
        public void invalidated(Observable observable) {
            ObservableObjectValue observableObjectValue = (ObservableObjectValue)observable;
            MediaView.this.fireEvent(new MediaErrorEvent((Object)MediaView.this.getMediaPlayer(), (EventTarget)MediaView.this.getMediaView(), (MediaException)observableObjectValue.get()));
        }
    }
}

