/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import loliland.launcher.client.Iill1lanD;

public final class lliliilanD {
    private static final Pattern I1O1I1LaNd = Pattern.compile("\\D+(\\d{4}-\\d{2}-\\d{2})\\s+(\\d{2}:\\d{2}).*");
    private static final DateTimeFormatter OOOIilanD = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private lliliilanD() {
    }

    public static long I1O1I1LaNd() {
        String string = Iill1lanD.OOOIilanD("/usr/bin/who -b");
        Matcher matcher = I1O1I1LaNd.matcher(string);
        if (matcher.matches()) {
            try {
                return LocalDateTime.parse(matcher.group(1) + " " + matcher.group(2), OOOIilanD).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            }
            catch (NullPointerException | DateTimeParseException runtimeException) {
                // empty catch block
            }
        }
        return 0L;
    }
}

