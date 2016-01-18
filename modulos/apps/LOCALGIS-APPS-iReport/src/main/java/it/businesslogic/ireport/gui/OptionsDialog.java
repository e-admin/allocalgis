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
 * OptionsDialog.java
 *
 * Created on 1 giugno 2003, 14.22
 *
 */

package it.businesslogic.ireport.gui;
import it.businesslogic.ireport.ReportElement;
import it.businesslogic.ireport.gui.event.SheetPropertyValueChangedEvent;
import it.businesslogic.ireport.gui.event.SheetPropertyValueChangedListener;
import it.businesslogic.ireport.gui.sheet.CategorySheetPanel;
import it.businesslogic.ireport.gui.sheet.ColorSelectorPanel;
import it.businesslogic.ireport.gui.sheet.HexColorChooserPanel;
import it.businesslogic.ireport.gui.sheet.NumberComboBoxSheetProperty;
import it.businesslogic.ireport.gui.sheet.SheetProperty;
import it.businesslogic.ireport.gui.sheet.Tag;
import it.businesslogic.ireport.util.I18n;
import it.businesslogic.ireport.util.LanguageChangedEvent;
import it.businesslogic.ireport.util.LanguageChangedListener;
import it.businesslogic.ireport.util.LocaleAdapter;
import it.businesslogic.ireport.util.Misc;

import java.awt.Color;
import java.io.File;
import java.util.Locale;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.SwingUtilities;
/**
 *
 * @author  Administrator
 */
public class OptionsDialog extends javax.swing.JDialog implements LanguageChangedListener, SheetPropertyValueChangedListener {

	MainFrame mf = null;
	it.businesslogic.ireport.util.Unit[] units;
	java.util.List listOfLanguages;
	java.util.List listOfPLAF;

        private CategorySheetPanel categorySheetPanel = null;

	/** Creates new form OptionsDialog */
	public OptionsDialog(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
		this.mf = (MainFrame)parent;
		initComponents();
		javax.swing.event.DocumentListener listener =  new javax.swing.event.DocumentListener()
                {
                    public void changedUpdate(javax.swing.event.DocumentEvent evt)
                    {
                        enableApplyAndOk();
                    }
                    public void insertUpdate(javax.swing.event.DocumentEvent evt)
                    {
                        enableApplyAndOk();
                    }
                    public void removeUpdate(javax.swing.event.DocumentEvent evt)
                    {
                        enableApplyAndOk();
                    }
                };

                this.jTextFieldBackupDirectory.getDocument().addDocumentListener(listener);
                this.jTextFieldCSVViewer.getDocument().addDocumentListener(listener);
                this.jTextFieldCompilationDir.getDocument().addDocumentListener(listener);
                this.jTextFieldEditor.getDocument().addDocumentListener(listener);
                this.jTextFieldHTMLViewer.getDocument().addDocumentListener(listener);
                this.jTextFieldPDFViewer.getDocument().addDocumentListener(listener);
                this.jTextFieldTemplate.getDocument().addDocumentListener(listener);
                this.jTextFieldXLSViewer.getDocument().addDocumentListener(listener);
                this.jTextFieldVirtualizerDir.getDocument().addDocumentListener(listener);
                this.jTextFieldTXTViewer.getDocument().addDocumentListener(listener);
                this.jTextFieldRTFViewer.getDocument().addDocumentListener(listener);
                this.jTextFieldODFViewer.getDocument().addDocumentListener(listener);



                jComboBoxVirtualizer.addItem( new Tag("JRFileVirtualizer", I18n.getString("virtualizer.file","File virtualizer") ));
                jComboBoxVirtualizer.addItem( new Tag("JRSwapFileVirtualizer",I18n.getString("virtualizer.swap","Single swap/file virtualizer")));
                jComboBoxVirtualizer.addItem( new Tag("JRGzipVirtualizer",I18n.getString("virtualizer.gzip","GZIP in-memory virtualizer")));

                units = it.businesslogic.ireport.util.Unit.getStandardUnits();

                jSpinnerVirtualizerSize.setModel(new javax.swing.SpinnerNumberModel(100,1,100000000,10));
                jSpinnerVirtualizerBlockSize.setModel(new javax.swing.SpinnerNumberModel(100,1,100000000,10));
                jSpinnerVirtualizerGrownCount.setModel(new javax.swing.SpinnerNumberModel(1,1,100000000,10));

                // Apply locale....
                //System.out.println("Apply i18n cc...");
                //applyI18n();
                I18n.addOnLanguageChangedListener( this );


                categorySheetPanel = new CategorySheetPanel();
                categorySheetPanel.setShowResetButton(false);

                createSheet();
                applyI18n();
                jPanel8.add("Center", categorySheetPanel);


                javax.swing.KeyStroke escape =  javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false);
                javax.swing.Action escapeAction = new javax.swing.AbstractAction() {
                    public void actionPerformed(java.awt.event.ActionEvent e) {
                        jButtonCancelActionPerformed(e);
                    }
                };

                getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, "ESCAPE");
                getRootPane().getActionMap().put("ESCAPE", escapeAction);


                //to make the default button ...
                this.getRootPane().setDefaultButton(this.jButtonOK);


                pack();

        }


        private void createSheet()
        {
            if (categorySheetPanel == null) return;
            // Adding all properties...
            categorySheetPanel.removeAllProperties();
                String category_name =  it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.TabGeneral","General");
                categorySheetPanel.addSheetProperty(category_name, new SheetProperty("RecentFilesLength",it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.LabelRecentFileListLength","Num of recent files in file menu"), SheetProperty.INTEGER));
                units = it.businesslogic.ireport.util.Unit.getStandardUnits();
		SheetProperty sp = new SheetProperty("DefaultUnit",it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.LabelDefaultUnit","Default unit"), SheetProperty.COMBOBOX);
                Vector tags = new Vector();
                for (int i=0; i< units.length; ++i) {
                    Tag t = new Tag(units[i].getKeyName(), units[i]+"");
                    tags.add(t);
		}
                sp.setTags( tags );
                categorySheetPanel.addSheetProperty(category_name, sp);

                categorySheetPanel.addSheetProperty(category_name, new SheetProperty("showGrid",it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.LabelViewGrid","View grid"), SheetProperty.BOOLEAN));
                categorySheetPanel.addSheetProperty(category_name, new SheetProperty("ViewRules",it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.LabelViewRules","View rules"), SheetProperty.BOOLEAN));
                categorySheetPanel.addSheetProperty(category_name, new SheetProperty("GridSize",it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.LabelGridSize","Grid size"), SheetProperty.INTEGER));
                categorySheetPanel.addSheetProperty(category_name, new SheetProperty("ViewBorderForTextElements",it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.LabelViewBorderForTextElements","View border for text elements"), SheetProperty.BOOLEAN));
                categorySheetPanel.addSheetProperty(category_name, new SheetProperty("NewViewBorderColor",it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.LabelTextBorderColor","Text border color"), SheetProperty.COLOR));
                categorySheetPanel.addSheetProperty(category_name, new SheetProperty("Antialias",it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.LabelAntialias","Use antialiasing"), SheetProperty.BOOLEAN));
                categorySheetPanel.addSheetProperty(category_name, new SheetProperty("ShowToolTipsInDesign",it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.LabelShowToolTipsInDesign","Show ToolTips in design"), SheetProperty.BOOLEAN));
                categorySheetPanel.addSheetProperty(category_name, new SheetProperty("MagneticPower",it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.magneticPower","Magnetic power"), SheetProperty.INTEGER));
                categorySheetPanel.addSheetProperty(category_name, new SheetProperty("RealTimeValidation",it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.RealTimeValidation","Enable real time validation"), SheetProperty.BOOLEAN));
                
                categorySheetPanel.addSheetProperty(category_name, new SheetProperty("enableRMIServer",it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.startRMIServer","Enable RMI server"), SheetProperty.BOOLEAN));
                categorySheetPanel.addSheetProperty(category_name, new SheetProperty("RMIServerPort",it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.RMIServerPort","RMI server port"), SheetProperty.INTEGER));

                SheetProperty sp_language = new SheetProperty("Language",it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.LabelLanguage","Language"), SheetProperty.COMBOBOX);
                Vector tags_language = new Vector();

                //Fill choosable Languages and select last selected language
                listOfLanguages = it.businesslogic.ireport.util.I18n.getListOfAvailLanguages();

                listOfLanguages.add(0, Locale.getDefault());
                
                
                
                it.businesslogic.ireport.Language languageModel = null;

                String selectedLanguage = mf.getProperties().getProperty("Language");
                String selectedCountry = mf.getProperties().getProperty("Country");
                // => added by RL, june 3, 2005
                String selectedVariant = mf.getProperties().getProperty("Variant");
                // <=

                String def_language = "";
                for (int i=0; i< listOfLanguages.size(); ++i)
                {
                    Locale lang = (Locale)listOfLanguages.get(i);
                    LocaleAdapter adapter = new LocaleAdapter(lang);
                    Tag t = new Tag("" + adapter);
                    tags_language.add(t);
                    if( lang.getCountry().equals(selectedCountry) &&
                        lang.getLanguage().equals(selectedLanguage) &&
                        lang.getVariant().equals(selectedVariant) ) {
				def_language = "" + adapter;
                    }
                }
                sp_language.setTags( tags_language);
                if (def_language.length() > 0)
                {
                    sp_language.setValue( def_language);
                }
                else
                {
                    sp_language.setValue( "" + new LocaleAdapter( Locale.getDefault()));
                }
                categorySheetPanel.addSheetProperty(category_name, sp_language );

                //categorySheetPanel.addSheetProperty(category_name, new SheetProperty("UseAutoRegiesterFields",it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.LabelAutoRegisterFields","Auto register SQL fields"), SheetProperty.BOOLEAN));
                categorySheetPanel.addSheetProperty(category_name, new SheetProperty("EnableTextResizeClick",it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.LabelEnableTextResizeClick","Enable text fields resize click"), SheetProperty.BOOLEAN));
                categorySheetPanel.addSheetProperty(category_name, new SheetProperty("SynchronizeReportName",it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.LabelSynchronizeReportName","Synchronize report name with file name"), SheetProperty.BOOLEAN));
                categorySheetPanel.addSheetProperty(category_name, new SheetProperty("AutoReload",it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.AutoReload","Reload externally modified files without ask"), SheetProperty.BOOLEAN));
                categorySheetPanel.addSheetProperty(category_name, new SheetProperty("AskToSave",it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.AskToSave","Ask to save modified files before close them"), SheetProperty.BOOLEAN));
                categorySheetPanel.addSheetProperty(category_name, new SheetProperty("LoadFontOnStartup",it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.LoadFontOnStartup","Load font files on startup"), SheetProperty.BOOLEAN));

                
                categorySheetPanel.addSheetProperty(category_name, new SheetProperty("RestoreLayout",it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.RestoreLayoutOnStartup","Restore layout on startup"), SheetProperty.BOOLEAN));
        

                category_name =  it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.ReportDefaults","Report defaults");
                SheetProperty sp_script_language = new SheetProperty("DefaultScriptingLanguage",it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.DefaultScriptingLanguage","Default language for expressions"), SheetProperty.COMBOBOX);
                Vector tags_script_languages = new Vector();
                tags_script_languages.add(new Tag("groovy", "Groovy"));
                tags_script_languages.add(new Tag("java", "Java"));
                sp_script_language.setTags(tags_script_languages);
                categorySheetPanel.addSheetProperty(category_name, sp_script_language);


                SheetProperty sp_default_style = new SheetProperty("DefaultStyle",it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.DefaultStyle","Default style"), SheetProperty.COMBOBOX);
                Vector tags_default_style = new Vector();
                tags_default_style.add(new Tag("", it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.DefaultStyle.none","None")));
                Vector libStyles = MainFrame.getMainInstance().getStyleLibrarySet();
                for (int i=0; i< libStyles.size(); ++i)
                {
                    String styleName = ""+libStyles.get(i);
                    Tag t = new Tag(styleName);
                    tags_default_style.add(t);
                }
                sp_default_style.setTags(tags_default_style);
                categorySheetPanel.addSheetProperty(category_name, sp_default_style);

                category_name = I18n.getString("optionsDialog.tabLookAndFeel","LookAndFeel");
                                
                SheetProperty sp_laf = new SheetProperty("LookAndFeel",it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.LabelLookAndFeel","Look & Feel"), SheetProperty.COMBOBOX);
                Vector tags_laf = new Vector();

                listOfPLAF = it.businesslogic.ireport.util.Misc.getAvailablePLAF();
                String selectedPAF = mf.getProperties().getProperty("LookAndFeel");
                for (int i=0; i< listOfPLAF.size(); ++i)
                {
                    String pnf = (String)listOfPLAF.get(i);
                    Tag t = new Tag(pnf + "");
                    tags_laf.add(t);
                }
                sp_laf.setTags( tags_laf);
                sp_laf.setValue( selectedPAF );

                categorySheetPanel.addSheetProperty(category_name, sp_laf );

                categorySheetPanel.addSheetProperty(category_name, new SheetProperty("overrideDefaultFont",it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.OverrideDefaultFont","Override default font"), SheetProperty.BOOLEAN));
                NumberComboBoxSheetProperty spFontSize = null;
                spFontSize =  new NumberComboBoxSheetProperty("overrideDefaultFontSize",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.fontSize","Font size"));
                spFontSize.addEntry("3",3);
                spFontSize.addEntry("5",5);
                spFontSize.addEntry("8",8);
                spFontSize.addEntry("10",10);
                spFontSize.addEntry("11",11);
                spFontSize.addEntry("12",13);
                spFontSize.addEntry("13",13);
                spFontSize.addEntry("14",14);
                spFontSize.addEntry("15",15);
                spFontSize.addEntry("16",16);

                SheetProperty spFontName = new SheetProperty("overrideDefaultFontName",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.fontName","Font"), SheetProperty.COMBOBOX);
                Vector fontsVec = new Vector();
                String[] fontFamilies = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
                for (int i=0; i<fontFamilies.length; ++i) {
                    fontsVec.add( new Tag(fontFamilies[i]));
                }
                spFontName.setTags( fontsVec );
                spFontName.setDefaultValue("Tahoma");

                categorySheetPanel.addSheetProperty(category_name, spFontName);
                categorySheetPanel.addSheetProperty(category_name, spFontSize);
                categorySheetPanel.addSheetProperty(category_name, new SheetProperty("overrideDefaultFontAttrs",it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.Bold","Bold"), SheetProperty.BOOLEAN));

                categorySheetPanel.setShowResetButton(false);
                categorySheetPanel.recreateSheet();

                categorySheetPanel.addSheetPropertyValueChangedListener( this );
        }

        /**
         *
         */
        public void applyI18n()
        {
                // Start autogenerated code ----------------------
                jCheckBoxDontUseTemplateFromMenu.setText(I18n.getString("optionsDialog.checkBoxDontUseTemplateFromMenu","Don't use the template when using menu File > New report"));
                jCheckBoxKeepJavaFile.setText(I18n.getString("optionsDialog.checkBoxKeepJavaFile","keep .java file (if available)"));
                // End autogenerated code ----------------------
                // Start autogenerated code ----------------------
                jLabel1.setText(I18n.getString("optionsDialog.label1","Use this virtualizer"));
                jLabelReportVirtualizerMinGrowCount.setText(I18n.getString("optionsDialog.labelReportVirtualizerMinGrowCount","Min. grow count"));
                jLabelReportVirtualizerSize1.setText(I18n.getString("optionsDialog.labelReportVirtualizerSize1","Block size"));
                // End autogenerated code ----------------------
            // TODO: Set in order of appearance

            //jMenuFile.setText(it.businesslogic.ireport.util.I18n.getString("file","File"));
            jLabelTemplateForNewDocs.setText( it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.LabelTemplateForNewDocs","Use this file as template for new documents"));

            //Compiler

            //Backup

            jButtonTemplate.setText( it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.Browse","Browse"));
            jButtonOK.setText( it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.ButtonSave","Save"));
            jButtonApply.setText( it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.ButtonApply","Apply"));
            jButtonCancel.setText( it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.ButtonCancel","Cancel"));
            jTabbedPane1.setTitleAt(0, it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.TabGeneral","General"));
            jTabbedPane1.setTitleAt(1, it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.TabCompiler","Compiler"));
            ((javax.swing.border.TitledBorder)jPanelCompileDir.getBorder()).setTitle(it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.DefaultCompileDir","Default compilation directory"));
            jCheckBoxUseCurrentFilesDirectoryForCompiles.setText( it.businesslogic.ireport.util.I18n.getString( "gui.OptionsDialog.UseCurrentFilesDirectoryForCompiles","Use the reports directory for compiles"));
            jCheckBoxAutosave.setText(it.businesslogic.ireport.util.I18n.getString( "gui.OptionsDialog.Autosave","Autosave before compiling"));
            jButtonCompilationDir.setText( it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.Browse","Browse"));
            jTabbedPane1.setTitleAt(2, it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.TabBackup","Backup"));
            jTabbedPane1.setTitleAt(3, it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.TabExternalPrograms","External programs"));
            ((javax.swing.border.TitledBorder)jPanel6.getBorder()).setTitle(it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.BackupFilesOnSave","Backup files on save"));
            jRadioButton1_NoBackup.setText( it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.NoBackup","No backup"));
            jRadioButton2_ReplaceExtension.setText( it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.ReplaceExtension","Replace extension with .bak"));
            jRadioButton3_AppendExtension.setText( it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.AppendExtension","Append .bak to existing extension"));
            jLabelDefaultBackupDir.setText( it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.LabelDefaultBackupDir","Default backup directory"));
            jButtonBackupDirectory.setText(  it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.Browse","Browse"));
            jButtonBrowseEditor.setText(  it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.Browse","Browse"));
            jButtonBrowseEditor1.setText(  it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.Browse","Browse"));
            jButtonBrowseEditor2.setText(  it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.Browse","Browse"));
            jButtonBrowseEditor3.setText(  it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.Browse","Browse"));
            jButtonBrowseEditor4.setText(  it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.Browse","Browse"));
            jButtonBrowseEditor5.setText(  it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.Browse","Browse"));
            jButtonBrowseEditor6.setText(  it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.Browse","Browse"));
            ((javax.swing.border.TitledBorder)jPanelExternalEditor.getBorder()).setTitle(it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.ExternalEditor","External editor"));
            ((javax.swing.border.TitledBorder)jPanelViewers.getBorder()).setTitle(it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.Viewers","Viewers"));

            //External programs
            jLabelPDFViewer.setText(  it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.PDFViewer","PDF Viewer"));
            jLabelCSVViewer.setText(  it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.CSVViewer","CSV Viewer"));
            jLabelHTMLViewer.setText(  it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.HTMLViewer","HTML Viewer"));
            jLabelXLSViewer.setText(  it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.XLSViewer","XLS Viewer"));
            jLabelTXTViewer.setText(  it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.TXTViewer","TXT Viewer"));
            jLabelRTFViewer.setText(  it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.RTFViewer","RTF Viewer"));


            jButtonVirtualizerDirBrowse.setText(  it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.Browse","Browse"));
            jLabelReportVirtualizerDirectory.setText(  it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.ReportVirtualizerDir","Directory where the paged out data is to be stored"));
            jLabelReportVirtualizerSize.setText(  it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.ReportVirtualizerSize","Maximum size (in JRVirtualizable objects) of the paged in cache"));


            jTabbedPane1.setTitleAt(0, I18n.getString("optionsDialog.tab.General","General"));
            jTabbedPane1.setTitleAt(1, I18n.getString("optionsDialog.tab.Compiler","Compiler"));
            jTabbedPane1.setTitleAt(2, I18n.getString("optionsDialog.tab.Backup","Backup"));
            jTabbedPane1.setTitleAt(3, I18n.getString("optionsDialog.tab.ExternalPrograms","External programs"));
        
            ((javax.swing.border.TitledBorder)jPanelCompileDir.getBorder()).setTitle( it.businesslogic.ireport.util.I18n.getString("optionsDialog.panelBorder.DefaultCompilationDirectory","Default compilation directory") );
            ((javax.swing.border.TitledBorder)jPanelCompileDir1.getBorder()).setTitle( it.businesslogic.ireport.util.I18n.getString("optionsDialog.panelBorder.Compiler","Compiler") );
            ((javax.swing.border.TitledBorder)jPanelCompileDir2.getBorder()).setTitle( it.businesslogic.ireport.util.I18n.getString("optionsDialog.panelBorder.ReportVirtualizer","Report Virtualizer") );
            ((javax.swing.border.TitledBorder)jPanel7.getBorder()).setTitle( it.businesslogic.ireport.util.I18n.getString("optionsDialog.panelBorder.SwapFile","Swap file") );
            ((javax.swing.border.TitledBorder)jPanel6.getBorder()).setTitle( it.businesslogic.ireport.util.I18n.getString("optionsDialog.panelBorder.BackupFilesOnSave","Backup files on save") );
            ((javax.swing.border.TitledBorder)jPanelExternalEditor.getBorder()).setTitle( it.businesslogic.ireport.util.I18n.getString("optionsDialog.panelBorder.ExternalEditor","External editor") );
            ((javax.swing.border.TitledBorder)jPanelViewers.getBorder()).setTitle( it.businesslogic.ireport.util.I18n.getString("optionsDialog.panelBorder.Viewers","Viewers") );
            
            createSheet();
            loadConfiguration();
            this.setTitle("iReport - " + it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.title","Options"));
            this.getRootPane().updateUI();

        }




        /**
         *This method populate the frame element according with iReport properties...
         */
        public void loadConfiguration()
        {
            if (mf == null || mf.getProperties() == null)
            {
                return;
            }
            try
            {
                this.categorySheetPanel.setPropertyValue("Antialias", Misc.nvl(mf.getProperties().getProperty("Antialias"),"true") );
                this.categorySheetPanel.setPropertyValue("RealTimeValidation", Misc.nvl(mf.getProperties().getProperty("RealTimeValidation"),"true") );
                
                this.categorySheetPanel.setPropertyValue("ShowToolTipsInDesign", Misc.nvl(mf.getProperties().getProperty("ShowToolTipsInDesign"),"false") );
                this.categorySheetPanel.setPropertyValue("MagneticPower", Misc.nvl(mf.getProperties().getProperty("MagneticPower"),"5") );
                
                this.categorySheetPanel.setPropertyValue("RecentFilesLength", Misc.nvl(mf.getProperties().getProperty("RecentFilesLength"),"10"));
                this.categorySheetPanel.setPropertyValue("DefaultUnit",  Misc.nvl(mf.getProperties().getProperty("DefaultUnit"),"cm") );
                this.categorySheetPanel.setPropertyValue("showGrid",  Misc.nvl(mf.getProperties().getProperty("showGrid"),"false") );
                this.categorySheetPanel.setPropertyValue("ViewRules",  Misc.nvl(mf.getProperties().getProperty("ViewRules"),"true") );
                this.categorySheetPanel.setPropertyValue("ViewBorderForTextElements",  Misc.nvl(mf.getProperties().getProperty("ViewBorderForTextElements"),"true") );

                this.categorySheetPanel.setPropertyValue("AutoReload",  Misc.nvl(mf.getProperties().getProperty("AutoReload"),"false") );
                this.categorySheetPanel.setPropertyValue("AskToSave",  Misc.nvl(mf.getProperties().getProperty("AskToSave"),"true") );

                this.categorySheetPanel.setPropertyValue("LoadFontOnStartup",  Misc.nvl(mf.getProperties().getProperty("LoadFontOnStartup"),"true") );

                this.categorySheetPanel.setPropertyValue("RestoreLayout",  Misc.nvl(mf.getProperties().getProperty("RestoreLayout"),"true") );

                
                java.awt.Color color = new java.awt.Color(Integer.parseInt( Misc.nvl(mf.getProperties().getProperty("NewViewBorderColor"), Color.lightGray.getRGB() + "" )));
                String color_str =  HexColorChooserPanel.getEncodedColor( color );
                        //"[" + color.getRed() + "," + color.getGreen() + "," +  color.getBlue()+ "]";
                this.categorySheetPanel.setPropertyValue("NewViewBorderColor", color_str );
                
                String size = Misc.nvl(mf.getProperties().getProperty("GridSize"),"10");
                if (size.equals("null") || size.trim().length() == 0) size = "10";
                this.categorySheetPanel.setPropertyValue("GridSize", size);
                //this.categorySheetPanel.setPropertyValue("UseAutoRegiesterFields",  Misc.nvl(mf.getProperties().getProperty("UseAutoRegiesterFields"),"true") );
                this.categorySheetPanel.setPropertyValue("EnableTextResizeClick",  Misc.nvl(mf.getProperties().getProperty("EnableTextResizeClick"),"true") );
                this.categorySheetPanel.setPropertyValue("SynchronizeReportName",  Misc.nvl(mf.getProperties().getProperty("SynchronizeReportName"),"true") );
                this.categorySheetPanel.setPropertyValue("DefaultScriptingLanguage", Misc.nvl(mf.getProperties().getProperty("DefaultScriptingLanguage"),"java") );
                this.categorySheetPanel.setPropertyValue("DefaultStyle", Misc.nvl(mf.getProperties().getProperty("DefaultStyle"),"") );

                this.categorySheetPanel.setPropertyValue("overrideDefaultFont", Misc.nvl(mf.getProperties().getProperty("overrideDefaultFont"),"true") );
                this.categorySheetPanel.setPropertyValue("overrideDefaultFontSize", Misc.nvl(mf.getProperties().getProperty("overrideDefaultFontSize","11"),"11") );
                this.categorySheetPanel.setPropertyValue("overrideDefaultFontName", mf.getProperties().getProperty("overrideDefaultFontName","Tahoma") );
                this.categorySheetPanel.setPropertyValue("overrideDefaultFontAttrs",  ""+Misc.nvl(mf.getProperties().getProperty("overrideDefaultFontAttrs"),"0").equals("1") );

                this.jTextFieldTemplate.setText( Misc.nvl(mf.getProperties().getProperty("DefaultTemplate"),"") );

                this.categorySheetPanel.setPropertyValue("enableRMIServer",  Misc.nvl(mf.getProperties().getProperty("enableRMIServer"),"false") );
                this.categorySheetPanel.setPropertyValue("RMIServerPort",  Misc.nvl(mf.getProperties().getProperty("RMIServerPort"),"2100"));

                // Compiler
                this.jTextFieldCompilationDir.setText( mf.getDefaultCompilationDirectory() );
                this.jCheckBoxUseCurrentFilesDirectoryForCompiles.setSelected(mf.isUsingCurrentFilesDirectoryForCompiles());
                this.jCheckBoxAutosave.setSelected(Misc.nvl(mf.getProperties().getProperty("SaveBeforeCompiling"),"true").equals("true"));
                this.jComboBox1.setSelectedIndex(Integer.parseInt( Misc.nvl(mf.getProperties().getProperty("DefaultCompiler"),"0") ));
                this.jCheckBoxKeepJavaFile.setSelected(Misc.nvl(mf.getProperties().getProperty("KeepJavaFile"),"true").equals("true"));

                // Backup
                this.jTextFieldBackupDirectory.setText( Misc.nvl(mf.getProperties().getProperty("DefaultBackupDirectory"),""));
                this.jRadioButton1_NoBackup.setSelected( Misc.nvl(mf.getProperties().getProperty("BackupPolicy"),"3").equals("1"));
                this.jRadioButton2_ReplaceExtension.setSelected( Misc.nvl(mf.getProperties().getProperty("BackupPolicy"),"3").equals("2"));
                this.jRadioButton3_AppendExtension.setSelected( Misc.nvl(mf.getProperties().getProperty("BackupPolicy"),"3").equals("3"));
                if(mf.isUsingCurrentFilesDirectoryForCompiles())
                {
                    this.disableCompileDirectoryBrowse();
                }

                // External programs
                this.jTextFieldEditor.setText( Misc.nvl(mf.getProperties().getProperty("ExternalEditor"),""));
                this.jTextFieldPDFViewer.setText( Misc.nvl(mf.getProperties().getProperty("ExternalPDFViewer"),""));
                this.jTextFieldHTMLViewer.setText( Misc.nvl(mf.getProperties().getProperty("ExternalHTMLViewer"),""));
                this.jTextFieldXLSViewer.setText( Misc.nvl(mf.getProperties().getProperty("ExternalXLSViewer"),""));
                this.jTextFieldCSVViewer.setText( Misc.nvl(mf.getProperties().getProperty("ExternalCSVViewer"),""));
                this.jTextFieldTXTViewer.setText( Misc.nvl(mf.getProperties().getProperty("ExternalTXTViewer"),""));
                this.jTextFieldRTFViewer.setText( Misc.nvl(mf.getProperties().getProperty("ExternalRTFViewer"),""));
                this.jTextFieldODFViewer.setText( Misc.nvl(mf.getProperties().getProperty("ExternalODFViewer"),""));

                this.jTextFieldVirtualizerDir.setText(  mf.getProperties().getProperty("ReportVirtualizerDirectory",mf.getDefaultCompilationDirectory()));
                this.jSpinnerVirtualizerSize.setValue(new  Integer( mf.getProperties().getProperty("ReportVirtualizerSize","100")));
                Misc.setComboboxSelectedTagValue(jComboBoxVirtualizer, mf.getProperties().getProperty("ReportVirtualizer", "JRFileVirtualizer") );
                this.jSpinnerVirtualizerBlockSize.setValue(new  Integer( mf.getProperties().getProperty("ReportVirtualizerBlockSize","100")));
                this.jSpinnerVirtualizerGrownCount.setValue(new  Integer( mf.getProperties().getProperty("ReportVirtualizerMinGrownCount","100")));

                this.jCheckBoxDontUseTemplateFromMenu.setSelected(  Misc.nvl(mf.getProperties().getProperty("DontUseTemplateFromMenu"),"true").equals("true") );

                //this.jCheckBoxUseMultiLineScripts.setSelected( mf.isUsingMultiLineExpressions() );
                //         this.jComboBoxLanguage.setSelectedItem( it.businesslogic.ireport.util.I18n.getLanguage(mf.getProperties().getProperty("Language")) );


            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

            this.disableApplyAndOk();
            pack();
        }

        /** This method is called from within the constructor to
         * initialize the form.
         * WARNING: Do NOT modify this code. The content of this method is
         * always regenerated by the Form Editor.
         */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        buttonGroup1 = new javax.swing.ButtonGroup();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanelGeneral = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabelTemplateForNewDocs = new javax.swing.JLabel();
        jTextFieldTemplate = new javax.swing.JTextField();
        jButtonTemplate = new javax.swing.JButton();
        jCheckBoxDontUseTemplateFromMenu = new javax.swing.JCheckBox();
        jPanelComper = new javax.swing.JPanel();
        jPanelCompileDir = new javax.swing.JPanel();
        jTextFieldCompilationDir = new javax.swing.JTextField();
        jButtonCompilationDir = new javax.swing.JButton();
        jCheckBoxAutosave = new javax.swing.JCheckBox();
        jCheckBoxUseCurrentFilesDirectoryForCompiles = new javax.swing.JCheckBox();
        jCheckBoxKeepJavaFile = new javax.swing.JCheckBox();
        jPanelCompileDir1 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox();
        jPanelCompileDir2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jComboBoxVirtualizer = new javax.swing.JComboBox();
        jPanel4 = new javax.swing.JPanel();
        jLabelReportVirtualizerDirectory = new javax.swing.JLabel();
        jTextFieldVirtualizerDir = new javax.swing.JTextField();
        jButtonVirtualizerDirBrowse = new javax.swing.JButton();
        jLabelReportVirtualizerSize = new javax.swing.JLabel();
        jSpinnerVirtualizerSize = new javax.swing.JSpinner();
        jPanel7 = new javax.swing.JPanel();
        jLabelReportVirtualizerSize1 = new javax.swing.JLabel();
        jSpinnerVirtualizerBlockSize = new javax.swing.JSpinner();
        jLabelReportVirtualizerMinGrowCount = new javax.swing.JLabel();
        jSpinnerVirtualizerGrownCount = new javax.swing.JSpinner();
        jPanel3 = new javax.swing.JPanel();
        jPanelBackup = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jTextFieldBackupDirectory = new javax.swing.JTextField();
        jButtonBackupDirectory = new javax.swing.JButton();
        jRadioButton1_NoBackup = new javax.swing.JRadioButton();
        jRadioButton2_ReplaceExtension = new javax.swing.JRadioButton();
        jRadioButton3_AppendExtension = new javax.swing.JRadioButton();
        jLabelDefaultBackupDir = new javax.swing.JLabel();
        jPanelExternalPrograms = new javax.swing.JPanel();
        jPanelExternalEditor = new javax.swing.JPanel();
        jTextFieldEditor = new javax.swing.JTextField();
        jButtonBrowseEditor = new javax.swing.JButton();
        jPanelViewers = new javax.swing.JPanel();
        jTextFieldPDFViewer = new javax.swing.JTextField();
        jButtonBrowseEditor1 = new javax.swing.JButton();
        jTextFieldHTMLViewer = new javax.swing.JTextField();
        jButtonBrowseEditor2 = new javax.swing.JButton();
        jTextFieldXLSViewer = new javax.swing.JTextField();
        jButtonBrowseEditor3 = new javax.swing.JButton();
        jLabelPDFViewer = new javax.swing.JLabel();
        jLabelHTMLViewer = new javax.swing.JLabel();
        jLabelXLSViewer = new javax.swing.JLabel();
        jLabelCSVViewer = new javax.swing.JLabel();
        jTextFieldCSVViewer = new javax.swing.JTextField();
        jButtonBrowseEditor4 = new javax.swing.JButton();
        jLabelTXTViewer = new javax.swing.JLabel();
        jTextFieldTXTViewer = new javax.swing.JTextField();
        jButtonBrowseEditor5 = new javax.swing.JButton();
        jLabelRTFViewer = new javax.swing.JLabel();
        jTextFieldRTFViewer = new javax.swing.JTextField();
        jButtonBrowseEditor6 = new javax.swing.JButton();
        jLabelODFViewer = new javax.swing.JLabel();
        jTextFieldODFViewer = new javax.swing.JTextField();
        jButtonBrowseEditor7 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jButtonOK = new javax.swing.JButton();
        jButtonApply = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();

        setTitle("iReport Options");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jTabbedPane1.setMinimumSize(new java.awt.Dimension(332, 430));
        jTabbedPane1.setPreferredSize(new java.awt.Dimension(505, 430));
        jPanelGeneral.setLayout(new java.awt.BorderLayout());

        jPanel8.setLayout(new java.awt.BorderLayout());

        jPanel8.setMinimumSize(new java.awt.Dimension(250, 325));
        jPanel8.setPreferredSize(new java.awt.Dimension(500, 330));
        jPanelGeneral.add(jPanel8, java.awt.BorderLayout.CENTER);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jLabelTemplateForNewDocs.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabelTemplateForNewDocs.setText("Use this file as template for new documents");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 0, 0);
        jPanel2.add(jLabelTemplateForNewDocs, gridBagConstraints);

        jTextFieldTemplate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldTemplateActionPerformed(evt);
            }
        });
        jTextFieldTemplate.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                jTextFieldTemplateInputMethodTextChanged(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1000.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 3, 0);
        jPanel2.add(jTextFieldTemplate, gridBagConstraints);

        jButtonTemplate.setText("Browse");
        jButtonTemplate.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButtonTemplate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTemplateActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 3, 6);
        jPanel2.add(jButtonTemplate, gridBagConstraints);

        jCheckBoxDontUseTemplateFromMenu.setText("Don't use the template when using menu File > New report");
        jCheckBoxDontUseTemplateFromMenu.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBoxDontUseTemplateFromMenu.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jCheckBoxDontUseTemplateFromMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxDontUseTemplateFromMenuActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 4, 8);
        jPanel2.add(jCheckBoxDontUseTemplateFromMenu, gridBagConstraints);

        jPanelGeneral.add(jPanel2, java.awt.BorderLayout.SOUTH);

        jTabbedPane1.addTab("General", jPanelGeneral);

        jPanelComper.setLayout(new java.awt.GridBagLayout());

        jPanelCompileDir.setLayout(new java.awt.GridBagLayout());

        jPanelCompileDir.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Default compilation directory"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1000.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        jPanelCompileDir.add(jTextFieldCompilationDir, gridBagConstraints);

        jButtonCompilationDir.setText("Browse");
        jButtonCompilationDir.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButtonCompilationDir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCompilationDirActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        jPanelCompileDir.add(jButtonCompilationDir, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanelComper.add(jPanelCompileDir, gridBagConstraints);

        jCheckBoxAutosave.setText("Autosave before compiling");
        jCheckBoxAutosave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxAutosaveActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 3, 3);
        jPanelComper.add(jCheckBoxAutosave, gridBagConstraints);

        jCheckBoxUseCurrentFilesDirectoryForCompiles.setText("Use the reports directory for compiles");
        jCheckBoxUseCurrentFilesDirectoryForCompiles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxUseCurrentFilesDirectoryForCompilesActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        jPanelComper.add(jCheckBoxUseCurrentFilesDirectoryForCompiles, gridBagConstraints);

        jCheckBoxKeepJavaFile.setSelected(true);
        jCheckBoxKeepJavaFile.setText("keep .java file (if available)");
        jCheckBoxKeepJavaFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxKeepJavaFileActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 3, 3);
        jPanelComper.add(jCheckBoxKeepJavaFile, gridBagConstraints);

        jPanelCompileDir1.setLayout(new java.awt.GridBagLayout());

        jPanelCompileDir1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Compiler"));
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "JasperReports default", "Java Compiler (need tools.jar)", "JDT Compiler  (need jdt-compiler.jar)", "Bean shell compiler (need bsh-1.3.0.jar)", "Jikes compiler (need jikes command)" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelCompileDir1.add(jComboBox1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanelComper.add(jPanelCompileDir1, gridBagConstraints);

        jPanelCompileDir2.setLayout(new java.awt.GridBagLayout());

        jPanelCompileDir2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Report Virtualizer"));
        jLabel1.setText("Use this virtualizer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        jPanelCompileDir2.add(jLabel1, gridBagConstraints);

        jComboBoxVirtualizer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxVirtualizerActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 8, 3);
        jPanelCompileDir2.add(jComboBoxVirtualizer, gridBagConstraints);

        jPanel4.setLayout(new java.awt.GridBagLayout());

        jLabelReportVirtualizerDirectory.setText("Directory where the paged out data is to be stored");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        jPanel4.add(jLabelReportVirtualizerDirectory, gridBagConstraints);

        jTextFieldVirtualizerDir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldVirtualizerDirActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1000.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        jPanel4.add(jTextFieldVirtualizerDir, gridBagConstraints);

        jButtonVirtualizerDirBrowse.setText("Browse");
        jButtonVirtualizerDirBrowse.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButtonVirtualizerDirBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVirtualizerDirBrowseActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        jPanel4.add(jButtonVirtualizerDirBrowse, gridBagConstraints);

        jLabelReportVirtualizerSize.setText("Maximum size (in JRVirtualizable objects) of the paged in cache");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 4);
        jPanel4.add(jLabelReportVirtualizerSize, gridBagConstraints);

        jSpinnerVirtualizerSize.setMinimumSize(new java.awt.Dimension(127, 20));
        jSpinnerVirtualizerSize.setPreferredSize(new java.awt.Dimension(127, 20));
        jSpinnerVirtualizerSize.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerVirtualizerSizeStateChanged(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        jPanel4.add(jSpinnerVirtualizerSize, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanelCompileDir2.add(jPanel4, gridBagConstraints);

        jPanel7.setLayout(new java.awt.GridBagLayout());

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Swap file"));
        jLabelReportVirtualizerSize1.setText("Block size");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        jPanel7.add(jLabelReportVirtualizerSize1, gridBagConstraints);

        jSpinnerVirtualizerBlockSize.setMinimumSize(new java.awt.Dimension(127, 20));
        jSpinnerVirtualizerBlockSize.setPreferredSize(new java.awt.Dimension(127, 20));
        jSpinnerVirtualizerBlockSize.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerVirtualizerSizeStateChanged2(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        jPanel7.add(jSpinnerVirtualizerBlockSize, gridBagConstraints);

        jLabelReportVirtualizerMinGrowCount.setText("Min. grow count");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        jPanel7.add(jLabelReportVirtualizerMinGrowCount, gridBagConstraints);

        jSpinnerVirtualizerGrownCount.setMinimumSize(new java.awt.Dimension(127, 20));
        jSpinnerVirtualizerGrownCount.setPreferredSize(new java.awt.Dimension(127, 20));
        jSpinnerVirtualizerGrownCount.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerVirtualizerSize1jSpinnerVirtualizerSizeStateChanged2(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        jPanel7.add(jSpinnerVirtualizerGrownCount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanelCompileDir2.add(jPanel7, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanelComper.add(jPanelCompileDir2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 99;
        gridBagConstraints.weighty = 1.0;
        jPanelComper.add(jPanel3, gridBagConstraints);

        jTabbedPane1.addTab("Compiler", jPanelComper);

        jPanelBackup.setLayout(new java.awt.GridBagLayout());

        jPanel6.setLayout(new java.awt.GridBagLayout());

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Backup files on save"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel6.add(jTextFieldBackupDirectory, gridBagConstraints);

        jButtonBackupDirectory.setText("Browse");
        jButtonBackupDirectory.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButtonBackupDirectory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBackupDirectoryActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel6.add(jButtonBackupDirectory, gridBagConstraints);

        buttonGroup1.add(jRadioButton1_NoBackup);
        jRadioButton1_NoBackup.setText("No backup");
        jRadioButton1_NoBackup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1_NoBackupActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        jPanel6.add(jRadioButton1_NoBackup, gridBagConstraints);

        buttonGroup1.add(jRadioButton2_ReplaceExtension);
        jRadioButton2_ReplaceExtension.setText("Replace extension with .bak");
        jRadioButton2_ReplaceExtension.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2_ReplaceExtensionActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        jPanel6.add(jRadioButton2_ReplaceExtension, gridBagConstraints);

        buttonGroup1.add(jRadioButton3_AppendExtension);
        jRadioButton3_AppendExtension.setSelected(true);
        jRadioButton3_AppendExtension.setText("Append .bak to existing extension");
        jRadioButton3_AppendExtension.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton3_AppendExtensionActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 5, 3);
        jPanel6.add(jRadioButton3_AppendExtension, gridBagConstraints);

        jLabelDefaultBackupDir.setText("Default backup directory");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        jPanel6.add(jLabelDefaultBackupDir, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanelBackup.add(jPanel6, gridBagConstraints);

        jTabbedPane1.addTab("Backup", jPanelBackup);

        jPanelExternalPrograms.setLayout(new java.awt.GridBagLayout());

        jPanelExternalEditor.setLayout(new java.awt.GridBagLayout());

        jPanelExternalEditor.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "External editor"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1000.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        jPanelExternalEditor.add(jTextFieldEditor, gridBagConstraints);

        jButtonBrowseEditor.setText("Browse");
        jButtonBrowseEditor.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButtonBrowseEditor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBrowseEditorActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        jPanelExternalEditor.add(jButtonBrowseEditor, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanelExternalPrograms.add(jPanelExternalEditor, gridBagConstraints);

        jPanelViewers.setLayout(new java.awt.GridBagLayout());

        jPanelViewers.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Viewers"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1000.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        jPanelViewers.add(jTextFieldPDFViewer, gridBagConstraints);

        jButtonBrowseEditor1.setText("Browse");
        jButtonBrowseEditor1.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButtonBrowseEditor1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBrowseEditor1ActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        jPanelViewers.add(jButtonBrowseEditor1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        jPanelViewers.add(jTextFieldHTMLViewer, gridBagConstraints);

        jButtonBrowseEditor2.setText("Browse");
        jButtonBrowseEditor2.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButtonBrowseEditor2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBrowseEditor2ActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        jPanelViewers.add(jButtonBrowseEditor2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        jPanelViewers.add(jTextFieldXLSViewer, gridBagConstraints);

        jButtonBrowseEditor3.setText("Browse");
        jButtonBrowseEditor3.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButtonBrowseEditor3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBrowseEditor3ActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        jPanelViewers.add(jButtonBrowseEditor3, gridBagConstraints);

        jLabelPDFViewer.setText("PDF viewer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        jPanelViewers.add(jLabelPDFViewer, gridBagConstraints);

        jLabelHTMLViewer.setText("HTML viewer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        jPanelViewers.add(jLabelHTMLViewer, gridBagConstraints);

        jLabelXLSViewer.setText("XLS viewer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        jPanelViewers.add(jLabelXLSViewer, gridBagConstraints);

        jLabelCSVViewer.setText("CSV viewer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        jPanelViewers.add(jLabelCSVViewer, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        jPanelViewers.add(jTextFieldCSVViewer, gridBagConstraints);

        jButtonBrowseEditor4.setText("Browse");
        jButtonBrowseEditor4.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButtonBrowseEditor4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBrowseEditor4ActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        jPanelViewers.add(jButtonBrowseEditor4, gridBagConstraints);

        jLabelTXTViewer.setText("Text viewer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        jPanelViewers.add(jLabelTXTViewer, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        jPanelViewers.add(jTextFieldTXTViewer, gridBagConstraints);

        jButtonBrowseEditor5.setText("Browse");
        jButtonBrowseEditor5.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButtonBrowseEditor5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBrowseEditor5ActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        jPanelViewers.add(jButtonBrowseEditor5, gridBagConstraints);

        jLabelRTFViewer.setText("CSV viewer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        jPanelViewers.add(jLabelRTFViewer, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        jPanelViewers.add(jTextFieldRTFViewer, gridBagConstraints);

        jButtonBrowseEditor6.setText("Browse");
        jButtonBrowseEditor6.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButtonBrowseEditor6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBrowseEditor6ActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        jPanelViewers.add(jButtonBrowseEditor6, gridBagConstraints);

        jLabelODFViewer.setText("OpenOffice (ODF) viewer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        jPanelViewers.add(jLabelODFViewer, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        jPanelViewers.add(jTextFieldODFViewer, gridBagConstraints);

        jButtonBrowseEditor7.setText("Browse");
        jButtonBrowseEditor7.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButtonBrowseEditor7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBrowseEditor6ActionPerformed1(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        jPanelViewers.add(jButtonBrowseEditor7, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 6, 3);
        jPanelExternalPrograms.add(jPanelViewers, gridBagConstraints);

        jTabbedPane1.addTab("External programs", jPanelExternalPrograms);

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jPanel1.setMinimumSize(new java.awt.Dimension(10, 30));
        jButtonOK.setMnemonic('s');
        jButtonOK.setText("Save");
        jButtonOK.setEnabled(false);
        jButtonOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOKActionPerformed(evt);
            }
        });

        jPanel1.add(jButtonOK);

        jButtonApply.setMnemonic('a');
        jButtonApply.setText("Apply");
        jButtonApply.setEnabled(false);
        jButtonApply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonApplyActionPerformed(evt);
            }
        });

        jPanel1.add(jButtonApply);

        jButtonCancel.setMnemonic('c');
        jButtonCancel.setText("Cancel");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        jPanel1.add(jButtonCancel);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        pack();
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        java.awt.Dimension dialogSize = getSize();
        setLocation((screenSize.width-dialogSize.width)/2,(screenSize.height-dialogSize.height)/2);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonBrowseEditor6ActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBrowseEditor6ActionPerformed1
            javax.swing.JFileChooser jfc = new javax.swing.JFileChooser();

	    //jfc.setDialogTitle("Choose a RTF viewer...");//it.businesslogic.ireport.util.I18n.getString("redo","Redo")
            jfc.setDialogTitle(it.businesslogic.ireport.util.I18n.getString("chooseODF","Choose the OpenOffice (ODF) viewer..."));
	    jfc.setMultiSelectionEnabled(false);
	    if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
		    jTextFieldODFViewer.setText( jfc.getSelectedFile().getPath());
	    }
    }//GEN-LAST:event_jButtonBrowseEditor6ActionPerformed1

    private void jCheckBoxDontUseTemplateFromMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxDontUseTemplateFromMenuActionPerformed
        this.enableApplyAndOk();
    }//GEN-LAST:event_jCheckBoxDontUseTemplateFromMenuActionPerformed

    private void jComboBoxVirtualizerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxVirtualizerActionPerformed
        this.enableApplyAndOk();
    }//GEN-LAST:event_jComboBoxVirtualizerActionPerformed

    private void jSpinnerVirtualizerSize1jSpinnerVirtualizerSizeStateChanged2(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerVirtualizerSize1jSpinnerVirtualizerSizeStateChanged2
        this.enableApplyAndOk();
    }//GEN-LAST:event_jSpinnerVirtualizerSize1jSpinnerVirtualizerSizeStateChanged2

    private void jSpinnerVirtualizerSizeStateChanged2(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerVirtualizerSizeStateChanged2
        this.enableApplyAndOk();
    }//GEN-LAST:event_jSpinnerVirtualizerSizeStateChanged2

    private void jButtonBrowseEditor6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBrowseEditor6ActionPerformed
            javax.swing.JFileChooser jfc = new javax.swing.JFileChooser();

	    //jfc.setDialogTitle("Choose a RTF viewer...");//it.businesslogic.ireport.util.I18n.getString("redo","Redo")
            jfc.setDialogTitle(it.businesslogic.ireport.util.I18n.getString("chooseRTF","Choose a RTF viewer..."));
	    jfc.setMultiSelectionEnabled(false);
	    if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
		    jTextFieldRTFViewer.setText( jfc.getSelectedFile().getPath());
	    }
    }//GEN-LAST:event_jButtonBrowseEditor6ActionPerformed

    private void jButtonBrowseEditor5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBrowseEditor5ActionPerformed
            javax.swing.JFileChooser jfc = new javax.swing.JFileChooser();

	    //jfc.setDialogTitle("Choose an Text viewer...");
            jfc.setDialogTitle(it.businesslogic.ireport.util.I18n.getString("chooseText","Choose a Text viewer..."));
	    jfc.setMultiSelectionEnabled(false);
	    if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
		    jTextFieldTXTViewer.setText( jfc.getSelectedFile().getPath());
	    }
    }//GEN-LAST:event_jButtonBrowseEditor5ActionPerformed

    private void jTextFieldVirtualizerDirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldVirtualizerDirActionPerformed
        this.enableApplyAndOk();
    }//GEN-LAST:event_jTextFieldVirtualizerDirActionPerformed

    private void jSpinnerVirtualizerSizeStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerVirtualizerSizeStateChanged
        this.enableApplyAndOk();
    }//GEN-LAST:event_jSpinnerVirtualizerSizeStateChanged

    private void jButtonVirtualizerDirBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVirtualizerDirBrowseActionPerformed
            JFileChooser jfc = new JFileChooser();
	    jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
		    jTextFieldVirtualizerDir.setText( jfc.getSelectedFile().getPath());
	    }
            this.enableApplyAndOk();
    }//GEN-LAST:event_jButtonVirtualizerDirBrowseActionPerformed

    private void jCheckBoxKeepJavaFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxKeepJavaFileActionPerformed
       this.enableApplyAndOk();
    }//GEN-LAST:event_jCheckBoxKeepJavaFileActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        this.enableApplyAndOk();
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jCheckBoxUseCurrentFilesDirectoryForCompilesActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jCheckBoxUseCurrentFilesDirectoryForCompilesActionPerformed
    {//GEN-HEADEREND:event_jCheckBoxUseCurrentFilesDirectoryForCompilesActionPerformed
	    if(this.jCheckBoxUseCurrentFilesDirectoryForCompiles.isSelected()) {
		    this.disableCompileDirectoryBrowse();
	    }
	    else {
		    this.enableCompileDirectoryBrowse();
	    }
	    this.enableApplyAndOk();
    }//GEN-LAST:event_jCheckBoxUseCurrentFilesDirectoryForCompilesActionPerformed

    private void jButtonOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOKActionPerformed
	    if  ( jButtonApply.isEnabled())
		    jButtonApplyActionPerformed(evt);

	    if (mf != null) mf.saveiReportConfiguration();
	    this.jButtonOK.setEnabled(false);
	    this.setVisible(false);
	    this.dispose();
    }//GEN-LAST:event_jButtonOKActionPerformed

    private void jButtonApplyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonApplyActionPerformed
	    applyConfiguration();
	    jButtonApply.setEnabled(false);
    }//GEN-LAST:event_jButtonApplyActionPerformed

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
	    this.setVisible(false);
	    this.dispose();
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jButtonBrowseEditor4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBrowseEditor4ActionPerformed
	    javax.swing.JFileChooser jfc = new javax.swing.JFileChooser();

	    //jfc.setDialogTitle("Choose a CSV viewer...");
            jfc.setDialogTitle(it.businesslogic.ireport.util.I18n.getString("chooseCSV","Choose a CSV viewer..."));
	    jfc.setMultiSelectionEnabled(false);
	    if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
		    jTextFieldCSVViewer.setText( jfc.getSelectedFile().getPath());
	    }
    }//GEN-LAST:event_jButtonBrowseEditor4ActionPerformed

    private void jButtonBrowseEditor3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBrowseEditor3ActionPerformed
	    javax.swing.JFileChooser jfc = new javax.swing.JFileChooser();

	    //jfc.setDialogTitle("Choose a XLS viewer...");
            jfc.setDialogTitle(it.businesslogic.ireport.util.I18n.getString("chooseXLS","Choose a XLS viewer..."));
	    jfc.setMultiSelectionEnabled(false);
	    if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
		    jTextFieldXLSViewer.setText( jfc.getSelectedFile().getPath());
	    }
    }//GEN-LAST:event_jButtonBrowseEditor3ActionPerformed

    private void jButtonBrowseEditor2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBrowseEditor2ActionPerformed
	    javax.swing.JFileChooser jfc = new javax.swing.JFileChooser();

	    //jfc.setDialogTitle("Choose a HTML viewer...");
            jfc.setDialogTitle(it.businesslogic.ireport.util.I18n.getString("chooseHTML","Choose a HTML viewer..."));
	    jfc.setMultiSelectionEnabled(false);
	    if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
		    jTextFieldHTMLViewer.setText( jfc.getSelectedFile().getPath());
	    }
    }//GEN-LAST:event_jButtonBrowseEditor2ActionPerformed

    private void jButtonBrowseEditor1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBrowseEditor1ActionPerformed
	    javax.swing.JFileChooser jfc = new javax.swing.JFileChooser();

	    //jfc.setDialogTitle("Choose a PDF viewer...");
            jfc.setDialogTitle(it.businesslogic.ireport.util.I18n.getString("choosePDF","Choose a PDF viewer..."));
	    jfc.setMultiSelectionEnabled(false);
	    if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
		    jTextFieldPDFViewer.setText( jfc.getSelectedFile().getPath());
	    }
    }//GEN-LAST:event_jButtonBrowseEditor1ActionPerformed

    private void jButtonBrowseEditorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBrowseEditorActionPerformed
	    javax.swing.JFileChooser jfc = new javax.swing.JFileChooser();

	    //jfc.setDialogTitle("Choose an editor...");
            jfc.setDialogTitle(it.businesslogic.ireport.util.I18n.getString("chooseEditor","Choose an editor..."));
	    jfc.setMultiSelectionEnabled(false);
	    if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
		    jTextFieldEditor.setText( jfc.getSelectedFile().getPath());
	    }

    }//GEN-LAST:event_jButtonBrowseEditorActionPerformed

    private void jButtonBackupDirectoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBackupDirectoryActionPerformed
	    JFileChooser jfc = new JFileChooser();
	    jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
		    jTextFieldBackupDirectory.setText( jfc.getSelectedFile().getPath());
	    }
    }//GEN-LAST:event_jButtonBackupDirectoryActionPerformed

    private void jButtonCompilationDirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCompilationDirActionPerformed

	    JFileChooser jfc = new JFileChooser();
	    jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (jTextFieldCompilationDir.getText().length() > 0)
            {
                try {
                    jfc.setCurrentDirectory( new File(jTextFieldCompilationDir.getText()));
                    
                } catch (Exception ex){
                    try {
                        jfc.setCurrentDirectory( new File(MainFrame.getMainInstance().getCurrentDirectory()));
                    } catch (Exception ex2){}
                }
            }
            
	    if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
		    jTextFieldCompilationDir.setText( jfc.getSelectedFile().getPath());
	    }
    }//GEN-LAST:event_jButtonCompilationDirActionPerformed

    private void jButtonTemplateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTemplateActionPerformed
	    // jfilechooser...
	    JFileChooser jfc = new JFileChooser();
	    jfc.setFileFilter( new javax.swing.filechooser.FileFilter() {
		    public boolean accept(java.io.File file) {
			    String filename = file.getName();
			    return (filename.endsWith(".xml") || file.isDirectory()) ;
		    }
		    public String getDescription() {
			    return "JasperReports XML *.xml";
		    }
	    });
	    if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
		    jTextFieldTemplate.setText( jfc.getSelectedFile().getPath());
	    }
    }//GEN-LAST:event_jButtonTemplateActionPerformed

    private void jRadioButton3_AppendExtensionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton3_AppendExtensionActionPerformed
	    this.enableApplyAndOk();
    }//GEN-LAST:event_jRadioButton3_AppendExtensionActionPerformed

    private void jRadioButton2_ReplaceExtensionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2_ReplaceExtensionActionPerformed
	    this.enableApplyAndOk();
    }//GEN-LAST:event_jRadioButton2_ReplaceExtensionActionPerformed

    private void jRadioButton1_NoBackupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1_NoBackupActionPerformed
	    this.enableApplyAndOk();
    }//GEN-LAST:event_jRadioButton1_NoBackupActionPerformed

    private void jCheckBoxAutosaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxAutosaveActionPerformed
	    this.enableApplyAndOk();
    }//GEN-LAST:event_jCheckBoxAutosaveActionPerformed

    private void jTextFieldTemplateInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jTextFieldTemplateInputMethodTextChanged
	    this.enableApplyAndOk();
    }//GEN-LAST:event_jTextFieldTemplateInputMethodTextChanged

    private void jTextFieldTemplateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldTemplateActionPerformed
	    this.enableApplyAndOk();
    }//GEN-LAST:event_jTextFieldTemplateActionPerformed

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
	    setVisible(false);
	    dispose();
    }//GEN-LAST:event_closeDialog

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
	    new OptionsDialog(new javax.swing.JFrame(), true).setVisible(true);
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButtonApply;
    private javax.swing.JButton jButtonBackupDirectory;
    private javax.swing.JButton jButtonBrowseEditor;
    private javax.swing.JButton jButtonBrowseEditor1;
    private javax.swing.JButton jButtonBrowseEditor2;
    private javax.swing.JButton jButtonBrowseEditor3;
    private javax.swing.JButton jButtonBrowseEditor4;
    private javax.swing.JButton jButtonBrowseEditor5;
    private javax.swing.JButton jButtonBrowseEditor6;
    private javax.swing.JButton jButtonBrowseEditor7;
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonCompilationDir;
    private javax.swing.JButton jButtonOK;
    private javax.swing.JButton jButtonTemplate;
    private javax.swing.JButton jButtonVirtualizerDirBrowse;
    private javax.swing.JCheckBox jCheckBoxAutosave;
    private javax.swing.JCheckBox jCheckBoxDontUseTemplateFromMenu;
    private javax.swing.JCheckBox jCheckBoxKeepJavaFile;
    private javax.swing.JCheckBox jCheckBoxUseCurrentFilesDirectoryForCompiles;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBoxVirtualizer;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelCSVViewer;
    private javax.swing.JLabel jLabelDefaultBackupDir;
    private javax.swing.JLabel jLabelHTMLViewer;
    private javax.swing.JLabel jLabelODFViewer;
    private javax.swing.JLabel jLabelPDFViewer;
    private javax.swing.JLabel jLabelRTFViewer;
    private javax.swing.JLabel jLabelReportVirtualizerDirectory;
    private javax.swing.JLabel jLabelReportVirtualizerMinGrowCount;
    private javax.swing.JLabel jLabelReportVirtualizerSize;
    private javax.swing.JLabel jLabelReportVirtualizerSize1;
    private javax.swing.JLabel jLabelTXTViewer;
    private javax.swing.JLabel jLabelTemplateForNewDocs;
    private javax.swing.JLabel jLabelXLSViewer;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanelBackup;
    private javax.swing.JPanel jPanelComper;
    private javax.swing.JPanel jPanelCompileDir;
    private javax.swing.JPanel jPanelCompileDir1;
    private javax.swing.JPanel jPanelCompileDir2;
    private javax.swing.JPanel jPanelExternalEditor;
    private javax.swing.JPanel jPanelExternalPrograms;
    private javax.swing.JPanel jPanelGeneral;
    private javax.swing.JPanel jPanelViewers;
    private javax.swing.JRadioButton jRadioButton1_NoBackup;
    private javax.swing.JRadioButton jRadioButton2_ReplaceExtension;
    private javax.swing.JRadioButton jRadioButton3_AppendExtension;
    private javax.swing.JSpinner jSpinnerVirtualizerBlockSize;
    private javax.swing.JSpinner jSpinnerVirtualizerGrownCount;
    private javax.swing.JSpinner jSpinnerVirtualizerSize;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextFieldBackupDirectory;
    private javax.swing.JTextField jTextFieldCSVViewer;
    private javax.swing.JTextField jTextFieldCompilationDir;
    private javax.swing.JTextField jTextFieldEditor;
    private javax.swing.JTextField jTextFieldHTMLViewer;
    private javax.swing.JTextField jTextFieldODFViewer;
    private javax.swing.JTextField jTextFieldPDFViewer;
    private javax.swing.JTextField jTextFieldRTFViewer;
    private javax.swing.JTextField jTextFieldTXTViewer;
    private javax.swing.JTextField jTextFieldTemplate;
    private javax.swing.JTextField jTextFieldVirtualizerDir;
    private javax.swing.JTextField jTextFieldXLSViewer;
    // End of variables declaration//GEN-END:variables
   /*
    public void setVisible(boolean visible)
    {
	if (visible == true && visible != this.isVisible()) loadConfiguration();
	super.setVisible( visible);
    }
    */

	public void applyConfiguration() {
		if (mf == null || mf.getProperties() == null) {
			return;
		}
		java.util.Properties prop = mf.getProperties();
		try {
                        // Property sheet
                        prop.put("RecentFilesLength", ""+this.categorySheetPanel.getPropertyValue("RecentFilesLength"));
                        prop.put("DefaultUnit",""+this.categorySheetPanel.getPropertyValue("DefaultUnit"));
                        prop.put("GridSize",""+this.categorySheetPanel.getPropertyValue("GridSize"));
                        prop.put("ViewRules",""+this.categorySheetPanel.getPropertyValue("ViewRules"));
                        prop.put("showGrid",""+this.categorySheetPanel.getPropertyValue("showGrid"));
                        prop.put("Antialias",""+this.categorySheetPanel.getPropertyValue("Antialias"));
                        prop.put("RealTimeValidation",""+this.categorySheetPanel.getPropertyValue("RealTimeValidation"));
                        
                        prop.put("AutoReload",""+this.categorySheetPanel.getPropertyValue("AutoReload"));
                        prop.put("AskToSave",""+this.categorySheetPanel.getPropertyValue("AskToSave"));
                        prop.put("LoadFontOnStartup",""+this.categorySheetPanel.getPropertyValue("LoadFontOnStartup"));
                        prop.put("RestoreLayout",""+this.categorySheetPanel.getPropertyValue("RestoreLayout"));

                        prop.put("ShowToolTipsInDesign",""+""+this.categorySheetPanel.getPropertyValue("ShowToolTipsInDesign"));

                        prop.put("MagneticPower", ""+this.categorySheetPanel.getPropertyValue("MagneticPower"));
                        
                                
                        prop.put("enableRMIServer",""+this.categorySheetPanel.getPropertyValue("enableRMIServer"));
                        prop.put("RMIServerPort",""+""+this.categorySheetPanel.getPropertyValue("RMIServerPort"));

                        mf.updateAntialiasMode();

                        prop.put("ViewBorderForTextElements",""+""+this.categorySheetPanel.getPropertyValue("ViewBorderForTextElements"));


                        java.awt.Color color = null;
                        color = ColorSelectorPanel.parseColorString(  ""+this.categorySheetPanel.getPropertyValue("NewViewBorderColor") );

                        prop.put("NewViewBorderColor",""+((color != null) ? color.getRGB() : java.awt.Color.LIGHT_GRAY.getRGB()));

                        if (color != null)
                        {
                            ReportElement.lightcolor = color;
                        }
                        
                        String currentLAF = prop.getProperty("LookAndFeel","");

                        prop.put("LookAndFeel",""+""+this.categorySheetPanel.getPropertyValue("LookAndFeel"));
                        //prop.put("UseAutoRegiesterFields",""+""+this.categorySheetPanel.getPropertyValue("UseAutoRegiesterFields"));
                        prop.put("EnableTextResizeClick",""+""+this.categorySheetPanel.getPropertyValue("EnableTextResizeClick"));
                        prop.put("SynchronizeReportName",""+""+this.categorySheetPanel.getPropertyValue("SynchronizeReportName"));
                        prop.put("DefaultScriptingLanguage",""+""+this.categorySheetPanel.getPropertyValue("DefaultScriptingLanguage"));

                        prop.put("DefaultStyle",""+""+this.categorySheetPanel.getPropertyValue("DefaultStyle"));

                        String currentOverrideDefaultFont = prop.getProperty("overrideDefaultFont","");
                        String currentOverrideDefaultFontSize = prop.getProperty("overrideDefaultFontSize","");

                        prop.put("overrideDefaultFont",""+this.categorySheetPanel.getPropertyValue("overrideDefaultFont"));
                        try {

                            Integer.parseInt(""+ this.categorySheetPanel.getPropertyValue("overrideDefaultFontSize") );
                            prop.put("overrideDefaultFontSize",""+this.categorySheetPanel.getPropertyValue("overrideDefaultFontSize"));

                        } catch (Exception ex)
                        {
                           ex.printStackTrace();
                        }

                        String currentOverrideDefaultFontName = prop.getProperty("overrideDefaultFontName","");
                        String currentOverrideDefaultFontAttrs = prop.getProperty("overrideDefaultFontAttrs","");


                        prop.put("overrideDefaultFontName",""+this.categorySheetPanel.getPropertyValue("overrideDefaultFontName"));
                        prop.put("overrideDefaultFontAttrs",""+this.categorySheetPanel.getPropertyValue("overrideDefaultFontAttrs"));



                        String lang_str = (String)this.categorySheetPanel.getPropertyValue("Language");

                        Locale selectedLang = null;



                        for (int i=0; i< listOfLanguages.size(); ++i)
                        {
                            Locale lang = (Locale)listOfLanguages.get(i);
                            LocaleAdapter adapter = new LocaleAdapter(lang);
                            if( lang_str.equals(""+adapter) ) {
                                selectedLang = adapter.getLocale();
        			prop.put("Language", selectedLang.getLanguage() );
                		prop.put("Country", selectedLang.getCountry() );
                                prop.put("Variant", selectedLang.getVariant() );
                                break;
                            }
                        }

                        // Other tabs...
			prop.put("DefaultBackupDirectory",""+this.jTextFieldBackupDirectory.getText());
			prop.put("ExternalCSVViewer",""+this.jTextFieldCSVViewer.getText());
			prop.put("ExternalPDFViewer",""+this.jTextFieldPDFViewer.getText());
			prop.put("ExternalXLSViewer",""+this.jTextFieldXLSViewer.getText());
			prop.put("ExternalHTMLViewer",""+this.jTextFieldHTMLViewer.getText());
                        prop.put("ExternalTXTViewer",""+this.jTextFieldTXTViewer.getText());
                        prop.put("ExternalRTFViewer",""+this.jTextFieldRTFViewer.getText());
                        prop.put("ExternalODFViewer",""+this.jTextFieldODFViewer.getText());
			prop.put("ExternalEditor",""+this.jTextFieldEditor.getText());
			prop.put("DefaultTemplate",""+this.jTextFieldTemplate.getText());
			prop.put("SaveBeforeCompiling",""+this.jCheckBoxAutosave.isSelected());



			//MainFrame and this code need to implement and use an object that houses the properties
			//object and have methods for these options.
			mf.setUsingMultiLineExpressions(false); // this.jCheckBoxUseMultiLineScripts.isSelected());
			mf.setUsingCurrentFilesDirectoryForCompiles(this.jCheckBoxUseCurrentFilesDirectoryForCompiles.isSelected());
			prop.put("useCurrentFilesDirectoryForCompiles", this.jCheckBoxUseCurrentFilesDirectoryForCompiles.isSelected()+"" );

			mf.setDefaultCompilationDirectory(this.jTextFieldCompilationDir.getText());
			prop.put("DefaultCompilationDirectory", this.jTextFieldCompilationDir.getText() );

			String policy = "3";
			if (this.jRadioButton1_NoBackup.isSelected())
                        { policy = "1"; }
                        if (this.jRadioButton2_ReplaceExtension.isSelected())
                        { policy = "2";}
                        if (this.jRadioButton3_AppendExtension.isSelected())
                        { policy = "3";}

			prop.put("BackupPolicy", policy );
			//prop.put("ViewBorderColor",""+this.jButtonBorderTextColor.getBackground().getRGB() );

			prop.put("DefaultCompiler", ""+jComboBox1.getSelectedIndex());
                        prop.put("KeepJavaFile", this.jCheckBoxKeepJavaFile.isSelected()+"" );

                        prop.put("ReportVirtualizer",""+((Tag)this.jComboBoxVirtualizer.getSelectedItem()).getValue());
                        prop.put("ReportVirtualizerDirectory",""+this.jTextFieldVirtualizerDir.getText().trim());
                        prop.put("ReportVirtualizerSize",""+this.jSpinnerVirtualizerSize.getValue());
			prop.put("ReportVirtualizerBlockSize",""+this.jSpinnerVirtualizerBlockSize.getValue());
                        prop.put("ReportVirtualizerMinGrownCount",""+this.jSpinnerVirtualizerGrownCount.getValue());

                        prop.put("DontUseTemplateFromMenu", this.jCheckBoxDontUseTemplateFromMenu.isSelected()+"" );

                        //  TODO Fire Language changed event to make all the existing
			// forms/dialogs etc, reload their static strings.

                        if (!currentLAF.equals(prop.getProperty("LookAndFeel","")) ||
                            !currentOverrideDefaultFontName.equals(prop.getProperty("overrideDefaultFontName","")) ||
                            !currentOverrideDefaultFontAttrs.equals(prop.getProperty("overrideDefaultFontAttrs","")) ||
                            !currentOverrideDefaultFont.equals(prop.getProperty("overrideDefaultFont","")) ||
                            !currentOverrideDefaultFontSize.equals(prop.getProperty("overrideDefaultFontSize","")))
                        {
                            Misc.setPLAF( prop.getProperty("LookAndFeel","") );
                            SwingUtilities.updateComponentTreeUI(this);
                            MainFrame.getMainInstance().upadateLAF();
                            JInternalFrame activeframe = MainFrame.getMainInstance().getJMDIDesktopPane().getSelectedFrame();
                            if (activeframe != null && activeframe.isMaximum())
                            {
                                activeframe.setMaximum(false);
                                activeframe.setMaximum(true);
                            }
                        }

                        if (selectedLang != null)
                        {
        			I18n.setCurrentLocale( selectedLang );
                        }

                        //update frames...
                        JInternalFrame[] frames = mf.getJMDIDesktopPane().getAllFrames();

                        for (int i=0; i < frames.length; ++i)
                        {
                            JInternalFrame f = frames[i];
                            if (f instanceof JReportFrame)
                            {
                                JReportFrame jrf = (JReportFrame)f;
                                jrf.repaint();
                                jrf.repaintRules();
                            }
                        }

		} catch (Exception ex)
		{ ex.printStackTrace(); }

		this.jButtonApply.setEnabled(false);

	}

	protected void enableCompileDirectoryBrowse() {
		this.jTextFieldCompilationDir.setEnabled(true);
		this.jButtonCompilationDir.setEnabled(true);
	}

	protected void disableCompileDirectoryBrowse() {
		this.jTextFieldCompilationDir.setEnabled(false);
		this.jButtonCompilationDir.setEnabled(false);
	}

	protected void disableApplyAndOk() {
		this.jButtonApply.setEnabled(false);
		this.jButtonOK.setEnabled(false);
	}

	protected void disableApply() {
		this.jButtonOK.setEnabled(false);
	}

	protected void enableApplyAndOk() {
		this.jButtonApply.setEnabled(true);
		this.jButtonOK.setEnabled(true);
	}

	protected void enableApply() {
		this.jButtonOK.setEnabled(true);
	}

        public void languageChanged(LanguageChangedEvent evt) {
            this.applyI18n();
        }

        public void sheetPropertyValueChanged(SheetPropertyValueChangedEvent evt)
        {
            this.enableApplyAndOk();
        }

}//end class OptionsDialog
