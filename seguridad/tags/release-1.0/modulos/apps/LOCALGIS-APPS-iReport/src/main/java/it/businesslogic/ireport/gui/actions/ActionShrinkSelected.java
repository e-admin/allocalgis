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
 * ActionShrinkSelected.java
 * 
 * Created on 12 juni 2005, 12:39
 *
 */

package it.businesslogic.ireport.gui.actions;

import it.businesslogic.ireport.OperationType;
import it.businesslogic.ireport.gui.command.FormatCommand;
import it.businesslogic.ireport.util.I18n;
import it.businesslogic.ireport.util.LanguageChangedEvent;
import it.businesslogic.ireport.util.LanguageChangedListener;
import javax.swing.AbstractAction;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

/**
 *
 * @author Fourdim
 */
public class ActionShrinkSelected extends AbstractAction implements LanguageChangedListener
{
    
//    /**
//     * The key used for storing the <code>String</code> name
//     * for the action, used for a menu or button.
//     */
//    public static final String NAME = "Name";
//
//    /**
//     * The key used for storing a short <code>String</code>
//     * description for the action, used for tooltip text.
//     */
//    public static String SHORT_DESCRIPTION = "ShortDescription";
//    /**
//     * The key used for storing a longer <code>String</code>
//     * description for the action, could be used for context-sensitive help.
//     */
//    public static String LONG_DESCRIPTION = "LongDescription";
//
//    /**
//     * The key used for storing a small <code>Icon</code>, such
//     * as <code>ImageIcon</code>, for the action, used for toolbar buttons.
//     */
//    public static final String SMALL_ICON = "SmallIcon";
//
//    /**
//     * The key used to determine the command <code>String</code> for the
//     * <code>ActionEvent</code> that will be created when an
//     * <code>Action</code> is going to be notified as the result of
//     * residing in a <code>Keymap</code> associated with a
//     * <code>JComponent</code>.
//     */
//    public static final String ACTION_COMMAND_KEY = "ActionCommandKey";
//
//    /**
//     * The key used for storing a <code>KeyStroke</code> to be used as the
//     * accelerator for the action.
//     *
//     * @since 1.3
//     */
//    public static final String ACCELERATOR_KEY="AcceleratorKey";
//
//    /**
//     * The key used for storing a <code>KeyEvent</code> to be used as
//     * the mnemonic for the action.
//     *
//     * @since 1.3
//     */
//    public static final String MNEMONIC_KEY="MnemonicKey";
    
    
    /** Creates a new instance of ActionShrinkSelected */
    public ActionShrinkSelected()
    {
        // putValue( MNEMONIC_KEY, new Integer(KeyEvent.VK_S)); 
        // http://java.sun.com/docs/books/tutorial/uiswing/components/tooltip.html   
        
        putValue( ACTION_COMMAND_KEY, "shrinkselected");
        //putValue(SMALL_ICON, "/it/businesslogic/ireport/icons/menu/elem_shrink.gif");
        ImageIcon imageIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/elem_shrink.png"));
        putValue(SMALL_ICON, imageIcon );
        
        this.
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_S, java.awt.Event.CTRL_MASK +  java.awt.Event.SHIFT_MASK ));
        
        applyI18n();
        I18n.addOnLanguageChangedListener( this );
    }
    
    public void languageChanged(LanguageChangedEvent evt)
    {
        applyI18n();
    }
    
    public void actionPerformed( java.awt.event.ActionEvent evt)
    {
        FormatCommand.getCommand(OperationType.SHRINK).execute();
    }
    
    public void applyI18n()
    {
        String description = it.businesslogic.ireport.util.I18n.getString( "gui.MainFrame.ShrinkSelected", "Shrink selected");
        putValue( NAME, description);
        putValue(SHORT_DESCRIPTION, description);
    }
    
}
