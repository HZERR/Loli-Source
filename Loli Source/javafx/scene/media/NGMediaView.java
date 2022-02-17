/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.media;

import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.media.PrismMediaFrameHandler;
import com.sun.javafx.sg.prism.MediaFrameTracker;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.media.jfxmedia.control.VideoDataBuffer;
import com.sun.prism.Graphics;
import com.sun.prism.Texture;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

class NGMediaView
extends NGNode {
    private boolean smooth = true;
    private final RectBounds dimension = new RectBounds();
    private final RectBounds viewport = new RectBounds();
    private PrismMediaFrameHandler handler;
    private MediaPlayer player;
    private MediaFrameTracker frameTracker;

    NGMediaView() {
    }

    public void renderNextFrame() {
        this.visualsChanged();
    }

    public boolean isSmooth() {
        return this.smooth;
    }

    public void setSmooth(boolean bl) {
        if (bl != this.smooth) {
            this.smooth = bl;
            this.visualsChanged();
        }
    }

    public void setX(float f2) {
        if (f2 != this.dimension.getMinX()) {
            float f3 = this.dimension.getWidth();
            this.dimension.setMinX(f2);
            this.dimension.setMaxX(f2 + f3);
            this.geometryChanged();
        }
    }

    public void setY(float f2) {
        if (f2 != this.dimension.getMinY()) {
            float f3 = this.dimension.getHeight();
            this.dimension.setMinY(f2);
            this.dimension.setMaxY(f2 + f3);
            this.geometryChanged();
        }
    }

    public void setMediaProvider(Object object) {
        if (object == null) {
            this.player = null;
            this.handler = null;
            this.geometryChanged();
        } else if (object instanceof MediaPlayer) {
            this.player = (MediaPlayer)object;
            this.handler = PrismMediaFrameHandler.getHandler(this.player);
            this.geometryChanged();
        }
    }

    public void setViewport(float f2, float f3, float f4, float f5, float f6, float f7, boolean bl) {
        float f8 = 0.0f;
        float f9 = 0.0f;
        float f10 = f2;
        float f11 = f3;
        if (null != this.player) {
            Media media = this.player.getMedia();
            f8 = media.getWidth();
            f9 = media.getHeight();
        }
        if (f6 > 0.0f && f7 > 0.0f) {
            this.viewport.setBounds(f4, f5, f4 + f6, f5 + f7);
            f8 = f6;
            f9 = f7;
        } else {
            this.viewport.setBounds(0.0f, 0.0f, f8, f9);
        }
        if (f2 <= 0.0f && f3 <= 0.0f) {
            f10 = f8;
            f11 = f9;
        } else if (bl) {
            if ((double)f2 <= 0.0) {
                f10 = f9 > 0.0f ? f8 * (f3 / f9) : 0.0f;
                f11 = f3;
            } else if ((double)f3 <= 0.0) {
                f10 = f2;
                f11 = f8 > 0.0f ? f9 * (f2 / f8) : 0.0f;
            } else {
                if (f8 == 0.0f) {
                    f8 = f2;
                }
                if (f9 == 0.0f) {
                    f9 = f3;
                }
                float f12 = Math.min(f2 / f8, f3 / f9);
                f10 = f8 * f12;
                f11 = f9 * f12;
            }
        } else if ((double)f3 <= 0.0) {
            f11 = f9;
        } else if ((double)f2 <= 0.0) {
            f10 = f8;
        }
        if (f11 < 1.0f) {
            f11 = 1.0f;
        }
        if (f10 < 1.0f) {
            f10 = 1.0f;
        }
        this.dimension.setMaxX(this.dimension.getMinX() + f10);
        this.dimension.setMaxY(this.dimension.getMinY() + f11);
        this.geometryChanged();
    }

    @Override
    protected void renderContent(Graphics graphics) {
        if (null == this.handler || null == this.player) {
            return;
        }
        VideoDataBuffer videoDataBuffer = this.player.impl_getLatestFrame();
        if (null == videoDataBuffer) {
            return;
        }
        Texture texture = this.handler.getTexture(graphics, videoDataBuffer);
        if (texture != null) {
            float f2;
            float f3;
            float f4 = this.viewport.getWidth();
            float f5 = this.viewport.getHeight();
            boolean bl = !this.dimension.isEmpty();
            boolean bl2 = bl && (f4 != this.dimension.getWidth() || f5 != this.dimension.getHeight());
            graphics.translate(this.dimension.getMinX(), this.dimension.getMinY());
            if (bl2 && f4 != 0.0f && f5 != 0.0f) {
                f3 = this.dimension.getWidth() / f4;
                f2 = this.dimension.getHeight() / f5;
                graphics.scale(f3, f2);
            }
            f3 = this.viewport.getMinX();
            f2 = this.viewport.getMinY();
            float f6 = f3 + f4;
            float f7 = f2 + f5;
            graphics.drawTexture(texture, 0.0f, 0.0f, f4, f5, f3, f2, f6, f7);
            texture.unlock();
            if (null != this.frameTracker) {
                this.frameTracker.incrementRenderedFrameCount(1);
            }
        }
        videoDataBuffer.releaseFrame();
    }

    @Override
    protected boolean hasOverlappingContents() {
        return false;
    }

    public void setFrameTracker(MediaFrameTracker mediaFrameTracker) {
        this.frameTracker = mediaFrameTracker;
    }
}

