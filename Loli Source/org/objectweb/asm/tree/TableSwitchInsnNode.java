/*
 * Decompiled with CFR 0.150.
 */
package org.objectweb.asm.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LabelNode;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class TableSwitchInsnNode
extends AbstractInsnNode {
    public int min;
    public int max;
    public LabelNode dflt;
    public List<LabelNode> labels;

    public TableSwitchInsnNode(int min, int max, LabelNode dflt, LabelNode ... labels) {
        super(170);
        this.min = min;
        this.max = max;
        this.dflt = dflt;
        this.labels = new ArrayList<LabelNode>();
        if (labels != null) {
            this.labels.addAll(Arrays.asList(labels));
        }
    }

    @Override
    public int getType() {
        return 11;
    }

    @Override
    public void accept(MethodVisitor mv) {
        Label[] labels = new Label[this.labels.size()];
        for (int i2 = 0; i2 < labels.length; ++i2) {
            labels[i2] = this.labels.get(i2).getLabel();
        }
        mv.visitTableSwitchInsn(this.min, this.max, this.dflt.getLabel(), labels);
    }

    @Override
    public AbstractInsnNode clone(Map<LabelNode, LabelNode> labels) {
        return new TableSwitchInsnNode(this.min, this.max, TableSwitchInsnNode.clone(this.dflt, labels), TableSwitchInsnNode.clone(this.labels, labels));
    }
}

