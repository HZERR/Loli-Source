/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import javax.swing.JComponent;
import org.pushingpixels.lafwidget.LafWidget;
import org.pushingpixels.lafwidget.LafWidgetSupport;

public class LafWidgetRepository {
    protected Map<Class<?>, Set<LafWidgetClassInfo>> widgets = new HashMap();
    protected Set<String> widgetClassesToIgnore;
    protected LafWidgetSupport lafSupport = new LafWidgetSupport();
    protected boolean isCustomLafSupportSet = false;
    protected static LafWidgetRepository repository;
    private static ResourceBundle LABEL_BUNDLE;
    private static ClassLoader labelBundleClassLoader;

    private LafWidgetRepository() {
        this.widgetClassesToIgnore = new HashSet<String>();
    }

    public static synchronized LafWidgetRepository getRepository() {
        if (repository == null) {
            repository = new LafWidgetRepository();
            repository.populate();
        }
        return repository;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected void populateFrom(URL url) {
        Properties props = new Properties();
        InputStream is = null;
        try {
            is = url.openStream();
            props.load(is);
            Enumeration<?> names = props.propertyNames();
            block12: while (true) {
                if (!names.hasMoreElements()) {
                    if (is == null) return;
                    try {
                        is.close();
                        return;
                    }
                    catch (IOException ioe) {
                        return;
                    }
                }
                String name = (String)names.nextElement();
                String value = props.getProperty(name);
                String[] values = value.split(";");
                int i2 = 0;
                while (true) {
                    if (i2 >= values.length) continue block12;
                    String className = values[i2].trim();
                    boolean isExact = className.startsWith("%");
                    if (isExact) {
                        className = className.substring(1);
                    }
                    try {
                        this.registerWidget(name, Class.forName(className), isExact);
                    }
                    catch (ClassNotFoundException cnfe) {
                        // empty catch block
                    }
                    ++i2;
                }
                break;
            }
        }
        catch (IOException ioe) {
            if (is == null) return;
            try {
                is.close();
                return;
            }
            catch (IOException ioe2) {
                return;
            }
            catch (Throwable throwable) {
                if (is == null) throw throwable;
                try {
                    is.close();
                    throw throwable;
                }
                catch (IOException ioe3) {
                    // empty catch block
                }
                throw throwable;
            }
        }
    }

    public void populate() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        try {
            Enumeration<URL> rs = cl.getResources("META-INF/lafwidget.properties");
            while (rs.hasMoreElements()) {
                URL rUrl = rs.nextElement();
                this.populateFrom(rUrl);
            }
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }

    public synchronized void registerWidget(String widgetClassName, List<Class<?>> supportedClasses) {
        for (Class<?> clazz : supportedClasses) {
            this.registerWidget(widgetClassName, clazz, false);
        }
    }

    public synchronized void registerWidget(String widgetClassName, Class<?> supportedClass, boolean isExact) {
        if (JComponent.class.isAssignableFrom(supportedClass) && !this.widgets.containsKey(supportedClass)) {
            this.widgets.put(supportedClass, new HashSet());
        }
        for (LafWidgetClassInfo registered : this.widgets.get(supportedClass)) {
            if (!registered.className.equals(widgetClassName)) continue;
            return;
        }
        this.widgets.get(supportedClass).add(new LafWidgetClassInfo(widgetClassName, isExact));
    }

    public synchronized Set<LafWidget> getMatchingWidgets(JComponent jcomp) {
        HashSet<LafWidget> result = new HashSet<LafWidget>();
        boolean isOriginator = true;
        for (Class<?> clazz = jcomp.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
            Set<LafWidgetClassInfo> registered = this.widgets.get(clazz);
            if (registered != null) {
                for (LafWidgetClassInfo widgetClassInfo : registered) {
                    if (widgetClassInfo.isExact && !isOriginator) continue;
                    try {
                        LafWidget widget;
                        Object widgetObj;
                        String widgetClassName = widgetClassInfo.className;
                        if (this.widgetClassesToIgnore.contains(widgetClassName) || !((widgetObj = Class.forName(widgetClassName).newInstance()) instanceof LafWidget) || (widget = (LafWidget)widgetObj).requiresCustomLafSupport() && !this.isCustomLafSupportSet) continue;
                        widget.setComponent(jcomp);
                        result.add(widget);
                    }
                    catch (InstantiationException ie) {
                    }
                    catch (IllegalAccessException iae) {
                    }
                    catch (ClassNotFoundException cnfe) {}
                }
            }
            isOriginator = false;
        }
        return result;
    }

    public void setLafSupport(LafWidgetSupport lafSupport) {
        if (lafSupport == null) {
            throw new IllegalArgumentException("LAF support can't be null");
        }
        this.lafSupport = lafSupport;
        this.isCustomLafSupportSet = this.lafSupport.getClass() != LafWidgetSupport.class;
    }

    public void unsetLafSupport() {
        this.lafSupport = new LafWidgetSupport();
        this.isCustomLafSupportSet = false;
    }

    public LafWidgetSupport getLafSupport() {
        return this.lafSupport;
    }

    public static synchronized ResourceBundle getLabelBundle() {
        LABEL_BUNDLE = labelBundleClassLoader == null ? ResourceBundle.getBundle("org.pushingpixels.lafwidget.resources.Labels", Locale.getDefault()) : ResourceBundle.getBundle("org.pushingpixels.lafwidget.resources.Labels", Locale.getDefault(), labelBundleClassLoader);
        return LABEL_BUNDLE;
    }

    public static synchronized ResourceBundle getLabelBundle(Locale locale) {
        if (labelBundleClassLoader == null) {
            return ResourceBundle.getBundle("org.pushingpixels.lafwidget.resources.Labels", locale);
        }
        return ResourceBundle.getBundle("org.pushingpixels.lafwidget.resources.Labels", locale, labelBundleClassLoader);
    }

    public static synchronized void resetLabelBundle() {
        LABEL_BUNDLE = null;
    }

    public static void setLabelBundleClassLoader(ClassLoader labelBundleClassLoader) {
        LafWidgetRepository.labelBundleClassLoader = labelBundleClassLoader;
    }

    public synchronized void addToIgnoreWidgets(String widgetClassName) {
        this.widgetClassesToIgnore.add(widgetClassName);
    }

    static {
        LABEL_BUNDLE = null;
    }

    protected static class LafWidgetClassInfo {
        public String className;
        public boolean isExact;

        public LafWidgetClassInfo(String className, boolean isExact) {
            this.className = className;
            this.isExact = isExact;
        }
    }
}

