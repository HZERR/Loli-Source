/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.webkit.theme;

import com.sun.javafx.util.Utils;
import com.sun.javafx.webkit.Accessor;
import com.sun.javafx.webkit.theme.RenderThemeImpl;
import com.sun.javafx.webkit.theme.Renderer;
import com.sun.webkit.graphics.Ref;
import com.sun.webkit.graphics.ScrollBarTheme;
import com.sun.webkit.graphics.WCGraphicsContext;
import com.sun.webkit.graphics.WCSize;
import java.lang.ref.WeakReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.Observable;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.ScrollBar;

public final class ScrollBarThemeImpl
extends ScrollBarTheme {
    private static final Logger log = Logger.getLogger(ScrollBarThemeImpl.class.getName());
    private WeakReference<ScrollBar> testSBRef = new WeakReference<Object>(null);
    private boolean thicknessInitialized = false;
    private final Accessor accessor;
    private final RenderThemeImpl.Pool<ScrollBarWidget> pool;

    public ScrollBarThemeImpl(final Accessor accessor) {
        this.accessor = accessor;
        this.pool = new RenderThemeImpl.Pool<ScrollBarWidget>(scrollBarWidget -> accessor.removeChild((Node)scrollBarWidget), ScrollBarWidget.class);
        accessor.addViewListener(new RenderThemeImpl.ViewListener(this.pool, accessor){

            @Override
            public void invalidated(Observable observable) {
                super.invalidated(observable);
                ScrollBarWidget scrollBarWidget = new ScrollBarWidget();
                accessor.addChild(scrollBarWidget);
                ScrollBarThemeImpl.this.testSBRef = new WeakReference<ScrollBarWidget>(scrollBarWidget);
            }
        });
    }

    private static Orientation convertOrientation(int n2) {
        return n2 == 1 ? Orientation.VERTICAL : Orientation.HORIZONTAL;
    }

    private void adjustScrollBar(ScrollBar scrollBar, int n2, int n3, int n4) {
        Orientation orientation = ScrollBarThemeImpl.convertOrientation(n4);
        if (orientation != scrollBar.getOrientation()) {
            scrollBar.setOrientation(orientation);
        }
        if (orientation == Orientation.VERTICAL) {
            n2 = ScrollBarTheme.getThickness();
        } else {
            n3 = ScrollBarTheme.getThickness();
        }
        if ((double)n2 != scrollBar.getWidth() || (double)n3 != scrollBar.getHeight()) {
            scrollBar.resize(n2, n3);
        }
    }

    private void adjustScrollBar(ScrollBar scrollBar, int n2, int n3, int n4, int n5, int n6, int n7) {
        this.adjustScrollBar(scrollBar, n2, n3, n4);
        boolean bl = n7 <= n6;
        scrollBar.setDisable(bl);
        if (bl) {
            return;
        }
        if (n5 < 0) {
            n5 = 0;
        } else if (n5 > n7 - n6) {
            n5 = n7 - n6;
        }
        if (scrollBar.getMax() != (double)n7 || scrollBar.getVisibleAmount() != (double)n6) {
            scrollBar.setValue(0.0);
            scrollBar.setMax(n7);
            scrollBar.setVisibleAmount(n6);
        }
        if (n7 > n6) {
            float f2 = (float)n7 / (float)(n7 - n6);
            if (scrollBar.getValue() != (double)((float)n5 * f2)) {
                scrollBar.setValue((float)n5 * f2);
            }
        }
    }

    @Override
    protected Ref createWidget(long l2, int n2, int n3, int n4, int n5, int n6, int n7) {
        ScrollBarWidget scrollBarWidget = this.pool.get(l2);
        if (scrollBarWidget == null) {
            scrollBarWidget = new ScrollBarWidget();
            this.pool.put(l2, scrollBarWidget, this.accessor.getPage().getUpdateContentCycleID());
            this.accessor.addChild(scrollBarWidget);
        }
        this.adjustScrollBar(scrollBarWidget, n2, n3, n4, n5, n6, n7);
        return new ScrollBarRef(scrollBarWidget);
    }

    @Override
    public void paint(WCGraphicsContext wCGraphicsContext, Ref ref, int n2, int n3, int n4, int n5) {
        ScrollBar scrollBar = (ScrollBar)((ScrollBarRef)ref).asControl();
        if (scrollBar == null) {
            return;
        }
        if (log.isLoggable(Level.FINEST)) {
            log.log(Level.FINEST, "[{0}, {1} {2}x{3}], {4}", new Object[]{n2, n3, scrollBar.getWidth(), scrollBar.getHeight(), scrollBar.getOrientation() == Orientation.VERTICAL ? "VERTICAL" : "HORIZONTAL"});
        }
        wCGraphicsContext.saveState();
        wCGraphicsContext.translate(n2, n3);
        Renderer.getRenderer().render(scrollBar, wCGraphicsContext);
        wCGraphicsContext.restoreState();
    }

    @Override
    public WCSize getWidgetSize(Ref ref) {
        ScrollBar scrollBar = (ScrollBar)((ScrollBarRef)ref).asControl();
        if (scrollBar != null) {
            return new WCSize((float)scrollBar.getWidth(), (float)scrollBar.getHeight());
        }
        return new WCSize(0.0f, 0.0f);
    }

    @Override
    protected int hitTest(int n2, int n3, int n4, int n5, int n6, int n7, int n8, int n9) {
        int n10;
        int n11;
        int n12;
        int n13;
        int n14;
        int n15;
        ScrollBar scrollBar;
        if (log.isLoggable(Level.FINEST)) {
            log.log(Level.FINEST, "[{0}, {1} {2}x{3}], {4}", new Object[]{n8, n9, n2, n3, n4 == 1 ? "VERTICAL" : "HORIZONTAL"});
        }
        if ((scrollBar = (ScrollBar)this.testSBRef.get()) == null) {
            return 0;
        }
        Node node = ScrollBarThemeImpl.getThumb(scrollBar);
        Node node2 = ScrollBarThemeImpl.getTrack(scrollBar);
        Node node3 = ScrollBarThemeImpl.getDecButton(scrollBar);
        Node node4 = ScrollBarThemeImpl.getIncButton(scrollBar);
        this.adjustScrollBar(scrollBar, n2, n3, n4, n5, n6, n7);
        if (n4 == 1) {
            n14 = n15 = n8;
            n13 = n15;
            n12 = n9 - (int)node3.getLayoutBounds().getHeight();
            n11 = n12 - this.thumbPosition();
            n10 = n12 - (int)node2.getLayoutBounds().getHeight();
        } else {
            n10 = n11 = n9;
            n12 = n11;
            n13 = n8 - (int)node3.getLayoutBounds().getWidth();
            n15 = n13 - this.thumbPosition();
            n14 = n13 - (int)node2.getLayoutBounds().getWidth();
        }
        if (node != null && node.isVisible() && node.contains(n15, n11)) {
            log.finer("thumb");
            return 8;
        }
        if (node2 != null && node2.isVisible() && node2.contains(n13, n12)) {
            if (n4 == 1 && this.thumbPosition() >= n12 || n4 == 0 && this.thumbPosition() >= n13) {
                log.finer("back track");
                return 4;
            }
            if (n4 == 1 && this.thumbPosition() < n12 || n4 == 0 && this.thumbPosition() < n13) {
                log.finer("forward track");
                return 16;
            }
        } else {
            if (node3 != null && node3.isVisible() && node3.contains(n8, n9)) {
                log.finer("back button");
                return 1;
            }
            if (node4 != null && node4.isVisible() && node4.contains(n14, n10)) {
                log.finer("forward button");
                return 2;
            }
        }
        log.finer("no part");
        return 0;
    }

    private int thumbPosition() {
        ScrollBar scrollBar = (ScrollBar)this.testSBRef.get();
        if (scrollBar == null) {
            return 0;
        }
        Node node = ScrollBarThemeImpl.getThumb(scrollBar);
        if (node == null) {
            return 0;
        }
        double d2 = scrollBar.getOrientation() == Orientation.VERTICAL ? node.getLayoutBounds().getHeight() : node.getLayoutBounds().getWidth();
        Node node2 = ScrollBarThemeImpl.getTrack(scrollBar);
        double d3 = scrollBar.getOrientation() == Orientation.VERTICAL ? node2.getLayoutBounds().getHeight() : node2.getLayoutBounds().getWidth();
        double d4 = Utils.clamp(scrollBar.getMin(), scrollBar.getValue(), scrollBar.getMax());
        double d5 = scrollBar.getMax() - scrollBar.getMin();
        return (int)Math.round(d5 > 0.0 ? (d3 - d2) * (d4 - scrollBar.getMin()) / d5 : 0.0);
    }

    @Override
    protected int getThumbLength(int n2, int n3, int n4, int n5, int n6, int n7) {
        ScrollBar scrollBar = (ScrollBar)this.testSBRef.get();
        if (scrollBar == null) {
            return 0;
        }
        Node node = ScrollBarThemeImpl.getThumb(scrollBar);
        if (node == null) {
            return 0;
        }
        this.adjustScrollBar(scrollBar, n2, n3, n4, n5, n6, n7);
        double d2 = 0.0;
        d2 = n4 == 1 ? node.getLayoutBounds().getHeight() : node.getLayoutBounds().getWidth();
        log.log(Level.FINEST, "thumb length: {0}", d2);
        return (int)d2;
    }

    @Override
    protected int getTrackPosition(int n2, int n3, int n4) {
        ScrollBar scrollBar = (ScrollBar)this.testSBRef.get();
        if (scrollBar == null) {
            return 0;
        }
        Node node = ScrollBarThemeImpl.getDecButton(scrollBar);
        if (node == null) {
            return 0;
        }
        this.adjustScrollBar(scrollBar, n2, n3, n4);
        double d2 = 0.0;
        d2 = n4 == 1 ? node.getLayoutBounds().getHeight() : node.getLayoutBounds().getWidth();
        log.log(Level.FINEST, "track position: {0}", d2);
        return (int)d2;
    }

    @Override
    protected int getTrackLength(int n2, int n3, int n4) {
        ScrollBar scrollBar = (ScrollBar)this.testSBRef.get();
        if (scrollBar == null) {
            return 0;
        }
        Node node = ScrollBarThemeImpl.getTrack(scrollBar);
        if (node == null) {
            return 0;
        }
        this.adjustScrollBar(scrollBar, n2, n3, n4);
        double d2 = 0.0;
        d2 = n4 == 1 ? node.getLayoutBounds().getHeight() : node.getLayoutBounds().getWidth();
        log.log(Level.FINEST, "track length: {0}", d2);
        return (int)d2;
    }

    @Override
    protected int getThumbPosition(int n2, int n3, int n4, int n5, int n6, int n7) {
        ScrollBar scrollBar = (ScrollBar)this.testSBRef.get();
        if (scrollBar == null) {
            return 0;
        }
        this.adjustScrollBar(scrollBar, n2, n3, n4, n5, n6, n7);
        int n8 = this.thumbPosition();
        log.log(Level.FINEST, "thumb position: {0}", n8);
        return n8;
    }

    private void initializeThickness() {
        if (!this.thicknessInitialized) {
            ScrollBar scrollBar = (ScrollBar)this.testSBRef.get();
            if (scrollBar == null) {
                return;
            }
            int n2 = (int)scrollBar.prefWidth(-1.0);
            if (n2 != 0 && ScrollBarTheme.getThickness() != n2) {
                ScrollBarTheme.setThickness(n2);
            }
            this.thicknessInitialized = true;
        }
    }

    private static Node getThumb(ScrollBar scrollBar) {
        return ScrollBarThemeImpl.findNode(scrollBar, "thumb");
    }

    private static Node getTrack(ScrollBar scrollBar) {
        return ScrollBarThemeImpl.findNode(scrollBar, "track");
    }

    private static Node getIncButton(ScrollBar scrollBar) {
        return ScrollBarThemeImpl.findNode(scrollBar, "increment-button");
    }

    private static Node getDecButton(ScrollBar scrollBar) {
        return ScrollBarThemeImpl.findNode(scrollBar, "decrement-button");
    }

    private static Node findNode(ScrollBar scrollBar, String string) {
        for (Node node : scrollBar.getChildrenUnmodifiable()) {
            if (!node.getStyleClass().contains(string)) continue;
            return node;
        }
        return null;
    }

    private static final class ScrollBarRef
    extends Ref {
        private final WeakReference<ScrollBarWidget> sbRef;

        private ScrollBarRef(ScrollBarWidget scrollBarWidget) {
            this.sbRef = new WeakReference<ScrollBarWidget>(scrollBarWidget);
        }

        private Control asControl() {
            return (Control)this.sbRef.get();
        }
    }

    private final class ScrollBarWidget
    extends ScrollBar
    implements RenderThemeImpl.Widget {
        private ScrollBarWidget() {
            this.setOrientation(Orientation.VERTICAL);
            this.setMin(0.0);
            this.setManaged(false);
        }

        @Override
        public void impl_updatePeer() {
            super.impl_updatePeer();
            ScrollBarThemeImpl.this.initializeThickness();
        }

        @Override
        public RenderThemeImpl.WidgetType getType() {
            return RenderThemeImpl.WidgetType.SCROLLBAR;
        }

        @Override
        protected void layoutChildren() {
            super.layoutChildren();
            ScrollBarThemeImpl.this.initializeThickness();
        }
    }
}

