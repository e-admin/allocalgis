/**
 * I18N.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * (c) 2007 by lat/lon GmbH
 *
 * @author Serge N'Cho (ncho@latlon.de)
 *
 * This program is free software under the GPL (v2.0)
 * Read the file LICENSE.txt coming with the sources for details.
 */
package de.latlon.deejump.wfs.i18n;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import org.deegreewfs.framework.util.BootLogger;

/**
 * @author sncho
 * 
 * TODO To change the template for this generated type comment go to Window - Preferences - Java -
 * Code Style - Code Templates
 */
public class I18N {
    private static final Properties props = new Properties();

    static {
        try {
            String fileName = "messages_en.properties";
            InputStream is = I18N.class.getResourceAsStream( fileName );
            if ( is == null ) {
                BootLogger.log( "Error while initializing " + I18N.class.getName() + " : " + " default message file: '"
                                + fileName + " not found." );
            }
            is = I18N.class.getResourceAsStream( fileName );
            props.load( is );
            is.close();

            // override messages using file "/message_en.properties"
            fileName = "/messages_en.properties";
            overrideMessages( fileName );

            String lang = Locale.getDefault().getLanguage();
            if ( !"".equals( lang ) && !"en".equals( lang ) ) {
                // override messages using file "org/deegree/i18n/message_LANG.properties"
                fileName = "messages_" + lang + ".properties";
                overrideMessages( fileName );
                // override messages using file "/message_LANG.properties"
                fileName = "/messages_" + lang + ".properties";
                overrideMessages( fileName );
            }
        } catch ( IOException e ) {
            BootLogger.logError( "Error while initializing " + I18N.class.getName() + " : " + e.getMessage(), e );
        }
    }

    private static void overrideMessages( String propertiesFile )
                            throws IOException {
        InputStream is = I18N.class.getResourceAsStream( propertiesFile );
        if ( is != null ) {
            // override default messages
            Properties overrideProps = new Properties();
            overrideProps.load( is );
            is.close();
            Iterator<Object> iter = overrideProps.keySet().iterator();
            while ( iter.hasNext() ) {
                String key = (String) iter.next();
                props.put( key, overrideProps.get( key ) );
            }
        }
    }

    /**
     * @param key
     * @param arguments
     * @return a translated string
     */
    public static String getString( String key, Object... arguments ) {
        String s = props.getProperty( key );
        if ( s != null ) {
            return MessageFormat.format( s, arguments );
        }

        return "$Message with key: " + key + " not found$";
    }
}