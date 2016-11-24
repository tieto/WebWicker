package com.tieto.webwicker.api.source;

import ro.fortsoft.pf4j.ExtensionPoint;

import com.tieto.webwicker.api.conf.Configuration;

public abstract class SourceFactory implements ExtensionPoint {
	public abstract Source create(Configuration configuration);
}
