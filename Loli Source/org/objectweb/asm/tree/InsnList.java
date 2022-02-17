/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm.tree;

import java.util.ListIterator;
import java.util.NoSuchElementException;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LabelNode;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class InsnList {
    public static boolean check;
    private int size;
    private AbstractInsnNode first;
    private AbstractInsnNode last;
    AbstractInsnNode[] cache;

    public int size() {
        return this.size;
    }

    public AbstractInsnNode getFirst() {
        return this.first;
    }

    public AbstractInsnNode getLast() {
        return this.last;
    }

    public AbstractInsnNode get(int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException();
        }
        if (this.cache == null) {
            this.cache = this.toArray();
        }
        return this.cache[index];
    }

    public boolean contains(AbstractInsnNode insn) {
        AbstractInsnNode i2 = this.first;
        while (i2 != null && i2 != insn) {
            i2 = i2.next;
        }
        return i2 != null;
    }

    public int indexOf(AbstractInsnNode insn) {
        if (check && !this.contains(insn)) {
            throw new IllegalArgumentException();
        }
        if (this.cache == null) {
            this.cache = this.toArray();
        }
        return insn.index;
    }

    public void accept(MethodVisitor mv) {
        AbstractInsnNode insn = this.first;
        while (insn != null) {
            insn.accept(mv);
            insn = insn.next;
        }
    }

    public ListIterator<AbstractInsnNode> iterator() {
        return this.iterator(0);
    }

    public ListIterator<AbstractInsnNode> iterator(int index) {
        return new InsnListIterator(index);
    }

    public AbstractInsnNode[] toArray() {
        int i2 = 0;
        AbstractInsnNode elem = this.first;
        AbstractInsnNode[] insns = new AbstractInsnNode[this.size];
        while (elem != null) {
            insns[i2] = elem;
            elem.index = i2++;
            elem = elem.next;
        }
        return insns;
    }

    public void set(AbstractInsnNode location, AbstractInsnNode insn) {
        AbstractInsnNode prev;
        AbstractInsnNode next;
        if (check && (!this.contains(location) || insn.index != -1)) {
            throw new IllegalArgumentException();
        }
        insn.next = next = location.next;
        if (next != null) {
            next.prev = insn;
        } else {
            this.last = insn;
        }
        insn.prev = prev = location.prev;
        if (prev != null) {
            prev.next = insn;
        } else {
            this.first = insn;
        }
        if (this.cache != null) {
            int index = location.index;
            this.cache[index] = insn;
            insn.index = index;
        } else {
            insn.index = 0;
        }
        location.index = -1;
        location.prev = null;
        location.next = null;
    }

    public void add(AbstractInsnNode insn) {
        if (check && insn.index != -1) {
            throw new IllegalArgumentException();
        }
        ++this.size;
        if (this.last == null) {
            this.first = insn;
            this.last = insn;
        } else {
            this.last.next = insn;
            insn.prev = this.last;
        }
        this.last = insn;
        this.cache = null;
        insn.index = 0;
    }

    public void add(InsnList insns) {
        if (check && insns == this) {
            throw new IllegalArgumentException();
        }
        if (insns.size == 0) {
            return;
        }
        this.size += insns.size;
        if (this.last == null) {
            this.first = insns.first;
            this.last = insns.last;
        } else {
            AbstractInsnNode elem;
            this.last.next = elem = insns.first;
            elem.prev = this.last;
            this.last = insns.last;
        }
        this.cache = null;
        insns.removeAll(false);
    }

    public void insert(AbstractInsnNode insn) {
        if (check && insn.index != -1) {
            throw new IllegalArgumentException();
        }
        ++this.size;
        if (this.first == null) {
            this.first = insn;
            this.last = insn;
        } else {
            this.first.prev = insn;
            insn.next = this.first;
        }
        this.first = insn;
        this.cache = null;
        insn.index = 0;
    }

    public void insert(InsnList insns) {
        if (check && insns == this) {
            throw new IllegalArgumentException();
        }
        if (insns.size == 0) {
            return;
        }
        this.size += insns.size;
        if (this.first == null) {
            this.first = insns.first;
            this.last = insns.last;
        } else {
            AbstractInsnNode elem;
            this.first.prev = elem = insns.last;
            elem.next = this.first;
            this.first = insns.first;
        }
        this.cache = null;
        insns.removeAll(false);
    }

    public void insert(AbstractInsnNode location, AbstractInsnNode insn) {
        if (check && (!this.contains(location) || insn.index != -1)) {
            throw new IllegalArgumentException();
        }
        ++this.size;
        AbstractInsnNode next = location.next;
        if (next == null) {
            this.last = insn;
        } else {
            next.prev = insn;
        }
        location.next = insn;
        insn.next = next;
        insn.prev = location;
        this.cache = null;
        insn.index = 0;
    }

    public void insert(AbstractInsnNode location, InsnList insns) {
        if (check && (!this.contains(location) || insns == this)) {
            throw new IllegalArgumentException();
        }
        if (insns.size == 0) {
            return;
        }
        this.size += insns.size;
        AbstractInsnNode ifirst = insns.first;
        AbstractInsnNode ilast = insns.last;
        AbstractInsnNode next = location.next;
        if (next == null) {
            this.last = ilast;
        } else {
            next.prev = ilast;
        }
        location.next = ifirst;
        ilast.next = next;
        ifirst.prev = location;
        this.cache = null;
        insns.removeAll(false);
    }

    public void insertBefore(AbstractInsnNode location, AbstractInsnNode insn) {
        if (check && (!this.contains(location) || insn.index != -1)) {
            throw new IllegalArgumentException();
        }
        ++this.size;
        AbstractInsnNode prev = location.prev;
        if (prev == null) {
            this.first = insn;
        } else {
            prev.next = insn;
        }
        location.prev = insn;
        insn.next = location;
        insn.prev = prev;
        this.cache = null;
        insn.index = 0;
    }

    public void insertBefore(AbstractInsnNode location, InsnList insns) {
        if (check && (!this.contains(location) || insns == this)) {
            throw new IllegalArgumentException();
        }
        if (insns.size == 0) {
            return;
        }
        this.size += insns.size;
        AbstractInsnNode ifirst = insns.first;
        AbstractInsnNode ilast = insns.last;
        AbstractInsnNode prev = location.prev;
        if (prev == null) {
            this.first = ifirst;
        } else {
            prev.next = ifirst;
        }
        location.prev = ilast;
        ilast.next = location;
        ifirst.prev = prev;
        this.cache = null;
        insns.removeAll(false);
    }

    public void remove(AbstractInsnNode insn) {
        if (check && !this.contains(insn)) {
            throw new IllegalArgumentException();
        }
        --this.size;
        AbstractInsnNode next = insn.next;
        AbstractInsnNode prev = insn.prev;
        if (next == null) {
            if (prev == null) {
                this.first = null;
                this.last = null;
            } else {
                prev.next = null;
                this.last = prev;
            }
        } else if (prev == null) {
            this.first = next;
            next.prev = null;
        } else {
            prev.next = next;
            next.prev = prev;
        }
        this.cache = null;
        insn.index = -1;
        insn.prev = null;
        insn.next = null;
    }

    private void removeAll(boolean mark) {
        if (mark) {
            AbstractInsnNode insn = this.first;
            while (insn != null) {
                AbstractInsnNode next = insn.next;
                insn.index = -1;
                insn.prev = null;
                insn.next = null;
                insn = next;
            }
        }
        this.size = 0;
        this.first = null;
        this.last = null;
        this.cache = null;
    }

    public void clear() {
        this.removeAll(check);
    }

    public void resetLabels() {
        AbstractInsnNode insn = this.first;
        while (insn != null) {
            if (insn instanceof LabelNode) {
                ((LabelNode)insn).resetLabel();
            }
            insn = insn.next;
        }
    }

    private final class InsnListIterator
    implements ListIterator {
        AbstractInsnNode next;
        AbstractInsnNode prev;

        InsnListIterator(int index) {
            if (index == InsnList.this.size()) {
                this.next = null;
                this.prev = InsnList.this.getLast();
            } else {
                this.next = InsnList.this.get(index);
                this.prev = this.next.prev;
            }
        }

        public boolean hasNext() {
            return this.next != null;
        }

        public Object next() {
            AbstractInsnNode result;
            if (this.next == null) {
                throw new NoSuchElementException();
            }
            this.prev = result = this.next;
            this.next = result.next;
            return result;
        }

        public void remove() {
            InsnList.this.remove(this.prev);
            this.prev = this.prev.prev;
        }

        public boolean hasPrevious() {
            return this.prev != null;
        }

        public Object previous() {
            AbstractInsnNode result;
            this.next = result = this.prev;
            this.prev = result.prev;
            return result;
        }

        public int nextIndex() {
            if (this.next == null) {
                return InsnList.this.size();
            }
            if (InsnList.this.cache == null) {
                InsnList.this.cache = InsnList.this.toArray();
            }
            return this.next.index;
        }

        public int previousIndex() {
            if (this.prev == null) {
                return -1;
            }
            if (InsnList.this.cache == null) {
                InsnList.this.cache = InsnList.this.toArray();
            }
            return this.prev.index;
        }

        public void add(Object o2) {
            InsnList.this.insertBefore(this.next, (AbstractInsnNode)o2);
            this.prev = (AbstractInsnNode)o2;
        }

        public void set(Object o2) {
            InsnList.this.set(this.next.prev, (AbstractInsnNode)o2);
            this.prev = (AbstractInsnNode)o2;
        }
    }
}

