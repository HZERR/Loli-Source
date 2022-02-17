/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.media;

import java.util.Map;
import javafx.scene.media.Track;

public final class VideoTrack
extends Track {
    private int width;
    private int height;

    public final int getWidth() {
        return this.width;
    }

    public final int getHeight() {
        return this.height;
    }

    VideoTrack(long l2, Map<String, Object> map) {
        super(l2, map);
        Object object = map.get("video width");
        if (null != object && object instanceof Number) {
            this.width = ((Number)object).intValue();
        }
        if (null != (object = map.get("video height")) && object instanceof Number) {
            this.height = ((Number)object).intValue();
        }
    }
}

