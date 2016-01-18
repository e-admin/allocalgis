package es.uji.dsign.applet2.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import es.uji.dsign.applet2.SignatureApplet;
import es.uji.dsign.applet2.Exceptions.SignatureAppletException;
import es.uji.dsign.util.OS;

public class RAWLocalFileInputParams extends AbstractData implements InputParams
{
	boolean initialized= false; 
	int count= 0, current=0;
	String path; 
	int timeout= 10000;
	SignatureApplet _base;
	JFileChooser fileChooser;
	
	public RAWLocalFileInputParams(SignatureApplet sa) {
	    _base = sa;
		count= 1;
		initialized= true;
        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileFilter() {
            public boolean accept(File f) {
                return true;
            }
            
            public String getDescription() {
                return "*.*";
            }
        });
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
	}
	
	public int getInputCount() throws Exception {
		if ( ! initialized )
			throw new IOException("Uninitialized Input method");
	
		return count;	
	}
	
	public byte[] getSignData() throws Exception {
	    int result = fileChooser.showOpenDialog(_base);
	    if (result != JFileChooser.APPROVE_OPTION) {
	        System.err.println("No se ha seleccionado ningun archivo a firmar");
	        throw new SignatureAppletException("No se ha seleccionado ningun archivo a firmar");
	    }
	        
        FileInputStream fileInputStream = new FileInputStream(fileChooser.getSelectedFile());

	    current++;
		
		byte[] signData = OS.inputStreamToByteArray(fileInputStream);
		fileInputStream.close();
		
		return signData;
	}
	
	public byte[] getSignData(int item) throws Exception {
		if ( ! initialized )
			throw new IOException("Uninitialized Input method");
		
		if ( item >= count )
			throw new IOException("Item count length exceeded");
		
		System.out.println("INPUTS: " + path);
		
        FileInputStream fileInputStream = new FileInputStream(path);

        byte[] signData;
		if (mustHash) {
		    signData = this.getMessageDigest(OS.inputStreamToByteArray(fileInputStream));
		} else {
		    signData = OS.inputStreamToByteArray(fileInputStream);
		}
		fileInputStream.close();
		
		return signData; 
	}


	public String getSignFormat(SignatureApplet base) {
		return (base.getParameter("signFormat") != null) ? base.getParameter("signFormat") : "es.uji.dsign.crypto.CMSSignatureFactory";
	}
	
	public void flush() {
		current= 0;
	}
	
	public File getSignedFile() {
	    return fileChooser.getSelectedFile();
	}
}