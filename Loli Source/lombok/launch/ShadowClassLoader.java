/*
 * Decompiled with CFR 0.150.
 */
package lombok.launch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
class ShadowClassLoader
extends ClassLoader {
    private static final String SELF_NAME = "lombok/launch/ShadowClassLoader.class";
    private static final ConcurrentMap<String, Class<?>> highlanderMap = new ConcurrentHashMap();
    private final String SELF_BASE;
    private final File SELF_BASE_FILE;
    private final int SELF_BASE_LENGTH;
    private final List<File> override = new ArrayList<File>();
    private final String sclSuffix;
    private final List<String> parentExclusion = new ArrayList<String>();
    private final List<String> highlanders = new ArrayList<String>();
    private final Map<String, Object> mapJarPathToTracker = new HashMap<String, Object>();
    private static final Map<Object, String> mapTrackerToJarPath = new WeakHashMap<Object, String>();
    private static final Map<Object, Set<String>> mapTrackerToJarContents = new WeakHashMap<Object, Set<String>>();
    private Map<String, Boolean> fileRootCache = new HashMap<String, Boolean>();
    private Map<String, Boolean> jarLocCache = new HashMap<String, Boolean>();

    ShadowClassLoader(ClassLoader source, String sclSuffix, String selfBase, List<String> parentExclusion, List<String> highlanders) {
        super(source);
        this.sclSuffix = sclSuffix;
        if (parentExclusion != null) {
            for (String pe : parentExclusion) {
                if (!(pe = pe.replace(".", "/")).endsWith("/")) {
                    pe = String.valueOf(pe) + "/";
                }
                this.parentExclusion.add(pe);
            }
        }
        if (highlanders != null) {
            for (String hl : highlanders) {
                this.highlanders.add(hl);
            }
        }
        if (selfBase != null) {
            this.SELF_BASE = selfBase;
            this.SELF_BASE_LENGTH = selfBase.length();
        } else {
            String decoded;
            String sclClassStr;
            URL sclClassUrl = ShadowClassLoader.class.getResource("ShadowClassLoader.class");
            String string = sclClassStr = sclClassUrl == null ? null : sclClassUrl.toString();
            if (sclClassStr == null || !sclClassStr.endsWith(SELF_NAME)) {
                ClassLoader cl = ShadowClassLoader.class.getClassLoader();
                throw new RuntimeException("ShadowLoader can't find itself. SCL loader type: " + (cl == null ? "*NULL*" : cl.getClass().toString()));
            }
            this.SELF_BASE_LENGTH = sclClassStr.length() - SELF_NAME.length();
            this.SELF_BASE = decoded = ShadowClassLoader.urlDecode(sclClassStr.substring(0, this.SELF_BASE_LENGTH));
        }
        this.SELF_BASE_FILE = this.SELF_BASE.startsWith("jar:file:") && this.SELF_BASE.endsWith("!/") ? new File(this.SELF_BASE.substring(9, this.SELF_BASE.length() - 2)) : (this.SELF_BASE.startsWith("file:") ? new File(this.SELF_BASE.substring(5)) : new File(this.SELF_BASE));
        String scl = System.getProperty("shadow.override." + sclSuffix);
        if (scl != null && !scl.isEmpty()) {
            for (String part : scl.split("\\s*" + (File.pathSeparatorChar == ';' ? ";" : ":") + "\\s*")) {
                if (part.endsWith("/*") || part.endsWith(String.valueOf(File.separator) + "*")) {
                    this.addOverrideJarDir(part.substring(0, part.length() - 2));
                    continue;
                }
                this.addOverrideClasspathEntry(part);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private Set<String> getOrMakeJarListing(String absolutePathToJar) {
        Map<Object, String> map = mapTrackerToJarPath;
        synchronized (map) {
            Object ourTracker = this.mapJarPathToTracker.get(absolutePathToJar);
            if (ourTracker != null) {
                return mapTrackerToJarContents.get(ourTracker);
            }
            for (Map.Entry<Object, String> entry : mapTrackerToJarPath.entrySet()) {
                if (!entry.getValue().equals(absolutePathToJar)) continue;
                Object otherTracker = entry.getKey();
                this.mapJarPathToTracker.put(absolutePathToJar, otherTracker);
                return mapTrackerToJarContents.get(otherTracker);
            }
            Object newTracker = new Object();
            Set<String> jarMembers = this.getJarMemberSet(absolutePathToJar);
            mapTrackerToJarContents.put(newTracker, jarMembers);
            mapTrackerToJarPath.put(newTracker, absolutePathToJar);
            this.mapJarPathToTracker.put(absolutePathToJar, newTracker);
            return jarMembers;
        }
    }

    private Set<String> getJarMemberSet(String absolutePathToJar) {
        try {
            int shiftBits = 1;
            JarFile jar = new JarFile(absolutePathToJar);
            int jarSizePower2 = Integer.highestOneBit(jar.size());
            if (jarSizePower2 != jar.size()) {
                jarSizePower2 <<= 1;
            }
            if (jarSizePower2 == 0) {
                jarSizePower2 = 1;
            }
            HashSet<String> jarMembers = new HashSet<String>(jarSizePower2 >> shiftBits, 1 << shiftBits);
            try {
                try {
                    Enumeration<JarEntry> entries = jar.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry jarEntry = entries.nextElement();
                        if (jarEntry.isDirectory()) continue;
                        jarMembers.add(jarEntry.getName());
                    }
                }
                catch (Exception exception) {
                    jar.close();
                }
            }
            finally {
                jar.close();
            }
            return jarMembers;
        }
        catch (Exception exception) {
            return Collections.emptySet();
        }
    }

    private URL getResourceFromLocation(String name, String altName, File location) {
        File absoluteFile;
        if (location.isDirectory()) {
            try {
                File f2;
                if (altName != null && (f2 = new File(location, altName)).isFile() && f2.canRead()) {
                    return f2.toURI().toURL();
                }
                f2 = new File(location, name);
                if (f2.isFile() && f2.canRead()) {
                    return f2.toURI().toURL();
                }
                return null;
            }
            catch (MalformedURLException malformedURLException) {
                return null;
            }
        }
        if (!location.isFile() || !location.canRead()) {
            return null;
        }
        try {
            absoluteFile = location.getCanonicalFile();
        }
        catch (Exception exception) {
            absoluteFile = location.getAbsoluteFile();
        }
        Set<String> jarContents = this.getOrMakeJarListing(absoluteFile.getAbsolutePath());
        String absoluteUri = absoluteFile.toURI().toString();
        try {
            if (jarContents.contains(altName)) {
                return new URI("jar:" + absoluteUri + "!/" + altName).toURL();
            }
        }
        catch (Exception exception) {}
        try {
            if (jarContents.contains(name)) {
                return new URI("jar:" + absoluteUri + "!/" + name).toURL();
            }
        }
        catch (Exception exception) {}
        return null;
    }

    private boolean partOfShadow(String item, String name) {
        return !name.startsWith("java/") && !name.startsWith("sun/") && (this.inOwnBase(item, name) || this.isPartOfShadowSuffix(item, name, this.sclSuffix));
    }

    private boolean inOwnBase(String item, String name) {
        if (item == null) {
            return false;
        }
        return item.length() == this.SELF_BASE_LENGTH + name.length() && this.SELF_BASE.regionMatches(0, item, 0, this.SELF_BASE_LENGTH);
    }

    private static boolean sclFileContainsSuffix(InputStream in, String suffix) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String line = br.readLine();
        while (line != null) {
            if (!(line = line.trim()).isEmpty() && line.charAt(0) != '#' && line.equals(suffix)) {
                return true;
            }
            line = br.readLine();
        }
        return false;
    }

    private static String urlDecode(String in) {
        try {
            return URLDecoder.decode(in, "UTF-8");
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            throw new InternalError("UTF-8 not supported");
        }
    }

    private boolean isPartOfShadowSuffixFileBased(String fileRoot, String suffix) {
        boolean bl;
        String key = String.valueOf(fileRoot) + "::" + suffix;
        Boolean existing = this.fileRootCache.get(key);
        if (existing != null) {
            return existing;
        }
        File f2 = new File(String.valueOf(fileRoot) + "/META-INF/ShadowClassLoader");
        FileInputStream fis = new FileInputStream(f2);
        try {
            boolean v2 = ShadowClassLoader.sclFileContainsSuffix(fis, suffix);
            this.fileRootCache.put(key, v2);
            bl = v2;
        }
        catch (Throwable throwable) {
            try {
                fis.close();
                throw throwable;
            }
            catch (FileNotFoundException fileNotFoundException) {
                this.fileRootCache.put(key, false);
                return false;
            }
            catch (IOException iOException) {
                this.fileRootCache.put(key, false);
                return false;
            }
        }
        fis.close();
        return bl;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private boolean isPartOfShadowSuffixJarBased(String jarLoc, String suffix) {
        String key = String.valueOf(jarLoc) + "::" + suffix;
        Boolean existing = this.jarLocCache.get(key);
        if (existing != null) {
            return existing;
        }
        if (jarLoc.startsWith("file:/")) {
            jarLoc = ShadowClassLoader.urlDecode(jarLoc.substring(5));
        }
        try {
            FileInputStream jar = new FileInputStream(jarLoc);
            try {
                ZipEntry entry;
                ZipInputStream zip = new ZipInputStream(jar);
                do {
                    if ((entry = zip.getNextEntry()) != null) continue;
                    this.jarLocCache.put(key, false);
                    return false;
                } while (!"META-INF/ShadowClassLoader".equals(entry.getName()));
                boolean v2 = ShadowClassLoader.sclFileContainsSuffix(zip, suffix);
                this.jarLocCache.put(key, v2);
                boolean bl = v2;
                return bl;
            }
            finally {
                jar.close();
            }
        }
        catch (FileNotFoundException fileNotFoundException) {
            this.jarLocCache.put(key, false);
            return false;
        }
        catch (IOException iOException) {
            this.jarLocCache.put(key, false);
            return false;
        }
    }

    private boolean isPartOfShadowSuffix(String url, String name, String suffix) {
        if (url == null) {
            return false;
        }
        if (url.startsWith("file:/")) {
            if ((url = ShadowClassLoader.urlDecode(url.substring(5))).length() <= name.length() || !url.endsWith(name) || url.charAt(url.length() - name.length() - 1) != '/') {
                return false;
            }
            String fileRoot = url.substring(0, url.length() - name.length() - 1);
            return this.isPartOfShadowSuffixFileBased(fileRoot, suffix);
        }
        if (url.startsWith("jar:")) {
            int sep = url.indexOf(33);
            if (sep == -1) {
                return false;
            }
            String jarLoc = url.substring(4, sep);
            return this.isPartOfShadowSuffixJarBased(jarLoc, suffix);
        }
        return false;
    }

    @Override
    public Enumeration<URL> getResources(String name) throws IOException {
        URL fromSelf;
        String altName = null;
        if (name.endsWith(".class")) {
            altName = String.valueOf(name.substring(0, name.length() - 6)) + ".SCL." + this.sclSuffix;
        }
        Vector<URL> vector = new Vector<URL>();
        for (File ce : this.override) {
            URL url = this.getResourceFromLocation(name, altName, ce);
            if (url == null) continue;
            vector.add(url);
        }
        if (this.override.isEmpty() && (fromSelf = this.getResourceFromLocation(name, altName, this.SELF_BASE_FILE)) != null) {
            vector.add(fromSelf);
        }
        Enumeration<URL> sec = super.getResources(name);
        while (sec.hasMoreElements()) {
            URL item = sec.nextElement();
            if (this.partOfShadow(item.toString(), name)) continue;
            vector.add(item);
        }
        if (altName != null) {
            Enumeration<URL> tern = super.getResources(altName);
            while (tern.hasMoreElements()) {
                URL item = tern.nextElement();
                if (this.partOfShadow(item.toString(), altName)) continue;
                vector.add(item);
            }
        }
        return vector.elements();
    }

    @Override
    public URL getResource(String name) {
        return this.getResource_(name, false);
    }

    private URL getResource_(String name, boolean noSuper) {
        URL res;
        String altName = null;
        if (name.endsWith(".class")) {
            altName = String.valueOf(name.substring(0, name.length() - 6)) + ".SCL." + this.sclSuffix;
        }
        for (File ce : this.override) {
            URL url = this.getResourceFromLocation(name, altName, ce);
            if (url == null) continue;
            return url;
        }
        if (!this.override.isEmpty()) {
            if (noSuper) {
                return null;
            }
            if (altName != null) {
                try {
                    URL res2 = this.getResourceSkippingSelf(altName);
                    if (res2 != null) {
                        return res2;
                    }
                }
                catch (IOException iOException) {}
            }
            try {
                return this.getResourceSkippingSelf(name);
            }
            catch (IOException iOException) {
                return null;
            }
        }
        URL url = this.getResourceFromLocation(name, altName, this.SELF_BASE_FILE);
        if (url != null) {
            return url;
        }
        if (altName != null && (res = super.getResource(altName)) != null && (!noSuper || this.partOfShadow(res.toString(), altName))) {
            return res;
        }
        res = super.getResource(name);
        if (res != null && (!noSuper || this.partOfShadow(res.toString(), name))) {
            return res;
        }
        return null;
    }

    private boolean exclusionListMatch(String name) {
        for (String pe : this.parentExclusion) {
            if (!name.startsWith(pe)) continue;
            return true;
        }
        return false;
    }

    private URL getResourceSkippingSelf(String name) throws IOException {
        URL candidate = super.getResource(name);
        if (candidate == null) {
            return null;
        }
        if (!this.partOfShadow(candidate.toString(), name)) {
            return candidate;
        }
        Enumeration<URL> en = super.getResources(name);
        while (en.hasMoreElements()) {
            candidate = en.nextElement();
            if (this.partOfShadow(candidate.toString(), name)) continue;
            return candidate;
        }
        return null;
    }

    @Override
    public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> alreadyDefined;
        Class<?> c2;
        block22: {
            byte[] b2;
            URL res;
            block20: {
                Class c3;
                Class<?> alreadyLoaded = this.findLoadedClass(name);
                if (alreadyLoaded != null) {
                    return alreadyLoaded;
                }
                if (this.highlanders.contains(name) && (c3 = (Class)highlanderMap.get(name)) != null) {
                    return c3;
                }
                String fileNameOfClass = String.valueOf(name.replace(".", "/")) + ".class";
                res = this.getResource_(fileNameOfClass, true);
                if (res == null && !this.exclusionListMatch(fileNameOfClass)) {
                    try {
                        return super.loadClass(name, resolve);
                    }
                    catch (ClassNotFoundException cnfe) {
                        res = this.getResource_("secondaryLoading.SCL." + this.sclSuffix + "/" + name.replace(".", "/") + ".SCL." + this.sclSuffix, true);
                        if (res != null) break block20;
                        throw cnfe;
                    }
                }
            }
            if (res == null) {
                throw new ClassNotFoundException(name);
            }
            int p2 = 0;
            try {
                InputStream in = res.openStream();
                try {
                    int r2;
                    b2 = new byte[65536];
                    while ((r2 = in.read(b2, p2, b2.length - p2)) != -1) {
                        if ((p2 += r2) != b2.length) continue;
                        byte[] nb = new byte[b2.length * 2];
                        System.arraycopy(b2, 0, nb, 0, p2);
                        b2 = nb;
                    }
                }
                finally {
                    in.close();
                }
            }
            catch (IOException e2) {
                throw new ClassNotFoundException("I/O exception reading class " + name, e2);
            }
            try {
                c2 = this.defineClass(name, b2, 0, p2);
            }
            catch (LinkageError e3) {
                Class alreadyDefined2;
                if (this.highlanders.contains(name) && (alreadyDefined2 = (Class)highlanderMap.get(name)) != null) {
                    return alreadyDefined2;
                }
                try {
                    c2 = this.findLoadedClass(name);
                }
                catch (LinkageError linkageError) {
                    throw e3;
                }
                if (c2 != null) break block22;
                throw e3;
            }
        }
        if (this.highlanders.contains(name) && (alreadyDefined = highlanderMap.putIfAbsent(name, c2)) != null) {
            c2 = alreadyDefined;
        }
        if (resolve) {
            this.resolveClass(c2);
        }
        return c2;
    }

    public void addOverrideJarDir(String dir) {
        File f2 = new File(dir);
        for (File j2 : f2.listFiles()) {
            if (!j2.getName().toLowerCase().endsWith(".jar") || !j2.canRead() || !j2.isFile()) continue;
            this.override.add(j2);
        }
    }

    public void addOverrideClasspathEntry(String entry) {
        this.override.add(new File(entry));
    }
}

