/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.media;

import java.util.Locale;
import java.util.Map;
import javafx.scene.media.Track;

public final class AudioTrack
extends Track {
    public final String getLanguage() {
        Locale locale = this.getLocale();
        return null == locale ? null : locale.getLanguage();
    }

    AudioTrack(long l2, Map<String, Object> map) {
        super(l2, map);
    }
}

