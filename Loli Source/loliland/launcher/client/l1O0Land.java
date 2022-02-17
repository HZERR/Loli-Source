/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class l1O0Land {
    private static final int I1O1I1LaNd = 4096;

    public void I1O1I1LaNd(String string, String string2) throws IOException {
        File file = new File(string2);
        if (!file.exists()) {
            file.mkdirs();
        }
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(string));
        ZipEntry zipEntry = zipInputStream.getNextEntry();
        while (zipEntry != null) {
            String string3 = string2 + File.separator + zipEntry.getName();
            if (!zipEntry.isDirectory()) {
                this.I1O1I1LaNd(zipInputStream, string3);
            } else {
                File file2 = new File(string3);
                file2.mkdirs();
            }
            zipInputStream.closeEntry();
            zipEntry = zipInputStream.getNextEntry();
        }
        zipInputStream.close();
    }

    private void I1O1I1LaNd(ZipInputStream zipInputStream, String string) throws IOException {
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(string));
        byte[] arrby = new byte[4096];
        int n2 = 0;
        while ((n2 = zipInputStream.read(arrby)) != -1) {
            bufferedOutputStream.write(arrby, 0, n2);
        }
        bufferedOutputStream.close();
    }
}

