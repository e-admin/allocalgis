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
 * GenericWizard.java
 * 
 * Created on March 22, 2006, 9:06 PM
 *
 */

package it.businesslogic.ireport.gui.wizard;

import javax.swing.JPanel;

/**
 *
 * @author gtoffoli
 */
public interface GenericWizard {
    
    public String[] getStepsNames();
    public String getStepDescription(int step);
    
    
    public void initWizard();
    
    /* Go to the next step (stepNumber)
     * return true if the nextStep is succesfully reached
     */
    public boolean nextStep(int nextStep);
    
    
    /* Go to the previous step (stepNumber)
     * return true if the nextStep is succesfully reached
     */
    public boolean previousStep(int previousStep);
    
    /* finish now
     *
     */
    public void finish(int currentStep);
    
    public boolean canFinish(int currentStep);
    public boolean canNext(int currentStep);
    public boolean canPrevious(int currentStep);
    
    public JPanel getStepPanel( int step);


}
