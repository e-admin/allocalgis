/**
 * ExcelExporter.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.utils;

import java.awt.Component;
import java.awt.HeadlessException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
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