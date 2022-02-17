/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.layout;

import com.sun.javafx.css.StyleConverterImpl;
import com.sun.javafx.css.StyleManager;
import com.sun.javafx.scene.layout.region.RepeatStruct;
import java.util.Map;
import javafx.css.CssMetaData;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.css.Styleable;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Paint;

class BackgroundConverter
extends StyleConverterImpl<ParsedValue[], Background> {
    static final StyleConverter<ParsedValue[], Background> INSTANCE = new BackgroundConverter();

    BackgroundConverter() {
    }

    @Override
    public Background convert(Map<CssMetaData<? extends Styleable, ?>, Object> map) {
        int n2;
        Object[] arrobject;
        Object object;
        Object object2;
        boolean bl;
        Paint[] arrpaint = (Paint[])map.get(Background.BACKGROUND_COLOR);
        String[] arrstring = (String[])map.get(Background.BACKGROUND_IMAGE);
        boolean bl2 = arrpaint != null && arrpaint.length > 0;
        boolean bl3 = bl = arrstring != null && arrstring.length > 0;
        if (!bl2 && !bl) {
            return null;
        }
        BackgroundFill[] arrbackgroundFill = null;
        if (bl2) {
            arrbackgroundFill = new BackgroundFill[arrpaint.length];
            object2 = map.get(Background.BACKGROUND_INSETS);
            object = object2 == null ? new Insets[0] : (Insets[])object2;
            object2 = map.get(Background.BACKGROUND_RADIUS);
            arrobject = object2 == null ? new CornerRadii[0] : (CornerRadii[])object2;
            int n3 = ((Insets[])object).length - 1;
            int n4 = arrobject.length - 1;
            for (n2 = 0; n2 < arrpaint.length; ++n2) {
                Insets insets;
                Insets insets2 = ((Insets[])object).length > 0 ? object[n2 <= n3 ? n2 : n3] : (insets = Insets.EMPTY);
                CornerRadii cornerRadii = arrobject.length > 0 ? arrobject[n2 <= n4 ? n2 : n4] : CornerRadii.EMPTY;
                arrbackgroundFill[n2] = new BackgroundFill(arrpaint[n2], cornerRadii, insets);
            }
        }
        object2 = null;
        if (bl) {
            object2 = new BackgroundImage[arrstring.length];
            object = map.get(Background.BACKGROUND_REPEAT);
            arrobject = object == null ? new RepeatStruct[0] : (RepeatStruct[])object;
            object = map.get(Background.BACKGROUND_POSITION);
            BackgroundPosition[] arrbackgroundPosition = object == null ? new BackgroundPosition[0] : (BackgroundPosition[])object;
            object = map.get(Background.BACKGROUND_SIZE);
            BackgroundSize[] arrbackgroundSize = object == null ? new BackgroundSize[0] : (BackgroundSize[])object;
            n2 = arrobject.length - 1;
            int n5 = arrbackgroundPosition.length - 1;
            int n6 = arrbackgroundSize.length - 1;
            for (int i2 = 0; i2 < arrstring.length; ++i2) {
                BackgroundPosition backgroundPosition;
                Object object3;
                Image image;
                if (arrstring[i2] == null || (image = StyleManager.getInstance().getCachedImage(arrstring[i2])) == null) continue;
                Object object4 = arrobject.length > 0 ? arrobject[i2 <= n2 ? i2 : n2] : (object3 = null);
                BackgroundPosition backgroundPosition2 = arrbackgroundPosition.length > 0 ? arrbackgroundPosition[i2 <= n5 ? i2 : n5] : (backgroundPosition = null);
                BackgroundSize backgroundSize = arrbackgroundSize.length > 0 ? arrbackgroundSize[i2 <= n6 ? i2 : n6] : null;
                object2[i2] = new BackgroundImage(image, object3 == null ? null : ((RepeatStruct)object3).repeatX, object3 == null ? null : ((RepeatStruct)object3).repeatY, backgroundPosition, backgroundSize);
            }
        }
        return new Background(arrbackgroundFill, (BackgroundImage[])object2);
    }
}

