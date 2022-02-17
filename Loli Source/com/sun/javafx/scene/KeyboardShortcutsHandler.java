/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.collections.ObservableListWrapper;
import com.sun.javafx.collections.ObservableMapWrapper;
import com.sun.javafx.event.BasicEventDispatcher;
import com.sun.javafx.scene.traversal.Direction;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.scene.Node;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.Mnemonic;

public final class KeyboardShortcutsHandler
extends BasicEventDispatcher {
    private ObservableMap<KeyCombination, Runnable> accelerators;
    private CopyOnWriteMap<KeyCombination, Runnable> acceleratorsBackingMap;
    private ObservableMap<KeyCombination, ObservableList<Mnemonic>> mnemonics;
    private boolean mnemonicsDisplayEnabled = false;

    public void addMnemonic(Mnemonic mnemonic) {
        ObservableListWrapper<Mnemonic> observableListWrapper = (ObservableListWrapper<Mnemonic>)this.getMnemonics().get(mnemonic.getKeyCombination());
        if (observableListWrapper == null) {
            observableListWrapper = new ObservableListWrapper<Mnemonic>(new ArrayList());
            this.getMnemonics().put(mnemonic.getKeyCombination(), observableListWrapper);
        }
        boolean bl = false;
        for (int i2 = 0; i2 < observableListWrapper.size(); ++i2) {
            if (observableListWrapper.get(i2) != mnemonic) continue;
            bl = true;
        }
        if (!bl) {
            observableListWrapper.add(mnemonic);
        }
    }

    public void removeMnemonic(Mnemonic mnemonic) {
        ObservableList observableList = (ObservableList)this.getMnemonics().get(mnemonic.getKeyCombination());
        if (observableList != null) {
            for (int i2 = 0; i2 < observableList.size(); ++i2) {
                if (((Mnemonic)observableList.get(i2)).getNode() != mnemonic.getNode()) continue;
                observableList.remove(i2);
            }
        }
    }

    public ObservableMap<KeyCombination, ObservableList<Mnemonic>> getMnemonics() {
        if (this.mnemonics == null) {
            this.mnemonics = new ObservableMapWrapper<KeyCombination, ObservableList<Mnemonic>>(new HashMap());
        }
        return this.mnemonics;
    }

    public ObservableMap<KeyCombination, Runnable> getAccelerators() {
        if (this.accelerators == null) {
            this.acceleratorsBackingMap = new CopyOnWriteMap();
            this.accelerators = new ObservableMapWrapper<KeyCombination, Runnable>(this.acceleratorsBackingMap);
        }
        return this.accelerators;
    }

    private void traverse(Event event, Node node, Direction direction) {
        if (node.impl_traverse(direction)) {
            event.consume();
        }
    }

    public void processTraversal(Event event) {
        EventTarget eventTarget;
        if (event instanceof KeyEvent && event.getEventType() == KeyEvent.KEY_PRESSED && !((KeyEvent)event).isMetaDown() && !((KeyEvent)event).isControlDown() && !((KeyEvent)event).isAltDown() && (eventTarget = event.getTarget()) instanceof Node) {
            switch (((KeyEvent)event).getCode()) {
                case TAB: {
                    if (((KeyEvent)event).isShiftDown()) {
                        this.traverse(event, (Node)eventTarget, Direction.PREVIOUS);
                        break;
                    }
                    this.traverse(event, (Node)eventTarget, Direction.NEXT);
                    break;
                }
                case UP: {
                    this.traverse(event, (Node)eventTarget, Direction.UP);
                    break;
                }
                case DOWN: {
                    this.traverse(event, (Node)eventTarget, Direction.DOWN);
                    break;
                }
                case LEFT: {
                    this.traverse(event, (Node)eventTarget, Direction.LEFT);
                    break;
                }
                case RIGHT: {
                    this.traverse(event, (Node)eventTarget, Direction.RIGHT);
                    break;
                }
            }
        }
    }

    @Override
    public Event dispatchBubblingEvent(Event event) {
        if (event.getEventType() == KeyEvent.KEY_PRESSED) {
            if (PlatformUtil.isMac()) {
                if (((KeyEvent)event).isMetaDown()) {
                    this.processMnemonics((KeyEvent)event);
                }
            } else if (((KeyEvent)event).isAltDown() || this.isMnemonicsDisplayEnabled()) {
                this.processMnemonics((KeyEvent)event);
            }
            if (!event.isConsumed()) {
                this.processAccelerators((KeyEvent)event);
            }
            if (!event.isConsumed()) {
                this.processTraversal(event);
            }
        }
        if (!PlatformUtil.isMac()) {
            if (event.getEventType() == KeyEvent.KEY_PRESSED && ((KeyEvent)event).isAltDown() && !event.isConsumed()) {
                if (!this.isMnemonicsDisplayEnabled()) {
                    this.setMnemonicsDisplayEnabled(true);
                } else if (PlatformUtil.isWindows()) {
                    this.setMnemonicsDisplayEnabled(!this.isMnemonicsDisplayEnabled());
                }
            }
            if (event.getEventType() == KeyEvent.KEY_RELEASED && !((KeyEvent)event).isAltDown() && !PlatformUtil.isWindows()) {
                this.setMnemonicsDisplayEnabled(false);
            }
        }
        return event;
    }

    /*
     * WARNING - void declaration
     */
    private void processMnemonics(KeyEvent keyEvent) {
        if (this.mnemonics != null) {
            Object object;
            ObservableList observableList = null;
            for (Map.Entry object2 : this.mnemonics.entrySet()) {
                if (!this.isMnemonicsDisplayEnabled()) {
                    if (!((KeyCombination)object2.getKey()).match(keyEvent)) continue;
                    observableList = (ObservableList)object2.getValue();
                    break;
                }
                object = new KeyEvent(null, keyEvent.getTarget(), KeyEvent.KEY_PRESSED, keyEvent.getCharacter(), keyEvent.getText(), keyEvent.getCode(), keyEvent.isShiftDown(), keyEvent.isControlDown(), true, keyEvent.isMetaDown());
                if (!((KeyCombination)object2.getKey()).match((KeyEvent)object)) continue;
                observableList = (ObservableList)object2.getValue();
                break;
            }
            if (observableList != null) {
                void var4_7;
                boolean bl = false;
                Object var4_6 = null;
                object = null;
                int n2 = -1;
                int n3 = -1;
                for (int i2 = 0; i2 < observableList.size(); ++i2) {
                    if (!(observableList.get(i2) instanceof Mnemonic)) continue;
                    Node node = ((Mnemonic)observableList.get(i2)).getNode();
                    if (object == null && node.impl_isTreeVisible() && !node.isDisabled()) {
                        object = (Mnemonic)observableList.get(i2);
                    }
                    if (node.impl_isTreeVisible() && node.isFocusTraversable() && !node.isDisabled()) {
                        if (var4_7 == null) {
                            Node node2 = node;
                        } else {
                            bl = true;
                            if (n2 != -1 && n3 == -1) {
                                n3 = i2;
                            }
                        }
                    }
                    if (!node.isFocused()) continue;
                    n2 = i2;
                }
                if (var4_7 != null) {
                    if (!bl) {
                        var4_7.requestFocus();
                        keyEvent.consume();
                    } else if (n2 == -1) {
                        var4_7.requestFocus();
                        keyEvent.consume();
                    } else if (n2 >= observableList.size()) {
                        var4_7.requestFocus();
                        keyEvent.consume();
                    } else {
                        if (n3 != -1) {
                            ((Mnemonic)observableList.get(n3)).getNode().requestFocus();
                        } else {
                            var4_7.requestFocus();
                        }
                        keyEvent.consume();
                    }
                }
                if (!bl && object != null) {
                    ((Mnemonic)object).fire();
                }
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void processAccelerators(KeyEvent keyEvent) {
        if (this.acceleratorsBackingMap != null) {
            this.acceleratorsBackingMap.lock();
            try {
                for (Map.Entry entry : ((CopyOnWriteMap)this.acceleratorsBackingMap).backingMap.entrySet()) {
                    Runnable runnable;
                    if (!((KeyCombination)entry.getKey()).match(keyEvent) || (runnable = (Runnable)entry.getValue()) == null) continue;
                    runnable.run();
                    keyEvent.consume();
                }
            }
            finally {
                this.acceleratorsBackingMap.unlock();
            }
        }
    }

    private void processMnemonicsKeyDisplay() {
        ObservableList observableList = null;
        if (this.mnemonics != null) {
            for (Map.Entry entry : this.mnemonics.entrySet()) {
                observableList = (ObservableList)entry.getValue();
                if (observableList == null) continue;
                for (int i2 = 0; i2 < observableList.size(); ++i2) {
                    Node node = ((Mnemonic)observableList.get(i2)).getNode();
                    node.impl_setShowMnemonics(this.mnemonicsDisplayEnabled);
                }
            }
        }
    }

    public boolean isMnemonicsDisplayEnabled() {
        return this.mnemonicsDisplayEnabled;
    }

    public void setMnemonicsDisplayEnabled(boolean bl) {
        if (bl != this.mnemonicsDisplayEnabled) {
            this.mnemonicsDisplayEnabled = bl;
            this.processMnemonicsKeyDisplay();
        }
    }

    public void clearNodeMnemonics(Node node) {
        if (this.mnemonics != null) {
            for (ObservableList observableList : this.mnemonics.values()) {
                Iterator iterator = observableList.iterator();
                while (iterator.hasNext()) {
                    Mnemonic mnemonic = (Mnemonic)iterator.next();
                    if (mnemonic.getNode() != node) continue;
                    iterator.remove();
                }
            }
        }
    }

    private static class CopyOnWriteMap<K, V>
    extends AbstractMap<K, V> {
        private Map<K, V> backingMap = new HashMap();
        private boolean lock;

        private CopyOnWriteMap() {
        }

        public void lock() {
            this.lock = true;
        }

        public void unlock() {
            this.lock = false;
        }

        @Override
        public V put(K k2, V v2) {
            if (this.lock) {
                this.backingMap = new HashMap<K, V>(this.backingMap);
                this.lock = false;
            }
            return this.backingMap.put(k2, v2);
        }

        @Override
        public Set<Map.Entry<K, V>> entrySet() {
            return new AbstractSet<Map.Entry<K, V>>(){

                @Override
                public Iterator<Map.Entry<K, V>> iterator() {
                    return new Iterator<Map.Entry<K, V>>(){
                        private Iterator<Map.Entry<K, V>> backingIt;
                        private Map<K, V> backingMapAtCreation;
                        private Map.Entry<K, V> lastNext;
                        {
                            this.backingIt = backingMap.entrySet().iterator();
                            this.backingMapAtCreation = backingMap;
                            this.lastNext = null;
                        }

                        @Override
                        public boolean hasNext() {
                            this.checkCoMod();
                            return this.backingIt.hasNext();
                        }

                        private void checkCoMod() {
                            if (backingMap != this.backingMapAtCreation) {
                                throw new ConcurrentModificationException();
                            }
                        }

                        @Override
                        public Map.Entry<K, V> next() {
                            this.checkCoMod();
                            this.lastNext = this.backingIt.next();
                            return this.lastNext;
                        }

                        @Override
                        public void remove() {
                            this.checkCoMod();
                            if (this.lastNext == null) {
                                throw new IllegalStateException();
                            }
                            if (lock) {
                                backingMap = new HashMap(backingMap);
                                this.backingIt = backingMap.entrySet().iterator();
                                while (!this.lastNext.equals(this.backingIt.next())) {
                                }
                                lock = false;
                            }
                            this.backingIt.remove();
                            this.lastNext = null;
                        }
                    };
                }

                @Override
                public int size() {
                    return backingMap.size();
                }
            };
        }
    }
}

