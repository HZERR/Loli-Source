/*
 * Decompiled with CFR 0.150.
 */
package org.java_websocket.server;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.List;
import org.java_websocket.WebSocketAdapter;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.WebSocketListener;
import org.java_websocket.WebSocketServerFactory;
import org.java_websocket.drafts.Draft;

public class DefaultWebSocketServerFactory
implements WebSocketServerFactory {
    @Override
    public WebSocketImpl createWebSocket(WebSocketAdapter a2, Draft d2) {
        return new WebSocketImpl((WebSocketListener)a2, d2);
    }

    @Override
    public WebSocketImpl createWebSocket(WebSocketAdapter a2, List<Draft> d2) {
        return new WebSocketImpl((WebSocketListener)a2, d2);
    }

    @Override
    public SocketChannel wrapChannel(SocketChannel channel, SelectionKey key) {
        return channel;
    }

    @Override
    public void close() {
    }
}

