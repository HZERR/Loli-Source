/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.media;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.media.Media;
import javafx.scene.media.Track;
import javafx.util.Builder;

@Deprecated
public final class MediaBuilder
implements Builder<Media> {
    private int __set;
    private Runnable onError;
    private String source;
    private Collection<? extends Track> tracks;

    protected MediaBuilder() {
    }

    public static MediaBuilder create() {
        return new MediaBuilder();
    }

    public void applyTo(Media media) {
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            media.setOnError(this.onError);
        }
        if ((n2 & 2) != 0) {
            media.getTracks().addAll(this.tracks);
        }
    }

    public MediaBuilder onError(Runnable runnable) {
        this.onError = runnable;
        this.__set |= 1;
        return this;
    }

    public MediaBuilder source(String string) {
        this.source = string;
        return this;
    }

    public MediaBuilder tracks(Collection<? extends Track> collection) {
        this.tracks = collection;
        this.__set |= 2;
        return this;
    }

    public MediaBuilder tracks(Track ... arrtrack) {
        return this.tracks(Arrays.asList(arrtrack));
    }

    @Override
    public Media build() {
        Media media = new Media(this.source);
        this.applyTo(media);
        return media;
    }
}

