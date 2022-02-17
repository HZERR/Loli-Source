/*
 * Decompiled with CFR 0.150.
 */
package javafx.stage;

import com.sun.javafx.stage.ScreenHelper;
import com.sun.javafx.tk.ScreenConfigurationAccessor;
import com.sun.javafx.tk.Toolkit;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;

public final class Screen {
    private static final AtomicBoolean configurationDirty = new AtomicBoolean(true);
    private static final ScreenConfigurationAccessor accessor;
    private static Screen primary;
    private static final ObservableList<Screen> screens;
    private static final ObservableList<Screen> unmodifiableScreens;
    private Rectangle2D bounds = Rectangle2D.EMPTY;
    private Rectangle2D visualBounds = Rectangle2D.EMPTY;
    private double dpi;
    private float renderScale;

    private Screen() {
    }

    private static void checkDirty() {
        if (configurationDirty.compareAndSet(true, false)) {
            Screen.updateConfiguration();
        }
    }

    private static void updateConfiguration() {
        Object object = Toolkit.getToolkit().getPrimaryScreen();
        Screen screen = Screen.nativeToScreen(object, primary);
        if (screen != null) {
            primary = screen;
        }
        List<?> list = Toolkit.getToolkit().getScreens();
        ObservableList observableList = FXCollections.observableArrayList();
        boolean bl = screens.size() == list.size();
        for (int i2 = 0; i2 < list.size(); ++i2) {
            Screen screen2;
            Object obj = list.get(i2);
            Screen screen3 = null;
            if (bl) {
                screen3 = (Screen)screens.get(i2);
            }
            if ((screen2 = Screen.nativeToScreen(obj, screen3)) == null) continue;
            if (bl) {
                bl = false;
                observableList.clear();
                observableList.addAll(screens.subList(0, i2));
            }
            observableList.add(screen2);
        }
        if (!bl) {
            screens.clear();
            screens.addAll(observableList);
        }
        configurationDirty.set(false);
    }

    private static Screen nativeToScreen(Object object, Screen screen) {
        int n2 = accessor.getMinX(object);
        int n3 = accessor.getMinY(object);
        int n4 = accessor.getWidth(object);
        int n5 = accessor.getHeight(object);
        int n6 = accessor.getVisualMinX(object);
        int n7 = accessor.getVisualMinY(object);
        int n8 = accessor.getVisualWidth(object);
        int n9 = accessor.getVisualHeight(object);
        double d2 = accessor.getDPI(object);
        float f2 = accessor.getRenderScale(object);
        if (screen == null || screen.bounds.getMinX() != (double)n2 || screen.bounds.getMinY() != (double)n3 || screen.bounds.getWidth() != (double)n4 || screen.bounds.getHeight() != (double)n5 || screen.visualBounds.getMinX() != (double)n6 || screen.visualBounds.getMinY() != (double)n7 || screen.visualBounds.getWidth() != (double)n8 || screen.visualBounds.getHeight() != (double)n9 || screen.dpi != d2 || screen.renderScale != f2) {
            Screen screen2 = new Screen();
            screen2.bounds = new Rectangle2D(n2, n3, n4, n5);
            screen2.visualBounds = new Rectangle2D(n6, n7, n8, n9);
            screen2.dpi = d2;
            screen2.renderScale = f2;
            return screen2;
        }
        return null;
    }

    static Screen getScreenForNative(Object object) {
        double d2 = accessor.getMinX(object);
        double d3 = accessor.getMinY(object);
        double d4 = accessor.getWidth(object);
        double d5 = accessor.getHeight(object);
        Screen screen = null;
        for (int i2 = 0; i2 < screens.size(); ++i2) {
            Screen screen2 = (Screen)screens.get(i2);
            if (screen2.bounds.contains(d2, d3, d4, d5)) {
                return screen2;
            }
            if (screen != null || !screen2.bounds.intersects(d2, d3, d4, d5)) continue;
            screen = screen2;
        }
        return screen == null ? Screen.getPrimary() : screen;
    }

    public static Screen getPrimary() {
        Screen.checkDirty();
        return primary;
    }

    public static ObservableList<Screen> getScreens() {
        Screen.checkDirty();
        return unmodifiableScreens;
    }

    public static ObservableList<Screen> getScreensForRectangle(double d2, double d3, double d4, double d5) {
        Screen.checkDirty();
        ObservableList<Screen> observableList = FXCollections.observableArrayList();
        for (Screen screen : screens) {
            if (!screen.bounds.intersects(d2, d3, d4, d5)) continue;
            observableList.add(screen);
        }
        return observableList;
    }

    public static ObservableList<Screen> getScreensForRectangle(Rectangle2D rectangle2D) {
        Screen.checkDirty();
        return Screen.getScreensForRectangle(rectangle2D.getMinX(), rectangle2D.getMinY(), rectangle2D.getWidth(), rectangle2D.getHeight());
    }

    public final Rectangle2D getBounds() {
        return this.bounds;
    }

    public final Rectangle2D getVisualBounds() {
        return this.visualBounds;
    }

    public final double getDpi() {
        return this.dpi;
    }

    private float getRenderScale() {
        return this.renderScale;
    }

    public int hashCode() {
        long l2 = 7L;
        l2 = 37L * l2 + (long)this.bounds.hashCode();
        l2 = 37L * l2 + (long)this.visualBounds.hashCode();
        l2 = 37L * l2 + Double.doubleToLongBits(this.dpi);
        l2 = 37L * l2 + (long)Float.floatToIntBits(this.renderScale);
        return (int)(l2 ^ l2 >> 32);
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof Screen) {
            Screen screen = (Screen)object;
            return (this.bounds == null ? screen.bounds == null : this.bounds.equals(screen.bounds)) && (this.visualBounds == null ? screen.visualBounds == null : this.visualBounds.equals(screen.visualBounds)) && screen.dpi == this.dpi && screen.renderScale == this.renderScale;
        }
        return false;
    }

    public String toString() {
        return super.toString() + " bounds:" + this.bounds + " visualBounds:" + this.visualBounds + " dpi:" + this.dpi + " renderScale:" + this.renderScale;
    }

    static {
        screens = FXCollections.observableArrayList();
        unmodifiableScreens = FXCollections.unmodifiableObservableList(screens);
        ScreenHelper.setScreenAccessor(new ScreenHelper.ScreenAccessor(){

            @Override
            public float getRenderScale(Screen screen) {
                return screen.getRenderScale();
            }
        });
        accessor = Toolkit.getToolkit().setScreenConfigurationListener(() -> Screen.updateConfiguration());
    }
}

