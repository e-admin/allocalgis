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
 * Language.java
 * 
 * Created on 19 March 2004, 12:23
 *
 */

package it.businesslogic.ireport;

import it.businesslogic.ireport.util.Misc;
/**
 *
 * @author  ertano
 */
public class Language {
    
    String  filenameWithPath,filename,languageName,Id;
        
    public void setFilenameWithPath(String f){
        filenameWithPath = f;
    }
    public void setFilename(String f){
        filename = f;
    }
    public void setLanguageName(String s){
        languageName = s;
    }
    public void setId(String s){
        Id = s;
    }
    public String getFilenameWithPath(){
        return filenameWithPath;
    }
    public String getFilename(){
        return filename;
    }
    public String getLanguageName(){
        return languageName;
    }
    public String getId(){
        return Id;
    }
    
    public String toString()
    {
        return Misc.nvl( this.getLanguageName(),"");        
    }
}
