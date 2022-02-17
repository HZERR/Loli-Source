/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import org.pushingpixels.lafwidget.utils.TrackableThread;
import org.pushingpixels.substance.internal.utils.LazyResettableHashMap;

public class MemoryAnalyzer
extends TrackableThread {
    private long delay;
    private String filename;
    private static MemoryAnalyzer instance;
    private static boolean isStopRequest;
    private static ArrayList<String> usages;
    private static SimpleDateFormat sdf;

    private MemoryAnalyzer(long delay, String filename) {
        this.delay = delay;
        this.filename = filename;
        this.setName("Substance memory analyzer");
    }

    public static synchronized void commence(long delay, String filename) {
        if (instance == null) {
            instance = new MemoryAnalyzer(delay, filename);
            usages = new ArrayList();
            sdf = new SimpleDateFormat("HH:mm:ss.SSS");
            instance.start();
        }
    }

    @Override
    public synchronized void requestStop() {
        isStopRequest = true;
    }

    private static synchronized boolean hasStopRequest() {
        return isStopRequest;
    }

    public static boolean isRunning() {
        return instance != null;
    }

    public static synchronized void enqueueUsage(String usage) {
        if (instance != null) {
            usages.add(sdf.format(new Date()) + ": " + usage);
        }
    }

    public static synchronized ArrayList<String> getUsages() {
        ArrayList<String> copy = new ArrayList<String>();
        for (String usage : usages) {
            copy.add(usage);
        }
        usages.clear();
        return copy;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void run() {
        try {
            SwingUtilities.invokeAndWait(new Runnable(){

                /*
                 * WARNING - Removed try catching itself - possible behaviour change.
                 * Loose catch block
                 * Enabled aggressive block sorting
                 * Enabled unnecessary exception pruning
                 * Enabled aggressive exception aggregation
                 * Lifted jumps to return sites
                 */
                @Override
                public void run() {
                    BufferedWriter bw = null;
                    bw = new BufferedWriter(new FileWriter(new File(MemoryAnalyzer.this.filename), true));
                    bw.write(sdf.format(new Date()) + "\n");
                    UIDefaults uidefs = UIManager.getLookAndFeel().getDefaults();
                    Set keySet = uidefs.keySet();
                    LinkedList<String> keyList = new LinkedList<String>();
                    for (Object k2 : keySet) {
                        keyList.add((String)k2);
                    }
                    Collections.sort(keyList);
                    for (String string : keyList) {
                        Object v2 = uidefs.get(string);
                        if (v2 instanceof Integer) {
                            int intVal = uidefs.getInt(string);
                            bw.write(string + " (int) : " + intVal);
                        } else if (v2 instanceof Boolean) {
                            boolean boolVal = uidefs.getBoolean(string);
                            bw.write(string + " (bool) : " + boolVal);
                        } else if (v2 instanceof String) {
                            String strVal = uidefs.getString(string);
                            bw.write(string + " (string) : " + strVal);
                        } else if (v2 instanceof Dimension) {
                            Dimension dimVal = uidefs.getDimension(string);
                            bw.write(string + " (Dimension) : " + dimVal.width + "*" + dimVal.height);
                        } else if (v2 instanceof Insets) {
                            Insets insetsVal = uidefs.getInsets(string);
                            bw.write(string + " (Insets) : " + insetsVal.top + "*" + insetsVal.left + "*" + insetsVal.bottom + "*" + insetsVal.right);
                        } else if (v2 instanceof Color) {
                            Color colorVal = uidefs.getColor(string);
                            bw.write(string + " (int) : " + colorVal.getRed() + "," + colorVal.getGreen() + "," + colorVal.getBlue());
                        } else if (v2 instanceof Font) {
                            Font fontVal = uidefs.getFont(string);
                            bw.write(string + " (Font) : " + fontVal.getFontName() + "*" + fontVal.getSize());
                        } else {
                            bw.write(string + " (Object) : " + uidefs.get(string));
                        }
                        bw.write("\n");
                    }
                    if (bw == null) return;
                    try {
                        bw.close();
                        return;
                    }
                    catch (Exception exc) {
                        return;
                    }
                    catch (IOException ioe) {
                        MemoryAnalyzer.this.requestStop();
                        if (bw == null) return;
                        {
                            catch (Throwable throwable) {
                                if (bw == null) throw throwable;
                                try {
                                    bw.close();
                                    throw throwable;
                                }
                                catch (Exception exc) {
                                    // empty catch block
                                }
                                throw throwable;
                            }
                        }
                        try {
                            bw.close();
                            return;
                        }
                        catch (Exception exc) {
                            return;
                        }
                        catch (Throwable t2) {
                            if (bw == null) return;
                            try {
                                bw.close();
                                return;
                            }
                            catch (Exception exception) {
                                return;
                            }
                        }
                    }
                }
            });
        }
        catch (Exception exc) {
            this.requestStop();
        }
        BufferedWriter bw = null;
        while (!MemoryAnalyzer.hasStopRequest()) {
            bw = null;
            try {
                bw = new BufferedWriter(new FileWriter(new File(this.filename), true));
                bw.write(sdf.format(new Date()) + "\n");
                List<String> stats = LazyResettableHashMap.getStats();
                if (stats != null) {
                    for (String stat : stats) {
                        bw.write(stat + "\n");
                    }
                }
                ArrayList<String> usages = MemoryAnalyzer.getUsages();
                for (String usage : usages) {
                    bw.write(usage + "\n");
                }
                bw.write("UIManager has " + UIManager.getDefaults().size() + " entries\n");
                long heapSize = Runtime.getRuntime().totalMemory();
                long heapFreeSize = Runtime.getRuntime().freeMemory();
                int heapSizeKB = (int)(heapSize / 1024L);
                int takenHeapSizeKB = (int)((heapSize - heapFreeSize) / 1024L);
                bw.write("Heap : " + takenHeapSizeKB + " / " + heapSizeKB);
                bw.write("\n");
            }
            catch (IOException ioe) {
                this.requestStop();
            }
            finally {
                if (bw != null) {
                    try {
                        bw.close();
                    }
                    catch (Exception exc) {}
                }
            }
            try {
                MemoryAnalyzer.sleep(this.delay);
            }
            catch (InterruptedException interruptedException) {}
        }
    }

    static {
        isStopRequest = false;
    }
}

