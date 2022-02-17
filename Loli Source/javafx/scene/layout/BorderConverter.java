/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.layout;

import com.sun.javafx.css.StyleConverterImpl;
import com.sun.javafx.css.StyleManager;
import com.sun.javafx.scene.layout.region.BorderImageSlices;
import com.sun.javafx.scene.layout.region.Margins;
import com.sun.javafx.scene.layout.region.RepeatStruct;
import java.util.Map;
import javafx.css.CssMetaData;
import javafx.css.ParsedValue;
import javafx.css.Styleable;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderImage;
import javafx.scene.layout.BorderRepeat;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

class BorderConverter
extends StyleConverterImpl<ParsedValue[], Border> {
    private static final BorderConverter BORDER_IMAGE_CONVERTER = new BorderConverter();

    public static BorderConverter getInstance() {
        return BORDER_IMAGE_CONVERTER;
    }

    private BorderConverter() {
    }

    @Override
    public Border convert(Map<CssMetaData<? extends Styleable, ?>, Object> map) {
        BorderWidths borderWidths;
        Object object;
        Object object2;
        Object object3;
        Object object4;
        int n2;
        int n3;
        Insets[] arrinsets;
        int n4;
        Object[] arrobject;
        int n5;
        Object[] arrobject2;
        boolean bl;
        Paint[][] arrpaint = (Paint[][])map.get(Border.BORDER_COLOR);
        BorderStrokeStyle[][] arrborderStrokeStyle = (BorderStrokeStyle[][])map.get(Border.BORDER_STYLE);
        String[] arrstring = (String[])map.get(Border.BORDER_IMAGE_SOURCE);
        boolean bl2 = arrpaint != null && arrpaint.length > 0 || arrborderStrokeStyle != null && arrborderStrokeStyle.length > 0;
        boolean bl3 = bl = arrstring != null && arrstring.length > 0;
        if (!bl2 && !bl) {
            return null;
        }
        BorderStroke[] arrborderStroke = null;
        if (bl2) {
            int n6 = arrpaint != null ? arrpaint.length - 1 : -1;
            int n7 = arrborderStrokeStyle != null ? arrborderStrokeStyle.length - 1 : -1;
            int n8 = (n6 >= n7 ? n6 : n7) + 1;
            Object object5 = map.get(Border.BORDER_WIDTH);
            arrobject2 = object5 == null ? new Margins[0] : (Margins[])object5;
            n5 = arrobject2.length - 1;
            object5 = map.get(Border.BORDER_RADIUS);
            arrobject = object5 == null ? new CornerRadii[0] : (CornerRadii[])object5;
            n4 = arrobject.length - 1;
            object5 = map.get(Border.BORDER_INSETS);
            arrinsets = object5 == null ? new Insets[0] : (Insets[])object5;
            n3 = arrinsets.length - 1;
            for (n2 = 0; n2 < n8; ++n2) {
                if (n7 < 0) {
                    object4 = new BorderStrokeStyle[4];
                    object4[2] = object4[3] = BorderStrokeStyle.SOLID;
                    object4[1] = object4[3];
                    object4[0] = object4[3];
                } else {
                    object4 = arrborderStrokeStyle[n2 <= n7 ? n2 : n7];
                }
                if (object4[0] == BorderStrokeStyle.NONE && object4[1] == BorderStrokeStyle.NONE && object4[2] == BorderStrokeStyle.NONE && object4[3] == BorderStrokeStyle.NONE) continue;
                if (n6 < 0) {
                    object3 = new Paint[4];
                    object3[2] = object3[3] = Color.BLACK;
                    object3[1] = object3[3];
                    object3[0] = object3[3];
                } else {
                    object3 = arrpaint[n2 <= n6 ? n2 : n6];
                }
                if (arrborderStroke == null) {
                    arrborderStroke = new BorderStroke[n8];
                }
                Object object6 = arrobject2.length == 0 ? null : (object2 = arrobject2[n2 <= n5 ? n2 : n5]);
                Object object7 = arrobject.length == 0 ? CornerRadii.EMPTY : (object = arrobject[n2 <= n4 ? n2 : n4]);
                borderWidths = arrinsets.length == 0 ? null : arrinsets[n2 <= n3 ? n2 : n3];
                arrborderStroke[n2] = new BorderStroke(object3[0], object3[1], object3[2], object3[3], object4[0], object4[1], object4[2], object4[3], (CornerRadii)object, object2 == null ? BorderStroke.DEFAULT_WIDTHS : new BorderWidths(((Margins)object2).getTop(), ((Margins)object2).getRight(), ((Margins)object2).getBottom(), ((Margins)object2).getLeft()), (Insets)((Object)borderWidths));
            }
        }
        BorderImage[] arrborderImage = null;
        if (bl) {
            arrborderImage = new BorderImage[arrstring.length];
            Object object8 = map.get(Border.BORDER_IMAGE_REPEAT);
            RepeatStruct[] arrrepeatStruct = object8 == null ? new RepeatStruct[0] : (RepeatStruct[])object8;
            int n9 = arrrepeatStruct.length - 1;
            object8 = map.get(Border.BORDER_IMAGE_SLICE);
            arrobject2 = object8 == null ? new BorderImageSlices[0] : (BorderImageSlices[])object8;
            n5 = arrobject2.length - 1;
            object8 = map.get(Border.BORDER_IMAGE_WIDTH);
            arrobject = object8 == null ? new BorderWidths[0] : (BorderWidths[])object8;
            n4 = arrobject.length - 1;
            object8 = map.get(Border.BORDER_IMAGE_INSETS);
            arrinsets = object8 == null ? new Insets[0] : (Insets[])object8;
            n3 = arrinsets.length - 1;
            for (n2 = 0; n2 < arrstring.length; ++n2) {
                if (arrstring[n2] == null) continue;
                object4 = BorderRepeat.STRETCH;
                object3 = BorderRepeat.STRETCH;
                if (arrrepeatStruct.length > 0) {
                    object2 = arrrepeatStruct[n2 <= n9 ? n2 : n9];
                    switch (((RepeatStruct)object2).repeatX) {
                        case SPACE: {
                            object4 = BorderRepeat.SPACE;
                            break;
                        }
                        case ROUND: {
                            object4 = BorderRepeat.ROUND;
                            break;
                        }
                        case REPEAT: {
                            object4 = BorderRepeat.REPEAT;
                            break;
                        }
                        case NO_REPEAT: {
                            object4 = BorderRepeat.STRETCH;
                        }
                    }
                    switch (((RepeatStruct)object2).repeatY) {
                        case SPACE: {
                            object3 = BorderRepeat.SPACE;
                            break;
                        }
                        case ROUND: {
                            object3 = BorderRepeat.ROUND;
                            break;
                        }
                        case REPEAT: {
                            object3 = BorderRepeat.REPEAT;
                            break;
                        }
                        case NO_REPEAT: {
                            object3 = BorderRepeat.STRETCH;
                        }
                    }
                }
                Object object9 = arrobject2.length > 0 ? arrobject2[n2 <= n5 ? n2 : n5] : (object2 = BorderImageSlices.DEFAULT);
                Object object10 = arrinsets.length > 0 ? arrinsets[n2 <= n3 ? n2 : n3] : (object = Insets.EMPTY);
                borderWidths = arrobject.length > 0 ? arrobject[n2 <= n4 ? n2 : n4] : BorderWidths.DEFAULT;
                Image image = StyleManager.getInstance().getCachedImage(arrstring[n2]);
                arrborderImage[n2] = new BorderImage(image, borderWidths, (Insets)object, ((BorderImageSlices)object2).widths, ((BorderImageSlices)object2).filled, (BorderRepeat)((Object)object4), (BorderRepeat)((Object)object3));
            }
        }
        return arrborderStroke == null && arrborderImage == null ? null : new Border(arrborderStroke, arrborderImage);
    }

    public String toString() {
        return "BorderConverter";
    }
}

