/*
 * Decompiled with CFR 0.150.
 */
package javafx.application;

import com.sun.javafx.application.PlatformImpl;
import javafx.application.ConditionalFeature;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;

public final class Platform {
    private static ReadOnlyBooleanWrapper accessibilityActiveProperty;

    private Platform() {
    }

    public static void runLater(Runnable runnable) {
        PlatformImpl.runLater(runnable);
    }

    public static boolean isFxApplicationThread() {
        return PlatformImpl.isFxApplicationThread();
    }

    public static void exit() {
        PlatformImpl.exit();
    }

    public static void setImplicitExit(boolean bl) {
        PlatformImpl.setImplicitExit(bl);
    }

    public static boolean isImplicitExit() {
        return PlatformImpl.isImplicitExit();
    }

    public static boolean isSupported(ConditionalFeature conditionalFeature) {
        return PlatformImpl.isSupported(conditionalFeature);
    }

    public static boolean isAccessibilityActive() {
        return accessibilityActiveProperty == null ? false : accessibilityActiveProperty.get();
    }

    public static ReadOnlyBooleanProperty accessibilityActiveProperty() {
        if (accessibilityActiveProperty == null) {
            accessibilityActiveProperty = new ReadOnlyBooleanWrapper(Platform.class, "accessibilityActive");
            accessibilityActiveProperty.bind(PlatformImpl.accessibilityActiveProperty());
        }
        return accessibilityActiveProperty.getReadOnlyProperty();
    }
}

