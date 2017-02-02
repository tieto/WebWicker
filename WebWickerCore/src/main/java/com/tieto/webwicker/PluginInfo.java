package com.tieto.webwicker;

import java.io.Serializable;

public class PluginInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name;
	private String version;
	private String status;

	public PluginInfo(String name, String version, String status) {

		this.name = name;
		this.version = version;
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}