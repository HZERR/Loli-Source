/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.plugin;

import java.util.HashSet;
import java.util.Set;
import org.pushingpixels.substance.api.skin.AutumnSkin;
import org.pushingpixels.substance.api.skin.BusinessBlackSteelSkin;
import org.pushingpixels.substance.api.skin.BusinessBlueSteelSkin;
import org.pushingpixels.substance.api.skin.BusinessSkin;
import org.pushingpixels.substance.api.skin.CeruleanSkin;
import org.pushingpixels.substance.api.skin.ChallengerDeepSkin;
import org.pushingpixels.substance.api.skin.CremeCoffeeSkin;
import org.pushingpixels.substance.api.skin.CremeSkin;
import org.pushingpixels.substance.api.skin.DustCoffeeSkin;
import org.pushingpixels.substance.api.skin.DustSkin;
import org.pushingpixels.substance.api.skin.EmeraldDuskSkin;
import org.pushingpixels.substance.api.skin.GeminiSkin;
import org.pushingpixels.substance.api.skin.GraphiteAquaSkin;
import org.pushingpixels.substance.api.skin.GraphiteGlassSkin;
import org.pushingpixels.substance.api.skin.GraphiteSkin;
import org.pushingpixels.substance.api.skin.MagellanSkin;
import org.pushingpixels.substance.api.skin.MarinerSkin;
import org.pushingpixels.substance.api.skin.MistAquaSkin;
import org.pushingpixels.substance.api.skin.MistSilverSkin;
import org.pushingpixels.substance.api.skin.ModerateSkin;
import org.pushingpixels.substance.api.skin.NebulaBrickWallSkin;
import org.pushingpixels.substance.api.skin.NebulaSkin;
import org.pushingpixels.substance.api.skin.OfficeBlack2007Skin;
import org.pushingpixels.substance.api.skin.OfficeBlue2007Skin;
import org.pushingpixels.substance.api.skin.OfficeSilver2007Skin;
import org.pushingpixels.substance.api.skin.RavenSkin;
import org.pushingpixels.substance.api.skin.SaharaSkin;
import org.pushingpixels.substance.api.skin.SkinInfo;
import org.pushingpixels.substance.api.skin.TwilightSkin;
import org.pushingpixels.substance.internal.plugin.SubstanceSkinPlugin;

public class BaseSkinPlugin
implements SubstanceSkinPlugin {
    private static SkinInfo create(String displayName, Class<?> skinClass, boolean isDefault) {
        SkinInfo result = new SkinInfo(displayName, skinClass.getName());
        result.setDefault(isDefault);
        return result;
    }

    @Override
    public Set<SkinInfo> getSkins() {
        HashSet<SkinInfo> result = new HashSet<SkinInfo>();
        result.add(BaseSkinPlugin.create("Business", BusinessSkin.class, false));
        result.add(BaseSkinPlugin.create("Business Black Steel", BusinessBlackSteelSkin.class, false));
        result.add(BaseSkinPlugin.create("Business Blue Steel", BusinessBlueSteelSkin.class, false));
        result.add(BaseSkinPlugin.create("Creme", CremeSkin.class, false));
        result.add(BaseSkinPlugin.create("Moderate", ModerateSkin.class, false));
        result.add(BaseSkinPlugin.create("Sahara", SaharaSkin.class, false));
        result.add(BaseSkinPlugin.create("Office Black 2007", OfficeBlack2007Skin.class, false));
        result.add(BaseSkinPlugin.create("Office Blue 2007", OfficeBlue2007Skin.class, false));
        result.add(BaseSkinPlugin.create("Office Silver 2007", OfficeSilver2007Skin.class, false));
        result.add(BaseSkinPlugin.create("Raven", RavenSkin.class, false));
        result.add(BaseSkinPlugin.create("Graphite", GraphiteSkin.class, false));
        result.add(BaseSkinPlugin.create("Graphite Glass", GraphiteGlassSkin.class, false));
        result.add(BaseSkinPlugin.create("Graphite Aqua", GraphiteAquaSkin.class, false));
        result.add(BaseSkinPlugin.create("Challenger Deep", ChallengerDeepSkin.class, false));
        result.add(BaseSkinPlugin.create("Emerald Dusk", EmeraldDuskSkin.class, false));
        result.add(BaseSkinPlugin.create("Nebula", NebulaSkin.class, false));
        result.add(BaseSkinPlugin.create("Nebula Brick Wall", NebulaBrickWallSkin.class, false));
        result.add(BaseSkinPlugin.create("Mist Silver", MistSilverSkin.class, false));
        result.add(BaseSkinPlugin.create("Mist Aqua", MistAquaSkin.class, false));
        result.add(BaseSkinPlugin.create("Autumn", AutumnSkin.class, false));
        result.add(BaseSkinPlugin.create("Cerulean", CeruleanSkin.class, false));
        result.add(BaseSkinPlugin.create("Creme Coffee", CremeCoffeeSkin.class, false));
        result.add(BaseSkinPlugin.create("Dust", DustSkin.class, false));
        result.add(BaseSkinPlugin.create("Dust Coffee", DustCoffeeSkin.class, false));
        result.add(BaseSkinPlugin.create("Twilight", TwilightSkin.class, false));
        result.add(BaseSkinPlugin.create("Magellan", MagellanSkin.class, false));
        result.add(BaseSkinPlugin.create("Gemini", GeminiSkin.class, false));
        result.add(BaseSkinPlugin.create("Mariner", MarinerSkin.class, false));
        return result;
    }

    @Override
    public String getDefaultSkinClassName() {
        return null;
    }
}

