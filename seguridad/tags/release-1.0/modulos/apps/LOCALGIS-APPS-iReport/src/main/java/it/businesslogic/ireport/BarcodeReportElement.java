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
 * BarcodeReportElement.java
 * 
 * Created on 15. April 2004, 13:21 by Heiko Wenzel
 *
 */

package it.businesslogic.ireport;
import java.awt.*;
import it.businesslogic.ireport.util.*;

import java.util.*;

/**
 *
 * @author  Administrator
 */
public class BarcodeReportElement extends it.businesslogic.ireport.ImageReportElement {

    private String title = "Sample barcode";
	private boolean legend = false;
	private boolean showText = false;
	private String text = "\"0815\"";
	private int type = 13;
	private boolean checkSum = false;
	private String lastError = null;
        private static java.awt.Image barcodeError = null;
        private int imageHeight = 0;
        private int imageWidth = 0;
        private String applicationIdentifier = "null";

	public BarcodeReportElement(int x, int y, int width, int height)  {
        // do not allow barcodes with a width or height == 0
		super(x, y, ( width == 0 ? 1 : Math.abs(width)) , (height == 0 ? 1 : Math.abs(height)) );
        
                if (barcodeError == null)
                {
                    barcodeError = Misc.loadImageFromResources("it/businesslogic/ireport/icons/barcodeerror.png");
                }
                
		//setGraphicElementPen("Thin");
		setBarCodeImg(type, text, showText, checkSum);
		setImageClass("java.awt.Image");
		setScaleImage("RetainShape");
		setGraphicElementPen("None");
		setHyperlinkType("None");
		setAnchorNameExpression("");
		setImgDef(null);
                setKey("barcode");
	}
	
	public void setBarCodeImg(int type, String text, boolean showText, boolean checkSum){
		
		StringBuffer bcCall = new StringBuffer("it.businesslogic.ireport.barcode.BcImage.getBarcodeImage(");
		//boolean isFormula = text.trim().startsWith("$");
		bcCall.append(type);
		
		//if (isFormula) {
			bcCall.append(",");
			bcCall.append(text);
			bcCall.append(",");
		//} else {
		//	bcCall.append(",\"");
		//	bcCall.append(text);
		//	bcCall.append("\",");
		//}
		
		bcCall.append(showText);
		bcCall.append(",");
		bcCall.append(checkSum);
                bcCall.append(",");
                bcCall.append( applicationIdentifier);
                bcCall.append(",");
                bcCall.append( getImageWidth() + "," + getImageHeight());
		bcCall.append(")");
		
		super.setImageExpression(bcCall.toString());

		try{
			
			setImg(it.businesslogic.ireport.barcode.BcImage.getBarcodeImage(type, text, showText, checkSum,getApplicationIdentifier(),getImageWidth(),getImageHeight()));
			lastError = null;
			
		} catch (RuntimeException e) {
			
			//reset image
			setImg(barcodeError);
                        
			
			//save last error message
			lastError = e.getMessage(); 
		}
	}
	
        /*
	public void setImageExpression(java.lang.String imageExpression) {
		super.setImageExpression(imageExpression);
		
		String iE = imageExpression.substring(imageExpression.indexOf("(") + 1,
                        imageExpression.lastIndexOf(")"));
		String[] params = iE.split(",");
		type = new Integer(params[0]).intValue();
		
		//if (params[1].startsWith("$")) {
                text = params[1];
                
        //} else {
        //    text = params[1].substring(1, params[1].length() - 1);
        //}
		
		showText = new Boolean(params[2]).booleanValue();
		checkSum = new Boolean(params[3]).booleanValue();

                if (params.length > 4)
                {
                    this.setApplicationIdentifier( params[4] );          
                    this.setImageWidth( Integer.parseInt( params[5]) );
                    this.setImageHeight( Integer.parseInt( params[6]) );
                }
                
		update();
	}*/
        public void setImageExpression(java.lang.String imageExpression) {
          super.setImageExpression(imageExpression);
          final int numberOfParams = 7;
          //We must get our params from the end of the  imageExpression by
          //using comma delimiters because the actual expression
          //may contain commas itself (e.g. String.format("%s", xx)

          String iE = imageExpression.substring(imageExpression.indexOf("(") + 1, imageExpression.lastIndexOf(")"));
          String[] params = iE.split(",");
          int paramCount = params.length;

          type = new Integer(params[0]).intValue(); //params[0] will always be type

          String text = "";
          for (int i=0; i <= paramCount - numberOfParams; i++)
          {
            text += params[i+1] + ",";
          }
          text = text.substring(0, text.length() - 1);

          setText(text);

          showText = new Boolean(params[2 + (paramCount - numberOfParams)]).booleanValue();
          checkSum = new Boolean(params[3 + (paramCount - numberOfParams)]).booleanValue();

          if (params.length > 4)
          {
            this.setApplicationIdentifier( params[4 + (paramCount - numberOfParams)] );          
            this.setImageWidth( Integer.parseInt( params[5 + (paramCount - numberOfParams)]) );
            this.setImageHeight( Integer.parseInt( params[6 + (paramCount - numberOfParams)]) );
          }

          update();
        }

	
	public void setShowText(boolean showText) {
		this.showText = showText;
		update();
	}
	
	public boolean isShowText() {
		return this.showText;
	}
	
	public void update() {
		
		setBarCodeImg(type, text, showText, checkSum);
		
		/*
                 boolean isFormula = text.trim().startsWith("$");
                
		if (! isFormula) {
			this.width = it.businesslogic.ireport.barcode.BcImage.getBarcode().getMinimumSize().width;
			this.height = it.businesslogic.ireport.barcode.BcImage.getBarcode().getMinimumSize().height;
		}
		*/
		this.updateBounds();
	}
	
	public ReportElement cloneMe() {
		BarcodeReportElement newReportElement = new BarcodeReportElement(position.x, position.y, width, height);
		copyBaseReportElement(newReportElement, this);
		newReportElement.setImageHeight( this.getImageHeight());
                newReportElement.setImageWidth( this.getImageWidth());
                newReportElement.setApplicationIdentifier( this.getApplicationIdentifier());
                return newReportElement;
	}
	
	public java.lang.String getTitle() {
		return title;
	}
	
	public void setTitle(java.lang.String title) {
		this.title = title;
		this.setImg(null);
	}
	
	public String getText() {
		return this.text;
	}
	
	public void setText(String text) {
		this.text = text;
		update();
	}
	
	public int getType() {
		return this.type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public boolean isCheckSum() {
		return this.checkSum;
	}
	
	public void setCheckSum(boolean checkSum) {
		this.checkSum = checkSum;
		update();
	}
	
	/**
	 * Last error message.
	 * @return Returns null if ok.
	 */
	public String getLastError() {
		return lastError;
	}



    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
        update();
    }

    public String getApplicationIdentifier() {
        if (applicationIdentifier == null || applicationIdentifier.equals("null")) return "";
        return applicationIdentifier;
    }

    public void setApplicationIdentifier(String applicationIdentifier) {
        if (applicationIdentifier == null || applicationIdentifier.equals("")) applicationIdentifier = "null";
        this.applicationIdentifier = applicationIdentifier;
        update();
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
        update();
    }

}
