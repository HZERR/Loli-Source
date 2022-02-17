/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.contrib.blogofbug.swing.components;

import java.awt.Image;
import java.io.File;
import java.net.URL;

public interface RichComponent {
    public void setRichImage(URL var1);

    public void setRichImage(File var1);

    public void setRichImage(Image var1);

    public void prePaintImage();

    public void setRichText(String var1);

    public String getRichText();

    public void setAlpha(float var1);
}

