/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.animation;

import java.awt.Component;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.pushingpixels.lafwidget.animation.AnimationFacet;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.ease.Spline;
import org.pushingpixels.trident.ease.TimelineEase;

public class AnimationConfigurationManager {
    private static final Spline DEFAULT_EASE = new Spline(0.5f);
    private static AnimationConfigurationManager instance;
    private long timelineDuration = 200L;
    private Set<AnimationFacet> globalAllowed = new HashSet<AnimationFacet>();
    private Map<AnimationFacet, Set<Class<?>>> classAllowed = new HashMap();
    private Map<AnimationFacet, Set<Class<?>>> classDisallowed = new HashMap();
    private Map<AnimationFacet, Set<Component>> instanceAllowed = new HashMap<AnimationFacet, Set<Component>>();
    private Map<AnimationFacet, Set<Component>> instanceDisallowed = new HashMap<AnimationFacet, Set<Component>>();

    public static synchronized AnimationConfigurationManager getInstance() {
        if (instance == null) {
            instance = new AnimationConfigurationManager();
        }
        return instance;
    }

    private AnimationConfigurationManager() {
    }

    public synchronized void allowAnimations(AnimationFacet animationFacet) {
        this.globalAllowed.add(animationFacet);
    }

    public synchronized void allowAnimations(AnimationFacet animationFacet, Class<?> clazz) {
        Set<Class<?>> existingAllowed = this.classAllowed.get(animationFacet);
        if (existingAllowed == null) {
            existingAllowed = new HashSet();
            this.classAllowed.put(animationFacet, existingAllowed);
        }
        existingAllowed.add(clazz);
        Set<Class<?>> existingDisallowed = this.classDisallowed.get(animationFacet);
        if (existingDisallowed != null) {
            existingDisallowed.remove(clazz);
        }
    }

    public synchronized void allowAnimations(AnimationFacet animationFacet, Class<?>[] clazz) {
        for (int i2 = 0; i2 < clazz.length; ++i2) {
            this.allowAnimations(animationFacet, clazz[i2]);
        }
    }

    public synchronized void allowAnimations(AnimationFacet animationFacet, Component comp) {
        Set<Component> existingAllowed = this.instanceAllowed.get(animationFacet);
        if (existingAllowed == null) {
            existingAllowed = new HashSet<Component>();
            this.instanceAllowed.put(animationFacet, existingAllowed);
        }
        existingAllowed.add(comp);
        Set<Component> existingDisallowed = this.instanceDisallowed.get(animationFacet);
        if (existingDisallowed != null) {
            existingDisallowed.remove(comp);
        }
    }

    public synchronized void disallowAnimations(AnimationFacet animationFacet) {
        this.globalAllowed.remove(animationFacet);
    }

    public synchronized void disallowAnimations(AnimationFacet animationFacet, Class<?> clazz) {
        Set<Class<?>> existingDisallowed;
        Set<Class<?>> existingAllowed = this.classAllowed.get(animationFacet);
        if (existingAllowed != null) {
            existingAllowed.remove(clazz);
            if (existingAllowed.size() == 0) {
                this.classAllowed.remove(animationFacet);
            }
        }
        if ((existingDisallowed = this.classDisallowed.get(animationFacet)) == null) {
            existingDisallowed = new HashSet();
            this.classDisallowed.put(animationFacet, existingDisallowed);
        }
        existingDisallowed.add(clazz);
    }

    public synchronized void disallowAnimations(AnimationFacet animationFacet, Class<?>[] clazz) {
        for (int i2 = 0; i2 < clazz.length; ++i2) {
            this.disallowAnimations(animationFacet, clazz[i2]);
        }
    }

    public synchronized void disallowAnimations(AnimationFacet animationFacet, Component comp) {
        Set<Component> existingDisallowed;
        Set<Component> existingAllowed = this.instanceAllowed.get(animationFacet);
        if (existingAllowed != null) {
            existingAllowed.remove(comp);
            if (existingAllowed.size() == 0) {
                this.instanceAllowed.remove(animationFacet);
            }
        }
        if ((existingDisallowed = this.instanceDisallowed.get(animationFacet)) == null) {
            existingDisallowed = new HashSet<Component>();
            this.instanceDisallowed.put(animationFacet, existingDisallowed);
        }
        existingDisallowed.add(comp);
    }

    public synchronized boolean isAnimationAllowed(AnimationFacet animationFacet, Component comp) {
        Set<Component> instanceDisallowed = this.instanceDisallowed.get(animationFacet);
        if (instanceDisallowed != null && instanceDisallowed.contains(comp)) {
            return false;
        }
        Set<Component> instanceAllowed = this.instanceAllowed.get(animationFacet);
        if (instanceAllowed != null && instanceAllowed.contains(comp)) {
            return true;
        }
        if (comp != null) {
            Class<?> clazz = comp.getClass();
            Set<Class<?>> classAllowed = this.classAllowed.get(animationFacet);
            Set<Class<?>> classDisallowed = this.classDisallowed.get(animationFacet);
            if (classDisallowed != null) {
                for (Class<?> disallowed : classDisallowed) {
                    if (!disallowed.isAssignableFrom(clazz)) continue;
                    return false;
                }
            }
            if (classAllowed != null) {
                for (Class<?> allowed : classAllowed) {
                    if (!allowed.isAssignableFrom(clazz)) continue;
                    return true;
                }
            }
        }
        return this.globalAllowed.contains(animationFacet);
    }

    public void setTimelineDuration(long timelineDuration) {
        this.timelineDuration = timelineDuration;
    }

    public long getTimelineDuration() {
        return this.timelineDuration;
    }

    public void configureTimeline(Timeline timeline) {
        timeline.setDuration(this.timelineDuration);
        timeline.setEase(DEFAULT_EASE);
    }

    public void configureModifiedTimeline(Timeline timeline) {
        timeline.setDuration(5L * this.timelineDuration);
        timeline.setEase(new TimelineEase(){

            @Override
            public float map(float durationFraction) {
                if (durationFraction < 0.8f) {
                    return 0.0f;
                }
                return 5.0f * (durationFraction - 0.8f);
            }
        });
    }
}

