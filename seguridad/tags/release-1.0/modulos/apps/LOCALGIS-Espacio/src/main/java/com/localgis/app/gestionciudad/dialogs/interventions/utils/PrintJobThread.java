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
