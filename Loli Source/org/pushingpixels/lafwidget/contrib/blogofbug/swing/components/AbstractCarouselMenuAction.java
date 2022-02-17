/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.contrib.blogofbug.swing.components;

import java.net.URL;
import javax.swing.AbstractAction;

public abstract class AbstractCarouselMenuAction
extends AbstractAction {
    public static final String ACTION_IMAGE_URL = "actionImageURL";

    public AbstractCarouselMenuAction(URL image, String label) {
        this.putValue(ACTION_IMAGE_URL, image);
        this.putValue("ShortDescription", label);
    }
}

