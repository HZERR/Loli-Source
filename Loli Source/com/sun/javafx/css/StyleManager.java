/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.css;

import com.sun.javafx.css.CssError;
import com.sun.javafx.css.FontFace;
import com.sun.javafx.css.Rule;
import com.sun.javafx.css.Selector;
import com.sun.javafx.css.SelectorPartitioning;
import com.sun.javafx.css.StyleCache;
import com.sun.javafx.css.StyleClassSet;
import com.sun.javafx.css.StyleConverterImpl;
import com.sun.javafx.css.StyleMap;
import com.sun.javafx.css.Stylesheet;
import com.sun.javafx.css.parser.CSSParser;
import com.sun.javafx.util.Logging;
import java.io.FileNotFoundException;
import java.io.FilePermission;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.AccessControlContext;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PermissionCollection;
import java.security.PrivilegedActionException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.css.StyleOrigin;
import javafx.css.Styleable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.stage.Window;
import sun.util.logging.PlatformLogger;

public final class StyleManager {
    private static final Object styleLock = new Object();
    private static PlatformLogger LOGGER;
    static final Map<Parent, CacheContainer> cacheContainerMap;
    final List<StylesheetContainer> userAgentStylesheetContainers = new ArrayList<StylesheetContainer>();
    final List<StylesheetContainer> platformUserAgentStylesheetContainers = new ArrayList<StylesheetContainer>();
    boolean hasDefaultUserAgentStylesheet = false;
    final Map<String, StylesheetContainer> stylesheetContainerMap = new HashMap<String, StylesheetContainer>();
    private final Map<String, Image> imageCache = new HashMap<String, Image>();
    private Key key = null;
    private static ObservableList<CssError> errors;
    private static List<String> cacheMapKey;

    private static PlatformLogger getLogger() {
        if (LOGGER == null) {
            LOGGER = Logging.getCSSLogger();
        }
        return LOGGER;
    }

    public static StyleManager getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private StyleManager() {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    CacheContainer getCacheContainer(Styleable styleable, SubScene subScene) {
        Object object;
        Object object2;
        if (styleable == null && subScene == null) {
            return null;
        }
        Parent parent = null;
        if (subScene != null) {
            parent = subScene.getRoot();
        } else if (styleable instanceof Node) {
            object2 = (Node)styleable;
            object = ((Node)object2).getScene();
            if (object != null) {
                parent = ((Scene)object).getRoot();
            }
        } else if (styleable instanceof Window && (object2 = ((Window)((Object)styleable)).getScene()) != null) {
            parent = ((Scene)object2).getRoot();
        }
        if (parent == null) {
            return null;
        }
        object2 = styleLock;
        synchronized (object2) {
            object = cacheContainerMap.get(parent);
            if (object == null) {
                object = new CacheContainer();
                cacheContainerMap.put(parent, (CacheContainer)object);
            }
            return object;
        }
    }

    public StyleCache getSharedCache(Styleable styleable, SubScene subScene, StyleCache.Key key) {
        CacheContainer cacheContainer = this.getCacheContainer(styleable, subScene);
        if (cacheContainer == null) {
            return null;
        }
        Map map = cacheContainer.getStyleCache();
        if (map == null) {
            return null;
        }
        StyleCache styleCache = (StyleCache)map.get(key);
        if (styleCache == null) {
            styleCache = new StyleCache();
            map.put(new StyleCache.Key(key), styleCache);
        }
        return styleCache;
    }

    public StyleMap getStyleMap(Styleable styleable, SubScene subScene, int n2) {
        if (n2 == -1) {
            return StyleMap.EMPTY_MAP;
        }
        CacheContainer cacheContainer = this.getCacheContainer(styleable, subScene);
        if (cacheContainer == null) {
            return StyleMap.EMPTY_MAP;
        }
        return cacheContainer.getStyleMap(n2);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void forget(Scene scene) {
        if (scene == null) {
            return;
        }
        this.forget(scene.getRoot());
        Object object = styleLock;
        synchronized (object) {
            Object object2;
            String string = null;
            if (scene.getUserAgentStylesheet() != null && !(string = scene.getUserAgentStylesheet().trim()).isEmpty()) {
                for (int i2 = this.userAgentStylesheetContainers.size() - 1; 0 <= i2; --i2) {
                    object2 = this.userAgentStylesheetContainers.get(i2);
                    if (!string.equals(((StylesheetContainer)object2).fname)) continue;
                    ((StylesheetContainer)object2).parentUsers.remove(scene.getRoot());
                    if (object2.parentUsers.list.size() != 0) continue;
                    this.userAgentStylesheetContainers.remove(i2);
                }
            }
            Set<Map.Entry<String, StylesheetContainer>> set = this.stylesheetContainerMap.entrySet();
            object2 = set.iterator();
            while (object2.hasNext()) {
                Map.Entry<String, StylesheetContainer> entry = object2.next();
                StylesheetContainer stylesheetContainer = entry.getValue();
                Iterator iterator = stylesheetContainer.parentUsers.list.iterator();
                while (iterator.hasNext()) {
                    Reference reference = iterator.next();
                    Parent parent = (Parent)reference.get();
                    if (parent != null && parent.getScene() != scene && parent.getScene() != null) continue;
                    reference.clear();
                    iterator.remove();
                }
                if (!stylesheetContainer.parentUsers.list.isEmpty()) continue;
                object2.remove();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void stylesheetsChanged(Scene scene, ListChangeListener.Change<String> change) {
        Object object = styleLock;
        synchronized (object) {
            Object object2;
            Set<Map.Entry<Parent, CacheContainer>> set = cacheContainerMap.entrySet();
            for (Map.Entry<Parent, CacheContainer> object3 : set) {
                object2 = object3.getKey();
                CacheContainer cacheContainer = object3.getValue();
                if (((Node)object2).getScene() != scene) continue;
                cacheContainer.clearCache();
            }
            change.reset();
            while (change.next()) {
                if (!change.wasRemoved()) continue;
                for (String string : change.getRemoved()) {
                    this.stylesheetRemoved(scene, string);
                    object2 = this.stylesheetContainerMap.get(string);
                    if (object2 == null) continue;
                    ((StylesheetContainer)object2).invalidateChecksum();
                }
            }
        }
    }

    private void stylesheetRemoved(Scene scene, String string) {
        this.stylesheetRemoved(scene.getRoot(), string);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void forget(Parent parent) {
        if (parent == null) {
            return;
        }
        Object object = styleLock;
        synchronized (object) {
            ObservableList<String> observableList;
            CacheContainer cacheContainer = cacheContainerMap.remove(parent);
            if (cacheContainer != null) {
                cacheContainer.clearCache();
            }
            if ((observableList = parent.getStylesheets()) != null && !observableList.isEmpty()) {
                for (String object2 : observableList) {
                    this.stylesheetRemoved(parent, object2);
                }
            }
            Iterator<Map.Entry<String, StylesheetContainer>> iterator = this.stylesheetContainerMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, StylesheetContainer> entry = iterator.next();
                StylesheetContainer stylesheetContainer = entry.getValue();
                stylesheetContainer.parentUsers.remove(parent);
                if (!stylesheetContainer.parentUsers.list.isEmpty()) continue;
                iterator.remove();
                if (stylesheetContainer.selectorPartitioning != null) {
                    stylesheetContainer.selectorPartitioning.reset();
                }
                String string = stylesheetContainer.fname;
                this.cleanUpImageCache(string);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void stylesheetsChanged(Parent parent, ListChangeListener.Change<String> change) {
        Object object = styleLock;
        synchronized (object) {
            change.reset();
            while (change.next()) {
                if (!change.wasRemoved()) continue;
                for (String string : change.getRemoved()) {
                    this.stylesheetRemoved(parent, string);
                    StylesheetContainer stylesheetContainer = this.stylesheetContainerMap.get(string);
                    if (stylesheetContainer == null) continue;
                    stylesheetContainer.invalidateChecksum();
                }
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void stylesheetRemoved(Parent parent, String string) {
        Object object = styleLock;
        synchronized (object) {
            StylesheetContainer stylesheetContainer = this.stylesheetContainerMap.get(string);
            if (stylesheetContainer == null) {
                return;
            }
            stylesheetContainer.parentUsers.remove(parent);
            if (stylesheetContainer.parentUsers.list.isEmpty()) {
                this.removeStylesheetContainer(stylesheetContainer);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void forget(SubScene subScene) {
        if (subScene == null) {
            return;
        }
        Parent parent = subScene.getRoot();
        if (parent == null) {
            return;
        }
        this.forget(parent);
        Object object = styleLock;
        synchronized (object) {
            Object object2;
            Object object3;
            String string = null;
            if (subScene.getUserAgentStylesheet() != null && !(string = subScene.getUserAgentStylesheet().trim()).isEmpty()) {
                object3 = this.userAgentStylesheetContainers.iterator();
                while (object3.hasNext()) {
                    object2 = (StylesheetContainer)object3.next();
                    if (!string.equals(((StylesheetContainer)object2).fname)) continue;
                    ((StylesheetContainer)object2).parentUsers.remove(subScene.getRoot());
                    if (object2.parentUsers.list.size() != 0) continue;
                    object3.remove();
                }
            }
            object3 = new ArrayList<StylesheetContainer>(this.stylesheetContainerMap.values());
            object2 = object3.iterator();
            while (object2.hasNext()) {
                StylesheetContainer stylesheetContainer = (StylesheetContainer)object2.next();
                Iterator iterator = stylesheetContainer.parentUsers.list.iterator();
                block5: while (iterator.hasNext()) {
                    Reference reference = iterator.next();
                    Parent parent2 = (Parent)reference.get();
                    if (parent2 == null) continue;
                    for (Parent parent3 = parent2; parent3 != null; parent3 = parent3.getParent()) {
                        if (parent != parent3.getParent()) continue;
                        reference.clear();
                        iterator.remove();
                        this.forget(parent2);
                        continue block5;
                    }
                }
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void removeStylesheetContainer(StylesheetContainer stylesheetContainer) {
        if (stylesheetContainer == null) {
            return;
        }
        Object object = styleLock;
        synchronized (object) {
            Object object2;
            Object object3;
            String string = stylesheetContainer.fname;
            this.stylesheetContainerMap.remove(string);
            if (stylesheetContainer.selectorPartitioning != null) {
                stylesheetContainer.selectorPartitioning.reset();
            }
            for (Map.Entry<Parent, CacheContainer> entry : cacheContainerMap.entrySet()) {
                Object object4;
                object3 = entry.getValue();
                if (object3 == null || ((CacheContainer)object3).cacheMap == null || ((CacheContainer)object3).cacheMap.isEmpty()) continue;
                object2 = new ArrayList();
                for (Map.Entry entry2 : ((CacheContainer)object3).cacheMap.entrySet()) {
                    object4 = (List)entry2.getKey();
                    if (!(object4 != null ? object4.contains(string) : string == null)) continue;
                    object2.add(object4);
                }
                if (object2.isEmpty()) continue;
                Iterator iterator = object2.iterator();
                while (iterator.hasNext()) {
                    Map.Entry entry2;
                    entry2 = (List)((Object)iterator.next());
                    object4 = (Map)((CacheContainer)object3).cacheMap.remove(entry2);
                    if (object4 == null) continue;
                    object4.clear();
                }
            }
            this.cleanUpImageCache(string);
            List list = stylesheetContainer.parentUsers.list;
            for (int i2 = list.size() - 1; 0 <= i2; --i2) {
                object3 = (Reference)list.remove(i2);
                object2 = (Parent)((Reference)object3).get();
                ((Reference)object3).clear();
                if (object2 == null || ((Node)object2).getScene() == null) continue;
                ((Node)object2).impl_reapplyCSS();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public Image getCachedImage(String string) {
        Object object = styleLock;
        synchronized (object) {
            Image image;
            block11: {
                image = null;
                if (this.imageCache.containsKey(string)) {
                    image = this.imageCache.get(string);
                } else {
                    try {
                        image = new Image(string);
                        if (image.isError()) {
                            PlatformLogger platformLogger = StyleManager.getLogger();
                            if (platformLogger != null && platformLogger.isLoggable(PlatformLogger.Level.WARNING)) {
                                platformLogger.warning("Error loading image: " + string);
                            }
                            image = null;
                        }
                        this.imageCache.put(string, image);
                    }
                    catch (IllegalArgumentException illegalArgumentException) {
                        PlatformLogger platformLogger = StyleManager.getLogger();
                        if (platformLogger != null && platformLogger.isLoggable(PlatformLogger.Level.WARNING)) {
                            platformLogger.warning(illegalArgumentException.getLocalizedMessage());
                        }
                    }
                    catch (NullPointerException nullPointerException) {
                        PlatformLogger platformLogger = StyleManager.getLogger();
                        if (platformLogger == null || !platformLogger.isLoggable(PlatformLogger.Level.WARNING)) break block11;
                        platformLogger.warning(nullPointerException.getLocalizedMessage());
                    }
                }
            }
            return image;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void cleanUpImageCache(String string) {
        Object object = styleLock;
        synchronized (object) {
            if (string == null && this.imageCache.isEmpty()) {
                return;
            }
            String string2 = string.trim();
            if (string2.isEmpty()) {
                return;
            }
            int n2 = string2.lastIndexOf(47);
            String string3 = n2 > 0 ? string2.substring(0, n2) : string2;
            int n3 = string3.length();
            String[] arrstring = new String[this.imageCache.size()];
            int n4 = 0;
            Set<Map.Entry<String, Image>> set = this.imageCache.entrySet();
            for (Map.Entry<String, Image> entry : set) {
                String string4 = entry.getKey();
                n2 = string4.lastIndexOf(47);
                String string5 = n2 > 0 ? string4.substring(0, n2) : string4;
                int n5 = string5.length();
                boolean bl = n5 > n3 ? string5.startsWith(string3) : string3.startsWith(string5);
                if (!bl) continue;
                arrstring[n4++] = string4;
            }
            for (int i2 = 0; i2 < n4; ++i2) {
                Map.Entry<String, Image> entry;
                entry = this.imageCache.remove(arrstring[i2]);
            }
        }
    }

    private static URL getURL(String string) {
        if (string == null || string.trim().isEmpty()) {
            return null;
        }
        try {
            URI uRI = new URI(string.trim());
            if (!uRI.isAbsolute()) {
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                String string2 = uRI.getPath();
                URL uRL = null;
                uRL = string2.startsWith("/") ? classLoader.getResource(string2.substring(1)) : classLoader.getResource(string2);
                return uRL;
            }
            return uRI.toURL();
        }
        catch (MalformedURLException malformedURLException) {
            return null;
        }
        catch (URISyntaxException uRISyntaxException) {
            return null;
        }
    }

    /*
     * Loose catch block
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    static byte[] calculateCheckSum(String string) {
        byte[] arrby;
        Throwable throwable;
        DigestInputStream digestInputStream;
        Throwable throwable2;
        InputStream inputStream;
        block28: {
            block26: {
                block27: {
                    if (string == null || string.isEmpty()) {
                        return new byte[0];
                    }
                    URL uRL = StyleManager.getURL(string);
                    if (uRL == null || !"file".equals(uRL.getProtocol())) return new byte[0];
                    inputStream = uRL.openStream();
                    throwable2 = null;
                    digestInputStream = new DigestInputStream(inputStream, MessageDigest.getInstance("MD5"));
                    throwable = null;
                    digestInputStream.getMessageDigest().reset();
                    while (digestInputStream.read() != -1) {
                    }
                    arrby = digestInputStream.getMessageDigest().digest();
                    if (digestInputStream == null) break block26;
                    if (throwable == null) break block27;
                    try {
                        digestInputStream.close();
                    }
                    catch (Throwable throwable3) {
                        throwable.addSuppressed(throwable3);
                    }
                    break block26;
                }
                digestInputStream.close();
            }
            if (inputStream == null) return arrby;
            if (throwable2 == null) break block28;
            try {
                inputStream.close();
                return arrby;
            }
            catch (Throwable throwable4) {
                throwable2.addSuppressed(throwable4);
            }
            return arrby;
        }
        inputStream.close();
        return arrby;
        {
            catch (Throwable throwable5) {
                try {
                    try {
                        try {
                            try {
                                throwable = throwable5;
                                throw throwable5;
                            }
                            catch (Throwable throwable6) {
                                if (digestInputStream == null) throw throwable6;
                                if (throwable != null) {
                                    try {
                                        digestInputStream.close();
                                        throw throwable6;
                                    }
                                    catch (Throwable throwable7) {
                                        throwable.addSuppressed(throwable7);
                                    }
                                    throw throwable6;
                                } else {
                                    digestInputStream.close();
                                }
                                throw throwable6;
                            }
                        }
                        catch (Throwable throwable8) {
                            throwable2 = throwable8;
                            throw throwable8;
                        }
                    }
                    catch (Throwable throwable9) {
                        if (inputStream == null) throw throwable9;
                        if (throwable2 != null) {
                            try {
                                inputStream.close();
                                throw throwable9;
                            }
                            catch (Throwable throwable10) {
                                throwable2.addSuppressed(throwable10);
                            }
                            throw throwable9;
                        } else {
                            inputStream.close();
                        }
                        throw throwable9;
                    }
                }
                catch (IOException | IllegalArgumentException | SecurityException | NoSuchAlgorithmException exception) {
                    // empty catch block
                }
            }
        }
        return new byte[0];
    }

    public static Stylesheet loadStylesheet(String string) {
        try {
            return StyleManager.loadStylesheetUnPrivileged(string);
        }
        catch (AccessControlException accessControlException) {
            if (string.length() < 7 && string.indexOf("!/") < string.length() - 7) {
                return null;
            }
            try {
                String string2;
                String string3;
                URI uRI;
                String string4;
                URI uRI2 = new URI(string);
                if ("jar".equals(uRI2.getScheme()) && (string4 = (uRI = AccessController.doPrivileged(() -> StyleManager.class.getProtectionDomain().getCodeSource().getLocation().toURI())).getSchemeSpecificPart()).equals(string3 = (string2 = uRI2.getSchemeSpecificPart()).substring(string2.indexOf(47), string2.indexOf("!/")))) {
                    String string5 = string.substring(string.indexOf("!/") + 2);
                    if (string.endsWith(".css") || string.endsWith(".bss")) {
                        JarEntry jarEntry;
                        FilePermission filePermission = new FilePermission(string4, "read");
                        PermissionCollection permissionCollection = filePermission.newPermissionCollection();
                        permissionCollection.add(filePermission);
                        AccessControlContext accessControlContext = new AccessControlContext(new ProtectionDomain[]{new ProtectionDomain(null, permissionCollection)});
                        JarFile jarFile = null;
                        try {
                            jarFile = AccessController.doPrivileged(() -> new JarFile(string4), accessControlContext);
                        }
                        catch (PrivilegedActionException privilegedActionException) {
                            return null;
                        }
                        if (jarFile != null && (jarEntry = jarFile.getJarEntry(string5)) != null) {
                            return AccessController.doPrivileged(() -> StyleManager.loadStylesheetUnPrivileged(string), accessControlContext);
                        }
                    }
                }
                return null;
            }
            catch (URISyntaxException uRISyntaxException) {
                return null;
            }
            catch (PrivilegedActionException privilegedActionException) {
                return null;
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static Stylesheet loadStylesheetUnPrivileged(String string) {
        Object object = styleLock;
        synchronized (object) {
            block23: {
                Boolean bl = AccessController.doPrivileged(() -> {
                    String string2 = System.getProperty("binary.css");
                    return !string.endsWith(".bss") && string2 != null ? !Boolean.valueOf(string2).booleanValue() : Boolean.FALSE;
                });
                try {
                    Object object2;
                    String string2 = bl != false ? ".css" : ".bss";
                    URL uRL = null;
                    Stylesheet stylesheet = null;
                    if (!string.endsWith(".css") && !string.endsWith(".bss")) {
                        uRL = StyleManager.getURL(string);
                        bl = true;
                    } else {
                        object2 = string.substring(0, string.length() - 4);
                        uRL = StyleManager.getURL((String)object2 + string2);
                        if (uRL == null && (bl = Boolean.valueOf(bl == false)).booleanValue()) {
                            uRL = StyleManager.getURL((String)object2 + ".css");
                        }
                        if (uRL != null && !bl.booleanValue()) {
                            try {
                                stylesheet = Stylesheet.loadBinary(uRL);
                            }
                            catch (IOException iOException) {
                                stylesheet = null;
                            }
                            if (stylesheet == null && (bl = Boolean.valueOf(bl == false)).booleanValue()) {
                                uRL = StyleManager.getURL(string);
                            }
                        }
                    }
                    if (uRL != null && bl.booleanValue()) {
                        stylesheet = new CSSParser().parse(uRL);
                    }
                    if (stylesheet == null) {
                        if (errors != null) {
                            object2 = new CssError("Resource \"" + string + "\" not found.");
                            errors.add((CssError)object2);
                        }
                        if (StyleManager.getLogger().isLoggable(PlatformLogger.Level.WARNING)) {
                            StyleManager.getLogger().warning(String.format("Resource \"%s\" not found.", string));
                        }
                    }
                    if (stylesheet != null) {
                        block8: for (FontFace fontFace : stylesheet.getFontFaces()) {
                            for (FontFace.FontFaceSrc fontFaceSrc : fontFace.getSources()) {
                                if (fontFaceSrc.getType() != FontFace.FontFaceSrcType.URL) continue;
                                Font font = Font.loadFont(fontFaceSrc.getSrc(), 10.0);
                                if (font != null) continue block8;
                                StyleManager.getLogger().info("Could not load @font-face font [" + fontFaceSrc.getSrc() + "]");
                                continue block8;
                            }
                        }
                    }
                    return stylesheet;
                }
                catch (FileNotFoundException fileNotFoundException) {
                    if (errors != null) {
                        CssError cssError = new CssError("Stylesheet \"" + string + "\" not found.");
                        errors.add(cssError);
                    }
                    if (StyleManager.getLogger().isLoggable(PlatformLogger.Level.INFO)) {
                        StyleManager.getLogger().info("Could not find stylesheet: " + string);
                    }
                }
                catch (IOException iOException) {
                    if (errors != null) {
                        CssError cssError = new CssError("Could not load stylesheet: " + string);
                        errors.add(cssError);
                    }
                    if (!StyleManager.getLogger().isLoggable(PlatformLogger.Level.INFO)) break block23;
                    StyleManager.getLogger().info("Could not load stylesheet: " + string);
                }
            }
            return null;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setUserAgentStylesheets(List<String> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        Object object = styleLock;
        synchronized (object) {
            String string;
            String string2;
            int n2;
            int n3;
            boolean bl;
            if (list.size() == this.platformUserAgentStylesheetContainers.size()) {
                bl = true;
                n3 = list.size();
                for (n2 = 0; n2 < n3 && bl; ++n2) {
                    string2 = list.get(n2);
                    String string3 = string = string2 != null ? string2.trim() : null;
                    if (string == null || string.isEmpty()) break;
                    StylesheetContainer stylesheetContainer = this.platformUserAgentStylesheetContainers.get(n2);
                    bl = string.equals(stylesheetContainer.fname);
                    if (!bl) continue;
                    String string4 = stylesheetContainer.stylesheet.getUrl();
                    byte[] arrby = StyleManager.calculateCheckSum(string4);
                    bl = Arrays.equals(arrby, stylesheetContainer.checksum);
                }
                if (bl) {
                    return;
                }
            }
            bl = false;
            n3 = list.size();
            for (n2 = 0; n2 < n3; ++n2) {
                string2 = list.get(n2);
                String string5 = string = string2 != null ? string2.trim() : null;
                if (string == null || string.isEmpty()) continue;
                if (!bl) {
                    this.platformUserAgentStylesheetContainers.clear();
                    bl = true;
                }
                if (n2 == 0) {
                    this._setDefaultUserAgentStylesheet(string);
                    continue;
                }
                this._addUserAgentStylesheet(string);
            }
            if (bl) {
                this.userAgentStylesheetsChanged();
            }
        }
    }

    public void addUserAgentStylesheet(String string) {
        this.addUserAgentStylesheet(null, string);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void addUserAgentStylesheet(Scene scene, String string) {
        String string2;
        String string3 = string2 = string != null ? string.trim() : null;
        if (string2 == null || string2.isEmpty()) {
            return;
        }
        Object object = styleLock;
        synchronized (object) {
            CssError.setCurrentScene(scene);
            if (this._addUserAgentStylesheet(string2)) {
                this.userAgentStylesheetsChanged();
            }
            CssError.setCurrentScene(null);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private boolean _addUserAgentStylesheet(String string) {
        Object object = styleLock;
        synchronized (object) {
            int n2 = this.platformUserAgentStylesheetContainers.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                StylesheetContainer stylesheetContainer = this.platformUserAgentStylesheetContainers.get(i2);
                if (!string.equals(stylesheetContainer.fname)) continue;
                return false;
            }
            Stylesheet stylesheet = StyleManager.loadStylesheet(string);
            if (stylesheet == null) {
                return false;
            }
            stylesheet.setOrigin(StyleOrigin.USER_AGENT);
            this.platformUserAgentStylesheetContainers.add(new StylesheetContainer(string, stylesheet));
            return true;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void addUserAgentStylesheet(Scene scene, Stylesheet stylesheet) {
        if (stylesheet == null) {
            throw new IllegalArgumentException("null arg ua_stylesheet");
        }
        String string = stylesheet.getUrl();
        String string2 = string != null ? string.trim() : "";
        Object object = styleLock;
        synchronized (object) {
            int n2 = this.platformUserAgentStylesheetContainers.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                StylesheetContainer stylesheetContainer = this.platformUserAgentStylesheetContainers.get(i2);
                if (!string2.equals(stylesheetContainer.fname)) continue;
                return;
            }
            CssError.setCurrentScene(scene);
            this.platformUserAgentStylesheetContainers.add(new StylesheetContainer(string2, stylesheet));
            if (stylesheet != null) {
                stylesheet.setOrigin(StyleOrigin.USER_AGENT);
            }
            this.userAgentStylesheetsChanged();
            CssError.setCurrentScene(null);
        }
    }

    public void setDefaultUserAgentStylesheet(String string) {
        this.setDefaultUserAgentStylesheet(null, string);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setDefaultUserAgentStylesheet(Scene scene, String string) {
        String string2;
        String string3 = string2 = string != null ? string.trim() : null;
        if (string2 == null || string2.isEmpty()) {
            return;
        }
        Object object = styleLock;
        synchronized (object) {
            CssError.setCurrentScene(scene);
            if (this._setDefaultUserAgentStylesheet(string2)) {
                this.userAgentStylesheetsChanged();
            }
            CssError.setCurrentScene(null);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private boolean _setDefaultUserAgentStylesheet(String string) {
        Object object = styleLock;
        synchronized (object) {
            int n2 = this.platformUserAgentStylesheetContainers.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                StylesheetContainer stylesheetContainer = this.platformUserAgentStylesheetContainers.get(i2);
                if (!string.equals(stylesheetContainer.fname)) continue;
                if (i2 > 0) {
                    this.platformUserAgentStylesheetContainers.remove(i2);
                    if (this.hasDefaultUserAgentStylesheet) {
                        this.platformUserAgentStylesheetContainers.set(0, stylesheetContainer);
                    } else {
                        this.platformUserAgentStylesheetContainers.add(0, stylesheetContainer);
                    }
                }
                return i2 > 0;
            }
            Stylesheet stylesheet = StyleManager.loadStylesheet(string);
            if (stylesheet == null) {
                return false;
            }
            stylesheet.setOrigin(StyleOrigin.USER_AGENT);
            StylesheetContainer stylesheetContainer = new StylesheetContainer(string, stylesheet);
            if (this.platformUserAgentStylesheetContainers.size() == 0) {
                this.platformUserAgentStylesheetContainers.add(stylesheetContainer);
            } else if (this.hasDefaultUserAgentStylesheet) {
                this.platformUserAgentStylesheetContainers.set(0, stylesheetContainer);
            } else {
                this.platformUserAgentStylesheetContainers.add(0, stylesheetContainer);
            }
            this.hasDefaultUserAgentStylesheet = true;
            return true;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void removeUserAgentStylesheet(String string) {
        String string2;
        String string3 = string2 = string != null ? string.trim() : null;
        if (string2 == null || string2.isEmpty()) {
            return;
        }
        Object object = styleLock;
        synchronized (object) {
            boolean bl = false;
            for (int i2 = this.platformUserAgentStylesheetContainers.size() - 1; i2 >= 0; --i2) {
                if (string2.equals(Application.getUserAgentStylesheet())) continue;
                StylesheetContainer stylesheetContainer = this.platformUserAgentStylesheetContainers.get(i2);
                if (!string2.equals(stylesheetContainer.fname)) continue;
                this.platformUserAgentStylesheetContainers.remove(i2);
                bl = true;
            }
            if (bl) {
                this.userAgentStylesheetsChanged();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setDefaultUserAgentStylesheet(Stylesheet stylesheet) {
        if (stylesheet == null) {
            return;
        }
        String string = stylesheet.getUrl();
        String string2 = string != null ? string.trim() : "";
        Object object = styleLock;
        synchronized (object) {
            int n2 = this.platformUserAgentStylesheetContainers.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                StylesheetContainer stylesheetContainer = this.platformUserAgentStylesheetContainers.get(i2);
                if (!string2.equals(stylesheetContainer.fname)) continue;
                if (i2 > 0) {
                    this.platformUserAgentStylesheetContainers.remove(i2);
                    if (this.hasDefaultUserAgentStylesheet) {
                        this.platformUserAgentStylesheetContainers.set(0, stylesheetContainer);
                    } else {
                        this.platformUserAgentStylesheetContainers.add(0, stylesheetContainer);
                    }
                }
                return;
            }
            StylesheetContainer stylesheetContainer = new StylesheetContainer(string2, stylesheet);
            if (this.platformUserAgentStylesheetContainers.size() == 0) {
                this.platformUserAgentStylesheetContainers.add(stylesheetContainer);
            } else if (this.hasDefaultUserAgentStylesheet) {
                this.platformUserAgentStylesheetContainers.set(0, stylesheetContainer);
            } else {
                this.platformUserAgentStylesheetContainers.add(0, stylesheetContainer);
            }
            this.hasDefaultUserAgentStylesheet = true;
            stylesheet.setOrigin(StyleOrigin.USER_AGENT);
            this.userAgentStylesheetsChanged();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void userAgentStylesheetsChanged() {
        ArrayList<Parent> arrayList = new ArrayList<Parent>();
        Iterator iterator = styleLock;
        synchronized (iterator) {
            for (CacheContainer object : cacheContainerMap.values()) {
                object.clearCache();
            }
            StyleConverterImpl.clearCache();
            for (Parent parent : cacheContainerMap.keySet()) {
                if (parent == null) continue;
                arrayList.add(parent);
            }
        }
        for (Object object : arrayList) {
            ((Node)object).impl_reapplyCSS();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private List<StylesheetContainer> processStylesheets(List<String> list, Parent parent) {
        Object object = styleLock;
        synchronized (object) {
            ArrayList<StylesheetContainer> arrayList = new ArrayList<StylesheetContainer>();
            int n2 = list.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                Object object2;
                String string = list.get(i2);
                StylesheetContainer stylesheetContainer = null;
                if (this.stylesheetContainerMap.containsKey(string)) {
                    stylesheetContainer = this.stylesheetContainerMap.get(string);
                    if (!arrayList.contains(stylesheetContainer)) {
                        if (stylesheetContainer.checksumInvalid) {
                            object2 = StyleManager.calculateCheckSum(string);
                            if (!Arrays.equals((byte[])object2, stylesheetContainer.checksum)) {
                                this.removeStylesheetContainer(stylesheetContainer);
                                Stylesheet stylesheet = StyleManager.loadStylesheet(string);
                                stylesheetContainer = new StylesheetContainer(string, stylesheet, (byte[])object2);
                                this.stylesheetContainerMap.put(string, stylesheetContainer);
                            } else {
                                stylesheetContainer.checksumInvalid = false;
                            }
                        }
                        arrayList.add(stylesheetContainer);
                    }
                    stylesheetContainer.parentUsers.add(parent);
                    continue;
                }
                object2 = StyleManager.loadStylesheet(string);
                stylesheetContainer = new StylesheetContainer(string, (Stylesheet)object2);
                stylesheetContainer.parentUsers.add(parent);
                this.stylesheetContainerMap.put(string, stylesheetContainer);
                arrayList.add(stylesheetContainer);
            }
            return arrayList;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private List<StylesheetContainer> gatherParentStylesheets(Parent parent) {
        if (parent == null) {
            return Collections.emptyList();
        }
        List<String> list = parent.impl_getAllParentStylesheets();
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        Object object = styleLock;
        synchronized (object) {
            CssError.setCurrentScene(parent.getScene());
            List<StylesheetContainer> list2 = this.processStylesheets(list, parent);
            CssError.setCurrentScene(null);
            return list2;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private List<StylesheetContainer> gatherSceneStylesheets(Scene scene) {
        if (scene == null) {
            return Collections.emptyList();
        }
        ObservableList<String> observableList = scene.getStylesheets();
        if (observableList == null || observableList.isEmpty()) {
            return Collections.emptyList();
        }
        Object object = styleLock;
        synchronized (object) {
            CssError.setCurrentScene(scene);
            List<StylesheetContainer> list = this.processStylesheets(observableList, scene.getRoot());
            CssError.setCurrentScene(null);
            return list;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public StyleMap findMatchingStyles(Node node, SubScene subScene, Set<PseudoClass>[] arrset) {
        Scene scene = node.getScene();
        if (scene == null) {
            return StyleMap.EMPTY_MAP;
        }
        CacheContainer cacheContainer = this.getCacheContainer(node, subScene);
        if (cacheContainer == null) {
            assert (false) : node.toString();
            return StyleMap.EMPTY_MAP;
        }
        Object object = styleLock;
        synchronized (object) {
            Object object2;
            boolean bl;
            Node node2;
            Parent parent = node instanceof Parent ? (Parent)node : node.getParent();
            List<StylesheetContainer> list = this.gatherParentStylesheets(parent);
            boolean bl2 = !list.isEmpty();
            List<StylesheetContainer> list2 = this.gatherSceneStylesheets(scene);
            boolean bl3 = !list2.isEmpty();
            String string = node.getStyle();
            boolean bl4 = string != null && !string.trim().isEmpty();
            String string2 = scene.getUserAgentStylesheet();
            boolean bl5 = string2 != null && !string2.trim().isEmpty();
            String string3 = subScene != null ? subScene.getUserAgentStylesheet() : null;
            boolean bl6 = string3 != null && !string3.trim().isEmpty();
            String string4 = null;
            for (node2 = node; node2 != null; node2 = node2.getParent()) {
                String string5 = string4 = node2 instanceof Region ? ((Region)node2).getUserAgentStylesheet() : null;
                if (string4 != null) break;
            }
            boolean bl7 = bl = string4 != null && !string4.trim().isEmpty();
            if (!(bl4 || bl2 || bl3 || bl5 || bl6 || bl || !this.platformUserAgentStylesheetContainers.isEmpty())) {
                return StyleMap.EMPTY_MAP;
            }
            String string6 = node.getTypeSelector();
            String string7 = node.getId();
            ObservableList<String> observableList = node.getStyleClass();
            if (this.key == null) {
                this.key = new Key();
            }
            this.key.className = string6;
            this.key.id = string7;
            int n2 = observableList.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                object2 = (String)observableList.get(i2);
                if (object2 == null || ((String)object2).isEmpty()) continue;
                this.key.styleClasses.add(StyleClassSet.getStyleClass((String)object2));
            }
            Map map = cacheContainer.getCacheMap(list, string4);
            Cache cache = (Cache)map.get(this.key);
            if (cache != null) {
                this.key.styleClasses.clear();
            } else {
                int n3;
                Object object3;
                object2 = new ArrayList();
                if (bl6 || bl5) {
                    object3 = bl6 ? subScene.getUserAgentStylesheet().trim() : scene.getUserAgentStylesheet().trim();
                    StylesheetContainer stylesheetContainer = null;
                    int n4 = this.userAgentStylesheetContainers.size();
                    for (n3 = 0; n3 < n4; ++n3) {
                        stylesheetContainer = this.userAgentStylesheetContainers.get(n3);
                        if (((String)object3).equals(stylesheetContainer.fname)) break;
                        stylesheetContainer = null;
                    }
                    if (stylesheetContainer == null) {
                        Stylesheet stylesheet = StyleManager.loadStylesheet((String)object3);
                        if (stylesheet != null) {
                            stylesheet.setOrigin(StyleOrigin.USER_AGENT);
                        }
                        stylesheetContainer = new StylesheetContainer((String)object3, stylesheet);
                        this.userAgentStylesheetContainers.add(stylesheetContainer);
                    }
                    if (stylesheetContainer.selectorPartitioning != null) {
                        Parent parent2 = bl6 ? subScene.getRoot() : scene.getRoot();
                        stylesheetContainer.parentUsers.add(parent2);
                        List<Selector> list3 = stylesheetContainer.selectorPartitioning.match(string7, string6, this.key.styleClasses);
                        object2.addAll(list3);
                    }
                } else if (!this.platformUserAgentStylesheetContainers.isEmpty()) {
                    int n5 = this.platformUserAgentStylesheetContainers.size();
                    for (int i3 = 0; i3 < n5; ++i3) {
                        StylesheetContainer stylesheetContainer = this.platformUserAgentStylesheetContainers.get(i3);
                        if (stylesheetContainer == null || stylesheetContainer.selectorPartitioning == null) continue;
                        List<Selector> list4 = stylesheetContainer.selectorPartitioning.match(string7, string6, this.key.styleClasses);
                        object2.addAll(list4);
                    }
                }
                if (bl) {
                    object3 = null;
                    n3 = this.userAgentStylesheetContainers.size();
                    for (int i4 = 0; i4 < n3; ++i4) {
                        object3 = this.userAgentStylesheetContainers.get(i4);
                        if (string4.equals(((StylesheetContainer)object3).fname)) break;
                        object3 = null;
                    }
                    if (object3 == null) {
                        Stylesheet stylesheet = StyleManager.loadStylesheet(string4);
                        if (stylesheet != null) {
                            stylesheet.setOrigin(StyleOrigin.USER_AGENT);
                        }
                        object3 = new StylesheetContainer(string4, stylesheet);
                        this.userAgentStylesheetContainers.add((StylesheetContainer)object3);
                    }
                    if (((StylesheetContainer)object3).selectorPartitioning != null) {
                        ((StylesheetContainer)object3).parentUsers.add((Parent)node2);
                        List<Selector> list5 = ((StylesheetContainer)object3).selectorPartitioning.match(string7, string6, this.key.styleClasses);
                        object2.addAll(list5);
                    }
                }
                if (!list2.isEmpty()) {
                    int n6 = list2.size();
                    for (int i5 = 0; i5 < n6; ++i5) {
                        StylesheetContainer stylesheetContainer = list2.get(i5);
                        if (stylesheetContainer == null || stylesheetContainer.selectorPartitioning == null) continue;
                        List<Selector> list6 = stylesheetContainer.selectorPartitioning.match(string7, string6, this.key.styleClasses);
                        object2.addAll(list6);
                    }
                }
                if (bl2) {
                    int n7 = list == null ? 0 : list.size();
                    for (int i6 = 0; i6 < n7; ++i6) {
                        StylesheetContainer stylesheetContainer = list.get(i6);
                        if (stylesheetContainer.selectorPartitioning == null) continue;
                        List<Selector> list7 = stylesheetContainer.selectorPartitioning.match(string7, string6, this.key.styleClasses);
                        object2.addAll(list7);
                    }
                }
                cache = new Cache((List<Selector>)object2);
                map.put(this.key, cache);
                this.key = null;
            }
            object2 = cache.getStyleMap(cacheContainer, node, arrset, bl4);
            return object2;
        }
    }

    public static ObservableList<CssError> errorsProperty() {
        if (errors == null) {
            errors = FXCollections.observableArrayList();
        }
        return errors;
    }

    public static ObservableList<CssError> getErrors() {
        return errors;
    }

    static {
        cacheContainerMap = new WeakHashMap<Parent, CacheContainer>();
        errors = null;
    }

    private static class Key {
        String className;
        String id;
        final StyleClassSet styleClasses = new StyleClassSet();

        private Key() {
        }

        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (object instanceof Key) {
                Key key = (Key)object;
                if (this.className == null ? key.className != null : !this.className.equals(key.className)) {
                    return false;
                }
                if (this.id == null ? key.id != null : !this.id.equals(key.id)) {
                    return false;
                }
                return this.styleClasses.equals(key.styleClasses);
            }
            return true;
        }

        public int hashCode() {
            int n2 = 7;
            n2 = 29 * n2 + (this.className != null ? this.className.hashCode() : 0);
            n2 = 29 * n2 + (this.id != null ? this.id.hashCode() : 0);
            n2 = 29 * n2 + this.styleClasses.hashCode();
            return n2;
        }
    }

    private static class Cache {
        private final List<Selector> selectors;
        private final Map<Key, Integer> cache;

        Cache(List<Selector> list) {
            this.selectors = list;
            this.cache = new HashMap<Key, Integer>();
        }

        private StyleMap getStyleMap(CacheContainer cacheContainer, Node node, Set<PseudoClass>[] arrset, boolean bl) {
            int n2;
            Selector selector;
            Object object;
            if ((this.selectors == null || this.selectors.isEmpty()) && !bl) {
                return StyleMap.EMPTY_MAP;
            }
            int n3 = this.selectors.size();
            long[] arrl = new long[n3 / 64 + 1];
            boolean bl2 = true;
            for (int i2 = 0; i2 < n3; ++i2) {
                long l2;
                object = this.selectors.get(i2);
                if (!((Selector)object).applies(node, arrset, 0)) continue;
                int n4 = i2 / 64;
                arrl[n4] = l2 = arrl[n4] | 1L << i2;
                bl2 = false;
            }
            if (bl2 && !bl) {
                return StyleMap.EMPTY_MAP;
            }
            String string = node.getStyle();
            object = new Key(arrl, string);
            if (this.cache.containsKey(object)) {
                Integer n5 = this.cache.get(object);
                StyleMap styleMap = n5 != null ? cacheContainer.getStyleMap(n5) : StyleMap.EMPTY_MAP;
                return styleMap;
            }
            ArrayList<Selector> arrayList = new ArrayList<Selector>();
            if (bl && (selector = cacheContainer.getInlineStyleSelector(string)) != null) {
                arrayList.add(selector);
            }
            for (n2 = 0; n2 < arrl.length; ++n2) {
                if (arrl[n2] == 0L) continue;
                int n6 = n2 * 64;
                for (int i3 = 0; i3 < 64; ++i3) {
                    long l3 = 1L << i3;
                    if ((l3 & arrl[n2]) != l3) continue;
                    Selector selector2 = this.selectors.get(n6 + i3);
                    arrayList.add(selector2);
                }
            }
            n2 = cacheContainer.nextSmapId();
            this.cache.put((Key)object, n2);
            StyleMap styleMap = new StyleMap(n2, arrayList);
            cacheContainer.addStyleMap(styleMap);
            return styleMap;
        }

        private static class Key {
            final long[] key;
            final String inlineStyle;

            Key(long[] arrl, String string) {
                this.key = arrl;
                this.inlineStyle = string != null && string.trim().isEmpty() ? null : string;
            }

            public String toString() {
                return Arrays.toString(this.key) + (this.inlineStyle != null ? "* {" + this.inlineStyle + "}" : "");
            }

            public int hashCode() {
                int n2 = 3;
                n2 = 17 * n2 + Arrays.hashCode(this.key);
                if (this.inlineStyle != null) {
                    n2 = 17 * n2 + this.inlineStyle.hashCode();
                }
                return n2;
            }

            public boolean equals(Object object) {
                if (object == null) {
                    return false;
                }
                if (this.getClass() != object.getClass()) {
                    return false;
                }
                Key key = (Key)object;
                if (this.inlineStyle == null ? key.inlineStyle != null : !this.inlineStyle.equals(key.inlineStyle)) {
                    return false;
                }
                return Arrays.equals(this.key, key.key);
            }
        }
    }

    static class CacheContainer {
        private Map<StyleCache.Key, StyleCache> styleCache;
        private Map<List<String>, Map<Key, Cache>> cacheMap;
        private List<StyleMap> styleMapList;
        private Map<String, Selector> inlineStylesCache;
        private int styleMapId = 0;
        private int baseStyleMapId = 0;

        CacheContainer() {
        }

        private Map<StyleCache.Key, StyleCache> getStyleCache() {
            if (this.styleCache == null) {
                this.styleCache = new HashMap<StyleCache.Key, StyleCache>();
            }
            return this.styleCache;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        private Map<Key, Cache> getCacheMap(List<StylesheetContainer> list, String string) {
            if (this.cacheMap == null) {
                this.cacheMap = new HashMap<List<String>, Map<Key, Cache>>();
            }
            Object object = styleLock;
            synchronized (object) {
                Map<Key, Cache> map;
                if ((list == null || list.isEmpty()) && (string == null || string.isEmpty())) {
                    Map<Key, Cache> map2 = this.cacheMap.get(null);
                    if (map2 == null) {
                        map2 = new HashMap<Key, Cache>();
                        this.cacheMap.put(null, map2);
                    }
                    return map2;
                }
                int n2 = list.size();
                if (cacheMapKey == null) {
                    cacheMapKey = new ArrayList(n2);
                }
                for (int i2 = 0; i2 < n2; ++i2) {
                    StylesheetContainer stylesheetContainer = list.get(i2);
                    if (stylesheetContainer == null || stylesheetContainer.fname == null || stylesheetContainer.fname.isEmpty()) continue;
                    cacheMapKey.add(stylesheetContainer.fname);
                }
                if (string != null) {
                    cacheMapKey.add(string);
                }
                if ((map = this.cacheMap.get(cacheMapKey)) == null) {
                    map = new HashMap<Key, Cache>();
                    this.cacheMap.put(cacheMapKey, map);
                    cacheMapKey = null;
                } else {
                    cacheMapKey.clear();
                }
                return map;
            }
        }

        private List<StyleMap> getStyleMapList() {
            if (this.styleMapList == null) {
                this.styleMapList = new ArrayList<StyleMap>();
            }
            return this.styleMapList;
        }

        private int nextSmapId() {
            this.styleMapId = this.baseStyleMapId + this.getStyleMapList().size();
            return this.styleMapId;
        }

        private void addStyleMap(StyleMap styleMap) {
            this.getStyleMapList().add(styleMap);
        }

        public StyleMap getStyleMap(int n2) {
            int n3 = n2 - this.baseStyleMapId;
            if (0 <= n3 && n3 < this.getStyleMapList().size()) {
                return this.getStyleMapList().get(n3);
            }
            return StyleMap.EMPTY_MAP;
        }

        private void clearCache() {
            if (this.cacheMap != null) {
                this.cacheMap.clear();
            }
            if (this.styleCache != null) {
                this.styleCache.clear();
            }
            if (this.styleMapList != null) {
                this.styleMapList.clear();
            }
            this.baseStyleMapId = this.styleMapId;
            if (this.baseStyleMapId > 0x6FFFFFF9) {
                this.styleMapId = 0;
                this.baseStyleMapId = 0;
            }
        }

        private Selector getInlineStyleSelector(String string) {
            Stylesheet stylesheet;
            if (string == null || string.trim().isEmpty()) {
                return null;
            }
            if (this.inlineStylesCache != null && this.inlineStylesCache.containsKey(string)) {
                return this.inlineStylesCache.get(string);
            }
            if (this.inlineStylesCache == null) {
                this.inlineStylesCache = new HashMap<String, Selector>();
            }
            if ((stylesheet = new CSSParser().parse("*{" + string + "}")) != null) {
                Selector selector;
                stylesheet.setOrigin(StyleOrigin.INLINE);
                List<Rule> list = stylesheet.getRules();
                Rule rule = list != null && !list.isEmpty() ? list.get(0) : null;
                List<Selector> list2 = rule != null ? rule.getUnobservedSelectorList() : null;
                Selector selector2 = selector = list2 != null && !list2.isEmpty() ? list2.get(0) : null;
                if (selector != null) {
                    selector.setOrdinal(-1);
                    this.inlineStylesCache.put(string, selector);
                    return selector;
                }
            }
            this.inlineStylesCache.put(string, null);
            return null;
        }
    }

    static class RefList<K> {
        final List<Reference<K>> list = new ArrayList<Reference<K>>();

        RefList() {
        }

        void add(K k2) {
            for (int i2 = this.list.size() - 1; 0 <= i2; --i2) {
                Reference<K> reference = this.list.get(i2);
                K k3 = reference.get();
                if (k3 == null) {
                    this.list.remove(i2);
                    continue;
                }
                if (k3 != k2) continue;
                return;
            }
            this.list.add(new WeakReference<K>(k2));
        }

        void remove(K k2) {
            for (int i2 = this.list.size() - 1; 0 <= i2; --i2) {
                Reference<K> reference = this.list.get(i2);
                K k3 = reference.get();
                if (k3 == null) {
                    this.list.remove(i2);
                    continue;
                }
                if (k3 != k2) continue;
                this.list.remove(i2);
                return;
            }
        }

        boolean contains(K k2) {
            for (int i2 = this.list.size() - 1; 0 <= i2; --i2) {
                Reference<K> reference = this.list.get(i2);
                K k3 = reference.get();
                if (k3 != k2) continue;
                return true;
            }
            return false;
        }
    }

    static class StylesheetContainer {
        final String fname;
        final Stylesheet stylesheet;
        final SelectorPartitioning selectorPartitioning;
        final RefList<Parent> parentUsers;
        final List<Image> imageCache;
        final int hash;
        final byte[] checksum;
        boolean checksumInvalid = false;

        StylesheetContainer(String string, Stylesheet stylesheet) {
            this(string, stylesheet, stylesheet != null ? StyleManager.calculateCheckSum(stylesheet.getUrl()) : new byte[0]);
        }

        StylesheetContainer(String string, Stylesheet stylesheet, byte[] arrby) {
            this.fname = string;
            this.hash = string != null ? string.hashCode() : 127;
            this.stylesheet = stylesheet;
            if (stylesheet != null) {
                this.selectorPartitioning = new SelectorPartitioning();
                List<Rule> list = stylesheet.getRules();
                int n2 = list == null || list.isEmpty() ? 0 : list.size();
                for (int i2 = 0; i2 < n2; ++i2) {
                    Rule rule = list.get(i2);
                    List<Selector> list2 = rule.getUnobservedSelectorList();
                    int n3 = list2 == null || list2.isEmpty() ? 0 : list2.size();
                    for (int i3 = 0; i3 < n3; ++i3) {
                        Selector selector = list2.get(i3);
                        this.selectorPartitioning.partition(selector);
                    }
                }
            } else {
                this.selectorPartitioning = null;
            }
            this.parentUsers = new RefList();
            this.imageCache = new ArrayList<Image>();
            this.checksum = arrby;
        }

        void invalidateChecksum() {
            this.checksumInvalid = this.checksum.length > 0;
        }

        public int hashCode() {
            return this.hash;
        }

        public boolean equals(Object object) {
            if (object == null) {
                return false;
            }
            if (this.getClass() != object.getClass()) {
                return false;
            }
            StylesheetContainer stylesheetContainer = (StylesheetContainer)object;
            return !(this.fname == null ? stylesheetContainer.fname != null : !this.fname.equals(stylesheetContainer.fname));
        }

        public String toString() {
            return this.fname;
        }
    }

    private static class InstanceHolder {
        static final StyleManager INSTANCE = new StyleManager();

        private InstanceHolder() {
        }
    }
}

