/**
 * PrintJobThread.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * 
 */
package com.localgis.app.gestionciudad.dialogs.interventions.utils;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.DecimalFormat;

import javax.swing.JProgressBar;

/**
 * @author javieraragon
 *
 */
public class PrintJobThread implements Runnable {
	private PrinterJob job;
	private boolean finished = false;
	private JProgressBar pgBar = null;
	private int initValue = 0;
	private int maxValue = 0;

	public boolean isFinisjhed(){
		return finished ;
	}
	
	public PrintJobThread (PrinterJob c, JProgressBar pgBar, int actualValue, int maxValue) {
		job = c;
		this.pgBar = pgBar;
		this.initValue = actualValue;
		this.maxValue  = maxValue;
	}

	public void run() {
		try {
//			job.print();
			Thread.sleep(1000);
			
			
			pgBar.setValue(initValue);
			pgBar.setString(
					initValue + 
					" de " +
					getPrecentageProgressBar()
			);
			
			pgBar.repaint();
//			pgBar.setText(initValue + " de " + maxValue);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} finally{
			this.finished = true;	
		}

	}
	
	private String getPrecentageProgressBar(){
		String resultado = "";
		if (this.pgBar != null){
			try{
				resultado = "(" +  (new DecimalFormat("0.0")).format(pgBar.getPercentComplete()*100) + "%)";
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return resultado;
	}
	
}
