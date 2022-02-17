/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm.tree.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;
import org.objectweb.asm.tree.TryCatchBlockNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.Frame;
import org.objectweb.asm.tree.analysis.Interpreter;
import org.objectweb.asm.tree.analysis.Subroutine;
import org.objectweb.asm.tree.analysis.Value;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class Analyzer<V extends Value>
implements Opcodes {
    private final Interpreter<V> interpreter;
    private int n;
    private InsnList insns;
    private List<TryCatchBlockNode>[] handlers;
    private Frame<V>[] frames;
    private Subroutine[] subroutines;
    private boolean[] queued;
    private int[] queue;
    private int top;

    public Analyzer(Interpreter<V> interpreter) {
        this.interpreter = interpreter;
    }

    public Frame<V>[] analyze(String owner, MethodNode m2) throws AnalyzerException {
        if ((m2.access & 0x500) != 0) {
            this.frames = new Frame[0];
            return this.frames;
        }
        this.n = m2.instructions.size();
        this.insns = m2.instructions;
        this.handlers = new List[this.n];
        this.frames = new Frame[this.n];
        this.subroutines = new Subroutine[this.n];
        this.queued = new boolean[this.n];
        this.queue = new int[this.n];
        this.top = 0;
        for (int i2 = 0; i2 < m2.tryCatchBlocks.size(); ++i2) {
            TryCatchBlockNode tcb = m2.tryCatchBlocks.get(i2);
            int begin = this.insns.indexOf(tcb.start);
            int end = this.insns.indexOf(tcb.end);
            for (int j2 = begin; j2 < end; ++j2) {
                List<TryCatchBlockNode> insnHandlers = this.handlers[j2];
                if (insnHandlers == null) {
                    this.handlers[j2] = insnHandlers = new ArrayList<TryCatchBlockNode>();
                }
                insnHandlers.add(tcb);
            }
        }
        Subroutine main = new Subroutine(null, m2.maxLocals, null);
        ArrayList<AbstractInsnNode> subroutineCalls = new ArrayList<AbstractInsnNode>();
        HashMap<LabelNode, Subroutine> subroutineHeads = new HashMap<LabelNode, Subroutine>();
        this.findSubroutine(0, main, subroutineCalls);
        while (!subroutineCalls.isEmpty()) {
            JumpInsnNode jsr = (JumpInsnNode)subroutineCalls.remove(0);
            Subroutine sub = (Subroutine)subroutineHeads.get(jsr.label);
            if (sub == null) {
                sub = new Subroutine(jsr.label, m2.maxLocals, jsr);
                subroutineHeads.put(jsr.label, sub);
                this.findSubroutine(this.insns.indexOf(jsr.label), sub, subroutineCalls);
                continue;
            }
            sub.callers.add(jsr);
        }
        for (int i3 = 0; i3 < this.n; ++i3) {
            if (this.subroutines[i3] == null || this.subroutines[i3].start != null) continue;
            this.subroutines[i3] = null;
        }
        Frame<V> current = this.newFrame(m2.maxLocals, m2.maxStack);
        Frame<V> handler = this.newFrame(m2.maxLocals, m2.maxStack);
        current.setReturn(this.interpreter.newValue(Type.getReturnType(m2.desc)));
        Type[] args = Type.getArgumentTypes(m2.desc);
        int local = 0;
        if ((m2.access & 8) == 0) {
            Type ctype = Type.getObjectType(owner);
            current.setLocal(local++, this.interpreter.newValue(ctype));
        }
        for (int i4 = 0; i4 < args.length; ++i4) {
            current.setLocal(local++, this.interpreter.newValue(args[i4]));
            if (args[i4].getSize() != 2) continue;
            current.setLocal(local++, this.interpreter.newValue(null));
        }
        while (local < m2.maxLocals) {
            current.setLocal(local++, this.interpreter.newValue(null));
        }
        this.merge(0, current, null);
        this.init(owner, m2);
        while (this.top > 0) {
            int insn = this.queue[--this.top];
            Frame<V> f2 = this.frames[insn];
            Subroutine subroutine = this.subroutines[insn];
            this.queued[insn] = false;
            AbstractInsnNode insnNode = null;
            try {
                List<TryCatchBlockNode> insnHandlers;
                insnNode = m2.instructions.get(insn);
                int insnOpcode = insnNode.getOpcode();
                int insnType = insnNode.getType();
                if (insnType == 8 || insnType == 15 || insnType == 14) {
                    this.merge(insn + 1, f2, subroutine);
                    this.newControlFlowEdge(insn, insn + 1);
                } else {
                    LabelNode label;
                    int j3;
                    int jump;
                    current.init(f2).execute(insnNode, this.interpreter);
                    Subroutine subroutine2 = subroutine = subroutine == null ? null : subroutine.copy();
                    if (insnNode instanceof JumpInsnNode) {
                        JumpInsnNode j4 = (JumpInsnNode)insnNode;
                        if (insnOpcode != 167 && insnOpcode != 168) {
                            this.merge(insn + 1, current, subroutine);
                            this.newControlFlowEdge(insn, insn + 1);
                        }
                        jump = this.insns.indexOf(j4.label);
                        if (insnOpcode == 168) {
                            this.merge(jump, current, new Subroutine(j4.label, m2.maxLocals, j4));
                        } else {
                            this.merge(jump, current, subroutine);
                        }
                        this.newControlFlowEdge(insn, jump);
                    } else if (insnNode instanceof LookupSwitchInsnNode) {
                        LookupSwitchInsnNode lsi = (LookupSwitchInsnNode)insnNode;
                        jump = this.insns.indexOf(lsi.dflt);
                        this.merge(jump, current, subroutine);
                        this.newControlFlowEdge(insn, jump);
                        for (j3 = 0; j3 < lsi.labels.size(); ++j3) {
                            label = lsi.labels.get(j3);
                            jump = this.insns.indexOf(label);
                            this.merge(jump, current, subroutine);
                            this.newControlFlowEdge(insn, jump);
                        }
                    } else if (insnNode instanceof TableSwitchInsnNode) {
                        TableSwitchInsnNode tsi = (TableSwitchInsnNode)insnNode;
                        jump = this.insns.indexOf(tsi.dflt);
                        this.merge(jump, current, subroutine);
                        this.newControlFlowEdge(insn, jump);
                        for (j3 = 0; j3 < tsi.labels.size(); ++j3) {
                            label = tsi.labels.get(j3);
                            jump = this.insns.indexOf(label);
                            this.merge(jump, current, subroutine);
                            this.newControlFlowEdge(insn, jump);
                        }
                    } else if (insnOpcode == 169) {
                        if (subroutine == null) {
                            throw new AnalyzerException(insnNode, "RET instruction outside of a sub routine");
                        }
                        for (int i5 = 0; i5 < subroutine.callers.size(); ++i5) {
                            JumpInsnNode caller = subroutine.callers.get(i5);
                            int call = this.insns.indexOf(caller);
                            if (this.frames[call] == null) continue;
                            this.merge(call + 1, this.frames[call], current, this.subroutines[call], subroutine.access);
                            this.newControlFlowEdge(insn, call + 1);
                        }
                    } else if (insnOpcode != 191 && (insnOpcode < 172 || insnOpcode > 177)) {
                        if (subroutine != null) {
                            if (insnNode instanceof VarInsnNode) {
                                int var2 = ((VarInsnNode)insnNode).var;
                                subroutine.access[var2] = true;
                                if (insnOpcode == 22 || insnOpcode == 24 || insnOpcode == 55 || insnOpcode == 57) {
                                    subroutine.access[var2 + 1] = true;
                                }
                            } else if (insnNode instanceof IincInsnNode) {
                                int var3 = ((IincInsnNode)insnNode).var;
                                subroutine.access[var3] = true;
                            }
                        }
                        this.merge(insn + 1, current, subroutine);
                        this.newControlFlowEdge(insn, insn + 1);
                    }
                }
                if ((insnHandlers = this.handlers[insn]) == null) continue;
                for (int i6 = 0; i6 < insnHandlers.size(); ++i6) {
                    TryCatchBlockNode tcb = insnHandlers.get(i6);
                    Type type = tcb.type == null ? Type.getObjectType("java/lang/Throwable") : Type.getObjectType(tcb.type);
                    int jump = this.insns.indexOf(tcb.handler);
                    if (!this.newControlFlowExceptionEdge(insn, jump)) continue;
                    handler.init(f2);
                    handler.clearStack();
                    handler.push(this.interpreter.newValue(type));
                    this.merge(jump, handler, subroutine);
                }
            }
            catch (AnalyzerException e2) {
                throw new AnalyzerException(e2.node, "Error at instruction " + insn + ": " + e2.getMessage(), e2);
            }
            catch (Exception e3) {
                throw new AnalyzerException(insnNode, "Error at instruction " + insn + ": " + e3.getMessage(), e3);
            }
        }
        return this.frames;
    }

    private void findSubroutine(int insn, Subroutine sub, List<AbstractInsnNode> calls) throws AnalyzerException {
        while (true) {
            LabelNode l2;
            int i2;
            if (insn < 0 || insn >= this.n) {
                throw new AnalyzerException(null, "Execution can fall off end of the code");
            }
            if (this.subroutines[insn] != null) {
                return;
            }
            this.subroutines[insn] = sub.copy();
            AbstractInsnNode node = this.insns.get(insn);
            if (node instanceof JumpInsnNode) {
                if (node.getOpcode() == 168) {
                    calls.add(node);
                } else {
                    JumpInsnNode jnode = (JumpInsnNode)node;
                    this.findSubroutine(this.insns.indexOf(jnode.label), sub, calls);
                }
            } else if (node instanceof TableSwitchInsnNode) {
                TableSwitchInsnNode tsnode = (TableSwitchInsnNode)node;
                this.findSubroutine(this.insns.indexOf(tsnode.dflt), sub, calls);
                for (i2 = tsnode.labels.size() - 1; i2 >= 0; --i2) {
                    l2 = tsnode.labels.get(i2);
                    this.findSubroutine(this.insns.indexOf(l2), sub, calls);
                }
            } else if (node instanceof LookupSwitchInsnNode) {
                LookupSwitchInsnNode lsnode = (LookupSwitchInsnNode)node;
                this.findSubroutine(this.insns.indexOf(lsnode.dflt), sub, calls);
                for (i2 = lsnode.labels.size() - 1; i2 >= 0; --i2) {
                    l2 = lsnode.labels.get(i2);
                    this.findSubroutine(this.insns.indexOf(l2), sub, calls);
                }
            }
            List<TryCatchBlockNode> insnHandlers = this.handlers[insn];
            if (insnHandlers != null) {
                for (i2 = 0; i2 < insnHandlers.size(); ++i2) {
                    TryCatchBlockNode tcb = insnHandlers.get(i2);
                    this.findSubroutine(this.insns.indexOf(tcb.handler), sub, calls);
                }
            }
            switch (node.getOpcode()) {
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
            ++insn;
        }
    }

    public Frame<V>[] getFrames() {
        return this.frames;
    }

    public List<TryCatchBlockNode> getHandlers(int insn) {
        return this.handlers[insn];
    }

    protected void init(String owner, MethodNode m2) throws AnalyzerException {
    }

    protected Frame<V> newFrame(int nLocals, int nStack) {
        return new Frame(nLocals, nStack);
    }

    protected Frame<V> newFrame(Frame<? extends V> src) {
        return new Frame<V>(src);
    }

    protected void newControlFlowEdge(int insn, int successor) {
    }

    protected boolean newControlFlowExceptionEdge(int insn, int successor) {
        return true;
    }

    private void merge(int insn, Frame<V> frame, Subroutine subroutine) throws AnalyzerException {
        boolean changes;
        Frame<V> oldFrame = this.frames[insn];
        Subroutine oldSubroutine = this.subroutines[insn];
        if (oldFrame == null) {
            this.frames[insn] = this.newFrame(frame);
            changes = true;
        } else {
            changes = oldFrame.merge(frame, this.interpreter);
        }
        if (oldSubroutine == null) {
            if (subroutine != null) {
                this.subroutines[insn] = subroutine.copy();
                changes = true;
            }
        } else if (subroutine != null) {
            changes |= oldSubroutine.merge(subroutine);
        }
        if (changes && !this.queued[insn]) {
            this.queued[insn] = true;
            this.queue[this.top++] = insn;
        }
    }

    private void merge(int insn, Frame<V> beforeJSR, Frame<V> afterRET, Subroutine subroutineBeforeJSR, boolean[] access) throws AnalyzerException {
        boolean changes;
        Frame<V> oldFrame = this.frames[insn];
        Subroutine oldSubroutine = this.subroutines[insn];
        afterRET.merge(beforeJSR, access);
        if (oldFrame == null) {
            this.frames[insn] = this.newFrame(afterRET);
            changes = true;
        } else {
            changes = oldFrame.merge(afterRET, access);
        }
        if (oldSubroutine != null && subroutineBeforeJSR != null) {
            changes |= oldSubroutine.merge(subroutineBeforeJSR);
        }
        if (changes && !this.queued[insn]) {
            this.queued[insn] = true;
            this.queue[this.top++] = insn;
        }
    }
}

