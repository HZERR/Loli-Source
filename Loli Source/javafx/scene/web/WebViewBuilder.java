/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.web;

import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.ParentBuilder;
import javafx.scene.web.PopupFeatures;
import javafx.scene.web.PromptData;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEngineBuilder;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.util.Builder;
import javafx.util.Callback;

@Deprecated
public final class WebViewBuilder
extends ParentBuilder<WebViewBuilder>
implements Builder<WebView> {
    private double fontScale;
    private boolean fontScaleSet;
    private double maxHeight;
    private boolean maxHeightSet;
    private double maxWidth;
    private boolean maxWidthSet;
    private double minHeight;
    private boolean minHeightSet;
    private double minWidth;
    private boolean minWidthSet;
    private double prefHeight;
    private boolean prefHeightSet;
    private double prefWidth;
    private boolean prefWidthSet;
    private WebEngineBuilder engineBuilder;

    public static WebViewBuilder create() {
        return new WebViewBuilder();
    }

    @Override
    public WebView build() {
        WebView webView = new WebView();
        this.applyTo(webView);
        return webView;
    }

    public void applyTo(WebView webView) {
        super.applyTo(webView);
        if (this.fontScaleSet) {
            webView.setFontScale(this.fontScale);
        }
        if (this.maxHeightSet) {
            webView.setMaxHeight(this.maxHeight);
        }
        if (this.maxWidthSet) {
            webView.setMaxWidth(this.maxWidth);
        }
        if (this.minHeightSet) {
            webView.setMinHeight(this.minHeight);
        }
        if (this.minWidthSet) {
            webView.setMinWidth(this.minWidth);
        }
        if (this.prefHeightSet) {
            webView.setPrefHeight(this.prefHeight);
        }
        if (this.prefWidthSet) {
            webView.setPrefWidth(this.prefWidth);
        }
        if (this.engineBuilder != null) {
            this.engineBuilder.applyTo(webView.getEngine());
        }
    }

    public WebViewBuilder fontScale(double d2) {
        this.fontScale = d2;
        this.fontScaleSet = true;
        return this;
    }

    public WebViewBuilder maxHeight(double d2) {
        this.maxHeight = d2;
        this.maxHeightSet = true;
        return this;
    }

    public WebViewBuilder maxWidth(double d2) {
        this.maxWidth = d2;
        this.maxWidthSet = true;
        return this;
    }

    public WebViewBuilder minHeight(double d2) {
        this.minHeight = d2;
        this.minHeightSet = true;
        return this;
    }

    public WebViewBuilder minWidth(double d2) {
        this.minWidth = d2;
        this.minWidthSet = true;
        return this;
    }

    public WebViewBuilder prefHeight(double d2) {
        this.prefHeight = d2;
        this.prefHeightSet = true;
        return this;
    }

    public WebViewBuilder prefWidth(double d2) {
        this.prefWidth = d2;
        this.prefWidthSet = true;
        return this;
    }

    public WebViewBuilder confirmHandler(Callback<String, Boolean> callback) {
        this.engineBuilder().confirmHandler(callback);
        return this;
    }

    public WebViewBuilder createPopupHandler(Callback<PopupFeatures, WebEngine> callback) {
        this.engineBuilder().createPopupHandler(callback);
        return this;
    }

    public WebViewBuilder onAlert(EventHandler<WebEvent<String>> eventHandler) {
        this.engineBuilder().onAlert(eventHandler);
        return this;
    }

    public WebViewBuilder onResized(EventHandler<WebEvent<Rectangle2D>> eventHandler) {
        this.engineBuilder().onResized(eventHandler);
        return this;
    }

    public WebViewBuilder onStatusChanged(EventHandler<WebEvent<String>> eventHandler) {
        this.engineBuilder().onStatusChanged(eventHandler);
        return this;
    }

    public WebViewBuilder onVisibilityChanged(EventHandler<WebEvent<Boolean>> eventHandler) {
        this.engineBuilder().onVisibilityChanged(eventHandler);
        return this;
    }

    public WebViewBuilder promptHandler(Callback<PromptData, String> callback) {
        this.engineBuilder().promptHandler(callback);
        return this;
    }

    public WebViewBuilder location(String string) {
        this.engineBuilder().location(string);
        return this;
    }

    private WebEngineBuilder engineBuilder() {
        if (this.engineBuilder == null) {
            this.engineBuilder = WebEngineBuilder.create();
        }
        return this.engineBuilder;
    }
}

