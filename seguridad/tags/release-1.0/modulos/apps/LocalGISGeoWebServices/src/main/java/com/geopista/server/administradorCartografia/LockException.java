package com.geopista.server.administradorCartografia;


public class LockException extends Exception{
	
	FeatureLockResult featureLockResult = null;
	
    public LockException(String s){
        super(s);
    }
    
    public LockException(String s, FeatureLockResult featureLockResult ){
        super(s);
        this.featureLockResult = featureLockResult;
    }
    
    public FeatureLockResult getFeatureLockResult(){
    	return featureLockResult;
    }
}
