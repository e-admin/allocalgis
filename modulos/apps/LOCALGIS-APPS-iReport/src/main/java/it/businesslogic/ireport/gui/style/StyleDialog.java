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
 * StyleDialog.java
 * 
 * Created on 9 maggio 2003, 17.25
 *
 */

package it.businesslogic.ireport.gui.style;
import it.businesslogic.ireport.ConditionedStyle;
import it.businesslogic.ireport.CrosstabReportElement;
import it.businesslogic.ireport.IReportFont;
import it.businesslogic.ireport.JasperTemplate;
import it.businesslogic.ireport.Report;
import it.businesslogic.ireport.ReportElement;
import it.businesslogic.ireport.Style;
import it.businesslogic.ireport.SubDataset;
import it.businesslogic.ireport.TemplateStyle;
import it.businesslogic.ireport.UndefinedStyle;
import it.businesslogic.ireport.gui.ExpressionEditor;
import it.businesslogic.ireport.gui.JRParameterDialog;
import it.businesslogic.ireport.gui.MainFrame;
import it.businesslogic.ireport.gui.event.SheetPropertyValueChangedListener;
import it.businesslogic.ireport.gui.event.StyleChangedEvent;
import it.businesslogic.ireport.gui.sheet.CategorySheetPanel;
import it.businesslogic.ireport.gui.sheet.ComboBoxSheetProperty;
import it.businesslogic.ireport.gui.sheet.FontSheetProperty;
import it.businesslogic.ireport.gui.sheet.SheetProperty;
import it.businesslogic.ireport.gui.sheet.Tag;
import it.businesslogic.ireport.util.I18n;

import java.awt.event.ActionEvent;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;

/**
 *
 * @author  Administrator
 */
public class StyleDialog extends javax.swing.JDialog  implements SheetPropertyValueChangedListener {

    /**
     * If libraryStyle is set to true, this means that you are not editing a report style,
     * but a style that belongs to the style library. In this case no events are generated on
     * completation and the modified Style is available with the method getStyle.
     *
     * The default value for this attribute is FALSE.
     */
    private boolean libraryStyle = false;
    
    /**
     *  The jasperTemplate is used to disable conditional styles
     *  and check for duplicated names...
     */
    private JasperTemplate jasperTemplate = null;

    private boolean readOnly = false;

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        //for (int i=0; i<sheetPanel.getProperties().size(); ++i)
        //{
        //    ((SheetProperty)sheetPanel.getProperties().get(i)).setReadOnly(readOnly);
        //}
        jButtonOK.setEnabled(!readOnly);
    }
    
    /**
     * A reference to the style appearing in the sheet. Could be the master or
     * a style referring to a condition
     */
    private Style style = null;

    /**
     * A reference to the style master
     */
    private Style masterStyle = null;

    /**
     * A reference to the style we are modifying. We work on a copy (masterStyle)
     */
    private Style editingStyle = null;

    /**
     * Prevent some events during initializazion...
     */
    private boolean init = false;

    /**
     * Panel showing a sample of the current style
     */
    JPanelStyleSample panelSample = new JPanelStyleSample();


    private CategorySheetPanel sheetPanel = null;


    private IReportFont ireportFont = null;
    private Report currentReport = null;


    //** Creates new dialog StyleDialog */
    public StyleDialog(java.awt.Dialog parent, boolean modal) {
        super(parent,modal);
        initAll();
    }

    /** Creates new dialog ChartPropertiesDialog */
    public StyleDialog(java.awt.Frame parent, boolean modal) {
        super(parent,modal);
        initAll();
    }

    public void initAll()
    {
        initComponents();

        style = new Style();

        setMasterStyle(style);
        applyI18n();
        //this.setSize(420, 620);
        this.pack();

        // Open in center...
        it.businesslogic.ireport.util.Misc.centerFrame(this);

        java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.fill = gridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, -1, -1, -1);
        sheetPanel = new CategorySheetPanel();
        sheetPanel.addSheetPropertyValueChangedListener(this);

        jPanelProperties.add(sheetPanel, gridBagConstraints);

        //applyI18n();

        // Add styles....
        
        //
        String category_name = I18n.getString("styleDialog.tabStyle","Style");
        //
        SheetProperty customizerClassProperty = new SheetProperty( Style.ATTRIBUTE_name  ,
        																I18n.getString("styleDialog.sheetProperty.customizerClass","Style name"),
        																//"Style name",
        																SheetProperty.STRING);
        sheetPanel.addSheetProperty(category_name, customizerClassProperty);

        SheetProperty isDefaultProperty = new SheetProperty(Style.ATTRIBUTE_isDefault,
        													I18n.getString("styleDialog.sheetProperty.isDefault","Default style"),
        													//"Default style", 
        													SheetProperty.BOOLEAN);
        sheetPanel.addSheetProperty(category_name, isDefaultProperty);

        ComboBoxSheetProperty styleProperty = new ComboBoxSheetProperty(Style.ATTRIBUTE_style,
        		I18n.getString("styleDialog.sheetProperty.style","Parent style"));
        //"Parent style");
        
        sheetPanel.addSheetProperty(category_name, styleProperty);
        
        ((JComboBox)styleProperty.getEditor()).setEditable(true);
        
        //
        category_name = I18n.getString("styleDialog.tabCommon","Common");
        //
        SheetProperty modeProperty = new SheetProperty(Style.ATTRIBUTE_mode,
        		I18n.getString("styleDialog.sheetProperty.mode","Mode"),
        		//"Mode", 
        		SheetProperty.COMBOBOX);
        
        modeProperty.setTags( new Tag[]{ 
                            new Tag(null, I18n.getString("styleDialog.sheetProperty.mode.default","Default")), 
                            new Tag("Opaque", I18n.getString("styleDialog.sheetProperty.mode.opaque","Opaque")),
                            new Tag("Transparent", I18n.getString("styleDialog.sheetProperty.mode.transparent","Transparent"))});
                            //new Tag(null,"Default"), 
                            //new Tag("Opaque","Opaque"),
                            //new Tag("Transparent","Transparent")});
                            //
        sheetPanel.addSheetProperty(category_name, modeProperty);

        SheetProperty forecolorProperty = new SheetProperty(Style.ATTRIBUTE_forecolor,
           		I18n.getString("styleDialog.sheetProperty.forecolor","Forecolor"),
        		//"Forecolor", 
        		SheetProperty.COLOR);
        forecolorProperty.setValue( null );
        sheetPanel.addSheetProperty(category_name, forecolorProperty);

        SheetProperty backcolorProperty = new SheetProperty(Style.ATTRIBUTE_backcolor,
           		I18n.getString("styleDialog.sheetProperty.backcolor","Backcolor"),
        		//"Backcolor", 
        		SheetProperty.COLOR);
        backcolorProperty.setValue( null );
        sheetPanel.addSheetProperty(category_name, backcolorProperty);

        //
        category_name = I18n.getString("styleDialog.tabGraphics","Graphics");
        //
        SheetProperty penProperty = new SheetProperty(Style.ATTRIBUTE_pen,
										        		I18n.getString("styleDialog.sheetProperty.pen","Pen"),
										        		//"Pen", 
										        		SheetProperty.COMBOBOX);
        penProperty.setTags( new Tag[]{
        									//
        									new Tag(null, I18n.getString("styleDialog.sheetProperty.pen.default","Default")),
        									new Tag("Thin", I18n.getString("styleDialog.sheetProperty.pen.thin","Thin")),
        									new Tag("1Point", I18n.getString("styleDialog.sheetProperty.pen.1Point","1Point")),
        									new Tag("2Point", I18n.getString("styleDialog.sheetProperty.pen.2Point","2Point")),
        									new Tag("4Point", I18n.getString("styleDialog.sheetProperty.pen.4Point","4Point")),
        									new Tag("Dotted", I18n.getString("styleDialog.sheetProperty.pen.dotted","Dotted"))});
											//new Tag(null,"Default"),
        									//new Tag("Thin","Thin"),
											//new Tag("1Point","1Point"),
											//new Tag("2Point","2Point"),
											//new Tag("4Point","4Point"),
											//new Tag("Dotted","Dotted")});
        									//
        sheetPanel.addSheetProperty(category_name, penProperty);

        SheetProperty fillProperty = new SheetProperty(Style.ATTRIBUTE_fill,
									        		I18n.getString("styleDialog.sheetProperty.fill","Fill"),
									        		//"Fill", 
									        		SheetProperty.COMBOBOX);
        fillProperty.setTags( new Tag[]{ 
        								//
        								new Tag(null, I18n.getString("styleDialog.sheetProperty.fill.default","Default")),
        								new Tag("Solid", I18n.getString("styleDialog.sheetProperty.fill.solid","Solid"))});
										//new Tag(null,"Default"),
										//new Tag("Solid","Solid")});
										//
        sheetPanel.addSheetProperty(category_name, fillProperty);

        SheetProperty radiusProperty = new SheetProperty(Style.ATTRIBUTE_radius,
											        		I18n.getString("styleDialog.sheetProperty.radius","Radius"),
											        		//"Radius", 
											        		SheetProperty.INTEGER);
        sheetPanel.addSheetProperty(category_name, radiusProperty);

        SheetProperty scaleImageProperty = new SheetProperty(Style.ATTRIBUTE_scaleImage,
													        		I18n.getString("styleDialog.sheetProperty.scaleImage","Scale image"),
													        		//"Scale image", 
													        		SheetProperty.COMBOBOX);
        scaleImageProperty.setTags( new Tag[]{
        											//
        											new Tag(null, I18n.getString("styleDialog.sheetProperty.scaleImage.default","Default")),
        											new Tag("Clip", I18n.getString("styleDialog.sheetProperty.scaleImage.clip","Clip")),
        											new Tag("FillFrame", I18n.getString("styleDialog.sheetProperty.scaleImage.fillFrame","FillFrame")),
        											new Tag("RetainShape", I18n.getString("styleDialog.sheetProperty.scaleImage.retainShape","RetainShape"))});
													//new Tag(null,"Default"),
													//new Tag("Clip","Clip"),
													//new Tag("FillFrame","FillFrame"),
													//new Tag("RetainShape","RetainShape")});
        											//
        sheetPanel.addSheetProperty(category_name, scaleImageProperty);

        SheetProperty hAlignProperty = new SheetProperty(Style.ATTRIBUTE_hAlign,
											        		I18n.getString("styleDialog.sheetProperty.hAlign","Horizontal align"),
											        		//"Horizontal align", 
											        		SheetProperty.COMBOBOX);
        hAlignProperty.setTags( new Tag[]{ 
        										//
        										new Tag(null, I18n.getString("styleDialog.sheetProperty.hAlign.default","Default")),
        										new Tag("Left", I18n.getString("styleDialog.sheetProperty.hAlign.left","Left")),
        										new Tag("Center", I18n.getString("styleDialog.sheetProperty.hAlign.center","Center")),
        										new Tag("Right", I18n.getString("styleDialog.sheetProperty.hAlign.right","Right")),
        										new Tag("Justified", I18n.getString("styleDialog.sheetProperty.hAlign.justified","Justified"))});
												//new Tag(null,"Default"),
												//new Tag("Left","Left"),
												//new Tag("Center","Center"),
												//new Tag("Right","Right"),
												//new Tag("Justified","Justified")});
												//
        sheetPanel.addSheetProperty(category_name, hAlignProperty);

        SheetProperty vAlignProperty = new SheetProperty(Style.ATTRIBUTE_vAlign,
														I18n.getString("styleDialog.sheetProperty.vAlign","Vertical align"),
														//"Vertical align", 
														SheetProperty.COMBOBOX);
        vAlignProperty.setTags( new Tag[]{ 
        										//
        										new Tag(null, I18n.getString("styleDialog.sheetProperty.vAlign.default","Default")),
        										new Tag("Top", I18n.getString("styleDialog.sheetProperty.vAlign.top","Top")),
        										new Tag("Middle", I18n.getString("styleDialog.sheetProperty.vAlign.middle","Middle")),
        										new Tag("Bottom", I18n.getString("styleDialog.sheetProperty.vAlign.bottom","Bottom"))});
												//new Tag(null,"Default"),
												//new Tag("Top","Top"),
												//new Tag("Middle","Middle"),
												//new Tag("Bottom","Bottom")});
        										//
        sheetPanel.addSheetProperty(category_name, vAlignProperty);

        //
        category_name = I18n.getString("styleDialog.tabBorderAndPadding","Border and padding");
        //
        SheetProperty borderProperty = new SheetProperty(Style.ATTRIBUTE_border,
														I18n.getString("styleDialog.sheetProperty.border","Border"),
														//"Border", 
														SheetProperty.COMBOBOX);
        borderProperty.setTags( new Tag[]{
        										//
        										new Tag(null, I18n.getString("styleDialog.sheetProperty.border.default","Default")),
        										new Tag("Thin", I18n.getString("styleDialog.sheetProperty.border.thin","Thin")),
        										new Tag("1Point", I18n.getString("styleDialog.sheetProperty.border.1Point","1Point")),
        										new Tag("2Point", I18n.getString("styleDialog.sheetProperty.border.2Point","2Point")),
        										new Tag("4Point", I18n.getString("styleDialog.sheetProperty.border.4Point","4Point")),
        										new Tag("Dotted", I18n.getString("styleDialog.sheetProperty.border.dotted","Dotted"))});
												//new Tag(null,"Default"),
												//new Tag("Thin","Thin"),
												//new Tag("1Point","1Point"),
												//new Tag("2Point","2Point"),
												//new Tag("4Point","4Point"),
												//new Tag("Dotted","Dotted")});
        										//
        sheetPanel.addSheetProperty(category_name, borderProperty);

        SheetProperty borderColorProperty = new SheetProperty(Style.ATTRIBUTE_borderColor,
														        	I18n.getString("styleDialog.sheetProperty.borderColor","Border color"),
														        	//"Border color", 
														        	SheetProperty.COLOR);
        sheetPanel.addSheetProperty(category_name, borderColorProperty);

        SheetProperty paddingProperty = new SheetProperty(Style.ATTRIBUTE_padding,
											        		I18n.getString("styleDialog.sheetProperty.padding","Padding"),
											        		//"Padding", 
											        		SheetProperty.INTEGER);
        sheetPanel.addSheetProperty(category_name, paddingProperty);

        SheetProperty topBorderProperty = new SheetProperty(Style.ATTRIBUTE_topBorder,
												        		I18n.getString("styleDialog.sheetProperty.topBorder","Top border"),
												        		//"Top border", 
												        		SheetProperty.COMBOBOX);
        topBorderProperty.setTags( new Tag[]{
        											//
                                        			new Tag(null, I18n.getString("styleDialog.sheetProperty.topBorder.default","Default")),
                                        			new Tag("Thin", I18n.getString("styleDialog.sheetProperty.topBorder.thin","Thin")),
                                        			new Tag("1Point", I18n.getString("styleDialog.sheetProperty.topBorder.1Point","1Point")),
                                        			new Tag("2Point", I18n.getString("styleDialog.sheetProperty.topBorder.2Point","2Point")),
                                        			new Tag("4Point", I18n.getString("styleDialog.sheetProperty.topBorder.4Point","4Point")),
                                        			new Tag("Dotted", I18n.getString("styleDialog.sheetProperty.topBorder.dotted","Dotted"))});
													//new Tag(null,"Default"),
													//new Tag("Thin","Thin"),
													//new Tag("1Point","1Point"),
													//new Tag("2Point","2Point"),
													//new Tag("4Point","4Point"),
													//new Tag("Dotted","Dotted")});
        											//
        sheetPanel.addSheetProperty(category_name, topBorderProperty);

        SheetProperty topBorderColorProperty = new SheetProperty(Style.ATTRIBUTE_topBorderColor,
														        		I18n.getString("styleDialog.sheetProperty.topBorderColor","Top border color"),
														        		//"Top border color", 
														        		SheetProperty.COLOR);
        sheetPanel.addSheetProperty(category_name, topBorderColorProperty);

        SheetProperty topPaddingProperty = new SheetProperty(Style.ATTRIBUTE_topPadding,
																	I18n.getString("styleDialog.sheetProperty.topPadding","Top padding"),
																    //"Top padding", 
																    SheetProperty.INTEGER);
        sheetPanel.addSheetProperty(category_name, topPaddingProperty);

        SheetProperty leftBorderProperty = new SheetProperty(Style.ATTRIBUTE_leftBorder,
												        		I18n.getString("styleDialog.sheetProperty.leftBorder","Left border"),
												        		//"Left border",
												        		SheetProperty.COMBOBOX);
        leftBorderProperty.setTags( new Tag[]{
													//
									    			new Tag(null, I18n.getString("styleDialog.sheetProperty.leftBorder.default","Default")),
									    			new Tag("Thin", I18n.getString("styleDialog.sheetProperty.leftBorder.thin","Thin")),
									    			new Tag("1Point", I18n.getString("styleDialog.sheetProperty.leftBorder.1Point","1Point")),
									    			new Tag("2Point", I18n.getString("styleDialog.sheetProperty.leftBorder.2Point","2Point")),
									    			new Tag("4Point", I18n.getString("styleDialog.sheetProperty.leftBorder.4Point","4Point")),
									    			new Tag("Dotted", I18n.getString("styleDialog.sheetProperty.leftBorder.dotted","Dotted"))});
													//new Tag(null,"Default"),
													//new Tag("Thin","Thin"),
													//new Tag("1Point","1Point"),
													//new Tag("2Point","2Point"),
													//new Tag("4Point","4Point"),
													//new Tag("Dotted","Dotted")});
													//
        sheetPanel.addSheetProperty(category_name, leftBorderProperty);

        SheetProperty leftBorderColorProperty = new SheetProperty(Style.ATTRIBUTE_leftBorderColor,
														        		I18n.getString("styleDialog.sheetProperty.leftBorderColor","Left border color"),
														        		//"Left border color", 
														        		SheetProperty.COLOR);
        sheetPanel.addSheetProperty(category_name, leftBorderColorProperty);

        SheetProperty leftPaddingProperty = new SheetProperty(Style.ATTRIBUTE_leftPadding,
													        	I18n.getString("styleDialog.sheetProperty.leftPadding","Left padding"),
													        	//"Left padding", 
													        	SheetProperty.INTEGER);
        sheetPanel.addSheetProperty(category_name, leftPaddingProperty);

        SheetProperty bottomBorderProperty = new SheetProperty(Style.ATTRIBUTE_bottomBorder,
													        		I18n.getString("styleDialog.sheetProperty.bottomBorder","Bottom border"),
													        		//"Bottom border", 
													        		SheetProperty.COMBOBOX);
        bottomBorderProperty.setTags( new Tag[]{
														//
										    			new Tag(null, I18n.getString("styleDialog.sheetProperty.leftBorder.default","Default")),
										    			new Tag("Thin", I18n.getString("styleDialog.sheetProperty.leftBorder.thin","Thin")),
										    			new Tag("1Point", I18n.getString("styleDialog.sheetProperty.leftBorder.1Point","1Point")),
										    			new Tag("2Point", I18n.getString("styleDialog.sheetProperty.leftBorder.2Point","2Point")),
										    			new Tag("4Point", I18n.getString("styleDialog.sheetProperty.leftBorder.4Point","4Point")),
										    			new Tag("Dotted", I18n.getString("styleDialog.sheetProperty.leftBorder.dotted","Dotted"))});
														//new Tag(null,"Default"),
														//new Tag("Thin","Thin"),
														//new Tag("1Point","1Point"),
														//new Tag("2Point","2Point"),
														//new Tag("4Point","4Point"),
														//new Tag("Dotted","Dotted")});
														//
        sheetPanel.addSheetProperty(category_name, bottomBorderProperty);

        SheetProperty bottomBorderColorProperty = new SheetProperty(Style.ATTRIBUTE_bottomBorderColor,
															        		I18n.getString("styleDialog.sheetProperty.bottomBorderColor","Bottom border color"),
															        		//"Bottom border color", 
															        		SheetProperty.COLOR);
        sheetPanel.addSheetProperty(category_name, bottomBorderColorProperty);

        SheetProperty bottomPaddingProperty = new SheetProperty(Style.ATTRIBUTE_bottomPadding,
														        		I18n.getString("styleDialog.sheetProperty.bottomPadding","Bottom padding"),
														        		//"Bottom padding", 
														        		SheetProperty.INTEGER);
        sheetPanel.addSheetProperty(category_name, bottomPaddingProperty);

        SheetProperty rightBorderProperty = new SheetProperty(Style.ATTRIBUTE_rightBorder,
													        		I18n.getString("styleDialog.sheetProperty.rightBorder","Right border"),
													        		//"Right border", 
													        		SheetProperty.COMBOBOX);
        rightBorderProperty.setTags( new Tag[]{
													//
									    			new Tag(null, I18n.getString("styleDialog.sheetProperty.rightBorder.default","Default")),
									    			new Tag("Thin", I18n.getString("styleDialog.sheetProperty.rightBorder.thin","Thin")),
									    			new Tag("1Point", I18n.getString("styleDialog.sheetProperty.rightBorder.1Point","1Point")),
									    			new Tag("2Point", I18n.getString("styleDialog.sheetProperty.rightBorder.2Point","2Point")),
									    			new Tag("4Point", I18n.getString("styleDialog.sheetProperty.rightBorder.4Point","4Point")),
									    			new Tag("Dotted", I18n.getString("styleDialog.sheetProperty.rightBorder.dotted","Dotted"))});
													//new Tag(null,"Default"),
													//new Tag("Thin","Thin"),
													//new Tag("1Point","1Point"),
													//new Tag("2Point","2Point"),
													//new Tag("4Point","4Point"),
													//new Tag("Dotted","Dotted")});
													//
        sheetPanel.addSheetProperty(category_name, rightBorderProperty);

        SheetProperty rightBorderColorProperty = new SheetProperty(Style.ATTRIBUTE_rightBorderColor,
															        	I18n.getString("styleDialog.sheetProperty.rightBorderColor","Right border color"),
															        	//"Right border color", 
															        	SheetProperty.COLOR);
        sheetPanel.addSheetProperty(category_name, rightBorderColorProperty);

        SheetProperty rightPaddingProperty = new SheetProperty(Style.ATTRIBUTE_rightPadding,
													        		I18n.getString("styleDialog.sheetProperty.rightPadding","Right padding"),
													        		//"Right padding", 
													        		SheetProperty.INTEGER);
        sheetPanel.addSheetProperty(category_name, rightPaddingProperty);
        
        //
        category_name = I18n.getString("styleDialog.tabTextProperties","Text properties");
        //
        SheetProperty rotationProperty = new SheetProperty(Style.ATTRIBUTE_rotation,
											        		I18n.getString("styleDialog.sheetProperty.rotation","Rotation"),
											        		//"Rotation", 
											        		SheetProperty.COMBOBOX);
        rotationProperty.setTags( new Tag[]{ 
        										//
        										new Tag(null, I18n.getString("styleDialog.sheetProperty.rotation.default","Default")),
        										new Tag("None", I18n.getString("styleDialog.sheetProperty.rotation.none","None")),
        										new Tag("Left", I18n.getString("styleDialog.sheetProperty.rotation.left","Left")),
        										new Tag("Right", I18n.getString("styleDialog.sheetProperty.rotation.right","Right")),
        										new Tag("UpsideDown", I18n.getString("styleDialog.sheetProperty.rotation.upsideDown","UpsideDown"))});
												//new Tag(null,"Default"),
												//new Tag("None","None"),
												//new Tag("Left","Left"),
												//new Tag("Right","Right"),
												//new Tag("UpsideDown","UpsideDown")});
        										//
        sheetPanel.addSheetProperty(category_name, rotationProperty);

        SheetProperty patternProperty = new SheetProperty(Style.ATTRIBUTE_pattern,
											        		I18n.getString("styleDialog.sheetProperty.pattern","Pattern"),
											        		//"Pattern", 
											        		SheetProperty.STRING);
        sheetPanel.addSheetProperty(category_name, patternProperty);

        SheetProperty isBlankWhenNullProperty = new SheetProperty(Style.ATTRIBUTE_isBlankWhenNull,
														        		I18n.getString("styleDialog.sheetProperty.isBlankWhenNull","Is Blank when Null"),
														        		//"Is Blank when Null", 
														        		SheetProperty.BOOLEAN);
        sheetPanel.addSheetProperty(category_name, isBlankWhenNullProperty);

        // FONT...
        FontSheetProperty theFontSpacingProperty = new FontSheetProperty("style.font",
        																	I18n.getString("styleDialog.sheetProperty.theFontSpacing","Font"));
        																	//"Font");
        theFontSpacingProperty.setFontMode( 3 );
        sheetPanel.addSheetProperty(category_name, theFontSpacingProperty);

        SheetProperty lineSpacingProperty = new SheetProperty(Style.ATTRIBUTE_lineSpacing,
												        		I18n.getString("styleDialog.sheetProperty.lineSpacing","Line spacing"),
												        		//"Line spacing", 
												        		SheetProperty.COMBOBOX);
        lineSpacingProperty.setTags( new Tag[]{ 
        											//
        											new Tag(null, I18n.getString("styleDialog.sheetProperty.lineSpacing.default","Default")),
        											new Tag("Single", I18n.getString("styleDialog.sheetProperty.lineSpacing.single","Single")),
        											new Tag("1_1_2", I18n.getString("styleDialog.sheetProperty.lineSpacing.1_1_2","1_1_2")),
        											new Tag("Double", I18n.getString("styleDialog.sheetProperty.lineSpacing.double","Double"))});
													//new Tag(null,"Default"),
													//new Tag("Single","Single"),
													//new Tag("1_1_2","1_1_2"),
													//new Tag("Double","Double")});
											        //
        sheetPanel.addSheetProperty(category_name, lineSpacingProperty);

        /*
        SheetProperty fontNameProperty = new SheetProperty("style.fontName","Font", SheetProperty.COMBOBOX);
        fontNameProperty.setTags( new Tag[]{ });
        sheetPanel.addSheetProperty("Text properties", fontNameProperty);
        */
        SheetProperty isBoldProperty = new SheetProperty(Style.ATTRIBUTE_isBold,
										        		I18n.getString("styleDialog.sheetProperty.isBold","Bold"),
										        		//"Bold", 
										        		SheetProperty.BOOLEAN);
        sheetPanel.addSheetProperty(category_name, isBoldProperty);

        SheetProperty isItalicProperty = new SheetProperty(Style.ATTRIBUTE_isItalic,
											        		I18n.getString("styleDialog.sheetProperty.isItalic","Italic"),
											        		//"Italic", 
											        		SheetProperty.BOOLEAN);
        sheetPanel.addSheetProperty(category_name, isItalicProperty);

        SheetProperty isUnderlineProperty = new SheetProperty(Style.ATTRIBUTE_isUnderline,
												        		I18n.getString("styleDialog.sheetProperty.isUnderline","Underline"),
												        		//"Underline", 
												        		SheetProperty.BOOLEAN);
        sheetPanel.addSheetProperty(category_name, isUnderlineProperty);

        SheetProperty isStrikeThroughProperty = new SheetProperty(Style.ATTRIBUTE_isStrikeThrough,
														        		I18n.getString("styleDialog.sheetProperty.isStrikeThrough","StrikeThrough"),
														        		//"StrikeThrough", 
														        		SheetProperty.BOOLEAN);
        sheetPanel.addSheetProperty(category_name, isStrikeThroughProperty);

        //SheetProperty fontSizeProperty = new SheetProperty(Style.ATTRIBUTE_,"Font size", SheetProperty.INTEGER);
        //sheetPanel.addSheetProperty("Text properties", fontSizeProperty);

        /*
        SheetProperty pdfFontNameProperty = new SheetProperty("style.pdfFontName","PDF Font name", SheetProperty.COMBOBOX);
        pdfFontNameProperty.setTags( new Tag[]{ });
        sheetPanel.addSheetProperty("Text properties", pdfFontNameProperty);

        SheetProperty pdfEncodingProperty = new SheetProperty("style.pdfEncoding","PDF Encoding", SheetProperty.STRING);
        sheetPanel.addSheetProperty("Text properties", pdfEncodingProperty);

	SheetProperty isPdfEmbeddedProperty = new SheetProperty("style.isPdfEmbedded","Embed PDF fonts", SheetProperty.STRING);
        sheetPanel.addSheetProperty("Text properties", isPdfEmbeddedProperty);
        */

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.fill = gridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0,0, 0, 0);

        this.jPanelSample.add(panelSample,gridBagConstraints);

        SheetProperty isStyledTextProperty = new SheetProperty(Style.ATTRIBUTE_isStyledText,
													        		I18n.getString("styleDialog.sheetProperty.isStyledText","Is Styled Text"),
													        		//"Is Styled Text", 
													        		SheetProperty.BOOLEAN);
        sheetPanel.addSheetProperty(category_name, isStyledTextProperty);
        // FINE FONT...
        sheetPanel.recreateSheet();

        //sheetPanel.collapseCategory("Border and padding");
        sheetPanel.collapseCategory(I18n.getString("styleDialog.tabBorderAndPadding","Border and padding"));

        this.updateStyleValues();

        this.jConditionsList.setModel( new DefaultListModel());
        this.jConditionsList.setSelectionMode( ListSelectionModel.SINGLE_SELECTION);

        //
        ((DefaultListModel)this.jConditionsList.getModel()).addElement(I18n.getString("styleDialog.conditionsList.default","Default"));
        //((DefaultListModel)this.jConditionsList.getModel()).addElement("Default");
        //
        this.jConditionsList.setSelectedIndex(0);
        
        javax.swing.KeyStroke escape =  javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false);
        javax.swing.Action escapeAction = new javax.swing.AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                jButtonCancelActionPerformed(e);
            }
        };
       
        getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", escapeAction);


        //to make the default button ...
        this.getRootPane().setDefaultButton(this.jButtonOK);
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jSplitPane1 = new javax.swing.JSplitPane();
        jPanelProperties = new javax.swing.JPanel();
        jPanelConditions = new javax.swing.JPanel();
        jLabelStyleConditions = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jConditionsList = new javax.swing.JList();
        jPanel2 = new javax.swing.JPanel();
        jButtonAddCondition = new javax.swing.JButton();
        jButtonModifyCondition = new javax.swing.JButton();
        jButtonRemoveCondition = new javax.swing.JButton();
        jButtonUpCondition = new javax.swing.JButton();
        jButtonDownCondition = new javax.swing.JButton();
        jLabelSample = new javax.swing.JLabel();
        jPanelSample = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jButtonOK = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setTitle("Add/modify Style");
        setModal(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jSplitPane1.setResizeWeight(0.5);
        jPanelProperties.setLayout(new java.awt.GridBagLayout());

        jPanelProperties.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jPanelProperties.setMinimumSize(new java.awt.Dimension(100, 4));
        jPanelProperties.setPreferredSize(new java.awt.Dimension(320, 470));
        jSplitPane1.setLeftComponent(jPanelProperties);

        jPanelConditions.setLayout(new java.awt.GridBagLayout());

        jPanelConditions.setMinimumSize(new java.awt.Dimension(100, 10));
        jPanelConditions.setPreferredSize(new java.awt.Dimension(200, 10));
        jLabelStyleConditions.setText("Style conditions");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        jPanelConditions.add(jLabelStyleConditions, gridBagConstraints);

        jConditionsList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jConditionsListValueChanged(evt);
            }
        });
        jConditionsList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jConditionsListMouseClicked(evt);
            }
        });

        jScrollPane1.setViewportView(jConditionsList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        jPanelConditions.add(jScrollPane1, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jButtonAddCondition.setText("Add");
        jButtonAddCondition.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButtonAddCondition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddConditionActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel2.add(jButtonAddCondition, gridBagConstraints);

        jButtonModifyCondition.setText("Modify");
        jButtonModifyCondition.setEnabled(false);
        jButtonModifyCondition.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButtonModifyCondition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModifyConditionActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel2.add(jButtonModifyCondition, gridBagConstraints);

        jButtonRemoveCondition.setText("Remove");
        jButtonRemoveCondition.setEnabled(false);
        jButtonRemoveCondition.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButtonRemoveCondition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemoveConditionActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel2.add(jButtonRemoveCondition, gridBagConstraints);

        jButtonUpCondition.setText("up");
        jButtonUpCondition.setEnabled(false);
        jButtonUpCondition.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButtonUpCondition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUpConditionActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel2.add(jButtonUpCondition, gridBagConstraints);

        jButtonDownCondition.setText("Down");
        jButtonDownCondition.setEnabled(false);
        jButtonDownCondition.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButtonDownCondition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDownConditionActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel2.add(jButtonDownCondition, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanelConditions.add(jPanel2, gridBagConstraints);

        jLabelSample.setText("Sample");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        jPanelConditions.add(jLabelSample, gridBagConstraints);

        jPanelSample.setLayout(new java.awt.GridBagLayout());

        jPanelSample.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanelSample.setMinimumSize(new java.awt.Dimension(100, 80));
        jPanelSample.setPreferredSize(new java.awt.Dimension(14, 80));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 4;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanelConditions.add(jPanelSample, gridBagConstraints);

        jSplitPane1.setRightComponent(jPanelConditions);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 4;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(jSplitPane1, gridBagConstraints);

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jButtonOK.setMnemonic('o');
        jButtonOK.setText("OK");
        jButtonOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOKActionPerformed(evt);
            }
        });

        jPanel1.add(jButtonOK);

        jButtonCancel.setText("Cancel");
        jButtonCancel.setMnemonic('c');
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        jPanel1.add(jButtonCancel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jPanel1, gridBagConstraints);

        pack();
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        java.awt.Dimension dialogSize = getSize();
        setLocation((screenSize.width-dialogSize.width)/2,(screenSize.height-dialogSize.height)/2);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened

            if (openCondition >= 0)
            {
                try {
                    if (jConditionsList.getModel().getSize() > openCondition+1)
                    {
                        this.jConditionsList.setSelectedIndex( openCondition + 1);
                        jButtonModifyConditionActionPerformed(new ActionEvent(jButtonModifyCondition,0,""));
                    }
                } catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        
    }//GEN-LAST:event_formWindowOpened

    private void jButtonDownConditionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDownConditionActionPerformed
        DefaultListModel dlm = (DefaultListModel)this.jConditionsList.getModel();
        setInit(true);
        int i = jConditionsList.getSelectedIndex();
        Object cs = jConditionsList.getSelectedValue();
        dlm.removeElementAt(i);
        dlm.insertElementAt(cs, i+1);
        jConditionsList.setSelectedIndex(i+1);

        jButtonUpCondition.setEnabled(true);
        if (i == dlm.size()-2) jButtonDownCondition.setEnabled(false);
        setInit(false);
    }//GEN-LAST:event_jButtonDownConditionActionPerformed

    private void jButtonUpConditionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUpConditionActionPerformed

        DefaultListModel dlm = (DefaultListModel)this.jConditionsList.getModel();
        setInit(true);
        int i = jConditionsList.getSelectedIndex();
        Object cs = jConditionsList.getSelectedValue();
        dlm.removeElementAt(i);
        dlm.insertElementAt(cs, i-1);
        jConditionsList.setSelectedIndex(i-1);

        if (i == 2) jButtonUpCondition.setEnabled(false);
        jButtonDownCondition.setEnabled(true);
        setInit(false);


    }//GEN-LAST:event_jButtonUpConditionActionPerformed

    private void jButtonRemoveConditionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemoveConditionActionPerformed

        if (jConditionsList.getSelectedIndex() == 0) return;
        int i =  jConditionsList.getSelectedIndex();
        jConditionsList.setSelectedIndex(i-1);
        ((DefaultListModel)this.jConditionsList.getModel()).remove( i);


    }//GEN-LAST:event_jButtonRemoveConditionActionPerformed

    private void jButtonModifyConditionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModifyConditionActionPerformed

        if (jConditionsList.getSelectedIndex() == 0) return;

        ConditionedStyle s = (ConditionedStyle) jConditionsList.getSelectedValue();
        String expression = s.getCondition();

        ExpressionEditor ed = new ExpressionEditor();
        if ( MainFrame.getMainInstance().getActiveReportFrame() != null)
        {
            Report report = MainFrame.getMainInstance().getActiveReportFrame().getReport();
            ed.setSubDataset( report );
            Vector v = report.getElements();
            for (int i=0; i<v.size(); ++i)
            {
                ReportElement re = (ReportElement)v.elementAt(i);
                if (re instanceof CrosstabReportElement)
                {
                    ed.addCrosstabReportElement( (CrosstabReportElement)re );
                }
            }
        }
        else
        {
            ed.setSubDataset( new SubDataset());
        }

        ed.setExpression(expression);
        ed.setVisible(true);
        if (ed.getDialogResult() == JOptionPane.OK_OPTION)
        {
            String exp = ed.getExpression();
            if (exp.trim().length() > 0)
            {
                s.setCondition( exp.trim());
                jConditionsList.updateUI();
            }
        }

    }//GEN-LAST:event_jButtonModifyConditionActionPerformed

    private void jConditionsListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jConditionsListMouseClicked

        if (evt.getClickCount() == 2 && evt.getButton() == evt.BUTTON1)
        {
            jButtonModifyConditionActionPerformed(null);
        }

    }//GEN-LAST:event_jConditionsListMouseClicked

    private void jButtonAddConditionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddConditionActionPerformed

        ExpressionEditor ed = new ExpressionEditor();
        if ( MainFrame.getMainInstance().getActiveReportFrame() != null)
        {
            Report report = MainFrame.getMainInstance().getActiveReportFrame().getReport();
            ed.setSubDataset( report );
            Vector v = report.getElements();
            for (int i=0; i<v.size(); ++i)
            {
                ReportElement re = (ReportElement)v.elementAt(i);
                if (re instanceof CrosstabReportElement)
                {
                    ed.addCrosstabReportElement( (CrosstabReportElement)re );
                }
            }
        }
        else
        {
            ed.setSubDataset( new SubDataset());
        }

        ed.setVisible(true);

        if (ed.getDialogResult() == JOptionPane.OK_OPTION)
        {
            String exp = ed.getExpression();
            if (exp.trim().length() > 0)
            {
                saveCurrentStyle();
                ConditionedStyle s = new ConditionedStyle(getMasterStyle());
                s.setConditionedStyles( new Vector() );
                s.setCondition( exp.trim());
                DefaultListModel dlm = (DefaultListModel)jConditionsList.getModel();
                dlm.addElement(s);
                jConditionsList.setSelectedValue(s, true);
            }
        }
    }//GEN-LAST:event_jButtonAddConditionActionPerformed

    private void jConditionsListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jConditionsListValueChanged

        if (isInit()) return;
        if (evt.getValueIsAdjusting()) return;

        //System.out.println(" style selected!");
        // Save the current style if needed
        saveCurrentStyle();

        if (jConditionsList.getSelectedIndex() == 0)
        {
            jButtonModifyCondition.setEnabled(false);
            jButtonRemoveCondition.setEnabled(false);
            jButtonUpCondition.setEnabled(false);
            jButtonDownCondition.setEnabled(false);

            setStyle(getMasterStyle(), false);
        }
        else
        {
            jButtonModifyCondition.setEnabled(true);
            jButtonRemoveCondition.setEnabled(true);
            jButtonUpCondition.setEnabled(jConditionsList.getSelectedIndex()>1);
            jButtonDownCondition.setEnabled(jConditionsList.getSelectedIndex()< ((DefaultListModel)jConditionsList.getModel()).size()-1);

            setStyle((Style)jConditionsList.getSelectedValue(), true);
        }


    }//GEN-LAST:event_jConditionsListValueChanged

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        setVisible(false);
        this.setDialogResult( javax.swing.JOptionPane.CANCEL_OPTION);
        dispose();
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jButtonOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOKActionPerformed

        saveCurrentStyle();

        String newName = getMasterStyle().getAttributeString( Style.ATTRIBUTE_name ,"");


        if (newName.trim().length() <= 0)
        {
            javax.swing.JOptionPane.showMessageDialog(this,
                    I18n.getString("messages.styleDialog.notValidName","Please insert a valid name for the style!"),
                    I18n.getString("messages.styleDialog.notValidNameCaption","Invalid name!"),
                    javax.swing.JOptionPane.WARNING_MESSAGE );
            jConditionsList.setSelectedIndex(0);
            return;
        }

        // check for duplicate name....
        Enumeration e = null;

        if (jasperTemplate != null)
        {
            e = new Vector( jasperTemplate.getStyles() ).elements();
        }
        else if (isLibraryStyle())
        {
            e = it.businesslogic.ireport.gui.MainFrame.getMainInstance().getStyleLibrarySet().elements();
        }
        else
        {
            e = it.businesslogic.ireport.gui.MainFrame.getMainInstance().getActiveReportFrame().getReport().getStyles().elements();
        }

        while (e.hasMoreElements())
        {
            Style st = (Style)e.nextElement();
            if (st.getAttributeString( st.ATTRIBUTE_name ,"").equals(newName) && st != getEditingStyle())
            {
                javax.swing.JOptionPane.showMessageDialog(this,"The name \"" + newName + "\" is already in use!\nPlease change it.","Invalid name!",javax.swing.JOptionPane.WARNING_MESSAGE );
                return;
            }
        }

        DefaultListModel dlm = (DefaultListModel)jConditionsList.getModel();
        Vector conditionedStyles = new Vector();
        for (int k = 1; k<dlm.size(); ++k)
        {
            conditionedStyles.add(dlm.getElementAt(k) );
        }
        getMasterStyle().setConditionedStyles(conditionedStyles);

        String oldName = "";
        boolean newStyle = false;

        if (getEditingStyle() == null)
        {
            newStyle = true;
            setEditingStyle( getMasterStyle() );

            if (!isLibraryStyle() && jasperTemplate == null)
            {
               MainFrame.getMainInstance().getActiveReportFrame().getReport().getStyles().add(getMasterStyle());
            }
            else if (jasperTemplate == null)
            {
                it.businesslogic.ireport.gui.MainFrame.getMainInstance().getStyleLibrarySet().add(getMasterStyle());
            }
        }
        else
        {
            oldName = getEditingStyle().getAttributeString( Style.ATTRIBUTE_name ,"");
            getEditingStyle().getAttributes().clear();
            getEditingStyle().copyStyleFrom(getMasterStyle());
        }

        if (!isLibraryStyle() || jasperTemplate == null)
        {
                MainFrame.getMainInstance().getActiveReportFrame().getReport().fireStyleChangedListenerStyleChanged(
                                new StyleChangedEvent(
                                MainFrame.getMainInstance().getActiveReportFrame().getReport(),
                                (newStyle) ? StyleChangedEvent.ADDED : StyleChangedEvent.CHANGED,
                                getEditingStyle(),
                                getEditingStyle()));
        }

        this.style = getEditingStyle();

        // Dump style...
        //System.out.println("Save style:............");
        //HashMap hm = getStyle().getAttributes();
        //Iterator i_keys = hm.keySet().iterator();
        //while (i_keys.hasNext())
        //  {
        //      Object key = i_keys.next();
        //      System.out.println( key + ": " +hm.get(key) );
        //  }

        setVisible(false);
        this.setDialogResult( javax.swing.JOptionPane.OK_OPTION);
        dispose();
    }//GEN-LAST:event_jButtonOKActionPerformed

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
        this.setDialogResult( javax.swing.JOptionPane.CLOSED_OPTION);
        dispose();
    }//GEN-LAST:event_closeDialog

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new JRParameterDialog(new javax.swing.JFrame(), true).setVisible(true);
    }


    /** Getter for property dialogResult.
     * @return Value of property dialogResult.
     *
     */
    public int getDialogResult() {
        return dialogResult;
    }

    /** Setter for property dialogResult.
     * @param dialogResult New value of property dialogResult.
     *
     */
    public void setDialogResult(int dialogResult) {
        this.dialogResult = dialogResult;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAddCondition;
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonDownCondition;
    private javax.swing.JButton jButtonModifyCondition;
    private javax.swing.JButton jButtonOK;
    private javax.swing.JButton jButtonRemoveCondition;
    private javax.swing.JButton jButtonUpCondition;
    private javax.swing.JList jConditionsList;
    private javax.swing.JLabel jLabelSample;
    private javax.swing.JLabel jLabelStyleConditions;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelConditions;
    private javax.swing.JPanel jPanelProperties;
    private javax.swing.JPanel jPanelSample;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane1;
    // End of variables declaration//GEN-END:variables

    private int dialogResult;

    public CategorySheetPanel getSheetPanel() {
        return sheetPanel;
    }

    public void setSheetPanel(CategorySheetPanel sheetPanel) {
        this.sheetPanel = sheetPanel;
    }

    public void sheetPropertyValueChanged(it.businesslogic.ireport.gui.event.SheetPropertyValueChangedEvent evt) {

        //System.out.println("Changed proprty " + evt.getPropertyName());


        if (isInit()) return;
        if (evt.getPropertyName() != null && evt.getPropertyName().equals("style.font"))
        {
            setIreportFont( (IReportFont)evt.getNewValue());

            boolean initNow = isInit();
            setInit(true);
            if (getIreportFont() != null)
            {
                sheetPanel.setPropertyValue(Style.ATTRIBUTE_isBold,  getIreportFont().getBeanProperties().get(getIreportFont().IS_BOLD));
                sheetPanel.setPropertyValue(Style.ATTRIBUTE_isItalic,  getIreportFont().getBeanProperties().get(getIreportFont().IS_ITALIC));
                sheetPanel.setPropertyValue(Style.ATTRIBUTE_isStrikeThrough,  getIreportFont().getBeanProperties().get(getIreportFont().IS_STRIKETROUGHT));
                sheetPanel.setPropertyValue(Style.ATTRIBUTE_isUnderline,  getIreportFont().getBeanProperties().get(getIreportFont().IS_UNDERLINE));
            }
            else
            {
                sheetPanel.setPropertyValue(Style.ATTRIBUTE_isBold, null);
                sheetPanel.setPropertyValue(Style.ATTRIBUTE_isItalic, null);
                sheetPanel.setPropertyValue(Style.ATTRIBUTE_isStrikeThrough, null);
                sheetPanel.setPropertyValue(Style.ATTRIBUTE_isUnderline, null);
            }
            setInit(initNow);

        }
        else if (evt.getPropertyName() != null && evt.getPropertyName().equals(Style.ATTRIBUTE_isBold))
        {
            IReportFont ifont = getIreportFont();
            ifont.getBeanProperties().put(ifont.IS_BOLD, evt.getNewValue());
            sheetPanel.setPropertyValue("style.font", ifont);
        }
        else if (evt.getPropertyName() != null && evt.getPropertyName().equals(Style.ATTRIBUTE_isItalic))
        {
            IReportFont ifont = getIreportFont();
            ifont.getBeanProperties().put(ifont.IS_ITALIC, evt.getNewValue());
            sheetPanel.setPropertyValue("style.font", ifont);
        }
        else if (evt.getPropertyName() != null && evt.getPropertyName().equals(Style.ATTRIBUTE_isUnderline))
        {
            IReportFont ifont = getIreportFont();
            ifont.getBeanProperties().put(ifont.IS_UNDERLINE, evt.getNewValue());
            sheetPanel.setPropertyValue("style.font", ifont);
        }
        else if (evt.getPropertyName() != null && evt.getPropertyName().equals(Style.ATTRIBUTE_isStrikeThrough))
        {
            IReportFont ifont = getIreportFont();
            ifont.getBeanProperties().put(ifont.IS_STRIKETROUGHT, evt.getNewValue());
            sheetPanel.setPropertyValue("style.font", ifont);
        }



        //saveCurrentStyle();
        if (!isInit() && panelSample != null && panelSample.getStyle() != null)
        {
            Vector properties = this.sheetPanel.getProperties();
            for (int i=0; i<properties.size(); ++i)
            {
                SheetProperty sp = (SheetProperty)properties.get(i);
                //if ( sp.getValue() != null)
                {
                    panelSample.getStyle().getAttributes().put( sp.getKeyName(),  sp.getValue());
                }
            }
            panelSample.invalidate();
            panelSample.repaint();
        }
    }

    public Style getStyle() {
        return style;
    }

    public void updateStyleValues()
    {
        setInit(true);
        //System.out.println("Updating style values...");

        sheetPanel.setPropertyValue(Style.ATTRIBUTE_name,getStyle().getAttribute(style.ATTRIBUTE_name));
        sheetPanel.setPropertyValue(Style.ATTRIBUTE_backcolor,getStyle().getAttribute(style.ATTRIBUTE_backcolor));
        sheetPanel.setPropertyValue(Style.ATTRIBUTE_borderColor,getStyle().getAttribute(style.ATTRIBUTE_borderColor));
        sheetPanel.setPropertyValue(Style.ATTRIBUTE_border,getStyle().getAttribute(style.ATTRIBUTE_border));
        sheetPanel.setPropertyValue(Style.ATTRIBUTE_bottomBorderColor,getStyle().getAttribute(style.ATTRIBUTE_bottomBorderColor));
        sheetPanel.setPropertyValue(Style.ATTRIBUTE_bottomBorder,getStyle().getAttribute(style.ATTRIBUTE_bottomBorder));
        sheetPanel.setPropertyValue(Style.ATTRIBUTE_bottomPadding,getStyle().getAttribute(style.ATTRIBUTE_bottomPadding));
        sheetPanel.setPropertyValue(Style.ATTRIBUTE_fill,getStyle().getAttribute(style.ATTRIBUTE_fill));
        sheetPanel.setPropertyValue(Style.ATTRIBUTE_forecolor,getStyle().getAttribute(style.ATTRIBUTE_forecolor));
        sheetPanel.setPropertyValue(Style.ATTRIBUTE_hAlign,getStyle().getAttribute(style.ATTRIBUTE_hAlign));
        sheetPanel.setPropertyValue(Style.ATTRIBUTE_isBlankWhenNull,getStyle().getAttribute(style.ATTRIBUTE_isBlankWhenNull));
        sheetPanel.setPropertyValue(Style.ATTRIBUTE_isDefault,getStyle().getAttribute(style.ATTRIBUTE_isDefault));
        sheetPanel.setPropertyValue(Style.ATTRIBUTE_leftBorderColor,getStyle().getAttribute(style.ATTRIBUTE_leftBorderColor));
        sheetPanel.setPropertyValue(Style.ATTRIBUTE_leftBorder,getStyle().getAttribute(style.ATTRIBUTE_leftBorder));
        sheetPanel.setPropertyValue(Style.ATTRIBUTE_leftPadding,getStyle().getAttribute(style.ATTRIBUTE_leftPadding));
        sheetPanel.setPropertyValue(Style.ATTRIBUTE_lineSpacing,getStyle().getAttribute(style.ATTRIBUTE_lineSpacing));
        sheetPanel.setPropertyValue(Style.ATTRIBUTE_mode,getStyle().getAttribute(style.ATTRIBUTE_mode));
        sheetPanel.setPropertyValue(Style.ATTRIBUTE_padding,getStyle().getAttribute(style.ATTRIBUTE_padding));
        sheetPanel.setPropertyValue(Style.ATTRIBUTE_pattern,getStyle().getAttribute(style.ATTRIBUTE_pattern));
        sheetPanel.setPropertyValue(Style.ATTRIBUTE_pen,getStyle().getAttribute(style.ATTRIBUTE_pen));
        sheetPanel.setPropertyValue(Style.ATTRIBUTE_radius,getStyle().getAttribute(style.ATTRIBUTE_radius));
        sheetPanel.setPropertyValue(Style.ATTRIBUTE_rightBorderColor,getStyle().getAttribute(style.ATTRIBUTE_rightBorderColor));
        sheetPanel.setPropertyValue(Style.ATTRIBUTE_rightBorder,getStyle().getAttribute(style.ATTRIBUTE_rightBorder));
        sheetPanel.setPropertyValue(Style.ATTRIBUTE_rightPadding,getStyle().getAttribute(style.ATTRIBUTE_rightPadding));
        sheetPanel.setPropertyValue(Style.ATTRIBUTE_rotation,getStyle().getAttribute(style.ATTRIBUTE_rotation));
        sheetPanel.setPropertyValue(Style.ATTRIBUTE_scaleImage,getStyle().getAttribute(style.ATTRIBUTE_scaleImage));
        sheetPanel.setPropertyValue(Style.ATTRIBUTE_style,getStyle().getAttribute(style.ATTRIBUTE_style));
        sheetPanel.setPropertyValue(Style.ATTRIBUTE_isStyledText,getStyle().getAttribute(style.ATTRIBUTE_isStyledText));

        sheetPanel.setPropertyValue("style.font", getIreportFont());
        sheetPanel.setPropertyValue(Style.ATTRIBUTE_topBorderColor,getStyle().getAttribute(style.ATTRIBUTE_topBorderColor));
        sheetPanel.setPropertyValue(Style.ATTRIBUTE_topBorder,getStyle().getAttribute(style.ATTRIBUTE_topBorder));
        sheetPanel.setPropertyValue(Style.ATTRIBUTE_topPadding,getStyle().getAttribute(style.ATTRIBUTE_topPadding));
        sheetPanel.setPropertyValue(Style.ATTRIBUTE_vAlign,getStyle().getAttribute(style.ATTRIBUTE_vAlign));

        sheetPanel.setPropertyValue(Style.ATTRIBUTE_isBold,getStyle().getAttribute(style.ATTRIBUTE_isBold));
        sheetPanel.setPropertyValue(Style.ATTRIBUTE_isUnderline,getStyle().getAttribute(style.ATTRIBUTE_isUnderline));
        sheetPanel.setPropertyValue(Style.ATTRIBUTE_isItalic,getStyle().getAttribute(style.ATTRIBUTE_isItalic));
        sheetPanel.setPropertyValue(Style.ATTRIBUTE_isStrikeThrough,getStyle().getAttribute(style.ATTRIBUTE_isStrikeThrough));
        setInit(false);

    }

    public void setJasperTemplate(JasperTemplate jt)
    {
        this.jasperTemplate = jt;
        this.jButtonAddCondition.setEnabled(false);
    }
    /**
     * Set the style to edit. A temporary copy of the style will be created. That
     * modified values will be copied into the original style if modified.
     * All substyles will be replaced at the end of the editing (eventually with exact copies).
     */
    public void setStyle(Style myStyle) {

            Style copyStyle = new Style( myStyle );
            copyStyle.setName(myStyle.getName());
            this.setMasterStyle(copyStyle);
            
            if (myStyle instanceof TemplateStyle)
            {
                setJasperTemplate( ((TemplateStyle)myStyle).getJasperTemplate() );
            }
            else if (myStyle instanceof UndefinedStyle)
            {
                this.jButtonAddCondition.setEnabled(false);
            }
            
            if (myStyle.getAttributeBoolean(myStyle.ATTRIBUTE_isDefault, false))
            {
                this.getMasterStyle().getAttributes().put( myStyle.ATTRIBUTE_isDefault, new Boolean(true) );
            }

            setStyle(getMasterStyle(), false);

            this.setEditingStyle(myStyle);

            updateParentStylesList();

            setInit(true);

            ((DefaultListModel)jConditionsList.getModel()).removeAllElements();
            //
            ((DefaultListModel)jConditionsList.getModel()).addElement(I18n.getString("styleDialog.conditionsList.default","Default"));
            //((DefaultListModel)jConditionsList.getModel()).addElement("Default");
            //


            for (int i=0; i<getMasterStyle().getConditionedStyles().size(); ++i)
            {
                ((DefaultListModel)jConditionsList.getModel()).addElement(
                        getMasterStyle().getConditionedStyles().elementAt(i) );
            }

            this.jConditionsList.setSelectedIndex(0);

            setInit(false);
    }
    
    
    public void updateParentStylesList()
    {
        // currentReport
        if (it.businesslogic.ireport.gui.MainFrame.getMainInstance().getActiveReportFrame() != null)
        {
            currentReport = it.businesslogic.ireport.gui.MainFrame.getMainInstance().getActiveReportFrame().getReport();
            Enumeration enum_styles = currentReport.getStyles().elements();

            Vector tags = new Vector();
            while (enum_styles.hasMoreElements())
            {
                Style s = (Style)enum_styles.nextElement();
                if (s != this.getMasterStyle() && !s.getName().equals( this.getMasterStyle().getName() ) )
                {
                   tags.add(s); //new Tag(s)
                }
            }
            ComboBoxSheetProperty sp = (ComboBoxSheetProperty)getSheetPanel().getSheetProperty(Style.ATTRIBUTE_style);
            Object s = sp.getValue();
            sp.updateValues(tags,true);
            ((JComboBox)sp.getEditor()).setSelectedItem(s);
        }
    }

    /**
     * Set the style to edit. Remember to call saveCurrentStyle() if needed before change the displayed
     * style.
     * if conditionalStyle == true, the common fields are set disabled.
     */
    private void setStyle(Style myStyle, boolean conditionalStyle)
    {
        this.style = myStyle;

        ((SheetProperty)sheetPanel.getSheetProperty(Style.ATTRIBUTE_name)).setReadOnly(conditionalStyle);
        ((SheetProperty)sheetPanel.getSheetProperty(Style.ATTRIBUTE_isDefault)).setReadOnly(conditionalStyle);
        ((SheetProperty)sheetPanel.getSheetProperty(Style.ATTRIBUTE_style)).setReadOnly(conditionalStyle);

        setIreportFont(new IReportFont());

        if (myStyle.getAttributes().get( myStyle.ATTRIBUTE_isBold) != null)
        getIreportFont().setBold( myStyle.getAttributeBoolean( myStyle.ATTRIBUTE_isBold, false));

        if (myStyle.getAttributes().get( myStyle.ATTRIBUTE_fontName) != null)
        getIreportFont().setFontName( myStyle.getAttributeString( myStyle.ATTRIBUTE_fontName, getIreportFont().getFontName()));

        if (myStyle.getAttributes().get( myStyle.ATTRIBUTE_fontSize) != null)
        getIreportFont().setFontSize( myStyle.getAttributeInteger( myStyle.ATTRIBUTE_fontSize, getIreportFont().getFontSize()));

        if (myStyle.getAttributes().get( myStyle.ATTRIBUTE_isItalic) != null)
        getIreportFont().setItalic( myStyle.getAttributeBoolean( myStyle.ATTRIBUTE_isItalic, false));

        if (myStyle.getAttributes().get( myStyle.ATTRIBUTE_isPdfEmbedded) != null)
        getIreportFont().setPdfEmbedded( myStyle.getAttributeBoolean( myStyle.ATTRIBUTE_isPdfEmbedded, false));

        if (myStyle.getAttributes().get( myStyle.ATTRIBUTE_pdfEncoding) != null)
        getIreportFont().setPdfEncoding( myStyle.getAttributeString( myStyle.ATTRIBUTE_pdfEncoding,  getIreportFont().getPdfEncoding()));

        if (myStyle.getAttributes().get( myStyle.ATTRIBUTE_isStrikeThrough) != null)
        getIreportFont().setStrikeTrought( myStyle.getAttributeBoolean( myStyle.ATTRIBUTE_isStrikeThrough, false));

        // TODO = We have to understand what kind of font is this...
        if (myStyle.getAttributes().get( myStyle.ATTRIBUTE_pdfFontName) != null)
        getIreportFont().setPDFFontName( myStyle.getAttributeString( myStyle.ATTRIBUTE_pdfFontName, getIreportFont().getPDFFontName()));

        if (myStyle.getAttributes().get( myStyle.ATTRIBUTE_isUnderline) != null)
        getIreportFont().setUnderline( myStyle.getAttributeBoolean( myStyle.ATTRIBUTE_isUnderline, false));

        updateParentStylesList();
        updateStyleValues();


        panelSample.setStyle(myStyle);
    }

    public IReportFont getIreportFont() {
        if (ireportFont == null) ireportFont = new IReportFont();
        return ireportFont;
    }

    public void setIreportFont(IReportFont ireportFont) {
        this.ireportFont = ireportFont;
    }

    private Style getMasterStyle() {
        return masterStyle;
    }

    private void setMasterStyle(Style masterStyle) {
        this.masterStyle = masterStyle;
    }

    /**
     * This method save all the propeties present in the sheet into the
     * current style (pointed by the variable style)
     */
    private void saveCurrentStyle()
    {
        // Store all values...
        //System.out.println("Saving: " + getStyle());
        Vector properties = this.sheetPanel.getProperties();

        if (getStyle() instanceof ConditionedStyle)
        {
            String condition = ((ConditionedStyle)getStyle()).getCondition();
            getStyle().getAttributes().clear();
            ((ConditionedStyle)getStyle()).setCondition(condition);
        }
        else
        {
            getStyle().getAttributes().clear();
        }

        for (int i=0; i<properties.size(); ++i)
        {
            SheetProperty sp = (SheetProperty)properties.get(i);
            if ( sp.getValue() != null)
            {
                getStyle().getAttributes().put( sp.getKeyName(),  sp.getValue());
                //System.out.println(sp.getKeyName() + " " + sp.getValue());
            }
        }

        if ( getStyle().getAttributes().get(getStyle().ATTRIBUTE_style) != null &&
             !(getStyle().getAttributes().get(getStyle().ATTRIBUTE_style) instanceof Style))
        {
            String sname = ""+getStyle().getAttributes().get(getStyle().ATTRIBUTE_style);
            if (sname.trim().length() == 0)
            {
                getStyle().getAttributes().remove(getStyle().ATTRIBUTE_style);
            }
            else if (currentReport != null)
            {
                for (int j=0; j<currentReport.getStyles().size(); ++j)
                {
                    Style sparent = (Style)currentReport.getStyles().elementAt(j);
                    if (sparent.getName().equals( sname ))
                    {
                        getStyle().getAttributes().put(getStyle().ATTRIBUTE_style, sparent);
                    }
                }
            }
        }

        if (getStyle().getAttributeBoolean(getStyle().ATTRIBUTE_isDefault, false))
        {
            if (!isLibraryStyle() && !isReadOnly() && jasperTemplate != null)
            {
                Enumeration enum_s = MainFrame.getMainInstance().getActiveReportFrame().getReport().getStyles().elements();
                while (enum_s.hasMoreElements())
                {
                    Style tmp_s = (Style)enum_s.nextElement();
                    if (tmp_s != getStyle())
                    {
                        tmp_s.getAttributes().remove( getStyle().ATTRIBUTE_isDefault);
                    }
                }
            }
        }

        if (ireportFont != null)
        {
                getStyle().getAttributes().put( Style.ATTRIBUTE_fontName,  ireportFont.getBeanProperties().get( ireportFont.FONT_NAME) );

                if (ireportFont.getBeanProperties().get( ireportFont.FONT_SIZE) != null)
                getStyle().getAttributes().put( Style.ATTRIBUTE_fontSize, new Integer(ireportFont.getFontSize()));

                if (ireportFont.getBeanProperties().get( ireportFont.IS_BOLD) != null)
                getStyle().getAttributes().put( style.ATTRIBUTE_isBold, new Boolean( ireportFont.isBold()));

                if (ireportFont.getBeanProperties().get( ireportFont.IS_ITALIC) != null)
                getStyle().getAttributes().put( style.ATTRIBUTE_isItalic, new Boolean( ireportFont.isItalic()));

                if (ireportFont.getBeanProperties().get( ireportFont.IS_UNDERLINE) != null)
                getStyle().getAttributes().put( style.ATTRIBUTE_isUnderline, new Boolean( ireportFont.isUnderline()));

                if (ireportFont.getBeanProperties().get( ireportFont.IS_STRIKETROUGHT) != null)
                getStyle().getAttributes().put( style.ATTRIBUTE_isStrikeThrough, new Boolean( ireportFont.isStrikeTrought()));

                if (ireportFont.getBeanProperties().get( ireportFont.IS_PDF_EMBEDDED) != null)
                getStyle().getAttributes().put( style.ATTRIBUTE_isPdfEmbedded, new Boolean( ireportFont.isPdfEmbedded()));

                if (ireportFont.getBeanProperties().get( ireportFont.PDF_FONT_NAME) != null)
                getStyle().getAttributes().put( Style.ATTRIBUTE_pdfFontName,   ireportFont.getPDFFontName());
                
                if (ireportFont.getBeanProperties().get( ireportFont.PDF_ENCODING) != null)
                getStyle().getAttributes().put( Style.ATTRIBUTE_pdfEncoding,  ireportFont.getPdfEncoding());

        }
        /*
        HashMap hm = getStyle().getAttributes();
        Iterator i_keys = hm.keySet().iterator();

            while (i_keys.hasNext())
            {
                Object key = i_keys.next();
                System.out.println( key + ": " +hm.get(key) );
            }
         */
    }

    public boolean isInit() {
        return init;
    }

    public void setInit(boolean init) {
        this.init = init;
    }

    public Style getEditingStyle() {
        return editingStyle;
    }

    public void setEditingStyle(Style editingStyle) {
        this.editingStyle = editingStyle;
    }

    public boolean isLibraryStyle() {
        return libraryStyle;
    }

    public void setLibraryStyle(boolean libraryStyle) {
        this.libraryStyle = libraryStyle;
    }
    public void applyI18n(){
                // Start autogenerated code ----------------------
                jButtonAddCondition.setText(I18n.getString("styleDialog.buttonAddCondition","Add"));
                jButtonCancel.setText(I18n.getString("styleDialog.buttonCancel","Cancel"));
                jButtonDownCondition.setText(I18n.getString("styleDialog.buttonDownCondition","Down"));
                jButtonModifyCondition.setText(I18n.getString("styleDialog.buttonModifyCondition","Modify"));
                jButtonOK.setText(I18n.getString("styleDialog.buttonOK","OK"));
                jButtonRemoveCondition.setText(I18n.getString("styleDialog.buttonRemoveCondition","Remove"));
                jButtonUpCondition.setText(I18n.getString("styleDialog.buttonUpCondition","up"));
                jLabelSample.setText(I18n.getString("styleDialog.labelSample","Sample"));
                jLabelStyleConditions.setText(I18n.getString("styleDialog.labelStyleConditions","Style conditions"));
                // End autogenerated code ----------------------
                
                //
                this.setTitle(I18n.getString("styleDialog.title","Add/modify style"));
                jButtonCancel.setMnemonic(I18n.getString("styleDialog.buttonCancelMnemonic","c").charAt(0));
                jButtonOK.setMnemonic(I18n.getString("styleDialog.buttonOKMnemonic","o").charAt(0));
                //
    }

    
    private int openCondition = -1;
    /**
     * Open the condition numbered itemIndex, once this window becomes visible...
     */
    public void setOpenCondition(int itemIndex) {
        this.openCondition = itemIndex;
    }
}
