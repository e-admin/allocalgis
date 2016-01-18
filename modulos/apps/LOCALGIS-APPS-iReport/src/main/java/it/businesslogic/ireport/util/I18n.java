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
 * I18n.java
 *
 * Created on 13 March 2004, 21:43
 *
 */

package it.businesslogic.ireport.util;

import it.businesslogic.ireport.gui.MainFrame;

import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 *
 * @author  ertano
 */
public class I18n
{

    public static final String localPackageName = "it/businesslogic/ireport/locale/";
    public static final String baseName = "Ireport";
    private static java.util.ResourceBundle oLanguage = null;

    public static java.util.Vector languageChangedListeners = null;

    static
    {

        languageChangedListeners = new Vector();
    }

    public static void addOnLanguageChangedListener(LanguageChangedListener listener)
    {
        languageChangedListeners.add( listener );
    }

    /**
     * Get the list of supported translations.
     *
     * Load the list of property files in the it/businesslogic/ireport/locale/
     * package.
     *
     */

    /*
    public static java.util.List getListOfAvailLanguages(){
        java.util.List supportedLocales = new java.util.ArrayList();
        try {
            Set names = Misc.getResoucesInPackage( localPackageName );
            Iterator it = names.iterator();
            while( it.hasNext() ) {
                String n = (String)it.next();

                // From
                //    'it/businesslogic/ireport/locale/Ireport_en.properties'
                //   or
                //    'it/businesslogic/ireport/locale/Ireport_en_UK.properties'
                // To
                // 'en' OR 'en_UK'

                String lang = n.substring( n.lastIndexOf('/')+1 );
                lang = lang.substring(0, lang.indexOf(".properties") );
                if(lang.indexOf("_")>0) // otherwise index exception
                    lang = lang.substring( baseName.length()+1 );       // +1 to include the underscore

                Locale model;
                int underscorePos = lang.indexOf('_');
                if( underscorePos==-1 ) {
                    String language = lang;
                    model = new Locale( language );
                } else {
                    String language = lang.substring( 0, lang.indexOf('_') );
                    String country = lang.substring( lang.indexOf('_')+1 );
                    model = new Locale( language, country );
                }
                supportedLocales.add( model );
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        // Sort the list. Probably should use the current locale when getting the
        // DisplayLanguage so the sort order is correct for the user.
        Collections.sort( supportedLocales, new Comparator() {
           public int compare(Object lhs, Object rhs) {
               Locale ll = (Locale)lhs;
               Locale rl = (Locale)rhs;
               return ll.getDisplayLanguage().compareTo( rl.getDisplayLanguage() );
           }
        });

        return supportedLocales;
    }
    */


    // => Modified by RL: June 3, 2005: Introducing Variant
    public static java.util.List getListOfAvailLanguages()
    {
        java.util.List supportedLocales = new java.util.ArrayList();
        
        try
        {
            Set names = Misc.getResoucesInPackage( localPackageName );
            Iterator it = names.iterator();
            while( it.hasNext() )
            {
                String n = (String)it.next();

                // From
                //    'it/businesslogic/ireport/locale/Ireport_en.properties'
                //   or
                //    'it/businesslogic/ireport/locale/Ireport_en_UK.properties'
                // To
                // 'en' OR 'en_UK_' OR even en_UK_Brighton dialect

                String lang = n.substring( n.lastIndexOf('/')+1 );

                // only except resources with extension '.properties'
                if ( lang.indexOf(".properties") < 0 )
                {
                    continue; // not very nice but efficient
                    //TODO: wrap the curly braces around the next statements
                }

                lang = lang.substring(0, lang.indexOf(".properties") );

                StringTokenizer tokenizer = new StringTokenizer( lang, "_");
                if ( tokenizer.countTokens() <=  1 )
                {
                    // empty filename or "iReport.properties"
                    continue;  // not very nice but efficient
                    //TODO: wrap the curly braces around the next statements
                }

                String language = "";
                String country  = "";
                String variant  = "";

                String[] parts = new String[tokenizer.countTokens()];
                // first token (position 0) is iReport
                //System.out.println( "\n File: " + lang  + "\n" );

                //System.out.println( "\n Aantal tokens: " + "  " + tokenizer.countTokens()  + "\n"  );
                int i = 0;
                while (tokenizer.hasMoreTokens() )
                {
                    String token = tokenizer.nextToken();

                    //System.out.println( "\n" + i + "  " + token  + "\n"  );

                    switch (i)
                    {
                        case 0:
                            //the word iReport
                            break;
                        case 1:
                            language = token;
                            break;
                        case 2:
                            country = token;
                            break;
                        case 3:
                            variant = token;
                            break;
                        default:
                            //
                    }
                    i++;

                }

                Locale model = new Locale( language, country, variant );
                supportedLocales.add( model );

            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        // Sort the list. Probably should use the current locale when getting the
        // DisplayLanguage so the sort order is correct for the user.
        Collections.sort( supportedLocales, new Comparator()
        {
            public int compare(Object lhs, Object rhs)
            {
                //Locale ll = (Locale)lhs;
                //Locale rl = (Locale)rhs;
                String ls = ((Locale)lhs).getDisplayLanguage();
                String rs = ((Locale)rhs).getDisplayLanguage();

                // this is not very nice
                // We should introduce a MyLocale
                if (ls.equals("pap"))
                {
                    ls = "Papiamentu";
                }
                if (rs.equals("pap"))
                {
                    rs = "Papiamentu";
                }


                //return ll.getDisplayLanguage().compareTo( rl.getDisplayLanguage() );
                return ls.compareTo( rs );
            }
        });

        return supportedLocales;
    }



//    public static it.businesslogic.ireport.Language getLanguage(String language){
//        java.io.File myFiles[] = new java.io.File("lang").listFiles(new it.businesslogic.ireport.util.I18nFilenameFilter());
//        String filename = "";
//        java.util.Properties p = new java.util.Properties();
//        it.businesslogic.ireport.Language model = null;
//        for(int i = 0; i < java.util.Arrays.asList(myFiles).size(); i++) {
//            filename = myFiles[i].getName();
//            try{
//                p.load(new java.io.FileInputStream("lang"+java.io.File.separatorChar+filename));
//                if (!filename.equals(baseName+".properties") && !p.getProperty("LanguageName").equals("")
//                && p.getProperty("LanguageID").equals(language)) {
//                    model = new it.businesslogic.ireport.Language();
//                    model.setFilenameWithPath("lang"+java.io.File.separatorChar+filename);
//                    model.setFilename(filename);
//                    model.setLanguageName(p.getProperty("LanguageName"));
//                    model.setId(p.getProperty("LanguageID"));
//                    break;
//                }
//            }catch(Exception e){e.printStackTrace();}
//        }
//        return model;
//    }


    // Default to english
    private static Locale currentLocale = Locale.ENGLISH;

    public static void setCurrentLocale( String language )
    {
        setCurrentLocale(language, null);
    }

    public static void setCurrentLocale( String language, String country )
    {
        if(language != null && !language.equals(""))
        {
            if(country != null && !country.equals(""))
            {
                setCurrentLocale(new java.util.Locale(language, country));
            }
            else
            {
                setCurrentLocale(new java.util.Locale(language));
            }
        }
        else
        {
            setCurrentLocale(java.util.Locale.getDefault());
        }

    }

    public static void setCurrentLocale( Locale locale )
    {
        currentLocale=locale;
        oLanguage = null;

        Enumeration enum_listeners = languageChangedListeners.elements();
        while (enum_listeners.hasMoreElements())
        {
            try
            {
                ((LanguageChangedListener)(enum_listeners.nextElement())).languageChanged(new LanguageChangedEvent(locale));
            }
            catch (Exception ex)
            {}
        }
    }

    public static Locale getCurrentLocale()
    {
        return currentLocale;
    }

    /**
     * Retreive a resource string using the current locale.
     * @param cID The resouce sting identifier
     * @return The locale specific string
     */
    public static String getString(String cID)
    {
        return getString(cID, currentLocale );
    }

    public static String getString(String cID,String defaultValue)
    {
        return getString(cID, currentLocale, defaultValue );
    }

    /**
     * Retreive a resource string using the current locale.
     * @param cID The resouce sting identifier
     * @return The locale specific string
     */
    public static String getFormattedString(String cID, String defaultValue, Object[] args)
    {
        String pattern = getString(cID, getCurrentLocale(), defaultValue );
        java.text.MessageFormat mf = new java.text.MessageFormat(pattern, I18n.getCurrentLocale());
        return mf.format(args);
    }


    private static String getString(String cID, Locale currentLocale)
    {
        if (currentLocale == null)
        {
            currentLocale = Locale.getDefault();
        }
        if(oLanguage == null)
        {
            oLanguage = java.util.ResourceBundle.getBundle( localPackageName + baseName,
                    currentLocale);
        }

        try {
            return oLanguage.getString(cID);
        } catch (Exception ex)
        {
            return cID;
        }
    }

    public static String getString(String cID, Locale currentLocale, String defaultValue)
    {
        try
        {
            if(oLanguage == null)
            {
                if (MainFrame.getMainInstance() != null)
                {
                    oLanguage = java.util.ResourceBundle.getBundle( localPackageName + baseName,
                        currentLocale, MainFrame.getMainInstance().getReportClassLoader());
                }
                else
                {
                    oLanguage = java.util.ResourceBundle.getBundle( localPackageName + baseName,
                        currentLocale);
                }
            }
            return oLanguage.getString(cID);
        }
        catch (MissingResourceException ex)
        {
            System.out.println("Can't find the translation for key = " + cID +": using default (" + defaultValue + ")");
        }
        catch (Exception ex)
        {
            System.out.println("Exception loading cID = " + cID +": " + ex.getMessage());
        }
        return defaultValue;
    }

    /** ErtanO 16.03.2004: not working currently as MainFrame is not accessible therefore we overgive languageid to getString() **/
    public static String getCurrentLocaleID()
    {
        //return it.businesslogic.ireport.gui.MainFrame.getProperties().getProperty("Language");
        return "";
    }
}
