/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.media;

import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.NodeBuilder;
import javafx.scene.media.MediaErrorEvent;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Builder;

@Deprecated
public class MediaViewBuilder<B extends MediaViewBuilder<B>>
extends NodeBuilder<B>
implements Builder<MediaView> {
    private int __set;
    private double fitHeight;
    private double fitWidth;
    private MediaPlayer mediaPlayer;
    private EventHandler<MediaErrorEvent> onError;
    private boolean preserveRatio;
    private boolean smooth;
    private Rectangle2D viewport;
    private double x;
    private double y;

    protected MediaViewBuilder() {
    }

    public static MediaViewBuilder<?> create() {
        return new MediaViewBuilder();
    }

    private void __set(int n2) {
        this.__set |= 1 << n2;
    }

    public void applyTo(MediaView mediaView) {
        super.applyTo(mediaView);
        int n2 = this.__set;
        while (n2 != 0) {
            int n3 = Integer.numberOfTrailingZeros(n2);
            n2 &= ~(1 << n3);
            switch (n3) {
                case 0: {
                    mediaView.setFitHeight(this.fitHeight);
                    break;
                }
                case 1: {
                    mediaView.setFitWidth(this.fitWidth);
                    break;
                }
                case 2: {
                    mediaView.setMediaPlayer(this.mediaPlayer);
                    break;
                }
                case 3: {
                    mediaView.setOnError(this.onError);
                    break;
                }
                case 4: {
                    mediaView.setPreserveRatio(this.preserveRatio);
                    break;
                }
                case 5: {
                    mediaView.setSmooth(this.smooth);
                    break;
                }
                case 6: {
                    mediaView.setViewport(this.viewport);
                    break;
                }
                case 7: {
                    mediaView.setX(this.x);
                    break;
                }
                case 8: {
                    mediaView.setY(this.y);
                }
            }
        }
    }

    public B fitHeight(double d2) {
        this.fitHeight = d2;
        this.__set(0);
        return (B)this;
    }

    public B fitWidth(double d2) {
        this.fitWidth = d2;
        this.__set(1);
        return (B)this;
    }

    public B mediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
        this.__set(2);
        return (B)this;
    }

    public B onError(EventHandler<MediaErrorEvent> eventHandler) {
        this.onError = eventHandler;
        this.__set(3);
        return (B)this;
    }

    public B preserveRatio(boolean bl) {
        this.preserveRatio = bl;
        this.__set(4);
        return (B)this;
    }

    public B smooth(boolean bl) {
        this.smooth = bl;
        this.__set(5);
        return (B)this;
    }

    public B viewport(Rectangle2D rectangle2D) {
        this.viewport = rectangle2D;
        this.__set(6);
        return (B)this;
    }

    public B x(double d2) {
        this.x = d2;
        this.__set(7);
        return (B)this;
    }

    public B y(double d2) {
        this.y = d2;
        this.__set(8);
        return (B)this;
    }

    @Override
    public MediaView build() {
        MediaView mediaView = new MediaView();
        this.applyTo(mediaView);
        return mediaView;
    }
}

