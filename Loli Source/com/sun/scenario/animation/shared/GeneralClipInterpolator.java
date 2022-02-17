/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.animation.shared;

import com.sun.scenario.animation.shared.ClipInterpolator;
import com.sun.scenario.animation.shared.InterpolationInterval;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;

class GeneralClipInterpolator
extends ClipInterpolator {
    private KeyFrame[] keyFrames;
    private long[] keyFrameTicks;
    private InterpolationInterval[][] interval = new InterpolationInterval[0][];
    private int[] undefinedStartValues = new int[0];
    private boolean invalid = true;

    GeneralClipInterpolator(KeyFrame[] arrkeyFrame, long[] arrl) {
        this.keyFrames = arrkeyFrame;
        this.keyFrameTicks = arrl;
    }

    @Override
    ClipInterpolator setKeyFrames(KeyFrame[] arrkeyFrame, long[] arrl) {
        if (ClipInterpolator.getRealKeyFrameCount(arrkeyFrame) == 2) {
            return ClipInterpolator.create(arrkeyFrame, arrl);
        }
        this.keyFrames = arrkeyFrame;
        this.keyFrameTicks = arrl;
        this.invalid = true;
        return this;
    }

    @Override
    void validate(boolean bl) {
        if (this.invalid) {
            int n2;
            Object object;
            Object object2;
            int n3;
            HashMap<Object, KeyValue> hashMap = new HashMap<Object, KeyValue>();
            int n4 = this.keyFrames.length;
            for (n3 = 0; n3 < n4; ++n3) {
                object2 = this.keyFrames[n3];
                if (this.keyFrameTicks[n3] != 0L) break;
                for (KeyValue object3 : ((KeyFrame)object2).getValues()) {
                    hashMap.put(object3.getTarget(), object3);
                }
            }
            object2 = new HashMap();
            HashSet hashSet = new HashSet();
            while (n3 < n4) {
                KeyFrame keyFrame = this.keyFrames[n3];
                long l2 = this.keyFrameTicks[n3];
                for (KeyValue keyValue : keyFrame.getValues()) {
                    object = keyValue.getTarget();
                    ArrayList<InterpolationInterval> arrayList = (ArrayList<InterpolationInterval>)object2.get(object);
                    KeyValue keyValue2 = (KeyValue)hashMap.get(object);
                    if (arrayList == null) {
                        arrayList = new ArrayList<InterpolationInterval>();
                        object2.put(object, arrayList);
                        if (keyValue2 == null) {
                            arrayList.add(InterpolationInterval.create(keyValue, l2));
                            hashSet.add(object);
                        } else {
                            arrayList.add(InterpolationInterval.create(keyValue, l2, keyValue2, l2));
                        }
                    } else {
                        assert (keyValue2 != null);
                        arrayList.add(InterpolationInterval.create(keyValue, l2, keyValue2, l2 - ((InterpolationInterval)arrayList.get((int)(arrayList.size() - 1))).ticks));
                    }
                    hashMap.put(object, keyValue);
                }
                ++n3;
            }
            int n5 = object2.size();
            if (this.interval.length != n5) {
                this.interval = new InterpolationInterval[n5][];
            }
            if (this.undefinedStartValues.length != (n2 = hashSet.size())) {
                this.undefinedStartValues = new int[n2];
            }
            int n6 = 0;
            Iterator<Object> iterator = object2.entrySet().iterator();
            for (int i2 = 0; i2 < n5; ++i2) {
                object = (Map.Entry)iterator.next();
                this.interval[i2] = new InterpolationInterval[((List)object.getValue()).size()];
                ((List)object.getValue()).toArray(this.interval[i2]);
                if (!hashSet.contains(object.getKey())) continue;
                this.undefinedStartValues[n6++] = i2;
            }
            this.invalid = false;
        } else if (bl) {
            for (int n7 : this.undefinedStartValues) {
                this.interval[n7][0].recalculateStartValue();
            }
        }
    }

    @Override
    void interpolate(long l2) {
        block0: for (InterpolationInterval[] arrinterpolationInterval : this.interval) {
            int n2 = arrinterpolationInterval.length;
            long l3 = 0L;
            for (int i2 = 0; i2 < n2 - 1; ++i2) {
                InterpolationInterval interpolationInterval = arrinterpolationInterval[i2];
                long l4 = interpolationInterval.ticks;
                if (l2 <= l4) {
                    double d2 = (double)(l2 - l3) / (double)(l4 - l3);
                    interpolationInterval.interpolate(d2);
                    continue block0;
                }
                l3 = l4;
            }
            InterpolationInterval interpolationInterval = arrinterpolationInterval[n2 - 1];
            double d3 = Math.min(1.0, (double)(l2 - l3) / (double)(interpolationInterval.ticks - l3));
            interpolationInterval.interpolate(d3);
        }
    }
}

