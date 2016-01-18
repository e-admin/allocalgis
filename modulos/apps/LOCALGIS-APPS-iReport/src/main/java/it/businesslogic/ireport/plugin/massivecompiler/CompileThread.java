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
 * CompileThread.java
 * 
 * Created on 19 maggio 2004, 19.52
 *
 */

package it.businesslogic.ireport.plugin.massivecompiler;

import it.businesslogic.ireport.ReportClassLoader;
import it.businesslogic.ireport.gui.MainFrame;
import it.businesslogic.ireport.util.Misc;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.table.DefaultTableModel;

import net.sf.jasperreports.engine.JasperCompileManager;
/**
 *
 * @author  Administrator
 */
public class CompileThread implements Runnable {
    
    private MassiveCompilerFrame massiveCompilerFrame = null;
    private boolean stop = false;
    private Thread thread = null;
    ReportClassLoader reportClassLoader = null;
    
    
    private boolean compileSelectedOnly = false;
    
    public CompileThread(MassiveCompilerFrame mcf)
    {
        this.massiveCompilerFrame = mcf;
        thread = new Thread(this);
        MainFrame.reportClassLoader.rescanLibDirectory();
        Thread.currentThread().setContextClassLoader( MainFrame.reportClassLoader);
    }
    
    public void stop()
    {
        stop = true;
    }
    
    public void start()
    {
        thread.start();
    }
    
    public void run() {
        if (massiveCompilerFrame == null)
        {
            return;
        }
        
        DefaultTableModel dtm = (DefaultTableModel)massiveCompilerFrame.getFileTable().getModel();
        
        for (int i=0; i<dtm.getRowCount(); ++i)
        {
            FileEntry fe = (FileEntry)dtm.getValueAt(i, 0);
            if ( isCompileSelectedOnly() &&  !massiveCompilerFrame.getFileTable().isRowSelected(i))
            {
                continue;
            }
            
            // Start to compile this report...
            String srcFileName = "";
            String destFileName = "";
            
            
            
            
            try
            {
                
                fe.setStatus( fe.STATUS_COMPILING );
                dtm.setValueAt( fe, i, 0);
                try  {
                dtm.setValueAt( fe.getFile().getCanonicalPath(), i, 1);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                dtm.setValueAt( fe.decodeStatus( fe.getStatus()), i, 2);
            
                srcFileName = fe.getFile().getCanonicalPath();
                
                try {
                        reportClassLoader.setRelodablePaths( fe.getFile().getParent() );
                        Thread.currentThread().setContextClassLoader( reportClassLoader );
                    } catch (Exception ex) {}
                
                if (massiveCompilerFrame.isSelectedChangeFileExt())
                {
                    String srcFileName2 = it.businesslogic.ireport.util.Misc.changeFileExtension(srcFileName,"jrxml");
                    
                    if (!fe.getFile().renameTo( new File(srcFileName2 )))
                    {
                        fe.setFile( new File(srcFileName2 ) );
                        fe.setStatus( fe.STATUS_ERROR_COMPILING);
                        fe.setMessage("Unable to rename " + srcFileName + " to " + it.businesslogic.ireport.util.Misc.changeFileExtension(srcFileName,"jrxml")+" **" + fe);
                        
                        dtm.setValueAt( fe, i, 0);
                        dtm.setValueAt( fe.decodeStatus( fe.getStatus()), i, 2);
                        massiveCompilerFrame.getFileTable().updateUI();
                        continue;
                    }
                    else
                    {
                        fe.setFile( new File(srcFileName2 ) );
                        dtm.setValueAt( fe.decodeStatus( fe.getStatus()), i, 2);
                        System.out.println("File renamed as:" + fe.getFile().getCanonicalPath() );
                    }
                    srcFileName = srcFileName2;
                }
                
                destFileName = it.businesslogic.ireport.util.Misc.changeFileExtension(srcFileName,"jasper");
                
                if (massiveCompilerFrame.isSelectedOptionsCompileDir())
                {
                    String destDir = MainFrame.getMainInstance().getProperties().getProperty("DefaultCompilationDirectory",".");
                    if (!destDir.equals("") && !destDir.equals("."))
                    {
                        File f = new File(destFileName);
                        File f2 = new File(destDir, f.getName());
                        destFileName = f2+"";
                    }
                }
                
                if (massiveCompilerFrame.isSelectedBackup())
                {
                    File old_jasper = new File(destFileName);
                    if (old_jasper.exists())
                    {
                        old_jasper.renameTo(new File( it.businesslogic.ireport.util.Misc.changeFileExtension(destFileName,"jasper.bak")) );
                    }
                        
                }
                
                String compiler_name  = "JasperReports default compiler";
            	String compiler_code = it.businesslogic.ireport.gui.MainFrame.getMainInstance().getProperties().getProperty("DefaultCompiler");
		
                // Load the file to look for the compiler to use....
                boolean useGroovy = false;
                java.io.FileInputStream fis = null;
                try {
                    byte[] buffer = new byte[1024*5];  // we hope that the compiler is specified in the first 5k...
                    fis = new FileInputStream( srcFileName );
                    int reads = fis.read(buffer);
                    String s = new String(buffer,0,reads);
                    if (s.indexOf("language=\"groovy\"") >= 0)
                    {
                        useGroovy = true;
                    }
                } catch (Exception ex)
                {
                    
                } finally
                {
                    if (fis != null) 
                    {
                        try { fis.close(); } catch (Exception ex2) {}
                    }
                }
                
                 net.sf.jasperreports.engine.util.JRProperties.setProperty(net.sf.jasperreports.engine.util.JRProperties.COMPILER_CLASSPATH, Misc.nvl( new File(srcFileName).getParent(), ".")  + File.pathSeparator  + Misc.getClassPath()); 
                
                if (!useGroovy && compiler_code !=  null && !compiler_code.equals("0") && !compiler_code.equals(""))
		{
			if (compiler_code.equals("1"))
			{
			    System.setProperty("jasper.reports.compiler.class","net.sf.jasperreports.engine.design.JRJdk13Compiler"  );
                            net.sf.jasperreports.engine.util.JRProperties.setProperty(net.sf.jasperreports.engine.util.JRProperties.COMPILER_CLASS,"net.sf.jasperreports.engine.design.JRJdk13Compiler"  );
			    compiler_name = "Java Compiler";
			}
			else if (compiler_code.equals("2"))
			{
			    System.setProperty("jasper.reports.compiler.class","net.sf.jasperreports.engine.design.JRJdtCompiler"  );
                            net.sf.jasperreports.engine.util.JRProperties.setProperty(net.sf.jasperreports.engine.util.JRProperties.COMPILER_CLASS,"net.sf.jasperreports.engine.design.JRJdtCompiler"  );
			    compiler_name = "JDT Compiler";
			}
			else if (compiler_code.equals("3"))
			{
			    System.setProperty("jasper.reports.compiler.class","net.sf.jasperreports.engine.design.JRBshCompiler"  );
                            net.sf.jasperreports.engine.util.JRProperties.setProperty(net.sf.jasperreports.engine.util.JRProperties.COMPILER_CLASS,"net.sf.jasperreports.engine.design.JRBshCompiler"  );
			    compiler_name = "BeanShell Compiler";
			}
			else if (compiler_code.equals("4"))
			{
			    System.setProperty("jasper.reports.compiler.class","net.sf.jasperreports.engine.design.JRJikesCompiler"  );
                            net.sf.jasperreports.engine.util.JRProperties.setProperty(net.sf.jasperreports.engine.util.JRProperties.COMPILER_CLASS,"net.sf.jasperreports.engine.design.JRJikesCompiler"  );
			    compiler_name = "Jikes Compiler";
			}
		}
                else if (useGroovy)
                {
                    System.setProperty("jasper.reports.compiler.class","net.sf.jasperreports.compilers.JRGroovyCompiler" );
                    net.sf.jasperreports.engine.util.JRProperties.setProperty(net.sf.jasperreports.engine.util.JRProperties.COMPILER_CLASS,"net.sf.jasperreports.compilers.JRGroovyCompiler");
                }
		else
		{
			System.setProperty("jasper.reports.compiler.class","" );
                        net.sf.jasperreports.engine.util.JRProperties.setProperty(net.sf.jasperreports.engine.util.JRProperties.COMPILER_CLASS,"");
		}
                
                
                
                JasperCompileManager.compileReportToFile(srcFileName, destFileName);
                
                if (useGroovy) 
                {
                    fe.setStatus(fe.STATUS_COMPILED_GROOVY);
                }
                else 
                {
                    fe.setStatus( fe.STATUS_COMPILED );
                }
            } catch (Exception ex)
            {
                fe.setStatus( fe.STATUS_ERROR_COMPILING );
                
                StringWriter sw = new StringWriter();
                ex.printStackTrace( new PrintWriter( sw ));
                fe.setMessage(  sw.getBuffer().toString() );
            }
            
            dtm.setValueAt( fe, i, 0);
            try  {
            dtm.setValueAt( fe.getFile().getCanonicalPath(), i, 1);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            dtm.setValueAt( fe.decodeStatus( fe.getStatus()), i, 2);
        }
        
        massiveCompilerFrame.getFileTable().updateUI();
        massiveCompilerFrame.finishedCompiling();
        return;
    }
    
    /** Getter for property compileSelectedOnly.
     * @return Value of property compileSelectedOnly.
     *
     */
    public boolean isCompileSelectedOnly() {
        return compileSelectedOnly;
    }    
    
    /** Setter for property compileSelectedOnly.
     * @param compileSelectedOnly New value of property compileSelectedOnly.
     *
     */
    public void setCompileSelectedOnly(boolean compileSelectedOnly) {
        this.compileSelectedOnly = compileSelectedOnly;
    }
    
}
