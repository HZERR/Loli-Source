/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.time.LocalDateTime;
import java.time.Year;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.iOI1I1iLaNd;

public final class ll1ILAnd {
    private static final Pattern I1O1I1LaNd = Pattern.compile("(\\S+)\\s+(\\S+)\\s+(\\d{4}-\\d{2}-\\d{2})\\s+(\\d{2}:\\d{2})\\s*(?:\\((.+)\\))?");
    private static final DateTimeFormatter OOOIilanD = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final Pattern lI00OlAND = Pattern.compile("(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\d+)\\s+(\\d{2}:\\d{2})\\s*(?:\\((.+)\\))?");
    private static final DateTimeFormatter lli0OiIlAND = new DateTimeFormatterBuilder().appendPattern("MMM d HH:mm").parseDefaulting(ChronoField.YEAR, Year.now().getValue()).toFormatter(Locale.US);

    private ll1ILAnd() {
    }

    public static synchronized List I1O1I1LaNd() {
        ArrayList arrayList = new ArrayList();
        boolean bl = false;
        for (String string : Iill1lanD.I1O1I1LaNd("who")) {
            if (!bl && ll1ILAnd.I1O1I1LaNd(arrayList, string)) continue;
            bl = ll1ILAnd.OOOIilanD(arrayList, string);
        }
        return arrayList;
    }

    private static boolean I1O1I1LaNd(List list, String string) {
        Matcher matcher = I1O1I1LaNd.matcher(string);
        if (matcher.matches()) {
            try {
                list.add(new iOI1I1iLaNd(matcher.group(1), matcher.group(2), LocalDateTime.parse(matcher.group(3) + " " + matcher.group(4), OOOIilanD).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(), matcher.group(5) == null ? "unknown" : matcher.group(5)));
                return true;
            }
            catch (NullPointerException | DateTimeParseException runtimeException) {
                // empty catch block
            }
        }
        return false;
    }

    private static boolean OOOIilanD(List list, String string) {
        Matcher matcher = lI00OlAND.matcher(string);
        if (matcher.matches()) {
            try {
                LocalDateTime localDateTime = LocalDateTime.parse(matcher.group(3) + " " + matcher.group(4) + " " + matcher.group(5), lli0OiIlAND);
                if (localDateTime.isAfter(LocalDateTime.now())) {
                    localDateTime = localDateTime.minus(1L, ChronoUnit.YEARS);
                }
                long l2 = localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                list.add(new iOI1I1iLaNd(matcher.group(1), matcher.group(2), l2, matcher.group(6) == null ? "" : matcher.group(6)));
                return true;
            }
            catch (NullPointerException | DateTimeParseException runtimeException) {
                // empty catch block
            }
        }
        return false;
    }
}

