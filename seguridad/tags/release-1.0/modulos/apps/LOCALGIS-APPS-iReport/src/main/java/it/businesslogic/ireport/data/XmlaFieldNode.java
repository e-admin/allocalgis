/**
 * Copyright (C) 2005, 2006 CINCOM SYSTEMS, INC.
 * All Rights Reserved
 * www.cincom.com
 * @author MPenningroth
 */
package it.businesslogic.ireport.data;


public class XmlaFieldNode extends it.businesslogic.ireport.JRField {
    private int axisNumber;
    
    /**
     * Creates a new instance of XmlaFieldNode 
     * assings the values for Node name and the Axis number.
     */
    public XmlaFieldNode(String name, int axisNumber) {
        super(name, "java.lang.String");
        this.axisNumber = axisNumber;
    }
    /**
     * Returns axisNumber.
     */
    public int getAxisNumber() {
        return axisNumber;
    }
    
}
