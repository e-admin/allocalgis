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
