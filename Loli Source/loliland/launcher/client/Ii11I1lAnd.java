/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.Comparator;
import loliland.launcher.client.iiO1LAnD;
import loliland.launcher.client.lOl00LAND;

public final class Ii11I1lAnd {
    public static final Comparator I1O1I1LaNd = (iiO1LAnD2, iiO1LAnD3) -> 0;
    public static final Comparator OOOIilanD = Comparator.comparingDouble(iiO1LAnD::OOOIilanD).reversed();
    public static final Comparator lI00OlAND = Comparator.comparingLong(iiO1LAnD::l0iIlIO1laNd).reversed();
    public static final Comparator lli0OiIlAND = Comparator.comparingLong(iiO1LAnD::ii1li00Land);
    public static final Comparator li0iOILAND = lli0OiIlAND.reversed();
    public static final Comparator O1il1llOLANd = Comparator.comparingInt(iiO1LAnD::I1O1I1LaNd);
    public static final Comparator Oill1LAnD = Comparator.comparingInt(iiO1LAnD::l0illAND);
    public static final Comparator lIOILand = Comparator.comparing(iiO1LAnD::lI00OlAND, String.CASE_INSENSITIVE_ORDER);

    private Ii11I1lAnd() {
    }

    private static Comparator OOOIilanD(lOl00LAND lOl00LAND2) {
        if (lOl00LAND2 != null) {
            switch (lOl00LAND2) {
                case I1O1I1LaNd: {
                    return OOOIilanD;
                }
                case OOOIilanD: {
                    return lI00OlAND;
                }
                case lI00OlAND: {
                    return li0iOILAND;
                }
                case lli0OiIlAND: {
                    return lli0OiIlAND;
                }
                case li0iOILAND: {
                    return O1il1llOLANd;
                }
                case O1il1llOLANd: {
                    return Oill1LAnD;
                }
                case Oill1LAnD: {
                    return lIOILand;
                }
            }
            throw new IllegalArgumentException("Unimplemented enum type: " + lOl00LAND2.toString());
        }
        return I1O1I1LaNd;
    }

    static /* synthetic */ Comparator I1O1I1LaNd(lOl00LAND lOl00LAND2) {
        return Ii11I1lAnd.OOOIilanD(lOl00LAND2);
    }
}

