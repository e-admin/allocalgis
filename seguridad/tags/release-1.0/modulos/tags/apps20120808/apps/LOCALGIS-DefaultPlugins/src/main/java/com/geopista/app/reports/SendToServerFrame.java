package com.geopista.app.reports;


import it.businesslogic.ireport.Report;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.geopista.app.AppContext;
import com.geopista.protocol.document.DocumentClient;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class SendToServerFrame extends javax.swing.JDialog{
	
	private int dialogResult;
	private String reportName;
	private String dir;
	private String subdir;
	private DocumentClient documentClient;
	private ArrayList files;
	
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel8;
	private javax.swing.JPanel jPanel9;
	private javax.swing.JPanel jPanel10;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JButton jButtonCANCEL;
    private javax.swing.JButton jButtonSAVE;
    private javax.swing.JTextField jTextFieldName;
    private javax.swing.JComboBox jComboBoxDir;
    private javax.swing.JComboBox jComboBoxSubdir;
    private javax.swing.JList jListFiles;  
    private DefaultListModel modelo = new DefaultListModel();
	
	public SendToServerFrame(String sUrl, java.awt.Frame parent, boolean modal){
        super(parent,modal);
        initComponents();
        
        this.setReportName(it.businesslogic.ireport.util.I18n.getString("untitledReport", "untitled_report_")+"1");
        
        this.jComboBoxDir.addItem("");
        
        documentClient = new DocumentClient(sUrl);
        ArrayList<String> dirs = null;
        
        try{
        	dirs = documentClient.getDirectories();
        }catch (Exception e){
    		e.printStackTrace();
    	}        
        
        for(String dir : dirs)
        	this.jComboBoxDir.addItem(dir);
  
	}
	
	public SendToServerFrame(String sUrl, javax.swing.JFrame parent, boolean modal){
        super(parent,modal);
        initComponents();
        
        this.setReportName(it.businesslogic.ireport.util.I18n.getString("untitledReport", "untitled_report_")+"1");
        
        this.jComboBoxDir.addItem("");
        
        documentClient = new DocumentClient(sUrl);
        ArrayList<String> dirs = null;
        
        try{
        	dirs = documentClient.getDirectories();
        }catch (Exception e){
    		e.printStackTrace();
    	}        
        
        for(String dir : dirs)
        	this.jComboBoxDir.addItem(dir);

	}
	
	public SendToServerFrame(String sUrl, javax.swing.JFrame parent, boolean modal, String restrictedPath, String restrictedSubPath){
        super(parent,modal);
        initComponents();
        
        this.setReportName(it.businesslogic.ireport.util.I18n.getString("untitledReport", "untitled_report_")+"1");
        
        this.jComboBoxDir.addItem("");
        
        documentClient = new DocumentClient(sUrl);
        ArrayList<String> dirs = null;
        
        try{
        	dirs = documentClient.getDirectories();
        }catch (Exception e){
    		e.printStackTrace();
    	}        
        
        for(String dir : dirs)
        	this.jComboBoxDir.addItem(dir);

       if(restrictedPath != null && !restrictedPath.equals("")) {
    	   this.jComboBoxDir.setSelectedItem(restrictedPath);	
    	   this.jComboBoxDir.setEnabled(false);
       }
       
       if(restrictedSubPath != null){
    	   this.jComboBoxSubdir.setSelectedItem(restrictedSubPath);	
    	   this.jComboBoxSubdir.setEnabled(false);
       } 
	}
	
	private void initComponents(){
		
		java.awt.GridBagConstraints gridBagConstraints;		

		jPanel1 = new javax.swing.JPanel();
		jPanel2 = new javax.swing.JPanel();
		jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButtonSAVE = new javax.swing.JButton();
        jButtonCANCEL = new javax.swing.JButton();
        jTextFieldName = new javax.swing.JTextField();
        jComboBoxDir = new javax.swing.JComboBox();
        jComboBoxSubdir = new javax.swing.JComboBox();
        jListFiles = new javax.swing.JList(modelo);
        
        getContentPane().setLayout(new java.awt.GridBagLayout());

        setTitle("Send to server");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("Send to server");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel8.setLayout(new java.awt.GridBagLayout());
        
        jButtonSAVE.setText("Save");
        jButtonSAVE.setMaximumSize(new java.awt.Dimension(200, 26));
        jButtonSAVE.setMinimumSize(new java.awt.Dimension(150, 26));
        jButtonSAVE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSAVEActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 5, 3);
        jPanel8.add(jButtonSAVE, gridBagConstraints);

        jButtonCANCEL.setText("Cancel");
        jButtonCANCEL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCANCELActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        jPanel8.add(jButtonCANCEL, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 3);
        getContentPane().add(jPanel8, gridBagConstraints);
        
        jPanel10.setLayout(new java.awt.GridBagLayout());
        jPanel1.setLayout(new java.awt.GridBagLayout());
        
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Path"));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Directory:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 5, 3);
        jPanel1.add(jLabel2, gridBagConstraints);

        jComboBoxDir.setMinimumSize(new java.awt.Dimension(300, 20));
        jComboBoxDir.setPreferredSize(new java.awt.Dimension(300, 20));
        jComboBoxDir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxTypeActionPerformed(evt);
            }
        });
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 5, 3);
        jPanel1.add(jComboBoxDir, gridBagConstraints);
        
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Subdirectory:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 5, 3);
        jPanel1.add(jLabel3, gridBagConstraints);
        
        jComboBoxSubdir.setMinimumSize(new java.awt.Dimension(300, 20));
        jComboBoxSubdir.setPreferredSize(new java.awt.Dimension(300, 20));
        jComboBoxSubdir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	jComboBoxSubtypeActionPerformed(evt);
            }
        });
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 5, 3);
        jPanel1.add(jComboBoxSubdir, gridBagConstraints);        
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 5, 3);
        jPanel10.add(jPanel1, gridBagConstraints);
        
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Report name:");
        jLabel1.setMaximumSize(new java.awt.Dimension(2000, 16));
        jLabel1.setMinimumSize(new java.awt.Dimension(150, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel9.add(jLabel1, gridBagConstraints);
        
        jPanel9.setLayout(new java.awt.GridBagLayout());

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Report name:");
        jLabel1.setMaximumSize(new java.awt.Dimension(2000, 16));
        jLabel1.setMinimumSize(new java.awt.Dimension(150, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel9.add(jLabel1, gridBagConstraints);
        
        jTextFieldName.setMinimumSize(new java.awt.Dimension(300, 20));
        jTextFieldName.setPreferredSize(new java.awt.Dimension(300, 20));
        jTextFieldName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldNameActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel9.add(jTextFieldName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 5, 3);
        jPanel10.add(jPanel9, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1000.0;
        getContentPane().add(jPanel10, gridBagConstraints);
        
        jPanel2.setLayout(new java.awt.GridBagLayout());
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Files"));
        jPanel2.setMinimumSize(new java.awt.Dimension(300, 300));
        jPanel2.setPreferredSize(new java.awt.Dimension(300, 300));
        
        jListFiles.setMinimumSize(new java.awt.Dimension(350, 270));
        jListFiles.setPreferredSize(new java.awt.Dimension(350, 270));
        jListFiles.setBackground(jPanel2.getBackground());        
        jListFiles.addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent e){
				int index = jListFiles.getSelectedIndex();
		        if(index != -1)
		        	jTextFieldName.setText((String)modelo.getElementAt(index));
			}        	
        });
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 7, 3);
        jPanel2.add(jListFiles, gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 7, 3);
        jPanel10.add(jPanel2, gridBagConstraints);
        
        pack();
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        java.awt.Dimension dialogSize = getSize();
        setLocation((screenSize.width-dialogSize.width)/2,(screenSize.height-dialogSize.height)/2);
	}
	
    private void exitForm(java.awt.event.WindowEvent evt) {
        this.setDialogResult(  JOptionPane.CLOSED_OPTION );
        this.dispose();
    }
    
    public void setDialogResult(int dialogResult) {
        this.dialogResult = dialogResult;
    }
    
    private void formWindowOpened(java.awt.event.WindowEvent evt) {
    }
    
    private void jButtonCANCELActionPerformed(java.awt.event.ActionEvent evt) {
        this.setDialogResult( JOptionPane.CANCEL_OPTION );
        this.dispose();
    }
    
    private void jButtonSAVEActionPerformed(java.awt.event.ActionEvent evt) {
        this.reportName = jTextFieldName.getText();
        this.dir = (String)jComboBoxDir.getSelectedItem();
        this.subdir = (String)jComboBoxSubdir.getSelectedItem();
        this.files = this.files;
        this.setDialogResult( JOptionPane.OK_OPTION );
        this.setVisible(false);
        this.dispose();
    }
    
    private void jTextFieldNameActionPerformed(java.awt.event.ActionEvent evt) {
        this.reportName = jTextFieldName.getText().trim();
    }
    
    private void jComboBoxTypeActionPerformed(java.awt.event.ActionEvent evt) {
        String dir = (String)this.jComboBoxDir.getSelectedItem();
        if(dir!=null)
        	subtypeComboBox(dir);
        else
        	this.jComboBoxSubdir.setSelectedIndex(0);
    }
    
    private void jComboBoxSubtypeActionPerformed(java.awt.event.ActionEvent evt) {
        String subdir = (String)this.jComboBoxSubdir.getSelectedItem();
        if(subdir!=null)
        	fileList(subdir);
    }
    
    private void subtypeComboBox(String dir){
    	this.jComboBoxSubdir.removeAllItems();
    	if(!dir.equals("")){
	    	ArrayList<String> subdirs = null; 
	        try{
	        	subdirs = documentClient.getSubdirectories(dir);
	        }catch (Exception e){
	    		e.printStackTrace();
	    	}       
	        this.jComboBoxSubdir.addItem("");
	        for(String subdir : subdirs)
	        	this.jComboBoxSubdir.addItem(subdir);       
    	}
    	else
    		this.modelo.clear();
    }
    
    private void fileList(String subdir){
    	this.modelo.clear();
    	ArrayList<String> files = null; 
    	if(!subdir.equals("")){	    	
	        try{
	        	files = documentClient.getFiles((String)this.jComboBoxDir.getSelectedItem()+ File.separator +subdir);
	        }catch (Exception e){
	    		e.printStackTrace();
	    	}       
	        for(String file : files)
	        	this.modelo.addElement(file); 
    	}else{
	        try{
	        	files = documentClient.getFiles((String)this.jComboBoxDir.getSelectedItem());
	        	
	        }catch (Exception e){
	    		e.printStackTrace();
	    	}       
	        for(String file : files)
	        	this.modelo.addElement(file);
    	}    
    	this.files = files;
    }

    public void setReportName(java.lang.String reportName) {
        this.reportName = reportName;
        this.jTextFieldName.setText(reportName);
        
    }
    
    public int getDialogResult() {
        return this.dialogResult;
    }
    
    public String getDir(){
    	return this.dir;
    }
    
    public String getSubdir(){
    	return this.subdir;
    }
    
    public String getReportName(){
    	return this.reportName;
    }

	public ArrayList getFiles() {
		return this.files;
	}
	
	public void executeSend(AppContext aplicacion, File file, ArrayList listImagenes) {
		send(aplicacion, file, listImagenes);
	}
	
	public void executeSend(AppContext aplicacion, Report report, ArrayList listImagenes) {
		report.setName(this.getReportName());
		File file = new java.io.File(report.getFilename());
		send(aplicacion, file, listImagenes);
	}
	
	private void send(AppContext aplicacion, File file, ArrayList listImagenes) {
		try {	
			if (this.getDialogResult() == javax.swing.JOptionPane.OK_OPTION){
				
				String dir = "";
				String subdir = "";
				dir = this.getDir();
				subdir = this.getSubdir();
				
				String path = "";		
				
				if(dir.equals("")) 
					ErrorDialog.show(aplicacion.getMainFrame(), "Error enviando plantilla", 
							"No se ha seleccionado el destino", 
						    "Debe seleccionar un directorio y/o un subdirectorio donde se guardará la plantilla");
				else if(subdir.equals("")) 
					path = dir ;
				else 
					path = dir + "/" + subdir;
				//El problema de utilizar File.separator es que luego si el cliente y el servidor son distintos 
				//Windows y Linux con el File.separator se hace un lio.
				
				ArrayList files = this.getFiles();
				if(files!=null && files.contains(file.getName())){
					int response = JOptionPane.showConfirmDialog(aplicacion.getMainFrame(),	
							"Se sobreescribirá la plantilla del servidor", 
			                "Sobreescribir plantilla?", JOptionPane.YES_NO_OPTION);
			        if (response != JOptionPane.YES_OPTION) 
			        	return;
				}
						    	
				DocumentClient.sendFile(file,path,file.getName());	
					
				
				if(listImagenes != null){		
					Iterator it=listImagenes.iterator();
					while (it.hasNext()){
						String imageExpression=(String)it.next();
						File imagen = new File(imageExpression.substring(1, imageExpression.length()-1));
						String nameImage = imageExpression.substring(imageExpression.lastIndexOf('\\') + 1, imageExpression.length()-1);						   
						
						DocumentClient.sendFile(imagen, "eiel"+"/"+"img",nameImage);
					}
				}
						
				JOptionPane.showMessageDialog(aplicacion.getMainFrame(),"Fichero enviado al servidor");
					     					
			}
		}catch (Exception e) {
			JOptionPane.showMessageDialog(aplicacion.getMainFrame(),"Error al enviar el informe al servidor");
			e.printStackTrace();
		}
	}
}
