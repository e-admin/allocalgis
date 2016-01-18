package es.satec.localgismobile.fw.net.communications.connector;

public class Statistics {

	private static long bytesSent=0;

	private static long bytesReceived=0;

	
	public static void addBytesSent(long s) {
       bytesSent+=s;
	}

	public static void addBytesReceived(long r) {
		 bytesReceived+=r;
	}

	public static long getBytesSent() {
		return bytesSent;
	
	}

	public static long getBytesReceived() {
		return bytesReceived;
	
	}

	public static void resetBytes(){
		bytesSent=0;
		bytesReceived=0;
	}
}
