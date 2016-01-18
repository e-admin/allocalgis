/*
 * Copyright (C) 2005-2007 JasperSoft http://www.jaspersoft.com
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
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
 * DesignVerifyerThread.java
 *
 * Created on February 27, 2007, 1:56 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package it.businesslogic.ireport;

import it.businesslogic.ireport.gui.JReportFrame;
import it.businesslogic.ireport.gui.MainFrame;
import it.businesslogic.ireport.gui.event.ReportDocumentStatusChangedEvent;
import it.businesslogic.ireport.gui.event.ReportDocumentStatusChangedListener;
import it.businesslogic.ireport.gui.logpane.ProblemItem;
import it.businesslogic.ireport.compiler.xml.SourceLocation;
import it.businesslogic.ireport.compiler.xml.SourceTraceDigester;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.SwingUtilities;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.design.JRValidationFault;
import net.sf.jasperreports.engine.design.JasperDesign;

/**
 *
 * @author gtoffoli
 */
public class DesignVerifyerThread implements Runnable, ReportDocumentStatusChangedListener {
    
    private boolean reportChanged = true;
    private Thread thisThread = null;
    private boolean stop = false;
    
    private JReportFrame jReportFrame = null;
    /** Creates a new instance of DesignVerifyerThread */
    public DesignVerifyerThread(JReportFrame jReportFrame) {
        this.jReportFrame = jReportFrame;
    
        this.jReportFrame.getReport().addReportDocumentStatusChangedListener(this);
        
        thisThread = new Thread( this );
    }
    
    public void start()
    {
        thisThread.start();
    }
    
    public void stop()
    {
        setStop(true);
    }

    public void run() {
        
        while (!isStop())
        {
            try {
                Thread.sleep(2000);
            } catch (Exception ex)
            {}
        
            if (MainFrame.getMainInstance().getProperties().getProperty("RealTimeValidation","true").equals("true") &&
                getJReportFrame() != null && isReportChanged() &&
                MainFrame.getMainInstance().getActiveReportFrame() == this.getJReportFrame() )    
            {
                setReportChanged(false);
                verifyDesign();
            }
        }
    }

    public JReportFrame getJReportFrame() {
        return jReportFrame;
    }

    public void setJReportFrame(JReportFrame jReportFrame) {
        this.jReportFrame = jReportFrame;
    }

    public boolean isReportChanged() {
        return reportChanged;
    }

    public void setReportChanged(boolean reportChanged) {
        this.reportChanged = reportChanged;
    }

    public void reportDocumentStatusChanged(ReportDocumentStatusChangedEvent evt) {
        setReportChanged(true);
    }

    public boolean isStop() {
        return stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }
    
    
    public void verifyDesign()
    {
        // Remove all the WARNINGS...
       for (int i=0; i<getJReportFrame().getReportProblems().size(); ++i)
       {
           ProblemItem pii = (ProblemItem)getJReportFrame().getReportProblems().get(i);
           if (pii.getProblemType() == ProblemItem.WARNING)
           {
               getJReportFrame().getReportProblems().remove(i);
               i--;
           }
       }
       //getJReportFrame().getReportProblems().clear();
       
       try {
            SourceTraceDigester digester = IReportCompiler.createDigester();
            ReportWriter rw = new ReportWriter(getJReportFrame().getReport());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            rw.writeToOutputStream(baos);
            JasperDesign jd = IReportCompiler.loadJasperDesign( new ByteArrayInputStream( baos.toByteArray() ), digester);
            
            Collection ls = JasperCompileManager.verifyDesign(jd);
            Iterator iterator = ls.iterator();
            while (iterator.hasNext())
            {
                JRValidationFault fault = (JRValidationFault)iterator.next();
                String s = fault.getMessage();
                SourceLocation sl = digester.getLocation( fault.getSource() );
                getJReportFrame().getReportProblems().add( new ProblemItem(ProblemItem.WARNING, s, fault.getSource(), sl.getXPath()));
            }
       
       } catch (Exception ex)
        {
            getJReportFrame().getReportProblems().add(new ProblemItem(ProblemItem.WARNING, ex.getMessage(), null, null) );
        }
       
       Runnable runner = new Runnable(){
                public void run()
                {
                    MainFrame.getMainInstance().getLogPane().getProblemsPanel().setProblems( getJReportFrame().getReportProblems() );
                }
        };

        SwingUtilities.invokeLater(runner);
    }
}
