/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import loliland.launcher.client.lOilLanD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class liOIOOlLAnD {
    private static final Logger I1O1I1LaNd = LoggerFactory.getLogger(liOIOOlLAnD.class);
    private static final String OOOIilanD = "Reading file {}";
    private static final String lI00OlAND = "Read {}";

    private liOIOOlLAnD() {
    }

    public static List I1O1I1LaNd(String string) {
        return liOIOOlLAnD.I1O1I1LaNd(string, true);
    }

    public static List I1O1I1LaNd(String string, boolean bl) {
        if (new File(string).canRead()) {
            if (I1O1I1LaNd.isDebugEnabled()) {
                I1O1I1LaNd.debug(OOOIilanD, (Object)string);
            }
            try {
                return Files.readAllLines(Paths.get(string, new String[0]), StandardCharsets.UTF_8);
            }
            catch (IOException iOException) {
                if (bl) {
                    I1O1I1LaNd.error("Error reading file {}. {}", (Object)string, (Object)iOException.getMessage());
                } else {
                    I1O1I1LaNd.debug("Error reading file {}. {}", (Object)string, (Object)iOException.getMessage());
                }
            }
        } else if (bl) {
            I1O1I1LaNd.warn("File not found or not readable: {}", (Object)string);
        }
        return new ArrayList();
    }

    public static long OOOIilanD(String string) {
        List list;
        if (I1O1I1LaNd.isDebugEnabled()) {
            I1O1I1LaNd.debug(OOOIilanD, (Object)string);
        }
        if (!(list = liOIOOlLAnD.I1O1I1LaNd(string, false)).isEmpty()) {
            if (I1O1I1LaNd.isTraceEnabled()) {
                I1O1I1LaNd.trace(lI00OlAND, list.get(0));
            }
            return lOilLanD.OOOIilanD((String)list.get(0), 0L);
        }
        return 0L;
    }

    public static long lI00OlAND(String string) {
        List list;
        if (I1O1I1LaNd.isDebugEnabled()) {
            I1O1I1LaNd.debug(OOOIilanD, (Object)string);
        }
        if (!(list = liOIOOlLAnD.I1O1I1LaNd(string, false)).isEmpty()) {
            if (I1O1I1LaNd.isTraceEnabled()) {
                I1O1I1LaNd.trace(lI00OlAND, list.get(0));
            }
            return lOilLanD.lI00OlAND((String)list.get(0), 0L);
        }
        return 0L;
    }

    public static int lli0OiIlAND(String string) {
        if (I1O1I1LaNd.isDebugEnabled()) {
            I1O1I1LaNd.debug(OOOIilanD, (Object)string);
        }
        try {
            List list = liOIOOlLAnD.I1O1I1LaNd(string, false);
            if (!list.isEmpty()) {
                if (I1O1I1LaNd.isTraceEnabled()) {
                    I1O1I1LaNd.trace(lI00OlAND, list.get(0));
                }
                return Integer.parseInt((String)list.get(0));
            }
        }
        catch (NumberFormatException numberFormatException) {
            I1O1I1LaNd.warn("Unable to read value from {}. {}", (Object)string, (Object)numberFormatException.getMessage());
        }
        return 0;
    }

    public static String li0iOILAND(String string) {
        List list;
        if (I1O1I1LaNd.isDebugEnabled()) {
            I1O1I1LaNd.debug(OOOIilanD, (Object)string);
        }
        if (!(list = liOIOOlLAnD.I1O1I1LaNd(string, false)).isEmpty()) {
            if (I1O1I1LaNd.isTraceEnabled()) {
                I1O1I1LaNd.trace(lI00OlAND, list.get(0));
            }
            return (String)list.get(0);
        }
        return "";
    }

    public static Map I1O1I1LaNd(String string, String string2) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        if (I1O1I1LaNd.isDebugEnabled()) {
            I1O1I1LaNd.debug(OOOIilanD, (Object)string);
        }
        List list = liOIOOlLAnD.I1O1I1LaNd(string, false);
        for (String string3 : list) {
            String[] arrstring = string3.split(string2);
            if (arrstring.length != 2) continue;
            hashMap.put(arrstring[0], arrstring[1].trim());
        }
        return hashMap;
    }

    public static Properties O1il1llOLANd(String string) {
        Properties properties = new Properties();
        for (ClassLoader classLoader : Stream.of(Thread.currentThread().getContextClassLoader(), ClassLoader.getSystemClassLoader(), liOIOOlLAnD.class.getClassLoader()).collect(Collectors.toCollection(LinkedHashSet::new))) {
            if (!liOIOOlLAnD.I1O1I1LaNd(string, properties, classLoader)) continue;
            return properties;
        }
        I1O1I1LaNd.warn("Failed to load default configuration");
        return properties;
    }

    private static boolean I1O1I1LaNd(String string, Properties properties, ClassLoader classLoader) {
        if (classLoader == null) {
            return false;
        }
        try {
            ArrayList<URL> arrayList = Collections.list(classLoader.getResources(string));
            if (arrayList.isEmpty()) {
                I1O1I1LaNd.debug("No {} file found from ClassLoader {}", (Object)string, (Object)classLoader);
                return false;
            }
            if (arrayList.size() > 1) {
                I1O1I1LaNd.warn("Configuration conflict: there is more than one {} file on the classpath", (Object)string);
                return true;
            }
            try (InputStream inputStream = ((URL)arrayList.get(0)).openStream();){
                if (inputStream != null) {
                    properties.load(inputStream);
                }
            }
            return true;
        }
        catch (IOException iOException) {
            return false;
        }
    }

    public static String I1O1I1LaNd(File file) {
        try {
            return Files.readSymbolicLink(Paths.get(file.getAbsolutePath(), new String[0])).toString();
        }
        catch (IOException iOException) {
            return null;
        }
    }
}

