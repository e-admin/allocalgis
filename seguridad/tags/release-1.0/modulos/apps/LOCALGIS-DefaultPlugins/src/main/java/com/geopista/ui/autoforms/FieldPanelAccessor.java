/*
 * Created on 10-may-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.geopista.ui.autoforms;
import java.awt.Component;

import javax.swing.JComponent;
/**
 * @author juacas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public interface FieldPanelAccessor {
	/**
	 * @param attName
	 * @return
	 */
	public abstract JComponent getComponentByFieldName(String attName);
	/* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.MultiInputDialog#getLabel(java.lang.String)
	 */public abstract JComponent getLabel(String fieldName);
} 