/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher;

import com.google.common.io.Files;
import com.google.common.util.concurrent.FutureCallback;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import loliland.launcher.client.IO0I1lilaNd;

final class I1O1I1LaNd
implements FutureCallback {
    final /* synthetic */ String I1O1I1LaNd;

    I1O1I1LaNd(String string) {
        this.I1O1I1LaNd = string;
    }

    public void I1O1I1LaNd(IO0I1lilaNd iO0I1lilaNd) {
        String string = iO0I1lilaNd.toString();
        try {
            File file = new File("profile-" + this.I1O1I1LaNd + ".txt");
            Files.write(string, file, StandardCharsets.UTF_8);
            System.out.println("CPU profiling data written to " + file.getAbsolutePath());
        }
        catch (IOException iOException) {
            System.out.println("Failed to write CPU profiling data: " + iOException.getMessage());
        }
    }

    @Override
    public void onFailure(Throwable throwable) {
    }

    public /* synthetic */ void onSuccess(Object object) {
        this.I1O1I1LaNd((IO0I1lilaNd)object);
    }
}

