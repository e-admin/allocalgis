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
 * JRTextExpressionArea.java
 * 
 * Created on 28 aprile 2003, 22.55
 *
 */

package it.businesslogic.ireport.gui;
import it.businesslogic.ireport.CrosstabReportElement;
import it.businesslogic.ireport.ReportElement;
import it.businesslogic.ireport.util.I18n;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Vector;

import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;

import org.syntax.jedit.JEditTextArea;
import org.syntax.jedit.SyntaxDocument;
import org.syntax.jedit.tokenmarker.JavaTokenMarker;
/**
 *
 * @author  Administrator
 */
public class JRTextExpressionArea extends JEditTextArea {
    
    private int spessore = 0;
    protected boolean viewScrollbars = true;
    
    private javax.swing.JPopupMenu jPopupMenuTextField;
    private javax.swing.JMenuItem jMenuItemTextEditor;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JMenuItem jMenuItemCut;
    private javax.swing.JMenuItem jMenuItemCopy;
    private javax.swing.JMenuItem jMenuItemPaste;
    private javax.swing.JMenuItem jMenuItemSelectAll;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JMenu jMenuFields;
    private javax.swing.JMenu jMenuVariables;
    private javax.swing.JMenu jMenuParameters;
    
    private boolean hasErrors = false;
    
    private it.businesslogic.ireport.SubDataset subDataset = null;
    private Vector crosstabElements = new Vector();
    
    
    /** Creates a new instance of JRTextExpressionArea */
    public JRTextExpressionArea() {
        super();
        
        setEditButtonVisible(true);

        painter.setEOLMarkersPainted(false);
        painter.setInvalidLinesPainted(false);
        painter.setLineHighlightEnabled(false);
        
        spessore = vertical.getWidth();
        super.painter.setLineHighlightColor(java.awt.Color.WHITE);
        this.setDocument(new SyntaxDocument());

        setTokenMarker(new JavaTokenMarker());
        
        getDocument().addUndoableEditListener(new SimpleUndoableEditListener());
        addMouseWheelListener(new SimpleMouseWheelListener());
        initPopupMenu();
        
        setToolTipText("TIP: open the text editor to edit your expression:\npress right mouse button and choose <b>Expression editor</b>");
        
/*        
        this.getInputHandler().addKeyBinding("C+Z", new java.awt.event.ActionListener() {

             public void actionPerformed(java.awt.event.ActionEvent evt)
             {                 
                  undo();
                  ;
                  System.out.println(getName() + " " + getDocument().getUM());
             }
        } );
  */                 
        
        this.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                setHasErrors(false);
            }
            public void insertUpdate(DocumentEvent e) {
                setHasErrors(false);
            }
            public void removeUpdate(DocumentEvent e) {
                setHasErrors(false);
            }
        });
        
    }
    
    
    public void initPopupMenu()
    {
        
        jPopupMenuTextField = new javax.swing.JPopupMenu();
        jMenuItemTextEditor = new javax.swing.JMenuItem();
        jSeparator8 = new javax.swing.JSeparator();
        jMenuItemCut = new javax.swing.JMenuItem();
        jMenuItemCopy = new javax.swing.JMenuItem();
        jMenuItemPaste = new javax.swing.JMenuItem();
        jMenuItemSelectAll = new javax.swing.JMenuItem();
        jSeparator9 = new javax.swing.JSeparator();
        jMenuFields = new javax.swing.JMenu();
        jMenuVariables = new javax.swing.JMenu();
        jMenuParameters = new javax.swing.JMenu();
        
        jMenuItemTextEditor.setText("Use texteditor");
        jMenuItemTextEditor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemTextEditorActionPerformed(evt);
            }
        });

        jPopupMenuTextField.add(jMenuItemTextEditor);

        jPopupMenuTextField.add(jSeparator8);

        jMenuItemCut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/cut.png")));
        jMenuItemCut.setText( I18n.getString("cut","Cut") );
        //jMenuItemCut.setEnabled(false);
        jPopupMenuTextField.add(jMenuItemCut);

        jMenuItemCut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCutActionPerformed(evt);
            }
        });
        
        jMenuItemCopy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/copy.png")));
        jMenuItemCopy.setText(I18n.getString("copy","Copy"));
        //jMenuItemCopy.setEnabled(false);
        jPopupMenuTextField.add(jMenuItemCopy);

        jMenuItemCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCopyActionPerformed(evt);
            }
        });
        
        jMenuItemPaste.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/paste.png")));
        jMenuItemPaste.setText(I18n.getString("paste","Paste"));
        //jMenuItemPaste.setEnabled(false);
        jPopupMenuTextField.add(jMenuItemPaste);

        jMenuItemPaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPasteActionPerformed(evt);
            }
        });
        
        jMenuItemSelectAll.setText("Select all");
        //jMenuItemPaste.setEnabled(false);
        jPopupMenuTextField.add(jMenuItemSelectAll);

        jMenuItemSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSelectAllActionPerformed(evt);
            }
        });
        
        
        
        /*
        jPopupMenuTextField.add(jSeparator9);

        jMenuFields.setText("Fields");
        jPopupMenuTextField.add(jMenuFields);

        jMenuVariables.setText("Variables");
        jPopupMenuTextField.add(jMenuVariables);

        jMenuParameters.setText("Parameters");
        jPopupMenuTextField.add(jMenuParameters);
        */
        
        this.setRightClickPopup(jPopupMenuTextField);
    }
    
    private void jMenuItemTextEditorActionPerformed(java.awt.event.ActionEvent evt)
    {
        if (MainFrame.getMainInstance().getActiveReportFrame() == null) return;
        ExpressionEditor ed = new ExpressionEditor();
        
        //System.out.println("Sundataset: " + this.getSubDataset());
        //System.out.println("Crosstabs: " + getCrosstabElements().size());
                 
        if (this.getSubDataset() == null && getCrosstabElements().size() == 0) 
        {    
            if (MainFrame.getMainInstance().getActiveReportFrame().getSelectedCrosstabEditorPanel() == null )
            {
                this.setSubDataset( MainFrame.getMainInstance().getActiveReportFrame().getReport() );
            }
            else
            {
                addCrosstab(MainFrame.getMainInstance().getActiveReportFrame().getSelectedCrosstabEditorPanel().getCrosstabElement() );
            }
        }
        
        if (getSubDataset() != null) ed.setSubDataset(this.getSubDataset());
        
        ed.setCrosstabElements(getCrosstabElements());
        ed.setExpression( getText() );
        ed.updateTreeEntries();
        //System.out.println(getText());
        ed.setVisible(true);
        if (ed.getDialogResult() == javax.swing.JOptionPane.OK_OPTION)
        {
            this.setText( ed.getExpression());
        }
        
    }
    
    private void jMenuItemCutActionPerformed(java.awt.event.ActionEvent evt)
    {
        this.cut();
    }
    
    private void jMenuItemCopyActionPerformed(java.awt.event.ActionEvent evt)
    {
        this.copy();
    }
    
    private void jMenuItemPasteActionPerformed(java.awt.event.ActionEvent evt)
    {
        this.paste();
    }
    
    private void jMenuItemSelectAllActionPerformed(java.awt.event.ActionEvent evt)
    {
        this.setSelectionStart(0);
        this.setSelectionEnd( getText().length());
    }
    
    /** Getter for property viewScrollbars.
     * @return Value of property viewScrollbars.
     *
     */
    public boolean isViewScrollbars() {
        return viewScrollbars;
    }
    
    /** Setter for property viewScrollbars.
     * @param viewScrollbars New value of property viewScrollbars.
     *
     */
    public void setViewScrollbars(boolean viewScrollbars) {
        
         //System.out.println("Ops"+viewScrollbars);
        //if (this.viewScrollbars == viewScrollbars) return;
        if (!viewScrollbars)
        {
            //super.vertical.setVisible(false);
            //super.horizontal.setVisible(false);
            
            //super.vertical.setSize(0,0);
        }
        else
        {
            //super.vertical.setVisible(true);
            //super.horizontal.setVisible(true);
            //super.vertical.setSize(spessore,  super.vertical.getHeight());
            //super.horizontal.setSize(spessore,  super.horizontal.getWidth());
        }
        this.viewScrollbars = viewScrollbars;
    }
    

    public void setEnabled(boolean enabled)
    {
        if (super.isEnabled() == enabled) return;
        super.setEnabled(enabled);
        if (!super.isEnabled())
        {
            for (int i=0; i<this.getComponentCount(); ++i)
                this.getComponent(i).setBackground(java.awt.Color.GRAY);
            this.setCaretVisible(false);
           this.getPainter().setLineHighlightEnabled(false);
           
            
        }
        else
        {
            for (int i=0; i<this.getComponentCount(); ++i)
                this.getComponent(i).setBackground(java.awt.Color.WHITE);
            this.setCaretVisible(true);
            this.getPainter().setLineHighlightEnabled(true);
        }  
    
    }
    
    
    class SimpleUndoableEditListener implements UndoableEditListener
    {
        public void undoableEditHappened(UndoableEditEvent e)
        {
            addEdit(e);
            //System.out.println("Add to undo.." + getDocument().getUM().canUndo());
            setHasErrors(false);
        }

        protected SimpleUndoableEditListener()
        {
        }
    }
    
    class SimpleMouseWheelListener implements MouseWheelListener
    {

        public void mouseWheelMoved(MouseWheelEvent e)
        {
            if(e.getScrollType() == 0 && ((getFirstLine() + getVisibleLines()) - 1) + e.getWheelRotation() < getLineCount() && getFirstLine() + e.getWheelRotation() >= 0)
                setFirstLine(getFirstLine() + e.getWheelRotation());
        }

        protected SimpleMouseWheelListener()
        {
        }
    }

     public void addEdit(UndoableEditEvent e)
     {
          getDocument().getUM().addEdit( e.getEdit() );
          //System.out.println( "Add edit to doc: " + getDocument().getUM() );
     }

    public it.businesslogic.ireport.SubDataset getSubDataset() {
        return subDataset;
    }

    public void setSubDataset(it.businesslogic.ireport.SubDataset subDataset) {
        this.subDataset = subDataset;
    }

    public Vector getCrosstabElements() {
        return crosstabElements;
    }

    public void setCrosstabElements(Vector crosstabElements) {
        this.crosstabElements = crosstabElements;
    }
    
    public void addCrosstab(CrosstabReportElement cr)
    {
        this.getCrosstabElements().add(cr);
        
    }
    
    public void editButtonActionPerformed(ActionEvent e)
     {
            jMenuItemTextEditorActionPerformed(e);
    }
        
    public void applyI18n(){
                // Start autogenerated code ----------------------
                jMenuItemTextEditor.setText(I18n.getString("jRTextExpressionArea.menuItemTextEditor","Use texteditor"));
                jMenuItemSelectAll.setText(I18n.getString("jRTextExpressionArea.menuItemSelectAll","Select all"));
                jMenuFields.setText(I18n.getString("jRTextExpressionArea.menuFields","Fields"));
                jMenuVariables.setText(I18n.getString("jRTextExpressionArea.menuVariables","Variables"));
                jMenuParameters.setText(I18n.getString("jRTextExpressionArea.menuParameters","Parameters"));
                // End autogenerated code ----------------------
    }
    
    public Border oldBorder = null;
    
    public void paint(Graphics graphics)
    {
        super.paint(graphics);
        
        if (isHasErrors() )
        {
            Graphics2D g = (Graphics2D)graphics;
            Stroke s = g.getStroke();
              g.setStroke( ReportElement.getPenStroke("4Point",1.0));
              g.setColor( ReportElement.getAlphaColor(Color.RED, 128)  );        
              g.drawRect(0,0,getWidth(), getHeight() );
              g.setStroke(s);
        }
    }


    public boolean isHasErrors() {
        return hasErrors;
    }

    public void setHasErrors(boolean hasErrors) {
        this.hasErrors = hasErrors;
        this.repaint();
    }

}
