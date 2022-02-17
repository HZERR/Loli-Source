/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import loliland.launcher.client.l01O01Iiland;
import loliland.launcher.client.l11OliLAnD;
import loliland.launcher.client.l1lIOlAND;

public class Olii000Land
extends l11OliLAnD {
    public Olii000Land(String string, String string2) {
        super(string, string2);
        this.O1il1llOLANd = this.I1O1I1LaNd(string, this.li0iOILAND);
    }

    private File I1O1I1LaNd(String string, String string2) {
        File file;
        File file2 = new File(string2);
        String string3 = "" + file2.getName();
        try {
            file = File.createTempFile(string3, string3);
            file.createNewFile();
        }
        catch (IOException iOException) {
            return null;
        }
        if (file.exists()) {
            file.delete();
        }
        try {
            Files.copy(this.getClass().getResourceAsStream("/assets/" + string + "/" + string2), Paths.get(file.toURI()), new CopyOption[0]);
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
        return file;
    }

    @Override
    void I1O1I1LaNd() throws Exception {
        FileInputStream fileInputStream = new FileInputStream(this.O1il1llOLANd);
        this.I1O1I1LaNd(fileInputStream, l01O01Iiland.I1O1I1LaNd(l1lIOlAND.OOOIilanD(this.O1il1llOLANd.getName())));
        if (this.O1il1llOLANd.exists()) {
            this.O1il1llOLANd.delete();
        }
    }
}

