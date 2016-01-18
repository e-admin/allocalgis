package es.uji.dsign.applet2.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import es.uji.dsign.applet2.SignatureApplet;
import es.uji.dsign.util.Base64;

public class XADESLocalFileOutputParams extends AbstractData implements OutputParams
{
	
	private Logger log = Logger.getLogger(XADESLocalFileOutputParams.class);
	
	String url= null; 
	boolean _initialized= false, signOkInvoked=false; 
	int _count= 1;
	SignatureApplet _base= null;
	String path;
	XADESLocalFileInputParams localFileInputParams;
	SimpleDateFormat simpleDateFormat;
		
	public XADESLocalFileOutputParams(XADESLocalFileInputParams localFileInputParams){
		this.localFileInputParams = localFileInputParams;
		simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
	}
	
	public void setSignData(byte[] data) throws IOException {
	    File signedFile = localFileInputParams.getSignedFile();
	    String signedFilePath = signedFile.getAbsolutePath();
	    String newFilePath = signedFilePath.substring(0, signedFilePath.lastIndexOf("."));
	    newFilePath += "-signed-" + simpleDateFormat.format(new Date())+".xsig"; 
	    log.debug("Guardamos el fichero como "+newFilePath);
	    //Los datos NO vienen codificados en base 64
	    FileOutputStream fileOutputStream = new FileOutputStream(newFilePath);
	    fileOutputStream.write(data);
	    fileOutputStream.flush();
	    fileOutputStream.close();

	    _count++;
	}
	
	public void setSignFormat(byte[] signFormat) throws IOException {
	}
	
	public void signOk() {
	}

	public void flush() {
		_count= 1;
	}
}
