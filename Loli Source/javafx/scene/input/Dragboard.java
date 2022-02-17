/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.input;

import com.sun.javafx.scene.input.DragboardHelper;
import com.sun.javafx.tk.PermissionHelper;
import com.sun.javafx.tk.TKClipboard;
import java.security.Permission;
import java.util.Set;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.scene.input.TransferMode;

public final class Dragboard
extends Clipboard {
    private boolean dataAccessRestricted = true;

    Dragboard(TKClipboard tKClipboard) {
        super(tKClipboard);
    }

    @Override
    Object getContentImpl(DataFormat dataFormat) {
        SecurityManager securityManager;
        if (this.dataAccessRestricted && (securityManager = System.getSecurityManager()) != null) {
            Permission permission = PermissionHelper.getAccessClipboardPermission();
            securityManager.checkPermission(permission);
        }
        return super.getContentImpl(dataFormat);
    }

    public final Set<TransferMode> getTransferModes() {
        return this.peer.getTransferModes();
    }

    @Deprecated
    public TKClipboard impl_getPeer() {
        return this.peer;
    }

    @Deprecated
    public static Dragboard impl_createDragboard(TKClipboard tKClipboard) {
        return new Dragboard(tKClipboard);
    }

    public void setDragView(Image image, double d2, double d3) {
        this.peer.setDragView(image);
        this.peer.setDragViewOffsetX(d2);
        this.peer.setDragViewOffsetY(d3);
    }

    public void setDragView(Image image) {
        this.peer.setDragView(image);
    }

    public void setDragViewOffsetX(double d2) {
        this.peer.setDragViewOffsetX(d2);
    }

    public void setDragViewOffsetY(double d2) {
        this.peer.setDragViewOffsetY(d2);
    }

    public Image getDragView() {
        return this.peer.getDragView();
    }

    public double getDragViewOffsetX() {
        return this.peer.getDragViewOffsetX();
    }

    public double getDragViewOffsetY() {
        return this.peer.getDragViewOffsetY();
    }

    static {
        DragboardHelper.setDragboardAccessor((dragboard, bl) -> {
            dragboard.dataAccessRestricted = bl;
        });
    }
}

