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
 * LocaleAdapter.java
 * 
 * Created on March 27, 2006, 8:19 PM
 *
 */

package it.businesslogic.ireport.util;

import java.util.Locale;

/**
 *
 * @author gtoffoli
 */
/**
         * A simple class to enable locales to be placed in a combo box.
         *
         */
public class LocaleAdapter
{
    Locale l;
    public LocaleAdapter( Locale locale )
    {
        l = locale;
    }

    public Locale getLocale()
    {
        return l;
    }

    // ==> Modified by RL, June 3, 2005, introducing getVariant and Language Papiamentu
    // Getdisplayname is now used to display the name according to the Locale.
    // Possibly in Chinese language parts are combined differently.
    public String toString()
    {
        if ( l.getLanguage().equals("") )
        {
            return " - iReport - " ;
        }
        else if ( l.getLanguage().equals("pap") )
        {
            // Work aournd, as long as LocaleWrapper not active yet.
            // TODO: Get LocaleWrapper active
            String s;
            s= "Papiamentu, " + "(" + l.getCountry() ;

            if (l.getVariant().length() > 0 )
            {
                s = s + ", " + l.getVariant() + ")";
            }
            else
            {
                s = s + ")" ;
            }

            return s;
        }
        else
        {
            return l.getDisplayName(I18n.getCurrentLocale());
        }
    }
    // <= End Modification RL, June 3, 2005

    /*
    public String toString()
    {
        if( l.getCountry()==null || l.getCountry().length()==0 )
        {
            return l.getDisplayLanguage();
        }
        else
        {
            return l.getDisplayLanguage() + " - " + l.getDisplayCountry();
        }
    }
     */

}
