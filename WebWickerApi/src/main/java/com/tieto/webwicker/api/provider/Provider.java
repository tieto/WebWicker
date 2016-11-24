package com.tieto.webwicker.api.provider;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;

import ro.fortsoft.pf4j.ExtensionPoint;

public abstract class Provider<T> extends SortableDataProvider<T, String> implements ExtensionPoint {
	private static final long serialVersionUID = -6012527574433496972L;

	public abstract String getModelType();
	
	public abstract T getInstance(String id);
}
