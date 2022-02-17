/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit;

import com.sun.javafx.scene.control.skin.CustomColorDialog;
import com.sun.webkit.WebPage;
import com.sun.webkit.WebPageClient;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;

public final class ColorChooser {
    private static final double COLOR_DOUBLE_TO_UCHAR_FACTOR = 255.0;
    private CustomColorDialog colorChooserDialog;
    private final long pdata;

    private ColorChooser(WebPage webPage, Color color, long l2) {
        this.pdata = l2;
        WebPageClient webPageClient = webPage.getPageClient();
        this.colorChooserDialog = new CustomColorDialog(((WebView)webPageClient.getContainer()).getScene().getWindow());
        this.colorChooserDialog.setSaveBtnToOk();
        this.colorChooserDialog.setShowUseBtn(false);
        this.colorChooserDialog.setShowOpacitySlider(false);
        this.colorChooserDialog.setOnSave(() -> this.twkSetSelectedColor(this.pdata, (int)Math.round(this.colorChooserDialog.getCustomColor().getRed() * 255.0), (int)Math.round(this.colorChooserDialog.getCustomColor().getGreen() * 255.0), (int)Math.round(this.colorChooserDialog.getCustomColor().getBlue() * 255.0)));
        this.colorChooserDialog.setCurrentColor(color);
        this.colorChooserDialog.show();
    }

    private static ColorChooser fwkCreateAndShowColorChooser(WebPage webPage, int n2, int n3, int n4, long l2) {
        return new ColorChooser(webPage, Color.rgb(n2, n3, n4), l2);
    }

    private void fwkShowColorChooser(int n2, int n3, int n4) {
        this.colorChooserDialog.setCurrentColor(Color.rgb(n2, n3, n4));
        this.colorChooserDialog.show();
    }

    private void fwkHideColorChooser() {
        this.colorChooserDialog.hide();
    }

    private native void twkSetSelectedColor(long var1, int var3, int var4, int var5);
}

