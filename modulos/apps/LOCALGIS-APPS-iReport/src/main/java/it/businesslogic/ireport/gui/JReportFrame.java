/*
 * Copyright (C) 2005 - 2007 JasperSoft Corporation.  All rights reserved.
 * http://www.jaspersoft.com.
 *
 * Unless you have purchased a commercial license agreement from JasperSoft,
 * the following license terms apply:
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; and without the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see http://www.gnu.org/licenses/gpl.txt
 * or write to:
 *
 * Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330,
 * Boston, MA  USA  02111-1307
 *
 *
 *
 *
 * JReportFrame.java
 *
 * Created on 12 febbraio 2003, 21.51
 *
 */

package it.businesslogic.ireport.gui;
import it.businesslogic.ireport.Band;
import it.businesslogic.ireport.BreakReportElement;
import it.businesslogic.ireport.ChartReportElement;
import it.businesslogic.ireport.ChartReportElement2;
import it.businesslogic.ireport.CrosstabReportElement;
import it.businesslogic.ireport.DesignVerifyerThread;
import it.businesslogic.ireport.FrameReportElement;
import it.businesslogic.ireport.GraphicReportElement;
import it.businesslogic.ireport.IReportFont;
import it.businesslogic.ireport.LineReportElement;
import it.businesslogic.ireport.OperationType;
import it.businesslogic.ireport.Report;
import it.businesslogic.ireport.ReportClassLoader;
import it.businesslogic.ireport.ReportElement;
import it.businesslogic.ireport.ReportElementFactory;
import it.businesslogic.ireport.ReportElementType;
import it.businesslogic.ireport.StaticTextReportElement;
import it.businesslogic.ireport.Style;
import it.businesslogic.ireport.SubReportElement;
import it.businesslogic.ireport.TextFieldReportElement;
import it.businesslogic.ireport.TextReportElement;
import it.businesslogic.ireport.TransformationType;
import it.businesslogic.ireport.UndoOperation;
import it.businesslogic.ireport.chart.gui.ChartSelectionJDialog;
import it.businesslogic.ireport.crosstab.CrosstabCell;
import it.businesslogic.ireport.crosstab.gui.CrosstabEditor;
import it.businesslogic.ireport.crosstab.gui.CrosstabEditorPanel;
import it.businesslogic.ireport.crosstab.gui.CrosstabWizardDialog;
import it.businesslogic.ireport.gui.command.FormatCommand;
import it.businesslogic.ireport.gui.event.ReportBandChangedEvent;
import it.businesslogic.ireport.gui.event.ReportBandsSelectionEvent;
import it.businesslogic.ireport.gui.event.ReportElementChangedEvent;
import it.businesslogic.ireport.gui.event.ReportElementsSelectionEvent;
import it.businesslogic.ireport.gui.event.ReportObjectsSelectionEvent;
import it.businesslogic.ireport.gui.wizard.SubreportWizard;
import it.businesslogic.ireport.undo.BandDraggedOperation;
import it.businesslogic.ireport.undo.ChangeEmentsOrderOperation;
import it.businesslogic.ireport.undo.DeleteElementsOperation;
import it.businesslogic.ireport.undo.GroupEmentsOperation;
import it.businesslogic.ireport.undo.GroupPositionedElement;
import it.businesslogic.ireport.undo.InsertElementOperation;
import it.businesslogic.ireport.undo.PasteStyleOperation;
import it.businesslogic.ireport.undo.PositionedElement;
import it.businesslogic.ireport.undo.ReplacedElementsOperation;
import it.businesslogic.ireport.undo.TransformElementsOperation;
import it.businesslogic.ireport.undo.UnGroupEmentsOperation;
import it.businesslogic.ireport.util.I18n;
import it.businesslogic.ireport.util.Java2DUtil;
import it.businesslogic.ireport.util.LanguageChangedEvent;
import it.businesslogic.ireport.util.LanguageChangedListener;
import it.businesslogic.ireport.util.Misc;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.TexturePaint;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import bsh.Interpreter;

/**
 *
 * @author  Administrator
 */
public class JReportFrame extends JMDIFrame implements LanguageChangedListener {

    static int id = 0;
    static java.awt.Cursor hsplit = null;
    //private Graphics2D offscreen = null;
    //private Graphics2D offscreenDoc = null;
    //private BufferedImage offscreenImage = null;
    //private BufferedImage offscreenImageDoc = null;
    private int offscreenWidth = 0;
    private Dimension offscreenDimension = null;
    private boolean isDocDirty = true;

    private MainFrame mf = null;
    private BufferedImage[] shadowsImages = null;

    /** This point rappresent coords of left top corner of the popup menu.... */
    private java.awt.Point popup_opened_at = null;

    /** true if the the user is trnsforming a new element....*/
    private boolean transforming = false;

    /** We are dragging a band ?  */
    boolean band_dragging=false;
    /** We have started to drag a band from here */
    int band_dragging_origin=0;
    /** We are dragging this band... */
    Band band_dragging_band=null;
    /** State for XOR operation in band dragging... */
    boolean first_draw_band=true;
    boolean firstXORDraw = false ; //= false;   // why not true?
    boolean firstXORDrawTransforming = false;
    
    boolean paintedXORDrawAlign = false;
    java.util.List paintedAlignLines = new java.util.ArrayList();

    int tabs = 0;

    private java.util.List reportProblems = new java.util.ArrayList();
    
    private TexturePaint mGridTexture = null;

    /**
     * This flag is true if we are selecting elements drawing a rectangle...
     */
    private boolean drag_selection_mode = false;
    /**
     * The origin of the selection rectangle...
     */
    private java.awt.Point drag_selection_origin = null;
    /**
     * The end of the selection rectangle...
     */
    private java.awt.Point drag_selection_end = null;
    /**
     * The flag is used to handle XOR operation on rectangle selection
     */
    private boolean first_draw_selection_rect = true;

    /**
     * Stroke to use when draw selection rectangle
     */
    private java.awt.Stroke selectionStroke = null;

    private JRulePanel jVerticalRule = null;
    
    private java.util.List verticalObjectsLines = new java.util.ArrayList();
    private java.util.List horizontalObjectsLines = new java.util.ArrayList();
    
    // Menus...
    private javax.swing.JMenu jMenuAlign;
    private javax.swing.JMenuItem jMenuItemAlignLeft;
    private javax.swing.JMenuItem jMenuItemAlignRight;
    private javax.swing.JMenuItem jMenuItemAlignTop;
    private javax.swing.JMenuItem jMenuItemAlignBottom;
    private javax.swing.JSeparator jSeparator19;
    private javax.swing.JMenuItem jMenuItemAlignVerticalAxis;
    private javax.swing.JMenuItem jMenuItemAlignHorizontalAxis ;
    private javax.swing.JSeparator jSeparator20;
    private javax.swing.JMenuItem jMenuItemAlignToBandTop;
    private javax.swing.JMenuItem jMenuItemAlignToBandBottom;
    private javax.swing.JMenu jMenuSize;
    private javax.swing.JMenuItem jMenuItemSameWidth;
    private javax.swing.JMenuItem jMenuItemSameWidthMax;
    private javax.swing.JMenuItem jMenuItemSameWidthMin;
    private javax.swing.JSeparator jSeparator17;
    private javax.swing.JMenuItem jMenuItemSameHeight;
    private javax.swing.JMenuItem jMenuItemSameHeightMax;
    private javax.swing.JMenuItem jMenuItemSameHeightMin;
    private javax.swing.JSeparator jSeparator18;
    private javax.swing.JMenuItem jMenuItemSameSize;
    private javax.swing.JMenu jMenuPosition;
    private javax.swing.JMenuItem jMenuItemCenterH;
    private javax.swing.JMenuItem jMenuItemCenterV;
    private javax.swing.JMenuItem jMenuItemCenterInBand;
    private javax.swing.JMenuItem jMenuItemCenterBackground;
    private javax.swing.JMenuItem jMenuItemJoinLeft;
    private javax.swing.JMenuItem jMenuItemJoinRight;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JMenuItem jMenuHSpacing;
    private javax.swing.JMenuItem jMenuItemHSMakeEqual;
    private javax.swing.JMenuItem jMenuItemHSIncrease;
    private javax.swing.JMenuItem jMenuItemHSDecrease;
    private javax.swing.JMenuItem jMenuItemHSRemove;
    private javax.swing.JMenuItem jMenuVSpacing;
    private javax.swing.JMenuItem jMenuItemVSMakeEqual;
    private javax.swing.JMenuItem jMenuItemVSIncrease;
    private javax.swing.JMenuItem jMenuItemVSDecrease;
    private javax.swing.JMenuItem jMenuItemVSRemove;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JMenuItem jMenuItemBringToFront;
    private javax.swing.JMenuItem jMenuItemSendToBack;
    private javax.swing.JPopupMenu jPopupMenuElementMS;
    private javax.swing.JMenuItem jMenuItemOrganize;
    private javax.swing.JMenuItem jMenuItemRightMargin;
    private javax.swing.JMenuItem jMenuItemLeftMargin;
    
    private javax.swing.JMenuItem jMenuItemEditExpression;

    private JTextPane floatingTextArea = new JTextPane(); //JTextArea();
    private String previousFloatingTextAreaValue = "";

    private java.util.List openedNodesDocumentStructure = null;

    private TextReportElement onlineEditingTextReportElement = null;
    
    private DesignVerifyerThread designVerifyerThread = null;
    
    private Vector selectedBands = new Vector();
    private Vector selectedObjects = new Vector();
    
    private static int MAGNETIC_POWER = 5;


    /*
     *  This variable is used to say if the resistence moving an element with mouse was
     *  exceeded
     */
    boolean resistenceExceeded = false;

    Point newObjectOrigin = null;

    boolean trasforming = false;
    int transformation_type= -1;
    Point transformation_origin=null;
    Point transformation_origin_end=null;
    Point transformation_undo_delta=null;
    
    // Used to draw matching lines...
    int matchedVerticalLine = -1;
    int matchedHorizontalLine = -1;

    

    /** Creates new form JReportFrame */
    public JReportFrame(Report report) {

        initComponents();
        
        jMenuItemEditExpression = new JMenuItem();
        jMenuItemEditExpression.setIcon(new javax.swing.ImageIcon(""));
        jMenuItemEditExpression.setText("Edit expression");
        jMenuItemEditExpression.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemEditExpressionActionPerformed(evt);
            }
        });
        jPopupMenuElement.insert(jMenuItemEditExpression, 0);

        jHorizontalRule.setJReportFrame( this );
        jHorizontalRule.repaint();
        jPanelReportContainer.setIgnoreRepaint(true);

        jVerticalRule = new JRulePanel();
        jVerticalRule.setType(jVerticalRule.TYPE_VERTICAL);
        jVerticalRule.setJReportFrame( this );
        jPanelVRule.add(jVerticalRule, java.awt.BorderLayout.CENTER);
        jVerticalRule.repaint();

        addFormatItemsToMenu(jPopupMenuElement);
        this.windowID = id++;

        floatingTextArea.setOpaque(true);
        floatingTextArea.addFocusListener( new FocusListener() {
            public void focusGained(FocusEvent e) {

            }
            public void focusLost(FocusEvent e) {
                floatingTextAreaFocusLost();
            }
        });

        floatingTextArea.addKeyListener( new KeyListener()
            {
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == e.VK_ENTER && (e.getModifiers() & e.SHIFT_DOWN_MASK) == 0)
                    {
                        e.consume();
                        floatingTextAreaFocusLost();
                    }
                    if (e.getKeyCode() == e.VK_ESCAPE)
                    {
                        e.consume();
                        floatingTextAreaFocusLost(false);
                    }
                }
                public void keyReleased(KeyEvent e) {
                }
                public void keyTyped(KeyEvent e) {
                }
            });



        floatingTextArea.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                floatingTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                floatingTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                floatingTextChanged();
            }
        });

        selectionStroke = new java.awt.BasicStroke(
                (float)(2f),
                java.awt.BasicStroke.CAP_BUTT,
                java.awt.BasicStroke.JOIN_BEVEL,
                0f,
                new float[]{5f, 3f},
                0f
                        );
                this.setPreferredSize(new Dimension(350,400));
                this.setNormalSize(new Dimension(350,400));
                if (hsplit == null) {
                    hsplit = Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR);
                }
                this.jPanelReport.setJrf(this);
                this.report = report;

                this.newObjectType = ReportElementType.NONE;
                this.report=report;
                this.report.setReportFrame(this);
                this.setTitle(this.report.getName()+" "+this.report.getWidth()+"x"+this.report.getDesignHeight()+" ["+Misc.nvl(this.report.getFilename(),"unnamed")+"]");
                //System.out.println(this.getTitle());

                undoOperations = new Vector();
                clipboardObjects = new  Vector();
                selectedElements = new Vector();
                transformation_undo_delta = new Point(0,0);

                zoomFactor = 1.0;

                transformationMinSize =5;

                undoIndex = -1;

                // Init images...
                shadowsImages = new BufferedImage[5];

                shadowsImages[0] = Misc.loadBufferedImageFromResources(this,"it/businesslogic/ireport/icons/layout/sh_ur.gif");
                shadowsImages[1] = Misc.loadBufferedImageFromResources(this,"it/businesslogic/ireport/icons/layout/sh_r.gif");
                shadowsImages[2] = Misc.loadBufferedImageFromResources(this,"it/businesslogic/ireport/icons/layout/sh_lr.gif");
                shadowsImages[3] = Misc.loadBufferedImageFromResources(this,"it/businesslogic/ireport/icons/layout/sh_d.gif");
                shadowsImages[4] = Misc.loadBufferedImageFromResources(this,"it/businesslogic/ireport/icons/layout/sh_dl.gif");

        /*
        try {
            cursorplus = new Cursor(ResourceManager.getResource( getClass(), "cursorplus.cur"));
            cursorminus = new Cursor( ResourceManager.getResource(getClass(), "cursorminus.cur"));
        } catch (Exception ex)
        {}

        if (cursorplus == null )
        {
                if (com.ms.wfc.io.File.exists(com.ms.wfc.io.File.combine(  getProgramDir(), "com\\businesslogic\\ireport\\ui\\cursorplus.cur")))
                cursorplus = new Cursor(com.ms.wfc.io.File.combine(  getProgramDir(), "com\\businesslogic\\ireport\\ui\\cursorplus.cur"));
        }

        if (cursorminus == null )
        {
                if (com.ms.wfc.io.File.exists(com.ms.wfc.io.File.combine(  getProgramDir(), "com\\businesslogic\\ireport\\ui\\cursorminus.cur")))
                        cursorminus = new Cursor( com.ms.wfc.io.File.combine(  getProgramDir(), "com\\businesslogic\\ireport\\ui\\cursorminus.cur"));
        }

        if (cursorplus == null)
                cursorplus = Cursor.CROSS;
        if (cursorminus == null)
                cursorminus = Cursor.CROSS;
         */
        /*
        Enumeration enum = report.getElements().elements();
        Brush br = new Brush(imageList2.getBitmap(0));
        while (enum.hasMoreElements())
        {
                ReportElement re = (ReportElement)enum.nextElement();
                re.hached = br;

                if (re instanceof SubReportElement)
                {
                        ((SubReportElement)re).img = this.imageList4.getImage(0);
                }
                else if (re instanceof  ImageReportElement )
                {

                        ((ImageReportElement)re).defimg  = imageList3.getBitmap(0);
                }
                }
         */

                //offscreen.setBackground(new Color(128,128,128));
                //setBackground(new Color(128,128,128));
                //Screen screen = Screen.getPrimary();
                offscreenDimension = Toolkit.getDefaultToolkit().getScreenSize();
                //offscreenImage = new java.awt.image.BufferedImage(offscreenDimension.width, offscreenDimension.height, java.awt.image.BufferedImage.TYPE_4BYTE_ABGR_PRE);
                //offscreenImageDoc  = new java.awt.image.BufferedImage(offscreenDimension.width, offscreenDimension.height, java.awt.image.BufferedImage.TYPE_4BYTE_ABGR_PRE);
                //offscreen = GraphicsEnvironment.getLocalGraphicsEnvironment().createGraphics(offscreenImage);

                //offscreen.setBackground(new Color(128,128,128));
                //offscreen.clearRect(0,0, offscreenDimension.width, offscreenDimension.height);
                //offscreen.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
                //offscreen.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
                //offscreen.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);

                //offscreenDoc = GraphicsEnvironment.getLocalGraphicsEnvironment().createGraphics(offscreenImageDoc);
                //offscreenDoc.setBackground(new Color(128,128,128));
                //offscreenDoc.clearRect(0,0, offscreenDimension.width, offscreenDimension.height);
                //offscreenDoc.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
                //offscreenDoc.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
                //offscreenDoc.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
                //offscreen.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                //redrawAll(offscreen,new Rectangle(0,0,report.getWidth(),report.getHeight()), 0);

                //redrawAll( offscreen,new Rectangle(0,0,report.getWidth(),report.getHeight()), 0);

                this.jHorizontalScrollBar.setValue(0);
                this.jVerticalScrollBar.setValue(0);

                updateTabs();

                for (int i=0; i<report.getElements().size(); ++i)
                {
                    ReportElement re = (ReportElement)report.getElements().elementAt(i);
                    if (re instanceof  CrosstabReportElement)
                    {
                        addCrosstabEditor( (CrosstabReportElement)re);
                    }
                }

                designVerifyerThread = new DesignVerifyerThread(this);
                designVerifyerThread.start();
                this.applyI18n();
    }

    public void floatingTextAreaFocusLost()
    {
        floatingTextAreaFocusLost(true);
    }

    public void floatingTextAreaFocusLost(boolean acceptModifications)
    {

        try {
            if (onlineEditingTextReportElement != null)
            {
                if (acceptModifications)
                {
                        onlineEditingTextReportElement.setText( floatingTextArea.getText() );
                        ReportElementChangedEvent changedEvent = new ReportElementChangedEvent(this , getSelectedElements() , ReportElementChangedEvent.CHANGED);
                        changedEvent.setEventSource( this );
                        changedEvent.setPropertyChanged( "text" );
                        changedEvent.setNewValue( floatingTextArea.getText() );
                        fireReportListenerReportElementsChanged(changedEvent);
                        getMainFrame().getElementPropertiesDialog().updateSelection();
                }
                else
                {
                    floatingTextArea.setText( onlineEditingTextReportElement.getText());
                }
            }

            onlineEditingTextReportElement = null;
            getReportPanel().remove( floatingTextArea );
            getReportPanel().updateUI();

        } catch (Exception ex) {}
    }


    /**
     * Return the element at the point location (if any...)
     */
    public ReportElement getElementAt(Point location)
    {
        Point p = new Point(getLogicalDim((int)location.getX()+jHorizontalScrollBar.getValue()-10)+10,
                        getLogicalDim((int)location.getY()+jVerticalScrollBar.getValue()-10)+10);

        for (int i=report.getElements().size()-1; i>=0; --i)
            {

                ReportElement re = (ReportElement)report.getElements().elementAt(i);
                if (re.intersects(p))
                {
                    return re;
                }
            }

        return null;
    }

    public void floatingTextChanged()
    {

    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPopupMenuBand = new javax.swing.JPopupMenu();
        jMenuItemPasteOnBand = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        jMenuItemBandProperties = new javax.swing.JMenuItem();
        jPopupMenuElement = new javax.swing.JPopupMenu();
        jMenuItemElementProperties = new javax.swing.JMenuItem();
        jMenuItemElementChartProperties = new javax.swing.JMenuItem();
        jMenuItemElementCrosstabProperties = new javax.swing.JMenuItem();
        jMenuItemElementCrosstabDesignProperties = new javax.swing.JMenuItem();
        jMenuItemElementOpenSubreport = new javax.swing.JMenuItem();
        jMenuItemBandProperties1 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        jMenuItemCut = new javax.swing.JMenuItem();
        jMenuItemCopy = new javax.swing.JMenuItem();
        jMenuItemPaste = new javax.swing.JMenuItem();
        jMenuItemDelete = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JSeparator();
        jMenuItemGroup = new javax.swing.JMenuItem();
        jMenuItemUngroup = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JSeparator();
        jMenuItemCopyStyle = new javax.swing.JMenuItem();
        jMenuItemPasteStyle = new javax.swing.JMenuItem();
        jMenuItemTransformStaticText = new javax.swing.JMenuItem();
        jMenuItemPattern = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JSeparator();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanelSuperContainer = new javax.swing.JPanel();
        jPanelReportContainer = new javax.swing.JPanel();
        jPanelVRule = new javax.swing.JPanel();
        jPanelHContainerRule = new javax.swing.JPanel();
        jPanelCorner = new javax.swing.JPanel();
        jHorizontalRule = new it.businesslogic.ireport.gui.JRulePanel();
        jPanelReport = new it.businesslogic.ireport.gui.JReportPanel();
        jVerticalScrollBar = new javax.swing.JScrollBar();
        jPanelHScroll = new javax.swing.JPanel();
        jHorizontalScrollBar = new javax.swing.JScrollBar();
        jPanel1 = new javax.swing.JPanel();

        jMenuItemPasteOnBand.setIcon(new javax.swing.ImageIcon(""));
        jMenuItemPasteOnBand.setText("Paste here...");
        jMenuItemPasteOnBand.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPasteOnBandActionPerformed(evt);
            }
        });
        jMenuItemPasteOnBand.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuItemPasteOnBandMouseClicked(evt);
            }
        });

        jPopupMenuBand.add(jMenuItemPasteOnBand);

        jPopupMenuBand.add(jSeparator2);

        jMenuItemBandProperties.setIcon(new javax.swing.ImageIcon(""));
        jMenuItemBandProperties.setText("Band properties");
        jMenuItemBandProperties.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemBandPropertiesActionPerformed(evt);
            }
        });

        jPopupMenuBand.add(jMenuItemBandProperties);

        jMenuItemElementProperties.setIcon(new javax.swing.ImageIcon(""));
        jMenuItemElementProperties.setText("Properties");
        jMenuItemElementProperties.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemElementPropertiesActionPerformed(evt);
            }
        });
        jMenuItemElementProperties.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuItemElementPropertiesMouseClicked(evt);
            }
        });

        jPopupMenuElement.add(jMenuItemElementProperties);

        jMenuItemElementChartProperties.setIcon(new javax.swing.ImageIcon(""));
        jMenuItemElementChartProperties.setText("Chart properties");
        jMenuItemElementChartProperties.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemElementChartPropertiesActionPerformed(evt);
            }
        });

        jPopupMenuElement.add(jMenuItemElementChartProperties);

        jMenuItemElementCrosstabProperties.setIcon(new javax.swing.ImageIcon(""));
        jMenuItemElementCrosstabProperties.setText("Crosstab properties");
        jMenuItemElementCrosstabProperties.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemElementCrosstabPropertiesActionPerformed(evt);
            }
        });

        jPopupMenuElement.add(jMenuItemElementCrosstabProperties);

        jMenuItemElementCrosstabDesignProperties.setIcon(new javax.swing.ImageIcon(""));
        jMenuItemElementCrosstabDesignProperties.setText("Go to crosstab design tab....");
        jMenuItemElementCrosstabDesignProperties.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemElementCrosstabPropertiesActionPerformed1(evt);
            }
        });

        jPopupMenuElement.add(jMenuItemElementCrosstabDesignProperties);

        jMenuItemElementOpenSubreport.setIcon(new javax.swing.ImageIcon(""));
        jMenuItemElementOpenSubreport.setText("Open subreport");
        jMenuItemElementOpenSubreport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemElementOpenSubreportActionPerformed1(evt);
            }
        });

        jPopupMenuElement.add(jMenuItemElementOpenSubreport);

        jMenuItemBandProperties1.setIcon(new javax.swing.ImageIcon(""));
        jMenuItemBandProperties1.setText("Band properties");
        jMenuItemBandProperties1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemBandPropertiesActionPerformed1(evt);
            }
        });

        jPopupMenuElement.add(jMenuItemBandProperties1);

        jPopupMenuElement.add(jSeparator1);

        jMenuItemCut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/cut.png")));
        jMenuItemCut.setText("Cut");
        jMenuItemCut.setEnabled(false);
        jMenuItemCut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCutActionPerformed(evt);
            }
        });

        jPopupMenuElement.add(jMenuItemCut);

        jMenuItemCopy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/copy.png")));
        jMenuItemCopy.setText("Copy");
        jMenuItemCopy.setEnabled(false);
        jMenuItemCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCopyActionPerformed(evt);
            }
        });

        jPopupMenuElement.add(jMenuItemCopy);

        jMenuItemPaste.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/paste.png")));
        jMenuItemPaste.setText("Paste");
        jMenuItemPaste.setEnabled(false);
        jMenuItemPaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPasteActionPerformed(evt);
            }
        });

        jPopupMenuElement.add(jMenuItemPaste);

        jMenuItemDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/delete.png")));
        jMenuItemDelete.setText("Delete");
        jMenuItemDelete.setEnabled(false);
        jMenuItemDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemDeleteActionPerformed(evt);
            }
        });

        jPopupMenuElement.add(jMenuItemDelete);

        jPopupMenuElement.add(jSeparator3);

        jMenuItemGroup.setText("Group selected element(s)");
        jMenuItemGroup.setEnabled(false);
        jMenuItemGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemGroupActionPerformed(evt);
            }
        });

        jPopupMenuElement.add(jMenuItemGroup);

        jMenuItemUngroup.setText("Ungroup selected element(s)");
        jMenuItemUngroup.setEnabled(false);
        jMenuItemUngroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemUngroupActionPerformed(evt);
            }
        });

        jPopupMenuElement.add(jMenuItemUngroup);

        jPopupMenuElement.add(jSeparator6);

        jMenuItemCopyStyle.setEnabled(false);
        jMenuItemCopyStyle.setLabel("Copy style");
        jMenuItemCopyStyle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCopyStyleActionPerformed(evt);
            }
        });

        jPopupMenuElement.add(jMenuItemCopyStyle);

        jMenuItemPasteStyle.setEnabled(false);
        jMenuItemPasteStyle.setLabel("Paste style");
        jMenuItemPasteStyle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPasteStyleActionPerformed(evt);
            }
        });

        jPopupMenuElement.add(jMenuItemPasteStyle);

        jMenuItemTransformStaticText.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3, 0));
        jMenuItemTransformStaticText.setLabel("Transform in Textfield");
        jMenuItemTransformStaticText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemTransformStaticTextActionPerformed(evt);
            }
        });

        jPopupMenuElement.add(jMenuItemTransformStaticText);

        jMenuItemPattern.setText("Field pattern");
        jMenuItemPattern.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPatternActionPerformed(evt);
            }
        });

        jPopupMenuElement.add(jMenuItemPattern);

        jPopupMenuElement.add(jSeparator4);

        try {
            setSelected(true);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        jPanelSuperContainer.setLayout(new java.awt.BorderLayout());

        jPanelReportContainer.setLayout(new java.awt.BorderLayout());

        jPanelVRule.setLayout(new java.awt.BorderLayout());

        jPanelVRule.setBackground(new java.awt.Color(255, 255, 255));
        jPanelVRule.setMaximumSize(new java.awt.Dimension(16, 32000));
        jPanelVRule.setMinimumSize(new java.awt.Dimension(16, 16));
        jPanelVRule.setPreferredSize(new java.awt.Dimension(16, 32767));
        jPanelReportContainer.add(jPanelVRule, java.awt.BorderLayout.WEST);

        jPanelHContainerRule.setLayout(new java.awt.BorderLayout());

        jPanelHContainerRule.setBackground(new java.awt.Color(255, 255, 255));
        jPanelHContainerRule.setMaximumSize(new java.awt.Dimension(32767, 16));
        jPanelHContainerRule.setMinimumSize(new java.awt.Dimension(16, 16));
        jPanelHContainerRule.setPreferredSize(new java.awt.Dimension(32767, 16));
        jPanelCorner.setLayout(null);

        jPanelCorner.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanelCorner.setMaximumSize(new java.awt.Dimension(16, 16));
        jPanelCorner.setMinimumSize(new java.awt.Dimension(16, 16));
        jPanelCorner.setPreferredSize(new java.awt.Dimension(16, 16));
        jPanelHContainerRule.add(jPanelCorner, java.awt.BorderLayout.WEST);

        jPanelHContainerRule.add(jHorizontalRule, java.awt.BorderLayout.CENTER);

        jPanelReportContainer.add(jPanelHContainerRule, java.awt.BorderLayout.NORTH);

        jPanelReport.setLayout(null);

        jPanelReport.setToolTipText("");
        jPanelReport.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                jPanelReportComponentResized(evt);
            }
        });
        jPanelReport.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jPanelReportMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jPanelReportMouseMoved(evt);
            }
        });
        jPanelReport.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jPanelReportKeyPressed(evt);
            }
        });
        jPanelReport.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanelReportMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jPanelReportMouseReleased(evt);
            }
        });
        jPanelReport.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                jPanelReportAncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        jPanelReport.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                jPanelReportMouseWheelMoved(evt);
            }
        });

        jPanelReportContainer.add(jPanelReport, java.awt.BorderLayout.CENTER);

        jPanelSuperContainer.add(jPanelReportContainer, java.awt.BorderLayout.CENTER);

        jVerticalScrollBar.setMaximum(0);
        jVerticalScrollBar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jVerticalScrollBarKeyPressed(evt);
            }
        });
        jVerticalScrollBar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jVerticalScrollBarMouseReleased(evt);
            }
        });
        jVerticalScrollBar.addAdjustmentListener(new java.awt.event.AdjustmentListener() {
            public void adjustmentValueChanged(java.awt.event.AdjustmentEvent evt) {
                jVerticalScrollBarAdjustmentValueChanged(evt);
            }
        });
        jVerticalScrollBar.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                jVerticalScrollBarMouseWheelMoved(evt);
            }
        });

        jPanelSuperContainer.add(jVerticalScrollBar, java.awt.BorderLayout.EAST);

        jPanelHScroll.setLayout(new java.awt.BorderLayout());

        jHorizontalScrollBar.setMaximum(0);
        jHorizontalScrollBar.setOrientation(javax.swing.JScrollBar.HORIZONTAL);
        jHorizontalScrollBar.addAdjustmentListener(new java.awt.event.AdjustmentListener() {
            public void adjustmentValueChanged(java.awt.event.AdjustmentEvent evt) {
                jHorizontalScrollBarAdjustmentValueChanged(evt);
            }
        });

        jPanelHScroll.add(jHorizontalScrollBar, java.awt.BorderLayout.CENTER);

        jPanel1.setMaximumSize(new java.awt.Dimension(17, 17));
        jPanel1.setMinimumSize(new java.awt.Dimension(17, 17));
        jPanel1.setPreferredSize(new java.awt.Dimension(17, 17));
        jPanelHScroll.add(jPanel1, java.awt.BorderLayout.EAST);

        jPanelSuperContainer.add(jPanelHScroll, java.awt.BorderLayout.SOUTH);

        jTabbedPane1.addTab("Main report", new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/mainreport.png")), jPanelSuperContainer);

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jPanelReportMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_jPanelReportMouseWheelMoved
        jVerticalScrollBarMouseWheelMoved(evt);
    }//GEN-LAST:event_jPanelReportMouseWheelMoved

    private void jVerticalScrollBarMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_jVerticalScrollBarMouseWheelMoved

       int notches = evt.getWheelRotation();

       jVerticalScrollBar.setValue( jVerticalScrollBar.getValue() + ( notches * 30 ) );

    }//GEN-LAST:event_jVerticalScrollBarMouseWheelMoved

    private void jMenuItemBandPropertiesActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemBandPropertiesActionPerformed1
        jMenuItemBandPropertiesActionPerformed(evt);
    }//GEN-LAST:event_jMenuItemBandPropertiesActionPerformed1

    private void jMenuItemElementOpenSubreportActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemElementOpenSubreportActionPerformed1

        if (getSelectedElements().size() > 0)
        {
            ReportElement re = (ReportElement)getSelectedElements().elementAt(0);

            //System.out.println(re);

            if (re instanceof SubReportElement)
            {
                SubReportElement sre = (SubReportElement)re;

                String subreportExp = sre.getSubreportExpression();
                String subreportExpClass = sre.getSubreportExpressionClass();

                if (subreportExpClass.equals("java.lang.String"))
                {
                    try {
                        Interpreter interpreter = prepareExpressionEvaluator();

                        subreportExp = ""+ReportQueryDialog.recursiveInterpreter( interpreter, subreportExp, getReport().getParameters());


                        if (subreportExp != null)
                            {
                                String fileName = subreportExp+"";

                                //fileName = Misc.string_replace("/","//",fileName);

                                if (fileName.toLowerCase().endsWith(".jasper"))
                                {
                                   java.util.List possibleSourceFiles = new java.util.ArrayList();

                                    possibleSourceFiles.add(fileName.substring(0, fileName.length() - 7) + ".jrxml");
                                    possibleSourceFiles.add(fileName.substring(0, fileName.length() - 7) + ".xml");

                                    // Look in the same directory as this file...
                                    File f = new File(fileName);
                                    String fname = f.getName();
                                    if (fname.length() > 7 && getReport().getFilename() != null && getReport().getFilename().length() > 0)
                                    {
                                        f = new File( getReport().getFilename());
                                        possibleSourceFiles.add( f.getParent() + File.separator + fname.substring(0, fname.length() - 7) + ".jrxml");
                                        possibleSourceFiles.add( f.getParent() + File.separator + fname.substring(0, fname.length() - 7) + ".xml");
                                    }

                                    f = getFirstValidFile(possibleSourceFiles);

                                    if (f == null)
                                    {
                                        String message = "No one of the following possible source files for this subreport were found:\n\n";
                                        for (int i=0; i<possibleSourceFiles.size(); ++i)
                                        {
                                            message += possibleSourceFiles.get(i)+"\n";
                                        }

                                        throw new  Exception(message);
                                    }

                                    JReportFrame rf = MainFrame.getMainInstance().openFile(f);
                                    rf.setSelected(true);
                                }

                            }

                    } catch (Exception ex)
                    {
                        JOptionPane.showMessageDialog(this,
                                I18n.getFormattedString( "messages.jReportFrame.errorOpeningSubreport","I have got a problem finding the subreport:\n{0}", new Object[]{ ex.getMessage()}) );
                    }
                }
            }

        }
    }//GEN-LAST:event_jMenuItemElementOpenSubreportActionPerformed1

    private File getFirstValidFile(java.util.List list)
    {
        for (int i=0; i<list.size(); ++i)
        {
            String fileName = list.get(i)+"";
            File f = new File(fileName);

            if (!f.exists())
            {
                ReportClassLoader rcl =  MainFrame.getMainInstance().getReportClassLoader();
                if (this.getReport().getFilename() != null)
                {
                    File rfn = new File( this.getReport().getFilename() );
                    rcl.addNoRelodablePath( rfn.getParent() );
                }
                try {
                    URL url = rcl.getResource(fileName);

                    if (url != null)
                    {
                        f = new File(url.getPath());
                        if (!f.exists())
                        {
                            continue;
                        }
                    }
                    else
                    {
                        continue;
                    }
                } catch (Exception ex)
                {
                    continue;
                }
           }


           return f;
        }

        return null;
    }

    private Interpreter prepareExpressionEvaluator() throws bsh.EvalError {
        Interpreter interpreter = new Interpreter();
        interpreter.setClassLoader(interpreter.getClass().getClassLoader());
        return interpreter;

        // return null;
    }

    private void jMenuItemElementCrosstabPropertiesActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemElementCrosstabPropertiesActionPerformed1


         if (getSelectedElements().size() > 0)
        {
            ReportElement re = (ReportElement)getSelectedElements().elementAt(0);

            if (re instanceof CrosstabReportElement )
            {
                setSelectedCrosstabEditor((CrosstabReportElement)re);
            }
        }

    }//GEN-LAST:event_jMenuItemElementCrosstabPropertiesActionPerformed1

    /**
     * Open the crosstab editor for the specified element...
     */
    public boolean setSelectedCrosstabEditor(CrosstabReportElement re)
    {
        try {
                if (re == null)
                {
                    jTabbedPane1.setSelectedIndex(0);
                }
                else
                {
                    jTabbedPane1.setSelectedComponent( getCrosstabEditor(re) );
                }
        } catch (Exception ex){
            return false;
        }
        
        return true;
    }
    
    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged



        //We have to check if the current panel is the first one or not...
        if (jTabbedPane1 == null || !jTabbedPane1.isVisible() || jTabbedPane1.getSelectedIndex() == 0)
        {
            MainFrame.getMainInstance().setCrosstabActive(null);
            MainFrame.getMainInstance().setComboBoxZoomFactor( this.zoomFactor * 100 );
        }
        else
        {
            MainFrame.getMainInstance().setCrosstabActive(getSelectedCrosstabEditorPanel().getCrosstabElement());
            MainFrame.getMainInstance().setComboBoxZoomFactor( this.getSelectedCrosstabEditorPanel().getZoomFactor() * 100 );
        }

        MainFrame.getMainInstance().getElementPropertiesDialog().updateSelection();
        try {
            MainFrame.getMainInstance().updateCutAndPasteMenu(this);
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }


    }//GEN-LAST:event_jTabbedPane1StateChanged

    private void jMenuItemElementCrosstabPropertiesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemElementCrosstabPropertiesActionPerformed
        if (getSelectedElements().size() > 0)
        {
            ReportElement re = (ReportElement)getSelectedElements().elementAt(0);

            if (re instanceof CrosstabReportElement )
            {
                    it.businesslogic.ireport.crosstab.gui.CrosstabPropertiesDialog cpd = new it.businesslogic.ireport.crosstab.gui.CrosstabPropertiesDialog(getMainFrame(),true);
                    cpd.setCurrentCrosstabReportElement( (CrosstabReportElement)re);
                    cpd.setVisible(true);
            }
        }
    }//GEN-LAST:event_jMenuItemElementCrosstabPropertiesActionPerformed

    private void jMenuItemUngroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemUngroupActionPerformed
        ungroupSelectedElements();
    }//GEN-LAST:event_jMenuItemUngroupActionPerformed

    private void jMenuItemGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemGroupActionPerformed
       groupSelectedElements();
    }//GEN-LAST:event_jMenuItemGroupActionPerformed

    public boolean groupSelectedElements()
    {
        if (getSelectedElements().size() == 0) return false;
        boolean sameBand = true;
        Band theBand = null;
        Enumeration selectedElementEnum = getSelectedElements().elements();

        while (selectedElementEnum.hasMoreElements())
        {
                ReportElement re = (ReportElement)selectedElementEnum.nextElement();
                if (theBand == null) theBand = re.getBand();
                else if (sameBand && theBand != re.getBand()) sameBand = false;

                if (!sameBand) return false;
        }

        selectedElementEnum = getSelectedElements().elements();
        String newGroup = getReport().createChildGroup("");

        while (selectedElementEnum.hasMoreElements())
        {
                ReportElement re = (ReportElement)selectedElementEnum.nextElement();
                String oldGroup = re.getElementGroup();
                if (oldGroup.length() > 0) oldGroup = "." + oldGroup;
                re.setElementGroup(newGroup + oldGroup);
        }


        GroupEmentsOperation undoOp = new GroupEmentsOperation(this);
        undoOp.setAddedGroup(newGroup);

        PositionedElement[] newPositionedElements = new PositionedElement[this.getSelectedElements().size()];

        for (int i=0; i<this.getSelectedElements().size(); ++i)
        {
                ReportElement element = (ReportElement)getSelectedElements().elementAt(i);
                int oldPosition = getReport().getElements().indexOf(element);
                newPositionedElements[i] = new PositionedElement(element,oldPosition,oldPosition);
        }

        // Reorder
        for (int i=0; i<this.getSelectedElements().size(); ++i)
        {
                ReportElement element = (ReportElement)getSelectedElements().elementAt(i);
                int newPosition = getReport().getElements().indexOf(element);
                undoOp.addElement(newPositionedElements[i].getElement(),newPositionedElements[i].getOldPosition() , newPosition);
        }

        addUndoOperation(undoOp);
        getReportPanel().repaint();
        fireReportListenerReportElementsChanged(new ReportElementChangedEvent(this, new Vector() , ReportElementChangedEvent.REMOVED ));
        return true;

      }


    public boolean ungroupSelectedElements()
    {
        if (getSelectedElements().size() == 0) return false;
        boolean sameBand = true;
        Band theBand = null;
        Enumeration selectedElementEnum = getSelectedElements().elements();

        while (selectedElementEnum.hasMoreElements())
        {
                ReportElement re = (ReportElement)selectedElementEnum.nextElement();
                if (theBand == null) theBand = re.getBand();
                else if (sameBand && theBand != re.getBand()) sameBand = false;

                if (!sameBand) return false;
        }

        selectedElementEnum = getSelectedElements().elements();

        UnGroupEmentsOperation undoOp = new UnGroupEmentsOperation(this);

        GroupPositionedElement[] newPositionedElements = new GroupPositionedElement[this.getSelectedElements().size()];

        for (int i=0; i<this.getSelectedElements().size(); ++i)
        {
                ReportElement element = (ReportElement)getSelectedElements().elementAt(i);
                String oldGroup = element.getElementGroup();
                String newGroup = oldGroup;
                if (newGroup.lastIndexOf(".")>=0)
                {
                    newGroup = newGroup.substring(0,newGroup.lastIndexOf("."));
                }
                else
                {
                    newGroup = "";
                }
                element.setElementGroup(newGroup);
                int oldPosition = getReport().getElements().indexOf(element);
                newPositionedElements[i] = new GroupPositionedElement(element,oldPosition,oldPosition, oldGroup,newGroup);
        }

        // Reorder
        for (int i=0; i<this.getSelectedElements().size(); ++i)
        {
                ReportElement element = (ReportElement)getSelectedElements().elementAt(i);
                int newPosition = getReport().getElements().indexOf(element);
                undoOp.addElement(newPositionedElements[i].getElement(),newPositionedElements[i].getOldPosition() , newPosition, newPositionedElements[i].getOldElementGroup(), newPositionedElements[i].getNewElementGroup());
        }

        addUndoOperation(undoOp);
        getReportPanel().repaint();
        fireReportListenerReportElementsChanged(new ReportElementChangedEvent(this, new Vector() , ReportElementChangedEvent.REMOVED ));
        return true;

      }

    private void jMenuItemEditExpressionActionPerformed(java.awt.event.ActionEvent evt) {                                                                

        if (getSelectedElements().size() > 0)
        {
            ReportElement re = (ReportElement)getSelectedElements().elementAt(0);

            if (re instanceof TextFieldReportElement )
            {
                    TextFieldReportElement tfre = (TextFieldReportElement)re;
                    ExpressionEditor ed = new ExpressionEditor();
                    ed.setSubDataset(this.getReport());
                    ed.setExpression( tfre.getText() );
                    ed.updateTreeEntries();
                    ed.setVisible(true);
                    if (ed.getDialogResult() == javax.swing.JOptionPane.OK_OPTION)
                    {
                        tfre.setText( ed.getExpression());
                        getReportPanel().repaint();
                        fireReportListenerReportElementsChanged(new ReportElementChangedEvent(this, tfre , ReportElementChangedEvent.CHANGED ));
                    }
            }
        }
    }
    
    private void jMenuItemElementChartPropertiesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemElementChartPropertiesActionPerformed

        if (getSelectedElements().size() > 0)
        {
            ReportElement re = (ReportElement)getSelectedElements().elementAt(0);

            if (re instanceof ChartReportElement2 )
            {
                    it.businesslogic.ireport.chart.gui.ChartPropertiesDialog cpd = new it.businesslogic.ireport.chart.gui.ChartPropertiesDialog(getMainFrame(),true);
                    cpd.setChartElement( (ChartReportElement2)re);
                    cpd.setVisible(true);
            }
        }


    }//GEN-LAST:event_jMenuItemElementChartPropertiesActionPerformed

    private void jMenuItemPatternActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPatternActionPerformed

        String pattern = "";
        FieldPatternDialog pd = new FieldPatternDialog( MainFrame.getMainInstance() ,true);
        pd.setVisible(true);
        if (pd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION ) {


            Enumeration selectedElementEnum = getSelectedElements().elements();

            while (selectedElementEnum.hasMoreElements()) {
                ReportElement re = (ReportElement)selectedElementEnum.nextElement();
                if (re instanceof TextFieldReportElement) {
                    ((TextFieldReportElement)re).setPattern( pd.getPattern() );
                }
            }
        }


    }//GEN-LAST:event_jMenuItemPatternActionPerformed

    private void jMenuItemPasteStyleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPasteStyleActionPerformed

        pasteStyle();

    }//GEN-LAST:event_jMenuItemPasteStyleActionPerformed

    private void jMenuItemCopyStyleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCopyStyleActionPerformed

        copyStyle();

    }//GEN-LAST:event_jMenuItemCopyStyleActionPerformed

    private void jMenuItemTransformStaticTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemTransformStaticTextActionPerformed
        transformStaticInTextFields();
    }//GEN-LAST:event_jMenuItemTransformStaticTextActionPerformed

    private void jMenuItemDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemDeleteActionPerformed
        // Remove selected elements...
        this.deleteSelectedElements();
    }//GEN-LAST:event_jMenuItemDeleteActionPerformed

    public void delete()
    {
        if (getSelectedCrosstabEditorPanel() != null)
        {
            getSelectedCrosstabEditorPanel().deleteSelectedElements();
        }
        else
        {
            this.deleteSelectedElements();
        }
    }

    private void jMenuItemPasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPasteActionPerformed
        paste();
    }//GEN-LAST:event_jMenuItemPasteActionPerformed

    private void jMenuItemCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCopyActionPerformed
        copy();
    }//GEN-LAST:event_jMenuItemCopyActionPerformed

    private void jMenuItemCutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCutActionPerformed
        cut();
    }//GEN-LAST:event_jMenuItemCutActionPerformed

    private void jMenuItemBandPropertiesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemBandPropertiesActionPerformed

        if ( popup_opened_at != null ) {

            // We must find the right band...
            int y = getLogicalDim( popup_opened_at.y - 10 + jVerticalScrollBar.getValue() ) + 10;
            Band myBand = getReport().getBandByPosition( y );

            if ( myBand != null ) {
                getMainFrame().jMenuItemBandsActionPerformed( evt );
                getMainFrame().getBandsDialog().setSelectedBand( myBand.getName() );
            }

        }
    }//GEN-LAST:event_jMenuItemBandPropertiesActionPerformed

    private void addFormatItemsToMenu(javax.swing.JComponent m){

        jMenuAlign = new javax.swing.JMenu();
        jMenuAlign.setText(it.businesslogic.ireport.util.I18n.getString("align", "Align..."));
        jMenuItemAlignLeft = new javax.swing.JMenuItem();
        jMenuItemAlignLeft.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/elem_align_left.png")));
        jMenuItemAlignLeft.setText(it.businesslogic.ireport.util.I18n.getString("alignLeft", "Align left"));
        jMenuItemAlignLeft.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemAlignLeftActionPerformed(evt);
            }
        });

        jMenuAlign.add(jMenuItemAlignLeft);

        jMenuItemAlignRight = new javax.swing.JMenuItem();
        jMenuItemAlignRight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/elem_align_right.png")));
        jMenuItemAlignRight.setText(it.businesslogic.ireport.util.I18n.getString("alignRight", "Align right"));
        jMenuItemAlignRight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemAlignRightActionPerformed(evt);
            }
        });

        jMenuAlign.add(jMenuItemAlignRight);

        jMenuItemAlignTop = new javax.swing.JMenuItem();
        jMenuItemAlignTop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/elem_align_top.png")));
        jMenuItemAlignTop.setText(it.businesslogic.ireport.util.I18n.getString("alignTop", "Align top"));
        jMenuItemAlignTop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemAlignTopActionPerformed(evt);
            }
        });

        jMenuAlign.add(jMenuItemAlignTop);

        jMenuItemAlignBottom = new javax.swing.JMenuItem();
        jMenuItemAlignBottom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/elem_align_bottom.png")));
        jMenuItemAlignBottom.setText(it.businesslogic.ireport.util.I18n.getString("alignBottom", "Align bottom"));
        jMenuItemAlignBottom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemAlignBottomActionPerformed(evt);
            }
        });

        jMenuAlign.add(jMenuItemAlignBottom);

        jSeparator19 = new javax.swing.JSeparator();
        jMenuAlign.add(jSeparator19);

        jMenuItemAlignVerticalAxis = new javax.swing.JMenuItem();
        jMenuItemAlignVerticalAxis.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/elem_center_axis.png")));
        jMenuItemAlignVerticalAxis.setText(it.businesslogic.ireport.util.I18n.getString("alignVerticalAxis", "Align vertical axis"));
        jMenuItemAlignVerticalAxis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemAlignVerticalAxisActionPerformed(evt);
            }
        });

        jMenuAlign.add(jMenuItemAlignVerticalAxis);

        jMenuItemAlignHorizontalAxis = new javax.swing.JMenuItem();
        jMenuItemAlignHorizontalAxis.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/elem_vcenter_axis.png")));
        jMenuItemAlignHorizontalAxis.setText(it.businesslogic.ireport.util.I18n.getString("alignHorizontalAxis", "Align horizontal axis"));
        jMenuItemAlignHorizontalAxis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemAlignHorizontalAxisActionPerformed(evt);
            }
        });

        jMenuAlign.add(jMenuItemAlignHorizontalAxis);

        jSeparator20 = new javax.swing.JSeparator();
        jMenuAlign.add(jSeparator20);

        jMenuItemAlignToBandTop = new javax.swing.JMenuItem();
        jMenuItemAlignToBandTop.setText(it.businesslogic.ireport.util.I18n.getString("alignToBandTop", "Align to band top"));
        jMenuItemAlignToBandTop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemAlignToBandTopActionPerformed(evt);
            }
        });

        jMenuAlign.add(jMenuItemAlignToBandTop);

        jMenuItemAlignToBandBottom = new javax.swing.JMenuItem();
        jMenuItemAlignToBandBottom.setText(it.businesslogic.ireport.util.I18n.getString("alignToBandBottom", "Align to band bottom"));
        jMenuItemAlignToBandBottom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemAlignToBandBottomActionPerformed(evt);
            }
        });

        jMenuAlign.add(jMenuItemAlignToBandBottom);

        m.add(jMenuAlign);

        jMenuSize = new javax.swing.JMenu();
        jMenuSize.setText(it.businesslogic.ireport.util.I18n.getString("size", "Size..."));
        jMenuItemSameWidth = new javax.swing.JMenuItem();
        jMenuItemSameWidth.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/elem_same_hsize.png")));
        jMenuItemSameWidth.setText(it.businesslogic.ireport.util.I18n.getString("sameWidth", "Same width"));
        jMenuItemSameWidth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemSameWidthActionPerformed(evt);
            }
        });

        jMenuSize.add(jMenuItemSameWidth);

        jMenuItemSameWidthMax = new javax.swing.JMenuItem();
        jMenuItemSameWidthMax.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/elem_same_hsize_plus.png")));
        jMenuItemSameWidthMax.setText(it.businesslogic.ireport.util.I18n.getString("sameWidthMax", "Same width (max)"));
        jMenuItemSameWidthMax.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemSameWidthMaxActionPerformed(evt);
            }
        });

        jMenuSize.add(jMenuItemSameWidthMax);

        jMenuItemSameWidthMin = new javax.swing.JMenuItem();
        jMenuItemSameWidthMin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/elem_same_hsize_min.png")));
        jMenuItemSameWidthMin.setText(it.businesslogic.ireport.util.I18n.getString("sameWidthMin", "Same width (min)"));
        jMenuItemSameWidthMin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemSameWidthMinActionPerformed(evt);
            }
        });

        jMenuSize.add(jMenuItemSameWidthMin);

        jSeparator17 = new javax.swing.JSeparator();
        jMenuSize.add(jSeparator17);

        jMenuItemSameHeight = new javax.swing.JMenuItem();
        jMenuItemSameHeight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/elem_same_vsize.png")));
        jMenuItemSameHeight.setText(it.businesslogic.ireport.util.I18n.getString("sameHeight", "Same height"));
        jMenuItemSameHeight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemSameHeightActionPerformed(evt);
            }
        });

        jMenuSize.add(jMenuItemSameHeight);

        jMenuItemSameHeightMin = new javax.swing.JMenuItem();
        jMenuItemSameHeightMin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/elem_same_vsize_min.png")));
        jMenuItemSameHeightMin.setText(it.businesslogic.ireport.util.I18n.getString("sameHeightMin", "Same height (min)"));
        jMenuItemSameHeightMin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemSameHeightMinActionPerformed(evt);
            }
        });

        jMenuSize.add(jMenuItemSameHeightMin);

        jMenuItemSameHeightMax = new javax.swing.JMenuItem();
        jMenuItemSameHeightMax.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/elem_same_vsize_plus.png")));
        jMenuItemSameHeightMax.setText(it.businesslogic.ireport.util.I18n.getString("sameHeightMax", "Same height (max)"));
        jMenuItemSameHeightMax.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemSameHeightMaxActionPerformed(evt);
            }
        });

        jMenuSize.add(jMenuItemSameHeightMax);

        jSeparator18 = new javax.swing.JSeparator();
        jMenuSize.add(jSeparator18);

        jMenuItemSameSize = new javax.swing.JMenuItem();
        jMenuItemSameSize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/elem_same_size.png")));
        jMenuItemSameSize.setText(it.businesslogic.ireport.util.I18n.getString("sameSize", "Same size"));
        jMenuItemSameSize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemSameSizeActionPerformed(evt);
            }
        });

        jMenuSize.add(jMenuItemSameSize);

        m.add(jMenuSize);

        jMenuPosition = new javax.swing.JMenu();
        jMenuPosition.setText(it.businesslogic.ireport.util.I18n.getString("position", "Position..."));
        jMenuItemCenterH = new javax.swing.JMenuItem();
        jMenuItemCenterH.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/elem_hcenter.png")));
        jMenuItemCenterH.setText(it.businesslogic.ireport.util.I18n.getString("centerHorizontallyBandBased", "Center horizontally (band based)"));
        jMenuItemCenterH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemCenterHActionPerformed(evt);
            }
        });

        jMenuPosition.add(jMenuItemCenterH);

        jMenuItemCenterV = new javax.swing.JMenuItem();
        jMenuItemCenterV.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/elem_vcenter.png")));
        jMenuItemCenterV.setText(it.businesslogic.ireport.util.I18n.getString("centerVerticallyBandBased", "Center vertically (band based)"));
        jMenuItemCenterV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemCenterVActionPerformed(evt);
            }
        });

        jMenuPosition.add(jMenuItemCenterV);

        jMenuItemCenterInBand = new javax.swing.JMenuItem();
        jMenuItemCenterInBand.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/elem_ccenter.png")));
        jMenuItemCenterInBand.setText(it.businesslogic.ireport.util.I18n.getString("centerInBand", "Center in band"));
        jMenuItemCenterInBand.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemCenterInBandActionPerformed(evt);
            }
        });

        jMenuPosition.add(jMenuItemCenterInBand);

        jMenuItemCenterBackground = new javax.swing.JMenuItem();
        jMenuItemCenterBackground.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/elem_bcenter.png")));
        jMenuItemCenterBackground.setText(it.businesslogic.ireport.util.I18n.getString("centerInBackground", "Center in background"));
        jMenuItemCenterBackground.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemCenterBackgroundActionPerformed(evt);
            }
        });

        jMenuPosition.add(jMenuItemCenterBackground);

        jMenuItemJoinLeft = new javax.swing.JMenuItem();
        jMenuItemJoinLeft.setText(it.businesslogic.ireport.util.I18n.getString("joinSidesLeft", "Join sides left"));
        jMenuItemJoinLeft.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemJoinLeftActionPerformed(evt);
            }
        });

        jMenuPosition.add(jMenuItemJoinLeft);

        jMenuItemJoinRight = new javax.swing.JMenuItem();
        jMenuItemJoinRight.setText(it.businesslogic.ireport.util.I18n.getString("joinSidesRight", "Join sides right"));
        jMenuItemJoinRight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemJoinRightActionPerformed(evt);
            }
        });

        jMenuPosition.add(jMenuItemJoinRight);

        jMenuItemLeftMargin = new javax.swing.JMenuItem();
        jMenuItemLeftMargin.setText("Join left page margin");
        jMenuItemLeftMargin.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_HOME, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemLeftMargin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemLeftMarginActionPerformed(evt);
            }
        });

        jMenuPosition.add(jMenuItemLeftMargin);

        jMenuItemRightMargin = new javax.swing.JMenuItem();
        jMenuItemRightMargin.setText("Join right page margin");
        jMenuItemRightMargin.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_END, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemRightMargin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemRightMarginActionPerformed(evt);
            }
        });

        jMenuPosition.add(jMenuItemRightMargin);

        m.add(jMenuPosition);

        jSeparator5 = new javax.swing.JSeparator();
        m.add(jSeparator5);

        jMenuHSpacing = new javax.swing.JMenu();
        jMenuHSpacing.setText(it.businesslogic.ireport.util.I18n.getString("horizontalSpacing", "Horizontal spacing..."));

        jMenuItemHSMakeEqual = new javax.swing.JMenuItem();
        jMenuItemHSMakeEqual.setText(it.businesslogic.ireport.util.I18n.getString("makeEqual", "Make equal"));
        jMenuItemHSMakeEqual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemHSMakeEqualActionPerformed(evt);
            }
        });

        jMenuHSpacing.add(jMenuItemHSMakeEqual);

        jMenuItemHSIncrease = new javax.swing.JMenuItem();
        jMenuItemHSIncrease.setText(it.businesslogic.ireport.util.I18n.getString("increase", "Increase"));
        jMenuItemHSIncrease.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemHSIncreaseActionPerformed(evt);
            }
        });

        jMenuHSpacing.add(jMenuItemHSIncrease);

        jMenuItemHSDecrease = new javax.swing.JMenuItem();
        jMenuItemHSDecrease.setText(it.businesslogic.ireport.util.I18n.getString("decrease", "Decrease"));
        jMenuItemHSDecrease.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemHSDecreaseActionPerformed(evt);
            }
        });

        jMenuHSpacing.add(jMenuItemHSDecrease);

        jMenuItemHSRemove = new javax.swing.JMenuItem();
        jMenuItemHSRemove.setText(it.businesslogic.ireport.util.I18n.getString("remove", "Remove"));
        jMenuItemHSRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemHSRemoveActionPerformed(evt);
            }
        });

        jMenuHSpacing.add(jMenuItemHSRemove);

        m.add(jMenuHSpacing);

        jMenuVSpacing = new javax.swing.JMenu();
        jMenuVSpacing.setText(it.businesslogic.ireport.util.I18n.getString("verticalSpacing", "Vertical spacing"));
        jMenuItemVSMakeEqual = new javax.swing.JMenuItem();
        jMenuItemVSMakeEqual.setText(it.businesslogic.ireport.util.I18n.getString("makeEqual", "Make equal"));
        jMenuItemVSMakeEqual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemVSMakeEqualActionPerformed(evt);
            }
        });

        jMenuVSpacing.add(jMenuItemVSMakeEqual);

        jMenuItemVSIncrease = new javax.swing.JMenuItem();
        jMenuItemVSIncrease.setText(it.businesslogic.ireport.util.I18n.getString("increase", "Increase"));
        jMenuItemVSIncrease.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemVSIncreaseActionPerformed(evt);
            }
        });

        jMenuVSpacing.add(jMenuItemVSIncrease);

        jMenuItemVSDecrease = new javax.swing.JMenuItem();
        jMenuItemVSDecrease.setText(it.businesslogic.ireport.util.I18n.getString("decrease", "Decrease"));
        jMenuItemVSDecrease.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemVSDecreaseActionPerformed(evt);
            }
        });

        jMenuVSpacing.add(jMenuItemVSDecrease);

        jMenuItemVSRemove = new javax.swing.JMenuItem();
        jMenuItemVSRemove.setText(it.businesslogic.ireport.util.I18n.getString("remove", "Remove"));
        jMenuItemVSRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemVSRemoveActionPerformed(evt);
            }
        });

        jMenuVSpacing.add(jMenuItemVSRemove);

        m.add(jMenuVSpacing);

        jSeparator8 = new javax.swing.JSeparator();
        m.add(jSeparator8);

        jMenuItemOrganize = new javax.swing.JMenuItem();
        //Modified by Felix Firgau for I18n on Feb 9th 2006
        jMenuItemOrganize.setText(it.businesslogic.ireport.util.I18n.getString("organizeAsTable","Organize as table"));
        jMenuItemOrganize.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemOrganize.setIcon(new javax.swing.ImageIcon(""));
        jMenuItemOrganize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemOrganizeActionPerformed(evt);
            }
        });

        m.add(jMenuItemOrganize);

        jMenuItemBringToFront = new javax.swing.JMenuItem();
        jMenuItemBringToFront.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/sendtofront.png")));
        jMenuItemBringToFront.setText(it.businesslogic.ireport.util.I18n.getString("bringToFront", "Bring to front"));
        jMenuItemBringToFront.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemBringToFrontActionPerformed(evt);
            }
        });

        m.add(jMenuItemBringToFront);

        jMenuItemSendToBack = new javax.swing.JMenuItem();
        jMenuItemSendToBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/sendtoback.png")));
        jMenuItemSendToBack.setText(it.businesslogic.ireport.util.I18n.getString("sendToBack", "Send to back"));
        jMenuItemSendToBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getMainFrame().jMenuItemSendToBackActionPerformed(evt);
            }
        });

        m.add(jMenuItemSendToBack);

    }
    private void jMenuItemElementPropertiesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemElementPropertiesActionPerformed
        openElementPropertiesDialog();
    }//GEN-LAST:event_jMenuItemElementPropertiesActionPerformed

    private void jMenuItemPasteOnBandActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPasteOnBandActionPerformed

        if (this.popup_opened_at != null) {
            pasteHere( popup_opened_at );
        }
    }//GEN-LAST:event_jMenuItemPasteOnBandActionPerformed

    private void jMenuItemPasteOnBandMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItemPasteOnBandMouseClicked

    }//GEN-LAST:event_jMenuItemPasteOnBandMouseClicked

    private void jMenuItemElementPropertiesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItemElementPropertiesMouseClicked

    }//GEN-LAST:event_jMenuItemElementPropertiesMouseClicked

    private void jVerticalScrollBarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jVerticalScrollBarKeyPressed

    }//GEN-LAST:event_jVerticalScrollBarKeyPressed

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed


    }//GEN-LAST:event_formKeyPressed

    private void jPanelReportKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPanelReportKeyPressed

        /*
        mf.logOnConsole(evt.getKeyCode() + " "+
        (((evt.getModifiers() & evt.CTRL_MASK) != 0 ) ? "CTL " : "") +
        (((evt.getModifiers() & evt.ALT_MASK) != 0 ) ? "ALT " : "") +
        (((evt.getModifiers() & evt.SHIFT_MASK) != 0 ) ? "SHIFT " : "") + "\n");
         */
        /*
        if (( evt.getModifiers() & evt.CTRL_MASK) != 0 &&
            evt.getKeyCode() == java.awt.event.KeyEvent.VK_Z )
        {
          .undo();
          return;
        }

        if (( evt.getModifiers() & evt.CTRL_MASK ) != 0 &&
            evt.getKeyCode() == java.awt.event.KeyEvent.VK_Y)
        {
          mf.redo();
          return;
        }
         */

        // Standard speed:
        int x=1, y=1;

        // Increase the speed of movement if the Shift key is pressed
        if (( evt.getModifiers() & evt.SHIFT_MASK ) != 0) {
            x = 10;
            y = 10;
        }


        //if ((evt.getModifiers() & evt.ALT_MASK) != 0)
        //    System.out.println("Tasto:"+evt.getKeyCode());
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_DELETE) {
            this.deleteSelectedElements();
            return;
        }
        /* Begin code Robert Lamping, 12 July 2004 */
        /* Short cut keys for quick alignment */
        else  if ( (( evt.getModifiers() & evt.SHIFT_MASK) != 0) &&
                (( evt.getModifiers() & evt.CTRL_MASK) != 0) ) {

            /* all key presses when SHIFT/CTRL are both pressed */

            /* Short cuts for alignments */
            /* most of them deactivated, since they are activated bia the menu options and their
             * short cuts
             */
            switch ( evt.getKeyCode()) {
                case java.awt.event.KeyEvent.VK_S:
                    /* shrink A3-A4 */
                    FormatCommand.getCommand(OperationType.SHRINK).execute();
                    break;

//                case java.awt.event.KeyEvent.VK_UP:
//                    /* align top */
//                    FormatCommand.getCommand(OperationType.ALIGN_TOP).execute();
//                    break;
//                case java.awt.event.KeyEvent.VK_DOWN:
//                    /* align bottom */
//                    FormatCommand.getCommand(OperationType.ALIGN_BOTTOM).execute();
//                    break;
//                case java.awt.event.KeyEvent.VK_LEFT:
//                    /* align left */
//                    FormatCommand.getCommand(OperationType.ALIGN_LEFT).execute();
//                    break;
//                case java.awt.event.KeyEvent.VK_RIGHT:
//                    /* align right */
//                    FormatCommand.getCommand(OperationType.ALIGN_RIGHT).execute();
//                    break;
//                case java.awt.event.KeyEvent.VK_PAGE_UP:
//                    /* align against band top */
//                    FormatCommand.getCommand(OperationType.ALIGN_TOP_TO_BAND).execute();
//                    break;
//                case java.awt.event.KeyEvent.VK_PAGE_DOWN:
//                    /* align against band bottom */
//                    FormatCommand.getCommand(OperationType.ALIGN_BOTTOM_TO_BAND).execute();
//                    break;
//                case java.awt.event.KeyEvent.VK_HOME:
//                    /* align against left margin */
//                    FormatCommand.getCommand(OperationType.ALIGN_TO_LEFT_MARGIN).execute();
//                    break;
//                case java.awt.event.KeyEvent.VK_END:
//                    /* align against right margin */
//                    FormatCommand.getCommand(OperationType.ALIGN_TO_RIGHT_MARGIN).execute();
//                    break;
//                case java.awt.event.KeyEvent.VK_L:
//                    /* join left */
//                    FormatCommand.getCommand(OperationType.JOIN_LEFT).execute();
//                    break;
//                case java.awt.event.KeyEvent.VK_R:
//                    /* join right */
//                    FormatCommand.getCommand(OperationType.JOIN_RIGHT).execute();
//                    break;
//                case java.awt.event.KeyEvent.VK_O:
//                    /* organize as a table, with spacing inbetwween */
//                    FormatCommand.getCommand(OperationType.ORGANIZE_AS_A_TABLE).execute();
//
//                    break;
            }

        }
        /* End code Robert Lamping, 12 July 2004 */

        else if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_UP) {
            y *= -1;
            x = 0;

            // Remove selected elements...
            // Up of x...
            Vector changed_elements = new Vector();
            for (Iterator i = selectedElements.iterator(); i.hasNext(); ) {
                ReportElement element = (ReportElement) i.next();
                element.trasform(new Point(x,y), TransformationType.TRANSFORMATION_MOVE);
                element.adjustBand();
                changed_elements.add(element);
            }
            fireReportListenerReportElementsChanged(new ReportElementChangedEvent(this, changed_elements , ReportElementChangedEvent.CHANGED));

            this.getMainFrame().getElementPropertiesDialog().updateSelection();
            jPanelReport.repaint();
        } else if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_LEFT) {
            x *= -1;
            y = 0;

            // Up of x...
            Vector changed_elements = new Vector();
            for (Iterator i = selectedElements.iterator(); i.hasNext(); ) {
                ReportElement element = (ReportElement) i.next();
                element.trasform(new Point(x,y), TransformationType.TRANSFORMATION_MOVE);
                element.adjustBand();
                changed_elements.add(element);
            }
            fireReportListenerReportElementsChanged(new ReportElementChangedEvent(this, changed_elements , ReportElementChangedEvent.CHANGED));
            this.getMainFrame().getElementPropertiesDialog().updateSelection();
            jPanelReport.repaint();
        } else if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_RIGHT) {
            y=0;
            // Remove selected elements...
            // Up of x...
            Vector changed_elements = new Vector();
            for (Iterator i = selectedElements.iterator(); i.hasNext(); ) {
                ReportElement element = (ReportElement) i.next();
                element.trasform(new Point(x,y), TransformationType.TRANSFORMATION_MOVE);
                element.adjustBand();
                changed_elements.add(element);
            }
            fireReportListenerReportElementsChanged(new ReportElementChangedEvent(this, changed_elements , ReportElementChangedEvent.CHANGED));
            this.getMainFrame().getElementPropertiesDialog().updateSelection();

            jPanelReport.repaint();
        } else if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_DOWN) {
            x = 0;
            // Remove selected elements...
            // Up of x...
            Vector changed_elements = new Vector();
            for (Iterator i = selectedElements.iterator(); i.hasNext(); ) {
                ReportElement element = (ReportElement) i.next();
                element.trasform(new Point(x,y), TransformationType.TRANSFORMATION_MOVE);
                element.adjustBand();
                changed_elements.add(element);
            }
            fireReportListenerReportElementsChanged(new ReportElementChangedEvent(this, changed_elements , ReportElementChangedEvent.CHANGED));
            this.getMainFrame().getElementPropertiesDialog().updateSelection();
            jPanelReport.repaint();
        } else if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_F2) {
            // Edit static Text

            DeleteElementsOperation undoOp = new DeleteElementsOperation(this);
            for (Iterator i = selectedElements.iterator(); i.hasNext(); ) {
                ReportElement re = (ReportElement) i.next();
                if(re instanceof TextReportElement){
                    if(re instanceof TextReportElement){
                        String te = ((TextReportElement)re).getText();

                        ExpressionEditor ed = new ExpressionEditor();
                        ed.setSubDataset(this.getReport());
                        ed.setExpression( te );
                        ed.updateTreeEntries();
                        ed.setVisible(true);
                        if (ed.getDialogResult() == javax.swing.JOptionPane.OK_OPTION)
                        {
                            ((TextReportElement)re).setText(ed.getExpression());
                            undoOp.addElement(  re, getReport().getElements().indexOf(re));
                            fireReportListenerReportElementsChanged(new ReportElementChangedEvent(this, re , ReportElementChangedEvent.CHANGED));
                        }
                    }
                }
            }
            this.addUndoOperation(undoOp);
            this.getMainFrame().getElementPropertiesDialog().updateSelection();
            jPanelReport.repaint();
        } else if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_F3) {

            transformStaticInTextFields();
            this.getMainFrame().getElementPropertiesDialog().updateSelection();
        }
        /* Begin code by Robert Lamping, 12 july 2004 */
        /* Copy and paste via CTRL/INS and SHIFT insert */
        else if ( (( evt.getModifiers() & evt.CTRL_MASK) != 0 ) && evt.getKeyCode() == java.awt.event.KeyEvent.VK_INSERT) {
            copy();
        } else if ( (( evt.getModifiers() & evt.SHIFT_MASK) != 0) && evt.getKeyCode() == java.awt.event.KeyEvent.VK_INSERT) {
            paste();
        }
        /* End code Robert Lamping, 12 July 2004 */


    }//GEN-LAST:event_jPanelReportKeyPressed

    /**
     * evaluateObjectType returns the Objecttype of a new Subreport
     * protected to be used in subclasses
     * added by Felix Firgau on February 21st 2007
     * @param newObjectType int
     * @param re ReportElement
     * @return boolean
     */
    protected boolean evaluateObjectType(int newObjectType, ReportElement re) {
      boolean ok = true;
      if ( newObjectType == ReportElementType.CROSSTAB_ELEMENT  ) {

        CrosstabWizardDialog cw = new CrosstabWizardDialog(getMainFrame(),true);
        cw.setCrosstabReportElement((CrosstabReportElement)re);
        cw.setReport( this.getReport());
        cw.setVisible(true);
        ((CrosstabReportElement)re).setReport(this.getReport());
        addCrosstabEditor( (CrosstabReportElement)re );
      }
      else if ( newObjectType == ReportElementType.SUBREPORT_ELEMENT  ) {

        SubreportWizard sr = new SubreportWizard();
        sr.setSubReportElement((SubReportElement)re);
        sr.startWizard();
        if (sr.getBaseWizardPanel().getDialogResult() != JOptionPane.OK_OPTION) ok=false;
        
      }
      else if ( newObjectType == ReportElementType.CHART_ELEMENT  ) {

        // New JasperReports 0.6.9 implementation...
        ChartSelectionJDialog csd = new ChartSelectionJDialog(this.getMainFrame(),true);
        csd.setVisible(true);
        if (csd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
          ChartReportElement2 cre = (ChartReportElement2)re ;
          cre.setChart( csd.getChart() );
        } else {
          ok = false;
        }
      }
      else if ( newObjectType == ReportElementType.BREAK_ELEMENT  ) {

        // New JasperReports 0.6.9 implementation...
        re.setWidth( this.getReport().getWidth() - getReport().getLeftMargin() - getReport().getRightMargin() );
        re.getPosition().x = getReport().getLeftMargin()+10;
        re.setHeight(0);
        re.updateBounds();
        if (re.getBand() == null) ok=false;
      }

      return ok;
    }

    private void jPanelReportMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelReportMouseReleased

        if (drag_selection_mode && evt.getButton() == evt.BUTTON1) {

            //Graphics2D gg = (Graphics2D)jPanelReport.getGraphics();
            //gg.setXORMode( ReportElement.getAlphaColor(Color.GREEN, 60)  );

            //Stroke s = gg.getStroke();
            //gg.setStroke(selectionStroke);

            //if (!first_draw_selection_rect) {
            //    gg.fillRect((int)Math.min( drag_selection_origin.x, drag_selection_end.x),
            //            (int)Math.min( drag_selection_origin.y, drag_selection_end.y),
            //            (int)Math.abs( drag_selection_origin.x - drag_selection_end.x),
            //            (int)Math.abs( drag_selection_origin.y - drag_selection_end.y));
            //}

            //gg.setPaintMode();
            //if (s != null) {
            //    gg.setStroke(s);
            //}
            drag_selection_mode = false;
            first_draw_selection_rect = true;

            if (( evt.getModifiers() & evt.SHIFT_MASK ) == 0){
                setSelectedElement(null);
            }

            int delta_w = evt.getX()- drag_selection_origin.x;
            int delta_h = evt.getY()- drag_selection_origin.y;
            int delta_x = (delta_w < 0) ? delta_w: 0;
            int delta_y = (delta_h < 0) ? delta_h : 0;

            if (jHorizontalScrollBar == null ||
                    jVerticalScrollBar == null  ||
                    drag_selection_origin == null ||
                    drag_selection_end == null) {
                return;
            }

            int originX = getLogicalDim(  Math.min( drag_selection_origin.x-10+jHorizontalScrollBar.getValue(),
                    drag_selection_end.x-10+jHorizontalScrollBar.getValue()))+10;
            int originY = getLogicalDim(  Math.min( drag_selection_origin.y-10+jVerticalScrollBar.getValue(),
                    drag_selection_end.y-10+jVerticalScrollBar.getValue()))+10;
            int width  =  getLogicalDim(  Math.abs( delta_w ) );
            int height =  getLogicalDim(  Math.abs( delta_h ) );

            // We need logical coordinates...
            java.awt.Rectangle rect = new java.awt.Rectangle( originX, originY, width, height );
            boolean selection_changed = false;
            for (Iterator i = getReport().getElements().iterator(); i.hasNext(); ) {
                ReportElement re = (ReportElement) i.next();
                if ( re.intersects( rect ) ) {
                    if (!selection_changed) selection_changed = true;
                    addSelectedElement(re,false);
                }
            }
            if (selection_changed) {
                fireSelectionChangedEvent();
            }

        } else if (band_dragging && evt.getButton() == evt.BUTTON1 ) {

            band_dragging = false;
            jPanelReport.setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

            // _________ BEGIN DRAGGING REAL TIME CODE ___________________
            // Work with logical dimensions...
            int delta = getLogicalDim( evt.getY()-band_dragging_origin );

            int okHeight = band_dragging_band.getHeight();
            int requestedHeight = okHeight + delta;

            if (delta != 0) {
                BandDraggedOperation undoOp = new BandDraggedOperation(this,band_dragging_band);
                // Search for min_height that don't cover elements in this band...

                int band_h = report.getBandYLocation(band_dragging_band) +10;

                int available_height = report.getHeight() - report.getTopMargin()- report.getBottomMargin();

                if (!band_dragging_band.getName().equals("background") && !band_dragging_band.getName().equals("noData") &&
                    !( band_dragging_band.getName().equals("title") && getReport().isIsTitleNewPage()) &&
                    !( band_dragging_band.getName().equals("summary") && getReport().isIsSummaryNewPage()))
                {
                    for (int i=0; i<report.getBands().size(); ++i)
                    {
                        Band aBand = (Band)report.getBands().elementAt(i);

                        if (!(aBand.getName().equals("background")) && !aBand.getName().equals("noData") &&
                            !(aBand.getName().equals("title") && getReport().isIsTitleNewPage()) &&
                            !(aBand.getName().equals("summary") && getReport().isIsSummaryNewPage()) &&
                            !(aBand == band_dragging_band))
                        {
                            available_height -= aBand.getHeight();
                        }
                    }
                }

                okHeight = Math.max(0, Math.min(available_height, requestedHeight));

                //System.out.println("Requested: " + requestedHeight + " " + available_height);

                delta = okHeight - band_dragging_band.getHeight();

                // Redraw all under the min height...
                band_dragging_band.setHeight( band_dragging_band.getHeight() + delta );

                this.jPanelReportComponentResized( new java.awt.event.ComponentEvent( jPanelReport, 0 ));

                //this.label1.setText("DRagged: "+band_dragging_band.name +" "+delta);
                int edge_y = Math.min(report.getBandYLocation(band_dragging_band)+band_dragging_band.getHeight(),
                        report.getBandYLocation(band_dragging_band)+band_dragging_band.getHeight()-delta);

                //e = report.getElements().elements();

                undoOp.setBandDelta(delta);

                // Adjust all elements positions...
                //
                Vector containers = new Vector();
                for (Iterator i = report.getElements().iterator(); i.hasNext(); ) {
                    ReportElement re = (ReportElement) i.next();
                    if (re.getParentElement() != null &&
                        re.getParentElement().position.y < edge_y+10 ) continue;

                    if (re instanceof FrameReportElement)
                    {
                        containers.add(re);
                    }

                    if (re.position.y >= edge_y+10 ||
                        (re.getParentElement() != null &&
                         re.getParentElement().position.y >= edge_y+10) ) {
                        // I should add the distance form the base of resized band, and my element...
                        Rectangle oldBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());

                        int space_before_band = band_h - re.position.y - re.height;
                        if (band_h <= report.getBandYLocation(re.band) +10){
                            space_before_band = 0;
                        }
                        re.setPosition(new Point(re.position.x,re.position.y+delta));
                        //this.updateElement(re,new Rectangle(re.position.x-5,re.position.y-5-delta,re.width+10, re.height+10+delta));
                        Rectangle newBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                        undoOp.addElement(re, oldBounds, newBounds );
                    }
                }
                addUndoOperation(undoOp);
                fireReportListenerReportBandChanged(new ReportBandChangedEvent(this,band_dragging_band, ReportBandChangedEvent.CHANGED));
            }
                        /*
                        getLogicalDim(panel9.getWidth()),getLogicalDim(panel9.getHeight()));
                        Rectangle rect = panel9.getDisplayRect(); //new Rectangle(0,0,report.getPagesize().x, report.getDesignHeight());

                        // drag down all elements under this band...
                        redrawAll(offscreenbm.getGraphics(),rect,0);
                        Rectangle clipRect = panel9.getDisplayRect();      //new Rectangle(rect);
                        //clipRect.offset(-HScrollBar1.getValue(),-VScrollBar1.getValue());
                        //panel9.createGraphics().drawRect(clipRect,RasterOp.BLACK);
                        panel9.createGraphics().drawImage( offscreenbm,rect, clipRect ,false);
                         */
            this.getMainFrame().getDocumentStructurePanel().updateUI();

            jPanelReport.repaint();
            // _________ STOP DRAGGING REAL TIME CODE ___________________
                        /* TODO
                        if (selectedElements.size()>0)
                        {
                                MainForm mf = (MainForm)this.getMDIParent();
                                if (mf != null)
                                {
                                        if (mf.isPropertiesOpened())
                                        {
                                                mf.initReportForm();
                                                mf.ep.setEelement(this,getSelectedElements());
                                                mf.ep.setVisible(true);
                                        }
                                }
                        }
                         TODO */
        }

        if (newObjectType != ReportElementType.NONE) {
            // The second point was clicked

            firstXORDraw = true;

            // Find the band to associate to the new element...

            int evtX = snapToGridOrizzontally(evt.getX());
            int evtY = snapToGridVertically(evt.getY());

            int delta_w = evtX - newObjectOrigin.x; //gridMultiple(evt.getX()-newObjectOrigin.x);
            int delta_h = evtY - newObjectOrigin.y; //gridMultiple(evt.getY()-newObjectOrigin.y);
            int delta_x = (delta_w < 0) ? delta_w : 0;
            int delta_y = (delta_h < 0) ? delta_h : 0;

            int originX = getLogicalDim( Math.min(newObjectOrigin.x-10+jHorizontalScrollBar.getValue(),
                    newObjectOrigin.x+delta_x-10+jHorizontalScrollBar.getValue()))+10;
            int originY = getLogicalDim( Math.min(newObjectOrigin.y-10+jVerticalScrollBar.getValue(),
                    newObjectOrigin.y+delta_y-10+jVerticalScrollBar.getValue()))+10;
            int width = getLogicalDim( Math.abs(delta_w) );
            int height = getLogicalDim( Math.abs(delta_h) );


            if (newObjectType == ReportElementType.LINE_ELEMENT) {
                /* When shift button is pressed too, then there will be no align ment on the grid */
                if ( ( evt.getModifiers() & evt.SHIFT_MASK)  != 0) {
                    Point straight = straighten( delta_w, delta_h);
                    delta_w = straight.x;
                    delta_h = straight.y;
                }
            }

            Band bname = getReport().getBandByPosition( originY );

            ReportElement re = ReportElementFactory.create( newObjectType, originX, originY, getLogicalDim(delta_w), getLogicalDim(delta_h));

            if (getReport().getStyles().size() > 0)
            {
                for (int i=0; i<getReport().getStyles().size(); ++i )
                {
                   Style s = (Style)getReport().getStyles().elementAt(i);

                   if (s.getAttributeBoolean(s.ATTRIBUTE_isDefault,false) == true)
                   {
                       re.setStyle( s );
                   }
                }
            }
            /* Begin Code Robert Lamping, 13 july 2004 */
            // Element does not know about other elements, the frame does, so here we reset the key.
            // barcode becomes barcode-1, or higher depending on what the highest sub number is.
            // Same for graphcs image, or whatever initial name
            re.setKey( getNextElementKey( re.getKey()) ) ;

            /* End Code Robert Lamping, 13 july 2004 */

            re.setBand( bname );

            for (int elnum = getReport().getElements().size()-1; elnum >= 0; --elnum)
            {
                ReportElement container_re = (ReportElement)getReport().getElements().elementAt(elnum);
                if (container_re instanceof FrameReportElement && container_re.getBand() == re.getBand())
                {
                    if ( container_re.getBounds().contains( re.getBounds()))
                    {
                        re.setParentElement( container_re);
                        break;
                    }
                }
            }



            /**
             * changed by Felix Firgau on February 21st 2007
             * for easier subclassing
             */
            boolean ok = evaluateObjectType(newObjectType, re);

            if (ok) {

                report.getElements().addElement(re);

                fireReportListenerReportElementsChanged(new ReportElementChangedEvent(this, re , ReportElementChangedEvent.ADDED));

                addUndoOperation( new  InsertElementOperation( this, re ) );


                            /*
                            if (selectedElements.size() > 0)
                            {
                                    ReportElement oldselected = (ReportElement)selectedElements.firstElement();
                                    Rectangle rect = new Rectangle(oldselected.position.x-5,oldselected.position.y-5,oldselected.width+10, oldselected.height+10);
                                    setSelectedElement(null);
                                    redrawAll(offscreen ,rect,5);
                                    Rectangle clipRect = new Rectangle(getZoomedDim(oldselected.position.x-10)-5+10-jHorizontalScrollBar.getValue(),
                                                                                                               getZoomedDim(oldselected.position.y-10)-5+10-jVerticalScrollBar.getValue(),
                                                                                                               getZoomedDim(oldselected.width)+10,
                                                                                                               getZoomedDim(oldselected.height)+10);
                                    clipRect.translate(-jHorizontalScrollBar.getValue(),-jVerticalScrollBar.getValue());

                                    //panel9.createGraphics().drawImage( offscreenbm,clipRect, clipRect ,false);
                                    jPanelReport.getGraphics().drawImage( offscreenImage ,
                                                                          clipRect.x ,clipRect.y, clipRect.width,clipRect.height,
                                                                          clipRect.x ,clipRect.y, clipRect.width,clipRect.height,
                                                                          (ImageObserver)this);
                            }
                             */
                setSelectedElement(re);
            }
            newObjectOrigin = null;
            // Add undo operation...
            //UndoOperation uo = new UndoOperation(UndoOperationType.UNDO_INSERT);
            //uo.params.addElement(new Integer(1));
            //ReportElement clone = re.cloneMe();

            //uo.params.addElement(clone);
            //addUndoOperation(uo);
            //repaint();

            newObjectType = ReportElementType.NONE;

            jPanelReport.setCursor( Cursor.getDefaultCursor());
            getMainFrame().setActiveTool(0);


        }

        if (trasforming) {
            trasforming = false;
            paintedAlignLines.clear();
            
            jPanelReport.setCursor( Cursor.getDefaultCursor());

            if (transformation_type != TransformationType.TRANSFORMATION_MOVE || resistenceExceeded == true) {
                Point p = new Point(transformation_origin_end);
                p.x = getLogicalDim(p.x);
                p.y = getLogicalDim(p.y);
                p.x -= getLogicalDim(transformation_origin.x);
                p.y -= getLogicalDim(transformation_origin.y);

                // Now  it's time to correct the deltas...
                // We simply have to do this correction because end coordinates can been calculated
                // with a strange percentage


                Vector selectedElementsToChange = new Vector();

                Enumeration e = getSelectedElements().elements();
                while (e.hasMoreElements()) {
                    ReportElement re = (ReportElement)e.nextElement();
                    if (!selectedElementsToChange.contains(re))
                    {
                        selectedElementsToChange.add(re);
                        if (transformation_type == TransformationType.TRANSFORMATION_MOVE)
                        {
                            if (re instanceof FrameReportElement)
                            {
                                // Add all child elements ..
                                addChildElements(re, selectedElementsToChange);
                            }
                        }
                    }
                }
                e = selectedElementsToChange.elements();
                TransformElementsOperation undoOp = new TransformElementsOperation(this);
                Vector changed_elements = new Vector();

                while (e.hasMoreElements()) {
                    ReportElement re = (ReportElement)e.nextElement();



                    undoOp.addElement( re );

                    // Rectangle oldBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());

                    if (re instanceof BreakReportElement)
                    {
                        Point specialP = new Point(0, p.y);

                        if (re.getPosition().y + p.y == re.getBand().getBandYLocation() + re.getBand().getHeight() + 10 )
                        {
                            specialP.y = p.y-1;
                        }
                        if (transformation_type == TransformationType.TRANSFORMATION_MOVE ||
                            transformation_type == TransformationType.TRANSFORMATION_RESIZE_N)
                        {
                            re.trasform(specialP, TransformationType.TRANSFORMATION_MOVE);
                        }
                        else
                        {
                            continue;
                        }
                    }
                    else
                    {
                        re.trasform(p, transformation_type);
                    }


                    // Band oldBand = re.getBand();
                    if (re.getParentElement() == null)
                    {
                        re.adjustBand();
                    }
                    // Band newBand = re.getBand();
                    // Rectangle newBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    // undoOp.addElement(re, oldBounds, newBounds,oldBand,newBand);

                    undoOp.captureUniqueModified(re);
                    changed_elements.add( re );

                }
                addUndoOperation(undoOp);
                if (changed_elements.size() > 0) {
                    fireReportListenerReportElementsChanged(new ReportElementChangedEvent(this, changed_elements , ReportElementChangedEvent.CHANGED));
                }

                jPanelReport.repaint();
            }

                /*
                        if (p.x!=0 || p.y != 0)
                        {
                                UndoOperation uo = new UndoOperation(UndoOperationType.UNDO_TRANSFORMATION);
                                uo.params.addElement(p);
                                uo.params.addElement(new Integer( transformation_type ) );
                                Enumeration enum = selectedElements.elements();
                                while (enum.hasMoreElements())
                                {
                                                uo.params.addElement(enum.nextElement());
                                }
                                addUndoOperation(uo);
                        }
                 */
            transformation_undo_delta = new Point(0,0);
            transformation_type = TransformationType.TRANSFORMATION_NONE;
                /*
                        MainForm mf = (MainForm)this.getMDIParent();
                        if (mf != null)
                        {
                                if (mf.isPropertiesOpened())
                                {
                                        mf.initReportForm();
                                        mf.ep.setEelement(this,getSelectedElements());
                                        mf.ep.setVisible(true);
                                }
                        }
                 */
            getMainFrame().getElementPropertiesDialog().updateSelection();
        }

    }//GEN-LAST:event_jPanelReportMouseReleased


    /**
     * Single Click
     * - Left button: ...
     * //TODO
     * - Right button: ...
     * //TODO
     *
     * Double click
     * - On a band split: If no elements are selected, then a double click on a band split tries to
     *   reduce the bandheight to the enclosing rectangle around the elements in the band above the split.
     *   Elements in the top marging are not taken into account.
     * - Otherwise
     * @param evt
     */
    private void jPanelReportMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelReportMousePressed

        jPanelReport.requestFocus();
        if (evt.getButton() == evt.BUTTON1 && evt.getClickCount() < 2)
        {

            if (newObjectType != ReportElementType.NONE)
            {
                firstXORDraw = false;
                newObjectOrigin = new Point(snapToGridOrizzontally(evt.getX()),snapToGridVertically(evt.getY()));
                mouse.x = newObjectOrigin.x;
                mouse.y = newObjectOrigin.y;
                return;
            }

            // Begin code Robert Lamping, 18 July 2004
            // Zoom in interactively with SHIFT/CTRL + LEFT mouse button
            if ( (( evt.getModifiers() & evt.SHIFT_MASK) != 0) &&
                    (( evt.getModifiers() & evt.CTRL_MASK)  != 0) )
            {
                // multiply zoomfactor by 2 and zoom in with current mouse position as center!
                //Point center = new Point(
                Point clickPoint = this.realPoint( evt.getX(), evt.getY() );
                setZoomFactor( getZoomFactor() * 2 );
                getMainFrame().setComboBoxZoomFactor( getZoomFactor() * 100 );
                centerSelectedElements( clickPoint );  // if not present use clickPoint
                return;
            }
            // End code Robert Lamping, 18 July 2004


            if (selectedElements.size() > 0 && (( evt.getModifiers() & evt.SHIFT_MASK ) == 0))
            {
                // We are transformation mode?
                if (transformation_type >=0)
                {
                    trasforming = true;
                    paintedAlignLines.clear();
                    firstXORDrawTransforming = true;
                    transformation_origin = new Point(evt.getX(),evt.getY());
                    transformation_undo_delta = new Point(0,0);
                    transformation_origin_end = new Point(evt.getX(),evt.getY());
                    updateObjectMagnetics();
                    return;
                }
            }

            if (jPanelReport.getCursor() == hsplit)
            {

                first_draw_band=true;
                int bl = evt.getY() + jVerticalScrollBar.getValue();
                int y=report.getTopMargin();
                boolean found=false;
                Band band=null;

                if (( evt.getModifiers() & evt.SHIFT_MASK ) != 0)
                {
                    //label1.setText("Inverse search");
                    y = report.getDesignHeight()-report.getBottomMargin();
                    for (int b =report.getBands().size()-1; b>=0; --b)
                    {
                        band = (Band)report.getBands().elementAt(b);
                        if (bl>getZoomedDim(y)+10-3 && bl < getZoomedDim(y)+10+3)
                        {
                            break;
                        }
                        y -= band.getHeight();
                    }
                }
                else
                {
                    for (Iterator i = getReport().getBands().iterator(); i.hasNext(); )
                    {
                        band = (Band) i.next();
                        y += band.getHeight();
                        if (bl>getZoomedDim(y)+10-3 && bl < getZoomedDim(y)+10+3)
                        {
                            break;
                        }
                    }
                }
                if (band != null)
                {
                    band_dragging = true;
                    band_dragging_origin = evt.getY();
                    band_dragging_band = band;

                    //this.label1.setText(band_dragging_band.name +  " band height: " +(band_dragging_band.height));
                    return;
                }
            }
            for (int i=report.getElements().size()-1; i>=0; --i)
            {

                ReportElement re = (ReportElement)report.getElements().elementAt(i);
                
                int treshold = 1;
                if (re instanceof LineReportElement) treshold = 2;
                treshold = Math.max(treshold, (int)getZoomFactor());
                
                Rectangle onScreenRect = new Rectangle(
                                getZoomedDim(re.getPosition().x-10)+10- jHorizontalScrollBar.getValue() - treshold, 
                                getZoomedDim(re.getPosition().y-10)+10- jVerticalScrollBar.getValue() - treshold,
                                getZoomedDim(re.getWidth()) + 2*treshold, 
                                getZoomedDim(re.getHeight()) + 2*treshold);
                if (re instanceof BreakReportElement)
                {
                    onScreenRect.x =  0;
                    onScreenRect.width = getReport().getWidth();
                }
                
                if (onScreenRect.contains( evt.getX(), evt.getY() ) )
                {
                /*
                if (re.intersects(new Point(getLogicalDim(evt.getX()+jHorizontalScrollBar.getValue()-10)+10,
                        getLogicalDim(evt.getY()+jVerticalScrollBar.getValue()-10)+10)) ||
                    (re instanceof BreakReportElement && re.intersects(new Rectangle(
                               0, getLogicalDim(evt.getY()+jVerticalScrollBar.getValue()-10)+10,
                               getReport().getWidth(), 1)
                          ))
                     )
                {
                */  
                    
                    if (selectedElements.size()>0 && re == ((ReportElement)selectedElements.firstElement())) return;
                    // 1.<Cancel old corona...

                    //if (selectedElements.size() > 0 &&  (( evt.getModifiers() & evt.SHIFT_MASK ) == 0))
                    //{
                    //    setSelectedElement(null);
                    //}
                    
                    if ((( evt.getModifiers() & evt.SHIFT_MASK ) == 0) || selectedElements.size()==0)
                    {
                        //getSelectedElements().removeAllElements();
                        setSelectedElement(re);
                        jPanelReport.repaint();
                    }
                    else if (( evt.getModifiers() & evt.SHIFT_MASK ) != 0)
                    {
                        if (getSelectedElements().contains(re))
                        {
                            getSelectedElements().remove(re);
                            jPanelReport.repaint();
                        }
                        else
                        {
                            addSelectedElement(re);
                        }
                    }
                    else
                    {
                        setSelectedElement(re);
                    }
                    trasforming = true;
                    updateObjectMagnetics();
                    firstXORDrawTransforming = true;
                    transformation_origin = new Point(evt.getX(),evt.getY());
                    transformation_origin_end = new Point(evt.getX(),evt.getY());

                    if (getMainFrame().isEMMActive())
                        transformation_type = TransformationType.TRANSFORMATION_NONE;
                    else
                        transformation_type = TransformationType.TRANSFORMATION_MOVE;

                    jPanelReport.setCursor(Cursor.getPredefinedCursor( Cursor.MOVE_CURSOR ));
                    return;

                }
            }
            getMainFrame().getElementPropertiesDialog().updateSelection();

            /*
            if (selectedElements.size()>0 && (( evt.getModifiers() & evt.SHIFT_MASK ) == 0))
            {
                ReportElement myre = (ReportElement)selectedElements.firstElement();
                if (myre != null)
                {
                        Rectangle rect = new Rectangle(myre.position.x-5,myre.position.y-5,myre.width+10, myre.height+10);
                        setSelectedElement(null);
                }
            }
             */

            if (( evt.getModifiers() & evt.SHIFT_MASK ) == 0 && getSelectedElements().size() > 0)
                setSelectedElement(null);

            // We can enter in drag selection mode...
            drag_selection_mode = true;
            initOffscreenImage();
            first_draw_selection_rect = true;
            drag_selection_origin = new Point(evt.getX(),evt.getY());
        }
        else if (evt.getButton() == evt.BUTTON3)
        {

            // Begin code Robert Lamping, 18 July 2004
            // Zoom out interactively with SHIFT/CTRL = RIGHT mouse button
            if ( (( evt.getModifiers() & evt.SHIFT_MASK) != 0) &&
                    (( evt.getModifiers() & evt.CTRL_MASK)  != 0) )
            {
                // divide zoomfactor by 2 and zoom in with current mouse position as center!
                //Point center = new Point(
                Point clickPoint = this.realPoint( evt.getX(), evt.getY() );
                setZoomFactor( getZoomFactor() / 2 );
                getMainFrame().setComboBoxZoomFactor( getZoomFactor() * 100 );
                centerSelectedElements( clickPoint );  // if not present use clickPoint
                return;
            }
            // End code Robert Lamping, 18 July 2004

            popup_opened_at = evt.getPoint();
            boolean found = false;
            //1. We must see if we are over an object...
            for (int i=report.getElements().size()-1; i>=0; --i)
            {
                ReportElement re = (ReportElement)report.getElements().elementAt(i);
                if (re.intersects(new Point(getLogicalDim(evt.getX()+jHorizontalScrollBar.getValue()-10)+10,getLogicalDim(evt.getY()+jVerticalScrollBar.getValue()-10)+10)))
                {

                    if ( this.getSelectedElements().size() <= 1 )
                        setSelectedElement(re);
                    //                    if ( this.getSelectedElements().size() > 0 && (( evt.getModifiers() & evt.SHIFT_MASK ) != 0))
                    //                    {
                    //                        addSelectedElement(re);
                    //                    }
                    //                    else
                    //                        setSelectedElement(re);

                    found = true;
                    break;
                }
            }

            jMenuItemEditExpression.setVisible(false);
            jMenuItemElementChartProperties.setVisible(false);
            jMenuItemElementCrosstabProperties.setVisible(false);
            jMenuItemElementCrosstabDesignProperties.setVisible(false);
            jMenuItemElementOpenSubreport.setVisible(false);
            if (getSelectedElements().size() > 0)
            {
                if (getSelectedElements().elementAt(0) instanceof ChartReportElement2)
                {
                    jMenuItemElementChartProperties.setVisible(true);
                }

                if (getSelectedElements().elementAt(0) instanceof CrosstabReportElement)
                {
                    jMenuItemElementCrosstabProperties.setVisible(true);
                    jMenuItemElementCrosstabDesignProperties.setVisible(true);
                }

                if (getSelectedElements().elementAt(0) instanceof SubReportElement)
                {
                    jMenuItemElementOpenSubreport.setVisible(true);
                }
                
                if (getSelectedElements().elementAt(0) instanceof TextFieldReportElement)
                {
                    jMenuItemEditExpression.setVisible(true);
                }
                
                

                this.jMenuItemCopy.setEnabled(true);
                this.jMenuItemCut.setEnabled(true);
                this.jMenuItemDelete.setEnabled(true);
                this.jMenuItemCopyStyle.setEnabled(true);
            }
            else
            {
                this.jMenuItemCopy.setEnabled(false);
                this.jMenuItemCut.setEnabled(false);
                this.jMenuItemDelete.setEnabled(false);
                this.jMenuItemCopyStyle.setEnabled(false);
            }


            if (getMainFrame().getStyleClipboard() != null)
            {
                this.jMenuItemPasteStyle.setEnabled(true);
            }
            else
            {
                this.jMenuItemPasteStyle.setEnabled(false);
            }

            if (getMainFrame().getClipBoard().size() > 0)
            {
                this.jMenuItemPaste.setEnabled(true);
            }
            else
            {
                this.jMenuItemPaste.setEnabled(false);
            }

            Enumeration selectedElementEnum = getSelectedElements().elements();
            boolean allTextFields = true;
            boolean onGroup = false; // true if at least an element belong to a group
            boolean sameBand = true;
            Band theBand = null;
            while (selectedElementEnum.hasMoreElements())
            {
                ReportElement re = (ReportElement)selectedElementEnum.nextElement();
                if (allTextFields && !(re instanceof TextFieldReportElement))
                {
                    allTextFields = false;
                }

                if (!onGroup && !re.getElementGroup().equals("")) onGroup = true;

                if (theBand == null) theBand = re.getBand();
                else if (sameBand && theBand != re.getBand()) sameBand = false;

                if (!allTextFields && onGroup && !sameBand) break;
            }

            jMenuItemPattern.setEnabled( allTextFields );

            jMenuItemGroup.setEnabled( sameBand );
            jMenuItemUngroup.setEnabled( onGroup );

            if(found)
            {
                // we have find an element, open the contextual menu over it...

                if ( this.getSelectedElements().size() == 1 )
                {
                    //Show Popup with menu for one element
                    jMenuHSpacing.setEnabled(false);
                    jMenuVSpacing.setEnabled(false);
                    jPopupMenuElement.show(jPanelReport,evt.getX(),evt.getY());

                    return;
                }
                if ( this.getSelectedElements().size() > 1 )
                {
                    //Show Popup with special menu for more elements
                    jMenuHSpacing.setEnabled(true);
                    jMenuVSpacing.setEnabled(true);
                    jPopupMenuElement.show(jPanelReport,evt.getX(),evt.getY());
                    return;
                }


            }
            // Looking for the band....
            jPopupMenuBand.show(jPanelReport,evt.getX(),evt.getY());
            return;
        }
        else if (evt.getButton() ==  evt.BUTTON1 && evt.getClickCount() == 2)
        {

            // Begin code Robert Lamping, 18 July 2004
            // --------------------------------------------------------------------------------
            // ZOOM IN
            // --------------------------------------------------------------------------------
            if ( (( evt.getModifiers() & evt.SHIFT_MASK) != 0) &&
                    (( evt.getModifiers() & evt.CTRL_MASK)  != 0) )
            {
                Point clickPoint = this.realPoint( evt.getX(), evt.getY() );
                setZoomFactor( getZoomFactor() * 2 );
                getMainFrame().setComboBoxZoomFactor( getZoomFactor() * 100 );
                centerSelectedElements( clickPoint);  // if not present use clickPoint
                return;
            }
            // End code Robert Lamping, 18 July 2004


            // Begin code Robert Lamping, 24 July 2004
            // --------------------------------------------------------------------------------
            // Double click on band: move band split to lowest element in band if any.
            // Otherwise no action.
            // --------------------------------------------------------------------------------

            if (getSelectedElements().size() == 0)
            {

                // If there are any elements in the band just above the band limit,
                // then set the band height to the maximum Y position of the elements.
                // THis way the band just encloses the elements.

                // TEST: Create square in detail band. Square must be smaller than band
                // double click on lower detail band split.
                // Expected result: band height aligns under element bottom.

                // We must find the right band split

                Band band = null;
                int y = report.getTopMargin();
                int clicked_y = evt.getY() + jVerticalScrollBar.getValue();
                int proximity = 3 ;

                for (Iterator i = getReport().getBands().iterator(); i.hasNext(); )
                {
                    band = (Band) i.next();
                    y += band.getHeight();
                    if ( clicked_y > getZoomedDim(y) + 10 - proximity && clicked_y < getZoomedDim(y) + 10 + proximity)
                    {
                        break;
                    }
                }

                if ( band != null )
                {
                    int bandHeight = band.getHeight();
                    shrinkBand( band );
                    if ( bandHeight != band.getHeight() )
                    {
                        jPanelReport.repaint();
                        return;
                    }
                }

            }
            else
            {
                // double clicked within a selected element
                // check if you clicked on South
                // in case of a text element, this will reset the height to fontsize() + 2

                //logOnConsole( "Try to check whether you clicked on south\n");
                //traverse all selectedelement and check whether you clicked on south.
                // if so then adjust the element height to textheight + 2


                //int clicked_x = evt.getX() + jHorizontalScrollBar.getValue();
                //int clicked_y = evt.getY() + jVerticalScrollBar.getValue();
                //int proximity = 4 ;
                //Rectangle clickedRect = new Rectangle( clicked_x - proximity, clicked_y - proximity, 2* proximity, 2* proximity);
                //logOnConsole( "Clickpoint: " + clicked_x + " " + clicked_y + "\n");

                Rectangle cursor = new Rectangle(evt.getX()+jHorizontalScrollBar.getValue(),
                        evt.getY()+jVerticalScrollBar.getValue(),
                        1,1);
                Rectangle grip = new Rectangle();
                grip.width  = 5;
                grip.height = 5;

                ReportElement clickedRe = null;

                for (Iterator i = report.getElements().iterator(); i.hasNext(); )
                {
                    ReportElement re = (ReportElement) i.next();

                    // Grip settings for South:
                    grip.x = getZoomedDim(re.position.x+(re.width/2)-10)+10-2;
                    grip.y = getZoomedDim(re.position.y+re.height-10)+10;

                    if (cursor.intersects(grip))
                    {
                        clickedRe = re;
                        break ;
                    }
                }

                if ( clickedRe != null && getMainFrame().getProperties().getProperty("EnableTextResizeClick","true").equals("true") )
                {
                    // logOnConsole( "South clicked of " + clickedRe.getKey() + " \n");
                    if (clickedRe instanceof TextReportElement )
                    {
                        TransformElementsOperation undoOp = new TransformElementsOperation(this);
                        TextReportElement tre = (TextReportElement) clickedRe;

                        undoOp.addElement(tre);

                        //Rectangle oldBounds = clickedRe.getBounds();
                        tre.setHeight( tre.getFontSize() + 3 );
                        tre.updateBounds();

                        //Rectangle newBounds = clickedRe.getBounds();

                        // let the transformationElement capture its needs
                        undoOp.captureUniqueModified(tre);
                        // add a task to the undo operation.
                        // undoOp.addElement(clickedRe, oldBounds, newBounds );

                        // Pass the undo tasks to the undo cluster.
                        addUndoOperation(undoOp);

                        // repaint the screen
                        jPanelReport.repaint();
                        return;
                    }
                }
                else
                {
                    // Check if we are over a textreportelement
                    if (getSelectedElements().size() == 1)
                    {
                        ReportElement re = (ReportElement)getSelectedElements().elementAt(0);
                        if (re instanceof TextReportElement)
                        {
                            TextReportElement tre = (TextReportElement) re;

                            Rectangle textArea = new Rectangle();
                            textArea.width  = getZoomedDim(re.getWidth());
                            textArea.height = getZoomedDim(re.getHeight());
                            textArea.x = getZoomedDim(re.position.x-10)+10;
                            textArea.y = getZoomedDim(re.position.y-10)+10;


                            try {

                            if (cursor.intersects( textArea))
                            {
                                onlineEditingTextReportElement = tre;
                                floatingTextArea.setText( tre.getText() );
                                floatingTextArea.setOpaque( true );

                                textArea.x -= jHorizontalScrollBar.getValue();
                                textArea.y -= jVerticalScrollBar.getValue();

                                //floatingTextArea.setFont( new Font(tre.getFontName(), (() ? Font.BOLD : 0) & ((tre.isItalic()) ? Font.BOLD : Font.ITALIC) , (int)getZoomedDim(tre.getFontSize())));
                                floatingTextArea.setForeground( tre.getFgcolor());
                                floatingTextArea.setBackground( tre.getBgcolor());
                                this.getReportPanel().add(floatingTextArea);
                                floatingTextArea.setBounds( textArea );
                                floatingTextArea.setSelectionStart(0);
                                floatingTextArea.setSelectionEnd(tre.getText().length());
                                SimpleAttributeSet set=new SimpleAttributeSet();

                                StyledDocument doc=floatingTextArea.getStyledDocument();
                                if (tre.getAlign().equals("Center"))
                                {
                                    StyleConstants.setAlignment(set,StyleConstants.ALIGN_CENTER);
                                }
                                else if (tre.getAlign().equals("Right"))
                                {
                                    StyleConstants.setAlignment(set,StyleConstants.ALIGN_RIGHT);
                                }
                                else if (tre.getAlign().equals("Left"))
                                {
                                    StyleConstants.setAlignment(set,StyleConstants.ALIGN_LEFT);
                                }
                                else if (tre.getAlign().equals("Justified"))
                                {
                                    StyleConstants.setAlignment(set,StyleConstants.ALIGN_JUSTIFIED);
                                }

                                StyleConstants.setFontFamily(set, tre.getFontName() );
                                StyleConstants.setFontSize(set, (int)getZoomedDim(tre.getFontSize()) );
                                StyleConstants.setBold(set,tre.isBold());
                                StyleConstants.setBold(set,tre.isItalic());
                                StyleConstants.setStrikeThrough(set,tre.isStrikeTrought());
                                StyleConstants.setUnderline(set,tre.isUnderline());

                                floatingTextArea.setParagraphAttributes(set,true);

                                this.getReportPanel().updateUI();
                                floatingTextArea.requestFocusInWindow();

                                //floatingTextArea.requestFocusInWindow();
                                //floatingTextArea.invalidate();
                                //floatingTextArea.repaint();
                            }
                            } catch (Exception ex)
                            {
                                ex.printStackTrace();
                            }
                        }
                        else
                        {
                            openElementPropertiesDialog();
                        }
                    }
                    else
                    {
                        openElementPropertiesDialog();
                    }
                }
            }
            // End code Robert Lamping, 24 July 2004, modified june 4, 2005

            // otherwise try to open
            // openElementPropertiesDialog();
        }
    }//GEN-LAST:event_jPanelReportMousePressed

    private void jHorizontalScrollBarAdjustmentValueChanged(java.awt.event.AdjustmentEvent evt) {//GEN-FIRST:event_jHorizontalScrollBarAdjustmentValueChanged
        floatingTextAreaFocusLost();
        isDocDirty = true;
        //logOnConsole( "jHorizontalScrollBar.getValue() : "  + jHorizontalScrollBar.getValue() + "\n" );
        jPanelReport.repaint();
        jHorizontalRule.repaint();
    }//GEN-LAST:event_jHorizontalScrollBarAdjustmentValueChanged

    private void jPanelReportComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jPanelReportComponentResized
        if (jPanelReport.getWidth() >= (getZoomedDim( report.getWidth())+19)) {
            jHorizontalScrollBar.setMaximum(report.getWidth());
            jHorizontalScrollBar.setVisibleAmount( jHorizontalScrollBar.getMaximum());
            jHorizontalScrollBar.setValue(0);
            ////VScrollBar1_scroll(VScrollBar1, new ScrollEvent( ScrollEventType.ENDSCROLL,0));
        } else {
            jHorizontalScrollBar.setMaximum((getZoomedDim( report.getWidth()) +19));
            this.jHorizontalScrollBar.setVisibleAmount(jPanelReport.getWidth());
            this.jHorizontalScrollBar.setBlockIncrement( this.jHorizontalScrollBar.getVisibleAmount() );
        }

        if (jPanelReport.getHeight() >= getZoomedDim(report.getDesignHeight())+19) {
            jVerticalScrollBar.setMaximum(report.getDesignHeight());
            jVerticalScrollBar.setVisibleAmount( jVerticalScrollBar.getMaximum());
            jVerticalScrollBar.setValue(0);
            //VScrollBar1_scroll(VScrollBar1, new ScrollEvent( ScrollEventType.ENDSCROLL,0));
        } else {
            jVerticalScrollBar.setMaximum(getZoomedDim( report.getDesignHeight())+19);
            this.jVerticalScrollBar.setVisibleAmount(jPanelReport.getHeight());
            this.jVerticalScrollBar.setBlockIncrement( this.jVerticalScrollBar.getVisibleAmount() );
        }
        // getMainFrame().updateFitToPage(this);  deactivated by Robert

        jPanelReport.repaint();
    }//GEN-LAST:event_jPanelReportComponentResized

    private void jVerticalScrollBarMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jVerticalScrollBarMouseReleased

    }//GEN-LAST:event_jVerticalScrollBarMouseReleased

    private void jVerticalScrollBarAdjustmentValueChanged(java.awt.event.AdjustmentEvent evt) {//GEN-FIRST:event_jVerticalScrollBarAdjustmentValueChanged

        floatingTextAreaFocusLost();
        isDocDirty = true;
        jPanelReport.repaint();
        jVerticalRule.repaint();
    }//GEN-LAST:event_jVerticalScrollBarAdjustmentValueChanged

    private void jPanelReportMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelReportMouseDragged

        this.jHorizontalRule.setCursorPosition(evt.getX());
        this.jVerticalRule.setCursorPosition(evt.getY());
        if (mouse == null) {
            mouse = new Point();
        }

        if (drag_selection_mode == true) {
            Graphics2D gg = (Graphics2D)jPanelReport.getGraphics();
            
            //gg.setXORMode(ReportElement.getAlphaColor(Color.GREEN, 60));
            Stroke s = gg.getStroke();
            gg.setStroke(selectionStroke);
            //if (!first_draw_selection_rect) {
                //gg.fillRect((int)Math.min( drag_selection_origin.x, drag_selection_end.x),
                //        (int)Math.min( drag_selection_origin.y, drag_selection_end.y),
                //        (int)Math.abs( drag_selection_origin.x - drag_selection_end.x),
                //        (int)Math.abs( drag_selection_origin.y - drag_selection_end.y));
            //    gg.drawImage(offscreenImage,0,0,null);
            //}

            
            // Draw the new rectangle...
            // Create the maximum rectangle...
            
            if (drag_selection_end == null)
            {
                drag_selection_end = new Point(drag_selection_origin.x, drag_selection_origin.y);
            }
            int rx1 = drag_selection_origin.x;
            int rx2 = drag_selection_end.x;
            int ry1 = drag_selection_origin.y;
            int ry2 = drag_selection_end.y;
            
            
            Rectangle2D r_old = new Rectangle2D.Double(Math.min(rx1,rx2), Math.min(ry1, ry2),
                                                   Math.abs(rx1-rx2), Math.abs(ry1-ry2));
            
            drag_selection_end = new java.awt.Point(evt.getX(), evt.getY());
            rx2 = drag_selection_end.x;
            ry2 = drag_selection_end.y;
            
            r_old.add( new Rectangle2D.Double(Math.min(rx1,rx2), Math.min(ry1, ry2),
                                                   Math.abs(rx1-rx2), Math.abs(ry1-ry2)) );
            
            offscreenImage2g.setClip(r_old);
            
            offscreenImage2g.drawImage(offscreenImage,0,0,null);
            offscreenImage2g.setColor(ReportElement.getAlphaColor(Color.BLUE, 60));
            offscreenImage2g.fillRect((int)Math.min( drag_selection_origin.x, drag_selection_end.x),
                    (int)Math.min( drag_selection_origin.y, drag_selection_end.y),
                    (int)Math.abs( drag_selection_origin.x - drag_selection_end.x),
                    (int)Math.abs( drag_selection_origin.y - drag_selection_end.y));
            
            offscreenImage2g.setColor(ReportElement.getAlphaColor(Color.BLUE, 255));
            offscreenImage2g.drawRect((int)Math.min( drag_selection_origin.x, drag_selection_end.x),
                    (int)Math.min( drag_selection_origin.y, drag_selection_end.y),
                    (int)Math.abs( drag_selection_origin.x - drag_selection_end.x)-1,
                    (int)Math.abs( drag_selection_origin.y - drag_selection_end.y)-1);
            
           gg.setClip(r_old);
            
            gg.drawImage(offscreenImage2,0,0,null);
            //gg.setPaintMode();
            first_draw_selection_rect = false;
            if (s != null) gg.setStroke(s);
        }

        if (newObjectType == ReportElementType.NONE && !transforming) {
            // Is the mouse over a band line ?...
            if (band_dragging) {
                //  2. delta = Max altezza banda precedente e position delta
                int delta = getLogicalDim( evt.getY() - band_dragging_origin );
                //this.label1.setText(band_dragging_band.name +  " band height: " +(band_dragging_band.height));
                if (delta != 0) {

                    // Vediamo se e' possibile andare piu' su...
                    // Non possiamo andare piu' su della banda prima...
                    //	1. Cerchiamo la banda precedente...

                    int min_logical_base = report.getTopMargin()+10;
                    int base_position=0;// = Math.max(min_logical_base, getReport().getBandYLocation(band_dragging_band) +delta);
                    base_position = getZoomedDim(report.getBandYLocation(band_dragging_band))+10- jVerticalScrollBar.getValue();
                    Graphics gg = jPanelReport.getGraphics();
                    gg.setXORMode(Color.WHITE);
                    if (!first_draw_band) {
                        gg.drawLine( 0,Math.max(mouse.y,base_position),jPanelReport.getWidth(),Math.max(mouse.y,base_position) );

                    }
                    gg.drawLine(0,Math.max(evt.getY(),base_position),jPanelReport.getWidth(),Math.max(evt.getY(),base_position) );
                    gg.setPaintMode();
                    first_draw_band = false;
                    //this.label1.setText(band_dragging_band.name +  " band height: " +(band_dragging_band.height + delta));
                }
            }
        }

        if (newObjectType != ReportElementType.NONE && newObjectOrigin!= null) {

            /* In the process of defing the second point. */

            Graphics gg = jPanelReport.getGraphics();
            gg.setXORMode(Color.WHITE);
            int delta_x = 0;
            int delta_y = 0;

            if (newObjectType != ReportElementType.LINE_ELEMENT && !firstXORDraw) {
                delta_x = mouse.x- newObjectOrigin.x;
                delta_y = mouse.y-newObjectOrigin.y;

                delta_x = (delta_x <0) ? delta_x : 0;
                delta_y = (delta_y<0) ? delta_y : 0;
            }


            int delta_w = 0;
            int delta_h = 0;
            if (!firstXORDraw) {
                int mevtX = snapToGridOrizzontally(mouse.x);
                int mevtY = snapToGridVertically(mouse.y);

                delta_w = mevtX - newObjectOrigin.x;  //gridMultiple(evtX-newObjectOrigin.x);
                delta_h = mevtY - newObjectOrigin.y;  //gridMultiple(evtY-newObjectOrigin.y);
                delta_x = (delta_w<0) ? delta_w: 0;
                delta_y = (delta_h<0) ? delta_h : 0;
            }

            int evtX = snapToGridOrizzontally(evt.getX());
            int evtY = snapToGridVertically(evt.getY());

            if (newObjectType == ReportElementType.LINE_ELEMENT) {

/*                if ( ( evt.getModifiers() & evt.SHIFT_MASK)  != 0) {
                  if (( delta_w + delta_h ) > 0 ) {
                      double cos =  Math.PI * delta_w /  Math.sqrt(  (delta_w* delta_w) + (delta_h*delta_h) );
                      logOnConsole( "Cosinus: " + cos + "\n");

                      if ( Math.abs( cos ) >  Math.abs( Math.cos(0.25 * Math.PI ) ) ) {
                          delta_h = 0 ;
                      }
                  }
                }
 */
                /* overwrite the other one, so that it disappears */

                if ( ( evt.getModifiers() & evt.SHIFT_MASK)  != 0) {
                    Point straight = straighten( delta_w, delta_h);
                    delta_w = straight.x;
                    delta_h = straight.y;
                }


                if (!firstXORDraw) {
                    gg.drawLine( newObjectOrigin.x, newObjectOrigin.y, newObjectOrigin.x+delta_w,newObjectOrigin.y+delta_h);
                }

                delta_w = evtX-newObjectOrigin.x;  //gridMultiple(evt.getX()-newObjectOrigin.x);
                delta_h = evtY-newObjectOrigin.y;  //gridMultiple(evt.getY()-newObjectOrigin.y);


                if ( ( evt.getModifiers() & evt.SHIFT_MASK)  != 0) {
                    Point straight = straighten( delta_w, delta_h);
                    delta_w = straight.x;
                    delta_h = straight.y;
                }


/*                if ( ( evt.getModifiers() & evt.SHIFT_MASK)  != 0) {
                  if (( delta_w + delta_h ) > 0 ) {
                      double cos =  Math.PI * delta_w /  Math.sqrt(  (delta_w* delta_w) + (delta_h*delta_h) );
                      logOnConsole( "Cosinus: " + cos + "\n");

                      if ( Math.abs( cos ) >  Math.abs( Math.cos(0.25 * Math.PI ) ) ) {
                          delta_h = 0 ;
                      }
                  }
                }
 */
                gg.drawLine( newObjectOrigin.x, newObjectOrigin.y, newObjectOrigin.x+delta_w,newObjectOrigin.y+delta_h);
                mouse.x = newObjectOrigin.x+delta_w;
                mouse.y = newObjectOrigin.x+delta_y;

            } else if (newObjectType == ReportElementType.ELLIPSE_ELEMENT) {
                if (!firstXORDraw)  gg.drawOval( newObjectOrigin.x+delta_x, snapToGridVertically(newObjectOrigin.y+delta_y), Math.abs(delta_w), Math.abs(delta_h));

                //delta_w = gridMultiple(evt.getX()-newObjectOrigin.x);
                //delta_h = gridMultiple(evt.getY()-newObjectOrigin.y);
                delta_w = evtX-newObjectOrigin.x;  //gridMultiple(evt.getX()-newObjectOrigin.x);
                delta_h = evtY-newObjectOrigin.y;  //gridMultiple(evt.getY()-newObjectOrigin.y);

                delta_x = (delta_w<0) ? delta_w: 0;
                delta_y = (delta_h<0) ? delta_h : 0;
                gg.drawOval( newObjectOrigin.x+delta_x, snapToGridVertically(newObjectOrigin.y+delta_y), Math.abs(delta_w), Math.abs(delta_h));
            } else if (newObjectType == ReportElementType.ROUND_RECTANGLE_ELEMENT) {
                if (!firstXORDraw) {
                    gg.drawRoundRect( newObjectOrigin.x+delta_x, snapToGridVertically(newObjectOrigin.y+delta_y), Math.abs(delta_w), Math.abs(delta_h),20,20);
                }
                //delta_w = gridMultiple(evt.getX()-newObjectOrigin.x);
                //delta_h = gridMultiple(evt.getY()-newObjectOrigin.y);
                delta_w = evtX-newObjectOrigin.x;  //gridMultiple(evt.getX()-newObjectOrigin.x);
                delta_h = evtY-newObjectOrigin.y;  //gridMultiple(evt.getY()-newObjectOrigin.y);
                delta_x = (delta_w<0) ? delta_w: 0;
                delta_y = (delta_h<0) ? delta_h : 0;
                //gg.drawRect( snapToGridOrizzontally(newObjectOrigin.x+w_delta_x), snapToGridVertically(newObjectOrigin.y+delta_y), gridMultiple(Math.abs(evt.getX()-newObjectOrigin.x)), gridMultiple(Math.abs(evt.getY()-newObjectOrigin.y)));
                gg.drawRoundRect( newObjectOrigin.x+delta_x, snapToGridVertically(newObjectOrigin.y+delta_y), Math.abs(delta_w), Math.abs(delta_h),20,20);
            } else {
                if (!firstXORDraw) {
                    gg.drawRect( newObjectOrigin.x+delta_x, snapToGridVertically(newObjectOrigin.y+delta_y), Math.abs(delta_w), Math.abs(delta_h));
                }
                //delta_w = gridMultiple(evt.getX()-newObjectOrigin.x);
                //delta_h = gridMultiple(evt.getY()-newObjectOrigin.y);
                delta_w = evtX-newObjectOrigin.x;  //gridMultiple(evt.getX()-newObjectOrigin.x);
                delta_h = evtY-newObjectOrigin.y;  //gridMultiple(evt.getY()-newObjectOrigin.y);
                delta_x = (delta_w<0) ? delta_w: 0;
                delta_y = (delta_h<0) ? delta_h : 0;
                //gg.drawRect( snapToGridOrizzontally(newObjectOrigin.x+w_delta_x), snapToGridVertically(newObjectOrigin.y+delta_y), gridMultiple(Math.abs(evt.getX()-newObjectOrigin.x)), gridMultiple(Math.abs(evt.getY()-newObjectOrigin.y)));
                gg.drawRect( newObjectOrigin.x+delta_x, snapToGridVertically(newObjectOrigin.y+delta_y), Math.abs(delta_w), Math.abs(delta_h));
                //gg.drawRect( newObjectOrigin.x+delta_x, newObjectOrigin.y+delta_y, Math.abs(delta_from_origin), Math.abs(evt.getY()-newObjectOrigin.y));
            }
            firstXORDraw = false;
            
            //mouse.x = mouse.x + delta_from_origin;
            //mouse.y = evt.getY();
            //return;
        }
        if (selectedElements.size() >0 && newObjectType == ReportElementType.NONE && transformation_origin_end != null) {
            int new_transformation_origin_end_x = transformation_origin_end.x;
            int new_transformation_origin_end_y = transformation_origin_end.y;

            if (transformation_type >=0 && trasforming) {
                Graphics gg = jPanelReport.getGraphics();
                
                gg.setXORMode(Color.WHITE);
                int iteration = 0;
                
                if (!firstXORDrawTransforming) {
                    // redraw old rectangles...
                    // transformation_origin
                    // transformation_origin_end
                    // Pain a rectangle....

                    // if no modifications is needed, return...

                    //if (this.isSnapToGrid()) {

                        new_transformation_origin_end_x = evt.getX();
                        new_transformation_origin_end_y = evt.getY();

                        // New code for snap to grid....
                        new_transformation_origin_end_x = optimizeX(evt.getX(), transformation_origin.x, transformation_type, (ReportElement)getSelectedElements().elementAt(0));
                        new_transformation_origin_end_y = optimizeY(evt.getY(), transformation_origin.y, transformation_type, (ReportElement)getSelectedElements().elementAt(0));
                        /*
                        if (Math.abs( transformation_origin_end.x - evt.getX())%(getGridSize()*getZoomFactor())  == 0)
                            new_transformation_origin_end_x = evt.getX();

                        if (Math.abs( transformation_origin_end.y - evt.getY() )%(getGridSize()*getZoomFactor())  == 0)
                            new_transformation_origin_end_y = evt.getY();
                        */

                        if (new_transformation_origin_end_x == transformation_origin_end.x &&
                                new_transformation_origin_end_y == transformation_origin_end.y) {
                            return;
                        }

                    //} else {
                    //    new_transformation_origin_end_x = evt.getX();
                    //    new_transformation_origin_end_y = evt.getY();
                    //}

                    Enumeration e = getSelectedElements().elements();
                    
                    while (e.hasMoreElements()) {
                        iteration++;
                        ReportElement re = (ReportElement)e.nextElement();
                        Rectangle bounds = new Rectangle(getZoomedDim(re.getPosition().x-10)+10,
                                getZoomedDim(re.getPosition().y-10)+10,
                                getZoomedDim(re.getWidth()),
                                getZoomedDim(re.getHeight()));
                        // Scale rectangle...
                        if (transformation_type == TransformationType.TRANSFORMATION_MOVE) {
                            // First of all we must see if the resistence was Exceeded...
                            if (resistenceExceeded) {
                                // Change location...
                                bounds.translate(  transformation_origin_end.x-transformation_origin.x,
                                        transformation_origin_end.y-transformation_origin.y);
                                gg.drawRect(bounds.x-jHorizontalScrollBar.getValue(), bounds.y-jVerticalScrollBar.getValue(),bounds.width, bounds.height );
                                if (iteration == 1)
                                {
                                    redrawReferenceGuides(gg);
                                }
                            }
                        } else if (transformation_type == TransformationType.TRANSFORMATION_RESIZE_SE ||
                                transformation_type == TransformationType.TRANSFORMATION_RESIZE_S ||
                                transformation_type == TransformationType.TRANSFORMATION_RESIZE_E) {
                            // Change location...
                            int x_delta = ( transformation_type == TransformationType.TRANSFORMATION_RESIZE_SE || transformation_type == TransformationType.TRANSFORMATION_RESIZE_E) ? transformation_origin_end.x-transformation_origin.x : 0;
                            int y_delta = ( transformation_type == TransformationType.TRANSFORMATION_RESIZE_SE || transformation_type == TransformationType.TRANSFORMATION_RESIZE_S) ? transformation_origin_end.y-transformation_origin.y : 0;
                            bounds.setSize( Math.max(0, bounds.width + x_delta),
                                    Math.max(0, bounds.height + y_delta));
                            gg.drawRect(bounds.x-jHorizontalScrollBar.getValue(), bounds.y-jVerticalScrollBar.getValue(),bounds.width, bounds.height );
                            if (iteration == 1)
                                {
                                    redrawReferenceGuides(gg);
                                }
                        } else if (transformation_type == TransformationType.TRANSFORMATION_RESIZE_NW ||
                                transformation_type == TransformationType.TRANSFORMATION_RESIZE_N  ||
                                transformation_type == TransformationType.TRANSFORMATION_RESIZE_W) {
                            // Change location...
                            int x_delta = ( transformation_type == TransformationType.TRANSFORMATION_RESIZE_NW || transformation_type == TransformationType.TRANSFORMATION_RESIZE_W) ? transformation_origin_end.x-transformation_origin.x : 0;
                            int y_delta = ( transformation_type == TransformationType.TRANSFORMATION_RESIZE_NW || transformation_type == TransformationType.TRANSFORMATION_RESIZE_N) ? transformation_origin_end.y-transformation_origin.y : 0;
                            int height_grow = Math.min( y_delta,bounds.height);
                            int width_grow = Math.min( x_delta,bounds.width);
                            bounds.translate(width_grow,height_grow);
                            bounds.setSize( bounds.width - width_grow, bounds.height - height_grow);
                            gg.drawRect(bounds.x-jHorizontalScrollBar.getValue(), bounds.y-jVerticalScrollBar.getValue(),bounds.width, bounds.height );
                            if (iteration == 1)
                                {
                                    redrawReferenceGuides(gg);
                                }
                        } else if (transformation_type == TransformationType.TRANSFORMATION_RESIZE_NE) {
                            // Change location...
                            int x_delta = Math.max(-bounds.width,(transformation_origin_end.x-transformation_origin.x));
                            int y_delta = Math.min(bounds.height,  (transformation_origin_end.y-transformation_origin.y) );
                            bounds.y += y_delta;
                            bounds.height -= y_delta;
                            bounds.width += x_delta;
                            gg.drawRect(bounds.x-jHorizontalScrollBar.getValue(), bounds.y-jVerticalScrollBar.getValue(),bounds.width, bounds.height );
                            if (iteration == 1)
                                {
                                    redrawReferenceGuides(gg);
                                }
                        } else if (transformation_type == TransformationType.TRANSFORMATION_RESIZE_SW) {
                            // Change location...
                            int x_delta = Math.min(bounds.width,  (transformation_origin_end.x-transformation_origin.x) );
                            int y_delta = Math.max(-bounds.height,(transformation_origin_end.y-transformation_origin.y));
                            bounds.x += x_delta;
                            bounds.width -= x_delta;
                            bounds.height += y_delta;
                            gg.drawRect(bounds.x-jHorizontalScrollBar.getValue(), bounds.y-jVerticalScrollBar.getValue(),bounds.width, bounds.height );
                            if (iteration == 1)
                                {
                                    redrawReferenceGuides(gg);
                                }
                        }
                    }
                } 
                
                
                

                    /*
                    if (Math.abs( transformation_origin_end.x - evt.getX())%(getGridSize()*getZoomFactor())  == 0)
                        transformation_origin_end.x = evt.getX();
                    if (Math.abs( transformation_origin_end.y - evt.getY() )%(getGridSize()*getZoomFactor())  == 0)
                        transformation_origin_end.y = evt.getY();
                     */
                transformation_origin_end.x = new_transformation_origin_end_x;
                transformation_origin_end.y = new_transformation_origin_end_y;

                Enumeration e = getSelectedElements().elements();
                Rectangle firsElementBound = null;

                iteration = 0;
                while (e.hasMoreElements()) {
                    iteration++;
                    ReportElement re = (ReportElement)e.nextElement();
                    Rectangle bounds = new Rectangle(getZoomedDim(re.getPosition().x-10)+10-jHorizontalScrollBar.getValue(),
                            getZoomedDim(re.getPosition().y-10)+10-jVerticalScrollBar.getValue(),
                            getZoomedDim(re.getWidth()),
                            getZoomedDim(re.getHeight()));
                    // Scale rectangle...
                    if (transformation_type == TransformationType.TRANSFORMATION_MOVE) {
                        // Change location...
                        if (Math.abs(transformation_origin_end.x-transformation_origin.x) >5 ||
                                Math.abs(transformation_origin_end.y-transformation_origin.y) >5)
                        {
                            resistenceExceeded = true;
                        }
                        
                        if (resistenceExceeded) {
                            // Search the closest x,y that match a grid intersection...

                            int pex = gridMultiple( transformation_origin_end.x-10 )+10;
                            int pey = gridMultiple( transformation_origin_end.y-10 )+10;
                            int pox = gridMultiple( transformation_origin.x-10 )+10;
                            int poy = gridMultiple( transformation_origin.y-10 )+10;

                            //System.out.println("x: "+transformation_origin_end + " y:" + transformation_origin_end.y);

                            //this.getGraphics().drawOval( grid_multiple_x, grid_multiple_y, 5,5);

                            bounds.translate( transformation_origin_end.x-transformation_origin.x,
                                    transformation_origin_end.y-transformation_origin.y);
                            gg.drawRect(bounds.x, bounds.y,bounds.width, bounds.height );
                            if (iteration == 1)
                            {
                                paintedAlignLines = getAlignMatches(bounds);
                                redrawReferenceGuides(gg);
                            }
                        }
                    } else if (transformation_type == TransformationType.TRANSFORMATION_RESIZE_SE ||
                            transformation_type == TransformationType.TRANSFORMATION_RESIZE_S ||
                            transformation_type == TransformationType.TRANSFORMATION_RESIZE_E) {
                        // Change location...
                        int x_delta = ( transformation_type == TransformationType.TRANSFORMATION_RESIZE_SE || transformation_type == TransformationType.TRANSFORMATION_RESIZE_E) ? transformation_origin_end.x-transformation_origin.x : 0;
                        int y_delta = ( transformation_type == TransformationType.TRANSFORMATION_RESIZE_SE || transformation_type == TransformationType.TRANSFORMATION_RESIZE_S) ? transformation_origin_end.y-transformation_origin.y : 0;
                        bounds.setSize( Math.max(0, bounds.width + x_delta),
                                Math.max(0, bounds.height + y_delta));
                        gg.drawRect(bounds.x, bounds.y,bounds.width, bounds.height );
                        if (iteration == 1)
                            {
                            paintedAlignLines = getAlignMatches(bounds);
                                redrawReferenceGuides(gg);
                            }
                    } else if (transformation_type == TransformationType.TRANSFORMATION_RESIZE_NW ||
                            transformation_type == TransformationType.TRANSFORMATION_RESIZE_N  ||
                            transformation_type == TransformationType.TRANSFORMATION_RESIZE_W) {
                        // Change location...
                        int x_delta = ( transformation_type == TransformationType.TRANSFORMATION_RESIZE_NW || transformation_type == TransformationType.TRANSFORMATION_RESIZE_W) ? transformation_origin_end.x-transformation_origin.x : 0;
                        int y_delta = ( transformation_type == TransformationType.TRANSFORMATION_RESIZE_NW || transformation_type == TransformationType.TRANSFORMATION_RESIZE_N) ? transformation_origin_end.y-transformation_origin.y : 0;
                        int height_grow = Math.min( y_delta,bounds.height);
                        int width_grow = Math.min( x_delta,bounds.width);
                        bounds.translate(width_grow,height_grow);
                        bounds.setSize( bounds.width - width_grow, bounds.height - height_grow);
                        gg.drawRect(bounds.x, bounds.y,bounds.width, bounds.height );
                        if (iteration == 1)
                            {
                            paintedAlignLines = getAlignMatches(bounds);
                                redrawReferenceGuides(gg);
                            }
                    } else if (transformation_type == TransformationType.TRANSFORMATION_RESIZE_NE) {
                        // Change location...
                        int x_delta = Math.max(-bounds.width,(transformation_origin_end.x-transformation_origin.x));
                        int y_delta = Math.min(bounds.height, (transformation_origin_end.y-transformation_origin.y) );
                        bounds.y += y_delta;
                        bounds.height -= y_delta;
                        bounds.width += x_delta;
                        gg.drawRect(bounds.x, bounds.y,bounds.width, bounds.height );
                        if (iteration == 1)
                            {
                            paintedAlignLines = getAlignMatches(bounds);
                                redrawReferenceGuides(gg);
                            }
                    } else if (transformation_type == TransformationType.TRANSFORMATION_RESIZE_SW) {
                        // Change location...
                        int x_delta = Math.min(bounds.width, (transformation_origin_end.x-transformation_origin.x) );
                        int y_delta = Math.max(-bounds.height , (transformation_origin_end.y-transformation_origin.y));
                        bounds.x += x_delta;
                        bounds.width -= x_delta;
                        bounds.height += y_delta;
                        gg.drawRect(bounds.x, bounds.y,bounds.width, bounds.height );
                        if (iteration == 1)
                            {
                            paintedAlignLines = getAlignMatches(bounds);
                                redrawReferenceGuides(gg);
                            }
                    }
                    
                    firsElementBound = bounds;
                }

                firstXORDrawTransforming = false;
                
                // Check if we are aligned with other objects....
                
                gg.setPaintMode();
            }
        }
        mouse.x = evt.getX();
        mouse.y = evt.getY();

    }//GEN-LAST:event_jPanelReportMouseDragged

    private void jPanelReportMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelReportMouseMoved
        this.jHorizontalRule.setCursorPosition(evt.getX());
        this.jVerticalRule.setCursorPosition(evt.getY());
        if (mouse == null) mouse = new Point();
        if (newObjectType == ReportElementType.NONE && !transforming) {
            // Is the mouse over a band line ?...
            int bl =  evt.getY() + jVerticalScrollBar.getValue();
            Enumeration e = report.getBands().elements();
            int y= report.getTopMargin();
            boolean found=false;
            while (!found && e.hasMoreElements()) {
                Band band = (Band)e.nextElement();
                y += band.getHeight();
                if (bl>getZoomedDim(y)+10-3 && bl < getZoomedDim(y)+3+10) {
                    jPanelReport.setCursor( hsplit);
                    //this.label1.setText("Drag "+band.name);
                    found = true;
                }
            }
            if (!found && jPanelReport.getCursor() == hsplit) {
                jPanelReport.setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        }

        if (selectedElements.size() >0 && newObjectType == ReportElementType.NONE) {
            // We are trasformation mode?
            {
                // Vediamo se siamo sopra la figura...
                Enumeration e = selectedElements.elements();
                boolean found = false;
                boolean sizeall = false;
                while (!found && e.hasMoreElements() ) {

                    ReportElement selectedRe = (ReportElement)e.nextElement();
                    Rectangle grip = new Rectangle(getZoomedDim(selectedRe.position.x-10)-5+10, getZoomedDim(selectedRe.position.y-10)-5+10,
                            getZoomedDim(selectedRe.width)+10,getZoomedDim(selectedRe.height)+10);
                    Rectangle cursor = new Rectangle(evt.getX()+jHorizontalScrollBar.getValue(),
                            evt.getY()+jVerticalScrollBar.getValue(),
                            1,1);

                    if (cursor.intersects(grip)) {
                        sizeall = true;
                        // Vediamo se interseca una grip...
                        grip.width  = 5;
                        grip.height = 5;

                        if (cursor.intersects(grip)) {
                            jPanelReport.setCursor(Cursor.getPredefinedCursor( Cursor.NW_RESIZE_CURSOR ));
                            found =true;
                            transformation_type = TransformationType.TRANSFORMATION_RESIZE_NW;
                        }

                        if (!found) {
                            grip.x = getZoomedDim(selectedRe.position.x-10+selectedRe.width)+10;
                            grip.y = getZoomedDim(selectedRe.position.y-10)+10-5;
                            grip.width  = 5;
                            grip.height = 5;
                            if (cursor.intersects(grip)) {
                                jPanelReport.setCursor(Cursor.getPredefinedCursor( Cursor.NE_RESIZE_CURSOR ));
                                found =true;
                                transformation_type = TransformationType.TRANSFORMATION_RESIZE_NE;
                            }
                        }

                        if (!found) {
                            grip.x = getZoomedDim(selectedRe.position.x-10)+10-5;
                            grip.y = getZoomedDim(selectedRe.position.y+(selectedRe.height/2)-10)+10-2;
                            grip.width  = 5;
                            grip.height = 5;
                            if (cursor.intersects(grip)) {
                                jPanelReport.setCursor(Cursor.getPredefinedCursor( Cursor.W_RESIZE_CURSOR));
                                found =true;
                                transformation_type = TransformationType.TRANSFORMATION_RESIZE_W;
                            }
                        }

                        if (!found) {
                            grip.x = getZoomedDim(selectedRe.position.x-10)+10-5;
                            grip.y = getZoomedDim(selectedRe.position.y-10+selectedRe.height)+10;
                            grip.width  = 5;
                            grip.height = 5;
                            if (cursor.intersects(grip)) {
                                jPanelReport.setCursor(Cursor.getPredefinedCursor( Cursor.SW_RESIZE_CURSOR));
                                found =true;
                                transformation_type = TransformationType.TRANSFORMATION_RESIZE_SW;
                            }
                        }

                        if (!found) {
                            grip.x = getZoomedDim(selectedRe.position.x+(selectedRe.width/2)-10)+10-2;
                            grip.y = getZoomedDim(selectedRe.position.y-10)+10-5;
                            grip.width  = 5;
                            grip.height = 5;
                            if (cursor.intersects(grip)) {
                                jPanelReport.setCursor(Cursor.getPredefinedCursor( Cursor.N_RESIZE_CURSOR));
                                found =true;
                                transformation_type = TransformationType.TRANSFORMATION_RESIZE_N;
                            }
                        }

                        if (!found) {
                            grip.x = getZoomedDim(selectedRe.position.x+(selectedRe.width/2)-10)+10-2;
                            grip.y = getZoomedDim(selectedRe.position.y+selectedRe.height-10)+10;
                            grip.width  = 5;
                            grip.height = 5;
                            if (cursor.intersects(grip)) {
                                jPanelReport.setCursor(Cursor.getPredefinedCursor( Cursor.S_RESIZE_CURSOR));
                                found =true;
                                transformation_type = TransformationType.TRANSFORMATION_RESIZE_S;
                            }
                        }

                        if (!found) {
                            grip.x = getZoomedDim(selectedRe.position.x+selectedRe.width-10)+10;
                            grip.y = getZoomedDim(selectedRe.position.y+selectedRe.height-10)+10;
                            grip.width  = 5;
                            grip.height = 5;
                            if (cursor.intersects(grip)) {
                                jPanelReport.setCursor(Cursor.getPredefinedCursor( Cursor.SE_RESIZE_CURSOR));
                                found =true;
                                transformation_type = TransformationType.TRANSFORMATION_RESIZE_SE;
                            }
                        }

                        if (!found) {
                            grip.x = getZoomedDim(selectedRe.position.x+selectedRe.width-10)+10;
                            grip.y = getZoomedDim(selectedRe.position.y+(selectedRe.height/2)-10)+10-2;
                            grip.width  = 5;
                            grip.height = 5;
                            if (cursor.intersects(grip)) {
                                jPanelReport.setCursor(Cursor.getPredefinedCursor( Cursor.E_RESIZE_CURSOR));
                                found =true;
                                transformation_type = TransformationType.TRANSFORMATION_RESIZE_E;
                            }
                        }

                        if (!found) {
                            jPanelReport.setCursor(Cursor.getPredefinedCursor( Cursor.MOVE_CURSOR));

                            if (getMainFrame().isEMMActive())
                                transformation_type = TransformationType.TRANSFORMATION_NONE;
                            else {
                                transformation_type = TransformationType.TRANSFORMATION_MOVE;
                                resistenceExceeded = false;
                            }
                        }
                    }
                }
                if (!sizeall) {
                    jPanelReport.setCursor(Cursor.getDefaultCursor());
                    transformation_type = TransformationType.TRANSFORMATION_NONE;
                }
            }
        }

        if (getMainFrame().getProperties().getProperty("ShowToolTipsInDesign","false").equals("true"))
        {
            Band bbb = getReport().getBandByPosition( getLogicalDim(evt.getY()+jVerticalScrollBar.getValue()-10)+10);
            if ( ((ReportPanelToolTip)getReportPanel().createToolTip()).getBand() == null ||
                 ((ReportPanelToolTip)getReportPanel().createToolTip()).getBand() != bbb )
            {
                ((ReportPanelToolTip)getReportPanel().createToolTip()).setBand(bbb);
                 getReportPanel().setToolTipText( ((ReportPanelToolTip)getReportPanel().createToolTip()).getTipText());
            }
        }
        else
        {
            getReportPanel().setToolTipText( null );
        }

        mouse.x = evt.getX();
        mouse.y = evt.getY();



    }//GEN-LAST:event_jPanelReportMouseMoved

    /* Unuseful...
    public String getToolTipByMousePosition(Point position)
    {
        Point p_to_intersect = new Point(getLogicalDim(position.x+jHorizontalScrollBar.getValue()-10)+10,getLogicalDim(position.y+jVerticalScrollBar.getValue()-10)+10);
        for (int i=report.getElements().size()-1; i>=0; --i) {
            ReportElement re = (ReportElement)report.getElements().elementAt(i);
            if (re.intersects(p_to_intersect)) {
               return "Element: " + re.getName()+"\nBand: " + re.getBand().getName();
            }
        }

        Band bbb = getReport().getBandByPosition( getLogicalDim(position.y+jVerticalScrollBar.getValue()-10) );
        if (bbb != null)
        {
             return "Band: " + bbb.getName();
        }
        return null;
    }
    */
    private void jPanelReportAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jPanelReportAncestorAdded
        // Add your handling code here:
    }//GEN-LAST:event_jPanelReportAncestorAdded

    /** Getter for property report.
     * @return Value of property report.
     *
     */
    public it.businesslogic.ireport.Report getReport() {
        return report;
    }


    /** Getter for property selectedElements.
     * @return Value of property selectedElements.
     *
     */
    public java.util.Vector getSelectedElements() {
        return selectedElements;
    }

    /** Setter for property selectedElements.
     * @param selectedElements New value of property selectedElements.
     *
     */
    public void setSelectedElements(java.util.Vector selectedElements) {
        this.selectedElements = selectedElements;
    }

    /** Getter for property clipboardObjects.
     * @return Value of property clipboardObjects.
     *
     */
    public java.util.Vector getClipboardObjects() {
        return clipboardObjects;
    }

    /** Setter for property clipboardObjects.
     * @param clipboardObjects New value of property clipboardObjects.
     *
     */
    public void setClipboardObjects(java.util.Vector clipboardObjects) {
        this.clipboardObjects = clipboardObjects;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private it.businesslogic.ireport.gui.JRulePanel jHorizontalRule;
    private javax.swing.JScrollBar jHorizontalScrollBar;
    private javax.swing.JMenuItem jMenuItemBandProperties;
    private javax.swing.JMenuItem jMenuItemBandProperties1;
    private javax.swing.JMenuItem jMenuItemCopy;
    private javax.swing.JMenuItem jMenuItemCopyStyle;
    private javax.swing.JMenuItem jMenuItemCut;
    private javax.swing.JMenuItem jMenuItemDelete;
    private javax.swing.JMenuItem jMenuItemElementChartProperties;
    private javax.swing.JMenuItem jMenuItemElementCrosstabDesignProperties;
    private javax.swing.JMenuItem jMenuItemElementCrosstabProperties;
    private javax.swing.JMenuItem jMenuItemElementOpenSubreport;
    private javax.swing.JMenuItem jMenuItemElementProperties;
    private javax.swing.JMenuItem jMenuItemGroup;
    private javax.swing.JMenuItem jMenuItemPaste;
    private javax.swing.JMenuItem jMenuItemPasteOnBand;
    private javax.swing.JMenuItem jMenuItemPasteStyle;
    private javax.swing.JMenuItem jMenuItemPattern;
    private javax.swing.JMenuItem jMenuItemTransformStaticText;
    private javax.swing.JMenuItem jMenuItemUngroup;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelCorner;
    private javax.swing.JPanel jPanelHContainerRule;
    private javax.swing.JPanel jPanelHScroll;
    private it.businesslogic.ireport.gui.JReportPanel jPanelReport;
    private javax.swing.JPanel jPanelReportContainer;
    private javax.swing.JPanel jPanelSuperContainer;
    private javax.swing.JPanel jPanelVRule;
    private javax.swing.JPopupMenu jPopupMenuBand;
    private javax.swing.JPopupMenu jPopupMenuElement;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JScrollBar jVerticalScrollBar;
    // End of variables declaration//GEN-END:variables

    private Report report;

    private int undoIndex = -1;
    private Vector undoOperations;
    private Vector clipboardObjects;
    private Vector selectedElements;

    /**
     *  Zoom factor used in this frame...
     */
    private double zoomFactor;

    /**
     *  Actual mouse position...
     */
    private Point mouse = null;

    private boolean lensMode;

    private int newObjectType;

    private boolean showGrid;

    private int transformationMinSize;

    private boolean snapToGrid;

    private int windowID;

    /** Utility field used by event firing mechanism. */
    private javax.swing.event.EventListenerList listenerList =  null;

    public JReportPanel getReportPanel() {
        return this.jPanelReport;
    }
    public void drawVoidDoc(Graphics2D g) {
        // Paint the background.
        g.setColor(new Color(128, 128, 128));
        // Fill a very large rectangle. Clipping will only paint what is
        //   necessary.
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

 //iR20       if (background != null)
 //iR20       {
 //iR20           g.drawImage(background.getImage(), 0,0, this);
 //iR20       }
        
        // Due to optimize document drawing, we use a cache. Redraw void doc only if it+
        // is dirty...
        /*
        if (!isDocDirty)
        {
            return;
        }
         */

        //g.clearRect(0,0,offscreenImageDoc.getWidth(), offscreenImageDoc.getHeight());
        int horizontal_scroll = jHorizontalScrollBar.getValue();
        int vertical_scroll = jVerticalScrollBar.getValue();
        int zoomed_width = getZoomedDim(report.getWidth());
        int design_height = report.getDesignHeight();
        int zoomed_height = getZoomedDim(design_height);


        g.setColor(Color.WHITE);
        g.fillRect( 10- horizontal_scroll,10-vertical_scroll,zoomed_width , zoomed_height);
        g.setColor(Color.BLACK);
        g.drawRect( 9-horizontal_scroll, 9-vertical_scroll, zoomed_width,zoomed_height );

        // Paint shadow corner top-right
        g.setPaint( new TexturePaint( shadowsImages[0], new Rectangle2D.Double( zoomed_width+10-horizontal_scroll,9-vertical_scroll,9,9)));
        g.fillRect(zoomed_width+10-horizontal_scroll,9-vertical_scroll,9,9);

        // Paint shadow corner top-right
        g.setPaint( new TexturePaint( shadowsImages[1], new Rectangle2D.Double( zoomed_width+10-horizontal_scroll,18-vertical_scroll,9,9)));
        g.fillRect( zoomed_width+10-horizontal_scroll,18-vertical_scroll, 9,zoomed_height-8);

        g.setPaint(new TexturePaint( shadowsImages[2], new Rectangle2D.Double( zoomed_width+10-horizontal_scroll,zoomed_height+10-vertical_scroll,9,9)));
        g.fillRect( zoomed_width+10-horizontal_scroll,zoomed_height+10-vertical_scroll,9,9);

        g.setPaint(new TexturePaint( shadowsImages[3], new Rectangle2D.Double( 9-horizontal_scroll,zoomed_height+10-vertical_scroll, zoomed_width+2,9)));
        g.fillRect(  9-horizontal_scroll,zoomed_height+10-vertical_scroll, zoomed_width+1,9);

        g.setPaint(new TexturePaint( shadowsImages[4], new Rectangle2D.Double( 9-horizontal_scroll,zoomed_height+10-vertical_scroll,9,9)));
        g.fillRect(   9-horizontal_scroll,zoomed_height+10-vertical_scroll,9,9);

        // Draw grid...

        g.setColor(new Color(230,230,230));
        if (this.isShowGrid()) {
            /*

            //g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT,
            //                     BasicStroke.JOIN_MITER, 1,
            //                     dashPattern, 0));
            float[] fArray = {2f,2f};
            BasicStroke m_Dashed3 = new BasicStroke(1, BasicStroke.CAP_SQUARE,
                    BasicStroke.JOIN_BEVEL, 1.0f, fArray, 0.0f);
            g.setStroke(m_Dashed3);

            int zoomed_grid_size = getZoomedDim(this.gridSize);
            int grid_left = 10+ zoomed_grid_size - horizontal_scroll;
            int line_top = 10-vertical_scroll+1;
            int line_bottom = getZoomedDim(design_height)+10-vertical_scroll-2;

            if (zoomed_grid_size>2) {
                int i=1;
                grid_left = 10 - horizontal_scroll + getZoomedDim(i*this.gridSize);

                while (grid_left < zoomed_width+10-horizontal_scroll) {
                    g.drawLine(grid_left, line_top, grid_left, line_bottom);
                    i++;
                    grid_left = 10 - horizontal_scroll + getZoomedDim(i*this.gridSize);
                }
            }

            int grid_top = 10 - vertical_scroll;
            int line_left = 10-horizontal_scroll+1;
            int line_right = zoomed_width+10-horizontal_scroll-1;
            if (zoomed_grid_size>2) {
                int i=1;
                grid_top = 10 - vertical_scroll + getZoomedDim(i*this.gridSize);
                while (grid_top < line_bottom ) {
                    g.drawLine(line_left,grid_top,line_right,grid_top);
                    i++;
                    grid_top = 10 - vertical_scroll + getZoomedDim(i*this.gridSize);
                }
            }


             */
             Stroke defaultStroke = g.getStroke();
            float[] fArray = {2f,2f};
            BasicStroke m_Dashed3 = new BasicStroke(1,
            BasicStroke.CAP_SQUARE,
            BasicStroke.JOIN_BEVEL, 1.0f, fArray, 0.0f);
            g.setStroke(m_Dashed3);

            int zoomed_grid_size = getZoomedDim(this.getGridSize());

            // mGridTexture is a member of type TexturePaint
            if ( mGridTexture == null ||
            mGridTexture.getImage().getWidth() != zoomed_grid_size ) {
            BufferedImage img = new BufferedImage(
            zoomed_grid_size, zoomed_grid_size,
            BufferedImage.TYPE_INT_RGB );

            Graphics2D g2 = img.createGraphics();

            g2.setColor(Color.WHITE);
            g2.fillRect( 0, 0, zoomed_grid_size,
            zoomed_grid_size );

            g2.setColor( new Color(230,230,230) );
            g2.setStroke( m_Dashed3 );

            g2.drawLine( zoomed_grid_size-1, 0,
            zoomed_grid_size-1, zoomed_grid_size-1 );

            g2.drawLine( 0, zoomed_grid_size-1,
            zoomed_grid_size-1, zoomed_grid_size-1 );

            mGridTexture = new TexturePaint( img, new
            Rectangle( 10, 10, zoomed_grid_size, zoomed_grid_size ) );

            } // if

            Paint oldPaint = g.getPaint();
            g.setPaint( mGridTexture );

            g.translate( -horizontal_scroll, -vertical_scroll);

            g.fillRect( 10, 10, zoomed_width-1, zoomed_height-1 );

            g.translate( horizontal_scroll, vertical_scroll );

            g.setPaint(oldPaint);
            g.setStroke(defaultStroke);
        }



        // Draw document structure lines...
        g.setColor(new Color(170,170,255));
        // LEFT MARGINE LINE
        g.drawLine( getZoomedDim(report.getLeftMargin())+10-horizontal_scroll,
                10-vertical_scroll,
                getZoomedDim(report.getLeftMargin())+10-horizontal_scroll,
                zoomed_height+10-vertical_scroll-2);

        // RIGHT MARGIN LINE
        g.drawLine(getZoomedDim(report.getWidth()- report.getRightMargin())+10-horizontal_scroll,
                10-vertical_scroll,
                getZoomedDim(report.getWidth()-report.getRightMargin())+10-horizontal_scroll,
                zoomed_height+10-vertical_scroll-2);

        g.drawLine( 10-horizontal_scroll,
                getZoomedDim(report.getTopMargin())+10-vertical_scroll,
                zoomed_width+10-horizontal_scroll-2,
                getZoomedDim(report.getTopMargin())+10-vertical_scroll);

        g.drawLine( 10-horizontal_scroll,
                getZoomedDim(design_height-report.getBottomMargin())+10-vertical_scroll,
                zoomed_width+10-horizontal_scroll-2,
                getZoomedDim(design_height- report.getBottomMargin())+10-vertical_scroll);

        int y=report.getTopMargin();

        Band page_header=null;
        Band page_footer=null;

        Enumeration e = report.getBands().elements();
        while (e.hasMoreElements()) {
            g.setColor(new Color(170,170,170));
            Band band = (Band)e.nextElement();
            y += band.getHeight();
            g.drawLine( 10-horizontal_scroll,
                    getZoomedDim(y)+10-vertical_scroll,
                    zoomed_width+10-horizontal_scroll-2,
                    getZoomedDim(y)+10-vertical_scroll);

            g.setFont( new Font( "Arial", Font.PLAIN, getZoomedDim(20)));
            // We must center the text..
            int txt_width = g.getFontMetrics().stringWidth(band.getName())/2;
            int txt_height = g.getFontMetrics().getHeight()/2;
            txt_height -= g.getFontMetrics().getMaxDescent();
            g.setColor(new Color(220,220,255));
                /*
                g.setClip(getZoomedDim(report.getLeftMargin())+10-horizontal_scroll,
                          getZoomedDim(y-band.getHeight())+10-vertical_scroll,
                          getZoomedDim(report.getWidth()),
                          getZoomedDim(band.getHeight()));
                 */
            //Rectangle orgClipBounds = g.getClipBounds();

            Java2DUtil.setClip(g,
                    getZoomedDim(report.getLeftMargin())+10-horizontal_scroll,
                    getZoomedDim(y-band.getHeight())+10-vertical_scroll,
                    getZoomedDim(report.getWidth()),
                    getZoomedDim(band.getHeight()));
               /*
                g.setClip(getZoomedDim(report.getLeftMargin())+10-horizontal_scroll,
                          getZoomedDim(y-band.getHeight())+10-vertical_scroll,
                          getZoomedDim(report.getWidth()),
                          getZoomedDim(band.getHeight()));
                */

            g.drawString( band.getName(),
                    getZoomedDim(report.getWidth()/2)+10-horizontal_scroll - txt_width,
                    getZoomedDim(y)+10-vertical_scroll +  txt_height - getZoomedDim(band.getHeight()/2) ); //, zoomed_width, getZoomedDim(band.getHeight()
            //g.drawLine(0, getZoomedDim(y)+10-vertical_scroll -  txt_height - getZoomedDim(band.getHeight()/2),  700,getZoomedDim(y)+10-vertical_scroll -  txt_height - getZoomedDim(band.getHeight()/2));
            //g.drawLine(0, getZoomedDim(y)+10-vertical_scroll +  txt_height - getZoomedDim(band.getHeight()/2),  700,getZoomedDim(y)+10-vertical_scroll +  txt_height - getZoomedDim(band.getHeight()/2));

            /* g.setClip(null); */

            //g.setClip(orgClipBounds);
            Java2DUtil.resetClip(g);

            if (band.getName().equals("pageHeader")) page_header = band;
            if (band.getName().equals("pageFooter")) page_footer = band;
        }

        // PAint columns...
        int y1 = report.getBandYLocation(report.getBandByName("columnHeader") );
        int y2 = report.getBandYLocation(page_footer);

        for (int i=0; i< report.getColumnCount()-1; ++i) {
            int x = 10+getZoomedDim(report.getLeftMargin()+report.getColumnWidth() + i*(report.getColumnWidth()+report.getColumnSpacing()));
            //if (i !=0 ) {
            //}
            if (x<10+zoomed_width)
                g.drawLine( x-horizontal_scroll, getZoomedDim(y1)+10-vertical_scroll,
                        x-horizontal_scroll, getZoomedDim(y2)+10-vertical_scroll);
            x = 10 + getZoomedDim(report.getLeftMargin() + report.getColumnWidth()+report.getColumnSpacing() + i *(report.getColumnWidth()+report.getColumnSpacing()));

            if (x<10+zoomed_width)
                g.drawLine( x-horizontal_scroll, getZoomedDim(y1)+10-vertical_scroll,
                        x-horizontal_scroll, getZoomedDim(y2)+10-vertical_scroll);
        }
         /*
                g.setPen(new Pen(new Color(170,170,255)));
                g.drawLine( 10,getZoomedDim(report.topMargin)+10, getZoomedDim(report.getPagesize().x)+10, getZoomedDim(report.topMargin)+10);
                g.drawLine( 10,getZoomedDim(report.getDesignHeight())- getZoomedDim(report.topMargin)+10,  getZoomedDim(report.getPagesize().x)+10,getZoomedDim(report.getDesignHeight())- getZoomedDim(report.topMargin)+10);
          */
        //	g.setPen(new Pen(Color.BLACK ));

        // Draw guide lines...
        Stroke oldStroke = g.getStroke();
        g.setStroke(jHorizontalRule.getDottedStroke());
        g.setColor(Color.BLACK);
        g.setXORMode(java.awt.Color.YELLOW);
        for (int i=0; i<jHorizontalRule.getGuideLines().size(); ++i)
        {
            Integer pos = (Integer)jHorizontalRule.getGuideLines().get(i);
            int posI = pos.intValue();
            // Calc posI....
            posI = 10 + (int)(posI*getZoomFactor()) - getHScroll();
            g.drawLine(posI, 0,posI,getHeight());
        }

        for (int i=0; i<jVerticalRule.getGuideLines().size(); ++i)
        {
            Integer pos = (Integer)jVerticalRule.getGuideLines().get(i);
            int posI = pos.intValue();
            // Calc posI....
            posI = 10 + (int)(posI*getZoomFactor()) - getVScroll();
            g.drawLine(0, posI, getWidth(),posI);
        }
        g.setPaintMode();
        g.setStroke(oldStroke);

        isDocDirty = false;
    }

    public int getReversedZoomedDim(int dim) {
        if (zoomFactor == 1.0) return dim;
        return (int)((double)dim/zoomFactor);
    }

    public int getZoomedDim(int dim) {
        if (zoomFactor == 1.0) return dim;
        //if (((double)dim*(double)zoom_factor)<0.5) return 1;
        // Truncate, don't round!!
        return (int)((double)dim*zoomFactor);
        //return (int)Math.ceil((double)dim*zoom_factor);
    }

    public int getLogicalDim(int dim) {
        if (zoomFactor == 1.0) return dim;
        //if (Math.abs(  ((double)dim/(double)zoom_factor)) < 1 &&
        //	Math.abs(  ((double)dim/(double)zoom_factor)) > 0) return 1;
        // Truncate, don't round!!
        return (int)((double)dim/zoomFactor);
        //return (int)Math.ceil((double)dim/zoom_factor);
    }

    /** Getter for property zoomFactor.
     * @return Value of property zoomFactor.
     *
     */
    public double getZoomFactor() {
        return zoomFactor;
    }

    /** Setter for property zoomFactor.
     * @param zoomFactor New value of property zoomFactor.
     *
     */
    public void setZoomFactor(double zoomFactor) {

        floatingTextAreaFocusLost();
        if (getSelectedCrosstabEditorPanel() != null)
        {
            getSelectedCrosstabEditorPanel().setZoomFactor(zoomFactor);
        }
        else
        {
            if (this.zoomFactor != zoomFactor) {
                this.zoomFactor = zoomFactor;
                this.jPanelReportComponentResized( new java.awt.event.ComponentEvent( jPanelReport,0));
                isDocDirty = true;
                this.getReportPanel().repaint();
                this.jHorizontalRule.repaint();
                this.jVerticalRule.repaint();
            }
        }
    }

    public void updateAntialiasMode()
    {
        this.getReportPanel().repaint();
    }

    public void paintReportPanel(Graphics g) {
                /*
                redrawAll(offscreen ,null, 0);
                g.drawImage( offscreenImage , 0,0, offscreenImage.getWidth(), offscreenImage.getHeight(), (ImageObserver)this);
                 */

        try {
        Graphics2D g2 = (Graphics2D) g;


        if (this.getMainFrame().getProperties().getProperty("Antialias","true").equals("false"))
        {

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_OFF);
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
                    RenderingHints.VALUE_STROKE_PURE);
            g2.setRenderingHint(RenderingHints.KEY_DITHERING,
                    RenderingHints.VALUE_DITHER_DISABLE);
        }
        else
        {

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
                    RenderingHints.VALUE_STROKE_DEFAULT);
            g2.setRenderingHint(RenderingHints.KEY_DITHERING,
                    RenderingHints.VALUE_DITHER_ENABLE);
        }

        //long start = System.currentTimeMillis();
        //int number = 1;
        //System.out.println("redrawAll: (clip=" + g2.getClipRect() + ")");
        //for(int i=0; i<number; i++)
        //{
        
        //System.out.println("redrawAll: (clip=" + g2.getClipRect() + ")");
        //g2.setClip(this.getWidth()-150, 0, 150, this.getHeight());
        redrawAll(g2, g2.getClipRect(), 0);
        //}
        //long stop = System.currentTimeMillis();
        //System.out.println(number + " paints took " + ((stop - start) / 1000.0) + " seconds");

        } catch (Throwable ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * Return the point in component coordinate as
     * point inside the report.  
     */
    public Point getLogicalPoint(Point p)
    {
        Point newP = new Point();
        newP.x = getReversedZoomedDim(p.x-10+jHorizontalScrollBar.getValue());
        newP.y = getReversedZoomedDim(p.y-10+jVerticalScrollBar.getValue());
        return newP;
    }
    
    /**
     * Return true if the point in component coordinate is inside the document...
     */
    public boolean isInsideDocument(Point p)
    {
        Point newP = getLogicalPoint(p);
        return (newP.x >=0 && newP.x < getReport().getWidth() &&
                newP.y >=0 && newP.y < getReport().getDesignHeight());
    }
    
//iR20    private ImageIcon background = new javax.swing.ImageIcon(ImageIcon.class.getResource("/it/businesslogic/ireport/gui/wp.jpg" ));
    
    private void redrawAll(Graphics2D g, Rectangle clipRect, int margin) {
       
        
        if (isRedrawWithBufferedImage())
        {
            g.setClip(clipRect);
            g.drawImage(offscreenImage,0,0,null);
            return;
        }
        
        
        //g.clearRect(0,0,offscreenImage.getWidth(), offscreenImage.getHeight());
        //System.out.println("redraw all "+clipRect);
        if (clipRect ==null) {
            clipRect = new Rectangle(0,0,jPanelReport.getWidth() ,this.getReport().getDesignHeight());
            //this.getReport().getWidth()
        }

        //clipRect.x += jHorizontalScrollBar.getValue();
        //clipRect.y += jVerticalScrollBar.getValue();
        //g.setClip(clipRect);

        g.clearRect(0,0, (int) clipRect.getWidth(),  (int) clipRect.getHeight());
        
/*
                                 = new Rectangle( getZoomedDim2(clipRect.x-10 - margin)+ margin +10 + jHorizontalScrollBar.getValue(),
                                        getZoomedDim2(clipRect.y-10 - margin)+ margin +10 + jVerticalScrollBar.getValue(),
                                        getZoomedDim2(clipRect.width-2*margin)+2*margin,
                                        getZoomedDim2(clipRect.height-2*margin)+2*margin);
 */
        Rectangle realRect = new Rectangle(
                getReversedZoomedDim(clipRect.x-10-margin+jHorizontalScrollBar.getValue())+ margin +10,
                getReversedZoomedDim(clipRect.y-10-margin+jVerticalScrollBar.getValue())+ margin +10,
                getReversedZoomedDim(clipRect.width-2*margin)+2*margin,
                getReversedZoomedDim(clipRect.height-2*margin)+2*margin);

                /*
realRect = new Rectangle(
  getZoomedDim2(clipRect.x + jHorizontalScrollBar.getValue()) + margin,
  getZoomedDim2(clipRect.y + jVerticalScrollBar.getValue()) + margin,
  getZoomedDim2(clipRect.width),
  getZoomedDim2(clipRect.height));
                 */

        //g.setPaint(Color.CYAN);
        //g.fillRect( 0 ,0,offscreenWidth ,offscreenDimension.height);
        //
        // 1. Draw the void document...
        //g.setClip( realRect );
        drawVoidDoc(g);

/*
                                g.drawImage( offscreenImageDoc , clipRect.x,clipRect.y, clipRect.x + clipRect.width, clipRect.y + clipRect.height,
                                                                 clipRect.x,clipRect.y, clipRect.x + clipRect.width, clipRect.y + clipRect.height, (ImageObserver)this);
 */
        //                              g.drawImage( offscreenImageDoc , 0,0, offscreenImageDoc.getWidth(), offscreenImageDoc.getHeight(), (ImageObserver)this);
        //g.setClip( realRect );
        // 2. Draw all Reportelements...
        Enumeration e = report.getElements().elements();
        while (e.hasMoreElements()) {
            ReportElement re = (ReportElement)e.nextElement();
            if (re.intersects(realRect) || re instanceof BreakReportElement)
                re.drawObject(g, getZoomFactor(), jHorizontalScrollBar.getValue(), jVerticalScrollBar.getValue());
        }

        // If there's a selected element, draw a corona
        if (selectedElements.size()>0) {
            //clipRect.x -=5;
            //clipRect.y -=5;
            //clipRect.width  +=10;
            //clipRect.height +=10;
            e = selectedElements.elements();
            boolean master=true;
            while (e.hasMoreElements()) {
                ReportElement re = (ReportElement)e.nextElement();
                if (re.intersects(realRect))
                    re.drawCorona( g,getZoomFactor(), jHorizontalScrollBar.getValue(), jVerticalScrollBar.getValue(), master);
                master = false;
            }
        }

        //g.setPaint(Color.CYAN);
        //g.fillRect(realRect.x, realRect.y, realRect.width-2, realRect.height-2);

    }

    public MainFrame getMainFrame() {
        if (mf != null) return mf;
        // Retrieve parent while != MainFrame
        Container parent = this.getDesktopPane().getParent();
        while (parent != null && !(parent instanceof MainFrame)) {
            parent = parent.getParent();
        }
        if (parent != null) {
            mf = (MainFrame)parent;
            return mf;
        } else
            return null;
    }

    /** Getter for property lensMode.
     * @return Value of property lensMode.
     *
     */
    public boolean isLensMode() {
        return lensMode;
    }

    /** Setter for property lensMode.
     * @param lensMode New value of property lensMode.
     *
     */
    public void setLensMode(boolean lensMode) {
        //
        if (this.lensMode == lensMode) return;
        if (lensMode == true)
            jPanelReport.setCursor( java.awt.Cursor.getDefaultCursor());
        else {
            //if (getShiftState())
            //if (this.is
            //		panel9.setCursor(cursorminus );
            //	else
            //		panel9.setCursor(cursorplus);
        }
        this.lensMode = lensMode;
    }

    /** Getter for property newObjectType.
     * @return Value of property newObjectType.
     *
     */
    public int getNewObjectType() {
        return newObjectType;
    }

    /** Setter for property newObjectType.
     * @param newObjectType New value of property newObjectType.
     *
     */
    public void setNewObjectType(int newObjectType) {


        if (this.newObjectType == newObjectType) return;

        if (newObjectType == ReportElementType.NONE) {
                /*
                if (!first_draw2) panel9.createGraphics().drawLines(new Point[]{new Point(ne.x, ne.y),new Point(mouse.x, ne.y),new Point(mouse.x, mouse.y),new Point(ne.x, mouse.y),new Point(ne.x, ne.y)},RasterOp.SOURCE.xorWith(RasterOp.TARGET));
                first_draw2 = true;
                panel9.setCursor(Cursor.DEFAULT);
                ne = null;
                 */
            jPanelReport.setCursor( java.awt.Cursor.getDefaultCursor());
        } else {
            jPanelReport.setCursor( java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.CROSSHAIR_CURSOR));
        }
        this.newObjectType = newObjectType;

        for (int i=1; i<jTabbedPane1.getTabCount(); ++i)
        {
            if ( jTabbedPane1.getComponentAt(i) instanceof CrosstabEditor)
            {
                CrosstabEditor ce= (CrosstabEditor)jTabbedPane1.getComponentAt(i);
                ce.getPanelEditor().setNewObjectType(newObjectType );
            }
        }
    }



    public void setSelectedElement(ReportElement el, boolean fireEvent) {
        // Cancello le corone attorno a tutti gli elementi selezionati...
        boolean makerefreshAll = false;
        //if (selectedElements.size() > 0)
        makerefreshAll=true;

        getSelectedElements().removeAllElements();

        // getMainFrame().getElementPropertiesDialog().updateSelection();
                    /*
                while (selectedElements.size() > 0)
                {


                        ReportElement re = (ReportElement)selectedElements.elementAt(0);
                        selectedElements.removeElementAt(0);

                        if (makerefreshAll) continue;

                        Rectangle rect = new Rectangle(re.position.x-5,re.position.y-5,re.width+10, re.height+10);

                        redrawAll(offscreen ,rect,5);
                        Rectangle clipRect = new Rectangle(getZoomedDim(re.position.x-10)-5+10-jHorizontalScrollBar.getValue(),
                                                                                           getZoomedDim(re.position.y-10)-5+10-jVerticalScrollBar.getValue(),
                                                                                           getZoomedDim(re.width)+10,
                                                                                           getZoomedDim(re.height)+10);
                        jPanelReport.getGraphics().drawImage( offscreenImage ,
                                                                      clipRect.x ,clipRect.y, clipRect.width,clipRect.height,
                                                                      clipRect.x ,clipRect.y, clipRect.width,clipRect.height,
                                                                      (ImageObserver)this);

                }
                     */

        if (el != null) {
            Rectangle rect = new Rectangle(el.position.x-5,el.position.y-5,el.width+10, el.height+10);
            selectedElements.addElement(el);

            getMainFrame().setCutCopyEnabled(true);
                        /*
                        if (!makerefreshAll)
                        {
                                redrawAll(offscreen ,rect,5);
                                Rectangle clipRect = new Rectangle(getZoomedDim(el.position.x-10)-5+10-jHorizontalScrollBar.getValue(),
                                                                                           getZoomedDim(el.position.y-10)-5+10-jVerticalScrollBar.getValue(),
                                                                                           getZoomedDim(el.width)+10,
                                                                                           getZoomedDim(el.height)+10);

                                jPanelReport.getGraphics().drawImage( offscreenImage ,
                                                                      clipRect.x ,clipRect.y, clipRect.width,clipRect.height,
                                                                      clipRect.x ,clipRect.y, clipRect.width,clipRect.height,
                                                                      (ImageObserver)this);
                        }
                         */
        } else {
            getMainFrame().setCutCopyEnabled(false);
        }
        
       
        
        if (makerefreshAll) {
            jPanelReport.repaint();
        }
        
       

               /*
                MainForm mf = (MainForm)this.getMDIParent();
                if (mf != null)
                {
                        if (mf.isPropertiesOpened())
                        {
                                        mf.initReportForm();
                                        mf.ep.setEelement(this,getSelectedElements());
                                        mf.ep.setVisible(true);
                        }
                }
                */

        
        if (fireEvent) {
            ReportElementsSelectionEvent rece = new ReportElementsSelectionEvent(this, this.getSelectedElements()) ;

            this.fireReportListenerReportElementsSelectionChanged(rece );
            
            getMainFrame().getElementPropertiesDialog().updateSelection();
        
        }
        
        
    }

    public void updateObjectMagnetics()
    {
        verticalObjectsLines.clear();
        horizontalObjectsLines.clear();
        
        if (MainFrame.getMainInstance().getMagnetEnabled())
        {
            Enumeration enum_rep = getReport().getElements().elements();
            while (enum_rep.hasMoreElements()) {
                ReportElement re = (ReportElement)enum_rep.nextElement();
                if (!getSelectedElements().contains(re))
                {
                    Integer in = new Integer(re.getPosition().x);
                    if (!verticalObjectsLines.contains(in)) verticalObjectsLines.add(in);
                    in = new Integer(re.getPosition().x + re.getWidth());
                    if (!verticalObjectsLines.contains(in)) verticalObjectsLines.add(in);

                    in = new Integer(re.getPosition().y);
                    if (!horizontalObjectsLines.contains(in)) horizontalObjectsLines.add(in);
                    in = new Integer(re.getPosition().y + re.getHeight());
                    if (!horizontalObjectsLines.contains(in)) horizontalObjectsLines.add(in);
                }
            }

            // add bands locations...
            Enumeration enum_bands = getReport().getBands().elements();
            while (enum_bands.hasMoreElements()) {
                Band band = (Band)enum_bands.nextElement();

                Integer in = new Integer(band.getBandYLocation()+10);
                if (!horizontalObjectsLines.contains(in)) horizontalObjectsLines.add(in);
            }


            Integer in = new Integer(getReport().getLeftMargin()+10);
            if (!verticalObjectsLines.contains(in)) verticalObjectsLines.add(in);
            
            in = new Integer(getReport().getWidth() - getReport().getLeftMargin()+10);
            if (!verticalObjectsLines.contains(in)) verticalObjectsLines.add(in);

            in = new Integer(getReport().getTopMargin()+10);
            if (!horizontalObjectsLines.contains(in)) horizontalObjectsLines.add(in);


            try {
                   MAGNETIC_POWER = Integer.parseInt(this.getMainFrame().getProperties().getProperty("MagneticPower","5"));
            } catch (Exception ex){}
        
        }
        else
        {
            MAGNETIC_POWER = 0;
        }
    
    }
    
    
    public void setSelectedElement(ReportElement el) {
        setSelectedElement(el, true);
    }

    public void fireSelectionChangedEvent() {
        ReportElementsSelectionEvent rece = new ReportElementsSelectionEvent(this, this.getSelectedElements()) ;

        this.fireReportListenerReportElementsSelectionChanged(rece );
        getMainFrame().getElementPropertiesDialog().updateSelection();
    }

    public void addSelectedElement(ReportElement el) {
        addSelectedElement(el, true);
    }

    public void addSelectedElement(ReportElement el, boolean fireEvent) {
        if (selectedElements.contains(el)) return;
        //label1.setText("ADDED");
        // Faccio una corona non selected attorno al nuovo elemento...
        Rectangle rect = new Rectangle(el.position.x-5,el.position.y-5,el.width+10, el.height+10);
        selectedElements.addElement(el);
                /*
                redrawAll(offscreen ,rect,5);
                Rectangle clipRect = new Rectangle(getZoomedDim(el.position.x-10)-5+10-jHorizontalScrollBar.getValue(),
                                                                        getZoomedDim(el.position.y-10)-5+10-jVerticalScrollBar.getValue(),
                                                                        getZoomedDim(el.width)+10,
                                                                        getZoomedDim(el.height)+10);
                jPanelReport.getGraphics().drawImage( offscreenImage ,
                                                                      clipRect.x ,clipRect.y, clipRect.width,clipRect.height,
                                                                      clipRect.x ,clipRect.y, clipRect.width,clipRect.height,
                                                                      (ImageObserver)this);
                 */
        jPanelReport.repaint();

        if (fireEvent) {
            fireSelectionChangedEvent();
        }

        getMainFrame().setCutCopyEnabled(true);
    }

    /** Getter for property jPanelReport.
     * @return Value of property jPanelReport.
     *
     */
    public it.businesslogic.ireport.gui.JReportPanel getJPanelReport() {
        return jPanelReport;
    }

    /** Setter for property jPanelReport.
     * @param jPanelReport New value of property jPanelReport.
     *
     */
    public void setJPanelReport(it.businesslogic.ireport.gui.JReportPanel jPanelReport) {
        this.jPanelReport = jPanelReport;
    }

    /** Getter for property showGrid.
     * @return Value of property showGrid.
     *
     */
    public boolean isShowGrid() {
        return showGrid;
    }

    /** Setter for property showGrid.
     * @param showGrid New value of property showGrid.
     *
     */
    public void setShowGrid(boolean showGrid) {

        if (this.showGrid == showGrid) return;

        this.showGrid = showGrid;
        isDocDirty = true;
        jPanelReport.repaint();
    }

    /** Getter for property gridSize.
     * @return Value of property gridSize.
     *
     */
    public int getGridSize() {
        try {
            return Integer.parseInt(MainFrame.getMainInstance().getProperties().getProperty("GridSize","10"));
        } catch (Exception ex)
        {
          return 60;
        }
    }

    /** Getter for property transformationMinSize.
     * @return Value of property transformationMinSize.
     *
     */
    public int getTransformationMinSize() {
        return transformationMinSize;
    }

    /** Setter for property transformationMinSize.
     * @param transformationMinSize New value of property transformationMinSize.
     *
     */
    public void setTransformationMinSize(int transformationMinSize) {
        this.transformationMinSize = transformationMinSize;
    }

    public int snapToGridVertically(int scaledYPoint) {

        int real_o_y = getLogicalDim((int)(scaledYPoint + jVerticalScrollBar.getValue() - 10)) + 10;

        return getZoomedDim(closestXMagneticElement(real_o_y,getGridSize(), jVerticalRule.getGuideLines()) -10)+10-getVScroll();

        /*
        if (!isSnapToGrid()) return scaledYPoint;

        int vertical_scroll = jVerticalScrollBar.getValue();
        int zoomed_grid_size = getZoomedDim(this.getGridSize());
        int grid_top = 10+ zoomed_grid_size - vertical_scroll;
        int design_height = report.getDesignHeight();
        int line_bottom = getZoomedDim(design_height)+10-vertical_scroll-2;

        if (zoomed_grid_size>2) {
            int i=1;
            while (grid_top < line_bottom ) {
                if ( Math.abs(grid_top - scaledYPoint) <= zoomed_grid_size/2) {
                    return grid_top;
                }
                i++;
                grid_top = 10 - vertical_scroll + getZoomedDim(i*this.getGridSize());
            }
        }
        return scaledYPoint;
         */
    }

    public int snapToGridOrizzontally(int scaledXPoint) {

        int real_o_x = getLogicalDim((int)(scaledXPoint + jHorizontalScrollBar.getValue() - 10)) + 10;

        return getZoomedDim(closestXMagneticElement(real_o_x,getGridSize(), jHorizontalRule.getGuideLines()) -10)+10-getHScroll();
        /*
        if (!isSnapToGrid()) return scaledXPoint;
        // get closest horizontal grid to the
        // get closest vetical grid to the
        int horizontal_scroll = jHorizontalScrollBar.getValue();
        //int vertical_scroll = jVerticalScrollBar.getValue();
        int zoomed_width = getZoomedDim(report.getWidth());
        //int design_height = report.getDesignHeight();
        //int zoomed_height = getZoomedDim(design_height);
        int zoomed_grid_size = getZoomedDim(this.getGridSize());
        int grid_left = 10+ zoomed_grid_size - horizontal_scroll;

        //int line_bottom = getZoomedDim(design_height)+10-vertical_scroll-2;
        if (zoomed_grid_size>2) {
            int i=1;
            while (grid_left < zoomed_width+10-horizontal_scroll) {
                if ( Math.abs(grid_left - scaledXPoint) <= zoomed_grid_size/2) {
                    return grid_left;
                }
                i++;
                grid_left =  10 - horizontal_scroll + getZoomedDim(i*this.getGridSize());
            }
        }
        return scaledXPoint;
         */
    }

    public int gridMultiple( int zoomedDim) {
        if (!isSnapToGrid()) {
            return zoomedDim;
        }

        int zoomedGridSize = getZoomedDim(this.getGridSize());
        double val = Math.abs(zoomedDim);
        if ( zoomedGridSize > 1.0 ) {
            val =  Math.floor( ( val +  (0.5 * zoomedGridSize)) / zoomedGridSize ) * zoomedGridSize;
        }

        return (int) ( (zoomedDim >= 0) ? val : -val );
    }

    // no need for gridMultipleMinues
    /*
    public int gridMultipleMinus( int zoomedDim) {
        // Round ++...
        if (!isSnapToGrid()) {
            return zoomedDim;
        }

        int zoomed_grid_size = getZoomedDim(this.gridSize);
        int val = Math.abs(zoomedDim);
        if (val%zoomed_grid_size == 0) {
            return zoomedDim;
        }
        if( val%zoomed_grid_size <= zoomed_grid_size/2) {
            while ( val%zoomed_grid_size != 0) {
                val--;
            }
        }
        else {
            while ( val%zoomed_grid_size != 0) {
                val++;
            }
        }
        return (zoomedDim >= 0) ? val : -val;
    }
     */


    /** Getter for property snapToGrid.
     * @return Value of property snapToGrid.
     *
     */
    public boolean isSnapToGrid() {
        return snapToGrid;
    }

    /** Setter for property snapToGrid.
     * @param snapToGrid New value of property snapToGrid.
     *
     */
    public void setSnapToGrid(boolean snapToGrid) {
        this.snapToGrid = snapToGrid;
    }

    /** Getter for property isDocDirty.
     * @return Value of property isDocDirty.
     *
     */
    public boolean isIsDocDirty() {
        return isDocDirty;
    }

    /** Setter for property isDocDirty.
     * @param isDocDirty New value of property isDocDirty.
     *
     */
    public void setIsDocDirty(boolean isDocDirty) {
        this.isDocDirty = isDocDirty;
    }

    public void checkSelection(boolean repaint) {
        boolean needRepaint = false;
        for (int i=0; i< getSelectedElements().size(); ++i) {
            // Delete all objects that don't exists..
            ReportElement selected = (ReportElement)getSelectedElements().get(i);
            if (!this.getReport().getElements().contains(selected)) {
                getSelectedElements().removeElementAt(i);
                i--;
                needRepaint = true;
            }
        }
        if (repaint && needRepaint) {
            jPanelReport.repaint();
        }
    }

    /** Getter for property windowID.
     * @return Value of property windowID.
     *
     */
    public int getWindowID() {
        return windowID;
    }

    /** Setter for property windowID.
     * @param windowID New value of property windowID.
     *
     */
    public void setWindowID(int windowID) {
        this.windowID = windowID;
    }

    /** Registers ReportListener to receive events.
     * @param listener The listener to register.
     *
     */
    public synchronized void addReportListener(it.businesslogic.ireport.gui.event.ReportListener listener) {
        if (listenerList == null ) {
            listenerList = new javax.swing.event.EventListenerList();
        }
        listenerList.add(it.businesslogic.ireport.gui.event.ReportListener.class, listener);
    }

    /** Removes ReportListener from the list of listeners.
     * @param listener The listener to remove.
     *
     */
    synchronized void removeReportListener(it.businesslogic.ireport.gui.event.ReportListener listener) {
        listenerList.remove(it.businesslogic.ireport.gui.event.ReportListener.class, listener);
    }

    /** Notifies all registered listeners about the event.
     *
     * @param event The event to be fired
     *
     */
    public void fireReportListenerReportElementsSelectionChanged(it.businesslogic.ireport.gui.event.ReportElementsSelectionEvent event) {
        
        if (listenerList == null) return;
        Object[] listeners = listenerList.getListenerList ();
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==it.businesslogic.ireport.gui.event.ReportListener.class) {
                ((it.businesslogic.ireport.gui.event.ReportListener)listeners[i+1]).reportElementsSelectionChanged (event);
            }
        }
    }
    
    /** Notifies all registered listeners about the event.
     *
     * @param event The event to be fired
     *
     */
    public void fireReportListenerReportBandsSelectionChanged(it.businesslogic.ireport.gui.event.ReportBandsSelectionEvent event) {
        
        if (listenerList == null) return;
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==it.businesslogic.ireport.gui.event.ReportListener.class) {
                
                ((it.businesslogic.ireport.gui.event.ReportListener)listeners[i+1]).reportBandsSelectionChanged(event);
            }
        }
    }
    
    /** Notifies all registered listeners about the event.
     *
     * @param event The event to be fired
     *
     */
    public void fireReportListenerReportObjectsSelectionChanged(it.businesslogic.ireport.gui.event.ReportObjectsSelectionEvent event) {
        
        if (listenerList == null) return;
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==it.businesslogic.ireport.gui.event.ReportListener.class) {
                
                ((it.businesslogic.ireport.gui.event.ReportListener)listeners[i+1]).reportObjectsSelectionChanged(event);
            }
        }
    }

    /** Notifies all registered listeners about the event.
     *
     * @param event The event to be fired
     *
     */
    public void fireReportListenerReportElementsChanged(it.businesslogic.ireport.gui.event.ReportElementChangedEvent event) {
        if (listenerList == null) return;
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==it.businesslogic.ireport.gui.event.ReportListener.class) {
                ((it.businesslogic.ireport.gui.event.ReportListener)listeners[i+1]).reportElementsChanged(event);
            }
        }
    }

    /** Notifies all registered listeners about the event.
     *
     * @param event The event to be fired
     *
     */
    public void fireReportListenerReportBandChanged(it.businesslogic.ireport.gui.event.ReportBandChangedEvent event) {
        if (listenerList == null) return;
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==it.businesslogic.ireport.gui.event.ReportListener.class) {
                ((it.businesslogic.ireport.gui.event.ReportListener)listeners[i+1]).reportBandChanged(event);
            }
        }
    }


        /* Return the operation that must be invoked on undo
         * Return null if no undo operation is available
         */
    public UndoOperation getUndoOperation() {
        if (this.undoOperations.size() > 0 && undoIndex >= 0)
            return (UndoOperation)this.undoOperations.elementAt(undoIndex);
        return null;
    }

        /* This call "consume" the undo operation and update
         * internally the poiter of the undo list...
         */
    public void undo() {
        if (getUndoOperation() != null) {
            UndoOperation u = getUndoOperation();
            u.undo();
            this.undoIndex--;

            this.getReport().decrementReportChanges();
            getMainFrame().updateOpenedDocumentsList();
        }
    }

        /* This call "consume" the redo operation and update
         * internally the poiter of the undo list...
         */
    public void redo() {
        if (getRedoOperation() != null) {
            getRedoOperation().redo();
            this.undoIndex++;

            this.getReport().incrementReportChanges();
            getMainFrame().updateOpenedDocumentsList();
        }
    }

        /* Return the redo operation that must be invoked on redo
         * Return null if no redo operation is available
         */
    public UndoOperation getRedoOperation() {
        if (this.undoOperations.size() > undoIndex+1)
            return (UndoOperation)this.undoOperations.elementAt(undoIndex+1);
        return null;
    }

    public void addUndoOperation(UndoOperation undo) {
        // Remove all operations from the index...
        while (this.undoOperations.size() > undoIndex+1) {
            this.undoOperations.removeElementAt( undoIndex+1 );
        }
        undoOperations.addElement(undo);
        undoIndex++;

        getReport().incrementReportChanges();

        /*
        try {
            getMainFrame().updateOpenedDocumentsList();
        } catch (Exception ex) {
            System.out.println( "[1]" + ex);
            ex.printStackTrace();
        }
        */
        try {
            getMainFrame().updateUndoMenu(this);
        } catch (Exception ex) {
            System.out.println( "[2]" + ex);
            ex.printStackTrace();
        }
    }

    public void deleteSelectedElements() {
        // Remove selected elements...
        Enumeration e = selectedElements.elements();

        DeleteElementsOperation undoOp = new DeleteElementsOperation(this);
        Vector deletedElements = new Vector();

        while (e.hasMoreElements()) {

            ReportElement re = (ReportElement) e.nextElement();
            if ( getReport().getElements().contains( re))
            {
                undoOp.addElement(  re, getReport().getElements().indexOf(re));
                getReport().getElements().remove( re );
                deletedElements.add(re);
                if (re instanceof FrameReportElement)
                {
                    removeSubElements(re, undoOp, deletedElements);
                }
                if (re instanceof CrosstabReportElement)
                {
                    removeCrosstabEditor((CrosstabReportElement)re);
                }
            }
            //fireReportListenerReportElementsChanged(new ReportElementChangedEvent(this, re , ReportElementChangedEvent.REMOVED));
        }

        fireReportListenerReportElementsChanged(new ReportElementChangedEvent(this, deletedElements,  ReportElementChangedEvent.REMOVED ));
        this.setSelectedElement(null);
        this.addUndoOperation(undoOp);
        //selectedElements.removeAllElements();

        jPanelReport.repaint();
    }

    protected void removeSubElements(ReportElement parentElement, DeleteElementsOperation undoOp, Vector deletedElements)
    {
        for (int i=0; i<getReport().getElements().size(); ++i)
        {
            ReportElement re = (ReportElement)getReport().getElements().elementAt(i);
            if (re.getParentElement() == parentElement)
            {
                undoOp.addElement(  re, getReport().getElements().indexOf(re));
                getReport().getElements().remove( re );
                i--;
                deletedElements.add(re);
                if (re instanceof FrameReportElement)
                {
                    removeSubElements(re, undoOp, deletedElements);
                }
                if (re instanceof CrosstabReportElement)
                {
                    removeCrosstabEditor((CrosstabReportElement)re);
                }

            }
        }
    }

    public void cut() {
        Vector copiedElements = new Vector();

        Vector tmpSelectedElements = null;
        if (getSelectedCrosstabEditorPanel() != null)
        {
            tmpSelectedElements = getSelectedCrosstabEditorPanel().getSelectedElements();
        }
        else
        {
            tmpSelectedElements = this.getSelectedElements();
        }

        Enumeration e = tmpSelectedElements.elements();
        while (e.hasMoreElements()) {
            ReportElement er = (ReportElement)e.nextElement();
            copiedElements.add(er.cloneMe());
        }


        if (copiedElements.size() > 0) {
            getMainFrame().setClipBoardContent(copiedElements);
        }

        // Remove selected elements...
        if (getSelectedCrosstabEditorPanel() != null)
        {
            getSelectedCrosstabEditorPanel().deleteSelectedElements();
        }
        else
        {
            this.deleteSelectedElements();
        }
        getMainFrame().updateCutAndPasteMenu(this);

    }

    public void copy() {
        Vector copiedElements = new Vector();

        Vector tmpSelectedElements = null;
        if (getSelectedCrosstabEditorPanel() != null)
        {
            tmpSelectedElements = getSelectedCrosstabEditorPanel().getSelectedElements();
        }
        else
        {
            tmpSelectedElements = this.getSelectedElements();
        }

        Enumeration e = tmpSelectedElements.elements();
        while (e.hasMoreElements()) {
            ReportElement er = (ReportElement)e.nextElement();
            if (!(er instanceof CrosstabReportElement))
            {
                copiedElements.add(er.cloneMe());
            }
        }
        if (copiedElements.size() > 0) {
            getMainFrame().setClipBoardContent(copiedElements);
        }

        getMainFrame().updateCutAndPasteMenu(this);
    }

    public void paste() {
        // Add elements...
        pasteHere(null);
     }

    public void pasteHere(Point p) {
        // Add elements...

        Enumeration e = getMainFrame().getClipBoard().elements();
        ReportElement container = null;

        Vector tmpSelectedElements = null;
        if (getSelectedCrosstabEditorPanel() != null)
        {
            tmpSelectedElements = getSelectedCrosstabEditorPanel().getSelectedElements();
        }
        else
        {
            tmpSelectedElements = this.getSelectedElements();
        }

        if (tmpSelectedElements.size() > 0)
        {
            container = (ReportElement)tmpSelectedElements.elementAt(0);
            if (!(container instanceof FrameReportElement))
            {
                container = null;
            }
        }

        if (getSelectedCrosstabEditorPanel() != null)
        {
            getSelectedCrosstabEditorPanel().setSelectedElement(null);
        }
        else
        {
            setSelectedElement(null);
        }

        Vector addedElements = new Vector();


        int y = 0;
        if (p != null){

            if (getSelectedCrosstabEditorPanel() != null)
            {
               y = getSelectedCrosstabEditorPanel().getZoomedDim(p.y-10);
            }
            else
            {
               y= getLogicalDim( p.y-10+jVerticalScrollBar.getValue())+10;
            }
        }

        CrosstabReportElement crReportElement = null;
        if (getSelectedCrosstabEditorPanel() != null) crReportElement = getSelectedCrosstabEditorPanel().getCrosstabElement();

        while (e.hasMoreElements()) {
            ReportElement re = ((ReportElement)e.nextElement()).cloneMe();

            re.setKey( getNextElementKey(re.getKey()) );


            if (getSelectedCrosstabEditorPanel() != null)
            {
                if (re instanceof BreakReportElement ||
                    re instanceof ChartReportElement ||
                    re instanceof ChartReportElement2 ||
                    re instanceof CrosstabReportElement ||
                    re instanceof SubReportElement) continue;

                CrosstabCell cell = null;
                if (getSelectedCrosstabEditorPanel().isDefaultCellMode())
                {
                   cell = getSelectedCrosstabEditorPanel().getWhenNoDataCell();

                }
                else if (p != null)
                {
                     cell = getSelectedCrosstabEditorPanel().getCellByPoint(p);
                     if (cell == null) cell = (CrosstabCell)getSelectedCrosstabEditorPanel().getCrosstabElement().getCells().elementAt(0);
                }
                re.setBand(null);
                if (re.getCell() != null)
                {
                    // Update relative position...
                    re.setRelativePosition(new Point(re.getPosition().x - re.getCell().getLeft()-10,
                                                     re.getPosition().y - re.getCell().getTop()-10));
                }
                else
                {
                    re.setRelativePosition(new Point(0,0));
                }

                //System.out.println(re.getRelativePosition());

                // Copia da cella a cella...
                if (cell != null)
                {
                    re.setCell(cell);
                    re.getPosition().x = re.getRelativePosition().x + re.getCell().getLeft()+10;
                    re.getPosition().y = re.getRelativePosition().y + re.getCell().getTop()+10;
                }
                else if (getSelectedCrosstabEditorPanel().getCrosstabElement().getCells().contains( re.getCell()))
                {
                    re.getPosition().x += 10;
                    re.getRelativePosition().x += 10;
                    re.getPosition().y += 10;
                    re.getRelativePosition().y += 10;
                }
                else
                {
                    cell = (CrosstabCell)getSelectedCrosstabEditorPanel().getCrosstabElement().getCells().elementAt(0);
                    re.setCell(cell);
                    re.getPosition().x = re.getRelativePosition().x + re.getCell().getLeft()+10;
                    re.getPosition().y = re.getRelativePosition().y + re.getCell().getTop()+10;
                }

            }
            else
            {
                String bandName = (re.getBand() == null) ? "" : re.getBand().getName();
                Band b =  getReport().getBandByName( bandName );
                re.setBand( b );


                if (p !=null)
                {
                    int ylocation = getReport().getBandYLocation( b );
                    Band myBand = null;

                    Enumeration enum_bands = getReport().getBands().elements();
                    while (enum_bands.hasMoreElements()) {
                        Band bb = (Band)enum_bands.nextElement();
                        if (report.getBandYLocation(bb)+10 <= y) {
                            myBand = bb;
                        }
                    }

                    if (myBand == null) return;

                    int ylocation2 = getReport().getBandYLocation( myBand );

                    Point location = re.getPosition();
                    location.y += ylocation2 - ylocation;

                    re.setBand( myBand );
                    re.setPosition(location);
                }
                else
                {
                    if (re.getPosition().y > getReport().getDesignHeight()-getReport().getBottomMargin())
                    {
                        re.getPosition().y = getReport().getDesignHeight()-1-getReport().getBottomMargin();
                    }
                    if (b == null) b = getReport().getBandByPosition( re.getPosition().y );
                    re.setBand( b );
                    java.awt.Point p2 = re.getPosition();
                    int add_x = this.getGridSize();
                    if (re instanceof BreakReportElement) add_x = 0;
                    re.setPosition(new java.awt.Point(p2.x+add_x,p2.y+this.getGridSize()));
                }

                if (container != null)
                {
                     re.setBand( container.getBand());
                     re.setParentElement( container);
                }
                else
                {
                     re.setParentElement( null );
                }

            }

            if (re instanceof CrosstabReportElement)
            {
                addCrosstabEditor((CrosstabReportElement)re);
            }
            re.updateBounds();

            // We must find the right band to paste into...
            //re.setBand( bname );
            //

            addUndoOperation( new  InsertElementOperation(this, crReportElement, re) );

            if (getSelectedCrosstabEditorPanel() != null)
            {
                crReportElement.getElements().addElement(re);
                getSelectedCrosstabEditorPanel().addSelectedElement(re);
            }
            else
            {
                report.getElements().addElement(re);
                addSelectedElement(re, false);
            }
            addedElements.add(re);
        }
        
        fireReportListenerReportElementsChanged(new ReportElementChangedEvent(this, crReportElement, addedElements , ReportElementChangedEvent.ADDED));

        if (crReportElement != null)
        {
            getSelectedCrosstabEditorPanel().fireSelectionChangedEvent();
        }
        else
        {
            fireSelectionChangedEvent();
        }

        getMainFrame().updateCutAndPasteMenu(this);

    }

    public void openElementPropertiesDialog(){
        // If there is at least an element selected, open the properties window...
        MainFrame mainFrame = getMainFrame();


        if ((getSelectedCrosstabEditorPanel() != null && getSelectedCrosstabEditorPanel().getSelectedElements().size() > 0) ||
            getSelectedElements().size() > 0)
        {
            if (!mainFrame.getElementPropertiesDialog().isVisible()) {
                //mainFrame.getElementPropertiesDialog().updateSelection();
                mainFrame.getElementPropertiesDialog().setVisible(true);
            }
        }

        if (getSelectedCrosstabEditorPanel() != null) getSelectedCrosstabEditorPanel().requestFocus();
        else jPanelReport.requestFocus();
    }

    public void  updateTitle() {

        this.setTitle(this.report.getName()+" "+this.report.getWidth()+"x"+this.report.getDesignHeight()+" ["+Misc.nvl(this.report.getFilename(),"unnamed")+"]");

    }

    /**
     *
     */
    public void bringToFront() {
        Enumeration e = this.getSelectedElements().elements();

        ChangeEmentsOrderOperation undoOp = new ChangeEmentsOrderOperation(this);

        while  (e.hasMoreElements()) {
            ReportElement element = (ReportElement)e.nextElement();

            int oldPosition = getReport().getElements().indexOf(element);
            getReport().getElements().remove(element);
            getReport().getElements().addElement(element);
            int newPosition = getReport().getElements().indexOf(element);
            undoOp.addElement(element,oldPosition, newPosition);
        }

        if (undoOp.getElements().size() > 0) {
            addUndoOperation(undoOp);
        }
        getReportPanel().repaint();
        // We must update the tree... the order i changed...
        /* TODO */
    }

    public void sendToBack() {
        Enumeration e = this.getSelectedElements().elements();

        ChangeEmentsOrderOperation undoOp = new ChangeEmentsOrderOperation(this);

        while  (e.hasMoreElements()) {
            ReportElement element = (ReportElement)e.nextElement();

            int oldPosition = getReport().getElements().indexOf(element);
            getReport().getElements().remove(element);
            if (element.getParentElement() != null)
            {
                getReport().getElements().insertElementAt( element, getReport().getElements().indexOf(element.getParentElement())+1);
            }
            else
            {
                 getReport().getElements().insertElementAt(element,0);
            }
            int newPosition = 0;
            undoOp.addElement(element,oldPosition, newPosition);
        }

        if (undoOp.getElements().size() > 0) {
            addUndoOperation(undoOp);
        }
        getReportPanel().repaint();
        // We must update the tree... the order is changed...
        /* TODO */
    }

    /*
    public void makeOperation(int operationType) {
        if (getSelectedElements().isEmpty()) return;

        Enumeration e = getSelectedElements().elements();
        ReportElement re = null;
        FormatElementsOperation undoOp = new FormatElementsOperation(this, operationType);

        switch (operationType) {
            case OperationType.ALIGN_LEFT:
                int left= ((ReportElement)getSelectedElements().firstElement()).getPosition().x;

                while (e.hasMoreElements()){
                    re = (ReportElement)e.nextElement();
                    Rectangle oldBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    re.setPosition(new Point(left, re.getPosition().y));
                    re.updateBounds();
                    Rectangle newBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    undoOp.addElement(re, oldBounds, newBounds );
                }
                break;
            case OperationType.ALIGN_RIGHT:
                int right= ((ReportElement)getSelectedElements().firstElement()).getPosition().x+((ReportElement)getSelectedElements().firstElement()).getWidth();
                while (e.hasMoreElements()){
                    re = (ReportElement)e.nextElement();
                    Rectangle oldBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    re.setPosition(new Point(right-re.getWidth(), re.getPosition().y));
                    re.updateBounds();
                    Rectangle newBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    undoOp.addElement(re, oldBounds, newBounds );
                }
                break;
            case OperationType.ALIGN_TOP:
                int top= ((ReportElement)getSelectedElements().firstElement()).getPosition().y;
                while (e.hasMoreElements()){
                    re = (ReportElement)e.nextElement();
                    Rectangle oldBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    re.setPosition(new Point(re.getPosition().x, top));
                    re.updateBounds();
                    Rectangle newBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    undoOp.addElement(re, oldBounds, newBounds );
                }
                break;
            case OperationType.ALIGN_BOTTOM:
                int bottom= ((ReportElement)getSelectedElements().firstElement()).getPosition().y+((ReportElement)getSelectedElements().firstElement()).height;
                while (e.hasMoreElements()){
                    re = (ReportElement)e.nextElement();
                    Rectangle oldBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    re.setPosition(new Point(re.getPosition().x, bottom-re.height));
                    re.updateBounds();
                    Rectangle newBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    undoOp.addElement(re, oldBounds, newBounds );
                }
                break;
            case OperationType.ALIGN_HORIZONTAL_AXIS:
                re = (ReportElement)getSelectedElements().firstElement();
                int center = re.getPosition().y + (re.getHeight()/2);
                while (e.hasMoreElements()){
                    re = (ReportElement)e.nextElement();
                    Rectangle oldBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    re.setPosition(new Point(re.getPosition().x, center-(re.getHeight()/2) ));
                    re.updateBounds();
                    Rectangle newBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    undoOp.addElement(re, oldBounds, newBounds );
                }
                break;
            case OperationType.ALIGN_VERTICAL_AXIS:
                re = (ReportElement)getSelectedElements().firstElement();
                center = re.getPosition().x + (re.getWidth()/2);
                while (e.hasMoreElements()){
                    re = (ReportElement)e.nextElement();
                    Rectangle oldBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    re.setPosition(new Point(center-(re.getWidth()/2), re.getPosition().y));
                    re.updateBounds();
                    Rectangle newBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    undoOp.addElement(re, oldBounds, newBounds );
                }
                break;
            case OperationType.ALIGN_CENTER_HORIZONTALLY:
            case OperationType.CENTER_IN_BAND_H:
                re = (ReportElement)getSelectedElements().firstElement();

                while (e.hasMoreElements()){
                    re = (ReportElement)e.nextElement();
                    center = 10+this.getReport().getWidth()/2;
                    Rectangle oldBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    re.setPosition(new Point(center-(re.getWidth()/2), re.getPosition().y));
                    re.updateBounds();
                    Rectangle newBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    undoOp.addElement(re, oldBounds, newBounds );
                }
                break;
            case OperationType.ALIGN_CENTER_VERTICALLY:
            case OperationType.CENTER_IN_BAND_V:
                re = (ReportElement)getSelectedElements().firstElement();
                while (e.hasMoreElements()){
                    re = (ReportElement)e.nextElement();
                    center = this.report.getBandYLocation(re.getBand())+10+re.getBand().getHeight()/2;
                    Rectangle oldBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    re.setPosition(new Point(re.getPosition().x, center-(re.getHeight()/2)));
                    re.updateBounds();
                    Rectangle newBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    undoOp.addElement(re, oldBounds, newBounds );
                }
                break;
            case OperationType.ALIGN_CENTER:
            case OperationType.CENTER_IN_BAND:
                re = (ReportElement)getSelectedElements().firstElement();
                int centerx = 10+this.getReport().getWidth()/2;
                int centery = this.report.getBandYLocation(re.getBand())+10+re.getBand().getHeight()/2;
                while (e.hasMoreElements()){
                    re = (ReportElement)e.nextElement();
                    Rectangle oldBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    re.setPosition(new Point(centerx-(re.getWidth()/2), this.report.getBandYLocation(re.getBand())+10+re.getBand().getHeight()/2-(re.getHeight()/2)));
                    re.updateBounds();
                    Rectangle newBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    undoOp.addElement(re, oldBounds, newBounds );
                }
                break;
            case OperationType.SAME_WIDTH:
                re = (ReportElement)getSelectedElements().firstElement();
                int width = re.getWidth();
                while (e.hasMoreElements()){
                    re = (ReportElement)e.nextElement();
                    Rectangle oldBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    re.trasform(new Point(width-re.getWidth(),0), TransformationType.TRANSFORMATION_RESIZE_SE);
                    re.updateBounds();
                    Rectangle newBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    undoOp.addElement(re, oldBounds, newBounds );
                }
                break;
            case OperationType.SAME_WIDTH_MAX:
                re = (ReportElement)getSelectedElements().firstElement();
                // Looking for the max with...
                Enumeration enum2 = getSelectedElements().elements();
                width = re.getWidth();
                while (enum2.hasMoreElements()) {
                    re = (ReportElement)enum2.nextElement();
                    if (width <= re.getWidth()) width =re.getWidth();
                }
                while (e.hasMoreElements()){
                    re = (ReportElement)e.nextElement();
                    Rectangle oldBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    re.trasform(new Point(width-re.getWidth(),0), TransformationType.TRANSFORMATION_RESIZE_SE);
                    re.updateBounds();
                    Rectangle newBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    undoOp.addElement(re, oldBounds, newBounds );
                }
                break;
            case OperationType.SAME_WIDTH_MIN:
                re = (ReportElement)getSelectedElements().firstElement();
                // Looking for the max with...
                enum2 = getSelectedElements().elements();
                width = re.getWidth();
                while (enum2.hasMoreElements()) {
                    re = (ReportElement)enum2.nextElement();
                    if (width > re.getWidth()) width =re.getWidth();
                }
                while (e.hasMoreElements()){
                    re = (ReportElement)e.nextElement();
                    Rectangle oldBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    re.trasform(new Point(width-re.getWidth(),0), TransformationType.TRANSFORMATION_RESIZE_SE);
                    re.updateBounds();
                    Rectangle newBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    undoOp.addElement(re, oldBounds, newBounds );
                }
                break;
            case OperationType.SAME_HEIGHT:
                re = (ReportElement)getSelectedElements().firstElement();
                int height = re.height;
                while (e.hasMoreElements()){
                    re = (ReportElement)e.nextElement();
                    Rectangle oldBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    re.trasform(new Point(0,height-re.height), TransformationType.TRANSFORMATION_RESIZE_SE);
                    re.updateBounds();
                    Rectangle newBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    undoOp.addElement(re, oldBounds, newBounds );
                }
                break;
            case OperationType.SAME_HEIGHT_MAX:
                re = (ReportElement)getSelectedElements().firstElement();
                height = re.getHeight();
                enum2 = getSelectedElements().elements();
                while (enum2.hasMoreElements()) {
                    re = (ReportElement)enum2.nextElement();
                    if (height <= re.getHeight()) height =re.getHeight();
                }
                while (e.hasMoreElements()){
                    re = (ReportElement)e.nextElement();
                    Rectangle oldBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    re.trasform(new Point(0,height-re.height), TransformationType.TRANSFORMATION_RESIZE_SE);
                    re.updateBounds();
                    Rectangle newBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    undoOp.addElement(re, oldBounds, newBounds );
                }
                break;
            case OperationType.SAME_HEIGHT_MIN:
                re = (ReportElement)getSelectedElements().firstElement();
                height = re.getHeight();
                enum2 = getSelectedElements().elements();
                while (enum2.hasMoreElements()) {
                    re = (ReportElement)enum2.nextElement();
                    if (height > re.getHeight()) height =re.getHeight();
                }
                while (e.hasMoreElements()){
                    re = (ReportElement)e.nextElement();
                    Rectangle oldBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    re.trasform(new Point(0,height-re.height), TransformationType.TRANSFORMATION_RESIZE_SE);
                    re.updateBounds();
                    Rectangle newBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    undoOp.addElement(re, oldBounds, newBounds );
                }
                break;
            case OperationType.SAME_SIZE:
                re = (ReportElement)getSelectedElements().firstElement();
                height = re.height;
                width = re.getWidth();
                while (e.hasMoreElements()){
                    re = (ReportElement)e.nextElement();
                    Rectangle oldBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    re.trasform(new Point(width-re.getWidth(),height-re.height), TransformationType.TRANSFORMATION_RESIZE_SE);
                    re.updateBounds();
                    Rectangle newBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    undoOp.addElement(re, oldBounds, newBounds );
                }
                break;
            case OperationType.ALIGN_TOP_TO_BAND:
                while (e.hasMoreElements()){
                    re = (ReportElement)e.nextElement();
                    Rectangle oldBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    re.trasform(new Point(0,getReport().getBandYLocation( re.band)+10- re.getPosition().y), TransformationType.TRANSFORMATION_MOVE);
                    re.updateBounds();
                    Rectangle newBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    undoOp.addElement(re, oldBounds, newBounds );
                }
                break;

            // Begin code Robert Lamping, 12 July 2004
            case OperationType.ALIGN_BOTTOM_TO_BAND:
                while (e.hasMoreElements()){
                    re = (ReportElement)e.nextElement();
                    Rectangle oldBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    //re.trasform(new Point(0,getReport().getBandYLocation( re.band)+10- re.getPosition().y), TransformationType.TRANSFORMATION_MOVE);
                    re.trasform( new Point(0, getReport().getBandYLocation( re.band)+10 + re.band.getHeight() -
                            ( re.getPosition().y + re.getHeight() ) ), TransformationType.TRANSFORMATION_MOVE);
                    re.updateBounds();
                    Rectangle newBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    undoOp.addElement(re, oldBounds, newBounds );
                }
                break;

            case OperationType.ALIGN_TO_LEFT_MARGIN:
                int leftMargin= getReport().getLeftMargin();
                //((ReportElement)getSelectedElements().firstElement()).getPosition().x;

                while (e.hasMoreElements()){
                    re = (ReportElement)e.nextElement();
                    Rectangle oldBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    re.setPosition(new Point(leftMargin + 10, re.getPosition().y));
                    re.updateBounds();
                    Rectangle newBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    undoOp.addElement(re, oldBounds, newBounds );
                }
                break;

            case OperationType.ALIGN_TO_RIGHT_MARGIN:
                int rightMargin= getReport().getRightMargin();

                while (e.hasMoreElements()){
                    re = (ReportElement)e.nextElement();
                    Rectangle oldBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    re.setPosition(new Point(this.getReport().getWidth() - rightMargin-re.getWidth()+ 10, re.getPosition().y));
                    re.updateBounds();
                    Rectangle newBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    undoOp.addElement(re, oldBounds, newBounds );
                }
                break;
            case OperationType.MOVE_TO_LEFT_MARGIN:
                // Move all elements to the left over the distance between first element and left margin
                re = (ReportElement)getSelectedElements().firstElement();
                int deltaLeft = re.getPosition().x - getReport().getLeftMargin();

                while (e.hasMoreElements()){
                    re = (ReportElement)e.nextElement();
                    Rectangle oldBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    re.setPosition(new Point(re.getPosition().x + 10 - deltaLeft,  re.getPosition().y));
                    re.updateBounds();
                    Rectangle newBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    undoOp.addElement(re, oldBounds, newBounds );
                }
                break;
            // End code Robert Lamping, 12 July 2004
            case OperationType.ORGANIZE_AS_A_TABLE:

                e = getSelectedElements().elements();
                while (e.hasMoreElements()){
                    re = (ReportElement)e.nextElement();
                    Rectangle oldBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    Rectangle newBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    undoOp.addElement(re, oldBounds, newBounds );
                }

                makeOperation( OperationType.ALIGN_TOP_TO_BAND);
                // Remove all operations from the index...
                while (this.undoOperations.size() > undoIndex) {
                    this.undoOperations.removeElementAt( undoIndex);
                }
                undoIndex--;
                this.getReport().decrementDirty();
                makeOperation( OperationType.JOIN_LEFT);
                // Remove all operations from the index...
                while (this.undoOperations.size() > undoIndex) {
                    this.undoOperations.removeElementAt( undoIndex);
                }
                undoIndex--;
                this.getReport().decrementDirty();
                makeOperation( OperationType.MOVE_TO_LEFT_MARGIN);
                // Remove all operations from the index...
                while (this.undoOperations.size() > undoIndex) {
                    this.undoOperations.removeElementAt( undoIndex);
                }
                undoIndex--;
                this.getReport().decrementDirty();
                makeOperation( OperationType.SAME_HEIGHT_MIN);
                // Remove all operations from the index...
                while (this.undoOperations.size() > undoIndex) {
                    this.undoOperations.removeElementAt( undoIndex);
                }
                undoIndex--;
                this.getReport().decrementDirty();
                makeOperation( OperationType.INCREASE_SPACE_H);
                // Remove all operations from the index...
                while (this.undoOperations.size() > undoIndex) {
                    this.undoOperations.removeElementAt( undoIndex);
                }
                undoIndex--;
                this.getReport().decrementDirty();

                e = getSelectedElements().elements();
                int kkk = 0;
                while (e.hasMoreElements()){
                    re = (ReportElement)e.nextElement();
                    Rectangle newBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    ((ElementTransformation)undoOp.getTransformations().elementAt(kkk)).newBounds = newBounds;
                    kkk++;
                }
                break;
            case OperationType.JOIN_LEFT:
            case OperationType.REMOVE_SPACE_H:
                // 1 Search for all different bands...

                Vector used_bands = new Vector();
                enum2 = getSelectedElements().elements();
                while (enum2.hasMoreElements()) {
                    re = (ReportElement)enum2.nextElement();
                    if (!used_bands.contains(re.getBand())) {
                        used_bands.add(re.getBand());
                    }
                }
                // 2 For any bands, create a vector of elements of this bands order by left position...
                //      2b Set positions using the array....
                Enumeration bands_enum = used_bands.elements();
                while (bands_enum.hasMoreElements()) {

                    Band b = (Band)bands_enum.nextElement();

                    Vector myElements = new Vector();
                    enum2 = getSelectedElements().elements();
                    while (enum2.hasMoreElements()) {
                        re = (ReportElement)enum2.nextElement();
                        if (re.getBand() == b) {
                            // insert this element in the right position...
                            if (myElements.size() == 0) myElements.add(re);
                            else {
                                boolean inserted=false;
                                for (int i=0; i<myElements.size(); ++i) {
                                    ReportElement re2 = (ReportElement)myElements.elementAt(i);
                                    if (re.getPosition().x < re2.getPosition().x) {
                                        myElements.insertElementAt(re, i);
                                        inserted = true;

                                        break;
                                    }
                                }
                                if (!inserted)
                                    myElements.addElement(re);
                            }
                        }
                    }

                    // Repositioning elements...
                    int actual_x =0;
                    for (int k=0; k<myElements.size(); ++k) {
                        re = (ReportElement)myElements.elementAt(k);
                        if (k==0) {
                            actual_x = ((ReportElement)myElements.elementAt(k)).getPosition().x;
                        }

                        Rectangle oldBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                        re.setPosition( new Point(actual_x, re.getPosition().y) );
                        actual_x += re.getWidth();
                        re.updateBounds();
                        Rectangle newBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                        undoOp.addElement(re, oldBounds, newBounds );
                    }
                }
                break;
            case OperationType.JOIN_RIGHT:

                // 1 Search for all different bands...

                used_bands = new Vector();
                enum2 = getSelectedElements().elements();
                while (enum2.hasMoreElements()) {
                    re = (ReportElement)enum2.nextElement();
                    if (!used_bands.contains(re.getBand())) {
                        used_bands.add(re.getBand());
                    }
                }

                // 2 For any bands, create a vector of elements of this bands order by left position...
                //      2b Set positions using the array....
                bands_enum = used_bands.elements();
                while (bands_enum.hasMoreElements()) {

                    Band b = (Band)bands_enum.nextElement();
                    Vector myElements = new Vector();
                    enum2 = getSelectedElements().elements();
                    while (enum2.hasMoreElements()) {
                        re = (ReportElement)enum2.nextElement();
                        if (re.getBand() == b) {
                            // insert this element in the right position...
                            if (myElements.size() == 0) myElements.add(re);
                            else {
                                boolean inserted=false;
                                for (int i=0; i<myElements.size(); ++i) {
                                    ReportElement re2 = (ReportElement)myElements.elementAt(i);
                                    if (re.getPosition().x > re2.getPosition().x) {
                                        myElements.insertElementAt(re, i);
                                        inserted = true;
                                        //System.out.println("Inserito re:" + re.getPosition().x + " pos:" + i );
                                        break;
                                    }
                                }
                                if (!inserted)
                                    myElements.addElement(re);
                            }
                        }
                    }

                    // Repositioning elements...
                    int actual_x =0;
                    for (int k=0; k<myElements.size(); ++k) {
                        re = (ReportElement)myElements.elementAt(k);
                        if (k==0) {
                            actual_x = re.getPosition().x+re.getWidth();
                        }

                        Rectangle oldBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                        re.setPosition( new Point(actual_x-re.getWidth(), re.getPosition().y) );
                        actual_x -= re.getWidth();
                        re.updateBounds();
                        Rectangle newBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                        undoOp.addElement(re, oldBounds, newBounds );
                    }
                }
                break;
            case OperationType.EQUALS_SPACE_H:
                // 1. Use an ordered array...
                if (getSelectedElements().size() <= 1) break;
                Vector myElements = new Vector();

                enum2 = getSelectedElements().elements();
                while (enum2.hasMoreElements()) {
                    re = (ReportElement)enum2.nextElement();
                    // insert this element in the right position...
                    if (myElements.size() == 0) myElements.add(re);
                    else {
                        boolean inserted=false;
                        for (int i=0; i<myElements.size(); ++i) {
                            ReportElement re2 = (ReportElement)myElements.elementAt(i);
                            if (re.getPosition().x < re2.getPosition().x) {
                                myElements.insertElementAt(re, i);
                                inserted = true;
                                //System.out.println("Inserito re:" + re.getPosition().x + " pos:" + i );
                                break;
                            }
                        }
                        if (!inserted)
                            myElements.addElement(re);
                    }
                }



                // Find the rightest element...

                int total_width=0;
                int available_width=0;
                int min_x = 0;
                int max_x = 0;
                re = (ReportElement)myElements.firstElement();
                min_x = re.getPosition().x;
                max_x = re.getPosition().x+re.getWidth();
                enum2 = myElements.elements();
                ReportElement leftElement = re;
                ReportElement rightElement = re;
                while (enum2.hasMoreElements()) {
                    re = (ReportElement)enum2.nextElement();
                    if (min_x > re.getPosition().x) {
                        min_x =re.getPosition().x;
                    }
                    if (max_x < re.getPosition().x+re.getWidth()) {
                        max_x =re.getPosition().x+re.getWidth();
                        rightElement = re;
                    }
                    available_width += re.getWidth();
                }
                available_width = max_x - min_x - available_width;
                available_width /= myElements.size()-1;
                System.out.println("Space: " + available_width);

                int actual_x = leftElement.getPosition().x + leftElement.getWidth() + available_width;
                e = myElements.elements();
                while (e.hasMoreElements()){
                    re = (ReportElement)e.nextElement();
                    if (re == leftElement) continue;
                    if (re == rightElement) continue;

                    Rectangle oldBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    re.setPosition(new Point(actual_x, re.getPosition().y) );
                    re.updateBounds();
                    actual_x += re.getWidth() + available_width;
                    Rectangle newBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    undoOp.addElement(re, oldBounds, newBounds );
                }
                break;
            case OperationType.INCREASE_SPACE_H:
                // 1. Use an ordered array...
                if (getSelectedElements().size() <= 1) break;
                myElements = new Vector();

                enum2 = getSelectedElements().elements();
                while (enum2.hasMoreElements()) {
                    re = (ReportElement)enum2.nextElement();
                    // insert this element in the right position...
                    if (myElements.size() == 0) myElements.add(re);
                    else {
                        boolean inserted=false;
                        for (int i=0; i<myElements.size(); ++i) {
                            ReportElement re2 = (ReportElement)myElements.elementAt(i);
                            if (re.getPosition().x < re2.getPosition().x) {
                                myElements.insertElementAt(re, i);
                                inserted = true;
                                //System.out.println("Inserito re:" + re.getPosition().x + " pos:" + i );
                                break;
                            }
                        }
                        if (!inserted)
                            myElements.addElement(re);
                    }
                }

                e = myElements.elements();

                for (int i=1; i< myElements.size(); ++i){
                    re = (ReportElement)myElements.elementAt(i);
                    Rectangle oldBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    re.setPosition(new Point(re.getPosition().x+5*i, re.getPosition().y) );
                    re.updateBounds();
                    Rectangle newBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    undoOp.addElement(re, oldBounds, newBounds );
                }
                break;
            case OperationType.DECREASE_SPACE_H:
                // 1. Use an ordered array...
                if (getSelectedElements().size() <= 1) break;
                myElements = new Vector();

                enum2 = getSelectedElements().elements();
                while (enum2.hasMoreElements()) {
                    re = (ReportElement)enum2.nextElement();
                    // insert this element in the right position...
                    if (myElements.size() == 0) myElements.add(re);
                    else {
                        boolean inserted=false;
                        for (int i=0; i<myElements.size(); ++i) {
                            ReportElement re2 = (ReportElement)myElements.elementAt(i);
                            if (re.getPosition().x < re2.getPosition().x) {
                                myElements.insertElementAt(re, i);
                                inserted = true;
                                //System.out.println("Inserito re:" + re.getPosition().x + " pos:" + i );
                                break;
                            }
                        }
                        if (!inserted)
                            myElements.addElement(re);
                    }
                }

                e = myElements.elements();

                for (int i=1; i< myElements.size(); ++i){
                    re = (ReportElement)myElements.elementAt(i);
                    Rectangle oldBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    re.setPosition(new Point(Math.max( re.getPosition().x-5*i, ((ReportElement)myElements.elementAt(i-1)).getPosition().x), re.getPosition().y) );
                    re.updateBounds();
                    Rectangle newBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    undoOp.addElement(re, oldBounds, newBounds );
                }
                break;

            case OperationType.DECREASE_SPACE_V:
                // 1. Use an ordered array...
                if (getSelectedElements().size() <= 1) break;
                myElements = new Vector();

                enum2 = getSelectedElements().elements();
                while (enum2.hasMoreElements()) {
                    re = (ReportElement)enum2.nextElement();
                    // insert this element in the right position...
                    if (myElements.size() == 0) myElements.add(re);
                    else {
                        boolean inserted=false;
                        for (int i=0; i<myElements.size(); ++i) {
                            ReportElement re2 = (ReportElement)myElements.elementAt(i);
                            if (re.getPosition().y < re2.getPosition().y) {
                                myElements.insertElementAt(re, i);
                                inserted = true;
                                //System.out.println("Inserito re:" + re.getPosition().x + " pos:" + i );
                                break;
                            }
                        }
                        if (!inserted)
                            myElements.addElement(re);
                    }
                }

                e = myElements.elements();

                for (int i=1; i< myElements.size(); ++i){
                    re = (ReportElement)myElements.elementAt(i);
                    Rectangle oldBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    re.setPosition(new Point(re.getPosition().x, Math.max( re.getPosition().y-5*i, ((ReportElement)myElements.elementAt(i-1)).getPosition().y)) );
                    re.updateBounds();
                    Rectangle newBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    undoOp.addElement(re, oldBounds, newBounds );
                }
                break;
            case OperationType.INCREASE_SPACE_V:
                // 1. Use an ordered array...
                if (getSelectedElements().size() <= 1) break;
                myElements = new Vector();

                enum2 = getSelectedElements().elements();
                while (enum2.hasMoreElements()) {
                    re = (ReportElement)enum2.nextElement();
                    // insert this element in the right position...
                    if (myElements.size() == 0) myElements.add(re);
                    else {
                        boolean inserted=false;
                        for (int i=0; i<myElements.size(); ++i) {
                            ReportElement re2 = (ReportElement)myElements.elementAt(i);
                            if (re.getPosition().y < re2.getPosition().y) {
                                myElements.insertElementAt(re, i);
                                inserted = true;
                                //System.out.println("Inserito re:" + re.getPosition().x + " pos:" + i );
                                break;
                            }
                        }
                        if (!inserted)
                            myElements.addElement(re);
                    }
                }

                e = myElements.elements();

                for (int i=1; i< myElements.size(); ++i){
                    re = (ReportElement)myElements.elementAt(i);
                    Rectangle oldBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    re.setPosition(new Point(re.getPosition().x, re.getPosition().y+5*i) );
                    re.updateBounds();
                    Rectangle newBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    undoOp.addElement(re, oldBounds, newBounds );
                }
                break;
            case OperationType.REMOVE_SPACE_V:
                // 1. Use an ordered array...
                if (getSelectedElements().size() <= 1) break;
                myElements = new Vector();

                enum2 = getSelectedElements().elements();
                while (enum2.hasMoreElements()) {
                    re = (ReportElement)enum2.nextElement();
                    // insert this element in the right position...
                    if (myElements.size() == 0) myElements.add(re);
                    else {
                        boolean inserted=false;
                        for (int i=0; i<myElements.size(); ++i) {
                            ReportElement re2 = (ReportElement)myElements.elementAt(i);
                            if (re.getPosition().y < re2.getPosition().y) {
                                myElements.insertElementAt(re, i);
                                inserted = true;
                                //System.out.println("Inserito re:" + re.getPosition().x + " pos:" + i );
                                break;
                            }
                        }
                        if (!inserted)
                            myElements.addElement(re);
                    }
                }

                e = myElements.elements();

                for (int i=1; i< myElements.size(); ++i){
                    re = (ReportElement)myElements.elementAt(i);
                    Rectangle oldBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    re.setPosition(new Point(re.getPosition().x, ((ReportElement)myElements.elementAt(i-1)).getPosition().y +((ReportElement)myElements.elementAt(i-1)).getHeight() ) );
                    re.updateBounds();
                    Rectangle newBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    undoOp.addElement(re, oldBounds, newBounds );
                }
                break;
            case OperationType.EQUALS_SPACE_V:
                // 1. Use an ordered array...
                if (getSelectedElements().size() <= 1) break;
                myElements = new Vector();

                enum2 = getSelectedElements().elements();
                while (enum2.hasMoreElements()) {
                    re = (ReportElement)enum2.nextElement();
                    // insert this element in the right position...
                    if (myElements.size() == 0) myElements.add(re);
                    else {
                        boolean inserted=false;
                        for (int i=0; i<myElements.size(); ++i) {
                            ReportElement re2 = (ReportElement)myElements.elementAt(i);
                            if (re.getPosition().y < re2.getPosition().y) {
                                myElements.insertElementAt(re, i);
                                inserted = true;
                                //System.out.println("Inserito re:" + re.getPosition().x + " pos:" + i );
                                break;
                            }
                        }
                        if (!inserted)
                            myElements.addElement(re);
                    }
                }



                // Find the rightest element...

                int total_height=0;
                int available_height=0;
                int min_y = 0;
                int max_y = 0;
                re = (ReportElement)myElements.firstElement();
                min_y = re.getPosition().y;
                max_y = re.getPosition().y+re.getHeight();
                enum2 = myElements.elements();
                ReportElement topElement = re;
                ReportElement bottomElement = re;
                while (enum2.hasMoreElements()) {
                    re = (ReportElement)enum2.nextElement();
                    if (min_y > re.getPosition().y) {
                        min_y =re.getPosition().y;
                    }
                    if (max_y < re.getPosition().y+re.getHeight()) {
                        max_y =re.getPosition().y+re.getHeight();
                        bottomElement = re;
                    }
                    available_height += re.getHeight();
                }
                available_height = max_y - min_y - available_height;
                available_height /= myElements.size()-1;

                int actual_y = topElement.getPosition().y + topElement.getHeight() + available_height;
                e = myElements.elements();
                while (e.hasMoreElements()){
                    re = (ReportElement)e.nextElement();
                    if (re == topElement) continue;
                    if (re == bottomElement) continue;

                    Rectangle oldBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    re.setPosition(new Point(re.getPosition().x,actual_y) );
                    re.updateBounds();
                    actual_y += re.getHeight() + available_height;
                    Rectangle newBounds = new Rectangle( re.getPosition().x, re.getPosition().y, re.getWidth(), re.getHeight());
                    undoOp.addElement(re, oldBounds, newBounds );
                }
                break;
        }
        this.getReportPanel().repaint();
        addUndoOperation(undoOp);
    }
    */

    public TextFieldReportElement dropNewTextField(Point newObjectOrigin, String textfieldExpression, String classExpression) {

        return dropNewTextField(newObjectOrigin, textfieldExpression, classExpression, "Now");
    }

    public Band getBandAt(Point newObjectOrigin) {
        Enumeration enum_bands = report.getBands().elements();
        Band bname = null;

        int delta_h = 0;
        delta_h = gridMultiple(18);
        int delta_y = (delta_h<0) ? delta_h : 0;
        int originY = getLogicalDim(  Math.min(newObjectOrigin.y-10+jVerticalScrollBar.getValue(),newObjectOrigin.y+delta_y-10+jVerticalScrollBar.getValue()))+10;

        while (enum_bands.hasMoreElements()) {
            Band b = (Band)enum_bands.nextElement();
            if (report.getBandYLocation(b)+10 <= originY) {
                bname = b;
            }
        }

        return bname;

    }

    public TextFieldReportElement dropNewTextField(Point newObjectOrigin, String textfieldExpression, String classExpression, String time) {
        TextFieldReportElement re = null;
        // Find the band to associate to the new element...

        re = new TextFieldReportElement( 0, 0, 10, 10);
        re.setText(textfieldExpression);
        re.setMatchingClassExpression( classExpression , true);

        if (re.getClassExpression().equals("java.math.BigDecimal") ||
            re.getClassExpression().equals("java.lang.Double") ||
            re.getClassExpression().equals("java.lang.Float"))
        {
            re.setPattern("##0.00");
        }

        re.setEvaluationTime( time );
        return (TextFieldReportElement)dropReportElement(newObjectOrigin,re);
    }

    /**
     * Drop an element into the report.
     * Default dimension: 100x18
     */
    public ReportElement dropReportElement(Point newObjectOrigin, ReportElement re)
    {
        return dropReportElement(newObjectOrigin, re, 100, 18);
    }

    /**
     * Drop an element into the report.
     * Origin: the drop position
     */
    public ReportElement dropReportElement(Point newObjectOrigin, ReportElement re, int defaultWidth, int defaultHeight) {

        Enumeration enum_bands = report.getBands().elements();
        Band bname = null;

        int delta_w = 0;
        int delta_h = 0;
        delta_w = gridMultiple(defaultWidth);
        delta_h = gridMultiple(defaultHeight);
        int delta_x = (delta_w<0) ? delta_w: 0;
        int delta_y = (delta_h<0) ? delta_h : 0;

        int originX = getLogicalDim(   Math.min(newObjectOrigin.x-10+jHorizontalScrollBar.getValue(),newObjectOrigin.x+delta_x-10+jHorizontalScrollBar.getValue()))+10;
        int originY = getLogicalDim(  Math.min(newObjectOrigin.y-10+jVerticalScrollBar.getValue(),newObjectOrigin.y+delta_y-10+jVerticalScrollBar.getValue()))+10;
        int width  =  getLogicalDim(  Math.abs( delta_w) );
        int height =  getLogicalDim( Math.abs(delta_h) );
        while (enum_bands.hasMoreElements()) {
            Band b = (Band)enum_bands.nextElement();
            if (report.getBandYLocation(b)+10 <= originY) {
                bname = b;
            }
        }
        re.setPosition( new Point(originX,originY));
        re.setHeight(height);
        re.setWidth(width);

        re.setBand( bname );

        

        report.getElements().addElement(re);
        
        fireReportListenerReportElementsChanged(new ReportElementChangedEvent(this, re , ReportElementChangedEvent.ADDED));
        addUndoOperation( new  InsertElementOperation(this, re) );
        
        setSelectedElement(re);
        jPanelReport.setCursor( Cursor.getDefaultCursor());
        getMainFrame().setActiveTool(0);

        return re;

    }

    public void moveUp() {
        if (this.getSelectedElements().size() == 0) return;

        Enumeration e = this.getSelectedElements().elements();

        ChangeEmentsOrderOperation undoOp = new ChangeEmentsOrderOperation(this);

        while  (e.hasMoreElements()) {
            ReportElement element = (ReportElement)e.nextElement();

            int oldPosition = getReport().getElements().indexOf(element);
            getReport().moveElementUp( element );
            /*
            getReport().getElements().remove(element);
            int newPosition = Math.max(0, oldPosition - 1);
            getReport().getElements().insertElementAt(element, newPosition);
            */
            int newPosition = getReport().getElements().indexOf(element);
            undoOp.addElement(element,oldPosition, newPosition);
            //getReport().elementGroupResort(element.getBand());
        }

        addUndoOperation(undoOp);
        getReportPanel().repaint();
        // We must update the tree... the order i changed...
        /* TODO */
    }

    public void moveDown() {
        if (getSelectedElements().size() == 0) return;

        ChangeEmentsOrderOperation undoOp = new ChangeEmentsOrderOperation(this);

        Vector selected = getSelectedElements();
        for(int i = selected.size() - 1; i >= 0; i--) {
            ReportElement element = (ReportElement) selected.get(i);

            int oldPosition = getReport().getElements().indexOf(element);

            // 1. We have to find the first element of this band with elementGroup >=
            getReport().moveElementDown(element);


            int newPosition = getReport().getElements().indexOf(element);
            undoOp.addElement(element,oldPosition, newPosition);

            //getReport().elementGroupResort(element.getBand());
        }



        addUndoOperation(undoOp);
        getReportPanel().repaint();

        // We must update the tree... the order i changed...
        /* TODO */
    }

    public void  transformStaticInTextFields() {
        // Begin code Robert Lamping, 13 july 2004
        String keyStaticText = (new StaticTextReportElement(0,0,0,0)).getKey();
        String keyTextField  = (new TextFieldReportElement(0,0,0,0)).getKey();
        // End code Robert Lamping, 13 july 2004


        // Edit static Text
        Enumeration e = selectedElements.elements();

        ReplacedElementsOperation undoOp = new ReplacedElementsOperation(this);

        while (e.hasMoreElements()) {
            ReportElement re = (ReportElement) e.nextElement();
            if(re instanceof StaticTextReportElement){
                // 1.
                TextFieldReportElement tfre = new TextFieldReportElement(re.getPosition().x, re.getPosition().y, re.width, re.height);
                // Set base characteristics...
                tfre.copyBaseReportElement(tfre, re);
                // Set Text characteristics...
                
                ((StaticTextReportElement)re).getIReportFont().clone( tfre.getIReportFont() ); 
                /*
                tfre.setBold(((TextReportElement)re).isBold() );
                tfre.setUnderline( ((TextReportElement)re).isUnderline() );
                tfre.setStrikeTrought( ((TextReportElement)re).isStrikeTrought() );
                tfre.setItalic( ((TextReportElement)re).isItalic() );
                tfre.setLineSpacing( ((TextReportElement)re).getLineSpacing() );
                tfre.setPDFFontName( ((TextReportElement)re).getPDFFontName() );
                tfre.setPdfEncoding( ((TextReportElement)re).getPdfEncoding() );
                tfre.setReportFont( ((TextReportElement)re).getReportFont() );
                tfre.setRotate( ((TextReportElement)re).getRotate() );
                tfre.setTTFFont( ((TextReportElement)re).getTTFFont() );
                tfre.setVerticalAlign( ((TextReportElement)re).getVerticalAlign());
                tfre.setFont( ((TextReportElement)re).getFont() );
                tfre.setFontName( ((TextReportElement)re).getFontName() );
                tfre.setFontSize(((TextReportElement)re).getFontSize() );
                */
                tfre.setClassExpression( "java.lang.String" );

                // Begin code Robert Lamping, 13 july 2004
                // Changing key element name
                if (re.getKey().indexOf( keyStaticText + "-" ) == 0 ) {
                    // key indeed begins with "staticText-"
                    //Okay then request the next available key for a normal field text
                    tfre.setKey( getNextElementKey( keyTextField ) );
                }
                /* else{
                    // key text begins with something else then the standard.
                    // Leave unchanged
                  )
                }
                 */
                // End code Robert Lamping, 13 july 2004

                String te = tfre.getText();
                ExpressionEditor ed = new ExpressionEditor();
                ed.setSubDataset(this.getReport());
                ed.setExpression( "\"" + te  + "\"");
                ed.updateTreeEntries();
                ed.setVisible(true);
                if (ed.getDialogResult() == javax.swing.JOptionPane.OK_OPTION)
                {
                    tfre.setText(ed.getExpression());

                    // 1. remove element
                    int index = getReport().getElements().indexOf( re );
                    getReport().getElements().remove( re );
                    getReport().getElements().add( index, tfre);

                    int index2 = getSelectedElements().indexOf( re );
                    getSelectedElements().remove( re );
                    getSelectedElements().add( index2, tfre);

                    // 2. add element
                    undoOp.addElement( re, tfre);
                    fireReportListenerReportElementsChanged(new ReportElementChangedEvent(this, re , ReportElementChangedEvent.REMOVED));
                    fireReportListenerReportElementsChanged(new ReportElementChangedEvent(this, tfre , ReportElementChangedEvent.ADDED));

                }

            }
        }
        this.addUndoOperation(undoOp);
        fireReportListenerReportElementsSelectionChanged(new ReportElementsSelectionEvent( this, getSelectedElements()));
        jPanelReport.repaint();
    }

    public void pasteStyle() {
        if (this.getSelectedElements().size() == 0) return;
        if (this.getMainFrame().getStyleClipboard() == null) return;

        // Style are relative to:
        ReportElement templateElement = this.getMainFrame().getStyleClipboard();

        PasteStyleOperation undoOp = new PasteStyleOperation(this);

        Enumeration elements = getSelectedElements().elements();
        Vector changedElements = new Vector();
        while (elements.hasMoreElements()) {
            ReportElement re = (ReportElement)elements.nextElement();
            undoOp.addElement(re, re.cloneMe(), templateElement);
            changedElements.add(re);
            applyStyle(re, templateElement);
        }
        this.addUndoOperation(undoOp);
        fireReportListenerReportElementsChanged(new ReportElementChangedEvent(this , changedElements  , ReportElementChangedEvent.CHANGED));
        jPanelReport.repaint();

    }

    public static void applyStyle(ReportElement re, ReportElement templateElement) {
        if (re == null || templateElement == null) return;
        // Colors...
        if (templateElement.getPropertyValue( re.BGCOLOR ) != null)
        {
            re.setBgcolor(templateElement.getBgcolor());
        }
        if (templateElement.getPropertyValue( re.FGCOLOR ) != null)
        {
            re.setFgcolor(templateElement.getFgcolor());
        }

        re.setStyle( templateElement.getStyle() );

        if (re instanceof GraphicReportElement && templateElement instanceof GraphicReportElement ) {
            re.setPropertyValue(((GraphicReportElement)templateElement).FILL, templateElement.getPropertyValue( ((GraphicReportElement)templateElement).FILL ));
            re.setPropertyValue(((GraphicReportElement)templateElement).PEN,  templateElement.getPropertyValue( ((GraphicReportElement)templateElement).PEN ));


        }
        if (re instanceof TextReportElement && templateElement instanceof TextReportElement ) {
            ((TextReportElement)re).setFont(((TextReportElement)templateElement).getFont());

            ((TextReportElement)re).setReportFont(((TextReportElement)templateElement).getReportFont());

            if (  ((TextReportElement)templateElement).getPropertyValue( TextReportElement.VERTICAL_ALIGN) != null)
            {
                ((TextReportElement)re).setVerticalAlign(((TextReportElement)templateElement).getVerticalAlign());
            }
            if (  ((TextReportElement)templateElement).getPropertyValue( TextReportElement.IS_STYLED_TEXT) != null)
            {
                ((TextReportElement)re).setIsStyledText(((TextReportElement)templateElement).isIsStyledText());
            }
            if (  ((TextReportElement)templateElement).getPropertyValue( TextReportElement.LINE_SPACING) != null)
            {
                ((TextReportElement)re).setLineSpacing(((TextReportElement)templateElement).getLineSpacing());
            }

            if (  ((TextReportElement)templateElement).getPropertyValue( TextReportElement.ROTATE) != null)
            {
                ((TextReportElement)re).setRotate(((TextReportElement)templateElement).getRotate());
            }
            if (  ((TextReportElement)templateElement).getPropertyValue( TextReportElement.ALIGN) != null)
            {
                ((TextReportElement)re).setAlign(((TextReportElement)templateElement).getAlign());
            }
            ((TextReportElement)re).setIReportFont(  ( IReportFont) ((TextReportElement)templateElement).getIReportFont().clone() );


        }

    }

    public void copyStyle() {
        // We should copy this element to use it as style template...
        if (this.getSelectedElements().size() == 0) return;
        this.getMainFrame().setStyleClipbardContent( ((ReportElement)this.getSelectedElements().elementAt(0)).cloneMe() );

    }

    public void updateScrollBars() {
        jPanelReportComponentResized(null);
    }

    // Begin code by Robert Lamping, 12 July 2004
    public String getNextElementKey(String keyElement) {
        int maxKey = 0;
        String prefix = keyElement;

        // check if there is already a "-"
        // if so shorten keyElement and assign to prefix
        if ( prefix.indexOf( "-")  > 0 ){
            prefix = prefix.substring(0,  prefix.indexOf("-") );
        }
        prefix = prefix + "-" ;

        int pos = prefix.indexOf( "-") + 1;

        for (Iterator i = report.getElements().iterator(); i.hasNext(); ) {
            ReportElement re = (ReportElement) i.next();
            if ( re.getKey().indexOf( prefix ) == 0 ) {
                try {
                    int elementNum = (int) Integer.valueOf( re.getKey().substring(pos) ).intValue();
                    maxKey = maxKey < elementNum ? elementNum: maxKey;
                } catch (NumberFormatException err) {
                    // do nothing.
                    // string behind contained something else than a number
                }
            }
        }

/*        if (maxKey == 0) {
            if (report.getElements().size() > 0  ) {
                // all keys were renamed to something else
                maxKey = report.getElements().size() ;
            }
            // else: No elements present: all deleted or start of new document.
        }
 */

        return ( prefix + (maxKey+1) ) ;
    }
    // End code by Robert Lamping, 12 July 2004

    // Begin code by Robert Lamping, 16 July 2004
    public void centerSelectedElements( ) {
        centerSelectedElements( null);
    }

    public void centerSelectedElements( Point center ) {

        // This action is done after zooming in or out.
        // logOnConsole ("centerSelectedElements \n");
        if ( getSelectedElements().size() > 0) {
            double minX = 999999999.0;  //a very high value
            double minY = 999999999.0;  //a very high value
            double maxX = 0.0;
            double maxY = 0.0;
            double centerX = 0.0;
            double centerY = 0.0;
            boolean intersects = true;
            Rectangle rect = new Rectangle( realPoint( 0.0, 0.0 ), new Dimension( this.getWidth(), this.getHeight()  ) ) ;

            for (Iterator i = this.getSelectedElements().iterator(); i.hasNext(); ) {
                ReportElement re = (ReportElement) i.next();

                minY = Math.min( minY, re.getPosition().y -10) ;
                minX = Math.min( minX, re.getPosition().x -10);

                maxY = Math.max( maxY, re.getPosition().y + re.getHeight() -10 ) ;
                maxX = Math.max( maxX, re.getPosition().x + re.getWidth() -10 );

            }
            centerX = ((minX + maxX)/2);
            centerY = ((minY + maxY)/2);

            repositionScrollBars( new Point( (int) centerX, (int) centerY));

        } else {
            // test: place object in Center, do not select, zoom in with 200%
            // and check whether is more or less in the center.
            if ( center != null ) {
                repositionScrollBars( center );
            }

        }
        this.setIsDocDirty(false);

    }
    /**
     * Try to reposition the scrollbars so that the point is in the center of the visible screen
     * If it is not possible to center it, this method will at least make sure
     * the point is visible.
     **/

    public void repositionScrollBars(Point center) {
        try{
            if (jHorizontalScrollBar.getVisibleAmount() < jHorizontalScrollBar.getMaximum() )  {
                double horbarvalue = this.jHorizontalScrollBar.getMaximum() * center.x /  this.report.getWidth();
                this.jHorizontalScrollBar.setValue( (int) ( horbarvalue - ( 0.5 * jHorizontalScrollBar.getVisibleAmount()  ))  );
            }
            if (jVerticalScrollBar.getVisibleAmount() < jVerticalScrollBar.getMaximum() )  {
                double verbarvalue = this.jVerticalScrollBar.getMaximum() * center.y /  this.report.getDesignHeight();
                this.jVerticalScrollBar.setValue( (int) ( verbarvalue - ( 0.5 * jVerticalScrollBar.getVisibleAmount() ) )  );
            }
        } catch (Exception ex) {
            /* Do nothing */
        }
    }

    /**
     * calculates center of visible (white) canvas (=report) .
     */
    public Point getCurrentCenter(){
        double centerX = 0.0 ;
        double centerY = 0.0 ;

        if (jVerticalScrollBar.getVisibleAmount() >= jVerticalScrollBar.getMaximum() )  {
            centerY = 0 ; // this.getReport().getDesignHeight() / 2;
        } else {
            centerY  = (double) this.jVerticalScrollBar.getValue();
            centerY += 0.5 * (double) this.jVerticalScrollBar.getVisibleAmount();
            centerY /= (double) this.jVerticalScrollBar.getMaximum();
            centerY *= (double) this.getReport().getDesignHeight();
        }

        if (jHorizontalScrollBar.getVisibleAmount() >= jHorizontalScrollBar.getMaximum() )  {
            //canvas width < frame boundary
            // so take report width
            centerX = 0 ; // this.getReport().getWidth() / 2;
        } else {
            centerX  = (double) this.jHorizontalScrollBar.getValue();
            centerX += 0.5 * (double) this.jHorizontalScrollBar.getVisibleAmount();
            centerX /= (double) this.jHorizontalScrollBar.getMaximum();
            centerX *= (double) this.getReport().getWidth();
        }
        return new Point( (int) centerX, (int) centerY);

    }

    public void logOnConsole(String s) {
        this.getMainFrame().logOnConsole(s);
    }

    public Point realPoint( double deltaX, double deltaY ){;
    double centerX = 0.0;
    double centerY = 0.0;

    centerX  = (double) this.jHorizontalScrollBar.getValue();
    centerX += (double) deltaX;
    centerX /= (double) this.jHorizontalScrollBar.getMaximum();
    centerX *= (double) this.getReport().getWidth();

    centerY  = (double) this.jVerticalScrollBar.getValue();
    centerY += (double) deltaY;
    centerY /= (double) this.jVerticalScrollBar.getMaximum();
    centerY *= (double) this.getReport().getDesignHeight();

    return new Point( (int) centerX, (int) centerY );
    }

    public java.util.Vector getBandElements(Band band) {
        Vector bandElements = new Vector();

        for (Iterator i = getReport().getElements().iterator(); i.hasNext(); ) {
            ReportElement re = (ReportElement) i.next();
            if ( re.getBand().equals(band) ) {
                bandElements.addElement(re);
            }
        }
        return bandElements;
    }


    /**
     * Resize the band, as a result of dragging or double click
     * @param Band band The band you wish to resize
     * @param int delta The change in height (plus or min)
     * @param Boolean include (TRUE: move elements in current band too)
     * @author: Robert Lamping, 24 july 2004
     */
    public void resizeBand( Band band, int delta, boolean include ) {

        BandDraggedOperation undoOp = new BandDraggedOperation(this, band );
        // Adjustband Height.
        band.setHeight( band.getHeight() + delta );
        // pass the change in bandheight to the undo operation
        undoOp.setBandDelta(delta);

        // adjust all elements that are positioned in the remaining bands.
        int edge_y = 0 ;

        if ( include ) {
            edge_y = band.getBandYLocation() + band.getHeight() + delta;
        } else {
            edge_y = band.getBandYLocation() + band.getHeight();
        }

        for (Iterator i = report.getElements().iterator(); i.hasNext(); ) {
            ReportElement re = (ReportElement) i.next();
            if (re.getParentElement() != null) continue;
            //logOnConsole( "re.position : " + re.position + "\n");
            //logOnConsole( "edge_y : " + edge_y + "\n");
            //logOnConsole( "edge_y + 10 : " + (edge_y + 10)  + "\n");
            if (re.position.y >= edge_y + 10 ) {
                // get the current position for the undo operation
                Rectangle oldBounds = re.getBounds();
                re.setPosition( new Point( re.getPosition().x, re.getPosition().y + delta) );
                // get the new position for the undo operation
                Rectangle newBounds = re.getBounds();
                // add another task to the undo operation.
                undoOp.addElement(re, oldBounds, newBounds );
            }
        }
        // Pass all undo tasks to the undo cluster.
        addUndoOperation(undoOp);
        
        fireReportListenerReportBandChanged(new ReportBandChangedEvent(this,band, ReportBandChangedEvent.CHANGED));
    }

    /**
     * Shrink the band to just hold the elements in it
     * No action if no elements present in band.
     * @param Band band The band you wish to shrink
     * @author: Robert Lamping, 24 july 2004
     */
    public void shrinkBand( Band band ) {

        int maxY = 0;
        // Go through the list of elements and add elements if they belong to the band
        for (Iterator i = getBandElements( band ).iterator(); i.hasNext(); ) {
            ReportElement re = (ReportElement) i.next();
            maxY = Math.max( maxY , re.position.y + re.height );
            if (re instanceof LineReportElement) {
                /* a line can not be displayed if position on a band boundary */
                /* A lines height ==0 , that is why */
                maxY = Math.max( maxY , re.position.y + re.height + 1);

            } else {
                maxY = Math.max( maxY , re.position.y + re.height );

            }
        }

        if (maxY > 0 ) {
            /* There was an element in the band, so changing the band height is allowed */
            // Calculate delta
            int delta = maxY -  band.getBandYLocation() - 10 - band.getHeight();
            resizeBand( band , delta, false);  // false: do not included items in the band.
            // Work done. nothing else
        }
    }


    public Point straighten( int delta_w, int delta_h  ){
        // straighten on 90 % degrees
        double clipAngle = 15;

        double R = Math.sqrt(  (delta_w * delta_w) + (delta_h*delta_h) );
        double cos =  delta_w /  R;
        double angle = 180 * Math.acos( cos )/ Math.PI ;

        int repeat = (int) Math.floor((angle + (0.5 * clipAngle)) / clipAngle) ;

        double newAngle = repeat * clipAngle ;

        // keep the sign the same!
        delta_h =  (int) (Math.abs(( R * Math.sin( (newAngle / 180 )* Math.PI))) * delta_h / Math.abs(delta_h) );

        delta_w = (int) ( Math.abs(( R * Math.cos( (newAngle / 180) * Math.PI)))  * delta_w / Math.abs(delta_w) ) ;

        // aligning on  the grid if necessary
        if (Math.abs(delta_h) == Math.abs(delta_w) ) {
            delta_h = gridMultiple( delta_h);
            delta_w = gridMultiple( delta_w);
        } else {
            if (delta_w == 0) {
                delta_h = gridMultiple( delta_h);
            }
            if (delta_h == 0) {
                delta_w = gridMultiple( delta_w);
            }
        }

        return new Point( delta_w, delta_h);
    }

    public java.awt.Image getImage()
    {
        java.awt.image.BufferedImage img = new BufferedImage(getReport().getWidth()*2+20, getReport().getDesignHeight()*2+20, BufferedImage.TYPE_INT_ARGB);
        GraphicsEnvironment grenv = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Graphics2D g2d = grenv.createGraphics(img );
        int oldVscroll = jVerticalScrollBar.getValue();
        int oldHscroll = jHorizontalScrollBar.getValue();

        jVerticalScrollBar.setValue(0);
        jHorizontalScrollBar.setValue(0);

        double oldZoom = this.zoomFactor;
        this.zoomFactor = 2;

        try {

            g2d.setClip(0,0,getReport().getWidth()*2+20, getReport().getDesignHeight()*2+20);
            this.paintReportPanel(g2d);
            /*
            com.keypoint.PngEncoder png_enc = new com.keypoint.PngEncoder(img);
            byte[] buf = png_enc.pngEncode();
            java.io.FileOutputStream fos = new java.io.FileOutputStream("C:\\tmp.png");
            fos.write(buf);
            fos.close();
            */
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        this.zoomFactor = oldZoom;
        jVerticalScrollBar.setValue(oldVscroll);
        jHorizontalScrollBar.setValue(oldHscroll);

        return img;

    }

    public java.util.List getOpenedNodesDocumentStructure() {
        return openedNodesDocumentStructure;
    }

    public void setOpenedNodesDocumentStructure(java.util.List openedNodesDocumentStructure) {
        this.openedNodesDocumentStructure = openedNodesDocumentStructure;
    }

    public void addCrosstabEditor( CrosstabReportElement re)
    {
        CrosstabEditor crosstabEditor = new CrosstabEditor(re);
        jTabbedPane1.addTab(re.getName(), new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/crosstab.png")),crosstabEditor);
        tabs++;
        updateTabs();
    }

    public CrosstabEditor getCrosstabEditor( CrosstabReportElement re)
    {
       if (re != null)
        {
            for (int i=1; i<jTabbedPane1.getTabCount(); ++i)
            {
                if ( jTabbedPane1.getComponentAt(i) instanceof CrosstabEditor)
                {
                    CrosstabEditor ce= (CrosstabEditor)jTabbedPane1.getComponentAt(i);
                    if (ce.getCrosstabElement() == re)
                    {
                        return ce;
                    }
                }
            }
        }
       return null;
    }

    public void removeCrosstabEditor(CrosstabReportElement re)
    {


        if (re != null)
        {
            for (int i=1; i<jTabbedPane1.getTabCount(); ++i)
            {
                if ( jTabbedPane1.getComponentAt(i) instanceof CrosstabEditor)
                {
                    CrosstabEditor ce= (CrosstabEditor)jTabbedPane1.getComponentAt(i);
                    if (ce.getCrosstabElement() == re)
                    {
                        jTabbedPane1.remove(i);
                        tabs--;
                        break;
                    }
                }
            }
        }

        updateTabs();

    }

    public void updateTabs()
    {
       if (tabs == 0 && jTabbedPane1.isVisible())
       {
            jTabbedPane1.setVisible(false);
            getContentPane().remove(jTabbedPane1);
            getContentPane().add(jPanelSuperContainer, java.awt.BorderLayout.CENTER);
            jPanelSuperContainer.updateUI();
       }
       else if (tabs > 0 && !jTabbedPane1.isVisible())
       {
            jTabbedPane1.setVisible(true);
            getContentPane().remove(jPanelSuperContainer);
            jTabbedPane1.insertTab(I18n.getString("jReportFrame.mainReport","Main report"),
                    new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/mainreport.png")),
                    jPanelSuperContainer,
                    I18n.getString("jReportFrame.mainReport.tooltip","Main report editor"),
                    0);
            jTabbedPane1.setSelectedIndex(0);
            getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);
            jTabbedPane1.updateUI();
       }

       this.updateUI();
       // This trick force the recalculation of the window position
       try {
           if (this.isMaximum())
           {
               //this.setMaximum(false);
               //this.setMaximum(true);
           }
       } catch (Exception ex)
       {
            ex.printStackTrace();
       }
    }


    public CrosstabEditorPanel getSelectedCrosstabEditorPanel()
    {
        if (tabs > 0)
        {
            int i = jTabbedPane1.getSelectedIndex();
            if ( jTabbedPane1.getComponentAt(i) instanceof CrosstabEditor)
            {
                CrosstabEditor ce= (CrosstabEditor)jTabbedPane1.getComponentAt(i);
                return ce.getPanelEditor();
            }
        }
        return null;
    }
    //Added by Felix Firgau on 9th Feb 2006
    public void applyI18n() {
        // Start autogenerated code ----------------------
        jMenuItemRightMargin.setText(I18n.getString("jReportFrame.menuItemRightMargin","Join right page margin"));
        jMenuItemLeftMargin.setText(I18n.getString("jReportFrame.menuItemLeftMargin","Join left page margin"));
        jMenuItemBandProperties1.setText(I18n.getString("jReportFrame.menuItemBandProperties1","Band properties"));
        jMenuItemElementChartProperties.setText(I18n.getString("jReportFrame.menuItemElementChartProperties","Chart properties"));
        jMenuItemElementCrosstabDesignProperties.setText(I18n.getString("jReportFrame.menuItemElementCrosstabDesignProperties","Go to crosstab design tab...."));
        jMenuItemElementCrosstabProperties.setText(I18n.getString("jReportFrame.menuItemElementCrosstabProperties","Crosstab properties"));
        jMenuItemElementOpenSubreport.setText(I18n.getString("jReportFrame.menuItemElementOpenSubreport","Open subreport"));
        jMenuItemEditExpression.setText(I18n.getString("jReportFrame.menuItemElementEditExpression","Edit Expression"));
        
        // End autogenerated code ----------------------
        jMenuItemPasteOnBand.setText(it.businesslogic.ireport.util.I18n.getString("pasteHere", "Paste here"));
        jMenuItemBandProperties.setText(it.businesslogic.ireport.util.I18n.getString("bandProperties", "Band properties"));
        jMenuItemCut.setText(it.businesslogic.ireport.util.I18n.getString("cut", "Cut"));
        jMenuItemCopy.setText(it.businesslogic.ireport.util.I18n.getString("copy", "Copy"));
        jMenuItemPaste.setText(it.businesslogic.ireport.util.I18n.getString("paste", "Paste"));
        jMenuItemDelete.setText(it.businesslogic.ireport.util.I18n.getString("delete", "Delete"));
        jMenuItemGroup.setText(it.businesslogic.ireport.util.I18n.getString("groupSelectedElements", "Group selected elements"));
        jMenuItemUngroup.setText(it.businesslogic.ireport.util.I18n.getString("ungroupSelectedElements", "Ungroup selected elements"));
        jMenuItemCopyStyle.setText(it.businesslogic.ireport.util.I18n.getString("copyStyle", "Copy style"));
        jMenuItemPasteStyle.setText(it.businesslogic.ireport.util.I18n.getString("pasteStyle", "Paste style"));
        jMenuItemElementProperties.setText(it.businesslogic.ireport.util.I18n.getString("properties", "Properties"));
        jMenuItemPattern.setText(it.businesslogic.ireport.util.I18n.getString("fieldPattern", "Field pattern"));

        if (jTabbedPane1.getTabCount() >= 1)
        {
            jTabbedPane1.setTitleAt(0, I18n.getString("jReportFrame.mainReport","Main report"));
            jTabbedPane1.setToolTipTextAt(0, I18n.getString("jReportFrame.mainReport.tooltip","Main report editor"));
        }
    }

    public void languageChanged(LanguageChangedEvent evt) {

      this.applyI18n();
    }//End



    /**
     * This method returns the correct mouse x checking for magnetic points like
     * a grid or a magnetic line...
     * The magnetism affects ever only the main element in a selection, in this way all elements
     * in a selection share the same delta_x and delta_y in a transformation.
     *
     *  mouse_x = the current position of the mouse
     *  original_x = the original mouse position when the transformation started...
     */
    protected int optimizeX(int mouse_x, int original_x, int transformation_type, ReportElement referenceElement)
    {
          int real_o_x = getLogicalDim((int)(original_x + jHorizontalScrollBar.getValue() - 10));
          int real_x = getLogicalDim(mouse_x + jHorizontalScrollBar.getValue() - 10);
          int gz = getGridSize();
          int x_delta = real_x - real_o_x;

          // 1. Calc delta between the current position and the first good snap...
          if (  transformation_type == TransformationType.TRANSFORMATION_MOVE ||
                transformation_type == TransformationType.TRANSFORMATION_RESIZE_NW ||
                transformation_type == TransformationType.TRANSFORMATION_RESIZE_SW  ||
                transformation_type == TransformationType.TRANSFORMATION_RESIZE_W) {
                // Get the minimum x to bring the left corner on a valid magnetic line or
                // on the grid...
                 int x_mag_ele = closestXMagneticElement( referenceElement.getPosition().x + x_delta, gz, jHorizontalRule.getGuideLines());
                 x_delta = x_mag_ele - referenceElement.getPosition().x;
          }
          else if (transformation_type == TransformationType.TRANSFORMATION_RESIZE_NE ||
                transformation_type == TransformationType.TRANSFORMATION_RESIZE_SE  ||
                transformation_type == TransformationType.TRANSFORMATION_RESIZE_E) {

               int x_mag_ele = closestXMagneticElement( referenceElement.getPosition().x + referenceElement.getWidth() + x_delta, gz, jHorizontalRule.getGuideLines());
               x_delta = x_mag_ele - referenceElement.getPosition().x - referenceElement.getWidth();

          }


          real_x = real_o_x + x_delta;

          if (real_x != real_o_x)
          {
            return  10 - jHorizontalScrollBar.getValue() + getZoomedDim(real_x);
          }

          return original_x;
    }

    /**
     * Find the closest grid or magnetic line to this x...
     * * x must be in logical size + 10
     */
    private int closestXMagneticElement(int x, int gsize, java.util.List magneticLines)
    {
        // If using grid....
        int optimizedx = x;
        matchedVerticalLine = -1;

        if (magneticLines != null)
        {
            for (int i=0; i<magneticLines.size(); ++i)
            {
                int line = ((Integer)magneticLines.get(i)).intValue()+10;
                if (x < line + MAGNETIC_POWER && x > line - MAGNETIC_POWER) return line;
            }
        }

        if (isSnapToGrid())
        {
            optimizedx = x - 10;
            int grid_positions = optimizedx / gsize;
            int rest = optimizedx % gsize;
            if (rest > gsize/2) grid_positions++;
            optimizedx = 10 + (grid_positions*gsize);
            return optimizedx;
        }

        // If using magnetic lines....
        // Check for magnetic lines....

        // Snap to objects...
        if (verticalObjectsLines != null)
        {
            for (int i=0; i<verticalObjectsLines.size(); ++i)
            {
                int line = ((Integer)verticalObjectsLines.get(i)).intValue();
                
                if (x < line + MAGNETIC_POWER && x > line - MAGNETIC_POWER) 
                {
                    matchedVerticalLine = line;
                    return line;
                }
            }
        }
        
        return x;
    }


    /**
     * This method returns the correct mouse x checking for magnetic points like
     * a grid or a magnetic line...
     * The magnetism affects ever only the main element in a selection, in this way all elements
     * in a selection share the same delta_x and delta_y in a transformation.
     *
     *  mouse_x = the current position of the mouse
     *  original_x = the original mouse position when the transformation started...
     */
    protected int optimizeY(int mouse_y, int original_y, int transformation_type, ReportElement referenceElement)
    {
          int real_o_y = getLogicalDim((int)(original_y + jVerticalScrollBar.getValue() - 10));
          int real_y = getLogicalDim(mouse_y + jVerticalScrollBar.getValue() - 10);
          int gz = getGridSize();
          int y_delta = real_y - real_o_y;

          // 1. Calc delta between the current position and the first good snap...
          if (  transformation_type == TransformationType.TRANSFORMATION_MOVE ||
                transformation_type == TransformationType.TRANSFORMATION_RESIZE_NW ||
                transformation_type == TransformationType.TRANSFORMATION_RESIZE_NE  ||
                transformation_type == TransformationType.TRANSFORMATION_RESIZE_N) {
                // Get the minimum x to bring the left corner on a valid magnetic line or
                // on the grid...
                 int y_mag_ele = closestYMagneticElement( referenceElement.getPosition().y + y_delta, gz, jVerticalRule.getGuideLines());
                 y_delta = y_mag_ele - referenceElement.getPosition().y;
          }
          else if (transformation_type == TransformationType.TRANSFORMATION_RESIZE_SE ||
                transformation_type == TransformationType.TRANSFORMATION_RESIZE_SW  ||
                transformation_type == TransformationType.TRANSFORMATION_RESIZE_S) {

               int y_mag_ele = closestYMagneticElement( referenceElement.getPosition().y + referenceElement.getHeight() + y_delta, gz, jVerticalRule.getGuideLines());
               y_delta = y_mag_ele - referenceElement.getPosition().y - referenceElement.getHeight();

          }


          real_y = real_o_y + y_delta;

          if (real_y != real_o_y)
          {
            return  10 - jVerticalScrollBar.getValue() + getZoomedDim(real_y);
          }

          return original_y;
    }

    /**
     * Find the closest grid or magnetic line to this x...
     * x must be in logical size + 10
     */
    private int closestYMagneticElement(int y, int gsize, java.util.List magneticLines)
    {
        // If using grid....
        int optimizedy = y;
        
        matchedHorizontalLine = -1;

        if (magneticLines != null)
        {
            for (int i=0; i<magneticLines.size(); ++i)
            {
                int line = ((Integer)magneticLines.get(i)).intValue()+10;
                if (y < line + MAGNETIC_POWER && y > line - MAGNETIC_POWER) return line;
            }
        }
        
        if (isSnapToGrid())
        {
            optimizedy = y - 10;
            int grid_positions = optimizedy / gsize;
            int rest = optimizedy % gsize;
            if (rest > gsize/2) grid_positions++;
            optimizedy = 10 + (grid_positions*gsize);
            return optimizedy;
        }

        // Snap to objects...
        
        if (horizontalObjectsLines != null)
        {
            for (int i=0; i<horizontalObjectsLines.size(); ++i)
            {
                int line = ((Integer)horizontalObjectsLines.get(i)).intValue();
                
                if (y < line + MAGNETIC_POWER && y > line - MAGNETIC_POWER) 
                {
                    matchedHorizontalLine = line;
                    return line;
                }
            }
        }
        
        // If using magnetic lines....
        // Check for magnetic lines....

        return y;
    }

    /**
     * On close event.
     * Please return true on force.
     * Return false to stop the window close.
     *
     * Overrided from JMDIFrame
     */
    public boolean closingFrame(boolean force)
    {
            if (force) return true;
            JReportFrame jrf = this;
            MainFrame.getMainInstance().setActiveReportForm( jrf );

            boolean saveIt = false;
            if (jrf.getReport().isModified() && !MainFrame.getMainInstance().getProperties().getProperty("AskToSave","true").equals("false"))
            {
                String message = I18n.getString("messages.jReportFrame.saveReport", "Would you like to save the report before exiting?");
                String caption = I18n.getString("messages.jReportFrame.unsavedFileUnchanged",  "Unsaved file (Unchanged)");
                if (jrf.getReport().isModified())
                {
                    caption = I18n.getString("messages.jReportFrame.unsavedFileChanged",  "Unsaved file (Changed)");
                }

                int ret = javax.swing.JOptionPane.showConfirmDialog(this, message, caption, javax.swing.JOptionPane.YES_NO_CANCEL_OPTION);

                switch(ret)
                {
                    case javax.swing.JOptionPane.YES_OPTION:
                        saveIt = true;
                        break;
                    case javax.swing.JOptionPane.NO_OPTION:
                        saveIt = false;
                        break;
                    default:
                        return false;
                }
            }

            if (saveIt)
            {
                java.awt.event.ActionEvent ae = new java.awt.event.ActionEvent(this,0, "");
                MainFrame.getMainInstance().jMenuItemSaveActionPerformed(ae);
                if (jrf.getReport().isModified()) return false;
            }
            return true;
    }

    public int getHScroll()
    {
        return jHorizontalScrollBar.getValue();
    }

    public int getVScroll()
    {
        return jVerticalScrollBar.getValue();
    }

    public void repaintRules()
    {
        jHorizontalRule.repaint();
        jVerticalRule.repaint();
    }

    public void updateGridSize(int newValue)
    {
        this.repaint();
    }

    public void addChildElements(ReportElement parentRe, Vector elements)
    {
        Enumeration e = getReport().getElements().elements();
        while (e.hasMoreElements()) {
            ReportElement re = (ReportElement)e.nextElement();

            if (re.getParentElement() == parentRe && !elements.contains(re))
            {
                elements.add(re);
                if (re instanceof FrameReportElement)
                {
                    // Add all child elements ..
                    addChildElements(re, elements);
                }
            }
        }
    }

    public java.util.List getReportProblems() {
        return reportProblems;
    }

    public void setReportProblems(java.util.List reportProblems) {
        this.reportProblems = reportProblems;
    }
    
    
    public Vector getSelectedBands() {
        return selectedBands;
    }

    public void setSelectedBands(Vector selectedBands) {
        this.selectedBands = selectedBands;
        fireReportListenerReportBandsSelectionChanged(new ReportBandsSelectionEvent(this, null, getSelectedBands() ));
    }
    
    /**
     *  Returns the objects selected in the document structure
     */
    public Vector getSelectedObjects() {
        return selectedObjects;
    }

    /**
     *  Set the selected objects. A ReportObjectSelectionEvent is fired...
     */
    public void setSelectedObjects(Vector selectedObjects) {
        this.selectedObjects = selectedObjects;
        fireReportListenerReportObjectsSelectionChanged(new ReportObjectsSelectionEvent(this, null, getSelectedObjects() ));
    }

    private java.util.List getAlignMatches(Rectangle bounds) {
        
        java.util.List list = new java.util.ArrayList();
        
        java.util.List listHorizontals = new java.util.ArrayList();
        java.util.List listVerticals = new java.util.ArrayList();
        
        // 1. transform the bound in a real rectangle...
        int originX = getLogicalDim(bounds.x-10+jHorizontalScrollBar.getValue())+10;
        int originY = getLogicalDim(bounds.y-10+jVerticalScrollBar.getValue())+10;
        int width  =  getLogicalDim(bounds.width);
        int height =  getLogicalDim(bounds.height);

        
        if (matchedVerticalLine > 0)
        {
            
            int minY = originY;
            int maxY = originY + height;
            // find the minimum value for X where Y = matchedVerticalLine....
            Enumeration enum_ele = this.getReport().getElements().elements();
            while (enum_ele.hasMoreElements())
            {
                ReportElement re = (ReportElement)enum_ele.nextElement();
                if (getSelectedElements().contains(re)) continue;
            
                if (re.getPosition().x == matchedVerticalLine ||
                    re.getPosition().x + re.getWidth() == matchedVerticalLine) // Same X
                {
                    minY = Math.min(re.getPosition().y, minY);
                    maxY = Math.max(re.getPosition().y + re.getHeight(), maxY);
                }
            }
            int x = getZoomedDim(matchedVerticalLine-10) + 10 - jHorizontalScrollBar.getValue();
            Line2D line = new Line2D.Double( x,
                             10 - jVerticalScrollBar.getValue() + getZoomedDim(minY-10)-20,
                             x,
                             10 - jVerticalScrollBar.getValue() + getZoomedDim(maxY-10)+20);
            list.add(line);  
            matchedVerticalLine = -1;
        }
        
        if (matchedHorizontalLine > 0)
        {
            
            int minX = originX;
            int maxX = originX + width;
            // find the minimum value for X where Y = matchedVerticalLine....
            Enumeration enum_ele = this.getReport().getElements().elements();
            while (enum_ele.hasMoreElements())
            {
                ReportElement re = (ReportElement)enum_ele.nextElement();
                if (getSelectedElements().contains(re)) continue;
            
                if (re.getPosition().y == matchedHorizontalLine ||
                    re.getPosition().y + re.getHeight() == matchedHorizontalLine) // Same Y
                {
                    minX = Math.min(re.getPosition().x, minX);
                    maxX = Math.max(re.getPosition().x + re.getWidth(), maxX);
                }
            }
            int y = 10 - jVerticalScrollBar.getValue() + getZoomedDim(matchedHorizontalLine-10);
            Line2D line = new Line2D.Double( getZoomedDim(minX-10) + 10 - jVerticalScrollBar.getValue()-20,
                             y,
                             getZoomedDim(maxX-10) + 10 - jVerticalScrollBar.getValue()+20,
                             y);
            list.add(line);  
            matchedHorizontalLine = -1;
        }
        
        return list;
    }
    
    public void redrawReferenceGuides(Graphics gg)
    {
        if (paintedAlignLines.size() > 0)
        {
            try {
                
                gg.setXORMode(new Color(112,91,22));
                Stroke st = ((Graphics2D)gg).getStroke();
                ((Graphics2D)gg).setStroke( ReportElement.getPenStroke("Dotted",1) );

                for (int dj=0; dj<paintedAlignLines.size(); ++dj)
                {
                    java.awt.geom.Line2D line = (java.awt.geom.Line2D)paintedAlignLines.get(dj);
                    ((Graphics2D)gg).draw(line);
                    //System.out.println("Removed" + line.getP1() + " " + line.getP2());
                }
                ((Graphics2D)gg).setStroke(st);
            } catch (Exception ex)
            {

            }
        }
        
        gg.setXORMode(Color.WHITE);
    }

    BufferedImage offscreenImage = null;
    BufferedImage offscreenImage2 = null;
    Graphics2D offscreenImage2g = null;
    private void initOffscreenImage() {
        offscreenImage = null;
        offscreenImage2 = null;
        int w = jPanelReport.getWidth();
        int h = jPanelReport.getHeight();
        offscreenImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        offscreenImage2 = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        GraphicsEnvironment grenv = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Graphics2D g2d = grenv.createGraphics(offscreenImage );
        offscreenImage2g = grenv.createGraphics(offscreenImage2 );
        this.paintReportPanel(g2d);
        g2d.dispose();
        
        //jPanelReport.getGraphics().drawImage(offscreenImage,0,0,null);
        
        //jPanelReport.getGraphics().drawLine(0,0,400,400);
    }


    private boolean redrawWithBufferedImage =false;

    public boolean isRedrawWithBufferedImage() {
        return redrawWithBufferedImage;
    }

    public void setRedrawWithBufferedImage(boolean b) {
        
        if (b && !isRedrawWithBufferedImage())
        {
            initOffscreenImage();
        }
        this.redrawWithBufferedImage = redrawWithBufferedImage;
    }

}

