/**
 * FeatureLockResult.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.administradorCartografia;

import java.io.Serializable;

public class FeatureLockResult implements Serializable {
	private int lockResultCode;
	private String layer;
	private int featureId;
	
	private Integer userId;
	private Integer lockOwnerUserId;
	private Integer layerOwnerUserId;
	
	private String lockOwnerUserName;
	private String layerOwnerUserName;
	
	public FeatureLockResult(int lockResultCode){
		this.lockResultCode = lockResultCode;
		layer = null;
		featureId = -1;
	}
	
	public FeatureLockResult(int lockResultCode, String layer, int featureId){
		this.lockResultCode = lockResultCode;
		this.layer = layer;
		this.featureId = featureId;
	}
	
	public int getLockResultCode() {
		return lockResultCode;
	}
	public String getLayer() {
		return layer;
	}
	public int getFeatureId() {
		return featureId;
	}
	public Integer getUserId() {
		return userId;
	}
	public Integer getLockOwnerUserId() {
		return lockOwnerUserId;
	}
	public Integer getLayerOwnerUserId() {
		return layerOwnerUserId;
	}
	public String getLockOwnerUserName() {
		return lockOwnerUserName;
	}
	public String getLayerOwnerUserName() {
		return layerOwnerUserName;
	}

	public void setLockResultCode(int lockResultCode) {
		this.lockResultCode = lockResultCode;
	}

	public void setLayer(String layer) {
		this.layer = layer;
	}

	public void setFeatureId(int featureId) {
		this.featureId = featureId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public void setLockOwnerUserId(Integer lockOwnerUserId) {
		this.lockOwnerUserId = lockOwnerUserId;
	}

	public void setLayerOwnerUserId(Integer layerOwnerUserId) {
		this.layerOwnerUserId = layerOwnerUserId;
	}

	public void setLockOwnerUserName(String lockOwnerUserName) {
		this.lockOwnerUserName = lockOwnerUserName;
	}

	public void setLayerOwnerUserName(String layerOwnerUserName) {
		this.layerOwnerUserName = layerOwnerUserName;
	}
}
