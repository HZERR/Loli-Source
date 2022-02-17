/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.webkit;

import com.sun.javafx.scene.input.ExtendedInputMethodRequests;
import com.sun.webkit.InputMethodClient;
import com.sun.webkit.WebPage;
import com.sun.webkit.event.WCInputMethodEvent;
import com.sun.webkit.graphics.WCPoint;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import javafx.geometry.Point2D;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.InputMethodHighlight;
import javafx.scene.input.InputMethodTextRun;
import javafx.scene.web.WebView;

public final class InputMethodClientImpl
implements InputMethodClient,
ExtendedInputMethodRequests {
    private final WeakReference<WebView> wvRef;
    private final WebPage webPage;
    private boolean state;

    public InputMethodClientImpl(WebView webView, WebPage webPage) {
        this.wvRef = new WeakReference<WebView>(webView);
        this.webPage = webPage;
        if (webPage != null) {
            webPage.setInputMethodClient(this);
        }
    }

    @Override
    public void activateInputMethods(boolean bl) {
        WebView webView = (WebView)this.wvRef.get();
        if (webView != null && webView.getScene() != null) {
            webView.getScene().impl_enableInputMethodEvents(bl);
        }
        this.state = bl;
    }

    public boolean getInputMethodState() {
        return this.state;
    }

    public static WCInputMethodEvent convertToWCInputMethodEvent(InputMethodEvent inputMethodEvent) {
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        StringBuilder stringBuilder = new StringBuilder();
        int n2 = 0;
        for (InputMethodTextRun object2 : inputMethodEvent.getComposed()) {
            String i2 = object2.getText();
            InputMethodHighlight inputMethodHighlight = object2.getHighlight();
            arrayList.add(n2);
            arrayList.add(n2 + i2.length());
            arrayList.add(inputMethodHighlight == InputMethodHighlight.SELECTED_CONVERTED || inputMethodHighlight == InputMethodHighlight.SELECTED_RAW ? 1 : 0);
            n2 += i2.length();
            stringBuilder.append(i2);
        }
        int n3 = arrayList.size();
        if (n3 == 0) {
            arrayList.add(0);
            arrayList.add(n2);
            arrayList.add(0);
            n3 = arrayList.size();
        }
        int[] arrn = new int[n3];
        for (int i2 = 0; i2 < n3; ++i2) {
            arrn[i2] = (Integer)arrayList.get(i2);
        }
        return new WCInputMethodEvent(inputMethodEvent.getCommitted(), stringBuilder.toString(), arrn, inputMethodEvent.getCaretPosition());
    }

    @Override
    public Point2D getTextLocation(int n2) {
        int[] arrn = this.webPage.getClientTextLocation(n2);
        WCPoint wCPoint = this.webPage.getPageClient().windowToScreen(new WCPoint(arrn[0], arrn[1] + arrn[3]));
        return new Point2D(wCPoint.getIntX(), wCPoint.getIntY());
    }

    @Override
    public int getLocationOffset(int n2, int n3) {
        WCPoint wCPoint = this.webPage.getPageClient().windowToScreen(new WCPoint(0.0f, 0.0f));
        return this.webPage.getClientLocationOffset(n2 - wCPoint.getIntX(), n3 - wCPoint.getIntY());
    }

    @Override
    public void cancelLatestCommittedText() {
    }

    @Override
    public String getSelectedText() {
        return this.webPage.getClientSelectedText();
    }

    @Override
    public int getInsertPositionOffset() {
        return this.webPage.getClientInsertPositionOffset();
    }

    @Override
    public String getCommittedText(int n2, int n3) {
        try {
            return this.webPage.getClientCommittedText().substring(n2, n3);
        }
        catch (StringIndexOutOfBoundsException stringIndexOutOfBoundsException) {
            throw new IllegalArgumentException(stringIndexOutOfBoundsException);
        }
    }

    @Override
    public int getCommittedTextLength() {
        return this.webPage.getClientCommittedTextLength();
    }
}

