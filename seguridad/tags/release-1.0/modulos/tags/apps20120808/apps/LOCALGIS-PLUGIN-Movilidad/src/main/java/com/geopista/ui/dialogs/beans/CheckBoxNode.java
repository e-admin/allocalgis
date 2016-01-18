package com.geopista.ui.dialogs.beans;

public class CheckBoxNode {
	  private String text;

	  private boolean selected;

	private boolean enabled;

	public CheckBoxNode(String text, boolean selected, boolean enabled) {
	    this.text = text;
	    this.selected = selected;
	    this.enabled = enabled;
	  }

	  public boolean isSelected() {
	    return selected;
	  }

	  public void setSelected(boolean newValue) {
	    selected = newValue;
	  }

	  public String getText() {
	    return text;
	  }

	  public void setText(String newValue) {
	    text = newValue;
	  }

	  public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	  public String toString() {
	    return getClass().getName() + "[" + text + "/" + selected + "]";
	  }
	}
