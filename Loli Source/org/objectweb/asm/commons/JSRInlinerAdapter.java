/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm.commons;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;
import org.objectweb.asm.tree.TryCatchBlockNode;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class JSRInlinerAdapter
extends MethodNode
implements Opcodes {
    private static final boolean LOGGING = false;
    private final MethodVisitor mv;
    private final Map<LabelNode, Subroutine> subroutineHeads = new HashMap<LabelNode, Subroutine>();
    private final Subroutine mainSubroutine = new Subroutine();
    final BitSet dualCitizens = new BitSet();

    public JSRInlinerAdapter(MethodVisitor mv, int access, String name, String desc, String signature, String[] exceptions) {
        super(access, name, desc, signature, exceptions);
        this.mv = mv;
    }

    @Override
    public void visitJumpInsn(int opcode, Label lbl) {
        super.visitJumpInsn(opcode, lbl);
        LabelNode ln = ((JumpInsnNode)this.instructions.getLast()).label;
        if (opcode == 168 && !this.subroutineHeads.containsKey(ln)) {
            this.subroutineHeads.put(ln, new Subroutine());
        }
    }

    @Override
    public void visitEnd() {
        if (!this.subroutineHeads.isEmpty()) {
            this.markSubroutines();
            this.emitCode();
        }
        if (this.mv != null) {
            this.accept(this.mv);
        }
    }

    private void markSubroutines() {
        BitSet anyvisited = new BitSet();
        this.markSubroutineWalk(this.mainSubroutine, 0, anyvisited);
        for (Map.Entry<LabelNode, Subroutine> entry : this.subroutineHeads.entrySet()) {
            LabelNode lab = entry.getKey();
            Subroutine sub = entry.getValue();
            int index = this.instructions.indexOf(lab);
            this.markSubroutineWalk(sub, index, anyvisited);
        }
    }

    private void markSubroutineWalk(Subroutine sub, int index, BitSet anyvisited) {
        this.markSubroutineWalkDFS(sub, index, anyvisited);
        boolean loop = true;
        while (loop) {
            loop = false;
            for (TryCatchBlockNode trycatch : this.tryCatchBlocks) {
                int handlerindex = this.instructions.indexOf(trycatch.handler);
                if (sub.instructions.get(handlerindex)) continue;
                int startindex = this.instructions.indexOf(trycatch.start);
                int endindex = this.instructions.indexOf(trycatch.end);
                int nextbit = sub.instructions.nextSetBit(startindex);
                if (nextbit == -1 || nextbit >= endindex) continue;
                this.markSubroutineWalkDFS(sub, handlerindex, anyvisited);
                loop = true;
            }
        }
    }

    private void markSubroutineWalkDFS(Subroutine sub, int index, BitSet anyvisited) {
        while (true) {
            LabelNode l2;
            int i2;
            int destidx;
            AbstractInsnNode node = this.instructions.get(index);
            if (sub.instructions.get(index)) {
                return;
            }
            sub.instructions.set(index);
            if (anyvisited.get(index)) {
                this.dualCitizens.set(index);
            }
            anyvisited.set(index);
            if (node.getType() == 7 && node.getOpcode() != 168) {
                JumpInsnNode jnode = (JumpInsnNode)node;
                destidx = this.instructions.indexOf(jnode.label);
                this.markSubroutineWalkDFS(sub, destidx, anyvisited);
            }
            if (node.getType() == 11) {
                TableSwitchInsnNode tsnode = (TableSwitchInsnNode)node;
                destidx = this.instructions.indexOf(tsnode.dflt);
                this.markSubroutineWalkDFS(sub, destidx, anyvisited);
                for (i2 = tsnode.labels.size() - 1; i2 >= 0; --i2) {
                    l2 = tsnode.labels.get(i2);
                    destidx = this.instructions.indexOf(l2);
                    this.markSubroutineWalkDFS(sub, destidx, anyvisited);
                }
            }
            if (node.getType() == 12) {
                LookupSwitchInsnNode lsnode = (LookupSwitchInsnNode)node;
                destidx = this.instructions.indexOf(lsnode.dflt);
                this.markSubroutineWalkDFS(sub, destidx, anyvisited);
                for (i2 = lsnode.labels.size() - 1; i2 >= 0; --i2) {
                    l2 = lsnode.labels.get(i2);
                    destidx = this.instructions.indexOf(l2);
                    this.markSubroutineWalkDFS(sub, destidx, anyvisited);
                }
            }
            switch (this.instructions.get(index).getOpcode()) {
                case 167: 
                case 169: 
                case 170: 
                case 171: 
                case 172: 
                case 173: 
                case 174: 
                case 175: 
                case 176: 
                case 177: 
                case 191: {
                    return;
                }
            }
            ++index;
        }
    }

    private void emitCode() {
        LinkedList<Instantiation> worklist = new LinkedList<Instantiation>();
        worklist.add(new Instantiation(null, this.mainSubroutine));
        InsnList newInstructions = new InsnList();
        ArrayList<TryCatchBlockNode> newTryCatchBlocks = new ArrayList<TryCatchBlockNode>();
        ArrayList<LocalVariableNode> newLocalVariables = new ArrayList<LocalVariableNode>();
        while (!worklist.isEmpty()) {
            Instantiation inst = (Instantiation)worklist.removeFirst();
            this.emitSubroutine(inst, worklist, newInstructions, newTryCatchBlocks, newLocalVariables);
        }
        this.instructions = newInstructions;
        this.tryCatchBlocks = newTryCatchBlocks;
        this.localVariables = newLocalVariables;
    }

    private void emitSubroutine(Instantiation instant, List<Instantiation> worklist, InsnList newInstructions, List<TryCatchBlockNode> newTryCatchBlocks, List<LocalVariableNode> newLocalVariables) {
        LabelNode end;
        LabelNode start;
        LabelNode duplbl = null;
        int c2 = this.instructions.size();
        for (int i2 = 0; i2 < c2; ++i2) {
            AbstractInsnNode insn = this.instructions.get(i2);
            Instantiation owner = instant.findOwner(i2);
            if (insn.getType() == 8) {
                LabelNode ilbl = (LabelNode)insn;
                LabelNode remap = instant.rangeLabel(ilbl);
                if (remap == duplbl) continue;
                newInstructions.add(remap);
                duplbl = remap;
                continue;
            }
            if (owner != instant) continue;
            if (insn.getOpcode() == 169) {
                LabelNode retlabel = null;
                Instantiation p2 = instant;
                while (p2 != null) {
                    if (p2.subroutine.ownsInstruction(i2)) {
                        retlabel = p2.returnLabel;
                    }
                    p2 = p2.previous;
                }
                if (retlabel == null) {
                    throw new RuntimeException("Instruction #" + i2 + " is a RET not owned by any subroutine");
                }
                newInstructions.add(new JumpInsnNode(167, retlabel));
                continue;
            }
            if (insn.getOpcode() == 168) {
                LabelNode lbl = ((JumpInsnNode)insn).label;
                Subroutine sub = this.subroutineHeads.get(lbl);
                Instantiation newinst = new Instantiation(instant, sub);
                LabelNode startlbl = newinst.gotoLabel(lbl);
                newInstructions.add(new InsnNode(1));
                newInstructions.add(new JumpInsnNode(167, startlbl));
                newInstructions.add(newinst.returnLabel);
                worklist.add(newinst);
                continue;
            }
            newInstructions.add(insn.clone(instant));
        }
        for (TryCatchBlockNode trycatch : this.tryCatchBlocks) {
            start = instant.rangeLabel(trycatch.start);
            if (start == (end = instant.rangeLabel(trycatch.end))) continue;
            LabelNode handler = instant.gotoLabel(trycatch.handler);
            if (start == null || end == null || handler == null) {
                throw new RuntimeException("Internal error!");
            }
            newTryCatchBlocks.add(new TryCatchBlockNode(start, end, handler, trycatch.type));
        }
        for (LocalVariableNode lvnode : this.localVariables) {
            start = instant.rangeLabel(lvnode.start);
            if (start == (end = instant.rangeLabel(lvnode.end))) continue;
            newLocalVariables.add(new LocalVariableNode(lvnode.name, lvnode.desc, lvnode.signature, start, end, lvnode.index));
        }
    }

    private static void log(String str) {
        System.err.println(str);
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private class Instantiation
    extends AbstractMap<LabelNode, LabelNode> {
        final Instantiation previous;
        public final Subroutine subroutine;
        public final Map<LabelNode, LabelNode> rangeTable = new HashMap<LabelNode, LabelNode>();
        public final LabelNode returnLabel;

        Instantiation(Instantiation prev, Subroutine sub) {
            this.previous = prev;
            this.subroutine = sub;
            Instantiation p2 = prev;
            while (p2 != null) {
                if (p2.subroutine == sub) {
                    throw new RuntimeException("Recursive invocation of " + sub);
                }
                p2 = p2.previous;
            }
            this.returnLabel = prev != null ? new LabelNode() : null;
            LabelNode duplbl = null;
            int c2 = JSRInlinerAdapter.this.instructions.size();
            for (int i2 = 0; i2 < c2; ++i2) {
                AbstractInsnNode insn = JSRInlinerAdapter.this.instructions.get(i2);
                if (insn.getType() == 8) {
                    LabelNode ilbl = (LabelNode)insn;
                    if (duplbl == null) {
                        duplbl = new LabelNode();
                    }
                    this.rangeTable.put(ilbl, duplbl);
                    continue;
                }
                if (this.findOwner(i2) != this) continue;
                duplbl = null;
            }
        }

        public Instantiation findOwner(int i2) {
            if (!this.subroutine.ownsInstruction(i2)) {
                return null;
            }
            if (!JSRInlinerAdapter.this.dualCitizens.get(i2)) {
                return this;
            }
            Instantiation own = this;
            Instantiation p2 = this.previous;
            while (p2 != null) {
                if (p2.subroutine.ownsInstruction(i2)) {
                    own = p2;
                }
                p2 = p2.previous;
            }
            return own;
        }

        public LabelNode gotoLabel(LabelNode l2) {
            Instantiation owner = this.findOwner(JSRInlinerAdapter.this.instructions.indexOf(l2));
            return owner.rangeTable.get(l2);
        }

        public LabelNode rangeLabel(LabelNode l2) {
            return this.rangeTable.get(l2);
        }

        @Override
        public Set<Map.Entry<LabelNode, LabelNode>> entrySet() {
            return null;
        }

        @Override
        public LabelNode get(Object o2) {
            return this.gotoLabel((LabelNode)o2);
        }
    }

    protected static class Subroutine {
        public final BitSet instructions = new BitSet();

        protected Subroutine() {
        }

        public void addInstruction(int idx) {
            this.instructions.set(idx);
        }

        public boolean ownsInstruction(int idx) {
            return this.instructions.get(idx);
        }

        public String toString() {
            return "Subroutine: " + this.instructions;
        }
    }
}

