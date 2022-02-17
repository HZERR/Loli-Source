/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser;

import java.awt.AWTException;
import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.security.AccessControlException;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.plaf.IconUIResource;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.internal.utils.SubstanceImageCreator;
import org.pushingpixels.substance.internal.utils.icon.TransitionAwareIcon;

public class ColorPicker
extends AbstractColorChooserPanel {
    private Dialog pickerFrame;
    private Timer pickerTimer;
    private BufferedImage cursorImage;
    private Graphics2D cursorGraphics;
    private Cursor pickerCursor;
    private Point hotSpot;
    private Point pickOffset;
    private BufferedImage magnifierImage;
    private Robot robot;
    private Color previousColor = Color.white;
    private Point previousLoc = new Point();
    private Point pickLoc = new Point();
    private Point captureOffset = new Point();
    private Rectangle captureRect;
    private static final Color transparentColor = new Color(0, true);
    private Rectangle zoomRect;
    private Rectangle glassRect;
    private JButton pickerButton;

    public ColorPicker() {
        try {
            this.robot = new Robot();
            this.robot.createScreenCapture(new Rectangle(0, 0, 1, 1));
        }
        catch (AWTException e2) {
            throw new AccessControlException("Unable to capture screen");
        }
    }

    private Dialog getPickerFrame() {
        if (this.pickerFrame == null) {
            Window owner = SwingUtilities.getWindowAncestor(this);
            this.pickerFrame = owner instanceof Dialog ? new Dialog((Dialog)owner) : (owner instanceof Frame ? new Dialog((Frame)owner) : new Dialog(new JFrame()));
            this.pickerFrame.addMouseListener(new MouseAdapter(){

                @Override
                public void mousePressed(MouseEvent evt) {
                    ColorPicker.this.pickFinish();
                }

                @Override
                public void mouseExited(MouseEvent evt) {
                    ColorPicker.this.updatePicker();
                }
            });
            this.pickerFrame.addMouseMotionListener(new MouseMotionAdapter(){

                @Override
                public void mouseMoved(MouseEvent evt) {
                    ColorPicker.this.updatePicker();
                }
            });
            this.pickerFrame.setSize(3, 3);
            this.pickerFrame.setUndecorated(true);
            this.pickerFrame.setAlwaysOnTop(true);
            this.pickerFrame.addKeyListener(new KeyAdapter(){

                @Override
                public void keyPressed(KeyEvent e2) {
                    switch (e2.getKeyCode()) {
                        case 27: {
                            ColorPicker.this.pickCancel();
                            break;
                        }
                        case 10: {
                            ColorPicker.this.pickFinish();
                        }
                    }
                }
            });
            this.magnifierImage = (BufferedImage)UIManager.get("ColorChooser.colorPickerMagnifier");
            this.glassRect = (Rectangle)UIManager.get("ColorChooser.colorPickerGlassRect");
            this.zoomRect = (Rectangle)UIManager.get("ColorChooser.colorPickerZoomRect");
            this.hotSpot = (Point)UIManager.get("ColorChooser.colorPickerHotSpot");
            this.captureRect = new Rectangle((Rectangle)UIManager.get("ColorChooser.colorPickerCaptureRect"));
            this.pickOffset = (Point)UIManager.get("ColorChooser.colorPickerPickOffset");
            this.captureOffset = new Point(this.captureRect.x, this.captureRect.y);
            this.cursorImage = this.getGraphicsConfiguration().createCompatibleImage(this.magnifierImage.getWidth(), this.magnifierImage.getHeight(), 3);
            this.cursorGraphics = this.cursorImage.createGraphics();
            this.cursorGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            this.pickerTimer = new Timer(5, new ActionListener(){

                @Override
                public void actionPerformed(ActionEvent evt) {
                    ColorPicker.this.updatePicker();
                }
            });
        }
        return this.pickerFrame;
    }

    protected void updatePicker() {
        if (this.pickerFrame != null && this.pickerFrame.isShowing()) {
            Color c2;
            PointerInfo info = MouseInfo.getPointerInfo();
            if (info == null) {
                return;
            }
            Point mouseLoc = info.getLocation();
            this.pickerFrame.setLocation(mouseLoc.x - this.pickerFrame.getWidth() / 2, mouseLoc.y - this.pickerFrame.getHeight() / 2);
            this.pickLoc.x = mouseLoc.x + this.pickOffset.x;
            this.pickLoc.y = mouseLoc.y + this.pickOffset.y;
            if (!(this.pickLoc.x < 0 || this.pickLoc.y < 0 || (c2 = this.robot.getPixelColor(this.pickLoc.x, this.pickLoc.y)).equals(this.previousColor) && mouseLoc.equals(this.previousLoc))) {
                this.previousColor = c2;
                this.previousLoc = mouseLoc;
                this.captureRect.setLocation(mouseLoc.x + this.captureOffset.x, mouseLoc.y + this.captureOffset.y);
                if (this.captureRect.x >= 0 && this.captureRect.y >= 0) {
                    BufferedImage capture = this.robot.createScreenCapture(this.captureRect);
                    this.cursorGraphics.setComposite(AlphaComposite.Src);
                    this.cursorGraphics.setColor(transparentColor);
                    this.cursorGraphics.fillRect(0, 0, this.cursorImage.getWidth(), this.cursorImage.getHeight());
                    this.cursorGraphics.setColor(Color.red);
                    this.cursorGraphics.fillOval(this.glassRect.x, this.glassRect.y, this.glassRect.width, this.glassRect.height);
                    this.cursorGraphics.setComposite(AlphaComposite.SrcIn);
                    this.cursorGraphics.drawImage(capture, this.zoomRect.x, this.zoomRect.y, this.zoomRect.width, this.zoomRect.height, this);
                    this.cursorGraphics.setComposite(AlphaComposite.SrcOver);
                    this.cursorGraphics.drawImage((Image)this.magnifierImage, 0, 0, this);
                    BufferedImage subImage = this.cursorImage.getSubimage(0, 0, this.cursorImage.getWidth(), this.cursorImage.getHeight());
                    this.pickerFrame.setCursor(this.getToolkit().createCustomCursor(this.cursorImage, this.hotSpot, "ColorPicker"));
                }
            }
        }
    }

    private void initComponents() {
        this.pickerButton = new JButton();
        this.setLayout(new BorderLayout());
        this.pickerButton.setBorderPainted(false);
        this.pickerButton.setMargin(new Insets(0, 0, 0, 0));
        this.pickerButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                ColorPicker.this.pickBegin(evt);
            }
        });
        this.add((Component)this.pickerButton, "Center");
    }

    private void pickBegin(ActionEvent evt) {
        this.getPickerFrame();
        this.pickerTimer.start();
        this.getPickerFrame().setVisible(true);
    }

    protected void pickFinish() {
        this.pickerTimer.stop();
        this.pickerFrame.setVisible(false);
        PointerInfo info = MouseInfo.getPointerInfo();
        if (info == null) {
            return;
        }
        Point loc = info.getLocation();
        Color c2 = this.robot.getPixelColor(loc.x + this.pickOffset.x, loc.y + this.pickOffset.y);
        this.getColorSelectionModel().setSelectedColor(c2);
    }

    protected void pickCancel() {
        this.pickerTimer.stop();
        this.pickerFrame.setVisible(false);
    }

    @Override
    protected void buildChooser() {
        this.initComponents();
        this.pickerButton.setIcon(new TransitionAwareIcon(this.pickerButton, new TransitionAwareIcon.Delegate(){

            @Override
            public Icon getColorSchemeIcon(SubstanceColorScheme scheme) {
                return new IconUIResource(SubstanceImageCreator.getSearchIcon(15, scheme, ColorPicker.this.pickerButton.getComponentOrientation().isLeftToRight()));
            }
        }, "ColorChooser.colorPickerIcon"));
    }

    @Override
    public String getDisplayName() {
        return "Color Picker";
    }

    @Override
    public Icon getLargeDisplayIcon() {
        return UIManager.getIcon("ColorChooser.colorPickerIcon");
    }

    @Override
    public Icon getSmallDisplayIcon() {
        return this.getLargeDisplayIcon();
    }

    @Override
    public void updateChooser() {
    }
}

