/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import com.sun.javafx.binding.ExpressionHelper;
import com.sun.javafx.collections.ListListenerHelper;
import com.sun.javafx.collections.NonIterableChange;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.scene.control.skin.TextAreaSkin;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.StyleConverter;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableIntegerProperty;
import javafx.css.StyleableProperty;
import javafx.scene.AccessibleRole;
import javafx.scene.control.Skin;
import javafx.scene.control.TextInputControl;

public class TextArea
extends TextInputControl {
    public static final int DEFAULT_PREF_COLUMN_COUNT = 40;
    public static final int DEFAULT_PREF_ROW_COUNT = 10;
    public static final int DEFAULT_PARAGRAPH_CAPACITY = 32;
    private BooleanProperty wrapText = new StyleableBooleanProperty(false){

        @Override
        public Object getBean() {
            return TextArea.this;
        }

        @Override
        public String getName() {
            return "wrapText";
        }

        @Override
        public CssMetaData<TextArea, Boolean> getCssMetaData() {
            return StyleableProperties.WRAP_TEXT;
        }
    };
    private IntegerProperty prefColumnCount = new StyleableIntegerProperty(40){
        private int oldValue;
        {
            this.oldValue = this.get();
        }

        @Override
        protected void invalidated() {
            int n2 = this.get();
            if (n2 < 0) {
                if (this.isBound()) {
                    this.unbind();
                }
                this.set(this.oldValue);
                throw new IllegalArgumentException("value cannot be negative.");
            }
            this.oldValue = n2;
        }

        @Override
        public CssMetaData<TextArea, Number> getCssMetaData() {
            return StyleableProperties.PREF_COLUMN_COUNT;
        }

        @Override
        public Object getBean() {
            return TextArea.this;
        }

        @Override
        public String getName() {
            return "prefColumnCount";
        }
    };
    private IntegerProperty prefRowCount = new StyleableIntegerProperty(10){
        private int oldValue;
        {
            this.oldValue = this.get();
        }

        @Override
        protected void invalidated() {
            int n2 = this.get();
            if (n2 < 0) {
                if (this.isBound()) {
                    this.unbind();
                }
                this.set(this.oldValue);
                throw new IllegalArgumentException("value cannot be negative.");
            }
            this.oldValue = n2;
        }

        @Override
        public CssMetaData<TextArea, Number> getCssMetaData() {
            return StyleableProperties.PREF_ROW_COUNT;
        }

        @Override
        public Object getBean() {
            return TextArea.this;
        }

        @Override
        public String getName() {
            return "prefRowCount";
        }
    };
    private DoubleProperty scrollTop = new SimpleDoubleProperty(this, "scrollTop", 0.0);
    private DoubleProperty scrollLeft = new SimpleDoubleProperty(this, "scrollLeft", 0.0);

    public TextArea() {
        this("");
    }

    public TextArea(String string) {
        super(new TextAreaContent());
        this.getStyleClass().add("text-area");
        this.setAccessibleRole(AccessibleRole.TEXT_AREA);
        this.setText(string);
    }

    @Override
    final void textUpdated() {
        this.setScrollTop(0.0);
        this.setScrollLeft(0.0);
    }

    public ObservableList<CharSequence> getParagraphs() {
        return ((TextAreaContent)this.getContent()).paragraphList;
    }

    public final BooleanProperty wrapTextProperty() {
        return this.wrapText;
    }

    public final boolean isWrapText() {
        return this.wrapText.getValue();
    }

    public final void setWrapText(boolean bl) {
        this.wrapText.setValue(bl);
    }

    public final IntegerProperty prefColumnCountProperty() {
        return this.prefColumnCount;
    }

    public final int getPrefColumnCount() {
        return this.prefColumnCount.getValue();
    }

    public final void setPrefColumnCount(int n2) {
        this.prefColumnCount.setValue(n2);
    }

    public final IntegerProperty prefRowCountProperty() {
        return this.prefRowCount;
    }

    public final int getPrefRowCount() {
        return this.prefRowCount.getValue();
    }

    public final void setPrefRowCount(int n2) {
        this.prefRowCount.setValue(n2);
    }

    public final DoubleProperty scrollTopProperty() {
        return this.scrollTop;
    }

    public final double getScrollTop() {
        return this.scrollTop.getValue();
    }

    public final void setScrollTop(double d2) {
        this.scrollTop.setValue(d2);
    }

    public final DoubleProperty scrollLeftProperty() {
        return this.scrollLeft;
    }

    public final double getScrollLeft() {
        return this.scrollLeft.getValue();
    }

    public final void setScrollLeft(double d2) {
        this.scrollLeft.setValue(d2);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new TextAreaSkin(this);
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return TextArea.getClassCssMetaData();
    }

    private static class StyleableProperties {
        private static final CssMetaData<TextArea, Number> PREF_COLUMN_COUNT = new CssMetaData<TextArea, Number>("-fx-pref-column-count", SizeConverter.getInstance(), (Number)40){

            @Override
            public boolean isSettable(TextArea textArea) {
                return !textArea.prefColumnCount.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(TextArea textArea) {
                return (StyleableProperty)((Object)textArea.prefColumnCountProperty());
            }
        };
        private static final CssMetaData<TextArea, Number> PREF_ROW_COUNT = new CssMetaData<TextArea, Number>("-fx-pref-row-count", SizeConverter.getInstance(), (Number)10){

            @Override
            public boolean isSettable(TextArea textArea) {
                return !textArea.prefRowCount.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(TextArea textArea) {
                return (StyleableProperty)((Object)textArea.prefRowCountProperty());
            }
        };
        private static final CssMetaData<TextArea, Boolean> WRAP_TEXT = new CssMetaData<TextArea, Boolean>("-fx-wrap-text", StyleConverter.getBooleanConverter(), Boolean.valueOf(false)){

            @Override
            public boolean isSettable(TextArea textArea) {
                return !textArea.wrapText.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(TextArea textArea) {
                return (StyleableProperty)((Object)textArea.wrapTextProperty());
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            ArrayList arrayList = new ArrayList(TextInputControl.getClassCssMetaData());
            arrayList.add(PREF_COLUMN_COUNT);
            arrayList.add(PREF_ROW_COUNT);
            arrayList.add(WRAP_TEXT);
            STYLEABLES = Collections.unmodifiableList(arrayList);
        }
    }

    private static final class ParagraphListChange
    extends NonIterableChange<CharSequence> {
        private List<CharSequence> removed;

        protected ParagraphListChange(ObservableList<CharSequence> observableList, int n2, int n3, List<CharSequence> list) {
            super(n2, n3, observableList);
            this.removed = list;
        }

        @Override
        public List<CharSequence> getRemoved() {
            return this.removed;
        }

        @Override
        protected int[] getPermutation() {
            return new int[0];
        }
    }

    private static final class ParagraphList
    extends AbstractList<CharSequence>
    implements ObservableList<CharSequence> {
        private TextAreaContent content;

        private ParagraphList() {
        }

        @Override
        public CharSequence get(int n2) {
            return (CharSequence)this.content.paragraphs.get(n2);
        }

        @Override
        public boolean addAll(Collection<? extends CharSequence> collection) {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(CharSequence ... arrcharSequence) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean setAll(Collection<? extends CharSequence> collection) {
            throw new UnsupportedOperationException();
        }

        public boolean setAll(CharSequence ... arrcharSequence) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int size() {
            return this.content.paragraphs.size();
        }

        @Override
        public void addListener(ListChangeListener<? super CharSequence> listChangeListener) {
            this.content.listenerHelper = ListListenerHelper.addListener(this.content.listenerHelper, listChangeListener);
        }

        @Override
        public void removeListener(ListChangeListener<? super CharSequence> listChangeListener) {
            this.content.listenerHelper = ListListenerHelper.removeListener(this.content.listenerHelper, listChangeListener);
        }

        public boolean removeAll(CharSequence ... arrcharSequence) {
            throw new UnsupportedOperationException();
        }

        public boolean retainAll(CharSequence ... arrcharSequence) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void remove(int n2, int n3) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addListener(InvalidationListener invalidationListener) {
            this.content.listenerHelper = ListListenerHelper.addListener(this.content.listenerHelper, invalidationListener);
        }

        @Override
        public void removeListener(InvalidationListener invalidationListener) {
            this.content.listenerHelper = ListListenerHelper.removeListener(this.content.listenerHelper, invalidationListener);
        }
    }

    private static final class TextAreaContent
    implements TextInputControl.Content {
        private ExpressionHelper<String> helper = null;
        private ArrayList<StringBuilder> paragraphs = new ArrayList();
        private int contentLength = 0;
        private ParagraphList paragraphList = new ParagraphList();
        private ListListenerHelper<CharSequence> listenerHelper;

        private TextAreaContent() {
            this.paragraphs.add(new StringBuilder(32));
            this.paragraphList.content = this;
        }

        @Override
        public String get(int n2, int n3) {
            StringBuilder stringBuilder;
            int n4;
            int n5;
            int n6 = n3 - n2;
            StringBuilder stringBuilder2 = new StringBuilder(n6);
            int n7 = this.paragraphs.size();
            int n8 = 0;
            for (n4 = n2; n8 < n7 && n4 >= (n5 = (stringBuilder = this.paragraphs.get(n8)).length() + 1); n4 -= n5, ++n8) {
            }
            stringBuilder = this.paragraphs.get(n8);
            for (n5 = 0; n5 < n6; ++n5) {
                if (n4 == stringBuilder.length() && n5 < this.contentLength) {
                    stringBuilder2.append('\n');
                    stringBuilder = this.paragraphs.get(++n8);
                    n4 = 0;
                    continue;
                }
                stringBuilder2.append(stringBuilder.charAt(n4++));
            }
            return stringBuilder2.toString();
        }

        @Override
        public void insert(int n2, String string, boolean bl) {
            if (n2 < 0 || n2 > this.contentLength) {
                throw new IndexOutOfBoundsException();
            }
            if (string == null) {
                throw new IllegalArgumentException();
            }
            int n3 = (string = TextInputControl.filterInput(string, false, false)).length();
            if (n3 > 0) {
                int n4;
                int n5;
                ArrayList<StringBuilder> arrayList = new ArrayList<StringBuilder>();
                StringBuilder stringBuilder = new StringBuilder(32);
                for (n5 = 0; n5 < n3; ++n5) {
                    n4 = string.charAt(n5);
                    if (n4 == 10) {
                        arrayList.add(stringBuilder);
                        stringBuilder = new StringBuilder(32);
                        continue;
                    }
                    stringBuilder.append((char)n4);
                }
                arrayList.add(stringBuilder);
                n5 = this.paragraphs.size();
                n4 = this.contentLength + 1;
                StringBuilder stringBuilder2 = null;
                while (n2 < (n4 -= (stringBuilder2 = this.paragraphs.get(--n5)).length() + 1)) {
                }
                int n6 = n2 - n4;
                int n7 = arrayList.size();
                if (n7 == 1) {
                    stringBuilder2.insert(n6, stringBuilder);
                    this.fireParagraphListChangeEvent(n5, n5 + 1, Collections.singletonList(stringBuilder2));
                } else {
                    int n8 = stringBuilder2.length();
                    CharSequence charSequence = stringBuilder2.subSequence(n6, n8);
                    stringBuilder2.delete(n6, n8);
                    StringBuilder stringBuilder3 = (StringBuilder)arrayList.get(0);
                    stringBuilder2.insert(n6, stringBuilder3);
                    stringBuilder.append(charSequence);
                    this.fireParagraphListChangeEvent(n5, n5 + 1, Collections.singletonList(stringBuilder2));
                    this.paragraphs.addAll(n5 + 1, arrayList.subList(1, n7));
                    this.fireParagraphListChangeEvent(n5 + 1, n5 + n7, Collections.EMPTY_LIST);
                }
                this.contentLength += n3;
                if (bl) {
                    ExpressionHelper.fireValueChangedEvent(this.helper);
                }
            }
        }

        @Override
        public void delete(int n2, int n3, boolean bl) {
            if (n2 > n3) {
                throw new IllegalArgumentException();
            }
            if (n2 < 0 || n3 > this.contentLength) {
                throw new IndexOutOfBoundsException();
            }
            int n4 = n3 - n2;
            if (n4 > 0) {
                int n5 = this.paragraphs.size();
                int n6 = this.contentLength + 1;
                StringBuilder stringBuilder = null;
                while (n3 < (n6 -= (stringBuilder = this.paragraphs.get(--n5)).length() + 1)) {
                }
                int n7 = n5++;
                int n8 = n6;
                StringBuilder stringBuilder2 = stringBuilder;
                n6 += stringBuilder.length() + 1;
                while (n2 < (n6 -= (stringBuilder = this.paragraphs.get(--n5)).length() + 1)) {
                }
                int n9 = n5;
                int n10 = n6;
                StringBuilder stringBuilder3 = stringBuilder;
                if (n9 == n7) {
                    stringBuilder3.delete(n2 - n10, n3 - n10);
                    this.fireParagraphListChangeEvent(n9, n9 + 1, Collections.singletonList(stringBuilder3));
                } else {
                    CharSequence charSequence = stringBuilder3.subSequence(0, n2 - n10);
                    int n11 = n2 + n4 - n8;
                    stringBuilder2.delete(0, n11);
                    this.fireParagraphListChangeEvent(n7, n7 + 1, Collections.singletonList(stringBuilder2));
                    if (n7 - n9 > 0) {
                        ArrayList<CharSequence> arrayList = new ArrayList<CharSequence>(this.paragraphs.subList(n9, n7));
                        this.paragraphs.subList(n9, n7).clear();
                        this.fireParagraphListChangeEvent(n9, n9, arrayList);
                    }
                    stringBuilder2.insert(0, charSequence);
                    this.fireParagraphListChangeEvent(n9, n9 + 1, Collections.singletonList(stringBuilder3));
                }
                this.contentLength -= n4;
                if (bl) {
                    ExpressionHelper.fireValueChangedEvent(this.helper);
                }
            }
        }

        @Override
        public int length() {
            return this.contentLength;
        }

        @Override
        public String get() {
            return this.get(0, this.length());
        }

        @Override
        public void addListener(ChangeListener<? super String> changeListener) {
            this.helper = ExpressionHelper.addListener(this.helper, this, changeListener);
        }

        @Override
        public void removeListener(ChangeListener<? super String> changeListener) {
            this.helper = ExpressionHelper.removeListener(this.helper, changeListener);
        }

        @Override
        public String getValue() {
            return this.get();
        }

        @Override
        public void addListener(InvalidationListener invalidationListener) {
            this.helper = ExpressionHelper.addListener(this.helper, this, invalidationListener);
        }

        @Override
        public void removeListener(InvalidationListener invalidationListener) {
            this.helper = ExpressionHelper.removeListener(this.helper, invalidationListener);
        }

        private void fireParagraphListChangeEvent(int n2, int n3, List<CharSequence> list) {
            ParagraphListChange paragraphListChange = new ParagraphListChange(this.paragraphList, n2, n3, list);
            ListListenerHelper.fireValueChangedEvent(this.listenerHelper, paragraphListChange);
        }
    }
}

