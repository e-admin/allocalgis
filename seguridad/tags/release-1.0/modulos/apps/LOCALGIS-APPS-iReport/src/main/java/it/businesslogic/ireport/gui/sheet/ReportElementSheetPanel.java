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
 * ReportElementSheetPanel.java
 * 
 * Created on 16 febbraio 2005, 6.57
 *
 */

package it.businesslogic.ireport.gui.sheet;

import it.businesslogic.ireport.*;
import it.businesslogic.ireport.BarcodeReportElement;
import it.businesslogic.ireport.FontListLoader;
import it.businesslogic.ireport.GraphicReportElement;
import it.businesslogic.ireport.IReportFont;
import it.businesslogic.ireport.LineReportElement;
import it.businesslogic.ireport.RectangleReportElement;
import it.businesslogic.ireport.TextFieldReportElement;
import it.businesslogic.ireport.TextReportElement;
import it.businesslogic.ireport.crosstab.CrosstabCell;
import it.businesslogic.ireport.gui.*;
import it.businesslogic.ireport.gui.JNumberComboBox;
import it.businesslogic.ireport.gui.JNumberField;
import it.businesslogic.ireport.gui.event.*;
import it.businesslogic.ireport.util.*;
import it.businesslogic.ireport.util.LanguageChangedEvent;
import it.businesslogic.ireport.util.LanguageChangedListener;
import java.awt.Point;
import java.util.*;
import javax.swing.FocusManager;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

/**
 *
 * @author  Administrator
 */
public class ReportElementSheetPanel extends CategorySheetPanel implements ReportListener, LanguageChangedListener, ReportFrameActivatedListener {
    
    // Sheet properties
    private ComboBoxSheetProperty spBands;
    private SheetProperty spTop;
    private SheetProperty spLeft;
    private SheetProperty spHeight;
    private SheetProperty spWidth;
    private SheetProperty spFgColor;
    private SheetProperty spBgColor;
    private SheetProperty spMode;
    
    private SheetProperty spPrintRepeatedValues;
    private SheetProperty spPrintWhenDetailOverflows;
    private SheetProperty spPrintInFirstWholeBand;
    private SheetProperty spRemoveLineWhenBlank;
    private SheetProperty spPositionType;
    private SheetProperty spElementKey;
    private SheetProperty spStretchType;
    private ComboBoxSheetProperty spStyle;
    
    private ComboBoxSheetProperty spGroups;
    private ExpressionSheetProperty spPrintWhenExpression;
    
    private SheetProperty spFill;
    private SheetProperty spPen;
    
    private SheetProperty spRadius;
    private SheetProperty spDirection;
    
    private SheetProperty spTextHAlign;
    private SheetProperty spTextVAlign;
    private SheetProperty spStyledText;
    private SheetProperty spLineSpacing;
    private SheetProperty spRotate;
    
    private SheetProperty spFontName;
    private NumberComboBoxSheetProperty spFontSize;
    private SheetProperty spPdfFontName;
    private SheetProperty spBold;
    private SheetProperty spItalic;
    private SheetProperty spUnderline;
    private SheetProperty spStriketrough;
    private SheetProperty spPdfEmbedded;
    private SheetProperty spPdfEncoding;
    
    private ExpressionSheetProperty spStaticText;
    private ExpressionSheetProperty spTextfieldExpression;
    private SheetProperty spTextfieldExpressionClass;
    private SheetProperty spTextfieldEvaluationTime;
    private ComboBoxSheetProperty spTextfieldEvaluationGroup;
    private SheetProperty spStretchWithOverflow;
    private SheetProperty spTextfieldBlankWhenNull;
    private PatternSheetProperty spPattern;
    
    private ExpressionSheetProperty spImageExpression;
    private SheetProperty spImageExpressionClass;
    private SheetProperty spImageEvaluationTime;
    private ComboBoxSheetProperty spImageEvaluationGroup;
    private SheetProperty spImageScale;
    private SheetProperty spImageError;
    private SheetProperty spImageHAlign;
    private SheetProperty spImageVAlign;
    private SheetProperty spImageLazy;
    private SheetProperty spImageCache;
    
    
    private SheetProperty spBCType;
    private SheetProperty spBCChecksum;
    private SheetProperty spBCShowText;
    private ExpressionSheetProperty spBCExpression;
    private SheetProperty spBCBarWidth;
    private SheetProperty spBCBarHeight;
    private ExpressionSheetProperty spBCApplicationIdentifier;
    private SheetProperty spBCScale;
    private SheetProperty spBCError;
    private SheetProperty spBCHAlign;
    private SheetProperty spBCVAlign;
    private SheetProperty spBCEvaluationTime;
    private ComboBoxSheetProperty spBCEvaluationGroup;
    
    private SheetProperty spBreakType;
    
    // --- CAHRT ---
    private SheetProperty spChartEvaluationTime;
    private ComboBoxSheetProperty spChartEvaluationGroup;
    
    
    public  static java.awt.Color sharedDifferentValueLabelColor = java.awt.Color.red.darker().darker();
    public  static java.awt.Color mandatoryPropertiesLabelColor = java.awt.Color.blue;
    public  static java.awt.Color notMandatoryPropertiesLabelColor = java.awt.Color.black;
        
    private JReportFrame jrf = null;
    private boolean init = false;   
    
    private Vector elementSelection = new Vector();
    
    /** Creates a new instance of ReportElementSheetPanel */
    public ReportElementSheetPanel() {
        super();
        
        initSheetProperties();
        // We have to register for element changes...
        MainFrame mf = MainFrame.getMainInstance();
        mf.addReportListener( this );
        mf.addReportFrameActivatedListener( this);
        
        I18n.addOnLanguageChangedListener( this );
        
        MainFrame.getMainInstance().addFontsListChangedListener( new FontsListChangedListener() {
            public void fontsListChanged(FontsListChangedEvent evt) {
                
                boolean localinit = isInit();
                setInit(true);
                updateReportFonts();
                setInit(localinit);
            }
        } );
        
        
    }

    public void reportFrameActivated(ReportFrameActivatedEvent evt) {
    
        if (evt.getReportFrame() == null)
        {
            updateSelection(evt.getReportFrame());
        }
        
    }
    
    public void languageChanged(LanguageChangedEvent evt) {
        super.applyI18n();
        this.removeAllProperties();
        initSheetProperties();
        updateSelection();
    }

    
     public void updateSelection()
     {
         JReportFrame newJrf = MainFrame.getMainInstance().getActiveReportFrame();
         updateSelection(newJrf);
     }
     
     /**
      * Numbers do not change when the focus is lost due to a selection change.
      * This apply the value....
      */
     public void applyValueForNumbers()
     {
         Enumeration e = getProperties().elements();
         while (e.hasMoreElements())
         {
             SheetProperty sp = (SheetProperty)e.nextElement();

             //if (sp instanceof NumberComboBoxSheetProperty)
             //{
             //    JNumberComboBox c = (JNumberComboBox)sp.getEditor();
             //    System.out.println("NNNN");
             //    if (c.hasFocus())
             //    {
             //       FocusManager.getCurrentManager().clearGlobalFocusOwner();
             //       System.out.println(FocusManager.getCurrentManager().getFocusOwner());
             //       return; // Only a component can be focused at time...
             //    }
             //}
             if (sp.getType() == sp.INTEGER || sp.getType() == sp.NUMBER)
             {
                 JComponent c = sp.getEditor();
                 if (c.hasFocus() && c instanceof JNumberField)
                 {
                    ((JNumberField)c).focusLost(null);
                    return; // Only a component can be focused at time...
                 }
             }
             
         }
     }
     
    /**
     * Update all the element properties...
     * 
     */
     public void updateSelection(JReportFrame newJrf)
     {
        if (getElementSelection().size() == 0 &&
            (newJrf == null || 
            (newJrf.getSelectedCrosstabEditorPanel() != null &&
             newJrf.getSelectedCrosstabEditorPanel().getSelectedElements().size() == 0) ||
             newJrf.getSelectedCrosstabEditorPanel() == null &&
             newJrf.getSelectedElements().size() == 0))
        {
            // Nothind to do!
            return;
        }
        
        // Fix for numbers focus losing...
        //****//       
        
        applyValueForNumbers();

        setInit(true);
        
        if (newJrf != null)
        {
             if (newJrf.getSelectedCrosstabEditorPanel() == null)
             {
                setElementSelection( newJrf.getSelectedElements() );
             }
             else
             {
                setElementSelection( newJrf.getSelectedCrosstabEditorPanel().getSelectedElements() );
             }
        }
        else
        {
             getElementSelection().removeAllElements();
        }
        

        this.removeAllProperties();

        
        this.jrf = newJrf;
        
        if (jrf == null || getElementSelection().size() == 0)
        {
            this.recreateSheet();
            return;
        }


        updateAllComboBoxes();
      
        
        try {
        Vector selectedElements = getElementSelection();
        
        boolean sameBand = true;
        boolean sameTop = true;
        boolean sameLeft = true;
        boolean sameWidth = true;
        boolean sameHeight = true;
        boolean sameForeground = true;
        boolean sameBackground = true;
        boolean sameMode = true;
        boolean samePrintRepeatedValues = true;
        boolean samePrintWhenDetailOverflows = true;
        boolean samePrintInFirstWholeBand = true;
        boolean sameRemoveLineWhenBlank = true;
        boolean samePositionType = true;
        boolean samePrintWhenGroupChanges = true;
        boolean sameStretchType = true;
        boolean sameStyle = true;
        boolean samePrintWhenExpression = true;
        
        // ---- GRAPHICS ELEMENT -----
        boolean areAllGraphicsElements = true;
        boolean sameFill = true;
        boolean samePen = true;
        
        // ---- RECTANGLE ELEMENT -----
        boolean areAllRectangleElements = true;
        boolean sameRadius = true;
        
        // ---- LINE ELEMENT -----
        boolean areAllLineElements = true;
        boolean sameDirection = true;
        
        // ---- TEXT ELEMENT ELEMENT -----
        boolean areAllTextElements = true;
        boolean sameHAlign = true;
        boolean sameVAlign = true;
        boolean sameStyledText = true;
        boolean sameLineSpacing = true;
        boolean sameRotate = true;
        boolean sameBold = true;
        boolean sameItalic = true;
        boolean sameUnderline = true;
        boolean sameStrikethrough = true;
        boolean samePdfEmbedded = true;
        boolean sameFontSize = true;
        boolean sameFontName = true;
        boolean samePDFFontName = true;
        boolean samePdfEncoding = true;
        
        // ---- BREAK ELEMENT ELEMENT -----
        boolean areAllBreakElements = true;
        boolean sameBreakType = true;
        
        // ---- SATIC TEXT ELEMENT ELEMENT -----
        boolean areAllStaticTextElements = true;
        boolean sameText = true;
        
        // ---- TEXTFIELD ELEMENT ELEMENT -----
        boolean areAllTextfieldElements = true;
        boolean sameTextfieldExpression = true;
        boolean sameTextfieldExpressionClass = true;
        boolean sameTextfieldEvaluationTime = true;
        boolean sameTextfieldEvaluationGroup = true;
        boolean sameStrtchWithOverflow = true;
        boolean sameBlankWhenNull = true;
        boolean samePattern = true;
        
        // ---- IAMGE ELEMENT -----
        boolean areAllImageElements = true;
        boolean sameImageExpression = true;
        boolean sameImageExpressionClass = true;
        boolean sameImageEvaluationTime = true;
        boolean sameImageEvaluationGroup = true;
        boolean sameImageLazy = true;
        boolean sameImageCache = true;
        boolean sameImageScale = true;
        boolean sameImageOnError = true;
        boolean sameImageVAlign = true;
        boolean sameImageHAlign = true;
        
        // ---- BARCODE ELEMENT ----
        boolean areAllBarcodeElements = true;
        boolean sameBCType = true;
        boolean sameBCChecksum = true;
        boolean sameBCShowText = true;
        boolean sameBCExpression = true;
        boolean sameBCBarWidth = true;
        boolean sameBCBarHeight = true;
        boolean sameBCApplicationIdentifier = true;
        boolean sameBCScale = true;
        boolean sameBCError = true;
        boolean sameBCHAlign = true;
        boolean sameBCVAlign = true;
        boolean sameBCEvaluationTime = true;
        boolean sameBCEvaluationGroup = true;
        
        // --- CHART ELEMENT ----
        boolean areAllChartElements = true;
        boolean sameChartEvaluationTime = true;
        boolean sameChartEvaluationGroup = true;

        boolean isTheFirstElement = true;
        
        //spBands.setSetting(true);
        Band last_band = null;
        int y_location = 0;
                
        for (int i=0; i<selectedElements.size(); ++i)
        {
            ReportElement re = (ReportElement)selectedElements.elementAt(i);
            if (re.getBand() != null && (last_band == null || last_band != re.getBand())) {
               y_location = this.jrf.getReport().getBandYLocation(re.getBand());
               last_band = re.getBand();
            }

            int x_location = this.jrf.getReport().getLeftMargin();

            if (re.getBand() == null && re.getCell() != null)
            {
                y_location = re.getCell().getTop();
                x_location = re.getCell().getLeft();
            }
                
            //if (sameBand) sameBand = setComboBox(isTheFirstElement, (re.getBand() == null) ? (Object)re.getCell() : (Object)re.getBand(), (JComboBox)spBands.getEditor());
            
            if (sameTop)
            {
                int position_y = re.getPosition().y;
                if (re.getParentElement() != null) position_y -= re.getParentElement().getPosition().y;
                else position_y = position_y - y_location - 10;
                
                sameTop = this.setElementNumber(isTheFirstElement, (double)position_y, (JNumberField)spTop.getEditor());
            }
            
            if (sameLeft)
            {
                int position_x = re.getPosition().x;
                if (re.getParentElement() != null) position_x -= re.getParentElement().getPosition().x;
                else position_x = position_x - 10 - x_location;
                
                sameLeft = this.setElementNumber(isTheFirstElement,position_x, (JNumberField)spLeft.getEditor());
            }
            if (sameWidth)  sameWidth = this.setElementNumber(isTheFirstElement, re.getWidth(), (JNumberField)spWidth.getEditor()); 
            if (sameHeight)  sameHeight = this.setElementNumber(isTheFirstElement, re.getHeight(), (JNumberField)spHeight.getEditor());
            if (sameForeground)  sameForeground = this.setColorProperty(isTheFirstElement, re.getColorValue(re.FGCOLOR, null), spFgColor);
            if (sameBackground)  sameBackground = this.setColorProperty(isTheFirstElement, re.getColorValue(re.BGCOLOR, null), spBgColor);
            
            if (sameMode)  sameMode = this.setCheckBox(isTheFirstElement, re.getTransparent().equals("Transparent"), re.getPropertyValue(re.MODE)==null, spMode );            
            if (samePrintRepeatedValues)  samePrintRepeatedValues = this.setCheckBox(isTheFirstElement, re.isIsPrintRepeatedValues(), false, spPrintRepeatedValues );
            if (samePrintWhenDetailOverflows)  samePrintWhenDetailOverflows= this.setCheckBox(isTheFirstElement, re.isIsPrintWhenDetailOverflows(), false, spPrintWhenDetailOverflows );
            if (samePrintInFirstWholeBand)  samePrintInFirstWholeBand = this.setCheckBox(isTheFirstElement, re.isIsPrintInFirstWholeBand(), false, spPrintInFirstWholeBand );
            if (sameRemoveLineWhenBlank)  sameRemoveLineWhenBlank = this.setCheckBox(isTheFirstElement, re.isIsRemoveLineWhenBlank(), false, spRemoveLineWhenBlank );
            if (samePositionType) samePositionType = setTagComboBox(isTheFirstElement, re.getPositionType(), spPositionType);
            if (samePrintWhenGroupChanges) samePrintWhenGroupChanges = setTagComboBox(isTheFirstElement, re.getPrintWhenGroupChanges(), spGroups);
            if (sameStretchType) sameStretchType = setTagComboBox(isTheFirstElement, re.getStretchType(), spStretchType);
            if (sameStyle) sameStyle = setTagComboBox(isTheFirstElement, (re.getStyle() == null) ? (Object)"" : (Object)re.getStyle(), spStyle);
            if (samePrintWhenExpression) samePrintWhenExpression = setTextArea(isTheFirstElement, re.getPrintWhenExpression(), spPrintWhenExpression);
            if (i==0) spElementKey.setValue(re.getKey());           

            // ---- GRAPHIC ELEMENTS ----
            if (areAllGraphicsElements && (re instanceof GraphicReportElement))
            {
                GraphicReportElement gre = (GraphicReportElement)re;
                if (samePen) samePen = setTagComboBox(isTheFirstElement, re.getPropertyValue(gre.PEN), spPen);
                if (sameFill) sameFill = setTagComboBox(isTheFirstElement, re.getPropertyValue(gre.FILL), spFill);
            }
            else
            {
                areAllGraphicsElements = false;
            }
            
            // ---- RECTANGLE ELEMENTS ----
            if (areAllRectangleElements && (re instanceof RectangleReportElement))
            {
                RectangleReportElement rre = (RectangleReportElement)re;
                if (sameRadius) sameRadius = setGenericSheetProperty(isTheFirstElement, rre.getPropertyValue(rre.RADIUS), spRadius);
            }
            else
            {
                areAllRectangleElements = false;
            }   
            
            // ---- LINE ELEMENTS ----
            if (areAllLineElements && (re instanceof LineReportElement))
            {
                LineReportElement rre = (LineReportElement)re;
                if (sameDirection) sameDirection = setTagComboBox(isTheFirstElement, rre.getDirection(), spDirection);
            }
            else
            {
                areAllLineElements = false;
            }   
            
            // ---- TEXT ELEMENTS ----
            if (areAllTextElements && (re instanceof TextReportElement))
            {
                TextReportElement rre = (TextReportElement)re;
                if (sameHAlign) sameHAlign = setTagComboBox(isTheFirstElement, re.getPropertyValue(rre.ALIGN), spTextHAlign);
                if (sameVAlign) sameVAlign = setTagComboBox(isTheFirstElement, re.getPropertyValue(rre.VERTICAL_ALIGN), spTextVAlign);
                if (sameStyledText)  sameStyledText = this.setCheckBox(isTheFirstElement, rre.isIsStyledText(), re.getPropertyValue(rre.IS_STYLED_TEXT)==null, spStyledText ); 
                if (sameLineSpacing) sameLineSpacing = setTagComboBox(isTheFirstElement, re.getPropertyValue(rre.LINE_SPACING), spLineSpacing);
                if (sameRotate) sameRotate = setTagComboBox(isTheFirstElement, re.getPropertyValue(rre.ROTATE), spRotate);
                
                if (sameBold)  sameBold = this.setCheckBox(isTheFirstElement, rre.isBold(), rre.getIReportFont().getPropertyValue( IReportFont.IS_BOLD)==null, spBold );
                if (sameItalic)  sameItalic = this.setCheckBox(isTheFirstElement, rre.isItalic(), rre.getIReportFont().getPropertyValue( IReportFont.IS_ITALIC)==null, spItalic );
                if (sameUnderline)  sameUnderline = this.setCheckBox(isTheFirstElement, rre.isUnderline(), rre.getIReportFont().getPropertyValue( IReportFont.IS_UNDERLINE)==null, spUnderline );
                if (sameStrikethrough)  sameStrikethrough = this.setCheckBox(isTheFirstElement, rre.isStrikeTrought(), rre.getIReportFont().getPropertyValue( IReportFont.IS_STRIKETROUGHT)==null, spStriketrough );
                if (samePdfEmbedded)  samePdfEmbedded = this.setCheckBox(isTheFirstElement, rre.isPdfEmbedded(), rre.getIReportFont().getPropertyValue( IReportFont.IS_PDF_EMBEDDED)==null, spPdfEmbedded );
                if (sameFontSize) sameFontSize = this.setElementNumber(isTheFirstElement, rre.getFontSize(), rre.getIReportFont().getPropertyValue( IReportFont.FONT_SIZE), spFontSize);
                if (sameFontName) sameFontName = setTagComboBox(isTheFirstElement, rre.getIReportFont().getPropertyValue( IReportFont.FONT_NAME), spFontName);
                if (samePDFFontName) samePDFFontName = this.setTagComboBox(isTheFirstElement, rre.getIReportFont().getPropertyValue( IReportFont.PDF_FONT_NAME) , spPdfFontName );
                if (samePdfEncoding) samePdfEncoding = this.setTagComboBox(isTheFirstElement, rre.getIReportFont().getPropertyValue( IReportFont.PDF_ENCODING) , spPdfEncoding );
                
            }
            else
            {
                areAllTextElements = false;
            }   
            
            // ---- STATIC TEXT ELEMENTS ----
            if (areAllStaticTextElements && (re instanceof StaticTextReportElement))
            {
                StaticTextReportElement rre = (StaticTextReportElement)re;
                if (sameText) sameText = setTextArea(isTheFirstElement, rre.getText(), spStaticText);
            }
            else
            {
                areAllStaticTextElements = false;
            }   
            
            // ---- TEXTFIELD ELEMENTS ----
            if (areAllTextfieldElements && (re instanceof TextFieldReportElement))
            {
                TextFieldReportElement rre = (TextFieldReportElement)re;
                if (sameTextfieldExpression) sameTextfieldExpression = setTextArea(isTheFirstElement, rre.getText(), spTextfieldExpression);
                if (sameTextfieldExpressionClass) sameTextfieldExpressionClass = setTagComboBox(isTheFirstElement, rre.getClassExpression(), spTextfieldExpressionClass);
                if (sameTextfieldEvaluationTime) sameTextfieldEvaluationTime = setTagComboBox(isTheFirstElement, rre.getEvaluationTime(), spTextfieldEvaluationTime);
                if (sameTextfieldEvaluationGroup && sameTextfieldEvaluationTime) sameTextfieldEvaluationGroup = setTagComboBox(isTheFirstElement, rre.getGroup(), spTextfieldEvaluationGroup);
                if (sameStrtchWithOverflow) sameStrtchWithOverflow = setCheckBox(isTheFirstElement, rre.isStretchWithOverflow(), false, spStretchWithOverflow);
                if (sameBlankWhenNull) sameBlankWhenNull = setCheckBox(isTheFirstElement, rre.isBlankWhenNull(), false, spTextfieldBlankWhenNull);     
                if (samePattern) samePattern = setTextPattern(isTheFirstElement, re.getPropertyValue( rre.PATTERN ), spPattern);
            }
            else
            {
                areAllTextfieldElements = false;
            }
            
            // ---- IMAGE ELEMENTS ----
            if (areAllImageElements && (re instanceof ImageReportElement) && !(re instanceof BarcodeReportElement))
            {
                ImageReportElement rre = (ImageReportElement)re;
                if (sameImageExpression) sameImageExpression = setTextArea(isTheFirstElement, rre.getImageExpression(), spImageExpression);
                if (sameImageExpressionClass) sameImageExpressionClass = setTagComboBox(isTheFirstElement, rre.getImageClass(), spImageExpressionClass);
                if (sameImageEvaluationTime) sameImageEvaluationTime = setTagComboBox(isTheFirstElement, rre.getEvaluationTime(), spImageEvaluationTime);
                if (sameImageEvaluationGroup && sameImageEvaluationTime) sameImageEvaluationGroup = setTagComboBox(isTheFirstElement, rre.getEvaluationGroup(), spImageEvaluationGroup);
                if (sameImageLazy) sameImageLazy = setCheckBox(isTheFirstElement, rre.isIsLazy(), false, spImageLazy);
                if (sameImageScale) sameImageScale = setTagComboBox(isTheFirstElement, re.getPropertyValue(rre.SCALE), spImageScale);
                if (sameImageCache) sameImageCache = setCheckBox(isTheFirstElement, rre.isIsUsingCache(), rre.getPropertyValue(rre.USING_CACHE)==null, spImageCache);     
                if (sameImageVAlign) sameImageVAlign = setTagComboBox(isTheFirstElement, re.getPropertyValue(rre.VERTICAL_ALIGN), spImageVAlign);
                if (sameImageHAlign) sameImageHAlign = setTagComboBox(isTheFirstElement, re.getPropertyValue(rre.HORIZONTAL_ALIGN), spImageHAlign);
            }
            else
            {
                areAllImageElements = false;
            }
        
            // ---- IMAGE ELEMENTS ----
            if (areAllBarcodeElements && re instanceof BarcodeReportElement)
            {
                BarcodeReportElement rre = (BarcodeReportElement)re;
                if (sameBCType) sameBCType = setTagComboBox(isTheFirstElement, rre.getType()+"", spBCType);
                if (sameBCBarWidth) sameBCBarWidth = setGenericSheetProperty(isTheFirstElement, ""+rre.getImageWidth(), spBCBarWidth);
                if (sameBCBarHeight) sameBCBarHeight = setGenericSheetProperty(isTheFirstElement, ""+rre.getImageHeight(), spBCBarHeight);
                if (sameBCExpression) sameBCExpression = setTextArea(isTheFirstElement, rre.getText(), spBCExpression);
                if (sameBCApplicationIdentifier) sameBCApplicationIdentifier = setTextArea(isTheFirstElement, rre.getApplicationIdentifier(), spBCApplicationIdentifier);
                if (sameBCEvaluationTime) sameBCEvaluationTime = setTagComboBox(isTheFirstElement, rre.getEvaluationTime(), spBCEvaluationTime);
                if (sameBCEvaluationGroup && sameBCEvaluationTime) sameBCEvaluationGroup = setTagComboBox(isTheFirstElement, rre.getEvaluationGroup(), spBCEvaluationGroup);
                if (sameBCShowText) sameBCShowText = setCheckBox(isTheFirstElement, rre.isShowText(), false, spBCShowText);
                if (sameBCChecksum) sameBCChecksum = setCheckBox(isTheFirstElement, rre.isCheckSum(), false, spBCChecksum);
                if (sameBCScale) sameBCScale = setTagComboBox(isTheFirstElement, re.getPropertyValue(rre.SCALE), spBCScale);
                if (sameBCVAlign) sameBCVAlign = setTagComboBox(isTheFirstElement, re.getPropertyValue(rre.VERTICAL_ALIGN), spBCVAlign);
                if (sameBCHAlign) sameBCHAlign = setTagComboBox(isTheFirstElement, re.getPropertyValue(rre.HORIZONTAL_ALIGN), spBCHAlign);
            }
            else
            {
                areAllBarcodeElements = false;
            }
            
            if (areAllBreakElements && (re instanceof BreakReportElement))
            {
                BreakReportElement rre = (BreakReportElement)re;
                if (sameBreakType) sameBreakType = setTagComboBox(isTheFirstElement, rre.getType(), spBreakType);
                
            }
            else
            {
                areAllBreakElements = false;
            }
            
            // ---- IMAGE ELEMENTS ----
            if (areAllChartElements && re instanceof ChartReportElement2)
            {
                ChartReportElement2 rre = (ChartReportElement2)re;
                if (sameChartEvaluationTime) sameChartEvaluationTime = setTagComboBox(isTheFirstElement, rre.getEvaluationTime(), spChartEvaluationTime);
                if (sameChartEvaluationGroup && sameChartEvaluationTime) sameChartEvaluationGroup = setTagComboBox(isTheFirstElement, rre.getEvaluationGroup(), spChartEvaluationGroup);
                }
            else
            {
                areAllChartElements = false;
            }
            
            if (sameBand) sameBand = setComboBox(isTheFirstElement, (re.getBand() == null) ? (Object)re.getCell() : (Object)re.getBand(), (JComboBox)spBands.getEditor());
            
            isTheFirstElement = false;
        }        

        spBands.setLabelColor( (sameBand) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spTop.setLabelColor( (sameTop) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spLeft.setLabelColor( (sameLeft) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spHeight.setLabelColor( (sameHeight) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spWidth.setLabelColor( (sameWidth) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spFgColor.setLabelColor( (sameForeground) ? notMandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spBgColor.setLabelColor( (sameBackground) ? notMandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spMode.setLabelColor( (sameMode) ? notMandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spPrintRepeatedValues.setLabelColor( (samePrintRepeatedValues) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spPrintWhenDetailOverflows.setLabelColor( (samePrintWhenDetailOverflows) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spPrintInFirstWholeBand.setLabelColor( (samePrintInFirstWholeBand) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spRemoveLineWhenBlank.setLabelColor( (sameRemoveLineWhenBlank) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spPositionType.setLabelColor( (samePositionType) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spGroups.setLabelColor( (samePrintWhenGroupChanges) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spStretchType.setLabelColor( (sameStretchType) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spStyle.setLabelColor( (sameStyle) ? notMandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spPrintWhenExpression.setLabelColor( (samePrintWhenExpression) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spPen.setLabelColor( (samePen) ? notMandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spFill.setLabelColor( (sameFill) ? notMandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spRadius.setLabelColor( (sameRadius) ? notMandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spDirection.setLabelColor( (sameDirection) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spBreakType.setLabelColor( (sameBreakType) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spTextHAlign.setLabelColor( (sameHAlign) ? notMandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spTextVAlign.setLabelColor( (sameVAlign) ? notMandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spStyledText.setLabelColor( (sameStyledText) ? notMandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spLineSpacing.setLabelColor( (sameLineSpacing) ? notMandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spRotate.setLabelColor( (sameRotate) ? notMandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spBold.setLabelColor( (sameBold) ? notMandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spItalic.setLabelColor( (sameItalic) ? notMandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spUnderline.setLabelColor( (sameUnderline) ? notMandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spStriketrough.setLabelColor( (sameStrikethrough) ? notMandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spPdfEmbedded.setLabelColor( (samePdfEmbedded) ? notMandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spFontSize.setLabelColor( (sameFontSize) ? notMandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spFontName.setLabelColor( (sameFontName) ? notMandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spPdfFontName.setLabelColor( (samePDFFontName) ? notMandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spPdfEncoding.setLabelColor( (samePdfEncoding) ? notMandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spStaticText.setLabelColor( (sameText) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spTextfieldExpression.setLabelColor( (sameTextfieldExpression) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spTextfieldExpressionClass.setLabelColor( (sameTextfieldExpressionClass) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spTextfieldEvaluationTime.setLabelColor( (sameTextfieldEvaluationTime) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spTextfieldEvaluationGroup.setLabelColor( (sameTextfieldEvaluationGroup) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spStretchWithOverflow.setLabelColor( (sameStrtchWithOverflow) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spTextfieldBlankWhenNull.setLabelColor( (sameBlankWhenNull) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spPattern.setLabelColor( (sameBlankWhenNull) ? notMandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        
        spImageExpression.setLabelColor( (sameImageExpression) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spImageExpressionClass.setLabelColor( (sameImageExpressionClass) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spImageEvaluationTime.setLabelColor( (sameImageEvaluationTime) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spImageEvaluationGroup.setLabelColor( (sameImageEvaluationGroup) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spImageLazy.setLabelColor( (sameImageLazy) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spImageCache.setLabelColor( (sameImageCache) ? notMandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spImageScale.setLabelColor( (sameImageScale) ? notMandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spImageError.setLabelColor( (sameImageOnError) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spImageVAlign.setLabelColor( (sameImageVAlign) ? notMandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spImageHAlign.setLabelColor( (sameImageHAlign) ? notMandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spBCType.setLabelColor( (sameBCType) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spBCChecksum.setLabelColor( (sameBCChecksum) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spBCShowText.setLabelColor( (sameBCShowText) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spBCExpression.setLabelColor( (sameBCExpression) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spBCBarWidth.setLabelColor( (sameBCBarWidth) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spBCBarHeight.setLabelColor( (sameBCBarHeight) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spBCApplicationIdentifier.setLabelColor( (sameBCApplicationIdentifier) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spBCScale.setLabelColor( (sameBCScale) ? notMandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spBCError.setLabelColor( (sameBCError) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spBCHAlign.setLabelColor( (sameBCHAlign) ? notMandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spBCVAlign.setLabelColor( (sameBCVAlign) ? notMandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spBCEvaluationTime.setLabelColor( (sameBCEvaluationTime) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spBCEvaluationGroup.setLabelColor( (sameBCEvaluationGroup) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spChartEvaluationTime.setLabelColor( (sameChartEvaluationTime) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );
        spChartEvaluationGroup.setLabelColor( (sameChartEvaluationGroup) ? mandatoryPropertiesLabelColor : sharedDifferentValueLabelColor );

                
        ExpressionContext ec = new ExpressionContext();
        if (jrf.getSelectedCrosstabEditorPanel() == null)
        {
            ec.setSubDataset( jrf.getReport() );
        }
        else
        {
            ec.addCrosstabReportElement( jrf.getSelectedCrosstabEditorPanel().getCrosstabElement());
        }
        spPrintWhenExpression.setExpressionContext( ec );
        spTextfieldExpression.setExpressionContext( ec );
        spImageExpression.setExpressionContext( ec );
        spBCExpression.setExpressionContext( ec );
        spBCApplicationIdentifier.setExpressionContext( ec );
        

        
        String commonStr = it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.common","Common");
        this.addSheetProperty(commonStr, spBands);
        this.addSheetProperty(commonStr, spTop);
        this.addSheetProperty(commonStr, spLeft);
        this.addSheetProperty(commonStr, spHeight);
        this.addSheetProperty(commonStr, spWidth); 
        this.addSheetProperty(commonStr, spFgColor);
        this.addSheetProperty(commonStr, spBgColor);
        this.addSheetProperty(commonStr, spMode);
        
        spPrintRepeatedValues.setDefaultValue( new Boolean(true));
        
        this.addSheetProperty(commonStr, spRemoveLineWhenBlank);
        this.addSheetProperty(commonStr, spPrintInFirstWholeBand);
        this.addSheetProperty(commonStr, spPrintWhenDetailOverflows);
        this.addSheetProperty(commonStr, spPrintRepeatedValues);
        this.addSheetProperty(commonStr, spPositionType);
        this.addSheetProperty(commonStr, spGroups);
        if (selectedElements.size() == 1)
        {
            this.addSheetProperty(commonStr, spElementKey);
        }
        this.addSheetProperty(commonStr, spStretchType);
        this.addSheetProperty(commonStr, spStyle);
        this.addSheetProperty(commonStr, spPrintWhenExpression);
        
        if (areAllGraphicsElements)
        {
            String graphicStr = it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.graphic","Graphic");
            this.addSheetProperty(graphicStr, spPen);
            this.addSheetProperty(graphicStr, spFill);
        }
        
        if (areAllRectangleElements)
        {
            String rectangleStr = it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.rectangle","Rectangle");
            this.addSheetProperty(rectangleStr, spRadius);
        }
        
        if (areAllLineElements)
        {
            String lineStr = it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.line","Line");
            this.addSheetProperty(lineStr, spDirection);
        }
        
        if (areAllBreakElements)
        {
            String breakStr = it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.break","Break");
            this.addSheetProperty(breakStr, spBreakType);
        }
        
        
        if (areAllTextElements)
        {
            String textStr = it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.text","Text");
            
            this.addSheetProperty(textStr, spFontName);
            this.addSheetProperty(textStr, spFontSize);
            this.addSheetProperty(textStr, spBold);
            this.addSheetProperty(textStr, spItalic);
            this.addSheetProperty(textStr, spUnderline);
            this.addSheetProperty(textStr, spStriketrough);
            this.addSheetProperty(textStr, spPdfFontName);
            this.addSheetProperty(textStr, spPdfEmbedded);
            this.addSheetProperty(textStr, spPdfEncoding);
            this.addSheetProperty(textStr, spTextHAlign);
            this.addSheetProperty(textStr, spTextVAlign);
            this.addSheetProperty(textStr, spLineSpacing);
            this.addSheetProperty(textStr, spRotate);
            this.addSheetProperty(textStr, spStyledText);
                      
        }
        
        if (areAllStaticTextElements)
        {
            String staticTextStr = it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.staticText","Static text");
            this.addSheetProperty(staticTextStr, spStaticText);
        }
        
        if (areAllTextfieldElements)
        {
            String textFieldStr = it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.textField","Textfield");
            this.addSheetProperty(textFieldStr, spTextfieldExpression);
            this.addSheetProperty(textFieldStr, spTextfieldExpressionClass);
            this.addSheetProperty(textFieldStr, spTextfieldEvaluationTime);
            this.addSheetProperty(textFieldStr, spTextfieldEvaluationGroup);
            this.addSheetProperty(textFieldStr, spStretchWithOverflow);
            this.addSheetProperty(textFieldStr, spTextfieldBlankWhenNull);
            this.addSheetProperty(textFieldStr, spPattern);
            
            if (!sameTextfieldEvaluationTime || !spTextfieldEvaluationTime.getValue().equals("Group"))
            {
                spTextfieldEvaluationGroup.setReadOnly(true);
            }
            else
            {
                spTextfieldEvaluationGroup.setReadOnly(false);
            }
        }
        
        if (areAllImageElements)
        {
            String imageStr = it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.image","Image");
            this.addSheetProperty(imageStr, spImageExpression);
            this.addSheetProperty(imageStr, spImageExpressionClass);
            this.addSheetProperty(imageStr, spImageEvaluationTime);
            this.addSheetProperty(imageStr, spImageEvaluationGroup);
            this.addSheetProperty(imageStr, spImageScale);
            this.addSheetProperty(imageStr, spImageError);
            this.addSheetProperty(imageStr, spImageVAlign);
            this.addSheetProperty(imageStr, spImageHAlign);
            this.addSheetProperty(imageStr, spImageLazy);
            this.addSheetProperty(imageStr, spImageCache);
            
            if (!sameImageEvaluationTime || !spImageEvaluationTime.getValue().equals("Group"))
            {
                spImageEvaluationGroup.setReadOnly(true);
            }
            else
            {
                spImageEvaluationGroup.setReadOnly(false);
            }
        }
        
        if (areAllBarcodeElements)
        {
            String barcodeStr = it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.barcode","Barcode");
            this.addSheetProperty(barcodeStr, spBCType);
            this.addSheetProperty(barcodeStr, spBCChecksum);
            this.addSheetProperty(barcodeStr, spBCShowText);
            this.addSheetProperty(barcodeStr, spBCExpression);
            this.addSheetProperty(barcodeStr, spBCBarWidth);
            this.addSheetProperty(barcodeStr, spBCBarHeight);
            this.addSheetProperty(barcodeStr, spBCApplicationIdentifier);
            this.addSheetProperty(barcodeStr, spBCScale);
            this.addSheetProperty(barcodeStr, spBCError);
            this.addSheetProperty(barcodeStr, spBCHAlign);
            this.addSheetProperty(barcodeStr, spBCVAlign);
            this.addSheetProperty(barcodeStr, spBCEvaluationTime);
            this.addSheetProperty(barcodeStr, spBCEvaluationGroup);
            
            if (!sameBCEvaluationTime || !spBCEvaluationTime.getValue().equals("Group"))
            {
                spBCEvaluationGroup.setReadOnly(true);
            }
            else
            {
                spBCEvaluationGroup.setReadOnly(false);
            }
        }
        
        if (areAllChartElements)
        {
            String chartStr = it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.chart","Chart");
            this.addSheetProperty(chartStr, spChartEvaluationTime);
            this.addSheetProperty(chartStr, spChartEvaluationGroup);
            
            if (!sameChartEvaluationTime || !spChartEvaluationTime.getValue().equals("Group"))
            {
                spChartEvaluationGroup.setReadOnly(true);
            }
            else
            {
                spChartEvaluationGroup.setReadOnly(false);
            }
        }

        

        for (int i=0; i< this.getProperties().size(); ++i)
        {
            SheetProperty sp = (SheetProperty)this.getProperties().get(i);
            sp.setLabelError(null);
        }

        
        this.recreateSheet();

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            
        }
        
        setInit(false);
        
     }
     
     
    
     
     
     /**
      * This methos is called when a property changes...
      */
     public void sheetPropertyValueChanged(SheetPropertyValueChangedEvent evt)
     {
         if (isInit()) return;
         
         
         
         //System.out.println("Changed: " + evt.getPropertyName());
         if (isNullItem((SheetProperty)evt.getSource())) return;
         
         removeNullItem( (SheetProperty)evt.getSource() );
         
         Vector selectedElements = getElementSelection();
         
         for (int i=0; i<selectedElements.size(); ++i)
         {
             
             ReportElement re = (ReportElement)selectedElements.elementAt(i);
             applyNewProperty(re,evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
         }
         repaintEditor();
         
         ReportElementChangedEvent changedEvent = new ReportElementChangedEvent(jrf , selectedElements , ReportElementChangedEvent.CHANGED);
         changedEvent.setEventSource( this );
         if (evt.getPropertyName().equals("textfieldEvaluationTime"))
         {
            changedEvent.setPropertyChanged( evt.getPropertyName() );
            changedEvent.setNewValue( evt.getNewValue() );
         }
         jrf.fireReportListenerReportElementsChanged(changedEvent);
         MainFrame.getMainInstance().getElementPropertiesDialog().updateSelection();
     }
     
     
     /*
      * This method apply the new value for the specified property
      * The oldValue can be wrong or null if a multiselection was performed
      */
     private void applyNewProperty(ReportElement re, String propertyName, Object oldValue, Object newValue)
     {
         
         
         if (propertyName == null) return;
         if (isInit()) return;
         
         if (propertyName.equals("band"))
         {
            if (newValue != null && newValue instanceof Band) re.setBand((Band)newValue);
            else if (newValue != null && newValue instanceof CrosstabCell) re.setCell((CrosstabCell)newValue);
         }
         else if (propertyName.equals("top"))
         {
            if (newValue != null && !newValue.equals(""))
            {   
                int val = Integer.parseInt(""+newValue);
                int y_location = 0;
                // For each panel, search
                if (re.getBand() != null) {
                    y_location = this.jrf.getReport().getBandYLocation(re.getBand());
                }
            
                if (re.getBand() == null && re.getCell() !=null)
                {
                    y_location = re.getCell().getTop();
                }
                
                if (re.getParentElement() != null)
                {
                    y_location = (int)(re.getParentElement().getPosition().getY())-10;
                }
            
                //System.out.println("Imposto position a: " + (val - (re.getPosition().y - 10 - y_location) ));
                re.trasform( new Point(0, val - (re.getPosition().y - 10 - y_location) ) , TransformationType.TRANSFORMATION_MOVE);
            }
         }
         else if (propertyName.equals("left"))
         {
            if (newValue != null && !newValue.equals("") && !(re instanceof BreakReportElement))
            {   
                int val = Integer.parseInt(""+newValue);
                
                int normalization = 0;
                if (re.getParentElement() != null)
                {
                    normalization = (int)(re.getParentElement().getPosition().getX()) - 10;
                }
                else if (re.getCell() != null)
                {
                    normalization = re.getCell().getLeft();
                }
                else
                {
                    normalization = jrf.getReport().getLeftMargin();
                }
                            
                re.trasform( new Point( val - (re.getPosition().x-10-normalization) ,0 ), TransformationType.TRANSFORMATION_MOVE);
            }
         }
         else if (propertyName.equals("width"))
         {
            if (newValue != null && !newValue.equals("") && !(re instanceof BreakReportElement))
            {   
                int val = Integer.parseInt(""+newValue);
                re.trasform( new Point( val - re.getWidth(),0 ), TransformationType.TRANSFORMATION_RESIZE_E);
            }
         }
         else if (propertyName.equals("height"))
         {
            if (newValue != null && !newValue.equals("") && !(re instanceof BreakReportElement))
            {   
                int val = Integer.parseInt(""+newValue);
                re.trasform( new Point(0, val- re.getHeight() ), TransformationType.TRANSFORMATION_RESIZE_S);
            }
         }
         else if (propertyName.equals("fgcolor"))
         {
            re.setPropertyValue( re.FGCOLOR, ColorSelectorPanel.parseColorString((String)newValue));
         }
         else if (propertyName.equals("bgcolor"))
         {
            re.setPropertyValue( re.BGCOLOR, ColorSelectorPanel.parseColorString((String)newValue));
         }
         else if (propertyName.equals("mode"))
         {
            if (newValue == null ) re.setPropertyValue( re.MODE, null);
            else
            {
                re.setPropertyValue( re.MODE, (newValue+"").equals("true") ? "Transparent" : "Opaque");
            }
         }
         else if (propertyName.equals("printRepeatedValues"))
         {
            if (newValue == null ) re.setPropertyValue( re.PRINT_REPEATED_VALUES, null);
            else
            {
                re.setPropertyValue( re.PRINT_REPEATED_VALUES, newValue+"" );
            }
         }
         else if (propertyName.equals("printWhenDetailOverflows"))
         {
            if (newValue == null ) re.setPropertyValue( re.PRINT_WHEN_DETAIL_OVERFLOW, null);
            else
            {
                re.setPropertyValue( re.PRINT_WHEN_DETAIL_OVERFLOW, newValue+"" );
            }
         }
         else if (propertyName.equals("printInFirstWholeBand"))
         {
            if (newValue == null ) re.setPropertyValue( re.PRINT_IN_FIRST_WHOLE_BAND, null);
            else
            {
                re.setPropertyValue( re.PRINT_IN_FIRST_WHOLE_BAND, newValue+"" );
            }
         }
         else if (propertyName.equals("removeLineWhenBlank"))
         {
            if (newValue == null ) re.setPropertyValue( re.REMOVE_LINE_WHEN_BLANK, null);
            else
            {
                re.setPropertyValue( re.REMOVE_LINE_WHEN_BLANK, newValue+"" );
            }
         }
         else if (propertyName.equals("positionType"))
         {
            if (newValue == null ) re.setPropertyValue( re.POSITION_TYPE, null);
            else
            {
                re.setPropertyValue( re.POSITION_TYPE, newValue+"" );
            }
         }
         else if (propertyName.equals("printWhenGroupChanges"))
         {
            if (newValue != null)
            {
                re.setPrintWhenGroupChanges(""+newValue);
            }
         }
         else if (propertyName.equals("stretchType"))
         {
            if (newValue != null)
            {
                re.setStretchType(""+newValue);
            }
         }
         else if (propertyName.equals("style"))
         {
            if ((newValue+"").equals("")) newValue = null;
            
            if (newValue != null && newValue instanceof String)
            {
                String sname = (""+newValue).trim();
                if (sname.length() == 0) return;
                // Look for a style with that name...
                Enumeration senum = jrf.getReport().getStyles().elements();
                boolean found = false;
                while (senum.hasMoreElements())
                {
                    Style s = (Style)senum.nextElement();
                    if (s.getName().equals(sname))
                    {
                        re.setStyle(s);
                        found = true;
                        break;
                    }
                }
                if (!found)
                {
                    final UndefinedStyle us = new UndefinedStyle();
                    us.setName( sname );
                    final ComboBoxSheetProperty sp =  spStyle;
                    re.setStyle(us);
                    jrf.getReport().addStyle(us);
                    final Vector styles = jrf.getReport().getStyles();
                    SwingUtilities.invokeLater(new Runnable(){
                    
                        public void run()
                        {
                            ((JComboBox)sp.getEditor()).addItem(us);
                        }
                    });
                    
                }
                
            }
            else
            {
                re.setStyle( (Style)newValue );
            }
         }
         else if (propertyName.equals("printWhenExpression"))
         {
            if (newValue != null)
            {
                re.setPrintWhenExpression( ""+newValue );
            }
         }
         else if (propertyName.equals("pen"))
         {
            if ((newValue+"").equals("")) newValue = null;
            re.setPropertyValue( GraphicReportElement.PEN, newValue );
         }
         else if (propertyName.equals("fill"))
         {
            if ((newValue+"").equals("")) newValue = null;
            re.setPropertyValue( GraphicReportElement.FILL, newValue );
         }
         else if (propertyName.equals("radius"))
         {
            if (newValue != null)
            {
                try {
                    
                   newValue = ""+(int)Double.parseDouble(""+ newValue);
                } catch (Exception ex)
                {
                    newValue = null;
                }
            }
            re.setPropertyValue( RectangleReportElement.RADIUS, newValue );
         }
         else if (propertyName.equals("direction"))
         {
            if (newValue != null)
            {
                ((LineReportElement)re).setDirection(""+newValue );
            }
         }
         else if (propertyName.equals("hAlign"))
         {
            re.setPropertyValue( ((TextReportElement)re).ALIGN, newValue );
         }
         else if (propertyName.equals("vAlign"))
         {
           re.setPropertyValue( ((TextReportElement)re).VERTICAL_ALIGN, newValue );
         }
         else if (propertyName.equals("styledText"))
         {
             if (newValue == null) re.setPropertyValue( ((TextReportElement)re).IS_STYLED_TEXT, null );
             else re.setPropertyValue( ((TextReportElement)re).IS_STYLED_TEXT, newValue+"" );
         }
         else if (propertyName.equals("lineSpacing"))
         {
           re.setPropertyValue( ((TextReportElement)re).LINE_SPACING, newValue );
         }
         else if (propertyName.equals("rotate"))
         {
           re.setPropertyValue( ((TextReportElement)re).ROTATE, newValue );
         }
         else if (propertyName.equals("bold"))
         {
             ((TextReportElement)re).getIReportFont().setPropertyValue( IReportFont.IS_BOLD, (newValue == null) ? null : ""+newValue);
         }
         else if (propertyName.equals("italic"))
         {
             ((TextReportElement)re).getIReportFont().setPropertyValue( IReportFont.IS_ITALIC, (newValue == null) ? null : ""+newValue);
         }
         else if (propertyName.equals("underline"))
         {
             ((TextReportElement)re).getIReportFont().setPropertyValue( IReportFont.IS_UNDERLINE, (newValue == null) ? null : ""+newValue);
         }
         else if (propertyName.equals("strikethrough"))
         {
             ((TextReportElement)re).getIReportFont().setPropertyValue( IReportFont.IS_STRIKETROUGHT, (newValue == null) ? null : ""+newValue);
         }
         else if (propertyName.equals("pdfEmbedded"))
         {
             ((TextReportElement)re).getIReportFont().setPropertyValue( IReportFont.IS_PDF_EMBEDDED, (newValue == null) ? null : ""+newValue);
         }
         else if (propertyName.equals("fontSize"))
         {
             try {
                 if (newValue == null || Integer.parseInt(""+newValue) != ((TextReportElement)re).getFontSize())
                 {
                    ((TextReportElement)re).getIReportFont().setPropertyValue( IReportFont.FONT_SIZE, (newValue == null) ? null : ""+newValue);
                 }
             } catch (Exception ex) {}
         }
         else if (propertyName.equals("fontName"))
         {
             ((TextReportElement)re).getIReportFont().setPropertyValue( IReportFont.FONT_NAME, (newValue == null) ? null : ""+newValue);
         }
         else if (propertyName.equals("pdfFontName"))
         {
            ((TextReportElement)re).getIReportFont().setPropertyValue( IReportFont.PDF_FONT_NAME, (newValue == null) ? null : ""+newValue);
         }
         else if (propertyName.equals("pdfEncoding"))
         {
            ((TextReportElement)re).getIReportFont().setPropertyValue( IReportFont.PDF_ENCODING, (newValue == null) ? null : ""+newValue);
         }
         else if (propertyName.equals("text"))
         {
             ((TextReportElement)re).setText( (newValue == null) ? "" : ""+newValue);
         }
         else if (propertyName.equals("textfieldExpression"))
         {
             ((TextReportElement)re).setText( (newValue == null) ? "" : ""+newValue);
         }
         else if (propertyName.equals("textfieldExpressionClass"))
         {
             ((TextFieldReportElement)re).setClassExpression( (newValue == null) ? "" : ""+newValue);
         }
         else if (propertyName.equals("textfieldEvaluationTime"))
         {
             ((TextFieldReportElement)re).setEvaluationTime( (newValue == null) ? "" : ""+newValue);
             if (newValue != null && newValue.equals("Group"))
             {
                spTextfieldEvaluationGroup.setReadOnly(false);
                ((TextFieldReportElement)re).setGroup(spTextfieldEvaluationGroup.getValue() +"");
             }
             else
             {
                 spTextfieldEvaluationGroup.setReadOnly(true);
                 ((TextFieldReportElement)re).setGroup("");
             }
             spTextfieldEvaluationGroup.updateLabel();
         } 
         else if (propertyName.equals("textfieldEvaluationGroup"))
         {
             ((TextFieldReportElement)re).setGroup( (newValue == null) ? "" : ""+newValue);
         }
         else if (propertyName.equals("blankWhenNull"))
         {
             try {
             ((TextFieldReportElement)re).setBlankWhenNull( Boolean.valueOf( newValue+"").booleanValue() );
            } catch (Exception ex) {}
         }
         else if (propertyName.equals("stretchWithOverflow"))
         {
             try {
             ((TextFieldReportElement)re).setStretchWithOverflow( Boolean.valueOf( newValue+"").booleanValue() );
            } catch (Exception ex) {}
         }
         else if (propertyName.equals("pattern"))
         {
             if ((newValue+"").equals("")) newValue = null;
             re.setPropertyValue(TextFieldReportElement.PATTERN, newValue);
         }
         else if (propertyName.equals("imageExpression"))
         {
             ((ImageReportElement)re).setImageExpression( (newValue == null) ? "" : ""+newValue);
         }
         else if (propertyName.equals("imageExpressionClass"))
         {
             ((ImageReportElement)re).setImageClass( (newValue == null) ? "" : ""+newValue);
         }
         else if (propertyName.equals("imageEvaluationTime"))
         {
             ((ImageReportElement)re).setEvaluationTime( (newValue == null) ? "" : ""+newValue);
             if (newValue != null && newValue.equals("Group"))
             {
                spImageEvaluationGroup.setReadOnly(false);
                ((ImageReportElement)re).setEvaluationGroup(spImageEvaluationGroup.getValue() +"");
             }
             else
             {
                 spImageEvaluationGroup.setReadOnly(true);
                 ((ImageReportElement)re).setEvaluationGroup("");
             }
             spImageEvaluationGroup.updateLabel();
         } 
         else if (propertyName.equals("imageEvaluationGroup"))
         {
             ((ImageReportElement)re).setEvaluationGroup( (newValue == null) ? "" : ""+newValue);
         }
         else if (propertyName.equals("imageLazy"))
         {
             try {
             ((ImageReportElement)re).setIsLazy( Boolean.valueOf( newValue+"").booleanValue() );
            } catch (Exception ex) {}
         }
         else if (propertyName.equals("imageCache"))
         {
             re.setPropertyValue(ImageReportElement.USING_CACHE,  newValue);
         }
         else if (propertyName.equals("imageScale"))
         {
            re.setPropertyValue(ImageReportElement.SCALE,  newValue);
         }
         else if (propertyName.equals("imageError"))
         {
             ((ImageReportElement)re).setOnErrorType( ""+ newValue );
         }
         else if (propertyName.equals("imageVAlign"))
         {
            re.setPropertyValue(ImageReportElement.VERTICAL_ALIGN,  newValue);
         }
         else if (propertyName.equals("imageHAlign"))
         {
            re.setPropertyValue(ImageReportElement.HORIZONTAL_ALIGN,  newValue);
         }
         else if (propertyName.equals("barcodeType"))
         {
            ((BarcodeReportElement)re).setType( Integer.parseInt( (newValue == null) ? "13" : ""+newValue));
            ((BarcodeReportElement)re).update();
            
         }
         else if (propertyName.equals("barcodeBarWidth"))
         {
            ((BarcodeReportElement)re).setImageWidth( Integer.parseInt( (newValue == null) ? "1" : ""+newValue));
            
         }
         else if (propertyName.equals("barcodeBarHeight"))
         {
            ((BarcodeReportElement)re).setImageHeight( Integer.parseInt( (newValue == null) ? "1" : ""+newValue));
            
         }
         else if (propertyName.equals("barcodeExpression"))
         {
             ((BarcodeReportElement)re).setText( (newValue == null) ? "" : ""+newValue);
         }
         else if (propertyName.equals("barcodeAppIdentifierExpression"))
         {
             ((BarcodeReportElement)re).setApplicationIdentifier( (newValue == null) ? "" : ""+newValue);
         }
         else if (propertyName.equals("barcodeEvaluationTime"))
         {
             ((ImageReportElement)re).setEvaluationTime( (newValue == null) ? "" : ""+newValue);
             if (newValue != null && newValue.equals("Group"))
             {
                spBCEvaluationGroup.setReadOnly(false);
                ((ImageReportElement)re).setEvaluationGroup(spBCEvaluationGroup.getValue() +"");
             }
             else
             {
                 spBCEvaluationGroup.setReadOnly(true);
                 ((ImageReportElement)re).setEvaluationGroup("");
             }
             spBCEvaluationGroup.updateLabel();
         } 
         else if (propertyName.equals("barcodeEvaluationGroup"))
         {
             ((ImageReportElement)re).setEvaluationGroup( (newValue == null) ? "" : ""+newValue);
         }
         else if (propertyName.equals("barcodeShowText"))
         {
             try {
             ((BarcodeReportElement)re).setShowText( Boolean.valueOf( newValue+"").booleanValue() );
            } catch (Exception ex) {}
         }
         else if (propertyName.equals("barcodeChecksum"))
         {
             try {
             ((BarcodeReportElement)re).setCheckSum( Boolean.valueOf( newValue+"").booleanValue() );
            } catch (Exception ex) {}
         }
         else if (propertyName.equals("barcodeScale"))
         {
            re.setPropertyValue(ImageReportElement.SCALE,  newValue);
         }
         else if (propertyName.equals("barcodeError"))
         {
             ((ImageReportElement)re).setOnErrorType( ""+ newValue );
         }
         else if (propertyName.equals("barcodeVAlign"))
         {
            re.setPropertyValue(ImageReportElement.VERTICAL_ALIGN,  newValue);
         }
         else if (propertyName.equals("barcodeHAlign"))
         {
            re.setPropertyValue(ImageReportElement.HORIZONTAL_ALIGN,  newValue);
         }
         else if (propertyName.equals("chartEvaluationTime"))
         {
             ((ChartReportElement2)re).setEvaluationTime( (newValue == null) ? "" : ""+newValue);
             if (newValue != null && newValue.equals("Group"))
             {
                spChartEvaluationGroup.setReadOnly(false);
                ((ChartReportElement2)re).setEvaluationGroup(spChartEvaluationGroup.getValue() +"");
             }
             else
             {
                 spChartEvaluationGroup.setReadOnly(true);
                 ((ChartReportElement2)re).setEvaluationGroup("");
             }
             spChartEvaluationGroup.updateLabel();
         } 
         else if (propertyName.equals("chartEvaluationGroup"))
         {
             ((ChartReportElement2)re).setEvaluationGroup( (newValue == null) ? "" : ""+newValue);
         }
     }
     
     /**
      *     This method is called when a new element is selected,
      *     or deselected.
      */
     
     public void reportElementsSelectionChanged(ReportElementsSelectionEvent evt){

            updateSelection();

     }

   
     /*
      *     This method is called when an element is removed, changed or added.
      */
      public void reportElementsChanged(ReportElementChangedEvent evt){
        
          if (evt.getEventSource() == null || evt.getEventSource() != this)
          {
              if (evt.getType() == evt.CHANGED && evt.getPropertyChanged() != null &&
                  !evt.getPropertyChanged().equals("textfieldEvaluationTime"))
              {
                    setPropertyValue(evt.getPropertyChanged(), evt.getNewValue());
              }
              else
              {
                  updateSelection();
              }
          }
      }
      
     /*
      *     This method is called when a band is removed, changed or added.
      */ 
     public void reportBandChanged(ReportBandChangedEvent evt ) {
         updateSelection();
     }
     
     protected void initSheetProperties()
     {
        spBands = new ComboBoxSheetProperty("band",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.band","Band"));
        spBands.setShowResetButton(false);
        
        spLeft =  new SheetProperty("left",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.left","Left"), SheetProperty.INTEGER);
        spLeft.setShowResetButton(false);
        
        spTop =  new SheetProperty("top",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.top","Top"), SheetProperty.INTEGER);
        spTop.setShowResetButton(false);
        
        spWidth =  new SheetProperty("width",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.width","Width"), SheetProperty.INTEGER);
        spWidth.setShowResetButton(false);
        
        spHeight =  new SheetProperty("height",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.height","Height"), SheetProperty.INTEGER);
        spHeight.setShowResetButton(false);
        
        spFgColor =  new SheetProperty("fgcolor",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.fgcolor","Foreground"), SheetProperty.COLOR);
        spBgColor =  new SheetProperty("bgcolor",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.bgcolor","Background"), SheetProperty.COLOR);
        spMode =  new SheetProperty("mode",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.mode","Transparent"), SheetProperty.BOOLEAN);
        spPrintRepeatedValues =  new SheetProperty("printRepeatedValues",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.printRepeatedValues","Print repeated values"), SheetProperty.BOOLEAN);
        spPrintRepeatedValues.setShowResetButton(false);
        spPrintWhenDetailOverflows =  new SheetProperty("printWhenDetailOverflows",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.printWhenDetailOverflows","Print when detail overflows"), SheetProperty.BOOLEAN);
        spPrintWhenDetailOverflows.setShowResetButton(false);
        spPrintInFirstWholeBand =  new SheetProperty("printInFirstWholeBand",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.printInFirstWholeBand","Print in first whole band"), SheetProperty.BOOLEAN);
        spPrintInFirstWholeBand.setShowResetButton(false);
        spRemoveLineWhenBlank =  new SheetProperty("removeLineWhenBlank",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.removeLineWhenBlank","Remove line when blank"), SheetProperty.BOOLEAN);
        spRemoveLineWhenBlank.setShowResetButton(false);
        
        
        spPositionType = new SheetProperty("positionType",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.positionType","Position type"), SheetProperty.COMBOBOX);
        spPositionType.setTags( new Tag[]{ new Tag("FixRelativeToTop",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.positionType.FixRelativeToTop","Fix relative to top")),
                                           new Tag("Float",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.positionType.Float","Float")),
                                           new Tag("FixRelativeToBottom",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.positionType.FixRelativeToBottom","Fix relative to bottom"))});
        spPositionType.setDefaultValue("FixRelativeToTop");
        spPositionType.setShowResetButton(false);
                                           
                                           
        spGroups = new ComboBoxSheetProperty("printWhenGroupChanges",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.printWhenGroupChanges","Print when group changes"));
        spGroups.setShowResetButton(false);
        spElementKey = new SheetProperty("elementKey",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.elementKey","Element key"), SheetProperty.STRING);
        spElementKey.setShowResetButton(false);
        
        spStretchType = new SheetProperty("stretchType",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.stretchType","Stretch type"), SheetProperty.COMBOBOX);
        spStretchType.setTags( new Tag[]{ new Tag("NoStretch",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.stretchType.NoStretch","Fix relative to top")),
                                           new Tag("RelativeToTallestObject",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.stretchType.RelativeToTallestObject","Float")),
                                           new Tag("RelativeToBandHeight",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.stretchType.RelativeToBandHeight","Fix relative to bottom"))});
        spStretchType.setDefaultValue("NoStretch");
        spStretchType.setShowResetButton(false);
        
        spStyle = new ComboBoxSheetProperty("style",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.style","Style"));
        spStyle.setShowResetButton(false);
        ((JComboBox)spStyle.getEditor()).setEditable(true);
        spStyle.setDefaultValue("");
        
        spPrintWhenExpression = new ExpressionSheetProperty("printWhenExpression",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.printWhenExpression","Print When Expression"));
        spPrintWhenExpression.setShowResetButton(false);

        spPen = new SheetProperty("pen",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.pen","Pen"), SheetProperty.COMBOBOX);
        spPen.setTags( new Tag[]{ new Tag("",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.default","Default")),
                                          new Tag("None",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.pen.None","None")),
                                          new Tag("Thin",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.pen.Thin","Thin")),
                                          new Tag("1Point",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.pen.1Point","1Point")),
                                          new Tag("2Point",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.pen.2Point","2Point")),
                                          new Tag("4Point",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.pen.4Point","4Point")),
                                          new Tag("Dotted",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.pen.Dotted","Dotted"))});
        spPen.setDefaultValue("");
        
        spFill = new SheetProperty("fill",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.fill","Fill"), SheetProperty.COMBOBOX);
        spFill.setTags( new Tag[]{ new Tag("",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.default","Default")),
                                          new Tag("Solid",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.fill.Solid","Solid"))});
        spFill.setDefaultValue("");
        
        spRadius = new SheetProperty("radius",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.radius","Radius"), SheetProperty.NUMBER);
        try {
        ((JNumberField)(spRadius.getEditor())).setDecimals(0);
        } catch (Exception ex) {};
        
        spDirection = new SheetProperty("direction",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.direction","Direction"), SheetProperty.COMBOBOX);
        spDirection.setTags( new Tag[]{ new Tag("TopDown",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.direction.TopDown","Top-down (\\)")),
                                          new Tag("BottomUp",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.direction.BottomUp","Bottom-up (/)"))});
        spDirection.setShowResetButton(false);
        
        
        spBreakType = new SheetProperty("breakType",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.breakType","Type"), SheetProperty.COMBOBOX);
        spBreakType.setTags( new Tag[]{ new Tag("Page",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.breakType.page","Page")),
                                          new Tag("Column",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.breakType.column","Column"))});
        spBreakType.setShowResetButton(false);
        
        spTextHAlign = new SheetProperty("hAlign",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.halign","Align"), SheetProperty.COMBOBOX);
        spTextHAlign.setTags( new Tag[]{  new Tag("Left",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.halign.Left","Left")),
                                          new Tag("Center",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.halign.Center","Center")),
                                          new Tag("Right",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.halign.Right","Right")),
                                          new Tag("Justified",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.halign.Justified","Justified"))});
        spTextHAlign.setDefaultValue("Left");
        
        spTextVAlign = new SheetProperty("vAlign",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.valign","Vertical align"), SheetProperty.COMBOBOX);
        spTextVAlign.setTags( new Tag[]{  new Tag("Top",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.valign.Top","Top")),
                                          new Tag("Middle",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.valign.Middle","Middle")),
                                          new Tag("Bottom",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.valign.Bottom","Bottom"))});
        spTextVAlign.setDefaultValue("Top");
        
        spStyledText =  new SheetProperty("styledText",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.styledText","Styled text"), SheetProperty.BOOLEAN);
        
        spLineSpacing = new SheetProperty("lineSpacing",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.lineSpacing","Line spacing"), SheetProperty.COMBOBOX);
        spLineSpacing.setTags( new Tag[]{ new Tag("Single",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.lineSpacing.Single","Single")),
                                          new Tag("1_1_2",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.lineSpacing.1_1_2","1_1_2")),
                                          new Tag("Double",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.lineSpacing.Double","Double"))});
        spLineSpacing.setDefaultValue("Single");
                                          
        spRotate = new SheetProperty("rotate",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.rotate","Ratation"), SheetProperty.COMBOBOX);
        spRotate.setTags( new Tag[]{ new Tag("None",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.rotate.None","None")),
                                          new Tag("Left",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.rotate.Left","Left")),
                                          new Tag("Right",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.rotate.Right","Right")),
                                          new Tag("UpsideDown",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.rotate.UpsideDown","Upside down")) });
        spRotate.setDefaultValue("None");
        
        spFontName = new SheetProperty("fontName",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.fontName","Font"), SheetProperty.COMBOBOX);
        Vector fontsVec = new Vector();
        String[] fontFamilies = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        for (int i=0; i<fontFamilies.length; ++i) {
            fontsVec.add( new Tag(fontFamilies[i]));
        }
        spFontName.setTags( fontsVec );
        spFontName.setDefaultValue("SansSerif");
        
        
        spFontSize =  new NumberComboBoxSheetProperty("fontSize",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.fontSize","Font size"));
        spFontSize.addEntry("3",3);
        spFontSize.addEntry("5",5);
        spFontSize.addEntry("8",8);
        spFontSize.addEntry("10",10);
        spFontSize.addEntry("12",12);
        spFontSize.addEntry("14",14);
        spFontSize.addEntry("18",18);
        spFontSize.addEntry("24",24);
        spFontSize.addEntry("36",36);
        spFontSize.addEntry("48",48);

        //spPdfFontName;
        spPdfFontName = new SheetProperty("pdfFontName",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.pdfFontName","PDF Font"), SheetProperty.COMBOBOX);
        fontsVec = new Vector();
        fontsVec.addElement(new Tag("Helvetica"));
        fontsVec.addElement(new Tag("Helvetica-Bold"));
        fontsVec.addElement(new Tag("Helvetica-BoldOblique"));
        fontsVec.addElement(new Tag("Helvetica-Oblique"));
        fontsVec.addElement(new Tag("Courier"));
        fontsVec.addElement(new Tag("Courier-Bold"));
        fontsVec.addElement(new Tag("Courier-BoldOblique"));
        fontsVec.addElement(new Tag("Courier-Oblique"));
        fontsVec.addElement(new Tag("Symbol"));
        fontsVec.addElement(new Tag("Times-Roman"));
        fontsVec.addElement(new Tag("Times-Bold"));
        fontsVec.addElement(new Tag("Times-BoldItalic"));
        fontsVec.addElement(new Tag("Times-Italic"));
        fontsVec.addElement(new Tag("ZapfDingbats"));
        fontsVec.addElement(new Tag("STSong-Light"));
        fontsVec.addElement(new Tag("MHei-Medium"));
        fontsVec.addElement(new Tag("MSung-Light"));
        fontsVec.addElement(new Tag("HeiseiKakuGo-W5"));
        fontsVec.addElement(new Tag("HeiseiMin-W3"));
        fontsVec.addElement(new Tag("HYGoThic-Medium"));
        fontsVec.addElement(new Tag("HYSMyeongJo-Medium"));
        Vector iRfonts = MainFrame.getMainInstance().getTtfFonts();
        for (int i_f=0; i_f<iRfonts.size(); ++i_f)
        {
            fontsVec.addElement(new Tag( ((IRFont)iRfonts.elementAt(i_f)).getFile(), 
                                   iRfonts.elementAt(i_f)+""));
        }
        
        spPdfFontName.setTags( fontsVec );
        spPdfFontName.setDefaultValue("Helvetica");     
        ((JComboBox)(spPdfFontName.getEditor())).setEditable(true);
        
        
        spBold =  new SheetProperty("bold",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.Bold","Bold"), SheetProperty.BOOLEAN);
        spItalic =  new SheetProperty("italic",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.Italic","Italic"), SheetProperty.BOOLEAN);
        spUnderline =  new SheetProperty("underline",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.Underline","Underline"), SheetProperty.BOOLEAN);
        spStriketrough =  new SheetProperty("strikethrough",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.Strikethrough","Strike through"), SheetProperty.BOOLEAN);
        spPdfEmbedded =  new SheetProperty("pdfEmbedded",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.PdfEmbedded","PDF embedded"), SheetProperty.BOOLEAN);
        //spPdfEncoding;
    
        
        spPdfEncoding = new SheetProperty("pdfEncoding",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.pdfEncoding","PDF Encoding"), SheetProperty.COMBOBOX);
        fontsVec = new Vector();
        fontsVec.addElement(new Tag("Cp1250","CP1250 (Central European)"));
        fontsVec.addElement(new Tag("Cp1251","CP1251 (Cyrillic)"));
        fontsVec.addElement(new Tag("Cp1252","CP1252 (Western European ANSI aka WinAnsi)"));
        fontsVec.addElement(new Tag("Cp1253","CP1253 (Greek)"));
        fontsVec.addElement(new Tag("Cp1254","CP1254 (Turkish)"));
        fontsVec.addElement(new Tag("Cp1255","CP1255 (Hebrew)"));
        fontsVec.addElement(new Tag("Cp1256","CP1256 (Arabic)"));
        fontsVec.addElement(new Tag("Cp1257","CP1257 (Baltic)"));
        fontsVec.addElement(new Tag("Cp1258","CP1258 (Vietnamese)"));
        fontsVec.addElement(new Tag("UniGB-UCS2-H","UniGB-UCS2-H (Chinese Simplified)"));
        fontsVec.addElement(new Tag("UniGB-UCS2-V","UniGB-UCS2-V (Chinese Simplified)"));
        fontsVec.addElement(new Tag("UniCNS-UCS2-H","UniCNS-UCS2-H (Chinese traditional)"));
        fontsVec.addElement(new Tag("UniCNS-UCS2-V","UniCNS-UCS2-V (Chinese traditional)"));
        fontsVec.addElement(new Tag("UniJIS-UCS2-H","UniJIS-UCS2-H (Japanese)"));
        fontsVec.addElement(new Tag("UniJIS-UCS2-V","UniJIS-UCS2-V (Japanese)"));
        fontsVec.addElement(new Tag("UniJIS-UCS2-HW-H","UniJIS-UCS2-HW-H (Japanese)"));
        fontsVec.addElement(new Tag("UniJIS-UCS2-HW-V","UniJIS-UCS2-HW-V (Japanese)"));
        fontsVec.addElement(new Tag("UniKS-UCS2-H","UniKS-UCS2-H (Korean)"));
        fontsVec.addElement(new Tag("UniKS-UCS2-V","UniKS-UCS2-V (Korean)"));
        fontsVec.addElement(new Tag("Identity-H","Identity-H (Unicode with horizontal writing)"));
        fontsVec.addElement(new Tag("Identity-V","Identity-V (Unicode with vertical writing)"));

        spPdfEncoding.setTags( fontsVec );
        ((JComboBox)spPdfEncoding.getEditor()).setEditable(true);
        spPdfEncoding.setDefaultValue("Cp1250");     
        
        spStaticText = new ExpressionSheetProperty("text",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.text","Text"));
        spStaticText.setPlainTextEditor(true);
        spStaticText.setShowResetButton(false);
        spTextfieldExpression = new ExpressionSheetProperty("textfieldExpression",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.textfieldExpression","Expression"));
        spTextfieldExpression.setShowResetButton(false);
        spTextfieldExpressionClass= new SheetProperty("textfieldExpressionClass",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.textfieldExpressionClass","Exp. class"), SheetProperty.COMBOBOX);
        fontsVec = new Vector();
        fontsVec.addElement(new Tag("java.lang.Boolean"));
        fontsVec.addElement(new Tag("java.lang.Byte"));
        fontsVec.addElement(new Tag("java.util.Date"));
        fontsVec.addElement(new Tag("java.sql.Timestamp"));
        fontsVec.addElement(new Tag("java.sql.Time"));
        fontsVec.addElement(new Tag("java.lang.Double"));
        fontsVec.addElement(new Tag("java.lang.Float"));
        fontsVec.addElement(new Tag("java.lang.Integer"));
        fontsVec.addElement(new Tag("java.lang.Long"));
        fontsVec.addElement(new Tag("java.lang.Short"));
        fontsVec.addElement(new Tag("java.math.BigDecimal"));
        fontsVec.addElement(new Tag("java.lang.String"));
        fontsVec.addElement(new Tag("java.lang.Number"));
        spTextfieldExpressionClass.setTags(fontsVec);
        spTextfieldExpressionClass.setDefaultValue("java.lang.String");
        spTextfieldExpressionClass.setShowResetButton(false);
        
        spTextfieldEvaluationTime = new SheetProperty("textfieldEvaluationTime",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime","Eval. time"), SheetProperty.COMBOBOX);
        // Text field Evaluation Time...
        fontsVec = new Vector();
        fontsVec.addElement(new Tag("Now",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Now","Now")));
        fontsVec.addElement(new Tag("Report",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Report","Report")));
        fontsVec.addElement(new Tag("Page",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Page","Page")));
        fontsVec.addElement(new Tag("Column",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Column","Column")));
        fontsVec.addElement(new Tag("Group",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Group","Group")));
        fontsVec.addElement(new Tag("Band",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Band","Bans")));
        fontsVec.addElement(new Tag("Auto",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Auto","Auto")));
        spTextfieldEvaluationTime.setTags(fontsVec);
        spTextfieldEvaluationTime.setDefaultValue("Now");
        spTextfieldEvaluationTime.setShowResetButton(false);
        
        spTextfieldEvaluationGroup = new ComboBoxSheetProperty("textfieldEvaluationGroup",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.textfieldEvaluationGroup","Eval. group"));
        spTextfieldEvaluationGroup.setShowResetButton(false);
        
        spStretchWithOverflow = new SheetProperty("stretchWithOverflow",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.stretchWithOverflow","Stretch with overflow"), SheetProperty.BOOLEAN);
        spStretchWithOverflow.setShowResetButton(false);
        spTextfieldBlankWhenNull = new SheetProperty("blankWhenNull",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.blankWehnNull","Blank when null"), SheetProperty.BOOLEAN);
        spTextfieldBlankWhenNull.setShowResetButton(false);
        spPattern = new PatternSheetProperty("pattern",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.Pattern","Pattern"));
        spPattern.setDefaultValue("");
        
        spImageExpression = new ExpressionSheetProperty("imageExpression",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.imageExpression","Image expression"));
        spImageExpression.setShowResetButton(false);
        spImageExpressionClass= new SheetProperty("imageExpressionClass",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.textfieldExpressionClass","Exp. class"), SheetProperty.COMBOBOX);
        fontsVec = new Vector();
        fontsVec.addElement(new Tag("java.lang.String"));
        fontsVec.addElement(new Tag("java.io.File"));
        fontsVec.addElement(new Tag("java.net.URL"));
        fontsVec.addElement(new Tag("java.io.InputStream"));
        fontsVec.addElement(new Tag("java.awt.Image"));
        fontsVec.addElement(new Tag("net.sf.jasperreports.engine.JRRenderable"));
        spImageExpressionClass.setTags(fontsVec);
        //spImageExpressionClass.setDefaultValue("java.lang.String");
        spImageExpressionClass.setShowResetButton(false);
        
        spImageEvaluationTime = new SheetProperty("imageEvaluationTime",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime","Eval. time"), SheetProperty.COMBOBOX);
        // Text field Evaluation Time...
        fontsVec = new Vector();
        fontsVec.addElement(new Tag("Now",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Now","Now")));
        fontsVec.addElement(new Tag("Report",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Report","Report")));
        fontsVec.addElement(new Tag("Page",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Page","Page")));
        fontsVec.addElement(new Tag("Column",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Column","Column")));
        fontsVec.addElement(new Tag("Group",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Group","Group")));
        fontsVec.addElement(new Tag("Band",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Band","Bans")));
        fontsVec.addElement(new Tag("Auto",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Auto","Auto")));
        spImageEvaluationTime.setTags(fontsVec);
        spImageEvaluationTime.setDefaultValue("Now");
        spImageEvaluationTime.setShowResetButton(false);
        
        spImageEvaluationGroup = new ComboBoxSheetProperty("imageEvaluationGroup",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.textfieldEvaluationGroup","Eval. group"));
        spImageEvaluationGroup.setShowResetButton(false);
        spImageScale = new SheetProperty("imageScale",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.imageScale","Image scale"), SheetProperty.COMBOBOX);
        fontsVec = new Vector();
        fontsVec.addElement(new Tag("Clip",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.imageScale.Clip","Clip")));
        fontsVec.addElement(new Tag("FillFrame",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.imageScale.FillFrame","Fill frame")));
        fontsVec.addElement(new Tag("RetainShape",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.imageScale.RetainShape","Retain shape")));
        spImageScale.setTags(fontsVec);
        spImageScale.setDefaultValue("FillFrame");
        
        spImageError = new SheetProperty("imageError",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.imageError","On error type"), SheetProperty.COMBOBOX);
        fontsVec = new Vector();
        fontsVec.addElement(new Tag("Error",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.imageScale.Error","Error")));
        fontsVec.addElement(new Tag("Blank",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.imageScale.Blank","Blank")));
        fontsVec.addElement(new Tag("Icon",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.imageScale.Icon","Icon")));
        spImageError.setTags(fontsVec);
        spImageError.setDefaultValue("Error");
        spImageError.setShowResetButton(false);
        
        spImageHAlign = new SheetProperty("imageHAlign",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.halign","Align"), SheetProperty.COMBOBOX);
        spImageHAlign.setTags( new Tag[]{  new Tag("Left",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.halign.Left","Left")),
                                          new Tag("Center",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.halign.Center","Center")),
                                          new Tag("Right",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.halign.Right","Right"))});
        spImageHAlign.setDefaultValue("Left");
        
        spImageVAlign = new SheetProperty("imageVAlign",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.valign","Vertical align"), SheetProperty.COMBOBOX);
        spImageVAlign.setTags( new Tag[]{  new Tag("Top",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.valign.Top","Top")),
                                          new Tag("Middle",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.valign.Middle","Middle")),
                                          new Tag("Bottom",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.valign.Bottom","Bottom"))});
        spImageVAlign.setDefaultValue("Top");
        
        spImageLazy = new SheetProperty("imageLazy",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.imageLazy","Is lazy"), SheetProperty.BOOLEAN);
        spImageLazy.setShowResetButton(false);
        
        spImageCache = new SheetProperty("imageCache",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.imageCache","Using cache"), SheetProperty.BOOLEAN);
        
        //spBarcodeGroup = new ComboBoxSheetProperty("barcode_group","Evaluation group");
        
        spBCExpression = new ExpressionSheetProperty("barcodeExpression",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.barcodeExpression","Barcode expression"));
        spBCExpression.setShowResetButton(false);
        
        spBCApplicationIdentifier = new ExpressionSheetProperty("barcodeAppIdentifierExpression",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.barcodeApplicationIdentifier","Applic. identifier"));
        spBCApplicationIdentifier.setShowResetButton(false);
        
        spBCBarWidth = new SheetProperty("barcodeBarWidth",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.barcodeBarWidth","Bar width"), SheetProperty.INTEGER);
        spBCBarWidth.setShowResetButton(false);
        
        spBCBarHeight = new SheetProperty("barcodeBarHeight",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.barcodeBarHeight","Bar height"), SheetProperty.INTEGER);
        spBCBarHeight.setShowResetButton(false);
        
        spBCType= new SheetProperty("barcodeType",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.barcodeType","Type"), SheetProperty.COMBOBOX);
        fontsVec = new Vector();
        fontsVec.add(new Tag("1","2of7"));
        fontsVec.add(new Tag("2","3of9"));
        fontsVec.add(new Tag("3","Bookland"));
        fontsVec.add(new Tag("4","Codabar"));
        fontsVec.add(new Tag("5","Code128"));
        fontsVec.add(new Tag("6","Code128A"));
        fontsVec.add(new Tag("7","Code128B"));
        fontsVec.add(new Tag("8","Code128C"));
        fontsVec.add(new Tag("9","Code39"));
        fontsVec.add(new Tag("26","Code39 (Extended)"));
        fontsVec.add(new Tag("10","EAN128"));
        fontsVec.add(new Tag("11","EAN13"));
        fontsVec.add(new Tag("12","GlobalTradeItemNumber"));
        fontsVec.add(new Tag("13","Int2of5"));
        fontsVec.add(new Tag("14","Monarch"));
        fontsVec.add(new Tag("15","NW7"));
        fontsVec.add(new Tag("16","PDF417"));
        fontsVec.add(new Tag("17","SCC14ShippingCode"));
        fontsVec.add(new Tag("18","ShipmentIdentificationNumber"));
        fontsVec.add(new Tag("19","SSCC18"));
        fontsVec.add(new Tag("20","Std2of5"));
        fontsVec.add(new Tag("21","UCC128"));
        fontsVec.add(new Tag("22","UPCA"));
        fontsVec.add(new Tag("23","USD3"));
        fontsVec.add(new Tag("24","USD4"));
        fontsVec.add(new Tag("25","USPS"));
        spBCType.setTags(fontsVec);
        //spImageExpressionClass.setDefaultValue("java.lang.String");
        spBCType.setShowResetButton(false);
        
        spBCEvaluationTime = new SheetProperty("barcodeEvaluationTime",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime","Eval. time"), SheetProperty.COMBOBOX);
        // Text field Evaluation Time...
        fontsVec = new Vector();
        fontsVec.addElement(new Tag("Now",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Now","Now")));
        fontsVec.addElement(new Tag("Report",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Report","Report")));
        fontsVec.addElement(new Tag("Page",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Page","Page")));
        fontsVec.addElement(new Tag("Column",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Column","Column")));
        fontsVec.addElement(new Tag("Group",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Group","Group")));
        fontsVec.addElement(new Tag("Band",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Band","Bans")));
        fontsVec.addElement(new Tag("Auto",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Auto","Auto")));
        spBCEvaluationTime.setTags(fontsVec);
        spBCEvaluationTime.setDefaultValue("Now");
        spBCEvaluationTime.setShowResetButton(false);
        
        spBCEvaluationGroup = new ComboBoxSheetProperty("barcodeEvaluationGroup",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.textfieldEvaluationGroup","Eval. group"));
        spBCEvaluationGroup.setShowResetButton(false);
        spBCScale = new SheetProperty("barcodeScale",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.imageScale","Image scale"), SheetProperty.COMBOBOX);
        fontsVec = new Vector();
        fontsVec.addElement(new Tag("Clip",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.imageScale.Clip","Clip")));
        fontsVec.addElement(new Tag("FillFrame",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.imageScale.FillFrame","Fill frame")));
        fontsVec.addElement(new Tag("RetainShape",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.imageScale.RetainShape","Retain shape")));
        spBCScale.setTags(fontsVec);
        spBCScale.setDefaultValue("FillFrame");
        
        spBCError = new SheetProperty("barcodeError",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.imageError","On error type"), SheetProperty.COMBOBOX);
        fontsVec = new Vector();
        fontsVec.addElement(new Tag("Error",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.imageScale.Error","Error")));
        fontsVec.addElement(new Tag("Blank",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.imageScale.Blank","Blank")));
        fontsVec.addElement(new Tag("Icon",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.imageScale.Icon","Icon")));
        spBCError.setTags(fontsVec);
        spBCError.setDefaultValue("Error");
        spBCError.setShowResetButton(false);
        
        spBCHAlign = new SheetProperty("barcodeHAlign",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.halign","Align"), SheetProperty.COMBOBOX);
        spBCHAlign.setTags( new Tag[]{  new Tag("Left",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.halign.Left","Left")),
                                          new Tag("Center",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.halign.Center","Center")),
                                          new Tag("Right",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.halign.Right","Right"))});
        spBCHAlign.setDefaultValue("Left");
        
        spBCVAlign = new SheetProperty("barcodeVAlign",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.valign","Vertical align"), SheetProperty.COMBOBOX);
        spBCVAlign.setTags( new Tag[]{  new Tag("Top",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.valign.Top","Top")),
                                          new Tag("Middle",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.valign.Middle","Middle")),
                                          new Tag("Bottom",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.valign.Bottom","Bottom"))});
        spBCVAlign.setDefaultValue("Top");
        
        spBCChecksum = new SheetProperty("barcodeChecksum",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.barcodeChecksum","Checksum"), SheetProperty.BOOLEAN);
        spBCChecksum.setShowResetButton(false);
        
        spBCShowText = new SheetProperty("barcodeShowText",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.barcodeShowText","Show Text"), SheetProperty.BOOLEAN);
        spBCShowText.setShowResetButton(false);
        
        
        spChartEvaluationTime = new SheetProperty("chartEvaluationTime",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime","Eval. time"), SheetProperty.COMBOBOX);
        // Text field Evaluation Time...
        fontsVec = new Vector();
        fontsVec.addElement(new Tag("Now",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Now","Now")));
        fontsVec.addElement(new Tag("Report",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Report","Report")));
        fontsVec.addElement(new Tag("Page",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Page","Page")));
        fontsVec.addElement(new Tag("Column",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Column","Column")));
        fontsVec.addElement(new Tag("Group",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Group","Group")));
        fontsVec.addElement(new Tag("Band",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Band","Bans")));
        fontsVec.addElement(new Tag("Auto",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Auto","Auto")));
        spChartEvaluationTime.setTags(fontsVec);
        spChartEvaluationTime.setDefaultValue("Now");
        spChartEvaluationTime.setShowResetButton(false);
        
        spChartEvaluationGroup = new ComboBoxSheetProperty("chartEvaluationGroup",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.textfieldEvaluationGroup","Eval. group"));
        spChartEvaluationGroup.setShowResetButton(false);
     }
     
     /**
      * Update groups
      */
     protected void updateAllComboBoxes()
     {
         if (jrf == null) return;
         // Use the name of the group and not the group object....
         Vector group_names = new Vector();
         Enumeration e = jrf.getReport().getGroups().elements();
         while (e.hasMoreElements()) {
            group_names.addElement(""+e.nextElement());
         }
         
         spTextfieldEvaluationGroup.updateValues( group_names,false);
         spImageEvaluationGroup.updateValues( group_names,false);
         spBCEvaluationGroup.updateValues( group_names,false);
         spChartEvaluationGroup.updateValues( group_names,false);
         
         group_names.insertElementAt("",0);
         spGroups.updateValues( group_names,false);
         
         
         Vector styles = new Vector();
         e = jrf.getReport().getStyles().elements();
         while (e.hasMoreElements()) {
            styles.addElement(e.nextElement());
         }
         styles.insertElementAt("",0);
         spStyle.updateValues( styles , true);
         
         updateBandsCell();
         
     }
     
     
     protected void updateBandsCell()
     {
         // Put all the bands in a tag...
         this.setInit(true);
         
         if (jrf.getSelectedCrosstabEditorPanel() == null)
         {
           spBands.setName(it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.band","Band"));
           Misc.updateComboBox( (JComboBox)spBands.getEditor(), jrf.getReport().getBands(), false);
         }
         else
         {
           spBands.setName(it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.cell","Cell"));
           Misc.updateComboBox( (JComboBox)spBands.getEditor(), jrf.getSelectedCrosstabEditorPanel().getCrosstabElement().getCells(), false);
         }
         
         this.setInit(false);
    }

    public boolean isInit() {
        return init;
    }

    public void setInit(boolean init) {
        this.init = init;
    }
    
   

    public ComboBoxSheetProperty getSpBands() {
        return spBands;
    }

    public void setSpBands(ComboBoxSheetProperty spBands) {
        this.spBands = spBands;
    }

    public SheetProperty getSpTop() {
        return spTop;
    }

    public void setSpTop(SheetProperty spTop) {
        this.spTop = spTop;
    }

    public SheetProperty getSpLeft() {
        return spLeft;
    }

    public void setSpLeft(SheetProperty spLeft) {
        this.spLeft = spLeft;
    }

    public SheetProperty getSpHeight() {
        return spHeight;
    }

    public void setSpHeight(SheetProperty spHeight) {
        this.spHeight = spHeight;
    }

    public SheetProperty getSpWidth() {
        return spWidth;
    }

    public void setSpWidth(SheetProperty spWidth) {
        this.spWidth = spWidth;
    }

    public ComboBoxSheetProperty getSpGroups() {
        return spGroups;
    }

    public void setSpGroups(ComboBoxSheetProperty spGroups) {
        this.spGroups = spGroups;
    }


    public JReportFrame getJrf() {
        return jrf;
    }

    public void setJrf(JReportFrame jrf) {
        this.jrf = jrf;
    }
    
    
    /**
     * Redraw the correct editor panel (JReportPanel or the active CrosstabPanel)
     *
     */
    public void repaintEditor()
    {
        if (jrf == null) return;
        if (jrf.getSelectedCrosstabEditorPanel() == null)
        {
            jrf.getJPanelReport().repaint( );
        }
        else
        {
            jrf.getSelectedCrosstabEditorPanel().repaint();
        }
    }
    
    public Vector getElementSelection()
    {
        return elementSelection;

    }

    public void setElementSelection(Vector newElementSelection) {
        
        this.elementSelection.removeAllElements();
        if (newElementSelection == null) return;
        this.elementSelection.addAll(newElementSelection);
    }
    
    protected void updateReportFonts()
    {
        Object value = spPdfFontName.getValue();
        
        Vector fontsVec = new Vector();
        fontsVec.addElement(new Tag("Helvetica"));
        fontsVec.addElement(new Tag("Helvetica-Bold"));
        fontsVec.addElement(new Tag("Helvetica-BoldOblique"));
        fontsVec.addElement(new Tag("Helvetica-Oblique"));
        fontsVec.addElement(new Tag("Courier"));
        fontsVec.addElement(new Tag("Courier-Bold"));
        fontsVec.addElement(new Tag("Courier-BoldOblique"));
        fontsVec.addElement(new Tag("Courier-Oblique"));
        fontsVec.addElement(new Tag("Symbol"));
        fontsVec.addElement(new Tag("Times-Roman"));
        fontsVec.addElement(new Tag("Times-Bold"));
        fontsVec.addElement(new Tag("Times-BoldItalic"));
        fontsVec.addElement(new Tag("Times-Italic"));
        fontsVec.addElement(new Tag("ZapfDingbats"));
        fontsVec.addElement(new Tag("STSong-Light"));
        fontsVec.addElement(new Tag("MHei-Medium"));
        fontsVec.addElement(new Tag("MSung-Light"));
        fontsVec.addElement(new Tag("HeiseiKakuGo-W5"));
        fontsVec.addElement(new Tag("HeiseiMin-W3"));
        fontsVec.addElement(new Tag("HYGoThic-Medium"));
        fontsVec.addElement(new Tag("HYSMyeongJo-Medium"));
        Vector iRfonts = MainFrame.getMainInstance().getTtfFonts();
        for (int i_f=0; i_f<iRfonts.size(); ++i_f)
        {
            fontsVec.addElement(new Tag( ((IRFont)iRfonts.elementAt(i_f)).getFile(), 
                                   iRfonts.elementAt(i_f)+""));
        }
        
        Misc.updateComboBox( (JComboBox)spPdfFontName.getEditor(), fontsVec );
        spPdfFontName.setEditorValue( spPdfFontName.getEditor(), value );
    }

    public void reportBandsSelectionChanged(ReportBandsSelectionEvent evt) {
    }
    
    public void reportObjectsSelectionChanged(ReportObjectsSelectionEvent evt) {
    }
}
