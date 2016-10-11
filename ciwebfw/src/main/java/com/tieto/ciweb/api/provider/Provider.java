package com.tieto.ciweb.api.provider;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;

public abstract class Provider<T> extends SortableDataProvider<T, String> {
	private static final long serialVersionUID = -6012527574433496972L;

	public abstract String getModelType();
	
	public abstract T getInstance(String id);
}
