/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.network;

import com.sun.webkit.network.ByteBufferAllocator;
import java.nio.ByteBuffer;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

final class ByteBufferPool {
    private final Queue<ByteBuffer> byteBuffers = new ConcurrentLinkedQueue<ByteBuffer>();
    private final int bufferSize;

    private ByteBufferPool(int n2) {
        this.bufferSize = n2;
    }

    static ByteBufferPool newInstance(int n2) {
        return new ByteBufferPool(n2);
    }

    ByteBufferAllocator newAllocator(int n2) {
        return new ByteBufferAllocatorImpl(n2);
    }

    private final class ByteBufferAllocatorImpl
    implements ByteBufferAllocator {
        private final Semaphore semaphore;

        private ByteBufferAllocatorImpl(int n2) {
            this.semaphore = new Semaphore(n2);
        }

        @Override
        public ByteBuffer allocate() throws InterruptedException {
            this.semaphore.acquire();
            ByteBuffer byteBuffer = (ByteBuffer)ByteBufferPool.this.byteBuffers.poll();
            if (byteBuffer == null) {
                byteBuffer = ByteBuffer.allocateDirect(ByteBufferPool.this.bufferSize);
            }
            return byteBuffer;
        }

        @Override
        public void release(ByteBuffer byteBuffer) {
            ByteBufferPool.this.byteBuffers.add(byteBuffer);
            this.semaphore.release();
        }
    }
}

