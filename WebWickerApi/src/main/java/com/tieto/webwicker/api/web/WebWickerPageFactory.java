package com.tieto.webwicker.api.web;

import java.io.Serializable;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.tieto.webwicker.api.conf.Configuration;

import ro.fortsoft.pf4j.ExtensionPoint;

public abstract class WebWickerPageFactory implements ExtensionPoint, Serializable {
	private static final long serialVersionUID = -3035912325568545954L;

	public abstract WebWickerPage create(String id, PageParameters pageParameters, Configuration configuration);
	
	public abstract boolean isTopLevelPage();
	public abstract int getOrder();
	
	public abstract String getPageTitle();
	public abstract String getPageClassName();
}
