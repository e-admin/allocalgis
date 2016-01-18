/**
 * SVGChangeEvent.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
