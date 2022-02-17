/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.css;

import com.sun.javafx.css.CascadingStyle;
import com.sun.javafx.css.Declaration;
import com.sun.javafx.css.Match;
import com.sun.javafx.css.Rule;
import com.sun.javafx.css.Selector;
import com.sun.javafx.css.Style;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class StyleMap {
    public static final StyleMap EMPTY_MAP = new StyleMap(-1, Collections.emptyList());
    private static final Comparator<CascadingStyle> cascadingStyleComparator = (cascadingStyle, cascadingStyle2) -> {
        String string;
        String string2 = cascadingStyle.getProperty();
        int n2 = string2.compareTo(string = cascadingStyle2.getProperty());
        if (n2 != 0) {
            return n2;
        }
        return cascadingStyle.compareTo((CascadingStyle)cascadingStyle2);
    };
    private final int id;
    private List<Selector> selectors;
    private Map<String, List<CascadingStyle>> cascadingStyles;

    StyleMap(int n2, List<Selector> list) {
        this.id = n2;
        this.selectors = list;
    }

    public int getId() {
        return this.id;
    }

    public boolean isEmpty() {
        if (this.selectors != null) {
            return this.selectors.isEmpty();
        }
        if (this.cascadingStyles != null) {
            return this.cascadingStyles.isEmpty();
        }
        return true;
    }

    public Map<String, List<CascadingStyle>> getCascadingStyles() {
        if (this.cascadingStyles == null) {
            int n2;
            List<CascadingStyle> list;
            Object object;
            int n3;
            if (this.selectors == null || this.selectors.isEmpty()) {
                this.cascadingStyles = Collections.emptyMap();
                return this.cascadingStyles;
            }
            ArrayList<CascadingStyle> arrayList = new ArrayList<CascadingStyle>();
            int n4 = 0;
            int n5 = this.selectors.size();
            for (n3 = 0; n3 < n5; ++n3) {
                object = this.selectors.get(n3);
                Match match = ((Selector)object).createMatch();
                list = ((Selector)object).getRule();
                int n6 = ((Rule)((Object)list)).getDeclarations().size();
                for (n2 = 0; n2 < n6; ++n2) {
                    Declaration declaration = (Declaration)((Rule)((Object)list)).getDeclarations().get(n2);
                    CascadingStyle cascadingStyle = new CascadingStyle(new Style(match.selector, declaration), match.pseudoClasses, match.specificity, n4++);
                    arrayList.add(cascadingStyle);
                }
            }
            if (arrayList.isEmpty()) {
                this.cascadingStyles = Collections.emptyMap();
                return this.cascadingStyles;
            }
            Collections.sort(arrayList, cascadingStyleComparator);
            n3 = arrayList.size();
            this.cascadingStyles = new HashMap<String, List<CascadingStyle>>(n3);
            CascadingStyle cascadingStyle = (CascadingStyle)arrayList.get(0);
            object = cascadingStyle.getProperty();
            int n7 = 0;
            while (n7 < n3) {
                list = this.cascadingStyles.get(object);
                if (list == null) {
                    n2 = n7;
                    Object object2 = object;
                    while (++n2 < n3 && ((String)(object = (cascadingStyle = (CascadingStyle)arrayList.get(n2)).getProperty())).equals(object2)) {
                    }
                    this.cascadingStyles.put((String)object2, arrayList.subList(n7, n2));
                    n7 = n2;
                    continue;
                }
                assert (false);
            }
            this.selectors.clear();
            this.selectors = null;
        }
        return this.cascadingStyles;
    }
}

