package com.tinyline.svg;

public class SVGChangeEvent {

	public static final int CHANGE_TYPE_NEW = 1;
	public static final int CHANGE_TYPE_MODIFIED = 1<<1;
	public static final int CHANGE_TYPE_DELETED = 1<<2;
	public static final int CHANGE_TYPE_METADATA_MODIFIED = 1<<3;
	
	private int changeType;
	private long timestamp;
	
	public SVGChangeEvent() {
		changeType = 0;
		timestamp = 0;
	}
	
	public SVGChangeEvent(int changeType, long timestamp) {
		this.changeType = changeType;
		this.timestamp = timestamp;
	}

	public long getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	public int getChangeType() {
		return changeType;
	}
	
	public void setChangeType(int changeType) {
		this.changeType = changeType;
	}
	
	public String toString()  {
		return "Change type=" + changeType + ", timestamp=" + timestamp;
	}
}
