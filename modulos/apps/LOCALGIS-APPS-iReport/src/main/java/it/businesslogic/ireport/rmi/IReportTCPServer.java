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
 * IReportTCPServer.java
 * 
 * Created on August 17, 2006, 3:00 PM
 *
 */

package it.businesslogic.ireport.rmi;

import it.businesslogic.ireport.Report;
import it.businesslogic.ireport.gui.JReportFrame;
import it.businesslogic.ireport.gui.MainFrame;
import it.businesslogic.ireport.gui.WizardDialog;
import it.businesslogic.ireport.util.PageSize;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.SwingUtilities;

/**
 *
 * @author gtoffoli
 */
public class IReportTCPServer implements Runnable {
    
  static IReportTCPServer mainInstance = null;
  
  public static IReportTCPServer getMainInstance()
  {
      if (mainInstance == null)
      {
          try {
                mainInstance = new IReportTCPServer();
          } catch (Exception ex)
          {
              ex.printStackTrace();
          }
      }
      
      return mainInstance;
  }
  
    /** Creates a new instance of IReportTCPServer */
    public IReportTCPServer() {
    }
    
    
    public static void runServer()
    {             
      if (MainFrame.getMainInstance().getProperties().getProperty( "enableRMIServer" ,"false").equals("true"))
      {
        Thread t = new Thread( IReportTCPServer.getMainInstance() );
        t.start();
        //SwingUtilities.invokeLater( IReportServerImpl.getMainInstance() );
      }
    }
    
    public void run() {

        ServerSocket serverSocket = null;
        DataInputStream is;
        PrintStream os;

        Socket clientSocket = null;

        int port = 2100;
        try {
            port = Integer.parseInt( MainFrame.getMainInstance().getProperties().getProperty( "RMIServerPort" ,"2100"));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        
        try {

            serverSocket = new ServerSocket( port );
            MainFrame.getMainInstance().logOnConsole("Demone listening on port: " + serverSocket.getLocalPort() );

        } catch (IOException e) {

               MainFrame.getMainInstance().logOnConsole("Error opening the socket : " + e.getMessage());
	       return;
        }

        // Non ci interessa gestire connessioni multiple...
        while ( true )
        {
            is = null;
            os = null;
            clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
                is = new DataInputStream(clientSocket.getInputStream());

                final String line = is.readLine();
                os = new PrintStream(clientSocket.getOutputStream());

                if (line == null)
                {
                    os.write( new String("-Unknow command!").getBytes() );
                    os.close();
                    clientSocket.close();
                } else {
                    
                     os.write( new String("+OK Give me five!").getBytes() );
                     os.close();
                     clientSocket.close();
                     
                    try {
                        
                        SwingUtilities.invokeAndWait( new Runnable()
                        {
                            public void run()
                            {
                        
                        
                    try {
                           if (line.toUpperCase().startsWith("PING"))
                           {
                               
                           }
                           else if (line.toUpperCase().startsWith("WIZARD "))
                           {
                               runWizard(line.substring(7));
                           }
                           else if (line.toUpperCase().startsWith("OPENFILE "))
                           {
                               openFile(line.substring(9));
                           }
                           else if (line.toUpperCase().startsWith("SETVISIBLE "))
                           {
                               setVisible( Boolean.valueOf(line.substring(11).trim()).booleanValue() );
                           }
                           else
                           {
                               throw new Exception("Unknow command: " + line);
                           }
                           //os.write( new String("+OK Give me five!").getBytes() );
                           
                    } catch (Throwable tr)
                    {
                           tr.printStackTrace();
                           //os.write( new String("-Error " + tr).getBytes() );
                    }
                            }
                    });
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    } catch (InvocationTargetException ex) {
                        ex.printStackTrace();
                    }
                    
                }
            } catch (IOException e) {
	    }
        }


    }
    
    
    // ---------------------------------------------------------------------------------
    
     public boolean runWizard(String destFile)
  {
  	MainFrame mainFrame = MainFrame.getMainInstance();
  	
  	if (mainFrame == null) return false;
  	
        
        
	mainFrame.logOnConsole("Invocato wizard");
	mainFrame.logOnConsole("Pronto ad invocare la nuova finestra..." + Thread.currentThread().getName());
	
        
        try {
                // TODO
                // Set the project directory as current directory;

                WizardDialog wd = new WizardDialog(mainFrame,true);

                mainFrame.logOnConsole("Lancio wizard");
                wd.setVisible(true);
                wd.requestFocus();


                Report report = null;
                if (wd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
                    report = wd.getReport();
                    if (report == null)
                    {
                        report = createBlankReport();
                    }
                }
                else
                {
                    report = createBlankReport();
                }

                if (report != null)
                {
                        mainFrame.openNewReportWindow(report);
                        report.setFilename(destFile);
                        report.saveXMLFile();
                        //setVisible(true);
                }

        } catch (Exception ex)
        {
              System.out.println(ex.getMessage());
                ex.printStackTrace();
        }
        

      return true;
  }
  
  private Report createBlankReport()
  {
      Report newReport = new Report();
      
        newReport.setName(it.businesslogic.ireport.util.I18n.getString("untitledReport", "untitled_report_")+"1");
        newReport.setUsingMultiLineExpressions(false); //this.isUsingMultiLineExpressions());
        newReport.setWidth(  PageSize.A4.x);
        newReport.setHeight( PageSize.A4.y);
        newReport.setTopMargin(20);
        newReport.setLeftMargin(30);
        newReport.setRightMargin(30);
        newReport.setBottomMargin(20);
        newReport.setColumnCount(1);
        newReport.setColumnWidth( newReport.getWidth() - newReport.getLeftMargin() - newReport.getRightMargin() );
        newReport.setColumnSpacing(0);
        
        return newReport;
  }
  
    /**
   * Used to check if iReport is alive
   */
  public boolean ping()
  {
      return true;
  }
  
  /**
   * Used to show the main window and bring the iReport window on top...
   */
  public boolean setVisible(boolean b)
  {
      MainFrame.getMainInstance().setVisible(b);
      if (MainFrame.getMainInstance().getState() == java.awt.Frame.ICONIFIED)
      {
            MainFrame.getMainInstance().setState( java.awt.Frame.NORMAL );
      }
      return MainFrame.getMainInstance().requestFocusInWindow();
  }
  
  /**
   * Open the file passed as parameter...
   */
  public boolean openFile(String file)
  {
      setVisible(true);
      try {
          JReportFrame jrf = MainFrame.getMainInstance().openFile( file );
          jrf.setSelected(true);
          return true;
      } catch (Exception ex){
           return false;
      }
  }
}
