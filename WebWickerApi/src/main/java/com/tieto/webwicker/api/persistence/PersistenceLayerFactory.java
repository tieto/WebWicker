package com.tieto.webwicker.api.persistence;

import ro.fortsoft.pf4j.ExtensionPoint;

import com.tieto.webwicker.api.conf.Configuration;

public abstract class PersistenceLayerFactory implements ExtensionPoint {
	public abstract PersistenceLayer create(Configuration configuration);
}
