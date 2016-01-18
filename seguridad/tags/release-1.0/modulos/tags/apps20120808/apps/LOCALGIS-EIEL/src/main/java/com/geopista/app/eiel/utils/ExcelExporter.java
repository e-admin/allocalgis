package com.geopista.app.eiel.utils;

import java.awt.Component;
import java.awt.HeadlessException;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;

public class ExcelExporter {
	public ExcelExporter() {
	}

	public void exportTable(JTable table, Component panel) {

		try {
			JFileChooser fc = new JFileChooser();
			 FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel & CSV Files", "xls", "csv");
			 fc.setFileFilter(filter);

			int returnVal = fc.showSaveDialog(panel);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
			    File file = fc.getSelectedFile();
			    
			    String fileName = file.getName();
			    
			    if (fileName.lastIndexOf(".") == -1 ){
			    	fileName = fileName.concat(".csv");
			    	String path = file.getParent();
			    	if (path != null){
			    		path = path.concat(File.separator).concat(fileName);
			    		file = new File (path);
			    	}
			    }
			    
			    
			    saveFile(table.getModel(),file);
			} 
		} catch (HeadlessException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
       
        
	}
	public void exportTable(JTable table, File file) throws IOException {
		
		TableModel model = table.getModel();

		
	}
	
	private void saveFile(TableModel model, File file) throws IOException{
		FileWriter out = new FileWriter(file);
		
		for (int i = 0; i < model.getColumnCount(); i++) {
			out.write(model.getColumnName(i) + ";");
		}
		out.write("\n");

		for (int i = 0; i < model.getRowCount(); i++) {
			for (int j = 0; j < model.getColumnCount(); j++) {
				if (model.getValueAt(i, j)!=null)
					out.write(model.getValueAt(i, j).toString() + ";");
				else
					out.write("" + ";");
			}
			out.write("\n");
		}
		out.close();
		System.out.println("write out to: " + file);
	}
}