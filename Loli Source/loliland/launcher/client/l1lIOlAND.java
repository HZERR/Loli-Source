/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import loliland.launcher.client.O1lO0ILaND;
import loliland.launcher.client.O1lllAnD;
import loliland.launcher.client.Olii000Land;
import loliland.launcher.client.l01O01Iiland;
import loliland.launcher.client.l0OO0lllAnd;
import loliland.launcher.client.l11OliLAnD;
import loliland.launcher.client.lI0lI11ILanD;

public class l1lIOlAND {
    private static HashMap I1O1I1LaNd = new HashMap();

    public static String I1O1I1LaNd(File file) {
        try {
            HashCode hashCode = com.google.common.io.Files.hash(file, Hashing.murmur3_32());
            return hashCode.toString() + ":" + file.length();
        }
        catch (Exception exception) {
            return "null";
        }
    }

    private static String I1O1I1LaNd(MessageDigest messageDigest, File file) {
        try {
            int n2;
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] arrby = new byte[1024];
            while ((n2 = fileInputStream.read(arrby)) != -1) {
                messageDigest.update(arrby, 0, n2);
            }
            fileInputStream.close();
            byte[] arrby2 = messageDigest.digest();
            StringBuilder stringBuilder = new StringBuilder();
            for (byte by : arrby2) {
                stringBuilder.append(Integer.toString((by & 0xFF) + 256, 16).substring(1));
            }
            return stringBuilder.toString();
        }
        catch (Exception exception) {
            return "null";
        }
    }

    public static void I1O1I1LaNd(File file, ArrayList arrayList) {
        if (file.isFile()) {
            arrayList.add(file);
            return;
        }
        if (!file.exists() || !file.isDirectory()) {
            return;
        }
        File[] arrfile = file.listFiles();
        if (arrfile == null) {
            return;
        }
        for (File file2 : arrfile) {
            if (file2.isFile()) {
                arrayList.add(file2);
                continue;
            }
            if (!file2.isDirectory()) continue;
            l1lIOlAND.I1O1I1LaNd(file2, arrayList);
        }
    }

    public static l11OliLAnD I1O1I1LaNd(String string, String string2) {
        String string3 = "resource:" + string + string2;
        if (I1O1I1LaNd.containsKey(string3)) {
            return (l11OliLAnD)I1O1I1LaNd.get(string3);
        }
        l11OliLAnD l11OliLAnD2 = new l11OliLAnD(string, string2);
        l11OliLAnD.I1O1I1LaNd.submit(l11OliLAnD2::lI00OlAND);
        I1O1I1LaNd.put(string3, l11OliLAnD2);
        return l11OliLAnD2;
    }

    public static l11OliLAnD OOOIilanD(String string, String string2) {
        String string3 = "local:" + string + string2;
        if (I1O1I1LaNd.containsKey(string3)) {
            return (l11OliLAnD)I1O1I1LaNd.get(string3);
        }
        Olii000Land olii000Land = new Olii000Land(string, string2);
        l11OliLAnD.I1O1I1LaNd.submit(olii000Land::lI00OlAND);
        I1O1I1LaNd.put(string3, olii000Land);
        return olii000Land;
    }

    public static long I1O1I1LaNd(InputStream inputStream, OutputStream outputStream) throws IOException {
        int n2;
        long l2 = 0L;
        byte[] arrby = new byte[8192];
        while ((n2 = inputStream.read(arrby)) > 0) {
            outputStream.write(arrby, 0, n2);
            l2 += (long)n2;
        }
        return l2;
    }

    public static l11OliLAnD I1O1I1LaNd(String string) {
        return l1lIOlAND.I1O1I1LaNd(string, null, null);
    }

    private static l11OliLAnD I1O1I1LaNd(String string, String string2, String string3) {
        String string4 = string + string2 + string3;
        if (I1O1I1LaNd.containsKey(string4)) {
            return (l11OliLAnD)I1O1I1LaNd.get(string4);
        }
        lI0lI11ILanD lI0lI11ILanD2 = new lI0lI11ILanD(string, string2, string3);
        lI0lI11ILanD.Oill1LAnD.submit(lI0lI11ILanD2::lI00OlAND);
        I1O1I1LaNd.put(string4, lI0lI11ILanD2);
        return lI0lI11ILanD2;
    }

    public static File lI00OlAND(String string, String string2) {
        File file = new File(string);
        if (!file.exists() || !file.isDirectory()) {
            return null;
        }
        for (File file2 : Objects.requireNonNull(file.listFiles())) {
            if (file2.isDirectory() || !l1lIOlAND.lli0OiIlAND(file2.getName()).equalsIgnoreCase(string2) && !file2.getName().equalsIgnoreCase(string2)) continue;
            return file2;
        }
        return null;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static boolean I1O1I1LaNd(File file, URL uRL) throws IOException {
        if (!l1lIOlAND.OOOIilanD(file.getName()).equals(l1lIOlAND.I1O1I1LaNd(uRL).toString())) {
            return false;
        }
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection)uRL.openConnection();
            httpURLConnection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
            httpURLConnection.setRequestMethod("HEAD");
            boolean bl = file.length() == httpURLConnection.getContentLengthLong();
            return bl;
        }
        catch (IOException iOException) {
            boolean bl = false;
            return bl;
        }
        finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
    }

    private static l01O01Iiland I1O1I1LaNd(URL uRL) throws IOException {
        URLConnection uRLConnection = uRL.openConnection();
        ((HttpURLConnection)uRLConnection).setRequestMethod("HEAD");
        uRLConnection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
        return l01O01Iiland.I1O1I1LaNd(uRLConnection.getHeaderField("Content-Type"));
    }

    public static File I1O1I1LaNd(URL uRL, String string, String string2) throws IOException {
        int n2;
        InputStream inputStream;
        URLConnection uRLConnection = uRL.openConnection();
        uRLConnection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
        l01O01Iiland l01O01Iiland2 = l01O01Iiland.I1O1I1LaNd(uRLConnection.getHeaderField("Content-Type"));
        try {
            inputStream = uRLConnection.getInputStream();
        }
        catch (Exception exception) {
            throw new O1lO0ILaND();
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] arrby = new byte[4096];
        while ((n2 = inputStream.read(arrby)) != -1) {
            byteArrayOutputStream.write(arrby, 0, n2);
        }
        File file = new File(string + "/" + string2 + "." + (Object)((Object)l01O01Iiland2));
        try (FileOutputStream fileOutputStream = new FileOutputStream(file);){
            fileOutputStream.write(byteArrayOutputStream.toByteArray());
        }
        inputStream.close();
        return file;
    }

    public static O1lllAnD I1O1I1LaNd(l0OO0lllAnd l0OO0lllAnd2, File file, String string) {
        if (l0OO0lllAnd2.iilIi1laND().get()) {
            System.out.println("Stop downloading!");
            return O1lllAnD.lI00OlAND;
        }
        try {
            int n2;
            File file2 = file.getParentFile();
            if (!file2.exists()) {
                file2.mkdirs();
            }
            file.createNewFile();
            URL uRL = new URL(string);
            URLConnection uRLConnection = uRL.openConnection();
            uRLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36 OPR/68.0.3618.173");
            uRLConnection.setDefaultUseCaches(false);
            int n3 = ((HttpURLConnection)uRLConnection).getResponseCode();
            if (n3 != 200) {
                System.out.println("Error " + n3 + " download file: " + file.getPath() + " | " + string);
                ((HttpURLConnection)uRLConnection).disconnect();
                return O1lllAnD.OOOIilanD;
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(uRLConnection.getInputStream());
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] arrby = new byte[1024];
            while ((n2 = bufferedInputStream.read(arrby, 0, 1024)) != -1) {
                if (l0OO0lllAnd2.iilIi1laND().get()) {
                    bufferedInputStream.close();
                    fileOutputStream.close();
                    ((HttpURLConnection)uRLConnection).disconnect();
                    System.out.println("Stop downloading!");
                    return O1lllAnD.lI00OlAND;
                }
                fileOutputStream.write(arrby, 0, n2);
                l0OO0lllAnd2.I1O1I1LaNd(n2);
            }
            bufferedInputStream.close();
            ((HttpURLConnection)uRLConnection).disconnect();
            fileOutputStream.close();
            System.out.println("Successful download file: " + file.getPath() + " | " + string);
            return O1lllAnD.I1O1I1LaNd;
        }
        catch (Exception exception) {
            System.out.println("Error download file: " + file.getPath() + " | " + string);
            exception.printStackTrace();
            return O1lllAnD.OOOIilanD;
        }
    }

    public static String OOOIilanD(String string) {
        if (string == null) {
            return null;
        }
        int n2 = l1lIOlAND.lI00OlAND(string);
        if (n2 == -1) {
            return "";
        }
        return string.substring(n2 + 1);
    }

    private static int lI00OlAND(String string) {
        if (string == null) {
            return -1;
        }
        int n2 = string.lastIndexOf(46);
        int n3 = l1lIOlAND.li0iOILAND(string);
        return n3 > n2 ? -1 : n2;
    }

    private static String lli0OiIlAND(String string) {
        if (string == null) {
            return null;
        }
        l1lIOlAND.O1il1llOLANd(string);
        int n2 = l1lIOlAND.lI00OlAND(string);
        if (n2 == -1) {
            return string;
        }
        return string.substring(0, n2);
    }

    private static int li0iOILAND(String string) {
        if (string == null) {
            return -1;
        }
        int n2 = string.lastIndexOf(47);
        int n3 = string.lastIndexOf(92);
        return Math.max(n2, n3);
    }

    private static void O1il1llOLANd(String string) {
        int n2 = string.length();
        for (int i2 = 0; i2 < n2; ++i2) {
            if (string.charAt(i2) != '\u0000') continue;
            throw new IllegalArgumentException("Null byte present in file/path name. There are no known legitimate use cases for such data, but several injection attacks may use it");
        }
    }

    public static void I1O1I1LaNd(InputStream inputStream, File file) {
        try {
            Files.copy(inputStream, Paths.get(file.toURI()), new CopyOption[0]);
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    public static void I1O1I1LaNd(l0OO0lllAnd l0OO0lllAnd2, File file, File file2) {
        try {
            Object object;
            if (file2.exists()) {
                file2.delete();
            }
            try {
                object = file2.getParentFile();
                if (!((File)object).exists()) {
                    ((File)object).mkdirs();
                }
                file2.createNewFile();
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
            object = new FileInputStream(file);
            Throwable throwable = null;
            try (FileOutputStream fileOutputStream = new FileOutputStream(file2);){
                int n2;
                byte[] arrby = new byte[1024];
                while ((n2 = ((InputStream)object).read(arrby)) > 0) {
                    ((OutputStream)fileOutputStream).write(arrby, 0, n2);
                    l0OO0lllAnd2.I1O1I1LaNd(n2);
                }
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
            finally {
                if (object != null) {
                    if (throwable != null) {
                        try {
                            ((InputStream)object).close();
                        }
                        catch (Throwable throwable3) {
                            throwable.addSuppressed(throwable3);
                        }
                    } else {
                        ((InputStream)object).close();
                    }
                }
            }
            System.out.println("Successful copy file from: " + file.getPath() + " to " + file2.getPath());
        }
        catch (Exception exception) {
            System.out.println("Error copy file from: " + file.getPath() + " to " + file2.getPath());
            exception.printStackTrace();
        }
    }

    public static void OOOIilanD(File file) {
        if (file == null || !file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            File[] arrfile = file.listFiles();
            if (arrfile == null) {
                return;
            }
            for (File file2 : arrfile) {
                l1lIOlAND.OOOIilanD(file2);
            }
        }
        file.delete();
    }

    public static HashMap I1O1I1LaNd() {
        return I1O1I1LaNd;
    }
}

