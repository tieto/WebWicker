package com.tieto.ciweb.api.source;

import com.tieto.ciweb.api.persistence.PersistenceLayer;

public interface Source extends Runnable {
	void init(PersistenceLayer persistenceLayer);
}
