/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.CharacterDataImpl;
import org.w3c.dom.Comment;

public class CommentImpl
extends CharacterDataImpl
implements Comment {
    CommentImpl(long l2) {
        super(l2);
    }

    static Comment getImpl(long l2) {
        return (Comment)CommentImpl.create(l2);
    }
}

