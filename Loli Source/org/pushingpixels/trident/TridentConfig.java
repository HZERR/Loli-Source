/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.trident;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import org.pushingpixels.trident.TimelineEngine;
import org.pushingpixels.trident.UIToolkitHandler;
import org.pushingpixels.trident.interpolator.PropertyInterpolator;
import org.pushingpixels.trident.interpolator.PropertyInterpolatorSource;

public class TridentConfig {
    private static TridentConfig config;
    private Set<UIToolkitHandler> uiToolkitHandlers;
    private Set<PropertyInterpolator> propertyInterpolators;
    private PulseSource pulseSource = new DefaultPulseSource();

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private TridentConfig() {
        this.uiToolkitHandlers = new HashSet<UIToolkitHandler>();
        this.propertyInterpolators = new HashSet<PropertyInterpolator>();
        ClassLoader classLoader = TridentConfig.getClassLoader();
        try {
            Enumeration<URL> urls = classLoader.getResources("META-INF/trident-plugin.properties");
            while (urls.hasMoreElements()) {
                URL pluginUrl = urls.nextElement();
                BufferedReader reader = null;
                try {
                    String line;
                    reader = new BufferedReader(new InputStreamReader(pluginUrl.openStream()));
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split("=");
                        if (parts.length != 2) continue;
                        String key = parts[0];
                        String value = parts[1];
                        if ("UIToolkitHandler".compareTo(key) == 0) {
                            try {
                                Class<?> pluginClass = classLoader.loadClass(value);
                                if (pluginClass == null) continue;
                                if (UIToolkitHandler.class.isAssignableFrom(pluginClass)) {
                                    UIToolkitHandler uiToolkitHandler = (UIToolkitHandler)pluginClass.newInstance();
                                    uiToolkitHandler.isHandlerFor(new Object());
                                    this.uiToolkitHandlers.add(uiToolkitHandler);
                                }
                            }
                            catch (NoClassDefFoundError ncdfe) {
                            }
                            catch (ClassNotFoundException ncdfe) {
                                // empty catch block
                            }
                        }
                        if ("PropertyInterpolatorSource".compareTo(key) != 0) continue;
                        try {
                            Class<?> piSourceClass = classLoader.loadClass(value);
                            if (piSourceClass == null || !PropertyInterpolatorSource.class.isAssignableFrom(piSourceClass)) continue;
                            PropertyInterpolatorSource piSource = (PropertyInterpolatorSource)piSourceClass.newInstance();
                            Set<PropertyInterpolator> interpolators = piSource.getPropertyInterpolators();
                            for (PropertyInterpolator pi : interpolators) {
                                try {
                                    Class basePropertyClass = pi.getBasePropertyClass();
                                    basePropertyClass.getClass();
                                    this.propertyInterpolators.add(pi);
                                }
                                catch (NoClassDefFoundError ncdfe) {}
                            }
                        }
                        catch (NoClassDefFoundError ncdfe) {
                        }
                    }
                }
                finally {
                    if (reader == null) continue;
                    try {
                        reader.close();
                    }
                    catch (IOException ignored) {}
                }
            }
        }
        catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    private static ClassLoader getClassLoader() {
        ClassLoader cl = null;
        try {
            cl = AccessController.doPrivileged(new PrivilegedAction<ClassLoader>(){

                @Override
                public ClassLoader run() {
                    return TridentConfig.class.getClassLoader();
                }
            });
        }
        catch (SecurityException ignore) {
            // empty catch block
        }
        if (cl == null) {
            final Thread t2 = Thread.currentThread();
            try {
                cl = AccessController.doPrivileged(new PrivilegedAction<ClassLoader>(){

                    @Override
                    public ClassLoader run() {
                        return t2.getContextClassLoader();
                    }
                });
            }
            catch (SecurityException securityException) {
                // empty catch block
            }
        }
        if (cl == null) {
            try {
                cl = AccessController.doPrivileged(new PrivilegedAction<ClassLoader>(){

                    @Override
                    public ClassLoader run() {
                        return ClassLoader.getSystemClassLoader();
                    }
                });
            }
            catch (SecurityException securityException) {
                // empty catch block
            }
        }
        return cl;
    }

    public static synchronized TridentConfig getInstance() {
        if (config == null) {
            config = new TridentConfig();
        }
        return config;
    }

    public synchronized Collection<UIToolkitHandler> getUIToolkitHandlers() {
        return Collections.unmodifiableSet(this.uiToolkitHandlers);
    }

    public synchronized Collection<PropertyInterpolator> getPropertyInterpolators() {
        return Collections.unmodifiableSet(this.propertyInterpolators);
    }

    public synchronized PropertyInterpolator getPropertyInterpolator(Object ... values) {
        for (PropertyInterpolator interpolator : this.propertyInterpolators) {
            try {
                Class basePropertyClass = interpolator.getBasePropertyClass();
                boolean hasMatch = true;
                for (Object value : values) {
                    if (basePropertyClass.isAssignableFrom(value.getClass())) continue;
                    hasMatch = false;
                }
                if (!hasMatch) continue;
                return interpolator;
            }
            catch (NoClassDefFoundError ignore) {
            }
        }
        return null;
    }

    public synchronized void addPropertyInterpolator(PropertyInterpolator pInterpolator) {
        this.propertyInterpolators.add(pInterpolator);
    }

    public synchronized void addPropertyInterpolatorSource(PropertyInterpolatorSource pInterpolatorSource) {
        this.propertyInterpolators.addAll(pInterpolatorSource.getPropertyInterpolators());
    }

    public synchronized void removePropertyInterpolator(PropertyInterpolator pInterpolator) {
        this.propertyInterpolators.remove(pInterpolator);
    }

    public synchronized void addUIToolkitHandler(UIToolkitHandler uiToolkitHandler) {
        this.uiToolkitHandlers.add(uiToolkitHandler);
    }

    public synchronized void removeUIToolkitHandler(UIToolkitHandler uiToolkitHandler) {
        this.uiToolkitHandlers.remove(uiToolkitHandler);
    }

    public synchronized void setPulseSource(PulseSource pulseSource) {
        TimelineEngine.TridentAnimationThread current = TimelineEngine.getInstance().animatorThread;
        if (current != null && current.isAlive()) {
            throw new IllegalStateException("Cannot replace the pulse source thread once it's running");
        }
        this.pulseSource = pulseSource;
    }

    public synchronized PulseSource getPulseSource() {
        return this.pulseSource;
    }

    private class DefaultPulseSource
    extends FixedRatePulseSource {
        DefaultPulseSource() {
            super(40);
        }
    }

    public static class FixedRatePulseSource
    implements PulseSource {
        private int msDelay;

        public FixedRatePulseSource(int msDelay) {
            this.msDelay = msDelay;
        }

        @Override
        public void waitUntilNextPulse() {
            try {
                Thread.sleep(this.msDelay);
            }
            catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }

    public static interface PulseSource {
        public void waitUntilNextPulse();
    }
}

