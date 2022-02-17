/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.webkit;

import com.sun.javafx.tk.Toolkit;
import com.sun.javafx.webkit.Accessor;
import com.sun.webkit.UIClient;
import com.sun.webkit.WebPage;
import com.sun.webkit.graphics.WCImage;
import com.sun.webkit.graphics.WCRectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.web.PopupFeatures;
import javafx.scene.web.PromptData;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javax.imageio.ImageIO;

public final class UIClientImpl
implements UIClient {
    private final Accessor accessor;
    private FileChooser chooser;
    private static final Map<String, FileExtensionInfo> fileExtensionMap = new HashMap<String, FileExtensionInfo>();
    private ClipboardContent content;
    private static final DataFormat DF_DRAG_IMAGE;
    private static final DataFormat DF_DRAG_IMAGE_OFFSET;

    public UIClientImpl(Accessor accessor) {
        this.accessor = accessor;
    }

    private WebEngine getWebEngine() {
        return this.accessor.getEngine();
    }

    private AccessControlContext getAccessContext() {
        return this.accessor.getPage().getAccessControlContext();
    }

    @Override
    public WebPage createPage(boolean bl, boolean bl2, boolean bl3, boolean bl4) {
        WebEngine webEngine = this.getWebEngine();
        if (webEngine != null && webEngine.getCreatePopupHandler() != null) {
            PopupFeatures popupFeatures = new PopupFeatures(bl, bl2, bl3, bl4);
            WebEngine webEngine2 = AccessController.doPrivileged(() -> webEngine.getCreatePopupHandler().call(popupFeatures), this.getAccessContext());
            return Accessor.getPageFor(webEngine2);
        }
        return null;
    }

    private void dispatchWebEvent(EventHandler eventHandler, WebEvent webEvent) {
        AccessController.doPrivileged(() -> {
            eventHandler.handle(webEvent);
            return null;
        }, this.getAccessContext());
    }

    private void notifyVisibilityChanged(boolean bl) {
        WebEngine webEngine = this.getWebEngine();
        if (webEngine != null && webEngine.getOnVisibilityChanged() != null) {
            this.dispatchWebEvent(webEngine.getOnVisibilityChanged(), new WebEvent<Boolean>((Object)webEngine, WebEvent.VISIBILITY_CHANGED, bl));
        }
    }

    @Override
    public void closePage() {
        this.notifyVisibilityChanged(false);
    }

    @Override
    public void showView() {
        this.notifyVisibilityChanged(true);
    }

    @Override
    public WCRectangle getViewBounds() {
        WebView webView = this.accessor.getView();
        Window window = null;
        if (webView != null && webView.getScene() != null && (window = webView.getScene().getWindow()) != null) {
            return new WCRectangle((float)window.getX(), (float)window.getY(), (float)window.getWidth(), (float)window.getHeight());
        }
        return null;
    }

    @Override
    public void setViewBounds(WCRectangle wCRectangle) {
        WebEngine webEngine = this.getWebEngine();
        if (webEngine != null && webEngine.getOnResized() != null) {
            this.dispatchWebEvent(webEngine.getOnResized(), new WebEvent<Rectangle2D>((Object)webEngine, WebEvent.RESIZED, new Rectangle2D(wCRectangle.getX(), wCRectangle.getY(), wCRectangle.getWidth(), wCRectangle.getHeight())));
        }
    }

    @Override
    public void setStatusbarText(String string) {
        WebEngine webEngine = this.getWebEngine();
        if (webEngine != null && webEngine.getOnStatusChanged() != null) {
            this.dispatchWebEvent(webEngine.getOnStatusChanged(), new WebEvent<String>((Object)webEngine, WebEvent.STATUS_CHANGED, string));
        }
    }

    @Override
    public void alert(String string) {
        WebEngine webEngine = this.getWebEngine();
        if (webEngine != null && webEngine.getOnAlert() != null) {
            this.dispatchWebEvent(webEngine.getOnAlert(), new WebEvent<String>((Object)webEngine, WebEvent.ALERT, string));
        }
    }

    @Override
    public boolean confirm(String string) {
        WebEngine webEngine = this.getWebEngine();
        if (webEngine != null && webEngine.getConfirmHandler() != null) {
            return AccessController.doPrivileged(() -> webEngine.getConfirmHandler().call(string), this.getAccessContext());
        }
        return false;
    }

    @Override
    public String prompt(String string, String string2) {
        WebEngine webEngine = this.getWebEngine();
        if (webEngine != null && webEngine.getPromptHandler() != null) {
            PromptData promptData = new PromptData(string, string2);
            return AccessController.doPrivileged(() -> webEngine.getPromptHandler().call(promptData), this.getAccessContext());
        }
        return "";
    }

    @Override
    public String[] chooseFile(String string, boolean bl, String string2) {
        String[] arrstring;
        Object object;
        Window window = null;
        WebView webView = this.accessor.getView();
        if (webView != null && webView.getScene() != null) {
            window = webView.getScene().getWindow();
        }
        if (this.chooser == null) {
            this.chooser = new FileChooser();
        }
        this.chooser.getExtensionFilters().clear();
        if (string2 != null && !string2.isEmpty()) {
            this.addMimeFilters(this.chooser, string2);
        }
        this.chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.*"));
        if (string != null) {
            for (object = new File(string); object != null && !((File)object).isDirectory(); object = ((File)object).getParentFile()) {
            }
            this.chooser.setInitialDirectory((File)object);
        }
        if (bl) {
            object = this.chooser.showOpenMultipleDialog(window);
            if (object != null) {
                int n2 = object.size();
                String[] arrstring2 = new String[n2];
                for (int i2 = 0; i2 < n2; ++i2) {
                    arrstring2[i2] = ((File)object.get(i2)).getAbsolutePath();
                }
                return arrstring2;
            }
            return null;
        }
        object = this.chooser.showOpenDialog(window);
        if (object != null) {
            String[] arrstring3 = new String[1];
            arrstring = arrstring3;
            arrstring3[0] = ((File)object).getAbsolutePath();
        } else {
            arrstring = null;
        }
        return arrstring;
    }

    private void addSpecificFilters(FileChooser fileChooser, String string) {
        if (string.contains("/")) {
            FileChooser.ExtensionFilter extensionFilter;
            String[] arrstring = string.split("/");
            String string2 = arrstring[0];
            String string3 = arrstring[1];
            FileExtensionInfo fileExtensionInfo = fileExtensionMap.get(string2);
            if (fileExtensionInfo != null && (extensionFilter = fileExtensionInfo.getExtensionFilter(string3)) != null) {
                fileChooser.getExtensionFilters().addAll(extensionFilter);
            }
        }
    }

    private void addMimeFilters(FileChooser fileChooser, String string) {
        if (string.contains(",")) {
            String[] arrstring;
            for (String string2 : arrstring = string.split(",")) {
                this.addSpecificFilters(fileChooser, string2);
            }
        } else {
            this.addSpecificFilters(fileChooser, string);
        }
    }

    @Override
    public void print() {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static DataFormat getDataFormat(String string) {
        Class<DataFormat> class_ = DataFormat.class;
        synchronized (DataFormat.class) {
            DataFormat dataFormat = DataFormat.lookupMimeType(string);
            if (dataFormat == null) {
                dataFormat = new DataFormat(string);
            }
            // ** MonitorExit[var1_1] (shouldn't be in output)
            return dataFormat;
        }
    }

    @Override
    public void startDrag(WCImage wCImage, int n2, int n3, int n4, int n5, String[] arrstring, Object[] arrobject) {
        this.content = new ClipboardContent();
        for (int i2 = 0; i2 < arrstring.length; ++i2) {
            if (arrobject[i2] == null) continue;
            try {
                this.content.put(UIClientImpl.getDataFormat(arrstring[i2]), "text/ie-shortcut-filename".equals(arrstring[i2]) ? ByteBuffer.wrap(((String)arrobject[i2]).getBytes("UTF-16LE")) : arrobject[i2]);
                continue;
            }
            catch (UnsupportedEncodingException unsupportedEncodingException) {
                // empty catch block
            }
        }
        if (wCImage != null) {
            Object object;
            ByteBuffer byteBuffer = ByteBuffer.allocate(8);
            byteBuffer.rewind();
            byteBuffer.putInt(n2);
            byteBuffer.putInt(n3);
            this.content.put(DF_DRAG_IMAGE_OFFSET, byteBuffer);
            int n6 = wCImage.getWidth();
            int n7 = wCImage.getHeight();
            ByteBuffer byteBuffer2 = wCImage.getPixelBuffer();
            ByteBuffer byteBuffer3 = ByteBuffer.allocate(8 + n6 * n7 * 4);
            byteBuffer3.putInt(n6);
            byteBuffer3.putInt(n7);
            byteBuffer3.put(byteBuffer2);
            this.content.put(DF_DRAG_IMAGE, byteBuffer3);
            Object object2 = object = wCImage.getWidth() > 0 && wCImage.getHeight() > 0 ? wCImage.getPlatformImage() : null;
            if (object != null) {
                try {
                    File file = File.createTempFile("jfx", ".png");
                    file.deleteOnExit();
                    ImageIO.write((RenderedImage)UIClientImpl.toBufferedImage(Image.impl_fromPlatformImage(Toolkit.getToolkit().loadPlatformImage(object))), "png", file);
                    this.content.put(DataFormat.FILES, Arrays.asList(file));
                }
                catch (IOException | SecurityException exception) {
                    // empty catch block
                }
            }
        }
    }

    @Override
    public void confirmStartDrag() {
        WebView webView = this.accessor.getView();
        if (webView != null && this.content != null) {
            Dragboard dragboard = webView.startDragAndDrop(TransferMode.ANY);
            dragboard.setContent(this.content);
        }
        this.content = null;
    }

    @Override
    public boolean isDragConfirmed() {
        return this.accessor.getView() != null && this.content != null;
    }

    public static BufferedImage toBufferedImage(Image image) {
        try {
            Class<?> class_ = Class.forName("javafx.embed.swing.SwingFXUtils");
            Method method = class_.getMethod("fromFXImage", Image.class, BufferedImage.class);
            Object object = method.invoke(null, image, null);
            return (BufferedImage)object;
        }
        catch (Exception exception) {
            exception.printStackTrace(System.err);
            return null;
        }
    }

    static {
        FileExtensionInfo.add("video", "Video Files", "*.webm", "*.mp4", "*.ogg");
        FileExtensionInfo.add("audio", "Audio Files", "*.mp3", "*.aac", "*.wav");
        FileExtensionInfo.add("text", "Text Files", "*.txt", "*.csv", "*.text", "*.ttf", "*.sdf", "*.srt", "*.htm", "*.html");
        FileExtensionInfo.add("image", "Image Files", "*.png", "*.jpg", "*.gif", "*.bmp", "*.jpeg");
        DF_DRAG_IMAGE = UIClientImpl.getDataFormat("application/x-java-drag-image");
        DF_DRAG_IMAGE_OFFSET = UIClientImpl.getDataFormat("application/x-java-drag-image-offset");
    }

    private static class FileExtensionInfo {
        private String description;
        private List<String> extensions;

        private FileExtensionInfo() {
        }

        static void add(String string, String string2, String ... arrstring) {
            FileExtensionInfo fileExtensionInfo = new FileExtensionInfo();
            fileExtensionInfo.description = string2;
            fileExtensionInfo.extensions = Arrays.asList(arrstring);
            fileExtensionMap.put(string, fileExtensionInfo);
        }

        private FileChooser.ExtensionFilter getExtensionFilter(String string) {
            String string2 = "*." + string;
            String string3 = this.description + " ";
            if (string.equals("*")) {
                string3 = string3 + this.extensions.stream().collect(Collectors.joining(", ", "(", ")"));
                return new FileChooser.ExtensionFilter(string3, this.extensions);
            }
            if (this.extensions.contains(string2)) {
                string3 = string3 + "(" + string2 + ")";
                return new FileChooser.ExtensionFilter(string3, string2);
            }
            return null;
        }
    }
}

