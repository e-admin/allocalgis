package es.satec.localgismobile.core;

import java.util.Hashtable;

public class ApplicationOption {

	private Application application;
	private String messageKey;
	private String screenClass;
	private Hashtable params;

	public ApplicationOption(Application application) {
		this(application, null, null, new Hashtable());
	}
	
	public ApplicationOption(Application application, String messageKey, String screenClass, Hashtable params) {
		this.application = application;
		this.messageKey = messageKey;
		this.screenClass = screenClass;
		this.params = params;
	}

	public Application getApplication() {
		return application;
	}
	
	public String getMessageKey() {
		return messageKey;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

	public String getScreenClass() {
		return screenClass;
	}

	public void setScreenClass(String screenClass) {
		this.screenClass = screenClass;
	}
	
	public Hashtable getParams() {
		return params;
	}
	
	public void setParams(Hashtable params) {
		this.params = params;
	}
}
