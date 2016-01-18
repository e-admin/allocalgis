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
 * SyntaxDocument.java
 * 
 */

package org.syntax.jedit;

import org.syntax.jedit.tokenmarker.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.undo.UndoableEdit;
import javax.swing.undo.UndoManager;


/**
 * A document implementation that can be tokenized by the syntax highlighting
 * system.
 *
 * @author Slava Pestov
 * @version $Id: SyntaxDocument.java,v 1.1 2011/12/14 18:15:22 satec Exp $
 */
public class SyntaxDocument extends PlainDocument implements UndoableEditListener
{
        UndoManager UM;

        public SyntaxDocument()
        {
            UM = new UndoManager(); 
            this.addUndoableEditListener((UndoableEditListener)this );
        }

        public void undoableEditHappened(UndoableEditEvent e) {
            //Remember the edit and update the menus
            UM.addEdit(e.getEdit());
        }

        
	/**
	 * Returns the token marker that is to be used to split lines
	 * of this document up into tokens. May return null if this
	 * document is not to be colorized.
	 */
	public TokenMarker getTokenMarker()
	{
		return tokenMarker;
	}

	/**
	 * Sets the token marker that is to be used to split lines of
	 * this document up into tokens. May throw an exception if
	 * this is not supported for this type of document.
	 * @param tm The new token marker
	 */
	public void setTokenMarker(TokenMarker tm)
	{
		tokenMarker = tm;
		if(tm == null)
			return;
		tokenMarker.insertLines(0,getDefaultRootElement()
			.getElementCount());
		tokenizeLines();
	}

	/**
	 * Reparses the document, by passing all lines to the token
	 * marker. This should be called after the document is first
	 * loaded.
	 */
	public void tokenizeLines()
	{
		tokenizeLines(0,getDefaultRootElement().getElementCount());
	}

	/**
	 * Reparses the document, by passing the specified lines to the
	 * token marker. This should be called after a large quantity of
	 * text is first inserted.
	 * @param start The first line to parse
	 * @param len The number of lines, after the first one to parse
	 */
	public void tokenizeLines(int start, int len)
	{
		if(tokenMarker == null || !tokenMarker.supportsMultilineTokens())
			return;

		Segment lineSegment = new Segment();
		Element map = getDefaultRootElement();

		len += start;

		try
		{
			for(int i = start; i < len; i++)
			{
				Element lineElement = map.getElement(i);
				int lineStart = lineElement.getStartOffset();
				getText(lineStart,lineElement.getEndOffset()
					- lineStart - 1,lineSegment);
				tokenMarker.markTokens(lineSegment,i);
			}
		}
		catch(BadLocationException bl)
		{
			bl.printStackTrace();
		}
	}

	/**
	 * Starts a compound edit that can be undone in one operation.
	 * Subclasses that implement undo should override this method;
	 * this class has no undo functionality so this method is
	 * empty.
	 */
	public void beginCompoundEdit() {}

	/**
	 * Ends a compound edit that can be undone in one operation.
	 * Subclasses that implement undo should override this method;
	 * this class has no undo functionality so this method is
	 * empty.
	 */
	public void endCompoundEdit() {}

	/**
	 * Adds an undoable edit to this document's undo list. The edit
	 * should be ignored if something is currently being undone.
	 * @param edit The undoable edit
	 *
	 * @since jEdit 2.2pre1
	 */
	public void addUndoableEdit(UndoableEdit edit) {
            //if (edit != null)  UM.addEdit(edit);
        }

	// protected members
	protected TokenMarker tokenMarker;

	/**
	 * We overwrite this method to update the token marker
	 * state immediately so that any event listeners get a
	 * consistent token marker.
	 */
	protected void fireInsertUpdate(DocumentEvent evt)
	{
		if(tokenMarker != null)
		{
			DocumentEvent.ElementChange ch = evt.getChange(
				getDefaultRootElement());
			if(ch != null)
			{
				tokenMarker.insertLines(ch.getIndex() + 1,
					ch.getChildrenAdded().length -
					ch.getChildrenRemoved().length);
			}
		}
                
                super.fireInsertUpdate(evt);
	}
	
	/**
	 * We overwrite this method to update the token marker
	 * state immediately so that any event listeners get a
	 * consistent token marker.
	 */
	protected void fireRemoveUpdate(DocumentEvent evt)
	{
		if(tokenMarker != null)
		{
			DocumentEvent.ElementChange ch = evt.getChange(
				getDefaultRootElement());
			if(ch != null)
			{
				tokenMarker.deleteLines(ch.getIndex() + 1,
					ch.getChildrenRemoved().length -
					ch.getChildrenAdded().length);
			}
		}

		super.fireRemoveUpdate(evt);
	}
        
        /** Getter for property UM.
         * @return Value of property UM.
         *
         */
        public javax.swing.undo.UndoManager getUM() {
            if (UM == null) {
                UM = new UndoManager();
            }
            //System.out.println(k);
            return UM;
        }
        
        /** Setter for property UM.
         * @param UM New value of property UM.
         *
         */
        public void setUM(javax.swing.undo.UndoManager UM) {
            this.UM = UM;
        }
        
}
