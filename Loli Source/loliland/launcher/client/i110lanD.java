/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.win32.COM.COMException;
import com.sun.jna.platform.win32.COM.WbemcliUtil;
import com.sun.jna.platform.win32.VersionHelpers;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import loliland.launcher.client.O1IiIiI1LAND;
import loliland.launcher.client.i1i1OilanD;
import loliland.launcher.client.ii00llanD;
import loliland.launcher.client.il0ii0ILANd;
import loliland.launcher.client.lO1OIlI1laNd;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.lOlOlaND;
import loliland.launcher.client.li0i01LanD;
import loliland.launcher.client.liOi1OLaNd;
import loliland.launcher.client.llOlOiLand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class i110lanD
extends lO1OIlI1laNd {
    private static final Logger I1O1I1LaNd = LoggerFactory.getLogger(i110lanD.class);
    private static final Pattern OOOIilanD = Pattern.compile(".*ObjectId=.*SP:(\\{.*\\}).*");
    private static final Pattern lI00OlAND = Pattern.compile(".*ObjectId=.*PD:(\\{.*\\}).*");
    private static final Pattern lli0OiIlAND = Pattern.compile(".*ObjectId=.*VD:(\\{.*\\})(\\{.*\\}).*");
    private static final boolean li0iOILAND = VersionHelpers.IsWindows8OrGreater();

    i110lanD(String string, Map map, Set set) {
        super(string, map, set);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    static List lli0OiIlAND() {
        if (!li0iOILAND) {
            return Collections.emptyList();
        }
        li0i01LanD li0i01LanD2 = li0i01LanD.I1O1I1LaNd();
        boolean bl = false;
        try {
            String string;
            Object object;
            Object object2;
            Object object3;
            bl = li0i01LanD2.OOOIilanD();
            WbemcliUtil.WmiResult wmiResult = il0ii0ILANd.I1O1I1LaNd(li0i01LanD2);
            int n2 = wmiResult.getResultCount();
            if (n2 == 0) {
                List list = Collections.emptyList();
                return list;
            }
            HashMap<Object, String> hashMap = new HashMap<Object, String>();
            WbemcliUtil.WmiResult wmiResult2 = il0ii0ILANd.lli0OiIlAND(li0i01LanD2);
            n2 = wmiResult2.getResultCount();
            for (int i2 = 0; i2 < n2; ++i2) {
                object3 = ii00llanD.I1O1I1LaNd(wmiResult2, llOlOiLand.OOOIilanD, i2);
                Matcher matcher = lli0OiIlAND.matcher((CharSequence)object3);
                if (matcher.matches()) {
                    object3 = matcher.group(2) + " " + matcher.group(1);
                }
                hashMap.put(object3, ii00llanD.I1O1I1LaNd(wmiResult2, llOlOiLand.I1O1I1LaNd, i2));
            }
            HashMap<Object, O1IiIiI1LAND> hashMap2 = new HashMap<Object, O1IiIiI1LAND>();
            object3 = il0ii0ILANd.lI00OlAND(li0i01LanD2);
            n2 = ((WbemcliUtil.WmiResult)object3).getResultCount();
            for (int i3 = 0; i3 < n2; ++i3) {
                object2 = ii00llanD.I1O1I1LaNd((WbemcliUtil.WmiResult)object3, lOlOlaND.lI00OlAND, i3);
                Matcher matcher = lI00OlAND.matcher((CharSequence)object2);
                if (matcher.matches()) {
                    object2 = matcher.group(1);
                }
                hashMap2.put(object2, new O1IiIiI1LAND(ii00llanD.I1O1I1LaNd((WbemcliUtil.WmiResult)object3, lOlOlaND.I1O1I1LaNd, i3), ii00llanD.I1O1I1LaNd((WbemcliUtil.WmiResult)object3, lOlOlaND.OOOIilanD, i3)));
            }
            HashMap<String, String> hashMap3 = new HashMap<String, String>();
            object2 = il0ii0ILANd.OOOIilanD(li0i01LanD2);
            n2 = ((WbemcliUtil.WmiResult)object2).getResultCount();
            for (int i4 = 0; i4 < n2; ++i4) {
                String string2 = ii00llanD.lli0OiIlAND((WbemcliUtil.WmiResult)object2, liOi1OLaNd.I1O1I1LaNd, i4);
                object = OOOIilanD.matcher(string2);
                if (((Matcher)object).matches()) {
                    string2 = ((Matcher)object).group(1);
                }
                if (((Matcher)(object = lI00OlAND.matcher(string = ii00llanD.lli0OiIlAND((WbemcliUtil.WmiResult)object2, liOi1OLaNd.OOOIilanD, i4)))).matches()) {
                    string = ((Matcher)object).group(1);
                }
                hashMap3.put(string2 + " " + string, string);
            }
            ArrayList<i110lanD> arrayList = new ArrayList<i110lanD>();
            n2 = wmiResult.getResultCount();
            for (int i5 = 0; i5 < n2; ++i5) {
                Object object4;
                Object object5;
                Map.Entry entry2;
                object = ii00llanD.I1O1I1LaNd(wmiResult, i1i1OilanD.I1O1I1LaNd, i5);
                string = ii00llanD.I1O1I1LaNd(wmiResult, i1i1OilanD.OOOIilanD, i5);
                Matcher matcher = OOOIilanD.matcher(string);
                if (matcher.matches()) {
                    string = matcher.group(1);
                }
                HashSet<String> hashSet = new HashSet<String>();
                for (Map.Entry entry2 : hashMap3.entrySet()) {
                    if (!((String)entry2.getKey()).contains(string) || (object5 = (O1IiIiI1LAND)hashMap2.get(object4 = (String)entry2.getValue())) == null) continue;
                    hashSet.add((String)((O1IiIiI1LAND)object5).I1O1I1LaNd() + " @ " + (String)((O1IiIiI1LAND)object5).OOOIilanD());
                }
                HashMap hashMap4 = new HashMap();
                entry2 = hashMap.entrySet().iterator();
                while (entry2.hasNext()) {
                    object4 = (Map.Entry)entry2.next();
                    if (!((String)object4.getKey()).contains(string)) continue;
                    object5 = lOilLanD.OOOIilanD.split((CharSequence)object4.getKey())[0];
                    hashMap4.put((String)object4.getValue() + " " + (String)object5, hashSet);
                }
                arrayList.add(new i110lanD((String)object, hashMap4, hashSet));
            }
            ArrayList<i110lanD> arrayList2 = arrayList;
            return arrayList2;
        }
        catch (COMException cOMException) {
            I1O1I1LaNd.warn("COM exception: {}", (Object)cOMException.getMessage());
            List list = Collections.emptyList();
            return list;
        }
        finally {
            if (bl) {
                li0i01LanD2.lI00OlAND();
            }
        }
    }
}

