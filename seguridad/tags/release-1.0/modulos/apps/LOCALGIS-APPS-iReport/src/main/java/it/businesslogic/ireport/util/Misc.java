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
 * Misc.java
 * 
 * Created on 14 febbraio 2003, 16.35
 *
 */

package it.businesslogic.ireport.util;
import it.businesslogic.ireport.ConditionedStyle;
import it.businesslogic.ireport.IReportConnection;
import it.businesslogic.ireport.Report;
import it.businesslogic.ireport.Style;
import it.businesslogic.ireport.SubDataset;
import it.businesslogic.ireport.gui.ExpressionEditor;
import it.businesslogic.ireport.gui.JRTextExpressionArea;
import it.businesslogic.ireport.gui.MainFrame;
import it.businesslogic.ireport.gui.sheet.Tag;
import java.awt.*;
import java.awt.image.*;
import java.util.*;
import java.util.jar.*;
import java.net.*;
import java.io.*;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.text.JTextComponent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import org.apache.xerces.parsers.DOMParser;
import org.flexdock.docking.Dockable;
import org.flexdock.docking.DockingConstants;
import org.flexdock.docking.defaults.DefaultDockingStrategy;
import org.flexdock.view.View;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author  Administrator
 */
public class Misc {
        public static final String[] special_chars= new String[]{
                                    "&","&amp;",
                                    "\"","&quot;",
                                    "'","&apos;",
                                    "<","&lt;",
                                    ">","&gt;"
                                    };
                                    /*
                                        "&","&amp;",
                                        "?","&aacute;",
                                        "?","&acirc;",
                                        "?","&aelig;",
                                        "?","&agrave;",
                                        "?","&aring;",
                                        "?","&atilde;",
                                        "?","&auml;",
                                        "?","&ccedil;",
                                        "?","&eacute;",
                                        "?","&ecirc;",
                                        "?","&egrave;",
                                        "?","&eth;",
                                        "?","&euml;",
                                        ">","&gt;",
                                        "?","&iacute;",
                                        "?","&icirc;",
                                        "?","&igrave;",
                                        "?","&iuml;",
                                        "<","&lt;",
                                        "?","&ntilde;",
                                        "?","&oacute;",
                                        "?","&ocirc;",
                                        "?","&ograve;",
                                        "?","&oslash;",
                                        "?","&otilde;",
                                        "?","&ouml;",
                                        "\"","&quot;",
                                        "?","&szlig;",
                                        "?","&thorn;",
                                        "?","&uacute;",
                                        "?","&ucirc;",
                                        "?","&ugrave;",
                                        "?","&uuml;",
                                        "?","&yacute;",
                                        "?","&yuml;",
                                        "?","&#161;",
                                        "?","&#170;",
                                        "?","&#183;",
                                        "?","&#162;",
                                        "?","&#171;",
                                        "?","&#184;",
                                        "?","&#163;",
                                        "?","&#174;",
                                        "?","&#185;",
                                        "?","&#164;",
                                        "?","&#176;",
                                        "?","&#186;",
                                        "?","&#165;",
                                        "?","&#177;",
                                        "?","&#187;",
                                        "?","&#166;",
                                        "?","&#178;",
                                        "?","&#188;",
                                        "?","&#167;",
                                        "?","&#179;",
                                        "?","&#189;",
                                        "?","&#168;",
                                        "?","&#181;",
                                        "?","&#190;",
                                        "?","&#169;",
                                        "?","&#182;",
                                        "?","&#191;",
                                        "?","&#172;",
                                        "?","&#215;",
                                        "?","&#247;",
                                        "?","&#177;",
                                        "?","&#183;",
                                        "?","&#189;",
                                        "?","&#171;",
                                        "?","&#178;",
                                        "?","&#185;",
                                        "?","&#190;",
                                        "?","&#172;",
                                        "?","&#179;",
                                        "?","&#187;",
                                        "?","&#215;",
                                        "?","&#176;",
                                        "?","&#181;",
                                        "?","&#188;",
                                        "?","&#247;"};
                                     */


        /** Creates a new instance of Misc */
        public Misc() {
        }

         public static String xmlEscape(String text)
          {
            if( text == null) return "";
            int i=0;
            String tmp = "";
            for (i=0; i < special_chars.length; i+=2)
            {
              text = string_replace(special_chars[i+1], special_chars[i], text);
              //text = string_replace(special_chars[i], special_chars[i+1], text);
            }

            return text;
          }

         /*
        public static java.awt.Image loadImageFromResources(String filename) {
                try {
                        ClassLoader cl = ClassLoader.getSystemClassLoader();
                        //java.io.InputStream in = new java.io.FileInputStream( cl.getResource(filename).getPath() );
                        java.io.InputStream in = cl.getResourceAsStream(filename);
                        byte[] data = getBytesFromInputStream(in, in.available());
                        return java.awt.Toolkit.getDefaultToolkit().createImage(data);
                } catch (Exception ex) {
                        System.out.println("Exception loading resource: "+filename);
                        //ex.getMessage();
                        //ex.printStackTrace();
                }
                return null;
        }
        */
        /** New version by Umberto Uderzo */
        public static java.awt.Image loadImageFromResources(String filename) {
                try {
                        return new javax.swing.ImageIcon( Misc.class.getResource( "/" + filename )).getImage();
                } catch (Exception ex) {
                        System.out.println("Exception loading resource: " +filename);
                }
                return null;
        }

        /**
         * Returns an array of bytes containing the bytecodes for
         * the class represented by the InputStream
         * @param in the inputstream of the class file
         * @return the bytecodes for the class
         * @exception java.io.IOException if the class cannot be read
         */
        private static byte[] getBytesFromInputStream(java.io.InputStream in, int length)  throws java.io.IOException {
                java.io.DataInputStream din = new java.io.DataInputStream(in);
                byte[] bytecodes = new byte[length];
                try {
                        din.readFully(bytecodes);
                } finally {
                        if (din != null) din.close();
                }
                return bytecodes;
        }

        public static java.awt.image.BufferedImage loadBufferedImageFromResources(Component c,String filename) {

                try {
                        Misc m = new Misc();
                        java.awt.Image img = loadImageFromResources(filename);
                        MediaTracker mt = new MediaTracker(c);
                        mt.addImage(img,0);
                        mt.waitForID(0);
                        int width= img.getWidth(null);
                        int height= img.getHeight(null);
                        BufferedImage bi = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
                        Graphics gg = bi.getGraphics();
                        gg.drawImage(img, 0,0, null);
                        gg.dispose();
                        return bi;
                } catch (Exception ex){
                        System.out.println(ex.toString());
                }
                return null;
        }

        public static void updateComboBox(javax.swing.JComboBox comboBox, Vector newItems) {
                updateComboBox(comboBox,newItems, false);
        }
        public static void updateComboBox(javax.swing.JComboBox comboBox, Vector newItems, boolean addNullEntry) {
                Object itemSelected = null;
                if (comboBox.getSelectedIndex() >=0 ) {
                        itemSelected = comboBox.getSelectedItem();
                }

                //comboBox.removeAllItems();
                
                java.util.Vector items = new java.util.Vector(newItems.size(),1);
                boolean selected = false;
                boolean foundNullItem = false;
                Enumeration e = newItems.elements();
                int selectedIndex = -1;
                int currentelement = 0;
                while (e.hasMoreElements()) {
                        Object item = e.nextElement();
                        items.add(item);
                        if (item == itemSelected) {
                                selectedIndex = currentelement;
                        }
                        if (item.equals("")) {
                                foundNullItem = true;
                        }
                        
                        currentelement++;
                }

                if (addNullEntry) {
                        if (!foundNullItem) items.add(0,""); 
                        if (selectedIndex < 0) selectedIndex = 0;
                }
                
                comboBox.setModel( new DefaultComboBoxModel(items)  );
                comboBox.setSelectedIndex(selectedIndex);

        }

        /**   Mthis method perform equals based on string rapresentation of objects
         *
         */
        public static void updateStringComboBox(javax.swing.JComboBox comboBox, Vector newItems, boolean addNullEntry) {
                Object itemSelected = null;
                if (comboBox.getSelectedIndex() >=0 ) {
                        itemSelected = comboBox.getSelectedItem()+"";
                }

                //comboBox.removeAllItems();
                
                java.util.Vector items = new java.util.Vector(newItems.size(),1);
                
                boolean selected = false;
                boolean foundNullItem = false;
                Enumeration e = newItems.elements();
                int selectedIndex = -1;
                int currentelement = 0;
                
                while (e.hasMoreElements()) {
                        String item = ""+e.nextElement();
                        items.add(item);
                        //comboBox.addItem(item);
                        if (item.equals(itemSelected)) {
                                selectedIndex = currentelement;
                        }
                        if (item.equals("")) {
                                foundNullItem = true;
                        }
                        currentelement++;
                }

                if (addNullEntry) {
                        if (!foundNullItem) items.add(0,""); 
                        if (selectedIndex < 0) selectedIndex = 0;
                }
                
                comboBox.setModel( new DefaultComboBoxModel(items)  );
                comboBox.setSelectedIndex(selectedIndex);

        }


        public static String nvl(Object obj, String def) {
                return (obj == null) ? def : obj.toString();
        }

        public static void centerFrame(java.awt.Component c) {
                java.awt.Toolkit tk = java.awt.Toolkit.getDefaultToolkit();
                c.setLocation((int)((tk.getScreenSize().getWidth()-c.getWidth())/2),
                (int)((tk.getScreenSize().getHeight()- c.getHeight())/2) );
        }

        /**
         *    Replace s2 with s1 in s3
         **/
        public static String string_replace(String s1, String s2, String s3) {
                String string="";
                string = "";

                if (s2 == null || s3 == null || s2.length() == 0) return s3;

                int pos_i = 0; // posizione corrente.
                int pos_f = 0; // posizione corrente finale

                int len = s2.length();
                while ( (pos_f = s3.indexOf(s2, pos_i)) >= 0) {
                        string += s3.substring(pos_i,pos_f)+s1;
                        //+string.substring(pos+ s2.length());
                        pos_f = pos_i = pos_f + len;

                }

                string += s3.substring(pos_i);
                return string;
        }

        public static java.awt.Image loadImageFromFile(String path) {
                java.io.File file = new java.io.File(path);
                if (file.exists()) {
                        java.awt.Toolkit tk = java.awt.Toolkit.getDefaultToolkit();
                        java.awt.Image img = tk.createImage(path);
                        try {
                                java.awt.MediaTracker mt = new java.awt.MediaTracker( new javax.swing.JPanel() );
                                mt.addImage(img,0);
                                mt.waitForID(0);
                        } catch (Exception ex){
                                return null;
                        }
                        return img;
                }
                return null;
        }



        /**
         * This method inserts a blank character between to consecutive
         * newline characters if encoutered. Also appends a blank character at
         * the beginning of the text, if the first character is a newline character
         * and at the end of the text, if the last character is also a newline.
         * This is useful when trying to layout the paragraphs.
         * Thanks to Teodor Danciu for this this method (c) 2003 Teodor Danciu
         */
        public static String treatNewLineChars(String source) {
                String result = source;

                if (source != null && source.length() > 0) {
                        StringBuffer sbuffer = new StringBuffer(source);

                        // insert a blank character between every two consecutives
                        // newline characters
                        int offset = source.length() - 1;
                        int pos = source.lastIndexOf("\n\n", offset);
                        while (pos >= 0 && offset > 0) {
                                sbuffer = sbuffer.insert(pos + 1, " ");
                                offset = pos - 1;
                                pos = source.lastIndexOf("\n\n", offset);
                        }

                        // append a blank character at the and of the text
                        // if the last character is a newline character
                        if (sbuffer.charAt(sbuffer.length() - 1) == '\n') {
                                sbuffer.append(' ');
                        }

                        // append a blank character at the begining of the text
                        // if the first character is a newline character
                        if (sbuffer.charAt(0) == '\n') {
                                sbuffer.insert(0, ' ');
                        }

                        result = sbuffer.toString();
                }

                // remove this if you want to treat the tab characters in a special way
                result = replaceTabWithBlank(result);

                return result;
        }


        /**
         *  Thanks to Teodor Danciu for this method (c) 2003 Teodor Danciu
         */
        public static String replaceTabWithBlank(String source) {
                String result = source;

                if (source != null && source.length() > 0) {
                        StringBuffer sbuffer = new StringBuffer(source);

                        int offset = 0;
                        int pos = source.indexOf("\t", offset);
                        while (pos >= 0) {
                                sbuffer.setCharAt(pos, ' ');
                                offset = pos + 1;
                                pos = source.indexOf("\t", offset);
                        }

                        result = sbuffer.toString();
                }

                return result;
        }

        public static String toHTML(String s) {
                s = Misc.string_replace("&gt;",">",s);
                s = Misc.string_replace("&lt;","<",s);
                s = Misc.string_replace("&nbsp;"," ",s);
                s = Misc.string_replace("&nbsp;&nbsp;&nbsp;&nbsp;","\t",s);
                s = Misc.string_replace("<br>", "\n", s);
                return s;
        }

        static public String getShortFileName(String filename) {
                if (filename.length() > 50) {
                        java.io.File f = new java.io.File(filename);
                        if (nvl(f.getParentFile(),"").length()>10) {
                                String dir = f.getParentFile().getPath()+java.io.File.separatorChar;

                                String shortDir = dir.substring(0,dir.indexOf( java.io.File.separatorChar)+1);
                                dir = dir.substring(dir.indexOf( java.io.File.separatorChar)+1);
                                if (dir.indexOf( java.io.File.separatorChar) > 0) {
                                        shortDir += dir.substring(0, dir.indexOf( java.io.File.separatorChar)+1);
                                }
                                return shortDir + "..."+ java.io.File.separatorChar +
                                f.getName();
                        }
                }

                return filename;

        }


        /**
         * Thanx to Jackie Manning j.m@programmer.net for this method!!
         */
        public static String getJdbcTypeClass(java.sql.ResultSetMetaData rsmd, int t ) {
                String cls = "java.lang.Object";

                try {
                    cls = rsmd.getColumnClassName(t);
                    cls =  getJRFieldType(cls);

                } catch (Exception ex)
                {
                    // if getColumnClassName is not supported...
                    try {
                        int type = rsmd.getColumnType(t);
                        switch( type ) {
                                case java.sql.Types.TINYINT:
                                case java.sql.Types.BIT:
                                        cls = "java.lang.Byte";
                                        break;
                                case java.sql.Types.SMALLINT:
                                        cls = "java.lang.Short";
                                        break;
                                case java.sql.Types.INTEGER:
                                        cls = "java.lang.Integer";
                                        break;
                                case java.sql.Types.FLOAT:
                                case java.sql.Types.REAL:
                                case java.sql.Types.DOUBLE:
                                case java.sql.Types.NUMERIC:
                                case java.sql.Types.DECIMAL:
                                        cls = "java.lang.Double";
                                        break;
                                case java.sql.Types.CHAR:
                                case java.sql.Types.VARCHAR:
                                        cls = "java.lang.String";
                                        break;

                                case java.sql.Types.BIGINT:
                                        cls = "java.lang.Long";
                                        break;
                                case java.sql.Types.DATE:
                                        cls = "java.util.Date";
                                        break;
                                case java.sql.Types.TIME:
                                        cls = "java.sql.Time";
                                        break;
                                case java.sql.Types.TIMESTAMP:
                                        cls = "java.sql.Timestamp";
                                        break;
                        }
                    } catch (Exception ex2){
                        ex2.printStackTrace();
                    }
                }
                return cls;
        }


    /**
     * Return the correct field type...
     *
     */
    public static String getJRFieldType(String type)
    {

        if (type == null) return "java.lang.Object";

        if (type.equals("java.lang.Boolean") || type.equals("boolean")) return "java.lang.Boolean";
        if (type.equals("java.lang.Byte") || type.equals("byte")) return "java.lang.Byte";
        if (type.equals("java.lang.Integer") || type.equals("int")) return "java.lang.Integer";
        if (type.equals("java.lang.Long") || type.equals("long")) return "java.lang.Long";
        if (type.equals("java.lang.Double") || type.equals("double")) return "java.lang.Double";
        if (type.equals("java.lang.Float") || type.equals("float")) return "java.lang.Float";
        if (type.equals("java.lang.Short") || type.equals("short")) return "java.lang.Short";
        if (type.startsWith("[")) return "java.lang.Object";
        /*
        if (type.equals("java.util.Date") ||
            type.equals("java.sql.Timestamp") ||
            type.equals("java.io.InputStream") ||
            type.equals("java.math.BigDecimal") ||
            type.equals("java.lang.String") ||
            type.equals("java.sql.Time")) return type;

        return "java.lang.Object";
        */
        return type;
    }



        public static long getLastWriteTime(String filename) {
                try {
                        java.io.File f = new java.io.File(filename);
                        if (f.exists())
                        {
                            return f.lastModified();
                        }
                } catch (Exception ex) {
                        
                }
                return -1;
        }

        /**
         *Method used to grab the Frame which is above this component in the hierarchy.
         *This allows programmers to make any component the parent of any window or
         *dialog easier.
         *@param comp the component to get the Frame for
         *@return the Frame above this component in the hierarchy
         */
        public static java.awt.Frame frameFromComponent(java.awt.Component parent) {
                java.awt.Frame f = (java.awt.Frame)javax.swing.SwingUtilities.getAncestorOfClass(java.awt.Frame.class, parent);
                return f;
        }//end frameFromComponent
        //ErtanO 12.03.2004
        public static java.util.List getAvailablePLAF(){
                java.util.List l = new java.util.ArrayList();
                l.add("System");
                l.add("TinyLAF");
                l.add("TonicLAF");
                l.add("JGoodiesLAF-PlasticXP");
                l.add("JGoodiesLAF-Plastic");
                l.add("JGoodiesLAF-Plastic3D");
                l.add("JGoodiesLAF-ExtWindows");
                l.add("JGoodiesLAF-ExtWindows");
		//l.add("KunststofLAF");

                javax.swing.UIManager.LookAndFeelInfo[] lfinfo = javax.swing.UIManager.getInstalledLookAndFeels();

                for (int i=0; i<lfinfo.length; ++i) {
                        l.add( lfinfo[i].getName() );
                }

                return l;
        }
        public static void setPLAF(String s){
                try {
                        
                        if(s.equals("TinyLAF")) {
                                javax.swing.UIManager.setLookAndFeel("de.muntjak.tinylookandfeel.TinyLookAndFeel");
                        } else if(s.equals("TonicLAF")) {
                                javax.swing.UIManager.setLookAndFeel("com.digitprop.tonic.TonicLookAndFeel");
                        } else if(s.equals("JGoodiesLAF-PlasticXP")) {
                                javax.swing.UIManager.setLookAndFeel("com.jgoodies.looks.plastic.PlasticXPLookAndFeel");
                        } else if(s.equals("JGoodiesLAF-Plastic")) {
                                javax.swing.UIManager.setLookAndFeel("com.jgoodies.looks.plastic.PlasticLookAndFeel");
                        } else if(s.equals("JGoodiesLAF-Plastic3D")) {
                                javax.swing.UIManager.setLookAndFeel("com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
                        } else if(s.equals("JGoodiesLAF-ExtWindows")) {
                                javax.swing.UIManager.setLookAndFeel("com.jgoodies.looks.windows.WindowsLookAndFeel");
                        //} else if(s.equals("KunststofLAF")) {
                        //        javax.swing.UIManager.setLookAndFeel("com.incors.plaf.kunststoff.KunststoffLookAndFeel");
                        } else if(s.equals("System")) {
                                javax.swing.UIManager.setLookAndFeel( javax.swing.UIManager.getSystemLookAndFeelClassName() );

                                UIDefaults uiDefaults = UIManager.getDefaults();
                                Object obj = uiDefaults.get("Panel.background");
                                if (obj instanceof javax.swing.plaf.ColorUIResource)
                                {
                                	javax.swing.plaf.ColorUIResource cr = (javax.swing.plaf.ColorUIResource)obj;
                                	uiDefaults.put("Desktop.background", new javax.swing.plaf.ColorUIResource(cr.darker()));
				}
                        } else {
                                javax.swing.UIManager.LookAndFeelInfo[] lfinfo = javax.swing.UIManager.getInstalledLookAndFeels();
                                for (int i=0; i<lfinfo.length; ++i) {
                                        if (lfinfo[i].getName().equalsIgnoreCase( s )) {
                                                javax.swing.UIManager.setLookAndFeel(  lfinfo[i].getClassName() );

                                                if (s.equals("Metal"))
                                                {
                                                    if (MainFrame.getMainInstance().getProperties().getProperty("overrideDefaultFont","true").equals("true"))
                                                    {

                                                        String fontFamily = MainFrame.getMainInstance().getProperties().getProperty("overrideDefaultFontName", I18n.getString("defaultFont","Tahoma"));

                                                        int fontSize = 11;
                                                        try {
                                                            fontSize = Integer.parseInt( MainFrame.getMainInstance().getProperties().getProperty("overrideDefaultFontSize","11"));
                                                        } catch (Exception ex)
                                                        {
                                                            ex.printStackTrace();
                                                        }
                                                        String sNum = MainFrame.getMainInstance().getProperties().getProperty("overrideDefaultFontAttrs","0");
                                                        if (sNum == null || !sNum.equals("1")) sNum = "0";
                                                        int fontAttrs = Integer.parseInt(sNum);

                                                        javax.swing.plaf.FontUIResource f = new javax.swing.plaf.FontUIResource(fontFamily,fontAttrs,fontSize);
                                                        java.util.Enumeration keys = javax.swing.UIManager.getDefaults().keys();
                                                        while (keys.hasMoreElements()) {
                                                            Object key = keys.nextElement();
                                                            Object value = javax.swing.UIManager.get (key);
                                                            if (value instanceof javax.swing.plaf.FontUIResource)
                                                            javax.swing.UIManager.put (key, f);
                                                        }
                                                    }
                                                }
                                                return;
                                        }
                                }
                        }
                        
                } catch (Exception ex) {
                        ex.printStackTrace();
                }
        }

        public static String getClassPath() {
                String cp = (String)System.getProperty("java.class.path");
                if (it.businesslogic.ireport.gui.MainFrame.getMainInstance() != null)
                {
                    Vector cp_v = it.businesslogic.ireport.gui.MainFrame.getMainInstance().getClasspath();
                    for (int i=0; i<cp_v.size(); ++i)
                    {
                        cp += File.pathSeparator + cp_v.elementAt(i);
                    }
                }
                return cp;
        }


        /**
         * Enumerates the resouces in a give package name.
         * This works even if the resources are loaded from a jar file!
         *
         * Adapted from code by mikewse
         * on the java.sun.com message boards.
         * http://forum.java.sun.com/thread.jsp?forum=22&thread=30984
         *
         * @param packageName The package to enumerate
         * @return A Set of Strings for each resouce in the package.
         */
        public static Set getResoucesInPackage(String packageName) throws IOException {
                String localPackageName;
                if( packageName.endsWith("/") ) {
                        localPackageName = packageName;
                } else {
                        localPackageName = packageName + '/';
                }

                ClassLoader cl = Misc.class.getClassLoader();
                if (MainFrame.getMainInstance() != null)
                {
                    cl = MainFrame.getMainInstance().getReportClassLoader();
                }
                
                Enumeration dirEnum = cl.getResources( localPackageName );
                
                Set names = new HashSet();

                // Loop CLASSPATH directories
                while( dirEnum.hasMoreElements() ) {
                        URL resUrl = (URL) dirEnum.nextElement();
                       
                        // Pointing to filesystem directory
                        if ( resUrl.getProtocol().equals("file") ) {
                            try {
                              File dir = new File( resUrl.getFile() );
                                File[] files = dir.listFiles();
                                if ( files != null ) {
                                        for( int i=0; i<files.length; i++ ) {
                                                File file = files[i];
                                                if ( file.isDirectory() )
                                                        continue;
                                                names.add( localPackageName + file.getName() );
                                        }
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            
                                // Pointing to Jar file
                        } else if ( resUrl.getProtocol().equals("jar") ) {
                                JarURLConnection jconn = (JarURLConnection) resUrl.openConnection();
                                JarFile jfile = jconn.getJarFile();
                                Enumeration entryEnum = jfile.entries();
                                while( entryEnum.hasMoreElements() ) {
                                        JarEntry entry = (JarEntry) entryEnum.nextElement();
                                        String entryName = entry.getName();
                                        // Exclude our own directory
                                        if ( entryName.equals(localPackageName) )
                                                continue;
                                        String parentDirName = entryName.substring( 0, entryName.lastIndexOf('/')+1 );
                                        if ( ! parentDirName.equals(localPackageName) )
                                                continue;
                                        names.add( entryName );
                                }
                        } else {
                                // Invalid classpath entry
                        }
                }

                return names;
        }


        /**
         *  Take a filename, strip out the extension and append the new extension
         *  newExtension =   ".xyz"  or "xyz"
         *  If filename is null, ".xyz" is returned
         */
        public static String changeFileExtension(String filename, String newExtension ) {
                if (!newExtension.startsWith(".")) newExtension = "."+newExtension;
                if (filename == null || filename.length()==0 ) {
                        return newExtension;
                }

                int index = filename.lastIndexOf(".");
                if (index >= 0) {
                        filename = filename.substring(0,index);
                }
                return filename += newExtension;
        }


        /**
         *  Take a string like _it_IT or it_IT or it
         *  and return the right locale
         *  Default return value is Locale.getDefault()
         */
        static public java.util.Locale getLocaleFromString( String localeName )
        {
            return getLocaleFromString(localeName, Locale.getDefault() );
        }

        /**
         *  Take a string like _it_IT or it_IT or it
         *  and return the right locale
         *  
         */
        static public java.util.Locale getLocaleFromString( String localeName, Locale defaultLocale )
	{
		String language = "";
		String country = "";
		String variant = "";
		Locale locale = defaultLocale;

		if (localeName == null || localeName.length() == 0) return locale;
		if (localeName.startsWith("_")) localeName = localeName.substring(1);
		if (localeName.indexOf("_") > 0)
		{
			language = localeName.substring(0,localeName.indexOf("_"));
			localeName = localeName.substring(localeName.indexOf("_")+1);

			if (localeName.indexOf("_") > 0)
			{
				country = localeName.substring(0,localeName.indexOf("_"));
				localeName = localeName.substring(localeName.indexOf("_")+1);

				if (localeName.indexOf("_") > 0)
				{
				    variant = localeName.substring(0,localeName.indexOf("_"));
				    localeName = localeName.substring(localeName.indexOf("_")+1);
				}
				else
				{
				    variant = localeName;
				}
			}
			else
			{
				country = localeName;
			}
		}
		else
		{
			language = localeName;
		}

		locale = new Locale(language,country,variant);

		return locale;
	}


        public static void setComboboxSelectedTagValue(javax.swing.JComboBox comboBox, String itemValue) {
            for (int i=0; i<comboBox.getItemCount(); ++i)
            {
                Object val = comboBox.getItemAt(i);
                if ( val instanceof it.businesslogic.ireport.gui.sheet.Tag)
                {
                    if (((it.businesslogic.ireport.gui.sheet.Tag)val).getValue().equals(itemValue))
                    {
                        comboBox.setSelectedIndex( i );
                        break;
                    }
                }
            }
        }
        

        /**
         * Return the named connection configured in iReport
         * Return a  java.sql.Connection;
         */
        public static java.sql.Connection getConnection(String name)
        {
            Vector v = MainFrame.getMainInstance().getConnections();
            for (int i=0; i<v.size(); ++i)
            {
                IReportConnection irc = (IReportConnection)v.get(i);
                if (irc.getName().equals(name))
                {
                    return irc.getConnection();
                }
            }

            return null;
        }

        /**
         * Return the named connection configured in iReport
         * Return a  net.sf.jasperreports.engine.getJRDataSource
         */
        public static net.sf.jasperreports.engine.JRDataSource getJRDataSource(String name)
        {
            Vector v = MainFrame.getMainInstance().getConnections();
            for (int i=0; i<v.size(); ++i)
            {
                IReportConnection irc = (IReportConnection)v.get(i);
                if (irc.getName().equals(name))
                {
                    return irc.getJRDataSource();
                }
            }

            return null;
        }

        public static Vector loadStyleLibrary(String fileName)
        {
            Vector v = new Vector();
            try {

                File f = new File(fileName);
                InputStream fis = null;

                if (!f.exists())
                {
                    fis = Misc.class.getClassLoader().getResourceAsStream("it/businesslogic/ireport/res/defaultStyleLibrary.xml");
                    fileName =  ""+Misc.class.getClassLoader().getResource("it/businesslogic/ireport/res/defaultStyleLibrary.xml");
                }
                else
                {
                    fis = new FileInputStream(f);
                    fileName =  "file:///"+fileName;
                }

                v.addAll( loadStyleLibrary( fis, fileName));
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }

            return v;
        }

         public static Vector loadStyleLibrary(InputStream is, String filename)
         {
             Vector v = new Vector();
             try {
                 DOMParser parser = new DOMParser();
                 org.xml.sax.InputSource input_sss  = new org.xml.sax.InputSource(is);
                 //input_sss.setSystemId(filename);
                 parser.parse( input_sss );

                 Document document = parser.getDocument();
                 Node node = document.getDocumentElement();


                 NodeList list_child = node.getChildNodes();
                 for (int ck=0; ck< list_child.getLength(); ck++) {
                     Node styleNode = (Node)list_child.item(ck);
                     if (styleNode.getNodeName() != null && styleNode.getNodeName().equals("style"))
                     {
                        Style s = readStyle(styleNode,null);
                        v.add( s );
                    }
                 }
             } catch (Exception ex)
             {
                 ex.printStackTrace();
             }

             return v;
         }

         /**
          * If a ConditionedStyle, the style is interpreted like part of the tag conditionalStyle
          */
         public static it.businesslogic.ireport.Style readStyle(Node styleNode, ConditionedStyle cStyle)
         {
            Style style = new Style();
            if (cStyle != null) style = cStyle;
            NamedNodeMap nnm = styleNode.getAttributes();

            for (int i=0; i<Style.JRXMLStyleAttributes.length; ++i)
            {
                 if ( nnm.getNamedItem(Style.JRXMLStyleAttributes[i]) != null) {
                    style.getAttributes().put(Style.JRXMLStyleAttributes[i], nnm.getNamedItem(Style.JRXMLStyleAttributes[i]).getNodeValue());
                 }
            }

            //conditionalStyle
            // Check for description and expression...
            NodeList children = styleNode.getChildNodes();
            if (children != null) {
                for (int k=0; k< children.getLength(); k++) {
                    Node nodeChild = (Node)children.item(k);
                    if (nodeChild.getNodeType() == Node.ELEMENT_NODE && nodeChild.getNodeName().equals("conditionalStyle")) {
                        ConditionedStyle childStyle = readConditionalStyle(nodeChild);
                        style.getConditionedStyles().add(childStyle);
                    }
                }
            }

            return style;
         }

         /**
     * If a ConditionedStyle, the style is interpreted like part of the tag conditionalStyle
     */
    private static it.businesslogic.ireport.ConditionedStyle readConditionalStyle(Node styleNode)
    {
        ConditionedStyle style = new ConditionedStyle();

        //conditionalStyle
        // Check for description and expression...
        NodeList children = styleNode.getChildNodes();
        if (children != null) {
            for (int k=0; k< children.getLength(); k++) {
                Node nodeChild = (Node)children.item(k);
                if (nodeChild.getNodeType() == Node.ELEMENT_NODE && nodeChild.getNodeName().equals("conditionExpression")) {
                    style.setCondition(Report.readPCDATA(nodeChild));
                }
                else if (nodeChild.getNodeType() == Node.ELEMENT_NODE && nodeChild.getNodeName().equals("style")) {
                    style = (ConditionedStyle)readStyle(nodeChild, style);
                }
            }
        }

        return style;
    }

    public static void saveStyleLibrary(String filename, Vector styles)
    {
        try {
        PrintWriter pw = new PrintWriter( new java.io.OutputStreamWriter( new java.io.FileOutputStream( filename ), "UTF8" )); //UTF8

        pw.print("<?xml version=\"1.0\"?>");
        pw.println("<!-- iReport styles library -->");
        pw.println("<styles>");

        writeXMLStyles(styles, pw, "\t");
        pw.println("</styles>");

        pw.close();

        } catch (Exception ex)
        {
            JOptionPane.showMessageDialog(MainFrame.getMainInstance(),
                    I18n.getFormattedString( "messages.misc.errorSavingStyles",
                    "Error saving styles library: {0}",
                    new Object[]{ ex.getMessage() }) );
            ex.printStackTrace();
        }
    }


    private static void writeXMLStyles(Vector styles, java.io.PrintWriter pw, String tabs)
    {

            Enumeration e = styles.elements();
            if (styles.size() > 0) pw.println("");
            while (e.hasMoreElements()) {
                it.businesslogic.ireport.Style style = (it.businesslogic.ireport.Style)e.nextElement();

                String tabs2 = tabs;
                if (style instanceof ConditionedStyle)
                {
                    pw.println(tabs2 + "<conditionalStyle>");
                    tabs += "\t";
                    pw.print(tabs + "<conditionExpression");
                    pw.println(">" + Report.getCDATAString(((ConditionedStyle)style).getCondition() ,tabs2.length()+1) +"</conditionExpression>");
                 }
                pw.println(tabs + "<style ");
                for (int i=0; i<Style.JRXMLStyleAttributes.length; ++i)
                {
                    if ( style.getAttributes().containsKey(Style.JRXMLStyleAttributes[i]) &&
                         style.getAttributes().get( Style.JRXMLStyleAttributes[i] ) != null) {
                         Object obj = style.getAttributes().get(Style.JRXMLStyleAttributes[i]);
                         String value = ""+obj;
                         if (obj instanceof java.awt.Color)
                         {
                             value = Report.writeEncodedColor( (java.awt.Color)obj);
                         }
                         if (Style.JRXMLStyleAttributes[i].toLowerCase().endsWith("color") && value.startsWith("["))
                         {
                             // The color is in the form [r,g,b]...
                             try {
                               value =   Report.writeEncodedColor(it.businesslogic.ireport.gui.sheet.ColorSelectorPanel.parseColorString(value));
                             } catch (Exception ex)
                             {
                                 value="black";
                             }
                         }
                         if (Style.JRXMLStyleAttributes[i].equals("style"))
                         {
                             if (value == null || value.trim().length() == 0) continue;
                         }
                         pw.println(tabs + "\t" + Style.JRXMLStyleAttributes[i] + "=\"" + value +"\"");
                    }
                }

                if (style.getConditionedStyles().size() == 0)
                {
                    pw.println(tabs + "/>");
                }
                else
                {
                    pw.println(tabs + ">");
                    writeXMLStyles(style.getConditionedStyles(), pw, tabs+"\t");
                    pw.println(tabs + "</style>");
                }
                if (style instanceof ConditionedStyle)
                {
                    pw.println(tabs2 + "</conditionalStyle>");
                }
            }
    }


     /**
     * Save the query asking for a file.
     * see saveSQLQuery(String query, Component c)
     */
    public static boolean saveSQLQuery(String query)
    {
         return saveSQLQuery(query, MainFrame.getMainInstance());
    }
    /**
     * Save the query asking for a file.
     * The optional component is used as parent for the file selection dialog
     * Default is the MainFrame
     */
    public static boolean saveSQLQuery(String query, Component c)
    {
            JFileChooser jfc = new JFileChooser();
            jfc.setFileFilter( new javax.swing.filechooser.FileFilter() {
		    public boolean accept(java.io.File file) {
			    String filename = file.getName().toLowerCase();
			    return (filename.endsWith(".sql") || filename.endsWith(".txt") ||file.isDirectory()) ;
		    }
		    public String getDescription() {
			    return "SQL query (*.sql, *.txt)";
		    }
	    });

	    if (jfc.showSaveDialog(c) == JFileChooser.APPROVE_OPTION) {

                try {

                    String fileName = jfc.getSelectedFile().getName();
                    if (fileName.indexOf(".") < 0)
                    {
                        fileName += ".sql";
                    }

                    File f = new File( jfc.getSelectedFile().getParent(), fileName);

                    FileWriter fw = new FileWriter(f);
                    fw.write( query );
                    fw.close();

                    return true;
                } catch (Exception ex)
                {
                    JOptionPane.showMessageDialog(c,"Error saving the query: " + ex.getMessage(),I18n.getString("message.title.error","Error"),JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
	    }

            return false;
    }

    /**
     * See loadSQLQuery(Component c)
     */
    public static String loadSQLQuery()
    {
        return loadSQLQuery(MainFrame.getMainInstance());
    }

    /**
     * Load the query asking for a file.
     * The optional component is used as parent for the file selection dialog
     * Default is the MainFrame
     */
    public static String loadSQLQuery(Component c)
    {
            JFileChooser jfc = new JFileChooser();
            jfc.setMultiSelectionEnabled(false);
            jfc.setFileFilter( new javax.swing.filechooser.FileFilter() {
		    public boolean accept(java.io.File file) {
			    String filename = file.getName().toLowerCase();
			    return (filename.endsWith(".sql") || filename.endsWith(".txt") ||file.isDirectory()) ;
		    }
		    public String getDescription() {
			    return "SQL query (*.sql, *.txt)";
		    }
	    });

	    if (jfc.showOpenDialog(c) == JFileChooser.APPROVE_OPTION) {

                try {

                    FileReader fr = new FileReader(jfc.getSelectedFile());
                    StringBuffer sb = new StringBuffer();
                    char[] cbuf = new char[1024];
                    int i = fr.read(cbuf);
                    while (i > 0)
                    {
                        sb.append( cbuf, 0, i);
                        i = fr.read(cbuf);
                    }
                    fr.close();

                    return sb.toString();
                } catch (Exception ex)
                {
                    JOptionPane.showMessageDialog(c,"Error loading the query: " + ex.getMessage(),I18n.getString("message.title.error","Error"),JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
	    }

            return null;
    }
    
    
    /**
     * Save the expressions list to the specified file...
     */
    public static boolean saveExpressionsList( Vector v, String xmlFile) {
        // Get the path of this class...
        // this.getClass().getResource("MainFrame");
        try {
            PrintWriter pw = new PrintWriter( new FileWriter(xmlFile) );
            // Find iReport configuration in the home directory...
            // 1. Save all properties using am XML style...
            pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            pw.println("<!-- iReport 2 expressions list - " + new Date() + " -->");
            pw.println("<!DOCTYPE iReportExpressionsList PUBLIC \"-//iReport/DTD iReport Configuration//EN\" \"http://ireport.sourceforge.net/dtds/iReportExpressionsList.dtd\">");
            pw.println("<iReportExpressionsList>");

            Enumeration e = v.elements();

            while (e.hasMoreElements()) {
                String exp = (String)e.nextElement();
                pw.println("\t<expression><![CDATA["+ exp +"]]></expression>");
            }
            pw.println("</iReportExpressionsList>");
            pw.close();
        } catch (Exception ex) {
            //ex.printStackTrace(s
            try {
                MainFrame.getMainInstance().logOnConsole("Error saving iReport file: "+xmlFile+"\n"+ex.getMessage()+"\n");

            } catch (Exception exsx)
            {}
            return false;
        }
        try {
            MainFrame.getMainInstance().logOnConsole("iReport file "+ xmlFile + " successfully updated!\n");
        } catch (Exception ex)
        { }
        return true;
    }
    
    /**
     * Read the expressions list from the specified file...
     */
    public static Vector loadExpressionsList(String xmlFile) {
        File file = null;
        Vector v = new Vector();
        try {
            file = new File(xmlFile);
        } catch (Exception ex)
        {}
        if (file == null || !file.exists() || file.isDirectory()) {

            return ExpressionEditor.defaultExpressions;
        }
        //  Create a Xerces DOM Parser
        DOMParser parser = new DOMParser();
        //  Parse the Document
        //  and traverse the DOM
        try {

            parser.setEntityResolver( new org.xml.sax.EntityResolver() {
                /* Code by Teodor Danciu */
                public org.xml.sax.InputSource resolveEntity(
                    String publicId,
                    String systemId
                ) throws SAXException//, java.io.IOException
                {
                    org.xml.sax.InputSource inputSource = null;

                    if (systemId != null) {
                        String dtd = null;

                        if (
                        systemId.equals("http://ireport.sourceforge.net/dtds/iReportExpressionsList.dtd")
                        ) {
                            dtd = "it/businesslogic/ireport/dtds/iReportExpressionsList.dtd";
                        }
                        else {
                            return new org.xml.sax.InputSource(systemId);
                        }

                        ClassLoader classLoader = this.getClass().getClassLoader();

                        java.net.URL url = null;


                        if (classLoader != null) {
                            url = classLoader.getResource(dtd);
                        }
                        if (url == null) {
                            classLoader = this.getClass().getClassLoader();
                        }

                        java.io.InputStream is = classLoader.getResourceAsStream(dtd);
                        if (is != null) {
                            java.io.InputStreamReader isr = new java.io.InputStreamReader(is);
                            inputSource = new org.xml.sax.InputSource(isr);
                        }

                    }

                    return inputSource;
                }
            });
            /* End Code by Teodor Danciu */
            parser.parse( new java.io.File(xmlFile).toURI().toString() );
            Document document = parser.getDocument();

            // Traverse the tree until we don't find a iReportFilesList element...
            System.out.println("FL3: " + document.getDocumentElement());
            Node fileList = document.getDocumentElement();
            //System.out.println("FL: " + fileList);
            if (fileList == null) return v;
            NodeList list = fileList.getChildNodes();
            for (int i=0; i < list.getLength(); ++i) {
                Node child = list.item(i);
                //System.out.println("FL: " + i + ">>" + child);
                if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("expression")) {
                    String expression = Report.readPCDATA(child);
                    if (expression != null && expression.trim().length()>0) {
                        v.addElement( expression );
                    }
                }
            }

        } catch (Exception ex) {
        }
        
        return v;
    }

/*
    public static Style getDefaultStyle(ReportElement re)
    {
        // 1. Look for the report to wich this element belong...
        JReportFrame frames = MainFrame.getMainInstance()
    }
    
    public static Report getReportByBand(Band b)
    {
        
    }
    
    public static Report getReportByReportElement(ReportElement re)
    {
        if (re instanceof CrosstabReportElement)
        {
            
        }
    }
  */  
    
    
   /**
    *     side can be:
    *        DockingConstants.NOTRH_REGION
    *        DockingConstants.EAST_REGION
    *        DockingConstants.SOUTH_REGION
    *        DockingConstants.WEST_REGION
    *
    *     index is the position into a side
    *     
    *     i.e.
    *         dockAt( dockable, DockingConstants.WEST_REGION, 0)   means the top left position
    *
    *
    */   
   static public boolean dockAt(Dockable toDock, String side, int index)
   {  
       Dockable rootDockable = MainFrame.getMainInstance().getDesktopView();
       Dockable dockable = rootDockable;
              
       String directionV = null;
       String directionH = null;
       String directionIndex = null;
       
       if (side.equals( DockingConstants.NORTH_REGION ))
       {
           directionV =  DockingConstants.NORTH_REGION;
           directionH = DockingConstants.WEST_REGION;
           directionIndex = DockingConstants.EAST_REGION;
       }
       if (side.equals( DockingConstants.WEST_REGION ))
       {
           directionV =  DockingConstants.NORTH_REGION;
           directionH = DockingConstants.WEST_REGION;
           directionIndex = DockingConstants.SOUTH_REGION;
       }
       if (side.equals( DockingConstants.EAST_REGION ))
       {
           directionV =  DockingConstants.NORTH_REGION;
           directionH = DockingConstants.EAST_REGION;
           directionIndex = DockingConstants.SOUTH_REGION;
       }
       if (side.equals( DockingConstants.SOUTH_REGION ))
       {
           directionV =  DockingConstants.SOUTH_REGION;
           directionH = DockingConstants.WEST_REGION;
           directionIndex = DockingConstants.EAST_REGION;
       }
       
       String counterDirectionIndex = null;
       if (directionIndex.equals( DockingConstants.NORTH_REGION) ) counterDirectionIndex = DockingConstants.SOUTH_REGION;
       if (directionIndex.equals( DockingConstants.SOUTH_REGION) ) counterDirectionIndex = DockingConstants.NORTH_REGION;
       if (directionIndex.equals( DockingConstants.WEST_REGION) ) counterDirectionIndex = DockingConstants.EAST_REGION;
       if (directionIndex.equals( DockingConstants.EAST_REGION) ) counterDirectionIndex = DockingConstants.WEST_REGION;
           
       
       
       try {
                if (((View)rootDockable).getSibling(side) != null)
                {
                    dockable = ((View)rootDockable).getSibling(side);
                    while (DefaultDockingStrategy.getSibling(dockable, directionV) != null ||
                           DefaultDockingStrategy.getSibling(dockable, directionH) != null )
                       {
                           Dockable tmp = DefaultDockingStrategy.getSibling(dockable, directionV);
                           dockable =  (tmp != null) ? tmp : DefaultDockingStrategy.getSibling(dockable, directionH);
                       }
                }
                    
           } catch (Throwable ex){
           
           ex.printStackTrace();
       } 
       
      
       
       if (dockable == rootDockable) // there are no siblings in side direction...
       {
           
           return dockable.dock(toDock, side);
       }
       
       
       // dockable is now the root...
       // Go in the indexDirection for index times (if possible)...
       int i = 0;
       for (i=0; i<index; ++i)
       {
           // Look for the right sibling..
           try {
           System.out.println("Index " + i); 
           if (DefaultDockingStrategy.getSibling(dockable, directionIndex) == null) break;
           dockable = DefaultDockingStrategy.getSibling(dockable, directionIndex);
           
           
           } catch (Throwable ex){
           
           ex.printStackTrace();
           }
       }
       
       if (i == index)
       {
           
           return dockable.dock(toDock, counterDirectionIndex);
       }
       else
       {
           return dockable.dock(toDock, directionIndex);
       }

   }
   
   
   
    /**
     * Save the expression asking for a file.
     * see saveSQLQuery(String query, Component c)
     */
    public static boolean saveExpression(String expression)
    {
         return saveExpression(expression, MainFrame.getMainInstance());
    }
    /**
     * Save the query asking for a file.
     * The optional component is used as parent for the file selection dialog
     * Default is the MainFrame
     */
    public static boolean saveExpression(String expression, Component c)
    {
            JFileChooser jfc = new JFileChooser();
            jfc.setFileFilter( new javax.swing.filechooser.FileFilter() {
		    public boolean accept(java.io.File file) {
			    String filename = file.getName().toLowerCase();
			    return (filename.endsWith(".txt") ||file.isDirectory()) ;
		    }
		    public String getDescription() {
			    return "Text file (*.txt)";
		    }
	    });

	    if (jfc.showSaveDialog(c) == JFileChooser.APPROVE_OPTION) {

                try {

                    String fileName = jfc.getSelectedFile().getName();
                    if (fileName.indexOf(".") < 0)
                    {
                        fileName += ".txt";
                    }

                    File f = new File( jfc.getSelectedFile().getParent(), fileName);

                    FileWriter fw = new FileWriter(f);
                    fw.write( expression );
                    fw.close();

                    return true;
                } catch (Exception ex)
                {
                    JOptionPane.showMessageDialog(c,"Error saving the expression: " + ex.getMessage(),I18n.getString("message.title.error","Error"),JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
	    }

            return false;
    }

    /**
     * See loadSQLQuery(Component c)
     */
    public static String loadExpression()
    {
        return loadExpression(MainFrame.getMainInstance());
    }

    /**
     * Load the query asking for a file.
     * The optional component is used as parent for the file selection dialog
     * Default is the MainFrame
     */
    public static String loadExpression(Component c)
    {
            JFileChooser jfc = new JFileChooser();
            jfc.setMultiSelectionEnabled(false);
            jfc.setFileFilter( new javax.swing.filechooser.FileFilter() {
		    public boolean accept(java.io.File file) {
			    String filename = file.getName().toLowerCase();
			    return (filename.endsWith(".txt") ||file.isDirectory()) ;
		    }
		    public String getDescription() {
			    return "Text file (*.txt)";
		    }
	    });

	    if (jfc.showOpenDialog(c) == JFileChooser.APPROVE_OPTION) {

                try {

                    FileReader fr = new FileReader(jfc.getSelectedFile());
                    StringBuffer sb = new StringBuffer();
                    char[] cbuf = new char[1024];
                    int i = fr.read(cbuf);
                    while (i > 0)
                    {
                        sb.append( cbuf, 0, i);
                        i = fr.read(cbuf);
                    }
                    fr.close();

                    return sb.toString();
                } catch (Exception ex)
                {
                    JOptionPane.showMessageDialog(c,"Error loading the expression: " + ex.getMessage(),I18n.getString("message.title.error","Error"),JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
	    }

            return null;
    }
    
    
    
   /** This method uses the code from: http://www.centerkey.com/java/browser/ 
     *  
     *  Bare Bones Browser Launch                          
     *  Version 1.5                                        
     *  December 10, 2005                                  
     *  Supports: Mac OS X, GNU/Linux, Unix, Windows XP    
     *  Example Usage:                                     
     *     String url = "http://www.centerkey.com/";       
     *     BareBonesBrowserLaunch.openURL(url);            
     *  Public Domain Software -- Free to Use as You Like  
     *
     **/
   public static void openURL(String url) {
             
      String osName = System.getProperty("os.name");
      try {
         if (osName.startsWith("Mac OS")) {
            Class fileMgr = Class.forName("com.apple.eio.FileManager");
            java.lang.reflect.Method openURL = fileMgr.getDeclaredMethod("openURL",
               new Class[] {String.class});
            openURL.invoke(null, new Object[] {url});
            }
         else if (osName.startsWith("Windows"))
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
         else { //assume Unix or Linux
            String[] browsers = {
               "firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape", "htmlview" };
            String browser = null;
            for (int count = 0; count < browsers.length && browser == null; count++)
            {
                if (Runtime.getRuntime().exec(new String[] {"which", browsers[count]}).waitFor() == 0)
                {
                    browser = browsers[count];
                }
            }
            if (browser == null)
               throw new Exception(I18n.getString("misc.browserNotFound","Could not find web browser"));
            else
               Runtime.getRuntime().exec(new String[] {browser, url});
            }
         }
      catch (Exception e) {
         JOptionPane.showMessageDialog(MainFrame.getMainInstance(), 
                 I18n.getString("misc.browserLunchError","Error attempting to launch web browser") + "\n" + e.getLocalizedMessage());
         }
      }

   /**
    * If the selected value is a Tag, tag.getValue is returned,
    * otherwise the selected item itself is returned.
    * If not item is selected, the method return null.
    */
   public static Object getComboboxSelectedValue(JComboBox combo)
   {
       Object result = null;
       if (combo != null)
       {
           result = combo.getSelectedItem();
           if (result != null && result instanceof Tag)
           {
               return ((Tag)result).getValue();
           }
       }
       return result;
   }
   
   /**
    * Creates fileName inside <user home>/classes/it/businesslogic/ireport/locale
    * and stores there the properties keys
    */
   public static void saveTemporaryLocale(java.util.Properties properties, String fileName)
   {
       if (properties == null) return;
       // Save in the user directory....
            String dir = MainFrame.getMainInstance().IREPORT_USER_HOME_DIR + File.separator +
                         "classes" + File.separator +
                         "it" + File.separator +
                         "businesslogic" + File.separator +
                         "ireport" + File.separator +
                         "locale";
            File fdir = new File(dir);
            if (!fdir.exists())
            {
                fdir.mkdirs();
                if (!fdir.exists())
                {
                    JOptionPane.showMessageDialog(null,I18n.getFormattedString("translationStatusDialog.messages.unableToCreateDir","Unable to create directory: {0}",new Object[]{""+fdir}));
                    return;
                }
            }
            try {
                File outFile = new File(fdir,fileName); 
                
                properties.store(new FileOutputStream(outFile),"");

            } catch (Exception ex)
            {
                ex.printStackTrace();
            }
  }
   
   
  /**
   * If treePath is not in the current jTree selection, set it as selected.
   *
   */
  public static void ensurePathIsSelected(TreePath treePath, JTree jTree)
  {
        if (jTree == null || treePath == null) return;
        
        TreePath[] selectedPaths = jTree.getSelectionPaths();
        for (int i=0; selectedPaths != null && i<selectedPaths.length; ++i)
        {
            if (selectedPaths[i].equals( treePath )) return;
        }
        jTree.setSelectionPath(treePath);
  }
  
  /**
   * This method select the whole text inside a textarea and set there the focus.
   * It should be used to select a component that contains a wrong expression.
   * In the future other properties of the componenct can be modified
   */
  public static void selectTextAndFocusArea(final JComponent expArea)
  {
      if (expArea == null) return;
      
      if (expArea instanceof JRTextExpressionArea)
      {
        ((JRTextExpressionArea)expArea).setSelectionStart(0);
        ((JRTextExpressionArea)expArea).setSelectionEnd(  ((JRTextExpressionArea)expArea).getText().length() );
        ((JRTextExpressionArea)expArea).setHasErrors(true); 
      }
      else if (expArea instanceof JTextComponent)
      {
        ((JTextComponent)expArea).setSelectionStart(0);
        ((JTextComponent)expArea).setSelectionEnd(  ((JTextComponent)expArea).getText().length() );
        ((JTextComponent)expArea).setBorder(new LineBorder(Color.RED.darker(),2));
      }
      
      //Border b = expArea.getBorder();
      
      expArea.requestFocusInWindow();
      /*
      SwingUtilities.invokeLater( new Runnable() {
          public void run() {
              
              Graphics2D g = (Graphics2D)expArea.getGraphics();
              Stroke s = g.getStroke();
              g.setStroke( ReportElement.getPenStroke("4Point",1.0));
              g.setColor( ReportElement.getAlphaColor(Color.RED, 128)  );        
              g.drawRect(0,0,expArea.getWidth(), expArea.getHeight() );
              g.setStroke(s);
          }
      });
      */
  }

    /**
     * Find the DefaultMutableTreeNode containing the userObject as UserObject
     * 
     * Returns null if node == null or userObject == null
     */
    public static DefaultMutableTreeNode findNodeWithUserObject(Object userObject, javax.swing.tree.TreeNode node)
    {
        if (node == null || userObject == null)
        {
            return null;
        }
        
        if (node instanceof DefaultMutableTreeNode)
        {
            DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)node;
            if (dmtn.getUserObject() != null &&
                dmtn.getUserObject().equals(userObject)) return dmtn;
        }
        
        // look in the children...
        for (int i=0; i<node.getChildCount(); ++i)
        {
            DefaultMutableTreeNode dmtn = findNodeWithUserObject( userObject, node.getChildAt(i));
            if (dmtn != null) return dmtn;
        }
        
        return null;
            
    }
    
    
    /**
     * Add the properties in resourceUri to props.
     * If resourceUri does not exists, nothing happen.
     *
     */
    public static void addProperties(String resourceUri, java.util.Properties props)
    {
        try {
            
            InputStream is = Misc.class.getResourceAsStream(resourceUri);
            if (is == null) return;
            props.load( is );
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }      
    }
    
    /**
     * Get the content of a text resource file...
     *
     */
    public static String getResourceContent(String resourceUri)
    {
        String content ="";
        if (resourceUri == null) return content;
        try {
            
            InputStream is = Misc.class.getResourceAsStream(resourceUri);
            if (is == null) return content;
        
            LineNumberReader lnr = new LineNumberReader(new InputStreamReader(is) );
            String line = null;
            boolean first = true;
            while ( (line =  lnr.readLine()) != null)
            {
                if (!first) content += "\n";
                content += line;
                first = false;
            }
            
            lnr.close();
            
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }      
        
        return content;
    }
    
    

    public static HashMap opTimes = new HashMap();

    /**
     * This function is used for debug porpuses. It is used to misure the time
     * of an operation.
     * Sample usage:
     * <pre><code>
     *   ....some code
     *   Misc.optime("My operation");  // Start the timer
     *   ....block to measure....
     *   Misc.optime("My operation");  // Stop the timer
     * </code></pre>  
     *  Result on video:
     * <pre><code>
     *    My operation START (1)
     *    My operation END (1)	20ms
     * </code></pre>
     */
    public static void optime(String opName) {
        long t = new java.util.Date().getTime();
        if (opTimes.containsKey( opName ))
        {
            long t0 = ((Long)opTimes.get(opName)).longValue();
            long opCounter = ((Long)opTimes.get(opName+"_coutner" )).longValue();
            
            opTimes.remove(opName);
            System.out.println(opName + " END (" + opCounter + ")\t" +  (t - t0) + "ms"); 
            System.out.flush();
            
            
        }
        else
        {
            long opCounter = 0;
            opTimes.put(opName, new Long(t) );
            if (opTimes.containsKey( opName+"_coutner" ))
            {
                opCounter = ((Long)opTimes.get(opName+"_coutner" )).longValue();
            }
            opCounter++;
            opTimes.put(opName+"_coutner", new Long(opCounter));
            System.out.println(opName + " START (" + opCounter + ")");
            System.out.flush();
        }
    }
  
    
    /**
     *  This method validates URL like:
     *
     *  123.123.123[:port]
     *  domain.domain.dom[:port]
     *
     */
    public static boolean isValidUrl(String url) 
    { 
        String strRegex = 
         "((([0-9]{1,3}\\.){3})[0-9]{1,3})" + // IP- 199.194.52.184 
         "|" + // allows either IP or domain 
         "(([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\.?)++" + // domain pice 
         "(:[0-9]{1,4})?"; // port number- :80 
         
         return url.matches( strRegex );
    } 
    
    
    /**
     *  Look for the SubDataset of an Object in the given report / subDataset.
     *  If the subDataset is a report, it looks recursively in each subdataset...
     * @param report Report or SubDataset to look in
     * @param object Object to search for (Parameter, Field, Variable)
     * 
     * @return a subdataset or null if the object is not found
     */
    public static SubDataset getObjectSubDataset(SubDataset report, Object object)
    {
        if (report.getParameters().contains(object)) return report;
        if (report.getFields().contains(object)) return report;
        if (report.getVariables().contains(object)) return report;
        
        if (report instanceof Report)
        {
            for (int i=0; i<((Report)report).getSubDatasets().size(); ++i)
            {
                SubDataset s = (SubDataset)((Report)report).getSubDatasets().get(i);
                if (getObjectSubDataset(s , object) != null)
                {
                    return s;
                }
            }
        }
        
        return null;
    }
}//end class Misc
