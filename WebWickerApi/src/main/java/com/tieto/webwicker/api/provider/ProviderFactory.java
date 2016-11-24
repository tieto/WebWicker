package com.tieto.webwicker.api.provider;

import ro.fortsoft.pf4j.ExtensionPoint;

import com.tieto.webwicker.api.conf.Configuration;
import com.tieto.webwicker.api.model.Model;

public abstract class ProviderFactory<T extends Model> implements ExtensionPoint {
	public abstract Provider<T> create(Configuration configuration);
	
	public abstract Class<? extends Model> getModelClass();
}
