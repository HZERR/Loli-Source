/*
 * Decompiled with CFR 0.150.
 */
package org.java_websocket;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;
import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketAdapter;
import org.java_websocket.WebSocketImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractWebSocket
extends WebSocketAdapter {
    private static final Logger log = LoggerFactory.getLogger(AbstractWebSocket.class);
    private boolean tcpNoDelay;
    private boolean reuseAddr;
    private Timer connectionLostTimer;
    private TimerTask connectionLostTimerTask;
    private int connectionLostTimeout = 60;
    private boolean websocketRunning = false;
    private final Object syncConnectionLost = new Object();

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public int getConnectionLostTimeout() {
        Object object = this.syncConnectionLost;
        synchronized (object) {
            return this.connectionLostTimeout;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setConnectionLostTimeout(int connectionLostTimeout) {
        Object object = this.syncConnectionLost;
        synchronized (object) {
            this.connectionLostTimeout = connectionLostTimeout;
            if (this.connectionLostTimeout <= 0) {
                log.trace("Connection lost timer stopped");
                this.cancelConnectionLostTimer();
                return;
            }
            if (this.websocketRunning) {
                log.trace("Connection lost timer restarted");
                try {
                    ArrayList<WebSocket> connections = new ArrayList<WebSocket>(this.getConnections());
                    for (WebSocket conn : connections) {
                        if (!(conn instanceof WebSocketImpl)) continue;
                        WebSocketImpl webSocketImpl = (WebSocketImpl)conn;
                        webSocketImpl.updateLastPong();
                    }
                }
                catch (Exception e2) {
                    log.error("Exception during connection lost restart", e2);
                }
                this.restartConnectionLostTimer();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void stopConnectionLostTimer() {
        Object object = this.syncConnectionLost;
        synchronized (object) {
            if (this.connectionLostTimer != null || this.connectionLostTimerTask != null) {
                this.websocketRunning = false;
                log.trace("Connection lost timer stopped");
                this.cancelConnectionLostTimer();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void startConnectionLostTimer() {
        Object object = this.syncConnectionLost;
        synchronized (object) {
            if (this.connectionLostTimeout <= 0) {
                log.trace("Connection lost timer deactivated");
                return;
            }
            log.trace("Connection lost timer started");
            this.websocketRunning = true;
            this.restartConnectionLostTimer();
        }
    }

    private void restartConnectionLostTimer() {
        this.cancelConnectionLostTimer();
        this.connectionLostTimer = new Timer("WebSocketTimer");
        this.connectionLostTimerTask = new TimerTask(){
            private ArrayList<WebSocket> connections = new ArrayList();

            @Override
            public void run() {
                this.connections.clear();
                try {
                    this.connections.addAll(AbstractWebSocket.this.getConnections());
                    long current = System.currentTimeMillis() - (long)(AbstractWebSocket.this.connectionLostTimeout * 1500);
                    for (WebSocket conn : this.connections) {
                        AbstractWebSocket.this.executeConnectionLostDetection(conn, current);
                    }
                }
                catch (Exception exception) {
                    // empty catch block
                }
                this.connections.clear();
            }
        };
        this.connectionLostTimer.scheduleAtFixedRate(this.connectionLostTimerTask, 1000L * (long)this.connectionLostTimeout, 1000L * (long)this.connectionLostTimeout);
    }

    private void executeConnectionLostDetection(WebSocket webSocket, long current) {
        if (!(webSocket instanceof WebSocketImpl)) {
            return;
        }
        WebSocketImpl webSocketImpl = (WebSocketImpl)webSocket;
        if (webSocketImpl.getLastPong() < current) {
            log.trace("Closing connection due to no pong received: {}", (Object)webSocketImpl);
            webSocketImpl.closeConnection(1006, "The connection was closed because the other endpoint did not respond with a pong in time. For more information check: https://github.com/TooTallNate/Java-WebSocket/wiki/Lost-connection-detection");
        } else if (webSocketImpl.isOpen()) {
            webSocketImpl.sendPing();
        } else {
            log.trace("Trying to ping a non open connection: {}", (Object)webSocketImpl);
        }
    }

    protected abstract Collection<WebSocket> getConnections();

    private void cancelConnectionLostTimer() {
        if (this.connectionLostTimer != null) {
            this.connectionLostTimer.cancel();
            this.connectionLostTimer = null;
        }
        if (this.connectionLostTimerTask != null) {
            this.connectionLostTimerTask.cancel();
            this.connectionLostTimerTask = null;
        }
    }

    public boolean isTcpNoDelay() {
        return this.tcpNoDelay;
    }

    public void setTcpNoDelay(boolean tcpNoDelay) {
        this.tcpNoDelay = tcpNoDelay;
    }

    public boolean isReuseAddr() {
        return this.reuseAddr;
    }

    public void setReuseAddr(boolean reuseAddr) {
        this.reuseAddr = reuseAddr;
    }
}

