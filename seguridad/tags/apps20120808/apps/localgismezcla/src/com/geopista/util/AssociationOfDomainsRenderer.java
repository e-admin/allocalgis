/*
 * Created on 06-may-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.geopista.util;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;



/**
 * @author jalopez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AssociationOfDomainsRenderer extends JLabel implements ListCellRenderer {

    public AssociationOfDomainsRenderer() {

        setOpaque(true);
        setHorizontalAlignment(CENTER);
        setVerticalAlignment(CENTER);
    }

    /*
     * This method finds the image and text corresponding
     * to the selected value and returns the label, set up
     * to display the text and image.
     */
    public Component getListCellRendererComponent(
                                       JList list,
                                       Object value,
                                       int index,
                                       boolean isSelected,
                                       boolean cellHasFocus) {
        //Get the selected index. (The index param isn't
        //always valid, so just use the value.)

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
            //setBorder(BorderFactory.createLineBorder(Color.red,2));
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        setHorizontalAlignment(LEFT);
        DomainValueAssociation associationList = (DomainValueAssociation) value;
        DomainDescriptionCode domainDescriptionCode = associationList.getExistingDomain();
        
        setText("El dominio " + associationList.getNewDomain() + " se vinculará al dominio " + domainDescriptionCode.getCode() + " ( "+domainDescriptionCode.getDescription() +" )");
        return this;
    }
}
